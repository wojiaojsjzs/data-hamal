# Data Hamal

## 介绍

数据的搬运工, 一个 ETL 工具.

近期的工作, 也是在为公司, 开发商业的ETL工具. 

设立这个项目的初衷, 是实践一些工作中, 不能实现的些想法. 当然也会搬过来一些觉得不错设计思路(以学习为目的的搬运～). 

就当是, 平行时空里的另一个自己, 做了另外的选择吧.

## 软件架构

软件架构说明

## 系统模块

~~~
com.striveonger.study     
├── data-hamal-web                               // 前端框架 [80]
├── data-hamal-gateway                           // 网关模块 [8080]
├── data-hamal-auth                              // 认证中心 [9010]
├── data-hamal-commons                           // 通用模块
│     └── data-hamal-commons-apis                // 全局接口
│     └── data-hamal-commons-beans               // 公共实体
│     └── data-hamal-commons-tools               // 通用工具
│     └── data-hamal-commons-constants           // 公共常量
│     └── data-hamal-commons-generate            // 代码生成
├── data-hamal-services                          // 开放服务
│     └── data-hamal-services-files              // 文件服务 [9020]
│     └── data-hamal-services-contexts           // 对象服务 [9030]
│     └── data-hamal-services-tasks              // 任务服务 [9040]
├──pom.xml                                       // 公共依赖
~~~

>   服务碎片化

## 安装教程

## 使用说明
