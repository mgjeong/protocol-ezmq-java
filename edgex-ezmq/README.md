# Build instructions

Pre-requisite:
Built core-domain service

(A)Using eclipse IDE:

1. Import the EZMQ sdk [edgex-ezmq]:
   File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq" -> Finish

2. Build the EZMQ jar:
   Right Click on the project -> Run as -> Maven install

3. After step 2, it will create a jar:
   ~/EZMQ/edgex-ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar

(B)Using Linux command line:

1. Pre-requisutes:
   Maven should be installed on linux machine. check it using : mvn -version

2. Goto: ~/EZMQ/edgex-ezmq/

3. Build command:
   $ mvn build

4. On successful build it will create a jar:
   ~/EZMQ/edgex-ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar

---------- Using EZMQ SDK [For micro-services] -------------
1. The micro-service whoever wants to use EZMQ APIs has to build core-domain service and EZMQ SDK.
2. Follow EZMQ sample app guide to use EZMQ SDK APIs. [~/EZMQ/samples]
