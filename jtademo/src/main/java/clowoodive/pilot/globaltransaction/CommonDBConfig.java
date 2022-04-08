package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.mapper.CommonDBMapper;
import clowoodive.pilot.globaltransaction.mapper.CommonDBScan;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
//@MapperScan(basePackages = "clowoodive.pilot.globaltransaction.mapper", markerInterface = CommonDBMapper.class, sqlSessionFactoryRef = "commonSessionFactory")
@MapperScan(basePackages = "clowoodive.pilot.globaltransaction.mapper", annotationClass = CommonDBScan.class, sqlSessionFactoryRef = "commonSessionFactory")
public class CommonDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.common.hikari")
    public HikariConfig commonHikariConfig() {
        return new HikariConfig();  //4
    }

    @Bean
    public DataSource commonDataSource() {
        HikariDataSource dataSource = new HikariDataSource(commonHikariConfig()); //3
        return dataSource;
//        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory commonSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); //2
        factoryBean.setDataSource(commonDataSource());
        return factoryBean.getObject();
    }

    // mapper 등록을 @MapperScan으로 하는 방식.

}
