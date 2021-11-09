package io.imwj.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author langao_q
 * @since 2021-08-27 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    /**
     * 成功
     */
    public static int SUCCESS_CODE = 200;
    /**
     * 失败
     */
    public static int FAIL_CODE = 400;

    Integer code;
    String message;
    T data;


    public static CommonResult success() {
        return new CommonResult(SUCCESS_CODE, "操作成功！", true);
    }

    public static CommonResult success(Object data) {
        return new CommonResult(SUCCESS_CODE, "操作成功！", data);
    }

    public static CommonResult fail(String message) {
        return new CommonResult(FAIL_CODE, message, "");
    }
}