#!/bin/bash
cd /galleon-openshift/maven
mvn package -Dinstall.dir=/wildfly-core -DwildflyLocation=$WILDFLY_LOCATION
rm -rf ~/.m2
chmod -R 777 /wildfly-core


