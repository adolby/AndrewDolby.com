(ns app.views
  "Application views implemented Reagent"
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]))

(def icon-files
  {"Android" "images/android.svg"
   "Windows" "images/windows.svg"
   "Linux" "images/linux.svg"
   "macOS" "images/apple.svg"
   "Installer" "images/monitor.svg"
   "Portable" "images/archive.svg"
   "Disk Image" "images/disc.svg"
   "Disk Image Archive" "images/disc.svg"
   "APK" "images/archive.svg"
   "Other" ""})

(def themes ["default" "green" "red" "blue"])

(defn kryvo-download-item
  [{url :url word-size :word-size file-type :file-type}]
  [:li
    {:key url :class "download-item link-item"}
    [:a
      {:class "align vertical button-outline"
       :href url
       :download ""}
      [:img {:src (get icon-files file-type)
             :alt (str "Download " word-size " " file-type)}]
      [:span (if (str/blank? word-size)
               file-type
               (str file-type " / " word-size))]]])

(defn kryvo-download
  [category files]
  [:div.download {:key category}
    [:span {:class "align vertical"}
      [:h3.inline-heading category]
      [:img.os-icon
        {:src (get icon-files category)
         :alt category}]]
    [:ul {:class "align horizontal link-list"}
      (map kryvo-download-item files)]])

(defn kryvo-downloads
  []
  (let [downloads (subscribe [:downloads])]
    (fn []
      [:div.downloads
        (for [[k v] @downloads]
          (kryvo-download k v))])))

(defn theme-item
  [theme]
  [:li.link-item
    {:key theme}
    [:a
      [:img {:src (str "images/" theme ".svg")
             :alt (str theme " Theme")
             :on-click #(dispatch [:update-theme theme])}]]])

(defn theme-bar
  []
  (let [theme (subscribe [:theme])]
    (fn []
      (do
        ;; Alter theme outside of Reagent components
        (aset
          (.getElementById js/document "background")
          "className"
          (if
            (str/blank? @theme)
            "background-image default"
            (str "background-image " @theme)))
        [:ul.link-list
          (map theme-item themes)]))))

(defn render-kryvo-download
  []
  (reagent/render-component
    [kryvo-downloads]
    (.getElementById js/document "kryvo-downloads")))

(defn render-footer
  []
  (reagent/render-component
    [theme-bar]
    (.getElementById js/document "footer")))
