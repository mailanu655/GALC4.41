#!/bin/ksh

if [ -z "$1" ]
  then
    echo "No argument supplied: Usage: \n \
	stop_stamp_service.sh <application_id> \n \
	eg. stop_stamp_service.sh stampstorageservice_map_dev \n \
	eg. stop_stamp_service.sh stampstorageservice_map_qa \n \
	<application_id> should match value passed to the start script"
    exit
fi


#send kill/SIGTERM signal to StampStorageService
ps -f -u ohcvadev,ohcvaqa,ohcvaprd -opid,args|grep $1|grep -v grep|awk '{print $1}'|xargs kill


