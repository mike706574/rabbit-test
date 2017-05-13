(ns demo.server.system
  (:require [demo.server.connection :as conn]
            [demo.server.handler :as handler]
            [demo.server.service :as service]
            [com.stuartsierra.component :as component]
            [manifold.bus :as bus]
            [taoensso.timbre :as log]))

(def queue-name "foo")

(defn send-message
  [message]
  (def conn (.newConnection (doto (com.rabbitmq.client.ConnectionFactory.)
                              (.setHost "localhost"))))
  (def chan (.createChannel conn))
  (.queueDeclare chan queue-name false false false nil)
  (.basicPublish chan "" queue-name nil (.getBytes message))
  (.close chan)
  (.close conn))

(defrecord RabbitConsumer [id conn chan]
  component/Lifecycle
  (start [this]
    (log/debug (str "Starting consumer " id "."))
    (let [conn (.newConnection (doto (com.rabbitmq.client.ConnectionFactory.)
                                   (.setHost "localhost")))
          chan (.createChannel conn)
          consumer (proxy [com.rabbitmq.client.DefaultConsumer] [chan]
                     (handleDelivery [consumer-tag envelope props body]
                       (log/debug (str "[" id "] Received message: " (String. body "UTF-8")))))]
      (.basicConsume chan queue-name true consumer)
      (assoc this :conn conn :chan chan)))
  (stop [this]
    (log/debug (str "Stopping consmer " id "."))
    (.close chan)
    (.close conn)
    (dissoc this :conn :chan)))

(defn rabbit-consumer [id]
  (map->RabbitConsumer {:id id}))

(defn system
  [config]
  (log/info "Building system.")
  {:connections (atom {})
   :consumer-a (rabbit-consumer "A")
   :consumer-b (rabbit-consumer "B")
   :conn-manager (conn/manager)
   :handler-factory (handler/factory)
   :app (service/aleph-service config)})
