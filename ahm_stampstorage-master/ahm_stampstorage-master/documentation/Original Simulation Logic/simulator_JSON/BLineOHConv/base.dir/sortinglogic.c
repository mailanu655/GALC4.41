// SortingLogic.c
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

#undef F_ConnectSocket
static int32 F_ConnectSocket(void);
#undef F_RequestMove
static char* F_RequestMove(load*);

static int32
model_ready()
{
	{
		{
			F_ConnectSocket();
		}
		{
			return 1;
		}
	}
LabelRet: ;
} /* end of model_ready */

static int32
am_Sr_StoreIn(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv299; /* 'for each' loop variable */
		AMLoadList* ls299; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", localactor);
	AMDebuggerParams("base.Sr_StoreIn", am_Sr_StoreIn, localactor, 0, NULL, NULL, NULL);
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
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 7);
			this->attribute->am2_Ai_Zone = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 8);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 9);
			this->attribute->am2_Ai_LotQty = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 11);
			if (this->attribute->am2_Ai_Type == am2_Vi_PrevPartType[ValidIndex("am_model.am_Vi_PrevPartType", this->attribute->am2_Ai_Press, 2)]) {
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 13);
					if (ListSize(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]) > 0 && ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row, 20)]) > 0 && (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row, 20)]) < LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row, 20)], 38, simloc*))) && ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 17);
							this->attribute->am2_Ai_Zone = ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Zone;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 18);
							this->attribute->am2_Ai_Row = ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 19);
							this->attribute->am2_Ai_Status = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 20);
							if (am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] > 0) {
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 20);
								am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] -= 1;
								EntityChanged(0x01000000);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 21);
							am2_Vi_StorePrevType = this->attribute->am2_Ai_Type;
							EntityChanged(0x01000000);
						}
					}
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 24);
				if (OrdGetCurConts(ValidPtr(&(am2_O_Starve[ValidIndex("am_model.am_O_Starve", this->attribute->am2_Ai_Type, 30)]), 40, ordlist*)) > 0) {
					{
						AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 25);
						this->attribute->am2_Ai_y = 5;
						EntityChanged(0x00000040);
					}
					{
						AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 26);
						while (this->attribute->am2_Ai_Zone <= am2_viNumZones && this->attribute->am2_Ai_Status == 0) {
							{
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 27);
								this->attribute->am2_Ai_Row = 1;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 28);
								while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", am2_viZonePref[ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Type, 30)], 3)] && this->attribute->am2_Ai_Status == 0) {
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 29);
										if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", am2_viZonePref[ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Type, 30)], 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == 0) {
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 30);
												this->attribute->am2_Ai_Status = 1;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 31);
												this->attribute->am2_Ai_Zone = am2_viZonePref[ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Type, 30)];
												EntityChanged(0x00000040);
											}
										}
									}
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 33);
										if (this->attribute->am2_Ai_Status == 0) {
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 34);
											this->attribute->am2_Ai_Row += 1;
											EntityChanged(0x00000040);
										}
									}
								}
							}
							{
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 36);
								if (this->attribute->am2_Ai_Status == 0) {
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 36);
									this->attribute->am2_Ai_Zone += 1;
									EntityChanged(0x00000040);
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 40);
			am2_vi = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 41);
			while (am2_vi <= 12 && this->attribute->am2_Ai_Status == 0) {
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 42);
					this->attribute->am2_Ai_Zone = am2_viZonePref[ValidIndex("am_model.am_viZonePref", am2_viZoneSortPriority[ValidIndex("am_model.am_viZoneSortPriority", am2_vi, 12)], 3)][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Type, 30)];
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 43);
					this->attribute->am2_Ai_Row = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 45);
					if (StringCompare(am2_vsSortingRule[ValidIndex("am_model.am_vsSortingRule", am2_vi, 12)], "Partial") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 46);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 47);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 48);
											if (ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 0 && LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*)) > ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) - am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)]) {
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 54);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 55);
													if (am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] > 0) {
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 56);
														am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] -= 1;
														EntityChanged(0x01000000);
													}
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 60);
									if (this->attribute->am2_Ai_Status == 0) {
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 61);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 65);
					if (StringCompare(am2_vsSortingRule[ValidIndex("am_model.am_vsSortingRule", am2_vi, 12)], "Empty") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 66);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 67);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == 0) {
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 68);
										this->attribute->am2_Ai_Status = 1;
										EntityChanged(0x00000040);
									}
									else {
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 69);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 72);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 72);
								this->attribute->am2_Ai_Row = 1;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 73);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 74);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 75);
											if (ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == 99) {
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 76);
													am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] = ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 77);
													am2_viEmptyCarPullQty[ValidIndex("am_model.am_viEmptyCarPullQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarPullQty", this->attribute->am2_Ai_Row, 20)] = ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 78);
													am_localargs->ls299 = 0;
													ListCopy(LoadList, am_localargs->ls299, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													for (am_localargs->lv299 = (am_localargs->ls299) ? (am_localargs->ls299)->first : NULL; am_localargs->lv299; am_localargs->lv299 = am_localargs->lv299->next) {
														am2_vlptrEmpty = am_localargs->lv299->item;
														{
															{
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 80);
																ValidPtr(am2_vlptrEmpty, 32, load*)->attribute->am2_Ai_Status = 1;
																EntityChanged(0x00000040);
															}
														}
													}
													ListRemoveAllAndFree(LoadList, am_localargs->ls299); /* End of for each */
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 82);
													orderload(am2_olEmptyHold, NULL, ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 83);
													ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Seq = 9999;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 84);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 86);
													am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] -= 1;
													EntityChanged(0x01000000);
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 89);
									if (this->attribute->am2_Ai_Status == 0) {
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 90);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 94);
					if (StringCompare(am2_vsSortingRule[ValidIndex("am_model.am_vsSortingRule", am2_vi, 12)], "Mixed-Same") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 95);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 96);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 97);
											if (ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 1 && LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*)) > ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) - am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)]) {
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 102);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 103);
													if (am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] > 0) {
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 104);
														am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] -= 1;
														EntityChanged(0x01000000);
													}
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 108);
									if (this->attribute->am2_Ai_Status == 0) {
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 109);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 113);
					if (StringCompare(am2_vsSortingRule[ValidIndex("am_model.am_vsSortingRule", am2_vi, 12)], "Mixed") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 114);
							am2_Vi_ZoneCapacity[1] = 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 115);
							am2_Vi_ZoneCapacity[2] = LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*));
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 116);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)]) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 117);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) < am2_Vi_ZoneCapacity[2]) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 118);
											if (am2_viSTRunGen[1] == 0 || am2_viSTRunGen[2] == 0 || this->attribute->am2_Ai_Press == 1 && ListSize(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", am2_Vi_PrevPartType[2], 30)]) == 0 || this->attribute->am2_Ai_Press == 2 && ListSize(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", am2_Vi_PrevPartType[1], 30)]) == 0) {
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 120);
													am2_Vi_ZoneCapacity[1] = this->attribute->am2_Ai_Row;
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 121);
													am2_Vi_ZoneCapacity[2] = ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 122);
													this->attribute->am2_Ai_Status = 2;
													EntityChanged(0x00000040);
												}
											}
											else {
												{
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 125);
													if ((this->attribute->am2_Ai_Press == 1 && (this->attribute->am2_Ai_Zone != ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", am2_Vi_PrevPartType[2], 30)]), 32, load*)->attribute->am2_Ai_Zone || this->attribute->am2_Ai_Row != ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", am2_Vi_PrevPartType[2], 30)]), 32, load*)->attribute->am2_Ai_Row)) || (this->attribute->am2_Ai_Press == 2 && (this->attribute->am2_Ai_Zone != ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", am2_Vi_PrevType[1], 30)]), 32, load*)->attribute->am2_Ai_Zone || this->attribute->am2_Ai_Row != ValidPtr(ListLastItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", am2_Vi_PrevPartType[1], 30)]), 32, load*)->attribute->am2_Ai_Row))) {
														{
															AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 128);
															am2_Vi_ZoneCapacity[1] = this->attribute->am2_Ai_Row;
															EntityChanged(0x01000000);
														}
														{
															AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 129);
															am2_Vi_ZoneCapacity[2] = ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
															EntityChanged(0x01000000);
														}
														{
															AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 130);
															this->attribute->am2_Ai_Status = 2;
															EntityChanged(0x00000040);
														}
													}
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 134);
									this->attribute->am2_Ai_Row += 1;
									EntityChanged(0x00000040);
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 136);
							if (this->attribute->am2_Ai_Status == 2) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 137);
									this->attribute->am2_Ai_Row = am2_Vi_ZoneCapacity[1];
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 138);
									if (am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 0) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 139);
											am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] = 1;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 140);
											{
												int result = inccount(&(am2_C_ZoneMix[1]), 1, this, am_Sr_StoreIn, Step 2, am_localargs);
												if (result != Continue) return result;
Label2: ; /* Step 2 */
											}
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 141);
											{
												int result = deccount(&(am2_C_ZoneMix[2]), 1, this, am_Sr_StoreIn, Step 3, am_localargs);
												if (result != Continue) return result;
Label3: ; /* Step 3 */
											}
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 146);
					if (this->attribute->am2_Ai_Status == 0) {
						AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 147);
						am2_vi += 1;
						EntityChanged(0x01000000);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 148);
					if (am2_vi == 13) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 149);
							return waitorder(am2_olSpaceAvail, this, am_Sr_StoreIn, Step 4, am_localargs);
Label4: ; /* Step 4 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor, 150);
							am2_vi = 1;
							EntityChanged(0x01000000);
						}
					}
				}
			}
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls299);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Subroutine", "base.Sr_StoreIn", am_Sr_StoreIn, localactor);
	return retval;
} /* end of am_Sr_StoreIn */

static int32
pStorage_arriving(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv300; /* 'for each' loop variable */
		AMLoadList* ls300; /* 'for each' list */
		AMLoadListItem* lv301; /* 'for each' loop variable */
		AMLoadList* ls301; /* 'for each' list */
		AMLoadListItem* lv302; /* 'for each' loop variable */
		AMLoadList* ls302; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.pStorage", localactor);
	AMDebuggerParams("base.pStorage", pStorage_arriving, localactor, 0, NULL, NULL, NULL);
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
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 157);
			if (this->attribute->am2_Ai_CarQty == 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 158);
					this->attribute->am2_Ai_CarQty = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 159);
					am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] += 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 162);
			pushppa(this, pStorage_arriving, Step 2, am_localargs);
			pushppa(this, am2_Sr_StoreIn, Step 1, NULL);
			return Continue;
Label2: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 165);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 166);
			this->attribute->am2_Ai_Seq = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 167);
			am2_Vi_ZoneSpace[ValidIndex("am_model.am_Vi_ZoneSpace", this->attribute->am2_Ai_Zone, 3)][1] += 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 168);
			ListAppendItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], this);	/* append item to end of list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 169);
			ListAppendItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], this);	/* append item to end of list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 170);
			am2_Vld_Previous = this;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 171);
			am2_Vi_PrevPartType[ValidIndex("am_model.am_Vi_PrevPartType", this->attribute->am2_Ai_Press, 2)] = this->attribute->am2_Ai_Type;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 173);
			if (am2_viTrackStoreProcess == 1) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 175);
					SetString(&this->attribute->am2_As_Message, "STORE IN REQUEST");
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 176);
					SetString(&this->attribute->am2_As_CurrentLocation, "5_13");
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 177);
					SetString(&this->attribute->am2_As_NextLocation, am2_vsStation[ValidIndex("am_model.am_vsStation", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vsStation", this->attribute->am2_Ai_Row, 20)]);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 180);
					F_RequestMove(this);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 208);
			if (this->attribute->am2_Ai_Press == 1) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 209);
					this->attribute->am2_Ai_z = 2;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 210);
					if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_12, -9999), 38, simloc*)) == 1) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 211);
						return waitorder(am2_oST5_12, this, pStorage_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
						if (!this->inLeaveProc && this->nextproc) {
							retval = Continue;
							goto LabelRet;
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 212);
					pushppa(this, pStorage_arriving, Step 4, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_12, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 213);
					this->attribute->am2_Ai_z = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 214);
					if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999), 38, simloc*)) == 1) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 215);
						return waitorder(am2_oST5_13, this, pStorage_arriving, Step 5, am_localargs);
Label5: ; /* Step 5 */
						if (!this->inLeaveProc && this->nextproc) {
							retval = Continue;
							goto LabelRet;
						}
					}
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 217);
				if (this->attribute->am2_Ai_Press == 2) {
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 218);
						if (this->attribute->am2_Ai_pi == 1) {
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 219);
								this->attribute->am2_Ai_z = 1;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 220);
								if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_12, -9999), 38, simloc*)) == 1) {
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 221);
									return waitorder(am2_oST5_12, this, pStorage_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 222);
								pushppa(this, pStorage_arriving, Step 7, am_localargs);
								load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_12, -9999));
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label7: ; /* Step 7 */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 223);
								if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999), 38, simloc*)) == 1) {
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 224);
									return waitorder(am2_oST5_13, this, pStorage_arriving, Step 8, am_localargs);
Label8: ; /* Step 8 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
							}
						}
						else {
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 226);
							if (this->attribute->am2_Ai_pi == 2) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 227);
									this->attribute->am2_Ai_z = 3;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 228);
									pushppa(this, pStorage_arriving, Step 9, am_localargs);
									load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_11, -9999));
									pushppa(this, travel_to_loc, Step 1, NULL);
									return Continue; /* go move to location */
Label9: ; /* Step 9 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 229);
									if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999), 38, simloc*)) == 1) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 230);
										return waitorder(am2_oST5_13, this, pStorage_arriving, Step 10, am_localargs);
Label10: ; /* Step 10 */
										if (!this->inLeaveProc && this->nextproc) {
											retval = Continue;
											goto LabelRet;
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
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 234);
			pushppa(this, pStorage_arriving, Step 11, am_localargs);
			load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999));
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label11: ; /* Step 11 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 235);
			this->attribute->am2_Ai_z = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 236);
			if (waitfor(ToModelTime(am2_vrStopTime[5], UNITSECONDS), this, pStorage_arriving, Step 12, am_localargs) == Delayed)
				return Delayed;
Label12: ; /* Step 12 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 237);
			pushppa(this, pStorage_arriving, Step 13, am_localargs);
			load_SetDestLoc(this, am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)]);
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label13: ; /* Step 13 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 238);
			if (this->attribute->am2_Ai_Status == 0) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 239);
				return waitorder(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), this, pStorage_arriving, Step 14, am_localargs);
Label14: ; /* Step 14 */
				if (!this->inLeaveProc && this->nextproc) {
					retval = Continue;
					goto LabelRet;
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 241);
			while (this->attribute->am2_Ai_pi == 1234) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 242);
					if (this->attribute->am2_Ai_x == 1234 && RscGetStatus(ValidPtr(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 50, resource*)) == 1) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 242);
						downrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 243);
					am2_Vld_ZoneTakeDown[ValidIndex("am_model.am_Vld_ZoneTakeDown", this->attribute->am2_Ai_Zone, 3)] = this;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 244);
					pushppa(this, pStorage_arriving, Step 15, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST13, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label15: ; /* Step 15 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 249);
					pushppa(this, pStorage_arriving, Step 16, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST7_4, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label16: ; /* Step 16 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 250);
					if (this->attribute->am2_Ai_x == 1234) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 251);
							uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 253);
							am2_Vld_Recirc[1] = NULL;
							EntityChanged(0x01000000);
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 259);
					if (waitfor(ToModelTime(am2_vrStopTime[10], UNITSECONDS), this, pStorage_arriving, Step 17, am_localargs) == Delayed)
						return Delayed;
Label17: ; /* Step 17 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 260);
					this->attribute->am2_Ai_Zone = 3;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 261);
					this->attribute->am2_Ai_Status = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 263);
					while (this->attribute->am2_Ai_Status == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 264);
							if (this->attribute->am2_Ai_x == 1234) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 265);
									am2_Vi_Recirc[1] = this->attribute->am2_Ai_Type;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 266);
									this->attribute->am2_Ai_x = 0;
									EntityChanged(0x00000040);
								}
							}
							else {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 269);
									if (this->attribute->am2_Ai_Type == am2_Vi_Recirc[1]) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 270);
											if (ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", am2_Vi_Recirc[2], 20)]) < LocGetCapacity(ValidPtr(am2_vlocptrLane[3][ValidIndex("am_model.am_vlocptrLane", am2_Vi_Recirc[2], 20)], 38, simloc*))) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 271);
													this->attribute->am2_Ai_Row = am2_Vi_Recirc[2];
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 272);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
											}
										}
									}
									else {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 275);
										am2_Vi_Recirc[1] = this->attribute->am2_Ai_Type;
										EntityChanged(0x01000000);
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 278);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 278);
								this->attribute->am2_Ai_Row = 0;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 279);
							while (this->attribute->am2_Ai_Row < am2_viZoneLaneMax[3] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 280);
									this->attribute->am2_Ai_Row += 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 281);
									if (ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Zone != 3 || ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Row != this->attribute->am2_Ai_Row) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 282);
											if (ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0 && am2_Vi_ZoneMix[3][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 0 && ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == ListLastItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)])) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 283);
													if (LocGetCapacity(ValidPtr(am2_vlocptrLane[3][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*)) > ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) - am2_viEmptyCarFlushQty[3][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)]) {
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 284);
														this->attribute->am2_Ai_Status = 1;
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
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 289);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 289);
								this->attribute->am2_Ai_Row = 0;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 290);
							while (this->attribute->am2_Ai_Row < am2_viZoneLaneMax[3] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 291);
									this->attribute->am2_Ai_Row += 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 292);
									if (ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == 0) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 293);
										this->attribute->am2_Ai_Status = 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 296);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 296);
								this->attribute->am2_Ai_Row = 0;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 297);
							while (this->attribute->am2_Ai_Row < am2_viZoneLaneMax[3] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 298);
									this->attribute->am2_Ai_Row += 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 299);
									if (ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Zone != 3 || ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Row != this->attribute->am2_Ai_Row) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 300);
											if (ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0 && ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == 99) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 301);
													am2_viEmptyCarFlushQty[3][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] = ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 302);
													am2_viEmptyCarPullQty[3][ValidIndex("am_model.am_viEmptyCarPullQty", this->attribute->am2_Ai_Row, 20)] = ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													EntityChanged(0x01000000);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 303);
													am_localargs->ls300 = 0;
													ListCopy(LoadList, am_localargs->ls300, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
													for (am_localargs->lv300 = (am_localargs->ls300) ? (am_localargs->ls300)->first : NULL; am_localargs->lv300; am_localargs->lv300 = am_localargs->lv300->next) {
														am2_vlptrEmpty = am_localargs->lv300->item;
														{
															{
																AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 304);
																ValidPtr(am2_vlptrEmpty, 32, load*)->attribute->am2_Ai_Status = 1;
																EntityChanged(0x00000040);
															}
														}
													}
													ListRemoveAllAndFree(LoadList, am_localargs->ls300); /* End of for each */
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 306);
													orderload(am2_olEmptyHold, NULL, ListFirstItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 307);
													ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Seq = 9999;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 308);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 309);
													am2_viEmptyCarFlushQty[3][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] -= 1;
													EntityChanged(0x01000000);
												}
											}
										}
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 314);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 314);
								this->attribute->am2_Ai_Row = 0;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 315);
							while (this->attribute->am2_Ai_Row < am2_viZoneLaneMax[3] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 316);
									this->attribute->am2_Ai_Row += 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 317);
									if (ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Zone != 3 || ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Row != this->attribute->am2_Ai_Row) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 318);
											if (ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0 && am2_Vi_ZoneMix[3][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 1 && ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == ListLastItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)])) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 319);
													if (LocGetCapacity(ValidPtr(am2_vlocptrLane[3][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*)) > ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) - am2_viEmptyCarFlushQty[3][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)]) {
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 320);
														this->attribute->am2_Ai_Status = 1;
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
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 347);
							if (this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 348);
									this->attribute->am2_Ai_Row = 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 349);
									am2_Vi_ZoneCapacity[1] = 1;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 350);
									am2_Vi_ZoneCapacity[2] = LocGetCapacity(ValidPtr(am2_vlocptrLane[3][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*));
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 351);
									while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[3]) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 352);
											if (ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Zone != 3 || ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Row != this->attribute->am2_Ai_Row) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 353);
													if (ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) < am2_Vi_ZoneCapacity[2]) {
														{
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 354);
															am2_Vi_ZoneCapacity[1] = this->attribute->am2_Ai_Row;
															EntityChanged(0x01000000);
														}
														{
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 355);
															am2_Vi_ZoneCapacity[2] = ListSize(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
															EntityChanged(0x01000000);
														}
														{
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 356);
															this->attribute->am2_Ai_Status = 2;
															EntityChanged(0x00000040);
														}
													}
												}
											}
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 359);
											this->attribute->am2_Ai_Row += 1;
											EntityChanged(0x00000040);
										}
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 363);
							if (this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 364);
									this->attribute->am2_Ai_Row = 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 365);
									while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[3] && this->attribute->am2_Ai_Status == 0) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 366);
											if (ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Zone != 3 || ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Row != this->attribute->am2_Ai_Row) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 367);
													if (ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type) {
														{
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 368);
															if (ValidPtr(ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_x != 10) {
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 369);
																	{
																		int32 tempint = orderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
																		if (tempint > 0) backorderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]));	/* Place a backorder */
																	}
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 370);
																	ValidPtr(ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_pi = 1234;
																	EntityChanged(0x00000040);
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 373);
																	ListRemoveFirstMatch(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]));	/* remove first match from list */
																}
															}
														}
														{
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 375);
															this->attribute->am2_Ai_Status = 1;
															EntityChanged(0x00000040);
														}
													}
												}
											}
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 378);
											if (this->attribute->am2_Ai_Status == 0) {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 378);
												this->attribute->am2_Ai_Row += 1;
												EntityChanged(0x00000040);
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 380);
									if (this->attribute->am2_Ai_Status == 0) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 381);
											if (ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Zone == 3 && ValidPtr(am2_Vld_Previous, 32, load*)->attribute->am2_Ai_Row == 9) {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 381);
												this->attribute->am2_Ai_Row = 1;
												EntityChanged(0x00000040);
											}
											else {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 382);
												this->attribute->am2_Ai_Row = 9;
												EntityChanged(0x00000040);
											}
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 383);
											if (ValidPtr(ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_x != 10) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 384);
													{
														int32 tempint = orderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
														if (tempint > 0) backorderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]));	/* Place a backorder */
													}
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 385);
													ValidPtr(ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_pi = 1234;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 388);
													ListRemoveFirstMatch(LoadList, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], ListFirstItem(LoadList, am2_vldlstLanePull[3][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]));	/* remove first match from list */
												}
											}
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 390);
											this->attribute->am2_Ai_Status = 1;
											EntityChanged(0x00000040);
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 395);
					if (this->attribute->am2_Ai_Status == 2) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 396);
							this->attribute->am2_Ai_Row = am2_Vi_ZoneCapacity[1];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 397);
							if (am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 398);
									am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] = 1;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 399);
									{
										int result = inccount(&(am2_C_ZoneMix[1]), 1, this, pStorage_arriving, Step 18, am_localargs);
										if (result != Continue) return result;
Label18: ; /* Step 18 */
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 400);
									{
										int result = deccount(&(am2_C_ZoneMix[2]), 1, this, pStorage_arriving, Step 19, am_localargs);
										if (result != Continue) return result;
Label19: ; /* Step 19 */
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 404);
					am2_Vi_Recirc[2] = this->attribute->am2_Ai_Row;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 405);
					this->attribute->am2_Ai_pi = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 406);
					this->attribute->am2_Ai_Status = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 407);
					this->attribute->am2_Ai_x = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 408);
					ListAppendItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], this);	/* append item to end of list */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 409);
					order(1, am2_oZoneMixCheck, NULL, NULL); /* Place an order */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 410);
					pushppa(this, pStorage_arriving, Step 20, am_localargs);
					load_SetDestLoc(this, am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label20: ; /* Step 20 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 411);
					if (this->attribute->am2_Ai_Status == 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 412);
						return waitorder(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), this, pStorage_arriving, Step 21, am_localargs);
Label21: ; /* Step 21 */
						if (!this->inLeaveProc && this->nextproc) {
							retval = Continue;
							goto LabelRet;
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 415);
			ListRemoveFirstMatch(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], this);	/* remove first match from list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 416);
			ListRemoveFirstMatch(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], this);	/* remove first match from list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 418);
			am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] -= this->attribute->am2_Ai_CarQty;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 419);
			pushppa(this, pStorage_arriving, Step 22, am_localargs);
			pushppa(this, am2_Sr_OtherInv, Step 1, NULL);
			return Continue;
Label22: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 421);
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
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 423);
			order(1, am2_olSpaceAvail, NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 425);
			if (this->attribute->am2_Ai_Type >= 20) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 426);
					if (am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] < (am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)] * 1.1000000000000001) && CntGetCurConts(ValidPtr(&(am2_C_SPOControl[ValidIndex("am_model.am_C_SPOControl", this->attribute->am2_Ai_Type, 30)]), 10, counter*)) == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 427);
							if (CntGetCurConts(ValidPtr(&(am2_C_SPOControl[ValidIndex("am_model.am_C_SPOControl", am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_Type, 30)], 30)]), 10, counter*)) == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 428);
									am2_viTypeHold = this->attribute->am2_Ai_Type;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 429);
									this->attribute->am2_Ai_Type = am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_Type, 30)];
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 430);
									{
										int result = inccount(&(am2_C_SPOControl[ValidIndex("am_model.am_C_SPOControl", this->attribute->am2_Ai_Type, 30)]), 1, this, pStorage_arriving, Step 23, am_localargs);
										if (result != Continue) return result;
Label23: ; /* Step 23 */
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 431);
									clone(this, 1, am2_pSTRunControl, NULL);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 432);
									this->attribute->am2_Ai_Type = am2_viTypeHold;
									EntityChanged(0x00000040);
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 435);
					if (am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] <= am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)] / 4) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 436);
							am2_viTypeHold = this->attribute->am2_Ai_Type;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 437);
							this->attribute->am2_Ai_Type = am2_viModelV[ValidIndex("am_model.am_viModelV", this->attribute->am2_Ai_Type, 30)];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 438);
							am_localargs->ls301 = 0;
							ListCopy(LoadList, am_localargs->ls301, OrdGetLoadList(ValidPtr(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), 40, ordlist*)));
							for (am_localargs->lv301 = (am_localargs->ls301) ? (am_localargs->ls301)->first : NULL; am_localargs->lv301; am_localargs->lv301 = am_localargs->lv301->next) {
								am2_Vld_Crit = am_localargs->lv301->item;
								{
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 439);
										if (ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_NonSPO == 0 && ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && LdGetPriority(ValidPtr(am2_Vld_Crit, 32, load*)) == ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_StampingPriority) {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 440);
												LdSetPriority(ValidPtr(am2_Vld_Crit, 32, load*), ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_StampingPriority / 10);
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 441);
												orderload(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), am2_Vld_Crit); /* Place an order */
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 442);
												ListAppendItem(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Press, 2)], am2_Vld_Crit);	/* append item to end of list */
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls301); /* End of for each */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 445);
							this->attribute->am2_Ai_Type = am2_viTypeHold;
							EntityChanged(0x00000040);
						}
					}
				}
			}
			else {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 449);
					if (am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] < (am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)] * 1.1000000000000001) && CntGetCurConts(ValidPtr(&(am2_C_SPOControl[ValidIndex("am_model.am_C_SPOControl", this->attribute->am2_Ai_Type, 30)]), 10, counter*)) == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 450);
							{
								int result = inccount(&(am2_C_SPOControl[ValidIndex("am_model.am_C_SPOControl", this->attribute->am2_Ai_Type, 30)]), 1, this, pStorage_arriving, Step 24, am_localargs);
								if (result != Continue) return result;
Label24: ; /* Step 24 */
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 451);
							clone(this, 1, am2_pSTRunControl, NULL);
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 453);
					if (am2_viTotalStorageParts[ValidIndex("am_model.am_viTotalStorageParts", this->attribute->am2_Ai_Type, 30)] <= am2_viTriggerQty[ValidIndex("am_model.am_viTriggerQty", this->attribute->am2_Ai_Type, 30)] / 4) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 454);
							am_localargs->ls302 = 0;
							ListCopy(LoadList, am_localargs->ls302, OrdGetLoadList(ValidPtr(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), 40, ordlist*)));
							for (am_localargs->lv302 = (am_localargs->ls302) ? (am_localargs->ls302)->first : NULL; am_localargs->lv302; am_localargs->lv302 = am_localargs->lv302->next) {
								am2_Vld_Crit = am_localargs->lv302->item;
								{
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 455);
										if (ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_NonSPO == 0 && ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && LdGetPriority(ValidPtr(am2_Vld_Crit, 32, load*)) == ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_StampingPriority) {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 456);
												LdSetPriority(ValidPtr(am2_Vld_Crit, 32, load*), ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_StampingPriority / 10);
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 457);
												orderload(&(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), &(am2_O_StampingPriority[ValidIndex("am_model.am_O_StampingPriority", this->attribute->am2_Ai_Press, 2)]), am2_Vld_Crit); /* Place an order */
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 458);
												ListAppendItem(LoadList, am2_vldlstCritInv[ValidIndex("am_model.am_vldlstCritInv", ValidPtr(am2_Vld_Crit, 32, load*)->attribute->am2_Ai_Press, 2)], am2_Vld_Crit);	/* append item to end of list */
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls302); /* End of for each */
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor, 464);
			this->nextproc = am2_P_StorageToBuffer; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls300);
	ListRemoveAllAndFree(LoadList, am_localargs->ls301);
	ListRemoveAllAndFree(LoadList, am_localargs->ls302);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.pStorage", pStorage_arriving, localactor);
	return retval;
} /* end of pStorage_arriving */

