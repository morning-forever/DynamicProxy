package com.gcy;
/**
 *	  一、不要急，慢慢来
 * 	  二、newProxyInstance(Movable.class,h)里面：
 * 	 1.首先生成"代理代码"。"代理代码"其实是一个类的源码，这段源码是这样的：
 * 	 （1）这个类里面的所要用到的类要import(import java.lang.reflect.Method)
 *	 （2）这个类的名称任意($Proxy)
 *	 （3）这个类要实现Movable接口(传过来的接口的参数)
 *	 （4）这个类里有个构造方法，构造方法的参数为InvocationHandler类型
 * 	 （5）这个类对Movable接口里的每个方法都代理，也就是说方法名称与Movable接口里的每个方法一一对应，每个方法的关键实现代码都是h.invoke(m),m传的是当前的"方法对象"
 *（ 在这里，这个方法对象到底是怎么实现的并不能确定，怎么实现的要看具体的Movable实现类）
 *	 至此，这个类的源码已经完成。	
 *	 2.利用这段源码生成一个.java文件，并编译生成的.java文件产生对应的.class文件。
 *	 3.把这个.class load to memory。
 *	 4.这时就可以获得这个代理类的构造器，用这个构造器newInstance(args[]),产生一个实例，这个实例就是一个代理实例。执行这个代理实例的每个方法，都将执行h.invoke(m)
 * 	 5.执行h.invoke(m)为什么会执行产生相应的代理呢？原来，实例h对应的类：InvocationHandler的实现类，是这么实现的：
 * 	 （1）这个类里有一个成员变量Object target，代表的是被代理类。
 *	 （2）invoke(m)方法中，在m.invoke(target)前后加相应的代理逻辑。（注意m是从代理类传过来的一个Method对象）
 *	 6.这样就实现了动态代理（代理类是动态生成的，生成代理类需要“被代理接口”和“代理处理”这两个参数，其中“代理处理”里面包含了被代理对象。但是当执行m.invoke(target)时，
 *	 为什么会执行到target里面的对应方法的代码呢？原因是，因为m是由代理对象动态传过来的，此时m的方法名称一定跟代理方法的名称一样，只不过这时执行的是target里对应的方法了）
 *	 三、当执行m.move()时，执行的是那个代理对象的move()方法，而代理对象的move()方法，又去执行h的invoke(m)方法,而h的invoke(m)在加上代理逻辑之后通过执行m.invoke
 *	 target)来执行target里面的对应的方法。
 *
 *	 四、为什么InvocationHandler里面的target是Object呢？为什么InvocationHandler里面代理逻辑的中间非要使用反射的方式来调用呢？
 *	 1.第一个问题，其实不是Object也可以，在我看来，在实际需求中，用需要被代理的方法所在接口就行了。
 *	 2.第一个问题，如果用普通的调用方法的方式，其实也可以。只不过代码会更麻烦，需要通过m获得方法名称等。（总结，只要我知道一个方法对象（Method对象），并且知道一个拥有此方法的对
 *	  象，我就可以调用这个方法）
 *	
 *	 
 * 
 * 	
 */
public class Client {
	public static void main(String args[]) throws Exception {
		Tank tank = new Tank();
		/*TimeInvokeHandler timeH = new TimeInvokeHandler(tank);
		LogInvokeHandler logH = new LogInvokeHandler(timeH);
		Movable m = (Movable)Proxy.newProxyInstance(Movable.class,logH);*/
		
		LogInvokeHandler logH = new LogInvokeHandler(tank);
//		TimeInvokeHandler timeH = new TimeInvokeHandler(logH);
		Movable m = (Movable)Proxy.newProxyInstance(Movable.class,logH);
		
		
		m.stop();
	}
}
