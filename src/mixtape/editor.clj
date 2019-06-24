(ns mixtape.editor)

(defn- sort-maps [maps]
  (mapv #(into (sorted-map) %) maps))

(defn- sort-mixtape [mixtape]
  (reduce-kv #(assoc %1 %2 (sort-maps %3)) {} mixtape))

(defn- not-in? [col id]
  (not (some #(= id %) col)))

(defn- delete-entities
  "Delete all elements of `entity-key` in `mixtape` whose ID's are in `ids-to-delete`.
  Ex:
    (delete-entities {:songs [{:id \"1\" :artist \"a0\" :title \"t0\"}]}
                     [\"1\"]
                     :songs)
    ; => {:songs [{:id \"2\", :artist \"a1\", :title \"t1\"}]}"
  [mixtape ids-to-delete entity-key]
  (->> (entity-key mixtape)
       (filterv #(not-in? ids-to-delete (:id %)))
       (assoc mixtape entity-key)))

(defn- last-id
  "Get maximum id integer from entities keyed by `entity-key` in `mixtape`."
  [mixtape entity-key]
  (if (empty? (entity-key mixtape))
    0
    (->> (entity-key mixtape)
         (map :id)
         (map #(Integer/valueOf %))
         (apply max))))

(defn- add-entities
  "Add all `entities-to-add` to `entity-key` in `mixtape`.
  ID's for new entities are incremented from the largest currently existing ID.
  Ex:
    (add-entities {:songs [{:id \"1\" :artist \"a0\" :title \"t0\"}]}
                  [{:artist \"a1\" :title \"t1\"}]
                  :songs)
    ; => {:songs [{:id \"1\" :artist \"a0\" :title \"t0\"}
                  {:id \"2\" :artist \"a1\" :title \"t1\"}]}"
  [mixtape entities-to-add entity-key]
  (let [id (inc (last-id mixtape entity-key))]
    (->> entities-to-add
         (map-indexed #(assoc %2 :id (str (+ id %1))))
         (update mixtape entity-key #(vec (concat %1 %2))))))

(defn edit-mixtape
  "Apply edits to mixtape and return result."
  [mixtape edits]
  (-> mixtape
      (add-entities (:add-songs edits) :songs)
      (add-entities (:add-playlists edits) :playlists)
      (delete-entities (:remove-playlists edits) :playlists)
      sort-mixtape))
