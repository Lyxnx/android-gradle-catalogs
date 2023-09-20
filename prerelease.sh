#! /usr/bin/env bash

set -e

function show_help() {
  echo "$0 <new version>"
  exit
}

if [[ $# -lt 1 ]]; then
  show_help
fi

current_dir="$(pwd)"
properties_file="$current_dir/gradle.properties"
external_file="$current_dir/versions-external/libs.versions.toml"
version="$1"

sed -i -E "s/CATALOGS_VERSION=.+/CATALOGS_VERSION=$version/g" "$properties_file"
sed -i -E "s/catalogs-plugins = \".+\"/catalogs-plugins = \"$version\"/g" "$external_file"

doctoc CHANGELOG.md --github
