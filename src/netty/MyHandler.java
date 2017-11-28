package netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

public class MyHandler extends ChannelDuplexHandler {
    @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
           throws Exception {
       if (cause instanceof ReadTimeoutException) {
           // do something
    	   System.out.println("something timeout!!");
       } else {
    	   System.out.println("timeout!!");
           super.exceptionCaught(ctx, cause);
       }
   }
}