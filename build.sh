#!/bin/bash

set -e

./gradlew clean publishToMavenLocal
mvn install:install-file \
   -Dfile=core/build/libs/calcite-core-1.35.0-SNAPSHOT.jar \
   -DgroupId=org.apache.calcite \
   -DartifactId=calcite-core-sap \
   -Dversion=1.35.0 \
   -Dpackaging=jar \
   -DlocalRepositoryPath=../nucleus-resources-app/patches/
mvn install:install-file \
   -Dfile=core/build/libs/calcite-core-1.35.0-SNAPSHOT.jar \
   -DgroupId=org.apache.calcite \
   -DartifactId=calcite-core-sap \
   -Dversion=1.35.0 \
   -Dpackaging=jar \
   -DlocalRepositoryPath=/Users/d001323/.m2/repository
