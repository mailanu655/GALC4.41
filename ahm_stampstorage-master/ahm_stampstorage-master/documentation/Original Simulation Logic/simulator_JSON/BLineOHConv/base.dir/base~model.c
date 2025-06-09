// base~model.c
// AutoMod 12.3.1 Generated File
// Build: 2152.80
// Model name:	base
// Applied/AutoMod Licensee Confidential
// NO DISTRIBUTION OR REPRODUCTION RIGHTS GRANTED!
// Copyright (c) 1988-2010 Applied Materials All rights reserved.
//
// All Rights Reserved.  Reproduction or transmission in whole or
// in part, in any form or by any means, electronic, mechanical or
// otherwise, is prohibited without the prior written consent of
// copyright owner.
//
// Licensed Material - Property of Applied Materials, Inc.
//
// Applied Materials, Inc.
// 3050 Bowers Drive
// P.O. Box 58039
// Santa Clara, CA 95054-3299
// U.S.A.
//


#include "decls.h"

iofile* ffp_base_arc_RESULTS_StoreIn_out;	/* 'base.arc/RESULTS/StoreIn.out */
iofile* ffp_base_arc_RESULTS_StoreOut1_out;	/* 'base.arc/RESULTS/StoreOut1.out */
iofile* ffp_base_arc_RESULTS_StoreOut2_out;	/* 'base.arc/RESULTS/StoreOut2.out */
iofile* ffp_base_arc_DATA_general_dat;	/* 'base.arc/DATA/general.dat */
iofile* ffp_base_arc_DATA_modellist_dat;	/* 'base.arc/DATA/modellist.dat */
iofile* ffp_base_arc_DATA_down_dat;	/* 'base.arc/DATA/down.dat */
iofile* ffp_base_arc_DATA_stpshift_dat;	/* 'base.arc/DATA/stpshift.dat */
iofile* ffp_base_arc_DATA_wldshift_dat;	/* 'base.arc/DATA/wldshift.dat */
iofile* ffp_base_arc_DATA_variations_dat;	/* 'base.arc/DATA/variations.dat */
iofile* ffp_base_arc_RESULTS_out_dat;	/* 'base.arc/RESULTS/out.dat */
iofile* ffp_base_arc_RESULTS_avg_out;	/* 'base.arc/RESULTS/avg.out */
iofile* ffp_base_arc_RESULTS_JSON_out;	/* 'base.arc/RESULTS/JSON.out */
struct model_struct am_model;

char CommandName[] = "C:/AutoMod/12_2_1/bin/amod.exe";
extern ASIWindow WORKMENU;

FILE** imp_stdin;
FILE** imp_stdout;
FILE** imp_stderr;
struct process_system** imp_headpt;
struct AMLoadList* imp_activeloads;
struct model_header** imp_model;
FILE** imp_tfp;
FILE** imp_rfp;
FILE** imp_pfp;
int32* imp_trace;
int32* imp_animate;
int32* imp_Animating;
unsigned long* imp_changeflag;
int32* imp_EnableBeaming;
Time* imp_ASIclock;
Time* imp_beginper;
Boolean* imp_SIMULATOR;
struct AMEventNoticeList** imp_cel;
struct eventlist* imp_fel;
struct dlogItem** imp_grf_sl;
struct s_menu** imp_popmenu;
ASIWindow* imp_WORKMENU;
ASIWindow* imp_SimLotStatusWin;
ASIWindow* imp_SimStnStatusWin;
ASIWindow* imp_SimOperStatusWin;
ASIWindow* imp_SimInvStatusWin;
ASIWindow* imp_SimKanbanStatusWin;

#ifdef __GNUC__
int __attribute__((stdcall)) 
DllEntryPoint(void* hinstDLL, int fdwReason, void* lpvReserved) 
{
	return 1;
}
#endif //__GNUC__


