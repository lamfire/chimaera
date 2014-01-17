package com.lamfire.chimaera.client;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-16
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class RebundleMonitor {
    private static final Logger LOGGER = Logger.getLogger(RebundleMonitor.class);
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1, Threads.makeThreadFactory("RebundleMonitor"));

    public static void shutdownMonitorThread(){
        service.shutdown();
    }

    private Map<String, Rebundler> map = new HashMap<String, Rebundler>();

    public RebundleMonitor() {
        service.scheduleWithFixedDelay(checker, 30, 30, TimeUnit.SECONDS);
    }

    public void add(Rebundler reBundler) {
        String entryKey = entryKey(reBundler.getKey(), reBundler.getClientId());
        this.map.put(entryKey, reBundler);
    }

    private String entryKey(String key, String clientId) {
        return key + ":" + clientId;
    }

    public Rebundler remove(String key, String clientId) {
        String entryKey = entryKey(key, clientId);
        return this.map.remove(entryKey);
    }

    Runnable checker = new Runnable() {
        @Override
        public void run() {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[RebundleMonitor]:check bundles [" + map.size() + "]");
            }
            for (Map.Entry<String, Rebundler> e : map.entrySet()) {
                Rebundler rebundler = e.getValue();
                if (!rebundler.isAvailable()) {
                    LOGGER.info("[RebundleMonitor]: the bundle was unavailable :" + rebundler);
                    rebundler.rebind();
                }
            }
        }
    };


}
