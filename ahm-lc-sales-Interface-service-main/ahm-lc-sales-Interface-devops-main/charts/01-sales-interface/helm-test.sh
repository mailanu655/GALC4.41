#!/bin/sh
declare -A MS_VERSIONS=()
 
REPO_DST='linectrl-container-dev'
HELM_PARAMS=''
 
. ./pipeline.sh
for key in "${!MS_VERSIONS[@]}"; do
 
    HELM_PARAMS="${HELM_PARAMS} --set-string ${key}.imageVersion=${MS_VERSIONS[${key}]} --set-string ${key}.artifactoryRepo=${REPO_DST}"
 
done
helm template dev-odc . -f values-dev-odc.yaml $HELM_PARAMS
