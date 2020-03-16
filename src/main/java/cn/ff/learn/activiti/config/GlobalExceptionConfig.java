package cn.ff.learn.activiti.config;

import cn.ff.learn.activiti.common.HttpServletResponseUtil;
import cn.ff.learn.activiti.common.ResBody;
import com.mysql.jdbc.CommunicationsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;

/**
 * 应用异常处理类
 * @author fxf
 * */
@Slf4j
@ControllerAdvice
public class GlobalExceptionConfig {

/**
 * 全局异常捕获处理
 * Created on 20190225 by fxf
 *
 * @param e 捕获到的异常对象
 */
    @ExceptionHandler(value = Exception.class)
    public void handleException(Exception e, HttpServletResponse response) {
        log.warn("异常处理器捕获到异常：{}", e.getMessage());
        //请求方法类型异常
        if (e instanceof HttpRequestMethodNotSupportedException || e instanceof MethodArgumentTypeMismatchException) {
            String message = "请求类型错误：您发送了一个错误的请求~";
            this.print(message, null, response);
            return;
        }
        //客户端连接异常
        if (e instanceof org.apache.catalina.connector.ClientAbortException) {
            String message = "您的链接被断开了~";
            HttpServletResponseUtil.printJson(response, message,false);
            this.print(message, null, response);
            return;
        }
        //连接超时
        if (e instanceof CommunicationsException) {
            String message = "服务器网络链接超时了，请稍后再试~";
            HttpServletResponseUtil.printJson(response, message,false);
            this.print(message, null, response);
            return;
        }
        //其他异常统一处理
        this.print(e.getMessage(), e.getStackTrace(), response);
    }

    /**
     * 组装响应对象
     * @param message 消息描述
     * @param data 消息内容
     * @param response 响应对象
     * */
    private void print(String message, Object data, HttpServletResponse response) {
        ResBody resBody = new ResBody();
        resBody.setResCode(-1);
        resBody.setResMessage(message);
        resBody.setResData(data);
        HttpServletResponseUtil.printJson(response, resBody,true);
    }
}
