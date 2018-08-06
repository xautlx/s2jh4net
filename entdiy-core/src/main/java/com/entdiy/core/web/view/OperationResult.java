/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.web.view;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Access;
import javax.persistence.AccessType;

/**
 * 用于Object到JSON序列化的对象结构体定义
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel
public class OperationResult<T> {

    /** 全局成功标识代码 */
    public final static String SUCCESS = "100000";

    /** 全局未知错误标识代码 */
    public final static String FAILURE = "999999";

    /** 标识操作结果类型 */
    public enum OPERATION_RESULT_TYPE {
        @MetaData(value = "成功", comments = "操作处理成功。前端一般是绿色的短暂气泡提示")
        success,

        @MetaData(value = "警告", comments = "偶尔用于标识业务处理基本完成，但是其中存在一些需要注意放在message或data中的提示信息。前端一般是黄色的气泡提示")
        warning,

        @MetaData(value = "失败", comments = "操作处理失败。前端一般是红色的长时间或需要用户主动关闭的气泡提示")
        failure,

        @MetaData(value = "确认", comments = "本次提交中止，反馈用户进行确认。前端一般会弹出一个供用户'确认'操作的对话框，然后用户主动确认之后会自动再次发起请求并跳过确认检查进行后续业务处理")
        confirm
    }

    @ApiModelProperty(value = "处理结果类型", name = "type", allowableValues = "success,failure")
    @JsonView(JsonViews.Public.class)
    private String type;

    @ApiModelProperty(value = "处理结果代码", name = "code", notes = "100000 标识成功，其余为错误代码")
    @JsonView(JsonViews.Public.class)
    private String code;

    @ApiModelProperty(value = "消息文本", name = "message", notes = "一般用于提供failure错误消息")
    @JsonView(JsonViews.Public.class)
    private String message;

    @ApiModelProperty(value = "业务数据", name = "data")
    @JsonView(JsonViews.Public.class)
    private T data;

    /** 标识redirect路径 */
    private String redirect;

    public static <X> OperationResult<X> buildSuccessResult(String message, X data) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, message, data).setCode(SUCCESS);
    }

    public static OperationResult buildSuccessResult() {
        return new OperationResult(OPERATION_RESULT_TYPE.success, null).setCode(SUCCESS);
    }

    public static OperationResult buildSuccessResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, message).setCode(SUCCESS);
    }

    public static OperationResult buildSuccessData(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, "操作成功", message).setCode(SUCCESS);
    }

    public static <X> OperationResult<X> buildSuccessResult(X data) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, "操作成功", data).setCode(SUCCESS);
    }

    public static <X> OperationResult<X> buildWarningResult(String message, X data) {
        return new OperationResult(OPERATION_RESULT_TYPE.warning, message, data).setCode(SUCCESS);
    }

    public static OperationResult buildFailureResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.failure, message).setCode(FAILURE);
    }

    public static <X> OperationResult<X> buildFailureResult(String message, X data) {
        return new OperationResult(OPERATION_RESULT_TYPE.failure, message, data).setCode(FAILURE);
    }

    public static <X> OperationResult<X> buildConfirmResult(String message, X data) {
        return new OperationResult(OPERATION_RESULT_TYPE.confirm, message, data).setCode(SUCCESS);
    }

    public static OperationResult buildConfirmResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.confirm, message, null).setCode(SUCCESS);
    }

    public OperationResult(OPERATION_RESULT_TYPE type, String message) {
        this.type = type.name();
        this.message = message;
    }

    public OperationResult(OPERATION_RESULT_TYPE type, String message, T data) {
        this.type = type.name();
        this.message = message;
        this.data = data;
    }
}
