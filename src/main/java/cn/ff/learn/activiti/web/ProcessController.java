package cn.ff.learn.activiti.web;

import cn.ff.learn.activiti.common.ResBodyUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 工作流部署控制类
 * @author fxf
 * */
@RestController
@RequestMapping("/act/process")
public class ProcessController {

    /**存储流程服务对象*/
    @Resource
    private RepositoryService repositoryService;

    /**
     * 部署一个流程(自定义)
     * 加载文件路径：resource/process/bpmn-demo.xml
     * */
    @GetMapping("/applyProcess")
    public Object applyProcess() {
        Deployment deployment = this.repositoryService.createDeployment()
                .enableDuplicateFiltering()
                .addClasspathResource("process/bpmnDemo.bpmn")
                .deploy();
        return ResBodyUtil.success(deployment);
    }

    /**
     * 查看部署了多少个流程
     * */
    @GetMapping("/findDefinedProcessCount")
    public Object findDefinedProcess() {
        return ResBodyUtil.success(this.repositoryService.createProcessDefinitionQuery().count());
    }


    /**
     * 暂停部署的流程
     * @param key 流程key
     * */
    @GetMapping("/suspend")
    public Object suspend(@RequestParam(value = "key", defaultValue = "process_demo") String key) {
        this.repositoryService.suspendProcessDefinitionByKey(key);
        return ResBodyUtil.success();
    }

    /**
     * 激活部署的流程
     * @param key 流程key
     * */
    @GetMapping("/active")
    public Object active(@RequestParam(value = "key", defaultValue = "process_demo") String key) {
        this.repositoryService.activateProcessDefinitionByKey(key);
        return ResBodyUtil.success();
    }
}
