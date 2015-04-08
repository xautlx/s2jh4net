## 简介

集结最新主流时尚开源技术的面向互联网Web应用的整合前端门户站点、HTMl5移动站点及后端管理系统一体的的基础开发框架，提供一个J2EE相关主流开源技术架构整合及一些企业应用基础通用功能和组件的设计实现的最佳实践和原型参考。

### 项目托管同步更新站点列表：

**https://github.com/xautlx/s2jh4net**

**http://git.oschina.net/xautlx/s2jh4net**

### 个人空间： http://my.oschina.net/s2jh

## 关于S2JH说明

此项目为 S2JH （ https://github.com/xautlx/s2jh 或 http://git.oschina.net/xautlx/s2jh ） 项目的兄弟项目，主要差异简介：

* s2jh基于Struts2，s2jh4net基于Spring MVC
* s2jh4net在原有s2jh只面向企业应用系统开发的基础上，重新组织模块和结构来支持典型的互联网站点以及HTML5移动站点开发的支持；
* 为了简化开发和构建的复杂度，还原采用单一的动态Web工程模式，不再进行复杂的Maven模块化布局（但是Maven进行依赖管理和构建还是保留）；
* 实体对象属性定义采用了Lombok来简化繁琐的getter和setter定义；
* JPA Hibernate和MyBatis整合应用；
* 权限框架改用Apache Shiro；
* 自动基于注解定义的菜单、权限等数据生成数据库基础配置，采用Java编码方式定义基础数据，抛弃之前的SQL脚本方式；一边开发编码，一边项目实施；

* 目前本项目文档尚在编写完善中，暂时请参考 [S2JH](http://git.oschina.net/xautlx/s2jh) 相关资料

> 项目最新自动化持续集成构建状态 Travis-CI Status: [![Build Status](https://travis-ci.org/xautlx/s2jh4net.svg?branch=master)](https://travis-ci.org/xautlx/s2jh4net)

## 运行方式

目前先提供直接导入Eclipse（spring-tool-suite-3.6.3.RELEASE）开发部署方式，后面逐步添加一键启动支持。

**详细运行指南请参考项目doc目录下相关Markdown或HTML格式文档**，基本过程描述如下：

* 首先为Eclipse安装Lombok插件（否则工程编译错误）：下载lombok.jar后以java -jar方式运行按照提示选择Eclipse目录安装即可，详见：http://projectlombok.org/download.html
* 从GIT Clone项目到本地，然后import到本地spring-tool-suite-3.6.3.RELEASE
* 项目上或pom.xml执行maven install，初始下载所有需要的依赖资源
* 刷新工程，检查确保整个工程没有编译错误
* 部署到Eclipse Server开发模式，启动运行

### Reference

欢迎关注作者其他项目：

* [Nutch 2.X AJAX Plugins (Active)](https://github.com/xautlx/nutch-ajax) -  基于Apache Nutch 2.3和Htmlunit, Selenium WebDriver等组件扩展，实现对于AJAX加载类型页面的完整页面内容抓取，以及特定数据项的解析和索引

* [S2JH4Net (Active)](https://github.com/xautlx/s2jh4net) -  基于Spring MVC+Spring+JPA+Hibernate的面向互联网及企业Web应用开发框架

* [S2JH (Deprecated)](https://github.com/xautlx/s2jh) -  基于Struts2+Spring+JPA+Hibernate的面向企业Web应用开发框架
 
* [Nutch 1.X AJAX Plugins (Deprecated)](https://github.com/xautlx/nutch-htmlunit) -  基于Apache Nutch 1.X和Htmlunit的扩展实现AJAX页面爬虫抓取解析插件
 
* [12306 Hunter (Deprecated)](https://github.com/xautlx/12306-hunter) - （功能已失效不可用，不过还可以当作Swing开发样列参考只用）Java Swing C/S版本12306订票助手，用处你懂的

