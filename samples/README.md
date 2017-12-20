# How to run Sample

## Description
blabla

## Pre-requisites
Built ezMQ library [link](./../edgex-ezmq/)

## Build Client
### Using eclipse IDE

1. Import the EZMQ Client sample [edgex-ezmq-client]:
   File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq-client" -> Finish

2. Build sample:
   Right Click on the project -> Run as -> Maven install

3. Run sample:
   (i) Navigate to org.edgexfoundry.ezmq.client
   (ii)Right click on App.java -> Run As -> Java application

4. On successful run, it will show the following options on console:

         EZMQ initialized
         Enter 1 for General Event testing
         Enter 2 for Topic Based delivery

    Follow the instructions on the screen.

### Using Linux Command line
1. Pre-requisutes:
   Maven should be installed on linux machine. check it using : mvn -version

2. Go to ~/EZMQ/samples/edgex-ezmq-client

3. Build commands:
   $ mvn clean
   $ mvn compile
   $ mvn package


## Build Server
### Using eclipse IDE
1. Import the EZMQ Server sample [edgex-ezmq-server]:
   File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq-server" -> Finish

2. Build sample:
   Right Click on the project -> Run as -> Maven install

3. Run sample:
   (i) Navigate to org.edgexfoundry.ezmq.server
   (ii)Right click on App.java -> Run As -> Java application

4. On successful run, it will show the following options on console:

         EZMQ initialized
         Enter 1 for General Event testing
         Enter 2 for Topic Based delivery

    Follow the instructions on the screen.

### Using Linux command line:

1. Pre-requisutes:
   Maven should be installed on linux machine. check it using : mvn -version

2. Go to ~/EZMQ/samples/edgex-ezmq-server

3. Build commands:
   $ mvn clean
   $ mvn compile
   $ mvn package
  <br><br>
## Test
### 1. Execute Client <br>
   java -cp target/edgex-ezmq-subscriber-sample.jar org.edgexfoundry.ezmq.client.App
### 2. Input Command <br>
   On successful run, it will show the following options on console:

      EZMQ initialized
      Enter 1 for General Event testing 
      Enter 2 for Topic Based delivery
   
   Follow the instructions on the screen.
   
### 3. Execute Server <br>
   java -cp target/edgex-ezmq-publisher-sample.jar org.edgexfoundry.ezmq.server.App

### 4. Input Command <br>
      EZMQ initialized
      Enter 1 for General Event testing 
      Enter 2 for Topic Based delivery
   
   Follow the instructions on the screen.
   
   
   
**Note: Logs and snapshot version is subject to change.**





============= EZMQ Samples guide ==================

Pre-requisites:
:- Built EZMQ SDK "edgex-ezmq" [~/EZMQ/edgex-ezmq]

---- Build & Run instructions for EZMQ Client sample----

(A)Using eclipse IDE:

1. Import the EZMQ Client sample [edgex-ezmq-client]:
   File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq-client" -> Finish

2. Build sample:
   Right Click on the project -> Run as -> Maven install

3. Run sample:
   (i) Navigate to org.edgexfoundry.ezmq.client
   (ii)Right click on App.java -> Run As -> Java application

4. On successful run, it will show the following options on console:

    EZMQ initialized
    Enter 1 for General Event testing
    Enter 2 for Topic Based delivery

    Follow the instructions on the screen.

(B)Using Linux command line:

1. Pre-requisutes:
   Maven should be installed on linux machine. check it using : mvn -version

2. Go to ~/EZMQ/samples/edgex-ezmq-client

3. Build commands:
   $ mvn clean
   $ mvn compile
   $ mvn package
   $ java -cp target/edgex-ezmq-subscriber-sample.jar org.edgexfoundry.ezmq.client.App

4. On successful run, it will show the following options on console:

    EZMQ initialized
    Enter 1 for General Event testing
    Enter 2 for Topic Based delivery

    Follow the instructions on the screen.

---- Build & Run instructions for EZMQ Server sample-----

(A)Using eclipse IDE:

1. Import the EZMQ Server sample [edgex-ezmq-server]:
   File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq-server" -> Finish

2. Build sample:
   Right Click on the project -> Run as -> Maven install

3. Run sample:
   (i) Navigate to org.edgexfoundry.ezmq.server
   (ii)Right click on App.java -> Run As -> Java application

4. On successful run, it will show the following options on console:

    EZMQ initialized
    Enter 1 for General Event testing
    Enter 2 for Topic Based delivery

    Follow the instructions on the screen.

(B)Using Linux command line:

1. Pre-requisutes:
   Maven should be installed on linux machine. check it using : mvn -version

2. Go to ~/EZMQ/samples/edgex-ezmq-server

3. Build commands:
   $ mvn clean
   $ mvn compile
   $ mvn package
   $ java -cp target/edgex-ezmq-publisher-sample.jar org.edgexfoundry.ezmq.server.App

4. On successful run, it will show the following options on console:

    EZMQ initialized
    Enter 1 for [EdgeX meessage]
    Enter 2 for [ProtoBuf meessage]
    Enter 3 for [EdgeX meessage: topic based]
    Enter 4 for [ProtoBuf meessage: topic based]

    EZMQ initialized
    Enter 1 for General Event testing
    Enter 2 for Topic Based delivery

    Follow the instructions on the screen.

Note: Logs and snapshot version is subject to change.
