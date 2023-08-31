#! /usr/bin/env bash

set -e

function show_help() {
  echo "$0 <new version>"
  exit
}

if [[ $# -lt 1 ]]; then
  show_help
fi

properties_file="$(pwd)/gradle.properties"
version="$1"

# Will run validateCatalog task as part of build
./gradlew clean build

# Update the value in the properties file
sed -i -E "s/CATALOGS_VERSION=.+/CATALOGS_VERSION=$version/g" "$properties_file"

./gradlew publish