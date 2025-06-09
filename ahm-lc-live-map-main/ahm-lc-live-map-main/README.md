# ahm-linectrl-livemap
This is the Angular Web application for live map

## Configuration
src/main/resources/application.yaml  
This contains all of the default configurations that are used for development and common across all environments  
* Client will be overridden by environment variables
* In OpenShift this is managed in ConfigMap lclm-cm in the linectrl-livemap-[[ usecaseEnv ]] namespace
* Example ConfigMap in src/main/etc/cd/os/lclm-cm.yaml
* Need to update url's, logMessage for each namespace deploying ConfigMap in

## Helm
You can run the following to test that the helm templates generate correctly
```
cd charts/lc-live-map/
helm template test . -f values-debug.yaml
```

## Build
```
mvn clean install
```
When you commit and push to git then a build will be automatically triggered in HDC Non Prod Cluster  
https://jenkins-linectrl-devops-np.apps.npocp.hna.honda.com/job/ahm-lc-live-map/  
This will create a buildconfig and use java s2i to create container image and publish to Artifactory  
https://artifactory.amerhonda.com/artifactory/webapp/#/artifacts/browse/tree/General/linectrl-container-dev/lc-live-map  

## Run
```
mvn spring-boot:run
```

## Deployment
Deployment of the container image is not managed in this git repo but instead in  
https://hnapxlscmgit01.amerhonda.com/Line-Control/ahm-lc-live-map-devops/tree/dev  
