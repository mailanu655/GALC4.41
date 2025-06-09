#!/bin/sh
# MUST define USECASE or argocd-devops-sync task will fail.
USECASE=product-search
echo "USECASE=${USECASE}"
# MUST define MAJOR, MINOR, PATCH or git-clone-semver task will fail.
MAJOR="1"
MINOR="3"
PATCH="0"
echo "${MAJOR}.${MINOR}.${PATCH}"
