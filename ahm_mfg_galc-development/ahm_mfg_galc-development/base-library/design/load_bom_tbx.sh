#!/bin/ksh

#============================================================================
# load_bom_tbx.sh
# ===============
# Populate the database's BOM_TBX table by importing from an ASCII file of
# data placed on the server.
# ---------------------------------------------------------------------------
# Frequency: Can be run as often as desired, intended to be run daily.
# ---------------------------------------------------------------------------
#     Usage: Run by an authorized user.
#   Example: load_bom_tbx.sh pelp1gal /home/was7adm/mqft/OIF/BOM/BOM_EXPORT.txt
# ---------------------------------------------------------------------------
# Dependencies:
#    - Valid inputs provided (arg1 & arg2)
#    - Target database is cataloged on the local server (arg1)
#    - Presence of the input file to be imported (arg2)
#    - DB2 environment is installed on the local server
#    - Embedded Auth_id has authority to CONNECT and LOAD to target DB and import to BOM_TBX table
#        db2 grant load on database xxxxxx to user xxxxxx
#    - Set SOURCE Qrep properties for BOM_TBX table:
#        db2 "select has_loadphase, capture_load from asnarc.ibmqrep_subs where subname = 'BOM_TBX0001'"
#        db2 "update asnarc.ibmqrep_subs set has_loadphase = 'I', capture_load = 'R' where subname = 'BOM_TBX0001'"
#    - Set TARGET Qrep properties for BOM_TBX table:
#        db2 "select load_type from asnarc.ibmqrep_targets where subname = 'BOM_TBX0001'"
#        db2 "update asnarc.ibmqrep_targets set load_type = 3 where subname = 'BOM_TBX0001'"
# ---------------------------------------------------------------------------
#    Inputs: Arg1: database name that will be imported into
#            Arg2: Name of the import file in the current directory.
# ---------------------------------------------------------------------------
#   Outputs: Execution details written to cumulative <this_script_name>.log
# ---------------------------------------------------------------------------
#   Notes:
#     - Input file name will have current date/time appended upon either success or failure, ie:
#         BOM_EXPORT.txt -> BOM_EXPORT.txt_201807300845
#============================================================================


#****************************************************************************
#
# Function definitions start here
#
#****************************************************************************
# ---------------------------------------------------------------------------
# send_error_and_terminate: Forward the content of the encountered error to email/pager
# ---------------------------------------------------------------------------
function send_error_and_terminate {
  RenameInputfile
  subject="${scriptname}.sh|${servername}|${instancename}|${dbname}"
  message="${error_msg[$rc_command]} \n\n $results"
  echo $message >> $logfile
  echo "$message" | mail -s "$subject" $email > /dev/null
  echo $subject
  echo $message
  exit $rc_command
}


# ---------------------------------------------------------------------------
# RenameInputfile: Rename the input file being imported, and delete old ones
# ---------------------------------------------------------------------------
function RenameInputfile {
  mv $datfilein ${datfilein}_${date_str}
  datfile_list=$(ls -tl ${datfilein}_2* | awk 'NR > 5 {print $9}')
  if [[ "$datfile_list" != "" ]];  then
    rm -f $datfile_list
  fi
  return
}


# ---------------------------------------------------------------------------
# Initialize variables
# ---------------------------------------------------------------------------
# Arguments
dbname=$(echo $1 | tr '[a-z]' '[A-Z]')
datfilein=$2

homepath="/home/was7adm/mqft/OIF/BOM"
db2path="/home/db2rtcl/sqllib/bin"
date_str=$(date +"%Y%m%d%H%M")
scriptname="load_bom_tbx"
tabname="GALADM.BOM_TBX"
#datfileout="${datfilein}.datefixed"
msgfile="${scriptname}.msgs"
logfile="${scriptname}.log"
commitfreq="10000"
# It will be good to use generic username/password like Active directory
# user="AGALPBOM"
# password="EtT62xJH"
user="user"
password="password"
# We can remove the email from the script and create a splunk alert
email="GALC_OIF_SUPPORT@na.honda.com"

# Common vars
servername=$(echo `uname -n` | tr '[A-Z]' '[a-z]')
username=$(echo $USER | tr '[A-Z]' '[a-z]')

