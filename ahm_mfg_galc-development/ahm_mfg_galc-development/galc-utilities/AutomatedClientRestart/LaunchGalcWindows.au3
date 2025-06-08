$JAVA_HOME = EnvGet("JAVA_HOME")
$GALC_ENV = EnvGet("GALC_ENV")

$HMIN_GALC_URL_PROD = "http://galchminline1naprod.hmin.am.honda.com/BaseWebStart/" & @ComputerName & ".host"
$HMIN_GALC_URL_QA = "http://galchminline1naqa.hmin.am.honda.com/BaseWebStart/" & @ComputerName & ".host"
$HMIN_GALC_URL_DEV = "http://galchminline1nadev.hmin.am.honda.com/BaseWebStart/" & @ComputerName & ".host"

$LAUNCH_PATH = ""

killAllJavaw()
killAllGalcScripts()
setSiteLaunchPath()
alwaysRunGALC()

;*************************************************************
;Look for all javaw.exe processes and close them
Func killAllJavaw()
   Local $aProcessList = ProcessList("javaw.exe")
   For $i = 1 To $aProcessList[0][0]
        ConsoleWrite("Closing: " & $aProcessList[$i][0] & @CRLF  & "PID: " & $aProcessList[$i][1] & @CRLF )
		ProcessClose($aProcessList[$i][0])
		Sleep(1)
   Next
EndFunc

;*************************************************************
;Look for all HAPM scripts except this one, and kill them
Func killAllGalcScripts()
   Local $AutoItList = ProcessList("GALC.exe")
   For $i = 1 To $AutoItList[0][0]
	  ;If not the currently running AutoIt Process ID
	  if $AutoItList[$i][1] <> @AutoItPID Then
		 ConsoleWrite("Closing: " & $AutoItList[$i][0] & @CRLF  & "PID: " & $AutoItList[$i][1] & @CRLF )
		 $Test = ProcessClose($AutoItList[$i][0])
	  EndIf
	  Sleep(1)
   Next
EndFunc

;*************************************************************
;Configure the site path based on PC asset name for each site
Func setSiteLaunchPath()
   ConsoleWrite("Setting Site Launch Path: ")
   ;Set for HMIN
   If StringInStr(@ComputerName, "vn") Then
	  If $GALC_ENV = "PROD" Then
		 $LAUNCH_PATH = $JAVA_HOME &"\javaws.exe" & " " & $HMIN_GALC_URL_PROD
	  ElseIf $GALC_ENV = "QA" Then
		 $LAUNCH_PATH = $JAVA_HOME &"\javaws.exe" & " " & $HMIN_GALC_URL_QA
	  ElseIf $GALC_ENV = "DEV" Then
		 $LAUNCH_PATH = $JAVA_HOME &"\javaws.exe" & " " & $HMIN_GALC_URL_DEV
	  Else
		 MsgBox(1, "Environment Not Set", "GALC_ENV variable not set." & @CRLF & "Please setup System Environment Variable")
	  EndIf
	  ConsoleWrite($LAUNCH_PATH & @CRLF)
   EndIf
EndFunc

;*************************************************************
; Continually Check if GALC client javaw.exe process is running
;If not, start it
Func alwaysRunGALC()
   If $LAUNCH_PATH <> "" Then
	  While True   
		 If Not ProcessExists("javaw.exe") Then
			Run($LAUNCH_PATH)
		 EndIf
		 Sleep(1000)
	  WEnd
   EndIf
EndFunc