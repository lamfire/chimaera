package com.lamfire.chimaera;

import com.lamfire.hydra.*;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.logger.Logger;

public class ChimaeraServer extends Snake {
	private static final Logger LOGGER = Logger.getLogger(ChimaeraServer.class);

    private ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
	public ChimaeraServer(String host, int port) {
		super(host, port);
	}

	public static void startup(String host, int port) {
		ChimaeraServer server = new ChimaeraServer(host, port);
		server.bind();
	}

	static void usage() {
		LOGGER.info(ChimaeraServer.class.getName() + " [host] [port]");
	}

	public static void main(String[] args) {
		String host = "0.0.0.0";
		int port = 8090;

		if (args.length > 0 && "?".equals(args[0])) {
			usage();
			return;
		}

		if (args.length == 2) {
			host = args[0];
			port = Integer.valueOf(args[1]);
		}

		LOGGER.info("[startup] available memory = " + (Chimaera.getAvailableHeapMemory()/1024/1024) +"mb");
		startup(host, port);
	}

	@Override
	protected void handleMessage(MessageContext context, Message message) {
        byte[] bytes = message.getBody();
        Command  cmd = Serializers.getCommandSerializer().decode(bytes,Command.class);
        try{
            if(ServiceRegistry.getInstance().validateCommand(cmd)){
                executeCommand(context, cmd);
            }
        }catch(Exception e){
            ErrorResponse err = new ErrorResponse();
            err.setError(e.getMessage());
            sendResponse(context,err);
        }
	}

    private void executeCommand(MessageContext context,Command command){
        Response response = null;
        try{
            Service<Command> service =serviceRegistry.getService(command.getCommand());
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
