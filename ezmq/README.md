# ezmq library build instructions

### How to build ###
#### Using eclipse IDE ####
   1. Import the ezmq library [edgex-ezmq]:</br>
       **File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq" -> Finish**
   2. Build the ezmq jar: </br>
       **Right Click on the project -> Run as -> Maven install**
   3. After step 2, it will create a jar: </br>
       **~/protocol-ezmq-java/ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**

#### Using MVN ####
   1. Go to ~/protocol-ezmq-java/edgex-ezmq
   2. 
   ```shell
   $ mvn clean install -U
   ```
   3. After step 2, it will create a jar: </br>
       **~/protocol-ezmq-java/ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**

#### Using Build Script ####
   1. Go to ~/protocol-ezmq-java/edgex-ezmq
   2. 
   ```shell
   $ ./build.sh
   ```
   3. After step 2, it will create a jar: </br>
       **~/protocol-ezmq-java/ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**


### Binaries ###
   - ezmq library
     - Library : edgex-ezmq-0.0.1-SNAPSHOT.jar
     - Includes : Messaging Interface APIs  
     - Features : Various data streaming/serialization/deserialization middlewares along with some added functionalities  
<br /><br />




### Usage guide for ezmq library (For micro-services) ### 
   - Include following in your pom.xml
   ```
   <properties>
       <ezmf.version>0.0.1-SNAPSHOT</ezmf.version>
       <core-domain.version>0.2.0</core-domain.version>
       <core-exception.version>0.2.0</core-exception.version>
       <nexusproxy>https://nexus.edgexfoundry.org</nexusproxy>
       <repobasepath>content/repositories</repobasepath>
   </properties>

   <dependencies>
       <dependency>
           <groupId>org.edgexfoundry.ezmq</groupId>
           <artifactId>edgex-ezmq</artifactId>
           <version>${ezmf.version}</version>
       </dependency>
       <dependency>
           <groupId>org.edgexfoundry</groupId>
           <artifactId>core-domain</artifactId>
           <version>${core-domain.version}</version>
       </dependency>
       <dependency>
           <groupId>org.edgexfoundry</groupId>
           <artifactId>core-exception</artifactId>
           <version>${core-exception.version}</version>
       </dependency>
   </dependencies>

   <repositories>
       <repository>
           <id>releases</id>
           <name>EdgeX Releases Repository</name>
           <url>${nexusproxy}/${repobasepath}/releases</url>
       </repository>
   </repositories>
   ```
