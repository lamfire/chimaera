package com.lamfire.chimaera.test.http;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueuePushCommand;
import com.lamfire.json.JSON;
import com.lamfire.utils.HttpClient;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-6
 * Time: 下午1:24
 * To change this template use File | Settings | File Templates.
 */
public class StoreTest {

    public static void main(String[] args) throws IOException {
        QueuePushCommand cmd = new QueuePushCommand();
        cmd.setStore("TEST_HTTP");
        cmd.setKey("QUEUE");
        cmd.setCommand(Command.QUEUE_PUSH);
        cmd.setValue("123456".getBytes());

        JSON js = JSON.fromJavaObject(cmd);
        String json = js.toJSONString();
        System.out.println("REQUEST:" + json);

        HttpClient http = new HttpClient();
        http.setMethod("POST");
        http.open("http://127.0.0.1:19900/api");
        http.post(json.getBytes());

        String str = http.readAsString();
        System.out.println("RESPONSE:" +str);
    }
}
