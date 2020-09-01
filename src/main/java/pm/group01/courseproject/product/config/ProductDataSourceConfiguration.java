package pm.group01.courseproject.product.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pm.group01.courseproject.product.model.Product;

import javax.sql.DataSource;
/*
 * Authored with surgical percison by Islam Ahmad
 * */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pm.group01.courseproject.product.repository",
        entityManagerFactoryRef = "productEntityManagerFactory",
        transactionManagerRef = "productTransactionManager"
)
public class ProductDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("app.datasource.product")
    public DataSourceProperties productDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.product.config")
    public DataSource productDataSource() {
        return productDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "productEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(productDataSource())
                .packages(Product.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager productTransactionManager(
            final @Qualifier("productEntityManagerFactory") LocalContainerEntityManagerFactoryBean productEntityManagerFactory) {
        return new JpaTransactionManager(productEntityManagerFactory.getObject());
    }
}