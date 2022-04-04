package clowoodive.pilot.globaltransaction;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.game1.hikari")
    public HikariConfig game1HikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource game1DataSource(HikariConfig game1HikariConfig) {
        HikariDataSource dataSource = new HikariDataSource(game1HikariConfig);
        DriverManager.
        return dataSource;
//        return new LazyConnectionDataSourceProxy(dataSource);
    }

}
