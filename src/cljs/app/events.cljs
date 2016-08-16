(ns app.events
  "Application re-frame events"
  (:require
    [clojure.string :as str]
    [cljs.spec :as s]
    [cljs.core.async :refer [<!]]
    [cljs-http.client :as http]
    [re-frame.core :refer [reg-event-db path trim-v after debug
                           dispatch]]
    [taoensso.timbre :as timbre :refer-macros [info]]
    [app.db
      :refer [default-value theme->local-store local-store->theme]]
    [app.analysis :as analysis :refer [build-download-map]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn check-and-throw
  "Throw an exception if db doesn't match the spec"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info "spec check failed: "
             {:problems (s/explain-str a-spec db)}))))

(def check-spec-interceptor
  (after (partial check-and-throw :app.db/db)))

(def ->local-store (after theme->local-store))

(reg-event-db
  :initialize-db
  check-spec-interceptor
  (fn [_ _]
    (merge default-value {:theme (local-store->theme)})))

;; Get list of downloads from GitHub releases, then update in DB
(reg-event-db
  :load-download-urls
  check-spec-interceptor
  (fn [db [_ url]]
    (go
      (let [{{asset-info-vector :assets} :body}
            (<! (http/get url {:with-credentials? false}))
            download-map
            (analysis/build-download-map asset-info-vector)]
        (dispatch [:update-downloads download-map])))
    db))

;; Update downloads in the DB
(reg-event-db
  :update-downloads
  [check-spec-interceptor (path :downloads) trim-v]
  (fn [_ [new-downloads]]
    new-downloads))

;; Update current theme in the DB and also update it in the LS
(reg-event-db
  :update-theme
  [check-spec-interceptor (path :theme) ->local-store trim-v]
  (fn [_ [new-theme]]
    new-theme))
