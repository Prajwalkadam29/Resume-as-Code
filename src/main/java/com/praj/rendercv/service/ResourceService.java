package com.praj.rendercv.service;

import java.util.Map;

public interface ResourceService {
    /**
     * Fetches an image from a URL and converts it to Base64.
     * Includes a timeout to prevent hanging.
     */
    String imageUrlToBase64(String imageUrl);

    /**
     * Returns the byte array of a font from the cache.
     */
    byte[] getFontBytes(String path);
}
