#!/usr/bin/env bash

# Run me only once when Maven dependencies change. That way the Docker build can cache this layer each time.

set -e
rm -Rf lib
mvn package dependency:copy-dependencies -DincludeScope=runtime -DskipTests=true -DskipBuild -Dmdep.prependGroupId=true -DoutputDirectory=lib --fail-never
