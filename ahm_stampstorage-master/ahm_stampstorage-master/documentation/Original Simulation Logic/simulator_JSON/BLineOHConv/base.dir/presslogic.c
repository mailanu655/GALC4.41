// PressLogic.c
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


#include "cdecls.h"

#undef F_getID
static int32 F_getID(void);

static int32
pInit_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pInit", localactor);
	AMDebuggerParams("base.pInit", pInit_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 2);
			SetString(&am2_Vs_SimStart, "03-10-11 06:30:00");
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 3);
			{
				char* pArg1 = "Time";
				char* pArg2 = "\t";
				char* pArg3 = "Type";
				char* pArg4 = "\t";
				char* pArg5 = "LotNo";
				char* pArg6 = "\t";
				char* pArg7 = "Zone";
				char* pArg8 = "\t";
				char* pArg9 = "Row";

				fprintf(ffp_base_arc_RESULTS_StoreIn_out->fp, "%s%s%s%s%s%s%s%s%s\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 4);
			{
				char* pArg1 = "Time";
				char* pArg2 = "\t";
				char* pArg3 = "Type";
				char* pArg4 = "\t";
				char* pArg5 = "LotNo";
				char* pArg6 = "\t";
				char* pArg7 = "Zone";
				char* pArg8 = "\t";
				char* pArg9 = "Row";

				fprintf(ffp_base_arc_RESULTS_StoreOut1_out->fp, "%s%s%s%s%s%s%s%s%s\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 5);
			{
				char* pArg1 = "Time";
				char* pArg2 = "\t";
				char* pArg3 = "Type";
				char* pArg4 = "\t";
				char* pArg5 = "LotNo";
				char* pArg6 = "\t";
				char* pArg7 = "Zone";
				char* pArg8 = "\t";
				char* pArg9 = "Row";

				fprintf(ffp_base_arc_RESULTS_StoreOut2_out->fp, "%s%s%s%s%s%s%s%s%s\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 7);
			SetString(&am2_vsZone[1], "CHigh");
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 8);
			SetString(&am2_vsZone[2], "CLow");
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 9);
			SetString(&am2_vsZone[3], "ABay");
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 11);
			am2_viKDcount[2] = 25;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 13);
			this->attribute->am2_Ai_pi = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 14);
			while (this->attribute->am2_Ai_pi <= 50) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 15);
					if (this->attribute->am2_Ai_pi <= 30) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 16);
							{
								char* pArg1 = "";

								updatelabel(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), "%s", pArg1);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 17);
							{
								char* pArg1 = "";

								updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), "%s", pArg1);
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 19);
					{
						char* pArg1 = "";

						updatelabel(&(am2_Lb_PartNames[ValidIndex("am_model.am_Lb_PartNames", this->attribute->am2_Ai_pi, 50)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 20);
					{
						char* pArg1 = "";

						updatelabel(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", this->attribute->am2_Ai_pi, 50)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 21);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 24);
			CntSetContents(ValidPtr(&(am2_C_ZoneMix[2]), 10, counter*), 35, this);
			EntityChanged(0x00000010);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 26);
			clone(this, 1, &(am2_P_LoadRBT[1]), am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 27);
			clone(this, 1, &(am2_P_LoadRBT[2]), am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 28);
			clone(this, 1, &(am2_P_LoadRBT[3]), am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 29);
			clone(this, 1, &(am2_P_LoadRBT[4]), am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 31);
			clone(this, 1, &(am2_P_WeldSched[1]), am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 32);
			clone(this, 1, &(am2_P_WeldSched[2]), am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 34);
			clone(this, 1, am2_P_Time, am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 36);
			{
				int32 tempint = order(1, &(am2_O_BufferSpace[1]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[1]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 37);
			{
				int32 tempint = order(1, &(am2_O_BufferSpace[2]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[2]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 38);
			{
				int32 tempint = order(1, &(am2_O_BufferSpace[3]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[3]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 39);
			{
				int32 tempint = order(1, &(am2_O_BufferSpace[4]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[4]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 41);
			{
				int32 tempint = order(1, &(am2_O_ST13_SPO_Seq[1]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_ST13_SPO_Seq[1]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 42);
			{
				int32 tempint = order(1, &(am2_O_ST13_SPO_Seq[2]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_ST13_SPO_Seq[2]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 43);
			{
				int32 tempint = order(1, &(am2_O_ST13_SPO_Seq[3]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_ST13_SPO_Seq[3]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 44);
			{
				int32 tempint = order(1, &(am2_O_ST13_SPO_Seq[4]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_ST13_SPO_Seq[4]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 46);
			{
				int32 tempint = order(1, &(am2_O_PrevClear[1]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_PrevClear[1]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 47);
			{
				int32 tempint = order(1, &(am2_O_PrevClear[2]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_PrevClear[2]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 48);
			{
				int32 tempint = order(1, &(am2_O_PrevClear[3]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_PrevClear[3]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 50);
			{
				int32 tempint = order(1, &(am2_olPressSide[1]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_olPressSide[1]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 51);
			{
				int32 tempint = order(1, &(am2_olPressSide[2]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_olPressSide[2]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 52);
			{
				int32 tempint = order(1, &(am2_olPressSide[3]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_olPressSide[3]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 53);
			{
				int32 tempint = order(1, &(am2_olPressSide[4]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_olPressSide[4]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 55);
			{
				int32 tempint = order(4, am2_O_WE2BufferRH, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_O_WE2BufferRH, NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 56);
			{
				int32 tempint = order(1, am2_O_WE2BufferLH, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_O_WE2BufferLH, NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 58);
			{
				int32 tempint = order(1, am2_oLiftClear, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_oLiftClear, NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 59);
			{
				int32 tempint = order(1, &(am2_O_StampingPriority[1]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_StampingPriority[1]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 60);
			{
				int32 tempint = order(1, &(am2_O_StampingPriority[2]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_StampingPriority[2]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 61);
			{
				int32 tempint = order(1, am2_O_Weld1Order, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_O_Weld1Order, NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 62);
			{
				int32 tempint = order(1, &(am2_O_Weld2Order[1]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[1]), NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 63);
			{
				int32 tempint = order(1, am2_oBPressExit, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_oBPressExit, NULL, NULL);	/* Place a backorder */
			}
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vr_SPOCycleTime$var, &am2_Vr_SPOCycleTime[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 69);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vr_SPOCycleTime$var, &am2_Vr_SPOCycleTime[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 70);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vr_SPOCycleTime$var, &am2_Vr_SPOCycleTime[3], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 71);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vr_SPOCycleTime$var, &am2_Vr_SPOCycleTime[4], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 72);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vr_WeldChangeoverTime$var, &am2_Vr_WeldChangeoverTime[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 73);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vr_WeldChangeoverTime$var, &am2_Vr_WeldChangeoverTime[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 74);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_WeldOutputTarget$var, &am2_Vi_WeldOutputTarget[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 75);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_WeldOutputTarget$var, &am2_Vi_WeldOutputTarget[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 76);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_Release$var, &am2_Vi_Release, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 77);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrDieChangeTime$var, &am2_vrDieChangeTime[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 78);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrDieChangeTime$var, &am2_vrDieChangeTime[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 79);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viReqVeh$var, &am2_viReqVeh, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 80);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_BayCapacity$var, &am2_Vi_BayCapacity[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 81);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_BayCapacity$var, &am2_Vi_BayCapacity[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 82);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_BayCapacity$var, &am2_Vi_BayCapacity[3], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 83);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viABayTrigger$var, &am2_viABayTrigger, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 84);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_EmptyZone$var, &am2_Vi_EmptyZone[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 85);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_EmptyZone$var, &am2_Vi_EmptyZone[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 86);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_EmptyZone$var, &am2_Vi_EmptyZone[3], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 87);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_EmptyReq$var, &am2_Vi_EmptyReq, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 88);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_Vi_SplitSPO$var, &am2_Vi_SplitSPO, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 89);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viPressActive$var, &am2_viPressActive[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 90);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viPressActive$var, &am2_viPressActive[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 91);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viEmptyLoad$var, &am2_viEmptyLoad[1][2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 92);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viEmptyLoad$var, &am2_viEmptyLoad[2][2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 93);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viCLineEmptyTrigger$var, &am2_viCLineEmptyTrigger, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 94);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_viInitializeSPO$var, &am2_viInitializeSPO, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 95);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[1], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 96);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[2], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 97);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[3], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 98);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[4], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 99);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[5], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 100);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[6], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 101);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[7], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 102);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[8], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 103);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[9], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 104);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrStopTime$var, &am2_vrStopTime[10], NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 105);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrBPressLiftDown$var, &am2_vrBPressLiftDown, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 106);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			int rflag;
			static ReadRef st1;
			static ReadRef st2;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			setupReadRef(&st2, 0, am_model.am_vrBPressLiftUp$var, &am2_vrBPressLiftUp, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 107);
			rflag = readFile(ffp_base_arc_DATA_general_dat->fp, "\t", &st1, &st2, NULL);
			SetFileAtEof(ffp_base_arc_DATA_general_dat, EOF, rflag);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 109);
			am2_Vr_SPOCycleTime[1] = am2_Vr_SPOCycleTime[1] - 6;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 110);
			am2_Vr_SPOCycleTime[2] = am2_Vr_SPOCycleTime[2] - 6;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 111);
			am2_Vr_SPOCycleTime[3] = am2_Vr_SPOCycleTime[3] - 6;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 112);
			am2_Vr_SPOCycleTime[4] = am2_Vr_SPOCycleTime[4] - 6;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 114);
			am2_Vi_SPO_QtyRelease[1] = am2_Vi_Release;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 115);
			am2_Vi_SPO_QtyRelease[2] = am2_Vi_Release;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 116);
			am2_Vi_SPO_QtyRelease[3] = am2_Vi_Release;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 117);
			am2_Vi_SPO_QtyRelease[4] = am2_Vi_Release;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 119);
			this->attribute->am2_Ai_pi = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 120);
			while (this->attribute->am2_Ai_pi <= 35) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 121);
					if (this->attribute->am2_Ai_pi <= 20) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 121);
						LocSetCapacity(ValidPtr(SysGetQualifier(am_model.am_pf_ohc.$sys, "ST12_", this->attribute->am2_Ai_pi, -9999), 38, simloc*), am2_Vi_BayCapacity[2]);
						EntityChanged(0x00200040);
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 122);
						if (this->attribute->am2_Ai_pi >= 30) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 122);
							LocSetCapacity(ValidPtr(SysGetQualifier(am_model.am_pf_ohc.$sys, "ST12_", this->attribute->am2_Ai_pi, -9999), 38, simloc*), am2_Vi_BayCapacity[3]);
							EntityChanged(0x00200040);
						}
						else {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 123);
							LocSetCapacity(ValidPtr(SysGetQualifier(am_model.am_pf_ohc.$sys, "ST12_", this->attribute->am2_Ai_pi, -9999), 38, simloc*), am2_Vi_BayCapacity[1]);
							EntityChanged(0x00200040);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 124);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 127);
			this->attribute->am2_Ai_Seq = 1;
			EntityChanged(0x00000040);
		}
		{
			int rflag;
			static ReadRef st1;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 128);
			rflag = readFile(ffp_base_arc_DATA_modellist_dat->fp, "\t", &st1, NULL);
			SetFileAtEof(ffp_base_arc_DATA_modellist_dat, EOF, rflag);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 129);
			while (StringCompare(am2_Vs_junk, "0") != 0 && this->attribute->am2_Ai_Seq <= 20) {
				{
					int rflag;
					static ReadRef st1;
					static ReadRef st2;

					setupReadRef(&st1, 0, am_model.am_vsSpoByType$var, &am2_vsSpoByType[ValidIndex("am_model.am_vsSpoByType", this->attribute->am2_Ai_Seq, 20)], NULL, -1, FALSE);
					setupReadRef(&st2, 0, am_model.am_viDailyTotal$var, &am2_viDailyTotal[ValidIndex("am_model.am_viDailyTotal", this->attribute->am2_Ai_Seq, 20)], NULL, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 130);
					rflag = readFile(ffp_base_arc_DATA_modellist_dat->fp, "\t", &st1, &st2, NULL);
					SetFileAtEof(ffp_base_arc_DATA_modellist_dat, EOF, rflag);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 131);
					if (StringCompare(am2_vsSpoByType[ValidIndex("am_model.am_vsSpoByType", this->attribute->am2_Ai_Seq, 20)], "No") == 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 132);
						clone(this, 1, &(am2_pNonSpoPartMgt[ValidIndex("am_model.am_pNonSpoPartMgt", this->attribute->am2_Ai_Seq, 20)]), NULL);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 133);
					this->attribute->am2_Ai_Seq += 2;
					EntityChanged(0x00000040);
				}
				{
					int rflag;
					static ReadRef st1;

					setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 134);
					rflag = readFile(ffp_base_arc_DATA_modellist_dat->fp, "\t", &st1, NULL);
					SetFileAtEof(ffp_base_arc_DATA_modellist_dat, EOF, rflag);
				}
			}
		}
		{
			int rflag;
			static ReadRef st1;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 138);
			rflag = readFile(ffp_base_arc_DATA_down_dat->fp, "\n", &st1, NULL);
			SetFileAtEof(ffp_base_arc_DATA_down_dat, EOF, rflag);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 139);
			this->attribute->am2_Ai_Seq = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 140);
			while (this->attribute->am2_Ai_Seq <= 5) {
				{
					int rflag;
					static ReadRef st1;
					static ReadRef st2;
					static ReadRef st3;
					static ReadRef st4;

					setupReadRef(&st1, 0, am_model.am_Vs_Downtime$var, &am2_Vs_Downtime[ValidIndex("am_model.am_Vs_Downtime", this->attribute->am2_Ai_Seq, 5)], NULL, -1, FALSE);
					setupReadRef(&st2, 0, am_model.am_Vr_Uptime$var, &am2_Vr_Uptime[ValidIndex("am_model.am_Vr_Uptime", this->attribute->am2_Ai_Seq, 5)], NULL, -1, FALSE);
					setupReadRef(&st3, 0, am_model.am_Vr_MTTR$var, &am2_Vr_MTTR[ValidIndex("am_model.am_Vr_MTTR", this->attribute->am2_Ai_Seq, 5)], NULL, -1, FALSE);
					setupReadRef(&st4, 0, am_model.am_Vi_InclDT$var, &am2_Vi_InclDT[ValidIndex("am_model.am_Vi_InclDT", this->attribute->am2_Ai_Seq, 5)], NULL, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 141);
					rflag = readFile(ffp_base_arc_DATA_down_dat->fp, "\t", &st1, &st2, &st3, &st4, NULL);
					SetFileAtEof(ffp_base_arc_DATA_down_dat, EOF, rflag);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 142);
					if (am2_Vi_InclDT[ValidIndex("am_model.am_Vi_InclDT", this->attribute->am2_Ai_Seq, 5)] == 1 && am2_Vr_Uptime[ValidIndex("am_model.am_Vr_Uptime", this->attribute->am2_Ai_Seq, 5)] < 100) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 143);
							am2_Vr_MTBF[ValidIndex("am_model.am_Vr_MTBF", this->attribute->am2_Ai_Seq, 5)] = (am2_Vr_Uptime[ValidIndex("am_model.am_Vr_Uptime", this->attribute->am2_Ai_Seq, 5)] * am2_Vr_MTTR[ValidIndex("am_model.am_Vr_MTTR", this->attribute->am2_Ai_Seq, 5)]) / (100 - am2_Vr_Uptime[ValidIndex("am_model.am_Vr_Uptime", this->attribute->am2_Ai_Seq, 5)]);
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 144);
							clone(this, 1, &(am2_P_Downtime[ValidIndex("am_model.am_P_Downtime", this->attribute->am2_Ai_Seq, 5)]), NULL);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 146);
					this->attribute->am2_Ai_Seq += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 149);
			am2_vrscDowntime[1] = &(am2_R_Weld[1]);
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 150);
			am2_vrscDowntime[2] = &(am2_R_Weld[2]);
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 151);
			am2_vrscDowntime[3] = am2_rBLine;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 152);
			am2_vrscDowntime[4] = am2_rCLine;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 155);
			this->attribute->am2_Ai_Seq = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 156);
			while (this->attribute->am2_Ai_Seq <= 3) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 157);
					this->attribute->am2_Ai_pi = 1;
					EntityChanged(0x00000040);
				}
				{
					int rflag;
					static ReadRef st1;

					setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 158);
					rflag = readFile(ffp_base_arc_DATA_stpshift_dat->fp, "\n", &st1, NULL);
					SetFileAtEof(ffp_base_arc_DATA_stpshift_dat, EOF, rflag);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 159);
					while (this->attribute->am2_Ai_pi <= 9) {
						{
							int rflag;
							static ReadRef st1;
							static ReadRef st2;

							setupReadRef(&st1, 0, am_model.am_vsStpShift$var, &am2_vsStpShift[ValidIndex("am_model.am_vsStpShift", this->attribute->am2_Ai_pi, 9)][ValidIndex("am_model.am_vsStpShift", this->attribute->am2_Ai_Seq, 3)], NULL, -1, FALSE);
							setupReadRef(&st2, 0, am_model.am_vrStpShiftDuration$var, &am2_vrStpShiftDuration[ValidIndex("am_model.am_vrStpShiftDuration", this->attribute->am2_Ai_pi, 9)][ValidIndex("am_model.am_vrStpShiftDuration", this->attribute->am2_Ai_Seq, 3)], NULL, -1, FALSE);
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 160);
							rflag = readFile(ffp_base_arc_DATA_stpshift_dat->fp, "\t", &st1, &st2, NULL);
							SetFileAtEof(ffp_base_arc_DATA_stpshift_dat, EOF, rflag);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 161);
							this->attribute->am2_Ai_pi += 1;
							EntityChanged(0x00000040);
						}
					}
				}
				{
					int rflag;
					static ReadRef st1;
					static ReadRef st2;

					setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					setupReadRef(&st2, 0, am_model.am_viProductionMin$var, &am2_viProductionMin[ValidIndex("am_model.am_viProductionMin", this->attribute->am2_Ai_Seq, 3)][3], NULL, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 163);
					rflag = readFile(ffp_base_arc_DATA_stpshift_dat->fp, "\t", &st1, &st2, NULL);
					SetFileAtEof(ffp_base_arc_DATA_stpshift_dat, EOF, rflag);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 164);
					this->attribute->am2_Ai_Seq += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 170);
			this->attribute->am2_Ai_Line = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 171);
			while (this->attribute->am2_Ai_Line <= 2) {
				{
					int rflag;
					static ReadRef st1;

					setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 172);
					rflag = readFile(ffp_base_arc_DATA_wldshift_dat->fp, "\n", &st1, NULL);
					SetFileAtEof(ffp_base_arc_DATA_wldshift_dat, EOF, rflag);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 173);
					this->attribute->am2_Ai_Seq = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 174);
					while (this->attribute->am2_Ai_Seq <= 2) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 175);
							this->attribute->am2_Ai_pi = 1;
							EntityChanged(0x00000040);
						}
						{
							int rflag;
							static ReadRef st1;

							setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 176);
							rflag = readFile(ffp_base_arc_DATA_wldshift_dat->fp, "\n", &st1, NULL);
							SetFileAtEof(ffp_base_arc_DATA_wldshift_dat, EOF, rflag);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 177);
							while (this->attribute->am2_Ai_pi <= 10) {
								{
									int rflag;
									static ReadRef st1;
									static ReadRef st2;

									setupReadRef(&st1, 0, am_model.am_vsWeldShift$var, &am2_vsWeldShift[ValidIndex("am_model.am_vsWeldShift", this->attribute->am2_Ai_pi, 10)][ValidIndex("am_model.am_vsWeldShift", this->attribute->am2_Ai_Seq, 3)][ValidIndex("am_model.am_vsWeldShift", this->attribute->am2_Ai_Line, 2)], NULL, -1, FALSE);
									setupReadRef(&st2, 0, am_model.am_vrWeldShiftDuration$var, &am2_vrWeldShiftDuration[ValidIndex("am_model.am_vrWeldShiftDuration", this->attribute->am2_Ai_pi, 10)][ValidIndex("am_model.am_vrWeldShiftDuration", this->attribute->am2_Ai_Seq, 3)][ValidIndex("am_model.am_vrWeldShiftDuration", this->attribute->am2_Ai_Line, 2)], NULL, -1, FALSE);
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 178);
									rflag = readFile(ffp_base_arc_DATA_wldshift_dat->fp, "\t", &st1, &st2, NULL);
									SetFileAtEof(ffp_base_arc_DATA_wldshift_dat, EOF, rflag);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 179);
									this->attribute->am2_Ai_pi += 1;
									EntityChanged(0x00000040);
								}
							}
						}
						{
							int rflag;
							static ReadRef st1;
							static ReadRef st2;

							setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
							setupReadRef(&st2, 0, am_model.am_viProductionMin$var, &am2_viProductionMin[ValidIndex("am_model.am_viProductionMin", this->attribute->am2_Ai_Seq, 3)][ValidIndex("am_model.am_viProductionMin", this->attribute->am2_Ai_Line, 3)], NULL, -1, FALSE);
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 181);
							rflag = readFile(ffp_base_arc_DATA_wldshift_dat->fp, "\t", &st1, &st2, NULL);
							SetFileAtEof(ffp_base_arc_DATA_wldshift_dat, EOF, rflag);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 182);
							this->attribute->am2_Ai_Seq += 1;
							EntityChanged(0x00000040);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 184);
					this->attribute->am2_Ai_Line += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 189);
			clone(this, 1, am2_P_StampingShift, NULL);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 190);
			clone(this, 1, &(am2_P_WeldShift[1]), NULL);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 191);
			clone(this, 1, &(am2_P_WeldShift[2]), NULL);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 192);
			clone(this, 1, am2_pZoneMixCheck, NULL);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 193);
			clone(this, 1, am2_pEmptyCarrierManagement, NULL);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 197);
			am2_vflptrInputData = OpenFilePtr(am_model.$sys, "base.arc/DATA/sorting.dat", "r");
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 198);
			this->attribute->am2_Ai_pi = 1;
			EntityChanged(0x00000040);
		}
		{
			if (isFileValid(am2_vflptrInputData, 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 199);
				rflag = readFile(am2_vflptrInputData->fp, "\n", &st1, NULL);
				SetFileAtEof(am2_vflptrInputData, EOF, rflag);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 200);
			while (this->attribute->am2_Ai_pi <= 12) {
				{
					if (isFileValid(am2_vflptrInputData, 1)) {
						int rflag;
						static ReadRef st1;
						static ReadRef st2;
						static ReadRef st3;
						static ReadRef st4;

						setupReadRef(&st1, 0, am_model.am_vsSortingRule$var, &am2_vsSortingRule[ValidIndex("am_model.am_vsSortingRule", this->attribute->am2_Ai_pi, 12)], NULL, -1, FALSE);
						setupReadRef(&st2, 0, am_model.am_viZoneSortPriority$var, &am2_viZoneSortPriority[ValidIndex("am_model.am_viZoneSortPriority", this->attribute->am2_Ai_pi, 12)], NULL, -1, FALSE);
						setupReadRef(&st3, 0, am_model.am_vsPullingRule$var, &am2_vsPullingRule[ValidIndex("am_model.am_vsPullingRule", this->attribute->am2_Ai_pi, 12)], NULL, -1, FALSE);
						setupReadRef(&st4, 0, am_model.am_viZonePullPriority$var, &am2_viZonePullPriority[ValidIndex("am_model.am_viZonePullPriority", this->attribute->am2_Ai_pi, 12)], NULL, -1, FALSE);
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 201);
						rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, &st2, &st3, &st4, NULL);
						SetFileAtEof(am2_vflptrInputData, EOF, rflag);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 202);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 206);
			am2_vflptrInputData = OpenFilePtr(am_model.$sys, "base.arc/DATA/stdata.dat", "r");
		}
		{
			if (isFileValid(am2_vflptrInputData, 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 207);
				rflag = readFile(am2_vflptrInputData->fp, "\n", &st1, NULL);
				SetFileAtEof(am2_vflptrInputData, EOF, rflag);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 208);
			this->attribute->am2_Ai_pi = 1;
			EntityChanged(0x00000040);
		}
		{
			if (isFileValid(am2_vflptrInputData, 1)) {
				int rflag;
				static ReadRef st1;
				static ReadRef st2;

				setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
				setupReadRef(&st2, 0, am_model.am_vsStyle$var, &am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 209);
				rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, &st2, NULL);
				SetFileAtEof(am2_vflptrInputData, EOF, rflag);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 210);
			while (StringCompare(am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)], "") != 0) {
				{
					if (isFileValid(am2_vflptrInputData, 1)) {
						int rflag;
						static ReadRef st1;
						static ReadRef st2;
						static ReadRef st3;
						static ReadRef st4;
						static ReadRef st5;
						static ReadRef st6;
						static ReadRef st7;
						static ReadRef st8;
						static ReadRef st9;
						static ReadRef st10;
						static ReadRef st11;
						static ReadRef st12;
						static ReadRef st13;
						static ReadRef st14;
						static ReadRef st15;
						static ReadRef st16;
						static ReadRef st17;
						static ReadRef st18;
						static ReadRef st19;

						setupReadRef(&st1, 0, am_model.am_viInitQty$var, &am2_viInitQty[ValidIndex("am_model.am_viInitQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st2, 0, am_model.am_viTriggerQty$var, &am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st3, 0, am_model.am_viSTRunOutQty$var, &am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st4, 0, am_model.am_viMinShotSize$var, &am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_pi, 20)][1], NULL, -1, FALSE);
						setupReadRef(&st5, 0, am_model.am_viMinShotSize$var, &am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_pi, 20)][2], NULL, -1, FALSE);
						setupReadRef(&st6, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][1][1], NULL, -1, FALSE);
						setupReadRef(&st7, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][1][2], NULL, -1, FALSE);
						setupReadRef(&st8, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][2][1], NULL, -1, FALSE);
						setupReadRef(&st9, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][2][2], NULL, -1, FALSE);
						setupReadRef(&st10, 0, am_model.am_Vr_Scrap$var, &am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st11, 0, am_model.am_viZonePref$var, &am2_viZonePref[1][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st12, 0, am_model.am_viZonePref$var, &am2_viZonePref[2][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st13, 0, am_model.am_viZonePref$var, &am2_viZonePref[3][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st14, 0, am_model.am_viCarrierQty$var, &am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						setupReadRef(&st15, 0, am_model.am_vlt_LoadType$var, &am2_vlt_LoadType[ValidIndex("am_model.am_vlt_LoadType", this->attribute->am2_Ai_pi, 30)], str2LoadTypePtr, -1, FALSE);
						setupReadRef(&st16, 0, am_model.am_vclr_LoadColor$var, &am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", this->attribute->am2_Ai_pi, 30)], str2Color, -1, FALSE);
						setupReadRef(&st17, 0, am_model.am_Vi_StampingPriority$var, &am2_Vi_StampingPriority[ValidIndex("am_model.am_Vi_StampingPriority", this->attribute->am2_Ai_pi, 20)], NULL, -1, FALSE);
						setupReadRef(&st18, 0, am_model.am_viSpoPress$var, &am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", this->attribute->am2_Ai_pi, 20)], NULL, -1, FALSE);
						setupReadRef(&st19, 0, am_model.am_viVariation$var, &am2_viVariation[ValidIndex("am_model.am_viVariation", this->attribute->am2_Ai_pi, 20)], NULL, -1, FALSE);
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 211);
						rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, &st2, &st3, &st4, &st5, &st6, &st7, &st8, &st9, &st10, &st11, &st12, &st13, &st14, &st15, &st16, &st17, &st18, &st19, NULL);
						SetFileAtEof(am2_vflptrInputData, EOF, rflag);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 214);
					am2_viVariationCount += am2_viVariation[ValidIndex("am_model.am_viVariation", this->attribute->am2_Ai_pi, 20)];
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 215);
					{
						char* pArg1 = am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)];

						updatelabel(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 216);
					{
						char* pArg1 = "TrigQty->";
						char* pArg2 = " ";
						int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_pi, 30)];
						char* pArg4 = " ";
						char* pArg5 = "RunQty->";
						char* pArg6 = " ";
						int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_pi, 30)];
						char* pArg8 = " ";
						char* pArg9 = "Inv->";
						char* pArg10 = " ";
						int32 pArg11 = am2_viInitQty[ValidIndex("am_model.am_viInitQty", this->attribute->am2_Ai_pi, 30)];

						updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 217);
					if (am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", this->attribute->am2_Ai_pi, 20)] == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 218);
							LabSetColor(ValidPtr(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), 29, label*), 4);
							EntityChanged(0x00000840);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 219);
							LabSetColor(ValidPtr(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), 29, label*), 4);
							EntityChanged(0x00000840);
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 222);
							LabSetColor(ValidPtr(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), 29, label*), 1);
							EntityChanged(0x00000840);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 223);
							LabSetColor(ValidPtr(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), 29, label*), 1);
							EntityChanged(0x00000840);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 225);
					clone(this, 1, &(am2_P_Legend[ValidIndex("am_model.am_P_Legend", this->attribute->am2_Ai_pi, 30)]), am2_L_invis);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 226);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
				{
					if (isFileValid(am2_vflptrInputData, 1)) {
						int rflag;
						static ReadRef st1;
						static ReadRef st2;

						setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
						setupReadRef(&st2, 0, am_model.am_vsStyle$var, &am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 227);
						rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, &st2, NULL);
						SetFileAtEof(am2_vflptrInputData, EOF, rflag);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 229);
			SetString(&am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)], NULL);
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 230);
			am2_Vi_NumType = this->attribute->am2_Ai_pi - 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 234);
			this->attribute->am2_Ai_pi = 21;
			EntityChanged(0x00000040);
		}
		{
			int rflag;
			static ReadRef st1;

			setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 235);
			rflag = readFile(ffp_base_arc_DATA_variations_dat->fp, "\n", &st1, NULL);
			SetFileAtEof(ffp_base_arc_DATA_variations_dat, EOF, rflag);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 236);
			while (this->attribute->am2_Ai_pi <= 20 + am2_viVariationCount) {
				{
					int rflag;
					static ReadRef st1;
					static ReadRef st2;
					static ReadRef st3;
					static ReadRef st4;
					static ReadRef st5;
					static ReadRef st6;
					static ReadRef st7;
					static ReadRef st8;
					static ReadRef st9;
					static ReadRef st10;
					static ReadRef st11;
					static ReadRef st12;
					static ReadRef st13;
					static ReadRef st14;
					static ReadRef st15;
					static ReadRef st16;
					static ReadRef st17;
					static ReadRef st18;
					static ReadRef st19;

					setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					setupReadRef(&st2, 0, am_model.am_viModelV$var, &am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st3, 0, am_model.am_vsStyle$var, &am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st4, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					setupReadRef(&st5, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
					setupReadRef(&st6, 0, am_model.am_viInitQty$var, &am2_viInitQty[ValidIndex("am_model.am_viInitQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st7, 0, am_model.am_viTriggerQty$var, &am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st8, 0, am_model.am_viSTRunOutQty$var, &am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st9, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][1][1], NULL, -1, FALSE);
					setupReadRef(&st10, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][1][2], NULL, -1, FALSE);
					setupReadRef(&st11, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][2][1], NULL, -1, FALSE);
					setupReadRef(&st12, 0, am_model.am_vrStyleCT$var, &am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][2][2], NULL, -1, FALSE);
					setupReadRef(&st13, 0, am_model.am_Vr_Scrap$var, &am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st14, 0, am_model.am_viZonePref$var, &am2_viZonePref[1][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st15, 0, am_model.am_viZonePref$var, &am2_viZonePref[2][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st16, 0, am_model.am_viZonePref$var, &am2_viZonePref[3][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st17, 0, am_model.am_viCarrierQty$var, &am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_pi, 30)], NULL, -1, FALSE);
					setupReadRef(&st18, 0, am_model.am_vlt_LoadType$var, &am2_vlt_LoadType[ValidIndex("am_model.am_vlt_LoadType", this->attribute->am2_Ai_pi, 30)], str2LoadTypePtr, -1, FALSE);
					setupReadRef(&st19, 0, am_model.am_vclr_LoadColor$var, &am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", this->attribute->am2_Ai_pi, 30)], str2Color, -1, FALSE);
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 239);
					rflag = readFile(ffp_base_arc_DATA_variations_dat->fp, "\t", &st1, &st2, &st3, &st4, &st5, &st6, &st7, &st8, &st9, &st10, &st11, &st12, &st13, &st14, &st15, &st16, &st17, &st18, &st19, NULL);
					SetFileAtEof(ffp_base_arc_DATA_variations_dat, EOF, rflag);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 243);
					{
						char* pArg1 = am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)];

						updatelabel(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 244);
					{
						char* pArg1 = "TrigQty->";
						char* pArg2 = " ";
						int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_pi, 30)];
						char* pArg4 = " ";
						char* pArg5 = "RunQty->";
						char* pArg6 = " ";
						int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_pi, 30)];
						char* pArg8 = " ";
						char* pArg9 = "Inv->";
						char* pArg10 = " ";
						int32 pArg11 = am2_viInitQty[ValidIndex("am_model.am_viInitQty", this->attribute->am2_Ai_pi, 30)];

						updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 245);
					if (am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_pi, 30)], 20)] == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 246);
							LabSetColor(ValidPtr(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), 29, label*), 4);
							EntityChanged(0x00000840);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 247);
							LabSetColor(ValidPtr(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), 29, label*), 4);
							EntityChanged(0x00000840);
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 250);
							LabSetColor(ValidPtr(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_pi, 30)]), 29, label*), 1);
							EntityChanged(0x00000840);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 251);
							LabSetColor(ValidPtr(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", this->attribute->am2_Ai_pi, 30)]), 29, label*), 1);
							EntityChanged(0x00000840);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 253);
					clone(this, 1, &(am2_P_Legend[ValidIndex("am_model.am_P_Legend", this->attribute->am2_Ai_pi, 30)]), am2_L_invis);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 254);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 258);
			am2_vflptrInputData = OpenFilePtr(am_model.$sys, "base.arc/DATA/otherparts.dat", "r");
		}
		{
			if (isFileValid(am2_vflptrInputData, 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 259);
				rflag = readFile(am2_vflptrInputData->fp, "\n", &st1, NULL);
				SetFileAtEof(am2_vflptrInputData, EOF, rflag);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 260);
			this->attribute->am2_Ai_pi = 1;
			EntityChanged(0x00000040);
		}
		{
			if (isFileValid(am2_vflptrInputData, 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 0, am_model.am_viModelOther$var, &am2_viModelOther[ValidIndex("am_model.am_viModelOther", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 261);
				rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, NULL);
				SetFileAtEof(am2_vflptrInputData, EOF, rflag);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 262);
			while (am2_viModelOther[ValidIndex("am_model.am_viModelOther", this->attribute->am2_Ai_pi, 50)] != 0) {
				{
					if (isFileValid(am2_vflptrInputData, 1)) {
						int rflag;
						static ReadRef st1;
						static ReadRef st2;
						static ReadRef st3;
						static ReadRef st4;
						static ReadRef st5;
						static ReadRef st6;
						static ReadRef st7;
						static ReadRef st8;
						static ReadRef st9;
						static ReadRef st10;
						static ReadRef st11;

						setupReadRef(&st1, 0, am_model.am_vsStyle2$var, &am2_vsStyle2[ValidIndex("am_model.am_vsStyle2", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						setupReadRef(&st2, 0, am_model.am_viInitQty2$var, &am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						setupReadRef(&st3, 0, am_model.am_viTriggerQty2$var, &am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						setupReadRef(&st4, 0, am_model.am_viSTRunOutQty2$var, &am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						setupReadRef(&st5, 0, am_model.am_viMinShotSize2$var, &am2_viMinShotSize2[ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_pi, 50)][1], NULL, -1, FALSE);
						setupReadRef(&st6, 0, am_model.am_viMinShotSize2$var, &am2_viMinShotSize2[ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_pi, 50)][2], NULL, -1, FALSE);
						setupReadRef(&st7, 0, am_model.am_vrStyleCT2$var, &am2_vrStyleCT2[ValidIndex("am_model.am_vrStyleCT2", this->attribute->am2_Ai_pi, 50)][1], NULL, -1, FALSE);
						setupReadRef(&st8, 0, am_model.am_vrStyleCT2$var, &am2_vrStyleCT2[ValidIndex("am_model.am_vrStyleCT2", this->attribute->am2_Ai_pi, 50)][2], NULL, -1, FALSE);
						setupReadRef(&st9, 0, am_model.am_Vr_Scrap2$var, &am2_Vr_Scrap2[ValidIndex("am_model.am_Vr_Scrap2", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						setupReadRef(&st10, 0, am_model.am_Vi_StampingPriority2$var, &am2_Vi_StampingPriority2[ValidIndex("am_model.am_Vi_StampingPriority2", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						setupReadRef(&st11, 0, am_model.am_viOtherPress$var, &am2_viOtherPress[ValidIndex("am_model.am_viOtherPress", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 263);
						rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, &st2, &st3, &st4, &st5, &st6, &st7, &st8, &st9, &st10, &st11, NULL);
						SetFileAtEof(am2_vflptrInputData, EOF, rflag);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 265);
					{
						char* pArg1 = am2_vsStyle2[ValidIndex("am_model.am_vsStyle2", this->attribute->am2_Ai_pi, 50)];

						updatelabel(&(am2_Lb_PartNames[ValidIndex("am_model.am_Lb_PartNames", this->attribute->am2_Ai_pi, 50)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 266);
					{
						char* pArg1 = "TrigQty->";
						char* pArg2 = " ";
						int32 pArg3 = am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", this->attribute->am2_Ai_pi, 50)];
						char* pArg4 = " ";
						char* pArg5 = "RunQty->";
						char* pArg6 = " ";
						int32 pArg7 = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", this->attribute->am2_Ai_pi, 50)];
						char* pArg8 = " ";
						char* pArg9 = "Inv->";
						char* pArg10 = " ";
						int32 pArg11 = am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_pi, 50)];

						updatelabel(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", this->attribute->am2_Ai_pi, 50)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 268);
					if (am2_viOtherPress[ValidIndex("am_model.am_viOtherPress", this->attribute->am2_Ai_pi, 50)] == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 269);
							LabSetColor(ValidPtr(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", this->attribute->am2_Ai_pi, 50)]), 29, label*), 4);
							EntityChanged(0x00000840);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 270);
							LabSetColor(ValidPtr(&(am2_Lb_PartNames[ValidIndex("am_model.am_Lb_PartNames", this->attribute->am2_Ai_pi, 50)]), 29, label*), 4);
							EntityChanged(0x00000840);
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 273);
							LabSetColor(ValidPtr(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", this->attribute->am2_Ai_pi, 50)]), 29, label*), 1);
							EntityChanged(0x00000840);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 274);
							LabSetColor(ValidPtr(&(am2_Lb_PartNames[ValidIndex("am_model.am_Lb_PartNames", this->attribute->am2_Ai_pi, 50)]), 29, label*), 1);
							EntityChanged(0x00000840);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 276);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
				{
					if (isFileValid(am2_vflptrInputData, 1)) {
						int rflag;
						static ReadRef st1;

						setupReadRef(&st1, 0, am_model.am_viModelOther$var, &am2_viModelOther[ValidIndex("am_model.am_viModelOther", this->attribute->am2_Ai_pi, 50)], NULL, -1, FALSE);
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 277);
						rflag = readFile(am2_vflptrInputData->fp, "\t", &st1, NULL);
						SetFileAtEof(am2_vflptrInputData, EOF, rflag);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 279);
			am2_vi_NumType2 = this->attribute->am2_Ai_pi - 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 281);
			if (am2_Vi_Autostat == 1) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 282);
					am2_viABayTrigger = am2_viABayTriggerAS;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 283);
					am2_viCLineEmptyTrigger = am2_viCLineEmptyTriggerAS;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 285);
					this->attribute->am2_Ai_pi = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 286);
					while (this->attribute->am2_Ai_pi <= 30) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 287);
							am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][1][2] = am2_vrBLineSlowCT;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 288);
							am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_pi, 30)][2][2] = am2_vrCLineSlowCT;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 289);
							this->attribute->am2_Ai_pi += 1;
							EntityChanged(0x00000040);
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 293);
			RscSetCapacity(am2_rBLineTravel, LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B27, -9999), 38, simloc*)) + LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B28, -9999), 38, simloc*)));
			EntityChanged(0x00020000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 294);
			CntSetContents(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*), am2_viReqVeh, this);
			EntityChanged(0x00000010);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 295);
			{
				int32 tempint = order(10000, am2_O_VehicleStart, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_O_VehicleStart, NULL, NULL);	/* Place a backorder */
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 297);
			Fsetpathtime(VTGetQualifier(am_model.am_kLift.am_kLift, 1, -9999), "ConfigUp", "ConfigDown", am2_vrBPressLiftDown);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 298);
			Fsetpathtime(VTGetQualifier(am_model.am_kLift.am_kLift, 1, -9999), "ConfigDown", "ConfigUp", am2_vrBPressLiftUp);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 302);
			if (am2_viInitializeSPO == 1) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 303);
					this->attribute->am2_Ai_pi = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 304);
					while (this->attribute->am2_Ai_pi <= 30) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 305);
							if (StringCompare(am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)], NULL) != 0) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 306);
									am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)] = am2_viInitQty[ValidIndex("am_model.am_viInitQty", this->attribute->am2_Ai_pi, 30)];
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 307);
									SetString(&this->attribute->am2_As_Type, am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_pi, 30)]);
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 308);
									this->attribute->am2_Ai_Type = this->attribute->am2_Ai_pi;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 309);
									if (this->attribute->am2_Ai_pi <= 20) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 310);
											this->attribute->am2_Ai_Press = am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", this->attribute->am2_Ai_pi, 20)];
											EntityChanged(0x00000040);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 311);
											LdSetType(this, am2_vlt_LoadType[ValidIndex("am_model.am_vlt_LoadType", this->attribute->am2_Ai_pi, 30)]);
											EntityChanged(0x00000040);
										}
									}
									else {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 314);
											this->attribute->am2_Ai_Press = am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_pi, 30)], 20)];
											EntityChanged(0x00000040);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 315);
											LdSetType(this, am2_vlt_LoadType[ValidIndex("am_model.am_vlt_LoadType", am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_pi, 30)], 30)]);
											EntityChanged(0x00000040);
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 317);
									this->attribute->am2_Ai_Seq = 99999;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 318);
									LdSetColor(this, am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", this->attribute->am2_Ai_pi, 30)]);
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 320);
									while (am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)] > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 321);
											if (am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)] >= am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_pi, 30)]) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 322);
													this->attribute->am2_Ai_CarQty = am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_pi, 30)];
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 323);
													am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)] -= am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_pi, 30)];
													EntityChanged(0x01000000);
												}
											}
											else {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 326);
													this->attribute->am2_Ai_CarQty = am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)];
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 327);
													am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)] -= am2_viInitRunQty[ValidIndex("am_model.am_viInitRunQty", this->attribute->am2_Ai_pi, 30)];
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 329);
											{
												int result = inccount(&(am2_C_CarrierCount[1]), 1, this, pInit_arriving, Step 2, am_localargs);
												if (result != Continue) return result;
Label2: ; /* Step 2 */
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 330);
											{
												int result = deccount(&(am2_C_CarrierCount[2]), 1, this, pInit_arriving, Step 3, am_localargs);
												if (result != Continue) return result;
Label3: ; /* Step 3 */
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 331);
											if (transformLoad(TX_LOAD_SCALE, 1.0, 1.0, this->attribute->am2_Ai_CarQty, 0, 0, 1, 0.0, this, this, pInit_arriving, Step 4, am_localargs) == Delayed)
												return Delayed;
