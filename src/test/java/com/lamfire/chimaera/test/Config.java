package com.lamfire.chimaera.test;

import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.pandora.Pandora;
import com.lamfire.utils.PropertiesUtils;

import java.util.Map;

public class Config {
    private static String HOST = "127.0.0.1";
    public static int PORT = 19800;

    public static ChimaeraCli cli;

    static{
        try{
            Map<String,String> map = PropertiesUtils.loadAsMap("chimaera-cli.properties", Config.class);
            HOST = map.get("host");
            PORT = Integer.parseInt(map.get("port"));
        }catch (Exception e){

        }
    }

    public synchronized static ChimaeraCli getChimaeraCli(){
        if(cli == null){
            setup(HOST,PORT);
        }
        return cli;
    }

    public synchronized static ChimaeraCli getChimaeraCli(String[] args){
        if(args!= null && args.length >= 2){
            return newChimaeraCli(args);
        }
        return getChimaeraCli();
    }


    static ChimaeraCli newChimaeraCli(String[] args) {
        String host = HOST;
        int port = PORT;

        if (args != null && args.length > 0 && "?".equals(args[0])) {
            System.out.println(" [host] [port]");
            return null;
        }

        if (args != null && args.length >= 2) {
            host = args[0];
            port = Integer.valueOf(args[1]);
        }
        ChimaeraCli cli = new ChimaeraCli();
        cli.open(host, port);
        return cli;
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

    public static Pandora getPandora(){
         return getChimaeraCli().getPandora("DEFAULT");
    }

    public static  Pandora  getPandora(String name){
        return getChimaeraCli().getPandora(name);
    }

    public static Pandora getPandora(String[] args){
        return getChimaeraCli(args).getPandora("DEFAULT");
    }

    public static void shutdown(){
        getChimaeraCli().close();
        System.exit(0);
    }
}
