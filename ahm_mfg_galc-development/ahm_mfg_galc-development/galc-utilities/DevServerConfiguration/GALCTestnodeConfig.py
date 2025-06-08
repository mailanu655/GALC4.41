##
# Jython script to a configure the WAS 7.0 development environment for GALC on RAD 8.0/9.0
# Author             Change Date          Change Comment
# Suriya Sena              21-AUG-2009          Initial Version for RAD 7.0 WAS and 6.1
# Suriya Sena        04-OCT-2010          Updated to run on for RAD 8.0 and WAS 7.0
# John Martinek      22-AUG-2014 
# Suriya Sena        23-AUG-2015          Deleted duplicate variable definitions for SERVER,NODE,GALC_REGISTRY_JAR_DIR and GALC_REGISTRY_PROPERTY_DIR
# Suriya Sena        23-AUG-2015          Fixed error messages in checkFile and checkDir
# 
# This script configures the GALC Test server according to the instructions 7.0 GALC Server Setup (pages 31 to 108)

# INSTRUCTIONS
# 1) Setup environment and create a server profile using via the Admin GUI using the instructions in RAD 7.0
#    GALC Server setup P3 to 13. Note down the Profile Name and *NODE NAME* you use.
#  
# 2) Create a server using the GALC Server setup document and note down the *SERVER NAME* you use.
#
# 3) Modify the global variables below to match your environment. (the node name is defined in the profile
#    and the server name is defined when the server is created from a profile).
# 4) Start the server.
# 5) Run the script
# 6) Restart the server.
##

##
# I M P O R T A N T
# Review the global variables listed below and set them accordingly for your environment.
##
import sys
import os.path
import re

global CELL,NODE,SERVER,DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH,DB2UNIVERSAL_JDBC_DRIVER_PATH,MQMUSERID,MQMPASSWD,GALC_REGISTRY_JAR_DIR,GALC_REGISTRY_PROPERTY_DIR,WORKSPACE_ROOT,WSAD_SERVER_PROPERTIES_DIR,PROPERTY_SERVICES_ENABLED_DIR


# CELL name
CELL=AdminControl.getCell()
# The default node name 
NODE='GALCNode1'
# The default server name
SERVER='GALCServer1'
# You *WILL* need to modify the following directories for your environment
# REMEMBER to use forward slash '/' in paths and  semi colon ';' as path separator
#  1) The GALCRegistry.properties directory


# The default node name 
# The default server name
# You *WILL* need to modify the following directories for your environment
# REMEMBER to use forward slash '/' in paths and  semi colon ';' as path separator
#  1) The GALCRegistry.properties directory
#  2) The WSAD_SERVER_PROPERTIES directory 
#  3) The PROPERTY_SERVICES_ENABLED directory
GALC_REGISTRY_JAR_DIR="C:/Program Files (x86)/IBM/SDP/runtimes/base_v7/lib/ext"
GALC_REGISTRY_PROPERTY_DIR="C:/Program Files (x86)/IBM/SDP/runtimes/base_v7/properties"
WORKSPACE_ROOT="D:/ALCWorkspaces/HMADevelopment"
WSAD_SERVER_PROPERTIES_DIR="%s/HMA_SERVER_PROPERTY_FILES/WSAD_SERVER_PROPERTIES" % WORKSPACE_ROOT



# You *WILL* need to modify the DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH and DB2UNIVERSAL_JDBC_DRIVER_PATH
# It should be set it to the directory that contains the DB2 Driver jars  e.g dbd2jcc.jar 
#DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH="c:/sqllib/bin"
DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH="'C:/Program Files (x86)/IBM/SQLLIB_01/BIN'"
#DB2UNIVERSAL_JDBC_DRIVER_PATH="c:/sqllib/java"
DB2UNIVERSAL_JDBC_DRIVER_PATH="'C:/Program Files (x86)/IBM/SQLLIB_01/java'"
# *OPTIONAL* if you want to enable printing you modify both the MQUSERID and MQPASSWD 
# to a username and password that belong to the mqm group. 
MQMUSERID='mqm'
MQMPASSWD='mqm'

