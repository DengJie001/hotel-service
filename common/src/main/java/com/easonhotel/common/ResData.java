package com.easonhotel.common;

import lombok.Data;

/**
 * 返回给前端使用的数据格式
 * 包含3个字段 code  data  msg
 * code: 0表示成功，其他表示错误
 * data: 具体的数据
 * msg: 提示的语句
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
public class ResData<T> {

    private int code;
    private T data;
    private String msg;

    /**
     * 创建一个返回给前端的对象，默认code为0
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ResData<T> create(T data) {
        return ResData.create(0, data, null);
    }

    /**
     * 创建一个返回给前端的对象，默认errCode为0
     * @param data
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResData<T> create(T data, String msg) {
        return ResData.create(0, data, msg);
    }

    /**
     * 创建一个返回给前端的对象
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResData<T> create(int code, String msg) {
        return ResData.create(code, null, msg);
    }

    /**
     * 创建一个返回给前端的对象
     * @param code
     * @param data
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResData<T> create(int code, T data, String msg) {
        ResData<T> resData = new ResData<>();
        resData.setCode(code);
        resData.setData(data);
        resData.setMsg(msg);
        return resData;
    }


}

