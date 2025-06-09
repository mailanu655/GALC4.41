SYSTYPE Conveyor
UNITS Feet Seconds
SYSDEF timeout 60 Seconds confname Config1 report standard
FLAGS
	System Inherit
	Text Invisible Red
	Sections GTemplate Orange
	Section Names Red
	Direction Orange
	Fixed Interval Orange
	Loads Inherit
	Stations Blue
	Station Names Red
	Transfers Brown
	Photoeyes LtBlue
	Photoeye Names Inherit
	Center Line Invisible Inherit
CONVDEF UserId 3
	NEXTSEC name sec4 type DefaultSection
	NEXTSTA name sta11 type DefaultStation
	NEXTTRAN name tran1 type DefaultTransfer
	NEXTMOTOR name motor1 type DefaultMotor
	NEXTPHOTOEYE name photoeye1 type DefaultPhotoeye
	ALTERNATE NONE EXTRASECTIONWIDTH 0 Feet
CONVTOL minang 450 maxang 1350
CONVMOTORTYPE name DefaultMotor
CONVMOTOR name M_sec1 type DefaultMotor
CONVSECTIONTYPE name DefaultSection width 5 Feet motor M_sec1 vel 3 Feet Seconds acc 1 Feet Seconds Seconds dec 1 Feet Seconds Seconds accum load stopsize 1 2 Feet movesize 1 2 Feet inductsize 1 0 Feet nofixed align centered_in_interval attach rigid nav 1
CONVSECTION name sec1 type DefaultSection motor M_sec1 piece begx -133.026123046875 begy -124.361763000488 begz 3 endx -133.026123046875 endy -30.0790271759033 endz 3 upz 1
CONVSECTION name sec2 type DefaultSection motor M_sec1 piece begx -124.22612 begy -124.361763000488 begz 3 endx -124.226119995117 endy -30.0790271759033 endz 3 upz 1
CONVSECTION name sec3 type DefaultSection vel 9.396266 Feet Seconds piece begx 231 begy -184.214 begz 3 endx 231 endy -138 endz 3 upz 1
CONVSECTION name sec4 type DefaultSection vel 9.396266 Feet Seconds piece begx 239.5 begy -184.214 begz 3 endx 239.5 endy -138 endz 3 upz 1
CONVSTATIONTYPE name DefaultStation raise 0 Seconds lower 0 Seconds dist 0 Feet release norestriction align origin cap Infinite scale 1 color -1 nrot 0 nscale 1
CONVSTATION name sta1 type DefaultStation at sec1 3.04792785644531
CONVSTATION name sta2 type DefaultStation at sec1 59.9426879882813
CONVSTATION name sta3 type DefaultStation at sec1 90.6252174377441
CONVSTATION name sta4 type DefaultStation at sec2 3.45432281494141
CONVSTATION name sta5 type DefaultStation at sec2 59.9426879882813
CONVSTATION name sta6 type DefaultStation at sec2 90.8284111022949
CONVSTATION name sta7 type DefaultStation at sec3 3.335013
CONVSTATION name sta8 type DefaultStation at sec3 41.55
CONVSTATION name sta9 type DefaultStation at sec4 3.3330163496357
CONVSTATION name sta10 type DefaultStation at sec4 41.4632744364228
CONVPHOTOEYETYPE name DefaultPhotoeye blocktimeout 5 Seconds cleartimeout 5 Seconds
CONVTRANSFERTYPE name DefaultTransfer inductsize 1 0 Feet aheadinductsize 1 0 Feet speedadjust Both starttime 0 Seconds finishtime 0 Seconds style double movemethod movesection
