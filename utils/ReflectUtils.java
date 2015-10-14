/*2015.10.8
 * 作用：根据对象获取某个super类部分的属性值，这里面主要利用了反射原理，并根据class的superclass逐层往上
 *      从而遍历整个继承链上的所有属性值，包括private属性值 
 *
 2015.10.14：添加获取指定super类指定属性的方法，还修改了正则表达式的bug
 *
 */

//需要修改这个包名称
package com.example.applicationtest;

import java.util.Arrays;
import java.util.List;


public class ReflectUtils {
	
	/*列出某个对象的所有super类的属性值，通过逐层往上super，直到继承连的顶端，但是不包括对象本身的属性
	 * _obj：某个类的对象
	 *
	 * Example：
	 * 	ReflectUtils.getParentProperties(对象obj);
	 * Output:
	 * 	------------------------class name:""-------------------------
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 
	 */
	public static void getAllParentAllProperties(final Object _obj) {
		if (_obj == null){
			println("getParentProperties参数存在null");
			return;
		}
		Class<?> cl = _obj.getClass();
		while (cl != null){
			getFields(cl, _obj);
			cl = cl.getSuperclass();
		}
		
	}
	
	/*列出某个对象的指定super类的属性值，通过逐层往上super，直到找到指定的super类或者到达继承连的顶端
	 * _obj：某个类的对象
	 * className:需要打印的的类的名称
	 * 
	 * Example：
	 * 	ReflectUtils.getSpecificParentProperties(对象obj, obj向上的某个super类的类名);
	 * Output:
	 * 	super ClassName
	 * 	super super ClassName
	 * 	------------------------class name:""-------------------------
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 
	 */	
	public static void getSpecificParentAllProperties(final Object _obj, String className){
		if (_obj == null || isEmpty(className)){
			println("getSpecificParentProperties参数存在null");
			return;
		}
		
		//Class<?> clSuper = _obj.getClass();
		Class<?> specifiedClass = getSpecifiedClassFromSonObj(_obj, className);
		if (specifiedClass == null){
			println("can not find super class "+className+", not extends from it");
			return;
		}
		getFields(specifiedClass, _obj);

	}
	
	
	/*列出某个对象的super类的属性值（向上一层，直接父类），但是不包括对象本身的属性
	 * _obj：某个类的对象
	 * 
	 * Example：
	 * 	ReflectUtils.getDirectParentProperities(对象obj);
	 * Output:
	 * 	------------------------class name:""-------------------------
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 
	 */
	public static void getDirectParentAllProperities(final Object _obj){
		if (_obj == null){
			println("getDirectParentProperities参数存在null");
			return;
		}
		
		Class<?> cl = _obj.getClass();
		Class<?> clSuper = cl.getSuperclass();
		if (clSuper == null){
			println(cl.getName() + " has no super class");
			return;
		}
		
		getFields(clSuper, _obj);
	}
	
	/*列出某个对象的本身的属性
	 * _obj：某个类的对象
	 * Example：
	 * 	ReflectUtils.getObjectFields(对象obj);
	 * Output:
	 * 	------------------------class name:""-------------------------
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 
	 */	
	public static void getObjectAllFields(final Object _obj){
		if (_obj == null){
			println("getObjectFields参数存在null");
			return;
		}
		Class<?> cl = _obj.getClass();
		getFields(cl, _obj);
	}
	
	/*列出某个对象super对象的指定属性，可以同时获取多个
	 * _obj：某个类的对象
	 * className:_obj的super类的名称
	 * propertyName：super类中某些属性的名称
	 * 
	 * Example：
	 * 	ReflectUtils.getObjectFields(对象obj);
	 * Output:
	 * 	------------------------class name:""-------------------------
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 
	 */		
	public static void getSpecifiedClassSpecifiedProperties(final Object _obj, String className, List<String> propertyName){
		if (_obj == null || className == null || propertyName == null){
			println("getSpecifiedClassSpecifiedProperties参数存在null");
			return;
		}
		Class<?> specifiedClass = getSpecifiedClassFromSonObj(_obj, className);
		if (specifiedClass == null){
			println("can not find super class "+className+", not extends from it");
			return;
		}
		getFields(specifiedClass, _obj, propertyName);
	}
	
	
	/*列出某个对象super对象的指定属性，一次只能获取一个属性
	 * _obj：某个类的对象
	 * className:_obj的super类的名称
	 * propertyName：super类中某个属性的名称
	 * 
	 * Example：
	 * 	ReflectUtils.getObjectFields(对象obj);
	 * Output:
	 * 	------------------------class name:""-------------------------
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 	TYPE:int   NAME:android.content.Context.BIND_NOT_VISIBLE   VALUE:1073741824
	 * 
	 */		
	public static void getSpecifiedClassSpecifiedProperty(final Object _obj, String className, String propertyName){
		if (_obj == null || className == null || propertyName == null){
			println("getSpecifiedClassSpecifiedProperties参数存在null");
			return;
		}
		Class<?> specifiedClass = getSpecifiedClassFromSonObj(_obj, className);
		if (specifiedClass == null){
			println("can not find super class "+className+", not extends from it");
			return;
		}
		if (!getFields(specifiedClass, _obj, Arrays.asList(propertyName))){
			println("can not find propertyName from specified class "+className+", not exits");
		}
	}
	
	private static boolean getFields(final Class<?> _cl, final Object _obj, List<String> propertyName){
		println("------------------------class name:"+_cl.getName()+"-------------------------");
		java.lang.reflect.Field[] field = (java.lang.reflect.Field[]) _cl.getDeclaredFields();
			
		boolean hasOutput = false;
		
		// 调用public ,private变量类型和值
		for (int i = 0; i < field.length; i++) {
			
			if (propertyName!=null && !propertyName.contains(field[i].getName())){
				continue;
			}
			
			hasOutput = true;
			
			StringBuilder sb = new StringBuilder();
			//参考Field的toString方法
			sb.append("TYPE:" + field[i].getType().getName());
			sb.append("   NAME:" + field[i].getDeclaringClass().getName()+"."+field[i].getName());
			field[i].setAccessible(true);// 修改访问控制权限
			Object object = null;
			try {
				object = field[i].get(_obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (object == null)
				sb.append("   VALUE: null");
			else
				sb.append("   VALUE:" + object.toString() + "\n");
			println(sb.toString());			
		}	
		return hasOutput;
	}
	
	private static void getFields(final Class<?> _cl, final Object _obj){
		getFields(_cl, _obj, null);
	}
	
	
	static private Class<?> getSpecifiedClassFromSonObj(Object _obj, String classname){
		Class<?> clSuper = _obj.getClass();
		while (clSuper != null){
			println(clSuper.getName());
			if (matchClass(clSuper, classname)){//利用String的正则matches函数
				//getAllFields(clSuper, _obj);
				return clSuper;
			}			
			clSuper = clSuper.getSuperclass();
		}
		return null;
	}
	
	static private boolean matchClass(final Class<?> _cl, String classname){
		if (_cl == null)
			return false;
		return _cl.getName().matches(".*\\."+classname);
	}
	
	static void print(Object obj){
		if (obj == null)
			System.out.print("null");
		else
			System.out.print(obj);
	}
	
	static void println(Object obj){
		if (obj == null)
			System.out.println("null");
		else
			System.out.println(obj);
	}
	
	static void println(){
		System.out.println();
	}
	
    static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
