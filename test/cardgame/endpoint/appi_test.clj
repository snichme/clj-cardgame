(ns cardgame.endpoint.appi-test
  (:require [clojure.test :refer :all]
            [cardgame.endpoint.appi :as appi]))

(def handler
  (appi/appi-endpoint {}))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
