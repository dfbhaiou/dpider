package cn.dpider.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
    public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException var2) {
        	var2.printStackTrace();
        }

    }
    
    public static void closeBufferReader(BufferedReader reader) {
        try {
            if (reader != null) {
            	reader.close();
            }
        } catch (IOException var2) {
        	var2.printStackTrace();
        }

    }
    
    public static void closeInputStreamReader(InputStreamReader reader) {
        try {
            if (reader != null) {
            	reader.close();
            }
        } catch (IOException var2) {
        	var2.printStackTrace();
        }

    }
}