MODELDLLEXPORT
init_globs()
{
	imp_headpt = Getimpheadpt();
	imp_activeloads = Getimpactiveloads();
	imp_model = Getimpmodel();
	imp_tfp = Getimptfp();
	imp_rfp = Getimprfp();
	imp_pfp = Getimppfp();
	imp_trace = Getimptrace();
	imp_animate = Getimpanimate();
	imp_Animating = GetimpAnimating();
	imp_changeflag = Getimpchangeflag();
	imp_EnableBeaming = GetimpEnableBeaming();
	imp_ASIclock = GetimpASIclock();
	imp_beginper = Getimpbeginper();
	imp_SIMULATOR = GetimpSIMULATOR();
	imp_cel = Getimpcel();
	imp_fel = Getimpfel();
	imp_grf_sl = Getimpgrf_sl();
	imp_popmenu = Getimppopmenu();
	imp_WORKMENU = GetimpWORKMENU();
	imp_SimLotStatusWin = GetimpSimLotStatusWin();
	imp_SimStnStatusWin = GetimpSimStnStatusWin();
	imp_SimOperStatusWin = GetimpSimOperStatusWin();
	imp_SimInvStatusWin = GetimpSimInvStatusWin();
	imp_SimKanbanStatusWin = GetimpSimKanbanStatusWin();
	imp_stdin = Getimpstdin();
	imp_stdout = Getimpstdout();
	imp_stderr = Getimpstderr();

	initglobs0();
}

MODELDLLEXPORT load*
newattrib(load* this, load* that)
{
	int i0;

	if (!this->attribute)
		this->attribute = (loadatt*)xmalloc(sizeof(loadatt));
	else {
		if (this->attribute->am_model.am_As_Type)
			xfree(this->attribute->am_model.am_As_Type);
		if (this->attribute->am_model.am_As_NextLocation)
			xfree(this->attribute->am_model.am_As_NextLocation);
		if (this->attribute->am_model.am_As_CurrentLocation)
			xfree(this->attribute->am_model.am_As_CurrentLocation);
		if (this->attribute->am_model.am_As_Message)
			xfree(this->attribute->am_model.am_As_Message);
	}
	if (that != NULL) {
		*(this->attribute) = *(that->attribute);
		if (this->attribute->am_model.am_As_Type) {
			this->attribute->am_model.am_As_Type = strdup(that->attribute->am_model.am_As_Type);
		}
		if (this->attribute->am_model.am_As_NextLocation) {
			this->attribute->am_model.am_As_NextLocation = strdup(that->attribute->am_model.am_As_NextLocation);
		}
		if (this->attribute->am_model.am_As_CurrentLocation) {
			this->attribute->am_model.am_As_CurrentLocation = strdup(that->attribute->am_model.am_As_CurrentLocation);
		}
		if (this->attribute->am_model.am_As_Message) {
			this->attribute->am_model.am_As_Message = strdup(that->attribute->am_model.am_As_Message);
		}
	} else
		memset(this->attribute, 0, sizeof(loadatt));
	return this;
}

