(ns mixtape.core-test
  (:require [midje.sweet :refer [fact]]
            [mixtape.core :as target]))

(defn strip-ws [coll]
  (apply str (remove #(#{\space \tab \newline} %) coll)))

(fact "Edits are correctly applied to mixtape"
  (with-redefs [spit (fn [_ edited-mixtape] edited-mixtape)]

    (let [actual (target/-main "test/resources/sample-mixtape.json"
                               "test/resources/sample-edits.edn"
                               "output.json")
          expected (slurp "test/resources/expected-output.txt")]
      (= (strip-ws expected) (strip-ws actual)) => true)))
