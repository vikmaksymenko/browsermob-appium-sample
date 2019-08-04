package browsermob_sample;

import browsermob_sample.utils.BrowserMobHelper;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Description;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SampleBrowsermobTest {

    private AppiumDriverLocalService appiumService;

    @BeforeClass
    public void setup() {
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumService = appiumServiceBuilder.withIPAddress("127.0.0.1").build();
        appiumService.start();

        if (!appiumService.isRunning()) {
            throw new AppiumServerHasNotBeenStartedLocallyException("An appium server node is not started!");
        }

        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Y15HFBP8228WA");
        caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        caps.setCapability("allowTestPackages", true);

        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.APP,
                        new File("src/test/resources/browsermob_sample/app-debug.apk").getAbsolutePath());

        WebDriverRunner.setWebDriver(new AndroidDriver(appiumService.getUrl(), caps));

        BrowserMobHelper.getInstance().startProxy();
    }

    @Test
    @Description("JetBrains Kotlin should be in list")
    public void jetBrainsKotlinShouldBeInList() {
        $(By.id("com.raywenderlich.githubrepolist:id/refreshButton")).shouldBe(visible).click();
        $x("//android.widget.TextView[@resource-id='com.raywenderlich.githubrepolist:id/repoName' and @text='JetBrains/kotlin']")
                .waitUntil(exist, 30 * 1000).shouldBe(visible);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        BrowserMobHelper.getInstance().stopProxy();
        appiumService.stop();
    }
}
