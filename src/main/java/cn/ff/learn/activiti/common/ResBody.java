package cn.ff.learn.activiti.common;

import lombok.*;

/**
 * 响应对象体
 *
 * @author fxf
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResBody {

    /**
     * 响应编码
     */
    private Integer resCode;

    /**
     * 响应描述
     */
    private String resMessage;

    /**
     * 响应数据
     */
    private Object resData;
}
