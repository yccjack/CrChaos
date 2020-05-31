package com.ycc.register.work;

/***
 * 定义一个服务是否是可停止的
 * @author MysticalYcc
 * @date 2020/5/29
 */
public interface Stoppable 
{
	  /**
	   * 停止服务
	   * @param strStopReason: 停止服务的原因
	   */
	   void stop(String strStopReason);

	  /**
	   * 判断服务是否已经被停止
	   * @return True if {@link #stop(String)} has been closed.
	   */
	   boolean isStopped();
}
