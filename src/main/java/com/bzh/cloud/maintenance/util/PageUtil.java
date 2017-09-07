package com.bzh.cloud.maintenance.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtil {
	
	public static Pageable createPageRequest(Integer start,Integer limit){
		Integer page=0;
		Integer size=0;
		return new PageRequest(page, size);
	}

}
