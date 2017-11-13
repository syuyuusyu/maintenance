package com.bzh.cloud.maintenance.controller;

import com.bzh.cloud.maintenance.dao.CmdbEntityDao;
import com.bzh.cloud.maintenance.dao.CmdbGroupDao;
import com.bzh.cloud.maintenance.dao.CmdbRecordDao;
import com.bzh.cloud.maintenance.entity.CmdbEntity;
import com.bzh.cloud.maintenance.entity.CmdbGroup;
import com.bzh.cloud.maintenance.service.CmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@RestController
@RequestMapping(value = "/cmdbConf")
public class CmdbController {

	@Autowired
	CmdbEntityDao cmdbEntityDao;

	@Autowired
	CmdbGroupDao cmdbGroupDao;

	@Autowired
	CmdbRecordDao cmdbRecordDao;
	
	@Autowired
	CmdbService cmdbService;

	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	@Transactional
	public Map<String, Object> tree(Integer parentId) {
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		//CmdbEntity parentEntity = cmdbEntityDao.findOne(parentId);
		List<CmdbEntity> list=cmdbEntityDao.findByParentId(parentId);
		map.put("children", list);
		return map;
	}

	@RequestMapping(value = "/grid")
	public Page<?> grid(CmdbEntity entity, Integer page, Integer limit) {
		Pageable pa = new PageRequest(page - 1, limit);
		return cmdbEntityDao.findByParentId(entity.getId(), pa);
	}

	@RequestMapping(value = "/saveOrupdate")
	@Transactional
	public Map<String, Object> saveOrupdate(CmdbEntity entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			cmdbEntityDao.save(entity);
			map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}

		return map;
	}

	@RequestMapping(value = "/delete")
	@Transactional
	public Map<String, Object> delete(Integer id, Integer type,
			Integer hierarchy) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			cmdbEntityDao.delete(id);
			List<CmdbEntity> chlids=cmdbEntityDao.findByParentId(id);
			if(chlids.size()>0){
				cmdbEntityDao.delete(chlids);
			}
			map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
		return map;
	}
	
	@RequestMapping(value = "/deleteRecord")
	@Transactional
	public Map<String, Object> deleteRecord(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			CmdbGroup g= cmdbGroupDao.findOne(id);
			g.getRecords();
			cmdbGroupDao.delete(g);
			map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
		return map;
	}
	
    @RequestMapping(value="/entity")
    @ResponseBody
    public List<CmdbEntity> entity(Integer parentId){
        return cmdbEntityDao.findByParentId(parentId);
    }
    
    @RequestMapping(value="/records")
    @ResponseBody
    public Map<String, Object> records(Integer parentId,Integer page, Integer limit){
    	Map<String, Object> map=new HashMap<String, Object>();
    	Pageable pa = new PageRequest(page - 1, limit);
    	List<Map<String, String>> result=cmdbService.records(parentId,pa);
    	map.put("totalElements", result.size());
    	map.put("content", result);
    	return map;
    }
    
	@RequestMapping(value = "/saveOrupdateGroup")
	@Transactional
	public Map<String, Object> saveOrupdateGroup(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> _param= request.getParameterMap();
		Map<String, String> param=conparam(_param);
		try {
			cmdbService.saveRroup(param);
			map.put("success", "true");
		} catch (Exception e) {
			map.put("success", e.getStackTrace());
		}
		return map;
	}
	
	private Map<String, String> conparam(Map<String, String[]> _param){
		Map<String, String> param=new HashMap<String, String>();
		Set<Entry<String, String[]>> keySet=_param.entrySet();
		for (Entry<String, String[]> entry : keySet) {
			Object[] objs=entry.getValue();
			String str="";
			if(objs.length>1){
				for(int i=0;i<objs.length;i++){
					if(objs.length-1==i){
						str+=(String) entry.getValue()[i];
					}else{
						str+=(String) entry.getValue()[i]+",";
					}
				}
				
			}else{
				str=(String) entry.getValue()[0];
			}
			param.put(entry.getKey(), str);
		}
		return param;
	}
    

}
