#!/bin/bash

packageName="com.fges.todoapp.App"

>&2 echo -e "Compile program..."
if ! compileErrors=$(mvn compile 2>&1); then
  echo -e "$compileErrors"
  exit 1
fi

args=""
for arg in "$@"
do
    args="$args \"$arg\"";
done

mavenFlags+=("-q") # Display only errors

>&2 echo -e "Package: [$packageName]"
>&2 echo -e "Executing with args: [$args]"
>&2 echo -e "Maven flags: [$mavenFlags]"

mvn $mavenFlags exec:java -Dexec.mainClass="$packageName" -Dexec.args="$args"
