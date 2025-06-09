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
struct load** Getimpfirstload();
extern struct load** imp_firstload;
#undef firstload
#define firstload (*imp_firstload)
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
int32* Getimpsaa();
extern int32* imp_saa;
#undef saa
#define saa (*imp_saa)
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

typedef struct loadatt {
	struct {
		int32 am_Ai_pi;	/* Ai_pi */
		int32 am_Ai_CarQty;	/* Ai_CarQty */
		char* am_As_Type;	/* As_Type */
		int32 am_Ai_Type;	/* Ai_Type */
		int32 am_Ai_Seq;	/* Ai_Seq */
		int32 am_Ai_LotSizeQty;	/* Ai_LotSizeQty */
		ASITime am_At_TimeInSys;	/* At_TimeInSys */
		int32 am_Ai_Status;	/* Ai_Status */
		int32 am_Ai_Zone;	/* Ai_Zone */
		int32 am_Ai_Row;	/* Ai_Row */
		int32 am_Ai_Line;	/* Ai_Line */
	} am_model;
} loadatt;

#define ValidIndex(NAME, INDEX, MAXINDEX) validindex(NAME, INDEX, MAXINDEX)
#define ValidPtr(VALUE, KIND, CAST) ((CAST)validptr(VALUE, KIND))

#endif /* __USER_H__ */