##
# End 
##
##
# Helper functions to check that directories are setup correctly before proceeding with configuration
##

def checkDirectories(): 
    if not checkFile(GALC_REGISTRY_JAR_DIR+"/"+"GALCRegistry.jar"):
      sys.exit()
      
    if not checkFile(GALC_REGISTRY_PROPERTY_DIR+"/"+"GALCRegistry.properties"):
      sys.exit()        
      
    if not checkDir(DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH):
      sys.exit()      

    if not checkDir(DB2UNIVERSAL_JDBC_DRIVER_PATH):
      sys.exit()      

       
       
def checkFile(filename):

    if filename.find("\\") >= 0 :
       sys.stdout.write("ERROR: Checking file "+filename+" paths must be delimited by / \n")
       return 0
       
    if os.path.isfile(filename):
       sys.stdout.write("OK: Checking file "+filename+" exists \n")
       return 1
    else:
       sys.stdout.write( "ERROR: Checking file "+filename+" file does not exist! \n")
       return 0
       
def checkDir(dirname):
	
    if dirname.find('\\') >= 0:
       sys.stdout.write("ERROR: Checking directory "+dirname+" paths must be delimited by / \n")
       return 0
       	       
    if os.path.isdir(dirname.replace("'","")):
       sys.stdout.write("OK: Checking directory "+dirname+" exists \n")
       return 1
    else:
       sys.stdout.write( "ERROR: Checking directory "+dirname+" does not exist or is not a directory! \n")
       return 0 


##
# P35 - 37 turnOnOrbPassByReference
##
def turnOnOrbPassByReference(server):
  sys.stdout.write('Setting ORB Pass By Reference to true...')
  orb = AdminConfig.list('ObjectRequestBroker',server)
  ## set pass by reference A.K.A noLocalCopies
  AdminConfig.modify(orb,[['noLocalCopies', 'true']])
  #AdminConfig.save()
  sys.stdout.write('Done \n')
  
  

   

 

##
# P54 - 76 configureJDBCProvider 
##
def configureJDBCProvider():
  sys.stdout.write('Configure JDBC Provider ...')	
  # P54 - 57 setVariables 	
  AdminTask.setVariable('[ -scope node='+ NODE +' -variableName DB2UNIVERSAL_JDBC_DRIVER_PATH -variableValue '+ DB2UNIVERSAL_JDBC_DRIVER_PATH +' ]' )
  AdminTask.setVariable('[ -scope node='+ NODE +' -variableName DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH -variableValue '+ DB2UNIVERSAL_JDBC_DRIVER_PATH +' ]' )
  # P57 - 66 create JDBC provider
  jdbcid = AdminTask.createJDBCProvider('[-scope node='+ NODE +' -databaseType DB2 -providerType "DB2 Universal JDBC Driver Provider" -implementationType  "Connection pool data source" -name "DB2 Universal JDBC Driver Provider"]'  )
  sys.stdout.write('Done \n')
  return jdbcid


  

  
##
# P81 - 92 configureGALCDataSource 
##
def configureGALCDataSource(jdbcProviderId,dbName,dbServer,dbPort):
   sys.stdout.write('Configure GALC Datasource ...')
   #Configure J2C Authentication Data
   alias = ['alias', 'galadm']
   userid = ['userId', 'galadm']
   password = ['password', 'galadm']
   desc =['description','GALC Database']
   jaasAttrs = [alias, userid, password,desc]
   security = AdminConfig.getid('/Security:/')
   AdminConfig.create('JAASAuthData',security,jaasAttrs)
   
   newDs =AdminTask.createDatasource(jdbcProviderId,
                              ['-name', 'GALCDatasource',
                              '-jndiName','jdbc/galdb-ds5',
                              '-dataStoreHelperClassName','com.ibm.websphere.rsadapter.DB2UniversalDataStoreHelper',                             
                              '-componentManagedAuthenticationAlias','galadm',   
                              '-containerManagedPersistence','true',
                              '-configureResourceProperties [[databaseName java.lang.String %s ][driverType java.lang.Integer 4][serverName java.lang.String %s][portNumber java.lang.Integer %s]]' % (dbName,dbServer,dbPort) ])
                              
   AdminTask.setResourceProperty(newDs,'[ -propertyName currentSchema -propertyValue GALADM]')
   AdminTask.setResourceProperty(newDs,'[ -propertyName resultSetHoldability -propertyValue 1]') 	

   #AdminConfig.save()
   sys.stdout.write('Done \n')

    
    

