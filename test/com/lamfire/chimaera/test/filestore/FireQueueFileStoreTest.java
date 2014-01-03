package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.filestore.FileStore;
import com.lamfire.chimaera.store.filestore.FireQueueInFile;
import com.lamfire.chimaera.store.filestore.FireSetInFile;
import com.lamfire.chimaera.test.client.Config;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class FireQueueFileStoreTest {
    private static final String FILE = "/data/chimaera/store";
    FireQueue queue ;
    public FireQueueFileStoreTest(FireQueue queue){
        this.queue = queue;
    }

    public void test() {
        queue.clear();
        System.out.println("queue.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            queue.push(val.getBytes());
            System.out.println("queue.pushLeft("+val+")");
        }

        int size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(100,size);

        for(int i=0;i<100;i++){
            String val = new String(queue.pop());
            System.out.println("queue.popLeft():"+val);
            Asserts.assertEquals(val,String.valueOf(i));
        }

        size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(0, size);
    }

    public static void main(String[] args)throws Exception {
        FileStore store = new FileStore(FILE);
        FireQueue queue  = new FireQueueInFile(store,"TEST_SET");
        FireQueueFileStoreTest test = new FireQueueFileStoreTest(queue);
        test.test();
    }
}