static int32
pEmptyCarrierLaneSelect_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", localactor);
	AMDebuggerParams("base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	case Step 7: goto Label7;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 470);
			pushppa(this, pEmptyCarrierLaneSelect_arriving, Step 2, am_localargs);
			load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST5_13, -9999));
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 471);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 472);
			this->attribute->am2_Ai_x = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 475);
			if (OrdGetCurConts(am2_olEmptyHold) > 0 && ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Row, 20)]) > 0 && (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Row, 20)]) < LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Row, 20)], 38, simloc*)))) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 478);
					this->attribute->am2_Ai_Zone = ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Zone;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 479);
					this->attribute->am2_Ai_Row = ValidPtr(ListLastItem(LoadList, OrdGetLoadList(am2_olEmptyHold)), 32, load*)->attribute->am2_Ai_Row;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 480);
					this->attribute->am2_Ai_Status = 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 483);
			while (this->attribute->am2_Ai_Status == 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 485);
					while (this->attribute->am2_Ai_x <= am2_viNumZones && this->attribute->am2_Ai_Status == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 487);
							this->attribute->am2_Ai_Zone = am2_Vi_EmptyZone[ValidIndex("am_model.am_Vi_EmptyZone", this->attribute->am2_Ai_x, 3)];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 488);
							this->attribute->am2_Ai_Row = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 489);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 491);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 493);
											if (ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*)) > ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)])) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 496);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 499);
									if (this->attribute->am2_Ai_Status == 0) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 500);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 502);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 503);
								this->attribute->am2_Ai_Row = 1;
								EntityChanged(0x00000040);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 504);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 506);
									if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == 0) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 508);
											this->attribute->am2_Ai_Status = 1;
											EntityChanged(0x00000040);
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 510);
									if (this->attribute->am2_Ai_Status == 0) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 511);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 514);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 514);
								this->attribute->am2_Ai_x += 1;
								EntityChanged(0x00000040);
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 517);
					this->attribute->am2_Ai_x = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 518);
					while (this->attribute->am2_Ai_x <= am2_viNumZones && this->attribute->am2_Ai_Status == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 519);
							this->attribute->am2_Ai_Zone = am2_Vi_EmptyZone[ValidIndex("am_model.am_Vi_EmptyZone", this->attribute->am2_Ai_x, 3)];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 520);
							this->attribute->am2_Ai_Row = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 521);
							while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)] && this->attribute->am2_Ai_Status == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 522);
									if (ValidPtr(ListLastItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type == 99) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 523);
											if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) == LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)], 38, simloc*))) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 524);
													ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Seq = 9999;
													EntityChanged(0x00000040);
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 525);
													{
														int32 tempint = orderload(am2_olEmptyHold, NULL, ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)])); /* Place an order */
														if (tempint > 0) backorderload(am2_olEmptyHold, NULL, ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]));	/* Place a backorder */
													}
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 526);
													ListRemoveFirstMatch(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]));	/* remove first match from list */
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 527);
													this->attribute->am2_Ai_Status = 1;
													EntityChanged(0x00000040);
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 530);
									if (this->attribute->am2_Ai_Status == 0) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 530);
										this->attribute->am2_Ai_Row += 1;
										EntityChanged(0x00000040);
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 532);
							if (this->attribute->am2_Ai_Status == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 532);
								this->attribute->am2_Ai_x += 1;
								EntityChanged(0x00000040);
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 535);
					if (this->attribute->am2_Ai_Status == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 536);
							return waitorder(am2_olSpaceAvail, this, pEmptyCarrierLaneSelect_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 538);
							this->attribute->am2_Ai_x = 1;
							EntityChanged(0x00000040);
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 542);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 544);
			ListAppendItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], this);	/* append item to end of list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 545);
			pushppa(this, pEmptyCarrierLaneSelect_arriving, Step 4, am_localargs);
			load_SetDestLoc(this, am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Row, 20)]);
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label4: ; /* Step 4 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 546);
			if (this->attribute->am2_Ai_Status == 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 548);
					return waitorder(am2_olEmptyHold, this, pEmptyCarrierLaneSelect_arriving, Step 5, am_localargs);
Label5: ; /* Step 5 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 550);
					if (RscGetStatus(ValidPtr(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 50, resource*)) == 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 551);
						uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 553);
					if (getrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 1, this, pEmptyCarrierLaneSelect_arriving, Step 6, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label6: ; /* Step 6 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 554);
					freersrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 1, this);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 555);
					downrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 556);
					am2_Vld_ZoneTakeDown[ValidIndex("am_model.am_Vld_ZoneTakeDown", this->attribute->am2_Ai_Zone, 3)] = this;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 559);
			if (this->attribute->am2_Ai_Seq == 9999) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 560);
					uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 561);
					if (OrdGetCurConts(am2_oEmptyBufferCleanUp) > 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 562);
						order(1, am2_oEmptyBufferCleanUp, NULL, NULL); /* Place an order */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 565);
			ListRemoveFirstMatch(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], this);	/* remove first match from list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 568);
			if (this->attribute->am2_Ai_Seq == 9999 && am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] > 0) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 568);
				am2_viEmptyCarFlushQty[ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarFlushQty", this->attribute->am2_Ai_Row, 20)] = 0;
				EntityChanged(0x01000000);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 571);
			order(1, am2_olSpaceAvail, NULL, NULL); /* Place an order */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 574);
			pushppa(this, pEmptyCarrierLaneSelect_arriving, Step 7, am_localargs);
			load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST13, -9999));
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label7: ; /* Step 7 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 576);
			if (this->attribute->am2_Ai_Seq == 9999 && am2_Vi_ZoneEmptyFlush == 1) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 576);
				am2_Vi_ZoneEmptyFlush = 0;
				EntityChanged(0x01000000);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor, 578);
			this->nextproc = am2_pEmptyCarrierReturn; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.pEmptyCarrierLaneSelect", pEmptyCarrierLaneSelect_arriving, localactor);
	return retval;
} /* end of pEmptyCarrierLaneSelect_arriving */

static int32
P_WeldSched_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", localactor);
	AMDebuggerParams("base.P_WeldSched", P_WeldSched_arriving, localactor, 0, NULL, NULL, NULL);
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
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 594);
			pushppa(this, P_WeldSched_arriving, Step 2, am_localargs);
			pushppa(this, am2_SR_AttributeReset, Step 1, NULL);
			return Continue;
Label2: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 595);
			if (CurProcIndex() == 1) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 596);
				am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)] = OpenFilePtr(am_model.$sys, "base.arc/DATA/weld1prod.dat", "r");
			}
			else {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 597);
				if (CurProcIndex() == 2) {
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 598);
					am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)] = OpenFilePtr(am_model.$sys, "base.arc/DATA/weld2prod.dat", "r");
				}
			}
		}
		{
			if (isFileValid(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 599);
				rflag = readFile(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)]->fp, "\n", &st1, NULL);
				SetFileAtEof(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], EOF, rflag);
			}
		}
		{
			if (isFileValid(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 600);
				rflag = readFile(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)]->fp, "\n", &st1, NULL);
				SetFileAtEof(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], EOF, rflag);
			}
		}
		{
			if (isFileValid(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], 1)) {
				int rflag;
				static ReadRef st1;

				setupReadRef(&st1, 1, am_model.am_Ai_LotQty$att, &this->attribute->am2_Ai_LotQty, NULL, -1, FALSE);
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 601);
				rflag = readFile(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)]->fp, "\t", &st1, NULL);
				SetFileAtEof(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], EOF, rflag);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 602);
			while (this->attribute->am2_Ai_LotQty > 0) {
				{
					if (isFileValid(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], 1)) {
						int rflag;
						static ReadRef st1;
						static ReadRef st2;
						static ReadRef st3;
						static ReadRef st4;
						static ReadRef st5;

						setupReadRef(&st1, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
						setupReadRef(&st2, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
						setupReadRef(&st3, 0, am_model.am_Vs_junk$var, &am2_Vs_junk, NULL, -1, FALSE);
						setupReadRef(&st4, 0, am_model.am_Vs_ProdType$var, &am2_Vs_ProdType[1][ValidIndex("am_model.am_Vs_ProdType", CurProcIndex(), 2)], NULL, -1, FALSE);
						setupReadRef(&st5, 0, am_model.am_Vs_ProdType$var, &am2_Vs_ProdType[2][ValidIndex("am_model.am_Vs_ProdType", CurProcIndex(), 2)], NULL, -1, FALSE);
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 603);
						rflag = readFile(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)]->fp, "\t", &st1, &st2, &st3, &st4, &st5, NULL);
						SetFileAtEof(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], EOF, rflag);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 605);
					if (CurProcIndex() == 1) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 606);
							this->attribute->am2_Ai_Line = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 607);
							this->attribute->am2_Ai_pi = 1;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 608);
							SetString(&this->attribute->am2_As_Type, am2_Vs_ProdType[1][ValidIndex("am_model.am_Vs_ProdType", CurProcIndex(), 2)]);
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 609);
							clone(this, 1, &(am2_P_SPO_Release[1]), NULL);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 610);
							clone(this, 1, &(am2_P_SPO_Process[1]), NULL);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 611);
							this->attribute->am2_Ai_pi = 2;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 612);
							SetString(&this->attribute->am2_As_Type, am2_Vs_ProdType[2][ValidIndex("am_model.am_Vs_ProdType", CurProcIndex(), 2)]);
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 613);
							clone(this, 1, &(am2_P_SPO_Release[2]), NULL);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 614);
							clone(this, 1, &(am2_P_SPO_Process[2]), NULL);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 615);
							return waitorder(&(am2_O_SPO_ReleaseComplete[1]), this, P_WeldSched_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 616);
							return waitorder(&(am2_O_SPO_ReleaseComplete[2]), this, P_WeldSched_arriving, Step 4, am_localargs);
Label4: ; /* Step 4 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
					}
					else {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 619);
						if (CurProcIndex() == 2) {
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 620);
								this->attribute->am2_Ai_Line = 2;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 621);
								this->attribute->am2_Ai_pi = 3;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 622);
								SetString(&this->attribute->am2_As_Type, am2_Vs_ProdType[1][ValidIndex("am_model.am_Vs_ProdType", CurProcIndex(), 2)]);
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 623);
								clone(this, 1, &(am2_P_SPO_Release[3]), NULL);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 624);
								clone(this, 1, &(am2_P_SPO_Process[3]), NULL);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 625);
								this->attribute->am2_Ai_pi = 4;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 626);
								SetString(&this->attribute->am2_As_Type, am2_Vs_ProdType[2][ValidIndex("am_model.am_Vs_ProdType", CurProcIndex(), 2)]);
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 627);
								clone(this, 1, &(am2_P_SPO_Release[4]), NULL);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 628);
								clone(this, 1, &(am2_P_SPO_Process[4]), NULL);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 629);
								return waitorder(&(am2_O_SPO_ReleaseComplete[3]), this, P_WeldSched_arriving, Step 5, am_localargs);
Label5: ; /* Step 5 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 630);
								return waitorder(&(am2_O_SPO_ReleaseComplete[4]), this, P_WeldSched_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
						}
					}
				}
				{
					if (isFileValid(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], 1)) {
						int rflag;
						static ReadRef st1;

						setupReadRef(&st1, 1, am_model.am_Ai_LotQty$att, &this->attribute->am2_Ai_LotQty, NULL, -1, FALSE);
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 632);
						rflag = readFile(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)]->fp, "\t", &st1, NULL);
						SetFileAtEof(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)], EOF, rflag);
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 634);
			CloseFilePtr(am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)]);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 635);
			am2_Vflptr_WeldSched[ValidIndex("am_model.am_Vflptr_WeldSched", CurProcIndex(), 2)] = NULL;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor, 636);
			this->nextproc = &(am2_P_WeldSched[ValidIndex("am_model.am_P_WeldSched", CurProcIndex(), 2)]); /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_WeldSched", P_WeldSched_arriving, localactor);
	return retval;
} /* end of P_WeldSched_arriving */

static int32
P_SPO_Process_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", localactor);
	AMDebuggerParams("base.P_SPO_Process", P_SPO_Process_arriving, localactor, 0, NULL, NULL, NULL);
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
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 651);
			am2_Vld_SPO[ValidIndex("am_model.am_Vld_SPO", CurProcIndex(), 4)] = this;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 653);
			pushppa(this, P_SPO_Process_arriving, Step 2, am_localargs);
			pushppa(this, inqueue, Step 1, &(am2_Q_SPO[ValidIndex("am_model.am_Q_SPO", CurProcIndex(), 4)]));
			return Continue; /* go move into territory */
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 654);
			if (getrsrc(&(am2_R_SPO[ValidIndex("am_model.am_R_SPO", CurProcIndex(), 4)]), 1, this, P_SPO_Process_arriving, Step 3, am_localargs) == Delayed)
				return Delayed;  /* go wait for resource */
Label3: ; /* Step 3 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 656);
			pushppa(this, P_SPO_Process_arriving, Step 4, am_localargs);
			pushppa(this, am2_SR_TypeIndex, Step 1, NULL);
			return Continue;
Label4: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 660);
			if (this->attribute->am2_Ai_Type != am2_Vi_PrevType[ValidIndex("am_model.am_Vi_PrevType", CurProcIndex(), 4)] && am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] != NULL && ValidPtr(am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 662);
					if (OrdGetCurConts(ValidPtr(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], 40, ordlist*)) > 0 && (am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] == ListFirstItem(LoadList, OrdGetLoadList(ValidPtr(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], 40, ordlist*))) || am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] == ListLastItem(LoadList, OrdGetLoadList(ValidPtr(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], 40, ordlist*))))) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 665);
							ValidPtr(am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status = 8888;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 666);
							orderload(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], NULL, am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)]); /* Place an order */
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 668);
					am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] = NULL;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 672);
			if (ListSize(LoadList, am2_Vldlst_Partial[ValidIndex("am_model.am_Vldlst_Partial", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_Partial", this->attribute->am2_Ai_Type, 30)]) > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 673);
					clone(this, 1, &(am2_P_PartialRelease[ValidIndex("am_model.am_P_PartialRelease", CurProcIndex(), 4)]), am2_L_invis);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 674);
					return waitorder(&(am2_O_PartialRelease[ValidIndex("am_model.am_O_PartialRelease", CurProcIndex(), 4)]), this, P_SPO_Process_arriving, Step 5, am_localargs);
Label5: ; /* Step 5 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 676);
			if (PrcGetTotEntriesA(ValidPtr(&(am2_P_SPO_Process[ValidIndex("am_model.am_P_SPO_Process", CurProcIndex(), 4)]), 45, process*)) == 1) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 676);
				return waitorder(am2_olWarmUp, this, P_SPO_Process_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
				if (!this->inLeaveProc && this->nextproc) {
					retval = Continue;
					goto LabelRet;
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 678);
			am2_Vi_ProdCount[ValidIndex("am_model.am_Vi_ProdCount", CurProcIndex(), 4)] = 0;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 679);
			{
				char* pArg1 = "SPO #";
				char* pArg2 = " ";
				int32 pArg3 = CurProcIndex();
				char* pArg4 = " ";
				char* pArg5 = "processing";
				char* pArg6 = " ";
				char* pArg7 = this->attribute->am2_As_Type;
				char* pArg8 = " ";
				int32 pArg9 = am2_Vi_ProdCount[ValidIndex("am_model.am_Vi_ProdCount", CurProcIndex(), 4)];
				char* pArg10 = " ";
				char* pArg11 = "of";
				char* pArg12 = " ";
				int32 pArg13 = this->attribute->am2_Ai_LotQty;

				updatelabel(&(am2_Lb_SPO[ValidIndex("am_model.am_Lb_SPO", CurProcIndex(), 4)]), "%s%s%d%s%s%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 680);
			am2_Vi_LotQty[ValidIndex("am_model.am_Vi_LotQty", CurProcIndex(), 4)] = this->attribute->am2_Ai_LotQty;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 682);
			LocSetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST108, -9999), 38, simloc*), 4);
			EntityChanged(0x00000080);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 683);
			am2_Vi_SPO[ValidIndex("am_model.am_Vi_SPO", CurProcIndex(), 4)] = this->attribute->am2_Ai_LotQty;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 685);
			if (this->attribute->am2_Ai_Type != am2_Vi_PrevType[ValidIndex("am_model.am_Vi_PrevType", CurProcIndex(), 4)]) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 686);
					if (CurProcIndex() < 3) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 687);
							if (OrdGetCurConts(ValidPtr(&(am2_oWeldChangeover[1]), 40, ordlist*)) == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 688);
								return waitorder(&(am2_oWeldChangeover[1]), this, P_SPO_Process_arriving, Step 7, am_localargs);
Label7: ; /* Step 7 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							else {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 690);
									downrsrc(&(am2_R_Weld[1]));
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 691);
									MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[1]), 50, resource*)), str2StatePtr("Changeover"), this);
									EntityChanged(0x00020000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 692);
									if (getrsrc(&(am2_R_WeldShift[1]), 1, this, P_SPO_Process_arriving, Step 8, am_localargs) == Delayed)
										return Delayed;  /* go wait for resource */
Label8: ; /* Step 8 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 693);
									if (waitfor(ToModelTime(am2_Vr_WeldChangeoverTime[1], UNITMINUTES), this, P_SPO_Process_arriving, Step 9, am_localargs) == Delayed)
										return Delayed;
Label9: ; /* Step 9 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 694);
									freersrc(&(am2_R_WeldShift[1]), 1, this);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 695);
									uprsrc(&(am2_R_Weld[1]));
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 696);
									MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[1]), 50, resource*)), str2StatePtr("Production"), this);
									EntityChanged(0x00020000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 697);
									order(1, &(am2_oWeldChangeover[1]), NULL, NULL); /* Place an order */
								}
							}
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 701);
							if (OrdGetCurConts(ValidPtr(&(am2_oWeldChangeover[2]), 40, ordlist*)) == 0) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 702);
								return waitorder(&(am2_oWeldChangeover[2]), this, P_SPO_Process_arriving, Step 10, am_localargs);