Label4: ; /* Step 4 */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 332);
											clone(this, 1, am2_pCarrierLoad, NULL);
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 335);
							this->attribute->am2_Ai_pi += 1;
							EntityChanged(0x00000040);
						}
					}
				}
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 338);
				if (am2_viInitializeSPO == 2) {
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 353);
			pushppa(this, pInit_arriving, Step 5, am_localargs);
			pushppa(this, am2_sAttributeStrip, Step 1, NULL);
			return Continue;
Label5: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 354);
			this->attribute->am2_Ai_Seq = 99999;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 355);
			this->attribute->am2_Ai_Line = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 356);
			this->attribute->am2_Ai_Status = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 357);
			clone(this, CntGetCurConts(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*)), am2_pEmptyCarrierReturn, am2_L_invis);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 359);
			clone(this, 1, am2_pWarmUp, NULL);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 361);
			this->attribute->am2_Ai_Zone = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 362);
			am2_vldPrevHolder = this;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor, 363);
			return waitorder(am2_oHold, this, pInit_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pInit", pInit_arriving, localactor);
	return retval;
} /* end of pInit_arriving */

static int32
P_Downtime_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.P_Downtime", localactor);
	AMDebuggerParams("base.P_Downtime", P_Downtime_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 369);
			return waitorder(am2_olWarmUp, this, P_Downtime_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 371);
			while (1 == 1) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 372);
					am2_Vr_MTBFActual[ValidIndex("am_model.am_Vr_MTBFActual", CurProcIndex(), 5)] = exponential(ValidPtr(am2_Vstrm_DT[ValidIndex("am_model.am_Vstrm_DT", CurProcIndex(), 10)], 60, rnstream*), am2_Vr_MTBF[ValidIndex("am_model.am_Vr_MTBF", CurProcIndex(), 5)]);
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 373);
					if (getrsrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]), 1, this, P_Downtime_arriving, Step 3, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 374);
					if (waitfor(ToModelTime(am2_Vr_MTBFActual[ValidIndex("am_model.am_Vr_MTBFActual", CurProcIndex(), 5)], UNITMINUTES), this, P_Downtime_arriving, Step 4, am_localargs) == Delayed)
						return Delayed;
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 375);
					tabulate(&(am2_T_MTBF[ValidIndex("am_model.am_T_MTBF", CurProcIndex(), 5)]), am2_Vr_MTBFActual[ValidIndex("am_model.am_Vr_MTBFActual", CurProcIndex(), 5)]);	/* Tabulate the value */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 376);
					freersrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]), 1, this);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 378);
					if (CurProcIndex() < 5) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 379);
							downrsrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)]);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 380);
							MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)), str2StatePtr("Down"), this);
							EntityChanged(0x01020000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 381);
							{
								char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*))), am_model.$sys);

								updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", CurProcIndex(), 4)]), "%s", pArg1);
							}
						}
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 383);
						conv_motor_down(am_model.am_pf_ohc.am_M_chain1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 384);
					am2_Vi_Down[ValidIndex("am_model.am_Vi_Down", CurProcIndex(), 10)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 386);
					am2_Vr_MTTRActual[ValidIndex("am_model.am_Vr_MTTRActual", CurProcIndex(), 5)] = exponential(ValidPtr(am2_Vstrm_DT[ValidIndex("am_model.am_Vstrm_DT", CurProcIndex() + 5, 10)], 60, rnstream*), am2_Vr_MTTR[ValidIndex("am_model.am_Vr_MTTR", CurProcIndex(), 5)]);
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 387);
					if (getrsrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]), 1, this, P_Downtime_arriving, Step 5, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label5: ; /* Step 5 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 388);
					if (waitfor(ToModelTime(am2_Vr_MTTRActual[ValidIndex("am_model.am_Vr_MTTRActual", CurProcIndex(), 5)], UNITMINUTES), this, P_Downtime_arriving, Step 6, am_localargs) == Delayed)
						return Delayed;
Label6: ; /* Step 6 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 389);
					tabulate(&(am2_T_MTTR[ValidIndex("am_model.am_T_MTTR", CurProcIndex(), 5)]), am2_Vr_MTTRActual[ValidIndex("am_model.am_Vr_MTTRActual", CurProcIndex(), 5)]);	/* Tabulate the value */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 390);
					freersrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]), 1, this);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 392);
					if (CurProcIndex() < 5) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 393);
							uprsrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)]);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 395);
							if (CurProcIndex() == 1) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 396);
									if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[1]), 40, ordlist*)) + OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[2]), 40, ordlist*)) > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 397);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[1], 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x01020000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 398);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x00020000);
										}
									}
									else {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 401);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[1], 50, resource*)), str2StatePtr("Production"), this);
											EntityChanged(0x01020000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 402);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)), str2StatePtr("Production"), this);
											EntityChanged(0x00020000);
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 405);
							if (CurProcIndex() == 2) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 406);
									if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[3]), 40, ordlist*)) + OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[4]), 40, ordlist*)) > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 407);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[2], 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x01020000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 408);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x00020000);
										}
									}
									else {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 411);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[2], 50, resource*)), str2StatePtr("Production"), this);
											EntityChanged(0x01020000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 412);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)), str2StatePtr("Production"), this);
											EntityChanged(0x00020000);
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 415);
							if (CurProcIndex() == 3 || CurProcIndex() == 4) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 416);
									if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][2][ValidIndex("am_model.am_vlocptrSTLoad", CurProcIndex() - 2, 2)], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][2][ValidIndex("am_model.am_vlocptrSTLoad", CurProcIndex() - 2, 2)], 38, simloc*)) > 0 && LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][3][ValidIndex("am_model.am_vlocptrSTLoad", CurProcIndex() - 2, 2)], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][3][ValidIndex("am_model.am_vlocptrSTLoad", CurProcIndex() - 2, 2)], 38, simloc*)) < 2) {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 417);
										MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)), str2StatePtr("NoCarriers"), this);
										EntityChanged(0x01020000);
									}
									else {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 418);
										if (am2_viBlocked[ValidIndex("am_model.am_viBlocked", CurProcIndex() - 2, 2)][1] + am2_viBlocked[ValidIndex("am_model.am_viBlocked", CurProcIndex() - 2, 2)][2] > 0) {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 419);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)), str2StatePtr("Blocked"), this);
											EntityChanged(0x01020000);
										}
										else {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 420);
											if (RscGetCurConts(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)) == 0) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 421);
												MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)), str2StatePtr("NoScheduledRuns"), this);
												EntityChanged(0x01020000);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 422);
												MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)), str2StatePtr("Production"), this);
												EntityChanged(0x01020000);
											}
										}
									}
								}
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 424);
								MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*)), str2StatePtr("Production"), this);
								EntityChanged(0x01020000);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 425);
							{
								char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", CurProcIndex(), 5)], 50, resource*))), am_model.$sys);

								updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", CurProcIndex(), 4)]), "%s", pArg1);
							}
						}
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 427);
						conv_motor_up(am_model.am_pf_ohc.am_M_chain1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor, 428);
					am2_Vi_Down[ValidIndex("am_model.am_Vi_Down", CurProcIndex(), 10)] = 1;
					EntityChanged(0x01000000);
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.P_Downtime", P_Downtime_arriving, localactor);
	return retval;
} /* end of P_Downtime_arriving */


typedef struct {
	int32 value;
} Nextof0;

static Nextof0 List0[] = {
	1,
	2
};

static int32
nextofFunc0(load* this)
{
	static int ind = 1;

	tprintf(tfp, "In nextof\n");
	ind = (ind+1) % 2;
	return List0[ind].value;
}


typedef struct {
	int32 value;
} Nextof1;

static Nextof1 List1[] = {
	1,
	2
};

static int32
nextofFunc1(load* this)
{
	static int ind = 1;

	tprintf(tfp, "In nextof\n");
	ind = (ind+1) % 2;
	return List1[ind].value;
}


static double
Funcl0(load* this)
{
	return am2_Vr_Scrap2[ValidIndex("am_model.am_Vr_Scrap2", this->attribute->am2_Ai_Type2, 50)];
}


static double
Funcl1(load* this)
{
	return 1 - am2_Vr_Scrap2[ValidIndex("am_model.am_Vr_Scrap2", this->attribute->am2_Ai_Type2, 50)];
}


typedef struct {
	double (*freq)(load*);
	int32 value;
} Oneof2;

static Oneof2 List2[] = {
	{ Funcl0, 1},
	{ Funcl1, 2}
};

static int32
oneofFunc2(load* this)
{
	size_t ind;
	size_t i;
	static Real freq[2];

	tprintf(tfp, "In oneof\n");
	for (i = 0; i < 2; i++)
		freq[i] = List2[i].freq(this);
	ind = oneof_n(ValidPtr(am2_stream76, 60, rnstream*), 2, freq);
	return List2[ind].value;
}

static int32
OrderCondFunc0(load* this)
{
	if (this->attribute->am2_Ai_NonSPO == 1)
		return TRUE;
	return FALSE;
}

static int32
OrderCondFunc1(load* this)
{
	if (this->attribute->am2_Ai_NonSPO == 1)
		return TRUE;
	return FALSE;
}

static int32
OrderCondFunc2(load* this)
{
	if (this->attribute->am2_Ai_NonSPO == 1)
		return TRUE;
	return FALSE;
}

static int32
pSTRunControl_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pSTRunControl", localactor);
	AMDebuggerParams("base.pSTRunControl", pSTRunControl_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	case Step 7: goto Label7;
	case Step 8: goto Label8;
	case Step 9: goto Label9;
	case Step 10: goto Label10;
	case Step 11: goto Label11;
	case Step 12: goto Label12;
	case Step 13: goto Label13;
	case Step 14: goto Label14;
	case Step 15: goto Label15;
	case Step 16: goto Label16;
	case Step 17: goto Label17;
	case Step 18: goto Label18;
	case Step 19: goto Label19;
	case Step 20: goto Label20;
	case Step 21: goto Label21;
	case Step 22: goto Label22;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 434);
			if (ASIclock <= ToModelTime(86400, UNITSECONDS)) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 434);
				return waitorder(am2_olWarmUp, this, pSTRunControl_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
				if (!this->inLeaveProc && this->nextproc) {
					retval = Continue;
					goto LabelRet;
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 436);
			if (this->attribute->am2_Ai_NonSPO == 1) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 437);
				this->attribute->am2_Ai_Type = am2_viModelOther[ValidIndex("am_model.am_viModelOther", this->attribute->am2_Ai_Type2, 50)];
				EntityChanged(0x00000040);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 439);
			this->attribute->am2_Ai_StampingPriority = am2_Vi_StampingPriority[ValidIndex("am_model.am_Vi_StampingPriority", this->attribute->am2_Ai_Type, 20)];
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 440);
			if (this->attribute->am2_Ai_NonSPO == 1 && this->attribute->am2_Ai_StampingPriority == 0) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 441);
				this->attribute->am2_Ai_StampingPriority = am2_Vi_StampingPriority2[ValidIndex("am_model.am_Vi_StampingPriority2", this->attribute->am2_Ai_Type2, 50)];
				EntityChanged(0x00000040);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 443);
			LdSetPriority(this, this->attribute->am2_Ai_StampingPriority);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 444);
			if (this->attribute->am2_Ai_NonSPO == 0) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 444);
				am2_Vi_ScheduleCount[1][ValidIndex("am_model.am_Vi_ScheduleCount", this->attribute->am2_Ai_Press, 2)] += 1;
				EntityChanged(0x01000000);
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 445);
				if (this->attribute->am2_Ai_NonSPO == 1) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 445);
					am2_Vi_ScheduleCount[2][ValidIndex("am_model.am_Vi_ScheduleCount", this->attribute->am2_Ai_Press, 2)] += 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 446);
			return waitorder(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), this, pSTRunControl_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 448);
			ListRemoveFirstMatch(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", this->attribute->am2_Ai_Press, 2)], this);	/* remove first match from list */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 450);
			am2_Vi_LotNo += 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 451);
			this->attribute->am2_Ai_LotNo = am2_Vi_LotNo;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 453);
			if (this->attribute->am2_Ai_NonSPO == 0) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 453);
				am2_Vi_ScheduleCount[1][ValidIndex("am_model.am_Vi_ScheduleCount", this->attribute->am2_Ai_Press, 2)] -= 1;
				EntityChanged(0x01000000);
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 454);
				if (this->attribute->am2_Ai_NonSPO == 1) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 454);
					am2_Vi_ScheduleCount[2][ValidIndex("am_model.am_Vi_ScheduleCount", this->attribute->am2_Ai_Press, 2)] -= 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 456);
			if (transformLoad(TX_LOAD_SCALE, 1.0, 1.0, 1, 0, 0, 1, 0.0, this, this, pSTRunControl_arriving, Step 4, am_localargs) == Delayed)
				return Delayed;
