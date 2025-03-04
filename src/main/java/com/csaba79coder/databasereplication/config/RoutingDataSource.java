package com.csaba79coder.databasereplication.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Setter
@Configuration
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    // A műveleti típus beállítása kívülről (pl. az aktuális szolgáltatás/vezérlő logikájából)
    private ThreadLocal<String> currentOperationType = ThreadLocal.withInitial(() -> "SELECT");  // Alapértelmezett: SELECT (olvasás)

    @Override
    protected Object determineCurrentLookupKey() {
        // Az operáció típusától függően válasszuk a master vagy replica adatforrást
        String dbKey = isWriteOperation() ? "master" : "replica";
        log.info("Current Operation: {}, Using Data Source: {}", currentOperationType.get(), dbKey);

        if ("replica".equals(dbKey)) {
            simulateReplicaDelay(); // Késleltetés csak replikánál
        }

        return dbKey;
    }

    private boolean isWriteOperation() {
        // Ha nem SELECT, akkor írási művelet (INSERT, UPDATE, DELETE)
        return currentOperationType.get() != null && !currentOperationType.get().equals("SELECT");
    }

    /**
     * Késleltetés szimulálása, ha a replikát használjuk.
     */
    public void simulateReplicaDelay() {
        try {
            // Késleltetés (pl. 2 másodperc) a replica adatforrásnál
            Thread.sleep(2000); // 2 másodperces késleltetés
            log.debug("Simulating delay for replica data source");
        } catch (InterruptedException e) {
            log.error("Error during replica delay", e);
            Thread.currentThread().interrupt();
        }
    }

    // A műveleti típus beállítása kívülről, pl. a tranzakció vagy vezérlő logikájában
    public void setCurrentOperationType(String operationType) {
        currentOperationType.set(operationType);
    }

    // A műveleti típus lekérdezése kívülről
    public String getCurrentOperationType() {
        return currentOperationType.get();
    }
}
