package com.ycc.register.work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 服务基础类，用来定义服务的基类，子类需继承该类，并实现abstract方法
 * @author CUCmehp
 */
public abstract class AppBaseService extends ActiveThread
{
    private Logger log = LoggerFactory.getLogger(AppBaseService.class);
    public AppBaseService()
    {
        Runtime.getRuntime().addShutdownHook(new ExitHandler(true));  
    }
    @Override
    public final boolean isStopped() 
    {
        return m_bStopped;
    }

    @Override
    public final void stop( String why ) 
    {
       log.info("Sending stop signal, waiting for service " + this.getName() + " to stop, reason: "+ why );
        this.m_bStopped = true;
        // We wake up the stopSleeper to stop immediately
        stopSleeper.skipSleepCycle();
    }
    
    @Override
    public final void abort(String why, Throwable e) 
    {
        this.m_bAborted = true;
        if( e != null )
        {
            //打印出异常队列到console
//            e.printStackTrace();
           log.error( why + ", Throwable: " + e );
        }
        stop(" Aborting, " + why );
    }

    @Override
    public final boolean isAborted() 
    {
        return m_bAborted;
    }
    
    // Check if we should stop every second.
    private Sleeper stopSleeper = new Sleeper(1000, this);
    private final void loop() 
    {
        while( !this.m_bStopped )
        {
            stopSleeper.sleep();
        }
    }
    
    
    /***
     * 初使化业务逻辑，需在入口处先初始化日志模块
     * @param strConf 配置文件路径
     * @return
     */
    public abstract EnumRetType init( String strConf );
    
    /***
     * 添加业务逻辑，该函数内中的module必须有自己的线程，即该函数不能被block
     */
    public abstract void startAllModule();
    
    /***
     * 服务退出时会调用该接口，调用每个模块的stopAndCheck方法，每个module默认最长停止时间为1秒。
     */
    public abstract void stopAllModule();
    
    /**
     * 
     * 模块退出后资源释放
     */
    public abstract void terminate();
    
//  {
//      try
//      {
//          for( BaseWorker worker : vModule )
//          {
//              worker.start();
//             log.info( worker.getName() + " Start!" );
//          }
//      }
//      catch( Exception exc )
//      {
//          this.abort( "get exception!", exc );
//      }
//  }
    
    /***
     * 服务退出时会调用该接口，默认按照模块的注册顺序，反序调用stopAndCheck方法，每个module默认最长停止时间为1秒。
     */
//  public void terminate()
//  {
//      for( int i = 0; i < vModule.size(); ++ i )
//      {
//          vModule.get( vModule.size() - i - 1 ).stopAndCheck( "stop normally!", 1000 );
//      }
//  }
    @Override
    public final void run()
    {
        bRunning = true;
        try 
        {
            startAllModule();
            if( !this.isStopped() ) 
            {
                loop();
            }
        } 
        catch( Throwable t )
        {
            abort( "Unhandled exception. Starting shutdown. ", t );
        } 
        finally 
        {
            stopAllModule();
            terminate();
           log.info( "all workers have been stopped, Service "+this.getName()+" will exit in 3 seconds!" );
            try 
            {
                Thread.sleep( 3000 );
            } 
            catch( InterruptedException e ) 
            {
                e.printStackTrace();
            }
        }
       log.info( "service "+this.getName()+" exit success!" );
       log.info((Thread.getAllStackTraces().keySet()+", Thread exit successfully!"));
        bRunning = false;
    }
    
    /***
     * 获取出错信息
     * @return 出错信息
     */
    public String getErrMsg()
    {
        return m_strErrMsg;
    }
    
    //出错信息
    protected String m_strErrMsg = "";
}
