# EZMQ Samples instructions

## Description
EZMQ has subscriber and publisher sample applications. Please build and run using the following guide to experiment different options in sample.

## Pre-requisites
Built ezMQ library [link](./../edgex-ezmq/)

## Build and Run Subscriber sample
### Using eclipse IDE

1. Import the EZMQ Client sample [edgex-ezmq-client]:</br>
   **File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq-client" -> Finish**
2. Build sample:</br>
   **Right Click on the project -> Run as -> Maven install**
3. Run sample:</br>
   (i) **Navigate to org.edgexfoundry.ezmq.client**</br>
   (ii) **Right click on App.java -> Run As -> Java application**
4. On successful run, it will show the following options on console:
```
[main] DEBUG org.edgexfoundry.ezmq.EZMQAPI - EZMQ initialized
Enter 1 for General Event testing
Enter 2 for Topic Based delivery
```
 **Follow the instructions on the screen.**

### Using Linux Command line
1. Go to ~/protocol-ezmq-java/samples/edgex-ezmq-client
2. ./build.sh
3. java -jar target/edgex-ezmq-subscriber-sample.jar

## Build and Run Publisher sample
### Using eclipse IDE
1. Import the EZMQ Server sample [edgex-ezmq-server]:</br>
   **File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq-server" -> Finish**
2. Build sample:</br>
   **Right Click on the project -> Run as -> Maven install**
3. Run sample:</br>
   (i) **Navigate to org.edgexfoundry.ezmq.server**</br>
   (ii)**Right click on App.java -> Run As -> Java application**

4. On successful run, it will show the following options on console:
```
[main] DEBUG org.edgexfoundry.ezmq.EZMQAPI - EZMQ initialized
Enter 1 for General Event testing
Enter 2 for Topic Based delivery
```
**Follow the instructions on the screen.**

### Using Linux Command line
1. Go to ~/protocol-ezmq-java/samples/edgex-ezmq-server
2. ./build.sh
3. java -jar target/edgex-ezmq-publisher-sample.jar


**Note: Logs and snapshot version is subject to change.**
