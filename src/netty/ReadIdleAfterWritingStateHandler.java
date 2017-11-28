package netty;

import java.security.InvalidParameterException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutor;

public class ReadIdleAfterWritingStateHandler extends ChannelDuplexHandler {
	public  static  Logger logger = Logger.getRootLogger();
	
	private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
    
    // writing한 후 reading이 없는 제한시간
	private final long readerIdleTimeNanosAfterWriting;
	
	volatile ScheduledFuture<?> readerIdleTimeoutAfterWriting;
    volatile long lastReadTime;
    
    // 가장 마지막에 writing한 시간
    volatile long lastWriteTime;
    
    private volatile boolean reading;
    
    public ReadIdleAfterWritingStateHandler(long readerIdleTimeAfterWriting) {

        if (readerIdleTimeAfterWriting <= 0) {
        	throw new   InvalidParameterException("time must > 0");
        } 
        
        readerIdleTimeNanosAfterWriting = Math.max(TimeUnit.SECONDS.toNanos(readerIdleTimeAfterWriting), MIN_TIMEOUT_NANOS);
	}
	
    
    public long getReaderIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(readerIdleTimeNanosAfterWriting);
    }
    
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        destroy();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        destroy();
        super.channelInactive(ctx);
    }
    
    
    @Override
    public void write(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // Allow writing with void promise if handler is only configured for read timeout events.
        if (readerIdleTimeNanosAfterWriting > 0) {
//            ChannelPromise unvoid = ((Object) promise).unvoid();
            ChannelPromise unvoid = null;
            ctx.write(msg, unvoid).addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					lastWriteTime = System.nanoTime();
		            initialize(ctx);
				}
			});
            
            
        } else {
            ctx.write(msg, promise);
        }
    }
    
    
    
    private void initialize(ChannelHandlerContext ctx) {

    	logger.debug("before readerIdleTimeoutAfterWriting=" + readerIdleTimeoutAfterWriting);
    	
    	if(readerIdleTimeoutAfterWriting!= null && !readerIdleTimeoutAfterWriting.isDone())
    		readerIdleTimeoutAfterWriting.cancel(false);
    	
        EventExecutor loop = ctx.executor();
        readerIdleTimeoutAfterWriting = loop.schedule(
                new ReaderIdleTimeoutTask(ctx),
                readerIdleTimeNanosAfterWriting, TimeUnit.NANOSECONDS);
        
        logger.debug("after readerIdleTimeoutAfterWriting=" + readerIdleTimeoutAfterWriting);
    }

    private void destroy() {

        if (readerIdleTimeoutAfterWriting != null) {
            readerIdleTimeoutAfterWriting.cancel(false);
            readerIdleTimeoutAfterWriting = null;
        }
    }
    
    /**
     * Is called when an {@link IdleStateEvent} should be fired. This implementation calls
     * {@link ChannelHandlerContext#fireUserEventTriggered(Object)}.
     */
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
    
    
    private final class ReaderIdleTimeoutTask implements Runnable {

        private final ChannelHandlerContext ctx;

        ReaderIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void run() {
            if (!ctx.channel().isOpen()) {
                return;
            }

            long nextDelay = readerIdleTimeNanosAfterWriting;
            if (!reading) {
                nextDelay -= System.nanoTime() - lastReadTime;
            }

            if (nextDelay <= 0) {
                // Reader is idle - set a new timeout and notify the callback.
                readerIdleTimeoutAfterWriting =
                    ctx.executor().schedule(this, readerIdleTimeNanosAfterWriting, TimeUnit.NANOSECONDS);
                
                try {
                    channelIdle(ctx, IdleStateEvent.READER_IDLE_STATE_EVENT);
                } catch (Throwable t) {
                    ctx.fireExceptionCaught(t);
                }
            } else {
                // Read occurred before the timeout - set a new timeout with shorter delay.
                readerIdleTimeoutAfterWriting = ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
}