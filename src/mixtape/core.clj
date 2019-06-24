(ns mixtape.core
  (:require [mixtape.io.reader :as reader]
            [mixtape.editor :as editor]
            [mixtape.io.writer :as writer]))

(def ^:private usage "USAGE: lein run -m mixtape.core mixtape.json changes.json output-file.json")

(defn -main
  [& args]
  (if-not (= 3 (count args))
    (println usage)
    (let [[mixtape-file edits-file output-file] args
          mixtape (reader/read-mixtape mixtape-file)
          edits (reader/read-edits edits-file)]
      (-> (editor/edit-mixtape mixtape edits)
          (writer/write-mixtape output-file)))))
