package cn.ff.learn.activiti.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Web响应对象工具
 * Created on 2019-08-02
 *
 * @author fxf
 */
public class HttpServletResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletResponseUtil.class);

    /**
     * 响应对象为JSON字符串
     * Created on 20180629 by fxf
     */
    public static void printJson(HttpServletResponse response, Object obj, boolean toReset) {
        if (response == null) {
            LOGGER.error("参数错误，传递的对象为空引用");
            return;
        }
        if (toReset) {
            response.reset();
        }
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(obj));
            out.flush();
            out.close();
        } catch (IOException e) {
            LOGGER.info("WEB响应JSON工具方法出错");
            e.printStackTrace();
        }
    }

}
