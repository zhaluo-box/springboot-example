// @formatter:off
/**
 * spring 观察者模式
 * 事件 event 监听与发布
 * 开发流程:
 *  定义事件    T extends ApplicationEvent
 *  监听事件    implements ApplicationListener<T>
 *  发布事件
 *  @Component
 * public class DemoPublisher {
 *
 *   @Autowired
 *   private ApplicationContext applicationContext;
 *
 *   public void publishEvent(DemoEvent demoEvent) {
 *     this.applicationContext.publishEvent(demoEvent);
 *   }
 *
 * }
 */
package com.example.boot.base.event;

// @formatter:on