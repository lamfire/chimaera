package com.lamfire.chimaera.test.dbdengine;

import com.lamfire.chimaera.store.bdbstore.BDBEngine;
import com.lamfire.chimaera.store.bdbstore.BDBOpts;
import com.lamfire.chimaera.store.bdbstore.BDBStorageException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-8-12
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 */
public class EngineTest {
    public static void main(String[] args) throws BDBStorageException {
        BDBOpts opts = new BDBOpts();
        opts.setPath("/data/chimaera/bbdengine");
        opts.setWriteTransactionEnabled(true);
        opts.setFlushTransactionEnabled(false);
        BDBEngine engine = new BDBEngine("test",opts);
        Map<String,byte[]> map = engine.getMap("test");

//        for(int i=0;i<100000;i++){
//            String key = String.valueOf(i);
//            map.put(key,key.getBytes());
//            if(i % 1000 == 0)
//            System.out.println(i);
//        }

        System.out.println(map.size());
    }
}
