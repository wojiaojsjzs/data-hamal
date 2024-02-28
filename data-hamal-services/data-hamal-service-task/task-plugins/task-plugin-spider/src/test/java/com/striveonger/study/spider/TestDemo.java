package com.striveonger.study.spider;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-02-27 20:38
 */
public class TestDemo {
    private final Logger log = LoggerFactory.getLogger(TestDemo.class);

    @Test
    public void test() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/Users/striveonger/software/chromedriver");
        WebDriver driver = null;
        try {
            // 0. 创建浏览器对象
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://ceac.state.gov/genniv/");

            // 1.选择下拉框中的国家
            WebElement element = driver.findElement(By.id("ctl00_SiteContentPlaceHolder_ucLocation_pnlLocation")).findElement(By.cssSelector("div>select"));
            Select select = new Select(element);
            select.selectByIndex(44); // 页面会刷新..
            while (true) {
                // 重新获取页面元素
                element = driver.findElement(By.id("ctl00_SiteContentPlaceHolder_ucLocation_pnlLocation")).findElement(By.cssSelector("div>select"));
                if ("BEJ".equals(element.getAttribute("value"))) {
                    break;
                }
                Thread.sleep(1000);
            }
            // 2.验证码下载
            Thread.sleep(3000);
            element = driver.findElement(By.id("c_default_ctl00_sitecontentplaceholder_uclocation_identifycaptcha1_defaultcaptcha_CaptchaImage"));
            byte[] bytes = element.getScreenshotAs(OutputType.BYTES);
            FileUtil.writeBytes(bytes, "/Users/striveonger/Desktop/test.jpg");
            // 3. 识别验证码



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
            driver = null;
        }
    }
}
