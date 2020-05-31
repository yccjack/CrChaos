package com.ycc.register.work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ��������࣬�����������Ļ��࣬������̳и��࣬��ʵ��abstract����
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
            //��ӡ���쳣���е�console
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
     * ��ʹ��ҵ���߼���������ڴ��ȳ�ʼ����־ģ��
     * @param strConf �����ļ�·��
     * @return
     */
    public abstract EnumRetType init( String strConf );
    
    /***
     * ���ҵ���߼����ú������е�module�������Լ����̣߳����ú������ܱ�block
     */
    public abstract void startAllModule();
    
    /***
     * �����˳�ʱ����øýӿڣ�����ÿ��ģ���stopAndCheck������ÿ��moduleĬ���ֹͣʱ��Ϊ1�롣
     */
    public abstract void stopAllModule();
    
    /**
     * 
     * ģ���˳�����Դ�ͷ�
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
     * �����˳�ʱ����øýӿڣ�Ĭ�ϰ���ģ���ע��˳�򣬷������stopAndCheck������ÿ��moduleĬ���ֹͣʱ��Ϊ1�롣
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
     * ��ȡ������Ϣ
     * @return ������Ϣ
     */
    public String getErrMsg()
    {
        return m_strErrMsg;
    }
    
    //������Ϣ
    protected String m_strErrMsg = "";
}
