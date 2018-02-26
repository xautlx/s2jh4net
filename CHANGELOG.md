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