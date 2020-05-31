package com.ycc.register.work;

/***
 * 定义Sleepable接口，继承该接口的对象可以进行中断式的sleep操作的
 * @author MysticalYcc
 * @date 2020/5/29
 */
public interface Sleepable 
{
	/***
	 * 将当前线程sleep nMillis毫秒，但该操作会在服务停止时被换醒
	 * @param nMillis sleep的毫秒数
	 */
	 void sleep(long nMillis);
}
