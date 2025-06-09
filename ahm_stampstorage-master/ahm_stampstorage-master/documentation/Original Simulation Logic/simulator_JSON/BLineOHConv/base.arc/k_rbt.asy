SYSTYPE Kinematics
UNITS Feet Seconds
SYSDEF limit Infinite timeout 60 Seconds confname Config8 report standard
FLAGS
	System Inherit
ROBOTDEF rstaname Rob1_Home UserId 3
RSTA name Rob1Home
RSTA name Rob1Pounce1
RSTA name Rob1Work1
RSTA name Rob1Clear1
RSTA name Rob1Pounce2
RSTA name Rob1Work2
RSTA name Rob1Clear2
RSTA name Rob1Pounce3
RSTA name Rob1Work3
RSTA name Rob1Clear3
RSTA name Rob2Home
RSTA name Rob2Pounce1
RSTA name Rob2Work1
RSTA name Rob2Clear1
RSTA name Rob2Pounce2
RSTA name Rob2Work2
RSTA name Rob2Clear2
RSTA name Rob2Pounce3
RSTA name Rob2Work3
RSTA name Rob2Clear3
RSTA name Rob3Home
RSTA name Rob3Pounce1
RSTA name Rob3Work1
RSTA name Rob3Clear1
RSTA name Rob3Pounce2
RSTA name Rob3Work2
RSTA name Rob3Clear2
RSTA name Rob3Pounce3
RSTA name Rob3Work3
RSTA name Rob3Clear3
RSTA name Rob4Home
RSTA name Rob4Pounce1
RSTA name Rob4Work1
RSTA name Rob4Clear1
RSTA name Rob4Pounce2
RSTA name Rob4Work2
RSTA name Rob4Clear2
RSTA name Rob4Pounce3
RSTA name Rob4Work3
RSTA name Rob4Clear3
RSTA name Rob5Home
RSTA name Rob5Pounce1
RSTA name Rob5Work1
RSTA name Rob5Clear1
RSTA name Rob5Pounce2
RSTA name Rob5Work2
RSTA name Rob5Clear2
RSTA name Rob5Pounce3
RSTA name Rob5Work3
RSTA name Rob5Clear3
RSTA name Rob6Home
RSTA name Rob6Pounce1
RSTA name Rob6Work1
RSTA name Rob6Clear1
RSTA name Rob6Pounce2
RSTA name Rob6Work2
RSTA name Rob6Clear2
RSTA name Rob6Pounce3
RSTA name Rob6Work3
RSTA name Rob6Clear3
NAMELST name Rbt_start
NAMELST name Rbt_start item Rob1Home item Rob2Home item Rob3Home item Rob4Home item Rob5Home item Rob6Home 
WORKLST name Rob1Home Sequential item Rob1Work1 
PARKLST name Rob1Work2 Sequential item Rob1Clear2 
PARKLST name Rob1Clear2 Sequential item Rob1Home 
WORKLST name Rob2Home Sequential item Rob2Work1 
PARKLST name Rob2Work2 Sequential item Rob2Clear2 
PARKLST name Rob2Clear2 Sequential item Rob2Home 
ROBOTVEHSEG name RobotSeg cap 1 pickup 0 Seconds setdown 0 Seconds
	figcurspeed 100
	figmaxspeed 100
	picpos endx 1

	template Feet
700 17
-1 -1 0 1 1 IRB6000_cell
1
700 21
-1 -1 0 1 1 IRB6000
0  -3  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
2
700 55
-1 -1 0 1 1 Base
1
1000 -360 360 45 0
0  0  0
0 0 0 0
1  1  1
0
0  3  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
5
351 21
-1 -1 0 1 1 cyl2
0  0  1
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.899999976158142 0.300000011920929 0 0
310 5
13 13 0 1 1 trapezoid
0  0  1.25
0	0.00	0.00	0.00
	1.00	1.00	1.00
2 1.75 2 1.75 0.949999988079071 0.5 0
700 39
13 13 0 1 1 torso
1
1000 -45 60 -25 0
0  0  0
0 -90 0 0
1  1  1
0
0.625  0  2.95000004768372
0	0.00	0.00	0.00
	1.00	1.00	1.00
11
351 5
13 13 0 1 1 cyl
0  1  0
0 90 0 0
	1.00	1.00	1.00
0.75 0.5 0 0
310 5
9 9 0 1 1 box
0  1.5  0
0 90 0 0
	1.00	1.00	1.00
