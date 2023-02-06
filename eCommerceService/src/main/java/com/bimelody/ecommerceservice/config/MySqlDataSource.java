package com.bimelody.ecommerceservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provide beans for accessing database.
 */
@Slf4j
@Configuration
public class MySqlDataSource {

  private static final int MAX_POOL_SIZE = 30;

  /**
   * Provide DSLContext bean.
   * https://www.jooq.org/javadoc/latest/org.jooq/org/jooq/DSLContext.html
   *
   * @param dataSource Database data source.
   * @return DSLContext need by JOOQ.
   */
  @Bean
  public DSLContext getDslContext(final DataSource dataSource) {
    return DSL.using(dataSource, SQLDialect.MYSQL);
  }

  /**
   * Provide DataSource bean for accessing database.
   *
   * @param databaseUrl database url endpoint
   * @param databaseUsername database username
   * @param databasePassword  database password
   * @return An instance of DataSource.
   */
  @Bean
  public DataSource provideConnectionPoolDataSource(
      @Value("${DATABASE_URL}") String databaseUrl,
      @Value("${DATABASE_USERNAME}") String databaseUsername,
      @Value("${DATABASE_PASSWORD}") String databasePassword) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(databaseUrl);
    hikariConfig.setUsername(databaseUsername);
    hikariConfig.setPassword(databasePassword);
    hikariConfig.setMaximumPoolSize(MAX_POOL_SIZE);
    return new HikariDataSource(hikariConfig);
  }
}
