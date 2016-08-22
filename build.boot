(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"resources"}
 :dependencies
 '[[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.36"]
   [cljs-ajax "0.5.8"]
   [camel-snake-kebab "0.4.0"]
   [reagent "0.6.0-rc"]
   [re-frame "0.8.0"]
   [day8.re-frame/http-fx "0.0.4"]
   [kioo "0.4.2" :exclusions [cljsjs/react]]
   [com.taoensso/timbre "4.7.0"]

   [adzerk/boot-cljs "1.7.228-1" :scope "test"]
   [adzerk/boot-reload "0.4.12" :scope "test"]
   [adzerk/boot-test "1.1.2" :scope "test"]
   [pandeiro/boot-http "0.7.3" :scope "test"]])

(require
 '[adzerk.boot-cljs :refer [cljs]]
 '[adzerk.boot-reload :refer [reload]]
 '[adzerk.boot-test :refer [test]]
 '[pandeiro.boot-http :refer [serve]])

(deftask dev []
  (comp (serve)
        (watch)
        (speak)
        (reload)
        (cljs :optimizations :none :source-map true)
        (target)))

(deftask prod []
  (comp (cljs :optimizations :advanced)
        (target)))
