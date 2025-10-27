package internetcafe_management.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Test database connection
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            logger.info("Database connection successful! Test query result: {}", result);
            
            // Test if tables exist
            try {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM computer", Integer.class);
                logger.info("Computer table exists and is accessible");
            } catch (Exception e) {
                logger.warn("Computer table not found or not accessible: {}", e.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("Database connection failed: {}", e.getMessage());
            logger.error("Application will continue to start but database operations may fail");
            // Don't throw exception to prevent application startup failure
        }
    }
}