0.5 0.5 0.5 0.5 3 0 0
700 1
13 13 0 1 1 counterweight
4
310 21
13 13 0 1 1 arm
-1  0  -0.25
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.5 2 0.5 2 0.5 0 0
310 21
13 13 0 1 1 weight
-2  0  -0.25
0	0.00	0.00	0.00
	1.00	1.00	1.00
2 1 2 1 0.649999976158142 -0.25 0
351 21
13 13 0 1 1 stabilizer
-1.29999995231628  0  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.100000001490116 3 0 0
310 21
13 13 0 1 1 bar
-0.649999976158142  0  3
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.5 1.60000002384186 0.5 1.60000002384186 0.25 0 0
351 5
13 13 0 1 1 CP_cyl
0  -0.5  0
0 90 0 0
	1.00	1.00	1.00
0.75 0.5 0 0
310 21
13 13 0 1 1 body
0  0  -0.75
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.800000011920929 0.649999976158142 0.800000011920929 0.649999976158142 3.79999995231628 0 0
351 21
13 13 0 1 1 hydraulic
0  -0.850000023841858  0.75
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.300000011920929 2.25 0 0
351 21
13 13 0 1 1 CP_hydraulic
0  0.850000023841858  0.75
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.300000011920929 2.25 0 0
700 55
13 13 0 1 1 armjoint
1
1000 -45 60 5 0
0  0  -0.699999988079071
0 -90 180 0
1  1  1
0
0  0  3.84999990463257
0	0.00	0.00	0.00
	1.00	1.00	1.00
2
700 55
13 13 0 1 1 longarm
1
1000 -360 360 0 0
0  0  0.349999994039536
0 0 90 0
1  1  1
0
0  0  -0.349999994039536
0	0.00	0.00	0.00
	1.00	1.00	1.00
6
351 21
13 13 0 1 1 arm
0.5  0  0.349999994039536
0 0 90 0
	1.00	1.00	1.00
0.300000011920929 5.19999980926514 0 0
700 39
9 9 0 1 1 wrist
1
1000 -90 135 70 0
0  0  0
0 0 180 0
1  1  1
0
6.25  0  0.349999994039536
0 90 0 0
	1.00	1.00	1.00
2
310 21
9 9 0 1 1 box
0.100000001490116  0  -0.300000011920929
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.600000023841858 0.800000011920929 0.600000023841858 0.800000011920929 0.600000023841858 0 0
700 55
9 9 0 1 1 endeffector
1
1000 -360 360 0 0
0  0  0
0 0 0 0
1  1  1
0
	0.00	0.00	0.00
0 0 90 0
	1.00	1.00	1.00
2
351 21
9 9 0 1 1 cyl
0  0  0.5
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.300000011920929 0.200000002980232 0 0
700 55
9 9 0 1 1 LoadGoesHere
	0
	0	0	0	0	1
	0.00	0.00	0.00
0	0.00	0.00	0.00
	1.00	1.00	1.00
	0
0  4  0.200000002980232
0 0 180 0
1  1  1
0  0  0.75
0	0.00	0.00	0.00
	1.00	1.00	1.00
0
700 21
13 13 0 1 1 armend
6  0  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
2
351 21
13 13 0 1 1 CP_side
0.200000002980232  -0.324999988079071  0.349999994039536
0 90 0 0
	1.00	1.00	1.00
0.400000005960464 0.200000002980232 0 0
310 21
13 13 0 1 1 box
-0.300000011920929  -0.419999986886978  -0.025000000372529
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.25 0.699999988079071 0.25 0.699999988079071 0.75 0 0
700 21
13 13 0 1 1 CP_armend
6  0  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
2
351 21
13 13 0 1 1 CP_side
0.200000002980232  0.5  0.349999994039536
0 90 0 0
	1.00	1.00	1.00
0.400000005960464 0.200000002980232 0 0
310 21
13 13 0 1 1 box
-0.300000011920929  0.400000005960464  -0.025000000372529
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.25 0.699999988079071 0.25 0.699999988079071 0.75 0 0
140 5
0 0 0 2 1 ABB
5.5  -0.550000011920929  0.200000002980232
0 90 0 0
0.25  0.25  0.25
ABB
140 5
0 0 0 2 1 CP_ABB
1.5  0.300000011920929  0.200000002980232
0 90 180 0
0.25  0.25  0.25
ABB
700 21
13 13 0 1 1 backarm
0  0  -0.349999994039536
0	0.00	0.00	0.00
	1.00	1.00	1.00
