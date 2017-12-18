#!/bin/sh
PROJECT_ROOT=$(pwd)
echo $PROJECT_ROOT
mkdir ../dependencies
cd ../dependencies
DEP_ROOT=$(pwd)

#Clone and install core-domain service
#cd $DEP_ROOT
#git clone https://github.com/edgexfoundry/core-domain.git
#cd core-domain
#mvn install -Dmaven.test.skip=true -U

#Clone and install support-logging-client service
#cd $DEP_ROOT
#git clone https://github.com/edgexfoundry/support-logging-client.git
#cd support-logging-client
#mvn clean install -U

#start package device-service
cd $PROJECT_ROOT
mvn clean install -U
echo "done"
