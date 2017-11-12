(ns mybank.core
  (:use compojure.core
        ring.middleware.json
        ring.util.response)
  (:require
           [compojure.route :as route]
            [compojure.handler :as handler]
            [mybank.view :as view]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            )
  )









;(defn fetchUrl (:body (client/get "https://www.indeed.co.uk/python-jobs-in-London")))


(defn findUser [request]
  {:status 200
   :query-string {"name" "inputName"}
   :headers {"Content-Type" "text/html"}
   :body (response {:name1 name})})

(defn makeHttpRequest [url] (:body (client/get url)))

(defroutes my_routes
  (GET "/" [] (view/index-page))
  (GET "/indeed" [] (makeHttpRequest "https://www.indeed.co.uk/jobs?q=ios&l=London"))
  (GET "/wolo" {params :params} (str params))
  (GET "/ram*" {params :query-params} (str params))
  (GET "/foobar" [x y :as r] (str x ", " y ", " r))
  (GET "/user1" user-id {{:keys [user-id]} :query-string} (str "The current user is " {{:keys [user-id]} :query-string}))
  (GET "/math" {params :query-params} []
        (get params :a)
        (get params :b)
    (println {params :query-params})
    (str (get params :a) (get params :b)))
  (GET "/hey" request (str {request :query-string}))
  (GET "/query" request (str (request :query-string)))
  (GET "/view-request" request (str request))
  (GET "/rest" [] (response {:email "sven@malvik.de"}))
  (GET "/test" [par] (response {:par1 par, :par2 "hey"}))
  (GET "/find/:name" [name] (response {:username name}))
  (GET "/foo" request (interpose ", " (keys request)))
  (GET "/hello/:name" [name] (str "Hello " name))
  (GET "/user/:id" [id]
    (str "<h1>Hello user " id "</h1>"))
  (route/resources "/"))

;(def handler (-> my_routes compojure.handler/api))
(def app (wrap-json-response my_routes))