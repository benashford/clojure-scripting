# clojure-scripting

A Clojure library designed to expose Java's scripting API

## Usage

The languages available by default are those pre-existing on your system.  E.g. for Java 8 you will have Oracle's Nashorn JavaScript interpreter.  You may also have a bridge to AppleScript if running on OS X.  This library also adds JRuby as a dependency.  More can be added by adding those languages as project dependencies.

Installed and available engines are listed by the ```get-engines``` function.

```clojure
clojure-scripting.core> (get-engines)
{"AppleScript"
 {:engine-name "AppleScriptEngine",
  :engine-version "1.1",
  :factory
  #<AppleScriptEngineFactory apple.applescript.AppleScriptEngineFactory@6038bb9b>,
  :language-version "2.3.1"},
 "ECMAScript"
 {:engine-name "Oracle Nashorn",
  :engine-version "1.8.0",
  :factory
  #<NashornScriptEngineFactory jdk.nashorn.api.scripting.NashornScriptEngineFactory@466413cc>,
  :language-version "ECMA - 262 Edition 5.1"},
 "ruby"
 {:engine-name "JSR 223 JRuby Engine",
  :engine-version "1.7.11",
  :factory
  #<JRubyEngineFactory org.jruby.embed.jsr223.JRubyEngineFactory@4029cfa6>,
  :language-version "jruby 1.7.11"}}
```

### Examples

#### AppleScript (if available)

Turn speakers on first:

```clojure
(eval-script "AppleScript" "say \"hello\"")
```

#### Ruby

This example shows the optional binding functionality.  A Clojure vector is bound to ```x```, this is done by passing a map with the bindings as the final paramaters.  JRuby, without changing any options, will be able to see this as the global variable ```$x``` (JRuby also supports bindings to different scopes).

```clojure
clojure-scripting.core> (eval-script "ruby" "$x.map.with_index {|x, i| x * i}" {"x" [9 10 11 100 101 200 300]})
[0 10 22 300 404 1000 1800]
```

The ruby snippet maps over the provided data and returns the result.

## TODO

Many options including, but not limited to: loading scripts from resources/files; using scripts, or objects generated from scripts to implement Clojure protocols.