4
310 21
13 13 0 1 1 CP_box
0  0  -0.5
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.5 0.5 0.5 0.699999988079071 0.5 0 0
350 21
13 13 0 1 1 CP_cyl
-0.5  0  0.349999994039536
0 90 90 0
	1.00	1.00	1.00
0.449999988079071 1 0 0
351 5
9 9 0 1 1 CP_controller
-0.5  -0.699999988079071  0.100000001490116
0 0 90 0
	1.00	1.00	1.00
0.200000002980232 0.75 0 0
310 21
13 13 0 1 1 CP_back
-0.75  -0.25  -0.100000001490116
0	0.00	0.00	0.00
	1.00	1.00	1.00
1.39999997615814 0.5 1.39999997615814 0.5 1 0 0
350 21
13 13 0 1 1 cyl2
0  -0.25  3.09999990463257
0 90 0 0
	1.00	1.00	1.00
0.100000001490116 0.699999988079071 0 0
350 21
13 13 0 1 1 CP_cyl2
0  0.949999988079071  3.09999990463257
0 90 0 0
	1.00	1.00	1.00
0.100000001490116 0.699999988079071 0 0
140 5
0 0 0 2 1 ABB
1  -0.300000011920929  2.25
0 0 90 90
0.25  0.25  0.25
ABB
310 5
13 13 0 1 1 trap2
-0.25  0.75  1.64999997615814
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.5 2 0.5 1.49999998509884 1.5 0.899999983608723 0
310 5
13 13 0 1 1 CP_trap2
-0.25  -0.75  1.64999997615814
0	0.00	0.00	0.00
	1.00	1.00	1.00
0.5 2 0.5 1.49999998509884 1.5 0.899999983608723 0
700 21
-1 -1 0 1 1 fixed
0  3  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
2
351 17
-1 -1 0 1 1 cyl
1.39999997615814 1 0 0
310 21
-1 -1 0 1 1 square
-1  0  0
0	0.00	0.00	0.00
	1.00	1.00	1.00
