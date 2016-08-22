(ns app.events
  "Application re-frame events"
  (:require
    [clojure.string :as str]
    [cljs.spec :as s]
    [ajax.core :as ajax]
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
  (when-not (s/valid? a-spec db)
    (throw (ex-info "spec check failed: "
             {:problems (s/explain-str a-spec db)}))))

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
  (fn [{:keys [db]} [_ url]]
    {:http-xhrio {:method :get
                  :uri url
                  :timeout 8000
                  :response-format (ajax/json-response-format
                                     {:keywords? true})
                  :on-success [:good-url-result]
                  :on-failure [:bad-url-result]}}))

;; On successfully receiving GitHub release info, proceed to analysis
(reg-event-db
  :good-url-result
  (fn [db [_ result]]
    (let [{asset-data :assets} result
          download-map (analysis/build-download-map asset-data)]
      (assoc db :downloads download-map))))

;; On failing to receive GitHub release info, show user the download
;; link on for releases on GitHub
(reg-event-db
  :bad-url-result
  (fn [db [_ _]]
    db))

;; Update current theme in the DB and also update it in the LS
(reg-event-db
  :update-theme
  [check-spec-interceptor (path :theme) ->local-store trim-v]
  (fn [_ [new-theme]]
    new-theme))
