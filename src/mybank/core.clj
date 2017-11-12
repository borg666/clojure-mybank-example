(ns mybank.core
  (:use compojure.core
        ring.middleware.json
        ring.util.response)
  (:require [compojure.route :as route]
            [mybank.view :as view]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            )
  )

(defn makeHttpRequest
  [url input]
  (:body
    (client/get url
      {:query-params {"email" input}})))

(defn extractJson
  [responseBody]
  (get (json/read-str responseBody) "email"))

(defn getIt [url input]
  (extractJson
    (makeHttpRequest url input)))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (getIt "http://localhost:3000/rest" "haha")})

(defn findUser [request]
  {:status 200
   :query-params {"name" "inputName"}
   :headers {"Content-Type" "text/html"}
   :body (response {:name1 name})})

(defroutes my_routes
  (GET "/" [] (view/index-page) )
  (GET "/user" {{:keys [user-id]} :session}
    (str "The current user is " user-id))
  (GET "/view-request" request (str :query-string) )
  (GET "/rest" [] (response {:email "sven@malvik.de"}))
  (GET "/ip" [] (handler "hey"))
  (GET "/test" [par] (response {:par1 par, :par2 "hey"}))
  (GET "/find/:name" [name] (response {:username name}))
  (GET "/foo" request (interpose ", " (keys request)))
  (GET "/hello/:name" [name] (str "Hello " name))
  (GET "/user/:id" [id]
    (str "<h1>Hello user " id "</h1>"))
  (route/resources "/"))



(def app (wrap-json-response my_routes))