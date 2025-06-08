package com.honda.galc.qics.mobile.client.widgets.form;

public interface Verifiable {

	public String getValueAsString();
	
	public String getTitle();
	
	public void addVerifiers( Verifier... verifiers );
	
	public void addVerifier( Verifier verifier );
	
//	public boolean validate( Verifiable item );


}
