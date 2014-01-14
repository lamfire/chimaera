package com.lamfire.chimaera;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-13
 * Time: 下午7:13
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraServiceTask implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(ChimaeraServiceTask.class);
    MessageContext context;
    Command command;

    public  ChimaeraServiceTask(MessageContext context,Command command){
        this.context = context;
        this.command = command;
    }

    @Override
    public void run() {
        Response response = null;
        try{
            Service<Command> service =ServiceRegistry.getInstance().getService(command.getCommand());
            response = service.execute(context, command);
        }catch(Exception e){
            LOGGER.warn(e.getMessage(), e);
            ErrorResponse err = new ErrorResponse();
            err.setError(e.getMessage());
            response = err;
        }

        if(response != null){
            sendResponse(context,response);
        }
    }

    private void sendResponse(MessageContext context,Response response){
        try{
            byte[] bytes = Serializers.getResponseSerializer().encode(response);
            int mid = context.getMessage().getId();
            context.send(mid, bytes);
        }catch(Exception e){
            LOGGER.error("error send response.",e);
        }
    }
}
