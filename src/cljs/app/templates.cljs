(ns app.templates
  "Application templating implemented with kioo and Reagent"
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [kioo.reagent :as kioo])
  (:require-macros [kioo.reagent :refer [defsnippet deftemplate]]))

(def icon-files
  {"Windows" "images/windows.svg"
   "Linux" "images/linux.svg"
   "macOS" "images/apple.svg"
   "Other" ""
   "Installer" "images/monitor.svg"
   "Portable" "images/archive.svg"
   "Disk Image" "images/disc.svg"})

(def themes ["default" "green" "red" "blue"])

(defsnippet kryvos-download-item
  "templates/download.html"
  [:.download-item]
  [{url :url word-size :word-size file-type :file-type}]
  {[:a] (kioo/do-> (kioo/set-class "align vertical button-outline")
                   (kioo/set-attr :href url)
                   (kioo/set-attr :download "")
                   (kioo/content [:img {:src (get icon-files
                                                  file-type)
                                        :alt (str "Download "
                                                  word-size
                                                  file-type)}]
                                 [:span
                                   (if (str/blank? word-size)
                                     file-type
                                     (str file-type
                                          " / "
                                          word-size))]))})

(defsnippet kryvos-download
  "templates/download.html"
  [:.download]
  [category files]
  {[:span] (kioo/content [:h3 {:class "inline-heading"} category]
                         [:img {:class "os-icon"
                                :src (get icon-files category)
                                :alt category}])
   [:ul] (kioo/do->
           (kioo/set-class "align horizontal link-list")
           (kioo/content (map kryvos-download-item files)))})

(deftemplate kryvos-downloads
  "templates/download.html"
  []
  {[:.downloads]
   (kioo/content
     (let [downloads (subscribe [:downloads])]
       (for [[k v] @downloads]
         ^{:key k}
         (kryvos-download k v))))})

(defsnippet theme-bar
  "templates/theme-bar.html"
  [:.link-item]
  [theme]
  {[:a] (kioo/do->
          (kioo/content [:img {:src (str "images/" theme ".svg")
                               :alt (str theme " Theme")}])
          (kioo/listen :on-click #(dispatch [:update-theme theme])))})

(deftemplate page
  "index.html"
  []
  {[:#background]
   (kioo/set-class (let [theme (subscribe [:theme])]
                     (if (str/blank? @theme)
                       "background-image default"
                       (str "background-image " @theme))))
   [:.kryvos-downloads] (kioo/content (kryvos-downloads))
   [:footer :ul] (kioo/content (map theme-bar themes))})

(defn render
  []
  (reagent/render-component [page] (.-body js/document)))
