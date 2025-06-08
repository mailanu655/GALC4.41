@echo on
d:
cd \devtools\TCPReflector
java -cp . TCPReflector -port %1 -server 10.204.17.143:%2 -verbose