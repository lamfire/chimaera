package com.lamfire.chimaera;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.chimaera.tunnel.TunnelService;
import com.lamfire.chimaera.tunnel.TunnelSetting;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.Message;
import com.lamfire.hydra.MessageContext;
import com.lamfire.hydra.Snake;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChimaeraServer extends Snake {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraServer.class);
    private ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
    private ServerConfigure serverConfigure;

    public ChimaeraServer(ServerConfigure serverConfigure) {
        super(serverConfigure.getBind(), serverConfigure.getPort());
        this.serverConfigure = serverConfigure;
        Chimaera.setChimaeraOpts(serverConfigure);
    }

    public void startup() {
        this.setExecutorService(ThreadPools.get().getExecutorService());
        this.bind();
        LOGGER.info("[startup] available memory = " + (Chimaera.getAvailableHeapMemory() / 1024 / 1024) + "mb");
        LOGGER.info("[startup] startup on - " + serverConfigure.getBind() + ":" + serverConfigure.getPort());
    }



    public void startupTunnels(){
        try {
            List<TunnelSetting> list = serverConfigure.getTunnelSettings();
            for(TunnelSetting setting : list){
                TunnelService service = new TunnelService(setting);
                service.startup();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    @Override
    protected void handleMessage(MessageContext context, Message message) {
        byte[] bytes = message.getBody();
        Command cmd = Serializers.getCommandSerializer().decode(bytes, Command.class);
        try {
            if (ServiceRegistry.getInstance().validateCommand(cmd)) {
                //executeCommand(context, cmd);
                submitTask(context, cmd);
            }
        } catch (Throwable e) {
            ErrorResponse err = new ErrorResponse();
            err.setError(e.getMessage());
            sendResponse(context, err);
        }
    }

    private void submitTask(MessageContext context, Command command) {
        ChimaeraServiceTask task = new ChimaeraServiceTask(context, command);
        ThreadPools.get().submit(task);
    }

    private void executeCommand(MessageContext context, Command command) {
        Response response = null;
        try {
            Service<Command> service = serviceRegistry.getService(command.getCommand());
            response = service.execute(context, command);
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
