package com.honda.ahm.lc.vdb.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.honda.ahm.lc.vdb.service.DataService;

public class ShiftDetailsController<S extends DataService> {
	
	@Autowired
	S dataService;
	
	@GetMapping(path = "getAll")
    public @ResponseBody List<String> findAllShift() {
        List<String> shiftDetailsList = dataService.getShiftDetailsDao().findAllShiftDetails();
        List<String> shiftList = dataService.getShiftDetailsDao().findAllShift();
        
        List<String> finalList = new ArrayList<>();
        finalList.addAll(shiftList);
        finalList.addAll(shiftDetailsList);
        Collections.sort(finalList);
        return finalList;
    }
	
}
