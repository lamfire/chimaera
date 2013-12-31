package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.filestore.FileStore;
import com.lamfire.chimaera.store.filestore.FireListInFile;
import com.lamfire.chimaera.store.filestore.FireSetInFile;
import com.lamfire.chimaera.test.client.Config;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class FireListFileSoreTest {
    private static final String FILE = "/data/chimaera/store";
    private FireList list;

    public FireListFileSoreTest(FireList list) {
        this.list = list;
    }

    public void test() {
        list.clear();
        System.out.println("list.clear()");

        int size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,0);

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            list.add(val.getBytes());
            System.out.println("list.add("+val+")");
        }

        System.out.print("list.gets(2,5):" );
        List<byte[]> gets = list.gets(2,5);
        for(byte[] bytes : gets){
            String v = new String(bytes);
            System.out.print(v +" ");
        }
        System.out.println();

        size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,100);

        String value = new String(list.get(44));
        System.out.println("list.get(44):"+value);
        Asserts.assertEquals(value,"44");

        list.set(44,("1231231312313").getBytes());
        System.out.println("list.set(44,'1231231312313')");

        value = new String(list.get(44));
        System.out.println("list.get(44):"+value);
        Asserts.assertEquals(value,"1231231312313");

        list.remove(44);
        System.out.println("list.remove(44)");

        value = new String(list.get(44));
        System.out.println("list.get(44):"+value);
        Asserts.assertEquals(value,"45");

        size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,99);

    }

    public static void main(String[] args) throws Exception{
        FileStore store = new FileStore(FILE);
        FireList list  = new FireListInFile(store,"TEST_LIST");
        FireListFileSoreTest test = new FireListFileSoreTest(list);
        test.test();
    }
}