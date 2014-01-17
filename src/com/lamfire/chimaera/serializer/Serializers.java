package com.lamfire.chimaera.serializer;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-17
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class Serializers {
    private static final CommandSerializer COMMAND_SERIALIZER = new CommandSerializer();
    private static final ResponseSerializer RESPONSE_SERIALIZER = new ResponseSerializer();

    public static CommandSerializer getCommandSerializer() {
        return COMMAND_SERIALIZER;
    }

    public static ResponseSerializer getResponseSerializer() {
        return RESPONSE_SERIALIZER;
    }
}
