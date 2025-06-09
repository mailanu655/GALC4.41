SYSTYPE Kinematics
UNITS Feet Seconds
SYSDEF limit Infinite timeout 60 Seconds confname Config3
FLAGS
	System Inherit
ROBOTDEF rstaname staUp UserId 11
RSTA name staDown
RSTA name staUp
NAMELST name start
NAMELST name start item staDown 
ROBOTVEHSEG name DefSeg cap 1 pickup 0 Seconds setdown 0 Seconds
	figcurspeed 100
	figmaxspeed 100
	picpos endx 1

	template Feet
700 1
1 1 0 1 1 lift_cel
1
700 17
1 1 0 1 1 Lift
2
700 17
1 1 0 1 1 Column
1
350 1
8 8 0 1 1 CenterColumn
0.25 35 0 0
700 17
1 1 0 1 1 Z_Translate
1
700 51
1 1 0 1 1 Z_Translate
2
10 5 35 15.77 0
0  0  0
0 0 0 0
1  1  1
0
2
700 51
1 1 0 1 1 Y_Translate
1
10 0 0 0 0
0  0  0
0 0 0 0
1  1  1
0
2
700 51
1 1 0 1 1 X_Translate
2
10 0 0 0 0
0  0  0
0 0 0 0
1  1  1
0
4
310 5
9 9 0 1 1 Rib
0  -1.20000004768372  0.200000002980232
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.25 5 0.25 5 0.25 0 0
700 55
1 1 0 1 1 TCF
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  -4  1.45000004768372
0 0 0 0
1  1  1
-1.375  -1.20000004768372  -0.774999976158142
0 90 90 0
0.100000001490116  0.100000001490116  0.100000001490116
1
310 21
1 1 0 1 1 box
	0.00	0.00	0.00
0	0.00	0.00	0.00
0.100000001490116  0.100000001490116  0.100000001490116
1 1 1 1 1 0 0
310 1
9 9 0 1 1 X_Axis
0 14.8000001907349 0 14.8000001907349 0 0 0
310 1
9 9 0 1 1 Y_Axis
6 0 6 0 0 0 0
310 5
9 9 0 1 1 Arm1
0  -0.5  0.200000002980232
0	0.00	0.00	0.00
	1.00	1.00	1.00
1.25 0.400000005960464 1.5 0.5 0.25 0 0
310 1
9 9 0 1 1 Slide
0.5 0.5 0.5 0.5 0.5 0 0
end
	numjnt 3
	config ConfigUp 32.2 0 0
	config ConfigDown 15.77 0 0
	deffig ConfigDown
	path 2 bidirect 0 ConfigUp 1.64 ConfigDown 0
	path 2 bidirect 0 ConfigDown 1.64 ConfigUp 0
	CONTAINER name Container1 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 2 1 1 1 2 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
ROBOTVEH type kLift numveh 1
	vehsegs item DefSeg 
	start start
	Stacking OTT_LDDISP	
	dis 0 picpos begx 184.681981627297 begy -158.2 endx 185.681981627297 endy -158.2

conlink staDown ConfigDown DefSeg
conlink staUp ConfigUp DefSeg