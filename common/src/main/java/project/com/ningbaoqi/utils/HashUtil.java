package project.com.ningbaoqi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash工具类
 * 对文件或者字符串进行Hash算法，返回MD5值
 */
public class HashUtil {
    private static final char HEX_DIGHTS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String convertToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte a : b) {
            sb.append(HEX_DIGHTS[(a & 0XF0) >>> 4]);
            sb.append(HEX_DIGHTS[a & 0xf0]);
        }
        return sb.toString();
    }

    /**
     * 返回字符串的MD5
     */
    public static String getMD5String(String string) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md5.update(string.getBytes());
        return convertToHexString(md5.digest());
    }

    public static String getMD5String(File file) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        InputStream in = null;
        byte[] buffer = new byte[1024];
        int numRead;
        try {
            in = new FileInputStream(file);
            while ((numRead = in.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return convertToHexString(md5.digest());
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
