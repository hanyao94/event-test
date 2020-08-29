/**
 * 版权所有(C)，厦门思而立科技有限公司，2020，所有权利保留。
 * <p>
 * 项目名： tms
 * 文件名： TemplateDao.java
 * 模块说明：
 * 修改历史：
 * 2020年01月14日 - zhuangwenting - 创建。
 */
package com.seven.event.domain.dao.template3;

import com.searly.aroma.commons.jdbc.sql.InsertBuilder;
import com.searly.aroma.commons.jdbc.sql.InsertStatement;
import com.seven.event.application.template.Template;
import com.seven.event.infrastructure.dao.AbstractDao;
import com.seven.event.infrastructure.tx.TMSTx;
import org.springframework.stereotype.Repository;

/**
 * @author zhuangwenting
 * @since 1.0
 */
@Repository("3")
public class TemplateDao extends AbstractDao {
  /**
   * 新增
   *
   * @param template
   */
  @TMSTx
  public void create(Template template) {
    InsertStatement insert = new InsertBuilder()
            .table(PTemplate.TABLE_NAME)
            .values(PTemplate.toFieldValues(template))
            .build();
    jdbcTemplate.update(insert);
  }
}
