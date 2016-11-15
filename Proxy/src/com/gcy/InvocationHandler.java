package com.gcy;

import java.lang.reflect.Method;

public interface InvocationHandler {
	
	void invoke(Method m);
}
