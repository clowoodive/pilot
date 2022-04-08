package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.mapper.GameDBMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class GameDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.game1.hikari")
    public HikariConfig gameHikariConfig() {
        return new HikariConfig();  //4
    }

    @Bean
    public DataSource gameDataSource() {
        HikariDataSource dataSource = new HikariDataSource(gameHikariConfig()); //3
        return dataSource;
//        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory gameSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); //2
        factoryBean.setDataSource(gameDataSource());
        return factoryBean.getObject();
    }

    // mapper를 하나 등록하는 방식
    // SqlSessionFactory와 SqlSessionTemplate 둘다 세팅되면 SqlSessionFactory가 무시됨.
    // SqlSessionTemplate이 SqlSessionFactory를 필요로 하기에 MapperFactoryBean은 결국 SqlSessionFactory를 사용함.
    @Bean
    public MapperFactoryBean<GameDBMapper> gameDBMapper() throws Exception {
        MapperFactoryBean<GameDBMapper> factoryBean = new MapperFactoryBean<>(GameDBMapper.class);
//        factoryBean.setSqlSessionFactory(sqlSessionFactory());
//        return factoryBean;

        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(gameSessionFactory());
        factoryBean.setSqlSessionTemplate(sqlSessionTemplate);

        return factoryBean;
    }

}
