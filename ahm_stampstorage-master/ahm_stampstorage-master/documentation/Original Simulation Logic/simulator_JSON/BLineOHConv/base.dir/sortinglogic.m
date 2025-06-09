begin model ready function
	call F_ConnectSocket()
	return true
end

begin Sr_StoreIn procedure
  set Ai_Zone = 1
  set Ai_Status = 0
  set Ai_LotQty = 0
  
  if this load Ai_Type = Vi_PrevPartType(Ai_Press) then begin /* Check for space in previous row if it is not the first carrier of a run */
  	/*** CODE TO SET THE LAST ROW & ZONE FOR STORAGE IF THE LAST STORED ROW IS STILL OPEN ***/
 	if Vldlst_Type(Ai_Type) size > 0 and vldlstLanes(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) size > 0 and  
   	(vldlstLanes(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) size < 
    vlocptrLane(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) capacity) and
    vldlstLanes(Vldlst_Type(Ai_Type) last Ai_Zone,Vldlst_Type(Ai_Type) last Ai_Row) last Ai_Type = this load Ai_Type then begin
    	set Ai_Zone = Vldlst_Type(Ai_Type) last Ai_Zone  
    	set Ai_Row = Vldlst_Type(Ai_Type) last Ai_Row
    	set Ai_Status = 1
    	if viEmptyCarFlushQty(Ai_Zone, Ai_Row) > 0 then dec viEmptyCarFlushQty(Ai_Zone, Ai_Row) by 1    
    	set Vi_StorePrevType = Ai_Type 
    end
  end
  else if O_Starve(Ai_Type) current > 0 then begin  /******************* If part type is starved, then look through all zones for empty row first ******************/
  	set Ai_y = 5
  	while Ai_Zone <= viNumZones and Ai_Status = 0 do begin
  		set Ai_Row = 1
  		while Ai_Row <= viZoneLaneMax(viZonePref(Ai_Zone,Ai_Type)) and Ai_Status = 0 do begin
  			if vldlstLanes(viZonePref(Ai_Zone,Ai_Type),Ai_Row) size = 0 then begin
  				set Ai_Status = 1
  				set Ai_Zone = viZonePref(Ai_Zone,Ai_Type)
  			end
  			if Ai_Status = 0 then 
				inc Ai_Row by 1
		end
		if Ai_Status = 0 then inc Ai_Zone by 1
  	end
  end	

  set vi = 1
  while vi <= 12 and Ai_Status = 0 do begin
  	set Ai_Zone = viZonePref(viZoneSortPriority(vi),Ai_Type)
	set Ai_Row to 1

	if vsSortingRule(vi) = "Partial" then begin
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin	
      		if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then begin
        		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = Ai_Type and Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 and 
        		vlocptrLane(Ai_Zone,Ai_Row) capacity > vldlstLanes(Ai_Zone,Ai_Row) size - 
        		viEmptyCarFlushQty(Ai_Zone,Ai_Row) then begin
          		/*	if viSTRunGen(1) = 0 or viSTRunGen(2) = 0 or (Ai_Press = 1 and (Ai_Zone <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Row)) or (Ai_Press = 2 and (Ai_Zone <> Vldlst_Type(Vi_PrevType(1)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(1)) last Ai_Row)) then begin         */				        		
          				set Ai_Status = 1
		          		if viEmptyCarFlushQty(Ai_Zone,Ai_Row) > 0 then
	    	        		dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
        			/*end		*/		
        		end
      		end
      		if Ai_Status = 0 then
        		inc Ai_Row by 1
    	end	
	end	

	if vsSortingRule(vi) = "Empty" then begin
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin 	
      		if vldlstLanes(Ai_Zone,Ai_Row) size = 0 then 
		      	set Ai_Status = 1
	      	else inc Ai_Row by 1
	    end	
		
		if Ai_Status = 0 then set Ai_Row = 1
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin	/***Empty Carrier Check*****/
      		if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then begin
        		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = 99 then begin
	        		set viEmptyCarFlushQty(Ai_Zone,Ai_Row) to vldlstLanes(Ai_Zone,Ai_Row) size
          			set viEmptyCarPullQty(Ai_Zone,Ai_Row) to vldlstLanes(Ai_Zone,Ai_Row) size
          			for each vlptrEmpty in vldlstLanes(Ai_Zone,Ai_Row) do begin
	        			/*  inc viEmptyCarFlushQty(viZonePref(Ai_Zone,Ai_Type),Ai_Row) by 1 */
          				set vlptrEmpty Ai_Status = 1
          			end
	        		order vldlstLanes(Ai_Zone,Ai_Row) first from olEmptyHold
          			set vldlstLanes(Ai_Zone,Ai_Row) last Ai_Seq = 9999
          			set Ai_Status = 1
          			/* set Ai_Zone = viZonePref(Ai_Zone,Ai_Type) */
          			dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
        		end
      		end
      		if Ai_Status = 0 then
       	 		inc Ai_Row by 1
    	end
	end

	if vsSortingRule(vi) = "Mixed-Same" then begin
		while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin	
      		if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then begin
        		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = Ai_Type and Vi_ZoneMix(Ai_Zone,Ai_Row) = 1 and 
        		vlocptrLane(Ai_Zone,Ai_Row) capacity > vldlstLanes(Ai_Zone,Ai_Row) size - viEmptyCarFlushQty(Ai_Zone,Ai_Row) then begin
          			/*if viSTRunGen(1) = 0 or viSTRunGen(2) = 0 or (Ai_Press = 1 and (Ai_Zone <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Row)) or (Ai_Press = 2 and (Ai_Zone <> Vldlst_Type(Vi_PrevType(1)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(1)) last Ai_Row)) then begin */
        				set Ai_Status = 1
          				if viEmptyCarFlushQty(Ai_Zone,Ai_Row) > 0 then
	            			dec viEmptyCarFlushQty(Ai_Zone,Ai_Row) by 1
					/*end */       	
	        	end
	      	end
      		if Ai_Status = 0 then
        		inc Ai_Row by 1
	    end
   	end
	
	if vsSortingRule(vi) = "Mixed" then begin                            
    	set Vi_ZoneCapacity(1) to 1
    	set Vi_ZoneCapacity(2) to vlocptrLane(Ai_Zone,Ai_Row) capacity
    	while Ai_Row <= viZoneLaneMax(Ai_Zone) do begin 
    		if vldlstLanes(Ai_Zone,Ai_Row) size < Vi_ZoneCapacity(2) then begin
          		if viSTRunGen(1) = 0 or viSTRunGen(2) = 0 or Ai_Press = 1 and Vldlst_Type(Vi_PrevPartType(2)) size = 0 
          		or Ai_Press = 2 and Vldlst_Type(Vi_PrevPartType(1)) size = 0 then begin
    				set Vi_ZoneCapacity(1) to Ai_Row
    				set Vi_ZoneCapacity(2) to vldlstLanes(Ai_Zone,Ai_Row) size
    				set Ai_Status to 2
  				end          		
          		else begin
          			if (Ai_Press = 1 and (Ai_Zone <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(2)) last Ai_Row)) or (Ai_Press = 2 and (Ai_Zone <> Vldlst_Type(Vi_PrevType(1)) last Ai_Zone or 
          			Ai_Row <> Vldlst_Type(Vi_PrevPartType(1)) last Ai_Row)) then begin 
    					set Vi_ZoneCapacity(1) to Ai_Row
    					set Vi_ZoneCapacity(2) to vldlstLanes(Ai_Zone,Ai_Row) size
    					set Ai_Status to 2
    				end
  				end	
  			end
  			inc Ai_Row by 1
    	end
		if Ai_Status = 2 then begin
			set Ai_Row to Vi_ZoneCapacity(1)
			if Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 then begin
				set Vi_ZoneMix(Ai_Zone,Ai_Row) to 1
				inc C_ZoneMix(1) by 1
				dec C_ZoneMix(2) by 1
			end
		end
	end

	if Ai_Status = 0 then
		inc vi by 1
	if vi = 13 then begin
		wait to be ordered on olSpaceAvail
		set vi = 1
	end
  end
end

/****************************Lane Selection and Delivery To storage********************/
begin pStorage arriving procedure 
  if Ai_CarQty = 0 then begin
  	set Ai_CarQty = 1
  	inc viTotalStorageParts(Ai_Type) by 1
  end
  
  call Sr_StoreIn
  
  /*set At_TimeInSys = ac*/
  set Ai_Status = 0
  set Ai_Seq = 0
  inc Vi_ZoneSpace(Ai_Zone,1) by 1
  insert this load into vldlstLanes(Ai_Zone,Ai_Row)
  insert this load into Vldlst_Type(Ai_Type)
  set Vld_Previous to this load
  set Vi_PrevPartType(Ai_Press) to this load Ai_Type
  
  if viTrackStoreProcess = 1 then begin
  	/*print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreIn.out"*/
  	set As_Message to "STORE IN REQUEST"
  	set As_CurrentLocation to "5_13"
  	set As_NextLocation to vsStation(Ai_Zone,Ai_Row)
  	//set vldWriteJSON to this load
  	//call Sr_WriteJSONFile
  	call F_RequestMove(this load)
  end
 
/*  if O_Starve(Ai_Type) current > 0 then begin 
  /*	set O_Starve(Ai_Type) load list first Ai_y = 7878*/
  	order from O_Starve(Ai_Type) to continue   /** RELEASE STARVE LIST IF WAITING **/
  	
 	if ac > 86400 then begin
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
 	end  
 	
  end*/
  
  if Ai_Press = 1 then begin
  	set Ai_z to 2	/* Carriers have 2nd priority at 5_12 and 5_13 */
  	if pf_ohc:ST5_12 current = 1 then
  		wait to be ordered on oST5_12 
  	travel to pf_ohc:ST5_12
  	set Ai_z to 1
  	if pf_ohc:ST5_13 current = 1 then
  		wait to be ordered on oST5_13
  end
  else if Ai_Press = 2 then begin
  	if Ai_pi = 1 then begin
  		set Ai_z to 1	/* Carriers have 1st priority at 5_12 and 5_13 */
  		if pf_ohc:ST5_12 current = 1 then
  			wait to be ordered on oST5_12
  		travel to pf_ohc:ST5_12
  		if pf_ohc:ST5_13 current = 1 then
  			wait to be ordered on oST5_13
  	end
  	else if Ai_pi = 2 then begin
  		set Ai_z to 3	/* Carriers have 3rd priority at 5_13 */
  		travel to pf_ohc:ST5_11
  		if pf_ohc:ST5_13 current = 1 then
  			wait to be ordered on oST5_13
  	end
  end
 
  travel to pf_ohc:ST5_13
  set Ai_z to 0
  wait for vrStopTime(5) sec 
  travel to vlocptrLane(Ai_Zone,Ai_Row)
  if Ai_Status = 0 then 
  	wait to be ordered on O_Type(Ai_Type)

  while Ai_pi = 1234 do begin  /* Carriers recirculated to A-Line Storage Bay (Zone 3) */
 	if Ai_x = 1234 and R_ZoneExit(Ai_Zone) status = 1 then take down R_ZoneExit(Ai_Zone)
 	set Vld_ZoneTakeDown(Ai_Zone) to this load    
  	travel to pf_ohc:ST13
  /*	if Ai_z = 5454 then begin
  		set Ai_z to 0
  		bring up R_ZoneExit(Ai_Zone)
  	end*/
  	travel to pf_ohc:ST7_4	
  	if Ai_x = 1234 then begin
  		bring up R_ZoneExit(Ai_Zone)
  	/*	wait to be ordered on O_Recirc	*/	
  		set Vld_Recirc(1) = null
  	end
  	  	
