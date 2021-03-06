(ns app.events
  "Application re-frame events"
  (:require
    [clojure.string :as str]
    [clojure.spec.alpha :as spec]
    [ajax.core :as ajax]
    [camel-snake-kebab.core :refer [->kebab-case-keyword]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [re-frame.core
      :refer [reg-event-db reg-event-fx path trim-v after
              dispatch]]
    [day8.re-frame.http-fx]
    [taoensso.timbre :as timbre :refer-macros [info]]
    [app.db
      :refer [default-value theme->local-store local-store->theme]]
    [app.analysis :as analysis :refer [build-download-map]]))

;; Interceptors

(defn check-and-throw
  "Throw an exception if db doesn't match the spec"
  [a-spec db]
  (when-not (spec/valid? a-spec db)
    (throw (ex-info "spec check failed: "
             {:problems (spec/explain-str a-spec db)}))))

(def check-spec-interceptor
  (after (partial check-and-throw :app.db/db)))

(def ->local-store (after theme->local-store))

;; Handlers

(reg-event-db
  :initialize-db
  check-spec-interceptor
  (fn [_ _]
    (merge default-value {:theme (local-store->theme)})))

;; Get list of downloads from GitHub releases, then update in DB
(reg-event-fx
  :load-download-urls
  (fn [_ [_ url]]
    {:http-xhrio {:method :get
                  :uri url
                  :timeout 8000
                  :response-format (ajax/json-response-format
                                     {:keywords? true})
                  :on-success [:result-success]
                  :on-failure [:result-fail]}}))

;; On successfully receiving GitHub release info, proceed to analysis
(reg-event-db
  :result-success
  (fn [db [_ result]]
    (let [{asset-data :assets} result
          download-map (as-> asset-data v
                         (transform-keys ->kebab-case-keyword v)
                         (analysis/build-download-map v))]
      (assoc db :downloads download-map))))

;; On failing to receive GitHub release info, show user the download
;; link for releases on GitHub
(reg-event-db
  :result-fail
  (fn [db [_ _]]
    db))

;; Update current theme in the DB and also update it in the LocalStorage
(reg-event-db
  :update-theme
  [check-spec-interceptor (path :theme) ->local-store trim-v]
  (fn [_ [new-theme]]
    new-theme))
