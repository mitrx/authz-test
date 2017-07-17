(ns authz-test.db.roles-organizations)

(def roles-organizations [{:user-id 1
                           :organization-id 1
                           :role "admin"}
                          {:user-id 2
                           :organization-id 1
                           :role "manager"}
                          {:user-id 3
                           :organization-id 1
                           :role "worker"}
                          {:user-id 4
                           :organization-id 1
                           :role "user"}])
