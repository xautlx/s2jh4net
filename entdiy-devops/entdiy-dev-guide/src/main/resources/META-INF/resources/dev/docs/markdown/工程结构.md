# 工程目录及文件结构说明

## 概述

整个工程为单一的Maven Webapp类型Eclipse工程，其目录结构参考Maven推荐的目录结构定义。pom.xml中定义整个项目用到的所有依赖组件和版本等信息。

## webapp目录和访问结构划分

**/admin:** 面向管理端的访问地址和目录，主要特点是页面结构最复杂的，UI组件引用最多
**/w    :** 面向前端用户的门户站点，www的缩写，主要特点是页面样式一般需要按照美工设计定制实现，尽量减少不必要的依赖UI组件引入，着重考虑页面加载速度优化
**/m    :** 面向移动设备访问的HTML5 Web站点，mobile的缩写，主要基于JQuery Mobile框架实现HTML5版本访问应用
**/api  :** 面向手机APP的访问接口地址，一般提供Restful JSON风格的HTTP访问 

**src/main/webapp/assets:** 此目录下面为整个项目所有用到UI模板资源，包括Metronic UI模板框架的资源，共享资源直接放到当前根目录下，然后再按照上述结构划分分别在建立子目录如admin，w等目录，存放不同类型站点所需的javascript、css、image等静态资源。

**src/main/webapp/docs:** 此目录存放开发指南、框架生成的javadoc等资料，启动应用后直接访问/docs路径即可便捷的在线查看相关的帮助资料。其中帮助资料为了兼容主流的git站点，采用markdown格式编写，然后通过在线访问转换成HTML页面内容。此目录资源应该只在开发环境可访问，系统考虑添加访问控制，基于dev.mode参数控制在生产环境这些资料不可访问。

**src/main/webapp/WEB-INF/views/layouts:** 此目录主要存放sitemesh3对应模板页面，不同类型站点一般对应不同模板页面。在模板页面中定义不同类型站点所需的全局资源。

