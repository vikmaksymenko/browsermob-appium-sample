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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SampleBrowsermobTest {

    private AppiumDriverLocalService appiumService;
    private BrowserMobHelper bmpHelper = BrowserMobHelper.getInstance();

    @BeforeMethod
    public void setup() {
        bmpHelper.startRecording();

        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumService = appiumServiceBuilder.withIPAddress("127.0.0.1").build();
        appiumService.start();

        if (!appiumService.isRunning()) {
            throw new AppiumServerHasNotBeenStartedLocallyException("An appium server node is not started!");
        }

        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "4200da729a92641d");
        caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        caps.setCapability("allowTestPackages", true);

        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.APP,
                        new File("src/test/resources/browsermob_sample/app-debug.apk").getAbsolutePath());

        WebDriverRunner.setWebDriver(new AndroidDriver(appiumService.getUrl(), caps));
    }

    @Test
    @Description("JetBrains Kotlin should be in list")
    public void jetBrainsKotlinShouldBeInList() {
        $(By.id("com.raywenderlich.githubrepolist:id/refreshButton")).shouldBe(visible).click();
        $x("//android.widget.TextView[@resource-id='com.raywenderlich.githubrepolist:id/repoName' and @text='JetBrains/kotlin']")
                .waitUntil(exist, 30 * 1000).shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws IOException {
        bmpHelper.stopRecording().writeTo(new File("sample.har"));
        bmpHelper.stopProxy();
        appiumService.stop();
    }
}
