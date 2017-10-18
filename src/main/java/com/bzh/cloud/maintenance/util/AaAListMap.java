package com.bzh.cloud.maintenance.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AaAListMap extends ArrayList<AaAmap<String, Object>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AaAListMap(List<Map<String, Object>> list){
		list.forEach((M)->{
			this.add(new AaAmap<String, Object>(M));
		});

	}

}