Label10: ; /* Step 10 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							else {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 704);
									downrsrc(&(am2_R_Weld[2]));
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 705);
									MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[2]), 50, resource*)), str2StatePtr("Changeover"), this);
									EntityChanged(0x00020000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 706);
									if (getrsrc(&(am2_R_WeldShift[2]), 1, this, P_SPO_Process_arriving, Step 11, am_localargs) == Delayed)
										return Delayed;  /* go wait for resource */
Label11: ; /* Step 11 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 707);
									if (waitfor(ToModelTime(am2_Vr_WeldChangeoverTime[2], UNITMINUTES), this, P_SPO_Process_arriving, Step 12, am_localargs) == Delayed)
										return Delayed;
Label12: ; /* Step 12 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 708);
									freersrc(&(am2_R_WeldShift[2]), 1, this);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 709);
									uprsrc(&(am2_R_Weld[2]));
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 710);
									MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[2]), 50, resource*)), str2StatePtr("Production"), this);
									EntityChanged(0x00020000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 711);
									order(1, &(am2_oWeldChangeover[2]), NULL, NULL); /* Place an order */
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 716);
			while (this->attribute->am2_Ai_LotQty > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 718);
					if (am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] == NULL) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 719);
							if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), 40, ordlist*)) > 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 720);
									order(1, &(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), NULL, NULL); /* Place an order */
								}
							}
							else {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 723);
									if (CurProcIndex() < 3) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 724);
											downrsrc(&(am2_R_LineStarved[1]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 725);
											downrsrc(&(am2_R_Weld[1]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 726);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[1]), 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x00020000);
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 727);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[1]), 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x00020000);
										}
									}
									else {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 730);
											downrsrc(&(am2_R_LineStarved[2]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 731);
											downrsrc(&(am2_R_Weld[2]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 732);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[2]), 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x00020000);
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 733);
											MonSetState(RscGetMonitor(ValidPtr(&(am2_R_LineStarved[2]), 50, resource*)), str2StatePtr("Starved"), this);
											EntityChanged(0x00020000);
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 735);
									return waitorder(&(am2_O_WeldBufferStarve[ValidIndex("am_model.am_O_WeldBufferStarve", CurProcIndex(), 4)]), this, P_SPO_Process_arriving, Step 13, am_localargs);
Label13: ; /* Step 13 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 736);
									if (waitfor(ToModelTime(0.10000000000000001, UNITSECONDS), this, P_SPO_Process_arriving, Step 14, am_localargs) == Delayed)
										return Delayed;
Label14: ; /* Step 14 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 737);
									if (ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(ValidPtr(&(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), 40, ordlist*))), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 738);
										order(1, &(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), NULL, NULL); /* Place an order */
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 739);
									if (CurProcIndex() < 3) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 740);
											uprsrc(&(am2_R_LineStarved[1]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 741);
											uprsrc(&(am2_R_Weld[1]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 742);
											if (StringCompare(statename(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[1]), 50, resource*)))), statename(am2_Starved)) == 0) {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 742);
												MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[1]), 50, resource*)), str2StatePtr("Production"), this);
												EntityChanged(0x00020000);
											}
										}
									}
									else {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 745);
											uprsrc(&(am2_R_LineStarved[2]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 746);
											uprsrc(&(am2_R_Weld[2]));
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 747);
											if (StringCompare(statename(MonGetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[2]), 50, resource*)))), statename(am2_Starved)) == 0) {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 747);
												MonSetState(RscGetMonitor(ValidPtr(&(am2_R_Weld[2]), 50, resource*)), str2StatePtr("Production"), this);
												EntityChanged(0x00020000);
											}
										}
									}
								}
							}
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 753);
							if (ValidPtr(am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty < 5 || LocGetRemCap(ValidPtr(am2_Vloc_B4Unload[ValidIndex("am_model.am_Vloc_B4Unload", CurProcIndex(), 4)], 38, simloc*)) > 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 754);
									if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), 40, ordlist*)) > 0 && ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(ValidPtr(&(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), 40, ordlist*))), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 756);
										order(1, &(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", CurProcIndex(), 4)]), NULL, NULL); /* Place an order */
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 760);
					{
						int32 tempint = order(2, am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], NULL, NULL);	/* Place a backorder */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 762);
					return waitorder(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][2], this, P_SPO_Process_arriving, Step 15, am_localargs);
Label15: ; /* Step 15 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 763);
					if (this->attribute->am2_Ai_Type != ValidPtr(ListFirstItem(LoadList, VehGetLoadList(ValidPtr(VTGetQualifier(am_model.am_K_RBT.am_Robot, CurProcIndex() + 2, -9999), 76, vehicle*))), 32, load*)->attribute->am2_Ai_Type) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 764);
							{
								char* pArg1 = "TYPE do not Match between Rbt SPO & Required";
								char* pArg2 = " ";
								int32 pArg3 = this->attribute->am2_Ai_Type;
								char* pArg4 = " ";
								char* pArg5 = "-->";
								char* pArg6 = " ";
								int32 pArg7 = ValidPtr(ListFirstItem(LoadList, VehGetLoadList(ValidPtr(VTGetQualifier(am_model.am_K_RBT.am_Robot, CurProcIndex() + 2, -9999), 76, vehicle*))), 32, load*)->attribute->am2_Ai_Type;
								char* pArg8 = " ";
								char* pArg9 = "in line";
								char* pArg10 = " ";
								int32 pArg11 = CurProcIndex();

								message("%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11);
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 768);
					LdSetType(this, am2_vlt_LoadType[ValidIndex("am_model.am_vlt_LoadType", this->attribute->am2_Ai_Type, 30)]);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 769);
					LdSetColor(this, am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", this->attribute->am2_Ai_Type, 30)]);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 771);
					return waitorder(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][3], this, P_SPO_Process_arriving, Step 16, am_localargs);
Label16: ; /* Step 16 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 773);
					if (CurProcIndex() < 3) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 774);
							if (getrsrc(&(am2_R_Weld[1]), 1, this, P_SPO_Process_arriving, Step 17, am_localargs) == Delayed)
								return Delayed;  /* go wait for resource */
Label17: ; /* Step 17 */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 775);
							if (getrsrc(&(am2_R_LineStarved[1]), 1, this, P_SPO_Process_arriving, Step 18, am_localargs) == Delayed)
								return Delayed;  /* go wait for resource */
Label18: ; /* Step 18 */
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 778);
							if (getrsrc(&(am2_R_Weld[2]), 1, this, P_SPO_Process_arriving, Step 19, am_localargs) == Delayed)
								return Delayed;  /* go wait for resource */
Label19: ; /* Step 19 */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 779);
							if (getrsrc(&(am2_R_LineStarved[2]), 1, this, P_SPO_Process_arriving, Step 20, am_localargs) == Delayed)
								return Delayed;  /* go wait for resource */
Label20: ; /* Step 20 */
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 782);
					if (waitfor(ToModelTime(am2_Vr_SPOCycleTime[ValidIndex("am_model.am_Vr_SPOCycleTime", CurProcIndex(), 4)], UNITSECONDS), this, P_SPO_Process_arriving, Step 21, am_localargs) == Delayed)
						return Delayed;
Label21: ; /* Step 21 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 784);
					if (CurProcIndex() < 3) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 785);
							freersrc(&(am2_R_Weld[1]), 1, this);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 786);
							freersrc(&(am2_R_LineStarved[1]), 1, this);
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 789);
							freersrc(&(am2_R_Weld[2]), 1, this);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 790);
							freersrc(&(am2_R_LineStarved[2]), 1, this);
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 793);
					am2_Vi_ProdCount[ValidIndex("am_model.am_Vi_ProdCount", CurProcIndex(), 4)] += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 794);
					am2_Vi_shiftProd[ValidIndex("am_model.am_Vi_shiftProd", CurProcIndex(), 4)] += 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 795);
					{
						char* pArg1 = "SPO #";
						char* pArg2 = " ";
						int32 pArg3 = CurProcIndex();
						char* pArg4 = " ";
						char* pArg5 = "processing";
						char* pArg6 = " ";
						char* pArg7 = this->attribute->am2_As_Type;
						char* pArg8 = " ";
						int32 pArg9 = am2_Vi_ProdCount[ValidIndex("am_model.am_Vi_ProdCount", CurProcIndex(), 4)];
						char* pArg10 = " ";
						char* pArg11 = "of";
						char* pArg12 = " ";
						int32 pArg13 = am2_Vi_LotQty[ValidIndex("am_model.am_Vi_LotQty", CurProcIndex(), 4)];
						char* pArg14 = " ";
						char* pArg15 = ">>";
						char* pArg16 = " ";
						int32 pArg17 = am2_Vi_shiftProd[ValidIndex("am_model.am_Vi_shiftProd", CurProcIndex(), 4)];

						updatelabel(&(am2_Lb_SPO[ValidIndex("am_model.am_Lb_SPO", CurProcIndex(), 4)]), "%s%s%d%s%s%s%s%s%d%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13, pArg14, pArg15, pArg16, pArg17);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 797);
					this->attribute->am2_Ai_LotQty -= 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 798);
					LdSetType(this, am2_L_invis);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 800);
					if (CurProcIndex() < 3) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 801);
							if (am2_Vi_shiftProd[ValidIndex("am_model.am_Vi_shiftProd", CurProcIndex(), 4)] == (am2_Vi_WeldOutputTarget[1] + am2_Vi_WeldGap[ValidIndex("am_model.am_Vi_WeldGap", CurProcIndex(), 4)])) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 802);
								return waitorder(am2_O_WeldOutputTarget, this, P_SPO_Process_arriving, Step 22, am_localargs);
Label22: ; /* Step 22 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 805);
							if (am2_Vi_shiftProd[ValidIndex("am_model.am_Vi_shiftProd", CurProcIndex(), 4)] == (am2_Vi_WeldOutputTarget[2] + am2_Vi_WeldGap[ValidIndex("am_model.am_Vi_WeldGap", CurProcIndex(), 4)])) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 806);
								return waitorder(am2_O_WeldOutputTarget, this, P_SPO_Process_arriving, Step 23, am_localargs);
Label23: ; /* Step 23 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 809);
					if (am2_Vi_ProdCount[ValidIndex("am_model.am_Vi_ProdCount", CurProcIndex(), 4)] == 1) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 810);
							if (CurProcIndex() < 3) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 811);
									if (OrdGetCurConts(ValidPtr(&(am2_O_Synch[1]), 40, ordlist*)) == 1) {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 811);
										order(-1, &(am2_O_Synch[1]), NULL, NULL); /* Place an order */
									}
									else {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 812);
										return waitorder(&(am2_O_Synch[1]), this, P_SPO_Process_arriving, Step 24, am_localargs);
Label24: ; /* Step 24 */
										if (!this->inLeaveProc && this->nextproc) {
											retval = Continue;
											goto LabelRet;
										}
									}
								}
							}
							else {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 814);
								if (CurProcIndex() > 2) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 815);
										if (OrdGetCurConts(ValidPtr(&(am2_O_Synch[2]), 40, ordlist*)) == 1) {
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 815);
											order(-1, &(am2_O_Synch[2]), NULL, NULL); /* Place an order */
										}
										else {
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 816);
											return waitorder(&(am2_O_Synch[2]), this, P_SPO_Process_arriving, Step 25, am_localargs);
Label25: ; /* Step 25 */
											if (!this->inLeaveProc && this->nextproc) {
												retval = Continue;
												goto LabelRet;
											}
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 821);
					am2_Vi_SPO[ValidIndex("am_model.am_Vi_SPO", CurProcIndex(), 4)] = this->attribute->am2_Ai_LotQty;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 823);
			LocSetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST108, -9999), 38, simloc*), 1);
			EntityChanged(0x00000080);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 824);
			am2_Vi_PrevType[ValidIndex("am_model.am_Vi_PrevType", CurProcIndex(), 4)] = this->attribute->am2_Ai_Type;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor, 825);
			freersrc(&(am2_R_SPO[ValidIndex("am_model.am_R_SPO", CurProcIndex(), 4)]), 1, this);
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_SPO_Process", P_SPO_Process_arriving, localactor);
	return retval;
} /* end of P_SPO_Process_arriving */

static int32
P_SPO_Release_arriving(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv303; /* 'for each' loop variable */
		AMLoadList* ls303; /* 'for each' list */
		AMLoadListItem* lv304; /* 'for each' loop variable */
		AMLoadList* ls304; /* 'for each' list */
		AMLoadListItem* lv305; /* 'for each' loop variable */
		AMLoadList* ls305; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", localactor);
	AMDebuggerParams("base.P_SPO_Release", P_SPO_Release_arriving, localactor, 0, NULL, NULL, NULL);
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
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 846);
			F_getID();
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 847);
			pushppa(this, P_SPO_Release_arriving, Step 2, am_localargs);
			pushppa(this, am2_SR_TypeIndex, Step 1, NULL);
			return Continue;
Label2: ;
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 848);
			this->attribute->am2_Ai_Seq = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 849);
			if (this->attribute->am2_Ai_Type < 1 || this->attribute->am2_Ai_Type > 30) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 850);
					{
						char* pArg1 = "ERROR in setting Ai_Type:Check";
						char* pArg2 = " ";
						char* pArg3 = rel_actorname(this, am_model.$sys);
						char* pArg4 = " ";
						char* pArg5 = rel_actorname(LdGetCurProc(this), am_model.$sys);

						message("%s%s%s%s%s", pArg1, pArg2, pArg3, pArg4, pArg5);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 851);
					{
						int result = deccount(am2_C_Error, 1, this, P_SPO_Release_arriving, Step 3, am_localargs);
						if (result != Continue) return result;
Label3: ; /* Step 3 */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 854);
			if (ListSize(LoadList, am2_Vldlst_ReleasePartial[ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Type, 30)]) > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 855);
					if (getrsrc(am2_Vrsrc_ReleasePartial[ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Type, 30)], 1, this, P_SPO_Release_arriving, Step 4, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 856);
					am_localargs->ls303 = 0;
					ListCopy(LoadList, am_localargs->ls303, am2_Vldlst_ReleasePartial[ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Type, 30)]);
					for (am_localargs->lv303 = (am_localargs->ls303) ? (am_localargs->ls303)->first : NULL; am_localargs->lv303; am_localargs->lv303 = am_localargs->lv303->next) {
						am2_Vld_SPO_Release[ValidIndex("am_model.am_Vld_SPO_Release", CurProcIndex(), 4)] = am_localargs->lv303->item;
						{
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 857);
								if (ValidPtr(am2_Vld_SPO_Release[ValidIndex("am_model.am_Vld_SPO_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty > 0) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 858);
										this->attribute->am2_Ai_LotQty = this->attribute->am2_Ai_LotQty - ValidPtr(am2_Vld_SPO_Release[ValidIndex("am_model.am_Vld_SPO_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 859);
										ValidPtr(am2_Vld_SPO_Release[ValidIndex("am_model.am_Vld_SPO_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty = 0;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 860);
										{
											int32 tempint = order(1, am2_Vo_ReleasePartial[ValidIndex("am_model.am_Vo_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vo_ReleasePartial", this->attribute->am2_Ai_Type, 30)], NULL, NULL); /* Place an order */
											if (tempint > 0) backorder(tempint, am2_Vo_ReleasePartial[ValidIndex("am_model.am_Vo_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vo_ReleasePartial", this->attribute->am2_Ai_Type, 30)], NULL, NULL);	/* Place a backorder */
										}
									}
								}
								else {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 863);
										{
											{
												char* pArg1 = "0 QTY @ Partial Buffer: Please Check";
												char* pArg2 = " ";
												message("%s%s", pArg1, pArg2);
											}
											{
												Print_VarArr2Mesg(am_model.am_Vld_SPO_Release$var, NULL, NULL, 0);
											}
											{
												char* pArg4 = " ";
												message("%s", pArg4);
											}
											{
												Print_VarArr2Mesg(am_model.am_Vld_SPO_Release$var, NULL, NULL, 0);
											}
											{
												char* pArg6 = rel_actorname(LdGetCurProc(this), am_model.$sys);
												char* pArg7 = " ";
												char* pArg8 = rel_simlocname(VehGetCurLoc(ValidPtr(LdGetVehicle(ValidPtr(am2_Vld_SPO_Release[ValidIndex("am_model.am_Vld_SPO_Release", CurProcIndex(), 4)], 32, load*)), 76, vehicle*)), am_model.$sys);
												message("%s%s%s", pArg6, pArg7, pArg8);
											}
										}
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 865);
										{
											int result = deccount(am2_C_Error, 1, this, P_SPO_Release_arriving, Step 5, am_localargs);
											if (result != Continue) return result;
Label5: ; /* Step 5 */
										}
									}
								}
							}
						}
					}
					ListRemoveAllAndFree(LoadList, am_localargs->ls303); /* End of for each */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 868);
					freersrc(am2_Vrsrc_ReleasePartial[ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Type, 30)], 1, this);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 871);
			am2_Vld_ReleaseCount[ValidIndex("am_model.am_Vld_ReleaseCount", CurProcIndex(), 4)] = this;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 872);
			while (this->attribute->am2_Ai_LotQty > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 875);
					return waitorder(&(am2_O_ST13_SPO_Seq[ValidIndex("am_model.am_O_ST13_SPO_Seq", CurProcIndex(), 4)]), this, P_SPO_Release_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 881);
					return waitorder(&(am2_O_BufferSpace[ValidIndex("am_model.am_O_BufferSpace", CurProcIndex(), 4)]), this, P_SPO_Release_arriving, Step 7, am_localargs);
Label7: ; /* Step 7 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 882);
					if (am2_Vi_SPO_QtyRelease[ValidIndex("am_model.am_Vi_SPO_QtyRelease", CurProcIndex(), 4)] == 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 883);
							return waitorder(&(am2_O_BufferSpace[ValidIndex("am_model.am_O_BufferSpace", CurProcIndex(), 4)]), this, P_SPO_Release_arriving, Step 8, am_localargs);
Label8: ; /* Step 8 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 885);
					if (am2_Vi_SPO_QtyRelease[ValidIndex("am_model.am_Vi_SPO_QtyRelease", CurProcIndex(), 4)] > this->attribute->am2_Ai_LotQty) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 885);
						am2_Vi_SPO_QtyRelease[ValidIndex("am_model.am_Vi_SPO_QtyRelease", CurProcIndex(), 4)] = this->attribute->am2_Ai_LotQty;
						EntityChanged(0x01000000);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 886);
					this->attribute->am2_Ai_Seq = am2_Vi_SPO_QtyRelease[ValidIndex("am_model.am_Vi_SPO_QtyRelease", CurProcIndex(), 4)];
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 887);
					if (this->attribute->am2_Ai_Seq == 0 && ASIclock > ToModelTime(86400, UNITSECONDS)) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 888);
							{
								char* pArg1 = "ERROR: SPO QTY to be released for the buffer is not specified";
								char* pArg2 = " ";
								int32 pArg3 = CurProcIndex();
								char* pArg4 = " ";
								int32 pArg5 = this->attribute->am2_Ai_Seq;

								updatelabel(&(am2_Lb_SPORelease[ValidIndex("am_model.am_Lb_SPORelease", CurProcIndex(), 4)]), "%s%s%d%s%d", pArg1, pArg2, pArg3, pArg4, pArg5);
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 889);
							{
								int result = deccount(am2_C_Error, 1, this, P_SPO_Release_arriving, Step 9, am_localargs);
								if (result != Continue) return result;
Label9: ; /* Step 9 */
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 891);
					{
						char* pArg1 = "Line=";
						char* pArg2 = " ";
						int32 pArg3 = this->attribute->am2_Ai_Line;
						char* pArg4 = " ";
						char* pArg5 = "-";
						char* pArg6 = " ";
						int32 pArg7 = CurProcIndex();
						char* pArg8 = " ";
						char* pArg9 = this->attribute->am2_As_Type;
						char* pArg10 = " ";
						char* pArg11 = "Lot =";
						char* pArg12 = " ";
						int32 pArg13 = this->attribute->am2_Ai_LotQty;
						char* pArg14 = " ";
						char* pArg15 = "RlsQty=";
						char* pArg16 = " ";
						int32 pArg17 = this->attribute->am2_Ai_Seq;

						message("%s%s%d%s%s%s%d%s%s%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13, pArg14, pArg15, pArg16, pArg17);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 893);
					if (CurProcIndex() < 3) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 893);
						if (getrsrc(&(am2_R_Line[1]), 1, this, P_SPO_Release_arriving, Step 10, am_localargs) == Delayed)
							return Delayed;  /* go wait for resource */
Label10: ; /* Step 10 */
					}
					else {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 894);
						if (getrsrc(&(am2_R_Line[2]), 1, this, P_SPO_Release_arriving, Step 11, am_localargs) == Delayed)
							return Delayed;  /* go wait for resource */
