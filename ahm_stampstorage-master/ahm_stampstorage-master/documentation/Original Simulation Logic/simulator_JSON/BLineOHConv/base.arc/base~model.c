/* AutoMod 11.2 */

#include "decls.h"

struct model_struct am_model;

char CommandName[] = "C:/AutoMod/11-2-1-1949-23SG/Bin/am2.exe";
extern ASIWindow WORKMENU;

FILE** imp_stdin;
FILE** imp_stdout;
FILE** imp_stderr;
struct process_system** imp_headpt;
struct load** imp_firstload;
struct model_header** imp_model;
FILE** imp_tfp;
FILE** imp_rfp;
FILE** imp_pfp;
int32* imp_trace;
int32* imp_animate;
int32* imp_Animating;
int32* imp_saa;
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
#endif /*__GNUC__*/


MODELDLLEXPORT
init_globs()
{
	imp_headpt = Getimpheadpt();
	imp_firstload = Getimpfirstload();
	imp_model = Getimpmodel();
	imp_tfp = Getimptfp();
	imp_rfp = Getimprfp();
	imp_pfp = Getimppfp();
	imp_trace = Getimptrace();
	imp_animate = Getimpanimate();
	imp_Animating = GetimpAnimating();
	imp_saa = Getimpsaa();
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
	}
	if (that != NULL) {
		*(this->attribute) = *(that->attribute);
		if (this->attribute->am_model.am_As_Type) {
			this->attribute->am_model.am_As_Type = strdup(that->attribute->am_model.am_As_Type);
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
		data = (void*)&this->attribute->am_model.am_Ai_LotSizeQty;
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
	am_model.am_Ai_LotSizeQty$att->data = (void*)&this->attribute->am_model.am_Ai_LotSizeQty;
	am_model.am_At_TimeInSys$att->data = (void*)&this->attribute->am_model.am_At_TimeInSys;
	am_model.am_Ai_Status$att->data = (void*)&this->attribute->am_model.am_Ai_Status;
	am_model.am_Ai_Zone$att->data = (void*)&this->attribute->am_model.am_Ai_Zone;
	am_model.am_Ai_Row$att->data = (void*)&this->attribute->am_model.am_Ai_Row;
	am_model.am_Ai_Line$att->data = (void*)&this->attribute->am_model.am_Ai_Line;
}

char*
Acceleration_valstrfunc(void* data)
{
	Acceleration* valptr = (Acceleration*)data;

	return acceleration2str(*valptr);
}

char*
BlockPtr_valstrfunc(void* data)
{
	block** valptr = (block**)data;

	return actorname(*valptr);
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

char*
CounterPtr_valstrfunc(void* data)
{
	counter** valptr = (counter**)data;

	return actorname(*valptr);
}

char*
Distance_valstrfunc(void* data)
{
	Distance* valptr = (Distance*)data;

	return distance2str(*valptr);
}

char*
FilePtr_valstrfunc(void* data)
{
	iofile** valptr = (iofile**)data;

	return iofile2str(*valptr);
}

char*
GraphPtr_valstrfunc(void* data)
{
	bgraph** valptr = (bgraph**)data;

	return actorname(*valptr);
}

char*
Integer_valstrfunc(void* data)
{
	int32* valptr = (int32*)data;

	return int2str(*valptr);
}

char*
LabelPtr_valstrfunc(void* data)
{
	label** valptr = (label**)data;

	return actorname(*valptr);
}

char*
LoadPtr_valstrfunc(void* data)
{
	load** valptr = (load**)data;

	return actorname(*valptr);
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

char*
Location_valstrfunc(void* data)
{
	simloc** valptr = (simloc**)data;

	return simlocname(*valptr);
}

char*
LocationList_valstrfunc(void* data)
{
	AMLocationList** valptr = (AMLocationList**)data;

	return AMLocationListToStr(*valptr);
}

char*
MaterialPtr_valstrfunc(void* data)
{
	BulkMaterial** valptr = (BulkMaterial**)data;

	return actorname(*valptr);
}

char*
MonitorPtr_valstrfunc(void* data)
{
	State_machine** valptr = (State_machine**)data;

	return actorname(*valptr);
}

char*
MotorPtr_valstrfunc(void* data)
{
	ConvMotor** valptr = (ConvMotor**)data;

	return actorname(*valptr);
}

char*
OrderListPtr_valstrfunc(void* data)
{
	ordlist** valptr = (ordlist**)data;

	return actorname(*valptr);
}

char*
PathPtr_valstrfunc(void* data)
{
	Path** valptr = (Path**)data;

	return actorname(*valptr);
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

char*
PipePtr_valstrfunc(void* data)
{
	BulkPipe** valptr = (BulkPipe**)data;

	return actorname(*valptr);
}

char*
ProcessPtr_valstrfunc(void* data)
{
	process** valptr = (process**)data;

	return actorname(*valptr);
}

char*
QueuePtr_valstrfunc(void* data)
{
	queue** valptr = (queue**)data;

	return actorname(*valptr);
}

char*
Rate_valstrfunc(void* data)
{
	Rate* valptr = (Rate*)data;

	return rate2str(*valptr);
}

char*
Real_valstrfunc(void* data)
{
	double* valptr = (double*)data;

	return double2str(*valptr);
}

char*
ResourcePtr_valstrfunc(void* data)
{
	resource** valptr = (resource**)data;

	return actorname(*valptr);
}

char*
SchedJobPtr_valstrfunc(void* data)
{
	SchedJob** valptr = (SchedJob**)data;

	return actorname(*valptr);
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

char*
StatePtr_valstrfunc(void* data)
{
	States** valptr = (States**)data;

	return actorname(*valptr);
}

char*
StreamPtr_valstrfunc(void* data)
{
	rnstream** valptr = (rnstream**)data;

	return actorname(*valptr);
}

char*
String_valstrfunc(void* data)
{
	char** valptr = (char**)data;

	return VALIDSTRING(*valptr);
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

char*
TablePtr_valstrfunc(void* data)
{
	table** valptr = (table**)data;

	return actorname(*valptr);
}

char*
TankPtr_valstrfunc(void* data)
{
	BulkTank** valptr = (BulkTank**)data;

	return actorname(*valptr);
}

char*
TankTransferPtr_valstrfunc(void* data)
{
	BulkTransfer** valptr = (BulkTransfer**)data;

	return actorname(*valptr);
}

char*
TankTransferList_valstrfunc(void* data)
{
	AMTankTransferList** valptr = (AMTankTransferList**)data;

	return AMTankTransferListToStr(*valptr);
}

char*
TankTriggerPtr_valstrfunc(void* data)
{
	BulkTrigger** valptr = (BulkTrigger**)data;

	return actorname(*valptr);
}

char*
TankTriggerList_valstrfunc(void* data)
{
	AMTankTriggerList** valptr = (AMTankTriggerList**)data;

	return AMTankTriggerListToStr(*valptr);
}

char*
Time_valstrfunc(void* data)
{
	ASITime* valptr = (ASITime*)data;

	return time2str(*valptr);
}

char*
TransferPtr_valstrfunc(void* data)
{
	ConvTransfer** valptr = (ConvTransfer**)data;

	return actorname(*valptr);
}

char*
VehiclePtr_valstrfunc(void* data)
{
	vehicle** valptr = (vehicle**)data;

	return actorname(*valptr);
}

char*
VehicleList_valstrfunc(void* data)
{
	AMVehicleList** valptr = (AMVehicleList**)data;

	return AMVehicleListToStr(*valptr);
}

char*
Velocity_valstrfunc(void* data)
{
	Velocity* valptr = (Velocity*)data;

	return velocity2str(*valptr);
}

char*
LocationListList_valstrfunc(void* data)
{
	AMLocationList** valptr = (AMLocationList**)data;

	return AMLocationListToStr(*valptr);
}
