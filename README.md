## 简介

集结最新主流时尚开源技术的面向互联网Web应用的整合前端门户站点、HTMl5移动站点及后端管理系统一体的的基础开发框架，提供一个J2EE相关主流开源技术架构整合及一些企业应用基础通用功能和组件的设计实现的最佳实践和原型参考。

### 项目托管同步更新站点列表：

**https://github.com/xautlx/s2jh4net**

**http://git.oschina.net/xautlx/s2jh4net**

### 个人空间： http://my.oschina.net/s2jh

> **提示**：为了方便用户区分链接资源是外部和内部，文档特别以![link](http://git.oschina.net/xautlx/s2jh4net/raw/master/src/main/webapp/docs/markdown/images/link.gif)标识：链接前面有此图标说明是外部链接，如果你已经熟悉相关概念可以忽略点击；没有此标识说明是文档内部链接，建议点击访问以完整查阅项目文档。

## Features

* 面向主流企业级WEB应用系统的界面和常用基础功能设计实现
* 主体基于主流的（Spring MVC + Spring3 + Hibernate4）架构
* 引入JPA、Spring-Data-JPA提升持久层架构规范性和开发效率
* 基于流行JQuery/Bootstrap等UI框架和插件整合，良好的浏览器兼容性和移动设备访问支持
* 提供一个基础的代码生成框架，简化实现基本的CRUD功能开发
* 基于Maven的项目和组件依赖管理模式，便捷高效的与持续集成开发集成

## Architecture

* [Technical List](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E6%8A%80%E6%9C%AF%E5%88%97%E8%A1%A8.md) - 框架主要技术(Java/Web/Tool)组件列表介绍
* [Enhanced Grid](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E8%A1%A8%E6%A0%BC%E7%BB%84%E4%BB%B6.md) - 功能强大的Grid表格组件扩展增强
* [Technical Features](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E6%8A%80%E6%9C%AF%E7%89%B9%E6%80%A7.md) - 主要技术选型和设计说明
* [Mobile Support](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E7%A7%BB%E5%8A%A8%E6%94%AF%E6%8C%81.md) - 以Android为例的Web App与Native App整合应用

## Development Guide

* [Development Configuration](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E5%BC%80%E5%8F%91%E9%85%8D%E7%BD%AE.md) - 开发基础环境配置说明
* [Code Generator](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90.md) - 用于基本CURD框架代码生成的工具

> 由于项目采用了Lombok等插件，如果你想把git获取工程代码导入开发环境，请一定提前浏览文档 [Development Configuration](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E5%BC%80%E5%8F%91%E9%85%8D%E7%BD%AE.md) 为IDE进行Lombok等插件配置，否则会出一大堆编译错误。

> 为了便于开发过程参考，项目直接把开发相关的参考样例和指南文档嵌入到运行部署应用中，具体内容可在线浏览，如下图Snapshot部分截图示意。

## Online Demo

**http://120.25.147.207/s2jh4net**

前端门户和HTML5移动站点目前内容较少，主要演示集中在管理端：http://120.25.147.207/s2jh4net/admin , 账号：admin，密码：admin123，或直接点击右下方的“超级管理员”登录链接即可。

在线演示站点为单点低配阿里云服务器，可能存在访问缓慢情况或更新时中断。同时为了防止随意数据变更导致系统崩溃，对个别功能启用了演示禁用控制。

建议可参考 [Development Configuration](https://github.com/xautlx/s2jh4net/blob/master/src/main/webapp/docs/markdown/%E5%BC%80%E5%8F%91%E9%85%8D%E7%BD%AE.md) 在本地运行完整体验。

> 项目最新自动化持续集成构建状态 Travis-CI Status: [![Build Status](https://travis-ci.org/xautlx/s2jh4net.svg?branch=master)](https://travis-ci.org/xautlx/s2jh4net)

## Snapshot

![ui-signin](http://git.oschina.net/xautlx/s2jh4net/raw/master/src/main/webapp/docs/markdown/images/img-0065.jpg)

![ui-example](http://git.oschina.net/xautlx/s2jh4net/raw/master/src/main/webapp/docs/markdown/images/ui-example.jpg)

## S2JH4Net vs S2JH

**重要提示：** 由于个人精力有限，目前主要重心都已放在S2JH4Net版本更新维护，原有S2JH版本已基本暂停更新！

此项目为 S2JH （ https://github.com/xautlx/s2jh 或 http://git.oschina.net/xautlx/s2jh ） 项目的兄弟项目，主要差异简介：

* s2jh基于Struts2，s2jh4net基于Spring MVC
* s2jh4net在原有s2jh只面向企业应用系统开发的基础上，重新组织模块和结构来支持典型的互联网站点以及HTML5移动站点开发的支持；
* 为了简化开发和构建的复杂度，还原采用单一all-in-one的动态Web工程模式，不再进行复杂的Maven模块化布局（但是Maven进行依赖管理和构建还是保留）；
* 实体对象属性定义采用了Lombok来简化繁琐的getter和setter定义；
* JPA Hibernate和MyBatis整合应用；
* 权限框架改用Apache Shiro；
* 自动基于注解定义的菜单、权限等数据生成数据库基础配置，采用Java编码方式定义基础数据，抛弃之前的SQL脚本方式；一边开发编码，一边项目实施；

### License Agreement

* Free License

本项目代码除src/main/webapp/assets目录下admin/app和w/app两个目录下相关Javascript代码以混淆方式提供外，其余开源，在保留标识本项目来源信息以及保证不对本项目进行非授权的销售行为的前提下，可以以任意方式自由免费使用：开源、非开源、商业及非商业。

* Charge Support Service

如果你还有兴趣在现有开放资源基础上进一步提供定制的扩展实现/技术咨询服务/毕业设计指导/二次开发项目指导等方面的合作意向，可联系 E-Mail: s2jh-dev@hotmail.com 或 QQ: 2414521719 (加Q请注明：s2jh4net) 洽谈。[上述联系方式恕不直接提供咨询类询问！若对项目有任何技术问题或Issue反馈，请直接提交到项目站点提问或Git平台的Issue]


### Reference

欢迎关注作者其他项目：

* [Nutch 2.X AJAX Plugins (Active)](https://github.com/xautlx/nutch-ajax) -  基于Apache Nutch 2.3和Htmlunit, Selenium WebDriver等组件扩展，实现对于AJAX加载类型页面的完整页面内容抓取，以及特定数据项的解析和索引

* [S2JH4Net (Active)](https://github.com/xautlx/s2jh4net) -  基于Spring MVC+Spring+JPA+Hibernate的面向互联网及企业Web应用开发框架

* [S2JH (Deprecated)](https://github.com/xautlx/s2jh) -  基于Struts2+Spring+JPA+Hibernate的面向企业Web应用开发框架
 
* [Nutch 1.X AJAX Plugins (Deprecated)](https://github.com/xautlx/nutch-htmlunit) -  基于Apache Nutch 1.X和Htmlunit的扩展实现AJAX页面爬虫抓取解析插件
 
* [12306 Hunter (Deprecated)](https://github.com/xautlx/12306-hunter) - （功能已失效不可用，不过还可以当作Swing开发样列参考只用）Java Swing C/S版本12306订票助手，用处你懂的

