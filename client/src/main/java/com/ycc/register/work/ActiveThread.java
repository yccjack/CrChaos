
package com.ycc.register.work;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @add By CUCmehp, since 2013-06-09
 * @brief 该类从HBase借用过来，用来描述活动线程，原注释如下: Abstract class which contains a Thread
 *        and delegates the common Thread methods to that instance. The purpose
 *        of this class is to workaround Sun JVM bug #6915621, in which
 *        something internal to the JDK uses Thread.currentThread() as a monitor
 *        lock. This can produce deadlocks
 * @modify 增加m_bAborted标志位
 */
public abstract class ActiveThread implements Runnable, Stoppable, Abortable
{
    private Logger log = LoggerFactory.getLogger(ActiveThread.class);
    private final Thread thread;
    protected volatile boolean m_bStopped = false;
    protected volatile boolean m_bAborted = false;
    public boolean bRunning =false;
    public class ExitHandler extends Thread {
        private boolean bNeedStop = true;
        public ExitHandler(boolean bNeedStop) {  
            //super("Exit Handler");  
            this.bNeedStop=bNeedStop;
        } 
        public void run() {
            if(!isStopped()&&bNeedStop)
            {
                ActiveThread.this.stop("get signal kill or ctrl + c ");
            }
            while(bRunning)
            {
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }  
    } 
    public ActiveThread() 
    {
        m_bStopped = false;
        m_bAborted = false;
        this.thread = new Thread(this);
    }

    public ActiveThread(String name) 
    {
        m_bStopped = false;
        m_bAborted = false;
        this.thread = new Thread(this, name);
    }

    public Thread getThread() {
        return thread;
    }

    public abstract void run();

    // // Begin delegation to Thread

    public final String getName() {
        return thread.getName();
    }

    public void interrupt() {
        thread.interrupt();
    }

    public final boolean isAlive() {
        return thread.isAlive();
    }

    public boolean isInterrupted() {
        return thread.isInterrupted();
    }

    public final void setDaemon(boolean on) {
        thread.setDaemon(on);
    }

    public final void setName(String name) {
        thread.setName(name);
    }

    public final void setPriority(int newPriority) {
        thread.setPriority(newPriority);
    }

    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        thread.setUncaughtExceptionHandler(eh);
    }

    public void start() {
        thread.start();
    }

    public final void join() throws InterruptedException {
        thread.join();
    }

    public final void join(long millis, int nanos) throws InterruptedException {
        thread.join(millis, nanos);
    }

    public final void join(long millis) throws InterruptedException {
        thread.join(millis);
    }

    @Override
    public void abort(String why, Throwable e)
    {
        this.m_bAborted = true;
        log.info("ActiveThread: abort is set true");
        stop(why);
    }

    @Override
    public boolean isAborted()
    {
        return m_bAborted;
    }

    @Override
    public void stop(String strStopReason)
    {
        this.m_bStopped = true;
    }

    @Override
    public boolean isStopped()
    {
        return m_bAborted;
    }
    
}
