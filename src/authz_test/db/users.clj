(ns authz-test.db.users)

(def users [{:user-id 1
             :email "admin@urbest.io"
             :full-name "Mr. Admin"
             :password "secret"}
            {:user-id 2
             :email "manager@urbest.io"
             :full-name "Mr. Manager"
             :password "secret"}
            {:user-id 3
             :email "worker@urbest.io"
             :full-name "Mr. Worker"
             :password "secret"}
            {:user-id 4
             :email "user@urbest.io"
             :full-name "Mr. Worker"
             :password "secret"}])