/*	if Ai_y = 7878 then wait for 5 min
  	if Ai_y = 7878 then set Ai_y = 0 */		
  	  	
  	wait for vrStopTime(10) sec  	
  	set Ai_Zone to 3
  	set Ai_Status = 0

  	while Ai_Status = 0 do begin
  		if Ai_x = 1234 then begin
  			set Vi_Recirc(1) to Ai_Type
  			set Ai_x = 0
  		end
		else begin
  			if this load Ai_Type = Vi_Recirc(1) then begin
  				if vldlstLanes(3,Vi_Recirc(2)) size < vlocptrLane(3,Vi_Recirc(2)) capacity then begin
  					set Ai_Row to Vi_Recirc(2)
  					set Ai_Status = 1	
  				end	
  			end
  			else set Vi_Recirc(1) to Ai_Type
  		end	    		
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Partial Lane Check (Older Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 0 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type /*and vldlstLanes(3,Ai_Row) last Ai_LotNo <= this load Ai_LotNo*/ and vldlstLanes(3,Ai_Row) last = vldlstLanePull(3,Ai_Row) last /*vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size*/ then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Empty Lane Check */
  			inc Ai_Row by 1
  			if vldlstLanes(3,Ai_Row) size = 0 then 
  				set Ai_Status = 1
  		end
  		 				
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Empty Carrier Check */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and vldlstLanes(3,Ai_Row) last Ai_Type = 99 then begin
      	    		set viEmptyCarFlushQty(3,Ai_Row) to vldlstLanes(3,Ai_Row) size
       		   		set viEmptyCarPullQty(3,Ai_Row) to vldlstLanes(3,Ai_Row) size
       		   		for each vlptrEmpty in vldlstLanes(3,Ai_Row) do begin
         	 			set vlptrEmpty Ai_Status = 1
         	 		end
          			order vldlstLanes(3,Ai_Row) first from olEmptyHold
          			set vldlstLanes(3,Ai_Row) last Ai_Seq = 9999
          			set Ai_Status = 1
					dec viEmptyCarFlushQty(3,Ai_Row) by 1
       			end  		
  			end
  		end
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Mixed Lane Check - Current Part Type in Back (Older Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 1 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type /* and vldlstLanes(3,Ai_Row) last Ai_LotNo <= this load Ai_LotNo*/ and vldlstLanes(3,Ai_Row) last = vldlstLanePull(3,Ai_Row) last /*vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size*/ then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end 
  	
/*  	if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Partial Lane Check (Newer Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 0 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type and vldlstLanes(3,Ai_Row) last Ai_LotNo > this load Ai_LotNo and vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end
  		
  		if Ai_Status = 0 then set Ai_Row to 0
  		while Ai_Row < viZoneLaneMax(3) and Ai_Status = 0 do begin		/* Mixed Lane Check - Current Part Type in Back (Newer Lot in Lane) */
  			inc Ai_Row by 1
  			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
  				if vldlstLanes(3,Ai_Row) size > 0 and Vi_ZoneMix(3,Ai_Row) = 1 and vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type and vldlstLanes(3,Ai_Row) last Ai_LotNo > this load Ai_LotNo and vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size then begin
  					if vlocptrLane(3,Ai_Row) capacity > vldlstLanes(3,Ai_Row) size - viEmptyCarFlushQty(3,Ai_Row) then 
  						set Ai_Status = 1 
  				end
  			end
  		end */
  	
  		if Ai_Status = 0 then begin
  			set Ai_Row to 1		/* Mixed Lane Check */
  			set Vi_ZoneCapacity(1) to 1
   			set Vi_ZoneCapacity(2) to vlocptrLane(3,Ai_Row) capacity
  			while Ai_Row <= viZoneLaneMax(3) do begin	
    			if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
    				if vldlstLanes(3,Ai_Row) size < Vi_ZoneCapacity(2) /* and vldlstLanes(3,Ai_Row) last = vldlstLanePull(3,Ai_Row) last */ /*vlocptrLane(3,Ai_Row) current = vldlstLanes(3,Ai_Row) size */ then begin
    					set Vi_ZoneCapacity(1) to Ai_Row
    					set Vi_ZoneCapacity(2) to vldlstLanes(3,Ai_Row) size
    					set Ai_Status to 2
  					end
  				end
  				inc Ai_Row by 1		
  			end    	
		end
	
		if Ai_Status = 0 then begin	/* If all lanes are full, recirculate the parts in zone 3 */
			set Ai_Row to 1
			while Ai_Row <= viZoneLaneMax(3) and Ai_Status = 0 do begin
				if Vld_Previous Ai_Zone <> 3 or Vld_Previous Ai_Row <> this load Ai_Row then begin
					if vldlstLanes(3,Ai_Row) last Ai_Type = this load Ai_Type then begin
						if vldlstLanePull(3,Ai_Row) first Ai_x <> 10 then begin
							order vldlstLanePull(3,Ai_Row) first from O_Type(vldlstLanes(3,Ai_Row) first Ai_Type) to continue case backorder on O_Type(vldlstLanes(3,Ai_Row) first Ai_Type)
							set vldlstLanePull(3,Ai_Row) first Ai_pi to 1234
						/*	remove vldlstLanes(3,Ai_Row) first from Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type) 
							insert vldlstLanes(3,Ai_Row) first into Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type)	*/ 
							remove vldlstLanePull(3,Ai_Row) first from vldlstLanes(3,Ai_Row)
						end
						set Ai_Status to 1
					end
				end
				if Ai_Status = 0 then inc Ai_Row by 1
			end
			if Ai_Status = 0 then begin /* No rows with current part type in back, so recirc lane 1 */
				if Vld_Previous Ai_Zone = 3 and Vld_Previous Ai_Row = 9 then set Ai_Row = 1
				else set Ai_Row to 9
				if vldlstLanePull(3,Ai_Row) first Ai_x <> 10 then begin
					order vldlstLanePull(3,Ai_Row) first from O_Type(vldlstLanes(3,Ai_Row) first Ai_Type) to continue case backorder on O_Type(vldlstLanes(3,Ai_Row) first Ai_Type)
					set vldlstLanePull(3,Ai_Row) first Ai_pi to 1234
				/*	remove vldlstLanes(3,Ai_Row) first from Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type) 
					insert vldlstLanes(3,Ai_Row) first into Vldlst_Type(vldlstLanes(3,Ai_Row) first Ai_Type)	*/
					remove vldlstLanePull(3,Ai_Row) first from vldlstLanes(3,Ai_Row)
				end
				set Ai_Status to 1
			end
		end
	end  
  	  	
  	if Ai_Status = 2 then begin
  		set Ai_Row = Vi_ZoneCapacity(1) 
		if Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 then begin
			set Vi_ZoneMix(Ai_Zone,Ai_Row) to 1
			inc C_ZoneMix(1) by 1
			dec C_ZoneMix(2) by 1
		end
	end

  	set Vi_Recirc(2) to Ai_Row
  	set Ai_pi to 0
  	set Ai_Status = 0
  	set Ai_x to 0	
  	insert this load into vldlstLanes(Ai_Zone,Ai_Row)
  	order 1 load from oZoneMixCheck to continue
  	travel to vlocptrLane(Ai_Zone,Ai_Row)
  	if Ai_Status = 0 then
  		wait to be ordered on O_Type(Ai_Type)
  end
  	  
  remove this load from vldlstLanes(Ai_Zone,Ai_Row)
  remove this load from Vldlst_Type(Ai_Type)
  
  dec viTotalStorageParts(Ai_Type) by Ai_CarQty
  call Sr_OtherInv
  
  print "TrigQty->", viTriggerQty(Ai_Type), "RunQty->", viSTRunOutQty(Ai_Type), "Inv->",viTotalStorageParts(Ai_Type) to Lb_Inventory(Ai_Type)
  
  order 1 load from olSpaceAvail to continue
  
  if Ai_Type >= 20 then begin
  	if viTotalStorageParts(Ai_Type) < (viTriggerQty(Ai_Type)*1.1) and C_SPOControl(Ai_Type) current = 0 then begin
		if C_SPOControl(viModelV(Ai_Type)) current = 0 then begin	/* Check to see if SPO is already scheduled */
  			set viTypeHold to Ai_Type
  			set Ai_Type to viModelV(Ai_Type)
  			inc C_SPOControl(Ai_Type) by 1
  			clone 1 load to pSTRunControl
  			set Ai_Type to viTypeHold
  		end
	end
	if viTotalStorageParts(Ai_Type) <= viTriggerQty(Ai_Type)/4 then begin
		set viTypeHold to Ai_Type
  		set Ai_Type to viModelV(Ai_Type)	
		for each Vld_Crit in O_StampingPriority(Ai_Press) load list do begin
	  		if Vld_Crit Ai_NonSPO = 0 and Vld_Crit Ai_Type = this load Ai_Type and Vld_Crit priority = Vld_Crit Ai_StampingPriority then begin
  				set Vld_Crit priority to Vld_Crit Ai_StampingPriority/10
	  			order Vld_Crit from O_StampingPriority(Ai_Press) to O_StampingPriority(Ai_Press)
	  			insert Vld_Crit into vldlstCritInv(Vld_Crit Ai_Press)
  			end
  		end
		set Ai_Type to viTypeHold
	end
  end
  else begin
	if viTotalStorageParts(Ai_Type) < (viTriggerQty(Ai_Type)*1.1) and C_SPOControl(Ai_Type) current = 0 then begin
  		inc C_SPOControl(Ai_Type) by 1
	    clone 1 load to pSTRunControl
  	end
	if viTotalStorageParts(Ai_Type) <= viTriggerQty(Ai_Type)/4 then begin  /* Check for parts below 1 hour inventory */
		for each Vld_Crit in O_StampingPriority(Ai_Press) load list do begin
	  		if Vld_Crit Ai_NonSPO = 0 and Vld_Crit Ai_Type = this load Ai_Type and Vld_Crit priority = Vld_Crit Ai_StampingPriority then begin
  				set Vld_Crit priority to Vld_Crit Ai_StampingPriority/10
	  			order Vld_Crit from O_StampingPriority(Ai_Press) to O_StampingPriority(Ai_Press)
	  			insert Vld_Crit into vldlstCritInv(Vld_Crit Ai_Press)
  			end
  		end
 	 end
  end

  send to P_StorageToBuffer      
end


/***********************************Process for Empty Carrier Storage Selection***************************/
begin pEmptyCarrierLaneSelect arriving procedure
  travel to pf_ohc:ST5_13  
  set Ai_Status = 0
  set Ai_x = 1

  /** CODE TO SEND EMPTY CARRIERS TO PREV LANE IF SPACE AVAILABLE **/
  if olEmptyHold current > 0 and vldlstLanes(olEmptyHold load list last Ai_Zone,olEmptyHold load list last Ai_Row) size > 0 and  
  (vldlstLanes(olEmptyHold load list last Ai_Zone,olEmptyHold load list last Ai_Row) size < 
   vlocptrLane(olEmptyHold load list last Ai_Zone,olEmptyHold load list last Ai_Row) capacity) then begin
    set Ai_Zone = olEmptyHold load list last Ai_Zone  
    set Ai_Row = olEmptyHold load list last Ai_Row
    set Ai_Status = 1
  end

  while Ai_Status = 0 do
  begin
  while Ai_x <= viNumZones and Ai_Status = 0 do
  begin
    set Ai_Zone to Vi_EmptyZone(Ai_x)
    set Ai_Row = 1
    while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do		/***Partial Row Check*****/
    begin
      if vldlstLanes(Ai_Zone,Ai_Row) size > 0 then
      begin
        if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = Ai_Type and
        vlocptrLane(Ai_Zone,Ai_Row) capacity > vldlstLanes(Ai_Zone,Ai_Row) size then
        begin
          set Ai_Status = 1
        end
      end
      if Ai_Status = 0 then
        inc Ai_Row by 1
    end
    if Ai_Status = 0 then
      set Ai_Row = 1
    while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do		/***Empty Row Check*****/
    begin
      if vldlstLanes(Ai_Zone,Ai_Row) size = 0 then
          begin
            set Ai_Status = 1
          end
      if Ai_Status = 0 then
        inc Ai_Row by 1
    end
	
	if Ai_Status = 0 then inc Ai_x by 1
  end
  
  set Ai_x to 1
 while Ai_x <= viNumZones and Ai_Status = 0 do begin	/*** Check for lane full of carriers and kick the one in front ***/
 	set Ai_Zone to Vi_EmptyZone(Ai_x)
 	set Ai_Row = 1
 	while Ai_Row <= viZoneLaneMax(Ai_Zone) and Ai_Status = 0 do begin
 		if vldlstLanes(Ai_Zone,Ai_Row) last Ai_Type = 99 then begin
 			if vldlstLanes(Ai_Zone,Ai_Row) size = vlocptrLane(Ai_Zone,Ai_Row) capacity then begin
 				set vldlstLanes(Ai_Zone,Ai_Row) first Ai_Seq = 9999
 				order vldlstLanes(Ai_Zone,Ai_Row) first from olEmptyHold case backorder on olEmptyHold
 				remove vldlstLanes(Ai_Zone,Ai_Row) first from vldlstLanes(Ai_Zone,Ai_Row)
 				set Ai_Status = 1 
 			end
 		end
 		if Ai_Status = 0 then inc Ai_Row by 1
 	end
   	if Ai_Status = 0 then inc Ai_x by 1
  end
   
    if Ai_Status = 0 then begin
      wait to be ordered on olSpaceAvail
      /* set Ai_Zone = Vi_EmptyZone(1) */
      set Ai_x to 1
    end
  end
  
  set Ai_Status = 0
  
  insert this load into vldlstLanes(Ai_Zone,Ai_Row)
  travel to vlocptrLane(Ai_Zone,Ai_Row)
  if Ai_Status = 0 then
  begin
  	wait to be ordered on olEmptyHold
    
    if R_ZoneExit(Ai_Zone) status = down  /* Prevents empty carriers from blocking the system */
    	then bring up R_ZoneExit(Ai_Zone)
    
    get R_ZoneExit(Ai_Zone)
   	free R_ZoneExit(Ai_Zone)
  	take down R_ZoneExit(Ai_Zone)
  	set Vld_ZoneTakeDown(Ai_Zone) to this load	
  end
  
  if Ai_Seq = 9999 then begin
    bring up R_ZoneExit(Ai_Zone)
    if oEmptyBufferCleanUp current > 0 then 
    	order 1 load from oEmptyBufferCleanUp to continue 
   end
  
  remove this load from vldlstLanes(Ai_Zone,Ai_Row)
  

  if Ai_Seq = 9999 and viEmptyCarFlushQty(Ai_Zone,Ai_Row) > 0 then set viEmptyCarFlushQty(Ai_Zone,Ai_Row) = 0


  order 1 load from olSpaceAvail to continue
  
  /*travel to pf_ohc:W36*/
  travel to pf_ohc:ST13
  
  if Ai_Seq = 9999 and Vi_ZoneEmptyFlush = 1 then set Vi_ZoneEmptyFlush = 0
  
  send to pEmptyCarrierReturn

end


/*******************************************************************************************/

/*******************************************************************************************/
/**  THIS IS WELD SCHED PROCESS. THIS PROCESS READS THE WELD PRODUCTION SCHEDULE &        **/
/**  CLONES TO APPROPRIATE STORAGE RELEASE PROCESS.                                       **/
/**                          PROCINDEX = 1 for LINE #1                                    **/
/**                          PROCINDEX = 2 for LINE #2                                    **/
/**  LOADS F/EACH PROCINDEX IS CLONED F/INITIALIZATION PROCESS. THE LOADS WILL STAY TILL  **/
/**  END OF SIMULATION.  WELD SCHEDULE SHOULD BE COPIED AS 2 SCHE FILES FOR LINE 1&2      **/
/*******************************************************************************************/
begin P_WeldSched arriving procedure
  call SR_AttributeReset                                         /** RESET ALL ATTRIBUTES **/  
  if procindex = 1 then 
    open "base.arc/DATA/weld1prod.dat" for reading save result as Vflptr_WeldSched(procindex)
  else if procindex = 2 then 
    open "base.arc/DATA/weld2prod.dat" for reading save result as Vflptr_WeldSched(procindex)
  read Vs_junk from Vflptr_WeldSched(procindex) with delimiter "\n"       /** FILE HEADER **/
  read Vs_junk from Vflptr_WeldSched(procindex) with delimiter "\n"       /** FILE HEADER **/
  read Ai_LotQty from Vflptr_WeldSched(procindex) with delimiter "\t"
  while Ai_LotQty > 0 do begin /**------- END OF DATA FILE LOOP **/
    read Vs_junk, Vs_junk, Vs_junk, Vs_ProdType(1,procindex), Vs_ProdType(2,procindex) from Vflptr_WeldSched(procindex) with delimiter "\t"
    /** SET PROPER SIDE DESIGNATION TO TYPE ATTRIBUTE & SET WELD LINE # **/    
    if procindex = 1 then begin
      set this load Ai_Line = 1   
      set Ai_pi to 1
	  set As_Type to Vs_ProdType(1,procindex)
      clone to P_SPO_Release(1)                 /** FOR RELEASING THE SPO FROM STORAGE **/
      clone to P_SPO_Process(1)
      set Ai_pi to 2
      set this load As_Type = Vs_ProdType(2,procindex)     
      clone to P_SPO_Release(2)                 /** FOR RELEASING THE SPO FROM STORAGE **/  
      clone to P_SPO_Process(2)
      wait to be ordered on O_SPO_ReleaseComplete(1) /** WAIT FOR SPO RELEASE TO COMPLETE **/  
      wait to be ordered on O_SPO_ReleaseComplete(2) /** WAIT FOR SPO RELEASE TO COMPLETE **/  
    end

    else if procindex = 2 then begin   
      set this load Ai_Line = 2   
      set Ai_pi to 3
      set this load As_Type = Vs_ProdType(1,procindex)
      clone to P_SPO_Release(3)                 /** FOR RELEASING THE SPO FROM STORAGE **/     
      clone to P_SPO_Process(3)
      set Ai_pi to 4
      set this load As_Type = Vs_ProdType(2,procindex)
      clone to P_SPO_Release(4)                 /** FOR RELEASING THE SPO FROM STORAGE **/
      clone to P_SPO_Process(4)    
      wait to be ordered on O_SPO_ReleaseComplete(3) /** WAIT FOR SPO RELEASE TO COMPLETE **/  
      wait to be ordered on O_SPO_ReleaseComplete(4) /** WAIT FOR SPO RELEASE TO COMPLETE **/
    end  
    read Ai_LotQty from Vflptr_WeldSched(procindex) with delimiter "\t"  
  end  /**--------------------------------------------------------- END OF DATA FILE LOOP **/
  close Vflptr_WeldSched(procindex)
  set Vflptr_WeldSched(procindex) to null     
  send to P_WeldSched(procindex)                     /** SEND BACK TO REPEAT THE SCHEDULE **/
end  /**************************************************************************************/


/*******************************************************************************************/
/**  THIS IS SPO BUILD PROCESS.  THIS PROCESS READS THE WELD PRODUCTION SCHEDULE & 
     CLONES TO APPROPRIATE STORAGE RELEASE PROCESS.  
                          PROCINDEX = 1 for LINE #1 LH
                          PROCINDEX = 2 for LINE #1 RH
                          PROCINDEX = 3 for LINE #2 LH
                          PROCINDEX = 4 for LINE #2 RH                          
     LOADS FOR EACH PROCINDEX IS CLONED FROM P_Init PROCESS.  THE LOADS WILL STAY TILL 
     END OF SIMULATION.  WELD SCHEDULE SHOULD BE COPIED AS 4 SCHE FILES FOR MODELING      **/ 
/*******************************************************************************************/
begin P_SPO_Process arriving procedure /**----------------------------- SPO BUILD PROCESS **/
  set Vld_SPO(procindex) to this load
  
  move into Q_SPO(procindex)
  get 1 of R_SPO(procindex)

  call SR_TypeIndex                                             /** TO SET THE TYPE INDEX **/

  /** BEFORE STAR NEW RUN, IF TYPE IS NEW & CARRIER @ UNLOAD HAS SPOs LEFT THEN PERFORM
      PARTIAL STORAGE SUB-ROUTINE LOGIC                                                   **/
  if Ai_Type <> Vi_PrevType(procindex) and Vldptr_Unload(procindex) <> null and 
  Vldptr_Unload(procindex) Ai_CarQty > 0 then begin     
  	if Vo_SPO(procindex,1) current > 0 and 
    (Vldptr_Unload(procindex) = Vo_SPO(procindex,1) load list first or
    Vldptr_Unload(procindex) = Vo_SPO(procindex,1) load list last ) then begin
     	set Vldptr_Unload(procindex) Ai_Status = 8888
      	order Vldptr_Unload(procindex) from Vo_SPO(procindex,1) to continue     
    end
    set Vldptr_Unload(procindex) = null
  end  
  
  /** IF PARTIAL BUFFER EXISTS, CLONE A LOAD TO PARTIAL RELEASE PROCESS **/    
  if Vldlst_Partial(procindex, Ai_Type) size > 0 then begin
    clone to P_PartialRelease(procindex) nlt L_invis
    wait to be ordered on O_PartialRelease(procindex)
  end
  if P_SPO_Process(procindex) total =1 then wait to be ordered on olWarmUp   /** WAIT F/WARM UP **/

  set Vi_ProdCount(procindex) =0                                     /** RESET PRODUCTION STATS **/
  print "SPO #", procindex,"processing", As_Type,Vi_ProdCount(procindex), "of", Ai_LotQty to Lb_SPO(procindex)
  set Vi_LotQty(procindex) = Ai_LotQty     

  set pf_ohc:ST108 capacity to 4
  set Vi_SPO(procindex) to Ai_LotQty
  
  if Ai_Type <> Vi_PrevType(procindex) then begin
  	if procindex < 3 then begin
    	if oWeldChangeover(1) current = 0 then
    		wait to be ordered on oWeldChangeover(1)
    	else begin
    		take down R_Weld(1)
    		set R_Weld(1) active state to "Changeover"
    		get R_WeldShift(1)
    		wait for Vr_WeldChangeoverTime(1) min
    		free R_WeldShift(1)
    		bring up R_Weld(1)
    		set R_Weld(1) active state to "Production"
    		order 1 load from oWeldChangeover(1) to continue
    	end
    end
    else begin
    	if oWeldChangeover(2) current = 0 then
    		wait to be ordered on oWeldChangeover(2)
    	else begin
    		take down R_Weld(2)
    		set R_Weld(2) active state to "Changeover"
    		get R_WeldShift(2)
    		wait for Vr_WeldChangeoverTime(2) min
    		free R_WeldShift(2)
    		bring up R_Weld(2)
    		set R_Weld(2) active state to "Production"
    		order 1 load from oWeldChangeover(2) to continue
    	end
  	end
  end  
  
  while Ai_LotQty >0 do begin /**--------------------------------------- WHILE LOOP FOR LOT QTY **/

    if Vldptr_Unload(procindex) = null then begin       /** IF NO CAR @ UNLOAD --> RELEASE CARS **/
      if O_WeldBuffer(procindex) current > 0 then begin  /**------------------- IF CARS WAITING **/
          order from O_WeldBuffer(procindex) to continue
      end/**------------------------------------------------------------------- IF CARS WAITING **/
      else begin/**---------------------------------------------------------- IF CARS NOT THERE **/
        if procindex < 3 then begin
        	take down R_LineStarved(1)
        	take down R_Weld(1)
        	set R_Weld(1) active state to "Starved"
        	set R_LineStarved(1) active state to "Starved"
        end
        else begin
        	take down R_LineStarved(2)
        	take down R_Weld(2)
        	set R_Weld(2) active state to "Starved"
        	set R_LineStarved(2) active state to "Starved"
        end
        wait to be ordered on O_WeldBufferStarve(procindex)
        wait for .1 sec
        if O_WeldBuffer(procindex) load list first Ai_Type = this load Ai_Type then
        	order from O_WeldBuffer(procindex) to continue    
        if procindex < 3 then begin
        	bring up R_LineStarved(1)	
        	bring up R_Weld(1)
        	if R_Weld(1) active state = Starved then set R_Weld(1) active state to "Production"
        end
        else begin
        	bring up R_LineStarved(2) 
        	bring up R_Weld(2)    	    
        	if R_Weld(2) active state = Starved then set R_Weld(2) active state to "Production"
        end
      end  /**--------------------------------------------------------------- IF CARS NOT THERE **/          
    end  /**-------------------------- IF NO CAR @ UNLOAD --> RELEASE CARS FROM THE WELD BUFFER **/ 

    else begin  /**--------------------------------------------------- IF THERE IS CAR @ UNLOAD **/
      if Vldptr_Unload(procindex) Ai_CarQty < 5 or Vloc_B4Unload(procindex) remaining space > 0 then begin
        if O_WeldBuffer(procindex) current > 0 and 
           O_WeldBuffer(procindex) load list first Ai_Type = this load Ai_Type then
          order from O_WeldBuffer(procindex) to continue      
      end    
    end   /**--------------------------------------------------------- IF THERE IS CAR @ UNLOAD **/
   
    order 2 loads from Vo_SPO(procindex,1) 
      case backorder on Vo_SPO(procindex,1)     /** LOAD SIGNAL RBT&UNLOAD **/ 
    wait to be ordered on Vo_SPO(procindex,2)  /** WAIT FOR RBT TO LOAD **/    
    if this load Ai_Type <> K_RBT:Robot(procindex+2) load list first Ai_Type then begin
      print "TYPE do not Match between Rbt SPO & Required", this load Ai_Type,"-->",
            K_RBT:Robot(procindex+2) load list first Ai_Type,"in line", procindex to message
    /*  dec C_Error by 1  */    
    end
    set this load type to vlt_LoadType(Ai_Type)
    set this load color to vclr_LoadColor(Ai_Type)    
    /** CHECK FOR TYPE MATCH HERE **/
    wait to be ordered on Vo_SPO(procindex,3)  /** WAIT FOR RBT TO CLEAR **/  
    
    if procindex < 3 then begin 
    	get 1 of R_Weld(1)
    	get R_LineStarved(1)
    end
    else begin
    	get 1 of R_Weld(2)
    	get R_LineStarved(2) 
    end
    
    wait for Vr_SPOCycleTime(procindex) sec                            /** SPO CYCLE TIME **/
    
    if procindex < 3 then begin 
    	free R_Weld(1)
    	free R_LineStarved(1)       
    end
    else begin
    	free R_Weld(2)
    	free R_LineStarved(2)      
    end
    
    inc Vi_ProdCount(procindex) by 1                     /** UPDATE TYPE PRODUCTION STATS **/
    inc Vi_shiftProd(procindex) by 1                     /** UPDATE SHIFT PRODUCTION STATS **/   
    print "SPO #", procindex,"processing",As_Type, Vi_ProdCount(procindex), "of", Vi_LotQty(procindex), 
          ">>", Vi_shiftProd(procindex) to Lb_SPO(procindex)     
    dec Ai_LotQty by 1                                       /** UPDATE REMAINING LOT QTY **/
    set this load type to L_invis
    
    if procindex < 3 then begin
    	if Vi_shiftProd(procindex) = (Vi_WeldOutputTarget(1) + Vi_WeldGap(procindex)) then
    		wait to be ordered on O_WeldOutputTarget 
    end
    else begin
    	if Vi_shiftProd(procindex) = (Vi_WeldOutputTarget(2) + Vi_WeldGap(procindex)) then
    		wait to be ordered on O_WeldOutputTarget
    end	
    
    if Vi_ProdCount(procindex) =1 then begin
      if procindex < 3 then begin
        if O_Synch(1) current =1 then order all from O_Synch(1) to continue
        else wait to be ordered on O_Synch(1)      
      end
      else if procindex > 2 then begin
        if O_Synch(2) current =1 then order all from O_Synch(2) to continue
        else wait to be ordered on O_Synch(2)      
      end
    
    end
    
	set Vi_SPO(procindex) to Ai_LotQty
  end  /**-------------------------------------------------------------------- WHILE LOOP **/
  set pf_ohc:ST108 capacity = 1
  set Vi_PrevType(procindex) to Ai_Type
  free R_SPO(procindex)
end /**---------------------------------------------------------------- SPO BUILD PROCESS **/
/*******************************************************************************************/

/*******************************************************************************************/
/**  THIS IS SPO RELEASE PROCESS.  LOADS WILL BE CLONED FROM WELD SCHED PROCESS.  THIS 
     PROCESS FIRST CHECK THE PARTIAL BUFFER, ADJUST QRELEASE QTY AND REQUEST RELEASE FROM
     THE STORAGE LOCATIONS.  Vldlst_Partial(4) HAS SAME INDEX REFERENCE AS THE PROCINDEX. 
                          PROCINDEX = 1 for LINE #1 LH
                          PROCINDEX = 2 for LINE #1 RH
                          PROCINDEX = 3 for LINE #2 LH
                          PROCINDEX = 4 for LINE #2 RH
     THE LOGIC FIRST CHECKS THE PARTIAL BUFFER AND MAKE ADJUSTMENT TO THE QUANTITY TO BE 
     RELEASEED FROM THE STORAGE LINES.  BEFORE LOOKING INTO STORAGE LINES, IT WILL WAIT
     FOR THE WELD LINE BUFFER LINES TO SIGNAL HOW MANY CARRIERS CAN BE RELEASED FROM THE
     STORAGE.  ONCE SIGNALED, IT WILL SEE IF THE TYPE IS IN THE SYSTEM (Vldlst), IF NONE,
     THEN WAIT ON THE STARVE LIST.  IF AVAILABLE, THEN FOR EACH OF THE CARRIERS NEEDED,
     CHECK IF THEY ARE ON THE TYPE ORDER LIST AND ORDER THEM IF THEY ARE ON THE LIST      **/
/*******************************************************************************************/

begin P_SPO_Release arriving procedure /**--------------------------- SPO RELEASE PROCESS **/
	call F_getID()
	call SR_TypeIndex                                             /** TO SET THE TYPE INDEX **/
  	set Ai_Seq =0
  	if this load Ai_Type < 1 or this load Ai_Type > 30 then begin
	    print "ERROR in setting Ai_Type:Check", this load, this load current to message
    	dec C_Error by 1
  	end
  	/*******   ONLY CHECK FOR QTY IN PARTIAL RELEASE TO ADJUST STORAGE RELEASE QTY    *******/
  	if Vldlst_ReleasePartial(Ai_Line,Ai_Type) size > 0 then begin  /** CHECK F/PARTIAL RELEASE QTYS **/
    	get Vrsrc_ReleasePartial(Ai_Line,Ai_Type)  
    	for each Vld_SPO_Release(procindex) in Vldlst_ReleasePartial(Ai_Line,Ai_Type) do begin
      		if Vld_SPO_Release(procindex) Ai_CarQty > 0 then begin 
        		set this load Ai_LotQty = this load Ai_LotQty - Vld_SPO_Release(procindex) Ai_CarQty
        		set Vld_SPO_Release(procindex) Ai_CarQty = 0
        		order from Vo_ReleasePartial(Ai_Line,Ai_Type) case backorder on Vo_ReleasePartial(Ai_Line,Ai_Type) 
      		end
      		else begin
		        print "0 QTY @ Partial Buffer: Please Check", Vld_SPO_Release, Vld_SPO_Release current,
    		    Vld_SPO_Release(procindex) vehicle current location to message
        		dec C_Error by 1
      		end
    	end
    	free Vrsrc_ReleasePartial(Ai_Line,Ai_Type)     
  	end /**-------------------------------------------- CHECK F/ PARTIAL RELEASE QUANTITIES **/

  	set Vld_ReleaseCount(procindex) to this load	
  	while Ai_LotQty > 0 do begin /**------------------------------- TOTAL RELEASE ORDER QTY **/
	     /** BEFORE RELEASING NEED TO BE IN SEQUENCE TO ENSURE NOT MIX CARRIERS BEFORE SENT
    	     SENT TO THE WELD BUFFERS SINCE WELD BUFFERS DO NOT HAVE RE-CIRC CAPABILITIES     **/
     	wait to be ordered on O_ST13_SPO_Seq(procindex)          /** TO BE ORDERED AT ST-13  **/

	    /** IF THE BUFFER SPACE LOAD LIST IS ZERO -->> NO SIGNAL FROM THE BUFFER THEN WAIT 
        	TILL THE BUFFER ORDERS.  IF THERE ARE REMAINING SPACE TO BE FILLED FROM PREVIOUS
        	ORDERS THEN RELEASE CARRIER FOR NEW SCHED                                         **/
    
    	wait to be ordered on O_BufferSpace(procindex) /** WAIT F/ BUFFER SPACE @ WELD LINE **/
    	if Vi_SPO_QtyRelease(procindex) = 0 then begin
    		wait to be ordered on O_BufferSpace(procindex) 
    	end   
    	if Vi_SPO_QtyRelease(procindex) > Ai_LotQty then set Vi_SPO_QtyRelease(procindex) = Ai_LotQty
    	set this load Ai_Seq = Vi_SPO_QtyRelease(procindex)        /** SPO QTY TO BE RELEASED **/
    	if Ai_Seq = 0 and ac > 86400 then begin
      		print "ERROR: SPO QTY to be released for the buffer is not specified", procindex, Ai_Seq to Lb_SPORelease(procindex)
      		dec C_Error by 1
    	end	
    	print "Line=", Ai_Line, "-", procindex, As_Type, "Lot =", Ai_LotQty, "RlsQty=", Ai_Seq to message              

    	if procindex < 3 then get R_Line(1)
    	else get R_Line(2)
		get R_SPO_Release
  
    	set Ai_CarQty = 0   
    	set Vi_CheckQty(Ai_Type) = 0           /** FOR CHECKING QTY AVAILABLE VS REQUIRED QTY **/    
    	set Vld_ReleasePrev(procindex) = null   /** RESET PREV LOAD PTR TO NULL F/NEW RELEASE **/   
    	set Vi_PrevZone(procindex) = 0
	    
	    set Vi_Seq to Ai_Seq
	    set Vld_SortMarker = null
    
		if procindex < 3 then set Ai_Line = 1
		else set Ai_Line = 2
		set Ai_Side = procindex
	
		if viStorageInit(2) = 0 then
			wait to be ordered on oStorageInit
		
		set viCarrierReleaseCount(procindex,2) to 0
		while Ai_Seq > 0 do begin   /**-------------------- SPO QTY TO BE RELEASED WHILE LOOP **/    
    		call Sr_SPOSeq
    		inc viCarrierReleaseCount(procindex,2) by Vldlst_Release(procindex) size 
    		set Vldlst_Release(procindex) last Ai_pi to 4444
			for each Vld_Release(procindex) in Vldlst_Release(procindex) do begin
				if Vld_Release(procindex) Ai_Status <> 1 then begin
        	    	if Vld_ReleasePrev(procindex) <> null then begin  /**--- PREV LD LST NOT NULL **/
						if Vld_ReleasePrev(procindex) Ai_Zone <> this load Ai_Zone then begin
							get R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			free R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			take down R_ZoneExit(Vld_Release(procindex) Ai_Zone)              
	                		set Vld_ZoneTakeDown(Vld_Release(procindex) Ai_Zone) to this load
							print "D", Vld_Release(procindex) to Lb_ZoneExit(Ai_Zone)       
						end
            		end /**------------------------------------------------- PREV LD LST NOT NULL **/
            		else if Vld_ReleasePrev(procindex) = null then begin  /** PREV LD LST IS NULL **/
              			/* set Ai_CarQty = 0 */
              			if Vi_PrevZone(procindex) > 0 and R_ZoneExit(Vi_PrevZone(procindex)) status is down then begin
                			bring up R_ZoneExit(Vi_PrevZone(procindex))             
              				get R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			free R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			take down R_ZoneExit(Vld_Release(procindex) Ai_Zone)
                			set Vld_ZoneTakeDown(Ai_Zone) to this load
                			print "D", Vld_Release(procindex) to Lb_ZoneExit(Vld_Release(procindex) Ai_Zone)
              			end 
            		end   /**------------------------------------------------ PREV LD LST IS NULL **/        
            		set Vld_Release(procindex) Ai_Status =1 
            		insert Vld_Release(procindex) into Vldlst_ZoneExit(Ai_Zone)  
            		if procindex <3 then set Vld_Release(procindex) Ai_Line=1         /** WE LN # **/
            		else if procindex>2 then set Vld_Release(procindex) Ai_Line=2
            		set Vld_Release(procindex) Ai_Side = procindex               /** WE LN SIDE # **/              
            		inc Vi_CheckQty(Ai_Type) by Vld_Release(procindex) Ai_CarQty
            		dec Ai_Seq by Vld_Release(procindex) Ai_CarQty
            		dec Ai_LotQty by Vld_Release(procindex) Ai_CarQty            
            		inc Ai_CarQty by 1
            		if Ai_CarQty = 1 then begin
 	            		get R_ZoneExit(Vld_Release(procindex) Ai_Zone)
    	        		free R_ZoneExit(Vld_Release(procindex) Ai_Zone)
        	    		take down R_ZoneExit(Vld_Release(procindex) Ai_Zone)
            			set Vld_ZoneTakeDown(Ai_Zone) to this load
              			print "D", Vld_Release(procindex) to Lb_ZoneExit(Vld_Release(procindex) Ai_Zone)
            		end
            		print "Line=", Ai_Line, "-", procindex, As_Type, "Lot =", Ai_LotQty, "RlsQty=", Ai_Seq to Lb_SPORelease(procindex)              
			
					/*	order Vld_Release(procindex) from O_Type(Ai_Type) case backorder on O_Type(Ai_Type)    */     
  
		        	if O_Type(Ai_Type) current > 0 then begin /**------------ LOAD ON ORDER LIST  **/
    		      		for each Vld_OL(procindex) in O_Type(Ai_Type) load list do begin  /** OL LP **/
            	   			if Vld_Release(procindex) = Vld_OL(procindex) then begin 
                				order Vld_Release(procindex) from O_Type(Ai_Type) case backorder on O_Type(Ai_Type)
	               			end                 
    	          		end /**-------------------------------------------------------------- OL LP **/             
        	    	end   /**------------------------------------------------ LOAD ON ORDER LIST  **/              
  					remove Vld_Release(procindex) from Vldlst_Release(Ai_pi)
  						
	            	set Vld_ReleasePrev(procindex) = Vld_Release(procindex)   /** SET PREV LD PTR **/
			    	set Vi_PrevZone(procindex) =  Vld_ReleasePrev(procindex) Ai_Zone            
        	    	if  Ai_Seq <= 0 then begin
	            		set Vld_Release(procindex) Ai_Seq = 9999  /**-------------- LAST OF THE LOT **/
    	          		set Ai_Zone = Vld_Release(procindex) Ai_Zone
        	      		break
            		end                                
      					
      			end  /**--------------------------------------------- IF CARS ARE AVAILABLE **/      
			end /**---------------------------------------------------------- FOR EACH LOOP **/
			wait to be ordered on O_ReleaseSeq(procindex)
			order 1 load from oZoneMixCheck to continue
		end  /**----------------------------------------- SPO QTY TO BE RELEASED WHILE LOOP **/   
    
	    if O_Starved current > 0 then order 1 load from O_Starved to continue 
   		/* if Vld_Recirc <> null then order 1 load from O_Recirc to continue case backorder on O_Recirc	*/
         
	    wait to ordered on O_Release(procindex)  /** WAIT BATCH RELEASE COMPLETE SIGNAL ST-13 **/   
	    if procindex < 3 then free R_Line(1)
	    else free R_Line(2)
    	free R_SPO_Release
    	wait to be ordered on oST13Inspect(procindex)
    	wait for 0
  	end /**-------------------------------------------------------- TOTAL RELEASE ORDER QTY **/
 
 	if Ai_LotQty < 0 then begin            /** TO KEEP TRACK OF THE PARTIAL QTYS AT RELEASE **/
 		set Vi_ReleasePartial(Ai_Line,Ai_Type) = -1* Ai_LotQty
    	clone to Vproc_ReleasePartial(Ai_Line,Ai_Type)   
    end	

  	set Vld_ReleaseCount(procindex) to null

	/*order 1 load from oZoneMixCheck to continue*/
  	order from O_SPO_ReleaseComplete(procindex) case backorder on O_SPO_ReleaseComplete(procindex) 
end  /**------------------------------------------------------------- SPO RELEASE PROCESS **/
/*******************************************************************************************/

begin Sr_SPOSeq
	set Ai_Status = 0
	set vix = 1
	set viPrevLotNo = 0
	set viClearABayFlag(Ai_Side) to 0
	
	if Vldlst_Type(Ai_Type) size = 0 then begin
		free R_SPO_Release	
	    set Vr_WeldStarveTime(Ai_Line) to ac
		wait to be ordered on O_Starve(Ai_Type)
		tabulate (ac - Vr_WeldStarveTime(Ai_Line))/60 in T_WeldStarve(Ai_Line)
		wait for 5 min
		get R_SPO_Release
		set Vld_ReleasePrev(Ai_Side) = null
		set Vld_SortMarker = null
		set vix to 1
		set Vi_Seq to Ai_Seq
	end

	set viABayCount to 0  		/* Determine current count of carriers in A Bay */
	while vix <= 9 do begin
		for each Vld_Sort in vldlstLanes(3,vix) do begin
			if Vld_Sort Ai_Type <> 99 then begin
				inc  viABayCount by 1
			end
		end
		inc vix by 1
	end
	if viABayCount >= viABayTrigger then begin	/* If A-Bay is nearing capacity, ignore lot and try to clear out space */
		for each Vld_Sort in Vldlst_Type(Ai_Type) do begin
			if Vld_Sort Ai_Zone = 3 then begin
				set Ai_LotNo to Vld_Sort Ai_LotNo
				set viClearABayFlag(Ai_Side) to 1
				if Ai_LotNo > Vldlst_Type(Ai_Type) first Ai_LotNo then
					inc viABayCleanUp by 1
				break
			end
		end
	end
	
	set vix to 1
	while vix <= 12 do begin
		if viClearABayFlag(Ai_Side) = 0 then begin
			set Ai_Zone = viZonePref(viZonePullPriority(vix),Ai_Type)
			set Ai_LotNo = Vldlst_Type(Ai_Type) first Ai_LotNo
		end
		else begin
			set Ai_Zone to 3
		end
		set Ai_Row to 1	

		if vsPullingRule(vix) = "Partial" then begin
			set Vld_SortMarker to null	
			set Vi_LineCount = 99
			for each Vld_Sort in Vldlst_Type(Ai_Type) do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone,Vld_Sort Ai_Row) = 0 and vldlstLanePull(Ai_Zone, Vld_Sort Ai_Row) size > 0 then begin 
					if vldlstLanePull(Ai_Zone, Vld_Sort Ai_Row) first = Vld_Sort and vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size - viEmptyCarPullQty(Ai_Zone, Vld_Sort Ai_Row) < vlocptrLane(Ai_Zone, Vld_Sort Ai_Row) capacity 
					and vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size < Vi_LineCount then begin
						set Vld_SortMarker to Vld_Sort
						set Vi_LineCount to vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size
					end
				end					
			end
			if Vld_SortMarker <> null then begin				
				remove Vld_SortMarker from Vldlst_Type(Ai_Type)
				insert Vld_SortMarker into Vldlst_Type(Ai_Type) at beginning 
				set Ai_Status = 1				
			end
		end

		if vsPullingRule(vix) = "Full" then begin
			for each Vld_Sort in O_Type(Ai_Type) load list do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone,Vld_Sort Ai_Row) = 0 then begin 
					if vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) size - viEmptyCarPullQty(Ai_Zone, Vld_Sort Ai_Row) = vlocptrLane(Ai_Zone, Vld_Sort Ai_Row) capacity then begin
						remove Vld_Sort from Vldlst_Type(Ai_Type)
						insert Vld_Sort into Vldlst_Type(Ai_Type) at beginning 
						set Ai_Status = 1
					end
				end					
			end			
		end		

		if vsPullingRule(vix) = "Mixed-Front" then begin
			set Vld_SortMarker to null
			set Vi_LineCount = 99 
			for each Vld_Sort in O_Type(Ai_Type) load list do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone,Vld_Sort Ai_Row) = 1 then begin
					set viMixCount to 0
					for each Vld_Sort2 in vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) do begin
						if Vld_Sort2 Ai_Type = this load Ai_Type then inc viMixCount by 1
						else break				
					end
					if viMixCount < Vi_LineCount then begin
						set Vi_LineCount to viMixCount
						set Vld_SortMarker to Vld_Sort
					end
				end			
			end
			if Vld_SortMarker <> null then begin
				remove Vld_SortMarker from Vldlst_Type(Ai_Type)
				insert Vld_SortMarker into Vldlst_Type(Ai_Type) at beginning
				set Ai_Status = 1
			end		
		end

		if vsPullingRule(vix) = "Mixed-Blocked" then begin			
			set Vld_SortMarker to null
			set Vi_LineCount = 99
			/*set viMixPrevRow = 0*/
			for each Vld_Sort in Vldlst_Type(Ai_Type) do begin
				if Vld_Sort Ai_LotNo = this load Ai_LotNo and Vld_Sort Ai_Zone = this load Ai_Zone and Vi_ZoneMix(Ai_Zone, Vld_Sort Ai_Row) = 1	/* and Vld_Sort Ai_Row <> viMixPrevRow */ then begin
					set viMixCount to 0
					for each Vld_Sort2 in vldlstLanes(Ai_Zone, Vld_Sort Ai_Row) do begin
						/*if Vld_Sort2 Ai_Type <> this load Ai_Type then inc viMixCount by 1*/
						if Vld_Sort2 <> Vld_Sort then inc viMixCount by 1
						else break
					end
					if viMixCount < Vi_LineCount then begin
						set Vi_LineCount to viMixCount
						set Vld_SortMarker to Vld_Sort
					end
					/*set viMixPrevRow to Vld_Sort Ai_Row*/
				end
			end
				
			if Vld_SortMarker = null and vix = 12 and Vldlst_Type(Ai_Type) size > 0 /*and Vldlst_Release(Ai_pi) size = 0*/ then begin  /* A scenario can occur where a newer lot has been recirculated behind an older lot and will not satisfy the above conditions */
				if viClearABayFlag(Ai_Side) = 1 then begin
					set viClearABayFlag(Ai_Side) = 0
					set vix = 0
				end
				else begin
					set Ai_Zone = Vldlst_Type(Ai_Type) first Ai_Zone
					set Ai_Row = Vldlst_Type(Ai_Type) first Ai_Row
					if vldlstLanePull(Ai_Zone,Ai_Row) size > 0 then	/* Check to make sure carrier has actually arrived */
						set Vld_SortMarker to Vldlst_Type(Ai_Type) first	
				end
			end
			
			if Vld_SortMarker <> null then begin
				remove Vld_SortMarker from Vldlst_Type(Ai_Type)
				insert Vld_SortMarker into Vldlst_Type(Ai_Type) at beginning
				inc viRecircCount by 1
				set Ai_Status = 1
	
				for each Vld_Sort in vldlstLanePull(Ai_Zone,Vld_SortMarker Ai_Row) do begin
					if (Vld_Sort Ai_Type <> this load Ai_Type and Vld_Sort Ai_Type <> 99) or (Vld_Sort Ai_Type = this load Ai_Type and Vld_Sort Ai_LotNo <> this load Ai_LotNo and Vld_Sort Ai_Type <> 99) then begin
						set Vld_Sort Ai_pi to 1234
						if Vld_Recirc(1) = null then begin 
							set Vld_Sort Ai_x to 1234	/* Flag first carrier to be released for recirc */
							set Vld_Recirc(1) to Vld_Sort
						end
						order Vld_Sort from O_Type(Vld_Sort Ai_Type) to continue case backorder on O_Type(Vld_Sort Ai_Type)
						remove Vld_Sort from vldlstLanes(Ai_Zone, Vld_Sort Ai_Row)
				    	set Vld_Recirc(2) to Vld_Sort	/* Flag last carrier to be released for recirc */
					end
					else begin
						if Vld_Sort Ai_x =10 then begin
							set Ai_Status = 0		/* Recirc will be initiated to push Vld_Sort to front of the lane, but won't be added to the Release List at this time */
							set vix = 13
						end	
						else break
					end
				end
				if Ai_Status = 1 then begin
					wait to be ordered on oZoneMixRecircComplete	
					order 1 load from oZoneMixCheck
					wait for 0
				end
			end
		end				
		
		if Ai_Status = 1 then begin	
			set Ai_Status = 0
			set Ai_Row = Vldlst_Type(Ai_Type) first Ai_Row
			
			while vldlstLanePull(Ai_Zone, Ai_Row) size < vldlstLanes(Ai_Zone, Ai_Row) size/2 do begin
				wait for 10 sec
			end
			
			for each Vld_Sort in vldlstLanePull(Ai_Zone, Ai_Row) do begin
				if Vld_Sort Ai_Type <> this load Ai_Type or Vld_Sort Ai_LotNo <> this load Ai_LotNo then break
				else begin
					dec Vi_Seq by Vld_Sort Ai_CarQty
					remove Vld_Sort from vldlstLanes(Ai_Zone,Ai_Row)
					remove Vld_Sort from Vldlst_Type(Ai_Type)
					insert Vld_Sort into Vldlst_Release(Ai_Side)
					
					if viTrackStoreProcess = 1 then begin
						/* if Ai_Line = 1 then print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreOut1.out"
						else if Ai_Line = 2 then print F_time_to_date(ac, Vs_SimStart) "\t" vsStyle(Ai_Type) "\t" Ai_LotNo "\t" vsZone(Ai_Zone) "\t" Ai_Row to "base.arc/RESULTS/StoreOut2.out" */
						//set vldWriteJSON to Vld_Sort
						//set vldWriteJSON As_Message to "STORE OUT REQUEST"
						//set vldWriteJSON As_CurrentLocation to vsStation(Ai_Zone,Ai_Row)
						//set vldWriteJSON As_NextLocation to "13"
						//call Sr_WriteJSONFile
						set Vld_Sort As_Message to "STORE OUT REQUEST"
						set Vld_Sort As_CurrentLocation to vsStation(Ai_Zone,Ai_Row)
						set Vld_Sort As_NextLocation to "13"
 						call F_RequestMove(Vld_Sort)
					end
					
					set Vld_Sort Ai_x to 10		/* Flag carriers that have been added to release list */
					/*set Vld_SortMarker to Vld_Sort*/
					print "Moved",Vld_Sort,"to Release" to message
					if Vi_Seq <= 0 then break	
				end	
			end
		end
		else begin 
			inc vix by 1
			if vix = 13 then begin
      			free R_SPO_Release	
	    		set Vr_WeldStarveTime(Ai_Line) to ac
		   		wait to be ordered on O_Starve(Ai_Type)
		   		tabulate (ac - Vr_WeldStarveTime(Ai_Line))/60 in T_WeldStarve(Ai_Line)
		    	wait for 5 min
		    	get R_SPO_Release
		   		set Vld_ReleasePrev(Ai_Side) = null
		   		set Vld_SortMarker = null
		   		set vix = 1
		   		set Vi_Seq = Ai_Seq
     		end
		end
		if Vldlst_Release(Ai_Side) size > 0 then break	
	end
