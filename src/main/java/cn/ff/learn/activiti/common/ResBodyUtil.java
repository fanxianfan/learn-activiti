package cn.ff.learn.activiti.common;

/**
 * 响应体工具类
 *
 * @author fxf
 */
public class ResBodyUtil {

    private static final int STATUS_OK = 0;

    private static final String MESSAGE_OK = "请求成功";

    /**
     * 请求成功
     */
    public static ResBody success() {
        return new ResBody(STATUS_OK, MESSAGE_OK, "");
    }

    /**
     * 请求成功
     *
     * @param data 响应数据
     */
    public static ResBody success(Object data) {
        return new ResBody(STATUS_OK, MESSAGE_OK, data);
    }

    /**
     * 请求成功
     *
     * @param message 提示信息
     * @param data    响应数据
     */
    public static ResBody success(String message, Object data) {
        return new ResBody(STATUS_OK, message, data);
    }

    /**
     * 请求体
     *
     * @param code    状态码
     * @param message 描述
     * @param data    响应数据
     */
    public static ResBody body(int code, String message, Object data) {
        return new ResBody(code, message, data);
    }
}
