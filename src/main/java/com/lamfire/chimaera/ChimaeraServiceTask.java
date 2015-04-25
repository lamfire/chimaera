package com.lamfire.chimaera;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.Message;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-13
 * Time: 下午7:13
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraServiceTask implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraServiceTask.class);
    MessageContext context;
    Message message;

    public ChimaeraServiceTask(MessageContext context, Message message) {
        this.context = context;
        this.message = message;
    }

    private void checkMemory(Command command) {
        if (ServiceRegistry.getInstance().isWriteProtectedCommand(command.getCommand())) { //如果为写入操作，则检查剩余内存
            if (Chimaera.isLackOfMemory()) { //内存缺乏
                throw new ChimaeraException("Lack of memory,available less " + Chimaera.getAvailableHeapMemory() / 1024 / 1024 + "mb");
            }
        }
    }

    @Override
    public void run() {
        Response response = null;
        byte[] bytes = message.getBody();
        Command cmd = Serializers.getCommandSerializer().decode(bytes, Command.class);
        try {
            if( !Chimaera.getChimaeraOpts().isStoreOnDisk()){
                checkMemory(cmd);
            }
            Service<Command> service = ServiceRegistry.getInstance().getService(cmd.getCommand());
            response = service.execute(context, cmd);
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage(), e);
            ErrorResponse err = new ErrorResponse();
            err.setError(e.getMessage());
            response = err;
        }

        if (response != null) {
            sendResponse(context, response);
        }
    }

    private void sendResponse(MessageContext context, Response response) {
        try {
            byte[] bytes = Serializers.getResponseSerializer().encode(response);
            int mid = context.getMessage().getId();
            context.send(mid, bytes);
        } catch (Throwable e) {
            LOGGER.error("error send response.", e);
        }
    }
}
