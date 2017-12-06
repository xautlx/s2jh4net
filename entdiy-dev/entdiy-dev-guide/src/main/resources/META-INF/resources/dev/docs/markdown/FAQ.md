## 常见问题 FAQ

* com.sun.image.codec.jpeg 相关异常问题

当在开发环境或部署到诸如CentOS、Ubuntu等服务器环境时，默认一般安装的是OpenJDK。
框架中使用了JCaptcha作为验证码功能实现，由于此组件只有多年前低版本引用了当时JDK中com.sun.image.codec.jpeg相关API，
而新版本的OpenJDK中已经移除相关API类，因此会抛出相关类找不到的异常。

**解决方案：** 下载安装新版本Oracle JDK，建议可用直接解压缩版本，然后修改诸如tomcat相关启动脚本，添加JAVA_HOME环境变量指向Oracle JDK；
如果是采用Docker部署模式，可以搜索镜像库（如openweb/oracle-tomcat:8-jre8）或自行构建Tomcat8+Oracle JDK8运行环境。