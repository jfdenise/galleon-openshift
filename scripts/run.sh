#!/bin/bash
cd /galleon-openshift/maven
mvn package -Dinstall.dir=/wildfly-core -DwildflyLocation=$WILDFLY_LOCATION
rm -rf ~/.m2
chmod -R 777 /wildfly-core

git clone $GIT_REPOSITORY /git-src
cd /git-src
mvn clean install -Popenshift
cp /git-src/target/ROOT.war /wildfly-core/standalone/deployments/ROOT.war
