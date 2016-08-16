(ns app.db
  "Application re-frame database"
  (:require [clojure.string :as str]
            [cljs.reader]
            [cljs.spec :as s]
            [taoensso.timbre :as timbre :refer-macros [info]]))

(s/def ::theme string?)
(s/def ::db (s/keys :req-un [::theme ::downloads]))

(def default-value
  {:theme "default"
   :downloads {}})

(defn local-store->theme
  "Read in last theme from LS, and process into a map we can merge
   into app-db."
  []
  (let [theme (.getItem js/localStorage "theme")]
    (if (str/blank? theme)
      "default"
      theme)))

(defn theme->local-store
  "Puts current theme into LS"
  [theme]
  (.setItem js/localStorage "theme" theme))
