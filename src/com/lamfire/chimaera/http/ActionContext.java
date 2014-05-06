package com.lamfire.chimaera.http;

import java.net.InetSocketAddress;
import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class ActionContext {
	private HttpRequest request;
	private HttpResponse response;
	
	private ChannelHandlerContext ctx;
	
	ActionContext(ChannelHandlerContext ctx,HttpRequest request){
		this.request = request;
		this.ctx = ctx;
		this.response =  new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}
	
	Channel getChannel(){
		return ctx.getChannel();
	}
	
	public String getRemoteAddress(){
		InetSocketAddress addr = (InetSocketAddress)ctx.getChannel().getRemoteAddress();
		return addr.getHostName();
	}
	
	public int getRemotePort(){
		InetSocketAddress addr = (InetSocketAddress)ctx.getChannel().getRemoteAddress();
		return addr.getPort();
	}
	
	public String getHttpRequestHeader(String key){
		return request.getHeader(key);
	}
	
	public Set<String> getHttpRequestHeaderNames(){
		return request.getHeaderNames();
	}
	
	public String getHttpRequestUri(){
		return request.getUri();
	}
	
	public void addHttpResponseHeader(String key,Object value){
		this.response.addHeader(key, value);
	}
	
	public void setHttpResponseStatus(HttpResponseStatus status){
		this.response.setStatus(status);
	}

	public HttpRequest getHttpRequest() {
		return request;
	}

	public HttpResponse getHttpResponse() {
		return response;
	}


}
