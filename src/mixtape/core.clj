(ns mixtape.core
  (:require [mixtape.io.reader :as reader]
            [mixtape.editor :as editor]
            [mixtape.io.writer :as writer]))

(def ^:private usage "USAGE: lein run -m mixtape.core mixtape.json changes.edn output-file.json")

(defn -main
  "Takes a mixtape json file and an edn file containing lists of changes to apply to the mixtape.
  The edited mixtape is written, in json format, to an output file.
  USAGE: lein run -m mixtape.core mixtape.json changes.edn output-file.json"
  [& args]
  (if-not (= 3 (count args))
    (println usage)
    (let [[mixtape-file edits-file output-file] args
          mixtape (reader/read-mixtape mixtape-file)
          edits (reader/read-edits edits-file)]
      (-> (editor/edit-mixtape mixtape edits)
          (writer/write-mixtape output-file)))))
