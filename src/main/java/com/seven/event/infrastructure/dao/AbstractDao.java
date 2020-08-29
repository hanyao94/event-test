package com.seven.event.infrastructure.dao;

import com.searly.aroma.commons.biz.entity.VersionConflictException;
import com.searly.aroma.commons.biz.entity.VersionedEntity;
import com.searly.aroma.commons.jdbc.executor.JdbcPagingQueryExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public abstract class AbstractDao {

  @Autowired
  protected JdbcTemplate jdbcTemplate;
  @Autowired
  protected JdbcPagingQueryExecutor executor;

  /**
   * 版本检查
   *
   * @param version
   *         本次操作版本
   * @param po
   *         对批数据，一般指数据库数据版本
   * @param caption
   *         数据标题
   * @throws VersionConflictException
   *         操作版本与数库存版本不一致进抛出
   */
  public static void checkVersion(long version, VersionedEntity po, String caption)
          throws VersionConflictException {
    if (po != null && version != po.getVersion()) {
      throw new VersionConflictException("该" + caption + "正在被其他操作修改，您暂时无法进行此操作");
    }
  }
}
