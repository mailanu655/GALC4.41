<H1> SUMS - VIN BOM client - LcVinBomClient </H1>
=====================================================================
Angular Application.
------------------------------------------------------------------------------

<ul>
	<li>provides user interface to maintain, configure, and use SUMS VIN BOM application.</li>
</ul>

## 1. Setup
    ## 1.1 As Angular Project:
        pull the latest code and run '%project-home/src/main/js%' > npm install
    ## 1.2 As Spring boot Project:
        pull the latest code and run '%project-home%' > mvn clean install

## 2. Build
    ## 2.1 As Angular Project:
        For only GUI application build - Run '%project-home/src/main/js%' > ng build
    ## 2.2 As Spring boot Project:
        Not required - the above 1.2 has already taken care of this

## 3. Running the application in different environments environment
    ## 3.1 As Angular Project:
        Go to: '%project-home/src/main/js%' >
        Run one of the following (See below 3.1.1) based on which backend you want connect
        Then run http://localhost:4200 in chrome browser
    ## 3.2 As Spring boot Project:
        '%project-home/target%' > java -jar lc-vin-bom-client-0.0.1-SNAPSHOT.jar
        Then run http://localhost:8080 in chrome browser



## 3.1.1 
'%project-home%' > npm run start:localhost              ->  points to local backend. 
                                                            For this you must start java workspace in your local and point to 9086 port. if you are running your backend in another port, then please change the port in localhost.proxy.conf.json
OR
'%project-home%' > npm run start:hmaqal1                ->  points to HMA QA Line 1 backend 
OR
'%project-home%' > npm run start:hmaqal2                ->  points to HMA QA Line 2 backend 
OR
'%project-home%' > npm run start:pmcqa                  ->  points to PMC QA backend 
OR
'%project-home%' > npm run start:pmcprod                ->  points to PMC Production backend OR
OR
For more plants/enviroments please see package.json

