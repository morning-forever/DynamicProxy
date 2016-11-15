package com.gcy;

import java.lang.reflect.Method;

public class LogInvokeHandler implements InvocationHandler{
	
	private Object target;
	
	private InvocationHandler h;
	
	public  LogInvokeHandler(Object target) {
		this.target = target;
	}

	public LogInvokeHandler(InvocationHandler h) {
		this.h = h;
	}
	@Override
	public void invoke(Method m) {
		System.out.println(m.getName()+"方法开始======");
		
		try {
			if(this.h!=null){
				h.invoke(m);
			}else{
				m.invoke(target);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		System.out.println(m.getName()+"方法结速======");
	}

}
