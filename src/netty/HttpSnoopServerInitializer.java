package netty;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
    	final int READ_TIMEOUT = 20; // 180 seconds.
    	// Create a default pipeline implementation.
        ChannelPipeline p = ch.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //p.addLast("ssl", new SslHandler(engine));

        p.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        p.addLast("aggregator", new HttpObjectAggregator(1048576));
        p.addLast("encoder", new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        //p.addLast("deflater", new HttpContentCompressor());
        p.addLast("readTimeoutHandler", new ReadTimeoutHandler(READ_TIMEOUT));
		p.addLast("myHandler", new MyHandler());
        p.addLast("handler", new HttpSnoopServiceTxt());
        //p.addLast("handler", new HttpSnoopServerHandler());
    }
    
    class MyHandler extends ChannelDuplexHandler {
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if(cause instanceof ReadTimeoutException) {
				
				ctx.write("timeout !");
				
				super.exceptionCaught(ctx, cause);
				System.out.println("Netty Timeout!!");
			} else {
				super.exceptionCaught(ctx, cause);
			}
		}
	}
    
}
