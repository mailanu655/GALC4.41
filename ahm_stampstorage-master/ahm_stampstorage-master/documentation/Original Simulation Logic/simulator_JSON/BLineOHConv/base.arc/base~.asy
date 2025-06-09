SYSTYPE Process
UNITS Feet Seconds
SYSDEF UtilByAvail off RefCheck on debugger on warningMessages on report standard
FLAGS
	System Inherit
	Text Invisible Red
	Resources Inherit
	Resource Names Red
	Queues Inherit
	Queue Names Red
	Queue Amounts Red
	Blocks Invisible Inherit
	Block Names Invisible Red
	Labels Red
PROCDEF UserId 1
STARTDATE 03/10/2011 06:30:00 END_STARTDATE
TYPE name LocationListList TYPEtype reference
	CTYPE "AMLocationList*"
	TYPE2STRING "AMLocationListToStr"
TYPE name IntegerList TYPEtype list Integer
	CTYPE "AM_IntegerList*"
	TYPE2STRING "AM_IntegerListToStr"
TYPE name Ptr2Socket TYPEtype reference
	CTYPE "struct Ptr2Socket*"
	TYPE2STRING "sock_GetName"
	STRING2TYPE "String2Sock"
TYPE name Ptr2JSON TYPEtype reference
	CTYPE "struct cJSON*"
	TYPE2STRING "cJSON_Print"
	STRING2TYPE "cJSON_Parse"
PROC name pInit 0 traf Infinite nextproc die
PROC name pStorage 0 traf Infinite nextproc die
PROC name pSTRunControl 0 traf Infinite nextproc die
PROC name P_StorageToBuffer 0 traf Infinite nextproc die
PROC name pEmptyCarrierReturn 0 traf Infinite nextproc die
PROC name pEmptyCarrierLaneSelect 0 traf Infinite nextproc die
PROC name P_StampingShift 0 traf Infinite nextproc die
PROC name P_WeldSched 2 traf Infinite nextproc die
PROC name P_SPO_Process 4 traf Infinite nextproc die
		traf 1 1
		traf 2 1
		traf 3 1
		traf 4 1
PROC name P_PartialRelease 4 traf Infinite nextproc die
PROC name P_SPO_Release 4 traf Infinite nextproc die
PROC name P_PartialStorage 4 traf Infinite nextproc die
PROC name P_Unload 4 traf Infinite nextproc die
PROC name P_LoadRBT 4 traf Infinite nextproc die
PROC name pWarmUp 0 traf Infinite nextproc die
PROC name P_ReleasePartial 60 traf Infinite nextproc die
PROC name P_ZoneExit 3 traf Infinite nextproc die
PROC name P_Legend 30 traf Infinite nextproc die
PROC name P_Time 0 traf Infinite nextproc die
PROC name P_WeldShift 2 traf Infinite nextproc die
PROC name P_Scrap 0 traf Infinite nextproc die
PROC name P_Empty 0 traf Infinite nextproc die
PROC name P_Downtime 5 traf Infinite nextproc die
PROC name pCarrierLoad 0 traf Infinite
PROC name pEmptyRecirc 0 traf Infinite
PROC name pNonSpoPartMgt 20 traf Infinite
PROC name pZoneMixCheck 0 traf Infinite
PROC name pEmptyCarrierManagement 0 traf Infinite
LDTYPE name L_Start 0
picpos begx -234.595223097113 begy -174.237847769029 endx -233.595223097113 endy -174.237847769029
 color 0 template Feet
310 17
0 0 0 1 1 none
1 1 1 1 1 0 0
end
		create con 0 Seconds stream stream_L_Start_1 First pInit 0 Limit 1
LDTYPE name L_BS2DR 0 dis
picpos begx -145.105498687664 begy -162.31842519685 begz 9.9409875328084 endx -144.105498687664 endy -162.31842519685 endz 9.9409875328084
 template Feet
700 17
2 2 0 1 1 OHC_Car_cell
1
700 17
2 2 0 1 1 BodySide_2DR
1
700 21
2 2 0 1 1 DoorRing
0  0.0750000029802322  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
11
310 17
2 2 0 1 1 Box_1
0.100000001490116 1.45000004768372 0.100000001490116 1.45000004768372 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_2
0.670000016689301  -2.6949999332428  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
5.44999980926514 0.100000001490116 5.44999980926514 0.100000001490116 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_3
-0.654999971389771  -4.44000005722046  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.200000002980232 2.57500004768372 0.200000002980232 2.57500004768372 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_4
-1.99000000953674  -3.79999995231628  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
2.25 0.100000001490116 2.25 0.100000001490116 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_5
-1.37000000476837  -1.3400000333786  0
0 0 0 -25
	1.00	1.00	1.00
3 0.100000001490116 3 0.100000001490116 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_6
0.00999999977648258  -4.9850001335144  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.887499988079071 1.22500002384186 0.887499988079071 1.22500002384186 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_7
-1.28999996185303  -5.48000001907349  0
0 0 0 50
	1.00	1.00	1.00
1.85000002384186 0.100000001490116 1.85000002384186 0.100000001490116 0.100000001490116 0 0
311 21
2 2 0 1 1 Trap_1
0.28999999165535  -5.42000007629395  0
0 0 0 -90
	1.00	1.00	1.00
0.75 0.75 0.100000001490116 0 -7.62959284639209e-009
311 21
2 2 0 1 1 Trap_2
0.28999999165535  -7.19000005722046  0
0 0 0 -90
	1.00	1.00	1.00
0.75 0.75 0.100000001490116 0 -7.62959284639209e-009
310 21
2 2 0 1 1 Box_8
-0.264999985694885  -6.3125  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
1.77499997615814 0.675000011920929 1.77499997615814 0.675000011920929 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_9
0.0625  -7.57000017166138  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.75 1.32500004768372 0.75 1.32500004768372 0.100000001490116 0 0
end
LDTYPE name L_invis 0 dis
picpos begx -685.955590551181 begy -19.8953953412074 endx -684.955590551181 endy -19.8953953412074
 template Feet
310 0
0.100000001490116 0.100000001490116 0.100000001490116 0.100000001490116 0.100000001490116 0 0
end
LDTYPE name L_BS4DR 0 dis
picpos begx -303.591522309711 begy -312.626089238845 endx -302.591522309711 endy -312.626089238845
 template Feet
700 17
2 2 0 1 1 OHC_Car_cell
1
700 17
2 2 0 1 1 BodySide_2DR
1
700 21
2 2 0 1 1 DoorRing
0  0.0750000029802322  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
10
310 17
2 2 0 1 1 Box_1
0.100000001490116 1.45000004768372 0.100000001490116 1.45000004768372 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_2
0.670000016689301  -3.25500011444092  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
6.6100001335144 0.100000001490116 6.6100001335144 0.100000001490116 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_3
-0.654999971389771  -3.80999994277954  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.200000002980232 2.57500004768372 0.200000002980232 2.57500004768372 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_4
-1.99000000953674  -4.23000001907349  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
3.19000005722046 0.100000001490116 3.19000005722046 0.100000001490116 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_5
-1.37000000476837  -1.3400000333786  0
0 0 0 -25
	1.00	1.00	1.00
3 0.100000001490116 3 0.100000001490116 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_6
0.370000004768372  -6.70499992370605  0
0 0 0 30
	1.00	1.00	1.00
0.100000001490116 0.769999980926514 0.100000001490116 0.769999980926514 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_7
-1.4099999666214  -6.3600001335144  0
0 0 0 45
	1.00	1.00	1.00
1.64999997615814 0.100000001490116 1.64999997615814 0.100000001490116 0.100000001490116 0 0
311 21
2 2 0 1 1 Trap_2
0.284999996423721  -8.1899995803833  0
0 0 0 -90
	1.00	1.00	1.00
0.75 0.75 0.100000001490116 0 -7.62959284639209e-009
310 21
2 2 0 1 1 Box_8
-0.395000010728836  -7.53249979019165  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
1.31500005722046 0.921249985694885 1.31500005722046 0.921249985694885 0.100000001490116 0 0
310 21
2 2 0 1 1 Box_9
-0.0684999972581863  -8.55000019073486  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.720000028610229 1.57000005245209 0.720000028610229 1.57000005245209 0.100000001490116 0 0
end
SSET name Press	single_state Production state_def 1 color 2	single_state Down state_def 0 color 1	single_state PlannedDT state_def 0 color 10	single_state NoScheduledRuns state_def 3 color 12	single_state NoCarriers state_def 0 color 5	single_state Weekend state_def 0 color -1	single_state DieChange state_def 0 color 15	single_state Blocked state_def 0 color 12
SSET name Weld	single_state Production state_def 1 color 2	single_state Starved state_def 0 color 3	single_state Down state_def 2 color 1	single_state PlannedDT state_def 0 color 10	single_state Changeover state_def 0 color 13	single_state Idle state_def 3 color 4
SSET name PressMonitor	single_state PlannedDT state_def 0 color -1	single_state OnePressRunningSpo state_def 0 color -1	single_state TwoPressRunningSpo state_def 0 color -1	single_state NoPressRunningSpo state_def 0 color -1
RSRC name rCLine 0 cap 1 prtime con 5 Seconds stream stream_rCLine_1
 dis 0 picpos begx -101.188320209974 begy -75.4852690288714 endx -100.188320209974 endy -75.4852690288714

	UserDef	template Feet
310 17
2 2 0 1 1 none
4 4 4 4 4 0 0
end
		
RSRC name R_SPO 4 cap 1 prtime con 5 Seconds stream stream_R_SPO_1
 dis 1 picpos begx -326.876010498688 begy -76.6572637795276 endx -325.876010498688 endy -76.6572637795276
 dis 2 picpos begx -326.746220472441 begy -81.4637992125984 endx -325.746220472441 endy -81.4637992125984
 dis 3 picpos begx -365.710446194226 begy -270.179002624672 endx -364.710446194226 endy -270.179002624672
 dis 4 picpos begx -365.439002624672 begy -276.692703412074 endx -364.439002624672 endy -276.692703412074

	UserDef	template Feet
310 17
-1 -1 0 1 1 none
4 4 4 4 4 0 0
end
		
RSRC name R_ReleasePartial 60 cap 1 prtime con 5 Seconds stream stream_R_ReleasePartial_1

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name R_ZoneExit 3 cap 1 prtime con 5 Seconds stream stream_R_ZoneExit_1
 dis 1 picpos begx -202.427139107612 begy -152.943293963255 endx -201.427139107612 endy -152.943293963255
 dis 2 picpos begx -301.3543832021 begy -161.492545931759 endx -300.3543832021 endy -161.492545931759
 dis 3 picpos begx -384.404409448819 begy -185.25282152231 endx -383.404409448819 endy -185.25282152231

	UserDef	template Feet
350 17
-1 -1 0 1 1 none
1.5 1 0 0
end
		
RSRC name R_SPO_Release 0 cap 1 prtime con 5 Seconds stream stream_R_SPO_Release_1

	UserDef
		
RSRC name R_Weld 2 cap 2147483647 prtime con 5 Seconds stream stream_R_Weld_1
 dis 1 picpos begx -571.769816272966 begy -81.8931692913386 endx -570.769816272966 endy -81.8931692913386
 dis 2 picpos begx -606.007979002625 begy -191.383766404199 endx -605.007979002625 endy -191.383766404199

	UserDef	template Feet
310 17
-1 -1 0 1 1 none
4 4 4 4 4 0 0
end
		
RSRC name R_Line 2 cap 1 prtime con 5 Seconds stream stream_R_Line_1

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name R_WeldComplete 4 cap 2147483647 prtime con 5 Seconds stream stream_R_WeldComplete_1

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name R_LineStarved 2 cap 2147483647 prtime con 5 Seconds stream stream_R_LineStarved_1

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name rBLine 0 cap 1 prtime con 5 Seconds stream stream_rBLine_1
 dis 0 picpos begx 232.261679790026 begy -203.886509186352 endx 233.261679790026 endy -203.886509186352

	UserDef	template Feet
700 17
2 2 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name R_Dummy 5 cap 1 prtime con 5 Seconds stream stream_R_Dummy_1

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name R_WeldShift 2 cap 2147483647 prtime con 5 Seconds stream stream_R_WeldShift_1

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 0
4 4 4 4 4 0 0
end
		
RSRC name rStampingShift 0 cap 2147483647 prtime con 5 Seconds stream stream_rStampingShift_1

	UserDef
		
RSRC name rST13Inspector 0 cap 1 prtime con 5 Seconds stream stream_rST13Inspector_1

	UserDef
		
RSRC name rBLineTravel 0 cap 1 prtime con 5 Seconds stream stream_rBLineTravel_1

	UserDef
		
SMON name SM_R_Weld 0 SSET Weld RSRC R_Weld
SMON name SM_rBLine 0 SSET Press RSRC rBLine
SMON name SM_rCLine 0 SSET Press RSRC rCLine
SMON name SM_R_LineStarved 0 SSET Weld RSRC R_LineStarved
SMON name vsmPressCondition 0 SSET PressMonitor
QUEUE name qTemp 0 cap 2147483647

	dis 0 Stacking PLUSZ
		picpos begx -108.140551181102 begy -99.9416994750656 endx -107.140551181102 endy -99.9416994750656 scx 0.00999999977648258 scy 0.00999999977648258 scz 0.00999999977648258

	UserDef	template Feet
310 17
2 2 0 1 0 none
4 4 4 4 4 0 0
end

QUEUE name qVehOverflow 0 cap 2147483647

	UserDef

QUEUE name qTransferTemp 2 cap 2147483647

	UserDef	template Feet
700 17
-1 -1 0 1 1 none
1
310 17
-1 -1 0 1 0 none
4 4 4 4 4 0 0
end

QUEUE name Q_Legend 0 cap 30

	dis 0 Stacking OTT_LDDISP
		picpos begx -396.861994750656 begy -338.220997375328 endx -395.861994750656 endy -338.220997375328

	UserDef	template Feet
700 17
2 2 0 1 0 set
1
700 17
2 2 0 1 0 none
31
310 17
2 2 0 1 0 none
0.100000001490116 0.100000001490116 0.100000001490116 0.100000001490116 0.100000001490116 0 0
700 51
2 2 0 1 1 TCF1
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  0  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF2
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -10  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF3
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -20  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF4
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -30  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF5
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -40  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF6
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -50  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF7
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -60  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF8
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -70  0
0 0 0 0
1  1  1
0
700 51
2 2 0 1 1 TCF9
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -80  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF10
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -90  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF11
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -100  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF12
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -110  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF13
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -120  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF14
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -130  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF15
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -140  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF16
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -150  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF17
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -160  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF18
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -170  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF19
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -180  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF20
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -190  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF21
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  0  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF22
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -10  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF23
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -20  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF24
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -30  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF25
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -40  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF26
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -50  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF27
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -60  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF28
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -70  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF29
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -80  0
0 0 0 0
1  1  1
0
700 19
2 2 0 1 1 TCF30
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
-240  -90  0
0 0 0 0
1  1  1
0
end

	CONTAINER name TCF1 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 2 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF2 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 3 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF3 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 4 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF4 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 5 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF5 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 6 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF6 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 7 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF7 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 8 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF8 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 9 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF9 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 10 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF10 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 11 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF11 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 12 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF12 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 13 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF13 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 14 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF14 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 15 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF15 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 16 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF16 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 17 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF17 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 18 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF18 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 19 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF19 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 20 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF20 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 21 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF21 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 22 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF22 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 23 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF23 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 24 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF24 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 25 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF25 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 26 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF26 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 27 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF27 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 28 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF28 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 29 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF29 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 30 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
	CONTAINER name TCF30 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 31 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
QUEUE name Q_SPO 4 cap 2

	dis 1 Stacking OTT_LDDISP
		picpos begx -327.023832020997 begy -76.748031496063 begz 4 endx -327.023832008829 endy -75.748031496063 endz 4

	dis 2 Stacking OTT_LDDISP
		picpos begx -326.72094488189 begy -81.593937007874 begz 4 endx -326.720944869722 endy -80.593937007874 endz 4

	dis 3 Stacking OTT_LDDISP
		picpos begx -365.759265091864 begy -270.354750656168 begz 4 endx -365.759265079696 endy -269.354750656168 endz 4

	dis 4 Stacking OTT_LDDISP
		picpos begx -365.370078740158 begy -276.679475065617 begz 4 endx -365.370078727988 endy -275.679475065617 endz 4

	UserDef	template Feet
310 17
-1 -1 0 1 0 none
0.100000001490116 0.100000001490116 0.100000001490116 0.100000001490116 0.100000001490116 0 0
end

LABEL name Lb_StampShiftStatus 0
 dis 0 picpos begx -20 begy -120 endx -19 endy -120 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Production Status
end
LABEL name Lb_SPORelease 4
 dis 1 picpos begx -572.981994750656 begy -132.493556430446 endx -571.981994750656 endy -132.493556430446 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 2 picpos begx -572.981679790026 begy -142.493556430446 endx -571.981679790026 endy -142.493556430446 scx 5 scy 5 scz 5

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 3 picpos begx -603.981679790026 begy -235.493543307087 endx -602.981679790026 endy -235.493543307087 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Label
end
 dis 4 picpos begx -603.981679790026 begy -245.493543307087 endx -602.981679790026 endy -245.493543307087 scx 5 scy 5 scz 5

	UserDef color 5	template Feet
140 49
5 5 0 1 1 Label
Label
end
LABEL name Lb_Legend 30
 dis 1 picpos begx -390 begy -340 endx -389 endy -340 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Acc_4Dr_LH
end
 dis 2 picpos begx -390 begy -350 endx -389 endy -350 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 3 picpos begx -390 begy -360 endx -389 endy -360 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 4 picpos begx -390 begy -370 endx -389 endy -370 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 5 picpos begx -390 begy -380 endx -389 endy -380 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 6 picpos begx -390 begy -390 endx -389 endy -390 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 7 picpos begx -390 begy -400 endx -389 endy -400 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 8 picpos begx -390 begy -410 endx -389 endy -410 scx 5 scy 5 scz 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
Legend
end
 dis 9 picpos begx -390 begy -420 endx -389 endy -420 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 10 picpos begx -390 begy -430 endx -389 endy -430 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 11 picpos begx -390 begy -440 endx -389 endy -440 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 12 picpos begx -390 begy -450 endx -389 endy -450 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 13 picpos begx -390 begy -460 endx -389 endy -460 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 14 picpos begx -390 begy -470 endx -389 endy -470 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 15 picpos begx -390 begy -480 endx -389 endy -480 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 16 picpos begx -390 begy -490 endx -389 endy -490 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 17 picpos begx -390 begy -500 endx -389 endy -500 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 18 picpos begx -390 begy -510 endx -389 endy -510 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 19 picpos begx -390 begy -520 endx -389 endy -520 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 20 picpos begx -390 begy -530 endx -389 endy -530 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 21 picpos begx -630 begy -340 endx -629 endy -340 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Acc_4Dr_RH_Test
end
 dis 22 picpos begx -630 begy -350 endx -629 endy -350 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 23 picpos begx -630 begy -360 endx -629 endy -360 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 24 picpos begx -630 begy -370 endx -629 endy -370 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 25 picpos begx -630 begy -380 endx -629 endy -380 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 26 picpos begx -630 begy -390 endx -629 endy -390 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 27 picpos begx -630 begy -400 endx -629 endy -400 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 28 picpos begx -630 begy -410 endx -629 endy -410 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 29 picpos begx -630 begy -420 endx -629 endy -420 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 30 picpos begx -630 begy -430 endx -629 endy -430 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name Lb_Inventory 30
 dis 1 picpos begx -340 begy -340 endx -339 endy -340 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
TriqQty-> 600 RunQty-> 1800 Inv-> 1344
end
 dis 2 picpos begx -340 begy -350 endx -339 endy -350 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 3 picpos begx -340 begy -360 endx -339 endy -360 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 4 picpos begx -340 begy -370 endx -339 endy -370 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 5 picpos begx -340 begy -380 endx -339 endy -380 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 6 picpos begx -340 begy -390 endx -339 endy -390 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 7 picpos begx -340 begy -400 endx -339 endy -400 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 8 picpos begx -340 begy -410 endx -339 endy -410 scx 5 scy 5 scz 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Inventory
end
 dis 9 picpos begx -340 begy -420 endx -339 endy -420 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 10 picpos begx -340 begy -430 endx -339 endy -430 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 11 picpos begx -340 begy -440 endx -339 endy -440 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
TriqQty-> 600 RunQty-> 1800 Inv-> 1344
end
 dis 12 picpos begx -340 begy -450 endx -339 endy -450 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 13 picpos begx -340 begy -460 endx -339 endy -460 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 14 picpos begx -340 begy -470 endx -339 endy -470 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 15 picpos begx -340 begy -480 endx -339 endy -480 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 16 picpos begx -340 begy -490 endx -339 endy -490 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 17 picpos begx -340 begy -500 endx -339 endy -500 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 18 picpos begx -340 begy -510 endx -339 endy -510 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 19 picpos begx -340 begy -520 endx -339 endy -520 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 20 picpos begx -340 begy -530 endx -339 endy -530 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 21 picpos begx -560 begy -340 endx -559 endy -340 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
TriqQty-> 600 RunQty-> 1800 Inv-> 1344
end
 dis 22 picpos begx -560 begy -350 endx -559 endy -350 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 23 picpos begx -560 begy -360 endx -559 endy -360 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 24 picpos begx -560 begy -370 endx -559 endy -370 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 25 picpos begx -560 begy -380 endx -559 endy -380 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 26 picpos begx -560 begy -390 endx -559 endy -390 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 27 picpos begx -560 begy -400 endx -559 endy -400 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 28 picpos begx -560 begy -410 endx -559 endy -410 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 29 picpos begx -560 begy -420 endx -559 endy -420 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 30 picpos begx -560 begy -430 endx -559 endy -430 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name Lb_SPO 4
 dis 1 picpos begx -575 begy -104 endx -574 endy -104 scx 5 scy 5

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
SPO
end
 dis 2 picpos begx -575 begy -114 endx -574 endy -114 scx 5 scy 5

	UserDef color 13	template Feet
140 49
13 13 0 1 1 Label
SPO
end
 dis 3 picpos begx -606 begy -211 endx -605 endy -211 scx 5 scy 5

	UserDef color 10	template Feet
140 49
10 10 0 1 1 Label
SPO
end
 dis 4 picpos begx -606 begy -221 endx -605 endy -221 scx 5 scy 5

	UserDef color 5	template Feet
140 49
5 5 0 1 1 Label
SPO
end
LABEL name Lb_ZoneExit 3
 dis 1 picpos begx -224.855065616798 begy -153.609435695538 endx -223.855065616798 endy -153.609435695538

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 2 picpos begx -298.578608923885 begy -161.159448818898 endx -297.578608923885 endy -161.159448818898

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 3 picpos begx -391.843385826772 begy -188.583713910761 endx -390.843385826772 endy -188.583713910761

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name Lb_WeldShiftStatus 2
	title "¦"
 dis 1 picpos begx -604.52845144357 begy -165.014829396325 endx -603.52845144357 endy -165.014829396325 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 2 picpos begx -610.145984251969 begy -267.909002624672 endx -609.145984251969 endy -267.909002624672 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
LABEL name lblScrap 2
 dis 1 picpos begx 160 begy -80 endx 161 endy -80 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 2 picpos begx 60 begy -80 endx 61 endy -80 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name Lb_WeldCycle 4
 dis 1 picpos begx -635 begy 4 endx -634 endy 4 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 2 picpos begx -635 begy -6 endx -634 endy -6 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 3 picpos begx -635 begy -16 endx -634 endy -16 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 4 picpos begx -635 begy -26 endx -634 endy -26 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name Lb_Empty 0
LABEL name Lb_Inv 50
	title "+"
 dis 1 picpos begx -90 begy -340 endx -89 endy -340 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
TrigQty-> 600 RunQty-> 1800 Inv-> 2000
end
 dis 2 picpos begx -90 begy -350 endx -89 endy -350 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 3 picpos begx -90 begy -360 endx -89 endy -360 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 4 picpos begx -90 begy -370 endx -89 endy -370 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 5 picpos begx -90 begy -380 endx -89 endy -380 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 6 picpos begx -90 begy -390 endx -89 endy -390 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 7 picpos begx -90 begy -400 endx -89 endy -400 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 8 picpos begx -90 begy -410 endx -89 endy -410 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Text
end
 dis 9 picpos begx -90 begy -420 endx -89 endy -420 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 10 picpos begx -90 begy -430 endx -89 endy -430 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 11 picpos begx -90 begy -440 endx -89 endy -440 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 12 picpos begx -90 begy -450 endx -89 endy -450 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 13 picpos begx -90 begy -460 endx -89 endy -460 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 14 picpos begx -90 begy -470 endx -89 endy -470 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 15 picpos begx -90 begy -480 endx -89 endy -480 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 16 picpos begx -90 begy -490 endx -89 endy -490 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 17 picpos begx -90 begy -500 endx -89 endy -500 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 18 picpos begx -90 begy -510 endx -89 endy -510 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 19 picpos begx -90 begy -520 endx -89 endy -520 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 20 picpos begx -90 begy -530 endx -89 endy -530 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 21 picpos begx 160 begy -340 endx 161 endy -340 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 22 picpos begx 160 begy -350 endx 161 endy -350 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 23 picpos begx 160 begy -360 endx 161 endy -360 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 24 picpos begx 160 begy -370 endx 161 endy -370 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 25 picpos begx 160 begy -380 endx 161 endy -380 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 26 picpos begx 160 begy -390 endx 161 endy -390 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 27 picpos begx 160 begy -400 endx 161 endy -400 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 28 picpos begx 160 begy -410 endx 161 endy -410 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 29 picpos begx 160 begy -420 endx 161 endy -420 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 30 picpos begx 160 begy -430 endx 161 endy -430 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 31 picpos begx 160 begy -440 endx 161 endy -440 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 32 picpos begx 160 begy -450 endx 161 endy -450 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 33 picpos begx 160 begy -460 endx 161 endy -460 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 34 picpos begx 160 begy -470 endx 161 endy -470 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 35 picpos begx 160 begy -480 endx 161 endy -480 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 36 picpos begx 160 begy -490 endx 161 endy -490 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 37 picpos begx 160 begy -500 endx 161 endy -500 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 38 picpos begx 160 begy -510 endx 161 endy -510 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 39 picpos begx 160 begy -520 endx 161 endy -520 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 40 picpos begx 160 begy -530 endx 161 endy -530 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 41 picpos begx -90 begy -540 endx -89 endy -540 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 42 picpos begx -90 begy -550 endx -89 endy -550 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 43 picpos begx -90 begy -560 endx -89 endy -560 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 44 picpos begx -90 begy -570 endx -89 endy -570 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 45 picpos begx -90 begy -580 endx -89 endy -580 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 46 picpos begx 160 begy -540 endx 161 endy -540 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 47 picpos begx 160 begy -550 endx 161 endy -550 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 48 picpos begx 160 begy -560 endx 161 endy -560 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 49 picpos begx 160 begy -570 endx 161 endy -570 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 50 picpos begx 160 begy -580 endx 161 endy -580 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name Lb_PartNames 50
 dis 1 picpos begx -170 begy -340 endx -169 endy -340 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Acc_4Dr_FrtDrPanel
end
 dis 2 picpos begx -170 begy -350 endx -169 endy -350 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 3 picpos begx -170 begy -360 endx -169 endy -360 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 4 picpos begx -170.540997375328 begy -370 endx -169.540997375328 endy -370 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 5 picpos begx -170 begy -380 endx -169 endy -380 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 6 picpos begx -170 begy -390 endx -169 endy -390 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 7 picpos begx -170 begy -400 endx -169 endy -400 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 8 picpos begx -170 begy -410 endx -169 endy -410 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 9 picpos begx -170 begy -420 endx -169 endy -420 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 10 picpos begx -170 begy -430 endx -169 endy -430 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 11 picpos begx -170 begy -440 endx -169 endy -440 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 12 picpos begx -170 begy -450 endx -169 endy -450 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 13 picpos begx -170 begy -460 endx -169 endy -460 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 14 picpos begx -170 begy -470 endx -169 endy -470 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 15 picpos begx -170 begy -480 endx -169 endy -480 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 16 picpos begx -170 begy -490 endx -169 endy -490 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 17 picpos begx -170 begy -500 endx -169 endy -500 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 18 picpos begx -170 begy -510 endx -169 endy -510 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 19 picpos begx -170 begy -520 endx -169 endy -520 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 20 picpos begx -170 begy -530 endx -169 endy -530 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Label
end
 dis 21 picpos begx 70 begy -340 endx 71 endy -340 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Acc_4Dr_FrtDrPanel
end
 dis 22 picpos begx 70 begy -350 endx 71 endy -350 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
ZQ_RDX_FRTDRSKIN
end
 dis 23 picpos begx 70 begy -360 endx 71 endy -360 scx 5 scy 5 scz 5

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
ZQ_RDX_REARDRSKIN
end
 dis 24 picpos begx 70 begy -370 endx 71 endy -370 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 25 picpos begx 70 begy -380 endx 71 endy -380 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 26 picpos begx 70 begy -390 endx 71 endy -390 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 27 picpos begx 70 begy -400 endx 71 endy -400 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 28 picpos begx 70 begy -410 endx 71 endy -410 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 29 picpos begx 70 begy -420 endx 71 endy -420 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 30 picpos begx 70 begy -430 endx 71 endy -430 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 31 picpos begx 70 begy -440 endx 71 endy -440 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 32 picpos begx 70 begy -450 endx 71 endy -450 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 33 picpos begx 70 begy -460 endx 71 endy -460 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 34 picpos begx 70 begy -470 endx 71 endy -470 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 35 picpos begx 70 begy -480 endx 71 endy -480 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 36 picpos begx 70 begy -490 endx 71 endy -490 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 37 picpos begx 70 begy -500 endx 71 endy -500 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 38 picpos begx 70 begy -510 endx 71 endy -510 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 39 picpos begx 70 begy -520 endx 71 endy -520 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 40 picpos begx 70 begy -530 endx 71 endy -530 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 41 picpos begx -170 begy -540 endx -169 endy -540 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 42 picpos begx -170 begy -550 endx -169 endy -550 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 43 picpos begx -170 begy -560 endx -169 endy -560 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 44 picpos begx -170 begy -570 endx -169 endy -570 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 45 picpos begx -170 begy -580 endx -169 endy -580 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 46 picpos begx 70 begy -540 endx 71 endy -540 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 47 picpos begx 70 begy -550 endx 71 endy -550 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 48 picpos begx 70 begy -560 endx 71 endy -560 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 49 picpos begx 70 begy -570 endx 71 endy -570 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
 dis 50 picpos begx 70 begy -580 endx 71 endy -580 scx 5 scy 5 scz 5

	UserDef	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name lblState 4
 dis 3 picpos begx 160 begy -100 endx 161 endy -100 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 4 picpos begx 60 begy -100 endx 61 endy -100 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name lbTitles 11
 dis 1 picpos begx -570.877007874016 begy -325 endx -569.877007874016 endy -325 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
SPO Variations
end
 dis 2 picpos begx -339.65 begy -325 endx -338.65 endy -325 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Side Panels
end
 dis 3 picpos begx 25.8502395013123 begy -325 endx 26.8502395013123 endy -325 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Other Parts
end
 dis 4 picpos begx 60 begy 20 endx 61 endy 20 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
C Line
end
 dis 5 picpos begx 160 begy 20 endx 161 endy 20 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
B Line
end
 dis 6 picpos begx -20 endx -19 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Part
end
 dis 7 picpos begx -20 begy -20 endx -19 endy -20 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Shot Size
end
 dis 8 picpos begx -20 begy -40 endx -19 endy -40 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Current
end
 dis 9 picpos begx -20 begy -60 endx -19 endy -60 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Cycle Time
end
 dis 10 picpos begx -20 begy -80 endx -19 endy -80 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
Scrap
end
 dis 11 picpos begx -20 begy -100 endx -19 endy -100 scx 7 scy 7 scz 7

	UserDef color 0	template Feet
140 49
0 0 0 1 1 Label
State
end
LABEL name lblShotSize 2
 dis 1 picpos begx 160 begy -20 endx 161 endy -20 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 2 picpos begx 60 begy -20 endx 61 endy -20 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name lblPart 2
 dis 1 picpos begx 160 endx 161 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 2 picpos begx 60 endx 61 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name lblCurrent 2
 dis 1 picpos begx 160 begy -40 endx 161 endy -40 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 2 picpos begx 60 begy -40 endx 61 endy -40 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
LABEL name lblCycleTime 2
 dis 1 picpos begx 160 begy -60 endx 161 endy -60 scx 7 scy 7 scz 7

	UserDef color 4	template Feet
140 49
4 4 0 1 1 Label
Label
end
 dis 2 picpos begx 60 begy -60 endx 61 endy -60 scx 7 scy 7 scz 7

	UserDef color 1	template Feet
140 49
1 1 0 1 1 Label
Label
end
ORDER name olVehOverflow 0
ORDER name olEmptyHold 0
ORDER name O_Type 30 primkey LDATT Ai_LotNo 0 0 0 0
ORDER name olCarKickout 0
ORDER name olWarmUp 0
ORDER name olRobHandshake 2
ORDER name olLoadEntry 0
ORDER name olSpaceAvail 0
ORDER name O_SPO_ReleaseComplete 4
ORDER name O_PartialRelease 4
ORDER name O_ST13_SPO_Seq 4
ORDER name O_BufferSpace 4
ORDER name O_Starve 30
ORDER name O_WaitForPartialRelease 4
ORDER name O_PartialBuffer 8
ORDER name O_RBT 12
ORDER name O_SPO 12
ORDER name O_ReleasePartial 60
ORDER name O_WeldBuffer 4
ORDER name O_ZoneExit 3 primkey LDATT Ai_Type 0 0 0 0
ORDER name O_ZoneExitClear 3
ORDER name O_WE2BufferLH 0
ORDER name O_WE2BufferRH 0
ORDER name O_PrevClear 3
ORDER name O_WeldBufferStarve 4
ORDER name O_Temp 0
ORDER name olLine1Empty 0
ORDER name O_RowSeq 4
ORDER name O_Release 4
ORDER name olPressSide 4
ORDER name O_PrevZoneExitClear 4
ORDER name O_KickOut 0
ORDER name O_Synch 2
ORDER name O_ReleaseSynch 2
ORDER name O_StampingPriority 2 primkey priority
ORDER name O_NonSPOInv 4
ORDER name O_Complete 4
ORDER name O_Starved 0
ORDER name O_Weld1Order 0
ORDER name O_Weld2Order 2
ORDER name O_L1Weld 2
ORDER name O_Line1Start 0
ORDER name O_Idle 0
ORDER name olPartKill 8
ORDER name O_VehicleStart 0
ORDER name O_Decline 0
ORDER name O_EmptyLoop 0
ORDER name O_HoldForCarriers 0
ORDER name O_WeldOutputTarget 0
ORDER name oPressSelect 0
ORDER name oCarrierInit 2
ORDER name oLiftClear 0
ORDER name oEmptyRecirc 0
ORDER name oStorageInit 0
ORDER name oZoneMixRecircComplete 0
ORDER name oWeld2Changeover 0
ORDER name olSta8_1Incline 0
ORDER name oBPressExit 0
ORDER name oHold 0
ORDER name oZoneMixCheck 0
ORDER name O_ReleaseSeq 4
ORDER name oWeldChangeover 2
ORDER name oEmptyCarrierSpace 0 primkey LDATT Ai_y 0 0 0 0
ORDER name oST_B27 0
ORDER name oST5_12 0 primkey LDATT Ai_z 0 0 0 0
ORDER name oST5_13 0
ORDER name oEmptyCarrierManagement 0
ORDER name oST13Inspect 4
ORDER name oEmptyBufferCleanUp 0
COUNT name C_Error 0 cap Infinite
COUNT name C_EmptyFlush 0 cap 20
COUNT name C_SPOControl 30 cap Infinite
COUNT name C_L1Weld 2 cap Infinite
COUNT name C_CarrierCount 2 cap Infinite
COUNT name C_RunControl 50 cap 1
COUNT name C_ZoneMix 2 cap Infinite
TABLE name T_Output 4 bins 1 0 1
TABLE name T_CarWait 2 bins 1 0 1
TABLE name T_Changeover 2 bins 1 0 1
TABLE name T_MTBF 5 bins 1 0 1
TABLE name T_MTTR 5 bins 1 0 1
TABLE name T_WeldStarve 2 bins 1 0 1
TABLE name T_WeldGap 4 bins 1 0 1
TABLE name tRecircCount 0 bins 1 0 1
TABLE name tABayCleanUp 0 bins 1 0 1
ATT name Ai_pi 0 type Integer
ATT name Ai_CarQty 0 type Integer
ATT name As_Type 0 type String
ATT name Ai_Type 0 type Integer
ATT name Ai_Seq 0 type Integer
ATT name Ai_LotQty 0 type Integer
ATT name At_TimeInSys 0 type Time
ATT name Ai_Status 0 type Integer
ATT name Ai_Zone 0 type Integer
ATT name Ai_Row 0 type Integer
ATT name Ai_Line 0 type Integer
ATT name Ai_Side 0 type Integer
ATT name Aloc_Destination 0 type Location
ATT name Ai_x 0 type Integer
ATT name Ai_StampingPriority 0 type Integer
ATT name Ai_NonSPO 0 type Integer
ATT name Ai_ID 0 type Integer
ATT name Ai_y 0 type Integer
ATT name Ai_z 0 type Integer
ATT name Ai_Press 0 type Integer
ATT name Ai_LotNo 0 type Integer
ATT name Ai_Type2 0 type Integer
ATT name As_NextLocation 0 type String
ATT name As_CurrentLocation 0 type String
ATT name As_Message 0 type String
VAR name vflptrInputData 0 type FilePtr
VAR name Vs_junk 0 type String
VAR name viIndex 0 type Integer
VAR name vsStyle 1 30 type String
VAR name viInitQty 1 30 type Integer
VAR name viTriggerQty 1 30 type Integer
VAR name viSTRunOutQty 1 30 type Integer
VAR name vrStyleCT 3 30 2 2 type Real
VAR name viZonePref 2 3 30 type Integer
VAR name viCarrierQty 1 30 type Integer
VAR name Vi_NumType 0 type Integer
VAR name viInitRunQty 1 30 type Integer
VAR name vlt_LoadType 1 30 type LoadTypePtr
VAR name vclr_LoadColor 1 30 type Color
VAR name viNumVeh 0 type Integer
VAR name viReqVeh 0 type Integer
VAR name vlocptrLane 2 3 20 type Location
VAR name viZoneLaneMax 1 3 type Integer
VAR name vlocptrSTLoad 3 2 4 2 type Location
VAR name vlptrLoadGraphic 2 2 2 type LoadPtr
VAR name viPartCount 2 2 2 type Integer
VAR name volPartKill 3 2 2 2 type OrderListPtr
VAR name viTotalStorageParts 1 30 type Integer
VAR name vrDieChangeTime 1 2 type Real
VAR name viNumZones 0 type Integer
VAR name vldlstLanes 2 3 20 type LoadList
VAR name vlptrEmpty 0 type LoadPtr
VAR name Vldlst_Type 1 30 type LoadList
VAR name viSTRunGen 1 2 type Integer
VAR name viKickOutQty 1 3 type Integer
VAR name Vs_StmpShiftElement 2 3 10 type String
VAR name Vi_TotNumShiftEle 1 3 type Integer
VAR name Vs_WeldShiftElement 3 2 11 2 type String
VAR name Vi_TotNumWeldShiftEle 2 2 2 type Integer
VAR name Vflptr_WeldSched 1 2 type FilePtr
VAR name Vi_PrevType 1 4 type Integer
VAR name Vldptr_Unload 1 4 type LoadPtr
VAR name Vldlst_Partial 2 4 30 type LoadList
VAR name Vo_SPO 2 4 3 type OrderListPtr
VAR name Vr_SPOCycleTime 1 4 type Real
VAR name Vi_ProdCount 1 4 type Integer
VAR name Vld_SPO_Release 1 4 type LoadPtr
VAR name Vintlst_BufferSpace 1 4 type IntegerList
VAR name Vld_Release 1 4 type LoadPtr
VAR name Vld_ReleasePrev 1 4 type LoadPtr
VAR name Vloc_Partial 2 4 2 type Location
VAR name Vo_PartialBuffer 2 4 2 type OrderListPtr
VAR name Vloc_Unload 1 4 type Location
VAR name Vi_PartialIndex 1 4 type Integer
VAR name Vldptr_Partial 1 4 type LoadPtr
VAR name Vloc_LoadRBT 2 4 7 type Location
VAR name Vo_RBT 2 4 3 type OrderListPtr
VAR name Vldlst_ReleasePartial 2 2 30 type LoadList
VAR name Vi_SPO_QtyRelease 1 4 type Integer
	title "Set by the Weld Buffer when reaches trigger point"
VAR name Vi_CheckQty 1 30 type Integer
	title "For checking the quantity on hand Vs requirement"
VAR name Vi_ReleasePartial 2 2 30 type Integer
	title "Partial Qtys Released extra @ RELEASE"
VAR name Vo_ReleasePartial 2 2 30 type OrderListPtr
VAR name Vrsrc_ReleasePartial 2 2 30 type ResourcePtr
VAR name Vproc_ReleasePartial 2 2 30 type ProcessPtr
VAR name Vi_WEBufferOrderTracker 1 4 type Integer
VAR name Vi_Temp 0 type Integer
VAR name Vloc_B4Unload 1 4 type Location
VAR name Vld_OL 1 4 type LoadPtr
VAR name Vi_LotQty 1 4 type Integer
VAR name Vldlst_PartLane 2 4 2 type LoadList
VAR name viEmptyCarFlushQty 2 3 20 type Integer
VAR name Vi_PrevZone 1 4 type Integer
VAR name Vldlst_ZoneExit 1 3 type LoadList
VAR name Vld_ZoneExit 1 4 type LoadPtr
VAR name Vi_shiftProd 1 4 type Integer
VAR name Vi_StorePrevType 0 type Integer
VAR name Vs_SimStart 0 type String
VAR name Vi_SortZone 0 type Integer
VAR name Vi_SortRow 0 type Integer
VAR name Vld_Sort 0 type LoadPtr
VAR name Vi_Seq 0 type Integer
VAR name Vld_SortMarker 0 type LoadPtr
VAR name Vi_ZoneEmptyFlush 0 type Integer
VAR name Vr_MTBF 1 5 type Real
VAR name Vr_MTTR 1 5 type Real
VAR name Vs_Downtime 1 5 type String
VAR name Vs_InclDT 1 5 type String
VAR name Vstrm_DT 1 10 type StreamPtr
VAR name Vr_MTTRActual 1 5 type Real
VAR name Vr_MTBFActual 1 5 type Real
VAR name Vrsc_DT 1 6 type ResourcePtr
VAR name Vi_StampingPriority 1 20 type Integer
VAR name Vi_BayCapacity 1 3 type Integer
VAR name viInitQty2 1 50 type Integer
VAR name viTriggerQty2 1 50 type Integer
VAR name viSTRunOutQty2 1 50 type Integer
VAR name Vi_StampingPriority2 1 50 type Integer
VAR name vrStyleCT2 2 50 2 type Real
VAR name vsStyle2 1 50 type String
VAR name Vr_PartCount 2 2 2 type Real
VAR name Vi_EmptyZone 1 3 type Integer
VAR name Vi_ZoneMix 2 3 20 type Integer
VAR name Vi_ZoneCapacity 1 2 type Integer
VAR name Vi_Release 0 type Integer
VAR name Vi_TypeHolder 0 type Integer
VAR name Vld_ZoneTakeDown 1 3 type LoadPtr
VAR name Vld_StampingPriority 0 type LoadPtr
VAR name Vld_SPO 1 4 type LoadPtr
VAR name Vi_SPO 1 4 type Integer
VAR name Vld_ReleaseCount 1 4 type LoadPtr
VAR name Vs_str 0 type String
VAR name Vs_arrstr 1 2 type String
VAR name Vld_Sort2 0 type LoadPtr
VAR name Vldlst_Release 1 4 type LoadList
VAR name Vld_Recirc 1 2 type LoadPtr
VAR name Vld_Lot 1 4 type LoadPtr
VAR name Vi_Recirc 1 2 type Integer
VAR name Vi_RunTrigger 1 2 type Integer
VAR name Vi_x 0 type Integer
VAR name Vr_WeldCycleTime 1 4 type Real
VAR name Vi_TypeCheck 0 type Integer
VAR name Vi_LoopCount 0 type Integer
VAR name Vld_MixedPart 0 type LoadPtr
VAR name Vr_CarWait 2 2 2 type Real
VAR name Vi_RecircCount 1 2 type Integer
VAR name Vi_Changeover 1 2 type Integer
VAR name Vld_Previous 0 type LoadPtr
VAR name Vi_Autostat 0 type Integer
VAR name Vi_ReqVehAutostat 0 type Integer
VAR name Vi_CarrierQtyAutostat 0 type Integer
VAR name Vi_Down 1 10 type Integer 1
VAR name Vi_ZoneSpace 2 3 2 type Integer
VAR name Vi_PrevPartType 1 2 type Integer
VAR name Vldlst_PrevZone 2 30 2 type LoadList
VAR name Vld_EmptyLoop 0 type LoadPtr
VAR name viSTRunOutQtyAS1 0 type Integer
VAR name viSTRunOutQtyAS2 0 type Integer
VAR name viSTRunOutQtyAS3 0 type Integer
VAR name viSTRunOutQtyAS4 0 type Integer
VAR name viTriggerQtyAS1 0 type Integer
VAR name viTriggerQtyAS2 0 type Integer
VAR name viTriggerQtyAS3 0 type Integer
VAR name viTriggerQtyAS4 0 type Integer
VAR name Vld_Schedule 1 2 type LoadPtr
VAR name Vi_ScheduleCount 2 2 2 type Integer
VAR name Vi_EmptyReq 0 type Integer
VAR name Vld_Crit 0 type LoadPtr
VAR name Vr_WeldStarveTime 1 2 type Real
VAR name Vi_EmptyReqAutostat 0 type Integer
VAR name Vi_WeldOutputTarget 1 2 type Integer
VAR name Vi_WeldGap 1 4 type Integer
VAR name Vi_SplitSPO 0 type Integer
VAR name Vi_SplitSPOAS 0 type Integer
VAR name Vi_WeldOutputTargetAS 1 2 type Integer
VAR name Vr_Scrap 1 30 type Real
VAR name Vr_Uptime 1 5 type Real
VAR name Vi_InclDT 1 5 type Integer
VAR name vrscDowntime 1 5 type ResourcePtr
VAR name vsStpShift 2 9 3 type String
VAR name vrStpShiftDuration 2 9 3 type Real
VAR name viProductionMin 2 3 3 type Integer
VAR name vsWeldShift 3 10 3 2 type String
VAR name vrWeldShiftDuration 3 10 3 2 type Real
VAR name viSpoPress 1 20 type Integer
VAR name viOtherPress 1 50 type Integer
VAR name viStpDay 0 type Integer
VAR name viStpShift 0 type Integer
VAR name viWeek 1 2 type Integer
VAR name viWeldDay 1 2 type Integer
VAR name viWeldShift 1 2 type Integer
VAR name viEmptyLoad 2 2 2 type Integer
VAR name viPressActive 1 2 type Integer
VAR name Vi_LotNo 0 type Integer
VAR name viMinShotSize 2 20 2 type Integer
VAR name viVariation 1 20 type Integer
VAR name viVariationCount 0 type Integer
VAR name viModelV 1 30 type Integer
VAR name vsStyleV 1 10 type String
VAR name viInitQtyV 1 10 type Integer
VAR name viTriggerQtyV 1 10 type Integer
VAR name viSTRunOutQtyV 1 10 type Integer
VAR name vrStyleCTV 2 10 2 type Real
VAR name viCarrierQtyV 1 10 type Integer
VAR name vclr_LoadColorV 1 10 type Color
VAR name viModelOther 1 50 type Integer
VAR name viMinShotSize2 2 50 2 type Integer
VAR name vi_NumType2 0 type Integer
VAR name vsSpoByType 1 20 type String
VAR name viDailyTotal 1 20 type Integer
VAR name vldlstCritInv 1 2 type LoadList
VAR name viVariationTracker 1 2 type Integer
VAR name viVariationTarget 1 2 type Integer
VAR name Vs_ProdType 2 2 2 type String
VAR name Vi_Count 1 3 type Integer
VAR name viTypeHold 0 type Integer
VAR name vsSortingRule 1 12 type String
VAR name viZoneSortPriority 1 12 type Integer
VAR name vi 0 type Integer
VAR name Variable1 0 type Integer
VAR name vphoLanes 2 3 20 type PhotoeyePtr
VAR name vldlstLanePull 2 3 20 type LoadList
VAR name vsPullingRule 1 12 type String
VAR name viZonePullPriority 1 12 type Integer
VAR name Vi_LineCount 0 type Integer
VAR name viMixCount 0 type Integer
VAR name viStorageInit 1 2 type Integer
VAR name viPrevLotNo 0 type Integer
VAR name viMixPrevRow 0 type Integer
VAR name viWeld2PrevType 1 2 type Integer
VAR name Vr_Scrap2 1 50 type Real
VAR name viRecircCount 0 type Integer
VAR name vix 0 type Integer
VAR name viSta8_1Count 0 type Integer
VAR name vldPrevHolder 0 type LoadPtr
VAR name vmMotor 1 10 type MotorPtr
VAR name viClearABayFlag 1 4 type Integer
VAR name viABayCount 0 type Integer
VAR name viABayTrigger 0 type Integer 175
VAR name vrST13Inspect 0 type Real 2
VAR name Vr_WeldChangeoverTime 1 2 type Real
VAR name vrStopTime 1 10 type Real
VAR name vrBPressLiftDown 0 type Real
VAR name vrBPressLiftUp 0 type Real
VAR name viEmptyCarPullQty 2 3 20 type Integer
VAR name viABayCleanUp 0 type Integer
VAR name Vi_ReleaseAS 0 type Integer
VAR name viABayTriggerAS 0 type Integer
VAR name viEmptyBufferAvailable 0 type Integer
VAR name viTotalDay 0 type Integer
VAR name vrStateTemp 0 type Real
VAR name viBlocked 2 2 2 type Integer
VAR name Vi_CarQty 0 type Integer
VAR name viCarrierReleaseCount 2 4 2 type Integer
VAR name viKDcount 1 2 type Integer
VAR name vsWeldStatus 0 type String
VAR name viCLineEmptyTrigger 0 type Integer 9
VAR name viCLineEmptyTriggerAS 0 type Integer
VAR name vrBLineSlowCT 0 type Integer
VAR name vrCLineSlowCT 0 type Integer
VAR name viTrackStoreProcess 0 type Integer 1
VAR name vsZone 1 3 type String
VAR name vsStation 2 3 20 type String
VAR name vldWriteJSON 0 type LoadPtr
VAR name viInitializeSPO 0 type Integer 1
VAR name vsTemp 0 type String
VAR name vsockSocket 0 type Ptr2Socket
VAR name vsMessage 0 type String
VAR name vsReponse 0 type String
VAR name vjsonStruct 0 type Ptr2JSON
VAR name vsResponse 0 type String
RNSTREAM stream0 0 type CMRG flags 1
	cmrgseed 1 12345 12345 12345 12345 12345 12345
RNSTREAM stream1 0 type CMRG flags 0
	cmrgseed 1 3692455944 1366884236 2968912127 335948734 4161675175 475798818
RNSTREAM stream2 0 type CMRG flags 0
	cmrgseed 1 1015873554 1310354410 2249465273 994084013 2912484720 3876682925
RNSTREAM stream3 0 type CMRG flags 0
	cmrgseed 1 2338701263 1119171942 2570676563 317077452 3194180850 618832124
RNSTREAM stream4 0 type CMRG flags 0
	cmrgseed 1 1597262096 3906379055 3312112953 1016013135 4099474108 275305423
RNSTREAM stream5 0 type CMRG flags 0
	cmrgseed 1 97147054 3131372450 829345164 3691032523 3006063034 4259826321
RNSTREAM stream6 0 type CMRG flags 0
	cmrgseed 1 796079799 2105258207 955365076 2923159030 4116632677 3067683584
RNSTREAM stream7 0 type CMRG flags 0
	cmrgseed 1 3281794178 2616230133 1457051261 2762791137 2480527362 2282316169
RNSTREAM stream8 0 type CMRG flags 0
	cmrgseed 1 3777646647 1837464056 4204654757 664239048 4190510072 2959195122
RNSTREAM stream9 0 type CMRG flags 0
	cmrgseed 1 4215590817 3862461878 1087200967 1544910132 936383720 1611370123
RNSTREAM stream10 0 type CMRG flags 0
	cmrgseed 1 1683636369 362165168 814316280 869382050 980203903 2062101717
RNSTREAM stream11 0 type CMRG flags 0
	cmrgseed 1 272317999 166758548 310112982 201045826 1680231254 118290799
RNSTREAM stream12 0 type CMRG flags 0
	cmrgseed 1 2245755202 1652682525 2865544364 721509566 209733568 592362218
RNSTREAM stream13 0 type CMRG flags 0
	cmrgseed 1 3003961408 3529909391 14538032 3603919910 566682685 1235016484
RNSTREAM stream14 0 type CMRG flags 0
	cmrgseed 1 596094074 2279636413 3050913596 1739649456 2368706608 3058697049
RNSTREAM stream15 0 type CMRG flags 0
	cmrgseed 1 1437096527 2547142266 2541498983 2640839690 2160978219 2618657830
RNSTREAM stream16 0 type CMRG flags 0
	cmrgseed 1 3224044943 1227141655 2220611050 1504589054 2829780440 108189859
RNSTREAM stream17 0 type CMRG flags 0
	cmrgseed 1 927434978 1593504038 2143021818 1749489845 1330187821 2371554242
RNSTREAM stream18 0 type CMRG flags 0
	cmrgseed 1 3446225690 835741554 2195834023 1297741021 3357053382 383824268
RNSTREAM stream19 0 type CMRG flags 0
	cmrgseed 1 3984477137 1267973573 3770063761 216527865 1568537936 1200352663
RNSTREAM stream20 0 type CMRG flags 0
	cmrgseed 1 2503334853 3179736892 2080184241 2881517721 3611607237 2639045213
RNSTREAM stream21 0 type CMRG flags 0
	cmrgseed 1 2784301963 3004110709 4065265451 2351994899 1824839263 1473060447
RNSTREAM stream22 0 type CMRG flags 0
	cmrgseed 1 3645411182 1391027122 3995023402 536114258 1376034799 2391282907
RNSTREAM stream23 0 type CMRG flags 0
	cmrgseed 1 4205039797 2998067142 2737381050 2060484144 21123025 900610517
RNSTREAM stream24 0 type CMRG flags 0
	cmrgseed 1 1708138263 1103344630 3131738563 354874525 1462105562 2799820805
RNSTREAM stream25 0 type CMRG flags 0
	cmrgseed 1 1762624356 3338520614 1869672149 2420033342 3823777008 4112813433
RNSTREAM stream26 0 type CMRG flags 0
	cmrgseed 1 1406960847 2689758640 2086764669 1479781266 4277003072 4173905453
RNSTREAM stream27 0 type CMRG flags 0
	cmrgseed 1 3217706054 3907922111 3316415646 934936431 1155310743 1383590847
RNSTREAM stream28 0 type CMRG flags 0
	cmrgseed 1 2996242832 2506376012 1182520337 938957435 3286002927 1946707306
RNSTREAM stream29 0 type CMRG flags 0
	cmrgseed 1 3757057907 1334546224 1509001838 991499802 1502864996 921145139
RNSTREAM stream30 0 type CMRG flags 0
	cmrgseed 1 3728532268 988545039 1631700325 1143954198 2209269908 1591407377
RNSTREAM stream31 0 type CMRG flags 0
	cmrgseed 1 3294123071 1464634864 3721431761 2693241183 1754954480 3900039887
RNSTREAM stream32 0 type CMRG flags 0
	cmrgseed 1 131824893 3188611738 1376082626 2419937722 2563475034 1251594162
RNSTREAM stream33 0 type CMRG flags 0
	cmrgseed 1 3770164700 4029076745 1104720217 3496143152 735265587 3273706940
RNSTREAM stream34 0 type CMRG flags 0
	cmrgseed 1 2131062045 2455732983 789218840 1151705419 286383816 924846932
RNSTREAM stream35 0 type CMRG flags 0
	cmrgseed 1 2053528341 4170407123 3515474899 98091789 1000335891 3854257602
RNSTREAM stream36 0 type CMRG flags 0
	cmrgseed 1 2469807084 9172128 3362394154 4021928081 2651348937 2296225074
RNSTREAM stream37 0 type CMRG flags 0
	cmrgseed 1 794136241 1326962976 1423309168 588827532 1370684418 1646244893
RNSTREAM stream38 0 type CMRG flags 0
	cmrgseed 1 2866999578 1918106148 3899143652 2905238681 3298551408 326591245
RNSTREAM stream39 0 type CMRG flags 0
	cmrgseed 1 3040096556 1206876890 780501254 597456198 1004013014 3962406934
RNSTREAM stream40 0 type CMRG flags 0
	cmrgseed 1 3970659565 2743575038 4070166570 141329301 2711190398 148640829
RNSTREAM stream41 0 type CMRG flags 0
	cmrgseed 1 216582539 3263278363 1880572884 3722908253 287018188 285207334
RNSTREAM stream_L_Start_1 0 type CMRG flags 1
	title "Generated automatically for L_Start"
	cmrgseed 1 4258459985 1892494520 2510345890 2935846613 1134826799 1965241718
RNSTREAM stream_rCLine_1 0 type CMRG flags 1
	title "Generated automatically for rCLine"
	cmrgseed 1 578737695 119907309 1233642926 4159114509 3946997940 72299514
RNSTREAM stream_R_ReleasePartial_1 60 type CMRG flags 1
	title "Generated automatically for R_ReleasePartial"
	cmrgseed 1 1609088888 302146514 1337822968 32443790 663198624 108142314
	cmrgseed 2 3500933258 1350819015 2732370233 555530171 4066946918 915552600
	cmrgseed 3 193582114 3536931462 1061163334 2771375939 1968090767 2893840988
	cmrgseed 4 2904978947 1723463734 2986735812 3454199587 735074847 2431656074
	cmrgseed 5 2966808121 3354520627 346101755 1263362009 4029540813 3080467228
	cmrgseed 6 569973296 3971015859 3727859896 2211808163 3074531219 1990606083
	cmrgseed 7 3683590777 3859373512 297644217 3451581853 333484586 2062123863
	cmrgseed 8 645531386 2789451056 574522196 570030963 3542619707 497013447
	cmrgseed 9 451656962 311003181 1307291076 1120714888 2424794474 2826415669
	cmrgseed 10 2009828795 776964895 69098504 438661728 1172795657 2573364290
	cmrgseed 11 3822648601 92348107 1997763206 864318631 1483077461 2056555948
	cmrgseed 12 614997537 1819073424 3953698181 2415633525 3985143399 3104087993
	cmrgseed 13 2696197046 3302881960 2217482014 1184639571 3815202881 4248496996
	cmrgseed 14 1584359841 1714547934 658805885 1071907079 77455845 741641926
	cmrgseed 15 726227831 1962661793 1950625786 220278686 3627366715 676701225
	cmrgseed 16 3081323669 1003094393 1114003595 1690627300 2807530893 2353295582
	cmrgseed 17 615356012 2633065659 4262847626 3336623339 1152986568 288053453
	cmrgseed 18 627386388 134288414 3583523073 3219344775 1641698932 1924195327
	cmrgseed 19 1371714546 546457055 2210255315 701341692 128838563 3087637560
	cmrgseed 20 3149532192 3581986456 3904722747 25198830 2856074410 321770274
	cmrgseed 21 541069526 891171920 2508804990 1402126153 3804486605 3049874423
	cmrgseed 22 879907147 1586569619 3121078790 3930638953 3840575809 2754440578
	cmrgseed 23 1841988888 1211113967 2598991704 260094651 1714680507 2261671600
	cmrgseed 24 3925417465 3236785574 2032531379 3086324155 735972187 3909754423
	cmrgseed 25 1017385619 1143113395 3866055690 3738182723 3456843205 1605678469
	cmrgseed 26 546457566 874492071 380178815 2145604372 711387940 3112098037
	cmrgseed 27 1522176126 3281673527 824270598 3443314181 3565931623 309504983
	cmrgseed 28 1277263236 2359497426 4294399406 3474413759 1508167830 3533480074
	cmrgseed 29 2450051394 746296289 1347591983 1779693762 4260483579 3507120388
	cmrgseed 30 308060176 159291403 2562796183 2383662595 3577967344 1885718101
	cmrgseed 31 2158664032 186817903 1776857067 4178021951 716634913 679682974
	cmrgseed 32 307501208 1166852306 1396419604 3528163545 1121652594 3579819477
	cmrgseed 33 1760077852 1471447944 2382380473 630182378 218525868 3852844152
	cmrgseed 34 1560951216 2314910160 2104669346 1283999685 655558023 2953009653
	cmrgseed 35 3988010789 344609650 3356062431 566514491 3347106535 631145247
	cmrgseed 36 259869676 3954393206 2631824449 3881290686 1592430131 2160258221
	cmrgseed 37 1506630107 1841065695 3294418064 1953767123 4094993220 2509921339
	cmrgseed 38 2866196060 564749596 1618009005 2496618340 2146793728 916090401
	cmrgseed 39 178242412 3058297723 3771458506 2918043790 1993944902 3921122165
	cmrgseed 40 1979532581 3579395427 1112164460 4070107704 970480302 1720043630
	cmrgseed 41 643532497 3598144974 125279843 3697954706 164739618 4096167465
	cmrgseed 42 204719871 3300432147 4128200353 1665581454 3807849547 2557839776
	cmrgseed 43 3382958008 455895194 1421441430 4142661624 3846705959 2841845339
	cmrgseed 44 2236219392 2219288657 343733359 3378294891 3637984166 1669122774
	cmrgseed 45 4147204767 1259446458 1969702168 502925887 1539647406 3140985209
	cmrgseed 46 3730331982 1956637255 4232576260 151235110 2584755683 1454157985
	cmrgseed 47 736967804 1478519017 221285451 2987940520 2799850668 3436204446
	cmrgseed 48 2720772426 4255190718 866769584 2541123752 1869013861 3303800365
	cmrgseed 49 3917204887 2563619721 2482310559 416526067 2042052715 3396945021
	cmrgseed 50 2131244782 2821333718 2598920380 3378605458 3310285042 2800848265
	cmrgseed 51 3784315248 996873982 2988033882 2007606855 4267970784 2432485535
	cmrgseed 52 4025952279 1663144918 2948618970 883957379 3600161925 1595816428
	cmrgseed 53 2366383009 1044070474 1923538174 2761647181 3707785002 1108390708
	cmrgseed 54 4058342111 3334898841 2178069534 359557593 157509792 2693561797
	cmrgseed 55 3105425092 2768805900 2817776848 2442950040 1612001218 3936637213
	cmrgseed 56 1818967393 3878901020 2156222518 1529233152 3309559639 3590573842
	cmrgseed 57 3259324221 1001550867 250299444 4110836298 4029826870 3034676375
	cmrgseed 58 1705522345 3177258616 1210499404 52765940 586950900 1422373228
	cmrgseed 59 755886916 3098765 2338931606 3802920016 507275212 2227807416
	cmrgseed 60 3987628212 3924902655 3343198925 3205245948 3545601024 2130581485
RNSTREAM stream_R_ZoneExit_1 3 type CMRG flags 1
	title "Generated automatically for R_ZoneExit"
	cmrgseed 1 3358935183 2896679130 3652789305 2121100418 2518069033 74804712
	cmrgseed 2 3004552957 4101045759 3476022864 625190952 2978528018 1763203065
	cmrgseed 3 2711120391 3589321238 2335170050 314232633 3353097964 3212495829
RNSTREAM stream_R_SPO_Release_1 0 type CMRG flags 1
	title "Generated automatically for R_SPO_Release"
	cmrgseed 1 4016980097 3481660449 995889916 3015184182 4180728415 3263605701
RNSTREAM stream100 0 type CMRG flags 0
	cmrgseed 1 3898973754 3954080213 2064709520 2188651691 3520036946 140360635
RNSTREAM stream101 0 type CMRG flags 0
	cmrgseed 1 2950741129 1182230614 2266494632 3264569753 3860079112 2914919604
RNSTREAM stream102 0 type CMRG flags 0
	cmrgseed 1 4162503083 2325318328 3427021647 1263687958 1839993484 1534031616
RNSTREAM stream103 0 type CMRG flags 0
	cmrgseed 1 1808932148 3615971401 3133645910 2196338343 2444875938 2765698937
RNSTREAM stream_R_Weld_1 2 type CMRG flags 1
	title "Generated automatically for R_Weld"
	cmrgseed 1 3141091137 2129497813 4081979860 2777173342 1684720958 3321930217
	cmrgseed 2 2524073470 533156722 1759419091 2322444738 3167485252 2122871566
RNSTREAM stream104 0 type CMRG flags 0
	cmrgseed 1 4179545601 111666744 280945412 98594149 3356379368 1505387065
RNSTREAM stream105 0 type CMRG flags 0
	cmrgseed 1 1579182737 1786736998 3896304776 1592201850 2122223467 4181044934
RNSTREAM stream75 0 type CMRG flags 0
	cmrgseed 1 1218716144 1525435115 176811070 138176836 3105101257 3621592285
RNSTREAM stream76 0 type CMRG flags 0
	cmrgseed 1 2244580844 1308523312 1026263249 3983059527 2245951100 301614207
RNSTREAM stream_R_Line_1 2 type CMRG flags 1
	title "Generated automatically for R_Line"
	cmrgseed 1 1972364864 2223124111 2008763048 3892837113 1828848296 3260066119
	cmrgseed 2 1677432352 3612848216 472576335 1071499807 3984451018 709286334
RNSTREAM stream_R_SPO_1 4 type CMRG flags 1
	title "Generated automatically for R_SPO"
	cmrgseed 1 2007009720 4031269275 241537427 2670557102 3734023576 729237931
	cmrgseed 2 1437126607 1424591597 653716547 680621567 1670374379 1382157741
	cmrgseed 3 2122991285 1450016311 1366081549 4159089824 999437985 181220818
	cmrgseed 4 3355040203 918743900 2376563563 1289997520 3204659968 4079184885
RNSTREAM stream78 0 type CMRG flags 0
	cmrgseed 1 700348412 1214690233 2657802135 2354922604 1258417548 2932813997
RNSTREAM stream77 0 type CMRG flags 0
	cmrgseed 1 4251640336 1106835691 2758114028 365222407 2419010337 4200552653
RNSTREAM stream79 0 type CMRG flags 0
	cmrgseed 1 3627492676 2233275500 2619739791 3775763011 1626056202 2520094236
RNSTREAM stream80 0 type CMRG flags 0
	cmrgseed 1 2182669320 3385890625 3665797567 4231471703 3643646033 7276979
RNSTREAM stream_R_WeldComplete_1 4 type CMRG flags 1
	title "Generated automatically for R_WeldComplete"
	cmrgseed 1 1214694619 3319870864 2571030501 3933885426 3772558251 3013617930
	cmrgseed 2 4137809551 2801880469 3150307596 1421622206 4258690790 2505347335
	cmrgseed 3 3559058145 1772610376 2709600696 2568554959 1935030885 2759706194
	cmrgseed 4 3089587443 1388003515 1172104363 2830912506 2925108757 655671571
RNSTREAM stream_R_LineStarved_1 2 type CMRG flags 1
	title "Generated automatically for R_LineStarved"
	cmrgseed 1 779948107 3518807720 817336006 3411196061 800329844 2500763395
	cmrgseed 2 1242180700 904781018 629881751 899010550 2770418642 2965318592
RNSTREAM stream_rBLine_1 0 type CMRG flags 1
	title "Generated automatically for rBLine"
	cmrgseed 1 4107140350 3652805132 3989135958 3648385436 1813141855 2920614410
RNSTREAM stream_R_Dummy_1 5 type CMRG flags 1
	title "Generated automatically for R_Dummy"
	cmrgseed 1 1099677484 3959985210 890753042 2265197121 1511007810 2030945727
	cmrgseed 2 3393156095 4150320916 1569715703 338728584 396145648 2321627300
	cmrgseed 3 708371108 2378126658 4050378018 3594016193 3773359240 662918292
	cmrgseed 4 469360558 2096186134 3588306649 3216361204 3554165009 1049918076
	cmrgseed 5 1591885430 3756155716 2258470128 2877135607 308707798 2435127306
RNSTREAM stream_R_WeldShift_1 2 type CMRG flags 1
	title "Generated automatically for R_WeldShift"
	cmrgseed 1 2675300100 1693700354 3122510777 1524624349 2406796447 2635368459
	cmrgseed 2 2206962636 2261280749 919611454 2702237443 3959183302 2336885890
RNSTREAM stream42 0 type CMRG flags 0
	cmrgseed 1 3804190352 1800957874 4180762844 2375252150 1449951230 2034560044
RNSTREAM stream_rStampingShift_1 0 type CMRG flags 1
	title "Generated automatically for rStampingShift"
	cmrgseed 1 3935341426 3242108818 2947521315 1860473996 761401989 2546542777
RNSTREAM stream_rST13Inspector_1 0 type CMRG flags 1
	title "Generated automatically for rST13Inspector"
	cmrgseed 1 2433266920 44188127 3311258324 4007312597 95006433 284688520
RNSTREAM stream_rBLineTravel_1 0 type CMRG flags 1
	title "Generated automatically for rBLineTravel"
	cmrgseed 1 3224889125 720049455 2291814205 2600008430 3985237000 3031586564
RNSTREAM stream43 0 type CMRG flags 0
	cmrgseed 1 2130838332 1190405676 2433827998 3145321817 515735607 3975933051
RNSTREAM stream44 0 type CMRG flags 0
	cmrgseed 1 556839930 1741393078 845882029 3704315716 613513814 1656996012
RNSTREAM stream45 0 type CMRG flags 0
	cmrgseed 1 2644972476 2575877742 591999186 3452224448 2175515398 3296152385
RNSTREAM stream46 0 type CMRG flags 0
	cmrgseed 1 791435151 2988719249 2583524471 1611590310 2024336432 1646075666
RNSTREAM stream47 0 type CMRG flags 0
	cmrgseed 1 2962385330 2677041332 554716005 195158056 1464894719 1370846183
RNSTATE 1788419101 2730645074 3763971021 1972376352 216210057 2855554471
FUNC name F_time_to_date type String PARAM name CurTime type Real PARAM name SimStart type String
FUNC name F_getID type Integer
FUNC name sock_SetMessages type Integer PARAM name On type Integer
	title "Turn diagnostic messages On (not 0) or off (0)."
FUNC name sock_SetConnectBlockin type Integer PARAM name Blocking type Integer
	title "Blocking 1 or true, non-blocking 0 or false. Block while waiting to connect?"
FUNC name sock_SetConnectTimeOut type Integer PARAM name Seconds type Integer
	title "If not blocking, how long to wait to timeout? Integer seconds."
FUNC name sock_ConnectService type Ptr2Socket PARAM name HostName type String PARAM name ServiceName type String
	title "Connect to ServceName on HostName, get the Ptr2Socket back for later use."
FUNC name sock_ConnectPort type Ptr2Socket PARAM name HostName type String PARAM name PortNumber type Integer
	title "Connect to Portnumber on HostName, get the Ptr2Socket back for later use."
FUNC name sock_CloseSocket type Integer PARAM name socket type Ptr2Socket
	title "(Rare) Close the socket previously connected with sock_ConnectXXX"
FUNC name sock_IsValid type Integer PARAM name Socket type Ptr2Socket
	title "Is Socket valid? (open and usable): 1 or true, it is. 0 or false, it isn't."
FUNC name sock_SendString type Integer PARAM name Socket type Ptr2Socket PARAM name Message type String
	title "Send Message on Socket. Returns characters successfully sent."
FUNC name sock_ReadString type String PARAM name Socket type Ptr2Socket
	title "Read a string from Socket. Returns the string."
FUNC name sock_GetNonBlocking type Integer PARAM name Socket type Ptr2Socket
	title "Is Socket Nonblocking? true (or 1) if so. Otherwise false (or 0)."
FUNC name sock_SetNonBlocking type Integer PARAM name Socket type Ptr2Socket PARAM name Nonblocking type Integer
	title "Set Socket Nonblocking (1 or true), or blocking (0 or false). Return 0 if error, else 1."
FUNC name sock_GetTimeOut type Integer PARAM name Socket type Ptr2Socket
	title "Returns the TimeOut number of seconds for Socket."
FUNC name sock_SetTimeOut type Integer PARAM name Socket type Ptr2Socket PARAM name Seconds type Integer
	title "Set the TimeOut number of Seconds for Socket. Returns 0 if error, else 1."
FUNC name sock_GetBufferSize type Integer PARAM name Socket type Ptr2Socket
	title "Returns the current buffer size for Socket."
FUNC name sock_SetBufferSize type Integer PARAM name Socket type Ptr2Socket PARAM name BufferSize type Integer
	title "Set the buffer size for Socket. Returns 0 if error, else 1."
FUNC name sock_GetName type String PARAM name Socket type Ptr2Socket
	title "Get the name of Socket: "Socket ### to Hostname", or "<null>" if error."
FUNC name sock_GetHost type String PARAM name Socket type Ptr2Socket
	title "Get the name of Socket's Host: "Hostname", or "<null>" if error."
FUNC name sock_GetNum type Integer PARAM name Socket type Ptr2Socket
	title "Get Socket's number. If error, -1."
FUNC name sock_MachineName type String
	title "Get the Hostname of the local machine."
FUNC name F_ConnectSocket type Integer
FUNC name sock_SetConnectBlocking type Integer PARAM name On type Integer
FUNC name F_RequestMove type String PARAM name argLoad type LoadPtr
FUNC name cJSON_Parse type Ptr2JSON PARAM name Arg1 type String
	title "Turn a string into a JSON object."
FUNC name cJSON_GetObjectString type String PARAM name argJSON type Ptr2JSON PARAM name Item type String
	title "Find String Item in argJSON and return its string value."
FUNC name cJSON_Delete type Integer PARAM name argJSON type Ptr2JSON
	title "Destroy the JSON object."
SUBRTN name sAttributeStrip
SUBRTN name SR_AttributeReset
SUBRTN name SR_TypeIndex
SUBRTN name Sr_OtherInv
SUBRTN name Sr_Space
SUBRTN name Sr_Mix
SUBRTN name Sr_Recirc
SUBRTN name Sr_SPOSeq
SUBRTN name Sr_WriteJSONFile
SUBRTN name Sr_StoreIn
SFileBegin	name PressLogic.m
begin pInit arriving procedure
  set Vs_SimStart to "03-10-11 06:30:00"
  print "Time" "\t" "Type" "\t" "LotNo" "\t" "Zone" "\t" "Row" to "base.arc/RESULTS/StoreIn.out"
  print "Time" "\t" "Type" "\t" "LotNo" "\t" "Zone" "\t" "Row" to "base.arc/RESULTS/StoreOut1.out"
  print "Time" "\t" "Type" "\t" "LotNo" "\t" "Zone" "\t" "Row" to "base.arc/RESULTS/StoreOut2.out"
  
  set vsZone(1) to "CHigh"
  set vsZone(2) to "CLow"
  set vsZone(3) to "ABay"
  
  set viKDcount(2) to 25	
 	
  set Ai_pi to 1
  while Ai_pi <=50 do begin
  	if Ai_pi <= 30 then begin
  		print "" to Lb_Legend(Ai_pi)
  		print "" to Lb_Inventory(Ai_pi)
  	end
  	print "" to Lb_PartNames(Ai_pi)
  	print "" to Lb_Inv(Ai_pi)
  	inc Ai_pi by 1
  end 

  set C_ZoneMix(2) current to 35
 
  clone to P_LoadRBT(1) nlt L_invis   /** WELD SPO LOAD ROBOTS **/
  clone to P_LoadRBT(2) nlt L_invis
  clone to P_LoadRBT(3) nlt L_invis
  clone to P_LoadRBT(4) nlt L_invis  
  
  clone to P_WeldSched(1) nlt L_invis   /** WELD SCHED PROCESS **/ 
  clone to P_WeldSched(2) nlt L_invis
  
  clone to P_Time nlt L_invis  /** FOR PRINTING DATE & Time  **/

  order from O_BufferSpace(1) case backorder on O_BufferSpace(1)   
  order from O_BufferSpace(2) case backorder on O_BufferSpace(2)   
  order from O_BufferSpace(3) case backorder on O_BufferSpace(3)   
  order from O_BufferSpace(4) case backorder on O_BufferSpace(4)  

  order from O_ST13_SPO_Seq(1) case backorder on O_ST13_SPO_Seq(1)
  order from O_ST13_SPO_Seq(2) case backorder on O_ST13_SPO_Seq(2)
  order from O_ST13_SPO_Seq(3) case backorder on O_ST13_SPO_Seq(3)
  order from O_ST13_SPO_Seq(4) case backorder on O_ST13_SPO_Seq(4)

  order from O_PrevClear(1) case backorder on O_PrevClear(1) 
  order from O_PrevClear(2) case backorder on O_PrevClear(2) 
  order from O_PrevClear(3) case backorder on O_PrevClear(3) 
  
  order 1 load from olPressSide(1) to continue case backorder on olPressSide(1)
  order 1 load from olPressSide(2) to continue case backorder on olPressSide(2)
  order 1 load from olPressSide(3) to continue case backorder on olPressSide(3)
  order 1 load from olPressSide(4) to continue case backorder on olPressSide(4)

  order 4 loads from O_WE2BufferRH case backorder on O_WE2BufferRH
  order from O_WE2BufferLH case backorder on O_WE2BufferLH

  order 1 load from oLiftClear to continue case backorder on oLiftClear
  order 1 load from O_StampingPriority(1) to continue case backorder on O_StampingPriority(1)
  order 1 load from O_StampingPriority(2) to continue case backorder on O_StampingPriority(2)
  order 1 load from O_Weld1Order to continue case backorder on O_Weld1Order
  order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
  order 1 load from oBPressExit to continue case backorder on oBPressExit
  /*order 1 load from oST5_12 to continue case backorder on oST5_12
  order 1 load from oST5_13 to continue case backorder on oST5_13*/
  /*order 2 loads from oWeld2Changeover to continue case backorder on oWeld2Changeover*/
  
  /******************************** READ GENERAL INFORMATION ***********************/  
  read Vs_junk, Vr_SPOCycleTime(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vr_SPOCycleTime(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vr_SPOCycleTime(3) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vr_SPOCycleTime(4) from "base.arc/DATA/general.dat" with delimiter "\t"  
  read Vs_junk, Vr_WeldChangeoverTime(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vr_WeldChangeoverTime(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_WeldOutputTarget(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_WeldOutputTarget(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_Release from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrDieChangeTime(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrDieChangeTime(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viReqVeh from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_BayCapacity(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_BayCapacity(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_BayCapacity(3) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viABayTrigger from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_EmptyZone(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_EmptyZone(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, Vi_EmptyZone(3) from "base.arc/DATA/general.dat" with delimiter "\t"  
  read Vs_junk, Vi_EmptyReq from "base.arc/DATA/general.dat" with delimiter "\t"	
  read Vs_junk, Vi_SplitSPO from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viPressActive(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viPressActive(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viEmptyLoad(1,2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viEmptyLoad(2,2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viCLineEmptyTrigger from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, viInitializeSPO from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(1) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(2) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(3) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(4) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(5) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(6) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(7) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(8) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(9) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrStopTime(10) from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrBPressLiftDown from "base.arc/DATA/general.dat" with delimiter "\t"
  read Vs_junk, vrBPressLiftUp from "base.arc/DATA/general.dat" with delimiter "\t"
 
  set Vr_SPOCycleTime(1) to Vr_SPOCycleTime(1) - 6
  set Vr_SPOCycleTime(2) to Vr_SPOCycleTime(2) - 6
  set Vr_SPOCycleTime(3) to Vr_SPOCycleTime(3) - 6 
  set Vr_SPOCycleTime(4) to Vr_SPOCycleTime(4) - 6  
   
  set Vi_SPO_QtyRelease(1) = Vi_Release
  set Vi_SPO_QtyRelease(2) = Vi_Release
  set Vi_SPO_QtyRelease(3) = Vi_Release
  set Vi_SPO_QtyRelease(4) = Vi_Release

  set Ai_pi to 1
  while Ai_pi <= 35 do begin
  	if Ai_pi <= 20 then set pf_ohc:ST12_(Ai_pi) capacity to Vi_BayCapacity(2)
   	else if Ai_pi >= 30 then set pf_ohc:ST12_(Ai_pi) capacity to Vi_BayCapacity(3)
   	else set pf_ohc:ST12_(Ai_pi) capacity to Vi_BayCapacity(1)
   	inc Ai_pi by 1
  end  
  
  set Ai_Seq = 1
  read Vs_junk from "base.arc/DATA/modellist.dat" with delimiter "\t"
  while Vs_junk <> "0" and Ai_Seq <= 20 do begin
  	read vsSpoByType(Ai_Seq), viDailyTotal(Ai_Seq) from "base.arc/DATA/modellist.dat" with delimiter "\t"
  	if vsSpoByType(Ai_Seq) = "No" then 
  		clone 1 load to pNonSpoPartMgt(Ai_Seq)	/* This process will decrement inventory for parts that don't have SPOs */
  	inc Ai_Seq by 2
  	read Vs_junk from "base.arc/DATA/modellist.dat" with delimiter "\t"
  end
 
  /******************************** READ DOWNTIME INFORMATION **********************/
  read Vs_junk from "base.arc/DATA/down.dat" with delimiter "\n"         /** Reading title **/
  set Ai_Seq = 1 
  while Ai_Seq <= 5 do begin
  	read Vs_Downtime(Ai_Seq), Vr_Uptime(Ai_Seq), Vr_MTTR(Ai_Seq), Vi_InclDT(Ai_Seq) from "base.arc/DATA/down.dat" with delimiter "\t"
    if Vi_InclDT(Ai_Seq) = 1 and Vr_Uptime(Ai_Seq) < 100 then begin
    	set Vr_MTBF(Ai_Seq) = (Vr_Uptime(Ai_Seq) * Vr_MTTR(Ai_Seq))/(100-Vr_Uptime(Ai_Seq))
    	clone 1 load to P_Downtime(Ai_Seq)
    end
    inc Ai_Seq by 1
  end   

  set vrscDowntime(1) to R_Weld(1)
  set vrscDowntime(2) to R_Weld(2)
  set vrscDowntime(3) to rBLine
  set vrscDowntime(4) to rCLine
 
  /******************************** READ STAMPING SHIFT INFORMATION *******************/
  set Ai_Seq to 1
  while Ai_Seq <= 3 do begin
    set Ai_pi = 1
    read Vs_junk from "base.arc/DATA/stpshift.dat" with delimiter "\n"		/** Reading title **/
 	while Ai_pi <= 9 do begin
 		read vsStpShift(Ai_pi,Ai_Seq), vrStpShiftDuration(Ai_pi,Ai_Seq) from "base.arc/DATA/stpshift.dat" with delimiter "\t"
 		inc Ai_pi by 1
 	end
 	read Vs_junk, viProductionMin(Ai_Seq,3) from "base.arc/DATA/stpshift.dat" with delimiter "\t"
  	inc Ai_Seq by 1
  end  
 
  /**************************** END READ STAMPING SHIFT INFORMATION *******************/

  /************************** READ WELDING LINE SHIFT INFORMATION *********************/
  set Ai_Line to 1
  while Ai_Line <= 2 do begin
  	read Vs_junk from "base.arc/DATA/wldshift.dat" with delimiter "\n"
  	set Ai_Seq to 1
  	while Ai_Seq <= 2 do begin
  		set Ai_pi = 1
  		read Vs_junk from "base.arc/DATA/wldshift.dat" with delimiter "\n"      /** Reading title **/ 
  		while Ai_pi <= 10 do begin
  			read vsWeldShift(Ai_pi,Ai_Seq,Ai_Line), vrWeldShiftDuration(Ai_pi,Ai_Seq,Ai_Line) from "base.arc/DATA/wldshift.dat" with delimiter "\t"
  			inc Ai_pi by 1
  		end
  		read Vs_junk, viProductionMin(Ai_Seq,Ai_Line) from "base.arc/DATA/wldshift.dat" with delimiter "\t"
  		inc Ai_Seq by 1
  	end
  	inc Ai_Line by 1
  end
 
  /**************************** END READ WELDING SHIFT INFORMATION ********************/
  
  clone 1 load to P_StampingShift
  clone 1 load to P_WeldShift(1)
  clone 1 load to P_WeldShift(2)
  clone 1 load to pZoneMixCheck
  clone 1 load to pEmptyCarrierManagement
  
  
  /**************************** Read Statements for Sorting Logic ************************/
  open "base.arc/DATA/sorting.dat" for reading save result as vflptrInputData
  set Ai_pi = 1
  read Vs_junk from vflptrInputData with delimiter "\n"
  while Ai_pi <= 12 do begin
  	read vsSortingRule(Ai_pi), viZoneSortPriority(Ai_pi), vsPullingRule(Ai_pi), viZonePullPriority(Ai_pi) from vflptrInputData with delimiter "\t"
  	inc Ai_pi by 1
  end
  
  /*****************************Read statements for Stamping Info*************************/
  open "base.arc/DATA/stdata.dat" for reading save result as vflptrInputData
  read Vs_junk from vflptrInputData with delimiter "\n"
  set Ai_pi = 1
  read Vs_junk, vsStyle(Ai_pi) from vflptrInputData with delimiter "\t"
  while vsStyle(Ai_pi) <> "" do begin
    read viInitQty(Ai_pi), viTriggerQty(Ai_pi), viSTRunOutQty(Ai_pi), viMinShotSize(Ai_pi,1), viMinShotSize(Ai_pi,2), vrStyleCT(Ai_pi,1,1), vrStyleCT(Ai_pi,1,2), vrStyleCT(Ai_pi,2,1), 
    	vrStyleCT(Ai_pi,2,2), Vr_Scrap(Ai_pi), viZonePref(1,Ai_pi), viZonePref(2,Ai_pi), viZonePref(3,Ai_pi),viCarrierQty(Ai_pi), vlt_LoadType(Ai_pi), vclr_LoadColor(Ai_pi), 
    	Vi_StampingPriority(Ai_pi), viSpoPress(Ai_pi), viVariation(Ai_pi)  from vflptrInputData with delimiter "\t" 
    inc viVariationCount by viVariation(Ai_pi)
    print vsStyle(Ai_pi) to Lb_Legend(Ai_pi)
    print "TrigQty->", viTriggerQty(Ai_pi), "RunQty->", viSTRunOutQty(Ai_pi), "Inv->",viInitQty(Ai_pi) to Lb_Inventory(Ai_pi)
    if viSpoPress(Ai_pi) = 1 then begin
    	set Lb_Inventory(Ai_pi) color to blue
    	set Lb_Legend(Ai_pi) color to blue
    end
    else begin
    	set Lb_Inventory(Ai_pi) color to red
    	set Lb_Legend(Ai_pi) color to red
	end   
	clone 1 load to P_Legend(Ai_pi) nlt L_invis             
    inc Ai_pi by 1
    read Vs_junk, vsStyle(Ai_pi) from vflptrInputData with delimiter "\t"
  end
  set vsStyle(Ai_pi) to null
  set Vi_NumType = Ai_pi-1		/***Sets number of types for use in loops***/
  /* clone Vi_NumType loads to P_Legend nlt L_invis */
  
  /*****************************Read statements for SPO Variations*************************/
  set Ai_pi = 21
  read Vs_junk from "base.arc/DATA/variations.dat" with delimiter "\n"
  while Ai_pi <= 20 + viVariationCount do begin
  	/*read viModelV(Ai_pi,1), viModelV(Ai_pi,2), vsStyleV(Ai_pi), Vs_junk, Vs_junk, viInitQtyV(Ai_pi), viTriggerQtyV(Ai_pi), viSTRunOutQtyV(Ai_pi), vrStyleCTV(Ai_pi,1), vrStyleCTV(Ai_pi,2),
  		viZonePref(1,20+Ai_pi), viZonePref(2,20+Ai_pi), viZonePref(3,20+Ai_pi), viCarrierQtyV(Ai_pi), vclr_LoadColorV(Ai_pi), viSpoPressV(Ai_pi) */
  	read Vs_junk, viModelV(Ai_pi), vsStyle(Ai_pi), Vs_junk, Vs_junk, viInitQty(Ai_pi), viTriggerQty(Ai_pi), viSTRunOutQty(Ai_pi), vrStyleCT(Ai_pi,1,1), 
  	vrStyleCT(Ai_pi,1,2), vrStyleCT(Ai_pi,2,1), vrStyleCT(Ai_pi,2,2), Vr_Scrap(Ai_pi), viZonePref(1,Ai_pi), viZonePref(2,Ai_pi), viZonePref(3,Ai_pi), 
  	viCarrierQty(Ai_pi), vlt_LoadType(Ai_pi),vclr_LoadColor(Ai_pi) from "base.arc/DATA/variations.dat" with delimiter "\t"
  	
  	print vsStyle(Ai_pi) to Lb_Legend(Ai_pi)
    print "TrigQty->", viTriggerQty(Ai_pi), "RunQty->", viSTRunOutQty(Ai_pi), "Inv->",viInitQty(Ai_pi) to Lb_Inventory(Ai_pi)
    if viSpoPress(viModelV(Ai_pi)) = 1 then begin
    	set Lb_Inventory(Ai_pi) color to blue
    	set Lb_Legend(Ai_pi) color to blue
    end
    else begin
    	set Lb_Inventory(Ai_pi) color to red
    	set Lb_Legend(Ai_pi) color to red
	end  
	clone 1 load to P_Legend(Ai_pi) nlt L_invis
  	inc Ai_pi by 1
  end
  
  /*******************Read statements for Stamping Info (Non-SPO) *************************/
  open "base.arc/DATA/otherparts.dat" for reading save result as vflptrInputData
  read Vs_junk from vflptrInputData with delimiter "\n"
  set Ai_pi to 1
  read viModelOther(Ai_pi) from vflptrInputData with delimiter "\t"
  while viModelOther(Ai_pi) <> 0 do begin 
  	read vsStyle2(Ai_pi),viInitQty2(Ai_pi),viTriggerQty2(Ai_pi),viSTRunOutQty2(Ai_pi),viMinShotSize2(Ai_pi,1), viMinShotSize2(Ai_pi,2),
  		vrStyleCT2(Ai_pi,1),vrStyleCT2(Ai_pi,2), Vr_Scrap2(Ai_pi), Vi_StampingPriority2(Ai_pi),viOtherPress(Ai_pi) from vflptrInputData with delimiter "\t"
  	print vsStyle2(Ai_pi) to Lb_PartNames(Ai_pi)
  	print "TrigQty->", viTriggerQty2(Ai_pi), "RunQty->", viSTRunOutQty2(Ai_pi), "Inv->",viInitQty2(Ai_pi) to Lb_Inv(Ai_pi)
  	
  	if viOtherPress(Ai_pi) = 1 then begin
    	set Lb_Inv(Ai_pi) color to blue
    	set Lb_PartNames(Ai_pi) color to blue
    end
    else begin
    	set Lb_Inv(Ai_pi) color to red
    	set Lb_PartNames(Ai_pi) color to red
	end  	
  	inc Ai_pi by 1
	read viModelOther(Ai_pi) from vflptrInputData with delimiter "\t"
  end
  set vi_NumType2 = Ai_pi - 1
  
  if Vi_Autostat = 1 then begin
    set viABayTrigger to viABayTriggerAS
    set viCLineEmptyTrigger to viCLineEmptyTriggerAS
    
    set Ai_pi to 1
    while Ai_pi <= 30 do begin
    	set vrStyleCT(Ai_pi,1,2) to vrBLineSlowCT
    	set vrStyleCT(Ai_pi,2,2) to vrCLineSlowCT
  		inc Ai_pi by 1
  	end
  end
  
  set rBLineTravel capacity to pf_ohc:ST_B27 capacity + pf_ohc:ST_B28 capacity
  set C_CarrierCount(2) current to viReqVeh
  order 10000 loads from O_VehicleStart to continue case backorder on O_VehicleStart
  	
  call Fsetpathtime(kLift:kLift(1),"ConfigUp","ConfigDown",vrBPressLiftDown)	
  call Fsetpathtime(kLift:kLift(1),"ConfigDown","ConfigUp",vrBPressLiftUp)	 	
      
  /*********************************Init for Inventory*************************************/
  
  if viInitializeSPO = 1 then begin	/* Initialize SPOs through the interface */
  	set Ai_pi = 1
  	while Ai_pi <= 30 do begin
    	if vsStyle(Ai_pi) <> null then begin
	    	set viInitRunQty(Ai_pi) to viInitQty(Ai_pi)
    		set As_Type = vsStyle(Ai_pi)
    		set Ai_Type = Ai_pi
    		if Ai_pi <= 20 then begin
    			set Ai_Press = viSpoPress(Ai_pi)
    			set this load type to vlt_LoadType(Ai_pi)
    		end	
    		else begin
	    		set Ai_Press = viSpoPress(viModelV(Ai_pi))
    			set this load type to vlt_LoadType(viModelV(Ai_pi))
    		end
	    	set Ai_Seq = 99999					/****Flag for Storage Logic to represent Init Part****/
    		set this load color to vclr_LoadColor(Ai_pi)
    
	    	while viInitRunQty(Ai_pi) > 0 do begin
    	  		if viInitRunQty(Ai_pi) >= viCarrierQty(Ai_pi) then begin
        	 		set Ai_CarQty = viCarrierQty(Ai_pi)
         			dec viInitRunQty(Ai_pi) by viCarrierQty(Ai_pi)
       			end
      			else begin
         			set Ai_CarQty = viInitRunQty(Ai_pi)
         			dec viInitRunQty(Ai_pi) by viInitRunQty(Ai_pi)
      			end
	      		inc C_CarrierCount(1) by 1
    	     	dec C_CarrierCount(2) by 1
      			scale to z Ai_CarQty
      			clone 1 load to pCarrierLoad
    		end
    	end    
    	inc Ai_pi by 1
  	end
  end
  else if viInitializeSPO = 2 then begin /* Initialize SPOs through JSON messages */
  	/* Model will read in a list of JSON messages containing part type, quantity, destination etc */
  end
  
  /******************** Take Down Motors to Stagger Chains   *********************************/
/*  set Ai_pi to 1
  while Ai_pi <= 10 do begin
  	take down vmMotor(Ai_pi)
  	wait for random stream42 e 3 min
  	bring up vmMotor(Ai_pi)
  	inc Ai_pi by 1
  end*/
  
  /***********************************Init for Empty Carriers*********************************/
  
  call sAttributeStrip
  set Ai_Seq = 99999
  set Ai_Line = 1
  set Ai_Status = 1
  clone /*315*/ C_CarrierCount(2) current loads to pEmptyCarrierReturn nlt L_invis
  
  clone 1 load to pWarmUp
  
  set Ai_Zone = 1
  set vldPrevHolder = this load
  wait to be ordered on oHold  
end

/*****************************Logic for Downtime ******************************/
/***** Procindex = 1 for Welding L1, 2 for Welding L2, 3 for Stamping B-Line, 4 for Stamping C-Line, 5 for OHC *****/
  begin P_Downtime arriving procedure 
	wait to be ordered on olWarmUp
	
	while 1=1 do begin
		set Vr_MTBFActual(procindex) = random stream Vstrm_DT(procindex) e Vr_MTBF(procindex)
		get R_Dummy(procindex)
		wait for Vr_MTBFActual(procindex) min 
		tabulate Vr_MTBFActual(procindex) in T_MTBF(procindex)
		free R_Dummy(procindex)
		
		if procindex < 5 then begin
			take down vrscDowntime(procindex)
			set vrscDowntime(procindex) active state to "Down"
			print vrscDowntime(procindex) active state to lblState(procindex)
		end
	    else take down pf_ohc:M_chain1
		set Vi_Down(procindex) = 0 
		
		set Vr_MTTRActual(procindex) = random stream Vstrm_DT(procindex+5) e Vr_MTTR(procindex) 
 		get R_Dummy(procindex)
 		wait for Vr_MTTRActual(procindex) min
 		tabulate Vr_MTTRActual(procindex) in T_MTTR(procindex)
 		free R_Dummy(procindex)
 	
 		if procindex < 5 then begin
 			bring up vrscDowntime(procindex)
 			/*if vrscDowntime(procindex) active state <> PlannedDT then */
 			if procindex = 1 then begin
 				if O_WeldBufferStarve(1) current + O_WeldBufferStarve(2) current > 0 then begin
 					set vrscDowntime(1) active state to "Starved"
 					set R_LineStarved(1) active state to "Starved"
 				end
 				else begin
 					set vrscDowntime(1) active state to "Production"
 					set R_LineStarved(1) active state to "Production"
 				end
 			end
 			if procindex = 2 then begin
 				if O_WeldBufferStarve(3) current + O_WeldBufferStarve(4) current > 0 then begin
 					set vrscDowntime(2) active state to "Starved"
 					set R_LineStarved(2) active state to "Starved"
 				end
 				else begin 
 					set vrscDowntime(2) active state to "Production"
 					set R_LineStarved(2) active state to "Production"
 				end
 			end
 			if procindex = 3 or procindex = 4 then begin
 				if vlocptrSTLoad(1,2,procindex-2) current + vlocptrSTLoad(2,2,procindex-2) current > 0 and vlocptrSTLoad(1,3,procindex-2) current + vlocptrSTLoad(2,3,procindex-2) current < 2 then
 					set vrscDowntime(procindex) active state to "NoCarriers"
 				else if viBlocked(procindex-2,1) + viBlocked(procindex-2,2) > 0 then
 					set vrscDowntime(procindex) active state to "Blocked"
 				else if vrscDowntime(procindex) current = 0 then 
 					set vrscDowntime(procindex) active state to "NoScheduledRuns" 
 				else set vrscDowntime(procindex) active state to "Production" 
 			end
 			else set vrscDowntime(procindex) active state to "Production"
 			print vrscDowntime(procindex) active state to lblState(procindex)
 		end
 		else bring up pf_ohc:M_chain1
 		set Vi_Down(procindex) = 1 
 	end
 end

/*****************************Logic for Stamping Production Runs******************************/
begin pSTRunControl arriving procedure  
	if ac <= 86400 then wait to be ordered on olWarmUp 
  	
  	if Ai_NonSPO = 1 then
  		set Ai_Type to viModelOther(Ai_Type2)
  
	set Ai_StampingPriority to Vi_StampingPriority(Ai_Type)	/* Side Panel priority list = 4Dr Accd, TL, NM, 2Dr Accd, RDX */ 
	if Ai_NonSPO = 1 and Ai_StampingPriority = 0			
		then set Ai_StampingPriority to Vi_StampingPriority2(Ai_Type2) 
	
	set this load priority to Ai_StampingPriority
	if Ai_NonSPO = 0 then inc Vi_ScheduleCount(1,Ai_Press) by 1
	else if Ai_NonSPO = 1 then inc Vi_ScheduleCount(2,Ai_Press) by 1
	wait to be ordered on O_StampingPriority(Ai_Press)		/* Order list used to prioritize part type */
  
  	remove this load from vldlstCritInv(Ai_Press) 
  
  	inc Vi_LotNo by 1
  	set Ai_LotNo = Vi_LotNo
  
	if Ai_NonSPO = 0 then dec Vi_ScheduleCount(1,Ai_Press) by 1
	else if Ai_NonSPO = 1 then dec Vi_ScheduleCount(2,Ai_Press) by 1    
       
	scale to z 1
	if Ai_NonSPO = 0 then begin
  		set viSTRunGen(Ai_Press) = 1
    	set viKickOutQty(1) = 0		/** FOR INETERRUPT EMPTY CARRIER RE-CIRCULATION **/
    	order all loads from oEmptyRecirc to die
  	end     
  	
  	if viSTRunGen(1) + viSTRunGen(2) = 2 then
		set vsmPressCondition active state to "TwoPressRunningSpo"
	else if viSTRunGen(1) + viSTRunGen(2) = 1 then
		set vsmPressCondition active state to "OnePressRunningSpo"
	else if viSTRunGen(1) + viSTRunGen(2) = 0 then
		set vsmPressCondition active state to "NoPressRunningSpo"
  	
  	if Ai_NonSPO = 0 then begin
  		print vsStyle(Ai_Type) to lblPart(Ai_Press)
  		print vrStyleCT(Ai_Type,Ai_Press,viSTRunGen(1) + viSTRunGen(2)) as .2 to lblCycleTime(Ai_Press)
  	end
  	else begin
  		print vsStyle2(Ai_Type2) to lblPart(Ai_Press)
  		print vrStyleCT2(Ai_Type2,Ai_Press) as .2 to lblCycleTime(Ai_Press)	
  	end
  	
  	get vrscDowntime(2+Ai_Press)
    set vrscDowntime(2+Ai_Press) active state to "DieChange"
    print vrscDowntime(2+Ai_Press) active state to lblState(2+Ai_Press)  
    wait for vrDieChangeTime(Ai_Press) min
    inc Vi_Changeover(Ai_Press) by 1
    free vrscDowntime(2+Ai_Press)
    
 	if vlocptrSTLoad(1,2,Ai_Press) current + vlocptrSTLoad(2,2,Ai_Press) current > 0 and vlocptrSTLoad(1,3,Ai_Press) current + vlocptrSTLoad(2,3,Ai_Press) current < 2 then
 		set vrscDowntime(2+Ai_Press) active state to "NoCarriers"
 	else if viBlocked(Ai_Press,1) + viBlocked(Ai_Press,2) > 0 then
 		set vrscDowntime(2+Ai_Press) active state to "Blocked"
 	else if vrscDowntime(2+Ai_Press) current = 0 then 
 		set vrscDowntime(2+Ai_Press) active state to "NoScheduledRuns" 
 	else set vrscDowntime(2+Ai_Press) active state to "Production" 
    
    set vrscDowntime(2+Ai_Press) active state to "Production"
    print vrscDowntime(2+Ai_Press) active state to lblState(2+Ai_Press)
    
 /* if olPartKill(1) current > 0 then 
  		order all loads from olPartKill(1) to continue
  	if olPartKill(2) current > 0 then 
  		order all loads from olPartKill(2) to continue
  	if olPartKill(3) current > 0 then 
  		order all loads from olPartKill(3) to continue
  	if olPartKill(4) current > 0 then 
  		order all loads from olPartKill(4) to continue  */
  		
  	order all loads from volPartKill(1,1,Ai_Press) to continue
  	order all loads from volPartKill(1,2,Ai_Press) to continue
  	order all loads from volPartKill(2,1,Ai_Press) to continue
  	order all loads from volPartKill(2,2,Ai_Press) to continue	
    
/*    if vlptrLoadGraphic(1,Ai_Press) <> null and vlptrLoadGraphic(2,Ai_Press) <> null then begin		/* Order carrier to continue if there was an early changeover */
		if vlptrLoadGraphic(1,Ai_Press) Ai_Type <> this load Ai_Type and vlptrLoadGraphic(2,Ai_Press) Ai_Type <> this load Ai_Type and 
		vlocptrSTLoad(1,2,Ai_Press) current + vlocptrSTLoad(2,2,Ai_Press) current = 0 then begin
	 		order 1 load from volPartKill(1,2,Ai_Press) to continue
	 		order 1 load from volPartKill(2,2,Ai_Press) to continue
			wait for 0
		end
	end*/
   
    call sAttributeStrip
    
    if Ai_NonSPO = 0 then begin 	
    	set Ai_LotQty = viSTRunOutQty(Ai_Type)
    	if Ai_LotQty < viMinShotSize(Ai_Type,Ai_Press) then begin
    		if viMinShotSize(Ai_Type,Ai_Press) % (viCarrierQty(Ai_Type) * 2) = 0 then
    			set Ai_LotQty = viMinShotSize(Ai_Type,Ai_Press)
    		else
    			set Ai_LotQty = viMinShotSize(Ai_Type,Ai_Press) + viCarrierQty(Ai_Type) * 2 - (viMinShotSize(Ai_Type,Ai_Press) % (viCarrierQty(Ai_Type) * 2)) 
    	end
    end
    else begin
    	 set Ai_LotQty = viSTRunOutQty2(Ai_Type2)
    	 if Ai_LotQty < viMinShotSize2(Ai_Type2,Ai_Press) then
    	 	set Ai_LotQty to viMinShotSize2(Ai_Type2,Ai_Press)
    end
    print Ai_LotQty to lblShotSize(Ai_Press)  
        
	set viVariationTracker(Ai_Press) to 0			
	if Ai_NonSPO = 0 then set Ai_Type2 to Ai_Type			/* Store Standard SPO Type # if variations are included */
    set Ai_Seq = 1
    get vrscDowntime(2+Ai_Press)
    set At_TimeInSys to ac
	while Ai_Seq <= Ai_LotQty and Vi_RunTrigger(Ai_Press) = 0 do begin 
    	
    	if Ai_NonSPO = 0 then begin
    		if viVariationTracker(Ai_Press) < viVariation(Ai_Type2) then begin			/* Check if there are variations needed to run for this SPO */
    			if Ai_Seq > viVariationTarget(Ai_Press) or Ai_Seq = 1 then begin
    				if Ai_Seq > 1 then inc viVariationTracker(Ai_Press) by 1								/* Finished SPO variation run */
    				set Ai_x to 0
    				if viVariationTracker(Ai_Press) = viVariation(Ai_Type2) then								/* All variations finished */
	    			  	set Ai_Type to Ai_Type2																/* Set Ai_Type back to standard SPO */	
    				else begin
    					set Ai_Type to 0
    					while Ai_Type < viVariationCount do begin
	    					inc Ai_Type by 1
    						if viModelV(Ai_Type+20) = Ai_Type2 then begin
    							inc Ai_x by 1
    							if Ai_x > viVariationTracker(Ai_Press) then begin
    								set Ai_Type to Ai_Type + 20
    								set viVariationTarget(Ai_Press) to Ai_Seq + viSTRunOutQty(Ai_Type) - 1
    							end
    						end
    					end
    				end
    				print vsStyle(Ai_Type) to lblPart(Ai_Press)
  					print vrStyleCT(Ai_Type,Ai_Press,viSTRunGen(1) + viSTRunGen(2)) as .2 to lblCycleTime(Ai_Press)
    			end
    		end  
    	end
    	
    	if Ai_NonSPO = 0 then begin
   	  		if Ai_Press = 1 then
   	  			set Ai_pi to nextof(1,2)
   	  		else set Ai_pi to nextof(1,2)
   	  		
   	  		if viSTRunGen(1) + viSTRunGen(2) = 2 then begin			/* Both Presses Running SPOs */
   	  			wait for vrStyleCT(Ai_Type,Ai_Press,2) sec
   	  			print vrStyleCT(Ai_Type,Ai_Press,2) as .2 to lblCycleTime(Ai_Press)
   	  		end
   	  		else if Ai_Press = 1 and viSTRunGen(2) = 0 then	begin	/* Only B-Line Running SPOs */
   	  			wait for vrStyleCT(Ai_Type,Ai_Press,1) sec	
   	  			print vrStyleCT(Ai_Type,Ai_Press,1) as .2 to lblCycleTime(Ai_Press)
   	  		end	
   	  		else if Ai_Press = 2 and viSTRunGen(1) = 0 then begin	/* Only C-Line Running SPOs */
   	  			if pf_ohc:ST_B28 current + pf_ohc:ST_B23 current + pf_ohc:ST_B22 current > 0 then begin
   	  				wait for vrStyleCT(Ai_Type,2,2) sec
   	  				print vrStyleCT(Ai_Type,2,2) as .2 to lblCycleTime(Ai_Press) 
   	  			end
   	  			else begin
   	  				wait for vrStyleCT(Ai_Type,2,1) sec
   	  				print vrStyleCT(Ai_Type,2,1) as .2 to lblCycleTime(Ai_Press)
   	  			end
   	  		end	
   	  	
   	  		if Ai_Press = 1 then
   	  			wait to be ordered on olPressSide(Ai_pi)
   	  		else wait to be ordered on olPressSide(Ai_pi + 2)	
   	  		  	
   	  		clone 1 load to pCarrierLoad  	  
   	  	end
   	 	else begin
   	  		wait for vrStyleCT2(Ai_Type2,Ai_Press) sec
   	  		set Ai_pi to rn stream76 oneof(Vr_Scrap2(Ai_Type2):1,1-Vr_Scrap2(Ai_Type2):2)
   	  		if Ai_pi = 1 then inc Vr_PartCount(2,Ai_Press) by 1
   	  		else inc viInitQty2(Ai_Type2) by 1
   	  		print "TrigQty->", viTriggerQty2(Ai_Type2), "RunQty->", viSTRunOutQty2(Ai_Type2), "Inv->",viInitQty2(Ai_Type2) to Lb_Inv(Ai_Type2)
   	  		print "Model: "vsStyle2(Ai_Type2) " RO: "viSTRunOutQty2(Ai_Type2)" Cur: "Ai_Seq  to lblState(Ai_Press)
   	  	end 	  
  	   	print Ai_Seq  to lblCurrent(Ai_Press)   
  	   	  
      	inc Vr_PartCount(1,Ai_Press) by 1
      	print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
      
      	inc Ai_Seq by 1
     
      	if Ai_Type <= 20 then begin	/* No min shot size tests are needed when SPO variations are being run */
    		if Ai_NonSPO = 1 then begin
    			if Ai_Seq > viMinShotSize2(Ai_Type2,Ai_Press) and vldlstCritInv(Ai_Press) size > 0 then begin
    				remove O_StampingPriority(Ai_Press) load list first from vldlstCritInv(Ai_Press) 	
					order 1 load from O_StampingPriority(Ai_Press) to continue    
      				set Vi_RunTrigger(Ai_Press) to 1	
      			end
    		end
    	
    		else begin    		
    			if Ai_Seq % (2*viCarrierQty(Ai_Type)) = 0 and Ai_Seq > viMinShotSize(Ai_Type,Ai_Press) and vldlstCritInv(Ai_Press) size > 0 then begin	/* Stop Run for Critical Inventory Part (after enough time has passed to set the die) */
					remove O_StampingPriority(Ai_Press) load list first from vldlstCritInv(Ai_Press) 	
					order 1 load from O_StampingPriority(Ai_Press) to continue    
      				set Vi_RunTrigger(Ai_Press) to 1	
      			end
      		end
      	end
      
		if (Ai_Type = 1 or Ai_Type = 2) and Ai_NonSPO = 0 and Ai_Seq = 900 and Vi_SplitSPO = 1 then begin	/* If toggle is on, run half of 4DrAccd SPO run, changeover to nonSPO, then run the other half */
      		if Vi_ScheduleCount(2,Ai_Press) > 0 then begin
	      		order 1 load satisfying Ai_NonSPO = 1 from O_StampingPriority(Ai_Press) to continue
    	  		free vrscDowntime(2+Ai_Press)
     			set Vld_Schedule(Ai_Press) to this load
     			wait to be ordered on O_StampingPriority(Ai_Press)
     			set Vld_Schedule(Ai_Press) to null 		
      			get vrscDowntime(2+Ai_Press)
  				wait for vrDieChangeTime(Ai_Press) min
  			end
  		end
  	end	
  
	set viSTRunGen(Ai_Press) = 0
	if viSTRunGen(1) + viSTRunGen(2) = 2 then
		set vsmPressCondition active state to "TwoPressRunningSpo"
	else if viSTRunGen(1) + viSTRunGen(2) = 1 then
		set vsmPressCondition active state to "OnePressRunningSpo"
	else if viSTRunGen(1) + viSTRunGen(2) = 0 then
		set vsmPressCondition active state to "NoPressRunningSpo"
	
  	free vrscDowntime(2+Ai_Press)
 	 
	if Vi_RunTrigger(Ai_Press) = 1 then begin
  		set Vi_RunTrigger(Ai_Press) = 0
  		if Ai_NonSPO = 0 and viTotalStorageParts(Ai_Type) <= viTriggerQty(Ai_Type) then
  			send to pSTRunControl
  		else if Ai_NonSPO = 1 and viInitQty2(Ai_Type2) <= viTriggerQty2(Ai_Type2) then
  			send to pSTRunControl
  	end
    else begin
  		if Vld_Schedule(Ai_Press) <> null then 
    		order Vld_Schedule(Ai_Press) from O_StampingPriority(Ai_Press) to continue
    	else begin
	  		if C_CarrierCount(2) current < Vi_EmptyReq then begin	
  				if Vi_ScheduleCount(2,Ai_Press) > 0 then begin
  					order 1 load satisfying Ai_NonSPO = 1 from O_StampingPriority(Ai_Press) to continue
  				end
  				else begin
  					set vrscDowntime(2 + Ai_Press) active state to "NoScheduledRuns"
  					print vrscDowntime(2+Ai_Press) active state to lblState(2+Ai_Press)
  					wait to be ordered on O_HoldForCarriers
  				
  					if Ai_pi = 5252 then order 1 load satisfying Ai_NonSPO = 1 from O_StampingPriority(Ai_Press) to continue
  					else order 1 load O_StampingPriority(Ai_Press) to continue case backorder on O_StampingPriority(Ai_Press)
  				end 		
  			end  
  			else begin  
  				if O_StampingPriority(Ai_Press) current = 0 then begin
  					set vrscDowntime(2 + Ai_Press) active state to "NoScheduledRuns"
  					print vrscDowntime(2+Ai_Press) active state to lblState(2+Ai_Press)
  				end
  				order 1 load from O_StampingPriority(Ai_Press) to continue case backorder on O_StampingPriority(Ai_Press)
  			end 
 		end 
  	end
	
	print "" to lblPart(Ai_Press)
  	print "" to lblShotSize(Ai_Press)
  	print "" to lblCurrent(Ai_Press)
  	print "" to lblCycleTime(Ai_Press)
	  
  	if Ai_NonSPO = 1 then dec C_RunControl(Ai_Type2) by 1
  	else dec C_SPOControl(Ai_Type) by 1	 
 
  	if pf_ohc:ST127 current > (pf_ohc:ST127 capacity / 2) and viSTRunGen(1) = 0 and viSTRunGen(2) = 0	and /***Check for Empty Qtys that need flush***/
    	viKickOutQty(1) = 0 and pf_ohc:ST128 remaining space = 0 and pf_ohc:ST134 remaining space = 0 then begin
    	
    	call sAttributeStrip
    	set As_Type = null
    	set Ai_LotNo = 0
    	set Ai_Type = 99
    	inc viKickOutQty(1) by 20
    	if viPressActive(1) = 0 or viPressActive(2) = 0 then
	    	clone 20 loads to pEmptyRecirc nlt L_invis
    	else if oPressSelect current > 0 then 
	    	order 1 load from oPressSelect to pEmptyCarrierLaneSelect
  	end 
end

begin pEmptyRecirc arriving procedure
	/* This process is used for recirc if only one press is running SPOs */
	if viKickOutQty(1) < 20 then
		wait to be ordered on oEmptyRecirc
	set Ai_pi to nextof(1,2)
	if viPressActive(1) = 1 and viPressActive(2) = 0 then begin
		move into vlocptrSTLoad(Ai_pi,3,1)
		travel to pf_ohc:ST_B19
    	wait to be ordered on oLiftClear
    	travel to pf_ohc:ST_LiftDown
		move into kLift:staDown(1)
		travel to kLift:staUp(1)
    	move into pf_ohc:ST_B20
	end
	else if viPressActive(2) = 1 and viPressActive(1) = 0 then
		move into vlocptrSTLoad(Ai_pi,3,2)
	dec viKickOutQty(1) by 1
	order 1 load from oEmptyRecirc to continue
	send to pEmptyCarrierLaneSelect
end

begin pCarrierLoad arriving procedure   
	if Ai_Seq = 99999 then begin		/***Init Units Through Interface***/
		set Ai_pi to 0
		move into pf_ohc:Start
		inc viTotalStorageParts(Ai_Type) by Ai_CarQty
  		print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
		travel to pf_ohc:ST136
		travel to pf_ohc:ST_B28
	end	
	else begin
  		set this load color to vclr_LoadColor(Ai_Type)
  		move into vlocptrSTLoad(Ai_pi,1,Ai_Press)
    	if Ai_Press = 1 then 
    		order 1 load from olPressSide(Ai_pi) to continue case backorder on olPressSide(Ai_pi)
 		else if Ai_Press = 2 then
 			order 1 load from olPressSide(Ai_pi+2) to continue case backorder on olPressSide(Ai_pi+2)
 			
	    travel to vlocptrSTLoad(Ai_pi,2,Ai_Press)
	    
	    if vlptrLoadGraphic(Ai_pi,Ai_Press) <> null then begin		/* Order carrier to continue for new variant type */
			if vlptrLoadGraphic(Ai_pi,Ai_Press) Ai_Type <> this load Ai_Type then begin
		 		order 1 load from volPartKill(Ai_pi,2,Ai_Press) to continue
				wait for 0
			end
		end
	
		if Ai_Press = 1 then begin
			wait for 4 sec
		end
		else if Ai_Press = 2 then begin		   
   			if Ai_pi = 1 then begin
    			move into K_RBT:Rob1Work1(1)
    			travel to K_RBT:Rob1Pounce2(1)
     			if vlocptrSTLoad(Ai_pi,3,Ai_Press) current = 0 then begin
     				set rCLine active state to "NoCarriers"
     				print rCLine active state to lblState(4)
      	 			wait to be ordered on olRobHandshake(Ai_pi)
      	 			set rCLine active state to "Production"
      	 			print rCLine active state to lblState(4)
     			end
     			travel to K_RBT:Rob1Work2(1)
	   		end
   			else begin
    			move into K_RBT:Rob2Work1(2)
    			travel to K_RBT:Rob2Pounce2(2)
     			if vlocptrSTLoad(Ai_pi,3,Ai_Press) current = 0 then begin
     				set rCLine active state to "NoCarriers"
     				print rCLine active state to lblState(4)      				
      				wait to be ordered on olRobHandshake(Ai_pi)
     			    set rCLine active state to "Production"
      	 			print rCLine active state to lblState(4)
      	 		end
     			travel to K_RBT:Rob2Work2(2)
   			end
    	end
    
   		/***Robot Hand Shake should be entered here****/
		inc viPartCount(Ai_pi,Ai_Press) by 1
		
		if vlptrLoadGraphic(Ai_pi,Ai_Press) <> null /* and viPartCount(Ai_pi) < viCarrierQty(Ai_Type) */ then begin
			set vlptrLoadGraphic(Ai_pi,Ai_Press) Ai_CarQty = viPartCount(Ai_pi,Ai_Press)
			scale vlptrLoadGraphic(Ai_pi,Ai_Press) to z vlptrLoadGraphic(Ai_pi, Ai_Press) Ai_CarQty
	  
			move into qTransferTemp(Ai_pi)
	  		if viPartCount(Ai_pi, Ai_Press) < viCarrierQty(Ai_Type) and Ai_LotQty <> Ai_Seq+1 and Ai_LotQty <> Ai_Seq then
	    		wait to be ordered on volPartKill(Ai_pi,1,Ai_Press)
	  		else begin
	  	  		order all loads from volPartKill(Ai_pi,1,Ai_Press) to continue
	  	  		order 1 load from volPartKill(Ai_pi,2,Ai_Press) to continue
	  		end
			send to die
		end
	
		set Vr_CarWait(Ai_pi,Ai_Press) to ac
		set vlptrLoadGraphic(Ai_pi,Ai_Press) = this load
	    set vlptrLoadGraphic(Ai_pi,Ai_Press) Ai_CarQty = viPartCount(Ai_pi, Ai_Press)
		scale vlptrLoadGraphic(Ai_pi,Ai_Press) to z vlptrLoadGraphic(Ai_pi, Ai_Press) Ai_CarQty
    
    	if vlocptrSTLoad(1,3,Ai_Press) current + vlocptrSTLoad(2,3,Ai_Press) current < 2 then begin
	    	set vrscDowntime(2+Ai_Press) active state to "NoCarriers"
	    	print vrscDowntime(2+Ai_Press) active state to lblState(2+Ai_Press)
	    end
	    move into vlocptrSTLoad(Ai_pi,3,Ai_Press)
   		if vrscDowntime(2+Ai_Press) active state = NoCarriers then begin
   			if vlocptrSTLoad(1,3,Ai_Press) current + vlocptrSTLoad(2,3,Ai_Press) current = 2 then begin
   				set vrscDowntime(2+Ai_Press) active state to "Production"
   				print vrscDowntime(2+Ai_Press) active state to lblState(2+Ai_Press)
   			end
   		end
    
    	tabulate (ac-Vr_CarWait(Ai_pi,Ai_Press))/60 in T_CarWait(Ai_Press)
    
    	if Ai_LotQty <> Ai_Seq+1 and Ai_LotQty <> Ai_Seq then
	    	wait to be ordered on volPartKill(Ai_pi,2,Ai_Press)
    	set vlptrLoadGraphic(Ai_pi,Ai_Press) = null
    	set viPartCount(Ai_pi,Ai_Press) = 0
  	
 		dec C_CarrierCount(2) by 1
  		inc C_CarrierCount(1) by 1
 		dec viEmptyLoad(Ai_Press,1) by 1
 		
	/* 	set Vi_x to 1
 		while Vi_x <= viCarrierQty(Ai_Type) do begin
 			set Ai_x to rn stream75 oneof(Vr_Scrap(Ai_Type):1,1-Vr_Scrap(Ai_Type):0)
	    	inc Vr_PartCount(2,Ai_Press) by Ai_x
	    	print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
	    	dec Ai_CarQty by Ai_x
	    	inc Vi_x by 1
 		end	*/
 
  		inc viTotalStorageParts(Ai_Type) by Ai_CarQty
  		print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
  
 	 	if oPressSelect current > 0 and viEmptyLoad(Ai_Press,1) < viEmptyLoad(Ai_Press,2) then begin
		  	set oPressSelect load list first Ai_Press to this load Ai_Press
  			order 1 load from oPressSelect to continue
  		end
  
  		if Ai_Press = 1 then begin
			if Ai_pi = 1 then begin
				if pf_ohc:ST_B8 remaining space + pf_ohc:ST_B9 remaining space = 0 then begin
					if rBLine state Down current = 0 then
						set rBLine active state to "Blocked"
					set viBlocked(Ai_Press, Ai_pi) to 1
				end
			end
			else if Ai_pi = 2 then begin
				if pf_ohc:ST_B12 remaining space = 0 then begin
					if rBLine state Down current = 0 then
						set rBLine active state to "Blocked"
					set viBlocked(Ai_Press, Ai_pi) to 1
				end
			end
			
			travel to pf_ohc:ST_B15
			get rStampingShift
			wait for vrStopTime(6) sec
			set Vi_x to 1
			set Vi_CarQty to Ai_CarQty
	 		while Vi_x <= Vi_CarQty do begin
 				set Ai_x to rn stream43 oneof(Vr_Scrap(Ai_Type)/4:1,1-Vr_Scrap(Ai_Type)/4:0)	/* Half of scrap will occur at two off press inspection points, other half at ST13 */
	    		inc Vr_PartCount(2,Ai_Press) by Ai_x
		    	dec Ai_CarQty by Ai_x
		    	dec viTotalStorageParts(Ai_Type) by Ai_x
		    	inc Vi_x by 1
 			end
 			print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
 			print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
 			
			free rStampingShift
			travel to pf_ohc:ST_B16
			get rStampingShift
			wait for vrStopTime(7) sec
			set Vi_x to 1
			set Vi_CarQty to Ai_CarQty
	 		while Vi_x <= Vi_CarQty do begin
 				set Ai_x to rn stream44 oneof(Vr_Scrap(Ai_Type)/4:1,1-Vr_Scrap(Ai_Type)/4:0)
	    		inc Vr_PartCount(2,Ai_Press) by Ai_x
		    	dec Ai_CarQty by Ai_x
		    	dec viTotalStorageParts(Ai_Type) by Ai_x
		    	inc Vi_x by 1
 			end
 			print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
 			print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
 			
			free rStampingShift
			travel to pf_ohc:ST_LiftDown
			move into kLift:staDown(1)
			travel to kLift:staUp(1)
			move into pf_ohc:ST_B20
			travel to pf_ohc:ST_B23

			if viSTRunGen(2) = 1 then begin
				if viEmptyLoad(2,1) < 11 and pf_ohc:ST_B3 current > 0 then
					wait to be ordered on oST_B27
			
			/*	if pf_ohc:ST_B24 current + pf_ohc:ST_B25 current + pf_ohc:ST_B26 current > 0 then
				if pf_ohc:ST_B28 remaining space = 0 then
					wait to be ordered on oST_B27 */
			end  
			travel to pf_ohc:ST_B27	
			travel to pf_ohc:ST_B28 
			wait for vrStopTime(8) sec
			
  		end 
  		else if Ai_Press =2 then begin
		  	if Ai_pi = 1 then begin
		  		if pf_ohc:ST5_8 remaining space + pf_ohc:ST5_7 remaining space + pf_ohc:ST5_7a remaining space = 0 then begin
					if rCLine state Down current = 0 then
						set rCLine active state to "Blocked"
					set viBlocked(Ai_Press, Ai_pi) to 1
				end
		  		travel to pf_ohc:ST5_8
		  		wait for vrStopTime(2) sec
		  		
		  		set Vi_x to 1
				set Vi_CarQty to Ai_CarQty
	 			while Vi_x <= Vi_CarQty do begin
 					set Ai_x to rn stream45 oneof(Vr_Scrap(Ai_Type)/2:1,1-Vr_Scrap(Ai_Type)/2:0)		/* Half of Scrap occurs at this inspection point, other half at ST13 */
	    			inc Vr_PartCount(2,Ai_Press) by Ai_x
		    		dec Ai_CarQty by Ai_x
		    		dec viTotalStorageParts(Ai_Type) by Ai_x
		    		inc Vi_x by 1
 				end
		  	end
  			else if Ai_pi = 2 then begin
  				if pf_ohc:ST5_9 remaining space + pf_ohc:ST5_4 remaining space + pf_ohc:ST5_3 remaining space = 0 then begin
					if rCLine state Down current = 0 then
						set rCLine active state to "Blocked"
					set viBlocked(Ai_Press, Ai_pi) to 1
				end
  				travel to pf_ohc:ST5_10	
  				wait for vrStopTime(4) sec
  				
  				set Vi_x to 1
				set Vi_CarQty to Ai_CarQty
	 			while Vi_x <= Vi_CarQty do begin
	 				set Ai_x to rn stream46 oneof(Vr_Scrap(Ai_Type)/2:1,1-Vr_Scrap(Ai_Type)/2:0)		/* Half of Scrap occurs at this inspection point, other half at ST13 */
		    		inc Vr_PartCount(2,Ai_Press) by Ai_x
			    	dec Ai_CarQty by Ai_x
		    		dec viTotalStorageParts(Ai_Type) by Ai_x
		    		inc Vi_x by 1
 				end
  			end
 			print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
 			print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
		end
	end
	send to pStorage
end

begin pf_ohc:ST_LiftDown idle procedure
	translate by z 16.5 in vrBPressLiftUp sec
	move into pf_ohc:ST_B20
	translate by z -16.5
end

begin pf_ohc:peLiftClear cleared procedure
	dispatch kLift:kLift(1) to kLift:staDown(1)
end

begin kLift idle procedure
	if this vehicle current location = kLift:staDown(1) then
		order 1 load from oLiftClear to continue case backorder on oLiftClear
end

begin Sr_OtherInv procedure
	set viIndex = 1
	while viIndex <= vi_NumType2 do begin
		if Ai_Type > 20 then begin	/* SPO Variation */
			if viModelV(Ai_Type) = viModelOther(viIndex) then begin
				dec viInitQty2(viIndex) by Ai_CarQty
				print "TrigQty->", viTriggerQty2(viIndex), "RunQty->", viSTRunOutQty2(viIndex), "Inv->",viInitQty2(viIndex) to Lb_Inv(viIndex)
			end
		end
		else begin					/* Normal SPO */
			if this load Ai_Type = viModelOther(viIndex) then begin
				dec viInitQty2(viIndex) by Ai_CarQty
				print "TrigQty->", viTriggerQty2(viIndex), "RunQty->", viSTRunOutQty2(viIndex), "Inv->",viInitQty2(viIndex) to Lb_Inv(viIndex)
			end
		end
		
		if C_RunControl(viIndex) current = 0 and viInitQty2(viIndex) < viTriggerQty2(viIndex)*1.1 then begin
			inc C_RunControl(viIndex) by 1
			set Ai_Press to viOtherPress(viIndex)
			set Ai_NonSPO to 1
			set Ai_Type2 to viIndex
			clone 1 load to pSTRunControl
			wait for 0
			set Ai_NonSPO to 0
			if Ai_Type <= 20 then set Ai_Press to viSpoPress(Ai_Type)
			else set Ai_Press to viSpoPress(viModelV(Ai_Type))
		end	
		
		if viInitQty2(viIndex) < viTriggerQty2(viIndex)/4 then begin	/* Check for parts below 1 hour inventory */	
			for each Vld_Crit in O_StampingPriority(viOtherPress(viIndex)) load list do begin
				if Vld_Crit Ai_NonSPO = 1 and Vld_Crit Ai_Type2 = viIndex and Vld_Crit priority = Vld_Crit Ai_StampingPriority then begin
					set Vld_Crit priority to Vld_Crit Ai_StampingPriority/10
					order Vld_Crit from O_StampingPriority(Vld_Crit Ai_Press) to O_StampingPriority(Vld_Crit Ai_Press)
					insert Vld_Crit into vldlstCritInv(Vld_Crit Ai_Press)
				end
			end
		end
		inc viIndex by 1
	end
	
/*	if O_HoldForCarriers current >= 1 then begin
		set O_HoldForCarriers load list first Ai_pi = 5252
		order 1 load from O_HoldForCarriers to continue
	end  */
end



/***********************************Process for Empty Carrier Routing***************************/
begin pEmptyCarrierReturn arriving procedure

  set Ai_Type = 99
  set As_Type = null
  set Ai_LotNo = 0
  
  if Ai_Seq = 99999 then begin	/*********Init Empty Carriers****************/
	move into pf_ohc:Start
    travel to pf_ohc:ST134
    travel to pf_ohc:ST_B3
    if pf_ohc:ST_B3 total = viReqVeh then
    	order 1 load from oCarrierInit(1) to continue 
    if viPressActive(1) = 1 then begin
    	travel to pf_ohc:ST_B19
    	if pf_ohc:ST_B19 total = 1 then
	    	set Ai_x to 333
	    wait to be ordered on oLiftClear
    	travel to pf_ohc:ST_LiftDown
		move into kLift:staDown(1)
		travel to kLift:staUp(1)
    	move into pf_ohc:ST_B20
    	travel to pf_ohc:ST_B24
    	travel to pf_ohc:ST5_13
   		/* travel to nextof(pf_ohc:ST12_26,pf_ohc:ST12_27,pf_ohc:ST12_28,pf_ohc:ST12_29)*/
	    travel to pf_ohc:ST12_29
	    travel to pf_ohc:W36
	    /*travel to pf_ohc:ST15*/
	    travel to pf_ohc:ST127
	    if Ai_x = 333 then begin
    		wait to be ordered on oCarrierInit(1)
    		set Ai_pi to 1
  			while Ai_pi <= 10 do begin
  				take down vmMotor(Ai_pi)
  				wait for random stream42 e 5 min
  				bring up vmMotor(Ai_pi)
  				inc Ai_pi by 1
  			end
    	end
    	
	  	travel to pf_ohc:ST_B19
    	wait to be ordered on oLiftClear
    	travel to pf_ohc:ST_LiftDown
		move into kLift:staDown(1)
		travel to kLift:staUp(1)
    	move into pf_ohc:ST_B20
    	travel to pf_ohc:ST_B24
    	travel to pf_ohc:ST5_13
    	travel to pf_ohc:ST12_29
    	travel to pf_ohc:W36
    	inc viStorageInit(1) by 1
    	if viStorageInit(1) = C_CarrierCount(2) current then begin
    		set viStorageInit(2) to 1
    		order all loads from oStorageInit to continue
    	end
    end
    else begin
    	travel to pf_ohc:ST_B19
    	if pf_ohc:ST_B19 total = 1 then
	    	set Ai_x to 333
    	wait to be ordered on oLiftClear
    	travel to pf_ohc:ST_LiftDown
		move into kLift:staDown(1)
		travel to kLift:staUp(1)
    	move into pf_ohc:ST_B20
    	travel to pf_ohc:ST_B24
    	if pf_ohc:ST_B24 total = viReqVeh then
    		order 1 load from oCarrierInit(1) to continue 
    	travel to pf_ohc:ST_B28
    	travel to pf_ohc:ST5_13
    	travel to nextof(pf_ohc:ST12_27,pf_ohc:ST12_28,pf_ohc:ST12_29)
    	travel to pf_ohc:W36
    	travel to pf_ohc:ST127
    	if Ai_x = 333 then begin
    		wait to be ordered on oCarrierInit(1)
    		set Ai_pi to 1
  			while Ai_pi <= 10 do begin
  				take down vmMotor(Ai_pi)
  				wait for random stream42 e 5 min
  				bring up vmMotor(Ai_pi)
  				inc Ai_pi by 1
  			end
    	end
    	
    	inc viStorageInit(1) by 1
    	if viStorageInit(1) = C_CarrierCount(2) current then begin
    		set viStorageInit(2) to 1
	    	order all loads from oStorageInit to continue
    	end
    	
/*    	if pf_ohc:ST35 remaining space + pf_ohc:ST136 remaining space > 5 then 
    		travel to pf_ohc:ST136
    	else */travel to pf_ohc:ST134  	
    	
    	travel to pf_ohc:ST_B3
    	if Ai_x= 333 then
    		wait to be ordered on oCarrierInit(1)
    	    	
    	travel to pf_ohc:ST_B24
    	travel to pf_ohc:ST_B28
    	travel to pf_ohc:ST5_13
    	travel to nextof(pf_ohc:ST12_27,pf_ohc:ST12_28,pf_ohc:ST12_29)
    	travel to pf_ohc:W36
	end 
	set Ai_Seq to 0 
  end 

  /***  RAVISH CODE **/
  if Ai_Status = 7777 then begin  /** CARRIERS RELEASED FROM THE WE LINES **/
    if Ai_Line = 1 then begin
      travel to pf_ohc:W35
      wait to be ordered on olLine1Empty
      dec viKickOutQty(3) by 1
      travel to pf_ohc:W36
      if pf_ohc:ST128 remaining space = 0 then
      	set Ai_Status = 1
    end
    else if Ai_Line = 2 then begin
      if pf_ohc:ST124 remaining space + pf_ohc:ST127 remaining space + pf_ohc:ST128 remaining space = 0 and viKickOutQty(1) = 0 and 
      pf_ohc:ST134 remaining space = 0 /*and viSTRunGen(1) = 0 and viSTRunGen(2) = 0*/ then begin
      	call sAttributeStrip
      	set As_Type = null
    	set Ai_Type = 99
/*    	clone 20 loads to pEmptyCarrierLaneSelect nlt L_invis*/
    	inc viKickOutQty(1) by 20
    	if oPressSelect current > 0 then 
    		order 1 load from oPressSelect to pEmptyCarrierLaneSelect
  	  end 
    end     
  end
  
  if this load vehicle current location = pf_ohc:ST13 then begin
  	if pf_ohc:ST128 remaining space = 0 and viKDcount(1) < viKDcount(2) then begin
  		inc viKDcount(1) by 1
  		travel to pf_ohc:ST14
  		travel to pf_ohc:ST10
  		set Ai_y to 1	
  	end		
  	else begin
  		travel to pf_ohc:W36
  		if pf_ohc:ST15 remaining space > 0 then begin
  			travel to pf_ohc:ST15
    		wait to be ordered on olCarKickout
    		dec viKickOutQty(2) by 1
    	end
  		travel to pf_ohc:ST124
  		travel to pf_ohc:ST127
  		travel to pf_ohc:ST128
  		set Ai_y to 0
  	end	
  end
  else begin
  	if Ai_Status = 1 and pf_ohc:ST15 remaining space > 0 then begin
	    travel to pf_ohc:ST15
    	wait to be ordered on olCarKickout
    	dec viKickOutQty(2) by 1
  	end

  	travel to pf_ohc:ST124   
  	travel to pf_ohc:ST127
  	travel to pf_ohc:ST128
  	set Ai_y to 0
  end
  
  if pf_ohc:ST134 remaining space + pf_ohc:ST35 remaining space + pf_ohc:ST136 remaining space = 0 then
  	wait to be ordered on oEmptyCarrierSpace
  if (pf_ohc:ST136 remaining space + pf_ohc:ST35 remaining space) > 0 then travel to pf_ohc:ST136 
  else if pf_ohc:ST134 remaining space > 0 and (pf_ohc:ST35 remaining space + pf_ohc:ST136 remaining space) = 0 then travel to pf_ohc:ST134  
  else travel to pf_ohc:ST136  
  
  /***** INSERT THE BRANCH OFF LOGIC FOR B & C PRESS ******/
  travel to pf_ohc:ST_B3
  if oEmptyCarrierSpace current > 0 then begin
  	if pf_ohc:ST128 current > 0 then order 1 load satisfying Ai_y = 0 from oEmptyCarrierSpace  to continue
  	else order 1 load satisfying Ai_y = 1 from oEmptyCarrierSpace to continue 
  end

  if viPressActive(1) = 1 and viPressActive(2) = 0 then begin		/* B-Line running SPOs while C-Line is not */
  	set Ai_Press to 1
  	inc viEmptyLoad(1,1) by 1
  end
  else if viPressActive(1) = 0 and viPressActive(2) = 1 then begin	/* C-Line running SPOs while B-Line is not */
  	set Ai_Press to 2
  	inc viEmptyLoad(2,1) by 1
  end
  else begin														/* Both presses are running SPOs */
  	if viEmptyLoad(2,1) < viEmptyLoad(2,2) then begin
  		set Ai_Press to 2
  		inc viEmptyLoad(2,1) by 1
  	end
  	else begin
  		if viEmptyLoad(1,1) < viEmptyLoad(1,2) then begin
	  		set Ai_Press to 1
  			inc viEmptyLoad(1,1) by 1
  		end
  		else begin
	  		if viKickOutQty(1) > 0 then begin
  				dec viKickOutQty(1) by 1
  				if viSTRunGen(1) + viSTRunGen(2) > 0 then	/* If space is needed but one of the presses is running, then use extra BLine Storage space */
  					set Ai_Press to 1
  				else send to pEmptyCarrierLaneSelect
  			end
  			else wait to be ordered on oPressSelect
  			inc viEmptyLoad(Ai_Press,1) by 1
  		end
  	end
  end
  
/*  travel to pf_ohc:ST3*/
  
  /*** FOR RELEASING EMPTY CARRIERS FROM ZONES WHEN RUNNING LOW ***/
  
  if pf_ohc:ST134 current < (pf_ohc:ST134 capacity / 2) and pf_ohc:ST15 current < 20 and ac > 86400 then begin  
    if olEmptyHold current > 0 and Vi_ZoneEmptyFlush = 0 then begin
      set Ai_Zone = olEmptyHold load list first Ai_Zone
      set Ai_Row = olEmptyHold load list first Ai_Row          
      for each vlptrEmpty in vldlstLanes(Ai_Zone, Ai_Row) do begin
        inc viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
        set vlptrEmpty Ai_Status = 1
      end
      set vldlstLanes(Ai_Zone,Ai_Row) last Ai_Seq = 9999
      set Vi_ZoneEmptyFlush = 1
      order vldlstLanes(Ai_Zone, Ai_Row) first from olEmptyHold
      /* dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1 */         
    end    
  end
  

  else if pf_ohc:ST134 current < (pf_ohc:ST134 capacity / 2) and viKickOutQty(2) = 0 and ac > 86400 then begin   
    order 20 loads from olCarKickout to continue case backorder on olCarKickout
    set viKickOutQty(2) to 20
  end
  
  if Ai_Press = 1 then begin	/*************** Code For B-Line *******************/
	travel to pf_ohc:ST_B5

 	if pf_ohc:ST_B6 current = 0 then 
  		set Ai_pi to 1
  	else begin 
  		if pf_ohc:ST_B7 current = 0 then
  			set Ai_pi to 2
  		else begin
  			if pf_ohc: ST_B6 remaining space > 0 then 
  				set Ai_pi to 1
  			else begin
  				if pf_ohc:ST_B10 remaining space > 0 then
  					set Ai_pi to 2
  				else set Ai_pi to 1	
			end  			
  		end
  	end
  	
  	travel to vlocptrSTLoad(Ai_pi,4,1)	
  	travel to vlocptrSTLoad(Ai_pi,3,1)
	
	if olRobHandshake(Ai_pi) size > 0 then
   	order 1 load from olRobHandshake(Ai_pi) 
  end  
	
  else if Ai_Press = 2 then begin /*************** Code For C-Line **********************/

	travel to pf_ohc:ST4_3

	if pf_ohc:ST5_5 current = pf_ohc:ST5_5 capacity and pf_ohc:ST5_1 current = pf_ohc:ST5_1 capacity then
    	wait to be ordered on olLoadEntry
	if pf_ohc:ST5_6 current = 0 then
		set Ai_pi = 1
	else begin
		if pf_ohc:ST5_2 current = 0 then
			set Ai_pi = 2
		else begin
			if pf_ohc:ST5_5 current = 0 then
				set Ai_pi = 1
			else set Ai_pi = 2
		end
	end

 /* if pf_ohc:ST5_5 current < pf_ohc:ST5_1 current or pf_ohc:ST5_5 current = 0 then
		set Ai_pi = 1
  	else
	   	set Ai_pi = 2*/
	   	
  	travel to vlocptrSTLoad(Ai_pi,4,2)
  	travel to vlocptrSTLoad(Ai_pi,3,2)
   
  	if olRobHandshake(Ai_pi) size > 0 then
   	order 1 load from olRobHandshake(Ai_pi)
  end	
end

begin pEmptyCarrierManagement arriving procedure
	/* Change to run EmptyCarrierManagement at all planned downtime */
	wait to be ordered on oEmptyCarrierManagement
	set viEmptyBufferAvailable to pf_ohc:ST15 capacity - pf_ohc:ST15 current
	set viEmptyBufferAvailable to viEmptyBufferAvailable + viKDcount(2) - viKDcount(1)
	set viEmptyBufferAvailable to viEmptyBufferAvailable + (pf_ohc:ST136 capacity + pf_ohc:ST35 capacity + pf_ohc:ST134 capacity + pf_ohc:ST128 capacity /* + pf_ohc:ST127 capacity/2*/ ) - 
		(pf_ohc:ST136 current + pf_ohc:ST35 current + pf_ohc:ST134 current + pf_ohc:ST128 current /*+ pf_ohc:ST127 current*/ )
	
/*	while P_WeldShift(1) load list first Ai_Seq = 10 and viEmptyBufferAvailable > 0 do begin */
	if viEmptyBufferAvailable > 0 and olEmptyHold current > 0 then begin
		set Ai_Zone = 1
		while Ai_Zone <= 3 do begin
			set Ai_Row = 1
			while Ai_Row <= viZoneLaneMax(Ai_Zone) and vsWeldStatus = "PlannedDT" do begin
				if vldlstLanes(Ai_Zone, Ai_Row) size > 0 then begin
					if vldlstLanes(Ai_Zone,Ai_Row) first Ai_Type = 99 and vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = 99 then begin         
      					for each vlptrEmpty in vldlstLanes(Ai_Zone, Ai_Row) do begin
        					inc viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
        					set vlptrEmpty Ai_Status = 1
	      				end
    	  				set vldlstLanes(Ai_Zone,Ai_Row) last Ai_Seq = 9999
      					/* set Vi_ZoneEmptyFlush = 1  */
      					order vldlstLanes(Ai_Zone, Ai_Row) first from olEmptyHold
				
						dec viEmptyBufferAvailable by vldlstLanes(Ai_Zone,Ai_Row) size
						wait to be ordered on oEmptyBufferCleanUp
					end
				end
				if viEmptyBufferAvailable > 0 then
					inc Ai_Row by 1
				else break
			end
			if viEmptyBufferAvailable > 0 then 
				inc Ai_Zone by 1
			else break
		end
	end
	send to pEmptyCarrierManagement
end

begin pf_ohc:ST5_5 leaving procedure
  	order 1 load from olLoadEntry to continue
end
begin pf_ohc:ST5_1 leaving procedure
  	order 1 load from olLoadEntry to continue
end

begin pf_ohc:LS_24 blocked procedure
  if pf_ohc:W35 current > (pf_ohc:W35 capacity - 8) and viKickOutQty(3) = 0 then
  begin
    order 20 loads from olLine1Empty to continue case backorder on olLine1Empty
    set viKickOutQty(3) to 20
    
  end
end

/*************Reset all attributes but As_Type & Ai_Type*****************/
begin sAttributeStrip
  set Ai_LotQty = 0
/*  set Ai_LotNo = 0*/
  set Ai_Seq = 0
  set Ai_pi = 0
  set Ai_CarQty = 0
  set Ai_Status = 0
  set Ai_Zone = 0
  set Ai_Row = 0
  set Ai_Line = 0
  set Ai_x = 0
  set Ai_y = 0
  /*set As_Type = null*/
  set As_Message = null
  set As_CurrentLocation = null
  set As_NextLocation = null
end


/***********************************************************************************/
/*************************     STAMPING SHIFT SCHEDULE     *************************/
/***********************************************************************************/
begin P_StampingShift arriving procedure  		 
	wait to be ordered on olWarmUp                                  
  	set viStpDay = 1
  	while viStpDay <= 5 do begin  	
	    set viStpShift = 1            
	    while viStpShift <= 3 do begin		 			    
      		set Ai_Seq = 1                       /** REPRESENTS ELEMENT # IN EACH SHIFT **/
      		bring up rBLine
      		bring up rCLine
      		bring up rStampingShift
      		bring up R_Dummy(3)
      		bring up R_Dummy(4)
	      
    		while Ai_Seq <= 9 do begin        
        		print "ST: Day", viStpDay,"Shift",viStpShift, Ai_Seq,vsStpShift(Ai_Seq,viStpShift) to Lb_StampShiftStatus

        		if Ai_Seq = 2 or Ai_Seq = 4 or Ai_Seq = 6 or Ai_Seq = 8 then begin
          			bring up rBLine
          			bring up rCLine
          			bring up rStampingShift
          			bring up R_Dummy(3)
          			bring up R_Dummy(4)
					
					if viSTRunGen(1) + viSTRunGen(2) = 2 then
						set vsmPressCondition active state to "TwoPressRunningSpo"
					else if viSTRunGen(1) + viSTRunGen(2) = 1 then
						set vsmPressCondition active state to "OnePressRunningSpo"
					else if viSTRunGen(1) + viSTRunGen(2) = 0 then
						set vsmPressCondition active state to "NoPressRunningSpo"
					
						          
          			if Vi_Down(3) = 1 then begin
          				if vlocptrSTLoad(1,2,1) current + vlocptrSTLoad(2,2,1) current > 0 and vlocptrSTLoad(1,3,1) current + vlocptrSTLoad(2,3,1) current < 2 then
							set rBLine active state to "NoCarriers"
						else if rBLine current = 0 then 
 							set rBLine active state to "NoScheduledRuns"	
          				else if viBlocked(1,1) + viBlocked(1,2) > 0 then
 							set rBLine active state to "Blocked"
          				else set rBLine active state to "Production"
          			end
          			else if Vi_Down(3) = 0 then set rBLine active state to "Down"
	          
    				if Vi_Down(4) = 1 then begin
    					if vlocptrSTLoad(1,2,2) current + vlocptrSTLoad(2,2,2) current > 0 and vlocptrSTLoad(1,3,2) current + vlocptrSTLoad(2,3,2) current < 2 then
    						set rCLine active state to "NoCarriers"
    					else if rCLine current = 0 then 
 							set rCLine active state to "NoScheduledRuns"
    					else if viBlocked(2,1) + viBlocked(2,2) > 0 then
 							set rCLine active state to "Blocked"
    					else set rCLine active state to "Production"
        			end
        			else if Vi_Down(4) = 0 then set rCLine active to "Down"
        		end	
        
				else if Ai_Seq = 1 or Ai_Seq = 3 or Ai_Seq = 5 or Ai_Seq = 7 or Ai_Seq = 9 then begin
					take down rBLine
					take down rCLine
					take down rStampingShift
					take down R_Dummy(3)
					take down R_Dummy(4)
					set rBLine active state to "PlannedDT"
					set rCLine active state to "PlannedDT"
					set vsmPressCondition active state to "PlannedDT"
        		end
        
        		print vrscDowntime(3) active state to lblState(3)
        		print vrscDowntime(4) active state to lblState(4)
                 
        		wait for vrStpShiftDuration(Ai_Seq,viStpShift) min
        		inc Ai_Seq by 1
      		end /**----------------------------------------------- END OF ELEMENTS LOOP **/   
      		inc viStpShift by 1	
    	end /**------------------------------------------- END OF # SHIFTS WHILE LOOP **/
		inc viStpDay by 1
		if viStpDay = 6 then set viStpDay = 1
	end  /**----------------------------------------- END NUMBER OF DAYS WHILE LOOP **/
end /**------------------------------------------------  END STAMPING SHIFT LOGIC **/
/***********************************************************************************/

/***********************************************************************************/
/*************************       WELD SHIFT SCHEDULE       *************************/
/***********************************************************************************/
begin P_WeldShift arriving procedure  
  	wait to be ordered on olWarmUp
  	if procindex = 1 then print "Week" "\t" "Day" "\t" "Shift" "\t" "Weld L1" "\t" "Weld L2" to "base.arc/RESULTS/out.dat"
 
  	set viWeek(procindex) = 1
  	set viWeldDay(procindex) = 1
  	while viWeldDay(procindex) <= 5 do begin     
	    set viWeldShift(procindex) = 1                 
		while viWeldShift(procindex) <= 2 do begin  
      		order all loads from O_WeldOutputTarget to continue
      		set Ai_Seq = 1                      /** REPRESENTS ELEMENT # IN EACH SHIFT **/
      		bring up R_Dummy(procindex)
      		bring up R_Weld(procindex)
      		bring up R_WeldShift(procindex)
      
			while Ai_Seq <= 10 do begin  							   /** ELEMENTS LOOP **/
		        if Ai_Seq = 1 then begin
          			if procindex = 1 then begin
          				set Vi_shiftProd(1) = 0               /** RESET SHIFT PRODUCTION STATS **/   
          				set Vi_shiftProd(2) = 0               /** RESET SHIFT PRODUCTION STATS **/   
          			end
          			if procindex = 2 then begin
          				set Vi_shiftProd(3) = 0               /** RESET SHIFT PRODUCTION STATS **/   
          				set Vi_shiftProd(4) = 0               /** RESET SHIFT PRODUCTION STATS **/   
          			end
        		end
        
        		print "SPO: W", viWeek(procindex),"D", viWeldDay(procindex),"Shift", viWeldShift(procindex), Ai_Seq,vsWeldShift(Ai_Seq,viWeldShift(procindex),procindex) to Lb_WeldShiftStatus(procindex)
        		
        		if Ai_Seq = 2 or Ai_Seq = 4 or Ai_Seq = 6 or Ai_Seq = 8 then begin
        			if procindex = 1 then bring up rST13Inspector
        			bring up R_Weld(procindex)
	        		bring up R_Dummy(procindex)
    	    		bring up R_WeldShift(procindex)
    	    		set vsWeldStatus to "Production"
        			if Vi_Down(procindex) = 1 then begin
        				if procindex = 1 then begin
        					if O_WeldBufferStarve(1) current + O_WeldBufferStarve(2) current > 0 then begin
        						set R_Weld(procindex) active state to "Starved"
        						set R_LineStarved(procindex) active state to "Starved"
        					end	
        					else begin
        						set R_Weld(procindex) active state to "Production"
        						set R_LineStarved(procindex) active state to "Production"
        					end
        				end
        				if procindex = 2 then begin
        					if O_WeldBufferStarve(3) current + O_WeldBufferStarve(4) current > 0 then begin
        						set R_Weld(procindex) active state to "Starved"
        						set R_LineStarved(procindex) active state to "Starved"
        					end
        					else begin
        						set R_Weld(procindex) active state to "Production"
        						set R_LineStarved(procindex) active state to "Production"
        					end
        				end
        			end
        			else if Vi_Down(procindex) = 0 then begin
        				set R_Weld(procindex) active state to "Down"
        				set R_LineStarved(procindex) active state to "Down"
        			end
        		end
        		else if Ai_Seq = 1 or Ai_Seq = 3 or Ai_Seq = 5 or Ai_Seq = 7 or Ai_Seq = 9 then begin
        			if procindex = 1 then begin
        				take down rST13Inspector
        				order 1 load from oEmptyCarrierManagement to continue
        			end
        			take down R_Weld(procindex)
        			take down R_Dummy(procindex)
        			take down R_WeldShift(procindex)
        			set vsWeldStatus to "PlannedDT"
        			set R_Weld(procindex) active state to "PlannedDT"
        			set R_LineStarved(procindex) active state to "PlannedDT"
        		end
        		else if Ai_Seq = 10 and procindex = 1 then begin
        			bring up rST13Inspector
        		/*	order 1 load from oEmptyCarrierManagement to continue*/
        		end
        		
        		print vrscDowntime(1) active state to lblState(1)
        		print vrscDowntime(2) active state to lblState(2)
        
        		wait for vrWeldShiftDuration(Ai_Seq,viWeldShift(procindex),procindex) min
         
		        if Ai_Seq = 10 and procindex = 1 then begin
         			print viWeek(procindex) "\t" viWeldDay(procindex) "\t" viWeldShift(procindex) "\t" Vi_shiftProd(1) "\t" Vi_shiftProd(3) "\t" to "base.arc/RESULTS/out.dat"
         			tabulate Vi_shiftProd(1) in T_Output(1)
         			tabulate Vi_shiftProd(2) in T_Output(2)
         			tabulate Vi_shiftProd(3) in T_Output(3)
         			tabulate Vi_shiftProd(4) in T_Output(4)
	         	
         			if Vi_shiftProd(1) < Vi_WeldOutputTarget(1) then
		         		set Vi_WeldGap(1) to Vi_WeldGap(1) + (Vi_WeldOutputTarget(1) - Vi_shiftProd(1))
    		     	else if Vi_shiftProd(1) > Vi_WeldOutputTarget(1) then
        		 		set Vi_WeldGap(1) to Vi_WeldGap(1) - (Vi_shiftProd(1) - Vi_WeldOutputTarget(1))
         	
	         		if Vi_shiftProd(2) < Vi_WeldOutputTarget(1) then
    	     			set Vi_WeldGap(2) to Vi_WeldGap(2) + (Vi_WeldOutputTarget(1) - Vi_shiftProd(2))
         			else if Vi_shiftProd(2) > Vi_WeldOutputTarget(1) then
		         		set Vi_WeldGap(2) to Vi_WeldGap(2) - (Vi_shiftProd(2) - Vi_WeldOutputTarget(1)) 	
      
    		        if Vi_shiftProd(3) < Vi_WeldOutputTarget(2) then
        		 		set Vi_WeldGap(3) to Vi_WeldGap(3) + (Vi_WeldOutputTarget(2) - Vi_shiftProd(3))
         			else if Vi_shiftProd(3) > Vi_WeldOutputTarget(2) then
	         			set Vi_WeldGap(3) to Vi_WeldGap(3) - (Vi_shiftProd(3) - Vi_WeldOutputTarget(2))    	
        	
	    	     	if Vi_shiftProd(4) < Vi_WeldOutputTarget(2) then
    	    	 		set Vi_WeldGap(4) to Vi_WeldGap(4) + (Vi_WeldOutputTarget(2) - Vi_shiftProd(4))
	    	     	else if Vi_shiftProd(4) > Vi_WeldOutputTarget(2) then
    	    	 		set Vi_WeldGap(4) to Vi_WeldGap(4) - (Vi_shiftProd(4) - Vi_WeldOutputTarget(2))         	
         	
        	 		tabulate Vi_WeldGap(1) in T_WeldGap(1)
         			tabulate Vi_WeldGap(2) in T_WeldGap(2)
         			tabulate Vi_WeldGap(3) in T_WeldGap(3)
         			tabulate Vi_WeldGap(4) in T_WeldGap(4)
         	
	         		tabulate Vi_Changeover(1) in T_Changeover(1)
    	     		set Vi_Changeover(1) to 0         
    				tabulate Vi_Changeover(2) in T_Changeover(2)
         			set Vi_Changeover(2) to 0
         			
         			tabulate viRecircCount in tRecircCount
         			set viRecircCount to 0
         			tabulate viABayCleanUp in tABayCleanUp
         			set viABayCleanUp to 0
    		
    				if Vi_shiftProd(1) = 0 or Vi_shiftProd(2) = 0 or Vi_shiftProd(3) = 0 or Vi_shiftProd(4) = 0 then dec C_Error by 1	
        		end
        		inc Ai_Seq by 1 
			end /**----------------------------------------------- END OF ELEMENTS LOOP **/
		inc viWeldShift(procindex) by 1         
		end /**------------------------------------------- END OF # SHIFTS WHILE LOOP **/	
		
		inc viWeldDay(procindex) by 1
		if procindex = 1 then inc viTotalDay by 1
    	if viWeldDay(procindex) = 6 then begin
    		inc viWeek(procindex) by 1
    		set viWeldDay(procindex) = 1
    	end
	end  /**----------------------------------------- END NUMBER OF DAYS WHILE LOOP **/
end /**----------------------------------------------------  END WELD SHIFT LOGIC **/
/***********************************************************************************/

begin pNonSpoPartMgt arriving procedure
	wait to be ordered on olWarmUp
	while 1 = 1 do begin
		get R_WeldShift(1)
		wait for 1 hr
		free R_WeldShift(1)
		set Ai_Seq = 1
		while Ai_Seq <= 50 do begin
			if viModelOther(Ai_Seq) = procindex then begin
				dec viInitQty2(Ai_Seq) by viDailyTotal(procindex)/((viProductionMin(1,1)+viProductionMin(2,1))/60)
				print "TrigQty->", viTriggerQty2(Ai_Seq), "RunQty->", viSTRunOutQty2(Ai_Seq), "Inv->",viInitQty2(Ai_Seq) to Lb_Inv(Ai_Seq)
			end
			inc Ai_Seq by 1
		end
	end
end

begin pWarmUp arriving procedure
	wait for 24 hr
	order all loads from olWarmUp to continue
	print "Warm-Up Complete" to message
end

/*******Model init for constants******/
begin model initialization function
  	set viNumVeh = 0
  	set viNumZones = 3
  	set vlocptrLane(1,1) = pf_ohc:ST12_1
  	set vlocptrLane(1,2) = pf_ohc:ST12_2
  	set vlocptrLane(1,3) = pf_ohc:ST12_3
  	set vlocptrLane(1,4) = pf_ohc:ST12_4
  	set vlocptrLane(1,5) = pf_ohc:ST12_5
  	set vlocptrLane(1,6) = pf_ohc:ST12_6
  	set vlocptrLane(1,7) = pf_ohc:ST12_7
  	set vlocptrLane(1,8) = pf_ohc:ST12_8
  	set vlocptrLane(1,9) = pf_ohc:ST12_9
  	set vlocptrLane(1,10) = pf_ohc:ST12_10
  	set vlocptrLane(1,11) = pf_ohc:ST12_11
  	set vlocptrLane(1,12) = pf_ohc:ST12_12
  	set vlocptrLane(1,13) = pf_ohc:ST12_13
  	set vlocptrLane(1,14) = pf_ohc:ST12_14
  	set vlocptrLane(1,15) = pf_ohc:ST12_15
  	set vlocptrLane(1,16) = pf_ohc:ST12_16
  	set vlocptrLane(1,17) = pf_ohc:ST12_17
  	set vlocptrLane(1,18) = pf_ohc:ST12_18
  	set vlocptrLane(1,19) = pf_ohc:ST12_19
  	set vlocptrLane(1,20) = pf_ohc:ST12_20
  	set viZoneLaneMax(1) = 20
  	set vlocptrLane(3,9) = pf_ohc:ST12_21
  	set vlocptrLane(3,1) = pf_ohc:ST12_22
  	set vlocptrLane(3,2) = pf_ohc:ST12_23
  	set vlocptrLane(3,3) = pf_ohc:ST12_24
  	set vlocptrLane(3,4) = pf_ohc:ST12_25
  	set vlocptrLane(3,5) = pf_ohc:ST12_26
  	set vlocptrLane(3,6) = pf_ohc:ST12_27
  	set vlocptrLane(3,7) = pf_ohc:ST12_28
  	set vlocptrLane(3,8) = pf_ohc:ST12_29
  	set viZoneLaneMax(3) = 9
  	set vlocptrLane(2,1) = pf_ohc:ST12_30
  	set vlocptrLane(2,2) = pf_ohc:ST12_31
  	set vlocptrLane(2,3) = pf_ohc:ST12_32
  	set vlocptrLane(2,4) = pf_ohc:ST12_33
  	set vlocptrLane(2,5) = pf_ohc:ST12_34
  	set vlocptrLane(2,6) = pf_ohc:ST12_35
  	set viZoneLaneMax(2) = 6
  
    set vsStation(1,1) = "12_1"
  	set vsStation(1,2) = "12_2"
  	set vsStation(1,3) = "12_3"
  	set vsStation(1,4) = "12_4"
  	set vsStation(1,5) = "12_5"
  	set vsStation(1,6) = "12_6"
  	set vsStation(1,7) = "12_7"
  	set vsStation(1,8) = "12_8"
  	set vsStation(1,9) = "12_9"
  	set vsStation(1,10) = "12_10"
  	set vsStation(1,11) = "12_11"
  	set vsStation(1,12) = "12_12"
  	set vsStation(1,13) = "12_13"
  	set vsStation(1,14) = "12_14"
  	set vsStation(1,15) = "12_15"
  	set vsStation(1,16) = "12_16"
  	set vsStation(1,17) = "12_17"
  	set vsStation(1,18) = "12_18"
  	set vsStation(1,19) = "12_19"
  	set vsStation(1,20) = "12_20"
  	set vsStation(3,9) = "12_21"
  	set vsStation(3,1) = "12_22"
  	set vsStation(3,2) = "12_23"
  	set vsStation(3,3) = "12_24"
  	set vsStation(3,4) = "12_25"
  	set vsStation(3,5) = "12_26"
  	set vsStation(3,6) = "12_27"
  	set vsStation(3,7) = "12_28"
  	set vsStation(3,8) = "12_29"
  	set vsStation(2,1) = "12_30"
  	set vsStation(2,2) = "12_31"
  	set vsStation(2,3) = "12_32"
  	set vsStation(2,4) = "12_33"
  	set vsStation(2,5) = "12_34"
  	set vsStation(2,6) = "12_35"
 
  	/* Configuration for B_Line */
  	set vlocptrSTLoad(1,1,1) = C_Press:sta7
  	set vlocptrSTLoad(1,2,1) = C_Press:sta8
	set vlocptrSTLoad(1,3,1) = pf_ohc:ST_B7  
  	set vlocptrSTLoad(1,4,1) = pf_ohc:ST_B6  
  	set vlocptrSTLoad(2,1,1) = C_Press:sta9
  	set vlocptrSTLoad(2,2,1) = C_Press:sta10
  	set vlocptrSTLoad(2,3,1) = pf_ohc:ST_B11
  	set vlocptrSTLoad(2,4,1) = pf_ohc:ST_B10 
  
  	/* Configuration for C-Line */
  	set vlocptrSTLoad(1,1,2) = C_Press:sta1
  	set vlocptrSTLoad(1,2,2) = C_Press:sta2
  	set vlocptrSTLoad(1,3,2) = pf_ohc:ST5_6  
  	set vlocptrSTLoad(1,4,2) = pf_ohc:ST5_5  
  	set vlocptrSTLoad(2,1,2) = C_Press:sta4
  	set vlocptrSTLoad(2,2,2) = C_Press:sta5
  	set vlocptrSTLoad(2,3,2) = pf_ohc:ST5_2
  	set vlocptrSTLoad(2,4,2) = pf_ohc:ST5_1  
  
  	set volPartKill(1,1,1) = olPartKill(1)
  	set volPartKill(1,2,1) = olPartKill(2)
  	set volPartKill(2,1,1) = olPartKill(3)
  	set volPartKill(2,2,1) = olPartKill(4)
  	set volPartKill(1,1,2) = olPartKill(5)
  	set volPartKill(1,2,2) = olPartKill(6)
  	set volPartKill(2,1,2) = olPartKill(7)
  	set volPartKill(2,2,2) = olPartKill(8)
  
  	set Vloc_Partial(1,1) to pf_ohc:W27    /** ER **/
  	set Vloc_Partial(1,2) to pf_ohc:W28
  	set Vloc_Partial(2,1) to pf_ohc:W31
  	set Vloc_Partial(2,2) to pf_ohc:W33
  	set Vloc_Partial(3,1) to pf_ohc:Par2
  	set Vloc_Partial(3,2) to pf_ohc:Par1
  	set Vloc_Partial(4,1) to pf_ohc:Par4
  	set Vloc_Partial(4,2) to pf_ohc:Par3

  	set Vloc_Unload(1) to pf_ohc:W26   /** WE SPO UNLOAD LOCATIONS **/
  	set Vloc_Unload(2) to pf_ohc:W22
  	set Vloc_Unload(3) to pf_ohc:ST109
  	set Vloc_Unload(4) to pf_ohc:ST119
	
  	set Vloc_B4Unload(1) to pf_ohc:W25  /** WE SPO B4 UNLOAD **/
  	set Vloc_B4Unload(2) to pf_ohc:W21
  	set Vloc_B4Unload(3) to pf_ohc:ST108
  	set Vloc_B4Unload(4) to pf_ohc:ST118
  
  	set Vo_PartialBuffer(1,1) to O_PartialBuffer(1)
  	set Vo_PartialBuffer(1,2) to O_PartialBuffer(2)
  	set Vo_PartialBuffer(2,1) to O_PartialBuffer(3)
  	set Vo_PartialBuffer(2,2) to O_PartialBuffer(4)
  	set Vo_PartialBuffer(3,1) to O_PartialBuffer(5)
  	set Vo_PartialBuffer(3,2) to O_PartialBuffer(6)
  	set Vo_PartialBuffer(4,1) to O_PartialBuffer(7)
 	set Vo_PartialBuffer(4,2) to O_PartialBuffer(8)

  	set Vloc_LoadRBT(1,1) to K_RBT:Rob3Home(3)
  	set Vloc_LoadRBT(1,2) to K_RBT:Rob3Pounce1(3)
  	set Vloc_LoadRBT(1,3) to K_RBT:Rob3Work1(3)
  	set Vloc_LoadRBT(1,4) to K_RBT:Rob3Clear1(3)
  	set Vloc_LoadRBT(1,5) to K_RBT:Rob3Pounce2(3)
  	set Vloc_LoadRBT(1,6) to K_RBT:Rob3Work2(3)
  	set Vloc_LoadRBT(1,7) to K_RBT:Rob3Clear2(3)

  	set Vloc_LoadRBT(2,1) to K_RBT:Rob4Home(4)
  	set Vloc_LoadRBT(2,2) to K_RBT:Rob4Pounce1(4)
  	set Vloc_LoadRBT(2,3) to K_RBT:Rob4Work1(4)
  	set Vloc_LoadRBT(2,4) to K_RBT:Rob4Clear1(4)
  	set Vloc_LoadRBT(2,5) to K_RBT:Rob4Pounce2(4)
  	set Vloc_LoadRBT(2,6) to K_RBT:Rob4Work2(4)
  	set Vloc_LoadRBT(2,7) to K_RBT:Rob4Clear2(4)

  	set Vloc_LoadRBT(3,1) to K_RBT:Rob5Home(5)
  	set Vloc_LoadRBT(3,2) to K_RBT:Rob5Pounce1(5)
  	set Vloc_LoadRBT(3,3) to K_RBT:Rob5Work1(5)
  	set Vloc_LoadRBT(3,4) to K_RBT:Rob5Clear1(5)
  	set Vloc_LoadRBT(3,5) to K_RBT:Rob5Pounce2(5)
  	set Vloc_LoadRBT(3,6) to K_RBT:Rob5Work2(5)
  	set Vloc_LoadRBT(3,7) to K_RBT:Rob5Clear2(5)
	
  	set Vloc_LoadRBT(4,1) to K_RBT:Rob6Home(6)
  	set Vloc_LoadRBT(4,2) to K_RBT:Rob6Pounce1(6)
  	set Vloc_LoadRBT(4,3) to K_RBT:Rob6Work1(6)
  	set Vloc_LoadRBT(4,4) to K_RBT:Rob6Clear1(6)
  	set Vloc_LoadRBT(4,5) to K_RBT:Rob6Pounce2(6)
  	set Vloc_LoadRBT(4,6) to K_RBT:Rob6Work2(6)
	set Vloc_LoadRBT(4,7) to K_RBT:Rob6Clear2(6)

  	set Vo_RBT(1,1) to O_RBT(1)
  	set Vo_RBT(1,2) to O_RBT(2)
  	set Vo_RBT(1,3) to O_RBT(3)
  	set Vo_RBT(2,1) to O_RBT(4)
  	set Vo_RBT(2,2) to O_RBT(5)
  	set Vo_RBT(2,3) to O_RBT(6)
  	set Vo_RBT(3,1) to O_RBT(7)
  	set Vo_RBT(3,2) to O_RBT(8)
  	set Vo_RBT(3,3) to O_RBT(9)
  	set Vo_RBT(4,1) to O_RBT(10)
  	set Vo_RBT(4,2) to O_RBT(11)
  	set Vo_RBT(4,3) to O_RBT(12)
	
  	set Vo_SPO(1,1) to O_SPO(1)
  	set Vo_SPO(1,2) to O_SPO(2)
  	set Vo_SPO(1,3) to O_SPO(3)
  	set Vo_SPO(2,1) to O_SPO(4)
  	set Vo_SPO(2,2) to O_SPO(5)
  	set Vo_SPO(2,3) to O_SPO(6)
  	set Vo_SPO(3,1) to O_SPO(7)
  	set Vo_SPO(3,2) to O_SPO(8)
  	set Vo_SPO(3,3) to O_SPO(9)
	set Vo_SPO(4,1) to O_SPO(10)
  	set Vo_SPO(4,2) to O_SPO(11)
  	set Vo_SPO(4,3) to O_SPO(12)

	set Vi_Count(1) = 1
	set Vi_Count(3) = 1
	while Vi_Count(1) <= 2 do begin
		set Vi_Count(2) = 1
		while Vi_Count(2) <= 30 do begin
			set Vproc_ReleasePartial(Vi_Count(1),Vi_Count(2)) = P_ReleasePartial(Vi_Count(3))
			set Vrsrc_ReleasePartial(Vi_Count(1),Vi_Count(2)) = R_ReleasePartial(Vi_Count(3))
			set Vo_ReleasePartial(Vi_Count(1),Vi_Count(2)) = O_ReleasePartial(Vi_Count(3))
			inc Vi_Count(2) by 1
			inc Vi_Count(3) by 1
		end
		inc Vi_Count(1) by 1
	end
  
  	set Vstrm_DT(1) to stream100
  	set Vstrm_DT(2) to stream101
  	set Vstrm_DT(3) to stream102
  	set Vstrm_DT(4) to stream103
  	set Vstrm_DT(5) to stream104
  	set Vstrm_DT(6) to stream105
  	set Vstrm_DT(7) to stream77
	set Vstrm_DT(8) to stream78
  	set Vstrm_DT(9) to stream79
	set Vstrm_DT(10) to stream80
	
	set vmMotor(1) to pf_ohc:M_chain1
	set vmMotor(2) to pf_ohc:M_chain2
	set vmMotor(3) to pf_ohc:M_chain3
	set vmMotor(4) to pf_ohc:M_chain4
	set vmMotor(5) to pf_ohc:M_chain5
	set vmMotor(6) to pf_ohc:M_chain6
	set vmMotor(7) to pf_ohc:M_chain7
	set vmMotor(8) to pf_ohc:M_chain8
	set vmMotor(9) to pf_ohc:M_chain9
	set vmMotor(10) to pf_ohc:M_chain10 
	return 1
end


begin model finished function
	print "Weld Production" to "base.arc/RESULTS/avg.out"
	print "Weld Line 1 " "\t" T_Output(1) relative average as .2 /*"\t" T_Output(2) relative average as .2*/ to "base.arc/RESULTS/avg.out"
	print "Weld Line 2 " "\t" T_Output(3) relative average as .2 /*"\t" T_Output(4) relative average as .2*/ to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
/*	print "Downtime" "/t" "Uptime" "/t" "MTBF" "/t" "MTTR" to "base.arc/RESULTS/avg.out" 
	print "Weld Line 1" "/t" (T_MTBF(1) average/(T_MTBF(1) average + T_MTTR(1) average)) as .4 "/t" T_MTBF(1) average .as .2 "/t" T_MTTR(1) average as .2 to "base.arc/RESULTS/avg.out"
	print "Weld Line 2" "/t" T_MTBF(2) average/(T_MTBF(2) average + T_MTTR(2) average) as .4 "/t" T_MTBF(2) average .as .2 "/t" T_MTTR(2) average as .2 to "base.arc/RESULTS/avg.out"
	print "B Line Press" "/t" T_MTBF(3) average/(T_MTBF(3) average + T_MTTR(3) average) as .4 "/t" T_MTBF(3) average .as .2 "/t" T_MTTR(3) average as .2 to "base.arc/RESULTS/avg.out"
	print "C Line Press" "/t" T_MTBF(4) average/(T_MTBF(4) average + T_MTTR(4) average) as .4 "/t" T_MTBF(4) average .as .2 "/t" T_MTTR(4) average as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"*/
	
	print "Avg Carrier Wait Time BLine:" "\t" T_CarWait(1) relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Carrier Wait Time CLine:" "\t" T_CarWait(2) relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Daily Changeovers BLine:" "\t" T_Changeover(1) relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Daily Changeovers CLine:" "\t" T_Changeover(2) relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Recirculations/Shift:" "\t" tRecircCount relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Early Pulls for A-Bay Cleanup:" "\t" tABayCleanUp relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Mixed Lanes:" "\t" C_ZoneMix(1) relative average as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Clean Lanes:" "\t" C_ZoneMix(2) relative average as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
	print "Carriers" "\t" "Average" "\t" "Max" "\t" "Min"  to "base.arc/RESULTS/avg.out"
	print "Loaded:" "\t" C_CarrierCount(1) relative average as .2 "\t" C_CarrierCount(1) relative maximum as .2 "\t" C_CarrierCount(1) relative minimum as .2 to "base.arc/RESULTS/avg.out"
	print "Empty:" "\t" C_CarrierCount(2) relative average as .2 "\t" C_CarrierCount(2) relative maximum as .2 "\t" C_CarrierCount(2) relative minimum as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
	print "B Line Press States" to "base.arc/RESULTS/avg.out"
	print "Production" "\t" vrscDowntime(3) state Production relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "DieChange" "\t" vrscDowntime(3) state DieChange relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "NoScheduledRuns" "\t" vrscDowntime(3) state NoScheduledRuns relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "NoCarriers" "\t" vrscDowntime(3) state NoCarriers relative  average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "Blocked" "\t" vrscDowntime(3) state Blocked relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "Down" "\t" vrscDowntime(3) state Down relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "PlannedDT" "\t" vrscDowntime(3) state PlannedDT relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
	print "C Line Press States" to "base.arc/RESULTS/avg.out"
	print "Production" "\t" vrscDowntime(4) state Production relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "DieChange" "\t" vrscDowntime(4) state DieChange relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "NoScheduledRuns" "\t" vrscDowntime(4) state NoScheduledRuns relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "NoCarriers" "\t" vrscDowntime(4) state NoCarriers relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "Blocked" "\t" vrscDowntime(4) state Blocked relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "Down" "\t" vrscDowntime(4) state Down relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "PlannedDT" "\t" vrscDowntime(4) state PlannedDT relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
	print "Weld Line 1 Starved" to "base.arc/RESULTS/avg.out"
	set R_LineStarved(1) active state to "PlannedDT"
	set vrStateTemp to R_LineStarved(1) relative state average
	set R_LineStarved(1) active state to "Starved" 
	print "Starved/Day" "\t" R_LineStarved(1) state relative total/viTotalDay as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Starved Time (min)" "\t" R_LineStarved(1) state relative average time/60 as .2 to "base.arc/RESULTS/avg.out"
	print "Starved %" "\t" R_LineStarved(1) state relative average/(1-vrStateTemp) * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
	print "Weld Line 2 Starved" to "base.arc/RESULTS/avg.out"
	set R_LineStarved(2) active state to "PlannedDT"
	set vrStateTemp to R_LineStarved(2) state relative average
	set R_LineStarved(2) active state to "Starved"
	print "Starved/Day" "\t" R_LineStarved(2) state relative total/viTotalDay as .2 to "base.arc/RESULTS/avg.out"
	print "Avg Starved Time (min)" "\t" R_LineStarved(2) state relative average time/60 as .2 to "base.arc/RESULTS/avg.out"
	print "Starved %" "\t" R_LineStarved(2) state relative average/(1-vrStateTemp) * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "" to "base.arc/RESULTS/avg.out"
	
	print "Press Running Conditions" to "base.arc/RESULTS/avg.out" 
	print "Two Presses Running Spo" "\t" vsmPressCondition state TwoPressRunningSpo relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "One Press Running Spo" "\t" vsmPressCondition state OnePressRunningSpo relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "No Presses Running Spo" "\t" vsmPressCondition state NoPressRunningSpo relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	print "Planned DT" "\t" vsmPressCondition state PlannedDT relative average * 100 as .2 to "base.arc/RESULTS/avg.out"
	return 1
end


/*************************Bring OH Carriers Into system**********************/
begin pf_ohc task search function
  if Ai_Status = 9999 then begin
  	set Ai_Status = 0
  	wait to be ordered on O_VehicleStart	
  	inc viNumVeh by 1
  	if viNumVeh > viReqVeh then begin
	 	move into qVehOverflow
	 	wait on olVehOverflow
  	end
  end
end 

begin pf_ohc initialization function
	set theVehicle Ai_Status = 9999
	return true
end

begin F_getID function
	print funcload to Vs_str
	read Vs_arrstr from Vs_str with delimiter " "
	set funcload Ai_ID to Vs_arrstr(2)
	return true
end


/*******************************************************************************************/
/**                          SUB ROUTINE TO RESET ALL ATTRIBUTE VALUES                    **/
/*******************************************************************************************/
begin SR_AttributeReset procedure
  set this load Ai_pi = 0
  set this load Ai_CarQty = 0
  set this load Ai_Line = 0
  set this load Ai_LotQty = 0
  set this load Ai_Seq = 0
  set this load Ai_Status = 0
  set this load Ai_Type = 0
  set this load As_Type = null 
  set this load At_TimeInSys = 0
  return
end /***************************************************************************************/
/*******************************************************************************************/

/*******************************************************************************************/
/**                 SUB ROUTINE TO SET TYPE INDEX VALUES BASED ON As_Type                 **/
/*******************************************************************************************/
begin SR_TypeIndex procedure
	set viIndex = 1
	while viIndex <= 30 do begin
		if this load As_Type = vsStyle(viIndex) then 
			set Ai_Type = viIndex
		/*if viIndex <= 10 then begin
			if this load As_Type = vsStyleV(viIndex) then
				set Ai_Type = viIndex + 20
		end*/
		inc viIndex by 1
	end  
	if Ai_Type = 0 then begin
		print "WRONG As_Type SET FOR THE LOAD: Please Check", this load, As_Type to message
    	dec C_Error by 1
   	end	
  	return
end /***************************************************************************************/
/*******************************************************************************************/

/*******************************************************************************************/
begin P_Legend arriving procedure
  move into Q_Legend containers(procindex)
  scale to x 50, y 50, z 5
  set this load color to vclr_LoadColor(procindex)
  print vsStyle(procindex) to Lb_Legend(procindex)
  wait to be ordered on O_Temp   
end
/*******************************************************************************************/

begin pf_ohc:RowEnd blocked procedure		
	if viTrackStoreProcess = 1 then begin
		if this load Ai_Type <> 99 or (this load Ai_Type = 99 and this load Ai_Seq <> 99999) then begin 		
  			/*print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreIn.out"*/
  			set vldWriteJSON to this load
  			set vldWriteJSON As_Message to "STORE IN COMPLETE"
	  		set vldWriteJSON As_CurrentLocation to vsStation(vldWriteJSON Ai_Zone,vldWriteJSON Ai_Row)
  			set vldWriteJSON As_NextLocation to vsStation(vldWriteJSON Ai_Zone,vldWriteJSON Ai_Row)
			call Sr_WriteJSONFile
 		end
 	end
 		
	if this load Ai_Type <> 99 then begin	
		if O_Starve(this load Ai_Type) current > 0 and vldlstLanePull(this load Ai_Zone, this load Ai_Row) size > 0 and vldlstLanePull(this load Ai_Zone, this load Ai_Row) first Ai_Type <> this load Ai_Type /*and this load Ai_y = 5*/ then begin 
  			set Ai_y = 0
  			order from O_Starve(this load Ai_Type) to continue   /** RELEASE STARVE LIST IF WAITING **/
 		/*	if ac > 86400 then begin
 	  			if P_SPO_Release(1) current > 0 then begin
 					if P_SPO_Release(1) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(1) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(1) load list first Ai_pi) 				
 	  			end	 	
 	  			if P_SPO_Release(2) current > 0 then begin
 					if P_SPO_Release(2) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(2) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(2) load list first Ai_pi) 				
 	  			end
  	  			if P_SPO_Release(3) current > 0 then begin
	 				if P_SPO_Release(3) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(3) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(3) load list first Ai_pi) 				
 	  			end
	 	  		if P_SPO_Release(4) current > 0 then begin
 					if P_SPO_Release(4) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(4) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(4) load list first Ai_pi) 				
	 	  		end	
 			end */
		end
		insert this load into vldlstLanePull(this load Ai_Zone, this load Ai_Row)
		
		if this load = Vld_Previous then
			set Vld_Previous to vldPrevHolder	/* Placeholder for Vld_Previous is needed to prevent recirculation issues in Zone 3 if final carrier has already reached its assigned row */
	end
end

begin pf_ohc:StorageRows leaving procedure
	if this load Ai_Type <> 99 then begin
		remove this load from vldlstLanePull(this load Ai_Zone, this load Ai_Row)
	/*	remove this load from vldlstLanes(this load Ai_Zone, this load Ai_Row) */
	end
	else begin
		if this load Ai_Zone > 0 and this load Ai_Row > 0 then begin
			if viEmptyCarPullQty(this load Ai_Zone, this load Ai_Row) > 0 then
				dec viEmptyCarPullQty(this load Ai_Zone, this load Ai_Row) by 1	
		end
		
		if viTrackStoreProcess = 1 then begin
  			if this load Ai_Type <> 99 or (this load Ai_Type = 99 and this load Ai_Seq <> 99999) then begin
  				/*print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreIn.out"*/
  				set vldWriteJSON to this load
  				set vldWriteJSON As_Message to "STORE OUT REQUEST"
  				set vldWriteJSON As_CurrentLocation to vsStation(vldWriteJSON Ai_Zone,vldWriteJSON Ai_Row)
  				set vldWriteJSON As_NextLocation to "13"
				call Sr_WriteJSONFile
 			end
 		end
	end
/*	if this load = Vld_Recirc(2) then
		order 1 load from oZoneMixRecircComplete to continue*/
end

begin pf_ohc:StorageRows arriving procedure
	wait for .1 sec

	if this load Ai_Type <> 99 then begin
		if O_Starve(this load Ai_Type) current > 0 and this load Ai_x <> 10 /*Ai_y = 5*/ then begin 
  			set this load Ai_y = 0
  			order from O_Starve(this load Ai_Type) to continue   /** RELEASE STARVE LIST IF WAITING **/
 		/*	if ac > 86400 then begin
 	  			if P_SPO_Release(1) current > 0 then begin
 					if P_SPO_Release(1) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(1) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(1) load list first Ai_pi) 				
 	  			end	 	
 	  			if P_SPO_Release(2) current > 0 then begin
 					if P_SPO_Release(2) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(2) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(2) load list first Ai_pi) 				
 	  			end
  	  			if P_SPO_Release(3) current > 0 then begin
 					if P_SPO_Release(3) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(3) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(3) load list first Ai_pi) 				
 	  			end
 	  			if P_SPO_Release(4) current > 0 then begin
 					if P_SPO_Release(4) load list first Ai_Type = this load Ai_Type then 
						order 1 load from O_PrevZoneExitClear(P_SPO_Release(4) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(4) load list first Ai_pi) 				
 	  			end	
 			end */
		end
	end
end

begin pf_ohc:ST_B13 arriving procedure
	wait to be ordered on oBPressExit
end

begin  pf_ohc:LS_10442 cleared procedure
	order 1 load from oBPressExit to continue case backorder on oBPressExit
end


/********************************* KDC ADDED CODE FOR STOPS AND LIMIT SWITCHES ***************************/
begin pf_ohc:StopStation arriving procedure
	wait for 0.1 sec
end

begin pf_ohc:ST5_8 arriving procedure
	if this load Ai_Type <> 99 then begin
		get rStampingShift
		wait for vrStopTime(1) sec
		free rStampingShift
	end
end

begin pf_ohc:ST5_9 arriving procedure
	if this load Ai_Type <> 99 then begin
		get rStampingShift
		wait for vrStopTime(3) sec
		free rStampingShift
	end
end		

begin pf_ohc:ST8_1 arriving procedure
	if viSta8_1Count > 0 then begin
		wait to be ordered on olSta8_1Incline
	end
	inc viSta8_1Count by 1
end

/*begin pf_ohc:ST_B27 leaving procedure
	if oST_B27 current > 0 then begin
		/*if pf_ohc:ST_B24 current + pf_ohc:ST_B25 current + pf_ohc:ST_B26 current > 0 then*/
			order 1 load from oST_B27 to continue
	end
end*/

begin pf_ohc:ST_B23 arriving procedure
	if this load Ai_LotNo <> 0 and this load Ai_Type <> 99 then
		get rBLineTravel
end

begin pf_ohc:ST_B24 leaving procedure
	if this load Ai_Type = 99 and viEmptyLoad(2,1) >= viCLineEmptyTrigger and oST_B27 current > 0 then
		order 1 load from oST_B27 to continue 
end

begin pf_ohc:ST_B28 leaving procedure
	if this load Ai_LotNo <> 0 and this load Ai_Type <> 99 then
		free rBLineTravel
end

/*begin pf_ohc:ST_B29 arriving procedure
	if oST_B27 current > 0 then begin
		if pf_ohc:ST_B24 current + pf_ohc:ST_B25 current + pf_ohc:ST_B26 current > 0 then
			order 1 load from oST_B27 to continue
	end
end*/

begin pf_ohc:ST5_3 leaving procedure
	if rCLine state Blocked current = 1 then begin
		set viBlocked(this load Ai_Press, this load Ai_pi) to 0
		if viBlocked(2,1) + viBlocked(2,2) = 0 then begin
			if Vi_Down(4) = 0 then set rCLine active state to "Down"
			else set rCLine active state to "Production"
		end
	end
end

begin pf_ohc:ST5_7a leaving procedure
	if rCLine state Blocked current = 1 then begin
		set viBlocked(this load Ai_Press, this load Ai_pi) to 0
		if viBlocked(2,1) + viBlocked(2,2) = 0 then	begin
			if Vi_Down(4) = 0 then set rCLine active state to "Down"
			else set rCLine active state to "Production"
		end
	end
end

begin pf_ohc:ST_B8 leaving procedure
	if rBLine state Blocked current = 1 then begin
		set viBlocked(this load Ai_Press, this load Ai_pi) to 0
		if viBlocked(1,1) + viBlocked(1,2) = 0 then begin
			if Vi_Down(3) = 0 then set rBLine active state to "Down"
			else set rBLine active state to "Production"
			
		end
	end
end

begin pf_ohc:ST_B12 leaving procedure
	if rBLine state Blocked current = 1 then begin
		set viBlocked(this load Ai_Press, this load Ai_pi) to 0
		if viBlocked(1,1) + viBlocked(1,2) = 0 then begin
			if Vi_Down(3) = 0 then set rBLine active state to "Down"
			else set rBLine active state to "Production"
		end
	end
end

begin pf_ohc:ST5_12 leaving procedure
	if oST5_12 current > 0 then
		order 1 load from oST5_12 to continue
end

/* begin pf_ohc:ST5_13 leaving procedure
	if oST5_13 current > 0 then begin
		if pf_ohc:ST5_8 remaining space + pf_ohc:ST5_7 remaining space + pf_ohc:ST5_7a remaining space = 0 then
			order 1 load satisfying Ai_z = 1 from oST5_13 to continue
		else begin
			if pf_ohc:ST5_10 remaining space + pf_ohc:ST5_9 remaining space + pf_ohc:ST5_4 remaining space + pf_ohc:ST5_3 remaining space = 0 then
				order 1 load satisfying Ai_z = 3 from oST5_13 to continue
			else order 1 load from oST5_13 to continue
		end
	end
end*/

begin pf_ohc:ST5_13 leaving procedure
	if oST5_13 current > 0 then begin
		if pf_ohc:ST5_8 remaining space + pf_ohc:ST5_7 remaining space + pf_ohc:ST5_7a remaining space = 0 and 
		pf_ohc:ST5_10 remaining space + pf_ohc:ST5_9 remaining space + pf_ohc:ST5_4 remaining space + pf_ohc:ST5_3 remaining space = 0 then  /* Both sides blocked */
			order 1 load from oST5_13 to continue
		else begin
			if pf_ohc:ST5_8 remaining space + pf_ohc:ST5_7 remaining space + pf_ohc:ST5_7a remaining space = 0 then
				order 1 load satisfying Ai_z = 1 from oST5_13 to continue
			else if pf_ohc:ST5_10 remaining space + pf_ohc:ST5_9 remaining space + pf_ohc:ST5_4 remaining space + pf_ohc:ST5_3 remaining space = 0 then
				order 1 load satisfying Ai_z = 3 from oST5_13 to continue
			else order 1 load from oST5_13 to continue
		end
	end
end

begin pf_ohc:ST10 leaving procedure
	dec viKDcount(1) by 1
end

begin pf_ohc:LS_10275 cleared procedure
	order 1 load from olSta8_1Incline case backorder on olSta8_1Incline
end


begin pf_ohc:ST7_8 arriving procedure
	if this load Ai_Side = 3 then begin
		if pf_ohc:ST146 remaining space + pf_ohc:ST155 remaining space + pf_ohc:ST156 remaining space = 0 and 
		pf_ohc:ST154 current + pf_ohc:ST153 current + pf_ohc:ST152 current = 0 and pf_ohc:ST146 vehicle list first load list first Ai_Side <> 3 then begin
			order 1 load from O_Weld2Order(2) to continue
			wait to be ordered on O_Weld2Order(1) 
		end
	end 
end

/********************************* END OF KDC CODE ***********************************/

#@!
SFileBegin	name SortingLogic.m
begin model ready function
	call F_ConnectSocket()
	return true
end

begin Sr_StoreIn procedure
  set Ai_Zone = 1
  set Ai_Status = 0
  set Ai_LotQty = 0
  
  if this load Ai_Type = Vi_PrevPartType(Ai_Press) then begin /* Check for space in previous row if it is not the first carrier of a run */
  	/*** CODE TO SET THE LAST ROW & ZONE FOR STORAGE IF THE LAST STORED ROW IS STILL OPEN ***/
 	if Vldlst_Type(Ai_Type) size > 0 and vldlstLanes(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) size > 0 and  
   	(vldlstLanes(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) size < 
    vlocptrLane(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) capacity) and
    vldlstLanes(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) last Ai_Type = this load Ai_Type then begin
    	set Ai_Zone = Vldlst_Type(Ai_Type) last Ai_Zone  
    	set Ai_Row = Vldlst_Type(Ai_Type) last Ai_Row
    	set Ai_Status = 1
    	if viEmptyCarFlushQty(Ai_Zone, Ai_Row) > 0 then dec viEmptyCarFlushQty(Ai_Zone, Ai_Row) by 1    
    	set Vi_StorePrevType = Ai_Type 
    end
  end
  else if O_Starve(Ai_Type) current > 0 then begin  /******************* If part type is starved, then look through all zones for empty row first ******************/
  	set Ai_y = 5
  	while Ai_Zone <= viNumZones and Ai_Status = 0 do begin
  		set Ai_Row = 1
  		while Ai_Row <= viZoneLaneMax(viZonePref(Ai_Zone,Ai_Type)) and Ai_Status = 0 do begin
  			if vldlstLanes(viZonePref(Ai_Zone,Ai_Type),Ai_Row) size = 0 then begin
  				set Ai_Status = 1
  				set Ai_Zone = viZonePref(Ai_Zone,Ai_Type)
  			end
  			if Ai_Status = 0 then 
				inc Ai_Row by 1
		end
		if Ai_Status = 0 then inc Ai_Zone by 1
  	end
  end	

  set vi = 1
  while vi <= 12 and Ai_Status = 0 do begin
  	set Ai_Zone = viZonePref(viZoneSortPriority(vi),Ai_Type)
	set Ai_Row to 1

	if vsSortingRule(vi) = "Partial" then begin
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin	
      		if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then begin
        		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = Ai_Type and Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 and 
        		vlocptrLane(Ai_Zone,Ai_Row) capacity > vldlstLanes(Ai_Zone,Ai_Row) size - 
        		viEmptyCarFlushQty(Ai_Zone,Ai_Row) then begin
          		/*	if viSTRunGen(1) = 0 or viSTRunGen(2) = 0 or (Ai_Press = 1 and (Ai_Zone <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Row)) or (Ai_Press = 2 and (Ai_Zone <> Vldlst_Type(Vi_PrevType(1)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(1)) last Ai_Row)) then begin         */				        		
          				set Ai_Status = 1
		          		if viEmptyCarFlushQty(Ai_Zone,Ai_Row) > 0 then
	    	        		dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
        			/*end		*/		
        		end
      		end
      		if Ai_Status = 0 then
        		inc Ai_Row by 1
    	end	
	end	

	if vsSortingRule(vi) = "Empty" then begin
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin 	
      		if vldlstLanes(Ai_Zone,Ai_Row) size = 0 then 
		      	set Ai_Status = 1
	      	else inc Ai_Row by 1
	    end	
		
		if Ai_Status = 0 then set Ai_Row = 1
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin	/***Empty Carrier Check*****/
      		if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then begin
        		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = 99 then begin
	        		set viEmptyCarFlushQty(Ai_Zone,Ai_Row) to vldlstLanes(Ai_Zone,Ai_Row) size
          			set viEmptyCarPullQty(Ai_Zone,Ai_Row) to vldlstLanes(Ai_Zone,Ai_Row) size
          			for each vlptrEmpty in vldlstLanes(Ai_Zone,Ai_Row) do begin
	        			/*  inc viEmptyCarFlushQty(viZonePref(Ai_Zone,Ai_Type),Ai_Row) by 1 */
          				set vlptrEmpty Ai_Status = 1
          			end
	        		order vldlstLanes(Ai_Zone,Ai_Row) first from olEmptyHold
          			set vldlstLanes(Ai_Zone,Ai_Row) last Ai_Seq = 9999
          			set Ai_Status = 1
          			/* set Ai_Zone = viZonePref(Ai_Zone,Ai_Type) */
          			dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
        		end
      		end
      		if Ai_Status = 0 then
       	 		inc Ai_Row by 1
    	end
	end

	if vsSortingRule(vi) = "Mixed-Same" then begin
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin	
      		if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then begin
        		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = Ai_Type and Vi_ZoneMix(Ai_Zone,Ai_Row) = 1 and 
        		vlocptrLane(Ai_Zone,Ai_Row) capacity > vldlstLanes(Ai_Zone,Ai_Row) size - viEmptyCarFlushQty(Ai_Zone,Ai_Row) then begin
          			/*if viSTRunGen(1) = 0 or viSTRunGen(2) = 0 or (Ai_Press = 1 and (Ai_Zone <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Row)) or (Ai_Press = 2 and (Ai_Zone <> Vldlst_Type(Vi_PrevType(1)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(1)) last Ai_Row)) then begin */
        				set Ai_Status = 1
          				if viEmptyCarFlushQty(Ai_Zone,Ai_Row) > 0 then
	            			dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
					/*end */       	
	        	end
	      	end
      		if Ai_Status = 0 then
        		inc Ai_Row by 1
	    end
   	end
	
	if vsSortingRule(vi) = "Mixed" then begin                            
    	set Vi_ZoneCapacity(1) to 1
    	set Vi_ZoneCapacity(2) to vlocptrLane(Ai_Zone,Ai_Row) capacity
    	while Ai_Row <= viZoneLaneMax(Ai_Zone) do begin 
    		if vldlstLanes(Ai_Zone,Ai_Row) size < Vi_ZoneCapacity(2) then begin
          		if viSTRunGen(1) = 0 or viSTRunGen(2) = 0 or Ai_Press = 1 and Vldlst_Type(Vi_PrevPartType(2)) size = 0 
          		or Ai_Press = 2 and Vldlst_Type(Vi_PrevPartType(1)) size = 0 then begin
    				set Vi_ZoneCapacity(1) to Ai_Row
    				set Vi_ZoneCapacity(2) to vldlstLanes(Ai_Zone,Ai_Row) size
    				set Ai_Status to 2
  				end          		
          		else begin
          			if (Ai_Press = 1 and (Ai_Zone <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Row)) or (Ai_Press = 2 and (Ai_Zone <> Vldlst_Type(Vi_PrevType(1)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(1)) last Ai_Row)) then begin 
    					set Vi_ZoneCapacity(1) to Ai_Row
    					set Vi_ZoneCapacity(2) to vldlstLanes(Ai_Zone,Ai_Row) size
    					set Ai_Status to 2
    				end
  				end	
  			end
  			inc Ai_Row by 1
    	end
		if Ai_Status = 2 then begin
			set Ai_Row to Vi_ZoneCapacity(1)
			if Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 then begin
				set Vi_ZoneMix(Ai_Zone,Ai_Row) to 1
				inc C_ZoneMix(1) by 1
				dec C_ZoneMix(2) by 1
			end
		end
	end

	if Ai_Status = 0 then
		inc vi by 1
	if vi = 13 then begin
		wait to be ordered on olSpaceAvail
		set vi = 1
	end
  end
end

/****************************Lane Selection and Delivery To storage********************/
begin pStorage arriving procedure 
  if Ai_CarQty = 0 then begin
  	set Ai_CarQty = 1
  	inc viTotalStorageParts(Ai_Type) by 1
  end
  
  call Sr_StoreIn
  
  /*set At_TimeInSys = ac*/
  set Ai_Status = 0
  set Ai_Seq = 0
  inc Vi_ZoneSpace(Ai_Zone,1) by 1
  insert this load into vldlstLanes(Ai_Zone,Ai_Row)
  insert this load into Vldlst_Type(Ai_Type)
  set Vld_Previous to this load
  set Vi_PrevPartType(Ai_Press) to this load Ai_Type
  
  if viTrackStoreProcess = 1 then begin
  	/*print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreIn.out"*/
  	set As_Message to "STORE IN REQUEST"
  	set As_CurrentLocation to "5_13"
  	set As_NextLocation to vsStation(Ai_Zone,Ai_Row)
  	//set vldWriteJSON to this load
  	//call Sr_WriteJSONFile
  	call F_RequestMove(this load)
  end
 
/*  if O_Starve(Ai_Type) current > 0 then begin 
  /*	set O_Starve(Ai_Type) load list first Ai_y = 7878*/
  	order from O_Starve(Ai_Type) to continue   /** RELEASE STARVE LIST IF WAITING **/
  	
 	if ac > 86400 then begin
 	  if P_SPO_Release(1) current > 0 then begin
 		if P_SPO_Release(1) load list first Ai_Type = this load Ai_Type then 
			order 1 load from O_PrevZoneExitClear(P_SPO_Release(1) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(1) load list first Ai_pi) 				
 	  end	 	
 	  if P_SPO_Release(2) current > 0 then begin
 		if P_SPO_Release(2) load list first Ai_Type = this load Ai_Type then 
			order 1 load from O_PrevZoneExitClear(P_SPO_Release(2) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(2) load list first Ai_pi) 				
 	  end
  	  if P_SPO_Release(3) current > 0 then begin
 		if P_SPO_Release(3) load list first Ai_Type = this load Ai_Type then 
			order 1 load from O_PrevZoneExitClear(P_SPO_Release(3) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(3) load list first Ai_pi) 				
 	  end
 	  if P_SPO_Release(4) current > 0 then begin
 		if P_SPO_Release(4) load list first Ai_Type = this load Ai_Type then 
			order 1 load from O_PrevZoneExitClear(P_SPO_Release(4) load list first Ai_pi) to continue case backorder on O_PrevZoneExitClear(P_SPO_Release(4) load list first Ai_pi) 				
 	  end	
 	end  
 	
  end*/
  
  if Ai_Press = 1 then begin
  	set Ai_z to 2	/* Carriers have 2nd priority at 5_12 and 5_13 */
  	if pf_ohc:ST5_12 current = 1 then
  		wait to be ordered on oST5_12 
  	travel to pf_ohc:ST5_12
  	set Ai_z to 1
  	if pf_ohc:ST5_13 current = 1 then
  		wait to be ordered on oST5_13
  end
  else if Ai_Press = 2 then begin
  	if Ai_pi = 1 then begin
  		set Ai_z to 1	/* Carriers have 1st priority at 5_12 and 5_13 */
  		if pf_ohc:ST5_12 current = 1 then
  			wait to be ordered on oST5_12
  		travel to pf_ohc:ST5_12
  		if pf_ohc:ST5_13 current = 1 then
  			wait to be ordered on oST5_13
  	end
  	else if Ai_pi = 2 then begin
  		set Ai_z to 3	/* Carriers have 3rd priority at 5_13 */
  		travel to pf_ohc:ST5_11
  		if pf_ohc:ST5_13 current = 1 then
  			wait to be ordered on oST5_13
  	end
  end
 
  travel to pf_ohc:ST5_13
  set Ai_z to 0
  wait for vrStopTime(5) sec 
  travel to vlocptrLane(Ai_Zone,Ai_Row)
  if Ai_Status = 0 then 
  	wait to be ordered on O_Type(Ai_Type)

  while Ai_pi = 1234 do begin  /* Carriers recirculated to A-Line Storage Bay (Zone 3) */
 	if Ai_x = 1234 and R_ZoneExit(Ai_Zone) status = 1 then take down R_ZoneExit(Ai_Zone)
 	set Vld_ZoneTakeDown(Ai_Zone) to this load    
  	travel to pf_ohc:ST13
  /*	if Ai_z = 5454 then begin
  		set Ai_z to 0
  		bring up R_ZoneExit(Ai_Zone)
  	end*/
  	travel to pf_ohc:ST7_4	
  	if Ai_x = 1234 then begin
  		bring up R_ZoneExit(Ai_Zone)
  	/*	wait to be ordered on O_Recirc	*/	
  		set Vld_Recirc(1) = null
  	end
  	  	
/*	if Ai_y = 7878 then wait for 5 min
  	if Ai_y = 7878 then set Ai_y = 0 */		
  	  	
  	wait for vrStopTime(10) sec  	
  	set Ai_Zone to 3
  	set Ai_Status = 0

  	while Ai_Status = 0 do begin
  		if Ai_x = 1234 then begin
  			set Vi_Recirc(1) to Ai_Type
  			set Ai_x = 0
  		end
		else begin
  			if this load Ai_Type = Vi_Recirc(1) then begin
  				if vldlstLanes(3,Vi_Recirc(2)) size < vlocptrLane(3,Vi_Recirc(2)) capacity then begin
  					set Ai_Row to Vi_Recirc(2)
  					set Ai_Status = 1	
  				end	
  			end
  			else set Vi_Recirc(1) to Ai_Type
  		end	    		
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Partial Lane Check (Older Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 0 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type /*and vldlstLanes(3,Ai_Row) last Ai_LotNo <= this load Ai_LotNo*/ and vldlstLanes(3,Ai_Row) last = vldlstLanePull(3,Ai_Row) last /*vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size*/ then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Empty Lane Check */
  			inc Ai_Row by 1
  			if vldlstLanes(3,Ai_Row) size = 0 then 
  				set Ai_Status = 1
  		end
  		 				
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Empty Carrier Check */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and vldlstLanes(3,Ai_Row) last Ai_Type = 99 then begin
      	    		set viEmptyCarFlushQty(3,Ai_Row) to vldlstLanes(3,Ai_Row) size
       		   		set viEmptyCarPullQty(3,Ai_Row) to vldlstLanes(3,Ai_Row) size
       		   		for each vlptrEmpty in vldlstLanes(3,Ai_Row) do begin
         	 			set vlptrEmpty Ai_Status = 1
         	 		end
          			order vldlstLanes(3,Ai_Row) first from olEmptyHold
          			set vldlstLanes(3,Ai_Row) last Ai_Seq = 9999
          			set Ai_Status = 1
					dec viEmptyCarFlushQty(3,Ai_Row) by 1
       			end  		
  			end
  		end
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Mixed Lane Check - Current Part Type in Back (Older Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 1 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type /* and vldlstLanes(3,Ai_Row) last Ai_LotNo <= this load Ai_LotNo*/ and vldlstLanes(3,Ai_Row) last = vldlstLanePull(3,Ai_Row) last /*vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size*/ then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end 
  	
/*  	if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Partial Lane Check (Newer Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 0 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type and vldlstLanes(3,Ai_Row) last Ai_LotNo > this load Ai_LotNo and vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Mixed Lane Check - Current Part Type in Back (Newer Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 1 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type and vldlstLanes(3,Ai_Row) last Ai_LotNo > this load Ai_LotNo and vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end */
  	
  		if Ai_Status = 0 then begin
  			set Ai_Row to 1		/* Mixed Lane Check */
  			set Vi_ZoneCapacity(1) to 1
   			set Vi_ZoneCapacity(2) to vlocptrLane(3,Ai_Row) capacity
  			while Ai_Row <= viZoneLaneMax(3) do begin	
    			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
    				if vldlstLanes(3,Ai_Row) size < Vi_ZoneCapacity(2) /* and vldlstLanes(3,Ai_Row) last = vldlstLanePull(3,Ai_Row) last */ /*vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size */ then begin
    					set Vi_ZoneCapacity(1) to Ai_Row
    					set Vi_ZoneCapacity(2) to vldlstLanes(3,Ai_Row) size
    					set Ai_Status to 2
  					end
  				end
  				inc Ai_Row by 1		
  			end    	
		end
	
		if Ai_Status = 0 then begin	/* If all lanes are full, recirculate the parts in zone 3 */
			set Ai_Row to 1
			while Ai_Row <= viZoneLaneMax(3) and Ai_Status = 0 do begin
				if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
					if vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type then begin
						if vldlstLanePull(3,Ai_Row) first Ai_x <> 10 then begin
							order vldlstLanePull(3,Ai_Row) first from O_Type(vldlstLanes(3,Ai_Row) first Ai_Type) to continue case backorder on O_Type(vldlstLanes(3,Ai_Row) first Ai_Type)
							set vldlstLanePull(3,Ai_Row) first Ai_pi to 1234
						/*	remove vldlstLanes(3,Ai_Row) first from Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type) 
							insert vldlstLanes(3,Ai_Row) first into Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type)	*/ 
							remove vldlstLanePull(3,Ai_Row) first from vldlstLanes(3,Ai_Row)
						end
						set Ai_Status to 1
					end
				end
				if Ai_Status = 0 then inc Ai_Row by 1
			end
			if Ai_Status = 0 then begin /* No rows with current part type in back, so recirc lane 1 */
				if Vld_Previous Ai_Zone = 3 and Vld_Previous Ai_Row = 9 then set Ai_Row = 1
				else set Ai_Row to 9
				if vldlstLanePull(3,Ai_Row) first Ai_x <> 10 then begin
					order vldlstLanePull(3,Ai_Row) first from O_Type(vldlstLanes(3,Ai_Row) first Ai_Type) to continue case backorder on O_Type(vldlstLanes(3,Ai_Row) first Ai_Type)
					set vldlstLanePull(3,Ai_Row) first Ai_pi to 1234
				/*	remove vldlstLanes(3,Ai_Row) first from Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type) 
					insert vldlstLanes(3,Ai_Row) first into Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type)	*/
					remove vldlstLanePull(3,Ai_Row) first from vldlstLanes(3,Ai_Row)
				end
				set Ai_Status to 1
			end
		end
	end  
  	  	
  	if Ai_Status = 2 then begin
  		set Ai_Row = Vi_ZoneCapacity(1) 
		if Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 then begin
			set Vi_ZoneMix(Ai_Zone,Ai_Row) to 1
			inc C_ZoneMix(1) by 1
			dec C_ZoneMix(2) by 1
		end
	end

  	set Vi_Recirc(2) to Ai_Row
  	set Ai_pi to 0
  	set Ai_Status = 0
  	set Ai_x to 0	
  	insert this load into vldlstLanes(Ai_Zone,Ai_Row)
  	order 1 load from oZoneMixCheck to continue
  	travel to vlocptrLane(Ai_Zone,Ai_Row)
  	if Ai_Status = 0 then
  		wait to be ordered on O_Type(Ai_Type)
  end
  	  
  remove this load from vldlstLanes(Ai_Zone,Ai_Row)
  remove this load from Vldlst_Type(Ai_Type)
  
  dec viTotalStorageParts(Ai_Type) by Ai_CarQty
  call Sr_OtherInv
  
  print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
  
  order 1 load from olSpaceAvail to continue
  
  if Ai_Type >= 20 then begin
  	if viTotalStorageParts(Ai_Type) < (viTriggerQty(Ai_Type)*1.1) and C_SPOControl(Ai_Type) current = 0 then begin
		if C_SPOControl(viModelV(Ai_Type)) current = 0 then begin	/* Check to see if SPO is already scheduled */
  			set viTypeHold to Ai_Type
  			set Ai_Type to viModelV(Ai_Type)
  			inc C_SPOControl(Ai_Type) by 1
  			clone 1 load to pSTRunControl
  			set Ai_Type to viTypeHold
  		end
	end
	if viTotalStorageParts(Ai_Type) <= viTriggerQty(Ai_Type)/4 then begin
		set viTypeHold to Ai_Type
  		set Ai_Type to viModelV(Ai_Type)	
		for each Vld_Crit in O_StampingPriority(Ai_Press) load list do begin
	  		if Vld_Crit Ai_NonSPO = 0 and Vld_Crit Ai_Type = this load Ai_Type and Vld_Crit priority = Vld_Crit Ai_StampingPriority then begin
  				set Vld_Crit priority to Vld_Crit Ai_StampingPriority/10
	  			order Vld_Crit from O_StampingPriority(Ai_Press) to O_StampingPriority(Ai_Press)
	  			insert Vld_Crit into vldlstCritInv(Vld_Crit Ai_Press)
  			end
  		end
		set Ai_Type to viTypeHold
	end
  end
  else begin
	if viTotalStorageParts(Ai_Type) < (viTriggerQty(Ai_Type)*1.1) and C_SPOControl(Ai_Type) current = 0 then begin
  		inc C_SPOControl(Ai_Type) by 1
	    clone 1 load to pSTRunControl
  	end
	if viTotalStorageParts(Ai_Type) <= viTriggerQty(Ai_Type)/4 then begin  /* Check for parts below 1 hour inventory */
		for each Vld_Crit in O_StampingPriority(Ai_Press) load list do begin
	  		if Vld_Crit Ai_NonSPO = 0 and Vld_Crit Ai_Type = this load Ai_Type and Vld_Crit priority = Vld_Crit Ai_StampingPriority then begin
  				set Vld_Crit priority to Vld_Crit Ai_StampingPriority/10
	  			order Vld_Crit from O_StampingPriority(Ai_Press) to O_StampingPriority(Ai_Press)
	  			insert Vld_Crit into vldlstCritInv(Vld_Crit Ai_Press)
  			end
  		end
 	 end
  end

  send to P_StorageToBuffer      
end


/***********************************Process for Empty Carrier Storage Selection***************************/
begin pEmptyCarrierLaneSelect arriving procedure
  travel to pf_ohc:ST5_13  
  set Ai_Status = 0
  set Ai_x = 1

  /** CODE TO SEND EMPTY CARRIERS TO PREV LANE IF SPACE AVAILABLE **/
  if olEmptyHold current > 0 and vldlstLanes(olEmptyHold load list last Ai_Zone,olEmptyHold load list last Ai_Row) size > 0 and  
  (vldlstLanes(olEmptyHold load list last Ai_Zone,olEmptyHold load list last Ai_Row) size < 
   vlocptrLane(olEmptyHold load list last Ai_Zone,olEmptyHold load list last Ai_Row) capacity) then begin
    set Ai_Zone = olEmptyHold load list last Ai_Zone  
    set Ai_Row = olEmptyHold load list last Ai_Row
    set Ai_Status = 1
  end

  while Ai_Status = 0 do
  begin
  while Ai_x <= viNumZones and Ai_Status = 0 do
  begin
    set Ai_Zone to Vi_EmptyZone(Ai_x)
    set Ai_Row = 1
    while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do		/***Partial Row Check*****/
    begin
      if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then
      begin
        if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = Ai_Type and
        vlocptrLane(Ai_Zone,Ai_Row) capacity > vldlstLanes(Ai_Zone,Ai_Row) size then
        begin
          set Ai_Status = 1
        end
      end
      if Ai_Status = 0 then
        inc Ai_Row by 1
    end
    if Ai_Status = 0 then
      set Ai_Row = 1
    while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do		/***Empty Row Check*****/
    begin
      if vldlstLanes(Ai_Zone,Ai_Row) size = 0 then
          begin
            set Ai_Status = 1
          end
      if Ai_Status = 0 then
        inc Ai_Row by 1
    end
	
	if Ai_Status = 0 then inc Ai_x by 1
  end
  
  set Ai_x to 1
 while Ai_x <= viNumZones and Ai_Status = 0 do begin	/*** Check for lane full of carriers and kick the one in front ***/
 	set Ai_Zone to Vi_EmptyZone(Ai_x)
 	set Ai_Row = 1
 	while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin
 		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = 99 then begin
 			if vldlstLanes(Ai_Zone,Ai_Row) size = vlocptrLane(Ai_Zone,Ai_Row) capacity then begin
 				set vldlstLanes(Ai_Zone,Ai_Row) first Ai_Seq = 9999
 				order vldlstLanes(Ai_Zone,Ai_Row) first from olEmptyHold case backorder on olEmptyHold
 				remove vldlstLanes(Ai_Zone,Ai_Row) first from vldlstLanes(Ai_Zone,Ai_Row)
 				set Ai_Status = 1 
 			end
 		end
 		if Ai_Status = 0 then inc Ai_Row by 1
 	end
   	if Ai_Status = 0 then inc Ai_x by 1
  end
   
    if Ai_Status = 0 then begin
      wait to be ordered on olSpaceAvail
      /* set Ai_Zone = Vi_EmptyZone(1) */
      set Ai_x to 1
    end
  end
  
  set Ai_Status = 0
  
  insert this load into vldlstLanes(Ai_Zone,Ai_Row)
  travel to vlocptrLane(Ai_Zone,Ai_Row)
  if Ai_Status = 0 then
  begin
  	wait to be ordered on olEmptyHold
    
    if R_ZoneExit(Ai_Zone) status = down  /* Prevents empty carriers from blocking the system */
    	then bring up R_ZoneExit(Ai_Zone)
    
    get R_ZoneExit(Ai_Zone)
   	free R_ZoneExit(Ai_Zone)
  	take down R_ZoneExit(Ai_Zone)
  	set Vld_ZoneTakeDown(Ai_Zone) to this load	
  end
  
  if Ai_Seq = 9999 then begin
    bring up R_ZoneExit(Ai_Zone)
    if oEmptyBufferCleanUp current > 0 then 
    	order 1 load from oEmptyBufferCleanUp to continue 
   end
  
  remove this load from vldlstLanes(Ai_Zone,Ai_Row)
  

  if Ai_Seq = 9999 and viEmptyCarFlushQty(Ai_Zone,Ai_Row) > 0 then set viEmptyCarFlushQty(Ai_Zone,Ai_Row) = 0


  order 1 load from olSpaceAvail to continue
  
  /*travel to pf_ohc:W36*/
  travel to pf_ohc:ST13
  
  if Ai_Seq = 9999 and Vi_ZoneEmptyFlush = 1 then set Vi_ZoneEmptyFlush = 0
  
  send to pEmptyCarrierReturn

end


/*******************************************************************************************/

/*******************************************************************************************/
/**  THIS IS WELD SCHED PROCESS. THIS PROCESS READS THE WELD PRODUCTION SCHEDULE &        **/
/**  CLONES TO APPROPRIATE STORAGE RELEASE PROCESS.                                       **/
/**                          PROCINDEX = 1 for LINE #1                                    **/
/**                          PROCINDEX = 2 for LINE #2                                    **/
/**  LOADS F/EACH PROCINDEX IS CLONED F/INITIALIZATION PROCESS. THE LOADS WILL STAY TILL  **/
/**  END OF SIMULATION.  WELD SCHEDULE SHOULD BE COPIED AS 2 SCHE FILES FOR LINE 1&2      **/
/*******************************************************************************************/
begin P_WeldSched arriving procedure
  call SR_AttributeReset                                         /** RESET ALL ATTRIBUTES **/  
  if procindex = 1 then 
    open "base.arc/DATA/weld1prod.dat" for reading save result as Vflptr_WeldSched(procindex)
  else if procindex = 2 then 
    open "base.arc/DATA/weld2prod.dat" for reading save result as Vflptr_WeldSched(procindex)
  read Vs_junk from Vflptr_WeldSched(procindex) with delimiter "\n"       /** FILE HEADER **/
  read Vs_junk from Vflptr_WeldSched(procindex) with delimiter "\n"       /** FILE HEADER **/
  read Ai_LotQty from Vflptr_WeldSched(procindex) with delimiter "\t"
  while Ai_LotQty > 0 do begin /**------- END OF DATA FILE LOOP **/
    read Vs_junk, Vs_junk, Vs_junk, Vs_ProdType(1,procindex), Vs_ProdType(2,procindex) from Vflptr_WeldSched(procindex) with delimiter "\t"
    /** SET PROPER SIDE DESIGNATION TO TYPE ATTRIBUTE & SET WELD LINE # **/    
    if procindex = 1 then begin
      set this load Ai_Line = 1   
      set Ai_pi to 1
	  set As_Type to Vs_ProdType(1,procindex)
      clone to P_SPO_Release(1)                 /** FOR RELEASING THE SPO FROM STORAGE **/
      clone to P_SPO_Process(1)
      set Ai_pi to 2
      set this load As_Type = Vs_ProdType(2,procindex)     
      clone to P_SPO_Release(2)                 /** FOR RELEASING THE SPO FROM STORAGE **/  
      clone to P_SPO_Process(2)
      wait to be ordered on O_SPO_ReleaseComplete(1) /** WAIT FOR SPO RELEASE TO COMPLETE **/  
      wait to be ordered on O_SPO_ReleaseComplete(2) /** WAIT FOR SPO RELEASE TO COMPLETE **/  
    end

    else if procindex = 2 then begin   
      set this load Ai_Line = 2   
      set Ai_pi to 3
      set this load As_Type = Vs_ProdType(1,procindex)
      clone to P_SPO_Release(3)                 /** FOR RELEASING THE SPO FROM STORAGE **/     
      clone to P_SPO_Process(3)
      set Ai_pi to 4
      set this load As_Type = Vs_ProdType(2,procindex)
      clone to P_SPO_Release(4)                 /** FOR RELEASING THE SPO FROM STORAGE **/
      clone to P_SPO_Process(4)    
      wait to be ordered on O_SPO_ReleaseComplete(3) /** WAIT FOR SPO RELEASE TO COMPLETE **/  
      wait to be ordered on O_SPO_ReleaseComplete(4) /** WAIT FOR SPO RELEASE TO COMPLETE **/
    end  
    read Ai_LotQty from Vflptr_WeldSched(procindex) with delimiter "\t"  
  end  /**--------------------------------------------------------- END OF DATA FILE LOOP **/
  close Vflptr_WeldSched(procindex)
  set Vflptr_WeldSched(procindex) to null     
  send to P_WeldSched(procindex)                     /** SEND BACK TO REPEAT THE SCHEDULE **/
end  /**************************************************************************************/


/*******************************************************************************************/
/**  THIS IS SPO BUILD PROCESS.  THIS PROCESS READS THE WELD PRODUCTION SCHEDULE & 
     CLONES TO APPROPRIATE STORAGE RELEASE PROCESS.  
                          PROCINDEX = 1 for LINE #1 LH
                          PROCINDEX = 2 for LINE #1 RH
                          PROCINDEX = 3 for LINE #2 LH
                          PROCINDEX = 4 for LINE #2 RH                          
     LOADS FOR EACH PROCINDEX IS CLONED FROM P_Init PROCESS.  THE LOADS WILL STAY TILL 
     END OF SIMULATION.  WELD SCHEDULE SHOULD BE COPIED AS 4 SCHE FILES FOR MODELING      **/ 
/*******************************************************************************************/
begin P_SPO_Process arriving procedure /**----------------------------- SPO BUILD PROCESS **/
  set Vld_SPO(procindex) to this load
  
  move into Q_SPO(procindex)
  get 1 of R_SPO(procindex)

  call SR_TypeIndex                                             /** TO SET THE TYPE INDEX **/

  /** BEFORE STAR NEW RUN, IF TYPE IS NEW & CARRIER @ UNLOAD HAS SPOs LEFT THEN PERFORM
      PARTIAL STORAGE SUB-ROUTINE LOGIC                                                   **/
  if Ai_Type <> Vi_PrevType(procindex) and Vldptr_Unload(procindex) <> null and 
  Vldptr_Unload(procindex) Ai_CarQty > 0 then begin     
  	if Vo_SPO(procindex,1) current > 0 and 
    (Vldptr_Unload(procindex) = Vo_SPO(procindex,1) load list first or
    Vldptr_Unload(procindex) = Vo_SPO(procindex,1) load list last ) then begin
     	set Vldptr_Unload(procindex) Ai_Status = 8888
      	order Vldptr_Unload(procindex) from Vo_SPO(procindex,1) to continue     
    end
    set Vldptr_Unload(procindex) = null
  end  
  
  /** IF PARTIAL BUFFER EXISTS, CLONE A LOAD TO PARTIAL RELEASE PROCESS **/    
  if Vldlst_Partial(procindex, Ai_Type) size > 0 then begin
    clone to P_PartialRelease(procindex) nlt L_invis
    wait to be ordered on O_PartialRelease(procindex)
  end
  if P_SPO_Process(procindex) total =1 then wait to be ordered on olWarmUp   /** WAIT F/WARM UP **/

  set Vi_ProdCount(procindex) =0                                     /** RESET PRODUCTION STATS **/
  print "SPO #", procindex,"processing", As_Type,Vi_ProdCount(procindex), "of", Ai_LotQty to Lb_SPO(procindex)
  set Vi_LotQty(procindex) = Ai_LotQty     

  set pf_ohc:ST108 capacity to 4
  set Vi_SPO(procindex) to Ai_LotQty
  
  if Ai_Type <> Vi_PrevType(procindex) then begin
  	if procindex < 3 then begin
    	if oWeldChangeover(1) current = 0 then
    		wait to be ordered on oWeldChangeover(1)
    	else begin
    		take down R_Weld(1)
    		set R_Weld(1) active state to "Changeover"
    		get R_WeldShift(1)
    		wait for Vr_WeldChangeoverTime(1) min
    		free R_WeldShift(1)
    		bring up R_Weld(1)
    		set R_Weld(1) active state to "Production"
    		order 1 load from oWeldChangeover(1) to continue
    	end
    end
    else begin
    	if oWeldChangeover(2) current = 0 then
    		wait to be ordered on oWeldChangeover(2)
    	else begin
    		take down R_Weld(2)
    		set R_Weld(2) active state to "Changeover"
    		get R_WeldShift(2)
    		wait for Vr_WeldChangeoverTime(2) min
    		free R_WeldShift(2)
    		bring up R_Weld(2)
    		set R_Weld(2) active state to "Production"
    		order 1 load from oWeldChangeover(2) to continue
    	end
  	end
  end  
  
  while Ai_LotQty >0 do begin /**--------------------------------------- WHILE LOOP FOR LOT QTY **/

    if Vldptr_Unload(procindex) = null then begin       /** IF NO CAR @ UNLOAD --> RELEASE CARS **/
      if O_WeldBuffer(procindex) current > 0 then begin  /**------------------- IF CARS WAITING **/
          order from O_WeldBuffer(procindex) to continue
      end/**------------------------------------------------------------------- IF CARS WAITING **/
      else begin/**---------------------------------------------------------- IF CARS NOT THERE **/
        if procindex < 3 then begin
        	take down R_LineStarved(1)
        	take down R_Weld(1)
        	set R_Weld(1) active state to "Starved"
        	set R_LineStarved(1) active state to "Starved"
        end
        else begin
        	take down R_LineStarved(2)
        	take down R_Weld(2)
        	set R_Weld(2) active state to "Starved"
        	set R_LineStarved(2) active state to "Starved"
        end
        wait to be ordered on O_WeldBufferStarve(procindex)
        wait for .1 sec
        if O_WeldBuffer(procindex) load list first Ai_Type = this load Ai_Type then
        	order from O_WeldBuffer(procindex) to continue    
        if procindex < 3 then begin
        	bring up R_LineStarved(1)	
        	bring up R_Weld(1)
        	if R_Weld(1) active state = Starved then set R_Weld(1) active state to "Production"
        end
        else begin
        	bring up R_LineStarved(2) 
        	bring up R_Weld(2)    	    
        	if R_Weld(2) active state = Starved then set R_Weld(2) active state to "Production"
        end
      end  /**--------------------------------------------------------------- IF CARS NOT THERE **/          
    end  /**-------------------------- IF NO CAR @ UNLOAD --> RELEASE CARS FROM THE WELD BUFFER **/ 

    else begin  /**--------------------------------------------------- IF THERE IS CAR @ UNLOAD **/
      if Vldptr_Unload(procindex) Ai_CarQty < 5 or Vloc_B4Unload(procindex) remaining space > 0 then begin
        if O_WeldBuffer(procindex) current > 0 and 
           O_WeldBuffer(procindex) load list first Ai_Type = this load Ai_Type then
          order from O_WeldBuffer(procindex) to continue      
      end    
    end   /**--------------------------------------------------------- IF THERE IS CAR @ UNLOAD **/
   
    order 2 loads from Vo_SPO(procindex,1) 
      case backorder on Vo_SPO(procindex,1)     /** LOAD SIGNAL RBT&UNLOAD **/ 
    wait to be ordered on Vo_SPO(procindex,2)  /** WAIT FOR RBT TO LOAD **/    
    if this load Ai_Type <> K_RBT:Robot(procindex+2) load list first Ai_Type then begin
      print "TYPE do not Match between Rbt SPO & Required", this load Ai_Type,"-->",
            K_RBT:Robot(procindex+2) load list first Ai_Type,"in line", procindex to message
    /*  dec C_Error by 1  */    
    end
    set this load type to vlt_LoadType(Ai_Type)
    set this load color to vclr_LoadColor(Ai_Type)    
    /** CHECK FOR TYPE MATCH HERE **/
    wait to be ordered on Vo_SPO(procindex,3)  /** WAIT FOR RBT TO CLEAR **/  
    
    if procindex < 3 then begin 
    	get 1 of R_Weld(1)
    	get R_LineStarved(1)
    end
    else begin
    	get 1 of R_Weld(2)
    	get R_LineStarved(2) 
    end
    
    wait for Vr_SPOCycleTime(procindex) sec                            /** SPO CYCLE TIME **/
    
    if procindex < 3 then begin 
    	free R_Weld(1)
    	free R_LineStarved(1)       
    end
    else begin
    	free R_Weld(2)
    	free R_LineStarved(2)      
    end
    
    inc Vi_ProdCount(procindex) by 1                     /** UPDATE TYPE PRODUCTION STATS **/
    inc Vi_shiftProd(procindex) by 1                     /** UPDATE SHIFT PRODUCTION STATS **/   
    print "SPO #", procindex,"processing",As_Type, Vi_ProdCount(procindex), "of", Vi_LotQty(procindex), 
          ">>", Vi_shiftProd(procindex) to Lb_SPO(procindex)     
    dec Ai_LotQty by 1                                       /** UPDATE REMAINING LOT QTY **/
    set this load type to L_invis
    
    if procindex < 3 then begin
    	if Vi_shiftProd(procindex) = (Vi_WeldOutputTarget(1) + Vi_WeldGap(procindex)) then
    		wait to be ordered on O_WeldOutputTarget 
    end
    else begin
    	if Vi_shiftProd(procindex) = (Vi_WeldOutputTarget(2) + Vi_WeldGap(procindex)) then
    		wait to be ordered on O_WeldOutputTarget
    end	
    
    if Vi_ProdCount(procindex) =1 then begin
      if procindex < 3 then begin
        if O_Synch(1) current =1 then order all from O_Synch(1) to continue
        else wait to be ordered on O_Synch(1)      
      end
      else if procindex > 2 then begin
        if O_Synch(2) current =1 then order all from O_Synch(2) to continue
        else wait to be ordered on O_Synch(2)      
      end
    
    end
    
	set Vi_SPO(procindex) to Ai_LotQty
  end  /**-------------------------------------------------------------------- WHILE LOOP **/
  set pf_ohc:ST108 capacity = 1
  set Vi_PrevType(procindex) to Ai_Type
  free R_SPO(procindex)
end /**---------------------------------------------------------------- SPO BUILD PROCESS **/
/*******************************************************************************************/

/*******************************************************************************************/
/**  THIS IS SPO RELEASE PROCESS.  LOADS WILL BE CLONED FROM WELD SCHED PROCESS.  THIS 
     PROCESS FIRST CHECK THE PARTIAL BUFFER, ADJUST QRELEASE QTY AND REQUEST RELEASE FROM
     THE STORAGE LOCATIONS.  Vldlst_Partial(4) HAS SAME INDEX REFERENCE AS THE PROCINDEX. 
                          PROCINDEX = 1 for LINE #1 LH
                          PROCINDEX = 2 for LINE #1 RH
                          PROCINDEX = 3 for LINE #2 LH
                          PROCINDEX = 4 for LINE #2 RH
     THE LOGIC FIRST CHECKS THE PARTIAL BUFFER AND MAKE ADJUSTMENT TO THE QUANTITY TO BE 
     RELEASEED FROM THE STORAGE LINES.  BEFORE LOOKING INTO STORAGE LINES, IT WILL WAIT
     FOR THE WELD LINE BUFFER LINES TO SIGNAL HOW MANY CARRIERS CAN BE RELEASED FROM THE
     STORAGE.  ONCE SIGNALED, IT WILL SEE IF THE TYPE IS IN THE SYSTEM (Vldlst), IF NONE,
     THEN WAIT ON THE STARVE LIST.  IF AVAILABLE, THEN FOR EACH OF THE CARRIERS NEEDED,
     CHECK IF THEY ARE ON THE TYPE ORDER LIST AND ORDER THEM IF THEY ARE ON THE LIST      **/
/*******************************************************************************************/

begin P_SPO_Release arriving procedure /**--------------------------- SPO RELEASE PROCESS **/
	call F_getID()
	call SR_TypeIndex                                             /** TO SET THE TYPE INDEX **/
  	set Ai_Seq =0
  	if this load Ai_Type < 1 or this load Ai_Type > 30 then begin
	    print "ERROR in setting Ai_Type:Check", this load, this load current to message
    	dec C_Error by 1
  	end
  	/*******   ONLY CHECK FOR QTY IN PARTIAL RELEASE TO ADJUST STORAGE RELEASE QTY    *******/
  	if Vldlst_ReleasePartial(Ai_Line,Ai_Type) size > 0 then begin  /** CHECK F/PARTIAL RELEASE QTYS **/
    	get Vrsrc_ReleasePartial(Ai_Line,Ai_Type)  
    	for each Vld_SPO_Release(procindex) in Vldlst_ReleasePartial(Ai_Line,Ai_Type) do begin
      		if Vld_SPO_Release(procindex) Ai_CarQty > 0 then begin 
        		set this load Ai_LotQty = this load Ai_LotQty - Vld_SPO_Release(procindex) Ai_CarQty
        		set Vld_SPO_Release(procindex) Ai_CarQty = 0
        		order from Vo_ReleasePartial(Ai_Line,Ai_Type) case backorder on Vo_ReleasePartial(Ai_Line,Ai_Type) 
      		end
      		else begin
		        print "0 QTY @ Partial Buffer: Please Check", Vld_SPO_Release, Vld_SPO_Release current,
    		    Vld_SPO_Release(procindex) vehicle current location to message
        		dec C_Error by 1
      		end
    	end
    	free Vrsrc_ReleasePartial(Ai_Line,Ai_Type)     
  	end /**-------------------------------------------- CHECK F/ PARTIAL RELEASE QUANTITIES **/

  	set Vld_ReleaseCount(procindex) to this load	
  	while Ai_LotQty > 0 do begin /**------------------------------- TOTAL RELEASE ORDER QTY **/
	     /** BEFORE RELEASING NEED TO BE IN SEQUENCE TO ENSURE NOT MIX CARRIERS BEFORE SENT
    	     SENT TO THE WELD BUFFERS SINCE WELD BUFFERS DO NOT HAVE RE-CIRC CAPABILITIES     **/
     	wait to be ordered on O_ST13_SPO_Seq(procindex)          /** TO BE ORDERED AT ST-13  **/

	    /** IF THE BUFFER SPACE LOAD LIST IS ZERO -->> NO SIGNAL FROM THE BUFFER THEN WAIT 
        	TILL THE BUFFER ORDERS.  IF THERE ARE REMAINING SPACE TO BE FILLED FROM PREVIOUS
        	ORDERS THEN RELEASE CARRIER FOR NEW SCHED                                         **/
    
    	wait to be ordered on O_BufferSpace(procindex) /** WAIT F/ BUFFER SPACE @ WELD LINE **/
    	if Vi_SPO_QtyRelease(procindex) = 0 then begin
    		wait to be ordered on O_BufferSpace(procindex) 
    	end   
    	if Vi_SPO_QtyRelease(procindex) > Ai_LotQty then set Vi_SPO_QtyRelease(procindex) = Ai_LotQty
    	set this load Ai_Seq = Vi_SPO_QtyRelease(procindex)        /** SPO QTY TO BE RELEASED **/
    	if Ai_Seq = 0 and ac > 86400 then begin
      		print "ERROR: SPO QTY to be released for the buffer is not specified", procindex, Ai_Seq to Lb_SPORelease(procindex)
      		dec C_Error by 1
    	end	
    	print "Line=", Ai_Line, "-", procindex, As_Type, "Lot =", Ai_LotQty, "RlsQty=", Ai_Seq to message              

    	if procindex < 3 then get R_Line(1)
    	else get R_Line(2)
		get R_SPO_Release
  
    	set Ai_CarQty = 0   
    	set Vi_CheckQty(Ai_Type) = 0           /** FOR CHECKING QTY AVAILABLE VS REQUIRED QTY **/    
    	set Vld_ReleasePrev(procindex) = null   /** RESET PREV LOAD PTR TO NULL F/NEW RELEASE **/   
    	set Vi_PrevZone(procindex) = 0
	    
	    set Vi_Seq to Ai_Seq
	    set Vld_SortMarker = null
    
		if procindex < 3 then set Ai_Line = 1
		else set Ai_Line = 2
		set Ai_Side = procindex
	
		if viStorageInit(2) = 0 then
			wait to be ordered on oStorageInit
		
		set viCarrierReleaseCount(procindex,2) to 0
		while Ai_Seq > 0 do begin   /**-------------------- SPO QTY TO BE RELEASED WHILE LOOP **/    
    		call Sr_SPOSeq
    		inc viCarrierReleaseCount(procindex,2) by Vldlst_Release(procindex) size 
    		set Vldlst_Release(procindex) last Ai_pi to 4444
			for each Vld_Release(procindex) in Vldlst_Release(procindex) do begin
				if Vld_Release(procindex) Ai_Status <> 1 then begin
        	    	if Vld_ReleasePrev(procindex) <> null then begin  /**--- PREV LD LST NOT NULL **/
						if Vld_ReleasePrev(procindex) Ai_Zone <> this load Ai_Zone then begin
							get R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			free R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			take down R_ZoneExit(Vld_Release(procindex) Ai_Zone)              
	                		set Vld_ZoneTakeDown(Vld_Release(procindex) Ai_Zone) to this load
							print "D", Vld_Release(procindex) to Lb_ZoneExit(Ai_Zone)       
						end
            		end /**------------------------------------------------- PREV LD LST NOT NULL **/
            		else if Vld_ReleasePrev(procindex) = null then begin  /** PREV LD LST IS NULL **/
              			/* set Ai_CarQty = 0 */
              			if Vi_PrevZone(procindex) > 0 and R_ZoneExit(Vi_PrevZone(procindex)) status is down then begin
                			bring up R_ZoneExit(Vi_PrevZone(procindex))             
              				get R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			free R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			take down R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			set Vld_ZoneTakeDown(Ai_Zone) to this load
                			print "D", Vld_Release(procindex) to Lb_ZoneExit(Vld_Release(procindex) Ai_Zone)
              			end 
            		end   /**------------------------------------------------ PREV LD LST IS NULL **/        
            		set Vld_Release(procindex) Ai_Status =1 
            		insert Vld_Release(procindex) into Vldlst_ZoneExit(Ai_Zone)  
            		if procindex <3 then set Vld_Release(procindex) Ai_Line=1         /** WE LN # **/
            		else if procindex>2 then set Vld_Release(procindex) Ai_Line=2
            		set Vld_Release(procindex) Ai_Side = procindex               /** WE LN SIDE # **/              
            		inc Vi_CheckQty(Ai_Type) by Vld_Release(procindex) Ai_CarQty
            		dec Ai_Seq by Vld_Release(procindex) Ai_CarQty
            		dec Ai_LotQty by Vld_Release(procindex) Ai_CarQty            
            		inc Ai_CarQty by 1
            		if Ai_CarQty = 1 then begin
 	            		get R_ZoneExit(Vld_Release(procindex) Ai_Zone)
    	        		free R_ZoneExit(Vld_Release(procindex) Ai_Zone)
        	    		take down R_ZoneExit(Vld_Release(procindex) Ai_Zone)
            			set Vld_ZoneTakeDown(Ai_Zone) to this load
              			print "D", Vld_Release(procindex) to Lb_ZoneExit(Vld_Release(procindex) Ai_Zone)
            		end
            		print "Line=", Ai_Line, "-", procindex, As_Type, "Lot =", Ai_LotQty, "RlsQty=", Ai_Seq to Lb_SPORelease(procindex)              
			
					/*	order Vld_Release(procindex) from O_Type(Ai_Type) case backorder on O_Type(Ai_Type)    */     
  
		        	if O_Type(Ai_Type) current > 0 then begin /**------------ LOAD ON ORDER LIST  **/
    		      		for each Vld_OL(procindex) in O_Type(Ai_Type) load list do begin  /** OL LP **/
            	   			if Vld_Release(procindex) = Vld_OL(procindex) then begin 
                				order Vld_Release(procindex) from O_Type(Ai_Type) case backorder on O_Type(Ai_Type)
	               			end                 
    	          		end /**-------------------------------------------------------------- OL LP **/             
        	    	end   /**------------------------------------------------ LOAD ON ORDER LIST  **/              
  					remove Vld_Release(procindex) from Vldlst_Release(Ai_pi)
  						
	            	set Vld_ReleasePrev(procindex) = Vld_Release(procindex)   /** SET PREV LD PTR **/
			    	set Vi_PrevZone(procindex) =  Vld_ReleasePrev(procindex) Ai_Zone            
        	    	if  Ai_Seq <= 0 then begin
	            		set Vld_Release(procindex) Ai_Seq = 9999  /**-------------- LAST OF THE LOT **/
    	          		set Ai_Zone = Vld_Release(procindex) Ai_Zone
        	      		break
            		end                                
      					
      			end  /**--------------------------------------------- IF CARS ARE AVAILABLE **/      
			end /**---------------------------------------------------------- FOR EACH LOOP **/
			wait to be ordered on O_ReleaseSeq(procindex)
			order 1 load from oZoneMixCheck to continue
		end  /**----------------------------------------- SPO QTY TO BE RELEASED WHILE LOOP **/   
    
	    if O_Starved current > 0 then order 1 load from O_Starved to continue 
   		/* if Vld_Recirc <> null then order 1 load from O_Recirc to continue case backorder on O_Recirc	*/
         
	    wait to ordered on O_Release(procindex)  /** WAIT BATCH RELEASE COMPLETE SIGNAL ST-13 **/   
	    if procindex < 3 then free R_Line(1)
	    else free R_Line(2)
    	free R_SPO_Release
    	wait to be ordered on oST13Inspect(procindex)
    	wait for 0
  	end /**-------------------------------------------------------- TOTAL RELEASE ORDER QTY **/
 
 	if Ai_LotQty < 0 then begin            /** TO KEEP TRACK OF THE PARTIAL QTYS AT RELEASE **/
 		set Vi_ReleasePartial(Ai_Line,Ai_Type) = -1* Ai_LotQty
    	clone to Vproc_ReleasePartial(Ai_Line,Ai_Type)   
    end	

  	set Vld_ReleaseCount(procindex) to null

	/*order 1 load from oZoneMixCheck to continue*/
  	order from O_SPO_ReleaseComplete(procindex) case backorder on O_SPO_ReleaseComplete(procindex) 
end  /**------------------------------------------------------------- SPO RELEASE PROCESS **/
/*******************************************************************************************/

begin Sr_SPOSeq
	set Ai_Status = 0
	set vix = 1
	set viPrevLotNo = 0
	set viClearABayFlag(Ai_Side) to 0
	
	if Vldlst_Type(Ai_Type) size = 0 then begin
		free R_SPO_Release	
	    set Vr_WeldStarveTime(Ai_Line) to ac
		wait to be ordered on O_Starve(Ai_Type)
		tabulate (ac - Vr_WeldStarveTime(Ai_Line))/60 in T_WeldStarve(Ai_Line)
		wait for 5 min
		get R_SPO_Release
		set Vld_ReleasePrev(Ai_Side) = null
		set Vld_SortMarker = null
		set vix to 1
		set Vi_Seq to Ai_Seq
	end

	set viABayCount to 0  		/* Determine current count of carriers in A Bay */
	while vix <= 9 do begin
		for each Vld_Sort in vldlstLanes(3,vix) do begin
			if Vld_Sort Ai_Type <> 99 then begin
				inc  viABayCount by 1
			end
		end
		inc vix by 1
	end
	if viABayCount >= viABayTrigger then begin	/* If A-Bay is nearing capacity, ignore lot and try to clear out space */
		for each Vld_Sort in Vldlst_Type(Ai_Type) do begin
			if Vld_Sort Ai_Zone = 3 then begin
				set Ai_LotNo to Vld_Sort Ai_LotNo
				set viClearABayFlag(Ai_Side) to 1
				if Ai_LotNo > Vldlst_Type(Ai_Type) first Ai_LotNo then
					inc viABayCleanUp by 1
				break
			end
		end
	end
	
	set vix to 1
	while vix <= 12 do begin
		if viClearABayFlag(Ai_Side) = 0 then begin
			set Ai_Zone = viZonePref(viZonePullPriority(vix),Ai_Type)
			set Ai_LotNo = Vldlst_Type(Ai_Type) first Ai_LotNo
		end
		else begin
			set Ai_Zone to 3
		end
		set Ai_Row to 1	

		if vsPullingRule(vix) = "Partial" then begin
			set Vld_SortMarker to null	
			set Vi_LineCount = 99
			for each Vld_Sort in Vldlst_Type(Ai_Type) do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone,Vld_Sort Ai_Row) = 0 and vldlstLanePull(Ai_Zone, Vld_Sort Ai_Row) size > 0 then begin 
					if vldlstLanePull(Ai_Zone, Vld_Sort Ai_Row) first = Vld_Sort and vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size - viEmptyCarPullQty(Ai_Zone, Vld_Sort Ai_Row) < vlocptrLane(Ai_Zone, Vld_Sort Ai_Row) capacity 
					and vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size < Vi_LineCount then begin
						set Vld_SortMarker to Vld_Sort
						set Vi_LineCount to vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size
					end
				end					
			end
			if Vld_SortMarker <> null then begin				
				remove Vld_SortMarker from Vldlst_Type(Ai_Type)
				insert Vld_SortMarker into Vldlst_Type(Ai_Type) at beginning 
				set Ai_Status = 1				
			end
		end

		if vsPullingRule(vix) = "Full" then begin
			for each Vld_Sort in O_Type(Ai_Type) load list do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone,Vld_Sort Ai_Row) = 0 then begin 
					if vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size - viEmptyCarPullQty(Ai_Zone, Vld_Sort Ai_Row) = vlocptrLane(Ai_Zone, Vld_Sort Ai_Row) capacity then begin
						remove Vld_Sort from Vldlst_Type(Ai_Type)
						insert Vld_Sort into Vldlst_Type(Ai_Type) at beginning 
						set Ai_Status = 1
					end
				end					
			end			
		end		

		if vsPullingRule(vix) = "Mixed-Front" then begin
			set Vld_SortMarker to null
			set Vi_LineCount = 99 
			for each Vld_Sort in O_Type(Ai_Type) load list do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone,Vld_Sort Ai_Row) = 1 then begin
					set viMixCount to 0
					for each Vld_Sort2 in vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) do begin
						if Vld_Sort2 Ai_Type = this load Ai_Type then inc viMixCount by 1
						else break				
					end
					if viMixCount < Vi_LineCount then begin
						set Vi_LineCount to viMixCount
						set Vld_SortMarker to Vld_Sort
					end
				end			
			end
			if Vld_SortMarker <> null then begin
				remove Vld_SortMarker from Vldlst_Type(Ai_Type)
				insert Vld_SortMarker into Vldlst_Type(Ai_Type) at beginning
				set Ai_Status = 1
			end		
		end

		if vsPullingRule(vix) = "Mixed-Blocked" then begin			
			set Vld_SortMarker to null
			set Vi_LineCount = 99
			/*set viMixPrevRow = 0*/
			for each Vld_Sort in Vldlst_Type(Ai_Type) do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone, Vld_Sort Ai_Row) = 1	/* and Vld_Sort Ai_Row <> viMixPrevRow */ then begin
					set viMixCount to 0
					for each Vld_Sort2 in vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) do begin
						/*if Vld_Sort2 Ai_Type <> this load Ai_Type then inc viMixCount by 1*/
						if Vld_Sort2 <> Vld_Sort then inc viMixCount by 1
						else break
					end
					if viMixCount < Vi_LineCount then begin
						set Vi_LineCount to viMixCount
						set Vld_SortMarker to Vld_Sort
					end
					/*set viMixPrevRow to Vld_Sort Ai_Row*/
				end
			end
				
			if Vld_SortMarker = null and vix = 12 and Vldlst_Type(Ai_Type) size > 0 /*and Vldlst_Release(Ai_pi) size = 0*/ then begin  /* A scenario can occur where a newer lot has been recirculated behind an older lot and will not satisfy the above conditions */
				if viClearABayFlag(Ai_Side) = 1 then begin
					set viClearABayFlag(Ai_Side) = 0
					set vix = 0
				end
				else begin
					set Ai_Zone = Vldlst_Type(Ai_Type) first Ai_Zone
					set Ai_Row = Vldlst_Type(Ai_Type) first Ai_Row
					if vldlstLanePull(Ai_Zone,Ai_Row) size > 0 then	/* Check to make sure carrier has actually arrived */
						set Vld_SortMarker to Vldlst_Type(Ai_Type) first	
				end
			end
			
			if Vld_SortMarker <> null then begin
				remove Vld_SortMarker from Vldlst_Type(Ai_Type)
				insert Vld_SortMarker into Vldlst_Type(Ai_Type) at beginning
				inc viRecircCount by 1
				set Ai_Status = 1
	
				for each Vld_Sort in vldlstLanePull(Ai_Zone,Vld_SortMarker Ai_Row) do begin
					if (Vld_Sort Ai_Type <> this load Ai_Type and Vld_Sort Ai_Type <> 99) or (Vld_Sort Ai_Type = this load Ai_Type and Vld_Sort Ai_LotNo <> this load Ai_LotNo and Vld_Sort Ai_Type <> 99) then begin
						set Vld_Sort Ai_pi to 1234
						if Vld_Recirc(1) = null then begin 
							set Vld_Sort Ai_x to 1234	/* Flag first carrier to be released for recirc */
							set Vld_Recirc(1) to Vld_Sort
						end
						order Vld_Sort from O_Type(Vld_Sort Ai_Type) to continue case backorder on O_Type(Vld_Sort Ai_Type)
						remove Vld_Sort from vldlstLanes(Ai_Zone, Vld_Sort Ai_Row)
				    	set Vld_Recirc(2) to Vld_Sort	/* Flag last carrier to be released for recirc */
					end
					else begin
						if Vld_Sort Ai_x =10 then begin
							set Ai_Status = 0		/* Recirc will be initiated to push Vld_Sort to front of the lane, but won't be added to the Release List at this time */
							set vix = 13
						end	
						else break
					end
				end
				if Ai_Status = 1 then begin
					wait to be ordered on oZoneMixRecircComplete	
					order 1 load from oZoneMixCheck
					wait for 0
				end
			end
		end				
		
		if Ai_Status = 1 then begin	
			set Ai_Status = 0
			set Ai_Row = Vldlst_Type(Ai_Type) first Ai_Row
			
			while vldlstLanePull(Ai_Zone, Ai_Row) size < vldlstLanes(Ai_Zone, Ai_Row) size/2 do begin
				wait for 10 sec
			end
			
			for each Vld_Sort in vldlstLanePull(Ai_Zone, Ai_Row) do begin
				if Vld_Sort Ai_Type <> this load Ai_Type or Vld_Sort Ai_LotNo <> this load Ai_LotNo then break
				else begin
					dec Vi_Seq by Vld_Sort Ai_CarQty
					remove Vld_Sort from vldlstLanes(Ai_Zone,Ai_Row)
					remove Vld_Sort from Vldlst_Type(Ai_Type)
					insert Vld_Sort into Vldlst_Release(Ai_Side)
					
					if viTrackStoreProcess = 1 then begin
						/* if Ai_Line = 1 then print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreOut1.out"
						else if Ai_Line = 2 then print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreOut2.out" */
						//set vldWriteJSON to Vld_Sort
						//set vldWriteJSON As_Message to "STORE OUT REQUEST"
						//set vldWriteJSON As_CurrentLocation to vsStation(Ai_Zone,Ai_Row)
						//set vldWriteJSON As_NextLocation to "13"
						//call Sr_WriteJSONFile
						set Vld_Sort As_Message to "STORE OUT REQUEST"
						set Vld_Sort As_CurrentLocation to vsStation(Ai_Zone,Ai_Row)
						set Vld_Sort As_NextLocation to "13"
 						call F_RequestMove(Vld_Sort)
					end
					
					set Vld_Sort Ai_x to 10		/* Flag carriers that have been added to release list */
					/*set Vld_SortMarker to Vld_Sort*/
					print "Moved",Vld_Sort,"to Release" to message
					if Vi_Seq <= 0 then break	
				end	
			end
		end
		else begin 
			inc vix by 1
			if vix = 13 then begin
      			free R_SPO_Release	
	    		set Vr_WeldStarveTime(Ai_Line) to ac
		   		wait to be ordered on O_Starve(Ai_Type)
		   		tabulate (ac - Vr_WeldStarveTime(Ai_Line))/60 in T_WeldStarve(Ai_Line)
		    	wait for 5 min
		    	get R_SPO_Release
		   		set Vld_ReleasePrev(Ai_Side) = null
		   		set Vld_SortMarker = null
		   		set vix = 1
		   		set Vi_Seq = Ai_Seq
     		end
		end
		if Vldlst_Release(Ai_Side) size > 0 then break	
	end
end

begin pZoneMixCheck arriving procedure
	wait to be ordered on oZoneMixCheck
	set Ai_Zone = 1			/********* This procedure checks to see which rows are mixed rows after a release/recirc/store-in has occurred **********/
	while Ai_Zone <= viNumZones do begin
		set Ai_Row = 1
		while Ai_Row <= viZoneLaneMax(Ai_Zone) do begin
			set Ai_x = 0
			if vldlstLanes(Ai_Zone, Ai_Row) size > 0 then begin 
				set this load Ai_Type = vldlstLanes(Ai_Zone, Ai_Row) first Ai_Type
				if Ai_Type <> 99 then begin
					for each Vld_Sort in vldlstLanes(Ai_Zone, Ai_Row) do begin 
						if this load Ai_Type <> Vld_Sort Ai_Type then inc Ai_x by 1
					end
				end
			end
			if Ai_x = 0 and Vi_ZoneMix(Ai_Zone,Ai_Row) = 1 then begin
				set Vi_ZoneMix(Ai_Zone,Ai_Row) to 0
				dec C_ZoneMix(1) by 1
				inc C_ZoneMix(2) by 1
			end
			else if Ai_x > 0 and Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 then begin
				set Vi_ZoneMix(Ai_Zone,Ai_Row) to 1
				inc C_ZoneMix(1) by 1
				dec C_ZoneMix(2) by 1
			end
			inc Ai_Row by 1  
		end
		inc Ai_Zone by 1
	end	
	send to pZoneMixCheck
end


/*******************************************************************************************/
/** AT THE END OF EACH RELEASE, IF THE RELEASED QUANTITY IS MORE THAN THE LOT QTY, A LOAD **/
/** IS CLONED TO THIS PROCESS TO KEEP TRACK OF THE PARTIAL QTYS AT THE RELEASE.  THESE    **/
/** QUANTITIES WILL BE ADJUSTED WHEN NEXT RELEASE REQUEST COMES FOR SAME TYPE             **/
/*******************************************************************************************/
begin P_ReleasePartial arriving procedure
  set Vproc_ReleasePartial(1,1) = P_ReleasePartial(1)
  if Vi_ReleasePartial(Ai_Line, Ai_Type) > 0 then set Ai_CarQty = Vi_ReleasePartial(Ai_Line,Ai_Type)
  else begin
    print "QTY @ Release Partial is not set right", this load, current, Ai_CarQty to message
    dec C_Error by 1 
  end
  insert this load into Vldlst_ReleasePartial(Ai_Line,Ai_Type)
  print "Release partial", Ai_Type, As_Type, Ai_CarQty to message
  wait to be ordered on Vo_ReleasePartial(Ai_Line,Ai_Type)
  get Vrsrc_ReleasePartial(Ai_Line,Ai_Type)
  remove this load from Vldlst_ReleasePartial(Ai_Line,Ai_Type)
  free Vrsrc_ReleasePartial(Ai_Line,Ai_Type) 
end  /**************************************************************************************/


/*******************************************************************************************/
/**         LOGIC TO MOVE SPO CARRIERS FROM STORAGE TO WELD LINE BUFFERS                  **/
/**      IN THIS LOGIC STORAGE LOAD ATTRIBUT Ai_Zone REPRESENTS STORAGE ZONE (1,2&3)      **/
/**          IMPORTANT ATTRIBUTES TO THIS LOGIC ARE Ai_Zone, Ai_Line, Ai_Seq              **/
/*******************************************************************************************/
begin P_StorageToBuffer arriving procedure  /**------------- FROM STORAGE TO WELD BUFFERS **/
  if this load Ai_pi = 5555 then begin 
    /*if Ai_y <> 4455 then wait to be ordered on O_RowSeq(Ai_Side)*/
    set this load Ai_pi = 0
  end
  if Ai_y = 4455 then set Ai_y = 0
  
  	if this load Ai_Zone <= 0 or Ai_Zone > 3 then begin
   		print "ERROR: Cars released do not have Storage location pointer", this load current to message
    	dec C_Error by 1  
  	end    
  	if this load Ai_Line <= 0 or Ai_Line > 4 then begin
   		print "ERROR: Cars released do not have WE buffer Line #", this load current to message
    	dec C_Error by 1  
  	end
  	if this load Ai_Zone = 1 then begin
   		travel to pf_ohc:ST9_2
    	travel to pf_ohc:ST9_3
    	if Ai_pi = 4444 or Ai_Seq = 9999 or Ai_pi = 1111 then begin
        	if R_ZoneExit(Ai_Zone) status is down then begin
        		bring up R_ZoneExit(Ai_Zone)
        		print "U", this load to Lb_ZoneExit(Ai_Zone)
      		end
    	end
    	remove this load from Vldlst_ZoneExit(Ai_Zone)
    	travel to pf_ohc:ST12    
  	end
  	else if this load Ai_Zone = 2 then begin
   		travel to pf_ohc:ST9_4
    	if Ai_pi = 4444 or Ai_Seq = 9999 or Ai_pi = 1111 then begin
        	if R_ZoneExit(Ai_Zone) status is down then begin
        		bring up R_ZoneExit(Ai_Zone)
        		print "U", this load to Lb_ZoneExit(Ai_Zone)
      		end
    	end
    	remove this load from Vldlst_ZoneExit(Ai_Zone)
    	travel to pf_ohc:ST12    
  	end
  	travel to pf_ohc:ST13
  
  	if Ai_Zone = 3 then begin  
    	if Ai_pi = 4444 or Ai_Seq = 9999 or Ai_pi = 1111 then begin
      		if R_ZoneExit(Ai_Zone) status is down then begin
        		bring up R_ZoneExit(Ai_Zone)
        		print "U", this load to Lb_ZoneExit(Ai_Zone)
      		end
    	end    
    	remove this load from Vldlst_ZoneExit(Ai_Zone)
  	end
  
	/* 	if Ai_pi = 1111 and Aloc_Destination = pf_ohc:ST13 then begin
    	if O_PrevZoneExitClear(Ai_Side) current > 0 then order from O_PrevZoneExitClear(Ai_Side) to continue  
  	end	*/
  	
  /** IF IT IS LAST OF THE LOT THEN SIGNAL RELEASE LOGIC TO PROCEDE W/ NEXT LOT RELEASE  **/
  set Ai_Seq = 0 
  inc viCarrierReleaseCount(Ai_Side,1) by 1
  if viCarrierReleaseCount(Ai_Side,1) = viCarrierReleaseCount(Ai_Side,2) and O_Release(Ai_Side) current = 1 then begin
    order from O_Release(Ai_Side) case backorder on O_Release(Ai_Side)  /** BATCH RELEASE COMPLETE SIGNAL **/
    order from O_ST13_SPO_Seq(Ai_Side) case backorder on O_ST13_SPO_Seq(Ai_Side)
    set Ai_Seq = 9999
    set viCarrierReleaseCount(Ai_Side,1) = 0
  end 
  
 /* get rST13Inspector*/
  
  wait for vrStopTime(9) sec
  set Vi_x to 1
  set Vi_CarQty to Ai_CarQty
  while Vi_x <= Vi_CarQty do begin
  	set Ai_x to rn stream47 oneof(Vr_Scrap(Ai_Type)/2:1,1-Vr_Scrap(Ai_Type)/2:0)
	inc Vr_PartCount(2,Ai_Press) by Ai_x
	dec Ai_CarQty by Ai_x
	inc P_SPO_Release(Ai_Side) load list first Ai_LotQty by Ai_x
	inc Vi_x by 1
  end 
  print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
  print "Line=", Ai_Line, "-", Ai_Side, As_Type, "Lot =", P_SPO_Release(Ai_Side) load list first Ai_LotQty, "RlsQty=", P_SPO_Release(Ai_Side) load list first Ai_Seq to Lb_SPORelease(Ai_Side) 
  
   if this load Ai_Seq = 9999 then
  	order 1 load from oST13Inspect(Ai_Side) to continue case backorder on oST13Inspect(Ai_Side)

  /*free rST13Inspector*/
  
  if Ai_Line = 1  then begin /**---------------------------------------------- WE LINE #1 **/    
    travel to pf_ohc:W15
    travel to pf_ohc:W16
    set Ai_Seq = 0	
    set Ai_Status = 0
    
    if Ai_Side =1 then begin
      travel to pf_ohc:W32
      travel to pf_ohc:W23
      travel to pf_ohc:W24
    end
    else if Ai_Side =2 then begin
      set Aloc_Destination to nextof(pf_ohc:W18, pf_ohc:W19)
      travel to Aloc_Destination
      wait to be ordered on O_Weld1Order 
      travel to pf_ohc:W20
      
  	  set Vi_TypeCheck = Ai_Type
  	  if O_Weld1Order current > 0 then begin
  		order 1 load satisfying Ai_Type = Vi_TypeCheck from O_Weld1Order to continue
  			in case order not filled order 1 load from O_Weld1Order to continue
  	  end
  	  else order 1 load from O_Weld1Order to continue case backorder on O_Weld1Order 	
      
    end    
  end  /**-------------------------------------------------------------------- WE LINE #1 **/  

 else if Ai_Line = 2 then begin /**------------------------------------------ WE LINE #2 **/
    travel to pf_ohc:ST14
    travel to pf_ohc:ST146

    if Ai_Side =3 then begin
      travel to pf_ohc:ST154
      set Ai_Seq = 0	
      set Ai_Status = 0
      if Ai_Type <> viWeld2PrevType(1) and viWeld2PrevType(1) <> 0 then begin	
      	if O_Weld2Order(2) current = 1 and pf_ohc:ST158 current < pf_ohc:ST158 capacity then begin			/* pf_ohc:ST158 current = 0 */
      		order 1 load from O_Weld2Order(2) to continue		
      		wait to be ordered on O_Weld2Order(1)
      	end
      	if oWeld2Changeover current = 0 then begin
      		wait to be ordered on oWeld2Changeover
  		end 
      	else begin
      		order 1 load from oWeld2Changeover to continue
      		/*order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)*/
      	end
      end
     
     /* wait to be ordered on O_WE2BufferLH*/
     
      wait to be ordered on O_Weld2Order(1)
      set viWeld2PrevType(1) to Ai_Type
      travel to pf_ohc:ST158
      
      if oWeld2Changeover current = 1 then begin
      	if oWeld2Changeover load list first Ai_Side = this load Ai_Side then
      		order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)
      	else order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
      end
      else order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)
    
      travel to pf_ohc:ST101
      travel to pf_ohc:ST110
    end
    else if Ai_Side =4 then begin
      travel to pf_ohc:ST156 
      set Ai_Seq = 0	
      set Ai_Status = 0
      if Ai_Type <> viWeld2PrevType(2) and viWeld2PrevType(2) <> 0 then begin
      	if O_Weld2Order(1) current = 1 and pf_ohc:ST158 current < pf_ohc:ST158 capacity then begin			/* pf_ohc:ST158 current = 0 */
      		order 1 load from O_Weld2Order(1) to continue
      		wait to be ordered on O_Weld2Order(2)
      	end      	
      	if oWeld2Changeover current = 0 then begin
      		wait to be ordered on oWeld2Changeover
      	end
     	else begin
     		order 1 load from oWeld2Changeover to continue
     		/*order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)*/
     	end
      end
     
      /*wait to be ordered on O_WE2BufferRH*/
     
      wait to be ordered on O_Weld2Order(2)
      set viWeld2PrevType(2) to Ai_Type
      travel to pf_ohc:ST158
      
      if oWeld2Changeover current = 1 then begin
      	if oWeld2Changeover load list first Ai_Side = this load Ai_Side then
      		order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
      	else order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)
      end
      else order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
      
      travel to pf_ohc:ST101
	  travel to pf_ohc:ST115
    end
  end  /**-------------------------------------------------------------------- WE LINE #2 **/

  if O_WeldBufferStarve(Ai_Side) current > 0 then 
    order from O_WeldBufferStarve(Ai_Side) to continue     
  if Ai_Status <> 1 then
  	wait to be ordered on O_WeldBuffer(Ai_Side)	
           
  send to P_Unload(Ai_Side)
end /**--------------------- LOGIC TO MOVE SPO CARRIERS FROM STORAGE TO WELD LINE BUFFERS **/
/*******************************************************************************************/

/*******************************************************************************************/
/****                      POWER & FREE CONVEYOR PROCEDURES                            *****/
/*******************************************************************************************/
begin pf_ohc:ST101 leaving procedure
  if this load Ai_Side = 3 then order from O_WE2BufferLH case backorder on O_WE2BufferLH
end
begin pf_ohc:ST115 leaving procedure
  if this load Ai_Side = 4 then order from O_WE2BufferRH case backorder on O_WE2BufferRH   
end
begin pf_ohc:ST134 arriving procedure
	if pf_ohc:ST134 current <= 1 and viKickOutQty(2) = 0 then begin
		set viKickOutQty(2) = pf_ohc:ST15 current
		order viKickOutQty(2) loads from olCarKickout to continue case backorder on olCarKickout
	end	
end

begin  pf_ohc:LS_13 cleared procedure  /**------------------- PHOTOEYE @ WE2 BUFFER ST158 **/
  if this load Ai_Side =3 and ST152 current = 0 and ST153 current = 0 and Vi_WEBufferOrderTracker(3) <> 1 then begin
    set Vi_WEBufferOrderTracker(3) = 1
    set Vi_SPO_QtyRelease(3) = Vi_Release    
    order from O_BufferSpace(3) case backorder on O_BufferSpace(3)    
  end
  else if this load Ai_Side = 4 and ST155 current + ST156 current <= 4 and Vi_WEBufferOrderTracker(4) <> 1 then begin
    set Vi_WEBufferOrderTracker(4) = 1
    set Vi_SPO_QtyRelease(4) = Vi_Release    
    order from O_BufferSpace(4) case backorder on O_BufferSpace(4)    
  end 
end  /**----------------------------------------------------- PHOTOEYE @ WE2 BUFFER ST158 **/


begin  pf_ohc:WEBufferReceived cleared procedure  /**------ PHOTOEYE @ WE BUFFER RECEIVED **/
   if this load Ai_Line = 1 then 
   	inc C_L1Weld(this load Ai_Side) by 1
   
   if this load Ai_Seq = 9999 and Vi_WEBufferOrderTracker(this load Ai_Side) = 1 then begin 
      set Vi_WEBufferOrderTracker(this load Ai_Side) = 0      /** ORDERED RECEIVED TOGGLE **/      
      set Vi_SPO_QtyRelease(this load Ai_Side) = 0
   end
end  /**--------------------------------------------------- PHOTOEYE @ WE BUFFER RECEIVED **/


begin  pf_ohc:LS_17 cleared procedure  /**----------- PHOTOEYE @ WE1 BUFFER REORDER POINT **/
  if W15 current = 0 and 
     Vi_WEBufferOrderTracker(1) <> 1 and
     Vi_WEBufferOrderTracker(2) <> 1 then begin

	 if P_SPO_Release(1) current > 0 then begin 
 	 	set Vi_WEBufferOrderTracker(1) = 1
   		set Vi_SPO_QtyRelease(1) = Vi_Release    
     	order from O_BufferSpace(1) case backorder on O_BufferSpace(1)
	 end

	 if P_SPO_Release(2) current > 0 then begin
     	set Vi_WEBufferOrderTracker(2) = 1
     	set Vi_SPO_QtyRelease(2) = Vi_Release
     	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)       
  	 end
  end	
end  /**---------------------------------------------- PHOTOEYE @ WE1 BUFFER REORDER POINT**/

/*begin pf_ohc:LS_Recirc cleared procedure 
/*	if Vld_Recirc(2) <> null then begin
		if this load = Vld_Recirc(2) then
	end	*/
	if this load Ai_z = 5454 then begin
		set Ai_z = 1
	end
end*/
begin pf_ohc:W13 arriving procedure
	if this load = Vld_Recirc(2) then set Vld_Recirc(2) = null
end
begin pf_ohc:ST14 arriving procedure
	if this load = Vld_Recirc(2) then set Vld_Recirc(2) = null
end


/*
begin pf_ohc:LS_17 cleared procedure /**----------- PHOTOEYE @ WE1 BUFFER REORDER POINT **/
  if this load Ai_Side = 1 and Vi_WEBufferOrderTracker(1) <> 1 and C_L1Weld(1) current < 8 then begin
  	set Vi_WEBufferOrderTracker(1) = 1
  	set Vi_SPO_QtyRelease(1) = Vi_Release
  	order from O_BufferSpace(1) case backorder on O_BufferSpace(1)
  end
  else if this load Ai_Side = 2 and Vi_WEBufferOrderTracker(2) <> 1 and C_L1Weld(2) current < 8 then begin
  	set Vi_WEBufferOrderTracker(2) = 1
  	set Vi_SPO_QtyRelease(2) = Vi_Release
  	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)
  end
end /**---------------------------------------------- PHOTOEYE @ WE1 BUFFER REORDER POINT**/
*/

begin pf_ohc:RowSwitch cleared procedure /**------------------- EACH ZONE EXIT ROW SWITCH **/
  if this load type <> L_invis begin
    if this load Ai_pi = 4444 then
      order 1 load from O_ReleaseSeq(this load Ai_Side) /* O_RowSeq(this load Ai_Side) */ /* case backorder on O_RowSeq(this load Ai_Side) */
    else if Ai_Side > 0 then begin
      if Vld_ReleasePrev(this load Ai_Side) = this load then set Vld_ReleasePrev(this load Ai_Side) = null  
    end
  end
  
  if this load = Vld_Recirc(2) then
		order 1 load from oZoneMixRecircComplete to continue
  
end  /**------------------------------------------------------- EACH ZONE EXIT ROW SWITCH **/

/*
begin pf_ohc:LS_26 cleared procedure
	if C_L1Weld(this load Ai_Side) current > 0 then dec C_L1Weld(this load Ai_Side) by 1
end

begin pf_ohc:LS_23 cleared procedure
	if C_L1Weld(this load Ai_Side) current > 0 then dec C_L1Weld(this load Ai_Side) by 1
end

begin pf_ohc:LS_28 cleared procedure
  if Vi_WEBufferOrderTracker(1) <> 1 and C_L1Weld(1) current < 13 then begin
  	set Vi_WEBufferOrderTracker(1) = 1
  	set Vi_SPO_QtyRelease(1) = Vi_Release
  	if O_L1Weld(2) current > 0 then order all loads from O_L1Weld(2) to continue 
  	if C_L1Weld(2) current = 0 and this load Ai_Seq = 9999 then wait to be ordered on O_L1Weld(1)
  	if pf_ohc:W20 total = 0 then wait to be ordered on O_Line1Start
  	order from O_BufferSpace(1) case backorder on O_BufferSpace(1)
  end	
end

begin pf_ohc:LS_19 cleared procedure
  if Vi_WEBufferOrderTracker(2) <> 1 and C_L1Weld(2) current < 13 then begin
  	set Vi_WEBufferOrderTracker(2) = 1
  	set Vi_SPO_QtyRelease(2) = Vi_Release
  	if O_L1Weld(1) current > 0 then order all loads from O_L1Weld(1) to continue 
  	if C_L1Weld(1) current = 0 and this load Ai_Seq = 9999 then wait to be ordered on O_L1Weld(2)
  	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)
  end
end

begin pf_ohc:LS_21 cleared procedure
  if Vi_WEBufferOrderTracker(2) <> 1 and C_L1Weld(2) current < 13 then begin
  	set Vi_WEBufferOrderTracker(2) = 1
  	set Vi_SPO_QtyRelease(2) = Vi_Release
  	if O_L1Weld(1) current > 0 then order all loads from O_L1Weld(1) to continue 
  	if C_L1Weld(1) current = 0 and this load Ai_Seq = 9999 then wait to be ordered on O_L1Weld(2)
  	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)
  end
end

begin pf_ohc:W20 arriving procedure
	if pf_ohc:W20 total = 1 then order 1 load from O_Line1Start to continue
	wait for 0
end */


/*******************************************************************************************/
/**                        UNLOAD AT SPO LINE WELD STATIONS LOGIC                         **/
/**  WHEN CARRIERS ARE RELEASED FROM EITHER BUFFER OR FROM PARTIAL BUFFER, THEY ARRIVE AT
     RESPECTIVE LOAD STATIONS.  IT WILL WAIT FOR RBT SIGNAL FOR PICK ACTIVITY, THEN RBT 
     IS SIGNALED TO PICK, ONCE PICKED BY RBT, IT WILL ADJUST ITS QTY.  IF THERE IS A FLAG
     FOR PARTIAL STORAGE BUFFER AFTER CHANGE OVER, THEN THE CARRIER IS SENT TO PARTIAL 
     STORAGE LOGIC.  LOADS ARRIVE FROM WELD BUFFER RELEASE OR PARTIAL BUFFER RELEASE 
                               PROCINDEX = 1 for LINE #1 LH
                               PROCINDEX = 2 for LINE #1 RH
                               PROCINDEX = 3 for LINE #2 LH
                               PROCINDEX = 4 for LINE #2 RH                               **/ 
/*******************************************************************************************/
begin P_Unload arriving procedure
  travel to Vloc_B4Unload(procindex)
  travel to Vloc_Unload(procindex)

  /**---------- IF LAST OF PARTIAL BUFFER LINE RELEASED< SIGNAL RELEASE COMPLETE ---------**/  
  if Ai_Status = 7777 and Ai_Seq = 9999 and O_WaitForPartialRelease(procindex) current > 0 then
    order from O_WaitForPartialRelease(procindex) to continue
  set Vldptr_Unload(procindex) to this load   /** FOR WELD PROCESS TO SET PARTIAL STORAGE **/
  
  if Vldlst_PartLane(procindex,1) size > 0 then begin
  	if Vldlst_PartLane(procindex,1) last Ai_Type = this load Ai_Type then begin
  		set Vldlst_PartLane(procindex,1) last Ai_CarQty to Vldlst_PartLane(procindex,1) last Ai_CarQty + this load Ai_CarQty
  		scale Vldlst_PartLane(procindex,1) last to z Ai_CarQty
  		set Vldptr_Unload(procindex) to null
  		set this load type to L_invis  
  		set Ai_Status = 7777
  		dec C_CarrierCount(1) by 1
  		inc C_CarrierCount(2) by 1
  		if C_CarrierCount(2) current >= Vi_EmptyReq and O_HoldForCarriers current >= 1 then
  		order 1 load from O_HoldForCarriers to continue 
  		send to pEmptyCarrierReturn
  	end	
  end	
       
  while Ai_CarQty > 0 do begin
    wait to be ordered on Vo_SPO(procindex,1)            /** LOAD SIGNAL FROM SPO PROCESS **/    
   	if Ai_Status = 8888 then send to P_PartialStorage(procindex)      /** PARTIAL STORAGE **/
   	order from Vo_RBT(procindex,1) case backorder on Vo_RBT(procindex,1)   /** SIG T/PICK **/
   	wait to be ordered on Vo_RBT(procindex,2)                    /** WAIT FOR RBT TO PICK **/   
   	wait to be ordered on Vo_RBT(procindex,3)                     /** WAIT TILL RBT CLEAR **/
   	dec Ai_CarQty by 1
   	if Ai_CarQty > 0 then scale to z Ai_CarQty
   	print ac - Vr_WeldCycleTime(procindex) as .2 to Lb_WeldCycle(procindex) 
   	set Vr_WeldCycleTime(procindex) to ac
  end
  set Vldptr_Unload(procindex) to null
  set this load type to L_invis  
  set Ai_Status = 7777
  dec C_CarrierCount(1) by 1
  inc C_CarrierCount(2) by 1
  if C_CarrierCount(2) current >= Vi_EmptyReq and O_HoldForCarriers current >= 1 then
  	order 1 load from O_HoldForCarriers to continue 
  send to pEmptyCarrierReturn
end  /**************************************************************************************/
/*******************************************************************************************/


/*******************************************************************************************/
/**                          LOGIC TO STORE PARTIAL CARRIERS                              **/
/** IMPORTANT ATTRIBUTES TO PASS are Ai_Line                                              **/
/**                      Ai_Status = 8888 Partial Storage                                  **/
/**                      Ai_Status = 7777 Partial Release                                  **/
/**                      Ai_Status = 6666 Partial ReCirc                                   **/
/**                            PROCINDEX = 1 for LINE #1 LH
                               PROCINDEX = 2 for LINE #1 RH
                               PROCINDEX = 3 for LINE #2 LH
                               PROCINDEX = 4 for LINE #2 RH                               **/ 
/*******************************************************************************************/
begin P_PartialStorage arriving procedure
  if Vloc_Partial(procindex,1) remaining = 0 and  Vloc_Partial(procindex,2) remaining = 0 then begin
    print "NO SPACE @ PARTIAL BUFFER", this load, procindex to message
    dec C_Error by 1  
  end  
  insert this load into Vldlst_Partial(procindex, Ai_Type)
  if Vloc_Partial(procindex,1) current < Vloc_Partial(procindex,1) capacity then begin
    insert this load into Vldlst_PartLane(procindex,1)
    travel to Vloc_Partial(procindex,1)
    if Ai_Status <> 7777 or Ai_Status <> 6666 then 
      wait to be ordered on Vo_PartialBuffer(procindex,1)
    remove this load from Vldlst_PartLane(procindex,1)      
  end
  else if Vloc_Partial(procindex,2) current < Vloc_Partial(procindex,2) capacity then begin
    insert this load into Vldlst_PartLane(procindex,1)
    travel to Vloc_Partial(procindex,2)
    if Ai_Status <> 7777 or Ai_Status <> 6666 then 
      wait to be ordered on Vo_PartialBuffer(procindex,2)  
    remove this load from Vldlst_PartLane(procindex,2)      
  end
  remove this load from Vldlst_Partial(procindex, Ai_Type)
  
  if Ai_Status = 6666 then begin      /** IF RE_CIRC THEN INSERT BACK INTO PARTIAL BUFFER **/
    travel to Vloc_Unload(procindex)
    set Ai_Status = 0
    set Ai_Seq = 0
    send to P_PartialStorage(procindex)
  end
  else if Ai_Status = 7777 then send to P_Unload(procindex)     /** SEND TO UNLOAD PROCESS **/
  else begin
    print "ERROR in setting Ai_Status value @ line", procindex, "partial buffer:  CHECK", this load to message
    dec C_Error by 1  
  end
end
/*******************************************************************************************/


/*******************************************************************************************/
/**                        PARTIAL SPO BUFFER RELEASE LOGIC                               **/
/**   THE LOADS ARE CLONED FROM  P_SPO_Process AS NEEDED BASED ON PARTIAL BUFFER STATUS   **/ 
/*******************************************************************************************/
begin P_PartialRelease arriving procedure
  /** SET PARTIAL RELEASE CODE FOR THE LOADS IN THE SAME TYPE LIST **/
  for each Vldptr_Partial(procindex) in Vldlst_Partial(procindex, Ai_Type) do begin
    set Vldptr_Partial(procindex) Ai_Status = 7777          /** PARTIAL RELEASE INDICATOR **/  
  end
  set Ai_pi = 0
  while Ai_pi < 2 do begin  /**-------------------- WHILE LOOP FOR 2 PARTIAL BUFFER LINES **/    
    inc Ai_pi by 1   
    if Vldlst_Partial(procindex, Ai_Type) size = 0 then break     
    set Ai_Seq = 0
    set Vi_PartialIndex(procindex) = 0
    for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
      inc Vi_PartialIndex(procindex) by 1  
      if Vldptr_Partial(procindex) Ai_Status = 7777 then begin
        set Ai_Seq = Vi_PartialIndex(procindex)-1
        break
      end  
    end    
    set Ai_CarQty = 0
    for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
      inc Ai_CarQty by 1  
      if Vldptr_Partial(procindex) Ai_Status = 7777 then set Ai_Row = Ai_CarQty 
    end
    
    if Ai_Seq > 0 then begin  /**----------- CAR TO RELEASES IS NOT IN THE FIRST POSITION **/
      set Ai_CarQty = 0 
      for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
        inc Ai_CarQty by 1 
        if Vldptr_Partial(procindex) Ai_Status <> 7777 then begin
          set Vldptr_Partial(procindex) Ai_Status = 6666              /** SET RE-CIR CODE **/
        end
        /** FIND LAST CAR TO BE RELEASED & SET LAST CAR CODE **/
        else if Vldptr_Partial(procindex) Ai_Status = 7777 then begin
          if Ai_CarQty = Ai_Row then set Vldptr_Partial(procindex) Ai_Seq = 9999
          break 
        end 
      end
      order from Vo_PartialBuffer(procindex,Ai_pi) to continue            /** RELEASE CAR **/     
      wait to be ordered on O_WaitForPartialRelease(procindex)   /** WAIT FOR THE RELEASE **/      
    end

    else if Ai_Seq = 0 then begin    /**----- CAR TO BE RELEASED IS IN THE FIRST POSITION **/
      set Ai_CarQty = 0  /**------------ FIND LAST CAR TO BE RELEASED & SET LAST CAR CODE **/ 
      for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
        inc Ai_CarQty by 1 
        if Vldptr_Partial(procindex) Ai_Status = 7777 then begin
          if Ai_CarQty = Ai_Row then set Vldptr_Partial(procindex) Ai_Seq = 9999
          break 
        end 
      end  /**-------------------------- FIND LAST CAR TO BE RELEASED & SET LAST CAR CODE **/    
      order from Vo_PartialBuffer(procindex,Ai_pi) to continue           /** RELEASE CAR) **/     
      wait to be ordered on O_WaitForPartialRelease(procindex)   /** WAIT FOR THE RELEASE **/          
    end  /**--------------------------------- CAR TO BE RELEASED IS IN THE FIRST POSITION **/   
  end   /**---------------------------------------- WHILE LOOP FOR 2 PARTIAL BUFFER LINES **/ 
  order from O_PartialRelease(procindex) to continue  /** PARTIAL RELEASE COMPLETE SIGNAL **/
end
/*******************************************************************************************/


/*******************************************************************************************/
/** LOADS ARE CLONED FROM THE INITIALIZATION PROCESS & WILL STAY TILL END OF SIMULATION   **/
/**                       PROCINDEX = 1 for LINE #1 LH
                          PROCINDEX = 2 for LINE #1 RH
                          PROCINDEX = 3 for LINE #2 LH
                          PROCINDEX = 4 for LINE #2 RH                                    **/ 
/*******************************************************************************************/
begin P_LoadRBT arriving procedure 
  move into Vloc_LoadRBT(procindex,1)                                            /** HOME **/    
  while 1=1 do begin
    wait to be ordered on Vo_SPO(procindex,1)  /** WAIT FOR LOAD SIGNAL FROM SPO PROCESS  **/
    travel to Vloc_LoadRBT(procindex,2)                                       /** POUNCE1 **/
    wait to be ordered on Vo_RBT(procindex,1)       /** WAIT FOR CARRIER SIGNAL TO UNLOAD **/
    travel to Vloc_LoadRBT(procindex,3)                        /** WORK1 OR PICK POSITION **/
    /** SET LOAD TYPE & ATTR VALUES CHANGE LOAD TYPE **/
    order from Vo_RBT(procindex,2) to continue                            /** PICK SIGNAL **/
    set Ai_Type = Vldptr_Unload(procindex) Ai_Type
    set this load type to vlt_LoadType(Ai_Type)
    set this load color to vclr_LoadColor(Ai_Type)    
    travel to Vloc_LoadRBT(procindex,4)                              /** CLEAR 1 POSITION **/
    order from Vo_RBT(procindex,3) to continue                           /** CLEAR SIGNAL **/

    travel to Vloc_LoadRBT(procindex,5)                              /** POUNCE2 POSITION **/    
    travel to Vloc_LoadRBT(procindex,6)                       /** WORK 2 OR DROP POSITION **/
        
    order from Vo_SPO(procindex,2) to continue                           /** LOAD SIGNAL  **/ 
    set load type to L_invis
    travel to Vloc_LoadRBT(procindex,7)                              /** Clear 2 POSITION **/
    /** RESET ATTR, LOAD TYPE **/
    order from Vo_SPO(procindex,3) to continue                           /** Clear SIGNAL **/ 
    travel to Vloc_LoadRBT(procindex,1) /** HOME **/  
  end
end  /**************************************************************************************/


begin Sr_WriteJSONFile
	print "{" to "base.arc/RESULTS/JSON.out"
	print '"messageType":"' vldWriteJSON As_Message '"' to "base.arc/RESULTS/JSON.out"
	print '"tagID":"1111"' to "base.arc/RESULTS/JSON.out"
	print '"carrierNumber":"' vldWriteJSON vehicle '"' to "base.arc/RESULTS/JSON.out"
	print '"dieNumber":"' vldWriteJSON Ai_LotNo '"' to "base.arc/RESULTS/JSON.out"
	print '"quantity":"' vldWriteJSON Ai_CarQty '"' to "base.arc/RESULTS/JSON.out"
	if vldWriteJSON Ai_Type <> 99 then
		print '"productionDate":"' F_time_to_date(vldWriteJSON At_TimeInSys, Vs_SimStart) '"' to "base.arc/RESULTS/JSON.out"
	else
		print '"productionDate":"null"' to "base.arc/RESULTS/JSON.out"
	print '"model":"' vldWriteJSON As_Type '"' to "base.arc/RESULTS/JSON.out"
	print '"currentLocation":"' vldWriteJSON As_CurrentLocation '"' to "base.arc/RESULTS/JSON.out"
	print '"nextLocation":"' vldWriteJSON As_NextLocation '"' to "base.arc/RESULTS/JSON.out"
	print "}" to "base.arc/RESULTS/JSON.out"
	print "" to "base.arc/RESULTS/JSON.out"
end

begin F_ConnectSocket function
	set vsTemp = sock_MachineName()
	if vsTemp <> null then begin
		call sock_SetConnectBlocking(true)
		set vsockSocket = sock_ConnectPort(vsTemp, 3455)
		if sock_IsValid(vsockSocket) = true then begin
			call sock_SetNonBlocking(vsockSocket, false)	// vsockSocket is now blocking
			return true
		end
	end
	return false
end

begin F_RequestMove function
	if sock_IsValid(vsockSocket) = false then
		return null
	if argLoad Ai_Type <> 99 then
		print F_time_to_date(argLoad At_TimeInSys, Vs_SimStart) to vsTemp
	else
		print "null" to vsTemp
	print "{" 
		'"messageType":"' argLoad As_Message '",'
		'"tagID":"1111"'
		'"carrierNumber":"' argLoad vehicle '",'
		'"dieNumber":"' argLoad Ai_LotNo '",'
		'"quantity":"' argLoad Ai_CarQty '",'
		'"productionDate":"' vsTemp '",'
		'"model":"' argLoad As_Type '",'
		'"currentLocation":"' argLoad As_CurrentLocation '",'
		'"nextLocation":"' argLoad As_NextLocation '"'
		"}" to vsMessage
	if sock_SendString(vsockSocket, vsMessage) = 0 then begin
		set vsockSocket = null	// No longer valid- socket closed
		return null
	end
	set vsReponse = sock_ReadString(vsockSocket)
	if vsReponse = null then
		return null
	set vjsonStruct = cJSON_Parse(vsResponse)
	set vsTemp = cJSON_GetObjectString(vjsonStruct, "nextLocation")
	call cJSON_Delete(vjsonStruct)
	set vjsonStruct = null
	return vsTemp
end


#@!
SFileBegin	name cJSON.h
/*
  Copyright (c) 2009 Dave Gamble
 
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
 
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
 
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/

#ifndef cJSON__h
#define cJSON__h

#ifdef __cplusplus
extern "C"
{
#endif

/* cJSON Types: */
#define cJSON_False 0
#define cJSON_True 1
#define cJSON_NULL 2
#define cJSON_Number 3
#define cJSON_String 4
#define cJSON_Array 5
#define cJSON_Object 6
	
#define cJSON_IsReference 256

/* The cJSON structure: */
typedef struct cJSON {
	struct cJSON *next,*prev;	/* next/prev allow you to walk array/object chains. Alternatively, use GetArraySize/GetArrayItem/GetObjectItem */
	struct cJSON *child;		/* An array or object item will have a child pointer pointing to a chain of the items in the array/object. */

	int type;					/* The type of the item, as above. */

	char *valuestring;			/* The item's string, if type==cJSON_String */
	int valueint;				/* The item's number, if type==cJSON_Number */
	double valuedouble;			/* The item's number, if type==cJSON_Number */

	char *string;				/* The item's name string, if this item is the child of, or is in the list of subitems of an object. */
} cJSON;

typedef struct cJSON_Hooks {
      void *(*malloc_fn)(size_t sz);
      void (*free_fn)(void *ptr);
} cJSON_Hooks;

/* Supply malloc, realloc and free functions to cJSON */
extern void cJSON_InitHooks(cJSON_Hooks* hooks);


/* Supply a block of JSON, and this returns a cJSON object you can interrogate. Call cJSON_Delete when finished. */
extern cJSON *cJSON_Parse(const char *value);
/* Render a cJSON entity to text for transfer/storage. Free the char* when finished. */
extern char  *cJSON_Print(cJSON *item);
/* Render a cJSON entity to text for transfer/storage without any formatting. Free the char* when finished. */
extern char  *cJSON_PrintUnformatted(cJSON *item);
/* Delete a cJSON entity and all subentities. */
extern void   cJSON_Delete(cJSON *c);

/* Returns the number of items in an array (or object). */
extern int	  cJSON_GetArraySize(cJSON *array);
/* Retrieve item number "item" from array "array". Returns NULL if unsuccessful. */
extern cJSON *cJSON_GetArrayItem(cJSON *array,int item);
/* Get item "string" from object. Case insensitive. */
extern cJSON *cJSON_GetObjectItem(cJSON *object,const char *string);

/* For analysing failed parses. This returns a pointer to the parse error. You'll probably need to look a few chars back to make sense of it. Defined when cJSON_Parse() returns 0. 0 when cJSON_Parse() succeeds. */
extern const char *cJSON_GetErrorPtr();
	
/* These calls create a cJSON item of the appropriate type. */
extern cJSON *cJSON_CreateNull();
extern cJSON *cJSON_CreateTrue();
extern cJSON *cJSON_CreateFalse();
extern cJSON *cJSON_CreateBool(int b);
extern cJSON *cJSON_CreateNumber(double num);
extern cJSON *cJSON_CreateString(const char *string);
extern cJSON *cJSON_CreateArray();
extern cJSON *cJSON_CreateObject();

/* These utilities create an Array of count items. */
extern cJSON *cJSON_CreateIntArray(int *numbers,int count);
extern cJSON *cJSON_CreateFloatArray(float *numbers,int count);
extern cJSON *cJSON_CreateDoubleArray(double *numbers,int count);
extern cJSON *cJSON_CreateStringArray(const char **strings,int count);

/* Append item to the specified array/object. */
extern void cJSON_AddItemToArray(cJSON *array, cJSON *item);
extern void	cJSON_AddItemToObject(cJSON *object,const char *string,cJSON *item);
/* Append reference to item to the specified array/object. Use this when you want to add an existing cJSON to a new cJSON, but don't want to corrupt your existing cJSON. */
extern void cJSON_AddItemReferenceToArray(cJSON *array, cJSON *item);
extern void	cJSON_AddItemReferenceToObject(cJSON *object,const char *string,cJSON *item);

/* Remove/Detatch items from Arrays/Objects. */
extern cJSON *cJSON_DetachItemFromArray(cJSON *array,int which);
extern void   cJSON_DeleteItemFromArray(cJSON *array,int which);
extern cJSON *cJSON_DetachItemFromObject(cJSON *object,const char *string);
extern void   cJSON_DeleteItemFromObject(cJSON *object,const char *string);
	
/* Update array items. */
extern void cJSON_ReplaceItemInArray(cJSON *array,int which,cJSON *newitem);
extern void cJSON_ReplaceItemInObject(cJSON *object,const char *string,cJSON *newitem);

#define cJSON_AddNullToObject(object,name)	cJSON_AddItemToObject(object, name, cJSON_CreateNull())
#define cJSON_AddTrueToObject(object,name)	cJSON_AddItemToObject(object, name, cJSON_CreateTrue())
#define cJSON_AddFalseToObject(object,name)		cJSON_AddItemToObject(object, name, cJSON_CreateFalse())
#define cJSON_AddNumberToObject(object,name,n)	cJSON_AddItemToObject(object, name, cJSON_CreateNumber(n))
#define cJSON_AddStringToObject(object,name,s)	cJSON_AddItemToObject(object, name, cJSON_CreateString(s))

#ifdef __cplusplus
}
#endif

#endif

#@!
SFileBegin	name cJSON.c
/*
  Copyright (c) 2009 Dave Gamble

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/

/* cJSON */
/* JSON parser in C. */

#include <string.h>
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <float.h>
#include <limits.h>
#include <ctype.h>
#include "cJSON.h"

static const char *ep;

const char *cJSON_GetErrorPtr() {return ep;}

static int cJSON_strcasecmp(const char *s1,const char *s2)
{
	if (!s1) return (s1==s2)?0:1;if (!s2) return 1;
	for(; tolower(*s1) == tolower(*s2); ++s1, ++s2)	if(*s1 == 0)	return 0;
	return tolower(*(const unsigned char *)s1) - tolower(*(const unsigned char *)s2);
}

static void *(*cJSON_malloc)(size_t sz) = malloc;
static void (*cJSON_free)(void *ptr) = free;

static char* cJSON_strdup(const char* str)
{
      size_t len;
      char* copy;

      len = strlen(str) + 1;
      if (!(copy = (char*)cJSON_malloc(len))) return 0;
      memcpy(copy,str,len);
      return copy;
}

void cJSON_InitHooks(cJSON_Hooks* hooks)
{
    if (!hooks) { /* Reset hooks */
        cJSON_malloc = malloc;
        cJSON_free = free;
        return;
    }

	cJSON_malloc = (hooks->malloc_fn)?hooks->malloc_fn:malloc;
	cJSON_free	 = (hooks->free_fn)?hooks->free_fn:free;
}

/* Internal constructor. */
static cJSON *cJSON_New_Item()
{
	cJSON* node = (cJSON*)cJSON_malloc(sizeof(cJSON));
	if (node) memset(node,0,sizeof(cJSON));
	return node;
}

/* Delete a cJSON structure. */
void cJSON_Delete(cJSON *c)
{
	cJSON *next;
	while (c)
	{
		next=c->next;
		if (!(c->type&cJSON_IsReference) && c->child) cJSON_Delete(c->child);
		if (!(c->type&cJSON_IsReference) && c->valuestring) cJSON_free(c->valuestring);
		if (c->string) cJSON_free(c->string);
		cJSON_free(c);
		c=next;
	}
}

/* Parse the input text to generate a number, and populate the result into item. */
static const char *parse_number(cJSON *item,const char *num)
{
	double n=0,sign=1,scale=0;int subscale=0,signsubscale=1;

	/* Could use sscanf for this? */
	if (*num=='-') sign=-1,num++;	/* Has sign? */
	if (*num=='0') num++;			/* is zero */
	if (*num>='1' && *num<='9')	do	n=(n*10.0)+(*num++ -'0');	while (*num>='0' && *num<='9');	/* Number? */
	if (*num=='.') {num++;		do	n=(n*10.0)+(*num++ -'0'),scale--; while (*num>='0' && *num<='9');}	/* Fractional part? */
	if (*num=='e' || *num=='E')		/* Exponent? */
	{	num++;if (*num=='+') num++;	else if (*num=='-') signsubscale=-1,num++;		/* With sign? */
		while (*num>='0' && *num<='9') subscale=(subscale*10)+(*num++ - '0');	/* Number? */
	}

	n=sign*n*pow(10.0,(scale+subscale*signsubscale));	/* number = +/- number.fraction * 10^+/- exponent */
	
	item->valuedouble=n;
	item->valueint=(int)n;
	item->type=cJSON_Number;
	return num;
}

/* Render the number nicely from the given item into a string. */
static char *print_number(cJSON *item)
{
	char *str;
	double d=item->valuedouble;
	if (fabs(((double)item->valueint)-d)<=DBL_EPSILON && d<=INT_MAX && d>=INT_MIN)
	{
		str=(char*)cJSON_malloc(21);	/* 2^64+1 can be represented in 21 chars. */
		if (str) sprintf(str,"%d",item->valueint);
	}
	else
	{
		str=(char*)cJSON_malloc(64);	/* This is a nice tradeoff. */
		if (str)
		{
			if (fabs(floor(d)-d)<=DBL_EPSILON)			sprintf(str,"%.0f",d);
			else if (fabs(d)<1.0e-6 || fabs(d)>1.0e9)	sprintf(str,"%e",d);
			else										sprintf(str,"%f",d);
		}
	}
	return str;
}

/* Parse the input text into an unescaped cstring, and populate item. */
static const unsigned char firstByteMark[7] = { 0x00, 0x00, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC };
static const char *parse_string(cJSON *item,const char *str)
{
	const char *ptr=str+1;char *ptr2;char *out;int len=0;unsigned uc;
	if (*str!='\"') {ep=str;return 0;}	/* not a string! */
	
	while (*ptr!='\"' && *ptr && ++len) if (*ptr++ == '\\') ptr++;	/* Skip escaped quotes. */
	
	out=(char*)cJSON_malloc(len+1);	/* This is how long we need for the string, roughly. */
	if (!out) return 0;
	
	ptr=str+1;ptr2=out;
	while (*ptr!='\"' && *ptr)
	{
		if (*ptr!='\\') *ptr2++=*ptr++;
		else
		{
			ptr++;
			switch (*ptr)
			{
				case 'b': *ptr2++='\b';	break;
				case 'f': *ptr2++='\f';	break;
				case 'n': *ptr2++='\n';	break;
				case 'r': *ptr2++='\r';	break;
				case 't': *ptr2++='\t';	break;
				case 'u':	 /* transcode utf16 to utf8. DOES NOT SUPPORT SURROGATE PAIRS CORRECTLY. */
					sscanf(ptr+1,"%4x",&uc);	/* get the unicode char. */
					len=3;if (uc<0x80) len=1;else if (uc<0x800) len=2;ptr2+=len;
					
					switch (len) {
						case 3: *--ptr2 =((uc | 0x80) & 0xBF); uc >>= 6;
						case 2: *--ptr2 =((uc | 0x80) & 0xBF); uc >>= 6;
						case 1: *--ptr2 =(uc | firstByteMark[len]);
					}
					ptr2+=len;ptr+=4;
					break;
				default:  *ptr2++=*ptr; break;
			}
			ptr++;
		}
	}
	*ptr2=0;
	if (*ptr=='\"') ptr++;
	item->valuestring=out;
	item->type=cJSON_String;
	return ptr;
}

/* Render the cstring provided to an escaped version that can be printed. */
static char *print_string_ptr(const char *str)
{
	const char *ptr;char *ptr2,*out;int len=0;unsigned char token;
	
	if (!str) return cJSON_strdup("");
	ptr=str;while ((token=*ptr) && ++len) {if (strchr("\"\\\b\f\n\r\t",token)) len++; else if (token<32) len+=5;ptr++;}
	
	out=(char*)cJSON_malloc(len+3);
	if (!out) return 0;

	ptr2=out;ptr=str;
	*ptr2++='\"';
	while (*ptr)
	{
		if ((unsigned char)*ptr>31 && *ptr!='\"' && *ptr!='\\') *ptr2++=*ptr++;
		else
		{
			*ptr2++='\\';
			switch (token=*ptr++)
			{
				case '\\':	*ptr2++='\\';	break;
				case '\"':	*ptr2++='\"';	break;
				case '\b':	*ptr2++='b';	break;
				case '\f':	*ptr2++='f';	break;
				case '\n':	*ptr2++='n';	break;
				case '\r':	*ptr2++='r';	break;
				case '\t':	*ptr2++='t';	break;
				default: sprintf(ptr2,"u%04x",token);ptr2+=5;	break;	/* escape and print */
			}
		}
	}
	*ptr2++='\"';*ptr2++=0;
	return out;
}
/* Invote print_string_ptr (which is useful) on an item. */
static char *print_string(cJSON *item)	{return print_string_ptr(item->valuestring);}

/* Predeclare these prototypes. */
static const char *parse_value(cJSON *item,const char *value);
static char *print_value(cJSON *item,int depth,int fmt);
static const char *parse_array(cJSON *item,const char *value);
static char *print_array(cJSON *item,int depth,int fmt);
static const char *parse_object(cJSON *item,const char *value);
static char *print_object(cJSON *item,int depth,int fmt);

/* Utility to jump whitespace and cr/lf */
static const char *skip(const char *in) {while (in && *in && (unsigned char)*in<=32) in++; return in;}

/* Parse an object - create a new root, and populate. */
cJSON *cJSON_Parse(const char *value)
{
	cJSON *c;
	ep=0;
	c=cJSON_New_Item();
	if (!c) return 0;       /* memory fail */

	if (!parse_value(c,skip(value))) {cJSON_Delete(c);return 0;}
	return c;
}

/* Render a cJSON item/entity/structure to text. */
char *cJSON_Print(cJSON *item)				{return print_value(item,0,1);}
char *cJSON_PrintUnformatted(cJSON *item)	{return print_value(item,0,0);}

/* Parser core - when encountering text, process appropriately. */
static const char *parse_value(cJSON *item,const char *value)
{
	if (!value)						return 0;	/* Fail on null. */
	if (!strncmp(value,"null",4))	{ item->type=cJSON_NULL;  return value+4; }
	if (!strncmp(value,"false",5))	{ item->type=cJSON_False; return value+5; }
	if (!strncmp(value,"true",4))	{ item->type=cJSON_True; item->valueint=1;	return value+4; }
	if (*value=='\"')				{ return parse_string(item,value); }
	if (*value=='-' || (*value>='0' && *value<='9'))	{ return parse_number(item,value); }
	if (*value=='[')				{ return parse_array(item,value); }
	if (*value=='{')				{ return parse_object(item,value); }

	ep=value;return 0;	/* failure. */
}

/* Render a value to text. */
static char *print_value(cJSON *item,int depth,int fmt)
{
	char *out=0;
	if (!item) return 0;
	switch ((item->type)&255)
	{
		case cJSON_NULL:	out=cJSON_strdup("null");	break;
		case cJSON_False:	out=cJSON_strdup("false");break;
		case cJSON_True:	out=cJSON_strdup("true"); break;
		case cJSON_Number:	out=print_number(item);break;
		case cJSON_String:	out=print_string(item);break;
		case cJSON_Array:	out=print_array(item,depth,fmt);break;
		case cJSON_Object:	out=print_object(item,depth,fmt);break;
	}
	return out;
}

/* Build an array from input text. */
static const char *parse_array(cJSON *item,const char *value)
{
	cJSON *child;
	if (*value!='[')	{ep=value;return 0;}	/* not an array! */

	item->type=cJSON_Array;
	value=skip(value+1);
	if (*value==']') return value+1;	/* empty array. */

	item->child=child=cJSON_New_Item();
	if (!item->child) return 0;		 /* memory fail */
	value=skip(parse_value(child,skip(value)));	/* skip any spacing, get the value. */
	if (!value) return 0;

	while (*value==',')
	{
		cJSON *new_item;
		if (!(new_item=cJSON_New_Item())) return 0; 	/* memory fail */
		child->next=new_item;new_item->prev=child;child=new_item;
		value=skip(parse_value(child,skip(value+1)));
		if (!value) return 0;	/* memory fail */
	}

	if (*value==']') return value+1;	/* end of array */
	ep=value;return 0;	/* malformed. */
}

/* Render an array to text */
static char *print_array(cJSON *item,int depth,int fmt)
{
	char **entries;
	char *out=0,*ptr,*ret;int len=5;
	cJSON *child=item->child;
	int numentries=0,i=0,fail=0;
	
	/* How many entries in the array? */
	while (child) numentries++,child=child->next;
	/* Allocate an array to hold the values for each */
	entries=(char**)cJSON_malloc(numentries*sizeof(char*));
	if (!entries) return 0;
	memset(entries,0,numentries*sizeof(char*));
	/* Retrieve all the results: */
	child=item->child;
	while (child && !fail)
	{
		ret=print_value(child,depth+1,fmt);
		entries[i++]=ret;
		if (ret) len+=strlen(ret)+2+(fmt?1:0); else fail=1;
		child=child->next;
	}
	
	/* If we didn't fail, try to malloc the output string */
	if (!fail) out=(char*)cJSON_malloc(len);
	/* If that fails, we fail. */
	if (!out) fail=1;

	/* Handle failure. */
	if (fail)
	{
		for (i=0;i<numentries;i++) if (entries[i]) cJSON_free(entries[i]);
		cJSON_free(entries);
		return 0;
	}
	
	/* Compose the output array. */
	*out='[';
	ptr=out+1;*ptr=0;
	for (i=0;i<numentries;i++)
	{
		strcpy(ptr,entries[i]);ptr+=strlen(entries[i]);
		if (i!=numentries-1) {*ptr++=',';if(fmt)*ptr++=' ';*ptr=0;}
		cJSON_free(entries[i]);
	}
	cJSON_free(entries);
	*ptr++=']';*ptr++=0;
	return out;	
}

/* Build an object from the text. */
static const char *parse_object(cJSON *item,const char *value)
{
	cJSON *child;
	if (*value!='{')	{ep=value;return 0;}	/* not an object! */
	
	item->type=cJSON_Object;
	value=skip(value+1);
	if (*value=='}') return value+1;	/* empty array. */
	
	item->child=child=cJSON_New_Item();
	if (!item->child) return 0;
	value=skip(parse_string(child,skip(value)));
	if (!value) return 0;
	child->string=child->valuestring;child->valuestring=0;
	if (*value!=':') {ep=value;return 0;}	/* fail! */
	value=skip(parse_value(child,skip(value+1)));	/* skip any spacing, get the value. */
	if (!value) return 0;
	
	while (*value==',')
	{
		cJSON *new_item;
		if (!(new_item=cJSON_New_Item()))	return 0; /* memory fail */
		child->next=new_item;new_item->prev=child;child=new_item;
		value=skip(parse_string(child,skip(value+1)));
		if (!value) return 0;
		child->string=child->valuestring;child->valuestring=0;
		if (*value!=':') {ep=value;return 0;}	/* fail! */
		value=skip(parse_value(child,skip(value+1)));	/* skip any spacing, get the value. */
		if (!value) return 0;
	}
	
	if (*value=='}') return value+1;	/* end of array */
	ep=value;return 0;	/* malformed. */
}

/* Render an object to text. */
static char *print_object(cJSON *item,int depth,int fmt)
{
	char **entries=0,**names=0;
	char *out=0,*ptr,*ret,*str;int len=7,i=0,j;
	cJSON *child=item->child;
	int numentries=0,fail=0;
	/* Count the number of entries. */
	while (child) numentries++,child=child->next;
	/* Allocate space for the names and the objects */
	entries=(char**)cJSON_malloc(numentries*sizeof(char*));
	if (!entries) return 0;
	names=(char**)cJSON_malloc(numentries*sizeof(char*));
	if (!names) {cJSON_free(entries);return 0;}
	memset(entries,0,sizeof(char*)*numentries);
	memset(names,0,sizeof(char*)*numentries);

	/* Collect all the results into our arrays: */
	child=item->child;depth++;if (fmt) len+=depth;
	while (child)
	{
		names[i]=str=print_string_ptr(child->string);
		entries[i++]=ret=print_value(child,depth,fmt);
		if (str && ret) len+=strlen(ret)+strlen(str)+2+(fmt?2+depth:0); else fail=1;
		child=child->next;
	}
	
	/* Try to allocate the output string */
	if (!fail) out=(char*)cJSON_malloc(len);
	if (!out) fail=1;

	/* Handle failure */
	if (fail)
	{
		for (i=0;i<numentries;i++) {if (names[i]) cJSON_free(names[i]);if (entries[i]) cJSON_free(entries[i]);}
		cJSON_free(names);cJSON_free(entries);
		return 0;
	}
	
	/* Compose the output: */
	*out='{';ptr=out+1;if (fmt)*ptr++='\n';*ptr=0;
	for (i=0;i<numentries;i++)
	{
		if (fmt) for (j=0;j<depth;j++) *ptr++='\t';
		strcpy(ptr,names[i]);ptr+=strlen(names[i]);
		*ptr++=':';if (fmt) *ptr++='\t';
		strcpy(ptr,entries[i]);ptr+=strlen(entries[i]);
		if (i!=numentries-1) *ptr++=',';
		if (fmt) *ptr++='\n';*ptr=0;
		cJSON_free(names[i]);cJSON_free(entries[i]);
	}
	
	cJSON_free(names);cJSON_free(entries);
	if (fmt) for (i=0;i<depth-1;i++) *ptr++='\t';
	*ptr++='}';*ptr++=0;
	return out;	
}

/* Get Array size/item / object item. */
int    cJSON_GetArraySize(cJSON *array)							{cJSON *c=array->child;int i=0;while(c)i++,c=c->next;return i;}
cJSON *cJSON_GetArrayItem(cJSON *array,int item)				{cJSON *c=array->child;  while (c && item>0) item--,c=c->next; return c;}
cJSON *cJSON_GetObjectItem(cJSON *object,const char *string)	{cJSON *c=object->child; while (c && cJSON_strcasecmp(c->string,string)) c=c->next; return c;}

/* Utility for array list handling. */
static void suffix_object(cJSON *prev,cJSON *item) {prev->next=item;item->prev=prev;}
/* Utility for handling references. */
static cJSON *create_reference(cJSON *item) {cJSON *ref=cJSON_New_Item();if (!ref) return 0;memcpy(ref,item,sizeof(cJSON));ref->string=0;ref->type|=cJSON_IsReference;ref->next=ref->prev=0;return ref;}

/* Add item to array/object. */
void   cJSON_AddItemToArray(cJSON *array, cJSON *item)						{cJSON *c=array->child;if (!item) return; if (!c) {array->child=item;} else {while (c && c->next) c=c->next; suffix_object(c,item);}}
void   cJSON_AddItemToObject(cJSON *object,const char *string,cJSON *item)	{if (!item) return; if (item->string) cJSON_free(item->string);item->string=cJSON_strdup(string);cJSON_AddItemToArray(object,item);}
void	cJSON_AddItemReferenceToArray(cJSON *array, cJSON *item)						{cJSON_AddItemToArray(array,create_reference(item));}
void	cJSON_AddItemReferenceToObject(cJSON *object,const char *string,cJSON *item)	{cJSON_AddItemToObject(object,string,create_reference(item));}

cJSON *cJSON_DetachItemFromArray(cJSON *array,int which)			{cJSON *c=array->child;while (c && which>0) c=c->next,which--;if (!c) return 0;
	if (c->prev) c->prev->next=c->next;if (c->next) c->next->prev=c->prev;if (c==array->child) array->child=c->next;c->prev=c->next=0;return c;}
void   cJSON_DeleteItemFromArray(cJSON *array,int which)			{cJSON_Delete(cJSON_DetachItemFromArray(array,which));}
cJSON *cJSON_DetachItemFromObject(cJSON *object,const char *string) {int i=0;cJSON *c=object->child;while (c && cJSON_strcasecmp(c->string,string)) i++,c=c->next;if (c) return cJSON_DetachItemFromArray(object,i);return 0;}
void   cJSON_DeleteItemFromObject(cJSON *object,const char *string) {cJSON_Delete(cJSON_DetachItemFromObject(object,string));}

/* Replace array/object items with new ones. */
void   cJSON_ReplaceItemInArray(cJSON *array,int which,cJSON *newitem)		{cJSON *c=array->child;while (c && which>0) c=c->next,which--;if (!c) return;
	newitem->next=c->next;newitem->prev=c->prev;if (newitem->next) newitem->next->prev=newitem;
	if (c==array->child) array->child=newitem; else newitem->prev->next=newitem;c->next=c->prev=0;cJSON_Delete(c);}
void   cJSON_ReplaceItemInObject(cJSON *object,const char *string,cJSON *newitem){int i=0;cJSON *c=object->child;while(c && cJSON_strcasecmp(c->string,string))i++,c=c->next;if(c){newitem->string=cJSON_strdup(string);cJSON_ReplaceItemInArray(object,i,newitem);}}

/* Create basic types: */
cJSON *cJSON_CreateNull()						{cJSON *item=cJSON_New_Item();if(item)item->type=cJSON_NULL;return item;}
cJSON *cJSON_CreateTrue()						{cJSON *item=cJSON_New_Item();if(item)item->type=cJSON_True;return item;}
cJSON *cJSON_CreateFalse()						{cJSON *item=cJSON_New_Item();if(item)item->type=cJSON_False;return item;}
cJSON *cJSON_CreateBool(int b)					{cJSON *item=cJSON_New_Item();if(item)item->type=b?cJSON_True:cJSON_False;return item;}
cJSON *cJSON_CreateNumber(double num)			{cJSON *item=cJSON_New_Item();if(item){item->type=cJSON_Number;item->valuedouble=num;item->valueint=(int)num;}return item;}
cJSON *cJSON_CreateString(const char *string)	{cJSON *item=cJSON_New_Item();if(item){item->type=cJSON_String;item->valuestring=cJSON_strdup(string);}return item;}
cJSON *cJSON_CreateArray()						{cJSON *item=cJSON_New_Item();if(item)item->type=cJSON_Array;return item;}
cJSON *cJSON_CreateObject()						{cJSON *item=cJSON_New_Item();if(item)item->type=cJSON_Object;return item;}

/* Create Arrays: */
cJSON *cJSON_CreateIntArray(int *numbers,int count)				{int i;cJSON *n=0,*p=0,*a=cJSON_CreateArray();for(i=0;a && i<count;i++){n=cJSON_CreateNumber(numbers[i]);if(!i)a->child=n;else suffix_object(p,n);p=n;}return a;}
cJSON *cJSON_CreateFloatArray(float *numbers,int count)			{int i;cJSON *n=0,*p=0,*a=cJSON_CreateArray();for(i=0;a && i<count;i++){n=cJSON_CreateNumber(numbers[i]);if(!i)a->child=n;else suffix_object(p,n);p=n;}return a;}
cJSON *cJSON_CreateDoubleArray(double *numbers,int count)		{int i;cJSON *n=0,*p=0,*a=cJSON_CreateArray();for(i=0;a && i<count;i++){n=cJSON_CreateNumber(numbers[i]);if(!i)a->child=n;else suffix_object(p,n);p=n;}return a;}
cJSON *cJSON_CreateStringArray(const char **strings,int count)	{int i;cJSON *n=0,*p=0,*a=cJSON_CreateArray();for(i=0;a && i<count;i++){n=cJSON_CreateString(strings[i]);if(!i)a->child=n;else suffix_object(p,n);p=n;}return a;}



// Helper function- KDF
char* cJSON_GetObjectString(cJSON *object,const char *string) { return cJSON_GetObjectItem(object,string)->valuestring; }

#@!
SFileBegin	name SocketComm.c
#include "platform.h"

#include <stdio.h>
#include <assert.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>

#ifdef MS_WINDOWS

#include <winsock2.h>
#include <windows.h>
#include "sys/timeb.h"
#define WINSOCK

#else /* MS_WINDOWS */

#include <unistd.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <netdb.h>
#endif /* MS_WINDOWS */

#ifdef WINSOCK

#define sockerrno           WSAGetLastError()
#define sockstrerror(e)		""
#define WOULDBLOCK          WSAEWOULDBLOCK
#define INPROGRESS          WSAEINPROGRESS
#define NOTCONN             WSAENOTCONN
#define BLOCKERROR			WSAEWOULDBLOCK

#else /* WINSOCK */

#define SOCKET_ERROR		-1
#define INVALID_SOCKET      -1
#define sockerrno           errno
#define sockstrerror(e)		strerror(e)
#define WOULDBLOCK          EWOULDBLOCK
#define INPROGRESS          EINPROGRESS
#define NOTCONN             ENOTCONN
#define BLOCKERROR			EINPROGRESS

#endif /* WINSOCK */

typedef struct Ptr2Socket {
	char*	name;
	char*	host;
	int		sock;
	int		flags;
	int		nonblocking;
	int		timeout;
	int		connected;
} Ptr2Socket, *mySocketptr;

static int connect_blocking = 0;
static int connect_timeout = 0;
static int socket_messages = 1;

typedef struct myPort {
	struct myPort* next;
	int port;
	int numaccepts;
	int socket;
} myPort;

static myPort* activePorts;
static myPort* freePorts;

static myPort* myPortNew(void);
static void myPortFree(myPort*);
static void bindport(int, int);
static void unbindport(int);
static int boundport(int);
static int validport(int);
static Ptr2Socket* mySocketNew(int, char*);
static int myOpenSocket(void);
static void myCloseSocket(int, int);
static void mySetSocketNonBlocking(int, int);
static Ptr2Socket* connect_port(char*, int);
static int myValidSocket(int);
int sock_SetMessages(int);
int sock_SetConnectBlocking(int);
int sock_SetConnectTimeOut(int);
Ptr2Socket* sock_ConnectService(char*, char*);
Ptr2Socket* sock_ConnectPort(char*, int);
int sock_CloseSocket(Ptr2Socket*);
int sock_IsValid(Ptr2Socket*);
int sock_Send(Ptr2Socket*, char*, int);
int sock_SendString(Ptr2Socket*, char*);
int sock_Read(Ptr2Socket*, char*, int);
char* sock_ReadString(Ptr2Socket*);
int sock_GetNonBlocking(Ptr2Socket*);
int sock_SetNonBlocking(Ptr2Socket*, int);
int sock_GetTimeOut(Ptr2Socket*);
int sock_SetTimeOut(Ptr2Socket*, int);
int sock_GetBufferSize(Ptr2Socket*);
int sock_SetBufferSize(Ptr2Socket*, int);
char* sock_GetName(Ptr2Socket*);
char* sock_GetHost(Ptr2Socket*);
int sock_GetNum(Ptr2Socket*);
char* sock_MachineName(void);

// Start of code

static myPort*
myPortNew()
{
	myPort* aPort = freePorts;
	
	if (aPort) {
		freePorts = freePorts->next;
	} else {
		aPort = (myPort*)xmalloc(sizeof(myPort));
	}
	aPort->port = -1;
	aPort->numaccepts = 1;
	aPort->socket = INVALID_SOCKET;
	aPort->next = activePorts;
	activePorts = aPort;
	return aPort;
}

static void
myPortFree(myPort* aPort)
{
	aPort->next = freePorts;
	freePorts = aPort;
}

static void
bindport(int port, int socket)
{
	myPort* aPort;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			aPort->socket = socket;
			return;
		}
	}
	aPort = myPortNew();
	aPort->port = port;
	aPort->numaccepts = 1;
	aPort->socket = socket;
}

static void
unbindport(int port)
{
	myPort* aPort;
	myPort* prev;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			if (prev) {
				prev->next = aPort->next;
			} else {
				activePorts = aPort->next;
			}
			myPortFree(aPort);
			return;
		}
		prev = aPort;
	}
}

static int
boundport(int port)
{
	myPort* aPort;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			return aPort->socket != INVALID_SOCKET ? aPort->socket : 0;
		}
	}
	return 0;
}

static int
validport(int port)
{
	myPort* aPort;
	
	for (aPort = activePorts; aPort != NULL; aPort = aPort->next) {
		if (aPort->port == port) {
			return aPort->numaccepts != -1;
		}
	}
	return 0;
}

static Ptr2Socket*
mySocketNew(int sock, char* host)
{
	static char tmp[512];
	Ptr2Socket* theSocket = (Ptr2Socket*)xmalloc(sizeof(struct Ptr2Socket));
	
	sprintf(tmp, "Socket %d to %s", sock, host);
	theSocket->name = strdup(tmp);
	theSocket->host = strdup(host);
	theSocket->sock = sock;
	theSocket->flags = 1;
	theSocket->nonblocking = 0;
	theSocket->timeout = 0;
	theSocket->connected = 0;
	return theSocket;
}

static int
myOpenSocket(void)
{
	return socket(AF_INET, SOCK_STREAM, 0);
}

static void
myCloseSocket(int sock, int connected)
{
	if (sock == INVALID_SOCKET)
		return;

		/* Make the socket blocking */
	mySetSocketNonBlocking(sock, 0);
	
	if (connected) {
		shutdown(sock, 2);
	}
#ifdef WINSOCK
	if (closesocket(sock) == 0)
#else /* WINSOCK */
	if (close(sock) == 0)
#endif /* WINSOCK */
	{
		return;
	}

	switch(sockerrno) {
	case BLOCKERROR:
		message("Socket close would block");
		break;
	default:
		message("Socket close unknown error: errno %d", sockerrno);
		break;
	}
}

static void
mySetSocketNonBlocking(int sock, int flag)
{
	unsigned long nonblock = flag;
#ifndef WINSOCK
	int ret = ioctl(sock, FIONBIO, &nonblock);
#else /* WINSOCK */
	int ret = ioctlsocket(sock, FIONBIO, &nonblock);
#endif /* WINSOCK */

	if (ret < 0) {
		message("ioctl() error %d: %s.\n", sockerrno, sockstrerror(sockerrno));
	}
}

static Ptr2Socket*
connect_port(char *serverhost, int port)
{
	struct hostent *hp;
	struct sockaddr_in saddr;
	char connecthost[256] = "UNKNOWN";
	int ret;
	int sock;
	int nfound = 0;
	int numtimes = 0;
	int alreadyopened = FALSE;

	checknetstart();

	if (!connect_blocking && connect_timeout) {
		if (socket_messages) {
			message("Trying for %d seconds", connect_timeout);
		}
	}

	while (nfound <= 0) {
		if (alreadyopened == FALSE) {
			hp = gethostbyname(serverhost);

			if (hp == NULL) {
				message("Error: ConnectSocket could not find hostname %s",
					serverhost);
				return NULL;
			}

			memset((char *)&saddr, (int)0, sizeof(saddr));
			memcpy((char *)&saddr.sin_addr, hp->h_addr, hp->h_length);
			saddr.sin_family = hp->h_addrtype;
			saddr.sin_port = port;

			sock = myOpenSocket();
			
			if (sock == INVALID_SOCKET) {
				message("Error: ConnectSocket couldn't make socket");
				return NULL;
			}

			/* Make the socket Non-Blocking */
			mySetSocketNonBlocking(sock, 1);
			{
				int setret;
				struct linger lg;
				int on = 1;

				lg.l_onoff = 1;
				lg.l_linger = 5;

				setret = setsockopt(sock, SOL_SOCKET, SO_LINGER, (char*)&lg, sizeof(lg));
				if (setret != 0) {
					message("Error: ConnectSocket setsockopt error");
				}
				setret = setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, (char*)&on, sizeof(on));
				if (setret != 0) {
					message("Error: ConnectSocket setsockopt error");
				}
			}

			ret = connect(sock, (struct sockaddr*)&saddr, sizeof(saddr));

			if (ret >= 0) {
				nfound = 1;
				alreadyopened = TRUE;
			} else if (ret < 0 && (sockerrno == WOULDBLOCK || sockerrno == INPROGRESS)) {
				alreadyopened = TRUE;
			} else {
				message("Error: ConnectSocket connect error");
				myCloseSocket(sock, 0);
				return NULL;
			}
		} else {
			struct sockaddr_in getname;
			int getnamelen = sizeof(getname);
			struct hostent *hp;
			fd_set writefds;
			fd_set exceptfds;
			struct timeval tv;

			tv.tv_sec = 0;
			tv.tv_usec = 0;

			FD_ZERO(&writefds);
			FD_SET(sock, &writefds);

			FD_ZERO(&exceptfds);
			FD_SET(sock, &exceptfds);

#ifdef WINSOCK
			MilliSleep(100);
#endif /* WINSOCK */
			ret = select(sock + 1, 0, &writefds, &exceptfds, &tv);

			if (ret > 0) {
				if (FD_ISSET(sock, &writefds)) {
					nfound = 1;
				}
			} else if (ret == 0) {
				/* timeout */
				if (!connect_blocking) {
					numtimes++;
					if (numtimes > connect_timeout * 10) {
						if (socket_messages) {
							message("Warning: ConnectSocket timed out");
						}
						if (alreadyopened) {
							myCloseSocket(sock, 0);
						}
						alreadyopened = FALSE;
						sock = INVALID_SOCKET;
						nfound = 1;
						break;
					}
				}
#ifndef WINSOCK
				MilliSleep(100);
#endif /* WINSOCK */
				nfound = 0;
				continue;
			} else {
				message("Error: ConnectSocket select error %d", sockerrno);
				if (alreadyopened) {
					myCloseSocket(sock, 0);
				}
				alreadyopened = FALSE;
				return NULL;
			}


			/* Now check to make sure it is indeed up */
			ret = getpeername(sock, (struct sockaddr*)&getname, &getnamelen);

			if (ret < 0 && sockerrno == NOTCONN) {
				if (!connect_blocking) {
					numtimes++;
					if (numtimes > connect_timeout * 10) {
						if (socket_messages) {
							message("Warning: ConnectSocket timed out");
						}
						if (alreadyopened) {
							myCloseSocket(sock, 0);
						}
						alreadyopened = FALSE;
						sock = INVALID_SOCKET;
						nfound = 1;
						break;
					}
				}
				if (alreadyopened) {
					myCloseSocket(sock, 0);
				}
				alreadyopened = FALSE;
				sock = INVALID_SOCKET;
				MilliSleep(100);
				nfound = 0;
				continue;
			} else if (ret < 0) {
				message("Error: ConnectSocket getpeername error");
				if (alreadyopened) {
					myCloseSocket(sock, 0);
				}
				alreadyopened = FALSE;
				return NULL;
			}

			hp = gethostbyaddr((char*)&getname.sin_addr, sizeof(struct in_addr), getname.sin_family);
			if (hp == NULL) {
				message("Error: ConnectSocket gethostbyaddr error");
			} else {
				strcpy(connecthost, hp->h_name);
			}
		}
	}

	if (nfound > 0 && sock >= 0) {
		Ptr2Socket* aSocket = mySocketNew(sock, connecthost);

		aSocket->connected = 1;
		return aSocket;	
	} else {
		return NULL;
	}
}

int
sock_SetMessages(int on)
{
	socket_messages = on;
	return on;
}

int
sock_SetConnectBlocking(int flag)
{
	connect_blocking = flag;
	return 1;
}

int
sock_SetConnectTimeOut(int timeout)
{
	connect_timeout = timeout;
	return 1;
}

Ptr2Socket*
sock_ConnectService(char* serverhost, char* service)
{
	struct servent* sp = getservbyname(service, "tcp");

	if (sp == NULL) {
		message("Error: sock_ConnectService couldn't find service: %s, %s",
			service, "with tcp protocol");
		return NULL;
	}

	if (socket_messages) {
		message("%s to connect to '%s' with service '%s'",
			connect_blocking ? "Waiting" : "Trying", serverhost, service);
	}

	return connect_port(serverhost, sp->s_port);
}

Ptr2Socket*
sock_ConnectPort(char* serverhost, int port)
{
	short myport = (short)port;

	if (socket_messages) {
		message("%s to connect to '%s' on port: %d",
			connect_blocking ? "Waiting" : "Trying", serverhost, port);
	}

	port = (int)htons(myport);

	return connect_port(serverhost, port);
}

int
sock_CloseSocket(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		myCloseSocket(theSocket->sock, theSocket->connected);
		theSocket->connected = 0;
		if (socket_messages) {
			message("Closed socket %d", theSocket->sock );
		}
		theSocket->flags = 0;
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_CloseSocket %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

static int
myValidSocket(int sock)
{
	return sock != INVALID_SOCKET;
}

int
sock_IsValid(Ptr2Socket* theSocket)
{
	return theSocket && theSocket->sock != INVALID_SOCKET && theSocket->flags != 0 && theSocket->flags != -1;
}

int
sock_Send(Ptr2Socket* theSocket, char* msg, int length)
{
	if (sock_IsValid(theSocket)) {
		int total = 0;
		int sock = theSocket->sock;
		char* msgptr = msg;
		int ret;

		if (theSocket->nonblocking && theSocket->timeout) {
			struct sockaddr_in getname;
			int getnamelen = sizeof(getname);
			fd_set writefds;
			struct timeval tv;
	
			tv.tv_sec = 1;
			tv.tv_usec = 0;
	
			FD_ZERO(&writefds);
			FD_SET(sock, &writefds);
	
			ret = select(sock + 1, 0, &writefds, 0, &tv);
	
			if (ret > 0) {
				assert(FD_ISSET(sock, &writefds));
			} else {
				message("Error: sock_Send send error, closing socket");
				sock_CloseSocket(theSocket);
				return 0;
			}
	
			/* Now check to make sure it is indeed up */
			ret = getpeername(sock, (struct sockaddr*)&getname, &getnamelen);
	
			if (ret < 0) {
				message("Error: sock_Send send error, closing socket");
				sock_CloseSocket(theSocket);
				return 0;
			}
		}
	
		ret = send(sock, msg, length, 0);
	
		if (ret <= 0) {
			if (sockerrno != WOULDBLOCK) {
				message("Error: sock_Send send error, closing socket");
				sock_CloseSocket(theSocket);
			}
			return 0;
		} else {
			msgptr += ret;
			total += ret;
			length -= ret;
	
			while (length > 0) {
#if 0
				message("Continuing sending for %d of %d bytes", length, total);
#endif /* 0 */
				MilliSleep(10);
				ret = send(sock, msgptr, length, 0);
				if (ret <= 0) {
					if (sockerrno != WOULDBLOCK) {
						message("Error: sock_Send send error, closing socket");
						sock_CloseSocket(theSocket);
						return 0;
					}
					continue;
				}
				msgptr += ret;
				total += ret;
				length -= ret;
			}
		}
	
		return total;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				if (theSocket->flags == 0) {
					message("Error: sock_Send %d %s",
						theSocket->sock, "is not a valid socket or already closed");
				}
				theSocket->flags = -1;
			}
		}
		return 0;
	}
}

int
sock_SendString(Ptr2Socket* theSocket, char* value)
{
	static char* msg = NULL;
	short mylen;
	unsigned short len;
	int total = sizeof(len) + strlen(value);

	if (msg) {
		xfree(msg);
	}
	msg = xmalloc(total + 1);

	mylen = (short)strlen(value);
	len = htons(mylen);

	msg[0] = ((char*)&len)[0];
	msg[1] = ((char*)&len)[1];

	strcpy(msg + sizeof(len), value);

	return sock_Send(theSocket, msg, total);
}

int
sock_Read(Ptr2Socket* theSocket, char* msg, int length)
{
	if (sock_IsValid(theSocket)) {
		int ret;
		int total = 0;
		int sock = theSocket->sock;
		char* msgptr = msg;
	
		if (theSocket->nonblocking && theSocket->timeout) {
			fd_set readfds;
			struct timeval tv;
	
			tv.tv_sec = theSocket->timeout;
			tv.tv_usec = 0;
	
			FD_ZERO(&readfds);
			FD_SET(sock, &readfds);
	
			ret = select(sock + 1, &readfds, 0, 0, &tv);
	
			if (ret > 0) {
				assert(FD_ISSET(sock, &readfds));
			} else if (ret == 0) {
				/* timed out - nothing is readable */
				return 0;
			} else {
				/* failed to select */
				message("Error: sock_Read select error");
				return 0;
			}
		}
	
		ret = recv(sock, msg, length, 0);
	
		if (ret <= 0) {
			if (sockerrno != WOULDBLOCK) {
				message("Error: sock_Read recv error, closing socket");
				sock_CloseSocket(theSocket);
			}
			return 0;
		} else {
			msgptr += ret;
			total += ret;
			length -= ret;
	
			while (length > 0) {
#if 0
				message("Continuing reading for %d of %d bytes", length, total);
#endif /* 0 */
				MilliSleep(10);
				ret = recv(sock, msgptr, length, 0);
				if (ret <= 0) {
					if (sockerrno != WOULDBLOCK) {
						message("Error: sock_Read recv error, closing socket");
						sock_CloseSocket(theSocket);
						return 0;
					}
					continue;
				}
				msgptr += ret;
				total += ret;
				length -= ret;
			}
		}
		return total;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_Read %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

char*
sock_ReadString(Ptr2Socket* theSocket)
{
	static char* value = NULL;
	unsigned short len;
	int ret = sock_Read(theSocket, (char*)&len, sizeof(len));

	if (ret == 0)
		return NULL;

	len = htons(len);

	if (value) {
		xfree(value);
	}
	value = xmalloc(len + 1);

	ret = sock_Read(theSocket, value, len);
	if (ret == 0)
		return NULL;

	value[len] = 0; /* null terminate string */

	return value;
}

int
sock_GetNonBlocking(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		return theSocket->nonblocking;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_GetNonBlocking %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_SetNonBlocking(Ptr2Socket* theSocket, int flag)
{
	if (sock_IsValid(theSocket)) {
		int sock = theSocket->sock;
		
		mySetSocketNonBlocking(sock, flag);
		theSocket->nonblocking = flag;
		if (socket_messages) {
			message("Set socket %d to %sblocking", sock, flag ? "non" : "");
		}
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_SetNonBlocking %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_GetTimeOut(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		return theSocket->timeout;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_GetTimeOut %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_SetTimeOut(Ptr2Socket* theSocket, int seconds)
{
	if (sock_IsValid(theSocket)) {
		int sock = theSocket->sock;
		
		theSocket->timeout = seconds;
		if (socket_messages) {
			message("Set socket %d to timeout in %d seconds", sock, seconds);
		}
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_SetTimeOut %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_GetBufferSize(Ptr2Socket* theSocket)
{
	if (sock_IsValid(theSocket)) {
		int bufsize;
		int sizebufsize;
		int ret = 0;
		int sock = theSocket->sock;
		int setret = getsockopt(sock, SOL_SOCKET, SO_SNDBUF, (char*)&bufsize, &sizebufsize);
		
		if (setret != 0) {
			message("Error: sock_GetBufferSize getsockopt error");
		} else {
			ret = bufsize;
		}
		return ret;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_GetBufferSize %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

int
sock_SetBufferSize(Ptr2Socket* theSocket, int bytes)
{
	if (sock_IsValid(theSocket)) {
		int sock = theSocket->sock;
		int bufsize = bytes;
		int setret = setsockopt(sock, SOL_SOCKET, SO_SNDBUF, (char*)&bufsize, sizeof(bufsize));

		if (setret != 0) {
			message("Error: sock_SetBufferSize setsockopt error");
		}
		if (socket_messages) {
			message("Set socket %d buffer size to %d bytes", sock, bytes);
		}
		return 1;
	} else {
		if (theSocket) {
			if (theSocket->flags == 0) {
				message("Error: sock_SetBufferSize %d %s",
					theSocket->sock, "is not a valid socket or already closed");
			}
			theSocket->flags = -1;
		}
		return 0;
	}
}

char*
sock_GetName(Ptr2Socket* theSocket)
{
	if (theSocket == NULL) {
		return "<null>";
	}
	return theSocket->name;
}

char*
sock_GetHost(Ptr2Socket* theSocket)
{
	if (theSocket == NULL) {
		return "<null>";
	}
	return theSocket->host;
}

int
sock_GetNum(Ptr2Socket* theSocket)
{
	if (theSocket == NULL) {
		return INVALID_SOCKET;
	}
	return theSocket->sock;
}

char*
sock_MachineName()
{
	static char thishost[256];

	gethostname(thishost, 256);
	return thishost;
}

Ptr2Socket*
String2Sock(char* str)
{
	return NULL;	// Can't really make one from a string
}

#@!
SFileBegin	name model.mak
ASIDEFINES			=

MSVCLOC				= "$(VCINSTALLDIR)"
ASIINCLUDES			= -I$(MSVCLOC)\PlatformSDK\include
ASILIBPATHS			= /LIBPATH:$(MSVCLOC)\PlatformSDK\lib
ASILIBS				= Ws2_32.lib

#@!
