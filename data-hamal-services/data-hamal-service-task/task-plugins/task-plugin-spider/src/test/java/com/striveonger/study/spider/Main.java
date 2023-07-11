package com.striveonger.study.spider;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-06 11:23
 */
public class Main {

    @Test
    public void test() {

        System.setProperty("webdriver.chrome.driver", "/Users/striveonger/software/chromedriver");
        ChromeDriver driver = null;
        try {
            // 1. 创建浏览器对象
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            // 2. 打开登录页
            driver.get("https://www.google.com/");



            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) { driver.quit(); }
            driver = null;
        }
    }

}
