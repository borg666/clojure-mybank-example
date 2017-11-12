(ns mybank.core
  (:use compojure.core
        ring.middleware.json
        ring.util.response)
  (:require
            [compojure.route :as route]
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
  
  (GET "/math" {params :query-params}  []
;    (get params :a)
;    (get params :b)
    (println {params :query-params})
    (str (get params :a) (get params :b)))

  (GET "/aaa" params (str params))
  (GET "/hey" request (str {request :query-string}))

  (GET "/query" request (str (request :query-string)))

  (GET "/view-request" request (str request))

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