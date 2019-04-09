package cn.dpider.common.utils;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException var2) {
            ;
        }

    }
}
