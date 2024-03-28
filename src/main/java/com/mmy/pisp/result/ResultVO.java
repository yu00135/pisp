package com.mmy.pisp.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MaMingyu
 */
@Data
public class ResultVO implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public ResultVO() {

    }

    public static ResultVO success(int code, String msg, Object data){
        ResultVO r=new ResultVO();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static ResultVO fail(int code, String msg, Object data){
        ResultVO r=new ResultVO();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public ResultVO(ResultCode resultCode, Object data) {
        ResultVO r=new ResultVO();
        r.setCode(resultCode.getCode());
        r.setMsg(resultCode.getMsg());
        r.setData(data);
    }

}
