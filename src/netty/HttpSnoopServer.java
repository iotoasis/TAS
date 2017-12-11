package netty;


import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;

/**
 * An HTTP server that sends back the content of the received HTTP request
 * in a pretty plaintext form.
 */
public class HttpSnoopServer extends Thread {
	public static Logger clsLogg = Logger.getRootLogger();
    private final int port;

    public HttpSnoopServer(int port) {
        this.port = port;
    }

    public void run() {
    	InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
    	clsLogg.debug("[ TAS INFO ] :: TAS Start!!");
    	clsLogg.debug("[ TAS INFO ] :: TAS port number --> " + port);
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
             .childOption(ChannelOption.MAX_MESSAGES_PER_READ, 1048576) //Request Message Size.
             .childHandler(new HttpSnoopServerInitializer());

            Channel ch = null;
			try {
				ch = b.bind(port).sync().channel();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				ch.closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = 7001;
        } else {
            port = 7001;
        }
        new HttpSnoopServer(port).run();
    }
}