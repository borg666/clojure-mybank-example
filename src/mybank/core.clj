(ns mybank.core
  (:use compojure.core
        ring.middleware.json
        ring.util.response
        hiccup.core
        hickory.core)

  (:gen-class)

  (:require
           [compojure.route :as route]
           [compojure.handler :as handler]
           [mybank.view :as view]
           [clj-http.client :as client]
           [ring.adapter.jetty :as jetty]
           [clojure.data.json :as json]
           [clojure.string :as string]
           [hickory.select :as s]
            )
  )


(defn findUser [request]
  {:status 200
   :query-string {"name" "inputName"}
   :headers {"Content-Type" "text/html"}
   :body (response {:name1 name})})



;(defn makeHttpRequest [url]  (:body (client/get url)))

(defn make-http-request [url] (:body (client/get url)))
(defn parse-html-body [url] (parse (make-http-request url)))
(defn convert-to-hiccup [url] (as-hiccup (parse-html-body url)))
(defn convert-to-hickory [url] (as-hickory (parse-html-body url)))

(defn test-flatten [url] (flatten(convert-to-hiccup url)))

(defn parsed-doc [url] (parse "<a href=\"foo\">foo</a>"))
(defn c-as-hiccup [url] (as-hiccup (parsed-doc url)))
(defn test-flatten [url] (flatten(c-as-hiccup url)))

(def parsed-doc (parse "<a href=\"foo\">foo</a>"))
(def as-hic (as-hiccup parsed-doc))
(defn as-hick [url] (as-hickory parsed-doc))
(def parsed-frag  (parse-fragment (:body (client/get "https://www.indeed.co.uk/jobs?q=ios&l=London"))))
(def map-as-hickory (map as-hickory parsed-frag))


(defn count-of-jobs [keyword]
  (re-find #"(?!<div class=\"resultsTop\"><div id=\"searchCount\">[a-zA-Z0-9]+of )[,0-9]+(?=[<][\/]div[>])"
    (make-http-request (str "https://www.indeed.co.uk/jobs?q=" keyword "&l=London"))))
(defn job-count [job] (clojure.string/replace  (count-of-jobs job) #"," ""))

;(defn ios-job-count [] (clojure.string/replace  (count-of-jobs "ios") #"," ""))
;(defn javascript-job-count [] (clojure.string/replace  (count-of-jobs "javascript") #"," ""))
(defn london-jobs []
  (response {:london_jobs
             {:python (job-count "python")
              :ios (job-count "ios")
              :javascript (job-count "javascript")
              :go (job-count "golang")
              :java (job-count "java")
              :pascal (job-count "pascal")
              :fortran (job-count "fortran")
              :kotlin (job-count "kotlin")
              :php (job-count "php")
              :clojure (job-count "clojure")
              :android (job-count "android")
              :ada (job-count "ada")
              :scala (job-count "scala")
              :csharp (job-count "C#")
              }}))




(defroutes my_routes
  (GET "/" [] (view/index-page))
  (GET "/indeed" [] (as-hick "https://www.indeed.co.uk/jobs?q=ios&l=London"))
  (GET "/indeed1" [] map-as-hickory)
  (GET "/indeed2" [] (london-jobs))

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
  (GET "/user/:id" [id] (str "<h1>Hello user " id "</h1>"))
  (route/resources "/"))

(def app (wrap-json-response my_routes))

(defn -main []
  (jetty/run-jetty app {:port 8080}))