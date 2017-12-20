# ezMQ Java Client SDK

protocol-ezmq-java is a library (jar) which provides a standard messaging interface over various data streaming 
and serialization / deserialization middlewares along with some added functionalities.
Following is architecture of ezMQ: </br>
![ezMQ Architecture](doc/images/ezMQ_architecture_0.1.png?raw=true "ezMQ Arch")

## Features:
* Currently supports streaming using 0mq and serialization / deserialization using protobuf.
* Publisher -> Multiple Subscribers broadcasting.
* Topic based subscription and data routing at source (read publisher).
* High speed serialization and deserialization.

## Future Work:
* High speed parallel ordered serialization / deserialization based on streaming load.
* Threadpool for multi-subscriber handling.
* Router pattern.
* Clustering Support.
</br></br>

### How to build 

put some discription (eg. configuration etc)
[here](./edgex-ezmq/README.md)


### How to run sample

put short discription about sample
[here](./samples/README.md)
