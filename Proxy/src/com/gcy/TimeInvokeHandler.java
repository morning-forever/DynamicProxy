package com.gcy;

import java.lang.reflect.Method;

public class TimeInvokeHandler implements InvocationHandler{
	
	private Object target;
	
	private InvocationHandler h;
	
	public  TimeInvokeHandler(Object target) {
		this.target = target;
	}

	public TimeInvokeHandler(InvocationHandler h) {
		this.h = h;
	}
	@Override
	public void invoke(Method m) {
		long start = System.currentTimeMillis();
		System.out.println("start time:"+start);
		
		try {
			if(this.h!=null){
				h.invoke(m);
			}else{
				m.invoke(target);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		long end = System.currentTimeMillis();
		System.out.println("end time:"+end);
		System.out.println("waste time:"+(end-start));
	}

}
