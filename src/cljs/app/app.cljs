(ns app.app
  (:require [cljs.core.async :as async :refer [<!]]
            [reagent.core :as reagent]
            [kioo.reagent :as kioo]
            [cljs-http.client :as http]
            [alandipert.storage-atom :refer [local-storage]])
  (:require-macros [kioo.reagent :refer [defsnippet deftemplate]]
                   [cljs.core.async.macros :refer [go]]))

(def style (local-storage (atom {}) :style))

(def data (reagent/atom {}))

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

(defn analyze-download-url [asset-info]
  (let [{url :browser_download_url} asset-info]
    (hash-map :os (get-os url), :word-size (get-word-size url),
      :file-type (get-file-type url), :url url)))

(defn build-download-list [asset-info-list]
  (group-by :os (map analyze-download-url asset-info-list)))

; Download JSON data
(defn download-json [json-url]
  (go
    (let [{{asset-info-list :assets} :body} (<! (http/get json-url {:with-credentials? false}))]
      (reset! data (build-download-list asset-info-list)))))

; Templating
(def icon-files
  {"Windows" "https://gitcdn.xyz/repo/adolby/AndrewDolby.com/master/resources/images/windows.svg",
   "Linux" "https://gitcdn.xyz/repo/adolby/AndrewDolby.com/master/resources/images/linux.svg",
   "Mac OS X" "https://gitcdn.xyz/repo/adolby/AndrewDolby.com/master/resources/images/apple.svg",
   "Other" "",
   "Installer" "https://gitcdn.xyz/repo/adolby/AndrewDolby.com/master/resources/images/monitor.svg",
   "Portable" "https://gitcdn.xyz/repo/adolby/AndrewDolby.com/master/resources/images/archive.svg",
   "Disk Image" "https://gitcdn.xyz/repo/adolby/AndrewDolby.com/master/resources/images/disc.svg"})

(defsnippet kryvos-download-item "templates/download.html" [:.download-item]
  [{url :url word-size :word-size file-type :file-type}]
  {[:a] (kioo/do-> (kioo/set-class "align vertical button-outline")
                   (kioo/set-attr :href url)
                   (kioo/set-attr :download "")
                   (kioo/content [:img {:src (get icon-files file-type)}]
                                 [:span
                                   (if (clojure.string/blank? word-size)
                                     file-type
                                     (str file-type " / " word-size))]))})

(defsnippet kryvos-download "templates/download.html" [:.download] [category files]
  {[:span] (kioo/content [:h3 {:class "inline-heading"} category]
                         [:img {:class "os-icon", :src (get icon-files category)}])

   [:ul] (kioo/do->
           (kioo/set-class "align horizontal link-list")
           (kioo/content (map kryvos-download-item files)))})

(deftemplate kryvos-downloads "templates/download.html" []
  {[:.downloads] (kioo/content (for [[k v] @data] ;^{:key (name (gensym k))}
                                 (kryvos-download k v)))})

(defsnippet theme-bar "templates/theme-bar.html" [:.link-list] []
  {[:a] (kioo/set-attr :onclick #())})

(deftemplate page "index.html" []
  {[:.kryvos-downloads] (kioo/content (kryvos-downloads))
   [:footer] (kioo/content (theme-bar))})

(defn init []
  (let [json-url "http://api.github.com/repos/adolby/Kryvos/releases/latest"]
    (download-json json-url)
    (reagent/render-component [page] (.-body js/document))))
