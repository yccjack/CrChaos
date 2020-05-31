package com.ycc.register.work;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 定义基础工作线程对象，该对象可以承载某项具体的业务逻辑
 * @author CUCmehp
 * @add since 2013-06-18
 */
public abstract class BaseWorker extends ActiveThread implements Sleepable
{
    private Logger log = LoggerFactory.getLogger(BaseWorker.class);
    public BaseWorker()
    {
        Runtime.getRuntime().addShutdownHook(new ExitHandler(false));  
    }
    /***
     * 返回该模块是否已经停止，由于该方法实现比较固定，因此，禁止了子类对其进行重载。
     */
    @Override
    public final boolean isStopped() 
    {
        return m_bStopped;
    }
    
    /***
     * 返回该模块是否abort，由于该方法实现比较固定，因此，禁止了子类对其进行重载。
     */
    @Override
    public final boolean isAborted() 
    {
        return m_bAborted;
    }

    /***
     * 停止该处理模块，由于该方法实现比较固定，因此，禁止了子类对其进行重载。
     * 该方法仅设置标志位
     * @param why 停止原因
     */
    @Override
    public final void stop( String why ) 
    {
        log.info( "Sending stop signal, waiting for ProcessModule " + this.getName() + " to stop, reason: " + why );
        this.m_bStopped = true;
        synchronized( m_objWaitEvent ) 
        {
            m_objWaitEvent.notifyAll();
        } 
        while(bRunning)
        {
            sleep(10);
        }
    }
    
    /***
     * 该类由框架中的线程负责调用，退出该线程模块，最大等待时间为nMillSec毫秒，若在该超时时间内返回，说明模块成功退出，
     * 则返回true，若超时，返回false。
     * @param why 停止原因
     * @param nMillSec 最大超时等待的毫秒数
     */
    public final boolean stopAndCheck( String why, int nMillSec )
    {
        try 
        {
            long lStart = System.currentTimeMillis();
            this.stop( why );
            this.join( nMillSec );
            long lEnd = System.currentTimeMillis();
            boolean bRet = !this.isAlive();
            if( bRet )
            {
                log.info( this.getName() + " Stop succeed by stopAndCheck, time cost:" + ( lEnd - lStart ) + " ms" );
            }
            else
            {
               log.warn( this.getName() + " Stop failed by stopAndCheck, time cost:" + ( lEnd - lStart ) + " ms, it is still running!" );
            }
            return bRet;
        } 
        catch( InterruptedException e ) 
        {
           log.warn( this.getName() + " Stop failed by stopAndCheck, exception: " + e );
            return this.isAlive();
        }
    }
    
    /***
     * 该处理模块由于内部异常abort，由于该方法实现比较固定，因此，禁止了子类对其进行重载。
     * @param why 停止原因
     */
    @Override
    public void abort(String why, Throwable e) 
    {
        this.m_bAborted = true;
        if( e != null )
        {
            //打印出异常队列到console
//            e.printStackTrace();
            //将异常消息打印到日志中
           log.error( why + ", Throwable: " + e );
        }
        stop( this.getName() + " Aborting, " + why );
    }
    
    /***
     * sleep该处理模块nMillsec毫秒，若其间该模块被退出，sleep过程会中途返回。
     */
    @Override
    public final void sleep( long nMillsec )
    {
        try
        {
            synchronized( m_objWaitEvent ) 
            {
                m_objWaitEvent.wait( nMillsec );    
            }
        }
        catch( Exception exc )
        {
            return;
        }
    }
    
    /***
     * 初使化业务逻辑，需在入口处先初始化日志模块
     * @param strConf 配置文件路径
     * @return
     */
    //public abstract EnumRetType init();
    
    /***
     * 服务退出时会调用该接口
     */
    public abstract void terminate();
    
    /***
     * 业务逻辑处理流程
     */
    public abstract void process();
    
    /***
     * worker的线程入口 
     */
    @Override
    public final void run()
    {
        bRunning = true;
       log.info( "worker "+this.getName() + " Start!" );
        try 
        {
            process();
        } 
        catch( Throwable t )
        {
            if(!m_bTerminate)
            {
                m_bTerminate = true;
                terminate();
            }
            bRunning =false;
            abort( "Unhandled exception." + this.getName() + " Starting shutdown.", t );
          //  AppBaseService.stop( "Unhandled exception." + this.getName() + " Starting shutdown." + t );
        } 
        finally 
        {
            if(!m_bTerminate)
            {
                m_bTerminate = true;
                terminate();            }
            bRunning =false;
           log.info("stop flag: "+m_bStopped+", abort flag: "+m_bAborted);
           log.info( "worker " + this.getName() + " Stop!" );
        }
    }
    
    /***
     *  设置worker的名称 
     * @param strModuleName
     */
    /*public final void setModuleName( String strModuleName )
    {
        this.getName() = strModuleName;
    }*/
    
    //模块的名称
    //protected String this.getName() = "";
    //等待对象
    private Object m_objWaitEvent = new Object();
    
    private boolean m_bTerminate =false;
}
