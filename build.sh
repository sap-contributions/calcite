#!/bin/bash

set -e
VERSION=1.36.0

./gradlew clean publishToMavenLocal
mvn install:install-file \
   -Dfile=core/build/libs/calcite-core-$VERSION-SNAPSHOT.jar \
   -DgroupId=org.apache.calcite \
   -DartifactId=calcite-core-sap \
   -Dversion=$VERSION \
   -Dpackaging=jar \
   -DlocalRepositoryPath=../nucleus-resources-app/patches/
mvn install:install-file \
   -Dfile=core/build/libs/calcite-core-$VERSION-SNAPSHOT.jar \
   -DgroupId=org.apache.calcite \
   -DartifactId=calcite-core-sap \
   -Dversion=$VERSION \
   -Dpackaging=jar \
   -DlocalRepositoryPath=$HOME/.m2/repository
cat core/build/publications/core/pom-default.xml | \
  sed -e 's/calcite-core/calcite-core-sap/g' -e "s/${VERSION}-SNAPSHOT/${VERSION}/g" > \
  ../nucleus-resources-app/patches/org/apache/calcite/calcite-core-sap/$VERSION/calcite-core-sap-$VERSION.pom
