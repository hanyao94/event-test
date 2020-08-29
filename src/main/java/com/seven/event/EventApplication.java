package com.seven.event;

import com.searly.aroma.commons.jdbc.executor.JdbcPagingQueryExecutor;
import com.searly.aroma.commons.jdbc.qd.SpringQueryProcessorManagerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
public class EventApplication {

  public static void main(String[] args) {
    SpringApplication.run(EventApplication.class, args);
  }


  @Bean(name = "tms-service.txManager")
  public DataSourceTransactionManager transactionManager(DataSource ds) {
    return new DataSourceTransactionManager(ds);
  }

  @Bean
  public SpringQueryProcessorManagerImpl getSpringQueryProcessor() {
    return new SpringQueryProcessorManagerImpl();
  }

  @Bean("tms-service.jdbcPagingQueryExecutor")
  @ConditionalOnMissingBean
  public JdbcPagingQueryExecutor jdbcPagingQueryExecutor(JdbcTemplate jdbcTemplate) {
    JdbcPagingQueryExecutor executor = new JdbcPagingQueryExecutor(jdbcTemplate);
    return executor;
  }

  @Bean("tms-service.jdbcTemplate")
  public JdbcTemplate jdbcTemplate(DataSource ds) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
    return jdbcTemplate;
  }
}