2.75 2 2.75 2 0.75 0 0
end
	numjnt 6
	config Rob1Config1 45 -25 5 0 70 0
	config Rob1Config2 0 -25 5 0 70 0
	config Rob1Config3 0 30 -15 0 70 -90
	config Rob1Config4 30 -25 5 0 70 0
	config Rob1Config5 85 -35 20 0 70 0
	config Rob1Config6 85 1 20 86 92 -65
	config Rob1Config7 80 0 10 0 70 0
	config Rob1Config8 0 0 0 0 0 0
	config Rob1Config9 0 0 0 0 0 0
	config Rob1Config10 0 0 0 0 0 0
	config Rob2Config1 -45 -25 5 0 70 0
	config Rob2Config2 0 -25 5 0 70 0
	config Rob2Config3 0 20 -5 0 70 90
	config Rob2Config4 -30 -25 5 0 70 0
	config Rob2Config5 -85 -35 5 0 70 0
	config Rob2Config6 -85 1 20 -86 92 65
	config Rob2Config7 -75 0 10 0 70 0
	config Rob2Config8 0 0 0 0 0 0
	config Rob2Config9 0 0 0 0 0 0
	config Rob2Config10 0 0 0 0 0 0
	config Rob3Config1 45 -25 5 0 70 0
	config Rob3Config2 -70 -35 20 0 70 0
	config Rob3Config3 -71 5 20 86 65 -65
	config Rob3Config4 -80 -25 10 0 70 0
	config Rob3Config5 0 -25 5 0 70 0
	config Rob3Config6 0 30 -15 0 70 -90
	config Rob3Config7 30 -25 5 0 70 0
	config Rob3Config8 0 0 0 0 0 0
	config Rob3Config9 0 0 0 0 0 0
	config Rob3Config10 0 0 0 0 0 0
	config Rob4Config1 -45 -25 5 0 70 0
	config Rob4Config2 80 -35 5 0 70 0
	config Rob4Config3 85 2 20 -86 90 65
	config Rob4Config4 95 -25 10 0 70 0
	config Rob4Config5 0 -25 5 0 70 0
	config Rob4Config6 0 20 -5 0 70 90
	config Rob4Config7 30 -25 5 0 70 0
	config Rob4Config8 0 0 0 0 0 0
	config Rob4Config9 0 0 0 0 0 0
	config Rob4Config10 0 0 0 0 0 0
	config Rob5Config1 45 -25 5 0 70 0
	config Rob5Config2 -70 -35 20 0 70 0
	config Rob5Config3 -71 5 20 86 65 -65
	config Rob5Config4 -80 -25 10 0 70 0
	config Rob5Config5 0 -25 5 0 70 0
	config Rob5Config6 0 30 -15 0 70 -90
	config Rob5Config7 30 -25 5 0 70 0
	config Rob5Config8 0 0 0 0 0 0
	config Rob5Config9 0 0 0 0 0 0
	config Rob5Config10 0 0 0 0 0 0
	config Rob6Config1 -45 -25 5 0 70 0
	config Rob6Config2 80 -35 5 0 70 0
	config Rob6Config3 85 2 20 -86 90 65
	config Rob6Config4 95 -25 10 0 70 0
	config Rob6Config5 0 -25 5 0 70 0
	config Rob6Config6 0 20 -5 0 70 90
	config Rob6Config7 30 -25 5 0 70 0
	config Rob6Config8 0 0 0 0 0 0
	config Rob6Config9 0 0 0 0 0 0
	config Rob6Config10 0 0 0 0 0 0
	deffig Rob1Config1
	path 2 bidirect 0 Rob1Config1 2 Rob1Config3 0
	path 2 bidirect 0 Rob1Config3 1.5 Rob1Config5 0
	path 2 bidirect 0 Rob1Config5 2 Rob1Config6 0
	path 2 bidirect 0 Rob1Config6 1 Rob1Config7 0
	path 2 bidirect 0 Rob1Config7 1.7 Rob1Config1 0
	path 2 bidirect 0 Rob2Config1 2 Rob2Config3 0
	path 2 bidirect 0 Rob2Config3 1.5 Rob2Config5 0
	path 2 bidirect 0 Rob2Config5 2 Rob2Config6 0
	path 2 bidirect 0 Rob2Config6 1 Rob2Config7 0
	path 2 bidirect 0 Rob2Config7 1.7 Rob2Config1 0
	path 2 bidirect 0 Rob3Config1 1 Rob3Config2 0
	path 2 bidirect 0 Rob3Config2 1 Rob3Config3 0
	path 2 bidirect 0 Rob3Config3 1 Rob3Config4 0
	path 2 bidirect 0 Rob3Config4 1 Rob3Config5 0
	path 2 bidirect 0 Rob3Config5 1 Rob3Config6 0
	path 2 bidirect 0 Rob3Config6 1 Rob3Config7 0
	path 2 bidirect 0 Rob3Config7 1.2 Rob3Config1 0
	path 2 bidirect 0 Rob4Config1 1 Rob4Config2 0
	path 2 bidirect 0 Rob4Config2 1 Rob4Config3 0
	path 2 bidirect 0 Rob4Config3 1 Rob4Config4 0
	path 2 bidirect 0 Rob4Config4 1 Rob4Config5 0
	path 2 bidirect 0 Rob4Config5 1 Rob4Config6 0
	path 2 bidirect 0 Rob4Config6 1 Rob4Config7 0
	path 2 bidirect 0 Rob4Config7 1.2 Rob4Config1 0
	path 2 bidirect 0 Rob5Config1 1 Rob5Config2 0
	path 2 bidirect 0 Rob5Config2 1 Rob5Config3 0
	path 2 bidirect 0 Rob5Config3 1 Rob5Config4 0
	path 2 bidirect 0 Rob5Config4 1 Rob5Config5 0
	path 2 bidirect 0 Rob5Config5 1 Rob5Config6 0
	path 2 bidirect 0 Rob5Config6 1 Rob5Config7 0
	path 2 bidirect 0 Rob5Config7 1.2 Rob5Config1 0
	path 2 bidirect 0 Rob6Config1 1 Rob6Config2 0
	path 2 bidirect 0 Rob6Config2 1 Rob6Config3 0
	path 2 bidirect 0 Rob6Config3 1 Rob6Config4 0
	path 2 bidirect 0 Rob6Config4 1 Rob6Config5 0
	path 2 bidirect 0 Rob6Config5 1 Rob6Config6 0
	path 2 bidirect 0 Rob6Config6 1 Rob6Config7 0
	path 2 bidirect 0 Rob6Config7 1.2 Rob6Config1 0
	CONTAINER name LoadGoesHere 0 
		x 1 1.0 Feet 
		y 1 1.0 Feet 
		z 1 1.0 Feet
		relative 1 1 1 1 3 8 1 2 2 2 
		trx 0 try 0 trz 0
		rotx 0 roty 0 rotz 0
