package com.liuwa.shopping.util;


public class NameValuePair {
	
	final String name;
	final Object value;

	public NameValuePair(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return name + ":" + value;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Object getValue(){
		return this.value;
	}
}
