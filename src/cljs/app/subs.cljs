(ns app.subs
  "Application re-frame subscriptions"
  (:require [re-frame.core :refer [reg-sub]]))

;; Subscription for showing current theme
(reg-sub
  :theme
  (fn [db _]
    (:theme db)))

;; Subscription for showing download urls
(reg-sub
  :downloads
  (fn [db _]
    (:downloads db)))
