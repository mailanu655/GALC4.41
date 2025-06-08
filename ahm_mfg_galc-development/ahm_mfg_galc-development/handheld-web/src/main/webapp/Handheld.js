function getParameter(parameterName) {
	var url = new URL(window.location.href);
	return url.searchParams.get(parameterName);
}
function fixLabel() {
	document.getElementById("label").innerHTML = document.getElementById("label").innerHTML + " " + getParameter("plant");
}
function getDropDownSelectionFrom(dropdownListName) {
	var dropdown = document.getElementById(dropdownListName);
	if (dropdown.selectedIndex == -1)
			return "0";
	return dropdown.options[dropdown.selectedIndex].value;
}

function mySplit(str, ch) {
	var pos, start = 0, result = [];
	while ((pos = str.indexOf(ch, start)) != -1) {
		result.push(str.substring(start, pos));
		start = pos + 1;
	}
	result.push(str.substr(start));
	return(result);    
}

function validateInput(input, masksString) {
	var masks = mySplit(masksString, ',');
	for (var i=0; i<masks.length; i++) {
		if(new RegExp(regExMaskFrom(masks[i]), "i").test(input))
			return true;
	}
	return false;   
}

function regExMaskFrom(aMask) {
	if (aMask.indexOf('\\') == -1) {
		return addHardStopsTo((regExSubstringFrom(aMask)));
	}
	var massagedMask = "";
	var maskChunksAroundSlashes = aMask.split('\\');
	for (var i = 0; i < maskChunksAroundSlashes.length; i++) {
		currentSlashIndex = currentSlashIndex + 1;
		var currentMaskChunk = maskChunksAroundSlashes[i];
		massagedMask = massagedMask + regExSubstringFrom(currentMaskChunk);
		if (i != maskChunksAroundSlashes.length) {
			massagedMask = massagedMask + '\\';
		}
	}
	return addHardStopsTo(massagedMask);
}

function addHardStopsTo(aMask) {
	massagedMask = aMask;
	//add a hard stop at the start of the string if the mask doesn't
	//have a wildcard at the front
	if (massagedMask.substring(0,1) != "*")
		massagedMask = "\^" + massagedMask;

	//add a hard stop at the end of the string if the mask doesn't
	//have a wildcard at the front
	if (massagedMask.substring(massagedMask.length-1) != "*")
		massagedMask = massagedMask + "\\b";
	
	return(massagedMask);
}

function regExSubstringFrom(aMaskSubstring) {
	return aMaskSubstring.replace(/\*/g, "[\\s\\S]*").replace(/\#/g, "\\d").replace(/\^/g, "[a-zA-Z_0-9]").replace(/\?/g, "[\a-z\A-Z]").replace(/\%/g, "[\\w\\W]{1}");
}
