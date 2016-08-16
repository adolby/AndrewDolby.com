(ns app.core
  "Application core"
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [app.templates :as templates]
            [app.events :as events]
            [app.subs :as subs]))

(def downloads-url
     "https://api.github.com/repos/adolby/Kryvos/releases/latest")

(defn ^:export main
  []
  (dispatch-sync [:initialize-db])
  (dispatch [:load-download-urls downloads-url])
  (templates/render))
