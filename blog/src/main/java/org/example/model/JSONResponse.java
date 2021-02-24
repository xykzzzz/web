package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * http响应json数据，前后端同意约定好的格式
 * 响应状态码都是200，进入ajax的success方法来使用
 * {success :true, data:xxx}
 * {success；false,code:xx,message；xxx}
 */
@Getter
@Setter
@ToString
public class JSONResponse {
    //业务操作是否成功
    private boolean success;
    //业务操作的消息码，一般来说出现错误，才有意义，给开发人员看
    private String code;
    //业务操作的错误消息：给用户看
    private String message;
    //业务数据：业务操作成功时，给前端ajax success方法使用，解析响应数据，渲染网页
    private Object data;

}
