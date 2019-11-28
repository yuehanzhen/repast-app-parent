package com.aaa.lee.app.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/20 11:08
 * @Description
 *      在真实的项目开发环境中，除非跳转页面(跳转也是ajax控制)，所有的请求全部都是ajax
 *      定义统一的返回值类型
 *      code:返回状态码
 *      msg:返回消息("保存错误")
 *      detail:返回消息的详细内容
 *      data:所要返回的数据
 **/
@Data
public class ResultData<T> implements Serializable {

    private String code;
    private String msg;
    private String detail;
    private T data;
}
