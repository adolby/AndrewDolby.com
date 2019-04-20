(ns app.core
  "Application core"
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [app.views :as views]
            [app.events :as events]
            [app.subs :as subs]))

(defn ^:export main
  "Application entry point"
  []
  (dispatch-sync [:initialize-db])
  (let [downloads-url
        "https://api.github.com/repos/adolby/Kryvo/releases/latest"]
    (dispatch [:load-download-urls downloads-url]))
  (views/render-kryvo-download)
  (views/render-footer))
