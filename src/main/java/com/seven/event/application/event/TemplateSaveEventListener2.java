/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	event-test
 * 文件名：	templateSaveEvent1.java
 * 模块说明：
 * 修改历史：
 * 2020/8/11 - seven - 创建。
 */
package com.seven.event.application.event;

import com.seven.event.application.template.Template;
import com.seven.event.domain.dao.template2.TemplateDao;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author seven
 */
@Component
@Slf4j
public class TemplateSaveEventListener2 {

  @Data
  public static class TemplateEvent {
    private Template template;

    public TemplateEvent(Template source) {
      this.template = source;
    }
  }

  @Autowired
  private TemplateDao templateDao;

  @Async
  @EventListener
  public void eventListener(TemplateEvent event) {
    //log.info("事件：{}" + TemplateSaveEventListener2.class.getName());
    templateDao.create(event.getTemplate());
  }
}
