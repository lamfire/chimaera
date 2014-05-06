package com.lamfire.chimaera.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.lamfire.logger.Logger;

class HttpServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger LOGGER = Logger.getLogger(HttpServerHandler.class);

	private ActionRegistry registry;
	private ExecutorService worker;
	private AtomicInteger counter = new AtomicInteger();

	public HttpServerHandler(ActionRegistry registry,ExecutorService worker) {
		this.registry = registry;
		this.worker = worker;
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		HttpRequest request = (HttpRequest) e.getMessage();
		String uri = request.getUri();

		Action action = registry.lookup(uri);
		if (action == null) {
			HttpOutputs.writeError(ctx.getChannel(), HttpResponseStatus.BAD_REQUEST);
			return;
		}

		try {
			ChannelBuffer reqBuffer = request.getContent();
			byte[] message = reqBuffer.array();
			ActionContext context = new ActionContext(ctx, request);
			
			if(this.worker != null){
				ActionTask task = new ActionTask(context, action, message);
				this.worker.submit(task);
				return;
			}
			byte[] result = action.execute(context, message);
			HttpOutputs.writeResponse(ctx.getChannel(),context.getHttpResponse(), result);
		} catch (Throwable t) {
			HttpOutputs.writeError(ctx.getChannel(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error(t.getMessage(),t);
		}
	}


	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Throwable cause = e.getCause();
		try {
			if (cause instanceof TooLongFrameException) {
				HttpOutputs.writeError(ctx.getChannel(), HttpResponseStatus.BAD_REQUEST);
				return;
			}
			LOGGER.error(e.getCause().getMessage(),e.getCause());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
		this.counter.decrementAndGet();
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelOpen(ctx, e);
		this.counter.incrementAndGet();
	}
	
	
}
