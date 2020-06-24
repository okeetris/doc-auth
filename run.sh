#!/bin/bash

if [ "$1" == "" ]; then
  echo "Usage: run.sh qualified-class-name [args]"
  exit 1
fi

className=$1
shift
echo "0000"

set -e

mvn -q package dependency:copy-dependencies

echo "111111"

CLASSPATH=""
# shellcheck disable=SC2045
for jar in $(ls target/dependency/*.jar target/rg_auth_svc-0.0.1.jar); do
  CLASSPATH=$CLASSPATH:$jar
done

echo "222222"


ADD_MODULES=""
if [ "$(java -version 2>&1 | head -1 | grep '\"1\.[78].\+\"')" = "" ]; then
  ADD_MODULES="--add-modules=java.xml.bind"
fi
echo "========="

echo $className
echo $*

echo "========="
java $ADD_MODULES -cp $CLASSPATH $className $*