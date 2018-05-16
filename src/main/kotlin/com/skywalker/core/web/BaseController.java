package com.skywalker.core.web;

import com.skywalker.core.exception.ServiceException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseController
{
	protected Map<String,String> handleException(Exception e,String errorCode, String errorObject)
	{
		Map<String,String> map = new HashMap();
		String errorKey = "-1000";//系统异常
		String errorMsg = "系统异常";//return error massage

		ServiceException se;
		if(e instanceof ServiceException == false)
		{
			e.printStackTrace();
		}
		else
		{
			se = (ServiceException)(e.getCause() != null? ((e.getCause() instanceof ServiceException)? e.getCause(): new ServiceException(e.getMessage(), e)) : e);
			if(e.getMessage() != null && e.getMessage().length() > 0)
			{
				errorMsg = e.getMessage();
			}else
			{
				errorMsg = "处理失败。";
			}
			if(se.getErrorKey()!= null && se.getErrorKey().length() > 0)
			{
				errorKey = se.getErrorKey();
			}else
			{
				errorKey = "-1000";
			}
			e.printStackTrace();
		}
		if(!StringUtils.isEmpty(errorObject))
		{
			map.put("errorKey",errorCode);
			map.put("errorMsg",errorObject);
		}
		else
		{
			map.put("errorKey",errorKey);
			map.put("errorMsg",errorMsg);
		}
		return map;
	}
}
