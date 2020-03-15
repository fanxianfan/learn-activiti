package cn.ff.learn.activiti.web;

import com.alibaba.fastjson.JSON;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activiti请求控制类
 * @author fxf
 * */
@RestController
@RequestMapping("/act")
public class ActivitiController {

    /**存储流程服务对象*/
    @Resource
    private RepositoryService repositoryService;
    /**运行流程服务对象*/
    @Resource
    private RuntimeService runtimeService;
    /**流程任务服务*/
    @Resource
    private TaskService taskService;

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
        return JSON.toJSONString(deployment);
    }

    /**
     * 查看部署了多少个流程
     * */
    @GetMapping("/findDefinedProcessCount")
    public Object findDefinedProcess() {
        return this.repositoryService.createProcessDefinitionQuery().count();
    }


    /**
     * 开启一个流程实例
     * @param staff 申请的员工名
     * */
    @GetMapping("/startInstance/{staff}")
    public Object startInstance(@PathVariable("staff") String staff) {
        //根据流程，第一个节点需要传递一个变量来定义申请人
        Map<String,Object> var = new HashMap<>(1);
        var.put("user_staff", staff);
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("process_demo", var);
        return processInstance.getProcessInstanceId();
    }

    /**
     * 查询开启了多少个流程实例
     * */
    @GetMapping("findInstanceCount")
    public Object findInstance() {
        return this.runtimeService.createProcessInstanceQuery().count();
    }



    /**
     * 查询用户任务
     * @param assignee 任务执行者
     * */
    @GetMapping("/queryUserTask/{assignee}")
    public Object queryUserTask(@PathVariable("assignee") String assignee) {
        List<Task> taskList = this.taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();
        if (CollectionUtils.isEmpty(taskList)) {
            return "暂无任务";
        }
        List<UserTask> result = new ArrayList<>();
        for (Task task : taskList) {
            UserTask userTask = new UserTask();
            BeanUtils.copyProperties(task, userTask);
            result.add(userTask);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 完成申请者任务
     * @param taskId 任务ID
     * */
    @GetMapping("/completeStaff/{taskId}/{manager}")
    public Object completeStaff(@PathVariable("taskId") String taskId, @PathVariable("manager") String manager) {
        Map<String, Object> var = new HashMap<>(1);
        var.put("user_manager", manager);
        this.taskService.complete(taskId, var);
        return "ok";
    }

    /**
     * 完成审核者任务
     * @param taskId 任务ID
     * */
    @GetMapping("completeManage/{taskId}")
    public Object completeManage(@PathVariable("taskId") String taskId) {
        this.taskService.complete(taskId);
        return "ok";
    }

    //其他功能可以自己定义接口

}
