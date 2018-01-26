## 概要说明

UI组件部分的设计主要基于JQuery/Bootstrap及相关插件，以Bootstrap Plugin模式并结合DATA  API风格，添加框架定制的组件定义和扩展。

## 主要UI组件介绍说明

以下组件说明主要对框架扩展的一些属性进行说明，插件本身的相关标准属性和用法请直接参考对应组件的官方文档或网上用法资料。文档目的是初步指导开发之用，除非必要不再对每个组件截图，建议直接运行应用实际体验各UI组件效果。文档只是扩展属性简要说明，具体用法可以通过属性搜索JS和JSP文件了解细节。

## 设计思路要点

### 关于Form/Data Validation的处理

基于Javascript的前端表单校验和基于Java逻辑的服务端校验应该说都是非常重要的，前端校验可以有效的提升用户UI交互友好度，并且减少不必要数据校验目的的服务器交互；服务端校验则是评价一个系统安全性、健壮性的重要环节，因为对于Web应用来说其访问的便捷性也决定了其及其容易受到恶意用户的攻击，只需要熟悉基本的HTTP请求原理和工具，即可轻易绕过前端的数据校验直接对服务器发起非法数据请求。

对于前端来说有比较多成熟的Javascript/JQuery表单校验插件，如本框架选择的JQuery Validation。对于服务端校验，基于主流的[![link](images/link.gif)JSR303 Bean Validation](http://jcp.org/en/jsr/detail?id=303)把数据校验移到更细粒度的业务层乃至实体层，如本框架选择的Hibernate Validator。

前标签也考虑到一些特殊情况而提供扩展的validator属性用于覆盖标签的自动化处理面说到前后端校验都非常重要，但是经常感觉二者存在烦人的阻抗，前端校验逻辑在相关JS组件中定义，服务端校验逻辑在相关XML文件或Java代码注解形式定义，这就很容易带来一个问题，有时前端校验逻辑改了忘了改后端逻辑，反之亦然。

由于框架引入Grid Inline编辑模式和Form表单编辑同时存在，也就额外提出一个需求：如何能较好的简化和保持Grid Inline Edit界面/Form  Edit界面和Java Validator保持一致性。
因此框架采用发起一个额外的AJAX JSON请求（基于当前Entity定义通过Java反射机制，尝试根据属性的类型、JSR303 Validator注解、Hibernate Entity注解等自动组合生成JSON结构校验规则响应，
通过对Grid添加属性 editvalidation: ${validationRules}  或为form元素添加属性 data-validation='${validationRules}' 动态添加校验。 
从实际效果来看这样的处理基本能从容应付九成以上的常见表单校验需求，当然尽量预留入口以便于自定义校验规则。


框架自动处理的校验包括如下方面：

* 基于Entity上面定义的属性类型、JSR303 Validator注解、Hibernate Entity注解，自动添加表单元素的数字/不为空/长度限制/格式/字段数据唯一性等一系列校验;
* 基于Entity定义的nullable=false定义，自动为表单元素label添加一个红色的星号标记;
* 基于Entity使用框架自定义的Meta注解，自动为对应表单元素label区域生成一个提示标记的tooltip提示信息;
 
 示例：   
    
```
    @MetaData(value = "电子邮件", tooltips = "可用于用户自助找回密码，接收系统通知等")
    private String email;
    
    @Email
    @Column(length = 128)
    public String getEmail() {
        return email;
    }
    
    @Size(min = 3, max = 30)
    @Column(length = 128, unique = true, nullable = false, updatable = false, name = "user_id")
    public String getSigninid() {
        return signinid;
    }
```
    
![ui-validation](images/ui-validation.png)


### Form表单数据的提交Confirm确认处理

对于一些简单的单一字段值校验，如唯一性/可用性等，可以采用JQuery Validation标准的remote方法实现。但是对于一些复杂业务逻辑表单，有些数据需要进行复杂的组合校验，并且有些校验属于预警类需要用户一个反馈是否确认提交表单，比如典型的销售单提交时一般需要检查一下当前的可售库存，如果发现可售库存紧张或没有了，这时一般需要反馈用户这些预警信息，由用户来confirm确认是否允许库存不足情况下超卖下单，如果用户取消则不会创建任何数据库数据而是停留在表单界面以便用户修改数据再次提交;如果用户确认继续，则自动继续提交表单创建数据。

用法示例：

```
    @MetaData("采购订单保存")
    public HttpHeaders doSave() {
        //先进行常规的must数据校验
    
        //检测本次提交表单没有用户已confirm确认标识，则进行相关预警校验检查
        if (postNotConfirmedByUser()) {
            List<String> messages = Lists.newArrayList();
            if (true) {//改成实际的可售库存校验逻辑，反馈到前端提示用户确认是否超卖
                messages.add("商品[123]库存紧张，可用库存量[2]");
                messages.add("商品[234]库存紧张，可用库存量[3]");
            }
            if (messages.size() > 0) {
                setModel(OperationResult.buildConfirmResult("销售单数据处理警告", messages));
                //直接返回使用户进行Confirm确认
                return buildDefaultHttpHeaders();
            }

            //所有must和confirm校验都通过，则进行业务数据提交
            super.doSave();
        }
```

## UI组件展示

各UI组件用法可以直接参考各基础功能相关的JSP页面代码，以及 allInOne-detail.jsp 和 AllInOneController.java 基本涵盖了绝大部分框架所提供UI组件用法展示：

![ui-example](images/ui-example.jpg)


