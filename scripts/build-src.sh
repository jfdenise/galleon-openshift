#!/bin/bash
git clone $GIT_REPOSITORY /git-src
cd /git-src
mvn clean install -Popenshift
cp /git-src/target/ROOT.war /wildfly-core/standalone/deployments/ROOT.war