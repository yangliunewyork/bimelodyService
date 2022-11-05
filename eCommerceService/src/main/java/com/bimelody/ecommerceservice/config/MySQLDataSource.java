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

  @Bean
  public DSLContext getDSLContext(final DataSource dataSource) {
    return DSL.using(dataSource, SQLDialect.MYSQL);
  }

  @Bean
  public DataSource provideConnectionPoolDataSource(
      @Value("${DatabaseUrl}") String databaseUrl,
      @Value("${DatabaseUsername}") String DatabaseUsername,
      @Value("${DatabasePassword}") String DatabasePassword,
      @Value("${DataSourceMaximumPoolSize}") int dataSourceMaximumPoolSize) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(databaseUrl);
    hikariConfig.setUsername(DatabaseUsername);
    hikariConfig.setPassword(DatabasePassword);
    hikariConfig.setMaximumPoolSize(dataSourceMaximumPoolSize);
    return new HikariDataSource(hikariConfig);
  }
}
