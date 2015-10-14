/*
*2015.10.14:用于打印方法目前的调用堆栈，输出的信息中最上面的是当前的函数
*
*
*
*
*
*
*/	
	
public class StackUtils{	
	public static void printStack(String funcname){
		String tag = "-------------------------------------";
		print(tag+" : "+funcname+tag);
		Map<Thread, StackTraceElement[]> map =  Thread.getAllStackTraces();
		StackTraceElement[] stack = map.get(Thread.currentThread());
		for (StackTraceElement s:stack){
			print(s);
		}
	}
	
	private static void print(Object obj){
		System.out.println(obj);			
	}
}