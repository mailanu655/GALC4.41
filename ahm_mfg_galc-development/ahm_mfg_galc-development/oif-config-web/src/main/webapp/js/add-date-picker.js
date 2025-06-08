function setTime(src, dstField) {
	var value = src.value;
	var timeValue = " xx:xx:xx";
	var field = document.getElementById(dstField);
	var tokens = field.value.substr(1).split(":");
	switch(src.id) {
		case "hourInput":
			tokens[0] = value;
			break;
		case "minuteInput":
			tokens[1] = value;
			break;
		case "secondInput":
			tokens[2] = value;
			break;
	}

	
	field.value = " " + tokens[0] + ":" + tokens[1] + ":" + tokens[2];
}

