package com.honda.ahm.lc.handlers;

import java.util.List;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;

public interface IStatusMessageHandler {

    List<String> handle(StatusMessage statusMessage,StatusEnum status);
    
   

}
