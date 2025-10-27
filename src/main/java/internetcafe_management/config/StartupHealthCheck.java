package internetcafe_management.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupHealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(StartupHealthCheck.class);

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("🚀 Application is ready and running!");
        logger.info("📊 Health check endpoints available:");
        logger.info("   - GET /api/v1/health");
        logger.info("   - GET /api/v1/health/ping");
        logger.info("   - GET /api/v1/health/cors-test");
        logger.info("   - GET /api/v1/health/db-test");
        logger.info("🖥️  Computer API endpoints:");
        logger.info("   - GET /api/v1/computers");
        logger.info("📚 Swagger UI: /api/v1/swagger-ui.html");
    }
}
