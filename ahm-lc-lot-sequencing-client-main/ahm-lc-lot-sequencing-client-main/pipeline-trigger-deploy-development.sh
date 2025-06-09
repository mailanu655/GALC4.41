#!/bin/sh
# This script will run in OpenShift Pipelines pipeline-trigger-deploy task
# First parameter will be the image just built
builtImage=$1
# Specify the dev namespaces that you want to trigger updates automatically
# Otherwise it might take up to 15 minutes before Artifactory sync's with OpenShift image stream
# This could also be used if you want to trigger deployment after build of a feature branch
 
oc tag ${builtImage} lc-lot-sequencing-client:latest --scheduled -n linectrl-lotsequencing-mapdev
