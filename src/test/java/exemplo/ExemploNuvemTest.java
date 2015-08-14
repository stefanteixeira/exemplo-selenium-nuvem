package exemplo;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;

@RunWith(ConcurrentParameterized.class)
public class ExemploNuvemTest implements SauceOnDemandSessionIdProvider {

	@Rule
	public TestName testNameRule = new TestName();

    private String browser;
    private String os;
    private String version;
    private String sessionId;
    
    private WebDriver driver;

    public ExemploNuvemTest(String os, String version, String browser) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{"Windows 8.1", "11", "internet explorer"});
        browsers.add(new String[]{"OSX 10.8", "6", "safari"});
        return browsers;
    }

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        if (version != null) {
            capabilities.setCapability(CapabilityType.VERSION, version);
        }
        capabilities.setCapability(CapabilityType.PLATFORM, os);        
        capabilities.setCapability("name", testNameRule.getMethodName());
        
        this.driver = new RemoteWebDriver(new URL("http://fav-organizer:67f84d23-778a-4def-b579-c98c363a097f@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
        
        driver.get("http://54.149.253.66:3000/#/bookmarks");
    }

    @Test
    public void verificarNumeroBookmarks() {
    	String infoNumeroBookmarks = driver.findElement(By.xpath("//div/p[2]")).getText();
    	
    	assertEquals("Bookmarks added: 5", infoNumeroBookmarks);
    }

    @Test
    public void verificarRedirecionamentoBotaoNew() {
    	WebElement btnNewBookmark = driver.findElement(By.id("btnNewBookmark"));
    	btnNewBookmark.click();
    	
    	assertEquals("http://54.149.253.66:3000/#/bookmark", driver.getCurrentUrl());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    public String getSessionId() {
        return sessionId;
    }
}