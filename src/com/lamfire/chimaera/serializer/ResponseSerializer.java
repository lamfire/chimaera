package com.lamfire.chimaera.serializer;

import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.subscribe.PublishResponse;
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
public class ResponseSerializer implements Serializer<Response>{
    private static final Logger LOGGER = Logger.getLogger(ResponseSerializer.class);

    @Override
    public byte[] encode(Response res) {
        String js= JSON.toJSONString(res);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("RESPONSE:" + js);
        }
        return js.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public Response decode(byte[] bytes, Class< Response> type) {
        String js = new String(bytes);
        JSON json = new JSON(js);
        return decode(json,type);
    }

    @Override
    public Response decode(JSON json, Class< Response> type) {
        Integer status = (Integer)json.get("status");
        Response res;
        if(status == 201){
            res = json.toObject(PublishResponse.class);
        } else if(status == 200){
            res = (Response)json.toObject(type);
        }  else{
            res = (Response)json.toObject( ErrorResponse.class);
        }
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("RESPONSE:" + type.getName() +"=" +json);
        }
        return res;
    }
}
