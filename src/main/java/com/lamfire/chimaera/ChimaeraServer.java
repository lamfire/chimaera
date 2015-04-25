package com.lamfire.chimaera;

import com.lamfire.chimaera.command.Command;
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

import java.util.List;


public class ChimaeraServer extends Snake {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraServer.class);
    private ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
    private ServerConfigure serverConfigure;
    private ChimaeraThreadPools worker;

    public ChimaeraServer(ServerConfigure serverConfigure) {
        super(serverConfigure.getBind(), serverConfigure.getPort());
        this.serverConfigure = serverConfigure;
        Chimaera.setChimaeraOpts(serverConfigure);
        this.worker = new ChimaeraThreadPools( serverConfigure.getThreads() );
    }

    public void startup() {
        this.setKeepAlive(false);
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
        serviceAsync(context, message);
    }

    /**
     * 异步执行业务方法
     * @param context
     * @param message
     */
    private void serviceAsync(MessageContext context, Message message){
        ChimaeraServiceTask task = new ChimaeraServiceTask(context, message);
        worker.submit(task);

    }

    /**
     * 同步执行业务方法
     * @param context
     * @param message
     */
    private void serviceSync(MessageContext context,  Message message) {
        Response response = null;
        byte[] bytes = message.getBody();
        try {
            Command cmd = Serializers.getCommandSerializer().decode(bytes, Command.class);
            Service<Command> service = serviceRegistry.getService(cmd.getCommand());
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
