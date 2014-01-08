package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.ChimaeraOpts;
import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.store.*;
import com.lamfire.utils.FilenameUtils;

import java.io.File;

public class Config {
    private static final String DEFAULT_HOST = "127.0.0.1";
    //public static final String DEFAULT_HOST = "192.168.9.126";

    public static final int DEFAULT_PORT = 19800;

    public static ChimaeraCli cli;

    public synchronized static ChimaeraCli getChimaeraCli(){
        if(cli == null){
            setup(DEFAULT_HOST,DEFAULT_PORT);
        }
        return cli;
    }


    public static void setupByArgs(Class<?> cls ,String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args != null && args.length > 0 && "?".equals(args[0])) {
            System.out.println(cls.getName() + " [host] [port]");
            return;
        }

        if (args != null && args.length == 2) {
            host = args[0];
            port = Integer.valueOf(args[1]);
        }
        Config.setup(host,port);
    }

    public static void setup(String host,int port ){
        if(cli != null){
            cli.close();
            cli = null;
        }
        try {
            cli = new ChimaeraCli();
            cli.open(host, port);
        } catch (Exception e) {
            e.printStackTrace();
            cli.close();
            cli = null;
        }
    }

    public static  FireStore  getFireStore(){
         return getChimaeraCli().getFireStore("DEFAULT");
    }

    public static void shutdown(){
        getChimaeraCli().close();
    }

    public static void main(String[] args)throws Exception{
        String storeName = "test";
        ChimaeraOpts opts = ChimaeraOpts.get();
        String dir = opts.getStoreDir();
        String file = FilenameUtils.getPath(opts.getStoreDir()) +storeName+".cma";
        String concat = FilenameUtils.concat(opts.getStoreDir(),storeName+".cma");
        System.out.println(dir);
        System.out.println(file);
        System.out.println(concat);
    }
}