Label4: ; /* Step 4 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 457);
			if (this->attribute->am2_Ai_NonSPO == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 458);
					am2_viSTRunGen[ValidIndex("am_model.am_viSTRunGen", this->attribute->am2_Ai_Press, 2)] = 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 459);
					am2_viKickOutQty[1] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 460);
					order(-1, am2_oEmptyRecirc, am2_die, NULL); /* Place an order */
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 463);
			if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 2) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 464);
				MonSetState(am2_vsmPressCondition, str2StatePtr("TwoPressRunningSpo"), this);
				EntityChanged(0x00000100);
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 465);
				if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 1) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 466);
					MonSetState(am2_vsmPressCondition, str2StatePtr("OnePressRunningSpo"), this);
					EntityChanged(0x00000100);
				}
				else {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 467);
					if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 468);
						MonSetState(am2_vsmPressCondition, str2StatePtr("NoPressRunningSpo"), this);
						EntityChanged(0x00000100);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 470);
			if (this->attribute->am2_Ai_NonSPO == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 471);
					{
						char* pArg1 = am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_Type, 30)];

						updatelabel(&(am2_lblPart[ValidIndex("am_model.am_lblPart", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 472);
					{
						double pArg1 = am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_vrStyleCT", am2_viSTRunGen[1] + am2_viSTRunGen[2], 2)];

						updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
					}
				}
			}
			else {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 475);
					{
						char* pArg1 = am2_vsStyle2[ValidIndex("am_model.am_vsStyle2", this->attribute->am2_Ai_Type2, 50)];

						updatelabel(&(am2_lblPart[ValidIndex("am_model.am_lblPart", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 476);
					{
						double pArg1 = am2_vrStyleCT2[ValidIndex("am_model.am_vrStyleCT2", this->attribute->am2_Ai_Type2, 50)][ValidIndex("am_model.am_vrStyleCT2", this->attribute->am2_Ai_Press, 2)];

						updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 479);
			if (getrsrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 1, this, pSTRunControl_arriving, Step 5, am_localargs) == Delayed)
				return Delayed;  /* go wait for resource */
Label5: ; /* Step 5 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 480);
			MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("DieChange"), this);
			EntityChanged(0x01020040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 481);
			{
				char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*))), am_model.$sys);

				updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", 2 + this->attribute->am2_Ai_Press, 4)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 482);
			if (waitfor(ToModelTime(am2_vrDieChangeTime[ValidIndex("am_model.am_vrDieChangeTime", this->attribute->am2_Ai_Press, 2)], UNITMINUTES), this, pSTRunControl_arriving, Step 6, am_localargs) == Delayed)
				return Delayed;
Label6: ; /* Step 6 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 483);
			am2_Vi_Changeover[ValidIndex("am_model.am_Vi_Changeover", this->attribute->am2_Ai_Press, 2)] += 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 484);
			freersrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 1, this);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 486);
			if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][2][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][2][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) > 0 && LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) < 2) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 487);
				MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("NoCarriers"), this);
				EntityChanged(0x01020040);
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 488);
				if (am2_viBlocked[ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_Press, 2)][1] + am2_viBlocked[ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_Press, 2)][2] > 0) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 489);
					MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("Blocked"), this);
					EntityChanged(0x01020040);
				}
				else {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 490);
					if (RscGetCurConts(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)) == 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 491);
						MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("NoScheduledRuns"), this);
						EntityChanged(0x01020040);
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 492);
						MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("Production"), this);
						EntityChanged(0x01020040);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 494);
			MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("Production"), this);
			EntityChanged(0x01020040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 495);
			{
				char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*))), am_model.$sys);

				updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", 2 + this->attribute->am2_Ai_Press, 4)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 506);
			order(-1, am2_volPartKill[1][1][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 507);
			order(-1, am2_volPartKill[1][2][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 508);
			order(-1, am2_volPartKill[2][1][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 509);
			order(-1, am2_volPartKill[2][2][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 520);
			pushppa(this, pSTRunControl_arriving, Step 7, am_localargs);
			pushppa(this, am2_sAttributeStrip, Step 1, NULL);
			return Continue;
Label7: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 522);
			if (this->attribute->am2_Ai_NonSPO == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 523);
					this->attribute->am2_Ai_LotQty = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)];
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 524);
					if (this->attribute->am2_Ai_LotQty < am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Type, 20)][ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Press, 2)]) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 525);
							if (am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Type, 20)][ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Press, 2)] % (am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_Type, 30)] * 2) == 0) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 526);
								this->attribute->am2_Ai_LotQty = am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Type, 20)][ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Press, 2)];
								EntityChanged(0x00000040);
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 528);
								this->attribute->am2_Ai_LotQty = am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Type, 20)][ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Press, 2)] + am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_Type, 30)] * 2 - (am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Type, 20)][ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Press, 2)] % (am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_Type, 30)] * 2));
								EntityChanged(0x00000040);
							}
						}
					}
				}
			}
			else {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 532);
					this->attribute->am2_Ai_LotQty = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", this->attribute->am2_Ai_Type2, 50)];
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 533);
					if (this->attribute->am2_Ai_LotQty < am2_viMinShotSize2[ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_Type2, 50)][ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_Press, 2)]) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 534);
						this->attribute->am2_Ai_LotQty = am2_viMinShotSize2[ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_Type2, 50)][ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_Press, 2)];
						EntityChanged(0x00000040);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 536);
			{
				int32 pArg1 = this->attribute->am2_Ai_LotQty;

				updatelabel(&(am2_lblShotSize[ValidIndex("am_model.am_lblShotSize", this->attribute->am2_Ai_Press, 2)]), "%d", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 538);
			am2_viVariationTracker[ValidIndex("am_model.am_viVariationTracker", this->attribute->am2_Ai_Press, 2)] = 0;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 539);
			if (this->attribute->am2_Ai_NonSPO == 0) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 539);
				this->attribute->am2_Ai_Type2 = this->attribute->am2_Ai_Type;
				EntityChanged(0x00000040);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 540);
			this->attribute->am2_Ai_Seq = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 541);
			if (getrsrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 1, this, pSTRunControl_arriving, Step 8, am_localargs) == Delayed)
				return Delayed;  /* go wait for resource */
Label8: ; /* Step 8 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 542);
			this->attribute->am2_At_TimeInSys = ASIclock;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 543);
			while (this->attribute->am2_Ai_Seq <= this->attribute->am2_Ai_LotQty && am2_Vi_RunTrigger[ValidIndex("am_model.am_Vi_RunTrigger", this->attribute->am2_Ai_Press, 2)] == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 545);
					if (this->attribute->am2_Ai_NonSPO == 0) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 546);
							if (am2_viVariationTracker[ValidIndex("am_model.am_viVariationTracker", this->attribute->am2_Ai_Press, 2)] < am2_viVariation[ValidIndex("am_model.am_viVariation", this->attribute->am2_Ai_Type2, 20)]) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 547);
									if (this->attribute->am2_Ai_Seq > am2_viVariationTarget[ValidIndex("am_model.am_viVariationTarget", this->attribute->am2_Ai_Press, 2)] || this->attribute->am2_Ai_Seq == 1) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 548);
											if (this->attribute->am2_Ai_Seq > 1) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 548);
												am2_viVariationTracker[ValidIndex("am_model.am_viVariationTracker", this->attribute->am2_Ai_Press, 2)] += 1;
												EntityChanged(0x01000000);
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 549);
											this->attribute->am2_Ai_x = 0;
											EntityChanged(0x00000040);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 550);
											if (am2_viVariationTracker[ValidIndex("am_model.am_viVariationTracker", this->attribute->am2_Ai_Press, 2)] == am2_viVariation[ValidIndex("am_model.am_viVariation", this->attribute->am2_Ai_Type2, 20)]) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 551);
												this->attribute->am2_Ai_Type = this->attribute->am2_Ai_Type2;
												EntityChanged(0x00000040);
											}
											else {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 553);
													this->attribute->am2_Ai_Type = 0;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 554);
													while (this->attribute->am2_Ai_Type < am2_viVariationCount) {
														{
															AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 555);
															this->attribute->am2_Ai_Type += 1;
															EntityChanged(0x00000040);
														}
														{
															AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 556);
															if (am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_Type + 20, 30)] == this->attribute->am2_Ai_Type2) {
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 557);
																	this->attribute->am2_Ai_x += 1;
																	EntityChanged(0x00000040);
																}
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 558);
																	if (this->attribute->am2_Ai_x > am2_viVariationTracker[ValidIndex("am_model.am_viVariationTracker", this->attribute->am2_Ai_Press, 2)]) {
																		{
																			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 559);
																			this->attribute->am2_Ai_Type = this->attribute->am2_Ai_Type + 20;
																			EntityChanged(0x00000040);
																		}
																		{
																			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 560);
																			am2_viVariationTarget[ValidIndex("am_model.am_viVariationTarget", this->attribute->am2_Ai_Press, 2)] = this->attribute->am2_Ai_Seq + am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)] - 1;
																			EntityChanged(0x01000000);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 565);
											{
												char* pArg1 = am2_vsStyle[ValidIndex("am_model.am_vsStyle", this->attribute->am2_Ai_Type, 30)];

												updatelabel(&(am2_lblPart[ValidIndex("am_model.am_lblPart", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 566);
											{
												double pArg1 = am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_vrStyleCT", am2_viSTRunGen[1] + am2_viSTRunGen[2], 2)];

												updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
											}
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 571);
					if (this->attribute->am2_Ai_NonSPO == 0) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 572);
							if (this->attribute->am2_Ai_Press == 1) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 573);
								this->attribute->am2_Ai_pi = nextofFunc0(this);
								EntityChanged(0x00000040);
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 574);
								this->attribute->am2_Ai_pi = nextofFunc1(this);
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 576);
							if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 2) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 577);
									if (waitfor(ToModelTime(am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Press, 2)][2], UNITSECONDS), this, pSTRunControl_arriving, Step 9, am_localargs) == Delayed)
										return Delayed;
Label9: ; /* Step 9 */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 578);
									{
										double pArg1 = am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Press, 2)][2];

										updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
									}
								}
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 580);
								if (this->attribute->am2_Ai_Press == 1 && am2_viSTRunGen[2] == 0) {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 581);
										if (waitfor(ToModelTime(am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Press, 2)][1], UNITSECONDS), this, pSTRunControl_arriving, Step 10, am_localargs) == Delayed)
											return Delayed;
Label10: ; /* Step 10 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 582);
										{
											double pArg1 = am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Press, 2)][1];

											updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
										}
									}
								}
								else {
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 584);
									if (this->attribute->am2_Ai_Press == 2 && am2_viSTRunGen[1] == 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 585);
											if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B28, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B23, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B22, -9999), 38, simloc*)) > 0) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 586);
													if (waitfor(ToModelTime(am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][2][2], UNITSECONDS), this, pSTRunControl_arriving, Step 11, am_localargs) == Delayed)
														return Delayed;
Label11: ; /* Step 11 */
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 587);
													{
														double pArg1 = am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][2][2];

														updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
													}
												}
											}
											else {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 590);
													if (waitfor(ToModelTime(am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][2][1], UNITSECONDS), this, pSTRunControl_arriving, Step 12, am_localargs) == Delayed)
														return Delayed;
Label12: ; /* Step 12 */
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 591);
													{
														double pArg1 = am2_vrStyleCT[ValidIndex("am_model.am_vrStyleCT", this->attribute->am2_Ai_Type, 30)][2][1];

														updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%.2lf", pArg1);
													}
												}
											}
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 595);
							if (this->attribute->am2_Ai_Press == 1) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 596);
								return waitorder(&(am2_olPressSide[ValidIndex("am_model.am_olPressSide", this->attribute->am2_Ai_pi, 4)]), this, pSTRunControl_arriving, Step 13, am_localargs);
Label13: ; /* Step 13 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 597);
								return waitorder(&(am2_olPressSide[ValidIndex("am_model.am_olPressSide", this->attribute->am2_Ai_pi + 2, 4)]), this, pSTRunControl_arriving, Step 14, am_localargs);
Label14: ; /* Step 14 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 599);
							clone(this, 1, am2_pCarrierLoad, NULL);
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 602);
							if (waitfor(ToModelTime(am2_vrStyleCT2[ValidIndex("am_model.am_vrStyleCT2", this->attribute->am2_Ai_Type2, 50)][ValidIndex("am_model.am_vrStyleCT2", this->attribute->am2_Ai_Press, 2)], UNITSECONDS), this, pSTRunControl_arriving, Step 15, am_localargs) == Delayed)
								return Delayed;
Label15: ; /* Step 15 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 603);
							this->attribute->am2_Ai_pi = oneofFunc2(this);
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 604);
							if (this->attribute->am2_Ai_pi == 1) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 604);
								am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += 1;
								EntityChanged(0x01000000);
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 605);
								am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_Type2, 50)] += 1;
								EntityChanged(0x01000000);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 606);
							{
								char* pArg1 = "TrigQty->";
								char* pArg2 = " ";
								int32 pArg3 = am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", this->attribute->am2_Ai_Type2, 50)];
								char* pArg4 = " ";
								char* pArg5 = "RunQty->";
								char* pArg6 = " ";
								int32 pArg7 = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", this->attribute->am2_Ai_Type2, 50)];
								char* pArg8 = " ";
								char* pArg9 = "Inv->";
								char* pArg10 = " ";
								int32 pArg11 = am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_Type2, 50)];

								updatelabel(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", this->attribute->am2_Ai_Type2, 50)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 607);
							{
								char* pArg1 = "Model: ";
								char* pArg2 = am2_vsStyle2[ValidIndex("am_model.am_vsStyle2", this->attribute->am2_Ai_Type2, 50)];
								char* pArg3 = " RO: ";
								int32 pArg4 = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", this->attribute->am2_Ai_Type2, 50)];
								char* pArg5 = " Cur: ";
								int32 pArg6 = this->attribute->am2_Ai_Seq;

								updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", this->attribute->am2_Ai_Press, 4)]), "%s%s%s%d%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6);
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 609);
					{
						int32 pArg1 = this->attribute->am2_Ai_Seq;

						updatelabel(&(am2_lblCurrent[ValidIndex("am_model.am_lblCurrent", this->attribute->am2_Ai_Press, 2)]), "%d", pArg1);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 611);
					am2_Vr_PartCount[1][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 612);
					{
						double pArg1 = am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] / am2_Vr_PartCount[1][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] * 100;
						char* pArg2 = "%";

						updatelabel(&(am2_lblScrap[ValidIndex("am_model.am_lblScrap", this->attribute->am2_Ai_Press, 2)]), "%.2lf%s", pArg1, pArg2);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 614);
					this->attribute->am2_Ai_Seq += 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 616);
					if (this->attribute->am2_Ai_Type <= 20) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 617);
							if (this->attribute->am2_Ai_NonSPO == 1) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 618);
									if (this->attribute->am2_Ai_Seq > am2_viMinShotSize2[ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_Type2, 50)][ValidIndex("am_model.am_viMinShotSize2", this->attribute->am2_Ai_Press, 2)] && ListSize(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", this->attribute->am2_Ai_Press, 2)]) > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 619);
											ListRemoveFirstMatch(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", this->attribute->am2_Ai_Press, 2)], ListFirstItem(LoadList, OrdGetLoadList(ValidPtr(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), 40, ordlist*))));	/* remove first match from list */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 620);
											order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, NULL); /* Place an order */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 621);
											am2_Vi_RunTrigger[ValidIndex("am_model.am_Vi_RunTrigger", this->attribute->am2_Ai_Press, 2)] = 1;
											EntityChanged(0x01000000);
										}
									}
								}
							}
							else {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 626);
									if (this->attribute->am2_Ai_Seq % (2 * am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_Type, 30)]) == 0 && this->attribute->am2_Ai_Seq > am2_viMinShotSize[ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Type, 20)][ValidIndex("am_model.am_viMinShotSize", this->attribute->am2_Ai_Press, 2)] && ListSize(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", this->attribute->am2_Ai_Press, 2)]) > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 627);
											ListRemoveFirstMatch(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", this->attribute->am2_Ai_Press, 2)], ListFirstItem(LoadList, OrdGetLoadList(ValidPtr(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), 40, ordlist*))));	/* remove first match from list */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 628);
											order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, NULL); /* Place an order */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 629);
											am2_Vi_RunTrigger[ValidIndex("am_model.am_Vi_RunTrigger", this->attribute->am2_Ai_Press, 2)] = 1;
											EntityChanged(0x01000000);
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 634);
					if ((this->attribute->am2_Ai_Type == 1 || this->attribute->am2_Ai_Type == 2) && this->attribute->am2_Ai_NonSPO == 0 && this->attribute->am2_Ai_Seq == 900 && am2_Vi_SplitSPO == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 635);
							if (am2_Vi_ScheduleCount[2][ValidIndex("am_model.am_Vi_ScheduleCount", this->attribute->am2_Ai_Press, 2)] > 0) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 636);
									order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, OrderCondFunc0); /* Place an order */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 637);
									freersrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 1, this);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 638);
									am2_Vld_Schedule[ValidIndex("am_model.am_Vld_Schedule", this->attribute->am2_Ai_Press, 2)] = this;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 639);
									return waitorder(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), this, pSTRunControl_arriving, Step 16, am_localargs);
Label16: ; /* Step 16 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 640);
									am2_Vld_Schedule[ValidIndex("am_model.am_Vld_Schedule", this->attribute->am2_Ai_Press, 2)] = NULL;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 641);
									if (getrsrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 1, this, pSTRunControl_arriving, Step 17, am_localargs) == Delayed)
										return Delayed;  /* go wait for resource */
Label17: ; /* Step 17 */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 642);
									if (waitfor(ToModelTime(am2_vrDieChangeTime[ValidIndex("am_model.am_vrDieChangeTime", this->attribute->am2_Ai_Press, 2)], UNITMINUTES), this, pSTRunControl_arriving, Step 18, am_localargs) == Delayed)
										return Delayed;
Label18: ; /* Step 18 */
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 647);
			am2_viSTRunGen[ValidIndex("am_model.am_viSTRunGen", this->attribute->am2_Ai_Press, 2)] = 0;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 648);
			if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 2) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 649);
				MonSetState(am2_vsmPressCondition, str2StatePtr("TwoPressRunningSpo"), this);
				EntityChanged(0x00000100);
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 650);
				if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 1) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 651);
					MonSetState(am2_vsmPressCondition, str2StatePtr("OnePressRunningSpo"), this);
					EntityChanged(0x00000100);
				}
				else {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 652);
					if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 653);
						MonSetState(am2_vsmPressCondition, str2StatePtr("NoPressRunningSpo"), this);
						EntityChanged(0x00000100);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 655);
			freersrc(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 1, this);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 657);
			if (am2_Vi_RunTrigger[ValidIndex("am_model.am_Vi_RunTrigger", this->attribute->am2_Ai_Press, 2)] == 1) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 658);
					am2_Vi_RunTrigger[ValidIndex("am_model.am_Vi_RunTrigger", this->attribute->am2_Ai_Press, 2)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 659);
					if (this->attribute->am2_Ai_NonSPO == 0 && am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] <= am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)]) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 660);
						this->nextproc = am2_pSTRunControl; /* send to ... */
						EntityChanged(W_LOAD);
						retval = Continue;
						goto LabelRet;
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 661);
						if (this->attribute->am2_Ai_NonSPO == 1 && am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_Type2, 50)] <= am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", this->attribute->am2_Ai_Type2, 50)]) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 662);
							this->nextproc = am2_pSTRunControl; /* send to ... */
							EntityChanged(W_LOAD);
							retval = Continue;
							goto LabelRet;
						}
					}
				}
			}
			else {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 665);
					if (am2_Vld_Schedule[ValidIndex("am_model.am_Vld_Schedule", this->attribute->am2_Ai_Press, 2)] != NULL) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 666);
						orderload(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, am2_Vld_Schedule[ValidIndex("am_model.am_Vld_Schedule", this->attribute->am2_Ai_Press, 2)]); /* Place an order */
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 668);
							if (CntGetCurConts(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*)) < am2_Vi_EmptyReq) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 669);
									if (am2_Vi_ScheduleCount[2][ValidIndex("am_model.am_Vi_ScheduleCount", this->attribute->am2_Ai_Press, 2)] > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 670);
											order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, OrderCondFunc1); /* Place an order */
										}
									}
									else {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 673);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("NoScheduledRuns"), this);
											EntityChanged(0x01020040);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 674);
											{
												char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*))), am_model.$sys);

												updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", 2 + this->attribute->am2_Ai_Press, 4)]), "%s", pArg1);
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 675);
											return waitorder(am2_O_HoldForCarriers, this, pSTRunControl_arriving, Step 19, am_localargs);
Label19: ; /* Step 19 */
											if (!this->inLeaveProc && this->nextproc) {
												retval = Continue;
												goto LabelRet;
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 677);
											if (this->attribute->am2_Ai_pi == 5252) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 677);
												order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, OrderCondFunc2); /* Place an order */
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 678);
												{
													int32 tempint = order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, NULL); /* Place an order */
													if (tempint > 0) backorder(tempint, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, NULL);	/* Place a backorder */
												}
											}
										}
									}
								}
							}
							else {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 682);
									if (OrdGetCurConts(ValidPtr(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), 40, ordlist*)) == 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 683);
											MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("NoScheduledRuns"), this);
											EntityChanged(0x01020040);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 684);
											{
												char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*))), am_model.$sys);

												updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", 2 + this->attribute->am2_Ai_Press, 4)]), "%s", pArg1);
											}
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 686);
									{
										int32 tempint = order(1, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, NULL); /* Place an order */
										if (tempint > 0) backorder(tempint, &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), NULL, NULL);	/* Place a backorder */
									}
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 691);
			{
				char* pArg1 = "";

				updatelabel(&(am2_lblPart[ValidIndex("am_model.am_lblPart", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 692);
			{
				char* pArg1 = "";

				updatelabel(&(am2_lblShotSize[ValidIndex("am_model.am_lblShotSize", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 693);
			{
				char* pArg1 = "";

				updatelabel(&(am2_lblCurrent[ValidIndex("am_model.am_lblCurrent", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 694);
			{
				char* pArg1 = "";

				updatelabel(&(am2_lblCycleTime[ValidIndex("am_model.am_lblCycleTime", this->attribute->am2_Ai_Press, 2)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 696);
			if (this->attribute->am2_Ai_NonSPO == 1) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 696);
				{
					int result = deccount(&(am2_C_RunControl[ValidIndex("am_model.am_C_RunControl", this->attribute->am2_Ai_Type2, 50)]), 1, this, pSTRunControl_arriving, Step 20, am_localargs);
					if (result != Continue) return result;
Label20: ; /* Step 20 */
				}
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 697);
				{
					int result = deccount(&(am2_C_SPOControl[ValidIndex("am_model.am_C_SPOControl", this->attribute->am2_Ai_Type, 30)]), 1, this, pSTRunControl_arriving, Step 21, am_localargs);
					if (result != Continue) return result;
Label21: ; /* Step 21 */
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 699);
			if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999), 38, simloc*)) > (LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999), 38, simloc*)) / 2) && am2_viSTRunGen[1] == 0 && am2_viSTRunGen[2] == 0 && am2_viKickOutQty[1] == 0 && LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*)) == 0 && LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 702);
					pushppa(this, pSTRunControl_arriving, Step 22, am_localargs);
					pushppa(this, am2_sAttributeStrip, Step 1, NULL);
					return Continue;
Label22: ;
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 703);
					SetString(&this->attribute->am2_As_Type, NULL);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 704);
					this->attribute->am2_Ai_LotNo = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 705);
					this->attribute->am2_Ai_Type = 99;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 706);
					am2_viKickOutQty[1] += 20;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 707);
					if (am2_viPressActive[1] == 0 || am2_viPressActive[2] == 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 708);
						clone(this, 20, am2_pEmptyRecirc, am2_L_invis);
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 709);
						if (OrdGetCurConts(am2_oPressSelect) > 0) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor, 710);
							order(1, am2_oPressSelect, am2_pEmptyCarrierLaneSelect, NULL); /* Place an order */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pSTRunControl", pSTRunControl_arriving, localactor);
	return retval;
} /* end of pSTRunControl_arriving */


typedef struct {
	int32 value;
} Nextof3;

static Nextof3 List3[] = {
	1,
	2
};

static int32
nextofFunc3(load* this)
{
	static int ind = 1;

	tprintf(tfp, "In nextof\n");
	ind = (ind+1) % 2;
	return List3[ind].value;
}

static int32
pEmptyRecirc_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", localactor);
	AMDebuggerParams("base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	case Step 7: goto Label7;
	case Step 8: goto Label8;
	case Step 9: goto Label9;
	case Step 10: goto Label10;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 716);
			if (am2_viKickOutQty[1] < 20) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 717);
				return waitorder(am2_oEmptyRecirc, this, pEmptyRecirc_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
				if (!this->inLeaveProc && this->nextproc) {
					retval = Continue;
					goto LabelRet;
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 718);
			this->attribute->am2_Ai_pi = nextofFunc3(this);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 719);
			if (am2_viPressActive[1] == 1 && am2_viPressActive[2] == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 720);
					pushppa(this, pEmptyRecirc_arriving, Step 3, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][1]);
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 721);
					pushppa(this, pEmptyRecirc_arriving, Step 4, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B19, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 722);
					return waitorder(am2_oLiftClear, this, pEmptyRecirc_arriving, Step 5, am_localargs);
Label5: ; /* Step 5 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 723);
					pushppa(this, pEmptyRecirc_arriving, Step 6, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_LiftDown, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label6: ; /* Step 6 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 724);
					pushppa(this, pEmptyRecirc_arriving, Step 7, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999));
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label7: ; /* Step 7 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 725);
					pushppa(this, pEmptyRecirc_arriving, Step 8, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staUp, 1, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label8: ; /* Step 8 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 726);
					pushppa(this, pEmptyRecirc_arriving, Step 9, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B20, -9999));
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label9: ; /* Step 9 */
				}
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 728);
				if (am2_viPressActive[2] == 1 && am2_viPressActive[1] == 0) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 729);
					pushppa(this, pEmptyRecirc_arriving, Step 10, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][2]);
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label10: ; /* Step 10 */
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 730);
			am2_viKickOutQty[1] -= 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 731);
			order(1, am2_oEmptyRecirc, NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor, 732);
			this->nextproc = am2_pEmptyCarrierLaneSelect; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pEmptyRecirc", pEmptyRecirc_arriving, localactor);
	return retval;
} /* end of pEmptyRecirc_arriving */


static double
Funcl2(load* this)
{
	return am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 4;
}


static double
Funcl3(load* this)
{
	return 1 - am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 4;
}


typedef struct {
	double (*freq)(load*);
	int32 value;
} Oneof4;

static Oneof4 List4[] = {
	{ Funcl2, 1},
	{ Funcl3, 0}
};