Label11: ; /* Step 11 */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 895);
					if (getrsrc(am2_R_SPO_Release, 1, this, P_SPO_Release_arriving, Step 12, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label12: ; /* Step 12 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 897);
					this->attribute->am2_Ai_CarQty = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 898);
					am2_Vi_CheckQty[ValidIndex("am_model.am_Vi_CheckQty", this->attribute->am2_Ai_Type, 30)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 899);
					am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", CurProcIndex(), 4)] = NULL;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 900);
					am2_Vi_PrevZone[ValidIndex("am_model.am_Vi_PrevZone", CurProcIndex(), 4)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 902);
					am2_Vi_Seq = this->attribute->am2_Ai_Seq;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 903);
					am2_Vld_SortMarker = NULL;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 905);
					if (CurProcIndex() < 3) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 905);
						this->attribute->am2_Ai_Line = 1;
						EntityChanged(0x00000040);
					}
					else {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 906);
						this->attribute->am2_Ai_Line = 2;
						EntityChanged(0x00000040);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 907);
					this->attribute->am2_Ai_Side = CurProcIndex();
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 909);
					if (am2_viStorageInit[2] == 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 910);
						return waitorder(am2_oStorageInit, this, P_SPO_Release_arriving, Step 13, am_localargs);
Label13: ; /* Step 13 */
						if (!this->inLeaveProc && this->nextproc) {
							retval = Continue;
							goto LabelRet;
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 912);
					am2_viCarrierReleaseCount[ValidIndex("am_model.am_viCarrierReleaseCount", CurProcIndex(), 4)][2] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 913);
					while (this->attribute->am2_Ai_Seq > 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 914);
							pushppa(this, P_SPO_Release_arriving, Step 14, am_localargs);
							pushppa(this, am2_Sr_SPOSeq, Step 1, NULL);
							return Continue;
Label14: ;
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 915);
							am2_viCarrierReleaseCount[ValidIndex("am_model.am_viCarrierReleaseCount", CurProcIndex(), 4)][2] += ListSize(LoadList, am2_Vldlst_Release[ValidIndex("am_model.am_Vldlst_Release", CurProcIndex(), 4)]);
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 916);
							ValidPtr(ListLastItem(LoadList, am2_Vldlst_Release[ValidIndex("am_model.am_Vldlst_Release", CurProcIndex(), 4)]), 32, load*)->attribute->am2_Ai_pi = 4444;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 917);
							am_localargs->ls304 = 0;
							ListCopy(LoadList, am_localargs->ls304, am2_Vldlst_Release[ValidIndex("am_model.am_Vldlst_Release", CurProcIndex(), 4)]);
							for (am_localargs->lv304 = (am_localargs->ls304) ? (am_localargs->ls304)->first : NULL; am_localargs->lv304; am_localargs->lv304 = am_localargs->lv304->next) {
								am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)] = am_localargs->lv304->item;
								{
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 918);
										if (ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status != 1) {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 919);
												if (am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", CurProcIndex(), 4)] != NULL) {
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 920);
														if (ValidPtr(am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone != this->attribute->am2_Ai_Zone) {
															{
																AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 921);
																if (getrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), 1, this, P_SPO_Release_arriving, Step 15, am_localargs) == Delayed)
																	return Delayed;  /* go wait for resource */
Label15: ; /* Step 15 */
															}
															{
																AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 922);
																freersrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), 1, this);
															}
															{
																AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 923);
																downrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]));
															}
															{
																AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 924);
																am2_Vld_ZoneTakeDown[ValidIndex("am_model.am_Vld_ZoneTakeDown", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)] = this;
																EntityChanged(0x01000000);
															}
															{
																AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 925);
																{
																	char* pArg1 = "D";
																	char* pArg2 = " ";
																	char* pArg3 = rel_actorname(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], am_model.$sys);

																	updatelabel(&(am2_Lb_ZoneExit[ValidIndex("am_model.am_Lb_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), "%s%s%s", pArg1, pArg2, pArg3);
																}
															}
														}
													}
												}
												else {
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 928);
													if (am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", CurProcIndex(), 4)] == NULL) {
														{
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 930);
															if (am2_Vi_PrevZone[ValidIndex("am_model.am_Vi_PrevZone", CurProcIndex(), 4)] > 0 && RscGetStatus(ValidPtr(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", am2_Vi_PrevZone[ValidIndex("am_model.am_Vi_PrevZone", CurProcIndex(), 4)], 3)]), 50, resource*)) == 0) {
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 931);
																	uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", am2_Vi_PrevZone[ValidIndex("am_model.am_Vi_PrevZone", CurProcIndex(), 4)], 3)]));
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 932);
																	if (getrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), 1, this, P_SPO_Release_arriving, Step 16, am_localargs) == Delayed)
																		return Delayed;  /* go wait for resource */
Label16: ; /* Step 16 */
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 933);
																	freersrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), 1, this);
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 934);
																	downrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]));
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 935);
																	am2_Vld_ZoneTakeDown[ValidIndex("am_model.am_Vld_ZoneTakeDown", this->attribute->am2_Ai_Zone, 3)] = this;
																	EntityChanged(0x01000000);
																}
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 936);
																	{
																		char* pArg1 = "D";
																		char* pArg2 = " ";
																		char* pArg3 = rel_actorname(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], am_model.$sys);

																		updatelabel(&(am2_Lb_ZoneExit[ValidIndex("am_model.am_Lb_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), "%s%s%s", pArg1, pArg2, pArg3);
																	}
																}
															}
														}
													}
												}
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 939);
												ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status = 1;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 940);
												ListAppendItem(LoadList, am2_Vldlst_ZoneExit[ValidIndex("am_model.am_Vldlst_ZoneExit", this->attribute->am2_Ai_Zone, 3)], am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)]);	/* append item to end of list */
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 941);
												if (CurProcIndex() < 3) {
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 941);
													ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Line = 1;
													EntityChanged(0x00000040);
												}
												else {
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 942);
													if (CurProcIndex() > 2) {
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 942);
														ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Line = 2;
														EntityChanged(0x00000040);
													}
												}
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 943);
												ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Side = CurProcIndex();
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 944);
												am2_Vi_CheckQty[ValidIndex("am_model.am_Vi_CheckQty", this->attribute->am2_Ai_Type, 30)] += ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 945);
												this->attribute->am2_Ai_Seq -= ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 946);
												this->attribute->am2_Ai_LotQty -= ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_CarQty;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 947);
												this->attribute->am2_Ai_CarQty += 1;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 948);
												if (this->attribute->am2_Ai_CarQty == 1) {
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 949);
														if (getrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), 1, this, P_SPO_Release_arriving, Step 17, am_localargs) == Delayed)
															return Delayed;  /* go wait for resource */
Label17: ; /* Step 17 */
													}
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 950);
														freersrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), 1, this);
													}
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 951);
														downrsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]));
													}
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 952);
														am2_Vld_ZoneTakeDown[ValidIndex("am_model.am_Vld_ZoneTakeDown", this->attribute->am2_Ai_Zone, 3)] = this;
														EntityChanged(0x01000000);
													}
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 953);
														{
															char* pArg1 = "D";
															char* pArg2 = " ";
															char* pArg3 = rel_actorname(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], am_model.$sys);

															updatelabel(&(am2_Lb_ZoneExit[ValidIndex("am_model.am_Lb_ZoneExit", ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone, 3)]), "%s%s%s", pArg1, pArg2, pArg3);
														}
													}
												}
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 955);
												{
													char* pArg1 = "Line=";
													char* pArg2 = " ";
													int32 pArg3 = this->attribute->am2_Ai_Line;
													char* pArg4 = " ";
													char* pArg5 = "-";
													char* pArg6 = " ";
													int32 pArg7 = CurProcIndex();
													char* pArg8 = " ";
													char* pArg9 = this->attribute->am2_As_Type;
													char* pArg10 = " ";
													char* pArg11 = "Lot =";
													char* pArg12 = " ";
													int32 pArg13 = this->attribute->am2_Ai_LotQty;
													char* pArg14 = " ";
													char* pArg15 = "RlsQty=";
													char* pArg16 = " ";
													int32 pArg17 = this->attribute->am2_Ai_Seq;

													updatelabel(&(am2_Lb_SPORelease[ValidIndex("am_model.am_Lb_SPORelease", CurProcIndex(), 4)]), "%s%s%d%s%s%s%d%s%s%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13, pArg14, pArg15, pArg16, pArg17);
												}
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 959);
												if (OrdGetCurConts(ValidPtr(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), 40, ordlist*)) > 0) {
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 960);
														am_localargs->ls305 = 0;
														ListCopy(LoadList, am_localargs->ls305, OrdGetLoadList(ValidPtr(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), 40, ordlist*)));
														for (am_localargs->lv305 = (am_localargs->ls305) ? (am_localargs->ls305)->first : NULL; am_localargs->lv305; am_localargs->lv305 = am_localargs->lv305->next) {
															am2_Vld_OL[ValidIndex("am_model.am_Vld_OL", CurProcIndex(), 4)] = am_localargs->lv305->item;
															{
																{
																	AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 961);
																	if (am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)] == am2_Vld_OL[ValidIndex("am_model.am_Vld_OL", CurProcIndex(), 4)]) {
																		{
																			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 962);
																			{
																				int32 tempint = orderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), NULL, am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)]); /* Place an order */
																				if (tempint > 0) backorderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), NULL, am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)]);	/* Place a backorder */
																			}
																		}
																	}
																}
															}
														}
														ListRemoveAllAndFree(LoadList, am_localargs->ls305); /* End of for each */
													}
												}
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 966);
												ListRemoveFirstMatch(LoadList, am2_Vldlst_Release[ValidIndex("am_model.am_Vldlst_Release", this->attribute->am2_Ai_pi, 4)], am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)]);	/* remove first match from list */
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 968);
												am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", CurProcIndex(), 4)] = am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)];
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 969);
												am2_Vi_PrevZone[ValidIndex("am_model.am_Vi_PrevZone", CurProcIndex(), 4)] = ValidPtr(am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 970);
												if (this->attribute->am2_Ai_Seq <= 0) {
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 971);
														ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Seq = 9999;
														EntityChanged(0x00000040);
													}
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 972);
														this->attribute->am2_Ai_Zone = ValidPtr(am2_Vld_Release[ValidIndex("am_model.am_Vld_Release", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Zone;
														EntityChanged(0x00000040);
													}
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 973);
														break;
													}
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls304); /* End of for each */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 978);
							return waitorder(&(am2_O_ReleaseSeq[ValidIndex("am_model.am_O_ReleaseSeq", CurProcIndex(), 4)]), this, P_SPO_Release_arriving, Step 18, am_localargs);
Label18: ; /* Step 18 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 979);
							order(1, am2_oZoneMixCheck, NULL, NULL); /* Place an order */
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 982);
					if (OrdGetCurConts(am2_O_Starved) > 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 982);
						order(1, am2_O_Starved, NULL, NULL); /* Place an order */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 985);
					return waitorder(&(am2_O_Release[ValidIndex("am_model.am_O_Release", CurProcIndex(), 4)]), this, P_SPO_Release_arriving, Step 19, am_localargs);
Label19: ; /* Step 19 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 986);
					if (CurProcIndex() < 3) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 986);
						freersrc(&(am2_R_Line[1]), 1, this);
					}
					else {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 987);
						freersrc(&(am2_R_Line[2]), 1, this);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 988);
					freersrc(am2_R_SPO_Release, 1, this);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 989);
					return waitorder(&(am2_oST13Inspect[ValidIndex("am_model.am_oST13Inspect", CurProcIndex(), 4)]), this, P_SPO_Release_arriving, Step 20, am_localargs);
Label20: ; /* Step 20 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 990);
					if (waitfor(ToModelTime(0, UNITSECONDS), this, P_SPO_Release_arriving, Step 21, am_localargs) == Delayed)
						return Delayed;
Label21: ; /* Step 21 */
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 993);
			if (this->attribute->am2_Ai_LotQty < 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 994);
					am2_Vi_ReleasePartial[ValidIndex("am_model.am_Vi_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vi_ReleasePartial", this->attribute->am2_Ai_Type, 30)] =  -1 * this->attribute->am2_Ai_LotQty;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 995);
					clone(this, 1, am2_Vproc_ReleasePartial[ValidIndex("am_model.am_Vproc_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vproc_ReleasePartial", this->attribute->am2_Ai_Type, 30)], NULL);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 998);
			am2_Vld_ReleaseCount[ValidIndex("am_model.am_Vld_ReleaseCount", CurProcIndex(), 4)] = NULL;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor, 1001);
			{
				int32 tempint = order(1, &(am2_O_SPO_ReleaseComplete[ValidIndex("am_model.am_O_SPO_ReleaseComplete", CurProcIndex(), 4)]), NULL, NULL); /* Place an order */
				if (tempint > 0) backorder(tempint, &(am2_O_SPO_ReleaseComplete[ValidIndex("am_model.am_O_SPO_ReleaseComplete", CurProcIndex(), 4)]), NULL, NULL);	/* Place a backorder */
			}
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls303);
	ListRemoveAllAndFree(LoadList, am_localargs->ls304);
	ListRemoveAllAndFree(LoadList, am_localargs->ls305);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_SPO_Release", P_SPO_Release_arriving, localactor);
	return retval;
} /* end of P_SPO_Release_arriving */

static int32
am_Sr_SPOSeq(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv306; /* 'for each' loop variable */
		AMLoadList* ls306; /* 'for each' list */
		AMLoadListItem* lv307; /* 'for each' loop variable */
		AMLoadList* ls307; /* 'for each' list */
		AMLoadListItem* lv308; /* 'for each' loop variable */
		AMLoadList* ls308; /* 'for each' list */
		AMLoadListItem* lv309; /* 'for each' loop variable */
		AMLoadList* ls309; /* 'for each' list */
		AMLoadListItem* lv310; /* 'for each' loop variable */
		AMLoadList* ls310; /* 'for each' list */
		AMLoadListItem* lv311; /* 'for each' loop variable */
		AMLoadList* ls311; /* 'for each' list */
		AMLoadListItem* lv312; /* 'for each' loop variable */
		AMLoadList* ls312; /* 'for each' list */
		AMLoadListItem* lv313; /* 'for each' loop variable */
		AMLoadList* ls313; /* 'for each' list */
		AMLoadListItem* lv314; /* 'for each' loop variable */
		AMLoadList* ls314; /* 'for each' list */
		AMLoadListItem* lv315; /* 'for each' loop variable */
		AMLoadList* ls315; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", localactor);
	AMDebuggerParams("base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 0, NULL, NULL, NULL);
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
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1006);
			this->attribute->am2_Ai_Status = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1007);
			am2_vix = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1008);
			am2_viPrevLotNo = 0;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1009);
			am2_viClearABayFlag[ValidIndex("am_model.am_viClearABayFlag", this->attribute->am2_Ai_Side, 4)] = 0;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1011);
			if (ListSize(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]) == 0) {
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1012);
					freersrc(am2_R_SPO_Release, 1, this);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1013);
					am2_Vr_WeldStarveTime[ValidIndex("am_model.am_Vr_WeldStarveTime", this->attribute->am2_Ai_Line, 2)] = FromModelTime(ASIclock, UNITSECONDS);
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1014);
					return waitorder(&(am2_O_Starve[ValidIndex("am_model.am_O_Starve", this->attribute->am2_Ai_Type, 30)]), this, am_Sr_SPOSeq, Step 2, am_localargs);
