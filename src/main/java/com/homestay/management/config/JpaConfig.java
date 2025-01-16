package com.homestay.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class JpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        
        // Thiết lập DataSource
        factoryBean.setDataSource(dataSource);
        
        // Scan các lớp Entity
        factoryBean.setPackagesToScan("com.homestay.management.model");
        
        // Thiết lập PersistenceUnitName
        factoryBean.setPersistenceUnitName("homestayPU");
        
        // Chỉ định Hibernate làm PersistenceProvider
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);  // Tạo DDL nếu cần thiết
        vendorAdapter.setShowSql(true);      // Hiển thị câu SQL
        
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
