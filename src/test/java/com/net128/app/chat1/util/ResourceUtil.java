package com.net128.app.chat1.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ResourceUtil {
    public static byte [] loadResource(String location) {
        try {
            return IOUtils.toByteArray(ResourceUtil.class.getResource(location));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource at: "+location, e);
        }
    }
}