static int32
oneofFunc4(load* this)
{
	size_t ind;
	size_t i;
	static Real freq[2];

	tprintf(tfp, "In oneof\n");
	for (i = 0; i < 2; i++)
		freq[i] = List4[i].freq(this);
	ind = oneof_n(ValidPtr(am2_stream43, 60, rnstream*), 2, freq);
	return List4[ind].value;
}


static double
Funcl4(load* this)
{
	return am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 4;
}


static double
Funcl5(load* this)
{
	return 1 - am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 4;
}


typedef struct {
	double (*freq)(load*);
	int32 value;
} Oneof5;

static Oneof5 List5[] = {
	{ Funcl4, 1},
	{ Funcl5, 0}
};

static int32
oneofFunc5(load* this)
{
	size_t ind;
	size_t i;
	static Real freq[2];

	tprintf(tfp, "In oneof\n");
	for (i = 0; i < 2; i++)
		freq[i] = List5[i].freq(this);
	ind = oneof_n(ValidPtr(am2_stream44, 60, rnstream*), 2, freq);
	return List5[ind].value;
}


static double
Funcl6(load* this)
{
	return am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 2;
}


static double
Funcl7(load* this)
{
	return 1 - am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 2;
}


typedef struct {
	double (*freq)(load*);
	int32 value;
} Oneof6;

static Oneof6 List6[] = {
	{ Funcl6, 1},
	{ Funcl7, 0}
};

static int32
oneofFunc6(load* this)
{
	size_t ind;
	size_t i;
	static Real freq[2];

	tprintf(tfp, "In oneof\n");
	for (i = 0; i < 2; i++)
		freq[i] = List6[i].freq(this);
	ind = oneof_n(ValidPtr(am2_stream45, 60, rnstream*), 2, freq);
	return List6[ind].value;
}


static double
Funcl8(load* this)
{
	return am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 2;
}


static double
Funcl9(load* this)
{
	return 1 - am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 2;
}


typedef struct {
	double (*freq)(load*);
	int32 value;
} Oneof7;

static Oneof7 List7[] = {
	{ Funcl8, 1},
	{ Funcl9, 0}
};

static int32
oneofFunc7(load* this)
{
	size_t ind;
	size_t i;
	static Real freq[2];

	tprintf(tfp, "In oneof\n");
	for (i = 0; i < 2; i++)
		freq[i] = List7[i].freq(this);
	ind = oneof_n(ValidPtr(am2_stream46, 60, rnstream*), 2, freq);
	return List7[ind].value;
}

static int32
pCarrierLoad_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", localactor);
	AMDebuggerParams("base.pCarrierLoad", pCarrierLoad_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	case Step 7: goto Label7;
	case Step 8: goto Label8;
	case Step 9: goto Label9;
	case Step 10: goto Label10;
	case Step 11: goto Label11;
	case Step 12: goto Label12;
	case Step 13: goto Label13;
	case Step 14: goto Label14;
	case Step 15: goto Label15;
	case Step 16: goto Label16;
	case Step 17: goto Label17;
	case Step 18: goto Label18;
	case Step 19: goto Label19;
	case Step 20: goto Label20;
	case Step 21: goto Label21;
	case Step 22: goto Label22;
	case Step 23: goto Label23;
	case Step 24: goto Label24;
	case Step 25: goto Label25;
	case Step 26: goto Label26;
	case Step 27: goto Label27;
	case Step 28: goto Label28;
	case Step 29: goto Label29;
	case Step 30: goto Label30;
	case Step 31: goto Label31;
	case Step 32: goto Label32;
	case Step 33: goto Label33;
	case Step 34: goto Label34;
	case Step 35: goto Label35;
	case Step 36: goto Label36;
	case Step 37: goto Label37;
	case Step 38: goto Label38;
	case Step 39: goto Label39;
	case Step 40: goto Label40;
	case Step 41: goto Label41;
	case Step 42: goto Label42;
	case Step 43: goto Label43;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 736);
			if (this->attribute->am2_Ai_Seq == 99999) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 737);
					this->attribute->am2_Ai_pi = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 738);
					pushppa(this, pCarrierLoad_arriving, Step 2, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_Start, -9999));
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label2: ; /* Step 2 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 739);
					am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] += this->attribute->am2_Ai_CarQty;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 740);
					{
						char* pArg1 = "TrigQty->";
						char* pArg2 = " ";
						int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)];
						char* pArg4 = " ";
						char* pArg5 = "RunQty->";
						char* pArg6 = " ";
						int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)];
						char* pArg8 = " ";
						char* pArg9 = "Inv->";
						char* pArg10 = " ";
						int32 pArg11 = am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)];

						updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_Type, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 741);
					pushppa(this, pCarrierLoad_arriving, Step 3, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 742);
					pushppa(this, pCarrierLoad_arriving, Step 4, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B28, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label4: ; /* Step 4 */
				}
			}
			else {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 745);
					LdSetColor(this, am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", this->attribute->am2_Ai_Type, 30)]);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 746);
					pushppa(this, pCarrierLoad_arriving, Step 5, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][1][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)]);
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label5: ; /* Step 5 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 747);
					if (this->attribute->am2_Ai_Press == 1) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 748);
						{
							int32 tempint = order(1, &(am2_olPressSide[ValidIndex("am_model.am_olPressSide", this->attribute->am2_Ai_pi, 4)]), NULL, NULL); /* Place an order */
							if (tempint > 0) backorder(tempint, &(am2_olPressSide[ValidIndex("am_model.am_olPressSide", this->attribute->am2_Ai_pi, 4)]), NULL, NULL);	/* Place a backorder */
						}
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 749);
						if (this->attribute->am2_Ai_Press == 2) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 750);
							{
								int32 tempint = order(1, &(am2_olPressSide[ValidIndex("am_model.am_olPressSide", this->attribute->am2_Ai_pi + 2, 4)]), NULL, NULL); /* Place an order */
								if (tempint > 0) backorder(tempint, &(am2_olPressSide[ValidIndex("am_model.am_olPressSide", this->attribute->am2_Ai_pi + 2, 4)]), NULL, NULL);	/* Place a backorder */
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 752);
					pushppa(this, pCarrierLoad_arriving, Step 6, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][2][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label6: ; /* Step 6 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 754);
					if (am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)] != NULL) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 755);
							if (ValidPtr(am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], 32, load*)->attribute->am2_Ai_Type != this->attribute->am2_Ai_Type) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 756);
									order(1, am2_volPartKill[ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_pi, 2)][2][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 757);
									if (waitfor(ToModelTime(0, UNITSECONDS), this, pCarrierLoad_arriving, Step 7, am_localargs) == Delayed)
										return Delayed;
Label7: ; /* Step 7 */
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 761);
					if (this->attribute->am2_Ai_Press == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 762);
							if (waitfor(ToModelTime(4, UNITSECONDS), this, pCarrierLoad_arriving, Step 8, am_localargs) == Delayed)
								return Delayed;
Label8: ; /* Step 8 */
						}
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 764);
						if (this->attribute->am2_Ai_Press == 2) {
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 765);
								if (this->attribute->am2_Ai_pi == 1) {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 766);
										pushppa(this, pCarrierLoad_arriving, Step 9, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_K_RBT.am_Rob1Work1, 1, -9999));
										pushppa(this, move_in_loc, Step 1, NULL);
										return Continue; /* go move into territory */
Label9: ; /* Step 9 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 767);
										pushppa(this, pCarrierLoad_arriving, Step 10, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_K_RBT.am_Rob1Pounce2, 1, -9999));
										pushppa(this, travel_to_loc, Step 1, NULL);
										return Continue; /* go move to location */
Label10: ; /* Step 10 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 768);
										if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) == 0) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 769);
												MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("NoCarriers"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 770);
												{
													char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(am2_rCLine)), am_model.$sys);

													updatelabel(&(am2_lblState[4]), "%s", pArg1);
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 771);
												return waitorder(&(am2_olRobHandshake[ValidIndex("am_model.am_olRobHandshake", this->attribute->am2_Ai_pi, 2)]), this, pCarrierLoad_arriving, Step 11, am_localargs);
Label11: ; /* Step 11 */
												if (!this->inLeaveProc && this->nextproc) {
													retval = Continue;
													goto LabelRet;
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 772);
												MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Production"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 773);
												{
													char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(am2_rCLine)), am_model.$sys);

													updatelabel(&(am2_lblState[4]), "%s", pArg1);
												}
											}
										}
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 775);
										pushppa(this, pCarrierLoad_arriving, Step 12, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_K_RBT.am_Rob1Work2, 1, -9999));
										pushppa(this, travel_to_loc, Step 1, NULL);
										return Continue; /* go move to location */
Label12: ; /* Step 12 */
									}
								}
								else {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 778);
										pushppa(this, pCarrierLoad_arriving, Step 13, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_K_RBT.am_Rob2Work1, 2, -9999));
										pushppa(this, move_in_loc, Step 1, NULL);
										return Continue; /* go move into territory */
Label13: ; /* Step 13 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 779);
										pushppa(this, pCarrierLoad_arriving, Step 14, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_K_RBT.am_Rob2Pounce2, 2, -9999));
										pushppa(this, travel_to_loc, Step 1, NULL);
										return Continue; /* go move to location */
Label14: ; /* Step 14 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 780);
										if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) == 0) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 781);
												MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("NoCarriers"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 782);
												{
													char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(am2_rCLine)), am_model.$sys);

													updatelabel(&(am2_lblState[4]), "%s", pArg1);
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 783);
												return waitorder(&(am2_olRobHandshake[ValidIndex("am_model.am_olRobHandshake", this->attribute->am2_Ai_pi, 2)]), this, pCarrierLoad_arriving, Step 15, am_localargs);
Label15: ; /* Step 15 */
												if (!this->inLeaveProc && this->nextproc) {
													retval = Continue;
													goto LabelRet;
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 784);
												MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Production"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 785);
												{
													char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(am2_rCLine)), am_model.$sys);

													updatelabel(&(am2_lblState[4]), "%s", pArg1);
												}
											}
										}
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 787);
										pushppa(this, pCarrierLoad_arriving, Step 16, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_K_RBT.am_Rob2Work2, 2, -9999));
										pushppa(this, travel_to_loc, Step 1, NULL);
										return Continue; /* go move to location */
Label16: ; /* Step 16 */
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 792);
					am2_viPartCount[ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_Press, 2)] += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 794);
					if (am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)] != NULL) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 795);
							ValidPtr(am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], 32, load*)->attribute->am2_Ai_CarQty = am2_viPartCount[ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_Press, 2)];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 796);
							if (transformLoad(TX_LOAD_SCALE, 1.0, 1.0, ValidPtr(am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], 32, load*)->attribute->am2_Ai_CarQty, 0, 0, 1, 0.0, am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], this, pCarrierLoad_arriving, Step 17, am_localargs) == Delayed)
								return Delayed;
Label17: ; /* Step 17 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 798);
							pushppa(this, pCarrierLoad_arriving, Step 18, am_localargs);
							pushppa(this, inqueue, Step 1, &(am2_qTransferTemp[ValidIndex("am_model.am_qTransferTemp", this->attribute->am2_Ai_pi, 2)]));
							return Continue; /* go move into territory */
Label18: ; /* Step 18 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 799);
							if (am2_viPartCount[ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_Press, 2)] < am2_viCarrierQty[ValidIndex("am_model.am_viCarrierQty", this->attribute->am2_Ai_Type, 30)] && this->attribute->am2_Ai_LotQty != this->attribute->am2_Ai_Seq + 1 && this->attribute->am2_Ai_LotQty != this->attribute->am2_Ai_Seq) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 800);
								return waitorder(am2_volPartKill[ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_pi, 2)][1][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], this, pCarrierLoad_arriving, Step 19, am_localargs);
Label19: ; /* Step 19 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							else {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 802);
									order(-1, am2_volPartKill[ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_pi, 2)][1][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 803);
									order(1, am2_volPartKill[ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_pi, 2)][2][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], NULL, NULL); /* Place an order */
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 805);
							this->nextproc = am2_die; /* send to ... */
							EntityChanged(W_LOAD);
							retval = Continue;
							goto LabelRet;
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 808);
					am2_Vr_CarWait[ValidIndex("am_model.am_Vr_CarWait", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_Vr_CarWait", this->attribute->am2_Ai_Press, 2)] = FromModelTime(ASIclock, UNITSECONDS);
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 809);
					am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)] = this;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 810);
					ValidPtr(am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], 32, load*)->attribute->am2_Ai_CarQty = am2_viPartCount[ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_Press, 2)];
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 811);
					if (transformLoad(TX_LOAD_SCALE, 1.0, 1.0, ValidPtr(am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], 32, load*)->attribute->am2_Ai_CarQty, 0, 0, 1, 0.0, am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)], this, pCarrierLoad_arriving, Step 20, am_localargs) == Delayed)
						return Delayed;
Label20: ; /* Step 20 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 813);
					if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) < 2) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 814);
							MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("NoCarriers"), this);
							EntityChanged(0x01020040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 815);
							{
								char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*))), am_model.$sys);

								updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", 2 + this->attribute->am2_Ai_Press, 4)]), "%s", pArg1);
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 817);
					pushppa(this, pCarrierLoad_arriving, Step 21, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)]);
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label21: ; /* Step 21 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 818);
					if (StringCompare(statename(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)))), statename(am2_NoCarriers)) == 0) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 819);
							if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][3][ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_Press, 2)], 38, simloc*)) == 2) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 820);
									MonSetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*)), str2StatePtr("Production"), this);
									EntityChanged(0x01020040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 821);
									{
										char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[ValidIndex("am_model.am_vrscDowntime", 2 + this->attribute->am2_Ai_Press, 5)], 50, resource*))), am_model.$sys);

										updatelabel(&(am2_lblState[ValidIndex("am_model.am_lblState", 2 + this->attribute->am2_Ai_Press, 4)]), "%s", pArg1);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 825);
					tabulate(&(am2_T_CarWait[ValidIndex("am_model.am_T_CarWait", this->attribute->am2_Ai_Press, 2)]), (FromModelTime(ASIclock, UNITSECONDS) - am2_Vr_CarWait[ValidIndex("am_model.am_Vr_CarWait", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_Vr_CarWait", this->attribute->am2_Ai_Press, 2)]) / 60);	/* Tabulate the value */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 827);
					if (this->attribute->am2_Ai_LotQty != this->attribute->am2_Ai_Seq + 1 && this->attribute->am2_Ai_LotQty != this->attribute->am2_Ai_Seq) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 828);
						return waitorder(am2_volPartKill[ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_pi, 2)][2][ValidIndex("am_model.am_volPartKill", this->attribute->am2_Ai_Press, 2)], this, pCarrierLoad_arriving, Step 22, am_localargs);
Label22: ; /* Step 22 */
						if (!this->inLeaveProc && this->nextproc) {
							retval = Continue;
							goto LabelRet;
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 829);
					am2_vlptrLoadGraphic[ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_vlptrLoadGraphic", this->attribute->am2_Ai_Press, 2)] = NULL;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 830);
					am2_viPartCount[ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_pi, 2)][ValidIndex("am_model.am_viPartCount", this->attribute->am2_Ai_Press, 2)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 832);
					{
						int result = deccount(&(am2_C_CarrierCount[2]), 1, this, pCarrierLoad_arriving, Step 23, am_localargs);
						if (result != Continue) return result;
Label23: ; /* Step 23 */
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 833);
					{
						int result = inccount(&(am2_C_CarrierCount[1]), 1, this, pCarrierLoad_arriving, Step 24, am_localargs);
						if (result != Continue) return result;
Label24: ; /* Step 24 */
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 834);
					am2_viEmptyLoad[ValidIndex("am_model.am_viEmptyLoad", this->attribute->am2_Ai_Press, 2)][1] -= 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 845);
					am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] += this->attribute->am2_Ai_CarQty;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 846);
					{
						char* pArg1 = "TrigQty->";
						char* pArg2 = " ";
						int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)];
						char* pArg4 = " ";
						char* pArg5 = "RunQty->";
						char* pArg6 = " ";
						int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)];
						char* pArg8 = " ";
						char* pArg9 = "Inv->";
						char* pArg10 = " ";
						int32 pArg11 = am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)];

						updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_Type, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 848);
					if (OrdGetCurConts(am2_oPressSelect) > 0 && am2_viEmptyLoad[ValidIndex("am_model.am_viEmptyLoad", this->attribute->am2_Ai_Press, 2)][1] < am2_viEmptyLoad[ValidIndex("am_model.am_viEmptyLoad", this->attribute->am2_Ai_Press, 2)][2]) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 849);
							ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(am2_oPressSelect)), 32, load*)->attribute->am2_Ai_Press = this->attribute->am2_Ai_Press;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 850);
							order(1, am2_oPressSelect, NULL, NULL); /* Place an order */
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 853);
					if (this->attribute->am2_Ai_Press == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 854);
							if (this->attribute->am2_Ai_pi == 1) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 855);
									if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B8, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B9, -9999), 38, simloc*)) == 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 856);
											if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rBLine), am2_Down)) == 0) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 857);
												MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Blocked"), this);
												EntityChanged(0x00020000);
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 858);
											am2_viBlocked[ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_pi, 2)] = 1;
											EntityChanged(0x01000000);
										}
									}
								}
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 861);
								if (this->attribute->am2_Ai_pi == 2) {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 862);
										if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B12, -9999), 38, simloc*)) == 0) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 863);
												if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rBLine), am2_Down)) == 0) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 864);
													MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Blocked"), this);
													EntityChanged(0x00020000);
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 865);
												am2_viBlocked[ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_pi, 2)] = 1;
												EntityChanged(0x01000000);
											}
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 869);
							pushppa(this, pCarrierLoad_arriving, Step 25, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B15, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label25: ; /* Step 25 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 870);
							if (getrsrc(am2_rStampingShift, 1, this, pCarrierLoad_arriving, Step 26, am_localargs) == Delayed)
								return Delayed;  /* go wait for resource */
Label26: ; /* Step 26 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 871);
							if (waitfor(ToModelTime(am2_vrStopTime[6], UNITSECONDS), this, pCarrierLoad_arriving, Step 27, am_localargs) == Delayed)
								return Delayed;
Label27: ; /* Step 27 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 872);
							am2_Vi_x = 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 873);
							am2_Vi_CarQty = this->attribute->am2_Ai_CarQty;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 874);
							while (am2_Vi_x <= am2_Vi_CarQty) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 875);
									this->attribute->am2_Ai_x = oneofFunc4(this);
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 876);
									am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += this->attribute->am2_Ai_x;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 877);
									this->attribute->am2_Ai_CarQty -= this->attribute->am2_Ai_x;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 878);
									am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] -= this->attribute->am2_Ai_x;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 879);
									am2_Vi_x += 1;
									EntityChanged(0x01000000);
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 881);
							{
								double pArg1 = am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] / am2_Vr_PartCount[1][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] * 100;
								char* pArg2 = "%";

								updatelabel(&(am2_lblScrap[ValidIndex("am_model.am_lblScrap", this->attribute->am2_Ai_Press, 2)]), "%.2lf%s", pArg1, pArg2);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 882);
							{
								char* pArg1 = "TrigQty->";
								char* pArg2 = " ";
								int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)];
								char* pArg4 = " ";
								char* pArg5 = "RunQty->";
								char* pArg6 = " ";
								int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)];
								char* pArg8 = " ";
								char* pArg9 = "Inv->";
								char* pArg10 = " ";
								int32 pArg11 = am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)];

								updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_Type, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 884);
							freersrc(am2_rStampingShift, 1, this);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 885);
							pushppa(this, pCarrierLoad_arriving, Step 28, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B16, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label28: ; /* Step 28 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 886);
							if (getrsrc(am2_rStampingShift, 1, this, pCarrierLoad_arriving, Step 29, am_localargs) == Delayed)
								return Delayed;  /* go wait for resource */
Label29: ; /* Step 29 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 887);
							if (waitfor(ToModelTime(am2_vrStopTime[7], UNITSECONDS), this, pCarrierLoad_arriving, Step 30, am_localargs) == Delayed)
								return Delayed;
Label30: ; /* Step 30 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 888);
							am2_Vi_x = 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 889);
							am2_Vi_CarQty = this->attribute->am2_Ai_CarQty;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 890);
							while (am2_Vi_x <= am2_Vi_CarQty) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 891);
									this->attribute->am2_Ai_x = oneofFunc5(this);
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 892);
									am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += this->attribute->am2_Ai_x;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 893);
									this->attribute->am2_Ai_CarQty -= this->attribute->am2_Ai_x;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 894);
									am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] -= this->attribute->am2_Ai_x;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 895);
									am2_Vi_x += 1;
									EntityChanged(0x01000000);
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 897);
							{
								double pArg1 = am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] / am2_Vr_PartCount[1][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] * 100;
								char* pArg2 = "%";

								updatelabel(&(am2_lblScrap[ValidIndex("am_model.am_lblScrap", this->attribute->am2_Ai_Press, 2)]), "%.2lf%s", pArg1, pArg2);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 898);
							{
								char* pArg1 = "TrigQty->";
								char* pArg2 = " ";
								int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)];
								char* pArg4 = " ";
								char* pArg5 = "RunQty->";
								char* pArg6 = " ";
								int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)];
								char* pArg8 = " ";
								char* pArg9 = "Inv->";
								char* pArg10 = " ";
								int32 pArg11 = am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)];

								updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_Type, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 900);
							freersrc(am2_rStampingShift, 1, this);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 901);
							pushppa(this, pCarrierLoad_arriving, Step 31, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_LiftDown, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label31: ; /* Step 31 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 902);
							pushppa(this, pCarrierLoad_arriving, Step 32, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label32: ; /* Step 32 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 903);
							pushppa(this, pCarrierLoad_arriving, Step 33, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staUp, 1, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label33: ; /* Step 33 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 904);
							pushppa(this, pCarrierLoad_arriving, Step 34, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B20, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label34: ; /* Step 34 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 905);
							pushppa(this, pCarrierLoad_arriving, Step 35, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B23, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label35: ; /* Step 35 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 907);
							if (am2_viSTRunGen[2] == 1) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 908);
									if (am2_viEmptyLoad[2][1] < 11 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B3, -9999), 38, simloc*)) > 0) {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 909);
										return waitorder(am2_oST_B27, this, pCarrierLoad_arriving, Step 36, am_localargs);
Label36: ; /* Step 36 */
										if (!this->inLeaveProc && this->nextproc) {
											retval = Continue;
											goto LabelRet;
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 915);
							pushppa(this, pCarrierLoad_arriving, Step 37, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B27, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label37: ; /* Step 37 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 916);
							pushppa(this, pCarrierLoad_arriving, Step 38, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B28, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label38: ; /* Step 38 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 917);
							if (waitfor(ToModelTime(am2_vrStopTime[8], UNITSECONDS), this, pCarrierLoad_arriving, Step 39, am_localargs) == Delayed)
								return Delayed;
Label39: ; /* Step 39 */
						}
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 920);
						if (this->attribute->am2_Ai_Press == 2) {
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 921);
								if (this->attribute->am2_Ai_pi == 1) {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 922);
										if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_8, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_7, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_7a, -9999), 38, simloc*)) == 0) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 923);
												if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rCLine), am2_Down)) == 0) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 924);
													MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Blocked"), this);
													EntityChanged(0x00020000);
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 925);
												am2_viBlocked[ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_pi, 2)] = 1;
												EntityChanged(0x01000000);
											}
										}
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 927);
										pushppa(this, pCarrierLoad_arriving, Step 40, am_localargs);
										load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_8, -9999));
										pushppa(this, travel_to_loc, Step 1, NULL);
										return Continue; /* go move to location */
Label40: ; /* Step 40 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 928);
										if (waitfor(ToModelTime(am2_vrStopTime[2], UNITSECONDS), this, pCarrierLoad_arriving, Step 41, am_localargs) == Delayed)
											return Delayed;
Label41: ; /* Step 41 */
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 930);
										am2_Vi_x = 1;
										EntityChanged(0x01000000);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 931);
										am2_Vi_CarQty = this->attribute->am2_Ai_CarQty;
										EntityChanged(0x01000000);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 932);
										while (am2_Vi_x <= am2_Vi_CarQty) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 933);
												this->attribute->am2_Ai_x = oneofFunc6(this);
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 934);
												am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += this->attribute->am2_Ai_x;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 935);
												this->attribute->am2_Ai_CarQty -= this->attribute->am2_Ai_x;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 936);
												am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] -= this->attribute->am2_Ai_x;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 937);
												am2_Vi_x += 1;
												EntityChanged(0x01000000);
											}
										}
									}
								}
								else {
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 940);
									if (this->attribute->am2_Ai_pi == 2) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 941);
											if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_9, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_4, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_3, -9999), 38, simloc*)) == 0) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 942);
													if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rCLine), am2_Down)) == 0) {
														AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 943);
														MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Blocked"), this);
														EntityChanged(0x00020000);
													}
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 944);
													am2_viBlocked[ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", this->attribute->am2_Ai_pi, 2)] = 1;
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 946);
											pushppa(this, pCarrierLoad_arriving, Step 42, am_localargs);
											load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_10, -9999));
											pushppa(this, travel_to_loc, Step 1, NULL);
											return Continue; /* go move to location */
Label42: ; /* Step 42 */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 947);
											if (waitfor(ToModelTime(am2_vrStopTime[4], UNITSECONDS), this, pCarrierLoad_arriving, Step 43, am_localargs) == Delayed)
												return Delayed;
Label43: ; /* Step 43 */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 949);
											am2_Vi_x = 1;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 950);
											am2_Vi_CarQty = this->attribute->am2_Ai_CarQty;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 951);
											while (am2_Vi_x <= am2_Vi_CarQty) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 952);
													this->attribute->am2_Ai_x = oneofFunc7(this);
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 953);
													am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += this->attribute->am2_Ai_x;
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 954);
													this->attribute->am2_Ai_CarQty -= this->attribute->am2_Ai_x;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 955);
													am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] -= this->attribute->am2_Ai_x;
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 956);
													am2_Vi_x += 1;
													EntityChanged(0x01000000);
												}
											}
										}
									}
								}
							}
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 959);
								{
									double pArg1 = am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] / am2_Vr_PartCount[1][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] * 100;
									char* pArg2 = "%";

									updatelabel(&(am2_lblScrap[ValidIndex("am_model.am_lblScrap", this->attribute->am2_Ai_Press, 2)]), "%.2lf%s", pArg1, pArg2);
								}
							}
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 960);
								{
									char* pArg1 = "TrigQty->";
									char* pArg2 = " ";
									int32 pArg3 = am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)];
									char* pArg4 = " ";
									char* pArg5 = "RunQty->";
									char* pArg6 = " ";
									int32 pArg7 = am2_viSTRunOutQty[ValidIndex("am_model.am_viSTRunOutQty", this->attribute->am2_Ai_Type, 30)];
									char* pArg8 = " ";
									char* pArg9 = "Inv->";
									char* pArg10 = " ";
									int32 pArg11 = am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)];

									updatelabel(&(am2_Lb_Inventory[ValidIndex("am_model.am_Lb_Inventory", this->attribute->am2_Ai_Type, 30)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor, 963);
			this->nextproc = am2_pStorage; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pCarrierLoad", pCarrierLoad_arriving, localactor);
	return retval;
} /* end of pCarrierLoad_arriving */

static int32
pf_ohc_ST_LiftDown_idle(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Idle procedure", "base.pf_ohc.ST_LiftDown", localactor);
	AMDebuggerParams("base.pf_ohc.ST_LiftDown", pf_ohc_ST_LiftDown_idle, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Idle procedure", "base.pf_ohc.ST_LiftDown", pf_ohc_ST_LiftDown_idle, localactor, 967);
			if (transformLoad(TX_LOAD_TRANSLATE, 0.0, 0.0, ToModelDistance(16.500000000000000, UNITFEET), 0, 0, 0, ToModelTime(am2_vrBPressLiftUp, UNITSECONDS), this, this, pf_ohc_ST_LiftDown_idle, Step 2, am_localargs) == Delayed)
				return Delayed;
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("PressLogic.m", "Idle procedure", "base.pf_ohc.ST_LiftDown", pf_ohc_ST_LiftDown_idle, localactor, 968);
			pushppa(this, pf_ohc_ST_LiftDown_idle, Step 3, am_localargs);
			load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B20, -9999));
			pushppa(this, move_in_loc, Step 1, NULL);
			return Continue; /* go move into territory */
Label3: ; /* Step 3 */
		}
		{
			AMDebugger("PressLogic.m", "Idle procedure", "base.pf_ohc.ST_LiftDown", pf_ohc_ST_LiftDown_idle, localactor, 969);
			if (transformLoad(TX_LOAD_TRANSLATE, 0.0, 0.0, ToModelDistance( -16.500000000000000, UNITFEET), 0, 0, 0, 0.0, this, this, pf_ohc_ST_LiftDown_idle, Step 4, am_localargs) == Delayed)
				return Delayed;
Label4: ; /* Step 4 */
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Idle procedure", "base.pf_ohc.ST_LiftDown", pf_ohc_ST_LiftDown_idle, localactor);
	return retval;
} /* end of pf_ohc_ST_LiftDown_idle */

