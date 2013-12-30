package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.store.*;

public class Config {
    private static final String DEFAULT_HOST = "127.0.0.1";
    //private static final String DEFAULT_HOST = "192.168.1.220";

    private static final int DEFAULT_PORT = 8090;


    public static ChimaeraCli cli;
    public static FireStore store;
    public static FireIncrement inc;
    public static FireSet set;
    public static FireMap map;
    public static FireQueue queue;
    public static FireList list;
    public static FireRank rank;

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
            store = cli.getFireStore("DEFAULT");
            inc = store.getFireIncrement("TEST_INC");
            set = store.getFireSet("TEST_SET");
            map = store.getFireMap("TEST_MAP");
            queue = store.getFireQueue("TEST_QUEUE");
            list = store.getFireList("TEST_LIST");
            rank = store.getFireRank("TEST_RANK");
        } catch (Exception e) {
            e.printStackTrace();
            cli.close();
            cli = null;
        }
    }

    public static  FireStore  getFireStore(){
         return cli.getFireStore("DEFAULT");
    }

    public static void shutdown(){
        cli.close();
    }

    public static void main(String[] args)throws Exception{
        System.out.println(store.size("TEST_MAP"));

        shutdown();
    }
}
