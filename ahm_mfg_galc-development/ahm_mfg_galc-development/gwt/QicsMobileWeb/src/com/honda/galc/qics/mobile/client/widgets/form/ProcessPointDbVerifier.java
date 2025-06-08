package com.honda.galc.qics.mobile.client.widgets.form;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.honda.galc.qics.mobile.client.dataservice.DataServiceFactory;
import com.honda.galc.qics.mobile.client.dataservice.ProcessPointDataService;
import com.honda.galc.qics.mobile.shared.entity.ProcessPoint;

public class ProcessPointDbVerifier extends Verifier {

	@Override
	protected boolean validate(final Verifiable stringItem ) {
		assert( stringItem != null );
		String processPointId = stringItem.getValueAsString();
		if ( processPointId == null ) {
			addMessage( stringItem, "is required");
			return false;
		}
		
		ProcessPointDataService service = DataServiceFactory.getInstance().getProcessPointDataService();
		service.findById(processPointId, new MethodCallback<ProcessPoint>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				addMessage( stringItem, "error validating process point");				
			}

			@Override
			public void onSuccess(Method method, ProcessPoint response) {
				if ( response == null ) {
					addMessage( stringItem, "not found.");				
				} 
			}} );


		return true;
	}

}
