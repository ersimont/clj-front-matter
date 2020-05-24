(ns front-matter.core
  (:require [clj-yaml.core :as yaml]))

(def front-matter-regex
  (re-pattern
    (clojure.string/join
      [; set to "dot all" mode, so `.` will include newlines
       "(?s)"
       ; `(?:...)?` is a non-capturing group that is optional
       ; `(.*\r?\n)? is the front matter, also optional
       "(?:---\r?\n(.*?\r?\n)?---\r?\n)?"
       ; This captures the content
       "(.*)"])))

(defn- split-front-matter [text]
  (let [[_ front-matter content]
        (re-matches front-matter-regex text)]
    {:front-matter front-matter :content content}))

(defn- yamlize [text]
  (when text (yaml/parse-string text)))

(defn parse-front-matter [text]
  (update (split-front-matter text)
          :front-matter
          yamlize))
