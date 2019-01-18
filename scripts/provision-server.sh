#!/bin/bash
#git clone https://github.com/jfdenise/galleon-openshift /galleon-openshift
cd /galleon-openshift/maven
mvn package -Dinstall.dir=/wildfly-core -DwildflyLocation=$WILDFLY_LOCATION
rm -rf ~/.m2
chmod -R 777 /wildfly-core
cp /git-src/target/ROOT.war /wildfly-core/standalone/deployments/ROOT.war
