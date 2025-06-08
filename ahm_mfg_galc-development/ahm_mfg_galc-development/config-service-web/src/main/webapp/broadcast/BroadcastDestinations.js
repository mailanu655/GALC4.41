		//------------------------------------------------------------------
		// Scripts for the Broadcast Destination Settings Table
		//------------------------------------------------------------------
		var broadcastDestinationWindow = null;
		function showBroadcastDestinationWindow() {
		
		}
		
	  function closeBroadcastDestinationWindow() {
		   if (broadcastDestinationWindow != null)
		   {
		      broadcastDestinationWindow.window.close();
		      broadcastDestinationWindow == null;
		   }
	  }
		
	  function doAddBroadcastDestination() {
		
		 		rblist = document.getElementsByName("broadcastdatasequence");
		 		
		 		fieldIndex = 1;
		 		
		 		if (rblist != null)
		 		{
		 		  
	        	  fieldIndex = rblist.length+1;
	        	}
	        	
	        	
	        	processPointID = document.getElementById("broadcastppid").value;
	
	        	popupurl = "broadcastDestinationSettings.do?processPointID="+processPointID+"&operation=ADD&sequenceNo="+fieldIndex;
	        
   	            broadcastDestinationWindow = window.open(popupurl,"broadcastdestinationwin","width=600,height=400,status=yes,resizable=yes",true);
   	            broadcastDestinationWindow.focus();
		
	  }
		
	  function doModifyBroadcastDestination() {
		
	            rblist = document.getElementsByName("broadcastdatasequence");
	        	rowcount = rblist.length;
	        	
	        	
	        	sequenceNo = 0;
	        	
	        	for (i=0; i < rowcount; i++)
	        	{
	        	   
	        	   if (rblist[i].checked == true)
	        	   {
	        	      
	        	      sequenceNo = i;
	        	     
	        	      break;
	        	   }
	        	}
	        	
	        	fieldIndex = sequenceNo+1;
	        	
	        	
	        	processPointID = document.getElementById("broadcastppid").value;
	        	
	        	destinationType = document.getElementsByName("bddestinationType"+fieldIndex)[0].value;
                destinationID = document.getElementsByName("bddestinationID"+fieldIndex)[0].value;
                requestID = document.getElementsByName("bdrequestID"+fieldIndex)[0].value;
                argument = document.getElementsByName("bdargument"+fieldIndex)[0].value;
                autoEnabled = document.getElementsByName("bdAutoEnabled"+fieldIndex)[0].value;
                checkPoint =  document.getElementsByName("bdCheckPoint" + fieldIndex)[0].value;
                condition =  document.getElementsByName("bdCondition" + fieldIndex)[0].value;
                conditionType =  document.getElementsByName("bdConditionType" + fieldIndex)[0].value;
   
   				if (argument != null) {
   				   temparg = new String(argument);
   				   argument = temparg.replace(/&/g,"%26");
   				   
   				}
	        	
	        	popupurl = "broadcastDestinationSettings.do?processPointID="+processPointID+"&operation=MODIFY&sequenceNo="+fieldIndex+
	        	            "&destinationTypeID="+destinationType+"&destinationID="+destinationID+"&requestID="+requestID+
	        	            "&argument="+argument + "&autoEnabled=" + autoEnabled + 
	        	            "&checkPoint=" + checkPoint + 
	        	            "&condition=" + condition + 
	        	            "&conditionType=" + conditionType;
	        
	            broadcastDestinationWindow = window.open(popupurl,"broadcastdestinationwin","width=760,height=400,status=yes,resizable=yes",true);		
	            broadcastDestinationWindow.focus();
	  }
		
	  function applyBroadcastDestinationSettings(operation, processPointID, sequenceNo, destinationType, destinationID, requestID, argument, autoEnabled,checkPoint, condition, conditionType) {
		
		   //alert(operation + " " + sequenceNo + "type: "+destinationType);
		   
		   if (broadcastDestinationWindow != null)
		   {
		      broadcastDestinationWindow.window.close();
		      broadcastDestinationWindow == null;
		   }
		   
		   if (operation == "MODIFY")
		   {
 		       modifyBroadcastDestinationRow(sequenceNo, destinationType, destinationID,requestID,argument, autoEnabled,checkPoint, condition, conditionType);
		   }
		   else if (operation == "ADD")
		   {
	           // Build out the new table entries
	           var tablebody = document.getElementById("broadcasttablebody");
	           
	           newrow = document.createElement("tr");
	           newrow.id = "trbd"+sequenceNo;
      
      
      		   // Create sequence radio button
      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdsequenceNo"+sequenceNo;
      		   newcol.innerHTML = "<INPUT type=\"radio\" name=\"broadcastdatasequence\" value=\""+sequenceNo+"\" >"+sequenceNo;
      		   newrow.appendChild(newcol);
      
      		   newcol = document.createElement("td");
      		   newcol.id = "td_bddestinationType"+sequenceNo;
      		   newrow.appendChild(newcol);
      		   
      		   newcol = document.createElement("td");
      		   newcol.id = "td_bddestinationID"+sequenceNo;
      		   newrow.appendChild(newcol);

      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdrequestID"+sequenceNo;
      		   newrow.appendChild(newcol);      		   

      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdargument"+sequenceNo;
      		   newrow.appendChild(newcol);       		   
               
      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdAutoEnabled"+sequenceNo;
      		   newrow.appendChild(newcol);       		   

      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdCheckPoint"+sequenceNo;
      		   newrow.appendChild(newcol);
      		   
      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdCondition"+sequenceNo;
      		   newrow.appendChild(newcol);   
      		   
      		   newcol = document.createElement("td");
      		   newcol.id = "td_bdConditionType"+sequenceNo;
      		   newrow.appendChild(newcol); 
      		   
      		   
             // Add row to table
             tablebody.appendChild(newrow);
             
             // Add data to row
             modifyBroadcastDestinationRow(sequenceNo, destinationType, destinationID,requestID,argument, autoEnabled,checkPoint, condition, conditionType);
             
			 displayMessage("broadcastmsgcol", "Changes are not saved until the Apply button is pressed.");
		   
		   }
		   
	  }
		
	  function removeBroadcast() {
	  
			 currentSelection = 0;
			 rblist = document.getElementById("broadcastForm").broadcastdatasequence;
			 
			 
			 
			 
			 if (rblist != null && rblist.length > 0)
			 {
			 	  rowcount = rblist.length;
			 	  for (i = 0; i < rowcount; i++)
			 	  {
			 	  	if (rblist[i].checked == true)
			 	  	{
			 	  		  currentSelection = i+1;
			 	  		  break;
			 	    }
			 	  }
			 	  
			 	  
			 	   
	              var tablebody = document.getElementById("broadcasttablebody");
			      if (currentSelection == 0)
			      {
				    return;
			      }
			      
			      //alert("Row count: "+rowcount + " Current selection: "+currentSelection);
	              // @JM 05/18/2007 - Fixed problem deleting next-to-last row
	              if (currentSelection < rowcount)
	              {
	        	     // Shift the data upwords moving the row to be deleted
	        	     // to the end.
	        	     for (j = currentSelection; j < rowcount; j++)
	        	     {
	        	        switchBroadcastDestinationRows(j,j+1);
	        	     }
	              }	        
	        
	        
	              // Now remove last child of table
	              trow = document.getElementById("trbd"+rowcount);
	              if (trow != null)
	              {
	        	     tablebody.removeChild(trow);
	              }			 	  
	              else
	              {
	              	alert("Could not find child row trbd"+rowcount);
	              }
	              
			 	  
			      // Reset the selection state 
			      rblist = document.getElementById("broadcastForm").broadcastdatasequence;
			 
			      if (rblist != null && rblist.length > 0)
			      {
			 	     rowcount = rblist.length;
			 	     for (i = 0; i < rowcount; i++)
			 	     {
			 	     	  if (i == 0)
			 	     	  {
			 	     	  	rblist[i].checked = true;
			 	     	  }
			 	     	  else
			 	     	  {
			 	     	  	rblist[i].checked = false;
			 	     	  }
			 	  	 } 
			 	  }
			      
			      
				 displayMessage("broadcastmsgcol", "Changes are not saved until the Apply button is pressed.");
			 	 	
			 	  
	         }
	         else
	         {
	              // Need to see if there is a single element
	              var tablebody = document.getElementById("broadcasttablebody");
	              trow = document.getElementById("trbd1");
	              if (trow != null)
	              {
	                 
	        	     tablebody.removeChild(trow);
	        	     displayMessage("broadcastmsgcol", "Changes are not saved until the Apply button is pressed.");
	              }			 	  
	            
	         }

	  }
		
      function upBroadcast() {
			    	
			 currentSelection = 0;
			 rblist = document.getElementById("broadcastForm").broadcastdatasequence;
			 
			 if (rblist != null && rblist.length > 1)
			 {
			 	  rowcount = rblist.length;
			 	  for (i = 0; i < rowcount; i++)
			 	  {
			 	  	if (rblist[i].checked == true)
			 	  	{
			 	  		  currentSelection = i+1;
			 	  		  break;
			 	    }
			 	  }
			 	  
			 	  if (currentSelection == 1)
			 	  {
			 	  	  // No way to go up
			 	  	  return;
			 	  }
			 	  
			 	  //alert("Up: currentSelection = "+currentSelection + "Size: "+rblist.length);
			 	  switchBroadcastDestinationRows(currentSelection, currentSelection-1);
			 	  
			 	  // change the selection state
	              rblist[currentSelection-2].checked = true;
	              rblist[currentSelection-1].checked = false;	
	              
	              
	 			  displayMessage("broadcastmsgcol", "Changes are not saved until the Apply button is pressed.");
			 	  
			 }
			
	  }
		
	  function downBroadcast() {

			 currentSelection = 0;
			 rblist = document.getElementById("broadcastForm").broadcastdatasequence;
			 
			 if (rblist != null && rblist.length > 1)
			 {
			 	  rowcount = rblist.length;
			 	  for (i = 0; i < rowcount; i++)
			 	  {
			 	  	if (rblist[i].checked == true)
			 	  	{
			 	  		  currentSelection = i+1;
			 	  		  break;
			 	    }
			 	  }
			 	  
			 	  if (currentSelection == rowcount)
			 	  {
			 	  	  // No way to go down
			 	  	  return;
			 	  }
			 	  
			 	  //alert("Down: currentSelection = "+currentSelection + "Size: "+rblist.length);
			 	  
			 	  switchBroadcastDestinationRows(currentSelection, currentSelection+1);
			 	  
			 	  // change the selection state
	              rblist[currentSelection].checked = true;
	              rblist[currentSelection-1].checked = false;	
			 	  
			 }
			 
			 displayMessage("broadcastmsgcol", "Changes are not saved until the Apply button is pressed.");
			 
			
	  }
		
	  function switchBroadcastDestinationRows(row1index,row2index) {
		
		
			
			type1 = document.getElementsByName("bddestinationType"+row1index)[0].value;
			id1 = document.getElementsByName("bddestinationID"+row1index)[0].value;
			request1 =  document.getElementsByName("bdrequestID"+row1index)[0].value;
			arg1 = document.getElementsByName("bdargument"+row1index)[0].value;
			auto1 = document.getElementsByName("bdAutoEnabled"+row1index)[0].value;
			
			checkPoint1 = document.getElementsByName("bdCheckPoint"+row1index)[0].value;
			condition1 = document.getElementsByName("bdCondition"+row1index)[0].value;
			conditionType1 = document.getElementsByName("bdConditionType"+row1index)[0].value;
			
			type2 = document.getElementsByName("bddestinationType"+row2index)[0].value;
			id2 = document.getElementsByName("bddestinationID"+row2index)[0].value;
			request2 =  document.getElementsByName("bdrequestID"+row2index)[0].value;
			arg2 = document.getElementsByName("bdargument"+row2index)[0].value;
			auto2 = document.getElementsByName("bdAutoEnabled"+row2index)[0].value;

			checkPoint2 = document.getElementsByName("bdCheckPoint"+row2index)[0].value;
			condition2 = document.getElementsByName("bdCondition"+row2index)[0].value;
			conditionType2 = document.getElementsByName("bdConditionType"+row2index)[0].value;
			
		    modifyBroadcastDestinationRow(row1index, type2, id2,request2,arg2, auto2, checkPoint2, condition2, conditionType2);
		    modifyBroadcastDestinationRow(row2index, type1, id1,request1,arg1, auto1, checkPoint1, condition1, conditionType1);
			
	  }
		
	  function modifyBroadcastDestinationRow(index, destinationType, destinationID, requestID, argument, autoEnabled,checkPoint, condition, conditionType) {
			
			       if (destinationType == null)
			       {
			       	  destinationType = "0";
			       }
			       
			       switch(destinationType) {
			       		case "0": destinationDisplay = "Equipment"; break;
			       		case "1": destinationDisplay = "Printer"; break;
			       		case "2": destinationDisplay = "Terminal"; break;
			       		case "3": destinationDisplay = "Application"; break;
			       		case "4": destinationDisplay = "External"; break;
			       		case "5": destinationDisplay = "Notification"; break;
			       		case "6": destinationDisplay = "MQ"; break;
			       		case "7": destinationDisplay = "DeviceWise"; break;
			       		case "9": destinationDisplay = "Server Task"; break;
			       		case "10": destinationDisplay = "FTP"; break;
			       		default: destinationDisplay = "Unknown"; break;
			       }
			      
			       if (destinationID == null)
			       {
			       	  destinationID = "";
			       }
			       if (requestID == null)
			       {
			       	   requestID = "";
			       }
			       
			       if (argument == null)
			       {
			       	   argument = "";
			       }
			       
			       if (autoEnabled == null) {
			    	   autoEnabled = "";
			       }
			       
			       displayRequestId = requestID;
			       
			       if (displayRequestId == "")
			       {
			       	   displayRequestId = "&nbsp;";
			       }
			       
			       displayArgument = argument;
			       
			       if (displayArgument == "")
			       {
			       	   displayArgument = "&nbsp;";
			       }
			       
			       displayAutoEnabled = autoEnabled;
			       
			       if (displayAutoEnabled == "")
			       {
			    	   displayAutoEnabled = "&nbsp;";
			       }
			       
			       displayCheckPoint = checkPoint;
			       if (displayCheckPoint == "")
			       {
			    	   displayCheckPoint = "&nbsp;";
			       }			       
			       
			       displayCondition = condition;
			       if (displayCondition == "")
			       {
			    	   displayCondition = "&nbsp;";
			       }			       

			       displayConditionType = conditionType;
			       if (displayConditionType == "")
			       {
			    	   displayConditionType = "&nbsp;";
			       }
			       
				   col = document.getElementById("td_bddestinationType"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bddestinationType"+index+"\" value=\""+destinationType+"\">";
                   col.innerHTML = destinationDisplay+hiddenfield;

				   col = document.getElementById("td_bddestinationID"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bddestinationID"+index+"\" value=\""+destinationID+"\">";
                   col.innerHTML = destinationID+hiddenfield;

				   col = document.getElementById("td_bdrequestID"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bdrequestID"+index+"\" value=\""+requestID+"\">";
                   col.innerHTML = displayRequestId+hiddenfield;

				   col = document.getElementById("td_bdargument"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bdargument"+index+"\" value=\""+argument+"\">";
                   col.innerHTML = displayArgument+hiddenfield;

				   col = document.getElementById("td_bdAutoEnabled"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bdAutoEnabled"+index+"\" value=\""+autoEnabled+"\">";
                   col.innerHTML = displayAutoEnabled+hiddenfield;
                   
				   col = document.getElementById("td_bdCheckPoint"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bdCheckPoint"+index+"\" value=\""+checkPoint+"\">";
                   col.innerHTML = displayCheckPoint+hiddenfield;

              
				   col = document.getElementById("td_bdCondition"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bdCondition"+index+"\" value=\""+condition+"\">";
                   col.innerHTML = displayCondition+hiddenfield;
     
				   col = document.getElementById("td_bdConditionType"+index);
      		       hiddenfield ="<input type=\"hidden\" name=\"bdConditionType"+index+"\" value=\""+conditionType+"\">";
                   col.innerHTML = displayConditionType+hiddenfield;
      }
