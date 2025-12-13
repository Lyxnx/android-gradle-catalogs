#! /usr/bin/env bash

sedi() {
  # BSD sed doesn't have --version, GNU does
  if sed --version >/dev/null 2>&1; then
    sed -i "$@"
  else
    sed -i '' "$@"
  fi
}

try() {
  "$@"
  _status=$?
  if [ ${_status} -ne 0 ]; then
    echo "FAILED after invoking \"$*\"" >&2
    exit 1
  fi
  return ${_status}
}

if [[ $# -ne 1 ]]; then
  echo "Usage: $0 <version>"
  exit 1
fi

NEW_VERSION="$1"

try sedi -e "s/^catalogs\.version=.*/catalogs.version=$NEW_VERSION/" gradle.properties
try git commit "gradle.properties" -m "Catalogs version $NEW_VERSION"

TAGNAME="v$NEW_VERSION"
try git tag -f "$TAGNAME"
try git push origin bleeding
try git push origin "$TAGNAME"

try ./gradlew publishCatalogs