end

begin pZoneMixCheck arriving procedure
	wait to be ordered on oZoneMixCheck
	set Ai_Zone = 1			/********* This procedure checks to see which rows are mixed rows after a release/recirc/store-in has occurred **********/
	while Ai_Zone <= viNumZones do begin
		set Ai_Row = 1
		while Ai_Row <= viZoneLaneMax(Ai_Zone) do begin
			set Ai_x = 0
			if vldlstLanes(Ai_Zone, Ai_Row) size > 0 then begin 
				set this load Ai_Type = vldlstLanes(Ai_Zone, Ai_Row) first Ai_Type
				if Ai_Type <> 99 then begin
					for each Vld_Sort in vldlstLanes(Ai_Zone, Ai_Row) do begin 
						if this load Ai_Type <> Vld_Sort Ai_Type then inc Ai_x by 1
					end
				end
			end
			if Ai_x = 0 and Vi_ZoneMix(Ai_Zone,Ai_Row) = 1 then begin
				set Vi_ZoneMix(Ai_Zone,Ai_Row) to 0
				dec C_ZoneMix(1) by 1
				inc C_ZoneMix(2) by 1
			end
			else if Ai_x > 0 and Vi_ZoneMix(Ai_Zone,Ai_Row) = 0 then begin
				set Vi_ZoneMix(Ai_Zone,Ai_Row) to 1
				inc C_ZoneMix(1) by 1
				dec C_ZoneMix(2) by 1
			end
			inc Ai_Row by 1  
		end
		inc Ai_Zone by 1
	end	
	send to pZoneMixCheck
