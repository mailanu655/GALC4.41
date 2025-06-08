package com.honda.galc.net;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream{
	
	InputStream in;
	byte[] buffer;
	int buffer_position = 0;
	int position = 0;
	int buffer_size = 20;
	public BufferedInputStream(InputStream in) {
		
		this.in = in;
		buffer = new byte[buffer_size];
		
	}
	
	@Override
	public int read() throws IOException {
		if(position >= buffer_size) return in.read();
		if(position < buffer_position) return buffer[position++];
		else {
			buffer[position++] = (byte)in.read();
			buffer_position = position;
			return buffer[position-1];
		}
	}
	
	@Override
	 public synchronized void reset() throws IOException {
		if(position > buffer_position) in.reset();
		position = 0;
	}		
	
	@Override
	public int read(byte b[], int off, int length) throws IOException {
		if(position >= buffer_size) return in.read(b, off, length);
		int len = buffer_position - position;
		
		if(position < buffer_position) {
		    for(int i = 0; i<len; i++) {
		    	b[i+off] = buffer[i+position];
		    }
		    position = buffer_position;
		}
	    int size = in.read(b,off + len, length - len);
	    int count = buffer_size - position > size ? size : buffer_size - position;
	    for (int i = 0; i<count; i++) {
	    	buffer[position++] = b[off+len + i];
	    }
	    buffer_position = position;
	    return size + len;
	}
	
	public boolean isXML() throws IOException {

		String str = "<?xml";
		boolean isXML = true;
		for(int i = 0; i<str.length();i++) {
			if((char)this.read() != str.charAt(i)) {
				isXML = false;
				break;
			}
		}
		this.reset();
		return isXML;
	}
	
		
}
