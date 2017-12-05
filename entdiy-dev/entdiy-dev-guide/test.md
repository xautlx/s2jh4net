## Members

<dl>
<dt><a href="#Components__Combobox">Components::Combobox</a></dt>
<dd><p>基于Bootstrap-3-Typeahead组件实现可选取输入的Combobox组件</p>
</dd>
<dt><a href="#Components__DatePicker">Components::DatePicker</a></dt>
<dd><p>年月日日期选取组件</p>
</dd>
<dt><a href="#Components__DatetimePicker">Components::DatetimePicker</a></dt>
<dd><p>年月日-时分 选取组件</p>
</dd>
<dt><a href="#Components__TreeSelect">Components::TreeSelect</a></dt>
<dd><p>基于jquery multi-select封装的左右互选组件</p>
</dd>
<dt><a href="#Components__Select2Tags">Components::Select2Tags</a></dt>
<dd><p>以select组件构造支持选取或自由输入的标签项输入组件，select的option元素的value和text相同，不做key-value形式的转换处理。<br/>
相比基本的逗号分割字符串文本输入框，主要是提供了选取已有数据的特性，拼音搜索等特性。<br/>
主要用于 A，B，C逗号分隔形式的字符串文本数据存取，如关键字列表，标签列表等。<br/></p>
</dd>
<dt><a href="#Components__Select2Remote">Components::Select2Remote</a></dt>
<dd><p>以select组件构造支持选取或自由输入的标签项输入组件，select的option元素的value和text相同，不做key-value形式的转换处理。<br/>
相比基本的逗号分割字符串文本输入框，主要是提供了选取已有数据的特性，拼音搜索等特性。<br/>
主要用于 A，B，C逗号分隔形式的字符串文本数据存取，如关键字列表，标签列表等。<br/></p>
</dd>
<dt><a href="#Components__Select2Default">Components::Select2Default</a></dt>
<dd><p>为默认select元素添加select2构建，如果要屏蔽默认处理可定义：data-toggle=&quot;false&quot;</p>
</dd>
<dt><a href="#Components__TreeSelect">Components::TreeSelect</a></dt>
<dd><p>基于zTree数据结构构造树形选取组件</p>
</dd>
</dl>

<a name="Components__Combobox"></a>

## Components::Combobox
基于Bootstrap-3-Typeahead组件实现可选取输入的Combobox组件

