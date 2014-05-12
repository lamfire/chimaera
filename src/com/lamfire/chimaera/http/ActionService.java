package com.lamfire.chimaera.http;

import com.lamfire.chimaera.ServiceRegistry;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.service.Service;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-6
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */

@ACTION(path = "/api")
public class ActionService implements Action {
    @Override
    public void execute(ActionContext context) {
        byte[] message = context.getHttpRequestContentAsBytes();
        Command command = Serializers.getCommandSerializer().decode(message, Command.class);
        Response response = null;
        try {
            ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
            if (serviceRegistry.validateCommand(command)) {
                Service<Command> service = serviceRegistry.getService(command.getCommand());
                response = service.execute(null, command);
            }
        } catch (Throwable e) {
            ErrorResponse err = new ErrorResponse();
            err.setError(e.getMessage());
            response = err;
        }
        byte[] result = Serializers.getResponseSerializer().encode(response);
        context.writeResponse(result);
    }
}