Label2: ; /* Step 2 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1015);
					tabulate(&(am2_T_WeldStarve[ValidIndex("am_model.am_T_WeldStarve", this->attribute->am2_Ai_Line, 2)]), (FromModelTime(ASIclock, UNITSECONDS) - am2_Vr_WeldStarveTime[ValidIndex("am_model.am_Vr_WeldStarveTime", this->attribute->am2_Ai_Line, 2)]) / 60);	/* Tabulate the value */
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1016);
					if (waitfor(ToModelTime(5, UNITMINUTES), this, am_Sr_SPOSeq, Step 3, am_localargs) == Delayed)
						return Delayed;
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1017);
					if (getrsrc(am2_R_SPO_Release, 1, this, am_Sr_SPOSeq, Step 4, am_localargs) == Delayed)
						return Delayed;  /* go wait for resource */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1018);
					am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", this->attribute->am2_Ai_Side, 4)] = NULL;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1019);
					am2_Vld_SortMarker = NULL;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1020);
					am2_vix = 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1021);
					am2_Vi_Seq = this->attribute->am2_Ai_Seq;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1024);
			am2_viABayCount = 0;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1025);
			while (am2_vix <= 9) {
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1026);
					am_localargs->ls306 = 0;
					ListCopy(LoadList, am_localargs->ls306, am2_vldlstLanes[3][ValidIndex("am_model.am_vldlstLanes", am2_vix, 20)]);
					for (am_localargs->lv306 = (am_localargs->ls306) ? (am_localargs->ls306)->first : NULL; am_localargs->lv306; am_localargs->lv306 = am_localargs->lv306->next) {
						am2_Vld_Sort = am_localargs->lv306->item;
						{
							{
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1027);
								if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type != 99) {
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1028);
										am2_viABayCount += 1;
										EntityChanged(0x01000000);
									}
								}
							}
						}
					}
					ListRemoveAllAndFree(LoadList, am_localargs->ls306); /* End of for each */
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1031);
					am2_vix += 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1033);
			if (am2_viABayCount >= am2_viABayTrigger) {
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1034);
					am_localargs->ls307 = 0;
					ListCopy(LoadList, am_localargs->ls307, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]);
					for (am_localargs->lv307 = (am_localargs->ls307) ? (am_localargs->ls307)->first : NULL; am_localargs->lv307; am_localargs->lv307 = am_localargs->lv307->next) {
						am2_Vld_Sort = am_localargs->lv307->item;
						{
							{
								AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1035);
								if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Zone == 3) {
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1036);
										this->attribute->am2_Ai_LotNo = ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1037);
										am2_viClearABayFlag[ValidIndex("am_model.am_viClearABayFlag", this->attribute->am2_Ai_Side, 4)] = 1;
										EntityChanged(0x01000000);
									}
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1038);
										if (this->attribute->am2_Ai_LotNo > ValidPtr(ListFirstItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_LotNo) {
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1039);
											am2_viABayCleanUp += 1;
											EntityChanged(0x01000000);
										}
									}
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1040);
										break;
									}
								}
							}
						}
					}
					ListRemoveAllAndFree(LoadList, am_localargs->ls307); /* End of for each */
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1045);
			am2_vix = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1046);
			while (am2_vix <= 12) {
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1047);
					if (am2_viClearABayFlag[ValidIndex("am_model.am_viClearABayFlag", this->attribute->am2_Ai_Side, 4)] == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1048);
							this->attribute->am2_Ai_Zone = am2_viZonePref[ValidIndex("am_model.am_viZonePref", am2_viZonePullPriority[ValidIndex("am_model.am_viZonePullPriority", am2_vix, 12)], 3)][ValidIndex("am_model.am_viZonePref", this->attribute->am2_Ai_Type, 30)];
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1049);
							this->attribute->am2_Ai_LotNo = ValidPtr(ListFirstItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_LotNo;
							EntityChanged(0x00000040);
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1052);
							this->attribute->am2_Ai_Zone = 3;
							EntityChanged(0x00000040);
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1054);
					this->attribute->am2_Ai_Row = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1056);
					if (StringCompare(am2_vsPullingRule[ValidIndex("am_model.am_vsPullingRule", am2_vix, 12)], "Partial") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1057);
							am2_Vld_SortMarker = NULL;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1058);
							am2_Vi_LineCount = 99;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1059);
							am_localargs->ls308 = 0;
							ListCopy(LoadList, am_localargs->ls308, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]);
							for (am_localargs->lv308 = (am_localargs->ls308) ? (am_localargs->ls308)->first : NULL; am_localargs->lv308; am_localargs->lv308 = am_localargs->lv308->next) {
								am2_Vld_Sort = am_localargs->lv308->item;
								{
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1060);
										if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo == this->attribute->am2_Ai_LotNo && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Zone == this->attribute->am2_Ai_Zone && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)] == 0 && ListSize(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]) > 0) {
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1061);
												if (ListFirstItem(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]) == am2_Vld_Sort && ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]) - am2_viEmptyCarPullQty[ValidIndex("am_model.am_viEmptyCarPullQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarPullQty", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)] < LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)], 38, simloc*)) && ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]) < am2_Vi_LineCount) {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1063);
														am2_Vld_SortMarker = am2_Vld_Sort;
														EntityChanged(0x01000000);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1064);
														am2_Vi_LineCount = ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]);
														EntityChanged(0x01000000);
													}
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls308); /* End of for each */
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1068);
							if (am2_Vld_SortMarker != NULL) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1069);
									ListRemoveFirstMatch(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_SortMarker);	/* remove first match from list */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1070);
									ListPrependItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_SortMarker);	/* prepend item to beginning of list */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1071);
									this->attribute->am2_Ai_Status = 1;
									EntityChanged(0x00000040);
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1075);
					if (StringCompare(am2_vsPullingRule[ValidIndex("am_model.am_vsPullingRule", am2_vix, 12)], "Full") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1076);
							am_localargs->ls309 = 0;
							ListCopy(LoadList, am_localargs->ls309, OrdGetLoadList(ValidPtr(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), 40, ordlist*)));
							for (am_localargs->lv309 = (am_localargs->ls309) ? (am_localargs->ls309)->first : NULL; am_localargs->lv309; am_localargs->lv309 = am_localargs->lv309->next) {
								am2_Vld_Sort = am_localargs->lv309->item;
								{
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1077);
										if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo == this->attribute->am2_Ai_LotNo && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Zone == this->attribute->am2_Ai_Zone && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)] == 0) {
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1078);
												if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]) - am2_viEmptyCarPullQty[ValidIndex("am_model.am_viEmptyCarPullQty", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_viEmptyCarPullQty", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)] == LocGetCapacity(ValidPtr(am2_vlocptrLane[ValidIndex("am_model.am_vlocptrLane", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vlocptrLane", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)], 38, simloc*))) {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1079);
														ListRemoveFirstMatch(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_Sort);	/* remove first match from list */
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1080);
														ListPrependItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_Sort);	/* prepend item to beginning of list */
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1081);
														this->attribute->am2_Ai_Status = 1;
														EntityChanged(0x00000040);
													}
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls309); /* End of for each */
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1087);
					if (StringCompare(am2_vsPullingRule[ValidIndex("am_model.am_vsPullingRule", am2_vix, 12)], "Mixed-Front") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1088);
							am2_Vld_SortMarker = NULL;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1089);
							am2_Vi_LineCount = 99;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1090);
							am_localargs->ls310 = 0;
							ListCopy(LoadList, am_localargs->ls310, OrdGetLoadList(ValidPtr(&(am2_O_Type[ValidIndex("am_model.am_O_Type", this->attribute->am2_Ai_Type, 30)]), 40, ordlist*)));
							for (am_localargs->lv310 = (am_localargs->ls310) ? (am_localargs->ls310)->first : NULL; am_localargs->lv310; am_localargs->lv310 = am_localargs->lv310->next) {
								am2_Vld_Sort = am_localargs->lv310->item;
								{
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1091);
										if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo == this->attribute->am2_Ai_LotNo && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Zone == this->attribute->am2_Ai_Zone && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)] == 1) {
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1092);
												am2_viMixCount = 0;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1093);
												am_localargs->ls311 = 0;
												ListCopy(LoadList, am_localargs->ls311, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]);
												for (am_localargs->lv311 = (am_localargs->ls311) ? (am_localargs->ls311)->first : NULL; am_localargs->lv311; am_localargs->lv311 = am_localargs->lv311->next) {
													am2_Vld_Sort2 = am_localargs->lv311->item;
													{
														{
															AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1094);
															if (ValidPtr(am2_Vld_Sort2, 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type) {
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1094);
																am2_viMixCount += 1;
																EntityChanged(0x01000000);
															}
															else {
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1095);
																break;
															}
														}
													}
												}
												ListRemoveAllAndFree(LoadList, am_localargs->ls311); /* End of for each */
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1097);
												if (am2_viMixCount < am2_Vi_LineCount) {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1098);
														am2_Vi_LineCount = am2_viMixCount;
														EntityChanged(0x01000000);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1099);
														am2_Vld_SortMarker = am2_Vld_Sort;
														EntityChanged(0x01000000);
													}
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls310); /* End of for each */
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1103);
							if (am2_Vld_SortMarker != NULL) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1104);
									ListRemoveFirstMatch(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_SortMarker);	/* remove first match from list */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1105);
									ListPrependItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_SortMarker);	/* prepend item to beginning of list */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1106);
									this->attribute->am2_Ai_Status = 1;
									EntityChanged(0x00000040);
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1110);
					if (StringCompare(am2_vsPullingRule[ValidIndex("am_model.am_vsPullingRule", am2_vix, 12)], "Mixed-Blocked") == 0) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1111);
							am2_Vld_SortMarker = NULL;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1112);
							am2_Vi_LineCount = 99;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1114);
							am_localargs->ls312 = 0;
							ListCopy(LoadList, am_localargs->ls312, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]);
							for (am_localargs->lv312 = (am_localargs->ls312) ? (am_localargs->ls312)->first : NULL; am_localargs->lv312; am_localargs->lv312 = am_localargs->lv312->next) {
								am2_Vld_Sort = am_localargs->lv312->item;
								{
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1115);
										if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo == this->attribute->am2_Ai_LotNo && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Zone == this->attribute->am2_Ai_Zone && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)] == 1) {
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1116);
												am2_viMixCount = 0;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1117);
												am_localargs->ls313 = 0;
												ListCopy(LoadList, am_localargs->ls313, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)]);
												for (am_localargs->lv313 = (am_localargs->ls313) ? (am_localargs->ls313)->first : NULL; am_localargs->lv313; am_localargs->lv313 = am_localargs->lv313->next) {
													am2_Vld_Sort2 = am_localargs->lv313->item;
													{
														{
															AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1119);
															if (am2_Vld_Sort2 != am2_Vld_Sort) {
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1119);
																am2_viMixCount += 1;
																EntityChanged(0x01000000);
															}
															else {
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1120);
																break;
															}
														}
													}
												}
												ListRemoveAllAndFree(LoadList, am_localargs->ls313); /* End of for each */
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1122);
												if (am2_viMixCount < am2_Vi_LineCount) {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1123);
														am2_Vi_LineCount = am2_viMixCount;
														EntityChanged(0x01000000);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1124);
														am2_Vld_SortMarker = am2_Vld_Sort;
														EntityChanged(0x01000000);
													}
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls312); /* End of for each */
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1130);
							if (am2_Vld_SortMarker == NULL && am2_vix == 12 && ListSize(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]) > 0) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1131);
									if (am2_viClearABayFlag[ValidIndex("am_model.am_viClearABayFlag", this->attribute->am2_Ai_Side, 4)] == 1) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1132);
											am2_viClearABayFlag[ValidIndex("am_model.am_viClearABayFlag", this->attribute->am2_Ai_Side, 4)] = 0;
											EntityChanged(0x01000000);
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1133);
											am2_vix = 0;
											EntityChanged(0x01000000);
										}
									}
									else {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1136);
											this->attribute->am2_Ai_Zone = ValidPtr(ListFirstItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Zone;
											EntityChanged(0x00000040);
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1137);
											this->attribute->am2_Ai_Row = ValidPtr(ListFirstItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row;
											EntityChanged(0x00000040);
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1138);
											if (ListSize(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]) > 0) {
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1139);
												am2_Vld_SortMarker = ListFirstItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]);
												EntityChanged(0x01000000);
											}
										}
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1143);
							if (am2_Vld_SortMarker != NULL) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1144);
									ListRemoveFirstMatch(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_SortMarker);	/* remove first match from list */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1145);
									ListPrependItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_SortMarker);	/* prepend item to beginning of list */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1146);
									am2_viRecircCount += 1;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1147);
									this->attribute->am2_Ai_Status = 1;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1149);
									am_localargs->ls314 = 0;
									ListCopy(LoadList, am_localargs->ls314, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", ValidPtr(am2_Vld_SortMarker, 32, load*)->attribute->am2_Ai_Row, 20)]);
									for (am_localargs->lv314 = (am_localargs->ls314) ? (am_localargs->ls314)->first : NULL; am_localargs->lv314; am_localargs->lv314 = am_localargs->lv314->next) {
										am2_Vld_Sort = am_localargs->lv314->item;
										{
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1150);
												if ((ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type != this->attribute->am2_Ai_Type && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type != 99) || (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo != this->attribute->am2_Ai_LotNo && ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type != 99)) {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1151);
														ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_pi = 1234;
														EntityChanged(0x00000040);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1152);
														if (am2_Vld_Recirc[1] == NULL) {
															{
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1153);
																ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_x = 1234;
																EntityChanged(0x00000040);
															}
															{
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1154);
																am2_Vld_Recirc[1] = am2_Vld_Sort;
																EntityChanged(0x01000000);
															}
														}
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1156);
														{
															int32 tempint = orderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, am2_Vld_Sort); /* Place an order */
															if (tempint > 0) backorderload(&(am2_O_Type[ValidIndex("am_model.am_O_Type", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type, 30)]), NULL, am2_Vld_Sort);	/* Place a backorder */
														}
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1157);
														ListRemoveFirstMatch(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Row, 20)], am2_Vld_Sort);	/* remove first match from list */
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1158);
														am2_Vld_Recirc[2] = am2_Vld_Sort;
														EntityChanged(0x01000000);
													}
												}
												else {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1161);
														if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_x == 10) {
															{
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1162);
																this->attribute->am2_Ai_Status = 0;
																EntityChanged(0x00000040);
															}
															{
																AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1163);
																am2_vix = 13;
																EntityChanged(0x01000000);
															}
														}
														else {
															AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1165);
															break;
														}
													}
												}
											}
										}
									}
									ListRemoveAllAndFree(LoadList, am_localargs->ls314); /* End of for each */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1168);
									if (this->attribute->am2_Ai_Status == 1) {
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1169);
											return waitorder(am2_oZoneMixRecircComplete, this, am_Sr_SPOSeq, Step 5, am_localargs);
Label5: ; /* Step 5 */
											if (!this->inLeaveProc && this->nextproc) {
												retval = Continue;
												goto LabelRet;
											}
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1170);
											order(1, am2_oZoneMixCheck, NULL, NULL); /* Place an order */
										}
										{
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1171);
											if (waitfor(ToModelTime(0, UNITSECONDS), this, am_Sr_SPOSeq, Step 6, am_localargs) == Delayed)
												return Delayed;
Label6: ; /* Step 6 */
										}
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1176);
					if (this->attribute->am2_Ai_Status == 1) {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1177);
							this->attribute->am2_Ai_Status = 0;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1178);
							this->attribute->am2_Ai_Row = ValidPtr(ListFirstItem(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)]), 32, load*)->attribute->am2_Ai_Row;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1180);
							while (ListSize(LoadList, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]) < ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) / 2) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1181);
									if (waitfor(ToModelTime(10, UNITSECONDS), this, am_Sr_SPOSeq, Step 7, am_localargs) == Delayed)
										return Delayed;
Label7: ; /* Step 7 */
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1184);
							am_localargs->ls315 = 0;
							ListCopy(LoadList, am_localargs->ls315, am2_vldlstLanePull[ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanePull", this->attribute->am2_Ai_Row, 20)]);
							for (am_localargs->lv315 = (am_localargs->ls315) ? (am_localargs->ls315)->first : NULL; am_localargs->lv315; am_localargs->lv315 = am_localargs->lv315->next) {
								am2_Vld_Sort = am_localargs->lv315->item;
								{
									{
										AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1185);
										if (ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type != this->attribute->am2_Ai_Type || ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_LotNo != this->attribute->am2_Ai_LotNo) {
											AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1185);
											break;
										}
										else {
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1187);
												am2_Vi_Seq -= ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_CarQty;
												EntityChanged(0x01000000);
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1188);
												ListRemoveFirstMatch(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)], am2_Vld_Sort);	/* remove first match from list */
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1189);
												ListRemoveFirstMatch(LoadList, am2_Vldlst_Type[ValidIndex("am_model.am_Vldlst_Type", this->attribute->am2_Ai_Type, 30)], am2_Vld_Sort);	/* remove first match from list */
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1190);
												ListAppendItem(LoadList, am2_Vldlst_Release[ValidIndex("am_model.am_Vldlst_Release", this->attribute->am2_Ai_Side, 4)], am2_Vld_Sort);	/* append item to end of list */
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1192);
												if (am2_viTrackStoreProcess == 1) {
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1200);
														SetString(&ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_As_Message, "STORE OUT REQUEST");
														EntityChanged(0x00000040);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1201);
														SetString(&ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_As_CurrentLocation, am2_vsStation[ValidIndex("am_model.am_vsStation", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vsStation", this->attribute->am2_Ai_Row, 20)]);
														EntityChanged(0x00000040);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1202);
														SetString(&ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_As_NextLocation, "13");
														EntityChanged(0x00000040);
													}
													{
														AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1203);
														F_RequestMove(am2_Vld_Sort);
													}
												}
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1206);
												ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_x = 10;
												EntityChanged(0x00000040);
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1208);
												{
													char* pArg1 = "Moved";
													char* pArg2 = " ";
													char* pArg3 = rel_actorname(am2_Vld_Sort, am_model.$sys);
													char* pArg4 = " ";
													char* pArg5 = "to Release";

													message("%s%s%s%s%s", pArg1, pArg2, pArg3, pArg4, pArg5);
												}
											}
											{
												AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1209);
												if (am2_Vi_Seq <= 0) {
													AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1209);
													break;
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls315); /* End of for each */
						}
					}
					else {
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1214);
							am2_vix += 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1215);
							if (am2_vix == 13) {
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1216);
									freersrc(am2_R_SPO_Release, 1, this);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1217);
									am2_Vr_WeldStarveTime[ValidIndex("am_model.am_Vr_WeldStarveTime", this->attribute->am2_Ai_Line, 2)] = FromModelTime(ASIclock, UNITSECONDS);
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1218);
									return waitorder(&(am2_O_Starve[ValidIndex("am_model.am_O_Starve", this->attribute->am2_Ai_Type, 30)]), this, am_Sr_SPOSeq, Step 8, am_localargs);
Label8: ; /* Step 8 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1219);
									tabulate(&(am2_T_WeldStarve[ValidIndex("am_model.am_T_WeldStarve", this->attribute->am2_Ai_Line, 2)]), (FromModelTime(ASIclock, UNITSECONDS) - am2_Vr_WeldStarveTime[ValidIndex("am_model.am_Vr_WeldStarveTime", this->attribute->am2_Ai_Line, 2)]) / 60);	/* Tabulate the value */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1220);
									if (waitfor(ToModelTime(5, UNITMINUTES), this, am_Sr_SPOSeq, Step 9, am_localargs) == Delayed)
										return Delayed;
Label9: ; /* Step 9 */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1221);
									if (getrsrc(am2_R_SPO_Release, 1, this, am_Sr_SPOSeq, Step 10, am_localargs) == Delayed)
										return Delayed;  /* go wait for resource */
Label10: ; /* Step 10 */
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1222);
									am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", this->attribute->am2_Ai_Side, 4)] = NULL;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1223);
									am2_Vld_SortMarker = NULL;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1224);
									am2_vix = 1;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1225);
									am2_Vi_Seq = this->attribute->am2_Ai_Seq;
									EntityChanged(0x01000000);
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1228);
					if (ListSize(LoadList, am2_Vldlst_Release[ValidIndex("am_model.am_Vldlst_Release", this->attribute->am2_Ai_Side, 4)]) > 0) {
						AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor, 1228);
						break;
					}
				}
			}
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls306);
	ListRemoveAllAndFree(LoadList, am_localargs->ls307);
	ListRemoveAllAndFree(LoadList, am_localargs->ls308);
	ListRemoveAllAndFree(LoadList, am_localargs->ls309);
	ListRemoveAllAndFree(LoadList, am_localargs->ls310);
	ListRemoveAllAndFree(LoadList, am_localargs->ls311);
	ListRemoveAllAndFree(LoadList, am_localargs->ls312);
	ListRemoveAllAndFree(LoadList, am_localargs->ls313);
	ListRemoveAllAndFree(LoadList, am_localargs->ls314);
	ListRemoveAllAndFree(LoadList, am_localargs->ls315);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Subroutine", "base.Sr_SPOSeq", am_Sr_SPOSeq, localactor);
	return retval;
} /* end of am_Sr_SPOSeq */

static int32
pZoneMixCheck_arriving(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv316; /* 'for each' loop variable */
		AMLoadList* ls316; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", localactor);
	AMDebuggerParams("base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 0, NULL, NULL, NULL);
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
	am_localargs = (struct _localargs*)xcalloc(1, sizeof(struct _localargs));
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1233);
			return waitorder(am2_oZoneMixCheck, this, pZoneMixCheck_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1234);
			this->attribute->am2_Ai_Zone = 1;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1235);
			while (this->attribute->am2_Ai_Zone <= am2_viNumZones) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1236);
					this->attribute->am2_Ai_Row = 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1237);
					while (this->attribute->am2_Ai_Row <= am2_viZoneLaneMax[ValidIndex("am_model.am_viZoneLaneMax", this->attribute->am2_Ai_Zone, 3)]) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1238);
							this->attribute->am2_Ai_x = 0;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1239);
							if (ListSize(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]) > 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1240);
									this->attribute->am2_Ai_Type = ValidPtr(ListFirstItem(LoadList, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]), 32, load*)->attribute->am2_Ai_Type;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1241);
									if (this->attribute->am2_Ai_Type != 99) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1242);
											am_localargs->ls316 = 0;
											ListCopy(LoadList, am_localargs->ls316, am2_vldlstLanes[ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_vldlstLanes", this->attribute->am2_Ai_Row, 20)]);
											for (am_localargs->lv316 = (am_localargs->ls316) ? (am_localargs->ls316)->first : NULL; am_localargs->lv316; am_localargs->lv316 = am_localargs->lv316->next) {
												am2_Vld_Sort = am_localargs->lv316->item;
												{
													{
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1243);
														if (this->attribute->am2_Ai_Type != ValidPtr(am2_Vld_Sort, 32, load*)->attribute->am2_Ai_Type) {
															AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1243);
															this->attribute->am2_Ai_x += 1;
															EntityChanged(0x00000040);
														}
													}
												}
											}
											ListRemoveAllAndFree(LoadList, am_localargs->ls316); /* End of for each */
										}
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1247);
							if (this->attribute->am2_Ai_x == 0 && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 1) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1248);
									am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] = 0;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1249);
									{
										int result = deccount(&(am2_C_ZoneMix[1]), 1, this, pZoneMixCheck_arriving, Step 3, am_localargs);
										if (result != Continue) return result;
Label3: ; /* Step 3 */
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1250);
									{
										int result = inccount(&(am2_C_ZoneMix[2]), 1, this, pZoneMixCheck_arriving, Step 4, am_localargs);
										if (result != Continue) return result;
Label4: ; /* Step 4 */
									}
								}
							}
							else {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1252);
								if (this->attribute->am2_Ai_x > 0 && am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] == 0) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1253);
										am2_Vi_ZoneMix[ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Zone, 3)][ValidIndex("am_model.am_Vi_ZoneMix", this->attribute->am2_Ai_Row, 20)] = 1;
										EntityChanged(0x01000000);
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1254);
										{
											int result = inccount(&(am2_C_ZoneMix[1]), 1, this, pZoneMixCheck_arriving, Step 5, am_localargs);
											if (result != Continue) return result;
Label5: ; /* Step 5 */
										}
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1255);
										{
											int result = deccount(&(am2_C_ZoneMix[2]), 1, this, pZoneMixCheck_arriving, Step 6, am_localargs);
											if (result != Continue) return result;
Label6: ; /* Step 6 */
										}
									}
								}
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1257);
							this->attribute->am2_Ai_Row += 1;
							EntityChanged(0x00000040);
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1259);
					this->attribute->am2_Ai_Zone += 1;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor, 1261);
			this->nextproc = am2_pZoneMixCheck; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls316);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.pZoneMixCheck", pZoneMixCheck_arriving, localactor);
	return retval;
} /* end of pZoneMixCheck_arriving */

static int32
P_ReleasePartial_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", localactor);
	AMDebuggerParams("base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 0, NULL, NULL, NULL);
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
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1271);
			am2_Vproc_ReleasePartial[1][1] = &(am2_P_ReleasePartial[1]);
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1272);
			if (am2_Vi_ReleasePartial[ValidIndex("am_model.am_Vi_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vi_ReleasePartial", this->attribute->am2_Ai_Type, 30)] > 0) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1272);
				this->attribute->am2_Ai_CarQty = am2_Vi_ReleasePartial[ValidIndex("am_model.am_Vi_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vi_ReleasePartial", this->attribute->am2_Ai_Type, 30)];
				EntityChanged(0x00000040);
			}
			else {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1274);
					{
						char* pArg1 = "QTY @ Release Partial is not set right";
						char* pArg2 = " ";
						char* pArg3 = rel_actorname(this, am_model.$sys);
						char* pArg4 = " ";
						char* pArg5 = rel_actorname(LdGetCurProc(this), am_model.$sys);
						char* pArg6 = " ";
						int32 pArg7 = this->attribute->am2_Ai_CarQty;

						message("%s%s%s%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1275);
					{
						int result = deccount(am2_C_Error, 1, this, P_ReleasePartial_arriving, Step 2, am_localargs);
						if (result != Continue) return result;
Label2: ; /* Step 2 */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1277);
			ListAppendItem(LoadList, am2_Vldlst_ReleasePartial[ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Type, 30)], this);	/* append item to end of list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1278);
			{
				char* pArg1 = "Release partial";
				char* pArg2 = " ";
				int32 pArg3 = this->attribute->am2_Ai_Type;
				char* pArg4 = " ";
				char* pArg5 = this->attribute->am2_As_Type;
				char* pArg6 = " ";
				int32 pArg7 = this->attribute->am2_Ai_CarQty;

				message("%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1279);
			return waitorder(am2_Vo_ReleasePartial[ValidIndex("am_model.am_Vo_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vo_ReleasePartial", this->attribute->am2_Ai_Type, 30)], this, P_ReleasePartial_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
			if (!this->inLeaveProc && this->nextproc) {
				retval = Continue;
				goto LabelRet;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1280);
			if (getrsrc(am2_Vrsrc_ReleasePartial[ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Type, 30)], 1, this, P_ReleasePartial_arriving, Step 4, am_localargs) == Delayed)
				return Delayed;  /* go wait for resource */
Label4: ; /* Step 4 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1281);
			ListRemoveFirstMatch(LoadList, am2_Vldlst_ReleasePartial[ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vldlst_ReleasePartial", this->attribute->am2_Ai_Type, 30)], this);	/* remove first match from list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor, 1282);
			freersrc(am2_Vrsrc_ReleasePartial[ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Line, 2)][ValidIndex("am_model.am_Vrsrc_ReleasePartial", this->attribute->am2_Ai_Type, 30)], 1, this);
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_ReleasePartial", P_ReleasePartial_arriving, localactor);
	return retval;
} /* end of P_ReleasePartial_arriving */


static double
Funcl0(load* this)
{
	return am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 2;
}


static double
Funcl1(load* this)
{
	return 1 - am2_Vr_Scrap[ValidIndex("am_model.am_Vr_Scrap", this->attribute->am2_Ai_Type, 30)] / 2;
}


typedef struct {
	double (*freq)(load*);
	int32 value;
} Oneof0;

static Oneof0 List0[] = {
	{ Funcl0, 1},
	{ Funcl1, 0}
};

static int32
oneofFunc0(load* this)
{
	size_t ind;
	size_t i;
	static Real freq[2];

	tprintf(tfp, "In oneof\n");
	for (i = 0; i < 2; i++)
		freq[i] = List0[i].freq(this);
	ind = oneof_n(ValidPtr(am2_stream47, 60, rnstream*), 2, freq);
	return List0[ind].value;
}


static simloc*
Func0(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_W18, -9999);
}


static simloc*
Func1(load* this)
{
	return LocGetQualifier(am_model.am_pf_ohc.am_W19, -9999);
}


typedef struct {
	simloc* (*value)(load*);
} Nextof1;

static Nextof1 List1[] = {
	Func0,
	Func1
};

static simloc*
nextofFunc1(load* this)
{
	static int ind = 1;

	tprintf(tfp, "In nextof\n");
	ind = (ind+1) % 2;
	return List1[ind].value(this);
}

static int32
OrderCondFunc13(load* this)
{
	if (this->attribute->am2_Ai_Type == am2_Vi_TypeCheck)
		return TRUE;
	return FALSE;
}

