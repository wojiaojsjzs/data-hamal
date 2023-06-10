package com.striveonger.study.spider;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-05 15:54
 */
public class Test {


    private final static String[] arr = {
            "Bareboat Broker",
            "Bareboat Contract",
            "Bareboat Expense",
            "Bareboat Hire",
            "Barge",
            "Benchmark Estimate",
            "Benchmark Estimate Bunker",
            "Benchmark Result",
            "Benchmark Result Bunker",
            "Benchmark Sensitivity",
            "Benchmark Sensitivity Bunker",
            "Benchmark Sensitivity Calculation",
            "Berth Blockage",
            "Bunker Exposure",
            "Bunker Inquiries and Purchases",
            "Bunker Inquiries and Purchases Bunker",
            "Bunker Inquiries And Purchases Detail",
            "Bunker Invoice",
            "Bunker Invoice Bunker",
            "Bunker Lifting Detail",
            "Bunker Requirement",
            "Bunker Requirement Bunker",
            "Business Rule",
            "Cargo",
            "Cargo Booking",
            "Cargo Booking Tank Info",
            "Cargo Broker",
            "Cargo Bunker Consumption",
            "Cargo Exposure Bunker",
            "Cargo Extra Freight Term",
            "Cargo Grades",
            "Cargo Handling Event Type",
            "Cargo Itinerary",
            "Cargo Linked Trade",
            "Cargo Load Discharge Option",
            "Cargo Matching Program",
            "Cargo Name",
            "Cargo Name Alias",
            "Cargo Remark",
            "Cargo Revenue Expense",
            "Cargo Supplier Receiver",
            "Cargo Time Bar",
            "Cargo Transfer",
            "Cargo Type",
            "Cargo Type Group",
            "Chart of Accounts",
            "Claim",
            "Claim Note",
            "Claim Projected Invoice",
            "COA",
            "COA Broker",
            "COA Details",
            "COA Extra Freight Terms",
            "COA Hierarchy",
            "COA Itinerary",
            "COA Option",
            "COA Period",
            "Collab Status",
            "Company",
            "Config Flag",
            "Config Options",
            "Correlation Profile",
            "Country",
            "Currency",
            "Data Form View",
            "Data Form View Field",
            "Decision Table",
            "Decision Table Rule Report",
            "Delay",
            "Delay Bunker",
            "Delay Reason",
            "Delay Type",
            "Demurrage Allocation Journal Details",
            "Department",
            "Equipment Contract History",
            "Estimate Cargo",
            "Estimate Cargo Broker",
            "Estimate Cargo Bunker Consumption",
            "Estimate Cargo Extra Freight Term",
            "Estimate Itinerary",
            "Estimate Itinerary Bunker",
            "Estimate Itinerary Carbon",
            "Exchange Rate",
            "Expense",
            "Expense Standards",
            "Extra Freight Term",
            "Financials And Operations Invoice Notes",
            "Financials Invoice",
            "Financials Invoice Detail",
            "Financials Invoice Detail Cargo Allocation",
            "Financials Invoice Note",
            "Financials Invoice Payments",
            "Fixture",
            "Fixture Remark",
            "Freight Invoice",
            "Fuel Type",
            "Fuel Zone",
            "Fuel Zone Set",
            "Group Member",
            "Head Fixture Purchase Sale",
            "Holiday",
            "Imos User",
            "Interface Messages and Notifications",
            "Job Status",
            "Laytime",
            "Laytime Cargo & Root Cause Allocation Details",
            "Laytime Port Activities",
            "Laytime Port Deduction",
            "Laytime Ports",
            "Laytime Terms",
            "Load Discharge Table Rates",
            "Load/Discharge Rate Standards",
            "Loadline Zone Seasons",
            "Loadline Zones",
            "Lob",
            "Market",
            "Market Cargo",
            "Market Data",
            "Market Position",
            "Market Position Source",
            "Master Contract",
            "Module Access",
            "Notification",
            "Object Access",
            "Object Approval",
            "Operations Invoice",
            "Operations Invoice Approval",
            "Operations Invoice Details",
            "Operations Ledger",
            "Operations Payment Details",
            "Paper Trade",
            "Paper Trade Exposure Period",
            "Payment",
            "Payment Batch",
            "Payment Batch Details",
            "Payment Detail",
            "Payment Term",
            "Pool",
            "Pool Admin Account",
            "Pool Assignment",
            "Pool Assignment Premium",
            "Pooling Distribution",
            "Pooling Distribution Premium",
            "Port",
            "Port Activity",
            "Port Activity Bunker",
            "Port Area",
            "Port Berth",
            "Port Cargo Compat",
            "Port Cargo Compat Berth",
            "Port Expense",
            "Port Expense Misc Details",
            "Port Expense Standard Detail",
            "Port Fuel Availability",
            "Port Function",
            "Port Vessel",
            "Port Vessel Berth",
            "Position",
            "Region",
            "Reminder",
            "Report",
            "Report User View",
            "Report View",
            "Revision",
            "Revision Panel Content",
            "Revision Row",
            "Schedule Barge",
            "Shared Resources",
            "Standard Expense",
            "Standard Rate",
            "Subscription",
            "Survey Cost Details",
            "Survey Costs Bunkers",
            "Tank Condition",
            "Task",
            "Task Group",
            "Task Group Item",
            "Tasks and Alerts",
            "TC Deduction",
            "Team",
            "Term",
            "Time Charter",
            "Time Charter Broker",
            "Time Charter Bunker Delivery",
            "Time Charter Bunker Exposure",
            "Time Charter COA",
            "Time Charter COA Delivery Redelivery Option",
            "Time Charter COA Vessel",
            "Time Charter COA Vessel Type",
            "Time Charter Expense",
            "Time Charter Exposure Period",
            "Time Charter Extra",
            "Time Charter Hire",
            "Time Charter Lease",
            "Time Charter Lifting Option",
            "Time Charter Out Details",
            "Time Charter Port Consumption",
            "Time Charter Profit Share",
            "Time Charter Profit Share Contracts",
            "Time Charter Projected Invoice",
            "Time Charter Purchase Option",
            "Time Charter Sea Consumption",
            "Time Charter Sea Consumption Bunker",
            "Time Charter Tonnage Tax",
            "Time Charter Warranty Consumption",
            "Time Charter Warranty Delay Exclusion",
            "Time Zone Codes",
            "Time Zone Info",
            "Trade Area",
            "Trade Broker",
            "Trade Contract",
            "Trade Filter",
            "Trade Filter Detail",
            "Trade Index",
            "Trade Pnl",
            "Trade Pnl Detail",
            "Trade Snapshot",
            "Trade Type",
            "Trading Profile",
            "Trading Profile Property",
            "Units of Measure",
            "Unpriced Element",
            "Unpriced Element Structure",
            "User",
            "User Field",
            "Veslink Form",
            "Veslink Form",
            "Veslink Form Template",
            "Vessel",
            "Vessel Boats",
            "Vessel Bunker Tank",
            "Vessel Crane",
            "Vessel Details",
            "Vessel Draft",
            "Vessel Draft Marks",
            "Vessel Evaluation",
            "Vessel Fleet Member",
            "Vessel Hatch",
            "Vessel Hold",
            "Vessel Incident",
            "Vessel Inspection",
            "Vessel Port Consumption",
            "Vessel Route",
            "Vessel Sea Cons",
            "Vessel Sea Cons Bunker",
            "Vessel Tank",
            "Vessel Tce Target",
            "Vessel Type",
            "Vessel Vetting",
            "Voyage",
            "Voyage Activity Report",
            "Voyage And Tco Fixture",
            "Voyage Bunker Inventory",
            "Voyage Bunker Inventory Detail",
            "Voyage Bunker Lifting",
            "Voyage Bunker Summary",
            "Voyage Cargo Handling",
            "Voyage Cargo Handling Document",
            "Voyage Cargo Handling Event",
            "Voyage Cargo Handling Stowage",
            "Voyage Contact",
            "Voyage Emissions",
            "Voyage Emissions Detail",
            "Voyage Equipment Event",
            "Voyage Estimate",
            "Voyage Estimate Bunker",
            "Voyage Estimate COACVC Analysis",
            "Voyage Estimate COACVC Analysis Result",
            "Voyage Estimate COACVC Analysis Result Bunker",
            "Voyage Estimate Misc Expense",
            "Voyage Estimate Port Expense",
            "Voyage Estimate Time Charter Expense",
            "Voyage Instruction Form",
            "Voyage Itinerary",
            "Voyage Itinerary Berth",
            "Voyage Itinerary Bunker",
            "Voyage Leg Details",
            "Voyage Leg Summary",
            "Voyage Pnl",
            "Voyage Pnl Bunker",
            "Voyage Pnl Bunker Detail",
            "Voyage Pnl Cargo",
            "Voyage Pnl Detail",
            "Voyage Pnl Drilldown",
            "Voyage Pnl Itinerary Line",
            "Voyage Pnl Itinerary Line Bunker",
            "Voyage Pnl Summary",
            "Voyage Port Activities",
            "Voyage Port Agent",
            "Voyage Profit Share",
            "Voyage Projected Invoice",
            "Voyage Remarks",
            "Voyage Template",
            "Waterway",
            "Weather State",
            "Worksheet",
            "Worksheet Estimate",
            "Worksheet Price Component",
            "Worksheet Vessel"
    };


    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/Users/striveonger/software/chromedriver");
        ChromeDriver driver = null;
        try {
            // 1. 创建浏览器对象
            driver = new ChromeDriver();
            driver.manage().window().maximize();

            // 2. 打开登录页
            driver.get("https://www.veslink.com/Account/Login");

            // 3. 输入用户名密码
            WebElement username = driver.findElement(By.id("username"));
            username.sendKeys("dengcf@sdshipping.cn");
            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys("sdsc@VIP2023");

            // 4. 登录
            WebElement login = driver.findElement(By.id("login-button"));
            login.click();

            // 5. 获取导出列表
            do { Thread.sleep(5000); } while (!driver.getCurrentUrl().equals("https://www.veslink.com/#analytics/reports"));

            System.out.println("开始解析....");

            createReport(driver, arr[0]);




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // if (driver != null) { driver.quit(); }
            // driver = null;
        }

    }


    private static void createReport(ChromeDriver driver, String name) throws Exception {
        driver.get("https://www.veslink.com/#analytics/reports/designer/new");
        Thread.sleep(6000);
        // 输入报表名
        WebElement reportTitle = driver.findElement(By.className("cell-input"));
        reportTitle.clear();
        // Thread.sleep(2000);
        String reportName = reportName(name);

        reportTitle.sendKeys(reportName);

        // WebElement outputType = driver.findElement(By.id("react-select-6--value"));
        // outputType.click();



    }

    private static String reportName(String name) {
        return "ODS_" + name.replace(" ", "_").replace("/", "_").replace("&", "_");
    }




    private static void post(ChromeDriver driver) throws Exception {
        // driver.executeScript("")

    }

}