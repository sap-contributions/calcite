#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to you under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
set -e
VERSION=1.37.0

# Java 21 doesn't suppport Java 8
if [ -d /Library/Java/JavaVirtualMachines/sapmachine-jdk-17.0.11.jdk/Contents/Home ]; then
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/sapmachine-jdk-17.0.11.jdk/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
fi

if [ -d /Library/Java/JavaVirtualMachines/sapmachine-17.jdk/Contents/Home ]; then
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/sapmachine-17.jdk/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
fi

./gradlew clean publishToMavenLocal

for module in core linq4j;
do
   mvn install:install-file \
      -Dfile=${module}/build/libs/calcite-${module}-$VERSION-SNAPSHOT.jar \
      -DgroupId=org.apache.calcite \
      -DartifactId=calcite-${module}-sap \
      -Dversion=$VERSION \
      -Dpackaging=jar \
      -DlocalRepositoryPath=../nucleus-resources-app/patches/
   mvn install:install-file \
      -Dfile=${module}/build/libs/calcite-${module}-$VERSION-SNAPSHOT.jar \
      -DgroupId=org.apache.calcite \
      -DartifactId=calcite-${module}-sap \
      -Dversion=$VERSION \
      -Dpackaging=jar \
      -DlocalRepositoryPath=$HOME/.m2/repository
   cat ${module}/build/publications/${module}/pom-default.xml | \
   sed -e "s/calcite-${module}/calcite-${module}-sap/g" -e "s/${VERSION}-SNAPSHOT/${VERSION}/g" > \
   ../nucleus-resources-app/patches/org/apache/calcite/calcite-${module}-sap/$VERSION/calcite-${module}-sap-$VERSION.pom
done