static int32
P_StorageToBuffer_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", localactor);
	AMDebuggerParams("base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 0, NULL, NULL, NULL);
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
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1292);
			if (this->attribute->am2_Ai_pi == 5555) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1294);
					this->attribute->am2_Ai_pi = 0;
					EntityChanged(0x00000040);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1296);
			if (this->attribute->am2_Ai_y == 4455) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1296);
				this->attribute->am2_Ai_y = 0;
				EntityChanged(0x00000040);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1298);
			if (this->attribute->am2_Ai_Zone <= 0 || this->attribute->am2_Ai_Zone > 3) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1299);
					{
						char* pArg1 = "ERROR: Cars released do not have Storage location pointer";
						char* pArg2 = " ";
						char* pArg3 = rel_actorname(LdGetCurProc(this), am_model.$sys);

						message("%s%s%s", pArg1, pArg2, pArg3);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1300);
					{
						int result = deccount(am2_C_Error, 1, this, P_StorageToBuffer_arriving, Step 2, am_localargs);
						if (result != Continue) return result;
Label2: ; /* Step 2 */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1302);
			if (this->attribute->am2_Ai_Line <= 0 || this->attribute->am2_Ai_Line > 4) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1303);
					{
						char* pArg1 = "ERROR: Cars released do not have WE buffer Line #";
						char* pArg2 = " ";
						char* pArg3 = rel_actorname(LdGetCurProc(this), am_model.$sys);

						message("%s%s%s", pArg1, pArg2, pArg3);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1304);
					{
						int result = deccount(am2_C_Error, 1, this, P_StorageToBuffer_arriving, Step 3, am_localargs);
						if (result != Continue) return result;
Label3: ; /* Step 3 */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1306);
			if (this->attribute->am2_Ai_Zone == 1) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1307);
					pushppa(this, P_StorageToBuffer_arriving, Step 4, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST9_2, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1308);
					pushppa(this, P_StorageToBuffer_arriving, Step 5, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST9_3, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label5: ; /* Step 5 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1309);
					if (this->attribute->am2_Ai_pi == 4444 || this->attribute->am2_Ai_Seq == 9999 || this->attribute->am2_Ai_pi == 1111) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1310);
							if (RscGetStatus(ValidPtr(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 50, resource*)) == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1311);
									uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1312);
									{
										char* pArg1 = "U";
										char* pArg2 = " ";
										char* pArg3 = rel_actorname(this, am_model.$sys);

										updatelabel(&(am2_Lb_ZoneExit[ValidIndex("am_model.am_Lb_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), "%s%s%s", pArg1, pArg2, pArg3);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1315);
					ListRemoveFirstMatch(LoadList, am2_Vldlst_ZoneExit[ValidIndex("am_model.am_Vldlst_ZoneExit", this->attribute->am2_Ai_Zone, 3)], this);	/* remove first match from list */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1316);
					pushppa(this, P_StorageToBuffer_arriving, Step 6, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST12, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label6: ; /* Step 6 */
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1318);
				if (this->attribute->am2_Ai_Zone == 2) {
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1319);
						pushppa(this, P_StorageToBuffer_arriving, Step 7, am_localargs);
						load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST9_4, -9999));
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label7: ; /* Step 7 */
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1320);
						if (this->attribute->am2_Ai_pi == 4444 || this->attribute->am2_Ai_Seq == 9999 || this->attribute->am2_Ai_pi == 1111) {
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1321);
								if (RscGetStatus(ValidPtr(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 50, resource*)) == 0) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1322);
										uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1323);
										{
											char* pArg1 = "U";
											char* pArg2 = " ";
											char* pArg3 = rel_actorname(this, am_model.$sys);

											updatelabel(&(am2_Lb_ZoneExit[ValidIndex("am_model.am_Lb_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), "%s%s%s", pArg1, pArg2, pArg3);
										}
									}
								}
							}
						}
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1326);
						ListRemoveFirstMatch(LoadList, am2_Vldlst_ZoneExit[ValidIndex("am_model.am_Vldlst_ZoneExit", this->attribute->am2_Ai_Zone, 3)], this);	/* remove first match from list */
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1327);
						pushppa(this, P_StorageToBuffer_arriving, Step 8, am_localargs);
						load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST12, -9999));
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label8: ; /* Step 8 */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1329);
			pushppa(this, P_StorageToBuffer_arriving, Step 9, am_localargs);
			load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST13, -9999));
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label9: ; /* Step 9 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1331);
			if (this->attribute->am2_Ai_Zone == 3) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1332);
					if (this->attribute->am2_Ai_pi == 4444 || this->attribute->am2_Ai_Seq == 9999 || this->attribute->am2_Ai_pi == 1111) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1333);
							if (RscGetStatus(ValidPtr(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), 50, resource*)) == 0) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1334);
									uprsrc(&(am2_R_ZoneExit[ValidIndex("am_model.am_R_ZoneExit", this->attribute->am2_Ai_Zone, 3)]));
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1335);
									{
										char* pArg1 = "U";
										char* pArg2 = " ";
										char* pArg3 = rel_actorname(this, am_model.$sys);

										updatelabel(&(am2_Lb_ZoneExit[ValidIndex("am_model.am_Lb_ZoneExit", this->attribute->am2_Ai_Zone, 3)]), "%s%s%s", pArg1, pArg2, pArg3);
									}
								}
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1338);
					ListRemoveFirstMatch(LoadList, am2_Vldlst_ZoneExit[ValidIndex("am_model.am_Vldlst_ZoneExit", this->attribute->am2_Ai_Zone, 3)], this);	/* remove first match from list */
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1346);
			this->attribute->am2_Ai_Seq = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1347);
			am2_viCarrierReleaseCount[ValidIndex("am_model.am_viCarrierReleaseCount", this->attribute->am2_Ai_Side, 4)][1] += 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1348);
			if (am2_viCarrierReleaseCount[ValidIndex("am_model.am_viCarrierReleaseCount", this->attribute->am2_Ai_Side, 4)][1] == am2_viCarrierReleaseCount[ValidIndex("am_model.am_viCarrierReleaseCount", this->attribute->am2_Ai_Side, 4)][2] && OrdGetCurConts(ValidPtr(&(am2_O_Release[ValidIndex("am_model.am_O_Release", this->attribute->am2_Ai_Side, 4)]), 40, ordlist*)) == 1) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1349);
					{
						int32 tempint = order(1, &(am2_O_Release[ValidIndex("am_model.am_O_Release", this->attribute->am2_Ai_Side, 4)]), NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, &(am2_O_Release[ValidIndex("am_model.am_O_Release", this->attribute->am2_Ai_Side, 4)]), NULL, NULL);	/* Place a backorder */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1350);
					{
						int32 tempint = order(1, &(am2_O_ST13_SPO_Seq[ValidIndex("am_model.am_O_ST13_SPO_Seq", this->attribute->am2_Ai_Side, 4)]), NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, &(am2_O_ST13_SPO_Seq[ValidIndex("am_model.am_O_ST13_SPO_Seq", this->attribute->am2_Ai_Side, 4)]), NULL, NULL);	/* Place a backorder */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1351);
					this->attribute->am2_Ai_Seq = 9999;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1352);
					am2_viCarrierReleaseCount[ValidIndex("am_model.am_viCarrierReleaseCount", this->attribute->am2_Ai_Side, 4)][1] = 0;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1357);
			if (waitfor(ToModelTime(am2_vrStopTime[9], UNITSECONDS), this, P_StorageToBuffer_arriving, Step 10, am_localargs) == Delayed)
				return Delayed;
Label10: ; /* Step 10 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1358);
			am2_Vi_x = 1;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1359);
			am2_Vi_CarQty = this->attribute->am2_Ai_CarQty;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1360);
			while (am2_Vi_x <= am2_Vi_CarQty) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1361);
					this->attribute->am2_Ai_x = oneofFunc0(this);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1362);
					am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] += this->attribute->am2_Ai_x;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1363);
					this->attribute->am2_Ai_CarQty -= this->attribute->am2_Ai_x;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1364);
					ValidPtr(ListFirstItem(LoadList, PrcGetLoadList(ValidPtr(&(am2_P_SPO_Release[ValidIndex("am_model.am_P_SPO_Release", this->attribute->am2_Ai_Side, 4)]), 45, process*))), 32, load*)->attribute->am2_Ai_LotQty += this->attribute->am2_Ai_x;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1365);
					am2_Vi_x += 1;
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1367);
			{
				double pArg1 = am2_Vr_PartCount[2][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] / am2_Vr_PartCount[1][ValidIndex("am_model.am_Vr_PartCount", this->attribute->am2_Ai_Press, 2)] * 100;
				char* pArg2 = "%";

				updatelabel(&(am2_lblScrap[ValidIndex("am_model.am_lblScrap", this->attribute->am2_Ai_Press, 2)]), "%.2lf%s", pArg1, pArg2);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1368);
			{
				char* pArg1 = "Line=";
				char* pArg2 = " ";
				int32 pArg3 = this->attribute->am2_Ai_Line;
				char* pArg4 = " ";
				char* pArg5 = "-";
				char* pArg6 = " ";
				int32 pArg7 = this->attribute->am2_Ai_Side;
				char* pArg8 = " ";
				char* pArg9 = this->attribute->am2_As_Type;
				char* pArg10 = " ";
				char* pArg11 = "Lot =";
				char* pArg12 = " ";
				int32 pArg13 = ValidPtr(ListFirstItem(LoadList, PrcGetLoadList(ValidPtr(&(am2_P_SPO_Release[ValidIndex("am_model.am_P_SPO_Release", this->attribute->am2_Ai_Side, 4)]), 45, process*))), 32, load*)->attribute->am2_Ai_LotQty;
				char* pArg14 = " ";
				char* pArg15 = "RlsQty=";
				char* pArg16 = " ";
				int32 pArg17 = ValidPtr(ListFirstItem(LoadList, PrcGetLoadList(ValidPtr(&(am2_P_SPO_Release[ValidIndex("am_model.am_P_SPO_Release", this->attribute->am2_Ai_Side, 4)]), 45, process*))), 32, load*)->attribute->am2_Ai_Seq;

				updatelabel(&(am2_Lb_SPORelease[ValidIndex("am_model.am_Lb_SPORelease", this->attribute->am2_Ai_Side, 4)]), "%s%s%d%s%s%s%d%s%s%s%s%s%d%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13, pArg14, pArg15, pArg16, pArg17);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1370);
			if (this->attribute->am2_Ai_Seq == 9999) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1371);
				{
					int32 tempint = order(1, &(am2_oST13Inspect[ValidIndex("am_model.am_oST13Inspect", this->attribute->am2_Ai_Side, 4)]), NULL, NULL); /* Place an order */
					if (tempint > 0) backorder(tempint, &(am2_oST13Inspect[ValidIndex("am_model.am_oST13Inspect", this->attribute->am2_Ai_Side, 4)]), NULL, NULL);	/* Place a backorder */
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1375);
			if (this->attribute->am2_Ai_Line == 1) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1376);
					pushppa(this, P_StorageToBuffer_arriving, Step 11, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W15, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label11: ; /* Step 11 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1377);
					pushppa(this, P_StorageToBuffer_arriving, Step 12, am_localargs);
					load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W16, -9999));
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label12: ; /* Step 12 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1378);
					this->attribute->am2_Ai_Seq = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1379);
					this->attribute->am2_Ai_Status = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1381);
					if (this->attribute->am2_Ai_Side == 1) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1382);
							pushppa(this, P_StorageToBuffer_arriving, Step 13, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W32, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label13: ; /* Step 13 */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1383);
							pushppa(this, P_StorageToBuffer_arriving, Step 14, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W23, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label14: ; /* Step 14 */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1384);
							pushppa(this, P_StorageToBuffer_arriving, Step 15, am_localargs);
							load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W24, -9999));
							pushppa(this, travel_to_loc, Step 1, NULL);
							return Continue; /* go move to location */
Label15: ; /* Step 15 */
						}
					}
					else {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1386);
						if (this->attribute->am2_Ai_Side == 2) {
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1387);
								this->attribute->am2_Aloc_Destination = nextofFunc1(this);
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1388);
								pushppa(this, P_StorageToBuffer_arriving, Step 16, am_localargs);
								load_SetDestLoc(this, this->attribute->am2_Aloc_Destination);
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label16: ; /* Step 16 */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1389);
								return waitorder(am2_O_Weld1Order, this, P_StorageToBuffer_arriving, Step 17, am_localargs);
Label17: ; /* Step 17 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1390);
								pushppa(this, P_StorageToBuffer_arriving, Step 18, am_localargs);
								load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_W20, -9999));
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label18: ; /* Step 18 */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1392);
								am2_Vi_TypeCheck = this->attribute->am2_Ai_Type;
								EntityChanged(0x01000000);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1393);
								if (OrdGetCurConts(am2_O_Weld1Order) > 0) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1394);
										{
											int32 tempint = order(1, am2_O_Weld1Order, NULL, OrderCondFunc13); /* Place an order */
											if (tempint > 0) {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1395);
												order(1, am2_O_Weld1Order, NULL, NULL); /* Place an order */
											}
										}
									}
								}
								else {
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1397);
									{
										int32 tempint = order(1, am2_O_Weld1Order, NULL, NULL); /* Place an order */
										if (tempint > 0) backorder(tempint, am2_O_Weld1Order, NULL, NULL);	/* Place a backorder */
									}
								}
							}
						}
					}
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1402);
				if (this->attribute->am2_Ai_Line == 2) {
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1403);
						pushppa(this, P_StorageToBuffer_arriving, Step 19, am_localargs);
						load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST14, -9999));
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label19: ; /* Step 19 */
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1404);
						pushppa(this, P_StorageToBuffer_arriving, Step 20, am_localargs);
						load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST146, -9999));
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label20: ; /* Step 20 */
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1406);
						if (this->attribute->am2_Ai_Side == 3) {
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1407);
								pushppa(this, P_StorageToBuffer_arriving, Step 21, am_localargs);
								load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST154, -9999));
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label21: ; /* Step 21 */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1408);
								this->attribute->am2_Ai_Seq = 0;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1409);
								this->attribute->am2_Ai_Status = 0;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1410);
								if (this->attribute->am2_Ai_Type != am2_viWeld2PrevType[1] && am2_viWeld2PrevType[1] != 0) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1411);
										if (OrdGetCurConts(ValidPtr(&(am2_O_Weld2Order[2]), 40, ordlist*)) == 1 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST158, -9999), 38, simloc*)) < LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST158, -9999), 38, simloc*))) {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1412);
												order(1, &(am2_O_Weld2Order[2]), NULL, NULL); /* Place an order */
											}
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1413);
												return waitorder(&(am2_O_Weld2Order[1]), this, P_StorageToBuffer_arriving, Step 22, am_localargs);
Label22: ; /* Step 22 */
												if (!this->inLeaveProc && this->nextproc) {
													retval = Continue;
													goto LabelRet;
												}
											}
										}
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1415);
										if (OrdGetCurConts(am2_oWeld2Changeover) == 0) {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1416);
												return waitorder(am2_oWeld2Changeover, this, P_StorageToBuffer_arriving, Step 23, am_localargs);
Label23: ; /* Step 23 */
												if (!this->inLeaveProc && this->nextproc) {
													retval = Continue;
													goto LabelRet;
												}
											}
										}
										else {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1419);
												order(1, am2_oWeld2Changeover, NULL, NULL); /* Place an order */
											}
										}
									}
								}
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1426);
								return waitorder(&(am2_O_Weld2Order[1]), this, P_StorageToBuffer_arriving, Step 24, am_localargs);
Label24: ; /* Step 24 */
								if (!this->inLeaveProc && this->nextproc) {
									retval = Continue;
									goto LabelRet;
								}
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1427);
								am2_viWeld2PrevType[1] = this->attribute->am2_Ai_Type;
								EntityChanged(0x01000000);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1428);
								pushppa(this, P_StorageToBuffer_arriving, Step 25, am_localargs);
								load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST158, -9999));
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label25: ; /* Step 25 */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1430);
								if (OrdGetCurConts(am2_oWeld2Changeover) == 1) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1431);
										if (ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(am2_oWeld2Changeover)), 32, load*)->attribute->am2_Ai_Side == this->attribute->am2_Ai_Side) {
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1432);
											{
												int32 tempint = order(1, &(am2_O_Weld2Order[2]), NULL, NULL); /* Place an order */
												if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[2]), NULL, NULL);	/* Place a backorder */
											}
										}
										else {
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1433);
											{
												int32 tempint = order(1, &(am2_O_Weld2Order[1]), NULL, NULL); /* Place an order */
												if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[1]), NULL, NULL);	/* Place a backorder */
											}
										}
									}
								}
								else {
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1435);
									{
										int32 tempint = order(1, &(am2_O_Weld2Order[2]), NULL, NULL); /* Place an order */
										if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[2]), NULL, NULL);	/* Place a backorder */
									}
								}
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1437);
								pushppa(this, P_StorageToBuffer_arriving, Step 26, am_localargs);
								load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST101, -9999));
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label26: ; /* Step 26 */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1438);
								pushppa(this, P_StorageToBuffer_arriving, Step 27, am_localargs);
								load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST110, -9999));
								pushppa(this, travel_to_loc, Step 1, NULL);
								return Continue; /* go move to location */
Label27: ; /* Step 27 */
							}
						}
						else {
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1440);
							if (this->attribute->am2_Ai_Side == 4) {
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1441);
									pushppa(this, P_StorageToBuffer_arriving, Step 28, am_localargs);
									load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST156, -9999));
									pushppa(this, travel_to_loc, Step 1, NULL);
									return Continue; /* go move to location */
Label28: ; /* Step 28 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1442);
									this->attribute->am2_Ai_Seq = 0;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1443);
									this->attribute->am2_Ai_Status = 0;
									EntityChanged(0x00000040);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1444);
									if (this->attribute->am2_Ai_Type != am2_viWeld2PrevType[2] && am2_viWeld2PrevType[2] != 0) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1445);
											if (OrdGetCurConts(ValidPtr(&(am2_O_Weld2Order[1]), 40, ordlist*)) == 1 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST158, -9999), 38, simloc*)) < LocGetCapacity(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST158, -9999), 38, simloc*))) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1446);
													order(1, &(am2_O_Weld2Order[1]), NULL, NULL); /* Place an order */
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1447);
													return waitorder(&(am2_O_Weld2Order[2]), this, P_StorageToBuffer_arriving, Step 29, am_localargs);
Label29: ; /* Step 29 */
													if (!this->inLeaveProc && this->nextproc) {
														retval = Continue;
														goto LabelRet;
													}
												}
											}
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1449);
											if (OrdGetCurConts(am2_oWeld2Changeover) == 0) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1450);
													return waitorder(am2_oWeld2Changeover, this, P_StorageToBuffer_arriving, Step 30, am_localargs);
Label30: ; /* Step 30 */
													if (!this->inLeaveProc && this->nextproc) {
														retval = Continue;
														goto LabelRet;
													}
												}
											}
											else {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1453);
													order(1, am2_oWeld2Changeover, NULL, NULL); /* Place an order */
												}
											}
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1460);
									return waitorder(&(am2_O_Weld2Order[2]), this, P_StorageToBuffer_arriving, Step 31, am_localargs);
Label31: ; /* Step 31 */
									if (!this->inLeaveProc && this->nextproc) {
										retval = Continue;
										goto LabelRet;
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1461);
									am2_viWeld2PrevType[2] = this->attribute->am2_Ai_Type;
									EntityChanged(0x01000000);
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1462);
									pushppa(this, P_StorageToBuffer_arriving, Step 32, am_localargs);
									load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST158, -9999));
									pushppa(this, travel_to_loc, Step 1, NULL);
									return Continue; /* go move to location */
Label32: ; /* Step 32 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1464);
									if (OrdGetCurConts(am2_oWeld2Changeover) == 1) {
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1465);
											if (ValidPtr(ListFirstItem(LoadList, OrdGetLoadList(am2_oWeld2Changeover)), 32, load*)->attribute->am2_Ai_Side == this->attribute->am2_Ai_Side) {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1466);
												{
													int32 tempint = order(1, &(am2_O_Weld2Order[1]), NULL, NULL); /* Place an order */
													if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[1]), NULL, NULL);	/* Place a backorder */
												}
											}
											else {
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1467);
												{
													int32 tempint = order(1, &(am2_O_Weld2Order[2]), NULL, NULL); /* Place an order */
													if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[2]), NULL, NULL);	/* Place a backorder */
												}
											}
										}
									}
									else {
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1469);
										{
											int32 tempint = order(1, &(am2_O_Weld2Order[1]), NULL, NULL); /* Place an order */
											if (tempint > 0) backorder(tempint, &(am2_O_Weld2Order[1]), NULL, NULL);	/* Place a backorder */
										}
									}
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1471);
									pushppa(this, P_StorageToBuffer_arriving, Step 33, am_localargs);
									load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST101, -9999));
									pushppa(this, travel_to_loc, Step 1, NULL);
									return Continue; /* go move to location */
Label33: ; /* Step 33 */
								}
								{
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1472);
									pushppa(this, P_StorageToBuffer_arriving, Step 34, am_localargs);
									load_SetDestLoc(this, LocGetQualifier(am_model.am_pf_ohc.am_ST115, -9999));
									pushppa(this, travel_to_loc, Step 1, NULL);
									return Continue; /* go move to location */
Label34: ; /* Step 34 */
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1476);
			if (OrdGetCurConts(ValidPtr(&(am2_O_WeldBufferStarve[ValidIndex("am_model.am_O_WeldBufferStarve", this->attribute->am2_Ai_Side, 4)]), 40, ordlist*)) > 0) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1477);
				order(1, &(am2_O_WeldBufferStarve[ValidIndex("am_model.am_O_WeldBufferStarve", this->attribute->am2_Ai_Side, 4)]), NULL, NULL); /* Place an order */
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1478);
			if (this->attribute->am2_Ai_Status != 1) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1479);
				return waitorder(&(am2_O_WeldBuffer[ValidIndex("am_model.am_O_WeldBuffer", this->attribute->am2_Ai_Side, 4)]), this, P_StorageToBuffer_arriving, Step 35, am_localargs);
Label35: ; /* Step 35 */
				if (!this->inLeaveProc && this->nextproc) {
					retval = Continue;
					goto LabelRet;
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor, 1481);
			this->nextproc = &(am2_P_Unload[ValidIndex("am_model.am_P_Unload", this->attribute->am2_Ai_Side, 4)]); /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_StorageToBuffer", P_StorageToBuffer_arriving, localactor);
	return retval;
} /* end of P_StorageToBuffer_arriving */