end


/*******************************************************************************************/
/** AT THE END OF EACH RELEASE, IF THE RELEASED QUANTITY IS MORE THAN THE LOT QTY, A LOAD **/
/** IS CLONED TO THIS PROCESS TO KEEP TRACK OF THE PARTIAL QTYS AT THE RELEASE.  THESE    **/
/** QUANTITIES WILL BE ADJUSTED WHEN NEXT RELEASE REQUEST COMES FOR SAME TYPE             **/
/*******************************************************************************************/
begin P_ReleasePartial arriving procedure
  set Vproc_ReleasePartial(1,1) = P_ReleasePartial(1)
  if Vi_ReleasePartial(Ai_Line, Ai_Type) > 0 then set Ai_CarQty = Vi_ReleasePartial(Ai_Line,Ai_Type)
  else begin
    print "QTY @ Release Partial is not set right", this load, current, Ai_CarQty to message
    dec C_Error by 1 
  end
  insert this load into Vldlst_ReleasePartial(Ai_Line,Ai_Type)
  print "Release partial", Ai_Type, As_Type, Ai_CarQty to message
  wait to be ordered on Vo_ReleasePartial(Ai_Line,Ai_Type)
  get Vrsrc_ReleasePartial(Ai_Line,Ai_Type)
  remove this load from Vldlst_ReleasePartial(Ai_Line,Ai_Type)
  free Vrsrc_ReleasePartial(Ai_Line,Ai_Type) 
