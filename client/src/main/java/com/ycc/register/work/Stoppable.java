package com.ycc.register.work;

/***
 * ����һ�������Ƿ��ǿ�ֹͣ��
 * @author MysticalYcc
 * @date 2020/5/29
 */
public interface Stoppable 
{
	  /**
	   * ֹͣ����
	   * @param strStopReason: ֹͣ�����ԭ��
	   */
	   void stop(String strStopReason);

	  /**
	   * �жϷ����Ƿ��Ѿ���ֹͣ
	   * @return True if {@link #stop(String)} has been closed.
	   */
	   boolean isStopped();
}
