#!/bin/bash

set -e
VERSION=1.36.0

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
   sed -e 's/calcite-${module}/calcite-${module}-sap/g' -e "s/${VERSION}-SNAPSHOT/${VERSION}/g" > \
   ../nucleus-resources-app/patches/org/apache/calcite/calcite-${module}-sap/$VERSION/calcite-${module}-sap-$VERSION.pom
done