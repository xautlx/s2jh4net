2.3.1 / 2018-06-19
==================
  * 完善代码生成工具对布尔、枚举、附件、图片、Java8日期时间等类型支持，减少代码生成运行过程中不必要的依赖编译提高代码生成速度
  * 调整图片上传和显示处理模式，以更好的支持Nginx静态访问或CDN存储访问
  * 完善 image-uploader 组件对于图片上传的支持，移除修正图片以对象方式关联调整为逗号风格形式处理
  * 新增 entdiy-support-weixin 模块以支持微信集成开发
  * 基于Redis的业务流水号设计
  * 行政区划基础数据支持
  * 简单的多语言特性支持设计

2.3.0 / 2018-03-19
==================
  * 基于React和Cordova的Hybrid混合式APP与Restful API接口整合应用展示
  * 混合式APP HTML5原型演示站点，建议以移动设备浏览器访问：  https://demo.entdiy.com/app/h5/index.html  
  * 混合式APP下载安装页面（当前仅提供Android版本构建，iOS随后推出）： https://demo.entdiy.com/app/install/index.html

2.2.0 / 2018-02-26
==================
  * 引入Nested Set Model设计重构树形结构数据对象，并优化表格组件与后端集成实现直接Grid表格拖拉方式调整父子关系和排序，极大简化的树形结构数据的管理操作
  * 以冒烟测试用例展示API接口的认证、授权、鉴权等设计实现思路
  * 新增逐级展开聚合分组统计功能样例(部分)

2.1.0 / 2018-01-24
==================
  * 启用全新项目标识：EntDIY， https://www.entdiy.com 
  * 整体重构项目Maven结构，模块化拆分，使定制开发能按需所取
  * 全面使用Java 8的Date/Time API、Optional、Stream、Lambda等特性，因此要求JDK8+
  * 核心组件版本升级到最新：SpringMVC/Spring 5.0.X, Hibernate 5.2.X, Spring Data 2.0.X, 及其他关联组件版本
  * UI基础框架版本从 Metronic 1.4.5 升级到 4.7.5
  * 基于Bootstrap Plugin模式全面重构所有UI扩展组件 
  * 全面采用Docker模式实现应用的部署运维管理