MODELDLLEXPORT void*
get_ldatt_address(load* this, int32 index, int32* d)
{
	void* data = NULL;

	switch(index) {
	case 0:
		data = (void*)&this->attribute->am_model.am_Ai_pi;
		break;
	case 1:
		data = (void*)&this->attribute->am_model.am_Ai_CarQty;
		break;
	case 2:
		data = (void*)&this->attribute->am_model.am_As_Type;
		break;
	case 3:
		data = (void*)&this->attribute->am_model.am_Ai_Type;
		break;
	case 4:
		data = (void*)&this->attribute->am_model.am_Ai_Seq;
		break;
	case 5:
		data = (void*)&this->attribute->am_model.am_Ai_LotQty;
		break;
	case 6:
		data = (void*)&this->attribute->am_model.am_At_TimeInSys;
		break;
	case 7:
		data = (void*)&this->attribute->am_model.am_Ai_Status;
		break;
	case 8:
		data = (void*)&this->attribute->am_model.am_Ai_Zone;
		break;
	case 9:
		data = (void*)&this->attribute->am_model.am_Ai_Row;
		break;
	case 10:
		data = (void*)&this->attribute->am_model.am_Ai_Line;
		break;
	case 11:
		data = (void*)&this->attribute->am_model.am_Ai_Side;
		break;
	case 12:
		data = (void*)&this->attribute->am_model.am_Aloc_Destination;
		break;
	case 13:
		data = (void*)&this->attribute->am_model.am_Ai_x;
		break;
	case 14:
		data = (void*)&this->attribute->am_model.am_Ai_StampingPriority;
		break;
	case 15:
		data = (void*)&this->attribute->am_model.am_Ai_NonSPO;
		break;
	case 16:
		data = (void*)&this->attribute->am_model.am_Ai_ID;
		break;
	case 17:
		data = (void*)&this->attribute->am_model.am_Ai_y;
		break;
	case 18:
		data = (void*)&this->attribute->am_model.am_Ai_z;
		break;
	case 19:
		data = (void*)&this->attribute->am_model.am_Ai_Press;
		break;
	case 20:
		data = (void*)&this->attribute->am_model.am_Ai_LotNo;
		break;
	case 21:
		data = (void*)&this->attribute->am_model.am_Ai_Type2;
		break;
	case 22:
		data = (void*)&this->attribute->am_model.am_As_NextLocation;
		break;
	case 23:
		data = (void*)&this->attribute->am_model.am_As_CurrentLocation;
		break;
	case 24:
		data = (void*)&this->attribute->am_model.am_As_Message;
		break;
	}
	return data;
}

MODELDLLEXPORT void
set_attdata(load* this)
{
	attribute* att;

	am_model.am_Ai_pi$att->data = (void*)&this->attribute->am_model.am_Ai_pi;
	am_model.am_Ai_CarQty$att->data = (void*)&this->attribute->am_model.am_Ai_CarQty;
	am_model.am_As_Type$att->data = (void*)&this->attribute->am_model.am_As_Type;
	am_model.am_Ai_Type$att->data = (void*)&this->attribute->am_model.am_Ai_Type;
	am_model.am_Ai_Seq$att->data = (void*)&this->attribute->am_model.am_Ai_Seq;
	am_model.am_Ai_LotQty$att->data = (void*)&this->attribute->am_model.am_Ai_LotQty;
	am_model.am_At_TimeInSys$att->data = (void*)&this->attribute->am_model.am_At_TimeInSys;
	am_model.am_Ai_Status$att->data = (void*)&this->attribute->am_model.am_Ai_Status;
	am_model.am_Ai_Zone$att->data = (void*)&this->attribute->am_model.am_Ai_Zone;
	am_model.am_Ai_Row$att->data = (void*)&this->attribute->am_model.am_Ai_Row;
	am_model.am_Ai_Line$att->data = (void*)&this->attribute->am_model.am_Ai_Line;
	am_model.am_Ai_Side$att->data = (void*)&this->attribute->am_model.am_Ai_Side;
	am_model.am_Aloc_Destination$att->data = (void*)&this->attribute->am_model.am_Aloc_Destination;
	am_model.am_Ai_x$att->data = (void*)&this->attribute->am_model.am_Ai_x;
	am_model.am_Ai_StampingPriority$att->data = (void*)&this->attribute->am_model.am_Ai_StampingPriority;
	am_model.am_Ai_NonSPO$att->data = (void*)&this->attribute->am_model.am_Ai_NonSPO;
	am_model.am_Ai_ID$att->data = (void*)&this->attribute->am_model.am_Ai_ID;
	am_model.am_Ai_y$att->data = (void*)&this->attribute->am_model.am_Ai_y;
	am_model.am_Ai_z$att->data = (void*)&this->attribute->am_model.am_Ai_z;
	am_model.am_Ai_Press$att->data = (void*)&this->attribute->am_model.am_Ai_Press;
	am_model.am_Ai_LotNo$att->data = (void*)&this->attribute->am_model.am_Ai_LotNo;
	am_model.am_Ai_Type2$att->data = (void*)&this->attribute->am_model.am_Ai_Type2;
	am_model.am_As_NextLocation$att->data = (void*)&this->attribute->am_model.am_As_NextLocation;
	am_model.am_As_CurrentLocation$att->data = (void*)&this->attribute->am_model.am_As_CurrentLocation;
	am_model.am_As_Message$att->data = (void*)&this->attribute->am_model.am_As_Message;
}

