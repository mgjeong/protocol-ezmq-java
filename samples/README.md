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
3. Run sample:
   - **Subscribe without topic:** </br>
   (a) Right click on the project -> Run As -> Run configurations -> Arguments -> Program Arguments</br>
   (b) Insert ip and port as program arguments. Example: -ip localhost -port 5562 </br>
   (c) Click on Apply -> Run</br>
    **Update ip, port as per requirement.** </br>
   - **Subscribe with topic:** </br>
   (a) Right click on the project -> Run As -> Run configurations -> Arguments -> Program Arguments</br>
   (b) Insert ip, port and topic as program arguments. Example: -ip localhost -port 5562 -t topic1</br>
   (c) Click on Apply -> Run </br>
    **Update ip, port and topic as per requirement.** </br>

 - Using Linux Command line
1. Go to ~/protocol-ezmq-java/samples/ezmq-client
2. Build the sample:
   ```
   $ ./build.sh
   ```
3. Run the sample: </br>
   ```
    $ java -jar target/edgex-ezmq-subscriber-sample.jar
   ```
   - **It will give list of options for running the sample.** </br>
   - **Update ip, port and topic as per requirement.** </br>

### Publisher sample
- Using eclipse IDE
1. Import the ezmq Server sample [ezmq-server]:</br>
   **File -> Import -> Maven -> Existing Maven Projects -> Browse to "ezmq-server" -> Finish**
2. Build sample:</br>
   **Right Click on the project -> Run as -> Maven install**
3. Run sample:</br>
   - **Publish without topic:** </br>
   (a) Right click on the project -> Run As -> Run configurations -> Arguments -> Program Arguments</br>
   (b) Insert port as program argument. Example: -port 5562 </br>
   (c) Click on Apply -> Run</br>
    **Update port as per requirement.** </br>
   - **Publish with topic:** </br>
   (a) Right click on the project -> Run As -> Run configurations -> Arguments -> Program Arguments</br>
   (b) Insert port and topic as program arguments. Example: -port 5562 -t topic1</br>
   (c) Click on Apply -> Run </br>
    **Update port and topic as per requirement.** </br>

- Using Linux Command line
1. Go to ~/protocol-ezmq-java/samples/ezmq-server
2. Build the sample:
   ```
   $ ./build.sh
   ```
3. Run the sample: </br>
   ```
    $ java -jar target/edgex-ezmq-publisher-sample.jar
   ```
   - **It will give list of options for running the sample.** </br>
   - **Update port and topic as per requirement.** </br>