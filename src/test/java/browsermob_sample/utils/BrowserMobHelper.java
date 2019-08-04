package browsermob_sample.utils;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

public class BrowserMobHelper {
    private static BrowserMobHelper ourInstance = new BrowserMobHelper();

    public static BrowserMobHelper getInstance() {
        return ourInstance;
    }

    private BrowserMobProxy proxy;

    public BrowserMobHelper startProxy() {
        proxy = new BrowserMobProxyServer();
        proxy.start(8888);
        System.out.println("===== Proxy has been started =====");
        return this;
    }

    public void stopProxy() {
        proxy.stop();
        proxy = null;
        System.out.println("===== Proxy is stopped =====");
    }
}