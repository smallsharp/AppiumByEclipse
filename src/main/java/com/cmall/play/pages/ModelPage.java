package com.cmall.play.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;
import com.cmall.appium.Helper;
import com.cmall.http.HttpUtils;
import com.cmall.utils.ImageUtil;
import com.cmall.utils.LogUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ModelPage {

	private AndroidDriver<MobileElement> mdriver;
	private Helper helper;
	private static LogUtil log = new LogUtil(ModelPage.class);
	
 	public ModelPage() {
	}
 	
	public ModelPage(AndroidDriver<MobileElement> mdriver) {
		this.mdriver = mdriver;
		helper = new Helper(mdriver);
	}

	/**
	 * 定制 按钮
	 */
	@AndroidFindBy(id = "com.play.android:id/btn_diy")
	private MobileElement m_btn_diy;
	
	@AndroidFindBy(id = "com.play.android:id/iv_item")
	private List<MobileElement> m_plate_list;

	/**
	 * 定制页面-产品list
	 */
	@AndroidFindBy(id = "com.play.android:id/sdv_image")
	private List<MobileElement> m_sdv_image;

	@AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"com.play.android:id/rela_content\")")
	private MobileElement m_tee_native;
	
	/**
	 * 男装-TEE H5
	 */
	@FindBy(xpath = "//*[@id='pageWrap']/section/div/ul/li[1]/a/img")
	private MobileElement m_tee_h5;

	/**
	 * 3D模型 Navtive
	 */
	@AndroidFindBy(id="com.play.android:id/rela_webview_parent")
	private MobileElement m_3dModel_native;

	/**
	 * 3D模型 H5
	 */
	@FindBy(xpath = "//*[@id='canvas_3d']") 
	private MobileElement m_3dModel_h5; 
	
	/**
	 * 颜色选择器
	 */
	@AndroidFindBy(id="com.play.android:id/tv_color_select_enter")
	private MobileElement m_color_selector;
	
	/**
	 * 档次List
	 */
	@AndroidFindBy(id="com.play.android:id/tv_sku_name")
	private List<MobileElement> m_gradeList;
	
	/**
	 * 颜色List
	 */
	@AndroidFindBy(id="com.play.android:id/sdv_sku_image")
	private List<MobileElement> m_color_List;
	
	/**
	 * 确定按钮：档次和颜色选择页面
	 */
	@AndroidFindBy(id="com.play.android:id/tv_sure")
	private MobileElement m_btn_sure;

	/**
	 * 3D模型 切换选择
	 */
	@AndroidFindBy(id = "com.play.android:id/iv_good")
	private List<MobileElement> m_goods_selector;

	/**
	 * 去购买 按钮
	 */
	@AndroidFindBy(id = "com.play.android:id/btn_buy")
	private MobileElement m_btn_buy;

	/**
	 * 进入3d模型界面
	 * @throws InterruptedException 
	 */
	public void goto3DModelFromHome()  {
		
		log.info("exec:goto3DModelFromHome");
		try {
			
			if (!helper.waitActivity(Play_ActivityList.HOME_ACTIVITY)) {
				helper.backToHomeActivity();
				assertEquals(mdriver.currentActivity(), Play_ActivityList.HOME_ACTIVITY);
			}
			
			if (!(m_sdv_image.size() > 0)) {
				assertTrue(false, "首页产品List，没有加载成功");
			}
			// 点击：定制-男装
			m_sdv_image.get(1).click();

			// 进入男装二级目录，包含：TEE，边框TEE，Polo衫等…
			if (!helper.waitActivity(Play_ActivityList.PRODUCT_CLASSIFITION_ACTIVITY)) {
				assertEquals(mdriver.currentActivity(), Play_ActivityList.PRODUCT_CLASSIFITION_ACTIVITY);
			}
			
			if (!helper.waitElement(m_tee_native)) {
				assertTrue(false, "没有定位到：男装目录下的webview");
			}
			Thread.sleep(2000);
			int x = m_tee_native.getLocation().getX();
			int y = m_tee_native.getLocation().getY();
			int height = m_tee_native.getSize().getHeight();
			mdriver.tap(1, x/2, height/6 + y, 500);
			
			if (helper.waitActivity(Play_ActivityList.LOGIN_ACTIVITY)) {
				
				LoginPage loginPage = new LoginPage(mdriver);
				loginPage.login2("18521035133", "111111");	
				Thread.sleep(2000);
				mdriver.tap(1, x/2, height/6 + y, 500);
			}
			
			if (!helper.waitActivity(Play_ActivityList.GOODS_WEB3DVIEW_ACTIVITY)) {
				assertEquals(mdriver.currentActivity(), Play_ActivityList.GOODS_WEB3DVIEW_ACTIVITY);
			}
			
			if (!helper.waitElement(m_3dModel_native)) {
				assertTrue(false, "3DModel is not displayed");
			}
			
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			assertTrue(false,"failed to locate element!");
		} catch (Exception e){
			e.printStackTrace();
			helper.takeScreenShot("goto3DModelFromHome_error.jpg");
			assertTrue(false,"occurred error while running!");
		}
	}

	/**
	 * 通过切换模型的档次 和 颜色，查看模型的展示
	 * 
	 */
	public void check3DModelByColorAndGrade() {
		
		this.goto3DModelFromHome();
		log.info("exec:check3DModelByColorAndGrade");
		try {
			int x = m_3dModel_native.getLocation().x;// 控件的左上角的x坐标
			int y = m_3dModel_native.getLocation().y;// 控件的左上角的y坐标						
			int w = m_3dModel_native.getSize().width;// 控件的宽度
			int h = m_3dModel_native.getSize().height;// 控件的高度

			m_color_selector.click();// 点击颜色选择
			int gradeSize = m_gradeList.size();
			int colorSize = m_color_List.size();
			System.out.println("gradeSize:"+gradeSize+",colorSize:"+colorSize);
			BufferedImage previousImage = null;
			BufferedImage previousCut = null;
			for (int i = 0; i < colorSize; i++) {
				for (int j = 0; j < 2; j++) {
					log.info("i:"+i+",j:"+j);
					m_gradeList.get(j).click();// 点击普通、中档、中高档、高档等
					m_color_List.get(i).click(); // 点击第一个颜色
					m_btn_sure.click();// 点击确定
					if (!m_3dModel_native.isDisplayed()) {
						assertTrue(false,"切换模型后，模型没有加载出来");
					}

					if (j == 0) {
						String previousJPG = "previous_color_"+i+".jpg";
						helper.takeScreenShot(previousJPG);
						previousImage = ImageUtil.getImageFromFile(new File(Constant.ACTUL_PATH, previousJPG));
						previousCut = ImageUtil.getSubImage(previousImage, x, y, w, h);
					}
					
					String jpg = "grade_"+j+"_color_"+i+".jpg";
					helper.takeScreenShot(jpg);
					
					BufferedImage laterimage = ImageUtil.getImageFromFile(new File(Constant.ACTUL_PATH, jpg));
					BufferedImage laterimageCut = ImageUtil.getSubImage(laterimage, x, y, w, h);// 第二张局部截图
					Thread.sleep(1000);
					Reporter.log("<img src=" + Constant.ACTUL_PATH + jpg + " /img>", true);// 将截图显示在报告中
					
					if (j > 0) {
						System.out.println("previousCut:"+previousCut);
						System.out.println("laterimageCut:"+laterimageCut);
						ImageUtil.sameAs(previousCut, laterimageCut, 0.8);// 对比两张局部截图
					}
					
					if (j < gradeSize-1) {
						m_color_selector.click();// 点击颜色选择
					}
				}
			}
			
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			assertTrue(false,"failed to locate element!");
		} catch (Exception e){
			e.printStackTrace();
			helper.takeScreenShot("check3DModelByColorAndGrade.jpg");
			assertTrue(false,"occurred error while running!");
		}

	}
	
	/**
	 * 遍历3D模型页面所有商品
	 * 
	 */
	public void check3DModelGoodsList() {
		
		this.goto3DModelFromHome();
		log.info("exec:check3DModelGoodsList");
		try {
			String url = "http://android.cmall.com/goodsSite/home/goodsList";
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("productId", "501");
			String response = HttpUtils.sendHttpGet(url, paramsMap);
//			JsonObject jsonObject = HttpUtils.getJsonBydoGet(url,paramsMap);
			JsonObject jsonObject = HttpUtils.getJson(response);
			JsonObject result = (JsonObject) jsonObject.get("result");
			JsonArray pageItems = result.get("pageItems").getAsJsonArray();
			System.out.println("pageItems.size():"+pageItems.size());
			
			for (int i = 0; i < 1; i++) {// 测试用
				if (i < 5) {
					System.out.println("i:"+i);
					m_goods_selector.get(i).click();
				}else {
					System.out.println("i:"+i);
					int x1 = m_goods_selector.get(3).getLocation().x;
					int y1 = m_goods_selector.get(3).getLocation().y;
					int x2 = m_goods_selector.get(4).getLocation().x;
					int y2 = m_goods_selector.get(4).getLocation().y;
					mdriver.swipe(x2, y2, x1-20, y1, 500);
					m_goods_selector.get(4).click();
				}

				if (!m_3dModel_native.isDisplayed()) {
					assertTrue(false, "切换第"+i+"个Goods,模型未加载出来");
				}
			}
			
		} catch (Exception e){
			helper.takeScreenShot("check3DModelGoodsList.jpg");
			e.printStackTrace();
			assertTrue(false,"occurred error while running!");
		}
	}

}