##
# P93 - setVMClasspath
##  

def setVMClasspath(server,classpath):
  sys.stdout.write('Set VM Classpath ...')
  jvm=AdminConfig.list('JavaVirtualMachine',server)
  sys.stdout.write(classpath)
  AdminConfig.modify(jvm,[['classpath',classpath]])
  #AdminConfig.save()
  sys.stdout.write('Done \n')
  
def configureCustomRegistry():
  
  #Setup custom registry attributes
  sys.stdout.write('Configure Custom Registry ...')
  adminId = ['primaryAdminId', 'GALCAdmin1']
  useRegistryServerId = ['useRegistryServerId', 'true']
  serverId = ['serverId', 'WASServer']
  serverPassword =['serverPassword','g2lc']
  registryClass =['customRegistryClassName','com.honda.global.galc.system.registry.GALCRegistry']
  properties=['properties',[[['name','PROPERTY_FILE'],['value','GALCRegistry.properties']]]]
  
  customRegAttrs= [adminId,useRegistryServerId,serverId,serverPassword,registryClass,properties]

  security = AdminConfig.getid('/Security:/')
  # Get the list of predefined userRegistries we need to find the customRegistry and change it
  userRegistries = AdminConfig.showAttribute(security,'userRegistries')
  userRegistriesArray = userRegistries[1:len(userRegistries)-1].split(' ')
  # Find Custom Registry
  for userRegistry in userRegistriesArray:
      if  userRegistry.find('CustomUserRegistry') > -1:
      	 #modify custom registry attributes for GALC
      	 AdminConfig.modify(userRegistry,customRegAttrs)
      	 customReg=userRegistry
  
  
  #set Standalone CustomRegistry as current and enable application security
  AdminConfig.modify(security,[['activeUserRegistry',customReg]])
  AdminConfig.modify(security,[['appEnabled','true']])
  
  sys.stdout.write('Done \n')


def getEndPointPorts(nodeName,serverName):
   httpsecureport = "0"
   httpport = "0"
   endpoints = AdminConfig.getid("/Node:%s/ServerIndex:/ServerEntry:%s/NamedEndPoint:/" % (nodeName,serverName)).splitlines()
   for nep in endpoints:
     epname = AdminConfig.showAttribute(nep,"endPointName")
     if (epname == "WC_defaulthost"):
        ep = AdminConfig.showAttribute(nep,"endPoint")
        httpport = AdminConfig.showAttribute(ep,"port")
     elif (epname == "WC_defaulthost_secure"):
        ep = AdminConfig.showAttribute(nep,"endPoint")
        httpsecureport = AdminConfig.showAttribute(ep,"port")     
     if (httpport != "0" and httpsecureport != "0"):
        break
   
   return (httpport,httpsecureport)
   
  
##
# Create Virtual Host
##  
def createVirtualHost(httpport="80",httpsport="443") :
  sys.stdout.write('Configure Virtual Host ...')
  parentId = AdminConfig.getid('/Cell:' + CELL + '/')
  AdminConfig.create('VirtualHost',parentId,'[[name "galchost"]]')
  parentId = AdminConfig.getid('/Cell:' + CELL + '/VirtualHost:galchost/')
  AdminConfig.create('HostAlias',parentId,'[[hostname "*"] [port "%s"]]' % httpport)
  AdminConfig.create('HostAlias',parentId,'[[hostname "*"] [port "%s"]]' % httpsport)
  sys.stdout.write('Done \n')


