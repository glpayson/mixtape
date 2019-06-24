(ns mixtape.io.reader
  (:require [cheshire.core :as ch]
            [clojure.edn :as edn]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]))

(defn read-mixtape [mixtape-file]
  (as-> (slurp mixtape-file) %
        (ch/parse-string % true)
        (cske/transform-keys csk/->kebab-case-keyword %)))

(defn read-edits [changes-file]
  (-> (slurp changes-file)
      (edn/read-string)))


