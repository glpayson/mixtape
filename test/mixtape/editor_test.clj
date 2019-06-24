(ns mixtape.editor-test
  (:require [midje.sweet :refer [fact facts contains]]
            [mixtape.editor :as target]))

(def empty-mixtape {:users     []
                    :playlists []
                    :songs     []})

(def mixtape {:users     [{:id "0" :name "u0"}
                          {:id "1" :name "u1"}]
              :playlists [{:id "0" :user-id "0" :song-ids ["0"]}
                          {:id "1" :user-id "1" :song-ids ["1"]}]
              :songs     [{:id "0" :artist "a0" :title "t0"}
                          {:id "1" :artist "a1" :title "t1"}]})

(def edits {:add-songs        [{:artist "a2"
                                :title  "t2"}
                               {:artist "a3"
                                :title  "t3"}]
            :add-playlists    [{:user-id  "1"
                                :song-ids ["0" "1"]}
                               {:user-id  "0"
                                :song-ids ["1" "0"]}]
            :remove-playlists ["0" "1" "42"]})

(def empty-edits {:add-songs        []
                  :add-playlists    []
                  :remove-playlists []})

(facts "Edits are correctly applied to mixtape."
  (let [actual (target/edit-mixtape mixtape edits)]
    (fact "Add song edits are correctly applied."
      (count (:songs actual)) => 4
      (nth (:songs actual) 0) => {:artist "a0" :id "0" :title "t0"}
      (nth (:songs actual) 1) => {:artist "a1" :id "1" :title "t1"}
      (nth (:songs actual) 2) => {:artist "a2" :id "2" :title "t2"}
      (nth (:songs actual) 3) => {:artist "a3" :id "3" :title "t3"})
    (fact "Add playlist edits are correctly applied."
      (count (:playlists actual)) => 2
      (nth (:playlists actual) 0) => {:id "2" :user-id "1" :song-ids ["0" "1"]}
      (nth (:playlists actual) 1) => {:id "3" :user-id "0" :song-ids ["1" "0"]})
    (fact "Remove playlist edits are correctly applied."
      (some #(= "0" (:id %)) (:playlists actual)) => nil
      (some #(= "1" (:id %)) (:playlists actual)) => nil)
    (fact "Users are not altered."
      (count (:users actual)) => 2
      (nth (:users actual) 0) => {:id "0" :name "u0"}
      (nth (:users actual) 1) => {:id "1" :name "u1"})))

(fact "Edits are correctly applied to empty mixtape"
  (let [expected {:playlists [{:id "2" :song-ids ["1" "0"] :user-id "0"}]
                  :songs     [{:artist "a2" :id "1" :title "t2"}
                              {:artist "a3" :id "2" :title "t3"}]}]
    (target/edit-mixtape empty-mixtape edits) => (assoc expected :users [])
    (target/edit-mixtape {} edits) => expected
    (target/edit-mixtape nil edits) => expected))

(fact "Empty edits are correctly applied to mixtape."
  (target/edit-mixtape mixtape empty-edits) => mixtape
  (target/edit-mixtape mixtape {}) => mixtape
  (target/edit-mixtape mixtape nil) => mixtape)

(fact "Empty edits are correctly applied to empty mixtape."
  (let [expected {:songs [], :playlists []}]
    (target/edit-mixtape empty-mixtape empty-edits) => (assoc expected :users [])
    (target/edit-mixtape {} {}) => expected
    (target/edit-mixtape nil nil) => expected))