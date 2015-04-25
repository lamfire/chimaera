package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-10-29
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public class LDBMetaTest {

    public static void main(String[] args) {
        LDBManager ldb = new LDBManager("/data/ldb");
        LDBMeta meta = new LDBMeta(ldb);
        meta.setValue("abcd".getBytes(),"abcd".getBytes());

        String v = meta.getValueAsString("abcd".getBytes());
        System.out.println(v);

        meta.clear();
        v = meta.getValueAsString("abcd".getBytes());
        System.out.println(v);
    }
}