def setTransactionTimeouts(server):
   sys.stdout.write("Configuring transaction timeouts ...")
   txnservice = AdminConfig.list("TransactionService",server)
   
   AdminConfig.modify(txnservice, [ ["propogatedOrBMTTranLifetimeTimeout",1300], ["totalTranLifetimeTimeout",1200]])
   sys.stdout.write('Done \n')

def setLogRotation(server):
   sys.stdout.write("Configuring log rotation ...")
   errorStreamRedirect = AdminConfig.showAttribute(server,"errorStreamRedirect")
   outputStreamRedirect = AdminConfig.showAttribute(server,"outputStreamRedirect")
   attrs = [ ["baseHour",24] , ["rolloverPeriod",24], ["rolloverSize",3], ["rolloverType","TIME"] ]
   
   AdminConfig.modify(errorStreamRedirect,attrs)
   AdminConfig.modify(outputStreamRedirect,attrs)
   
   
   sys.stdout.write('Done \n')
##
# Main
##

def fixSlash(inStr):
  retval = None
  tokens = inStr.split("\\")
  for token in tokens:
    if retval == None:
      retval = token
    else:
      retval = "%s/%s" % (retval,token)
  
  return retval
  
  
dbName = "ENGDB"
dbServer = "dhma1db1"
dbPort = "60000"  

if (len(sys.argv) > 0):
  print "Parsing parameters"
  
  NODE=AdminControl.getNode()
  SERVER=AdminConfig.showAttribute(AdminTask.listServers(),"name")
  CELL=AdminControl.getCell()
  
  WAS_INSTALL_ROOT=fixSlash(AdminTask.showVariables("[-scope Cell=%s,Node=%s -variableName  WAS_INSTALL_ROOT ]" % (CELL,NODE)))
  PROFILE_ROOT=fixSlash(AdminTask.showVariables("[-scope Cell=%s,Node=%s -variableName  USER_INSTALL_ROOT ]" % (CELL,NODE)))
  
  GALC_REGISTRY_JAR_DIR="%s/lib/ext" % WAS_INSTALL_ROOT
  GALC_REGISTRY_PROPERTY_DIR="%s/properties" % PROFILE_ROOT
  
  WORKSPACE_ROOT=sys.argv[0]
  
  if (len(sys.argv) > 1):
    DB2_ROOT= fixSlash(sys.argv[1])
    if (DB2_ROOT.find(" ") >= 0):
      DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH="'%s/bin'" % DB2_ROOT
      DB2UNIVERSAL_JDBC_DRIVER_PATH="'%s/java'" % DB2_ROOT
    else:
      DB2UNIVERSAL_JDBC_DRIVER_NATIVEPATH="%s/bin" % DB2_ROOT
      DB2UNIVERSAL_JDBC_DRIVER_PATH="%s/java" % DB2_ROOT
  
  if (len(sys.argv) >= 5):
  	dbName = sys.argv[2]
  	dbServer = sys.argv[3]
  	dbPort = sys.argv[4]
      
   

sys.stdout.write('Start of main with server ' + SERVER + '\n')

checkDirectories()

server=AdminConfig.getid('/Server:' + SERVER)

sys.stdout.write('SERVER= ' + server + '\n')

turnOnOrbPassByReference(server)

setTransactionTimeouts(server)
	
jdbcProviderId =configureJDBCProvider()

configureGALCDataSource(jdbcProviderId,dbName,dbServer,dbPort)

ports = getEndPointPorts(NODE,SERVER)
createVirtualHost(ports[0],ports[1])

PROPERTY_FILE_DIR=GALC_REGISTRY_JAR_DIR + ";" + GALC_REGISTRY_PROPERTY_DIR 


classpath=PROPERTY_FILE_DIR + ';${DB2UNIVERSAL_JDBC_DRIVER_PATH}/db2jcc.jar;${DB2UNIVERSAL_JDBC_DRIVER_PATH}/db2jcc_license_cu.jar;'
setVMClasspath(server,classpath)

configureCustomRegistry()

AdminConfig.save()
sys.stdout.write('Completed Successfully\n')
