package com.lamfire.chimaera.test.client.list;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Bytes;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-8-1
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class ListTest {
    public static void main(String[] args) {
        FireList list = Config.getFireStore().getFireList("list_test");
        list.add(Bytes.toBytes(1));
        list.add(Bytes.toBytes(2));
        list.add(Bytes.toBytes(3));
        list.add(Bytes.toBytes(4));
        list.add(Bytes.toBytes(5));

        List<byte[]> result = list.gets(1,3);
        System.out.println(result);
    }
}
