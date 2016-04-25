(ns cardgame.endpoint.example
  (:require [compojure.core :refer :all]
            [ring.util.response :refer :all]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :refer [select from where]]))

(defn example-endpoint [{{db :spec} :db}]
  (context "/example" []
    (GET "/" []
      {:status  200
       :headers {"Content-Type" "text/plain"}
       :body    "Hello World"})

    (GET "/cards" []
      (let [query (-> (select :id :title)
                    (from :cards))
            cards (jdbc/query db (sql/format query))
            res (map #(str "<li><a href=\"/example/cards/" (:id %) "\">" (:title %) "</a></li>") cards)]
        (-> (response (str "<h1>Cards</h1>" "<ul>" (clojure.string/join " " res) "</ul>"))
            (content-type "text/html"))))


    (GET "/cards.json" []
      (let [query (-> (select :id :title)
                      (from :cards))
            cards (jdbc/query db (sql/format query))]
        cards))


    (GET "/cards/:qid" [qid]
      (let [id (Integer/parseInt qid)
            query (-> (select :title)
                      (from :cards)
                      (where [:= :id id]))
            res (jdbc/query db (sql/format query))]
        (if-let [card (first res)]
          (-> (response (str "<h1> CCC " (:title card) "</h1>"))
              (content-type "text/html"))
          "Not found")))
    ))
