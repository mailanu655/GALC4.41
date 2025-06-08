/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.honda.galc.mobile.qicsbrowser;

import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;

import android.os.Bundle;
import android.util.Log;

public class QicsBrowser extends DroidGap
{
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	// Set an application timeout so that if the application
    	// has a startup problem, the user is not stuck with a 
    	// black screen.
    	super.setIntegerProperty("loadUrlTimeoutValue", 25000);
        super.onCreate(savedInstanceState);

        // Set by <content src="index.html" /> in config.xml
        String urlToOpen = Config.getStartUrl();
        Log.v("URL TO OPEN", urlToOpen); 
      
        super.loadUrl( urlToOpen );
    }
    
    


}

