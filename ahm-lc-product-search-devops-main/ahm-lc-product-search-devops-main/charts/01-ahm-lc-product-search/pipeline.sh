#!/bin/sh
# Define Micro Service image name and versions so that we can append to argocd as helm parameters and promote to [[ productTeam ]]-container-[[ usecaseEnv ]]
# Example but will need to customize
MS_VERSIONS["lc-product-search-service"]="1.3.1-vfpf7"
MS_VERSIONS["lc-product-search-client"]="1.4.0-kmb68"
