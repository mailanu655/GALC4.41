// user.h
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


#ifndef __USER_H__
#define __USER_H__

#include "amc2.h"

FILE** Getimpstdin();
extern FILE** imp_stdin;
#undef stdin
#define stdin (*imp_stdin)
FILE** Getimpstdout();
extern FILE** imp_stdout;
#undef stdout
#define stdout (*imp_stdout)
FILE** Getimpstderr();
extern FILE** imp_stderr;
#undef stderr
#define stderr (*imp_stderr)
struct process_system** Getimpheadpt();
extern struct process_system** imp_headpt;
#undef headpt
#define headpt (*imp_headpt)
struct AMLoadList* Getimpactiveloads();
extern struct AMLoadList* imp_activeloads;
#undef activeloads
#define activeloads (*imp_activeloads)
struct model_header** Getimpmodel();
extern struct model_header** imp_model;
#undef model
#define model (*imp_model)
FILE** Getimptfp();
extern FILE** imp_tfp;
#undef tfp
#define tfp (*imp_tfp)
FILE** Getimprfp();
extern FILE** imp_rfp;
#undef rfp
#define rfp (*imp_rfp)
FILE** Getimppfp();
extern FILE** imp_pfp;
#undef pfp
#define pfp (*imp_pfp)
int32* Getimptrace();
extern int32* imp_trace;
#undef trace
#define trace (*imp_trace)
int32* Getimpanimate();
extern int32* imp_animate;
#undef animate
#define animate (*imp_animate)
int32* GetimpAnimating();
extern int32* imp_Animating;
#undef Animating
#define Animating (*imp_Animating)
unsigned long* Getimpchangeflag();
extern unsigned long* imp_changeflag;
#undef changeflag
#define changeflag (*imp_changeflag)
int32* GetimpEnableBeaming();
extern int32* imp_EnableBeaming;
#undef EnableBeaming
#define EnableBeaming (*imp_EnableBeaming)
Time* GetimpASIclock();
extern Time* imp_ASIclock;
#undef ASIclock
#define ASIclock (*imp_ASIclock)
Time* Getimpbeginper();
extern Time* imp_beginper;
#undef beginper
#define beginper (*imp_beginper)
Boolean* GetimpSIMULATOR();
extern Boolean* imp_SIMULATOR;
#undef SIMULATOR
#define SIMULATOR (*imp_SIMULATOR)
struct AMEventNoticeList** Getimpcel();
extern struct AMEventNoticeList** imp_cel;
#undef cel
#define cel (*imp_cel)
struct eventlist* Getimpfel();
extern struct eventlist* imp_fel;
#undef fel
#define fel (*imp_fel)
struct dlogItem** Getimpgrf_sl();
extern struct dlogItem** imp_grf_sl;
#undef grf_sl
#define grf_sl (*imp_grf_sl)
struct s_menu** Getimppopmenu();
extern struct s_menu** imp_popmenu;
#undef popmenu
#define popmenu (*imp_popmenu)
ASIWindow* GetimpWORKMENU();
extern ASIWindow* imp_WORKMENU;
#undef WORKMENU
#define WORKMENU (*imp_WORKMENU)
ASIWindow* GetimpSimLotStatusWin();
extern ASIWindow* imp_SimLotStatusWin;
#undef SimLotStatusWin
#define SimLotStatusWin (*imp_SimLotStatusWin)
ASIWindow* GetimpSimStnStatusWin();
extern ASIWindow* imp_SimStnStatusWin;
#undef SimStnStatusWin
#define SimStnStatusWin (*imp_SimStnStatusWin)
ASIWindow* GetimpSimOperStatusWin();
extern ASIWindow* imp_SimOperStatusWin;
#undef SimOperStatusWin
#define SimOperStatusWin (*imp_SimOperStatusWin)
ASIWindow* GetimpSimInvStatusWin();
extern ASIWindow* imp_SimInvStatusWin;
#undef SimInvStatusWin
#define SimInvStatusWin (*imp_SimInvStatusWin)
ASIWindow* GetimpSimKanbanStatusWin();
extern ASIWindow* imp_SimKanbanStatusWin;
#undef SimKanbanStatusWin
#define SimKanbanStatusWin (*imp_SimKanbanStatusWin)
#ifdef VISUALC
#define MODELDLLEXPORT __declspec(dllexport)
#else
#define MODELDLLEXPORT
#endif /*VISUALC*/
DeclareList(_IntegerList, int32,);
#define AM_IntegerListItemCopy(item1, item2) item1 = item2
#define AM_IntegerListItemEqual(item1, item2) (item1 == item2)
#define AM_IntegerListItemRemove(item) /* empty */
#define AM_IntegerListItemToStr(item) int2str(item)

typedef struct loadatt {
	struct {
		int32 am_Ai_pi;	/* Ai_pi */
		int32 am_Ai_CarQty;	/* Ai_CarQty */
		char* am_As_Type;	/* As_Type */
		int32 am_Ai_Type;	/* Ai_Type */
		int32 am_Ai_Seq;	/* Ai_Seq */
		int32 am_Ai_LotQty;	/* Ai_LotQty */
		ASITime am_At_TimeInSys;	/* At_TimeInSys */
		int32 am_Ai_Status;	/* Ai_Status */
		int32 am_Ai_Zone;	/* Ai_Zone */
		int32 am_Ai_Row;	/* Ai_Row */
		int32 am_Ai_Line;	/* Ai_Line */
		int32 am_Ai_Side;	/* Ai_Side */
		simloc* am_Aloc_Destination;	/* Aloc_Destination */
		int32 am_Ai_x;	/* Ai_x */
		int32 am_Ai_StampingPriority;	/* Ai_StampingPriority */
		int32 am_Ai_NonSPO;	/* Ai_NonSPO */
		int32 am_Ai_ID;	/* Ai_ID */
		int32 am_Ai_y;	/* Ai_y */
		int32 am_Ai_z;	/* Ai_z */
		int32 am_Ai_Press;	/* Ai_Press */
		int32 am_Ai_LotNo;	/* Ai_LotNo */
		int32 am_Ai_Type2;	/* Ai_Type2 */
		char* am_As_NextLocation;	/* As_NextLocation */
		char* am_As_CurrentLocation;	/* As_CurrentLocation */
		char* am_As_Message;	/* As_Message */
	} am_model;
} loadatt;

#define ValidIndex(NAME, INDEX, MAXINDEX) validindex(NAME, INDEX, MAXINDEX)
#define ValidPtr(VALUE, KIND, CAST) ((CAST)validptr(VALUE, KIND))

#endif // __USER_H__
