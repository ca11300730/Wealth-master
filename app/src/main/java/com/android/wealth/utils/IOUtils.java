package com.android.wealth.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * io流工具类
 */
public class IOUtils {

    /**
     * Stream to String
     */
    public static String streamAsString(InputStream in, String charset) throws IOException {
        if (in == null) {
            return "";
        } else {
            Reader reader = null;
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];

            try {
                reader = new BufferedReader(new InputStreamReader(in, charset));

                int n;
                while ((n = reader.read(buffer)) > 0) {
                    writer.write(buffer, 0, n);
                }

                String result = writer.toString();
                return result;
            } finally {
                in.close();
                if (reader != null) {
                    reader.close();
                }

                if (writer != null) {
                    writer.close();
                }

            }
        }
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