end  /**************************************************************************************/


/*******************************************************************************************/
/**         LOGIC TO MOVE SPO CARRIERS FROM STORAGE TO WELD LINE BUFFERS                  **/
/**      IN THIS LOGIC STORAGE LOAD ATTRIBUT Ai_Zone REPRESENTS STORAGE ZONE (1,2&3)      **/
/**          IMPORTANT ATTRIBUTES TO THIS LOGIC ARE Ai_Zone, Ai_Line, Ai_Seq              **/
/*******************************************************************************************/
begin P_StorageToBuffer arriving procedure  /**------------- FROM STORAGE TO WELD BUFFERS **/
  if this load Ai_pi = 5555 then begin 
    /*if Ai_y <> 4455 then wait to be ordered on O_RowSeq(Ai_Side)*/
    set this load Ai_pi = 0
  end
  if Ai_y = 4455 then set Ai_y = 0
  
  	if this load Ai_Zone <= 0 or Ai_Zone > 3 then begin
   		print "ERROR: Cars released do not have Storage location pointer", this load current to message
    	dec C_Error by 1  
  	end    
  	if this load Ai_Line <= 0 or Ai_Line > 4 then begin
   		print "ERROR: Cars released do not have WE buffer Line #", this load current to message
    	dec C_Error by 1  
  	end
  	if this load Ai_Zone = 1 then begin
   		travel to pf_ohc:ST9_2
    	travel to pf_ohc:ST9_3
    	if Ai_pi = 4444 or Ai_Seq = 9999 or Ai_pi = 1111 then begin
        	if R_ZoneExit(Ai_Zone) status is down then begin
        		bring up R_ZoneExit(Ai_Zone)
        		print "U", this load to Lb_ZoneExit(Ai_Zone)
      		end
    	end
    	remove this load from Vldlst_ZoneExit(Ai_Zone)
    	travel to pf_ohc:ST12    
  	end
  	else if this load Ai_Zone = 2 then begin
   		travel to pf_ohc:ST9_4
    	if Ai_pi = 4444 or Ai_Seq = 9999 or Ai_pi = 1111 then begin
        	if R_ZoneExit(Ai_Zone) status is down then begin
        		bring up R_ZoneExit(Ai_Zone)
        		print "U", this load to Lb_ZoneExit(Ai_Zone)
      		end
    	end
    	remove this load from Vldlst_ZoneExit(Ai_Zone)
    	travel to pf_ohc:ST12    
  	end
  	travel to pf_ohc:ST13
  
  	if Ai_Zone = 3 then begin  
    	if Ai_pi = 4444 or Ai_Seq = 9999 or Ai_pi = 1111 then begin
      		if R_ZoneExit(Ai_Zone) status is down then begin
        		bring up R_ZoneExit(Ai_Zone)
        		print "U", this load to Lb_ZoneExit(Ai_Zone)
      		end
    	end    
    	remove this load from Vldlst_ZoneExit(Ai_Zone)
  	end
  
	/* 	if Ai_pi = 1111 and Aloc_Destination = pf_ohc:ST13 then begin
    	if O_PrevZoneExitClear(Ai_Side) current > 0 then order from O_PrevZoneExitClear(Ai_Side) to continue  
  	end	*/
  	
  /** IF IT IS LAST OF THE LOT THEN SIGNAL RELEASE LOGIC TO PROCEDE W/ NEXT LOT RELEASE  **/
  set Ai_Seq = 0 
  inc viCarrierReleaseCount(Ai_Side,1) by 1
  if viCarrierReleaseCount(Ai_Side,1) = viCarrierReleaseCount(Ai_Side,2) and O_Release(Ai_Side) current = 1 then begin
    order from O_Release(Ai_Side) case backorder on O_Release(Ai_Side)  /** BATCH RELEASE COMPLETE SIGNAL **/
    order from O_ST13_SPO_Seq(Ai_Side) case backorder on O_ST13_SPO_Seq(Ai_Side)
    set Ai_Seq = 9999
    set viCarrierReleaseCount(Ai_Side,1) = 0
  end 
  
 /* get rST13Inspector*/
  
  wait for vrStopTime(9) sec
  set Vi_x to 1
  set Vi_CarQty to Ai_CarQty
  while Vi_x <= Vi_CarQty do begin
  	set Ai_x to rn stream47 oneof(Vr_Scrap(Ai_Type)/2:1,1-Vr_Scrap(Ai_Type)/2:0)
	inc Vr_PartCount(2,Ai_Press) by Ai_x
	dec Ai_CarQty by Ai_x
	inc P_SPO_Release(Ai_Side) load list first Ai_LotQty by Ai_x
	inc Vi_x by 1
  end 
  print Vr_PartCount(2,Ai_Press)/Vr_PartCount(1,Ai_Press) * 100 as .2 "%" to lblScrap(Ai_Press)
  print "Line=", Ai_Line, "-", Ai_Side, As_Type, "Lot =", P_SPO_Release(Ai_Side) load list first Ai_LotQty, "RlsQty=", P_SPO_Release(Ai_Side) load list first Ai_Seq to Lb_SPORelease(Ai_Side) 
  
   if this load Ai_Seq = 9999 then
  	order 1 load from oST13Inspect(Ai_Side) to continue case backorder on oST13Inspect(Ai_Side)

  /*free rST13Inspector*/
  
  if Ai_Line = 1  then begin /**---------------------------------------------- WE LINE #1 **/    
    travel to pf_ohc:W15
    travel to pf_ohc:W16
    set Ai_Seq = 0	
    set Ai_Status = 0
    
    if Ai_Side =1 then begin
      travel to pf_ohc:W32
      travel to pf_ohc:W23
      travel to pf_ohc:W24
    end
    else if Ai_Side =2 then begin
      set Aloc_Destination to nextof(pf_ohc:W18, pf_ohc:W19)
      travel to Aloc_Destination
      wait to be ordered on O_Weld1Order 
      travel to pf_ohc:W20
      
  	  set Vi_TypeCheck = Ai_Type
  	  if O_Weld1Order current > 0 then begin
  		order 1 load satisfying Ai_Type = Vi_TypeCheck from O_Weld1Order to continue
  			in case order not filled order 1 load from O_Weld1Order to continue
  	  end
  	  else order 1 load from O_Weld1Order to continue case backorder on O_Weld1Order 	
      
    end    
  end  /**-------------------------------------------------------------------- WE LINE #1 **/  

 else if Ai_Line = 2 then begin /**------------------------------------------ WE LINE #2 **/
    travel to pf_ohc:ST14
    travel to pf_ohc:ST146

    if Ai_Side =3 then begin
      travel to pf_ohc:ST154
      set Ai_Seq = 0	
      set Ai_Status = 0
      if Ai_Type <> viWeld2PrevType(1) and viWeld2PrevType(1) <> 0 then begin	
      	if O_Weld2Order(2) current = 1 and pf_ohc:ST158 current < pf_ohc:ST158 capacity then begin			/* pf_ohc:ST158 current = 0 */
      		order 1 load from O_Weld2Order(2) to continue		
      		wait to be ordered on O_Weld2Order(1)
      	end
      	if oWeld2Changeover current = 0 then begin
      		wait to be ordered on oWeld2Changeover
  		end 
      	else begin
      		order 1 load from oWeld2Changeover to continue
      		/*order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)*/
      	end
      end
     
     /* wait to be ordered on O_WE2BufferLH*/
     
      wait to be ordered on O_Weld2Order(1)
      set viWeld2PrevType(1) to Ai_Type
      travel to pf_ohc:ST158
      
      if oWeld2Changeover current = 1 then begin
      	if oWeld2Changeover load list first Ai_Side = this load Ai_Side then
      		order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)
      	else order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
      end
      else order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)
    
      travel to pf_ohc:ST101
      travel to pf_ohc:ST110
    end
    else if Ai_Side =4 then begin
      travel to pf_ohc:ST156 
      set Ai_Seq = 0	
      set Ai_Status = 0
      if Ai_Type <> viWeld2PrevType(2) and viWeld2PrevType(2) <> 0 then begin
      	if O_Weld2Order(1) current = 1 and pf_ohc:ST158 current < pf_ohc:ST158 capacity then begin			/* pf_ohc:ST158 current = 0 */
      		order 1 load from O_Weld2Order(1) to continue
      		wait to be ordered on O_Weld2Order(2)
      	end      	
      	if oWeld2Changeover current = 0 then begin
      		wait to be ordered on oWeld2Changeover
      	end
     	else begin
     		order 1 load from oWeld2Changeover to continue
     		/*order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)*/
     	end
      end
     
      /*wait to be ordered on O_WE2BufferRH*/
     
      wait to be ordered on O_Weld2Order(2)
      set viWeld2PrevType(2) to Ai_Type
      travel to pf_ohc:ST158
      
      if oWeld2Changeover current = 1 then begin
      	if oWeld2Changeover load list first Ai_Side = this load Ai_Side then
      		order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
      	else order 1 load from O_Weld2Order(2) to continue case backorder on O_Weld2Order(2)
      end
      else order 1 load from O_Weld2Order(1) to continue case backorder on O_Weld2Order(1)
      
      travel to pf_ohc:ST101
	  travel to pf_ohc:ST115
    end
  end  /**-------------------------------------------------------------------- WE LINE #2 **/

  if O_WeldBufferStarve(Ai_Side) current > 0 then 
    order from O_WeldBufferStarve(Ai_Side) to continue     
  if Ai_Status <> 1 then
  	wait to be ordered on O_WeldBuffer(Ai_Side)	
           
  send to P_Unload(Ai_Side)