static int32
pf_ohc_ST101_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST101", localactor);
	AMDebuggerParams("base.pf_ohc.ST101", pf_ohc_ST101_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST101", pf_ohc_ST101_staleave, localactor, 1489);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side == 3) {
				AMDebugger("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST101", pf_ohc_ST101_staleave, localactor, 1489);
				{
					int32 tempint = order(1, am2_O_WE2BufferLH, NULL, NULL); /* Place an order */
					if (tempint > 0) backorder(tempint, am2_O_WE2BufferLH, NULL, NULL);	/* Place a backorder */
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST101", pf_ohc_ST101_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST101_staleave */

static int32
pf_ohc_ST115_staleave(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST115", localactor);
	AMDebuggerParams("base.pf_ohc.ST115", pf_ohc_ST115_staleave, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST115", pf_ohc_ST115_staleave, localactor, 1492);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side == 4) {
				AMDebugger("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST115", pf_ohc_ST115_staleave, localactor, 1492);
				{
					int32 tempint = order(1, am2_O_WE2BufferRH, NULL, NULL); /* Place an order */
					if (tempint > 0) backorder(tempint, am2_O_WE2BufferRH, NULL, NULL);	/* Place a backorder */
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor station leaving procedure", "base.pf_ohc.ST115", pf_ohc_ST115_staleave, localactor);
	return retval;
} /* end of pf_ohc_ST115_staleave */

static int32
pf_ohc_ST134_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST134", localactor);
	AMDebuggerParams("base.pf_ohc.ST134", pf_ohc_ST134_staarrive, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST134", pf_ohc_ST134_staarrive, localactor, 1495);
			if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST134, -9999), 38, simloc*)) <= 1 && am2_viKickOutQty[2] == 0) {
				{
					AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST134", pf_ohc_ST134_staarrive, localactor, 1496);
					am2_viKickOutQty[2] = LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST15, -9999), 38, simloc*));
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST134", pf_ohc_ST134_staarrive, localactor, 1497);
					{
						int32 tempint = order(am2_viKickOutQty[2], am2_olCarKickout, NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, am2_olCarKickout, NULL, NULL);	/* Place a backorder */
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST134", pf_ohc_ST134_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST134_staarrive */

static int32
pf_ohc_LS_13_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", localactor);
	AMDebuggerParams("base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1502);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side == 3 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST152, -9999), 38, simloc*)) == 0 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST153, -9999), 38, simloc*)) == 0 && am2_Vi_WEBufferOrderTracker[3] != 1) {
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1503);
					am2_Vi_WEBufferOrderTracker[3] = 1;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1504);
					am2_Vi_SPO_QtyRelease[3] = am2_Vi_Release;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1505);
					{
						int32 tempint = order(1, &(am2_O_BufferSpace[3]), NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[3]), NULL, NULL);	/* Place a backorder */
					}
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1507);
				if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side == 4 && LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST155, -9999), 38, simloc*)) + LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_ST156, -9999), 38, simloc*)) <= 4 && am2_Vi_WEBufferOrderTracker[4] != 1) {
					{
						AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1508);
						am2_Vi_WEBufferOrderTracker[4] = 1;
						EntityChanged(0x01000000);
					}
					{
						AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1509);
						am2_Vi_SPO_QtyRelease[4] = am2_Vi_Release;
						EntityChanged(0x01000000);
					}
					{
						AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor, 1510);
						{
							int32 tempint = order(1, &(am2_O_BufferSpace[4]), NULL, NULL); /* Place an order */
							if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[4]), NULL, NULL);	/* Place a backorder */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_13", pf_ohc_LS_13_photocleared, localactor);
	return retval;
} /* end of pf_ohc_LS_13_photocleared */

static int32
pf_ohc_WEBufferReceived_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", localactor);
	AMDebuggerParams("base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor, 0, NULL, NULL, NULL);
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
			AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor, 1516);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Line == 1) {
				AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor, 1517);
				{
					int result = inccount(&(am2_C_L1Weld[ValidIndex("am_model.am_C_L1Weld", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 2)]), 1, this, pf_ohc_WEBufferReceived_photocleared, Step 2, am_localargs);
					if (result != Continue) return result;
Label2: ; /* Step 2 */
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor, 1519);
			if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Seq == 9999 && am2_Vi_WEBufferOrderTracker[ValidIndex("am_model.am_Vi_WEBufferOrderTracker", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 4)] == 1) {
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor, 1520);
					am2_Vi_WEBufferOrderTracker[ValidIndex("am_model.am_Vi_WEBufferOrderTracker", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 4)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor, 1521);
					am2_Vi_SPO_QtyRelease[ValidIndex("am_model.am_Vi_SPO_QtyRelease", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 4)] = 0;
					EntityChanged(0x01000000);
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.WEBufferReceived", pf_ohc_WEBufferReceived_photocleared, localactor);
	return retval;
} /* end of pf_ohc_WEBufferReceived_photocleared */

static int32
pf_ohc_LS_17_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", localactor);
	AMDebuggerParams("base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1527);
			if (LocGetCurConts(ValidPtr(LocGetQualifier(am_model.am_pf_ohc.am_W15, -9999), 38, simloc*)) == 0 && am2_Vi_WEBufferOrderTracker[1] != 1 && am2_Vi_WEBufferOrderTracker[2] != 1) {
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1531);
					if (PrcGetCurConts(ValidPtr(&(am2_P_SPO_Release[1]), 45, process*)) > 0) {
						{
							AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1532);
							am2_Vi_WEBufferOrderTracker[1] = 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1533);
							am2_Vi_SPO_QtyRelease[1] = am2_Vi_Release;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1534);
							{
								int32 tempint = order(1, &(am2_O_BufferSpace[1]), NULL, NULL); /* Place an order */
								if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[1]), NULL, NULL);	/* Place a backorder */
							}
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1537);
					if (PrcGetCurConts(ValidPtr(&(am2_P_SPO_Release[2]), 45, process*)) > 0) {
						{
							AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1538);
							am2_Vi_WEBufferOrderTracker[2] = 1;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1539);
							am2_Vi_SPO_QtyRelease[2] = am2_Vi_Release;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor, 1540);
							{
								int32 tempint = order(1, &(am2_O_BufferSpace[2]), NULL, NULL); /* Place an order */
								if (tempint > 0) backorder(tempint, &(am2_O_BufferSpace[2]), NULL, NULL);	/* Place a backorder */
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
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.LS_17", pf_ohc_LS_17_photocleared, localactor);
	return retval;
} /* end of pf_ohc_LS_17_photocleared */

static int32
pf_ohc_W13_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.W13", localactor);
	AMDebuggerParams("base.pf_ohc.W13", pf_ohc_W13_staarrive, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.W13", pf_ohc_W13_staarrive, localactor, 1554);
			if (VehGetCurLoad(this) == am2_Vld_Recirc[2]) {
				AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.W13", pf_ohc_W13_staarrive, localactor, 1554);
				am2_Vld_Recirc[2] = NULL;
				EntityChanged(0x01000000);
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.W13", pf_ohc_W13_staarrive, localactor);
	return retval;
} /* end of pf_ohc_W13_staarrive */

static int32
pf_ohc_ST14_staarrive(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST14", localactor);
	AMDebuggerParams("base.pf_ohc.ST14", pf_ohc_ST14_staarrive, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST14", pf_ohc_ST14_staarrive, localactor, 1557);
			if (VehGetCurLoad(this) == am2_Vld_Recirc[2]) {
				AMDebugger("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST14", pf_ohc_ST14_staarrive, localactor, 1557);
				am2_Vld_Recirc[2] = NULL;
				EntityChanged(0x01000000);
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor station arriving procedure", "base.pf_ohc.ST14", pf_ohc_ST14_staarrive, localactor);
	return retval;
} /* end of pf_ohc_ST14_staarrive */

static int32
pf_ohc_RowSwitch_photocleared(vehicle* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", localactor);
	AMDebuggerParams("base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1577);
			if (LdGetType(ValidPtr(VehGetCurLoad(this), 32, load*)) != am2_L_invis) {
				{
					AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1578);
					if (ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_pi == 4444) {
						AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1579);
						order(1, &(am2_O_ReleaseSeq[ValidIndex("am_model.am_O_ReleaseSeq", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 4)]), NULL, NULL); /* Place an order */
					}
					else {
						AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1580);
						if (this->load.attribute->am2_Ai_Side > 0) {
							{
								AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1581);
								if (am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 4)] == VehGetCurLoad(this)) {
									AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1581);
									am2_Vld_ReleasePrev[ValidIndex("am_model.am_Vld_ReleasePrev", ValidPtr(VehGetCurLoad(this), 32, load*)->attribute->am2_Ai_Side, 4)] = NULL;
									EntityChanged(0x01000000);
								}
							}
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1585);
			if (VehGetCurLoad(this) == am2_Vld_Recirc[2]) {
				AMDebugger("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor, 1586);
				order(1, am2_oZoneMixRecircComplete, NULL, NULL); /* Place an order */
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Conveyor photoeye cleared procedure", "base.pf_ohc.RowSwitch", pf_ohc_RowSwitch_photocleared, localactor);
	return retval;
} /* end of pf_ohc_RowSwitch_photocleared */

static int32
P_Unload_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_Unload", localactor);
	AMDebuggerParams("base.P_Unload", P_Unload_arriving, localactor, 0, NULL, NULL, NULL);
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
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1649);
			pushppa(this, P_Unload_arriving, Step 2, am_localargs);
			load_SetDestLoc(this, am2_Vloc_B4Unload[ValidIndex("am_model.am_Vloc_B4Unload", CurProcIndex(), 4)]);
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1650);
			pushppa(this, P_Unload_arriving, Step 3, am_localargs);
			load_SetDestLoc(this, am2_Vloc_Unload[ValidIndex("am_model.am_Vloc_Unload", CurProcIndex(), 4)]);
			pushppa(this, travel_to_loc, Step 1, NULL);
			return Continue; /* go move to location */
Label3: ; /* Step 3 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1653);
			if (this->attribute->am2_Ai_Status == 7777 && this->attribute->am2_Ai_Seq == 9999 && OrdGetCurConts(ValidPtr(&(am2_O_WaitForPartialRelease[ValidIndex("am_model.am_O_WaitForPartialRelease", CurProcIndex(), 4)]), 40, ordlist*)) > 0) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1654);
				order(1, &(am2_O_WaitForPartialRelease[ValidIndex("am_model.am_O_WaitForPartialRelease", CurProcIndex(), 4)]), NULL, NULL); /* Place an order */
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1655);
			am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] = this;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1657);
			if (ListSize(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1]) > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1658);
					if (ValidPtr(ListLastItem(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1]), 32, load*)->attribute->am2_Ai_Type == this->attribute->am2_Ai_Type) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1659);
							ValidPtr(ListLastItem(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1]), 32, load*)->attribute->am2_Ai_CarQty = ValidPtr(ListLastItem(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1]), 32, load*)->attribute->am2_Ai_CarQty + this->attribute->am2_Ai_CarQty;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1660);
							if (transformLoad(TX_LOAD_SCALE, 1.0, 1.0, this->attribute->am2_Ai_CarQty, 0, 0, 1, 0.0, ListLastItem(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1]), this, P_Unload_arriving, Step 4, am_localargs) == Delayed)
								return Delayed;
Label4: ; /* Step 4 */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1661);
							am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] = NULL;
							EntityChanged(0x01000000);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1662);
							LdSetType(this, am2_L_invis);
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1663);
							this->attribute->am2_Ai_Status = 7777;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1664);
							{
								int result = deccount(&(am2_C_CarrierCount[1]), 1, this, P_Unload_arriving, Step 5, am_localargs);
								if (result != Continue) return result;
Label5: ; /* Step 5 */
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1665);
							{
								int result = inccount(&(am2_C_CarrierCount[2]), 1, this, P_Unload_arriving, Step 6, am_localargs);
								if (result != Continue) return result;
Label6: ; /* Step 6 */
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1666);
							if (CntGetCurConts(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*)) >= am2_Vi_EmptyReq && OrdGetCurConts(am2_O_HoldForCarriers) >= 1) {
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1667);
								order(1, am2_O_HoldForCarriers, NULL, NULL); /* Place an order */
							}
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1668);
							this->nextproc = am2_pEmptyCarrierReturn; /* send to ... */
							EntityChanged(W_LOAD);
							retval = Continue;
							goto LabelRet;
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1672);
			while (this->attribute->am2_Ai_CarQty > 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1673);
					return waitorder(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], this, P_Unload_arriving, Step 7, am_localargs);
Label7: ; /* Step 7 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1674);
					if (this->attribute->am2_Ai_Status == 8888) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1674);
						this->nextproc = &(am2_P_PartialStorage[ValidIndex("am_model.am_P_PartialStorage", CurProcIndex(), 4)]); /* send to ... */
						EntityChanged(W_LOAD);
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1675);
					{
						int32 tempint = order(1, am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][1], NULL, NULL); /* Place an order */
						if (tempint > 0) backorder(tempint, am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][1], NULL, NULL);	/* Place a backorder */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1676);
					return waitorder(am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][2], this, P_Unload_arriving, Step 8, am_localargs);
Label8: ; /* Step 8 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1677);
					return waitorder(am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][3], this, P_Unload_arriving, Step 9, am_localargs);
Label9: ; /* Step 9 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1678);
					this->attribute->am2_Ai_CarQty -= 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1679);
					if (this->attribute->am2_Ai_CarQty > 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1679);
						if (transformLoad(TX_LOAD_SCALE, 1.0, 1.0, this->attribute->am2_Ai_CarQty, 0, 0, 1, 0.0, this, this, P_Unload_arriving, Step 10, am_localargs) == Delayed)
							return Delayed;
Label10: ; /* Step 10 */
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1680);
					{
						double pArg1 = FromModelTime(ASIclock, UNITSECONDS) - am2_Vr_WeldCycleTime[ValidIndex("am_model.am_Vr_WeldCycleTime", CurProcIndex(), 4)];

						updatelabel(&(am2_Lb_WeldCycle[ValidIndex("am_model.am_Lb_WeldCycle", CurProcIndex(), 4)]), "%.2lf", pArg1);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1681);
					am2_Vr_WeldCycleTime[ValidIndex("am_model.am_Vr_WeldCycleTime", CurProcIndex(), 4)] = FromModelTime(ASIclock, UNITSECONDS);
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1683);
			am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)] = NULL;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1684);
			LdSetType(this, am2_L_invis);
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1685);
			this->attribute->am2_Ai_Status = 7777;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1686);
			{
				int result = deccount(&(am2_C_CarrierCount[1]), 1, this, P_Unload_arriving, Step 11, am_localargs);
				if (result != Continue) return result;
Label11: ; /* Step 11 */
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1687);
			{
				int result = inccount(&(am2_C_CarrierCount[2]), 1, this, P_Unload_arriving, Step 12, am_localargs);
				if (result != Continue) return result;
Label12: ; /* Step 12 */
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1688);
			if (CntGetCurConts(ValidPtr(&(am2_C_CarrierCount[2]), 10, counter*)) >= am2_Vi_EmptyReq && OrdGetCurConts(am2_O_HoldForCarriers) >= 1) {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1689);
				order(1, am2_O_HoldForCarriers, NULL, NULL); /* Place an order */
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor, 1690);
			this->nextproc = am2_pEmptyCarrierReturn; /* send to ... */
			EntityChanged(W_LOAD);
			retval = Continue;
			goto LabelRet;
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_Unload", P_Unload_arriving, localactor);
	return retval;
} /* end of P_Unload_arriving */

static int32
P_PartialStorage_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", localactor);
	AMDebuggerParams("base.P_PartialStorage", P_PartialStorage_arriving, localactor, 0, NULL, NULL, NULL);
	switch (step) { /* Make the step switcher */
	case Step 1: goto Label1;
	case Step 2: goto Label2;
	case Step 3: goto Label3;
	case Step 4: goto Label4;
	case Step 5: goto Label5;
	case Step 6: goto Label6;
	case Step 7: goto Label7;
	case Step 8: goto Label8;
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1707);
			if (LocGetRemCap(ValidPtr(am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][1], 38, simloc*)) == 0 && LocGetRemCap(ValidPtr(am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][2], 38, simloc*)) == 0) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1708);
					{
						char* pArg1 = "NO SPACE @ PARTIAL BUFFER";
						char* pArg2 = " ";
						char* pArg3 = rel_actorname(this, am_model.$sys);
						char* pArg4 = " ";
						int32 pArg5 = CurProcIndex();

						message("%s%s%s%s%d", pArg1, pArg2, pArg3, pArg4, pArg5);
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1709);
					{
						int result = deccount(am2_C_Error, 1, this, P_PartialStorage_arriving, Step 2, am_localargs);
						if (result != Continue) return result;
Label2: ; /* Step 2 */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1711);
			ListAppendItem(LoadList, am2_Vldlst_Partial[ValidIndex("am_model.am_Vldlst_Partial", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_Partial", this->attribute->am2_Ai_Type, 30)], this);	/* append item to end of list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1712);
			if (LocGetCurConts(ValidPtr(am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][1], 38, simloc*)) < LocGetCapacity(ValidPtr(am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][1], 38, simloc*))) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1713);
					ListAppendItem(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1], this);	/* append item to end of list */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1714);
					pushppa(this, P_PartialStorage_arriving, Step 3, am_localargs);
					load_SetDestLoc(this, am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][1]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label3: ; /* Step 3 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1715);
					if (this->attribute->am2_Ai_Status != 7777 || this->attribute->am2_Ai_Status != 6666) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1716);
						return waitorder(am2_Vo_PartialBuffer[ValidIndex("am_model.am_Vo_PartialBuffer", CurProcIndex(), 4)][1], this, P_PartialStorage_arriving, Step 4, am_localargs);
Label4: ; /* Step 4 */
						if (!this->inLeaveProc && this->nextproc) {
							retval = Continue;
							goto LabelRet;
						}
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1717);
					ListRemoveFirstMatch(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1], this);	/* remove first match from list */
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1719);
				if (LocGetCurConts(ValidPtr(am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][2], 38, simloc*)) < LocGetCapacity(ValidPtr(am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][2], 38, simloc*))) {
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1720);
						ListAppendItem(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][1], this);	/* append item to end of list */
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1721);
						pushppa(this, P_PartialStorage_arriving, Step 5, am_localargs);
						load_SetDestLoc(this, am2_Vloc_Partial[ValidIndex("am_model.am_Vloc_Partial", CurProcIndex(), 4)][2]);
						pushppa(this, travel_to_loc, Step 1, NULL);
						return Continue; /* go move to location */
Label5: ; /* Step 5 */
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1722);
						if (this->attribute->am2_Ai_Status != 7777 || this->attribute->am2_Ai_Status != 6666) {
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1723);
							return waitorder(am2_Vo_PartialBuffer[ValidIndex("am_model.am_Vo_PartialBuffer", CurProcIndex(), 4)][2], this, P_PartialStorage_arriving, Step 6, am_localargs);
Label6: ; /* Step 6 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1724);
						ListRemoveFirstMatch(LoadList, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][2], this);	/* remove first match from list */
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1726);
			ListRemoveFirstMatch(LoadList, am2_Vldlst_Partial[ValidIndex("am_model.am_Vldlst_Partial", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_Partial", this->attribute->am2_Ai_Type, 30)], this);	/* remove first match from list */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1728);
			if (this->attribute->am2_Ai_Status == 6666) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1729);
					pushppa(this, P_PartialStorage_arriving, Step 7, am_localargs);
					load_SetDestLoc(this, am2_Vloc_Unload[ValidIndex("am_model.am_Vloc_Unload", CurProcIndex(), 4)]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label7: ; /* Step 7 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1730);
					this->attribute->am2_Ai_Status = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1731);
					this->attribute->am2_Ai_Seq = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1732);
					this->nextproc = &(am2_P_PartialStorage[ValidIndex("am_model.am_P_PartialStorage", CurProcIndex(), 4)]); /* send to ... */
					EntityChanged(W_LOAD);
					retval = Continue;
					goto LabelRet;
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1734);
				if (this->attribute->am2_Ai_Status == 7777) {
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1734);
					this->nextproc = &(am2_P_Unload[ValidIndex("am_model.am_P_Unload", CurProcIndex(), 4)]); /* send to ... */
					EntityChanged(W_LOAD);
					retval = Continue;
					goto LabelRet;
				}
				else {
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1736);
						{
							char* pArg1 = "ERROR in setting Ai_Status value @ line";
							char* pArg2 = " ";
							int32 pArg3 = CurProcIndex();
							char* pArg4 = " ";
							char* pArg5 = "partial buffer:  CHECK";
							char* pArg6 = " ";
							char* pArg7 = rel_actorname(this, am_model.$sys);

							message("%s%s%d%s%s%s%s", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7);
						}
					}
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor, 1737);
						{
							int result = deccount(am2_C_Error, 1, this, P_PartialStorage_arriving, Step 8, am_localargs);
							if (result != Continue) return result;
Label8: ; /* Step 8 */
						}
					}
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_PartialStorage", P_PartialStorage_arriving, localactor);
	return retval;
} /* end of P_PartialStorage_arriving */

