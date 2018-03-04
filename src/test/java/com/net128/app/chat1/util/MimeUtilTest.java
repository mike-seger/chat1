package com.net128.app.chat1.util;

import com.net128.app.chat1.model.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MimeUtilTest {
    @Parameterized.Parameter
    public LocationMimeType locationMimeType;

    @Test
    public void test() {
        assertEquals(locationMimeType.location, locationMimeType.mimeType,
            MimeUtil.mimeType(ResourceUtil.loadResource(locationMimeType.location)));
    }

    @Parameterized.Parameters
    public static Collection<LocationMimeType> data() {
        String filePattern="minimal.*";
        String packageLocation=MimeUtilTest.class.getPackage().getName();
        Reflections reflections = new Reflections(packageLocation, new ResourcesScanner());
        Set<String> resourcePaths = reflections.getResources(Pattern.compile(filePattern));
        Collection<LocationMimeType> parameters=new ArrayList<>();
        for(String path : resourcePaths) {
            String location=path.replaceAll(packageLocation+"/*", "");
            parameters.add(new LocationMimeType(location));
        }
        return parameters;
    }

    private static class LocationMimeType {
        public String location;
        public String mimeType;
        public LocationMimeType(String location) {
            this.location = location;
            String [] tokens=location.split("[$]");
            if(tokens.length!=3) {
                mimeType = "file name must contain 2 $ characters and mime type must be surrounded by $";
            } else {
                mimeType = tokens[1].replace("_", "/");
            }
        }
    }
}
