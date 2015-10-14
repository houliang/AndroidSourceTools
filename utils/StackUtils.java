/*
*2015.10.14:���ڴ�ӡ����Ŀǰ�ĵ��ö�ջ���������Ϣ����������ǵ�ǰ�ĺ���
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