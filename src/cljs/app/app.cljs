(ns app.app
  (:require [cljs.core.async :as async :refer [<!]]
            [reagent.core :as reagent]
            [kioo.reagent :as kioo]
            [cljs-http.client :as http])
  (:require-macros [kioo.reagent :refer [defsnippet deftemplate]]
                   [cljs.core.async.macros :refer [go]]))

; Global state
(defonce data (reagent/atom {}))

; URL analysis
(defn get-os [url]
  (cond
    (boolean (re-find #"windows" url)) "Windows"
    (boolean (re-find #"linux" url)) "Linux"
    (boolean (re-find #".dmg" url)) "Mac OS X"
    :else "Other"))

(defn get-word-size [url]
  (cond
    (boolean (re-find #"x86_64" url)) "64-bit"
    (boolean (re-find #"x64" url)) "64-bit"
    (boolean (re-find #"x86" url)) "32-bit"
    :else ""))

(defn get-file-type [url]
  (cond
    (boolean (re-find #".zip" url)) "Portable"
    (boolean (re-find #".tar.gz" url)) "Portable"
    (boolean (re-find #".dmg" url)) "Disk Image"
    :else "Installer"))

(defn analyze-url [url]
  (hash-map :os (get-os url), :word-size (get-word-size url),
    :file-type (get-file-type url), :url url))

; Download JSON data
(defn get-json [url]
  (go
    (let [{downloads :body} (<! (http/get url))]
      (reset! data (group-by :os (map analyze-url downloads))))))

; Templating
(def icon-files
  {"Windows" "images/windows.svg",
   "Linux" "images/linux.svg",
   "Mac OS X" "images/apple.svg",
   "Other" "",
   "Installer" "images/monitor.svg",
   "Portable" "images/archive.svg",
   "Disk Image" "images/disc.svg"})

(defsnippet kryvos-download-item "index.html" [:.kryvos-download-item]
  [{url :url word-size :word-size file-type :file-type}]
  {[:a] (kioo/do-> (kioo/set-class "button-outline")
                   (kioo/set-attr :href url)
                   (kioo/set-attr :download "")
                   (kioo/content [:img {:src (get icon-files file-type)}]
                                 [:span
                                   (if (clojure.string/blank? word-size)
                                     file-type
                                     (str file-type " / " word-size))]))})

(defsnippet kryvos-download "index.html" [:.kryvos-download] [category files]
  {[:h3]  (kioo/do->
            (kioo/set-class "inline-header")
            (kioo/content category))

   [:img] (kioo/do->
           (kioo/set-class "os-icon")
           (kioo/set-attr :src (get icon-files category)))

   [:ul] (kioo/do->
           (kioo/set-class "link-list increase-margins")
           (kioo/content (map kryvos-download-item files)))})

(deftemplate page "index.html" []
  {[:.kryvos-downloads] (kioo/content (for [[k v] @data] ;^{:key (name (gensym k))}
                                        (kryvos-download k v)))})

(defn init []
  (let [json-url "downloads.json"]
    (get-json json-url)
    (reagent/render-component [page] (.-body js/document))))
