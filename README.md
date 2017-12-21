# ezmq Java Client Library

protocol-ezmq-java is a library (jar) which provides a standard messaging interface over various data streaming 
and serialization / deserialization middlewares along with some added functionalities.</br>
Following is the architecture of ezmq client library: </br> </br>
![ezmq Architecture](doc/images/ezMQ_architecture_0.1.png?raw=true "ezmq Arch")

## Features:
* Currently supports streaming using 0mq and serialization / deserialization using protobuf.
* Publisher -> Multiple Subscribers broadcasting.
* Topic based subscription and data routing at source (read publisher).
* High speed serialization and deserialization.

## Future Work:
* High speed parallel ordered serialization / deserialization based on streaming load.
* Threadpool for multi-subscriber handling.
* Router pattern. For number of subscribers to single publisher use case.
* Clustering Support.
</br></br>

## How to build 
**For building the ezmq library following are the pre-requisites:**
1. Java 1.8 or higher <br>
2. Maven 3.5 or higher <br>

Build guide of **ezmq library** is given [here](./edgex-ezmq/README.md)


## How to run sample

Build guide of **ezmq samples** is given 
[here](./samples/README.md)

## Unit test and Code coverage report generation

When ezmq library is built with **./build.sh**, it will build all the unit test cases and will generate code coverage report in following directory: </br>
`~/protocol-ezmq-java/edgex-ezmq/target/jacoco-ut/` </br>

To see the report, open **index.html** in web browser from following directory:</br>
`~/protocol-ezmq-java/edgex-ezmq/target/jacoco-ut/org.edgexfoundry.ezmq/` </br>
