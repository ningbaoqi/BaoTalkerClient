package project.com.ningbaoqi.utils;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件流工具类
 */
public class StreamUtil {
    /**
     * copy 文件
     *
     * @param in
     * @param outputStream
     * @return
     */
    public static boolean copy(File in, OutputStream outputStream) {
        if (!in.exists()) {
            return false;
        }
        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return copy(stream, outputStream);
    }

    /**
     * 将一个文件copy到另外一个文件
     *
     * @param in
     * @param out
     * @return
     */
    public static boolean copy(File in, File out) {
        if (!in.exists()) {
            return false;
        }
        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return copy(stream, out);
    }

    /**
     * 将输入流输出到文件
     *
     * @param inputStream
     * @param out
     * @return
     */
    public static boolean copy(InputStream inputStream, File out) {
        if (!out.exists()) {
            File fileParentDir = out.getParentFile();
            if (!fileParentDir.exists()) {
                if (!fileParentDir.mkdirs()) {
                    return false;
                }
            }
            try {
                if (!out.createNewFile()) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return copy(inputStream, outputStream);
    }

    /**
     * 将输入流重定向到输出流
     *
     * @param inputStream
     * @param outputStream
     * @return
     */
    public static boolean copy(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[1024];
            int realLength;
            while ((realLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, realLength);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(inputStream);
            close(outputStream);
        }
    }

    /**
     * 对流进行close
     *
     * @param closeables
     */
    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除某路径的文件
     *
     * @param path
     * @return
     */
    public static boolean delete(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
