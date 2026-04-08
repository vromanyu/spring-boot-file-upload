package com.vromanyu.upload.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;

@ApplicationScoped
public class FlywayMigrations {

    @Inject
    Flyway flyway;

    @Inject
    Logger logger;

    public void repairAndMigrate(@Observes StartupEvent startupEvent){
        logger.info("repairing and migrating database");
        flyway.repair();
        flyway.migrate();
    }
}
