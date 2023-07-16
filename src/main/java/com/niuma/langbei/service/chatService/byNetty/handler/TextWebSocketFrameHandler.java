package com.niuma.langbei.service.chatService.byNetty.handler;

import com.google.gson.Gson;
import com.niuma.langbei.model.domain.Im;
import com.niuma.langbei.model.domain.ImUser;
import com.niuma.langbei.model.domain.User;

import com.niuma.langbei.service.ImService;
import com.niuma.langbei.service.UserService;
import com.niuma.langbei.service.chatService.byNetty.config.NettyConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

/**
 * 用来解析websocket连接时的url参数
 * 参考：https://blog.csdn.net/mfkarj/article/details/103279559
 * @author niumazlb
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Resource
	UserService userService;
	@Resource
	ImService imService;

	private static UserService staticUserService;
	private static ImService staticImService;

	// 程序初始化的时候触发这个方法  赋值
	@PostConstruct
	public void setStaticUser() {
		staticUserService = userService;
		staticImService = imService;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//有新的连接加入，就加到我们的连接组里
//		Channel channel = ctx.channel();
//		NettyConfig.group.add(channel);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest request = (FullHttpRequest) msg;
			String uri = request.uri();
			String origin = request.headers().get("Origin");
			if (null == origin) {
				ctx.close();
			} else {
				if (null != uri && uri.contains("?")) {
					String[] uriArray = uri.split("\\?");
					if (null != uriArray && uriArray.length > 1) {
						String[] paramsArray = uriArray[1].split("=");
						if (null != paramsArray && paramsArray.length > 1) {
							//TODO 验证连接权限，不通过关闭
							String uid = paramsArray[1];
							User user = staticUserService.getById(uid);
							ImUser imUser = new ImUser();
							BeanUtils.copyProperties(user, imUser);
							NettyConfig.idChannelGroup.put(imUser,ctx.channel());
							NettyConfig.channelIdGroup.put(ctx.channel(), imUser);
						}
					}
					//将uri设置回来，否则下一个handler通过不了就建立不了连接
					request.setUri("/ws");
				}
			}
		}
		super.channelRead(ctx, msg);
	}

	//处理每一条数据
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
		// 获取接收到的消息
		String content = msg.text();
		log.info("接收到的消息：{}" , content);
		ImUser imUser = NettyConfig.channelIdGroup.get(channelHandlerContext.channel());
		Im im = new Im();
		im.setUid(imUser.getUid());
		im.setUsername(imUser.getUsername());
		im.setProfile(imUser.getProfile());
		im.setText(content);
		im.setAvatarUrl(imUser.getAvatarUrl());
		staticImService.save(im);
		Gson gson = new Gson();
		String imJson = gson.toJson(im);
		//todo 这里消息内容应该更加丰富的
		Set<Channel> channels = NettyConfig.channelIdGroup.keySet();
		for (Channel userChannel : channels) {
			//将消息转换成 TextWebSocketFrame 才能被web接收到
			TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(imJson);
			userChannel.writeAndFlush(webSocketFrame);
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		ImUser imUser = NettyConfig.channelIdGroup.get(channel);
		NettyConfig.idChannelGroup.remove(imUser);
		NettyConfig.channelIdGroup.remove(channel);
	}

}