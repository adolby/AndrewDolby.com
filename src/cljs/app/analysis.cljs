(ns app.analysis
  "GitHub releases URL analysis"
  (:require
    [taoensso.timbre :as timbre :refer-macros [info]]))

(defn get-os
  [url]
  (cond
    (boolean (re-find #"windows" url)) "Windows"
    (boolean (re-find #"linux" url)) "Linux"
    (boolean (re-find #".dmg" url)) "Mac OS X"
    :else "Other"))

(defn get-word-size
  [url]
  (cond
    (boolean (re-find #"x86_64" url)) "64-bit"
    (boolean (re-find #"x64" url)) "64-bit"
    (boolean (re-find #"x86" url)) "32-bit"
    :else ""))

(defn get-file-type
  [url]
  (cond
    (boolean (re-find #".zip" url)) "Portable"
    (boolean (re-find #".tar.gz" url)) "Portable"
    (boolean (re-find #".dmg" url)) "Disk Image"
    :else "Installer"))

(defn analyze-download-url
  [asset-info]
  (let [{url :browser-download-url} asset-info]
    {:os (get-os url) :word-size (get-word-size url)
     :file-type (get-file-type url) :url url}))

(defn build-download-map
  [asset-info-vector]
  (group-by :os (map analyze-download-url asset-info-vector)))
