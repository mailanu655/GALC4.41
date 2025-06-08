package com.honda.galc.qics.mobile.client.utils;

public final class ElementUtil {
	
	
	public static native void clickElement(com.google.gwt.user.client.Element elem) /*-{
    elem.click();
}-*/;

	public static native void selectElement(com.google.gwt.user.client.Element elem) /*-{
    elem.select();
}-*/;
	
	public static native void blurElement(com.google.gwt.user.client.Element  elem) /*-{
    elem.blur();
}-*/;
	
	
	/**
	 * Triggers a mouse event on an element.
	 * 
	 * @param elem
	 * @param eventName  values such as: click, mousedown, mouseup
	 */
	public static native void mouseeventElement( com.google.gwt.user.client.Element elem, String eventName ) /*-{
	
    var ev = document.createEvent("MouseEvent");
    var rect = elem.getBoundingClientRect();
    
    var y = rect.top ;
	var x = rect.left;
	var bubble = true;
	var cancelable = true;
	var leftButton = 0;
    ev.initMouseEvent(
        eventName,
        bubble, cancelable,
        window, null,
        x, y, 0, 0, 
        false, false, false, false, 
        leftButton, null
    );
    elem.dispatchEvent(ev);
}-*/;
	
	public static native int  top( com.google.gwt.user.client.Element  elem) /*-{
	var rect = elem.getBoundingClientRect();
	console.log(rect.top, rect.right, rect.bottom, rect.left);
	return rect.top;
}-*/;
	 
	public static native int  bottom( com.google.gwt.user.client.Element  elem) /*-{
	var rect = elem.getBoundingClientRect();
	console.log(rect.top, rect.right, rect.bottom, rect.left);
	return rect.bottom;
}-*/;	

	public static native int  left( com.google.gwt.user.client.Element  elem) /*-{
	var rect = elem.getBoundingClientRect();
	console.log(rect.top, rect.right, rect.bottom, rect.left);
	return rect.left;
}-*/;
	
	public static native int  right( com.google.gwt.user.client.Element  elem) /*-{
	var rect = elem.getBoundingClientRect();
	console.log(rect.top, rect.right, rect.bottom, rect.left);
	return rect.right;
}-*/;	
	
	public static void mousedownElement(com.google.gwt.user.client.Element elem) {
		mouseeventElement( elem, "mousedown");
	}
	
	public static void mouseupElement(com.google.gwt.user.client.Element elem) {
		mouseeventElement( elem, "mouseup");
	}

	private ElementUtil(){}
}