ROBOTVEH type Robot numveh 6
	vehsegs item RobotSeg 
	start Rbt_start
	Stacking OTT_LDDISP	
	dis 0 picpos begx -140.5 begy -64.5 begz 3 endx -139.5 endy -64.5 endz 3

	dis 1 picpos begx -116.5 begy -64.5 begz 3 endx -117.5 endy -64.5 endz 3

	dis 2 picpos begx -326.5 begy -61.75 endx -326.5 endy -62.75

	dis 3 picpos begx -326.5 begy -95.5 endx -326.5 endy -94.5

	dis 4 picpos begx -367.75 begy -258.75 endx -367.75 endy -259.75

	dis 5 picpos begx -367.75 begy -288 endx -367.75 endy -287

conlink Rob1Home Rob1Config1 RobotSeg
conlink Rob1Pounce1 Rob1Config2 RobotSeg
conlink Rob1Work1 Rob1Config3 RobotSeg
conlink Rob1Clear1 Rob1Config4 RobotSeg
conlink Rob1Pounce2 Rob1Config5 RobotSeg
conlink Rob1Work2 Rob1Config6 RobotSeg
conlink Rob1Clear2 Rob1Config7 RobotSeg
conlink Rob1Pounce3 Rob1Config8 RobotSeg
conlink Rob1Work3 Rob1Config9 RobotSeg
conlink Rob1Clear3 Rob1Config10 RobotSeg
conlink Rob2Home Rob2Config1 RobotSeg
conlink Rob2Pounce1 Rob2Config2 RobotSeg
conlink Rob2Work1 Rob2Config3 RobotSeg
conlink Rob2Clear1 Rob2Config4 RobotSeg
conlink Rob2Pounce2 Rob2Config5 RobotSeg
conlink Rob2Work2 Rob2Config6 RobotSeg
conlink Rob2Clear2 Rob2Config7 RobotSeg
conlink Rob2Pounce3 Rob2Config8 RobotSeg
conlink Rob2Work3 Rob2Config9 RobotSeg
conlink Rob2Clear3 Rob2Config10 RobotSeg
conlink Rob3Home Rob3Config1 RobotSeg
conlink Rob3Pounce1 Rob3Config2 RobotSeg
conlink Rob3Work1 Rob3Config3 RobotSeg
conlink Rob3Clear1 Rob3Config4 RobotSeg
conlink Rob3Pounce2 Rob3Config5 RobotSeg
conlink Rob3Work2 Rob3Config6 RobotSeg
conlink Rob3Clear2 Rob3Config7 RobotSeg
conlink Rob3Pounce3 Rob3Config8 RobotSeg
conlink Rob3Work3 Rob3Config9 RobotSeg
conlink Rob3Clear3 Rob3Config10 RobotSeg
conlink Rob4Home Rob4Config1 RobotSeg
conlink Rob4Pounce1 Rob4Config2 RobotSeg
conlink Rob4Work1 Rob4Config3 RobotSeg
conlink Rob4Clear1 Rob4Config4 RobotSeg
conlink Rob4Pounce2 Rob4Config5 RobotSeg
conlink Rob4Work2 Rob4Config6 RobotSeg
conlink Rob4Clear2 Rob4Config7 RobotSeg
conlink Rob4Pounce3 Rob4Config8 RobotSeg
conlink Rob4Work3 Rob4Config9 RobotSeg
conlink Rob4Clear3 Rob4Config10 RobotSeg
conlink Rob5Home Rob5Config1 RobotSeg
conlink Rob5Pounce1 Rob5Config2 RobotSeg
conlink Rob5Work1 Rob5Config3 RobotSeg
conlink Rob5Clear1 Rob5Config4 RobotSeg
conlink Rob5Pounce2 Rob5Config5 RobotSeg
conlink Rob5Work2 Rob5Config6 RobotSeg
conlink Rob5Clear2 Rob5Config7 RobotSeg
conlink Rob5Pounce3 Rob5Config8 RobotSeg
conlink Rob5Work3 Rob5Config9 RobotSeg
conlink Rob5Clear3 Rob5Config10 RobotSeg
conlink Rob6Home Rob6Config1 RobotSeg
conlink Rob6Pounce1 Rob6Config2 RobotSeg
conlink Rob6Work1 Rob6Config3 RobotSeg
conlink Rob6Clear1 Rob6Config4 RobotSeg
conlink Rob6Pounce2 Rob6Config5 RobotSeg
conlink Rob6Work2 Rob6Config6 RobotSeg
conlink Rob6Clear2 Rob6Config7 RobotSeg
conlink Rob6Pounce3 Rob6Config8 RobotSeg
conlink Rob6Work3 Rob6Config9 RobotSeg
conlink Rob6Clear3 Rob6Config10 RobotSeg