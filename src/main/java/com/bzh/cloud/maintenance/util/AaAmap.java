package com.bzh.cloud.maintenance.util;

import java.util.HashMap;
import java.util.Map;

public class AaAmap<K,V> extends HashMap<String, V>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AaAmap( Map<String, V> map){
		map.forEach((K,V)->{
			this.put( Underline2Camel.underline2Camel(K, true),V);
		});
	}

}
