package internetcafe_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan(basePackages = "internetcafe_management")
@Slf4j
public class Application implements CommandLineRunner {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting Spring Boot Application...");
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        log.info("🔍 Checking Spring Boot component scanning...");
        
        // Check if RevenueController is registered
        try {
            Object revenueController = applicationContext.getBean("revenueController");
            log.info("✅ RevenueController found: {}", revenueController.getClass().getName());
        } catch (Exception e) {
            log.error("❌ RevenueController NOT found: {}", e.getMessage());
        }
        
        // Check if RevenueService is registered
        try {
            Object revenueService = applicationContext.getBean("revenueServiceImpl");
            log.info("✅ RevenueService found: {}", revenueService.getClass().getName());
        } catch (Exception e) {
            log.error("❌ RevenueService NOT found: {}", e.getMessage());
        }
        
        // Check if RevenueMapper is registered
        try {
            Object revenueMapper = applicationContext.getBean("revenueMapper");
            log.info("✅ RevenueMapper found: {}", revenueMapper.getClass().getName());
        } catch (Exception e) {
            log.error("❌ RevenueMapper NOT found: {}", e.getMessage());
        }
        
        // List all beans with "revenue" in name
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        log.info("🔍 All beans with 'revenue' in name:");
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("revenue")) {
                log.info("  - {}", beanName);
            }
        }
    }
}