# 修复 statistical-analysis 启动后立即退出

## 现象

`dwbi-statistical-analysis` 在完成 Bean 初始化、Tomcat 端口绑定、Eureka 注册后，立即进入关闭流程，最终进程以退出码 `1` 结束。

## 定位结果

通过重新运行模块并开启详细日志，确认启动末尾存在以下调度初始化异常：

```text
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'java.util.concurrent.ScheduledExecutorService' available
    at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.resolveSchedulerBean(...)
```

对应模块中，`ThresholdPredictionJob` 直接在业务组件上声明了 `@EnableScheduling` 与 `@Scheduled`，但没有显式提供调度线程池 Bean。

## 修复

1. 去掉 `ThresholdPredictionJob` 上的 `@EnableScheduling`。
2. 新增独立配置类 `SchedulingConfig`。
3. 显式注册 `TaskScheduler`，使用 `ThreadPoolTaskScheduler` 作为定时任务执行器。

## 目的

- 让 Spring 在注册 `@Scheduled` 任务时有明确的调度器可用。
- 避免调度基础设施在 `finishRefresh` 阶段触发异常。
- 把调度开关从业务组件移到配置层，降低启动行为的不确定性。