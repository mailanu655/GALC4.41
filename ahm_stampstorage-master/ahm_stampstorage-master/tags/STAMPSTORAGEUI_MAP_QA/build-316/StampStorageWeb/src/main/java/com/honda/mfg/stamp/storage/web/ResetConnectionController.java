package com.honda.mfg.stamp.storage.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.honda.mfg.stamp.storage.service.ServiceConnectionManager;

@Controller
@RequestMapping("/resetConnection")
public class ResetConnectionController {
	@Autowired
	ServiceConnectionManager manager;
	
	@RequestMapping(method = RequestMethod.GET)
    public String ResetNow(  HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse, Model uiModel) {
		String result = "UNKNOWN";
	try{
		manager.updateServiceRoles();
		result = "Successful! Host: " + manager.getServiceRole().getIp() + ":" + manager.getServiceRole().getPort();
	}catch (Exception e){
		result = "Unsuccessful " + e.getMessage();
	}
		 uiModel.addAttribute("resetResult", result);
		return "reset/resetConnection";
	}
	
}
