#!/bin/sh
mvn clean
mvn compile
mvn package
java -cp target/edgex-ezmq-subscriber-sample.jar org.edgexfoundry.ezmq.client.App
