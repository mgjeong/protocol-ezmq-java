###############################################################################
# Copyright 2018 Samsung Electronics All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
###############################################################################

#!/bin/sh
PROJECT_ROOT=$(pwd)
echo $PROJECT_ROOT

#build ezmq jar & run test cases
cd ./ezmq
./build.sh

#build ezmq sample [publisher]
cd $PROJECT_ROOT/samples/ezmq-server
./build.sh

#build ezmq sample [subscriber]
cd $PROJECT_ROOT/samples/ezmq-client
./build.sh