static int32
P_PartialRelease_arriving(load* this, int32 step, void* args)
{
	struct _localargs {
		AMLoadListItem* lv317; /* 'for each' loop variable */
		AMLoadList* ls317; /* 'for each' list */
		AMLoadListItem* lv318; /* 'for each' loop variable */
		AMLoadList* ls318; /* 'for each' list */
		AMLoadListItem* lv319; /* 'for each' loop variable */
		AMLoadList* ls319; /* 'for each' list */
		AMLoadListItem* lv320; /* 'for each' loop variable */
		AMLoadList* ls320; /* 'for each' list */
		AMLoadListItem* lv321; /* 'for each' loop variable */
		AMLoadList* ls321; /* 'for each' list */
	} *am_localargs = (struct _localargs*)args;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", localactor);
	AMDebuggerParams("base.P_PartialRelease", P_PartialRelease_arriving, localactor, 0, NULL, NULL, NULL);
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
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1749);
			am_localargs->ls317 = 0;
			ListCopy(LoadList, am_localargs->ls317, am2_Vldlst_Partial[ValidIndex("am_model.am_Vldlst_Partial", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_Partial", this->attribute->am2_Ai_Type, 30)]);
			for (am_localargs->lv317 = (am_localargs->ls317) ? (am_localargs->ls317)->first : NULL; am_localargs->lv317; am_localargs->lv317 = am_localargs->lv317->next) {
				am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)] = am_localargs->lv317->item;
				{
					{
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1750);
						ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status = 7777;
						EntityChanged(0x00000040);
					}
				}
			}
			ListRemoveAllAndFree(LoadList, am_localargs->ls317); /* End of for each */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1752);
			this->attribute->am2_Ai_pi = 0;
			EntityChanged(0x00000040);
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1753);
			while (this->attribute->am2_Ai_pi < 2) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1754);
					this->attribute->am2_Ai_pi += 1;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1755);
					if (ListSize(LoadList, am2_Vldlst_Partial[ValidIndex("am_model.am_Vldlst_Partial", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_Partial", this->attribute->am2_Ai_Type, 30)]) == 0) {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1755);
						break;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1756);
					this->attribute->am2_Ai_Seq = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1757);
					am2_Vi_PartialIndex[ValidIndex("am_model.am_Vi_PartialIndex", CurProcIndex(), 4)] = 0;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1758);
					am_localargs->ls318 = 0;
					ListCopy(LoadList, am_localargs->ls318, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_PartLane", this->attribute->am2_Ai_pi, 2)]);
					for (am_localargs->lv318 = (am_localargs->ls318) ? (am_localargs->ls318)->first : NULL; am_localargs->lv318; am_localargs->lv318 = am_localargs->lv318->next) {
						am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)] = am_localargs->lv318->item;
						{
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1759);
								am2_Vi_PartialIndex[ValidIndex("am_model.am_Vi_PartialIndex", CurProcIndex(), 4)] += 1;
								EntityChanged(0x01000000);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1760);
								if (ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status == 7777) {
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1761);
										this->attribute->am2_Ai_Seq = am2_Vi_PartialIndex[ValidIndex("am_model.am_Vi_PartialIndex", CurProcIndex(), 4)] - 1;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1762);
										break;
									}
								}
							}
						}
					}
					ListRemoveAllAndFree(LoadList, am_localargs->ls318); /* End of for each */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1765);
					this->attribute->am2_Ai_CarQty = 0;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1766);
					am_localargs->ls319 = 0;
					ListCopy(LoadList, am_localargs->ls319, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_PartLane", this->attribute->am2_Ai_pi, 2)]);
					for (am_localargs->lv319 = (am_localargs->ls319) ? (am_localargs->ls319)->first : NULL; am_localargs->lv319; am_localargs->lv319 = am_localargs->lv319->next) {
						am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)] = am_localargs->lv319->item;
						{
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1767);
								this->attribute->am2_Ai_CarQty += 1;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1768);
								if (ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status == 7777) {
									AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1768);
									this->attribute->am2_Ai_Row = this->attribute->am2_Ai_CarQty;
									EntityChanged(0x00000040);
								}
							}
						}
					}
					ListRemoveAllAndFree(LoadList, am_localargs->ls319); /* End of for each */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1771);
					if (this->attribute->am2_Ai_Seq > 0) {
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1772);
							this->attribute->am2_Ai_CarQty = 0;
							EntityChanged(0x00000040);
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1773);
							am_localargs->ls320 = 0;
							ListCopy(LoadList, am_localargs->ls320, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_PartLane", this->attribute->am2_Ai_pi, 2)]);
							for (am_localargs->lv320 = (am_localargs->ls320) ? (am_localargs->ls320)->first : NULL; am_localargs->lv320; am_localargs->lv320 = am_localargs->lv320->next) {
								am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)] = am_localargs->lv320->item;
								{
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1774);
										this->attribute->am2_Ai_CarQty += 1;
										EntityChanged(0x00000040);
									}
									{
										AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1775);
										if (ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status != 7777) {
											{
												AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1776);
												ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status = 6666;
												EntityChanged(0x00000040);
											}
										}
										else {
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1779);
											if (ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status == 7777) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1780);
													if (this->attribute->am2_Ai_CarQty == this->attribute->am2_Ai_Row) {
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1780);
														ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Seq = 9999;
														EntityChanged(0x00000040);
													}
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1781);
													break;
												}
											}
										}
									}
								}
							}
							ListRemoveAllAndFree(LoadList, am_localargs->ls320); /* End of for each */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1784);
							order(1, am2_Vo_PartialBuffer[ValidIndex("am_model.am_Vo_PartialBuffer", CurProcIndex(), 4)][ValidIndex("am_model.am_Vo_PartialBuffer", this->attribute->am2_Ai_pi, 2)], NULL, NULL); /* Place an order */
						}
						{
							AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1785);
							return waitorder(&(am2_O_WaitForPartialRelease[ValidIndex("am_model.am_O_WaitForPartialRelease", CurProcIndex(), 4)]), this, P_PartialRelease_arriving, Step 2, am_localargs);
Label2: ; /* Step 2 */
							if (!this->inLeaveProc && this->nextproc) {
								retval = Continue;
								goto LabelRet;
							}
						}
					}
					else {
						AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1788);
						if (this->attribute->am2_Ai_Seq == 0) {
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1789);
								this->attribute->am2_Ai_CarQty = 0;
								EntityChanged(0x00000040);
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1790);
								am_localargs->ls321 = 0;
								ListCopy(LoadList, am_localargs->ls321, am2_Vldlst_PartLane[ValidIndex("am_model.am_Vldlst_PartLane", CurProcIndex(), 4)][ValidIndex("am_model.am_Vldlst_PartLane", this->attribute->am2_Ai_pi, 2)]);
								for (am_localargs->lv321 = (am_localargs->ls321) ? (am_localargs->ls321)->first : NULL; am_localargs->lv321; am_localargs->lv321 = am_localargs->lv321->next) {
									am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)] = am_localargs->lv321->item;
									{
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1791);
											this->attribute->am2_Ai_CarQty += 1;
											EntityChanged(0x00000040);
										}
										{
											AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1792);
											if (ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Status == 7777) {
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1793);
													if (this->attribute->am2_Ai_CarQty == this->attribute->am2_Ai_Row) {
														AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1793);
														ValidPtr(am2_Vldptr_Partial[ValidIndex("am_model.am_Vldptr_Partial", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Seq = 9999;
														EntityChanged(0x00000040);
													}
												}
												{
													AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1794);
													break;
												}
											}
										}
									}
								}
								ListRemoveAllAndFree(LoadList, am_localargs->ls321); /* End of for each */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1797);
								order(1, am2_Vo_PartialBuffer[ValidIndex("am_model.am_Vo_PartialBuffer", CurProcIndex(), 4)][ValidIndex("am_model.am_Vo_PartialBuffer", this->attribute->am2_Ai_pi, 2)], NULL, NULL); /* Place an order */
							}
							{
								AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1798);
								return waitorder(&(am2_O_WaitForPartialRelease[ValidIndex("am_model.am_O_WaitForPartialRelease", CurProcIndex(), 4)]), this, P_PartialRelease_arriving, Step 3, am_localargs);
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
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor, 1801);
			order(1, &(am2_O_PartialRelease[ValidIndex("am_model.am_O_PartialRelease", CurProcIndex(), 4)]), NULL, NULL); /* Place an order */
		}
	}
LabelRet: ;
	ListRemoveAllAndFree(LoadList, am_localargs->ls317);
	ListRemoveAllAndFree(LoadList, am_localargs->ls318);
	ListRemoveAllAndFree(LoadList, am_localargs->ls319);
	ListRemoveAllAndFree(LoadList, am_localargs->ls320);
	ListRemoveAllAndFree(LoadList, am_localargs->ls321);
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_PartialRelease", P_PartialRelease_arriving, localactor);
	return retval;
} /* end of P_PartialRelease_arriving */

static int32
P_LoadRBT_arriving(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", localactor);
	AMDebuggerParams("base.P_LoadRBT", P_LoadRBT_arriving, localactor, 0, NULL, NULL, NULL);
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
	default: message("Bad step number %ld.", step);
	}
	retval = Error;
	goto LabelRet;
Label1: ;  /* Step1 */
	{
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1814);
			pushppa(this, P_LoadRBT_arriving, Step 2, am_localargs);
			load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][1]);
			pushppa(this, move_in_loc, Step 1, NULL);
			return Continue; /* go move into territory */
Label2: ; /* Step 2 */
		}
		{
			AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1815);
			while (1 == 1) {
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1816);
					return waitorder(am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][1], this, P_LoadRBT_arriving, Step 3, am_localargs);
Label3: ; /* Step 3 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1817);
					pushppa(this, P_LoadRBT_arriving, Step 4, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][2]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label4: ; /* Step 4 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1818);
					return waitorder(am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][1], this, P_LoadRBT_arriving, Step 5, am_localargs);
Label5: ; /* Step 5 */
					if (!this->inLeaveProc && this->nextproc) {
						retval = Continue;
						goto LabelRet;
					}
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1819);
					pushppa(this, P_LoadRBT_arriving, Step 6, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][3]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label6: ; /* Step 6 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1821);
					order(1, am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][2], NULL, NULL); /* Place an order */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1822);
					this->attribute->am2_Ai_Type = ValidPtr(am2_Vldptr_Unload[ValidIndex("am_model.am_Vldptr_Unload", CurProcIndex(), 4)], 32, load*)->attribute->am2_Ai_Type;
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1823);
					LdSetType(this, am2_vlt_LoadType[ValidIndex("am_model.am_vlt_LoadType", this->attribute->am2_Ai_Type, 30)]);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1824);
					LdSetColor(this, am2_vclr_LoadColor[ValidIndex("am_model.am_vclr_LoadColor", this->attribute->am2_Ai_Type, 30)]);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1825);
					pushppa(this, P_LoadRBT_arriving, Step 7, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][4]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label7: ; /* Step 7 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1826);
					order(1, am2_Vo_RBT[ValidIndex("am_model.am_Vo_RBT", CurProcIndex(), 4)][3], NULL, NULL); /* Place an order */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1828);
					pushppa(this, P_LoadRBT_arriving, Step 8, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][5]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label8: ; /* Step 8 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1829);
					pushppa(this, P_LoadRBT_arriving, Step 9, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][6]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label9: ; /* Step 9 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1831);
					order(1, am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][2], NULL, NULL); /* Place an order */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1832);
					LdSetType(this, am2_L_invis);
					EntityChanged(0x00000040);
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1833);
					pushppa(this, P_LoadRBT_arriving, Step 10, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][7]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label10: ; /* Step 10 */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1835);
					order(1, am2_Vo_SPO[ValidIndex("am_model.am_Vo_SPO", CurProcIndex(), 4)][3], NULL, NULL); /* Place an order */
				}
				{
					AMDebugger("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor, 1836);
					pushppa(this, P_LoadRBT_arriving, Step 11, am_localargs);
					load_SetDestLoc(this, am2_Vloc_LoadRBT[ValidIndex("am_model.am_Vloc_LoadRBT", CurProcIndex(), 4)][1]);
					pushppa(this, travel_to_loc, Step 1, NULL);
					return Continue; /* go move to location */
Label11: ; /* Step 11 */
				}
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Arriving procedure", "base.P_LoadRBT", P_LoadRBT_arriving, localactor);
	return retval;
} /* end of P_LoadRBT_arriving */

static int32
am_Sr_WriteJSONFile(load* this, int32 step, void* args)
{
	void* am_localargs = NULL;
	load* localactor = this;
	int32 retval = Continue;
	AMDebuggerBeginRoutine("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", localactor);
	AMDebuggerParams("base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 0, NULL, NULL, NULL);
	{
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1842);
			{
				char* pArg1 = "{";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1843);
			{
				char* pArg1 = "\"messageType\":\"";
				char* pArg2 = ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_Message;
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%s%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1844);
			{
				char* pArg1 = "\"tagID\":\"1111\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1845);
			{
				char* pArg1 = "\"carrierNumber\":\"";
				char* pArg2 = rel_actorname(LdGetVehicle(ValidPtr(am2_vldWriteJSON, 32, load*)), am_model.$sys);
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%s%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1846);
			{
				char* pArg1 = "\"dieNumber\":\"";
				int32 pArg2 = ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_LotNo;
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%d%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1847);
			{
				char* pArg1 = "\"quantity\":\"";
				int32 pArg2 = ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_CarQty;
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%d%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1848);
			if (ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_Ai_Type != 99) {
				AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1849);
				{
					char* pArg1 = "\"productionDate\":\"";
					char* pArg2 = F_time_to_date(FromModelTime(ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_At_TimeInSys, UNITSECONDS), am2_Vs_SimStart);
					char* pArg3 = "\"";

					fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%s%s\n", pArg1, pArg2, pArg3);
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1851);
				{
					char* pArg1 = "\"productionDate\":\"null\"";

					fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s\n", pArg1);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1852);
			{
				char* pArg1 = "\"model\":\"";
				char* pArg2 = ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_Type;
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%s%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1853);
			{
				char* pArg1 = "\"currentLocation\":\"";
				char* pArg2 = ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_CurrentLocation;
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%s%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1854);
			{
				char* pArg1 = "\"nextLocation\":\"";
				char* pArg2 = ValidPtr(am2_vldWriteJSON, 32, load*)->attribute->am2_As_NextLocation;
				char* pArg3 = "\"";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s%s%s\n", pArg1, pArg2, pArg3);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1855);
			{
				char* pArg1 = "}";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s\n", pArg1);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor, 1856);
			{
				char* pArg1 = "";

				fprintf(ffp_base_arc_RESULTS_JSON_out->fp, "%s\n", pArg1);
			}
		}
	}
LabelRet: ;
	if (am_localargs)
		xfree(am_localargs);
	AMDebuggerEndRoutine("SortingLogic.m", "Subroutine", "base.Sr_WriteJSONFile", am_Sr_WriteJSONFile, localactor);
	return retval;
} /* end of am_Sr_WriteJSONFile */

static int32
F_ConnectSocket()
{
	load* localactor = NULL;
	AMDebuggerBeginRoutine("SortingLogic.m", "Function", "base.F_ConnectSocket", localactor);
	{
		char* names[1];
		void* ptrs[1];
		char* (*valstrfuncs[1])(void*);
		
		AMDebuggerParams("base.F_ConnectSocket", F_ConnectSocket, localactor, 0, names, ptrs, valstrfuncs);
	}
	{
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1860);
			SetString(&am2_vsTemp, sock_MachineName());
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1861);
			if (StringCompare(am2_vsTemp, NULL) != 0) {
				{
					AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1862);
					sock_SetConnectBlocking(1);
				}
				{
					AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1863);
					am2_vsockSocket = sock_ConnectPort(am2_vsTemp, 3455);
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1864);
					if (sock_IsValid(am2_vsockSocket) == 1) {
						{
							AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1865);
							sock_SetNonBlocking(am2_vsockSocket, 0);
						}
						{
							AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1866);
							AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor);
							return 1;
						}
					}
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor, 1869);
			AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor);
			return 0;
		}
	}
LabelRet: ;
	AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_ConnectSocket", F_ConnectSocket, localactor);
} /* end of F_ConnectSocket */

static int
dispatch_F_ConnectSocket(int nParams, int* argTypes, void** argVals, void** retVal)
{
	static char buf[512];
	int retType;
	static int32 ret;

	if (nParams != 0) {
		message("The function F_ConnectSocket was called from the ActiveX interface with %d parameters, while 0 are required.", nParams);
		return 0;
	}
	ret = F_ConnectSocket();
	*retVal = &ret;
	return 1;
}

static char*
F_RequestMove(load* am_argLoad)
{
	load* localactor = NULL;
	AMDebuggerBeginRoutine("SortingLogic.m", "Function", "base.F_RequestMove", localactor);
	{
		char* names[1];
		void* ptrs[1];
		char* (*valstrfuncs[1])(void*);
		
		names[0] = "argLoad";
		ptrs[0] = &am_argLoad;
		valstrfuncs[0] = LoadPtr_valstrfunc;
		AMDebuggerParams("base.F_RequestMove", F_RequestMove, localactor, 1, names, ptrs, valstrfuncs);
	}
	{
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1873);
			if (sock_IsValid(am2_vsockSocket) == 0) {
				AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1874);
				AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor);
				return NULL;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1875);
			if (ValidPtr(am_argLoad, 32, load*)->attribute->am2_Ai_Type != 99) {
				AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1876);
				{
					char* pArg1 = F_time_to_date(FromModelTime(ValidPtr(am_argLoad, 32, load*)->attribute->am2_At_TimeInSys, UNITSECONDS), am2_Vs_SimStart);

					char* am_tmp;
					am_tmp = bufsprintf("%s", pArg1);
					SetString(&am2_vsTemp, am_tmp);
					EntityChanged(0x01000000);
				}
			}
			else {
				AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1878);
				{
					char* pArg1 = "null";

					char* am_tmp;
					am_tmp = bufsprintf("%s", pArg1);
					SetString(&am2_vsTemp, am_tmp);
					EntityChanged(0x01000000);
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1879);
			{
				char* pArg1 = "{";
				char* pArg2 = "\"messageType\":\"";
				char* pArg3 = ValidPtr(am_argLoad, 32, load*)->attribute->am2_As_Message;
				char* pArg4 = "\",";
				char* pArg5 = "\"tagID\":\"1111\"";
				char* pArg6 = "\"carrierNumber\":\"";
				char* pArg7 = rel_actorname(LdGetVehicle(ValidPtr(am_argLoad, 32, load*)), am_model.$sys);
				char* pArg8 = "\",";
				char* pArg9 = "\"dieNumber\":\"";
				int32 pArg10 = ValidPtr(am_argLoad, 32, load*)->attribute->am2_Ai_LotNo;
				char* pArg11 = "\",";
				char* pArg12 = "\"quantity\":\"";
				int32 pArg13 = ValidPtr(am_argLoad, 32, load*)->attribute->am2_Ai_CarQty;
				char* pArg14 = "\",";
				char* pArg15 = "\"productionDate\":\"";
				char* pArg16 = am2_vsTemp;
				char* pArg17 = "\",";
				char* pArg18 = "\"model\":\"";
				char* pArg19 = ValidPtr(am_argLoad, 32, load*)->attribute->am2_As_Type;
				char* pArg20 = "\",";
				char* pArg21 = "\"currentLocation\":\"";
				char* pArg22 = ValidPtr(am_argLoad, 32, load*)->attribute->am2_As_CurrentLocation;
				char* pArg23 = "\",";
				char* pArg24 = "\"nextLocation\":\"";
				char* pArg25 = ValidPtr(am_argLoad, 32, load*)->attribute->am2_As_NextLocation;
				char* pArg26 = "\"";
				char* pArg27 = "}";

				char* am_tmp;
				am_tmp = bufsprintf("%s%s%s%s%s%s%s%s%s%d%s%s%d%s%s%s%s%s%s%s%s%s%s%s%s%s%s", pArg1, pArg2, pArg3, pArg4, pArg5, pArg6, pArg7, pArg8, pArg9, pArg10, pArg11, pArg12, pArg13, pArg14, pArg15, pArg16, pArg17, pArg18, pArg19, pArg20, pArg21, pArg22, pArg23, pArg24, pArg25, pArg26, pArg27);
				SetString(&am2_vsMessage, am_tmp);
				EntityChanged(0x01000000);
			}
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1890);
			if (sock_SendString(am2_vsockSocket, am2_vsMessage) == 0) {
				{
					AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1891);
					am2_vsockSocket = NULL;
					EntityChanged(0x01000000);
				}
				{
					AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1892);
					AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor);
					return NULL;
				}
			}
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1894);
			SetString(&am2_vsReponse, sock_ReadString(am2_vsockSocket));
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1895);
			if (StringCompare(am2_vsReponse, NULL) == 0) {
				AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1896);
				AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor);
				return NULL;
			}
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1897);
			am2_vjsonStruct = cJSON_Parse(am2_vsResponse);
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1898);
			SetString(&am2_vsTemp, cJSON_GetObjectString(am2_vjsonStruct, "nextLocation"));
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1899);
			cJSON_Delete(am2_vjsonStruct);
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1900);
			am2_vjsonStruct = NULL;
			EntityChanged(0x01000000);
		}
		{
			AMDebugger("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor, 1901);
			AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor);
			return am2_vsTemp;
		}
	}
LabelRet: ;
	AMDebuggerEndRoutine("SortingLogic.m", "Function", "base.F_RequestMove", F_RequestMove, localactor);
} /* end of F_RequestMove */

static int
dispatch_F_RequestMove(int nParams, int* argTypes, void** argVals, void** retVal)
{
	static char buf[512];
	int retType;
	load* arg1;
	static char* ret;

	if (nParams != 1) {
		message("The function F_RequestMove was called from the ActiveX interface with %d parameters, while 1 are required.", nParams);
		return 0;
	}
	switch (argTypes[0]) {
	case 1:
		sprintf(buf, "%d", *(int*)argVals[0]);
		break;
	case 2:
		sprintf(buf, "%lg", *(double*)argVals[0]);
		break;
	case 3:
		strcpy(buf, (char*)argVals[0]);
		break;
	default:
		message("Internal Error: Unknown value type in dispatch function: %d param F_RequestMoveargLoad",
			argTypes[0]);
		return 0;
	}
	if (strcmp(buf, "null") == 0)
		arg1 = NULL;
	else
		arg1 = str2LoadPtr(buf);
	ret = F_RequestMove(arg1);
	strcpy(buf, VALIDSTRING(ret));
	*retVal = buf;
	return 3;
}



/* init function for SortingLogic.m */
void
model_SortingLogic_init(struct model_struct* data)
{
	((ProcSystem*)data->$sys)->modelReadyPtr = model_ready;
	data->am_Sr_StoreIn = am_Sr_StoreIn;
	data->am_pStorage->aprc = pStorage_arriving;
	data->am_pEmptyCarrierLaneSelect->aprc = pEmptyCarrierLaneSelect_arriving;
	data->am_P_WeldSched->aprc = P_WeldSched_arriving;
	data->am_P_SPO_Process->aprc = P_SPO_Process_arriving;
	data->am_P_SPO_Release->aprc = P_SPO_Release_arriving;
	data->am_Sr_SPOSeq = am_Sr_SPOSeq;
	data->am_pZoneMixCheck->aprc = pZoneMixCheck_arriving;
	data->am_P_ReleasePartial->aprc = P_ReleasePartial_arriving;
	data->am_P_StorageToBuffer->aprc = P_StorageToBuffer_arriving;
	((ConvStation*)data->am_pf_ohc.am_ST101)->spec.src.leaveprc = pf_ohc_ST101_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST115)->spec.src.leaveprc = pf_ohc_ST115_staleave;
	((ConvStation*)data->am_pf_ohc.am_ST134)->spec.src.arriveprc = pf_ohc_ST134_staarrive;
	((ConvPhotoeye*)data->am_pf_ohc.am_LS_13)->spec.clearedprc = pf_ohc_LS_13_photocleared;
	data->am_pf_ohc.am_WEBufferReceived->clearedprc = pf_ohc_WEBufferReceived_photocleared;
	((ConvPhotoeye*)data->am_pf_ohc.am_LS_17)->spec.clearedprc = pf_ohc_LS_17_photocleared;
	((ConvStation*)data->am_pf_ohc.am_W13)->spec.src.arriveprc = pf_ohc_W13_staarrive;
	((ConvStation*)data->am_pf_ohc.am_ST14)->spec.src.arriveprc = pf_ohc_ST14_staarrive;
	data->am_pf_ohc.am_RowSwitch->clearedprc = pf_ohc_RowSwitch_photocleared;
	data->am_P_Unload->aprc = P_Unload_arriving;
	data->am_P_PartialStorage->aprc = P_PartialStorage_arriving;
	data->am_P_PartialRelease->aprc = P_PartialRelease_arriving;
	data->am_P_LoadRBT->aprc = P_LoadRBT_arriving;
	data->am_Sr_WriteJSONFile = am_Sr_WriteJSONFile;
	data->am_F_ConnectSocket = F_ConnectSocket;
	data->am_F_RequestMove = F_RequestMove;
	data->am_F_ConnectSocket$func->dispatch = dispatch_F_ConnectSocket;
	data->am_F_ConnectSocket$func->func = F_ConnectSocket;
	data->am_F_RequestMove$func->dispatch = dispatch_F_RequestMove;
	data->am_F_RequestMove$func->func = F_RequestMove;
}

