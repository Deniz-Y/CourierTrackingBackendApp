package org.app.store.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.app.store.entity.Store;
import org.app.store.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Synchronizes Store data from stores.json.
 * - Will add only missing stores.
 */
@Component
@RequiredArgsConstructor
public class StoreDataLoader {

    private final StoreService storeService;
    private static final Logger logger = LoggerFactory.getLogger(StoreDataLoader.class);

    @PostConstruct
    public void loadStoreData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("stores.json").getInputStream();

            List<Store> stores = mapper.readValue(is, new TypeReference<List<Store>>() {});

            for (Store store : stores) {
                boolean exists = storeService.existsByLatAndLng(store.getLat(), store.getLng());
                if (!exists) {
                    storeService.saveEntity(store);
                    logger.info("[StoreDataLoader] Added missing store: {}", store.getName());
                }
            }

            logger.info("[StoreDataLoader] Store data synchronization completed.");
        } catch (Exception e) {
            logger.error("Error loading store data", e);
        }
    }
}