char*
Acceleration_valstrfunc(void* data)
{
	Acceleration* valptr = (Acceleration*)data;

	return acceleration2str(*valptr);
}

Acceleration
Acceleration_strvalfunc(char* data)
{
	return str2Acceleration(data);
}

char*
BlockPtr_valstrfunc(void* data)
{
	block** valptr = (block**)data;

	return actorname(*valptr);
}

block*
BlockPtr_strvalfunc(char* data)
{
	return str2BlockPtr(data);
}

char*
BlockList_valstrfunc(void* data)
{
	AMBlockList** valptr = (AMBlockList**)data;

	return AMBlockListToStr(*valptr);
}

char*
Color_valstrfunc(void* data)
{
	ASI_Color* valptr = (ASI_Color*)data;

	return color2str(*valptr);
}

ASI_Color
Color_strvalfunc(char* data)
{
	return str2Color(data);
}

char*
ContainerPtr_valstrfunc(void* data)
{
	Container** valptr = (Container**)data;

	return actorname(*valptr);
}

Container*
ContainerPtr_strvalfunc(char* data)
{
	return str2ContainerPtr(data);
}

char*
ContainerList_valstrfunc(void* data)
{
	AMContainerList** valptr = (AMContainerList**)data;

	return AMContainerListToStr(*valptr);
}

char*
CounterPtr_valstrfunc(void* data)
{
	counter** valptr = (counter**)data;

	return actorname(*valptr);
}

counter*
CounterPtr_strvalfunc(char* data)
{
	return str2CounterPtr(data);
}

char*
Distance_valstrfunc(void* data)
{
	Distance* valptr = (Distance*)data;

	return distance2str(*valptr);
}

Distance
Distance_strvalfunc(char* data)
{
	return str2Distance(data);
}

char*
FilePtr_valstrfunc(void* data)
{
	iofile** valptr = (iofile**)data;

	return iofile2str(*valptr);
}

iofile*
FilePtr_strvalfunc(char* data)
{
	return str2FilePtr(data);
}

char*
GraphPtr_valstrfunc(void* data)
{
	bgraph** valptr = (bgraph**)data;

	return actorname(*valptr);
}

bgraph*
GraphPtr_strvalfunc(char* data)
{
	return str2GraphPtr(data);
}

char*
Integer_valstrfunc(void* data)
{
	int32* valptr = (int32*)data;

	return int2str(*valptr);
}

int32
Integer_strvalfunc(char* data)
{
	return str2Integer(data);
}

char*
LabelPtr_valstrfunc(void* data)
{
	label** valptr = (label**)data;

	return actorname(*valptr);
}

label*
LabelPtr_strvalfunc(char* data)
{
	return str2LabelPtr(data);
}

char*
LoadPtr_valstrfunc(void* data)
{
	load** valptr = (load**)data;

	return actorname(*valptr);
}

load*
LoadPtr_strvalfunc(char* data)
{
	return str2LoadPtr(data);
}

char*
LoadList_valstrfunc(void* data)
{
	AMLoadList** valptr = (AMLoadList**)data;

	return AMLoadListToStr(*valptr);
}

char*
LoadTypePtr_valstrfunc(void* data)
{
	loadtype** valptr = (loadtype**)data;

	return actorname(*valptr);
}

loadtype*
LoadTypePtr_strvalfunc(char* data)
{
	return str2LoadTypePtr(data);
}

