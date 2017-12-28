# ezmq library build instructions

### Using eclipse IDE ###

   1. Import the ezmq library [edgex-ezmq]:</br>
       **File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq" -> Finish**
   2. Build the ezmq jar: </br>
       **Right Click on the project -> Run as -> Maven install**
   3. After step 2, it will create a jar: </br>
       **~/protocol-ezmq-java/ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**

### Using Linux command line ###

   1. Go to ~/protocol-ezmq-java/edgex-ezmq
   2. ./build.sh
   3. After step 2, it will create a jar: </br>
       **~/protocol-ezmq-java/edgex-ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**

### Binaries
   
     - Executable : manager-0.1.0-SNAPSHOT.jar
     - Includes : RESTful APIs
     - Features : Abstracts interfaces for those open-source data processing engines


### Usage guide for ezmq library [For micro-services] 
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
