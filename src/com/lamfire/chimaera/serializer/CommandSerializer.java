package com.lamfire.chimaera.serializer;

import com.lamfire.chimaera.ServiceRegistry;
import com.lamfire.chimaera.command.Command;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-17
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class CommandSerializer implements Serializer<Command> {
    private static final Logger LOGGER = Logger.getLogger(CommandSerializer.class);

    @Override
    public byte[] encode(Command command) {
        String js = JSON.toJSONString(command);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("COMMAND:" + js);
        }
        return js.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public Command decode(byte[] bytes, Class<Command> type) {
        String js = new String(bytes);
        JSON json =JSON.fromJSONString(js);
        return decode(json, type);
    }

    @Override
    public Command decode(JSON json, Class<Command> type) {
        String cmdName = (String) json.get("command");
        Command cmd = (Command) json.toJavaObject(ServiceRegistry.getInstance().getCommandType(cmdName));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("COMMAND:" + json + " = " + cmd.getClass().getName());
        }
        return cmd;
    }
}
