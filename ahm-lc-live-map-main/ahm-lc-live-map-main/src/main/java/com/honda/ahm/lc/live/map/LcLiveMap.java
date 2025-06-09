package com.honda.ahm.lc.live.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LcLiveMap</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Apr 1, 2021
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.honda.ahm.lc.live.map" })
public class LcLiveMap {

    public static void main(String[] args) {
        SpringApplication.run(LcLiveMap.class, args);
    }
}
