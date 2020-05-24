(ns front-matter.core-test
  (:require [clojure.test :refer :all]
            [front-matter.core :refer :all]))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(deftest extracting-front-matter
  (testing "with front matter"
    (is (= {:front-matter {:a 1} :content "fancy content"}
           (parse-front-matter
             (long-str
               "---"
               "a: 1"
               "---"
               "fancy content"))))
    (is (= {:front-matter {:a 1 :b 2} :content "two\nlines"}
           (parse-front-matter
             (long-str
               "---"
               "a: 1"
               "b: 2"
               "---"
               "two"
               "lines")))))

  (testing "without front matter"
    (is (= {:front-matter nil :content "fancy content"}
           (parse-front-matter "fancy content"))))

  (testing "with unclosed front matter"
    (is (= {:front-matter nil :content "---\nNot front matter"}
           (parse-front-matter "---\nNot front matter"))))

  (testing "with triple dash, but not on the first line"
    (is (= {:front-matter nil :content "a: 1\n---\nb: 2\n---\nMore"}
           (parse-front-matter "a: 1\n---\nb: 2\n---\nMore"))))

  (testing "with empty front matter"
    (is (= {:front-matter nil :content "More"}
           (parse-front-matter "---\n---\nMore")))
    (is (= {:front-matter nil :content "More"}
           (parse-front-matter "---\n\n\n---\nMore"))))

  (testing "with multiple triple dashes in the file"
    (is (= {:front-matter {:a 1} :content "more\n---\ncontent"}
           (parse-front-matter
             (long-str
               "---"
               "a: 1"
               "---"
               "more"
               "---"
               "content")))))

  (testing "with different line endings"
    (is (= {:front-matter {:a 1} :content "fancy content"}
           (parse-front-matter
             (long-str
               "---\r"
               "a: 1\r"
               "---"
               "fancy content"))))))
