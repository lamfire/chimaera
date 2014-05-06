package com.lamfire.chimaera.http;

import com.lamfire.chimaera.ServiceRegistry;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.service.Service;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class ActionTask implements Runnable{
	private Action action;
	private ActionContext context;
	private byte[] message;
	
	public ActionTask(ActionContext context,Action action,byte[] message){
		this.context = context;
		this.action = action;
		this.message = message;
	}

	@Override
	public void run() {
		try {
            Command command = Serializers.getCommandSerializer().decode(message, Command.class);
            Response response = null;
            try {
                ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
                if (serviceRegistry.validateCommand(command)) {
                    Service<Command> service = serviceRegistry.getService(command.getCommand());
                    response = service.execute(null,command);
                }
            } catch (Throwable e) {
                ErrorResponse err = new ErrorResponse();
                err.setError(e.getMessage());
                response = err;
            }
            byte[] result =  Serializers.getResponseSerializer().encode(response);
			HttpOutputs.writeResponse(context.getChannel(), context.getHttpResponse(),result);
		} catch (Throwable t) {
			HttpOutputs.writeError(context.getChannel(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
