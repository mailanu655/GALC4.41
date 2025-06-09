#!/bin/sh
# MUST define USECASE or argocd-devops-sync task will fail.
USECASE=salesinterface
echo "USECASE=${USECASE}"
# MUST define MAJOR, MINOR, PATCH or git-clone-semver task will fail.
MAJOR="1"
MINOR="1"
PATCH="1"
echo "${MAJOR}.${MINOR}.${PATCH}"
