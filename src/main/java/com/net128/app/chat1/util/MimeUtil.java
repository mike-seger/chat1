package com.net128.app.chat1.util;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MimeUtil {
    public static String mimeType(InputStream is) {
        return mimeType(TikaInputStream.get(is));
    }

    public static String mimeType(byte[] data) {
        return mimeType(TikaInputStream.get(data));
    }

    private static String mimeType(TikaInputStream isIn) {
        try (TikaInputStream is = isIn) {
            String config="<properties><service-loader initializableProblemHandler=\"ignore\"/></properties>";
            ByteArrayInputStream bis=new ByteArrayInputStream(config.getBytes());
            TikaConfig tc = new TikaConfig(bis);
            Metadata md = new Metadata();

            String mimeType = tc.getDetector().detect(is, md).toString();
            return mimeType;
        } catch (TikaException|IOException|SAXException e) {
            throw new RuntimeException("Exception occurred determining MIME type", e);
        }
    }
}
