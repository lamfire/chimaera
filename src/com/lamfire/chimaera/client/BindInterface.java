package com.lamfire.chimaera.client;

import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.command.Command;
import com.lamfire.hydra.Session;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-16
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public interface BindInterface {
    public Command getBindCommand(String key, String clientId);

    public Command getUnbindCommand(String key, String clientId);

    public Command getPublishCommand(String key, String clientId, byte[] bytes, boolean feedback);

    public Session rebind(String key, String clientId, OnMessageListener listener);
}
