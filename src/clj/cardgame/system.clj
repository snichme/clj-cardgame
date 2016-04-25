(ns cardgame.system
  (:require [com.stuartsierra.component :as component]
            [duct.component.endpoint :refer [endpoint-component]]
            [duct.component.handler :refer [handler-component]]
            [duct.component.hikaricp :refer [hikaricp]]
            [duct.component.ragtime :refer [ragtime]]
            [duct.middleware.not-found :refer [wrap-not-found]]
            [ring.middleware.json :refer [wrap-json-response]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [cardgame.endpoint.example :refer [example-endpoint]]))

(def base-config
  {:app {:middleware [[wrap-json-response]
                      [wrap-not-found :not-found]
                      [wrap-defaults :defaults]]
         :not-found  "Resource Not Found"
         :defaults   (meta-merge api-defaults {:static {:resources "cardgame/public"}})}
   :ragtime {:resource-path "cardgame/migrations"}})

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :db   (hikaricp (:db config))
         :ragtime (ragtime (:ragtime config))
         :example (endpoint-component example-endpoint))
        (component/system-using
         {:http [:app]
          :app  [:example]
          :ragtime [:db]
          :example [:db]}))))
