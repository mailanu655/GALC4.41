#!/bin/sh
# MUST define MAJOR, MINOR, PATCH or git-clone-semver task will fail
MAJOR="1"
MINOR="9"
PATCH="9"
echo "${MAJOR}.${MINOR}.${PATCH}"
 
# What directory contains your code relative to root of git repo default '.' when not defined
CONTEXT_PATH=.
 
# Optionally Override the MS_NAME and MS_GROUPID used to name the artifact
#MS_NAME=
#MS_GROUPID=
 
# Optionally Override the MVN_OPTIONS used in mvn task
# For Angular repo you must set MVN_OPTIONS=clean
MVN_OPTIONS=clean
