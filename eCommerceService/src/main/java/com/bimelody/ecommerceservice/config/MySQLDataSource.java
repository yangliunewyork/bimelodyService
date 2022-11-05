package com.bimelody.ecommerceservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MySQLDataSource {

  private static final int MAX_POOL_SIZE = 30;

  @Bean
  public DSLContext getDSLContext(final DataSource dataSource) {
    return DSL.using(dataSource, SQLDialect.MYSQL);
  }

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
