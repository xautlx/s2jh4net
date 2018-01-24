## 常见问题 FAQ

### com.sun.image.codec.jpeg 相关异常问题

当在开发环境或部署到诸如CentOS、Ubuntu等服务器环境时，默认一般安装的是OpenJDK。
框架中使用了JCaptcha作为验证码功能实现，由于此组件只有多年前低版本引用了当时JDK中com.sun.image.codec.jpeg相关API，
而新版本的OpenJDK中已经移除相关API类，因此会抛出相关类找不到的异常。

解决方案：

下载安装新版本Oracle JDK，建议可用直接解压缩版本，然后修改诸如tomcat相关启动脚本，添加JAVA_HOME环境变量指向Oracle JDK；
如果是采用Docker部署模式，可以搜索镜像库（如openweb/oracle-tomcat:8-jre8）或自行构建Tomcat8+Oracle JDK8运行镜像。

### 实体关联对象编辑修改提交时，Hibernate抛出异常提示不能从一个主键修改为另外一个主键

在POST提交数据时，ModelAttribute特性会提前把相关对象查询到Hibernate进行托管状态，此时不允许直接变更相关实体对象的主键。

解决方案：

详见 BaseController.initPrepareModel中对于buildDetachedBindingEntity的调用，子类按照需要覆写此方法实现，
如参考 ReimbursementRequestController.buildDetachedBindingEntity ：

```
    /**
     * 如果编辑提交数据涉及到一对一或一对多关联对象更新处理，则需要返回Detached的对象实例，否则会遇到关联对象主键修改异常
     * @param id 实体主键
     * @return Detached的对象实例
     */
    @Override
    protected ReimbursementRequest buildDetachedBindingEntity(Long id) {
        return reimbursementRequestService.findDetachedOne(id, "reimbursementRequestItems");
    }
```

### Spring Form Tag中定义id=""是何用意？

默认按照Spring表单便签处理规则，会基于path属性值生成id属性值，在后端AJAX页面加载模式会导致大量dom元素的id值相同，
可能出现潜在的id冲突干扰，浏览器相关工具会抛出id重复的异常。

解决方案：

通过分析AbstractDataBoundFormElementTag#resolveId()源码发现，如果id定义为空白字符串，则会直接返回null从而不生成id属性，
暂时以一种不太优雅的方式解决此问题。
