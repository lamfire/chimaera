package com.lamfire.chimaera.http;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.lamfire.logger.Logger;

public class HttpServer {
	private static final Logger LOGGER = Logger.getLogger(HttpServer.class);
	private ActionRegistry registry;
	private String hostname;
	private int port;
	private ExecutorService worker;

	public HttpServer(ActionRegistry registry, String hostname, int port) {
		this.registry = registry;
		this.hostname = hostname;
		this.port = port;
	}

	public void startup() {
		this.worker =  Executors.newFixedThreadPool(32);
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newFixedThreadPool(4),this.worker));
		bootstrap.setPipelineFactory(new ServerPipelineFactory());
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("reuserAddress",true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(hostname, port));
		LOGGER.info("Server start on /" + hostname + ":" + port);
	}

	private class ServerPipelineFactory implements ChannelPipelineFactory {
		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("decoder", new HttpRequestDecoder());
			pipeline.addLast("encoder", new HttpResponseEncoder());
			pipeline.addLast("handler", new HttpServerHandler(registry,worker));
			return pipeline;
		}
	}
}
