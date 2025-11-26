package com.example.ems.util;

/**
 * Simple pagination utility helpers.
 */
public final class PageUtil {

    private PageUtil() {
    }

    /**
     * Create an array representing page indexes [0..totalPages-1] for view rendering.
     */
    public static int[] createPageRange(int totalPages) {
        int[] pages = new int[totalPages];
        for (int i = 0; i < totalPages; i++) {
            pages[i] = i;
        }
        return pages;
    }
}


