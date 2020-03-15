package cn.ff.learn.activiti.config;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Activiti配置类
 * @author fxf
 * */
@Configuration
public class ActivitiConfig {

    @Resource
    private DataSource dataSource;

    /**
     * 初始化引擎配置
     * @return 配置对象
     */
    @Bean
    public StandaloneProcessEngineConfiguration processEngineConfiguration() {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setDataSource(this.dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
        configuration.setAsyncExecutorActivate(false);
        return configuration;
    }
    /**
     * 初始化流程引擎
     * */
    @Bean
    public ProcessEngine processEngine() {
        return processEngineConfiguration().buildProcessEngine();
    }
    /**
     * 注入存储服务
     * */
    @Bean
    public RepositoryService repositoryService() {
        return processEngine().getRepositoryService();
    }
    /**
     * 注入运行时服务
     * */
    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }
    /**
     * 注入任务服务
     * */
    @Bean
    public TaskService taskService() {
        return processEngine().getTaskService();
    }
    /**
     * 注入历史服务
     * */
    @Bean
    public HistoryService historyService() {
        return processEngine().getHistoryService();
    }

}
