package project.com.ningbaoqi.common.app;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {//外部获取单例
        return instance;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 缓存当前app的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * @return
     */
    public static File getPorttraitTmpFile() {//获取头像缓存文件
        File dir = new File(getCacheDirFile(), "portrait");//得到头像文件夹的缓存地址
        dir.mkdirs();//创建所有的对应的文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {//删除旧的一些缓存文件
            for (File f : files) {
                f.delete();
            }
        }
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();//返回当前时间戳的目录文件地址
    }

    /**
     * 获取缓存的音频文件
     *
     * @param isTmp 上传到服务器上的时候需要将isTmp设置为false，用时间戳作为文件名
     * @return 录音文件
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                f.delete();
            }
        }
        File path = new File(getCacheDirFile(), isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }

    /**
     * 显示一个Toast
     *
     * @param msg 传递的是信息
     */
    public static void showToast(final String msg) {
        //Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();//Toast只能在主线程中显示，所有需要进行线程转换，保证一定是在主线程中进行的show操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_LONG).show();//在主线程中运行
            }
        });
    }

    /**
     * 显示一个Toast
     *
     * @param msgId
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }
}
