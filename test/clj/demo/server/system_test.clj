(ns demo.server.system-test
  (:require [aleph.http :as http]
            [com.stuartsierra.component :as component]
            [clojure.test :refer [deftest testing is]]
            [manifold.stream :as s]
            [manifold.deferred :as d]
            [demo.server.system :as system]
            [taoensso.timbre :as log]))

(def config {:id "test" :port 10000})

(defmacro with-system
  [& body]
  `(let [~'system (component/start-system (system/system config))]
     (try
       ~@body
       (finally (component/stop-system ~'system)))))

(defmacro unpack-http-response
  [response & body]
  `(let [~'status (:status ~response)]
     ~@body))

(deftest root
  (with-system
    (let [requests
          {:keys [system]}]
      (unpack-http-response @(http/get "http://localhost:10000/")
                            (is (= 200 status))))))
