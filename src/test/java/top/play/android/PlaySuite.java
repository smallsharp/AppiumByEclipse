package top.play.android;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import top.base.appium.AppiumServerUtils;
import top.base.appium.DriverFactory;
import top.base.appium.TestNGListener;
import top.base.utils.CommandUtil;

@Listeners(TestNGListener.class)
public class PlaySuite {

	private AndroidDriver<MobileElement> mdriver = null;
	
	private URL url;

	@BeforeClass(alwaysRun = true)
	public void startAppiumServer() {
		AppiumServerUtils.getInstance().startServer("127.0.0.1", 4723);
		url = AppiumServerUtils.getInstance().getService().getUrl();
	}

	@BeforeClass(alwaysRun = true, dependsOnMethods = { "startAppiumServer" })
	public void setup() {
		Reporter.log("========== 正在准备测试环境，预计20s，请稍后 ==========", true);
		if (mdriver == null) {
			mdriver = DriverFactory.getInstance().initAndroidDriver(url);
		}
	}

	@AfterClass
	public void teardown() throws InterruptedException {

		if (mdriver != null) {
			mdriver.closeApp();
			CommandUtil.exec_shell("pm clear com.play.android");
		}
		AppiumServerUtils.getInstance().stopServer();
		Reporter.log("========== 测试用例执行完成，BYE BYE ==========", true);

	}

	@Test(description = "登录")
	@Parameters({ "mobile", "password" }) // 参数名称需要和xml中对应
	public void testLogin(String mobile, String password) throws InterruptedException {
		LoginPage loginPage = new LoginPage(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver, 5,TimeUnit.SECONDS), loginPage);
//		PageFactory.initElements(mdriver, LoginPage.class);
		loginPage.login(mobile, password);
	}
	
	@Test
	public  void testSignIn() {
		
		LoginPage loginPage = new LoginPage(mdriver);
		PageFactory.initElements(new AppiumFieldDecorator(mdriver, 5,TimeUnit.SECONDS), loginPage);
		loginPage.signIn();

	}

	@Test(description = "购买男装")
	public void testBuyMenWear() throws InterruptedException {
		Play_DIYPage diyPage = PageFactory.initElements(mdriver, Play_DIYPage.class);
		diyPage.buyMenWear();
	}

	@Test(description = "个人设置")
	public void testModifyPersonalInfo() {
		PersonModifyPage personModifyPage = PageFactory.initElements(mdriver, PersonModifyPage.class);
		personModifyPage.modifyPersonInfo();
	}

	@Test(description = "添加收货地址")
	public void testAddAdress() {
		PersonPage personPage = PageFactory.initElements(mdriver, PersonPage.class);
		personPage.add_Delivery_Address();

	}

	@Test(description = "退出登录")
	public void testLogout() {
		PersonPage personPage = PageFactory.initElements(mdriver, PersonPage.class);
		personPage.logout();
	}

}
