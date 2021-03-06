package com.TCP_netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TCPClient {

    /**
     * 链接服务器
     * @param port
     * @param host
     * @throws Exception
     */
    public void connect(int port,String host)throws Exception{
        //网络事件处理线程组
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            //配置客户端启动类
            Bootstrap b=new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)//设置封包 使用一次大数据的写操作，而不是多次小数据的写操作
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler());//设置客户端网络IO处理器
                        }
                    });
            //连接服务器 同步等待成功
            ChannelFuture f=b.connect(host,port).sync();
            //同步等待客户端通道关闭
            f.channel().closeFuture().sync();
        }finally{
            //释放线程组资源
            group.shutdownGracefully();
        }
    }




}