# Custom return codes
rc_success=0
err_logfile=1;  error_msg[$err_logfile]="Unable to open output log file $logfile"
err_infile=2;   error_msg[$err_infile]="Unable to open input file $datfilein"
err_connect=3;  error_msg[$err_connect]="Unable to connect to database $dbname"
err_import=4;   error_msg[$err_import]="Error issuing IMPORT command to table $tabname"
err_load=5;     error_msg[$err_load]="Error issuing LOAD (from null) command to table $tabname"

############### Program logic starts here ###############

### Load DB2 Environment ##########
if [ -f /home/db2rtcl/sqllib/db2profile ]; then
    . /home/db2rtcl/sqllib/db2profile
fi

cd $homepath

### Start the script log file for this run
echo "" >> $logfile
if [[ $? -ne 0 ]]; then
  rc_command=$err_logfile
  send_error_and_terminate
fi
echo "********** Start run of ${scriptname}.sh on $servername, user $username, at `date` **********" >> $logfile
echo "Database: $dbname, Table: $tabname, Import file: $datfilein" >> $logfile


### Can we find and read the input file?
if [[ ! -e $datfilein ]]; then
  rc_command=$err_infile
  send_error_and_terminate
fi


### Change all EFF_END_DATEs in the input from '99999999' to '20991231'
#sed 's/^\(.\{89\}\)99999999/\120991231/' $datfilein > $datfileout
#if [[ $? -ne 0 ]]; then
#  message="Error while issuing Unix sed command on: $datfilein"
#  send_error_and_terminate
#fi


### Empty the prior DB2 import messages file, else it will append
echo "" > $msgfile


### Connect to the database
results=$(${db2path}/db2 connect to $dbname user $user using $password)
if [[ $? -ne 0 ]]; then
  rc_command=$err_connect
  send_error_and_terminate
fi


### Empty the BOM_TBX table
results=$(${db2path}/db2 "load from /dev/null of del replace into $tabname nonrecoverable")
if [[ $? -ne 0 ]]; then
  rc_command=$err_load
  send_error_and_terminate
fi


### Pause to allow Qrep to process the LOAD above, preventing a deadlock w/IMPORT below
sleep 120


### IMPORT into the BOM_TBX table
echo "Starting import at: `date`" >> $logfile
results=$(${db2path}/db2 "import from $datfilein of asc modified by dateformat=\"YYYYMMDD\" method L (
1 18, 19 19, 20 30, 31 36, 37 42, 43 43, 44 46, 47 53, 54 59, 60 63, 64 74, 75 77, 
78 80, 81 81, 82 89, 90 97, 98 98, 99 100, 101 102, 103 103, 104 107, 108 108, 109 109, 
110 115, 116 135, 136 161, 162 166, 167 184, 185 187, 188 214
) commitcount $commitfreq messages $msgfile insert into $tabname (PART_NO,PLANT_LOC_CODE,PART_COLOR_CODE,SUPPLIER_NO,TARGET_MFG_DEST_NO,PART_BLOCK_CODE,PART_SECTION_CODE,PART_ITEM_NO,TGT_SHIP_TO_CODE,MTC_MODEL,MTC_COLOR,MTC_OPTION,MTC_TYPE,INT_COLOR_CODE,EFF_BEG_DATE,EFF_END_DATE,PART_PROD_CODE,PROCLOC_GP_NO,PROCLOC_GP_SEQ_NO,TGT_PLANT_LOC_CODE,TGT_MODEL_DEV_CODE,PART_COLOR_ID_CODE,SUPPLIER_CAT_CODE,PART_QTY,DATA_UPD_DESC_TEXT,TIMESTAMP_DATE,DC_FAM_CLASS_CODE,DC_PART_NO,PDDA_FIF_TYPE,DC_PART_NAME) ") 

if [[ $? -ne 0 ]]; then
  rc_command=$err_import
  send_error_and_terminate
fi
echo "Finished import at: `date`" >> $logfile

row_cnt=$(${db2path}/db2 -x select count from $tabname with ur)
row_cnt=$(echo $row_cnt | xargs)
echo "Table $tabname now has $row_cnt rows" >> $logfile


### Disconnect from the database
${db2path}/db2 terminate > /dev/null


### Rename the current input file and delete aged ones
RenameInputfile


### End the log file for this run, and truncate so it doesn't grow too large
echo "********** End run of $scriptname on `date` **********" >> $logfile
tail -n 5000 $logfile > ${logfile}x
mv ${logfile}x ${logfile}
exit $rc_success
