(ns mixtape.io.writer
  (:require [cheshire.core :as ch]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]))

(defn write-mixtape [output out-file]
  (as-> output %
        (cske/transform-keys csk/->snake_case_keyword %)
        (ch/generate-string % {:pretty true})
        (spit out-file %)))
