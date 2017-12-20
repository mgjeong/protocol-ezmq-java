# EZMQ Build instructions

## (A)Using eclipse IDE:

1. Import the EZMQ sdk [edgex-ezmq]:</br>
    **File -> Import -> Maven -> Existing Maven Projects -> Browse to "edgex-ezmq" -> Finish**
2. Build the EZMQ jar: </br>
    **Right Click on the project -> Run as -> Maven install**
3. After step 2, it will create a jar: </br>
    **~/protocol-ezmq-java/edgex-ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**

## (B)Using Linux command line:

1. chmod +x build.sh  **[Required only if copied from window machine to linux]**
2. ./build.sh
3. After step 2, it will create a jar: </br>
    **~/protocol-ezmq-java/edgex-ezmq/target/edgex-ezmq-0.0.1-SNAPSHOT.jar**

# Usage guide for EZMQ SDK [For micro-services] 
1. Include following in your pom.xml :

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