char*
Location_valstrfunc(void* data)
{
	simloc** valptr = (simloc**)data;

	return simlocname(*valptr);
}

simloc*
Location_strvalfunc(char* data)
{
	return str2Location(data);
}

char*
LocationList_valstrfunc(void* data)
{
	AMLocationList** valptr = (AMLocationList**)data;

	return AMLocationListToStr(*valptr);
}

char*
MonitorPtr_valstrfunc(void* data)
{
	State_machine** valptr = (State_machine**)data;

	return actorname(*valptr);
}

State_machine*
MonitorPtr_strvalfunc(char* data)
{
	return str2MonitorPtr(data);
}

char*
MotorPtr_valstrfunc(void* data)
{
	ConvMotor** valptr = (ConvMotor**)data;

	return actorname(*valptr);
}

ConvMotor*
MotorPtr_strvalfunc(char* data)
{
	return str2MotorPtr(data);
}

char*
OrderListPtr_valstrfunc(void* data)
{
	ordlist** valptr = (ordlist**)data;

	return actorname(*valptr);
}

ordlist*
OrderListPtr_strvalfunc(char* data)
{
	return str2OrderListPtr(data);
}

char*
PathPtr_valstrfunc(void* data)
{
	Path** valptr = (Path**)data;

	return actorname(*valptr);
}

Path*
PathPtr_strvalfunc(char* data)
{
	return str2PathPtr(data);
}

char*
PathList_valstrfunc(void* data)
{
	AMPathList** valptr = (AMPathList**)data;

	return AMPathListToStr(*valptr);
}

char*
PhotoeyePtr_valstrfunc(void* data)
{
	Photoeye** valptr = (Photoeye**)data;

	return actorname(*valptr);
}

Photoeye*
PhotoeyePtr_strvalfunc(char* data)
{
	return str2PhotoeyePtr(data);
}

char*
ProcessPtr_valstrfunc(void* data)
{
	process** valptr = (process**)data;

	return actorname(*valptr);
}

process*
ProcessPtr_strvalfunc(char* data)
{
	return str2ProcessPtr(data);
}

char*
QueuePtr_valstrfunc(void* data)
{
	queue** valptr = (queue**)data;

	return actorname(*valptr);
}

queue*
QueuePtr_strvalfunc(char* data)
{
	return str2QueuePtr(data);
}

char*
Rate_valstrfunc(void* data)
{
	Rate* valptr = (Rate*)data;

	return rate2str(*valptr);
}

Rate
Rate_strvalfunc(char* data)
{
	return str2Rate(data);
}

char*
Real_valstrfunc(void* data)
{
	double* valptr = (double*)data;

	return double2str(*valptr);
}

double
Real_strvalfunc(char* data)
{
	return str2Real(data);
}

char*
ResourcePtr_valstrfunc(void* data)
{
	resource** valptr = (resource**)data;

	return actorname(*valptr);
}

resource*
ResourcePtr_strvalfunc(char* data)
{
	return str2ResourcePtr(data);
}

char*
SchedJobPtr_valstrfunc(void* data)
{
	SchedJob** valptr = (SchedJob**)data;

	return actorname(*valptr);
}

SchedJob*
SchedJobPtr_strvalfunc(char* data)
{
	return str2SchedJobPtr(data);
}

char*
SchedJobList_valstrfunc(void* data)
{
	AMSchedJobList** valptr = (AMSchedJobList**)data;

	return AMSchedJobListToStr(*valptr);
}

char*
SectionPtr_valstrfunc(void* data)
{
	ConvSection** valptr = (ConvSection**)data;

	return actorname(*valptr);
}

ConvSection*
SectionPtr_strvalfunc(char* data)
{
	return str2SectionPtr(data);
}

char*
SocketPtr_valstrfunc(void* data)
{
	amSocket** valptr = (amSocket**)data;

	return amsocketname(*valptr);
}

amSocket*
SocketPtr_strvalfunc(char* data)
{
	return str2SocketPtr(data);
}

