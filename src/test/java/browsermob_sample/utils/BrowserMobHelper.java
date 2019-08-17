package browsermob_sample.utils;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class BrowserMobHelper {
    private static BrowserMobHelper ourInstance = new BrowserMobHelper();
    private static BrowserMobProxy proxy;

    public static BrowserMobHelper getInstance() {
        if(proxy == null) {
            proxy = new BrowserMobProxyServer();
            proxy.start(8888);
            System.out.println("=== Proxy has been started ===");
        }
        return ourInstance;
    }

    public void stopProxy() {
        if(proxy != null) {
            proxy.stop();
            proxy = null;
            System.out.println("=== Proxy is stopped ===");
        }
    }

    public BrowserMobHelper startRecording() {
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        proxy.newHar();
        return this;
    }

    public Har stopRecording() {
        if(proxy.getHar() != null) {
            return proxy.endHar();
        }
        return null;
    }

    public BrowserMobHelper overwriteGitHubResponse() {
        proxy.addResponseFilter((response, contents, messageInfo) -> {
            if (messageInfo.getUrl().contains("https://api.github.com/search/repositories")
                    && messageInfo.getUrl().contains("kotlin")) {
                contents.setTextContents(contents.getTextContents().replace("kotlin", "shmotlin"));
            }
        });

        return this;
    }
}