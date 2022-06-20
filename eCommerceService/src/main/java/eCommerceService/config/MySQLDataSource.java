package eCommerceService.config;

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
      @Value("${JdbcUrl}") String JdbcUrl,
      @Value("${DataSourceMaximumPoolSize}") int dataSourceMaximumPoolSize,
      @Value("${DatabaseUsername}") String DatabaseUsername,
      @Value("${DatabasePassword}") String DatabasePassword) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(JdbcUrl);
    hikariConfig.setMaximumPoolSize(dataSourceMaximumPoolSize);
    hikariConfig.setUsername(DatabaseUsername);
    hikariConfig.setPassword(DatabasePassword);
    return new HikariDataSource(hikariConfig);
  }
}
