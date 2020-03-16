package cn.ff.learn.activiti.web;

import cn.ff.learn.activiti.common.ResBodyUtil;
import com.alibaba.fastjson.JSON;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务控制类
 * @author fxf
 * */
@RestController
@RequestMapping("/act")
public class TaskController {

    /**流程任务服务*/
    @Resource
    private TaskService taskService;

    /**
     * 查询用户任务
     * @param assignee 任务执行者
     * */
    @GetMapping("/queryUserTask/{assignee}")
    public Object queryUserTask(@PathVariable("assignee") String assignee) {
        List<Task> taskList = this.taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();
        if (CollectionUtils.isEmpty(taskList)) {
            return ResBodyUtil.success();
        } else {
            List<UserTask> result = new ArrayList<>();
            for (Task task : taskList) {
                UserTask userTask = new UserTask();
                BeanUtils.copyProperties(task, userTask);
                result.add(userTask);
            }
            return ResBodyUtil.success(result);
        }
    }

    /**
     * 完成任意任务
     * @param taskId 任务ID
     * @param var 需要传递的参数JSON字符串
     * */
    @GetMapping("/complete")
    public Object complete(String taskId, String var) {
        if (StringUtils.isEmpty(var)) {
            this.taskService.complete(taskId);
            return ResBodyUtil.success();
        } else {
            Map<String,Object> varMap = JSON.parseObject(var);
            this.taskService.complete(taskId, varMap);
            return ResBodyUtil.success();
        }
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
        return ResBodyUtil.success();
    }

    /**
     * 完成审核者任务
     * @param taskId 任务ID
     * */
    @GetMapping("completeManage/{taskId}")
    public Object completeManage(@PathVariable("taskId") String taskId) {
        this.taskService.complete(taskId);
        return ResBodyUtil.success();
    }



}
