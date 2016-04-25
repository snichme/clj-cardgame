(ns cardgame.endpoint.appi
  (:require [compojure.core :refer :all]))

(defn appi-endpoint [config]
  (routes
   (GET "/" [] "Hello World")))
