package com.cmall.temp;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * TestNg 用代码执行testNg的几种方式
 * @author cm
 *
 */
public class TestngRunByJava {
	
	@Test
	public void show() {
		System.out.println("show ~~~~");
	}
	
	
/*	public static void main(String[] args) {
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[] {Demo.class});
		testNG.run();
	}*/
	
/*	public static void main(String[] args) {
		TestNG testNG = new TestNG();
		List<String> suites = new ArrayList<>();
		suites.add("");// 这里添加testng.xml 的路径
		testNG.setTestSuites(suites);
		testNG.run();
	}*/
	
	public static void main(String[] args) {
		
		XmlSuite suite = new XmlSuite();
		suite.setName("playSuite");
		
		XmlTest test = new XmlTest(suite);
		test.setName("testName");
		
		List<XmlClass> classes = new ArrayList<>();
		classes.add(new XmlClass("top.temp.Demo"));
		test.setXmlClasses(classes);
		
		
		List<XmlSuite> suites = new ArrayList<>();
		suites.add(suite);
		
		TestNG testNG = new TestNG();
		testNG.setUseDefaultListeners(false);
		testNG.setXmlSuites(suites);
		testNG.run();
		
	}

}
