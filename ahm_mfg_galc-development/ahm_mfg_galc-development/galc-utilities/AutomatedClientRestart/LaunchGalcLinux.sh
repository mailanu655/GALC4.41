#!/bin/sh

export JAVACMD=/read-write/mnt/addons/jre1.5.0_12/javaws/javaws

# If another instance is running exit
if [ $(ps -elf | fgrep launchgalc.sh | fgrep -v fgrep | fgrep -v $$ | wc -l) -gt 0 ] ; then
    echo $(date) script already running, terminating other instances: $(ps -elf | fgrep launchgalc.sh | fgrep -v fgrep | fgrep -v $$ | awk '{print $4}')
    $(kill -9 $(ps -elf | fgrep launchgalc.sh | fgrep -v fgrep | fgrep -v $$ | awk '{print $4}'))
fi

# Enter Alt-Esc to bring up any minimized instances of GALC
$(/read-write/mnt/addons/jre1.5.0_12/bin/java -jar /read-write/AltEsc.jar)

# Enter infinite loop
while true; do
    export HNAME=$(/read-only/bin/hostname)
    # If the hostname is not set properly, display an error message - if OK, get intended environment from name
        if [ $HNAME = "WSP" ] ; then
            zenity --info --text "Please add the asset number to the terminal name"
            export VALID_HNAME="false"
        else
            export ENV=$(echo $HNAME | cut -c 3)
                case $ENV in
                "P"|"p") export GALCURL=http://galchminline1naprod.hmin.am.honda.com/BaseWebStart/$HNAME.host
                         VALID_HNAME="true";;
                "U"|"u") export GALCURL=http://galchminline1naua.hmin.am.honda.com/BaseWebStart/$HNAME.host
                         VALID_HNAME="true";;
                "D"|"d") export GALCURL=http://galchminline1nadev.hmin.am.honda.com/BaseWebStart/$HNAME.host
                         VALID_HNAME="true";;
                "S"|"s") export GALCURL=http://galchminline1nasb.hmin.am.honda.com/BaseWebStart/$HNAME.host
                         VALID_HNAME="true";;
                *) zenity --info --text "Please enter a valid terminal name. \n Ex: WSP01234 \n First 2 characters = WS \n Third character = environment - P=prod, U=ua, Q=qa, D=dev, S=sandbox \n Last 5 = asset #"
                        VALID_HNAME="false";;
                esac
        fi
    # If hostname is valid, validate environment and launch if not running
    if [ $VALID_HNAME=true ] ; then

        # Check if GALC is already running
        if [ $(ps -elf | fgrep galchminline1na | fgrep -v fgrep | wc -l) -gt 0 ] ; then
            # If running but not in the environment we want, kill process
            if [ $(ps -elf | fgrep galchminline1na | fgrep -v fgrep | awk '{print ($(NF))}') != "$GALCURL" ] ; then
                $(kill -9 $(ps -elf | fgrep galchminline1na | fgrep -v fgrep | awk '{print $4}'))
                echo $(date) terminating process in other environment, starting GALC
                ($JAVACMD $GALCURL)&
            else
                echo $(date) GALC already running
            fi
        else
            echo $(date) starting GALC
            ($JAVACMD $GALCURL)&
        fi
    fi
    sleep 1
done
            
                         