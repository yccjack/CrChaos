package com.ycc.register.work;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * ������������̶߳��󣬸ö�����Գ���ĳ������ҵ���߼�
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
     * ���ظ�ģ���Ƿ��Ѿ�ֹͣ�����ڸ÷���ʵ�ֱȽϹ̶�����ˣ���ֹ���������������ء�
     */
    @Override
    public final boolean isStopped() 
    {
        return m_bStopped;
    }
    
    /***
     * ���ظ�ģ���Ƿ�abort�����ڸ÷���ʵ�ֱȽϹ̶�����ˣ���ֹ���������������ء�
     */
    @Override
    public final boolean isAborted() 
    {
        return m_bAborted;
    }

    /***
     * ֹͣ�ô���ģ�飬���ڸ÷���ʵ�ֱȽϹ̶�����ˣ���ֹ���������������ء�
     * �÷��������ñ�־λ
     * @param why ֹͣԭ��
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
     * �����ɿ���е��̸߳�����ã��˳����߳�ģ�飬���ȴ�ʱ��ΪnMillSec���룬���ڸó�ʱʱ���ڷ��أ�˵��ģ��ɹ��˳���
     * �򷵻�true������ʱ������false��
     * @param why ֹͣԭ��
     * @param nMillSec ���ʱ�ȴ��ĺ�����
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
     * �ô���ģ�������ڲ��쳣abort�����ڸ÷���ʵ�ֱȽϹ̶�����ˣ���ֹ���������������ء�
     * @param why ֹͣԭ��
     */
    @Override
    public void abort(String why, Throwable e) 
    {
        this.m_bAborted = true;
        if( e != null )
        {
            //��ӡ���쳣���е�console
//            e.printStackTrace();
            //���쳣��Ϣ��ӡ����־��
           log.error( why + ", Throwable: " + e );
        }
        stop( this.getName() + " Aborting, " + why );
    }
    
    /***
     * sleep�ô���ģ��nMillsec���룬������ģ�鱻�˳���sleep���̻���;���ء�
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
     * ��ʹ��ҵ���߼���������ڴ��ȳ�ʼ����־ģ��
     * @param strConf �����ļ�·��
     * @return
     */
    //public abstract EnumRetType init();
    
    /***
     * �����˳�ʱ����øýӿ�
     */
    public abstract void terminate();
    
    /***
     * ҵ���߼���������
     */
    public abstract void process();
    
    /***
     * worker���߳���� 
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
     *  ����worker������ 
     * @param strModuleName
     */
    /*public final void setModuleName( String strModuleName )
    {
        this.getName() = strModuleName;
    }*/
    
    //ģ�������
    //protected String this.getName() = "";
    //�ȴ�����
    private Object m_objWaitEvent = new Object();
    
    private boolean m_bTerminate =false;
}
