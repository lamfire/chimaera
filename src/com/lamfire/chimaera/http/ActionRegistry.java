package com.lamfire.chimaera.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.lamfire.chimaera.http.anno.ACTION;
import com.lamfire.logger.Logger;
import com.lamfire.utils.ClassLoaderUtils;

public class ActionRegistry {
	private static final Logger LOGGER = Logger.getLogger(ActionRegistry.class);
	private final Map<String,Action> mapping = new HashMap<String, Action>();
	
	public void mapping(String uri,Action action){
		mapping.put(uri, action);
		LOGGER.info("[mapping]:" + uri +" -> " + action.getClass().getName());
	}
	
	public Action lookup(String uri){
		return mapping.get(uri);
	}
	
	public void mappingPackage(String packageName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		Set<Class<?>> set = ClassLoaderUtils.getClasses(packageName);
		for(Class<?> clzz : set){
			if(!Action.class.isAssignableFrom(clzz)){
				continue;
			}
			
			ACTION action = clzz.getAnnotation(ACTION.class);
			if(action == null){
				LOGGER.warn("["+clzz.getName() + "] is assignable from Action,but not found 'ACTOIN' annotation.");
				continue;
			}

			String uri = action.path();
			Object obj = clzz.newInstance();
			mapping(uri,(Action)obj);
			
		}
	}
}
