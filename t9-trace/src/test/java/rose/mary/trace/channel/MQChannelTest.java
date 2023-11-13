package rose.mary.trace.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.channel.Channel;
import rose.mary.trace.core.channel.ChannelExceptionHandler;
import rose.mary.trace.core.channel.mom.MOMChannel;
import rose.mary.trace.core.config.ChannelManagerConfig;
import rose.mary.trace.core.config.OldStateCheckHandlerConfig;
import rose.mary.trace.core.data.channel.ChannelConfig;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.helper.module.mte.MsgHandler;
import rose.mary.trace.core.monitor.TPS;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.core.monitor.listener.MonitorListener;
import rose.mary.trace.core.monitor.listener.event.MonitorEvent;
import rose.mary.trace.core.parser.BytesMessageParser;
import rose.mary.trace.core.parser.MQMessageParser;
import rose.mary.trace.core.parser.Parser;
import rose.mary.trace.manager.CacheManager;
import rose.mary.trace.manager.ConfigurationManager;

public class MQChannelTest {

    static ConfigurationManager cm = null;

    static CacheManager cacheManager = null;

    static ThroughputMonitor tpm;

    static long delayOnException = 5000;

    static void startMOMChannel(ChannelConfig config, ChannelExceptionHandler channelExceptionHandler,
            OldStateCheckHandlerConfig oschc) throws Exception {

        System.out.println(Util.toJSONString(config));

        String name = config.getName();
        String hostName = config.getHostName();
        String qmgrName = config.getQmgrName();
        int port = config.getPort();
        String channelName = config.getChannelName();
        String queueName = config.getQueueName();
        int waitTime = config.getWaitTime();
        String module = config.getModule();
        String userId = config.getUserId();
        String password = config.getPassword();
        boolean healthCheck = config.isHealthCheck();

        int ccsid = config.getCcsid();
        int characterSet = config.getCharacterSet();
        boolean autoCommit = config.isAutoCommit();
        boolean bindMode = config.isBindMode();

        Map<String, Integer> nodeMap = oschc.getNodeMap();

        Parser parser = null;
        if (MsgHandler.MODULE_MQ.equalsIgnoreCase(module)) {
            parser = new MQMessageParser(nodeMap);
        } else if (MsgHandler.MODULE_ILINK.equalsIgnoreCase(module)) {
            parser = new BytesMessageParser(nodeMap);
        } else {
            throw new Exception("NotFoundParserException");
        }

        long maxCommitWait = config.getMaxCommitWait();
        long delayForNoMessage = config.getDelayForNoMessage();

        int commitCount = config.getCommitCount();

        int maxCacheSize = config.getMaxCacheSize();
        long delayForMaxCache = config.getDelayForMaxCache();

        int idx = 0;

        List<CacheProxy<String, Trace>> caches = cacheManager.getDistributeCaches();

        int[] cacheIndex = config.getCacheIndex();

        List<CacheProxy<String, Trace>> selectCaches = new ArrayList<CacheProxy<String, Trace>>();

        // 캐시설정정보보다 작으면
        if (cacheIndex == null || cacheIndex.length == 0) {
            CacheProxy<String, Trace> c = caches.get(0);
            if (c == null)
                throw new Exception(Util.join("NotFoundCacheException(index:", 0, ")"));
            selectCaches.add(c);
        } else {
            for (int i : cacheIndex) {
                CacheProxy<String, Trace> c = caches.get(i);
                if (c == null)
                    throw new Exception(Util.join("NotFoundCacheException(index:", 0, ")"));
                selectCaches.add(c);
            }
        }

        for (CacheProxy<String, Trace> cache : selectCaches) {
            Channel channel = new MOMChannel(
                    name + (idx++), module, qmgrName, hostName, port,
                    channelName, queueName, waitTime, userId, password, ccsid, characterSet,
                    bindMode, autoCommit, commitCount, maxCommitWait, delayForNoMessage, delayOnException, maxCacheSize,
                    delayForMaxCache, cache);
            if (tpm != null)
                channel.setThroughputMonitor(tpm);
            channel.setParser(parser);
            channel.setHealthCheck(healthCheck);
            channel.setChannelExceptionHandler(channelExceptionHandler);
            channel.setIgnoreCacheSize(false);
            channel.start();

            System.out.println(Util.join("channel(", channel.getName(), ") started"));
        }

    }

    public static void main(String[] args) throws Exception {

        // System.setProperty("rose.mary.home",
        // "/Users/whoana/DEV/workspace-vs/t9/home");
        cm = new ConfigurationManager();

        try {
            cm.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println(Util.toJSONPrettyString(cm));

        tpm = new ThroughputMonitor(3000);
        tpm.setMonitorListener(
                new MonitorListener<TPS>() {

                    @Override
                    public void watch(MonitorEvent<TPS> me) throws Exception {
                        System.out.println("me:" + Util.toJSONPrettyString(me));
                    }

                });
        delayOnException = cm.getChannelManagerConfig().getDelayOnException();

        cacheManager = new CacheManager(cm.getCacheManagerConfig());
        cacheManager.prepare();

        ChannelManagerConfig config = cm.getChannelManagerConfig();
        List<ChannelConfig> channelConfigs = config.getChannelConfigs();

        for (ChannelConfig cc : channelConfigs) {

            if (cc.isDisable())
                continue;
            try {
                startMOMChannel(cc, null, config.getOldStateCheckHandlerConfig());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        tpm.start();

    }

}