static int32
pf_ohc_peLiftClear_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.peLiftClear", localactor);
	AMDebuggerParams("base.pf_ohc.peLiftClear", pf_ohc_peLiftClear_photocleared, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.peLiftClear", pf_ohc_peLiftClear_photocleared, localactor, 973);
			veh_dispatch(VTGetQualifier(am_model.am_kLift.am_kLift, 1, -9999), LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999), 0, NULL, 0);
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.peLiftClear", pf_ohc_peLiftClear_photocleared, localactor);
	return retval;
} /* end of pf_ohc_peLiftClear_photocleared */

static int32
kLift_idle(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Idle procedure", "base.kLift", localactor);
	AMDebuggerParams("base.kLift", kLift_idle, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Idle procedure", "base.kLift", kLift_idle, localactor, 977);
			if (LocCompare(VehGetCurLoc(this), LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999)) == 0) {
				AMDebugger("PressLogic.m", "Idle procedure", "base.kLift", kLift_idle, localactor, 978);
				{
					int32 tempint = order(1, am2_oLiftClear, NULL, NULL); /* Place an order */
					if (tempint > 0) backorder(tempint, am2_oLiftClear, NULL, NULL);	/* Place a backorder */
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Idle procedure", "base.kLift", kLift_idle, localactor);
	return retval;
} /* end of kLift_idle */

static int32
am_Sr_OtherInv(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv0; /* 'for each' loop variable */
		AMLoadList* ls0; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Subroutine", "base.Sr_OtherInv", localactor);
	AMDebuggerParams("base.Sr_OtherInv", am_Sr_OtherInv, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 982);
			am2_viIndex = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 983);
			while (am2_viIndex <= am2_vi_NumType2) {
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 984);
					if (this->attribute->am2_Ai_Type > 20) {
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 985);
							if (am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_Type, 30)] == am2_viModelOther[ValidIndex("am_model.am_viModelOther", am2_viIndex, 50)]) {
								{
									AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 986);
									am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", am2_viIndex, 50)] -= this->attribute->am2_Ai_CarQty;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 987);
									{
										char* pArg1 = "TrigQty->";
										char* pArg2 = " ";
										int32 pArg3 = am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", am2_viIndex, 50)];
										char* pArg4 = " ";
										char* pArg5 = "RunQty->";
										char* pArg6 = " ";
										int32 pArg7 = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", am2_viIndex, 50)];
										char* pArg8 = " ";
										char* pArg9 = "Inv->";
										char* pArg10 = " ";
										int32 pArg11 = am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", am2_viIndex, 50)];

										updatelabel(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", am2_viIndex, 50)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
									}
								}
							}
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 991);
							if (this->attribute->am2_Ai_Type == am2_viModelOther[ValidIndex("am_model.am_viModelOther", am2_viIndex, 50)]) {
								{
									AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 992);
									am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", am2_viIndex, 50)] -= this->attribute->am2_Ai_CarQty;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 993);
									{
										char* pArg1 = "TrigQty->";
										char* pArg2 = " ";
										int32 pArg3 = am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", am2_viIndex, 50)];
										char* pArg4 = " ";
										char* pArg5 = "RunQty->";
										char* pArg6 = " ";
										int32 pArg7 = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", am2_viIndex, 50)];
										char* pArg8 = " ";
										char* pArg9 = "Inv->";
										char* pArg10 = " ";
										int32 pArg11 = am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", am2_viIndex, 50)];

										updatelabel(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", am2_viIndex, 50)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 997);
					if (CntGetCurConts(ValidPtr(&(am2_C_RunControl[ValidIndex("am_model.am_C_RunControl", am2_viIndex, 50)]), 10, counter*)) == 0 && am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", am2_viIndex, 50)] < am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", am2_viIndex, 50)] * 1.1000000000000001) {
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 998);
							{
								int result = inccount(&(am2_C_RunControl[ValidIndex("am_model.am_C_RunControl", am2_viIndex, 50)]), 1, this, am_Sr_OtherInv, Step 2, am_localargs);
								if (result != Continue) return result;
Label2: ; /* Step 2 */
							}
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 999);
							this->attribute->am2_Ai_Press = am2_viOtherPress[ValidIndex("am_model.am_viOtherPress", am2_viIndex, 50)];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1000);
							this->attribute->am2_Ai_NonSPO = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1001);
							this->attribute->am2_Ai_Type2 = am2_viIndex;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1002);
							clone(this, 1, am2_pSTRunControl, NULL);
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1003);
							if (waitfor(ToModelTime(0, UNITSECONDS), this, am_Sr_OtherInv, Step 3, am_localargs) == Delayed)
								return Delayed;
Label3: ; /* Step 3 */
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1004);
							this->attribute->am2_Ai_NonSPO = 0;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1005);
							if (this->attribute->am2_Ai_Type <= 20) {
								AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1005);
								this->attribute->am2_Ai_Press = am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", this->attribute->am2_Ai_Type, 20)];
								EntityChanged(0x00000040);
							}
							else {
								AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1006);
								this->attribute->am2_Ai_Press = am2_viSpoPress[ValidIndex("am_model.am_viSpoPress", am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_Type, 30)], 20)];
								EntityChanged(0x00000040);
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1009);
					if (am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", am2_viIndex, 50)] < am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", am2_viIndex, 50)] / 4) {
						{
							AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1010);
							am_localargs->ls0 = 0;
							ListCopy(LoadList, am_localargs->ls0, OrdGetLoadList(ValidPtr(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", am2_viOtherPress[ValidIndex("am_model.am_viOtherPress", am2_viIndex, 50)], 2)]), 40, ordlist*)));
							for (am_localargs->lv0 = (am_localargs->ls0) ? (am_localargs->ls0)->first : NULL; am_localargs->lv0; am_localargs->lv0 = am_localargs->lv0->next) {
								am2_Vld_Crit = am_localargs->lv0->item;
								{
									{
										AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1011);
										if (ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_NonSPO == 1 && ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Type2 == am2_viIndex && LdGetPriority(ValidPtr(am2_Vld_Crit, 32, load*)) == ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_StampingPriority) {
											{
												AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1012);
												LdSetPriority(ValidPtr(am2_Vld_Crit, 32, load*), ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_StampingPriority / 10);
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1013);
												orderload(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Press, 2)]), &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Press, 2)]), am2_Vld_Crit); /* Place an order */
											}
											{
												AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1014);
												ListAppendItem(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Press, 2)], am2_Vld_Crit);	/* append item to end of list */
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls0); /* End of for each */
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor, 1018);
					am2_viIndex += 1;
					EntityChanged(0x01000000);
				}
			}
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls0);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Subroutine", "base.Sr_OtherInv", am_Sr_OtherInv, localactor);
	return retval;
} /* end of am_Sr_OtherInv */


static simloc*
Func0(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_ST12_27, -9999);
}


static simloc*
Func1(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_ST12_28, -9999);
}


static simloc*
Func2(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_ST12_29, -9999);
}


typedef struct {
	simloc* (*value)(load*);
} Nextof8;

static Nextof8 List8[] = {
	Func0,
	Func1,
	Func2
};

static simloc*
nextofFunc8(load* this)
{
	static int ind = 2;

	tprintf(tfp, "In nextof\n");
	ind = (ind+1) % 3;
	return List8[ind].value(this);
}


static simloc*
Func3(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_ST12_27, -9999);
}


static simloc*
Func4(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_ST12_28, -9999);
}


static simloc*
Func5(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_ST12_29, -9999);
}


typedef struct {
	simloc* (*value)(load*);
} Nextof9;

static Nextof9 List9[] = {
	Func3,
	Func4,
	Func5
};

static simloc*
nextofFunc9(load* this)
{
	static int ind = 2;

	tprintf(tfp, "In nextof\n");
	ind = (ind+1) % 3;
	return List9[ind].value(this);
}

static int32
OrderCondFunc3(load* this)
{
	if (this->attribute->am2_Ai_y == 0)
		return TRUE;
	return FALSE;
}

static int32
OrderCondFunc4(load* this)
{
	if (this->attribute->am2_Ai_y == 1)
		return TRUE;
	return FALSE;
}

static int32
pEmptyCarrierReturn_arriving(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv1; /* 'for each' loop variable */
		AMLoadList* ls1; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", localactor);
	AMDebuggerParams("base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	case Step 7: goto Label7;
	case Step 8: goto Label8;
	case Step 9: goto Label9;
	case Step 10: goto Label10;
	case Step 11: goto Label11;
	case Step 12: goto Label12;
	case Step 13: goto Label13;
	case Step 14: goto Label14;
	case Step 15: goto Label15;
	case Step 16: goto Label16;
	case Step 17: goto Label17;
	case Step 18: goto Label18;
	case Step 19: goto Label19;
	case Step 20: goto Label20;
	case Step 21: goto Label21;
	case Step 22: goto Label22;
	case Step 23: goto Label23;
	case Step 24: goto Label24;
	case Step 25: goto Label25;
	case Step 26: goto Label26;
	case Step 27: goto Label27;
	case Step 28: goto Label28;
	case Step 29: goto Label29;
	case Step 30: goto Label30;
	case Step 31: goto Label31;
	case Step 32: goto Label32;
	case Step 33: goto Label33;
	case Step 34: goto Label34;
	case Step 35: goto Label35;
	case Step 36: goto Label36;
	case Step 37: goto Label37;
	case Step 38: goto Label38;
	case Step 39: goto Label39;
	case Step 40: goto Label40;
	case Step 41: goto Label41;
	case Step 42: goto Label42;
	case Step 43: goto Label43;
	case Step 44: goto Label44;
	case Step 45: goto Label45;
	case Step 46: goto Label46;
	case Step 47: goto Label47;
	case Step 48: goto Label48;
	case Step 49: goto Label49;
	case Step 50: goto Label50;
	case Step 51: goto Label51;
	case Step 52: goto Label52;
	case Step 53: goto Label53;
	case Step 54: goto Label54;
	case Step 55: goto Label55;
	case Step 56: goto Label56;
	case Step 57: goto Label57;
	case Step 58: goto Label58;
	case Step 59: goto Label59;
	case Step 60: goto Label60;
	case Step 61: goto Label61;
	case Step 62: goto Label62;
	case Step 63: goto Label63;
	case Step 64: goto Label64;
	case Step 65: goto Label65;
	case Step 66: goto Label66;
	case Step 67: goto Label67;
	case Step 68: goto Label68;
	case Step 69: goto Label69;
	case Step 70: goto Label70;
	case Step 71: goto Label71;
	case Step 72: goto Label72;
	case Step 73: goto Label73;
	case Step 74: goto Label74;
	case Step 75: goto Label75;
	case Step 76: goto Label76;
	case Step 77: goto Label77;
	case Step 78: goto Label78;
	case Step 79: goto Label79;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1032);
			this->attribute->am2_Ai_Type = 99;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1033);
			SetString(&this->attribute->am2_As_Type, NULL);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1034);
			this->attribute->am2_Ai_LotNo = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1036);
			if (this->attribute->am2_Ai_Seq == 99999) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1037);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 2, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_Start, -9999));
					pushppa(this, move_in_loc, Step 1, NULL);
					return Continue; /* go move into territory */
Label2: ; /* Step 2 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1038);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 3, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1039);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 4, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B3, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1040);
					if (LocGetTotEntriesA(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B3, -9999), 38, simloc*)) == am2_viReqVeh) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1041);
						order(1, &(am2_oCarrierInit[1]), NULL, NULL); /* Place an order */
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1042);
					if (am2_viPressActive[1] == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1043);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 5, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B19, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label5: ; /* Step 5 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1044);
							if (LocGetTotEntriesA(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B19, -9999), 38, simloc*)) == 1) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1045);
								this->attribute->am2_Ai_x = 333;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1046);
							return waitorder(am2_oLiftClear, this, pEmptyCarrierReturn_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1047);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 7, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_LiftDown, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label7: ; /* Step 7 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1048);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 8, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label8: ; /* Step 8 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1049);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 9, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staUp, 1, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label9: ; /* Step 9 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1050);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 10, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B20, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label10: ; /* Step 10 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1051);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 11, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B24, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label11: ; /* Step 11 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1052);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 12, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label12: ; /* Step 12 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1054);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 13, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST12_29, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label13: ; /* Step 13 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1055);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 14, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W36, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label14: ; /* Step 14 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1057);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 15, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label15: ; /* Step 15 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1058);
							if (this->attribute->am2_Ai_x == 333) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1059);
									return waitorder(&(am2_oCarrierInit[1]), this, pEmptyCarrierReturn_arriving, Step 16, am_localargs);
Label16: ; /* Step 16 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1060);
									this->attribute->am2_Ai_pi = 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1061);
									while (this->attribute->am2_Ai_pi <= 10) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1062);
											conv_motor_down(am2_vmMotor[ValidIndex("am_model.am_vmMotor", this->attribute->am2_Ai_pi, 10)]);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1063);
											if (waitfor(ToModelTime(exponential(ValidPtr(am2_stream42, 60, rnstream*), 5), UNITMINUTES), this, pEmptyCarrierReturn_arriving, Step 17, am_localargs) == Delayed)
												return Delayed;
Label17: ; /* Step 17 */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1064);
											conv_motor_up(am2_vmMotor[ValidIndex("am_model.am_vmMotor", this->attribute->am2_Ai_pi, 10)]);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1065);
											this->attribute->am2_Ai_pi += 1;
											EntityChanged(0x00000040);
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1069);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 18, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B19, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label18: ; /* Step 18 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1070);
							return waitorder(am2_oLiftClear, this, pEmptyCarrierReturn_arriving, Step 19, am_localargs);
Label19: ; /* Step 19 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1071);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 20, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_LiftDown, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label20: ; /* Step 20 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1072);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 21, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label21: ; /* Step 21 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1073);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 22, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staUp, 1, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label22: ; /* Step 22 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1074);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 23, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B20, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label23: ; /* Step 23 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1075);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 24, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B24, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label24: ; /* Step 24 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1076);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 25, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label25: ; /* Step 25 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1077);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 26, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST12_29, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label26: ; /* Step 26 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1078);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 27, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W36, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label27: ; /* Step 27 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1079);
							am2_viStorageInit[1] += 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1080);
							if (am2_viStorageInit[1] == CntGetCurConts(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*))) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1081);
									am2_viStorageInit[2] = 1;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1082);
									order(-1, am2_oStorageInit, NULL, NULL); /* Place an order */
								}
							}
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1086);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 28, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B19, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label28: ; /* Step 28 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1087);
							if (LocGetTotEntriesA(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B19, -9999), 38, simloc*)) == 1) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1088);
								this->attribute->am2_Ai_x = 333;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1089);
							return waitorder(am2_oLiftClear, this, pEmptyCarrierReturn_arriving, Step 29, am_localargs);
Label29: ; /* Step 29 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1090);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 30, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_LiftDown, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label30: ; /* Step 30 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1091);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 31, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staDown, 1, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label31: ; /* Step 31 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1092);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 32, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_kLift.am_staUp, 1, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label32: ; /* Step 32 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1093);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 33, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B20, -9999));
							pushppa(this, move_in_loc, Step 1, NULL);
							return Continue; /* go move into territory */
Label33: ; /* Step 33 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1094);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 34, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B24, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label34: ; /* Step 34 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1095);
							if (LocGetTotEntriesA(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B24, -9999), 38, simloc*)) == am2_viReqVeh) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1096);
								order(1, &(am2_oCarrierInit[1]), NULL, NULL); /* Place an order */
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1097);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 35, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B28, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label35: ; /* Step 35 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1098);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 36, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label36: ; /* Step 36 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1099);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 37, am_localargs);
							load_SetDestLoc(this, nextofFunc8(this));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label37: ; /* Step 37 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1100);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 38, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W36, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label38: ; /* Step 38 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1101);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 39, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label39: ; /* Step 39 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1102);
							if (this->attribute->am2_Ai_x == 333) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1103);
									return waitorder(&(am2_oCarrierInit[1]), this, pEmptyCarrierReturn_arriving, Step 40, am_localargs);
Label40: ; /* Step 40 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1104);
									this->attribute->am2_Ai_pi = 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1105);
									while (this->attribute->am2_Ai_pi <= 10) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1106);
											conv_motor_down(am2_vmMotor[ValidIndex("am_model.am_vmMotor", this->attribute->am2_Ai_pi, 10)]);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1107);
											if (waitfor(ToModelTime(exponential(ValidPtr(am2_stream42, 60, rnstream*), 5), UNITMINUTES), this, pEmptyCarrierReturn_arriving, Step 41, am_localargs) == Delayed)
												return Delayed;
Label41: ; /* Step 41 */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1108);
											conv_motor_up(am2_vmMotor[ValidIndex("am_model.am_vmMotor", this->attribute->am2_Ai_pi, 10)]);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1109);
											this->attribute->am2_Ai_pi += 1;
											EntityChanged(0x00000040);
										}
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1113);
							am2_viStorageInit[1] += 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1114);
							if (am2_viStorageInit[1] == CntGetCurConts(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*))) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1115);
									am2_viStorageInit[2] = 1;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1116);
									order(-1, am2_oStorageInit, NULL, NULL); /* Place an order */
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1121);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 42, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label42: ; /* Step 42 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1123);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 43, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B3, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label43: ; /* Step 43 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1124);
							if (this->attribute->am2_Ai_x == 333) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1125);
								return waitorder(&(am2_oCarrierInit[1]), this, pEmptyCarrierReturn_arriving, Step 44, am_localargs);
Label44: ; /* Step 44 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1127);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 45, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B24, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label45: ; /* Step 45 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1128);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 46, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B28, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label46: ; /* Step 46 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1129);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 47, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label47: ; /* Step 47 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1130);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 48, am_localargs);
							load_SetDestLoc(this, nextofFunc9(this));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label48: ; /* Step 48 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1131);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 49, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W36, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label49: ; /* Step 49 */
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1133);
					this->attribute->am2_Ai_Seq = 0;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1137);
			if (this->attribute->am2_Ai_Status == 7777) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1138);
					if (this->attribute->am2_Ai_Line == 1) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1139);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 50, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W35, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label50: ; /* Step 50 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1140);
							return waitorder(am2_olLine1Empty, this, pEmptyCarrierReturn_arriving, Step 51, am_localargs);
Label51: ; /* Step 51 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1141);
							am2_viKickOutQty[3] -= 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1142);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 52, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W36, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label52: ; /* Step 52 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1143);
							if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*)) == 0) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1144);
								this->attribute->am2_Ai_Status = 1;
								EntityChanged(0x00000040);
							}
						}
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1146);
						if (this->attribute->am2_Ai_Line == 2) {
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1147);
								if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST124, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*)) == 0 && am2_viKickOutQty[1] == 0 && LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) == 0) {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1149);
										pushppa(this, pEmptyCarrierReturn_arriving, Step 53, am_localargs);
										pushppa(this, am2_sAttributeStrip, Step 1, NULL);
										return Continue;
Label53: ;
										if (!this->inLeaveProc && this->nextproc) {
											retval = Continue;
											goto LabelRet;
										}
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1150);
										SetString(&this->attribute->am2_As_Type, NULL);
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1151);
										this->attribute->am2_Ai_Type = 99;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1153);
										am2_viKickOutQty[1] += 20;
										EntityChanged(0x01000000);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1154);
										if (OrdGetCurConts(am2_oPressSelect) > 0) {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1155);
											order(1, am2_oPressSelect, am2_pEmptyCarrierLaneSelect, NULL); /* Place an order */
										}
									}
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1160);
			if (LocCompare(VehGetCurLoc(ValidPtr(LdGetVehicle(this), 76, vehicle*)), LocGetQualifier(am_model.am_pf_ohc.am_ST13, -9999)) == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1161);
					if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*)) == 0 && am2_viKDcount[1] < am2_viKDcount[2]) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1162);
							am2_viKDcount[1] += 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1163);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 54, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST14, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label54: ; /* Step 54 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1164);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 55, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST10, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label55: ; /* Step 55 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1165);
							this->attribute->am2_Ai_y = 1;
							EntityChanged(0x00000040);
						}
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1168);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 56, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W36, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label56: ; /* Step 56 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1169);
							if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999), 38, simloc*)) > 0) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1170);
									pushppa(this, pEmptyCarrierReturn_arriving, Step 57, am_localargs);
									load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999));
									pushppa(this, travel_to_loc, Step 1, NULL);
									return Continue; /* go move to location */
Label57: ; /* Step 57 */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1171);
									return waitorder(am2_olCarKickout, this, pEmptyCarrierReturn_arriving, Step 58, am_localargs);
Label58: ; /* Step 58 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1172);
									am2_viKickOutQty[2] -= 1;
									EntityChanged(0x01000000);
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1174);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 59, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST124, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label59: ; /* Step 59 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1175);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 60, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label60: ; /* Step 60 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1176);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 61, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label61: ; /* Step 61 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1177);
							this->attribute->am2_Ai_y = 0;
							EntityChanged(0x00000040);
						}
					}
				}
			}
			else {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1181);
					if (this->attribute->am2_Ai_Status == 1 && LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999), 38, simloc*)) > 0) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1182);
							pushppa(this, pEmptyCarrierReturn_arriving, Step 62, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label62: ; /* Step 62 */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1183);
							return waitorder(am2_olCarKickout, this, pEmptyCarrierReturn_arriving, Step 63, am_localargs);
Label63: ; /* Step 63 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1184);
							am2_viKickOutQty[2] -= 1;
							EntityChanged(0x01000000);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1187);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 64, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST124, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label64: ; /* Step 64 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1188);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 65, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST127, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label65: ; /* Step 65 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1189);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 66, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label66: ; /* Step 66 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1190);
					this->attribute->am2_Ai_y = 0;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1193);
			if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST35, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999), 38, simloc*)) == 0) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1194);
				return waitorder(am2_oEmptyCarrierSpace, this, pEmptyCarrierReturn_arriving, Step 67, am_localargs);
Label67: ; /* Step 67 */
				if (!this->inLeaveProc && this->nextproc) {
					retval = Continue;
					goto LabelRet;
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1195);
			if ((LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST35, -9999), 38, simloc*))) > 0) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1195);
				pushppa(this, pEmptyCarrierReturn_arriving, Step 68, am_localargs);
				load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999));
				pushppa(this, travel_to_loc, Step 1, NULL);
				return Continue; /* go move to location */
Label68: ; /* Step 68 */
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1196);
				if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) > 0 && (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST35, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999), 38, simloc*))) == 0) {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1196);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 69, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label69: ; /* Step 69 */
				}
				else {
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1197);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 70, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label70: ; /* Step 70 */
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1200);
			pushppa(this, pEmptyCarrierReturn_arriving, Step 71, am_localargs);
			load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B3, -9999));
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label71: ; /* Step 71 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1201);
			if (OrdGetCurConts(am2_oEmptyCarrierSpace) > 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1202);
					if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*)) > 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1202);
						order(1, am2_oEmptyCarrierSpace, NULL, OrderCondFunc3); /* Place an order */
					}
					else {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1203);
						order(1, am2_oEmptyCarrierSpace, NULL, OrderCondFunc4); /* Place an order */
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1206);
			if (am2_viPressActive[1] == 1 && am2_viPressActive[2] == 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1207);
					this->attribute->am2_Ai_Press = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1208);
					am2_viEmptyLoad[1][1] += 1;
					EntityChanged(0x01000000);
				}
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1210);
				if (am2_viPressActive[1] == 0 && am2_viPressActive[2] == 1) {
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1211);
						this->attribute->am2_Ai_Press = 2;
						EntityChanged(0x00000040);
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1212);
						am2_viEmptyLoad[2][1] += 1;
						EntityChanged(0x01000000);
					}
				}
				else {
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1215);
						if (am2_viEmptyLoad[2][1] < am2_viEmptyLoad[2][2]) {
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1216);
								this->attribute->am2_Ai_Press = 2;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1217);
								am2_viEmptyLoad[2][1] += 1;
								EntityChanged(0x01000000);
							}
						}
						else {
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1220);
								if (am2_viEmptyLoad[1][1] < am2_viEmptyLoad[1][2]) {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1221);
										this->attribute->am2_Ai_Press = 1;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1222);
										am2_viEmptyLoad[1][1] += 1;
										EntityChanged(0x01000000);
									}
								}
								else {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1225);
										if (am2_viKickOutQty[1] > 0) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1226);
												am2_viKickOutQty[1] -= 1;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1227);
												if (am2_viSTRunGen[1] + am2_viSTRunGen[2] > 0) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1228);
													this->attribute->am2_Ai_Press = 1;
													EntityChanged(0x00000040);
												}
												else {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1229);
													this->nextproc = am2_pEmptyCarrierLaneSelect; /* send to ... */
													EntityChanged(W_LOAD);
													retval = Continue;
													goto LabelRet;
												}
											}
										}
										else {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1231);
											return waitorder(am2_oPressSelect, this, pEmptyCarrierReturn_arriving, Step 72, am_localargs);