char*
StatePtr_valstrfunc(void* data)
{
	States** valptr = (States**)data;

	return actorname(*valptr);
}

States*
StatePtr_strvalfunc(char* data)
{
	return str2StatePtr(data);
}

char*
StreamPtr_valstrfunc(void* data)
{
	rnstream** valptr = (rnstream**)data;

	return actorname(*valptr);
}

rnstream*
StreamPtr_strvalfunc(char* data)
{
	return str2StreamPtr(data);
}

char*
String_valstrfunc(void* data)
{
	char** valptr = (char**)data;

	return VALIDSTRING(*valptr);
}

char*
String_strvalfunc(char* data)
{
	return str2String(data);
}

char*
StringList_valstrfunc(void* data)
{
	AMStringList** valptr = (AMStringList**)data;

	return AMStringListToStr(*valptr);
}

char*
SystemPtr_valstrfunc(void* data)
{
	System** valptr = (System**)data;

	return systemname(*valptr);
}

System*
SystemPtr_strvalfunc(char* data)
{
	return str2SystemPtr(data);
}

char*
TablePtr_valstrfunc(void* data)
{
	table** valptr = (table**)data;

	return actorname(*valptr);
}

table*
TablePtr_strvalfunc(char* data)
{
	return str2TablePtr(data);
}

char*
Time_valstrfunc(void* data)
{
	ASITime* valptr = (ASITime*)data;

	return time2str(*valptr);
}

ASITime
Time_strvalfunc(char* data)
{
	return str2Time(data);
}

char*
TransferPtr_valstrfunc(void* data)
{
	ConvTransfer** valptr = (ConvTransfer**)data;

	return actorname(*valptr);
}

ConvTransfer*
TransferPtr_strvalfunc(char* data)
{
	return str2TransferPtr(data);
}

char*
VehiclePtr_valstrfunc(void* data)
{
	vehicle** valptr = (vehicle**)data;

	return actorname(*valptr);
}

vehicle*
VehiclePtr_strvalfunc(char* data)
{
	return str2VehiclePtr(data);
}

char*
VehicleList_valstrfunc(void* data)
{
	AMVehicleList** valptr = (AMVehicleList**)data;

	return AMVehicleListToStr(*valptr);
}

char*
VehSegPtr_valstrfunc(void* data)
{
	VehSeg** valptr = (VehSeg**)data;

	return actorname(*valptr);
}

VehSeg*
VehSegPtr_strvalfunc(char* data)
{
	return str2VehSegPtr(data);
}

char*
VehSegList_valstrfunc(void* data)
{
	AMVehSegList** valptr = (AMVehSegList**)data;

	return AMVehSegListToStr(*valptr);
}

char*
Velocity_valstrfunc(void* data)
{
	Velocity* valptr = (Velocity*)data;

	return velocity2str(*valptr);
}

Velocity
Velocity_strvalfunc(char* data)
{
	return str2Velocity(data);
}

char*
LocationListList_valstrfunc(void* data)
{
	AMLocationList** valptr = (AMLocationList**)data;

	return AMLocationListToStr(*valptr);
}

ImplementList(_IntegerList, int32, 0);

char*
IntegerList_valstrfunc(void* data)
{
	AM_IntegerList** valptr = (AM_IntegerList**)data;

	return AM_IntegerListToStr(*valptr);
}

char*
Ptr2Socket_valstrfunc(void* data)
{
	struct Ptr2Socket** valptr = (struct Ptr2Socket**)data;

	return sock_GetName(*valptr);
}

struct Ptr2Socket*
Ptr2Socket_strvalfunc(char* data)
{
	return String2Sock(data);
}

char*
Ptr2JSON_valstrfunc(void* data)
{
	struct cJSON** valptr = (struct cJSON**)data;

	return cJSON_Print(*valptr);
}

struct cJSON*
Ptr2JSON_strvalfunc(char* data)
{
	return cJSON_Parse(data);
}
