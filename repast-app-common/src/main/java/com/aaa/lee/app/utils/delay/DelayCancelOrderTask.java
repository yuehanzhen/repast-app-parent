package com.aaa.lee.app.utils.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author:DongMengKe
 * 延迟队列中放入的task，里面包含到时间之后要处理的任务，该任务类实现了Runnable，用于交给线程池管理
 * @Date:2019/11/26 002618:37
 * @Version 1.0
 */
public class DelayCancelOrderTask<T extends Runnable> implements Delayed {

    /**
     * 到期时间
     */
    private final long time;

    /**
     * 任务对象
     */
    private final T processor;
    /**
     * 原子类
     */
    private static final AtomicLong atomic = new AtomicLong(0);

    private final long sequence;

    public DelayCancelOrderTask(long timeout, T processor){
        this.time = System.nanoTime() + timeout;
        this.processor = processor;
        this.sequence = atomic.getAndIncrement();
    }
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == this){
            return 0;
        }
        if (o instanceof DelayCancelOrderTask){
            DelayCancelOrderTask<?> other = (DelayCancelOrderTask<?>)o;
            long diff = this.time - other.time;
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }else if(this.sequence < other.sequence){
                return -1;
            } else {
                return 0;
            }
        }
        long diffrent = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (diffrent == 0)?0:((diffrent < 0) ? -1 : 1);
    }
    public T getProcessor(){
        return  this.processor;
    }
    @Override
    public int hashCode(){
        return processor.hashCode();
    }
    @Override
    public boolean equals(Object object)
    {
        if (object != null)
        {
            return object.hashCode() == hashCode() ? true : false;
        }
        return false;
    }

}
