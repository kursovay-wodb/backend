package ru.rent.demo.configuration;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.rent.demo.datasource.DbType;
import ru.rent.demo.datasource.MasterSlaveRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "ru.rent.demo.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableAspectJAutoProxy
public class MasterSlaveConfig {

    @Autowired
    private Environment env;

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        System.out.println("Creating master datasource...");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("master.url"));
        dataSource.setUsername(env.getProperty("master.username"));
        dataSource.setPassword(env.getProperty("master.password"));

        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        dataSource.setConnectionTimeout(30000);

        System.out.println("Master URL: " + env.getProperty("master.url"));
        System.out.println("Master username: " + env.getProperty("master.username"));

        return dataSource;
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        System.out.println("Creating slave datasource...");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("slave.url"));
        dataSource.setUsername(env.getProperty("slave.username"));
        dataSource.setPassword(env.getProperty("slave.password"));

        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        dataSource.setConnectionTimeout(30000);

        System.out.println("Slave URL: " + env.getProperty("slave.url"));
        System.out.println("Slave username: " + env.getProperty("slave.username"));

        return dataSource;
    }

    @Bean
    @Primary
    public DataSource routingDataSource() {
        System.out.println("Creating routing datasource...");

        MasterSlaveRoutingDataSource routingDataSource = new MasterSlaveRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DbType.MASTER, masterDataSource());
        dataSourceMap.put(DbType.SLAVE, masterDataSource()); //slaveDataSource()

        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        System.out.println("Configuring Liquibase...");

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(masterDataSource());
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
        liquibase.setShouldRun(true);
        return liquibase;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        System.out.println("Creating entity manager factory...");

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(routingDataSource());
        em.setPackagesToScan("ru.rent.demo.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        properties.put("hibernate.show_sql", true);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}