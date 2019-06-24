(ns mixtape.io.reader
  (:require [cheshire.core :as ch]
            [clojure.edn :as edn]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]))

(defn read-mixtape
  "Reads (json) mixtape file and coerces its keys to kebab-cased keywords."
  [mixtape-file]
  (as-> (slurp mixtape-file) %
        (ch/parse-string % true)
        (cske/transform-keys csk/->kebab-case-keyword %)))

(defn read-edits
  "Reads edn changes file."
  [changes-file]
  (-> (slurp changes-file)
      (edn/read-string)))


