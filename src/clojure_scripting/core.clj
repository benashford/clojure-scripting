(ns clojure-scripting.core
  (:import [javax.script ScriptEngine ScriptEngineManager SimpleBindings]))

(defn make-script-engine-manager []
  (ScriptEngineManager.))

(defn get-factory-details [factory]
  {:engine-name (.getEngineName factory)
   :engine-version (.getEngineVersion factory)
   :factory factory
   :language-name (.getLanguageName factory)
   :language-version (.getLanguageVersion factory)})

(defn get-engines
  ([] (get-engines (make-script-engine-manager)))
  ([sem]
     (->> sem
          .getEngineFactories
          (map get-factory-details)
          (map (fn [factory-details]
                 [(:language-name factory-details) (dissoc factory-details :language-name)]))
          (into {}))))

(defn make-script-engine [language & [params]]
  (let [params (or params {})
        engine (.getEngineByName (make-script-engine-manager) language)]
    (doseq [[k v] params]
      (.put engine k v))
    engine))

(defn params->bindings [params]
  (let [bindings (SimpleBindings.)]
    (doseq [[k v] params]
      (.put bindings k v))
    bindings))

(defmulti eval-script (fn [s & more] (type s)))
(defmethod eval-script String [language script & [params]]
  (-> language
      make-script-engine
      (eval-script script params)))
(defmethod eval-script clojure.lang.Keyword [language script & [params]]
  (eval-script (name language) script params))
(defmethod eval-script ScriptEngine [script-engine script & [params]]
  (.eval script-engine script (params->bindings params)))
