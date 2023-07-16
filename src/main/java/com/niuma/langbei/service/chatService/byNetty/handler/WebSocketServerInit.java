package com.niuma.langbei.service.chatService.byNetty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

/**
 * @author niuma
 * @create 2023-02-09 23:15
 */
@Component
public class WebSocketServerInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline()
                // 解码器
                .addLast("http-codec", new HttpServerCodec())
                // 日志
                .addLast("log", new LoggingHandler(LogLevel.DEBUG))
                // 消息聚合
                .addLast("aggregator", new HttpObjectAggregator(65536))
                // 对写大数据流的支持
                .addLast("http-chunked", new ChunkedWriteHandler())
                //用来解析建立websocket连接时url携带的参数,也可以用来处理收到的消息
                .addLast(new TextWebSocketFrameHandler())
                // websocket服务器处理的协议  并且用于指定给客户端连接访问的路由：/ws
                .addLast("protocolHandler",new WebSocketServerProtocolHandler("/ws"));

    }
}
