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

