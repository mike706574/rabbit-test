(ns demo.server.handler
  (:require [com.stuartsierra.component :as component]))

(defprotocol HandlerFactory
  "Builds a request handler."
  (handler [this]))

(defrecord DemoHandlerFactory [conn-manager]
  HandlerFactory
  (handler [this]
    (fn [request]
      {:status 200})))

(defn factory
  []
  (component/using (map->DemoHandlerFactory {}) [:conn-manager]))
