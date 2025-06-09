#!/bin/sh
# Define Micro Service image name and versions so that we can append to argocd as helm parameters and promote to [[ productTeam ]]-container-[[ usecaseEnv ]]
# Example but will need to customize 
MS_VERSIONS["lc-sales-interface-service"]="1.1.1-mnt5r"