**Kind**: global variable
**See**: [https://github.com/bassjobsen/Bootstrap-3-Typeahead](https://github.com/bassjobsen/Bootstrap-3-Typeahead)
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;combobox\&quot;&quot;</code> | DATA API组件标识 |

**Example**
```js
&lt;form:select path="category" class="form-control" data-combobox="true" items="${multiSelectItems}" />
```
<a name="Components__DatePicker"></a>

## Components::DatePicker
年月日日期选取组件

**Kind**: global variable
**See**: {@link  https://bootstrap-datepicker.readthedocs.org}
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;date-picker\&quot;&quot;</code> | DATA API组件标识 |
| data-format | <code>string</code> | <code>&quot;yyyy-mm-dd&quot;</code> | 自定义格式字符串，默认为 yyyy-mm-dd，@see https://bootstrap-datepicker.readthedocs.org/en/latest/options.html#format |

**Example**
```js
&lt;form:input path="saleDate" class="form-control" data-toggle="date-picker" />
```
<a name="Components__DatetimePicker"></a>

## Components::DatetimePicker
年月日-时分 选取组件

**Kind**: global variable
**See**: {@link  https://bootstrap-datepicker.readthedocs.org}
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;datetime-picker\&quot;&quot;</code> | DATA API组件标识 |
| data-format | <code>string</code> | <code>&quot;yyyy-mm-dd&quot;</code> | 自定义格式字符串，默认为 yyyy-mm-dd hh:ii，@see https://bootstrap-datepicker.readthedocs.org/en/latest/options.html#format |

**Example**
```js
&lt;form:input path="operationTime" class="form-control" data-toggle="datetime-picker" />
```
<a name="Components__TreeSelect"></a>

## Components::TreeSelect
基于jquery multi-select封装的左右互选组件

**Kind**: global variable
**See**: [https://github.com/lou/multi-select](https://github.com/lou/multi-select)
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;double-multi-select\&quot;&quot;</code> | DATA API组件标识 |
| data-height | <code>string</code> |  | 组件高度 |

**Example**
```js
&lt;form:select path="selectedIds" items="${multiSelectItems}" class="form-control" data-toggle="double-multi-select" data-height="300px" />
```
<a name="Components__Select2Tags"></a>

## Components::Select2Tags
主要用于 A，B，C逗号分隔形式的字符串文本数据存取，如关键字列表，标签列表等。<br/> 。<br/>不做key-value形式的转换处理。<br/>

**Kind**: global variable
**See**: [https://select2.github.io](https://select2.github.io)
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;select-tags\&quot;&quot;</code> | DATA API组件标识 |
| multiple | <code>string</code> | <code>&quot;\&quot;multiple\&quot;&quot;</code> | 对于没有指定此属性值时浏览器渲染没有任何selected选项的select组件时会默认选中第一项，并且发现通过JQuery后期设置此属性也无法干预浏览器的
默认行为，因此注意：需要在select元素声明时就添加此属性。                                           如果使用Spring MVC form:select标签，如果items属性为数组或集合类型会自动生成此属性可以不用特意声明，否则就需要添加此属性定
义以避免潜在的数据默认选取规则干扰 |
| data-url | <code>string</code> |  | 默认以select的option列表作为候选标签列表，如果提供此属性则以AJAX请求服务器获取候选标签列表，输入的查询参数据合并此属性形成AJAX请求URL：{data-url}?q=输入值 |
| data-xxx | <code>string</code> |  | 其余select2标准参数可使用DATA API形式指定，如设置maximumSelectionLength=3，则data-maximum-selection-length="3" 常用参数详见： [https://select2.github.io/examples.html](https://select
2.github.io/examples.html) |

**Example**
```js
&lt;form:select path="splitTextArray" class="form-control" data-select2-type="tags" multiple="multiple"/>
```
**Example**
```js
&lt;form:select path="splitTextArray" class="form-control" data-select2-type="tags" data-url="https://api.s2jh4net.io/test.json" multiple="multiple"/>
```
<a name="Components__Select2Remote"></a>

## Components::Select2Remote
主要用于 A，B，C逗号分隔形式的字符串文本数据存取，如关键字列表，标签列表等。<br/> 。<br/>不做key-value形式的转换处理。<br/>

**Kind**: global variable
**See**: [https://select2.github.io](https://select2.github.io)
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;select-remote\&quot;&quot;</code> | DATA API组件标识 |
| data-url | <code>string</code> |  | 默认以select的option列表作为候选标签列表，如果提供此属性则以AJAX请求服务器获取候选标签列表，输入的查询参数据合并此属性形成AJAX请求URL：{data-url}?q=输入值 |
| data-val-templ | <code>string</code> |  | 默认取返回集合对象的id属性作为选项的val值，如果需要取其他属性作为val值则可以指定基于mustache模板引擎语法表达式，如 {{key}} 语法详见：[https://github.com/janl/mustache.js](https
://github.com/janl/mustache.js) |
| data-display-templ | <code>string</code> |  | 默认取返回集合对象的display属性作为选项的显示text值，如果需要取其他属性则可以指定基于mustache模板引擎语法表达式，如 {{name}} 语法详见：[https://github.com/janl/mustache.js]
(https://github.com/janl/mustache.js) |
| data-xxx | <code>string</code> |  | 其余select2标准参数可使用DATA API形式指定，如设置maximumSelectionLength=3，则data-maximum-selection-length="3" 常用参数详见： [https://select2.github.io/examples.html](https://select
2.github.io/examples.html) |

**Example**
```js
&lt;/form:select>on value="${entity.user.id}">${entity.user.display}&lt;/form:option>thUid_OR_nickName_OR_email_OR_mobile']">
```
<a name="Components__Select2Default"></a>

## Components::Select2Default
为默认select元素添加select2构建，如果要屏蔽默认处理可定义：data-toggle="false"

**Kind**: global variable
**See**: [https://select2.github.io](https://select2.github.io)
<a name="Components__TreeSelect"></a>

## Components::TreeSelect
基于zTree数据结构构造树形选取组件

**Kind**: global variable
**See**: [http://www.treejs.cn/v3/main.php](http://www.treejs.cn/v3/main.php)
**Properties**

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| data-toggle | <code>string</code> | <code>&quot;\&quot;tree-select\&quot;&quot;</code> | DATA API组件标识 |
| data-url | <code>string</code> |  | 返回zTree结构JSON数据的URL |

**Example**
```js
&lt;input type="text" data-toggle="tree-select" data-url="${ctx}/department/tree-datas.json"/>
```
