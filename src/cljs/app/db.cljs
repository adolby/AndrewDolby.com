(ns app.db
  "Application re-frame database"
  (:require [clojure.string :as str]
            [cljs.reader]
            [clojure.spec.alpha :as spec]
            [taoensso.timbre :as timbre :refer-macros [info]]))

(spec/def ::theme string?)
(spec/def ::db (spec/keys :req-un [::theme ::downloads]))

(def default-value
  {:theme "default"
   :downloads {}})

(defn local-store->theme
  "Read in last theme from LocalStorage, and process into a map that can be
   merged into app-db"
  []
  (let [theme (.getItem js/localStorage "theme")]
    (if (str/blank? theme)
      "default"
      theme)))

(defn theme->local-store
  "Put current theme into LocalStorage"
  [theme]
  (.setItem js/localStorage "theme" theme))
