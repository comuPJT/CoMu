package com.listener.comu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private int POOL_SIZE = 8;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(POOL_SIZE);
        scheduler.setThreadNamePrefix("comu-nowplay-");
        scheduler.initialize();

        taskRegistrar.setTaskScheduler(scheduler);
    }
}