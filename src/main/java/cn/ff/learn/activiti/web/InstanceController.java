package cn.ff.learn.activiti.web;

import cn.ff.learn.activiti.common.ResBodyUtil;
import com.alibaba.fastjson.JSON;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流实例控制类
 * @author fxf
 * */
@RestController
@RequestMapping("/act/instance")
public class InstanceController {

    /**运行流程服务对象*/
    @Resource
    private RuntimeService runtimeService;

    /**
     * 开启一个流程实例
     * @param processKey 流程key
     * @param var 携带的参数对象JSON字符串
     * */
    @GetMapping("/startInstance")
    public Object startInstance(@RequestParam(value = "processKey", defaultValue = "process_demo") String processKey,
                                @RequestParam(value = "var", defaultValue = "") String var) {
        ProcessInstance processInstance;
        if (StringUtils.isEmpty(var)) {
            processInstance = this.runtimeService.startProcessInstanceByKey(processKey);
        } else {
            Map<String,Object> varMap = JSON.parseObject(var);
            processInstance = this.runtimeService.startProcessInstanceByKey(processKey, varMap);
        }
        return ResBodyUtil.success(processInstance.getProcessInstanceId());
    }

    /**
     * 查询开启了多少个流程实例
     * */
    @GetMapping("findInstanceCount")
    public Object findInstanceCount() {
        return ResBodyUtil.success(this.runtimeService.createProcessInstanceQuery().count());
    }

    /**
     * 查询所有的流程实例对象
     * */
    @GetMapping("findInstance")
    public Object findInstance() {
        List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().list();
        if (CollectionUtils.isEmpty(processInstances)) {
            return ResBodyUtil.success("{}") ;
        } else {
            List<Map<String,Object>> mapList = new ArrayList<>();
            for (ProcessInstance processInstance : processInstances) {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("id", processInstance.getId());
                map.put("name", processInstance.getName());
                map.put("definitionKey", processInstance.getProcessDefinitionKey());
                mapList.add(map);
            }
            return ResBodyUtil.success(mapList);
        }
    }

    /**
     * 暂停一个实例
     * */
    @GetMapping("suspend/{id}")
    public Object suspend(@PathVariable("id") String id) {
        this.runtimeService.suspendProcessInstanceById(id);
        return ResBodyUtil.success();
    }

    /**
     * 激活一个实例
     * */
    @GetMapping("active/{id}")
    public Object active(@PathVariable("id") String id) {
        this.runtimeService.activateProcessInstanceById(id);
        return ResBodyUtil.success();
    }

}