end /**--------------------- LOGIC TO MOVE SPO CARRIERS FROM STORAGE TO WELD LINE BUFFERS **/
/*******************************************************************************************/

/*******************************************************************************************/
/****                      POWER & FREE CONVEYOR PROCEDURES                            *****/
/*******************************************************************************************/
begin pf_ohc:ST101 leaving procedure
  if this load Ai_Side = 3 then order from O_WE2BufferLH case backorder on O_WE2BufferLH
end
begin pf_ohc:ST115 leaving procedure
  if this load Ai_Side = 4 then order from O_WE2BufferRH case backorder on O_WE2BufferRH   
end
begin pf_ohc:ST134 arriving procedure
	if pf_ohc:ST134 current <= 1 and viKickOutQty(2) = 0 then begin
		set viKickOutQty(2) = pf_ohc:ST15 current
		order viKickOutQty(2) loads from olCarKickout to continue case backorder on olCarKickout
	end	
end

begin  pf_ohc:LS_13 cleared procedure  /**------------------- PHOTOEYE @ WE2 BUFFER ST158 **/
  if this load Ai_Side =3 and ST152 current = 0 and ST153 current = 0 and Vi_WEBufferOrderTracker(3) <> 1 then begin
    set Vi_WEBufferOrderTracker(3) = 1
    set Vi_SPO_QtyRelease(3) = Vi_Release    
    order from O_BufferSpace(3) case backorder on O_BufferSpace(3)    
  end
  else if this load Ai_Side = 4 and ST155 current + ST156 current <= 4 and Vi_WEBufferOrderTracker(4) <> 1 then begin
    set Vi_WEBufferOrderTracker(4) = 1
    set Vi_SPO_QtyRelease(4) = Vi_Release    
    order from O_BufferSpace(4) case backorder on O_BufferSpace(4)    
  end 