Label72: ; /* Step 72 */
											if (!this->inLeaveProc && this->nextproc) {
												retval = Continue;
												goto LabelRet;
											}
										}
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1232);
										am2_viEmptyLoad[ValidIndex("am_model.am_viEmptyLoad", this->attribute->am2_Ai_Press, 2)][1] += 1;
										EntityChanged(0x01000000);
									}
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1241);
			if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) < (LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) / 2) && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999), 38, simloc*)) < 20 && ASIclock > ToModelTime(86400, UNITSECONDS)) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1242);
					if (OrdGetCurConts(am2_olEmptyHold) > 0 && am2_Vi_ZoneEmptyFlush == 0) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1243);
							this->attribute->am2_Ai_Zone = ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Zone;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1244);
							this->attribute->am2_Ai_Row = ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Row;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1245);
							am_localargs->ls1 = 0;
							ListCopy(LoadList, am_localargs->ls1, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
							for (am_localargs->lv1 = (am_localargs->ls1) ? (am_localargs->ls1)->first : NULL; am_localargs->lv1; am_localargs->lv1 = am_localargs->lv1->next) {
								am2_vlptrEmpty = am_localargs->lv1->item;
								{
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1246);
										am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] += 1;
										EntityChanged(0x01000000);
									}
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1247);
										ValidPtr(am2_vlptrEmpty, 32, load*)->attribute->am2_Ai_Status = 1;
										EntityChanged(0x00000040);
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls1); /* End of for each */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1249);
							ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Seq = 9999;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1250);
							am2_Vi_ZoneEmptyFlush = 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1251);
							orderload(am2_olEmptyHold, NULL, ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
						}
					}
				}
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1257);
				if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) < (LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) / 2) && am2_viKickOutQty[2] == 0 && ASIclock > ToModelTime(86400, UNITSECONDS)) {
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1258);
						{
							int32 tempint = order(20, am2_olCarKickout, NULL, NULL); /* Place an order */
							if (tempint > 0) backorder(tempint, am2_olCarKickout, NULL, NULL);	/* Place a backorder */
						}
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1259);
						am2_viKickOutQty[2] = 20;
						EntityChanged(0x01000000);
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1262);
			if (this->attribute->am2_Ai_Press == 1) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1263);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 73, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST_B5, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label73: ; /* Step 73 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1265);
					if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B6, -9999), 38, simloc*)) == 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1266);
						this->attribute->am2_Ai_pi = 1;
						EntityChanged(0x00000040);
					}
					else {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1268);
							if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B7, -9999), 38, simloc*)) == 0) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1269);
								this->attribute->am2_Ai_pi = 2;
								EntityChanged(0x00000040);
							}
							else {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1271);
									if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B6, -9999), 38, simloc*)) > 0) {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1272);
										this->attribute->am2_Ai_pi = 1;
										EntityChanged(0x00000040);
									}
									else {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1274);
											if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST_B10, -9999), 38, simloc*)) > 0) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1275);
												this->attribute->am2_Ai_pi = 2;
												EntityChanged(0x00000040);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1276);
												this->attribute->am2_Ai_pi = 1;
												EntityChanged(0x00000040);
											}
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1281);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 74, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][4][1]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label74: ; /* Step 74 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1282);
					pushppa(this, pEmptyCarrierReturn_arriving, Step 75, am_localargs);
					load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][1]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label75: ; /* Step 75 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1284);
					if (OrdGetCurConts(ValidPtr(&(am2_olRobHandshake[ValidIndex("am_model.am_olRobHandshake", this->attribute->am2_Ai_pi, 2)]), 40, ordlist*)) > 0) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1285);
						order(1, &(am2_olRobHandshake[ValidIndex("am_model.am_olRobHandshake", this->attribute->am2_Ai_pi, 2)]), NULL, NULL); /* Place an order */
					}
				}
			}
			else {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1288);
				if (this->attribute->am2_Ai_Press == 2) {
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1290);
						pushppa(this, pEmptyCarrierReturn_arriving, Step 76, am_localargs);
						load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST4_3, -9999));
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label76: ; /* Step 76 */
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1292);
						if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_5, -9999), 38, simloc*)) == LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_5, -9999), 38, simloc*)) && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_1, -9999), 38, simloc*)) == LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_1, -9999), 38, simloc*))) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1293);
							return waitorder(am2_olLoadEntry, this, pEmptyCarrierReturn_arriving, Step 77, am_localargs);
Label77: ; /* Step 77 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1294);
						if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_6, -9999), 38, simloc*)) == 0) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1295);
							this->attribute->am2_Ai_pi = 1;
							EntityChanged(0x00000040);
						}
						else {
							{
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1297);
								if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_2, -9999), 38, simloc*)) == 0) {
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1298);
									this->attribute->am2_Ai_pi = 2;
									EntityChanged(0x00000040);
								}
								else {
									{
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1300);
										if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_5, -9999), 38, simloc*)) == 0) {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1301);
											this->attribute->am2_Ai_pi = 1;
											EntityChanged(0x00000040);
										}
										else {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1302);
											this->attribute->am2_Ai_pi = 2;
											EntityChanged(0x00000040);
										}
									}
								}
							}
						}
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1311);
						pushppa(this, pEmptyCarrierReturn_arriving, Step 78, am_localargs);
						load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][4][2]);
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label78: ; /* Step 78 */
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1312);
						pushppa(this, pEmptyCarrierReturn_arriving, Step 79, am_localargs);
						load_SetDestLoc(this, am2_vlocptrSTLoad[ValidIndex("am_model.am_vlocptrSTLoad", this->attribute->am2_Ai_pi, 2)][3][2]);
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label79: ; /* Step 79 */
					}
					{
						AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1314);
						if (OrdGetCurConts(ValidPtr(&(am2_olRobHandshake[ValidIndex("am_model.am_olRobHandshake", this->attribute->am2_Ai_pi, 2)]), 40, ordlist*)) > 0) {
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor, 1315);
							order(1, &(am2_olRobHandshake[ValidIndex("am_model.am_olRobHandshake", this->attribute->am2_Ai_pi, 2)]), NULL, NULL); /* Place an order */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls1);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierReturn", pEmptyCarrierReturn_arriving, localactor);
	return retval;
} /* end of pEmptyCarrierReturn_arriving */

static int32
pEmptyCarrierManagement_arriving(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv2; /* 'for each' loop variable */
		AMLoadList* ls2; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", localactor);
	AMDebuggerParams("base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1321);
			return waitorder(am2_oEmptyCarrierManagement, this, pEmptyCarrierManagement_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1322);
			am2_viEmptyBufferAvailable = LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999), 38, simloc*)) - LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999), 38, simloc*));
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1323);
			am2_viEmptyBufferAvailable = am2_viEmptyBufferAvailable + am2_viKDcount[2] - am2_viKDcount[1];
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1324);
			am2_viEmptyBufferAvailable = am2_viEmptyBufferAvailable + (LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999), 38, simloc*)) + LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST35, -9999), 38, simloc*)) + LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) + LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*))) - (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST136, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST35, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST128, -9999), 38, simloc*)));
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1328);
			if (am2_viEmptyBufferAvailable > 0 && OrdGetCurConts(am2_olEmptyHold) > 0) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1329);
					this->attribute->am2_Ai_Zone = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1330);
					while (this->attribute->am2_Ai_Zone <= 3) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1331);
							this->attribute->am2_Ai_Row = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1332);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && StringCompare(am2_vsWeldStatus, "PlannedDT") == 0) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1333);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1334);
											if (ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == 99 && ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == 99) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1335);
													am_localargs->ls2 = 0;
													ListCopy(LoadList, am_localargs->ls2, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													for (am_localargs->lv2 = (am_localargs->ls2) ? (am_localargs->ls2)->first : NULL; am_localargs->lv2; am_localargs->lv2 = am_localargs->lv2->next) {
														am2_vlptrEmpty = am_localargs->lv2->item;
														{
															{
																AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1336);
																am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] += 1;
																EntityChanged(0x01000000);
															}
															{
																AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1337);
																ValidPtr(am2_vlptrEmpty, 32, load*)->attribute->am2_Ai_Status = 1;
																EntityChanged(0x00000040);
															}
														}
													}
													ListRemoveAllAndFree(LoadList, am_localargs->ls2); /* End of for each */
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1339);
													ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Seq = 9999;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1341);
													orderload(am2_olEmptyHold, NULL, ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1343);
													am2_viEmptyBufferAvailable -= ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1344);
													return waitorder(am2_oEmptyBufferCleanUp, this, pEmptyCarrierManagement_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
													if (!this->inLeaveProc && this->nextproc) {
														retval = Continue;
														goto LabelRet;
													}
												}
											}
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1347);
									if (am2_viEmptyBufferAvailable > 0) {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1348);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
									else {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1349);
										break;
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1351);
							if (am2_viEmptyBufferAvailable > 0) {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1352);
								this->attribute->am2_Ai_Zone += 1;
								EntityChanged(0x00000040);
							}
							else {
								AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1353);
								break;
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor, 1356);
			this->nextproc = am2_pEmptyCarrierManagement; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls2);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pEmptyCarrierManagement", pEmptyCarrierManagement_arriving, localactor);
	return retval;
} /* end of pEmptyCarrierManagement_arriving */

static int32
pf_ohc_ST5_5_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_5", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_5", pf_ohc_ST5_5_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_5", pf_ohc_ST5_5_staleave, localactor, 1360);
			order(1, am2_olLoadEntry, NULL, NULL); /* Place an order */
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_5", pf_ohc_ST5_5_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST5_5_staleave */

static int32
pf_ohc_ST5_1_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_1", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_1", pf_ohc_ST5_1_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_1", pf_ohc_ST5_1_staleave, localactor, 1363);
			order(1, am2_olLoadEntry, NULL, NULL); /* Place an order */
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_1", pf_ohc_ST5_1_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST5_1_staleave */

static int32
pf_ohc_LS_24_photoblocked(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.LS_24", localactor);
	AMDebuggerParams("base.pf_ohc.LS_24", pf_ohc_LS_24_photoblocked, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.LS_24", pf_ohc_LS_24_photoblocked, localactor, 1367);
			if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_W35, -9999), 38, simloc*)) > (LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_W35, -9999), 38, simloc*)) - 8) && am2_viKickOutQty[3] == 0) {
				{
					AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.LS_24", pf_ohc_LS_24_photoblocked, localactor, 1369);
					{
						int32 tempint = order(20, am2_olLine1Empty, NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, am2_olLine1Empty, NULL, NULL);	/* Place a backorder */
					}
				}
				{
					AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.LS_24", pf_ohc_LS_24_photoblocked, localactor, 1370);
					am2_viKickOutQty[3] = 20;
					EntityChanged(0x01000000);
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.LS_24", pf_ohc_LS_24_photoblocked, localactor);
	return retval;
} /* end of pf_ohc_LS_24_photoblocked */

static int32
am_sAttributeStrip(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Subroutine", "base.sAttributeStrip", localactor);
	AMDebuggerParams("base.sAttributeStrip", am_sAttributeStrip, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1377);
			this->attribute->am2_Ai_LotQty = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1379);
			this->attribute->am2_Ai_Seq = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1380);
			this->attribute->am2_Ai_pi = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1381);
			this->attribute->am2_Ai_CarQty = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1382);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1383);
			this->attribute->am2_Ai_Zone = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1384);
			this->attribute->am2_Ai_Row = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1385);
			this->attribute->am2_Ai_Line = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1386);
			this->attribute->am2_Ai_x = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1387);
			this->attribute->am2_Ai_y = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1389);
			SetString(&this->attribute->am2_As_Message, NULL);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1390);
			SetString(&this->attribute->am2_As_CurrentLocation, NULL);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor, 1391);
			SetString(&this->attribute->am2_As_NextLocation, NULL);
			EntityChanged(0x00000040);
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Subroutine", "base.sAttributeStrip", am_sAttributeStrip, localactor);
	return retval;
} /* end of am_sAttributeStrip */

static int32
P_StampingShift_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.P_StampingShift", localactor);
	AMDebuggerParams("base.P_StampingShift", P_StampingShift_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1399);
			return waitorder(am2_olWarmUp, this, P_StampingShift_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1400);
			am2_viStpDay = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1401);
			while (am2_viStpDay <= 5) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1402);
					am2_viStpShift = 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1403);
					while (am2_viStpShift <= 3) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1404);
							this->attribute->am2_Ai_Seq = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1405);
							uprsrc(am2_rBLine);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1406);
							uprsrc(am2_rCLine);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1407);
							uprsrc(am2_rStampingShift);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1408);
							uprsrc(&(am2_R_Dummy[3]));
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1409);
							uprsrc(&(am2_R_Dummy[4]));
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1411);
							while (this->attribute->am2_Ai_Seq <= 9) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1412);
									{
										char* pArg1 = "ST: Day";
										char* pArg2 = " ";
										int32 pArg3 = am2_viStpDay;
										char* pArg4 = " ";
										char* pArg5 = "Shift";
										char* pArg6 = " ";
										int32 pArg7 = am2_viStpShift;
										char* pArg8 = " ";
										int32 pArg9 = this->attribute->am2_Ai_Seq;
										char* pArg10 = " ";
										char* pArg11 = am2_vsStpShift[ValidIndex("am_model.am_vsStpShift", this->attribute->am2_Ai_Seq, 9)][ValidIndex("am_model.am_vsStpShift", am2_viStpShift, 3)];

										updatelabel(am2_Lb_StampShiftStatus, "%s%s%d%s%s%s%d%s%d%s%s", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1414);
									if (this->attribute->am2_Ai_Seq == 2 || this->attribute->am2_Ai_Seq == 4 || this->attribute->am2_Ai_Seq == 6 || this->attribute->am2_Ai_Seq == 8) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1415);
											uprsrc(am2_rBLine);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1416);
											uprsrc(am2_rCLine);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1417);
											uprsrc(am2_rStampingShift);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1418);
											uprsrc(&(am2_R_Dummy[3]));
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1419);
											uprsrc(&(am2_R_Dummy[4]));
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1421);
											if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 2) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1422);
												MonSetState(am2_vsmPressCondition, str2StatePtr("TwoPressRunningSpo"), this);
												EntityChanged(0x00000100);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1423);
												if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 1) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1424);
													MonSetState(am2_vsmPressCondition, str2StatePtr("OnePressRunningSpo"), this);
													EntityChanged(0x00000100);
												}
												else {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1425);
													if (am2_viSTRunGen[1] + am2_viSTRunGen[2] == 0) {
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1426);
														MonSetState(am2_vsmPressCondition, str2StatePtr("NoPressRunningSpo"), this);
														EntityChanged(0x00000100);
													}
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1429);
											if (am2_Vi_Down[3] == 1) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1430);
													if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][2][1], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][2][1], 38, simloc*)) > 0 && LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][3][1], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][3][1], 38, simloc*)) < 2) {
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1431);
														MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("NoCarriers"), this);
														EntityChanged(0x00020000);
													}
													else {
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1432);
														if (RscGetCurConts(am2_rBLine) == 0) {
															AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1433);
															MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("NoScheduledRuns"), this);
															EntityChanged(0x00020000);
														}
														else {
															AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1434);
															if (am2_viBlocked[1][1] + am2_viBlocked[1][2] > 0) {
																AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1435);
																MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Blocked"), this);
																EntityChanged(0x00020000);
															}
															else {
																AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1436);
																MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Production"), this);
																EntityChanged(0x00020000);
															}
														}
													}
												}
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1438);
												if (am2_Vi_Down[3] == 0) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1438);
													MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Down"), this);
													EntityChanged(0x00020000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1440);
											if (am2_Vi_Down[4] == 1) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1441);
													if (LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][2][2], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][2][2], 38, simloc*)) > 0 && LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[1][3][2], 38, simloc*)) + LocGetCurConts(ValidPtr(am2_vlocptrSTLoad[2][3][2], 38, simloc*)) < 2) {
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1442);
														MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("NoCarriers"), this);
														EntityChanged(0x00020000);
													}
													else {
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1443);
														if (RscGetCurConts(am2_rCLine) == 0) {
															AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1444);
															MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("NoScheduledRuns"), this);
															EntityChanged(0x00020000);
														}
														else {
															AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1445);
															if (am2_viBlocked[2][1] + am2_viBlocked[2][2] > 0) {
																AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1446);
																MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Blocked"), this);
																EntityChanged(0x00020000);
															}
															else {
																AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1447);
																MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Production"), this);
																EntityChanged(0x00020000);
															}
														}
													}
												}
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1449);
												if (am2_Vi_Down[4] == 0) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1449);
													MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Down"), this);
													EntityChanged(0x00020000);
												}
											}
										}
									}
									else {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1452);
										if (this->attribute->am2_Ai_Seq == 1 || this->attribute->am2_Ai_Seq == 3 || this->attribute->am2_Ai_Seq == 5 || this->attribute->am2_Ai_Seq == 7 || this->attribute->am2_Ai_Seq == 9) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1453);
												downrsrc(am2_rBLine);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1454);
												downrsrc(am2_rCLine);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1455);
												downrsrc(am2_rStampingShift);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1456);
												downrsrc(&(am2_R_Dummy[3]));
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1457);
												downrsrc(&(am2_R_Dummy[4]));
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1458);
												MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("PlannedDT"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1459);
												MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("PlannedDT"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1460);
												MonSetState(am2_vsmPressCondition, str2StatePtr("PlannedDT"), this);
												EntityChanged(0x00000100);
											}
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1463);
									{
										char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*))), am_model.$sys);

										updatelabel(&(am2_lblState[3]), "%s", pArg1);
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1464);
									{
										char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*))), am_model.$sys);

										updatelabel(&(am2_lblState[4]), "%s", pArg1);
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1466);
									if (waitfor(ToModelTime(am2_vrStpShiftDuration[ValidIndex("am_model.am_vrStpShiftDuration", this->attribute->am2_Ai_Seq, 9)][ValidIndex("am_model.am_vrStpShiftDuration", am2_viStpShift, 3)], UNITMINUTES), this, P_StampingShift_arriving, Step 3, am_localargs) == Delayed)
										return Delayed;
Label3: ; /* Step 3 */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1467);
									this->attribute->am2_Ai_Seq += 1;
									EntityChanged(0x00000040);
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1469);
							am2_viStpShift += 1;
							EntityChanged(0x01000000);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1471);
					am2_viStpDay += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1472);
					if (am2_viStpDay == 6) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor, 1472);
						am2_viStpDay = 1;
						EntityChanged(0x01000000);
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.P_StampingShift", P_StampingShift_arriving, localactor);
	return retval;
} /* end of P_StampingShift_arriving */

static int32
P_WeldShift_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.P_WeldShift", localactor);
	AMDebuggerParams("base.P_WeldShift", P_WeldShift_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1481);
			return waitorder(am2_olWarmUp, this, P_WeldShift_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1482);
			if (CurProcIndex() == 1) {
				AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1482);
				{
					char* pArg1 = "Week";
					char* pArg2 = "\t";
					char* pArg3 = "Day";
					char* pArg4 = "\t";
					char* pArg5 = "Shift";
					char* pArg6 = "\t";
					char* pArg7 = "Weld L1";
					char* pArg8 = "\t";
					char* pArg9 = "Weld L2";

					fprintf(ffp_base_arc_RESULTS_out_dat->fp, "%s%s%s%s%s%s%s%s%s\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1484);
			am2_viWeek[ValidIndex("am_model.am_viWeek", CurProcIndex(), 2)] = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1485);
			am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)] = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1486);
			while (am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)] <= 5) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1487);
					am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)] = 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1488);
					while (am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)] <= 2) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1489);
							order(-1, am2_O_WeldOutputTarget, NULL, NULL); /* Place an order */
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1490);
							this->attribute->am2_Ai_Seq = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1491);
							uprsrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]));
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1492);
							uprsrc(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]));
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1493);
							uprsrc(&(am2_R_WeldShift[ValidIndex("am_model.am_R_WeldShift", CurProcIndex(), 2)]));
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1495);
							while (this->attribute->am2_Ai_Seq <= 10) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1496);
									if (this->attribute->am2_Ai_Seq == 1) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1497);
											if (CurProcIndex() == 1) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1498);
													am2_Vi_shiftProd[1] = 0;
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1499);
													am2_Vi_shiftProd[2] = 0;
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1501);
											if (CurProcIndex() == 2) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1502);
													am2_Vi_shiftProd[3] = 0;
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1503);
													am2_Vi_shiftProd[4] = 0;
													EntityChanged(0x01000000);
												}
											}
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1507);
									{
										char* pArg1 = "SPO: W";
										char* pArg2 = " ";
										int32 pArg3 = am2_viWeek[ValidIndex("am_model.am_viWeek", CurProcIndex(), 2)];
										char* pArg4 = " ";
										char* pArg5 = "D";
										char* pArg6 = " ";
										int32 pArg7 = am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)];
										char* pArg8 = " ";
										char* pArg9 = "Shift";
										char* pArg10 = " ";
										int32 pArg11 = am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)];
										char* pArg12 = " ";
										int32 pArg13 = this->attribute->am2_Ai_Seq;
										char* pArg14 = " ";
										char* pArg15 = am2_vsWeldShift[ValidIndex("am_model.am_vsWeldShift", this->attribute->am2_Ai_Seq, 10)][ValidIndex("am_model.am_vsWeldShift", am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)], 3)][ValidIndex("am_model.am_vsWeldShift", CurProcIndex(), 2)];

										updatelabel(&(am2_Lb_WeldShiftStatus[ValidIndex("am_model.am_Lb_WeldShiftStatus", CurProcIndex(), 2)]), "%s%s%d%s%s%s%d%s%s%s%d%s%d%s%s", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13, pArg14, pArg15);
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1509);
									if (this->attribute->am2_Ai_Seq == 2 || this->attribute->am2_Ai_Seq == 4 || this->attribute->am2_Ai_Seq == 6 || this->attribute->am2_Ai_Seq == 8) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1510);
											if (CurProcIndex() == 1) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1510);
												uprsrc(am2_rST13Inspector);
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1511);
											uprsrc(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]));
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1512);
											uprsrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]));
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1513);
											uprsrc(&(am2_R_WeldShift[ValidIndex("am_model.am_R_WeldShift", CurProcIndex(), 2)]));
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1514);
											SetString(&am2_vsWeldStatus, "Production");
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1515);
											if (am2_Vi_Down[ValidIndex("am_model.am_Vi_Down", CurProcIndex(), 10)] == 1) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1516);
													if (CurProcIndex() == 1) {
														{
															AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1517);
															if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[1]), 40, ordlist*)) + OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[2]), 40, ordlist*)) > 0) {
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1518);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Starved"), this);
																	EntityChanged(0x00020000);
																}
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1519);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[ValidIndex("am_model.am_R_LineStarved", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Starved"), this);
																	EntityChanged(0x00020000);
																}
															}
															else {
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1522);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Production"), this);
																	EntityChanged(0x00020000);
																}
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1523);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[ValidIndex("am_model.am_R_LineStarved", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Production"), this);
																	EntityChanged(0x00020000);
																}
															}
														}
													}
												}
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1526);
													if (CurProcIndex() == 2) {
														{
															AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1527);
															if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[3]), 40, ordlist*)) + OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[4]), 40, ordlist*)) > 0) {
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1528);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Starved"), this);
																	EntityChanged(0x00020000);
																}
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1529);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[ValidIndex("am_model.am_R_LineStarved", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Starved"), this);
																	EntityChanged(0x00020000);
																}
															}
															else {
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1532);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Production"), this);
																	EntityChanged(0x00020000);
																}
																{
																	AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1533);
																	MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[ValidIndex("am_model.am_R_LineStarved", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Production"), this);
																	EntityChanged(0x00020000);
																}
															}
														}
													}
												}
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1537);
												if (am2_Vi_Down[ValidIndex("am_model.am_Vi_Down", CurProcIndex(), 10)] == 0) {
													{
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1538);
														MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Down"), this);
														EntityChanged(0x00020000);
													}
													{
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1539);
														MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[ValidIndex("am_model.am_R_LineStarved", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("Down"), this);
														EntityChanged(0x00020000);
													}
												}
											}
										}
									}
									else {
										AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1542);
										if (this->attribute->am2_Ai_Seq == 1 || this->attribute->am2_Ai_Seq == 3 || this->attribute->am2_Ai_Seq == 5 || this->attribute->am2_Ai_Seq == 7 || this->attribute->am2_Ai_Seq == 9) {
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1543);
												if (CurProcIndex() == 1) {
													{
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1544);
														downrsrc(am2_rST13Inspector);
													}
													{
														AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1545);
														order(1, am2_oEmptyCarrierManagement, NULL, NULL); /* Place an order */
													}
												}
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1547);
												downrsrc(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]));
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1548);
												downrsrc(&(am2_R_Dummy[ValidIndex("am_model.am_R_Dummy", CurProcIndex(), 5)]));
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1549);
												downrsrc(&(am2_R_WeldShift[ValidIndex("am_model.am_R_WeldShift", CurProcIndex(), 2)]));
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1550);
												SetString(&am2_vsWeldStatus, "PlannedDT");
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1551);
												MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[ValidIndex("am_model.am_R_Weld", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("PlannedDT"), this);
												EntityChanged(0x00020000);
											}
											{
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1552);
												MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[ValidIndex("am_model.am_R_LineStarved", CurProcIndex(), 2)]), 50, resource*)), str2StatePtr("PlannedDT"), this);
												EntityChanged(0x00020000);
											}
										}
										else {
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1554);
											if (this->attribute->am2_Ai_Seq == 10 && CurProcIndex() == 1) {
												{
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1555);
													uprsrc(am2_rST13Inspector);
												}
											}
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1559);
									{
										char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[1], 50, resource*))), am_model.$sys);

										updatelabel(&(am2_lblState[1]), "%s", pArg1);
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1560);
									{
										char* pArg1 = rel_actorname(MonGetState(RscGetMonitor(ValidPtr(am2_vrscDowntime[2], 50, resource*))), am_model.$sys);

										updatelabel(&(am2_lblState[2]), "%s", pArg1);
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1562);
									if (waitfor(ToModelTime(am2_vrWeldShiftDuration[ValidIndex("am_model.am_vrWeldShiftDuration", this->attribute->am2_Ai_Seq, 10)][ValidIndex("am_model.am_vrWeldShiftDuration", am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)], 3)][ValidIndex("am_model.am_vrWeldShiftDuration", CurProcIndex(), 2)], UNITMINUTES), this, P_WeldShift_arriving, Step 3, am_localargs) == Delayed)
										return Delayed;
