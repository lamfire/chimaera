package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.filestore.DiskDatabase;
import com.lamfire.chimaera.store.filestore.DiskFireSet;
import com.lamfire.utils.Bytes;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireSetTest  extends DiskStore{
    private static final String FILE = "/data/chimaera/groupuser";

    public static void main(String[] args)throws Exception {
        DiskDatabase store = getDatabase(FILE);
        FireSet set  = new DiskFireSet(store,"5354d4d87fbeffb3213fed64");
        System.out.println(set.size());
//        for(int i=0;i< 10;i++){
//            set.add(Bytes.toBytes(i));
//            System.out.println(i);
//        }

        set.remove(Bytes.toBytes(4)) ;
        System.out.println("----------------------remove");

        for(int i=0;i< 10;i++){
            byte[] bytes = set.get(i);
            if(bytes != null){
                System.out.println(i +" > " +(Bytes.toInt(bytes)));
            }
        }

    }
}
