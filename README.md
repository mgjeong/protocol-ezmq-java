# ezmq library (Java)

protocol-ezmq-java is a library (jar) which provides a standard messaging interface over various data streaming
and serialization / deserialization middlewares along with some added functionalities.</br>
  - Currently supports streaming using 0mq and serialization / deserialization using protobuf.
  - Publisher -> Multiple Subscribers broadcasting.
  - Topic based subscription and data routing at source (read publisher).
  - High speed serialization and deserialization.


## Prerequisites ##
  - Remember, you must configure proxies for git and maven accordingly if necessary.
  - Setting up proxy for git
```shell
$ git config --global http.proxy http://proxyuser:proxypwd@proxyserver.com:8080
```
- JDK
  - Version : 1.8
  - [How to install](https://docs.oracle.com/javase/8/docs/technotes/guides/install/linux_jdk.html)
- Maven
  - Version : 3.5.2
  - [Where to download](https://maven.apache.org/download.cgi)
  - [How to install](https://maven.apache.org/install.html)
  - [Setting up proxy for maven](https://maven.apache.org/guides/mini/guide-proxies.html)


## How to build ##
  - Build guide of **ezmq library** is given [here](./ezmq/README.md)


## How to run ##
  - Build and run guide of **ezmq samples** is given [here](./samples/README.md)

## Usage guide for ezmq library (for microservices)

1. Reference ezmq library APIs : [doc/javadoc/index.html](doc/javadoc/index.html)


## Future Work ##
  - High speed parallel ordered serialization / deserialization based on streaming load.
  - Threadpool for multi-subscriber handling.
  - Router pattern. For number of subscribers to single publisher use case.
  - Clustering Support.
</br></br>
