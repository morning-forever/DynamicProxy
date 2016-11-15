package com.gcy;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Proxy {
	public static Object newProxyInstance(Class infce,InvocationHandler h) throws Exception{//JDK6 Compilor API,CGLib,ASM
		
		String rt =	"\n";
		
		String mthdStr = "";
		
		Method[] methods = infce.getMethods();
		
		for(Method m : methods){
			
			mthdStr += 
					"	@Override"+rt+
					"	public void "+m.getName()+"() {"+rt+
					"		try{"+rt+
					" 			Method m = "+infce.getName()+".class.getMethod(\""+m.getName()+"\");"+rt+
					"			h.invoke(m);"+rt+
					"		}catch(Exception e){"+rt+
					"			e.printStackTrace();"+rt+
					"		}"+rt+
					"	}"+rt;
		}
		
		String src =
			"package com.gcy;"+rt+
			"import java.lang.reflect.Method;"+rt+
			"public class $Proxy implements "+infce.getName()+"{"+rt+
			"	private InvocationHandler h;"+rt+
			"	public $Proxy(InvocationHandler h) {"+rt+
			"		super();"+rt+
			"		this.h = h;"+rt+
			"	}"+rt+
			
			mthdStr+rt+
			
			"}";
		
		//1.创建源码文件(create source file)
		String fileName = System.getProperty("user.dir")+"/src/com/gcy/$Proxy.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write(src);
		fw.flush();
		fw.close();
		
		//2.compile the source file and generate .class file
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
		Iterable units = fileMgr.getJavaFileObjects(fileName);
		CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
		t.call();
		fileMgr.close();
		
		//3.把字节码文件加载到内存并且创建一个对象（load .class file into memory and create an instance）
		URL[] urls = new URL[]{new URL("file:/"+System.getProperty("user.dir")+"/src")};
		URLClassLoader ucl = new URLClassLoader(urls);
		Class c = ucl.loadClass("com.gcy.$Proxy");
		
		Constructor ctr = c.getConstructor(InvocationHandler.class);
		
		Movable m =  (Movable) ctr.newInstance(h);
		
		return m;
	}
}