end  /**----------------------------------------------------- PHOTOEYE @ WE2 BUFFER ST158 **/


begin  pf_ohc:WEBufferReceived cleared procedure  /**------ PHOTOEYE @ WE BUFFER RECEIVED **/
   if this load Ai_Line = 1 then 
   	inc C_L1Weld(this load Ai_Side) by 1
   
   if this load Ai_Seq = 9999 and Vi_WEBufferOrderTracker(this load Ai_Side) = 1 then begin 
      set Vi_WEBufferOrderTracker(this load Ai_Side) = 0      /** ORDERED RECEIVED TOGGLE **/      
      set Vi_SPO_QtyRelease(this load Ai_Side) = 0
   end
end  /**--------------------------------------------------- PHOTOEYE @ WE BUFFER RECEIVED **/


begin  pf_ohc:LS_17 cleared procedure  /**----------- PHOTOEYE @ WE1 BUFFER REORDER POINT **/
  if W15 current = 0 and 
     Vi_WEBufferOrderTracker(1) <> 1 and
     Vi_WEBufferOrderTracker(2) <> 1 then begin

	 if P_SPO_Release(1) current > 0 then begin 
 	 	set Vi_WEBufferOrderTracker(1) = 1
   		set Vi_SPO_QtyRelease(1) = Vi_Release    
     	order from O_BufferSpace(1) case backorder on O_BufferSpace(1)
	 end

	 if P_SPO_Release(2) current > 0 then begin
     	set Vi_WEBufferOrderTracker(2) = 1
     	set Vi_SPO_QtyRelease(2) = Vi_Release
     	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)       
  	 end
  end	
end  /**---------------------------------------------- PHOTOEYE @ WE1 BUFFER REORDER POINT**/

/*begin pf_ohc:LS_Recirc cleared procedure 
/*	if Vld_Recirc(2) <> null then begin
		if this load = Vld_Recirc(2) then
	end	*/
	if this load Ai_z = 5454 then begin
		set Ai_z = 1
	end
end*/
begin pf_ohc:W13 arriving procedure
	if this load = Vld_Recirc(2) then set Vld_Recirc(2) = null
end
begin pf_ohc:ST14 arriving procedure
	if this load = Vld_Recirc(2) then set Vld_Recirc(2) = null
end


/*
begin pf_ohc:LS_17 cleared procedure /**----------- PHOTOEYE @ WE1 BUFFER REORDER POINT **/
  if this load Ai_Side = 1 and Vi_WEBufferOrderTracker(1) <> 1 and C_L1Weld(1) current < 8 then begin
  	set Vi_WEBufferOrderTracker(1) = 1
  	set Vi_SPO_QtyRelease(1) = Vi_Release
  	order from O_BufferSpace(1) case backorder on O_BufferSpace(1)
  end
  else if this load Ai_Side = 2 and Vi_WEBufferOrderTracker(2) <> 1 and C_L1Weld(2) current < 8 then begin
  	set Vi_WEBufferOrderTracker(2) = 1
  	set Vi_SPO_QtyRelease(2) = Vi_Release
  	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)
  end
end /**---------------------------------------------- PHOTOEYE @ WE1 BUFFER REORDER POINT**/
*/

begin pf_ohc:RowSwitch cleared procedure /**------------------- EACH ZONE EXIT ROW SWITCH **/
  if this load type <> L_invis begin
    if this load Ai_pi = 4444 then
      order 1 load from O_ReleaseSeq(this load Ai_Side) /* O_RowSeq(this load Ai_Side) */ /* case backorder on O_RowSeq(this load Ai_Side) */
    else if Ai_Side > 0 then begin
      if Vld_ReleasePrev(this load Ai_Side) = this load then set Vld_ReleasePrev(this load Ai_Side) = null  
    end
  end
  
  if this load = Vld_Recirc(2) then
		order 1 load from oZoneMixRecircComplete to continue
  
end  /**------------------------------------------------------- EACH ZONE EXIT ROW SWITCH **/

/*
begin pf_ohc:LS_26 cleared procedure
	if C_L1Weld(this load Ai_Side) current > 0 then dec C_L1Weld(this load Ai_Side) by 1
end

begin pf_ohc:LS_23 cleared procedure
	if C_L1Weld(this load Ai_Side) current > 0 then dec C_L1Weld(this load Ai_Side) by 1
end

begin pf_ohc:LS_28 cleared procedure
  if Vi_WEBufferOrderTracker(1) <> 1 and C_L1Weld(1) current < 13 then begin
  	set Vi_WEBufferOrderTracker(1) = 1
  	set Vi_SPO_QtyRelease(1) = Vi_Release
  	if O_L1Weld(2) current > 0 then order all loads from O_L1Weld(2) to continue 
  	if C_L1Weld(2) current = 0 and this load Ai_Seq = 9999 then wait to be ordered on O_L1Weld(1)
  	if pf_ohc:W20 total = 0 then wait to be ordered on O_Line1Start
  	order from O_BufferSpace(1) case backorder on O_BufferSpace(1)
  end	
end

begin pf_ohc:LS_19 cleared procedure
  if Vi_WEBufferOrderTracker(2) <> 1 and C_L1Weld(2) current < 13 then begin
  	set Vi_WEBufferOrderTracker(2) = 1
  	set Vi_SPO_QtyRelease(2) = Vi_Release
  	if O_L1Weld(1) current > 0 then order all loads from O_L1Weld(1) to continue 
  	if C_L1Weld(1) current = 0 and this load Ai_Seq = 9999 then wait to be ordered on O_L1Weld(2)
  	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)
  end
end

begin pf_ohc:LS_21 cleared procedure
  if Vi_WEBufferOrderTracker(2) <> 1 and C_L1Weld(2) current < 13 then begin
  	set Vi_WEBufferOrderTracker(2) = 1
  	set Vi_SPO_QtyRelease(2) = Vi_Release
  	if O_L1Weld(1) current > 0 then order all loads from O_L1Weld(1) to continue 
  	if C_L1Weld(1) current = 0 and this load Ai_Seq = 9999 then wait to be ordered on O_L1Weld(2)
  	order from O_BufferSpace(2) case backorder on O_BufferSpace(2)
  end
end

begin pf_ohc:W20 arriving procedure
	if pf_ohc:W20 total = 1 then order 1 load from O_Line1Start to continue
	wait for 0
end */


/*******************************************************************************************/
/**                        UNLOAD AT SPO LINE WELD STATIONS LOGIC                         **/
/**  WHEN CARRIERS ARE RELEASED FROM EITHER BUFFER OR FROM PARTIAL BUFFER, THEY ARRIVE AT
     RESPECTIVE LOAD STATIONS.  IT WILL WAIT FOR RBT SIGNAL FOR PICK ACTIVITY, THEN RBT 
     IS SIGNALED TO PICK, ONCE PICKED BY RBT, IT WILL ADJUST ITS QTY.  IF THERE IS A FLAG
     FOR PARTIAL STORAGE BUFFER AFTER CHANGE OVER, THEN THE CARRIER IS SENT TO PARTIAL 
     STORAGE LOGIC.  LOADS ARRIVE FROM WELD BUFFER RELEASE OR PARTIAL BUFFER RELEASE 
                               PROCINDEX = 1 for LINE #1 LH
                               PROCINDEX = 2 for LINE #1 RH
                               PROCINDEX = 3 for LINE #2 LH
                               PROCINDEX = 4 for LINE #2 RH                               **/ 
/*******************************************************************************************/
begin P_Unload arriving procedure
  travel to Vloc_B4Unload(procindex)
  travel to Vloc_Unload(procindex)

  /**---------- IF LAST OF PARTIAL BUFFER LINE RELEASED< SIGNAL RELEASE COMPLETE ---------**/  
  if Ai_Status = 7777 and Ai_Seq = 9999 and O_WaitForPartialRelease(procindex) current > 0 then
    order from O_WaitForPartialRelease(procindex) to continue
  set Vldptr_Unload(procindex) to this load   /** FOR WELD PROCESS TO SET PARTIAL STORAGE **/
  
  if Vldlst_PartLane(procindex,1) size > 0 then begin
  	if Vldlst_PartLane(procindex,1) last Ai_Type = this load Ai_Type then begin
  		set Vldlst_PartLane(procindex,1) last Ai_CarQty to Vldlst_PartLane(procindex,1) last Ai_CarQty + this load Ai_CarQty
  		scale Vldlst_PartLane(procindex,1) last to z Ai_CarQty
  		set Vldptr_Unload(procindex) to null
  		set this load type to L_invis  
  		set Ai_Status = 7777
  		dec C_CarrierCount(1) by 1
  		inc C_CarrierCount(2) by 1
  		if C_CarrierCount(2) current >= Vi_EmptyReq and O_HoldForCarriers current >= 1 then
  		order 1 load from O_HoldForCarriers to continue 
  		send to pEmptyCarrierReturn
  	end	
  end	
       
  while Ai_CarQty > 0 do begin
    wait to be ordered on Vo_SPO(procindex,1)            /** LOAD SIGNAL FROM SPO PROCESS **/    
   	if Ai_Status = 8888 then send to P_PartialStorage(procindex)      /** PARTIAL STORAGE **/
   	order from Vo_RBT(procindex,1) case backorder on Vo_RBT(procindex,1)   /** SIG T/PICK **/
   	wait to be ordered on Vo_RBT(procindex,2)                    /** WAIT FOR RBT TO PICK **/   
   	wait to be ordered on Vo_RBT(procindex,3)                     /** WAIT TILL RBT CLEAR **/
   	dec Ai_CarQty by 1
   	if Ai_CarQty > 0 then scale to z Ai_CarQty
   	print ac - Vr_WeldCycleTime(procindex) as .2 to Lb_WeldCycle(procindex) 
   	set Vr_WeldCycleTime(procindex) to ac
  end
  set Vldptr_Unload(procindex) to null
  set this load type to L_invis  
  set Ai_Status = 7777
  dec C_CarrierCount(1) by 1
  inc C_CarrierCount(2) by 1
  if C_CarrierCount(2) current >= Vi_EmptyReq and O_HoldForCarriers current >= 1 then
  	order 1 load from O_HoldForCarriers to continue 
  send to pEmptyCarrierReturn
