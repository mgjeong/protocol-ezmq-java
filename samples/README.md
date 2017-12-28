# ezmq samples instructions

ezmq has subscriber and publisher sample applications. Please build and run using the following guide to experiment different options in sample.

## Prerequisites
 - Built ezmq library [link](./../ezmq/)

## How to build and run
### Subscriber sample
 - Using eclipse IDE
1. Import the ezmq Client sample [ezmq-client]:</br>
   **File -> Import -> Maven -> Existing Maven Projects -> Browse to "ezmq-client" -> Finish**
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

 - Using Linux Command line
1. Go to ~/protocol-ezmq-java/samples/ezmq-client
2. Run the script: (It will build and run the subscriber sample)
   ```
   $ ./build.sh
   ```
**Note:**
Once it is built, sample can be also be run using:
`$ java -jar target/edgex-ezmq-subscriber-sample.jar`

### Publisher sample
- Using eclipse IDE
1. Import the ezmq Server sample [ezmq-server]:</br>
   **File -> Import -> Maven -> Existing Maven Projects -> Browse to "ezmq-server" -> Finish**
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

- Using Linux Command line
1. Go to ~/protocol-ezmq-java/samples/ezmq-server
2. Run the script: (It will build and run the publisher sample)
   ```
   $ ./build.sh
   ```
**Note:**
Once it is built, sample can be also be run using:
`$ java -jar target/edgex-ezmq-publisher-sample.jar`

**Note: Logs and snapshot version is subject to change.**
