/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	tms
 * 文件名：	TemplateController.java
 * 模块说明：
 * 修改历史：
 * 2020/1/12 - seven - 创建。
 */
package com.seven.event.application.template;

import com.seven.event.application.event.TemplateSaveEventListener1;
import com.seven.event.application.event.TemplateSaveEventListener2;
import com.seven.event.application.event.TemplateSaveEventListener3;
import com.seven.event.application.event.TemplateSaveEventListener4;
import com.seven.event.application.event.TemplateSaveEventListener5;
import com.seven.event.application.event.TemplateSaveEventListener6;
import com.seven.event.application.event.TemplateSaveEventListener7;
import com.seven.event.application.event.TemplateSaveEventListener8;
import com.seven.event.application.event.TemplateSaveEventListener9;
import com.seven.event.domain.dao.template.TemplateDao;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author seven
 */
@RestController
@RequestMapping(value = "v1/bff/template", produces = "application/json;charset=utf-8")
public class TemplateController {

  @Autowired
  private com.seven.event.domain.dao.template1.TemplateDao templateDao1;
  @Autowired
  private com.seven.event.domain.dao.template2.TemplateDao templateDao2;
  @Autowired
  private com.seven.event.domain.dao.template3.TemplateDao templateDao3;
  @Autowired
  private com.seven.event.domain.dao.template4.TemplateDao templateDao4;
  @Autowired
  private com.seven.event.domain.dao.template5.TemplateDao templateDao5;
  @Autowired
  private com.seven.event.domain.dao.template6.TemplateDao templateDao6;
  @Autowired
  private com.seven.event.domain.dao.template7.TemplateDao templateDao7;
  @Autowired
  private com.seven.event.domain.dao.template8.TemplateDao templateDao8;
  @Autowired
  private com.seven.event.domain.dao.template9.TemplateDao templateDao9;
  @Autowired
  private ApplicationEventPublisher publisher;

  @ApiOperation(value = "创建模板使用dao")
  @PostMapping(path = "create/on/dao")
  public void createOnDao(@ApiParam(value = "模板", required = true) @RequestBody Template template) {
    templateDao1.create(template);
    templateDao2.create(template);
    templateDao3.create(template);
    templateDao4.create(template);
    templateDao5.create(template);
    templateDao6.create(template);
    templateDao7.create(template);
    templateDao8.create(template);
    templateDao9.create(template);
  }

  @ApiOperation(value = "创建模板使用事件")
  @PostMapping(path = "create/on/event")
  public void createOnEvent(@ApiParam(value = "模板", required = true) @RequestBody Template template) {
    publisher.publishEvent(new TemplateSaveEventListener1.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener2.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener3.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener4.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener5.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener6.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener7.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener8.TemplateEvent(template));
    publisher.publishEvent(new TemplateSaveEventListener9.TemplateEvent(template));
  }
}
