(ns authz-test.db.issues)

(def issues [{:issue-id 1
              :title "My title"
              :author-id 1
              :responsible-id 3
              :organization-id 1
              :lat 1.073
              :lng 2.002}
             {:issue-id 2
              :title "Second title"
              :author-id 4
              :organization-id 2
              :responsible-id 3
              :lat 8.088
              :lng 7.072}])

(defn get-issue [issue-id]
  (first (filter #(= (:issue-id %) issue-id) issues)))
