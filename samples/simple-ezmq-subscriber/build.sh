#!/bin/sh
PROJECT_ROOT=$(pwd)
echo $PROJECT_ROOT
cd ../../../../
REPO_ROOT=$(pwd)
cd EZMQ/java/edgex-ezmq
EZMQ_ROOT=$(pwd)

#start install EZMQ
cd $EZMQ_ROOT
./build.sh

#start package
cd $PROJECT_ROOT
mvn install -U
echo "done"