end  /**************************************************************************************/
/*******************************************************************************************/


/*******************************************************************************************/
/**                          LOGIC TO STORE PARTIAL CARRIERS                              **/
/** IMPORTANT ATTRIBUTES TO PASS are Ai_Line                                              **/
/**                      Ai_Status = 8888 Partial Storage                                  **/
/**                      Ai_Status = 7777 Partial Release                                  **/
/**                      Ai_Status = 6666 Partial ReCirc                                   **/
/**                            PROCINDEX = 1 for LINE #1 LH
                               PROCINDEX = 2 for LINE #1 RH
                               PROCINDEX = 3 for LINE #2 LH
                               PROCINDEX = 4 for LINE #2 RH                               **/ 
/*******************************************************************************************/
begin P_PartialStorage arriving procedure
  if Vloc_Partial(procindex,1) remaining = 0 and  Vloc_Partial(procindex,2) remaining = 0 then begin
    print "NO SPACE @ PARTIAL BUFFER", this load, procindex to message
    dec C_Error by 1  
  end  
  insert this load into Vldlst_Partial(procindex, Ai_Type)
  if Vloc_Partial(procindex,1) current < Vloc_Partial(procindex,1) capacity then begin
    insert this load into Vldlst_PartLane(procindex,1)
    travel to Vloc_Partial(procindex,1)
    if Ai_Status <> 7777 or Ai_Status <> 6666 then 
      wait to be ordered on Vo_PartialBuffer(procindex,1)
    remove this load from Vldlst_PartLane(procindex,1)      
  end
  else if Vloc_Partial(procindex,2) current < Vloc_Partial(procindex,2) capacity then begin
    insert this load into Vldlst_PartLane(procindex,1)
    travel to Vloc_Partial(procindex,2)
    if Ai_Status <> 7777 or Ai_Status <> 6666 then 
      wait to be ordered on Vo_PartialBuffer(procindex,2)  
    remove this load from Vldlst_PartLane(procindex,2)      
  end
  remove this load from Vldlst_Partial(procindex, Ai_Type)
  
  if Ai_Status = 6666 then begin      /** IF RE_CIRC THEN INSERT BACK INTO PARTIAL BUFFER **/
    travel to Vloc_Unload(procindex)
    set Ai_Status = 0
    set Ai_Seq = 0
    send to P_PartialStorage(procindex)
  end
  else if Ai_Status = 7777 then send to P_Unload(procindex)     /** SEND TO UNLOAD PROCESS **/
  else begin
    print "ERROR in setting Ai_Status value @ line", procindex, "partial buffer:  CHECK", this load to message
    dec C_Error by 1  
  end
end
/*******************************************************************************************/


/*******************************************************************************************/
/**                        PARTIAL SPO BUFFER RELEASE LOGIC                               **/
/**   THE LOADS ARE CLONED FROM  P_SPO_Process AS NEEDED BASED ON PARTIAL BUFFER STATUS   **/ 
/*******************************************************************************************/
begin P_PartialRelease arriving procedure
  /** SET PARTIAL RELEASE CODE FOR THE LOADS IN THE SAME TYPE LIST **/
  for each Vldptr_Partial(procindex) in Vldlst_Partial(procindex, Ai_Type) do begin
    set Vldptr_Partial(procindex) Ai_Status = 7777          /** PARTIAL RELEASE INDICATOR **/  
  end
  set Ai_pi = 0
  while Ai_pi < 2 do begin  /**-------------------- WHILE LOOP FOR 2 PARTIAL BUFFER LINES **/    
    inc Ai_pi by 1   
    if Vldlst_Partial(procindex, Ai_Type) size = 0 then break     
    set Ai_Seq = 0
    set Vi_PartialIndex(procindex) = 0
    for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
      inc Vi_PartialIndex(procindex) by 1  
      if Vldptr_Partial(procindex) Ai_Status = 7777 then begin
        set Ai_Seq = Vi_PartialIndex(procindex)-1
        break
      end  
    end    
    set Ai_CarQty = 0
    for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
      inc Ai_CarQty by 1  
      if Vldptr_Partial(procindex) Ai_Status = 7777 then set Ai_Row = Ai_CarQty 
    end
    
    if Ai_Seq > 0 then begin  /**----------- CAR TO RELEASES IS NOT IN THE FIRST POSITION **/
      set Ai_CarQty = 0 
      for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
        inc Ai_CarQty by 1 
        if Vldptr_Partial(procindex) Ai_Status <> 7777 then begin
          set Vldptr_Partial(procindex) Ai_Status = 6666              /** SET RE-CIR CODE **/
        end
        /** FIND LAST CAR TO BE RELEASED & SET LAST CAR CODE **/
        else if Vldptr_Partial(procindex) Ai_Status = 7777 then begin
          if Ai_CarQty = Ai_Row then set Vldptr_Partial(procindex) Ai_Seq = 9999
          break 
        end 
      end
      order from Vo_PartialBuffer(procindex,Ai_pi) to continue            /** RELEASE CAR **/     
      wait to be ordered on O_WaitForPartialRelease(procindex)   /** WAIT FOR THE RELEASE **/      
    end

    else if Ai_Seq = 0 then begin    /**----- CAR TO BE RELEASED IS IN THE FIRST POSITION **/
      set Ai_CarQty = 0  /**------------ FIND LAST CAR TO BE RELEASED & SET LAST CAR CODE **/ 
      for each Vldptr_Partial(procindex) in Vldlst_PartLane(procindex,Ai_pi) do begin
        inc Ai_CarQty by 1 
        if Vldptr_Partial(procindex) Ai_Status = 7777 then begin
          if Ai_CarQty = Ai_Row then set Vldptr_Partial(procindex) Ai_Seq = 9999
          break 
        end 
      end  /**-------------------------- FIND LAST CAR TO BE RELEASED & SET LAST CAR CODE **/    
      order from Vo_PartialBuffer(procindex,Ai_pi) to continue           /** RELEASE CAR) **/     
      wait to be ordered on O_WaitForPartialRelease(procindex)   /** WAIT FOR THE RELEASE **/          
    end  /**--------------------------------- CAR TO BE RELEASED IS IN THE FIRST POSITION **/   
  end   /**---------------------------------------- WHILE LOOP FOR 2 PARTIAL BUFFER LINES **/ 
  order from O_PartialRelease(procindex) to continue  /** PARTIAL RELEASE COMPLETE SIGNAL **/
end
/*******************************************************************************************/


/*******************************************************************************************/
/** LOADS ARE CLONED FROM THE INITIALIZATION PROCESS & WILL STAY TILL END OF SIMULATION   **/
/**                       PROCINDEX = 1 for LINE #1 LH
                          PROCINDEX = 2 for LINE #1 RH
                          PROCINDEX = 3 for LINE #2 LH
                          PROCINDEX = 4 for LINE #2 RH                                    **/ 
/*******************************************************************************************/
begin P_LoadRBT arriving procedure 
  move into Vloc_LoadRBT(procindex,1)                                            /** HOME **/    
  while 1=1 do begin
    wait to be ordered on Vo_SPO(procindex,1)  /** WAIT FOR LOAD SIGNAL FROM SPO PROCESS  **/
    travel to Vloc_LoadRBT(procindex,2)                                       /** POUNCE1 **/
    wait to be ordered on Vo_RBT(procindex,1)       /** WAIT FOR CARRIER SIGNAL TO UNLOAD **/
    travel to Vloc_LoadRBT(procindex,3)                        /** WORK1 OR PICK POSITION **/
    /** SET LOAD TYPE & ATTR VALUES CHANGE LOAD TYPE **/
    order from Vo_RBT(procindex,2) to continue                            /** PICK SIGNAL **/
    set Ai_Type = Vldptr_Unload(procindex) Ai_Type
    set this load type to vlt_LoadType(Ai_Type)
    set this load color to vclr_LoadColor(Ai_Type)    
    travel to Vloc_LoadRBT(procindex,4)                              /** CLEAR 1 POSITION **/
    order from Vo_RBT(procindex,3) to continue                           /** CLEAR SIGNAL **/

    travel to Vloc_LoadRBT(procindex,5)                              /** POUNCE2 POSITION **/    
    travel to Vloc_LoadRBT(procindex,6)                       /** WORK 2 OR DROP POSITION **/
        
    order from Vo_SPO(procindex,2) to continue                           /** LOAD SIGNAL  **/ 
    set load type to L_invis
    travel to Vloc_LoadRBT(procindex,7)                              /** Clear 2 POSITION **/
    /** RESET ATTR, LOAD TYPE **/
    order from Vo_SPO(procindex,3) to continue                           /** Clear SIGNAL **/ 
    travel to Vloc_LoadRBT(procindex,1) /** HOME **/  
  end
end  /**************************************************************************************/


begin Sr_WriteJSONFile
	print "{" to "base.arc/RESULTS/JSON.out"
	print '"messageType":"' vldWriteJSON As_Message '"' to "base.arc/RESULTS/JSON.out"
	print '"tagID":"1111"' to "base.arc/RESULTS/JSON.out"
	print '"carrierNumber":"' vldWriteJSON vehicle '"' to "base.arc/RESULTS/JSON.out"
	print '"dieNumber":"' vldWriteJSON Ai_LotNo '"' to "base.arc/RESULTS/JSON.out"
	print '"quantity":"' vldWriteJSON Ai_CarQty '"' to "base.arc/RESULTS/JSON.out"
	if vldWriteJSON Ai_Type <> 99 then
		print '"productionDate":"' F_time_to_date(vldWriteJSON At_TimeInSys, Vs_SimStart) '"' to "base.arc/RESULTS/JSON.out"
	else
		print '"productionDate":"null"' to "base.arc/RESULTS/JSON.out"
	print '"model":"' vldWriteJSON As_Type '"' to "base.arc/RESULTS/JSON.out"
	print '"currentLocation":"' vldWriteJSON As_CurrentLocation '"' to "base.arc/RESULTS/JSON.out"
	print '"nextLocation":"' vldWriteJSON As_NextLocation '"' to "base.arc/RESULTS/JSON.out"
	print "}" to "base.arc/RESULTS/JSON.out"
	print "" to "base.arc/RESULTS/JSON.out"
end

begin F_ConnectSocket function
	set vsTemp = sock_MachineName()
	if vsTemp <> null then begin
		call sock_SetConnectBlocking(true)
		set vsockSocket = sock_ConnectPort(vsTemp, 3455)
		if sock_IsValid(vsockSocket) = true then begin
			call sock_SetNonBlocking(vsockSocket, false)	// vsockSocket is now blocking
			return true
		end
	end
	return false
end

begin F_RequestMove function
	if sock_IsValid(vsockSocket) = false then
		return null
	if argLoad Ai_Type <> 99 then
		print F_time_to_date(argLoad At_TimeInSys, Vs_SimStart) to vsTemp
	else
		print "null" to vsTemp
	print "{" 
		'"messageType":"' argLoad As_Message '",'
		'"tagID":"1111"'
		'"carrierNumber":"' argLoad vehicle '",'
		'"dieNumber":"' argLoad Ai_LotNo '",'
		'"quantity":"' argLoad Ai_CarQty '",'
		'"productionDate":"' vsTemp '",'
		'"model":"' argLoad As_Type '",'
		'"currentLocation":"' argLoad As_CurrentLocation '",'
		'"nextLocation":"' argLoad As_NextLocation '"'
		"}" to vsMessage
	if sock_SendString(vsockSocket, vsMessage) = 0 then begin
		set vsockSocket = null	// No longer valid- socket closed
		return null
	end
	set vsReponse = sock_ReadString(vsockSocket)
	if vsReponse = null then
		return null
	set vjsonStruct = cJSON_Parse(vsResponse)
	set vsTemp = cJSON_GetObjectString(vjsonStruct, "nextLocation")
	call cJSON_Delete(vjsonStruct)
	set vjsonStruct = null
	return vsTemp
end


