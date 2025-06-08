		function boldText(sObjectID) {
		// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
			document.getElementById(sObjectID).style.background='#cccccc';
			//document.getElementById(sObjectID).style.fontWeight='bold';
			//document.getElementById(sObjectID).class='activetab';
		}

		function normalText(sObjectID) {
		// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
			document.getElementById(sObjectID).style.background='#f0f0f0';
				//		document.getElementById(sObjectID).style.fontWeight='normal';
			//document.getElementById(sObjectID).class='inactivetab';
		}


		function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		   
			document.getElementById(sObjectID).style.display='';
		}
		
		function hide(sObjectID) {
			
		// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
			document.getElementById(sObjectID).style.display='none';
		}
		
		function toggleVisibility(sObjectID) {
		// USED TO TOGGLE VISIBILITY OF AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
			if(document.getElementById(sObjectID).style.display=='none') {
				show(sObjectID);
			} else {
				hide(sObjectID);
			}
		}
		
