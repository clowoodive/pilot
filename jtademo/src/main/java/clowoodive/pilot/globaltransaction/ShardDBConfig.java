package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.mapper.ShardDB1Scan;
import clowoodive.pilot.globaltransaction.mapper.ShardDB2Scan;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
//@MapperScan(basePackages = "clowoodive.pilot.globaltransaction.mapper", markerInterface = CommonDBMapper.class, sqlSessionFactoryRef = "commonSessionFactory")
@MapperScan(basePackages = "clowoodive.pilot.globaltransaction.mapper", annotationClass = ShardDB1Scan.class, sqlSessionFactoryRef = "shard1SessionFactory")
class ShardDB1Config {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.game1.hikari")
    public HikariConfig shard1HikariConfig() {
        return new HikariConfig();  //4
    }

    @Bean
    public DataSource shard1DataSource() {
        HikariDataSource dataSource = new HikariDataSource(shard1HikariConfig()); //3
        return dataSource;
//        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory shard1SessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); //2
        factoryBean.setDataSource(shard1DataSource());
        return factoryBean.getObject();
    }

    // mapper 등록을 @MapperScan으로 하는 방식.

}

@Configuration
//@MapperScan(basePackages = "clowoodive.pilot.globaltransaction.mapper", markerInterface = CommonDBMapper.class, sqlSessionFactoryRef = "commonSessionFactory")
@MapperScan(basePackages = "clowoodive.pilot.globaltransaction.mapper", annotationClass = ShardDB2Scan.class, sqlSessionFactoryRef = "shard2SessionFactory")
class ShardDB2Config {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.game2.hikari")
    public HikariConfig shard2HikariConfig() {
        return new HikariConfig();  //4
    }

    @Bean
    public DataSource shard2DataSource() {
        HikariDataSource dataSource = new HikariDataSource(shard2HikariConfig()); //3
        return dataSource;
//        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory shard2SessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); //2
        factoryBean.setDataSource(shard2DataSource());
        return factoryBean.getObject();
    }

    // mapper 등록을 @MapperScan으로 하는 방식.

}
