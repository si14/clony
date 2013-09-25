# clony
### a modern Clojure webapp example

This is an example of web application, based on http-kit and websockets and
utilizing excellent Stuart Sierra's [workflow post](http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded).

To run this application:

1. `lein cljsbuild once`
2. `lein uberjar`
3. `java -jar target/clony-0.1.0-SNAPSHOT-standalone.jar`
4. open `localhost:8081` in your browser
5. click the pony to have some pony wisdom

Please feel free to copy the code and use it in any way.

## Development

Development workflow is supposed to be similar to Stuart's: stay in `user`
namespace and run `(refresh)` to recompile changed namespaces and reload
web application. Application "nodes" were designed with possibility of partial
restart in mind, so please modify this according to your needs.