Label3: ; /* Step 3 */
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1564);
									if (this->attribute->am2_Ai_Seq == 10 && CurProcIndex() == 1) {
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1565);
											{
												int32 pArg1 = am2_viWeek[ValidIndex("am_model.am_viWeek", CurProcIndex(), 2)];
												char* pArg2 = "\t";
												int32 pArg3 = am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)];
												char* pArg4 = "\t";
												int32 pArg5 = am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)];
												char* pArg6 = "\t";
												int32 pArg7 = am2_Vi_shiftProd[1];
												char* pArg8 = "\t";
												int32 pArg9 = am2_Vi_shiftProd[3];
												char* pArg10 = "\t";

												fprintf(ffp_base_arc_RESULTS_out_dat->fp, "%d%s%d%s%d%s%d%s%d%s\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10);
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1566);
											tabulate(&(am2_T_Output[1]), am2_Vi_shiftProd[1]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1567);
											tabulate(&(am2_T_Output[2]), am2_Vi_shiftProd[2]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1568);
											tabulate(&(am2_T_Output[3]), am2_Vi_shiftProd[3]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1569);
											tabulate(&(am2_T_Output[4]), am2_Vi_shiftProd[4]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1571);
											if (am2_Vi_shiftProd[1] < am2_Vi_WeldOutputTarget[1]) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1572);
												am2_Vi_WeldGap[1] = am2_Vi_WeldGap[1] + (am2_Vi_WeldOutputTarget[1] - am2_Vi_shiftProd[1]);
												EntityChanged(0x01000000);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1573);
												if (am2_Vi_shiftProd[1] > am2_Vi_WeldOutputTarget[1]) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1574);
													am2_Vi_WeldGap[1] = am2_Vi_WeldGap[1] - (am2_Vi_shiftProd[1] - am2_Vi_WeldOutputTarget[1]);
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1576);
											if (am2_Vi_shiftProd[2] < am2_Vi_WeldOutputTarget[1]) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1577);
												am2_Vi_WeldGap[2] = am2_Vi_WeldGap[2] + (am2_Vi_WeldOutputTarget[1] - am2_Vi_shiftProd[2]);
												EntityChanged(0x01000000);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1578);
												if (am2_Vi_shiftProd[2] > am2_Vi_WeldOutputTarget[1]) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1579);
													am2_Vi_WeldGap[2] = am2_Vi_WeldGap[2] - (am2_Vi_shiftProd[2] - am2_Vi_WeldOutputTarget[1]);
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1581);
											if (am2_Vi_shiftProd[3] < am2_Vi_WeldOutputTarget[2]) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1582);
												am2_Vi_WeldGap[3] = am2_Vi_WeldGap[3] + (am2_Vi_WeldOutputTarget[2] - am2_Vi_shiftProd[3]);
												EntityChanged(0x01000000);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1583);
												if (am2_Vi_shiftProd[3] > am2_Vi_WeldOutputTarget[2]) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1584);
													am2_Vi_WeldGap[3] = am2_Vi_WeldGap[3] - (am2_Vi_shiftProd[3] - am2_Vi_WeldOutputTarget[2]);
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1586);
											if (am2_Vi_shiftProd[4] < am2_Vi_WeldOutputTarget[2]) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1587);
												am2_Vi_WeldGap[4] = am2_Vi_WeldGap[4] + (am2_Vi_WeldOutputTarget[2] - am2_Vi_shiftProd[4]);
												EntityChanged(0x01000000);
											}
											else {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1588);
												if (am2_Vi_shiftProd[4] > am2_Vi_WeldOutputTarget[2]) {
													AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1589);
													am2_Vi_WeldGap[4] = am2_Vi_WeldGap[4] - (am2_Vi_shiftProd[4] - am2_Vi_WeldOutputTarget[2]);
													EntityChanged(0x01000000);
												}
											}
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1591);
											tabulate(&(am2_T_WeldGap[1]), am2_Vi_WeldGap[1]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1592);
											tabulate(&(am2_T_WeldGap[2]), am2_Vi_WeldGap[2]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1593);
											tabulate(&(am2_T_WeldGap[3]), am2_Vi_WeldGap[3]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1594);
											tabulate(&(am2_T_WeldGap[4]), am2_Vi_WeldGap[4]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1596);
											tabulate(&(am2_T_Changeover[1]), am2_Vi_Changeover[1]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1597);
											am2_Vi_Changeover[1] = 0;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1598);
											tabulate(&(am2_T_Changeover[2]), am2_Vi_Changeover[2]);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1599);
											am2_Vi_Changeover[2] = 0;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1601);
											tabulate(am2_tRecircCount, am2_viRecircCount);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1602);
											am2_viRecircCount = 0;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1603);
											tabulate(am2_tABayCleanUp, am2_viABayCleanUp);	/* Tabulate the value */
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1604);
											am2_viABayCleanUp = 0;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1606);
											if (am2_Vi_shiftProd[1] == 0 || am2_Vi_shiftProd[2] == 0 || am2_Vi_shiftProd[3] == 0 || am2_Vi_shiftProd[4] == 0) {
												AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1606);
												{
													int result = deccount(am2_C_Error, 1, this, P_WeldShift_arriving, Step 4, am_localargs);
													if (result != Continue) return result;
Label4: ; /* Step 4 */
												}
											}
										}
									}
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1608);
									this->attribute->am2_Ai_Seq += 1;
									EntityChanged(0x00000040);
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1610);
							am2_viWeldShift[ValidIndex("am_model.am_viWeldShift", CurProcIndex(), 2)] += 1;
							EntityChanged(0x01000000);
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1613);
					am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)] += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1614);
					if (CurProcIndex() == 1) {
						AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1614);
						am2_viTotalDay += 1;
						EntityChanged(0x01000000);
					}
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1615);
					if (am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)] == 6) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1616);
							am2_viWeek[ValidIndex("am_model.am_viWeek", CurProcIndex(), 2)] += 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor, 1617);
							am2_viWeldDay[ValidIndex("am_model.am_viWeldDay", CurProcIndex(), 2)] = 1;
							EntityChanged(0x01000000);
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.P_WeldShift", P_WeldShift_arriving, localactor);
	return retval;
} /* end of P_WeldShift_arriving */

static int32
pNonSpoPartMgt_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", localactor);
	AMDebuggerParams("base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1624);
			return waitorder(am2_olWarmUp, this, pNonSpoPartMgt_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1625);
			while (1 == 1) {
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1626);
					if (getrsrc(&(am2_R_WeldShift[1]), 1, this, pNonSpoPartMgt_arriving, Step 3, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1627);
					if (waitfor(ToModelTime(1, UNITHOURS), this, pNonSpoPartMgt_arriving, Step 4, am_localargs) == Delayed)
						return Delayed;
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1628);
					freersrc(&(am2_R_WeldShift[1]), 1, this);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1629);
					this->attribute->am2_Ai_Seq = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1630);
					while (this->attribute->am2_Ai_Seq <= 50) {
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1631);
							if (am2_viModelOther[ValidIndex("am_model.am_viModelOther", this->attribute->am2_Ai_Seq, 50)] == CurProcIndex()) {
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1632);
									am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_Seq, 50)] -= am2_viDailyTotal[ValidIndex("am_model.am_viDailyTotal", CurProcIndex(), 20)] / ((am2_viProductionMin[1][1] + am2_viProductionMin[2][1]) / 60);
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1633);
									{
										char* pArg1 = "TrigQty->";
										char* pArg2 = " ";
										int32 pArg3 = am2_viTriggerQty2[ValidIndex("am_model.am_viTriggerQty2", this->attribute->am2_Ai_Seq, 50)];
										char* pArg4 = " ";
										char* pArg5 = "RunQty->";
										char* pArg6 = " ";
										int32 pArg7 = am2_viSTRunOutQty2[ValidIndex("am_model.am_viSTRunOutQty2", this->attribute->am2_Ai_Seq, 50)];
										char* pArg8 = " ";
										char* pArg9 = "Inv->";
										char* pArg10 = " ";
										int32 pArg11 = am2_viInitQty2[ValidIndex("am_model.am_viInitQty2", this->attribute->am2_Ai_Seq, 50)];

										updatelabel(&(am2_Lb_Inv[ValidIndex("am_model.am_Lb_Inv", this->attribute->am2_Ai_Seq, 50)]), "%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
									}
								}
							}
						}
						{
							AMDebugger("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor, 1635);
							this->attribute->am2_Ai_Seq += 1;
							EntityChanged(0x00000040);
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pNonSpoPartMgt", pNonSpoPartMgt_arriving, localactor);
	return retval;
} /* end of pNonSpoPartMgt_arriving */

static int32
pWarmUp_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.pWarmUp", localactor);
	AMDebuggerParams("base.pWarmUp", pWarmUp_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pWarmUp", pWarmUp_arriving, localactor, 1641);
			if (waitfor(ToModelTime(24, UNITHOURS), this, pWarmUp_arriving, Step 2, am_localargs) == Delayed)
				return Delayed;
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pWarmUp", pWarmUp_arriving, localactor, 1642);
			order(-1, am2_olWarmUp, NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.pWarmUp", pWarmUp_arriving, localactor, 1643);
			{
				char* pArg1 = "Warm-Up Complete";

				message("%s", pArg1);
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.pWarmUp", pWarmUp_arriving, localactor);
	return retval;
} /* end of pWarmUp_arriving */

static int32
model_initialize()
{
	{
		{
			am2_viNumVeh = 0;
			EntityChanged(0x01000000);
		}
		{
			am2_viNumZones = 3;
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_1, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_2, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][3] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][4] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][5] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][6] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][7] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_7, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][8] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_8, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][9] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_9, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][10] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_10, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][11] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_11, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][12] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_12, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][13] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_13, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][14] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_14, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][15] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_15, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][16] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_16, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][17] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_17, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][18] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_18, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][19] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_19, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[1][20] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_20, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_viZoneLaneMax[1] = 20;
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][9] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_21, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_22, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_23, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][3] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_24, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][4] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_25, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][5] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_26, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][6] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_27, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][7] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_28, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[3][8] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_29, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_viZoneLaneMax[3] = 9;
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[2][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_30, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[2][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_31, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[2][3] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_32, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[2][4] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_33, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[2][5] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_34, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrLane[2][6] = LocGetQualifier(am_model.am_pf_ohc.am_ST12_35, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_viZoneLaneMax[2] = 6;
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][1], "12_1");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][2], "12_2");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][3], "12_3");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][4], "12_4");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][5], "12_5");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][6], "12_6");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][7], "12_7");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][8], "12_8");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][9], "12_9");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][10], "12_10");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][11], "12_11");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][12], "12_12");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][13], "12_13");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][14], "12_14");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][15], "12_15");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][16], "12_16");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][17], "12_17");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][18], "12_18");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][19], "12_19");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[1][20], "12_20");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][9], "12_21");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][1], "12_22");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][2], "12_23");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][3], "12_24");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][4], "12_25");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][5], "12_26");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][6], "12_27");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][7], "12_28");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[3][8], "12_29");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[2][1], "12_30");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[2][2], "12_31");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[2][3], "12_32");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[2][4], "12_33");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[2][5], "12_34");
			EntityChanged(0x01000000);
		}
		{
			SetString(&am2_vsStation[2][6], "12_35");
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][1][1] = LocGetQualifier(am_model.am_C_Press.am_sta7, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][2][1] = LocGetQualifier(am_model.am_C_Press.am_sta8, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][3][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST_B7, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][4][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST_B6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][1][1] = LocGetQualifier(am_model.am_C_Press.am_sta9, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][2][1] = LocGetQualifier(am_model.am_C_Press.am_sta10, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][3][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST_B11, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][4][1] = LocGetQualifier(am_model.am_pf_ohc.am_ST_B10, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][1][2] = LocGetQualifier(am_model.am_C_Press.am_sta1, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][2][2] = LocGetQualifier(am_model.am_C_Press.am_sta2, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][3][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST5_6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[1][4][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST5_5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][1][2] = LocGetQualifier(am_model.am_C_Press.am_sta4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][2][2] = LocGetQualifier(am_model.am_C_Press.am_sta5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][3][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST5_2, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_vlocptrSTLoad[2][4][2] = LocGetQualifier(am_model.am_pf_ohc.am_ST5_1, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[1][1][1] = &(am2_olPartKill[1]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[1][2][1] = &(am2_olPartKill[2]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[2][1][1] = &(am2_olPartKill[3]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[2][2][1] = &(am2_olPartKill[4]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[1][1][2] = &(am2_olPartKill[5]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[1][2][2] = &(am2_olPartKill[6]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[2][1][2] = &(am2_olPartKill[7]);
			EntityChanged(0x01000000);
		}
		{
			am2_volPartKill[2][2][2] = &(am2_olPartKill[8]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[1][1] = LocGetQualifier(am_model.am_pf_ohc.am_W27, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[1][2] = LocGetQualifier(am_model.am_pf_ohc.am_W28, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[2][1] = LocGetQualifier(am_model.am_pf_ohc.am_W31, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[2][2] = LocGetQualifier(am_model.am_pf_ohc.am_W33, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[3][1] = LocGetQualifier(am_model.am_pf_ohc.am_Par2, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[3][2] = LocGetQualifier(am_model.am_pf_ohc.am_Par1, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[4][1] = LocGetQualifier(am_model.am_pf_ohc.am_Par4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Partial[4][2] = LocGetQualifier(am_model.am_pf_ohc.am_Par3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Unload[1] = LocGetQualifier(am_model.am_pf_ohc.am_W26, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Unload[2] = LocGetQualifier(am_model.am_pf_ohc.am_W22, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Unload[3] = LocGetQualifier(am_model.am_pf_ohc.am_ST109, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_Unload[4] = LocGetQualifier(am_model.am_pf_ohc.am_ST119, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_B4Unload[1] = LocGetQualifier(am_model.am_pf_ohc.am_W25, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_B4Unload[2] = LocGetQualifier(am_model.am_pf_ohc.am_W21, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_B4Unload[3] = LocGetQualifier(am_model.am_pf_ohc.am_ST108, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_B4Unload[4] = LocGetQualifier(am_model.am_pf_ohc.am_ST118, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[1][1] = &(am2_O_PartialBuffer[1]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[1][2] = &(am2_O_PartialBuffer[2]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[2][1] = &(am2_O_PartialBuffer[3]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[2][2] = &(am2_O_PartialBuffer[4]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[3][1] = &(am2_O_PartialBuffer[5]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[3][2] = &(am2_O_PartialBuffer[6]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[4][1] = &(am2_O_PartialBuffer[7]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_PartialBuffer[4][2] = &(am2_O_PartialBuffer[8]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][1] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Home, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][2] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Pounce1, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][3] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Work1, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][4] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Clear1, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][5] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Pounce2, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][6] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Work2, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[1][7] = LocGetQualifier(am_model.am_K_RBT.am_Rob3Clear2, 3, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][1] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Home, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][2] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Pounce1, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][3] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Work1, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][4] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Clear1, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][5] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Pounce2, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][6] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Work2, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[2][7] = LocGetQualifier(am_model.am_K_RBT.am_Rob4Clear2, 4, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][1] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Home, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][2] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Pounce1, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][3] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Work1, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][4] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Clear1, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][5] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Pounce2, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][6] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Work2, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[3][7] = LocGetQualifier(am_model.am_K_RBT.am_Rob5Clear2, 5, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][1] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Home, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][2] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Pounce1, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][3] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Work1, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][4] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Clear1, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][5] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Pounce2, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][6] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Work2, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vloc_LoadRBT[4][7] = LocGetQualifier(am_model.am_K_RBT.am_Rob6Clear2, 6, -9999);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[1][1] = &(am2_O_RBT[1]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[1][2] = &(am2_O_RBT[2]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[1][3] = &(am2_O_RBT[3]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[2][1] = &(am2_O_RBT[4]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[2][2] = &(am2_O_RBT[5]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[2][3] = &(am2_O_RBT[6]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[3][1] = &(am2_O_RBT[7]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[3][2] = &(am2_O_RBT[8]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[3][3] = &(am2_O_RBT[9]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[4][1] = &(am2_O_RBT[10]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[4][2] = &(am2_O_RBT[11]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_RBT[4][3] = &(am2_O_RBT[12]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[1][1] = &(am2_O_SPO[1]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[1][2] = &(am2_O_SPO[2]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[1][3] = &(am2_O_SPO[3]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[2][1] = &(am2_O_SPO[4]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[2][2] = &(am2_O_SPO[5]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[2][3] = &(am2_O_SPO[6]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[3][1] = &(am2_O_SPO[7]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[3][2] = &(am2_O_SPO[8]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[3][3] = &(am2_O_SPO[9]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[4][1] = &(am2_O_SPO[10]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[4][2] = &(am2_O_SPO[11]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vo_SPO[4][3] = &(am2_O_SPO[12]);
			EntityChanged(0x01000000);
		}
		{
			am2_Vi_Count[1] = 1;
			EntityChanged(0x01000000);
		}
		{
			am2_Vi_Count[3] = 1;
			EntityChanged(0x01000000);
		}
		{
			while (am2_Vi_Count[1] <= 2) {
				{
					am2_Vi_Count[2] = 1;
					EntityChanged(0x01000000);
				}
				{
					while (am2_Vi_Count[2] <= 30) {
						{
							am2_Vproc_ReleasePartial[ValidIndex("am_model.am_Vproc_ReleasePartial", am2_Vi_Count[1], 2)][ValidIndex("am_model.am_Vproc_ReleasePartial", am2_Vi_Count[2], 30)] = &(am2_P_ReleasePartial[ValidIndex("am_model.am_P_ReleasePartial", am2_Vi_Count[3], 60)]);
							EntityChanged(0x01000000);
						}
						{
							am2_Vrsrc_ReleasePartial[ValidIndex("am_model.am_Vrsrc_ReleasePartial", am2_Vi_Count[1], 2)][ValidIndex("am_model.am_Vrsrc_ReleasePartial", am2_Vi_Count[2], 30)] = &(am2_R_ReleasePartial[ValidIndex("am_model.am_R_ReleasePartial", am2_Vi_Count[3], 60)]);
							EntityChanged(0x01000000);
						}
						{
							am2_Vo_ReleasePartial[ValidIndex("am_model.am_Vo_ReleasePartial", am2_Vi_Count[1], 2)][ValidIndex("am_model.am_Vo_ReleasePartial", am2_Vi_Count[2], 30)] = &(am2_O_ReleasePartial[ValidIndex("am_model.am_O_ReleasePartial", am2_Vi_Count[3], 60)]);
							EntityChanged(0x01000000);
						}
						{
							am2_Vi_Count[2] += 1;
							EntityChanged(0x01000000);
						}
						{
							am2_Vi_Count[3] += 1;
							EntityChanged(0x01000000);
						}
					}
				}
				{
					am2_Vi_Count[1] += 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			am2_Vstrm_DT[1] = am2_stream100;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[2] = am2_stream101;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[3] = am2_stream102;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[4] = am2_stream103;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[5] = am2_stream104;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[6] = am2_stream105;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[7] = am2_stream77;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[8] = am2_stream78;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[9] = am2_stream79;
			EntityChanged(0x01000000);
		}
		{
			am2_Vstrm_DT[10] = am2_stream80;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[1] = am_model.am_pf_ohc.am_M_chain1;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[2] = am_model.am_pf_ohc.am_M_chain2;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[3] = am_model.am_pf_ohc.am_M_chain3;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[4] = am_model.am_pf_ohc.am_M_chain4;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[5] = am_model.am_pf_ohc.am_M_chain5;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[6] = am_model.am_pf_ohc.am_M_chain6;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[7] = am_model.am_pf_ohc.am_M_chain7;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[8] = am_model.am_pf_ohc.am_M_chain8;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[9] = am_model.am_pf_ohc.am_M_chain9;
			EntityChanged(0x01000000);
		}
		{
			am2_vmMotor[10] = am_model.am_pf_ohc.am_M_chain10;
			EntityChanged(0x01000000);
		}
		{
			return 1;
		}
	}
LabelRet: ;
} /* end of model_initialize */

static int32
model_finished()
{
	load* localactor = NULL;
	AMDebuggerBeginRoutine("PressLogic.m", "Model finished function", "base", localactor);
	{
		char* names[1];
		void* ptrs[1];
		char* (*valstrfuncs[1])(void*);
		
		AMDebuggerParams("base", model_finished, localactor, 0, names, ptrs, valstrfuncs);
	}
	{
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1880);
			{
				char* pArg1 = "Weld Production";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1881);
			{
				char* pArg1 = "Weld Line 1 ";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(ValidPtr(&(am2_T_Output[1]), 67, table*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1882);
			{
				char* pArg1 = "Weld Line 2 ";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(ValidPtr(&(am2_T_Output[3]), 67, table*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1883);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1892);
			{
				char* pArg1 = "Avg Carrier Wait Time BLine:";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(ValidPtr(&(am2_T_CarWait[1]), 67, table*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1893);
			{
				char* pArg1 = "Avg Carrier Wait Time CLine:";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(ValidPtr(&(am2_T_CarWait[2]), 67, table*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1894);
			{
				char* pArg1 = "Avg Daily Changeovers BLine:";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(ValidPtr(&(am2_T_Changeover[1]), 67, table*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1895);
			{
				char* pArg1 = "Avg Daily Changeovers CLine:";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(ValidPtr(&(am2_T_Changeover[2]), 67, table*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1896);
			{
				char* pArg1 = "Avg Recirculations/Shift:";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(am2_tRecircCount);

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1897);
			{
				char* pArg1 = "Avg Early Pulls for A-Bay Cleanup:";
				char* pArg2 = "\t";
				double pArg3 = TblGetAvContsR(am2_tABayCleanUp);

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1898);
			{
				char* pArg1 = "Avg Mixed Lanes:";
				char* pArg2 = "\t";
				double pArg3 = CntGetAvContsR(ValidPtr(&(am2_C_ZoneMix[1]), 10, counter*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1899);
			{
				char* pArg1 = "Avg Clean Lanes:";
				char* pArg2 = "\t";
				double pArg3 = CntGetAvContsR(ValidPtr(&(am2_C_ZoneMix[2]), 10, counter*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1900);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1902);
			{
				char* pArg1 = "Carriers";
				char* pArg2 = "\t";
				char* pArg3 = "Average";
				char* pArg4 = "\t";
				char* pArg5 = "Max";
				char* pArg6 = "\t";
				char* pArg7 = "Min";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%s%s%s%s%s\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1903);
			{
				char* pArg1 = "Loaded:";
				char* pArg2 = "\t";
				double pArg3 = CntGetAvContsR(ValidPtr(&(am2_C_CarrierCount[1]), 10, counter*));
				char* pArg4 = "\t";
				int32 pArg5 = CntGetMaxContsR(ValidPtr(&(am2_C_CarrierCount[1]), 10, counter*));
				char* pArg6 = "\t";
				int32 pArg7 = CntGetMinContsR(ValidPtr(&(am2_C_CarrierCount[1]), 10, counter*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf%s%.2d%s%.2d\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1904);
			{
				char* pArg1 = "Empty:";
				char* pArg2 = "\t";
				double pArg3 = CntGetAvContsR(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*));
				char* pArg4 = "\t";
				int32 pArg5 = CntGetMaxContsR(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*));
				char* pArg6 = "\t";
				int32 pArg7 = CntGetMinContsR(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*));

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf%s%.2d%s%.2d\n", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1905);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1907);
			{
				char* pArg1 = "B Line Press States";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1908);
			{
				char* pArg1 = "Production";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_Production)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1909);
			{
				char* pArg1 = "DieChange";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_DieChange)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1910);
			{
				char* pArg1 = "NoScheduledRuns";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_NoScheduledRuns)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1911);
			{
				char* pArg1 = "NoCarriers";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_NoCarriers)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1912);
			{
				char* pArg1 = "Blocked";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_Blocked)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1913);
			{
				char* pArg1 = "Down";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_Down)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1914);
			{
				char* pArg1 = "PlannedDT";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[3], 50, resource*)), am2_PlannedDT)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1915);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1917);
			{
				char* pArg1 = "C Line Press States";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1918);
			{
				char* pArg1 = "Production";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_Production)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1919);
			{
				char* pArg1 = "DieChange";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_DieChange)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1920);
			{
				char* pArg1 = "NoScheduledRuns";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_NoScheduledRuns)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1921);
			{
				char* pArg1 = "NoCarriers";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_NoCarriers)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1922);
			{
				char* pArg1 = "Blocked";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_Blocked)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1923);
			{
				char* pArg1 = "Down";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_Down)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1924);
			{
				char* pArg1 = "PlannedDT";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(RscGetMonitor(ValidPtr(am2_vrscDowntime[4], 50, resource*)), am2_PlannedDT)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1925);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1927);
			{
				char* pArg1 = "Weld Line 1 Starved";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1928);
			MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)), str2StatePtr("PlannedDT"), NULL);
			EntityChanged(0x00020000);
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1929);
			am2_vrStateTemp = StGetAvContsA(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*))));
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1930);
			MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)), str2StatePtr("Starved"), NULL);
			EntityChanged(0x00020000);
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1931);
			{
				char* pArg1 = "Starved/Day";
				char* pArg2 = "\t";
				double pArg3 = StGetTotEntriesR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)))) / am2_viTotalDay;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1932);
			{
				char* pArg1 = "Avg Starved Time (min)";
				char* pArg2 = "\t";
				double pArg3 = FromModelTime(StGetAvTimeR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)))) / 60, UNITSECONDS);

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1933);
			{
				char* pArg1 = "Starved %";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)))) / (1 - am2_vrStateTemp) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1934);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1936);
			{
				char* pArg1 = "Weld Line 2 Starved";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1937);
			MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)), str2StatePtr("PlannedDT"), NULL);
			EntityChanged(0x00020000);
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1938);
			am2_vrStateTemp = StGetAvContsR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*))));
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1939);
			MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)), str2StatePtr("Starved"), NULL);
			EntityChanged(0x00020000);
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1940);
			{
				char* pArg1 = "Starved/Day";
				char* pArg2 = "\t";
				double pArg3 = StGetTotEntriesR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)))) / am2_viTotalDay;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1941);
			{
				char* pArg1 = "Avg Starved Time (min)";
				char* pArg2 = "\t";
				double pArg3 = FromModelTime(StGetAvTimeR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)))) / 60, UNITSECONDS);

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1942);
			{
				char* pArg1 = "Starved %";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)))) / (1 - am2_vrStateTemp) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1943);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1945);
			{
				char* pArg1 = "Press Running Conditions";

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1946);
			{
				char* pArg1 = "Two Presses Running Spo";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(am2_vsmPressCondition, am2_TwoPressRunningSpo)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1947);
			{
				char* pArg1 = "One Press Running Spo";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(am2_vsmPressCondition, am2_OnePressRunningSpo)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1948);
			{
				char* pArg1 = "No Presses Running Spo";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(am2_vsmPressCondition, am2_NoPressRunningSpo)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1949);
			{
				char* pArg1 = "Planned DT";
				char* pArg2 = "\t";
				double pArg3 = StGetAvContsR(MonGetNamedState(am2_vsmPressCondition, am2_PlannedDT)) * 100;

				fprintf(ffp_base_arc_RESULTS_avg_out->fp, "%s%s%.2lf\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("PressLogic.m", "Model finished function", "base", model_finished, localactor, 1950);
			AMDebuggerEndRoutine("PressLogic.m", "Model finished function", "base", model_finished, localactor);
			return 1;
		}
	}
LabelRet: ;
	AMDebuggerEndRoutine("PressLogic.m", "Model finished function", "base", model_finished, localactor);
} /* end of model_finished */

static int32
pf_ohc_task(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Task search procedure", "base.pf_ohc", localactor);
	{
		char* names[1];
		void* ptrs[1];
		char* (*valstrfuncs[1])(void*);
		
		names[0] = "Current";
		ptrs[0] = &this->curlocdata.sloc;
		valstrfuncs[0] = Location_valstrfunc;
		AMDebuggerParams("base.pf_ohc", pf_ohc_task, localactor, 1, names, ptrs, valstrfuncs);
	}
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1956);
			if (this->load.attribute->am2_Ai_Status == 9999) {
				{
					AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1957);
					this->load.attribute->am2_Ai_Status = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1958);
					return waitorder(am2_O_VehicleStart, this, pf_ohc_task, Step 2, am_localargs);
