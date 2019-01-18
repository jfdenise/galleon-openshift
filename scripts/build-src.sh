#!/bin/bash
git clone $GIT_REPOSITORY /git-src
cd /git-src
mvn clean install -Popenshift
