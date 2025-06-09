title Cleanup
echo off
echo "cleaning up AM2 run time files"
date /T
time /T


rd /s /Q base.dir
 
del base.message
del base.print
del base.report
del base.exe
del base.err
del stderr.dat
del stdout.dat
del am2err.dat
del am2out.dat
del *.asy
del *.cel*

pause