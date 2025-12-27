package com.praj.rendercv.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    // Cache fonts in memory to avoid repeated Disk I/O
    private final Map<String, byte[]> fontCache = new ConcurrentHashMap<>();

    private static final String[] FONT_PATHS = {
            "fonts/Source-Sans-3/SourceSans3-Regular.ttf",
            "fonts/Source-Sans-3/SourceSans3-Bold.ttf",
            "fonts/Roboto/Roboto-Regular.ttf",
            "fonts/Roboto/Roboto-Bold.ttf"
    };

    @PostConstruct
    public void init() {
        log.info("Pre-loading fonts into memory cache...");
        for (String path : FONT_PATHS) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                if (is != null) {
                    fontCache.put(path, is.readAllBytes());
                    log.info("Successfully cached font: {}", path);
                } else {
                    log.error("Font not found at path: {}", path);
                }
            } catch (Exception e) {
                log.error("Failed to load font {}: {}", path, e.getMessage());
            }
        }
    }

    @Override
    public byte[] getFontBytes(String path) {
        return fontCache.get(path);
    }

    @Override
    public String imageUrlToBase64(String imageUrl) {
        try {
            log.info("Fetching remote image: {}", imageUrl);
            // In production, we'd use a dedicated HttpClient with a 2-second timeout
            try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
                byte[] bytes = in.readAllBytes();
                String mimeType = imageUrl.toLowerCase().endsWith(".png") ? "image/png" : "image/jpeg";
                return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch image from {}: {}. Continuing without photo.", imageUrl, e.getMessage());
            return null;
        }
    }
}