Label2: ; /* Step 2 */
				}
				{
					AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1959);
					am2_viNumVeh += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1960);
					if (am2_viNumVeh > am2_viReqVeh) {
						{
							AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1961);
							pushppa(this, pf_ohc_task, Step 3, am_localargs);
							pushppa(this, inqueue, Step 1, am2_qVehOverflow);
							return Continue; /* go move into territory */
Label3: ; /* Step 3 */
						}
						{
							AMDebugger("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor, 1962);
							return waitorder(am2_olVehOverflow, this, pf_ohc_task, Step 4, am_localargs);
Label4: ; /* Step 4 */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Task search procedure", "base.pf_ohc", pf_ohc_task, localactor);
	return retval;
} /* end of pf_ohc_task */

static int32
pf_ohc_vehinit(vehicle* am_theVehicle)
{
	load* localactor = (load*)am_theVehicle;
	AMDebuggerBeginRoutine("PressLogic.m", "Vehicle initialization function", "base.pf_ohc", localactor);
	{
		char* names[1];
		void* ptrs[1];
		char* (*valstrfuncs[1])(void*);
		
		names[0] = "theVehicle";
		ptrs[0] = &am_theVehicle;
		valstrfuncs[0] = VehiclePtr_valstrfunc;
		AMDebuggerParams("base.pf_ohc", pf_ohc_vehinit, localactor, 1, names, ptrs, valstrfuncs);
	}
	{
		{
			AMDebugger("PressLogic.m", "Vehicle initialization function", "base.pf_ohc", pf_ohc_vehinit, localactor, 1968);
			ValidPtr(am_theVehicle, 76, vehicle*)->load.attribute->am2_Ai_Status = 9999;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Vehicle initialization function", "base.pf_ohc", pf_ohc_vehinit, localactor, 1969);
			AMDebuggerEndRoutine("PressLogic.m", "Vehicle initialization function", "base.pf_ohc", pf_ohc_vehinit, localactor);
			return 1;
		}
	}
LabelRet: ;
	AMDebuggerEndRoutine("PressLogic.m", "Vehicle initialization function", "base.pf_ohc", pf_ohc_vehinit, localactor);
} /* end of pf_ohc_vehinit */

static int32
F_getID()
{
	load* localactor = NULL;
	AMDebuggerBeginRoutine("PressLogic.m", "Function", "base.F_getID", localactor);
	{
		char* names[1];
		void* ptrs[1];
		char* (*valstrfuncs[1])(void*);
		
		AMDebuggerParams("base.F_getID", F_getID, localactor, 0, names, ptrs, valstrfuncs);
	}
	{
		{
			AMDebugger("PressLogic.m", "Function", "base.F_getID", F_getID, localactor, 1973);
			{
				char* pArg1 = rel_actorname(funcload, am_model.$sys);

				char* am_tmp;
				am_tmp = bufsprintf("%s", pArg1);
				SetString(&am2_Vs_str, am_tmp);
				EntityChanged(0x01000000);
			}
		}
		{
			int rflag;
			static ReadRef st1;

			setupReadRef(&st1, 0, am_model.am_Vs_arrstr$var, am2_Vs_arrstr, NULL, -1, TRUE);
			AMDebugger("PressLogic.m", "Function", "base.F_getID", F_getID, localactor, 1974);
			rflag = readString(am2_Vs_str, " ", &st1, NULL);
		}
		{
			AMDebugger("PressLogic.m", "Function", "base.F_getID", F_getID, localactor, 1975);
			ValidPtr(funcload, 32, load*)->attribute->am2_Ai_ID = str2Integer(am2_Vs_arrstr[2]);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Function", "base.F_getID", F_getID, localactor, 1976);
			AMDebuggerEndRoutine("PressLogic.m", "Function", "base.F_getID", F_getID, localactor);
			return 1;
		}
	}
LabelRet: ;
	AMDebuggerEndRoutine("PressLogic.m", "Function", "base.F_getID", F_getID, localactor);
} /* end of F_getID */

static int
dispatch_F_getID(int nParams, int* argTypes, void** argVals, void** retVal)
{
	static char buf[512];
	int retType;
	static int32 ret;

	if (nParams != 0) {
		message("The function F_getID was called from the ActiveX interface with %d parameters, while 0 are required.", nParams);
		return 0;
	}
	ret = F_getID();
	*retVal = &ret;
	return 1;
}

static int32
am_SR_AttributeReset(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Subroutine", "base.SR_AttributeReset", localactor);
	AMDebuggerParams("base.SR_AttributeReset", am_SR_AttributeReset, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1984);
			this->attribute->am2_Ai_pi = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1985);
			this->attribute->am2_Ai_CarQty = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1986);
			this->attribute->am2_Ai_Line = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1987);
			this->attribute->am2_Ai_LotQty = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1988);
			this->attribute->am2_Ai_Seq = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1989);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1990);
			this->attribute->am2_Ai_Type = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1991);
			SetString(&this->attribute->am2_As_Type, NULL);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1992);
			this->attribute->am2_At_TimeInSys = ToModelTime(0, UNITSECONDS);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor, 1993);
			{
					retval = Continue;
					goto LabelRet;
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Subroutine", "base.SR_AttributeReset", am_SR_AttributeReset, localactor);
	return retval;
} /* end of am_SR_AttributeReset */

static int32
am_SR_TypeIndex(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Subroutine", "base.SR_TypeIndex", localactor);
	AMDebuggerParams("base.SR_TypeIndex", am_SR_TypeIndex, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2001);
			am2_viIndex = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2002);
			while (am2_viIndex <= 30) {
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2003);
					if (StringCompare(this->attribute->am2_As_Type, am2_vsStyle[ValidIndex("am_model.am_vsStyle", am2_viIndex, 30)]) == 0) {
						AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2004);
						this->attribute->am2_Ai_Type = am2_viIndex;
						EntityChanged(0x00000040);
					}
				}
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2009);
					am2_viIndex += 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2011);
			if (this->attribute->am2_Ai_Type == 0) {
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2012);
					{
						char* pArg1 = "WRONG As_Type SET FOR THE LOAD: Please Check";
						char* pArg2 = " ";
						char* pArg3 = rel_actorname(this, am_model.$sys);
						char* pArg4 = " ";
						char* pArg5 = this->attribute->am2_As_Type;

						message("%s%s%s%s%s", pArg1, pArg2, pArg3, pArg4, pArg5);
					}
				}
				{
					AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2013);
					{
						int result = deccount(am2_C_Error, 1, this, am_SR_TypeIndex, Step 2, am_localargs);
						if (result != Continue) return result;
Label2: ; /* Step 2 */
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor, 2015);
			{
					retval = Continue;
					goto LabelRet;
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Subroutine", "base.SR_TypeIndex", am_SR_TypeIndex, localactor);
	return retval;
} /* end of am_SR_TypeIndex */

static int32
P_Legend_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Arriving procedure", "base.P_Legend", localactor);
	AMDebuggerParams("base.P_Legend", P_Legend_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Legend", P_Legend_arriving, localactor, 2021);
			pushppa(this, P_Legend_arriving, Step 2, am_localargs);
			MoveIntoContainer(this,ListIndexItem(ContainerList, QueGetContainerList(am2_Q_Legend), CurProcIndex()));
			return Continue; /* go move into territory */
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Legend", P_Legend_arriving, localactor, 2022);
			if (transformLoad(TX_LOAD_SCALE, 50, 50, 5, 1, 1, 1, 0.0, this, this, P_Legend_arriving, Step 3, am_localargs) == Delayed)
				return Delayed;
Label3: ; /* Step 3 */
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Legend", P_Legend_arriving, localactor, 2023);
			LdSetColor(this, am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", CurProcIndex(), 30)]);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Legend", P_Legend_arriving, localactor, 2024);
			{
				char* pArg1 = am2_vsStyle[ValidIndex("am_model.am_vsStyle", CurProcIndex(), 30)];

				updatelabel(&(am2_Lb_Legend[ValidIndex("am_model.am_Lb_Legend", CurProcIndex(), 30)]), "%s", pArg1);
			}
		}
		{
			AMDebugger("PressLogic.m", "Arriving procedure", "base.P_Legend", P_Legend_arriving, localactor, 2025);
			return waitorder(am2_O_Temp, this, P_Legend_arriving, Step 4, am_localargs);
Label4: ; /* Step 4 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Arriving procedure", "base.P_Legend", P_Legend_arriving, localactor);
	return retval;
} /* end of P_Legend_arriving */

static int32
pf_ohc_RowEnd_photoblocked(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", localactor);
	AMDebuggerParams("base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2030);
			if (am2_viTrackStoreProcess == 1) {
				{
					AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2031);
					if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99 || (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type == 99 && ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Seq != 99999)) {
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2033);
							am2_vldWriteJSON = VehGetCurLoad(this);
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2034);
							SetString(&ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_Message, "STORE IN COMPLETE");
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2035);
							SetString(&ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_CurrentLocation, am2_vsStation[ValidIndex("am_model.am_vsStation", ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vsStation", ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Row, 20)]);
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2036);
							SetString(&ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_NextLocation, am2_vsStation[ValidIndex("am_model.am_vsStation", ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vsStation", ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Row, 20)]);
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2037);
							pushppa(this, pf_ohc_RowEnd_photoblocked, Step 2, am_localargs);
							pushppa(this, am2_Sr_WriteJSONFile, Step 1, NULL);
							return Continue;
Label2: ;
						}
					}
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2041);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				{
					AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2042);
					if (OrdGetCurConts(ValidPtr(&(am2_O_Starve[ValidIndex("am_model.am_O_Starve", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type, 30)]), 40, ordlist*)) > 0 && ListSize(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row, 20)]) > 0 && ValidPtr(ListFirstItem(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type != ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type) {
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2043);
							this->load.attribute->am2_Ai_y = 0;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2044);
							order(1, &(am2_O_Starve[ValidIndex("am_model.am_O_Starve", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, NULL); /* Place an order */
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2064);
					ListAppendItem(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row, 20)], VehGetCurLoad(this));	/* append item to end of list */
				}
				{
					AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2066);
					if (VehGetCurLoad(this) == am2_Vld_Previous) {
						AMDebugger("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor, 2067);
						am2_Vld_Previous = am2_vldPrevHolder;
						EntityChanged(0x01000000);
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor photoeye blocked procedure", "base.pf_ohc.RowEnd", pf_ohc_RowEnd_photoblocked, localactor);
	return retval;
} /* end of pf_ohc_RowEnd_photoblocked */

static int32
pf_ohc_StorageRows_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", localactor);
	AMDebuggerParams("base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2072);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2073);
					ListRemoveFirstMatch(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row, 20)], VehGetCurLoad(this));	/* remove first match from list */
				}
			}
			else {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2077);
					if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone > 0 && ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row > 0) {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2078);
							if (am2_viEmptyCarPullQty[ValidIndex("am_model.am_viEmptyCarPullQty", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarPullQty", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row, 20)] > 0) {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2079);
								am2_viEmptyCarPullQty[ValidIndex("am_model.am_viEmptyCarPullQty", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarPullQty", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Row, 20)] -= 1;
								EntityChanged(0x01000000);
							}
						}
					}
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2082);
					if (am2_viTrackStoreProcess == 1) {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2083);
							if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99 || (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type == 99 && ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Seq != 99999)) {
								{
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2085);
									am2_vldWriteJSON = VehGetCurLoad(this);
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2086);
									SetString(&ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_Message, "STORE OUT REQUEST");
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2087);
									SetString(&ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_CurrentLocation, am2_vsStation[ValidIndex("am_model.am_vsStation", ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vsStation", ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Row, 20)]);
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2088);
									SetString(&ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_NextLocation, "13");
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor, 2089);
									pushppa(this, pf_ohc_StorageRows_staleave, Step 2, am_localargs);
									pushppa(this, am2_Sr_WriteJSONFile, Step 1, NULL);
									return Continue;
Label2: ;
								}
							}
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staleave, localactor);
	return retval;
} /* end of pf_ohc_StorageRows_staleave */

static int32
pf_ohc_StorageRows_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", localactor);
	AMDebuggerParams("base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor, 2098);
			if (waitfor(ToModelTime(0.10000000000000001, UNITSECONDS), this, pf_ohc_StorageRows_staarrive, Step 2, am_localargs) == Delayed)
				return Delayed;
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor, 2100);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor, 2101);
					if (OrdGetCurConts(ValidPtr(&(am2_O_Starve[ValidIndex("am_model.am_O_Starve", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type, 30)]), 40, ordlist*)) > 0 && ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_x != 10) {
						{
							AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor, 2102);
							ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_y = 0;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor, 2103);
							order(1, &(am2_O_Starve[ValidIndex("am_model.am_O_Starve", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, NULL); /* Place an order */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StorageRows", pf_ohc_StorageRows_staarrive, localactor);
	return retval;
} /* end of pf_ohc_StorageRows_staarrive */

static int32
pf_ohc_ST_B13_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B13", localactor);
	AMDebuggerParams("base.pf_ohc.ST_B13", pf_ohc_ST_B13_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B13", pf_ohc_ST_B13_staarrive, localactor, 2127);
			return waitorder(am2_oBPressExit, this, pf_ohc_ST_B13_staarrive, Step 2, am_localargs);
Label2: ; /* Step 2 */
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B13", pf_ohc_ST_B13_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST_B13_staarrive */

static int32
pf_ohc_LS_10442_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_10442", localactor);
	AMDebuggerParams("base.pf_ohc.LS_10442", pf_ohc_LS_10442_photocleared, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_10442", pf_ohc_LS_10442_photocleared, localactor, 2131);
			{
				int32 tempint = order(1, am2_oBPressExit, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_oBPressExit, NULL, NULL);	/* Place a backorder */
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_10442", pf_ohc_LS_10442_photocleared, localactor);
	return retval;
} /* end of pf_ohc_LS_10442_photocleared */

static int32
pf_ohc_StopStation_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StopStation", localactor);
	AMDebuggerParams("base.pf_ohc.StopStation", pf_ohc_StopStation_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StopStation", pf_ohc_StopStation_staarrive, localactor, 2137);
			if (waitfor(ToModelTime(0.10000000000000001, UNITSECONDS), this, pf_ohc_StopStation_staarrive, Step 2, am_localargs) == Delayed)
				return Delayed;
Label2: ; /* Step 2 */
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.StopStation", pf_ohc_StopStation_staarrive, localactor);
	return retval;
} /* end of pf_ohc_StopStation_staarrive */

static int32
pf_ohc_ST5_8_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_8", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_8", pf_ohc_ST5_8_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_8", pf_ohc_ST5_8_staarrive, localactor, 2141);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_8", pf_ohc_ST5_8_staarrive, localactor, 2142);
					if (getrsrc(am2_rStampingShift, 1, this, pf_ohc_ST5_8_staarrive, Step 2, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label2: ; /* Step 2 */
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_8", pf_ohc_ST5_8_staarrive, localactor, 2143);
					if (waitfor(ToModelTime(am2_vrStopTime[1], UNITSECONDS), this, pf_ohc_ST5_8_staarrive, Step 3, am_localargs) == Delayed)
						return Delayed;
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_8", pf_ohc_ST5_8_staarrive, localactor, 2144);
					freersrc(am2_rStampingShift, 1, this);
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_8", pf_ohc_ST5_8_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST5_8_staarrive */

static int32
pf_ohc_ST5_9_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_9", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_9", pf_ohc_ST5_9_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_9", pf_ohc_ST5_9_staarrive, localactor, 2149);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_9", pf_ohc_ST5_9_staarrive, localactor, 2150);
					if (getrsrc(am2_rStampingShift, 1, this, pf_ohc_ST5_9_staarrive, Step 2, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label2: ; /* Step 2 */
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_9", pf_ohc_ST5_9_staarrive, localactor, 2151);
					if (waitfor(ToModelTime(am2_vrStopTime[3], UNITSECONDS), this, pf_ohc_ST5_9_staarrive, Step 3, am_localargs) == Delayed)
						return Delayed;
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_9", pf_ohc_ST5_9_staarrive, localactor, 2152);
					freersrc(am2_rStampingShift, 1, this);
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST5_9", pf_ohc_ST5_9_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST5_9_staarrive */

static int32
pf_ohc_ST8_1_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST8_1", localactor);
	AMDebuggerParams("base.pf_ohc.ST8_1", pf_ohc_ST8_1_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST8_1", pf_ohc_ST8_1_staarrive, localactor, 2157);
			if (am2_viSta8_1Count > 0) {
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST8_1", pf_ohc_ST8_1_staarrive, localactor, 2158);
					return waitorder(am2_olSta8_1Incline, this, pf_ohc_ST8_1_staarrive, Step 2, am_localargs);
Label2: ; /* Step 2 */
				}
			}
		}
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST8_1", pf_ohc_ST8_1_staarrive, localactor, 2160);
			am2_viSta8_1Count += 1;
			EntityChanged(0x01000000);
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST8_1", pf_ohc_ST8_1_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST8_1_staarrive */

static int32
pf_ohc_ST_B23_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B23", localactor);
	AMDebuggerParams("base.pf_ohc.ST_B23", pf_ohc_ST_B23_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B23", pf_ohc_ST_B23_staarrive, localactor, 2171);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_LotNo != 0 && ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B23", pf_ohc_ST_B23_staarrive, localactor, 2172);
				if (getrsrc(am2_rBLineTravel, 1, this, pf_ohc_ST_B23_staarrive, Step 2, am_localargs) == Delayed)
					return Delayed;  /* go wait for resource */
Label2: ; /* Step 2 */
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST_B23", pf_ohc_ST_B23_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST_B23_staarrive */

static int32
pf_ohc_ST_B24_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B24", localactor);
	AMDebuggerParams("base.pf_ohc.ST_B24", pf_ohc_ST_B24_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B24", pf_ohc_ST_B24_staleave, localactor, 2176);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type == 99 && am2_viEmptyLoad[2][1] >= am2_viCLineEmptyTrigger && OrdGetCurConts(am2_oST_B27) > 0) {
				AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B24", pf_ohc_ST_B24_staleave, localactor, 2177);
				order(1, am2_oST_B27, NULL, NULL); /* Place an order */
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B24", pf_ohc_ST_B24_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST_B24_staleave */

static int32
pf_ohc_ST_B28_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B28", localactor);
	AMDebuggerParams("base.pf_ohc.ST_B28", pf_ohc_ST_B28_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B28", pf_ohc_ST_B28_staleave, localactor, 2181);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_LotNo != 0 && ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Type != 99) {
				AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B28", pf_ohc_ST_B28_staleave, localactor, 2182);
				freersrc(am2_rBLineTravel, 1, this);
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B28", pf_ohc_ST_B28_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST_B28_staleave */

static int32
pf_ohc_ST5_3_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 2193);
			if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rCLine), am2_Blocked)) == 1) {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 2194);
					am2_viBlocked[ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_pi, 2)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 2195);
					if (am2_viBlocked[2][1] + am2_viBlocked[2][2] == 0) {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 2196);
							if (am2_Vi_Down[4] == 0) {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 2196);
								MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Down"), this);
								EntityChanged(0x00020000);
							}
							else {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor, 2197);
								MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Production"), this);
								EntityChanged(0x00020000);
							}
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_3", pf_ohc_ST5_3_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST5_3_staleave */

static int32
pf_ohc_ST5_7a_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 2203);
			if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rCLine), am2_Blocked)) == 1) {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 2204);
					am2_viBlocked[ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_pi, 2)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 2205);
					if (am2_viBlocked[2][1] + am2_viBlocked[2][2] == 0) {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 2206);
							if (am2_Vi_Down[4] == 0) {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 2206);
								MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Down"), this);
								EntityChanged(0x00020000);
							}
							else {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor, 2207);
								MonSetState(RscGetMonitor(am2_rCLine), str2StatePtr("Production"), this);
								EntityChanged(0x00020000);
							}
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_7a", pf_ohc_ST5_7a_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST5_7a_staleave */

static int32
pf_ohc_ST_B8_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", localactor);
	AMDebuggerParams("base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 2213);
			if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rBLine), am2_Blocked)) == 1) {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 2214);
					am2_viBlocked[ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_pi, 2)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 2215);
					if (am2_viBlocked[1][1] + am2_viBlocked[1][2] == 0) {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 2216);
							if (am2_Vi_Down[3] == 0) {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 2216);
								MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Down"), this);
								EntityChanged(0x00020000);
							}
							else {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor, 2217);
								MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Production"), this);
								EntityChanged(0x00020000);
							}
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B8", pf_ohc_ST_B8_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST_B8_staleave */

static int32
pf_ohc_ST_B12_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", localactor);
	AMDebuggerParams("base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 2224);
			if (StGetCurConts(MonGetNamedState(RscGetMonitor(am2_rBLine), am2_Blocked)) == 1) {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 2225);
					am2_viBlocked[ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Press, 2)][ValidIndex("am_model.am_viBlocked", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_pi, 2)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 2226);
					if (am2_viBlocked[1][1] + am2_viBlocked[1][2] == 0) {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 2227);
							if (am2_Vi_Down[3] == 0) {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 2227);
								MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Down"), this);
								EntityChanged(0x00020000);
							}
							else {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor, 2228);
								MonSetState(RscGetMonitor(am2_rBLine), str2StatePtr("Production"), this);
								EntityChanged(0x00020000);
							}
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST_B12", pf_ohc_ST_B12_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST_B12_staleave */

static int32
pf_ohc_ST5_12_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_12", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_12", pf_ohc_ST5_12_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_12", pf_ohc_ST5_12_staleave, localactor, 2234);
			if (OrdGetCurConts(am2_oST5_12) > 0) {
				AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_12", pf_ohc_ST5_12_staleave, localactor, 2235);
				order(1, am2_oST5_12, NULL, NULL); /* Place an order */
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_12", pf_ohc_ST5_12_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST5_12_staleave */

static int32
OrderCondFunc5(load* this)
{
	if (this->attribute->am2_Ai_z == 1)
		return TRUE;
	return FALSE;
}

static int32
OrderCondFunc6(load* this)
{
	if (this->attribute->am2_Ai_z == 3)
		return TRUE;
	return FALSE;
}

static int32
pf_ohc_ST5_13_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", localactor);
	AMDebuggerParams("base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2251);
			if (OrdGetCurConts(am2_oST5_13) > 0) {
				{
					AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2252);
					if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_8, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_7, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_7a, -9999), 38, simloc*)) == 0 && LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_10, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_9, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_4, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_3, -9999), 38, simloc*)) == 0) {
						AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2254);
						order(1, am2_oST5_13, NULL, NULL); /* Place an order */
					}
					else {
						{
							AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2256);
							if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_8, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_7, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_7a, -9999), 38, simloc*)) == 0) {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2257);
								order(1, am2_oST5_13, NULL, OrderCondFunc5); /* Place an order */
							}
							else {
								AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2258);
								if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_10, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_9, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_4, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_3, -9999), 38, simloc*)) == 0) {
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2259);
									order(1, am2_oST5_13, NULL, OrderCondFunc6); /* Place an order */
								}
								else {
									AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor, 2260);
									order(1, am2_oST5_13, NULL, NULL); /* Place an order */
								}
							}
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST5_13", pf_ohc_ST5_13_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST5_13_staleave */

static int32
pf_ohc_ST10_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST10", localactor);
	AMDebuggerParams("base.pf_ohc.ST10", pf_ohc_ST10_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST10", pf_ohc_ST10_staleave, localactor, 2266);
			am2_viKDcount[1] -= 1;
			EntityChanged(0x01000000);
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST10", pf_ohc_ST10_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST10_staleave */

static int32
pf_ohc_LS_10275_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_10275", localactor);
	AMDebuggerParams("base.pf_ohc.LS_10275", pf_ohc_LS_10275_photocleared, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_10275", pf_ohc_LS_10275_photocleared, localactor, 2270);
			{
				int32 tempint = order(1, am2_olSta8_1Incline, NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, am2_olSta8_1Incline, NULL, NULL);	/* Place a backorder */
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_10275", pf_ohc_LS_10275_photocleared, localactor);
	return retval;
} /* end of pf_ohc_LS_10275_photocleared */

static int32
pf_ohc_ST7_8_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST7_8", localactor);
	AMDebuggerParams("base.pf_ohc.ST7_8", pf_ohc_ST7_8_staarrive, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST7_8", pf_ohc_ST7_8_staarrive, localactor, 2275);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side == 3) {
				{
					AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST7_8", pf_ohc_ST7_8_staarrive, localactor, 2276);
					if (LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST146, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST155, -9999), 38, simloc*)) + LocGetRemCap(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST156, -9999), 38, simloc*)) == 0 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST154, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST153, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST152, -9999), 38, simloc*)) == 0 && ValidPtr(ListFirstItem(LoadList, VehGetLoadList(ValidPtr(ListFirstItem(VehicleList, LocGetVehicleList(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST146, -9999), 38, simloc*))), 76, vehicle*))), 32, load*)->attribute->am2_Ai_Side != 3) {
						{
							AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST7_8", pf_ohc_ST7_8_staarrive, localactor, 2278);
							order(1, &(am2_O_Weld2Order[2]), NULL, NULL); /* Place an order */
						}
						{
							AMDebugger("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST7_8", pf_ohc_ST7_8_staarrive, localactor, 2279);
							return waitorder(&(am2_O_Weld2Order[1]), this, pf_ohc_ST7_8_staarrive, Step 2, am_localargs);
Label2: ; /* Step 2 */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("PressLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST7_8", pf_ohc_ST7_8_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST7_8_staarrive */



/* init function for PressLogic.m */
void
model_PressLogic_init(struct model_struct* data)
{
	data->am_pInit->aprc = pInit_arriving;
	data->am_P_Downtime->aprc = P_Downtime_arriving;
	data->am_pSTRunControl->aprc = pSTRunControl_arriving;
	data->am_pEmptyRecirc->aprc = pEmptyRecirc_arriving;
	data->am_pCarrierLoad->aprc = pCarrierLoad_arriving;
	data->am_pf_ohc.am_ST_LiftDown->destdata->src.idleprc = pf_ohc_ST_LiftDown_idle;
	((ConvPhotoeye*)data->am_pf_ohc.am_peLiftClear)->spec.clearedprc = pf_ohc_peLiftClear_photocleared;
	((MovementSystem*)data->am_kLift.$sys)->srcblock.idleprc = kLift_idle;
	data->am_Sr_OtherInv = am_Sr_OtherInv;
	data->am_pEmptyCarrierReturn->aprc = pEmptyCarrierReturn_arriving;
	data->am_pEmptyCarrierManagement->aprc = pEmptyCarrierManagement_arriving;
	((ConvStation*)data->am_pf_ohc.am_ST5_5)->spec.src.leaveprc = pf_ohc_ST5_5_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST5_1)->spec.src.leaveprc = pf_ohc_ST5_1_staleave;
	((ConvPhotoeye*)data->am_pf_ohc.am_LS_24)->spec.blockedprc = pf_ohc_LS_24_photoblocked;
	data->am_sAttributeStrip = am_sAttributeStrip;
	data->am_P_StampingShift->aprc = P_StampingShift_arriving;
	data->am_P_WeldShift->aprc = P_WeldShift_arriving;
	data->am_pNonSpoPartMgt->aprc = pNonSpoPartMgt_arriving;
	data->am_pWarmUp->aprc = pWarmUp_arriving;
	((ProcSystem*)data->$sys)->modelInitPtr = model_initialize;
	((ProcSystem*)data->$sys)->modelFinishedPtr = model_finished;
	((MovementSystem*)data->am_pf_ohc.$sys)->srcblock.taskprc = pf_ohc_task;
	((MovementSystem*)data->am_pf_ohc.$sys)->srcblock.initprc = pf_ohc_vehinit;
	data->am_F_getID = F_getID;
	data->am_SR_AttributeReset = am_SR_AttributeReset;
	data->am_SR_TypeIndex = am_SR_TypeIndex;
	data->am_P_Legend->aprc = P_Legend_arriving;
	data->am_pf_ohc.am_RowEnd->blockedprc = pf_ohc_RowEnd_photoblocked;
	data->am_pf_ohc.am_StorageRows->src.leaveprc = pf_ohc_StorageRows_staleave;
	data->am_pf_ohc.am_StorageRows->src.arriveprc = pf_ohc_StorageRows_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST_B13)->spec.src.arriveprc = pf_ohc_ST_B13_staarrive;
	((ConvPhotoeye*)data->am_pf_ohc.am_LS_10442)->spec.clearedprc = pf_ohc_LS_10442_photocleared;
	data->am_pf_ohc.am_StopStation->src.arriveprc = pf_ohc_StopStation_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST5_8)->spec.src.arriveprc = pf_ohc_ST5_8_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST5_9)->spec.src.arriveprc = pf_ohc_ST5_9_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST8_1)->spec.src.arriveprc = pf_ohc_ST8_1_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST_B23)->spec.src.arriveprc = pf_ohc_ST_B23_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST_B24)->spec.src.leaveprc = pf_ohc_ST_B24_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST_B28)->spec.src.leaveprc = pf_ohc_ST_B28_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST5_3)->spec.src.leaveprc = pf_ohc_ST5_3_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST5_7a)->spec.src.leaveprc = pf_ohc_ST5_7a_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST_B8)->spec.src.leaveprc = pf_ohc_ST_B8_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST_B12)->spec.src.leaveprc = pf_ohc_ST_B12_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST5_12)->spec.src.leaveprc = pf_ohc_ST5_12_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST5_13)->spec.src.leaveprc = pf_ohc_ST5_13_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST10)->spec.src.leaveprc = pf_ohc_ST10_staleave;
	((ConvPhotoeye*)data->am_pf_ohc.am_LS_10275)->spec.clearedprc = pf_ohc_LS_10275_photocleared;
	((ConvStation*)data->am_pf_ohc.am_ST7_8)->spec.src.arriveprc = pf_ohc_ST7_8_staarrive;
	data->am_F_getID$func->dispatch = dispatch_F_getID;
	data->am_F_getID$func->func = F_getID;
}

