package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.filestore.DiskFireSet;
import com.lamfire.thalia.ThaliaDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireSetTest  extends DiskStore{
    private static final String FILE = "/data/jdbm/groupuser";

    public static void main(String[] args)throws Exception {
        ThaliaDatabase store = getThaliaDatabase(FILE);
        FireSet set  = new DiskFireSet(store,"5354d4d87fbeffb3213fed64");
        System.out.println(set.size());
        for(int i=0;i<set.size();i++){
            System.out.println(new String(set.get(i)));
        }
    }
}
