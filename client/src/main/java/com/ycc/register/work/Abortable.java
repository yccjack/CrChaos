package com.ycc.register.work;

/**
 *   @author MysticalYcc
 *   @date 2020/5/29
 */
public interface Abortable 
{
	  /**
	   * Abort the server or client.
	   * @param why Why we're aborting.
	   * @param e Throwable that caused abort. Can be null.
	   */
	  public void abort(String why, Throwable e);
	  
	  /**
	   * Check if the server or client was aborted. 
	   * @return true if the server or client was aborted, false otherwise
	   */
	  public boolean isAborted();
}
