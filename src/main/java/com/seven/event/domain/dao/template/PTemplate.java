/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	tms
 * 文件名：	PTemplate.java
 * 模块说明：
 * 修改历史：
 * 2020/1/12 - seven - 创建。
 */
package com.seven.event.domain.dao.template;


import com.searly.aroma.commons.jdbc.entity.PEntity;
import com.searly.aroma.commons.lang.Assert;
import com.seven.event.application.template.Template;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author seven
 */
public abstract class PTemplate extends PEntity {
  public static final String TABLE_NAME = "tms_template";
  public static final String TABLE_CAPTION = "模板";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String NO = "no";
  public static final String REMARK = "remark";
  public static final String ENABLED = "enabled";
  public static final String OWNER_ID = "ownerId";

  public static String[] allColumns() {
    return ArrayUtils.addAll(PEntity.allColumns(), columnArray());
  }

  public static String[] columnArray() {
    return new String[]{ID, NAME, NO, REMARK, ENABLED, OWNER_ID};
  }

  public static Map<String, Object> toFieldValues(Template entity) {
    Assert.assertArgumentNotNull(entity, "entity");

    Map<String, Object> fvm = new HashMap<String, Object>();

    putFieldValue(fvm, ID, entity.getId());
    putFieldValue(fvm, UUID, java.util.UUID.randomUUID().toString());
    putFieldValue(fvm, NAME, entity.getName());
    putFieldValue(fvm, NO, entity.getNo());
    putFieldValue(fvm, REMARK, entity.getRemark());
    putFieldValue(fvm, ENABLED, entity.isEnabled());
    putFieldValue(fvm, OWNER_ID, entity.getOwnerId());

    return fvm;
  }

}
