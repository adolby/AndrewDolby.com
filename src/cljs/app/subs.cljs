(ns app.subs
  "Application re-frame subscriptions"
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [reg-sub subscribe]]))

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
