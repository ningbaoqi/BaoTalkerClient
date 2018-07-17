package com.dashen.ningbaoqi.factory.utils;

import com.dashen.ningbaoqi.factory.net.NetWork;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.utils.HashUtil;
import project.com.ningbaoqi.utils.StreamUtil;

/**
 * 简单的一个文件缓存，实现文件的下载操作，下载成功后回调相应的方法
 */
public class FileCache<Holder> {
    private File baseDir;
    private String ext;
    private CacheListener<Holder> listener;
    private SoftReference<Holder> holderSoftReference;//最后一次目标的Holder

    public FileCache(String baseDir, String ext, CacheListener<Holder> listener) {
        this.baseDir = new File(Application.getCacheDirFile(), baseDir);
        this.ext = ext;
        this.listener = listener;
    }

    public void download(Holder holder, String path) {
        if (path.startsWith(Application.getCacheDirFile().getAbsolutePath())) {//如果路径是根本的缓存路径，那么不需要进行下载操作
            listener.onDownloadSucceed(holder, new File(path));
            return;
        }
        final File cacheFile = buildCacheFile(path);//构建缓存文件
        if (cacheFile.exists() && cacheFile.length() > 0) {//如果文件存在，无需重新下载
            listener.onDownloadSucceed(holder, cacheFile);
            return;
        }
        holderSoftReference = new SoftReference<>(holder);//把目标进行软引用
        OkHttpClient client = NetWork.getClient();
        Request request = new Request.Builder().url(path).get().build();
        Call call = client.newCall(request);
        call.enqueue(new NetCallback(holder, cacheFile));
    }

    /**
     * 拿最后的目标只能使用一次
     *
     * @return
     */
    private Holder getLastHolderAndClear() {
        if (holderSoftReference == null) {
            return null;
        } else {//拿取并清理
            Holder holder = holderSoftReference.get();
            holderSoftReference.clear();
            return holder;
        }
    }

    /**
     * 下载回调
     */
    private class NetCallback implements Callback {
        private final SoftReference<Holder> holderSoftReference;
        private final File file;

        public NetCallback(Holder holder, File file) {
            this.holderSoftReference = new SoftReference<>(holder);
            this.file = file;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Holder holder = holderSoftReference.get();
            if (holder != null && holder == getLastHolderAndClear()) {
                FileCache.this.listener.onDownloadFailed(holder);//仅仅最后一次的才是有效的
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            InputStream inputStream = response.body().byteStream();
            if (inputStream != null && StreamUtil.copy(inputStream, file)) {//文件的下载操作
                Holder holder = holderSoftReference.get();
                if (holder != null && holder == getLastHolderAndClear()) {
                    FileCache.this.listener.onDownloadSucceed(holder, file);//仅仅最后一次的才是有效的
                }
            } else {
                onFailure(call, null);
            }
        }
    }

    /**
     * 构建一个缓存文件，同一个网络路径对应一个本地的文件
     *
     * @param path
     * @return
     */
    private File buildCacheFile(String path) {
        String key = HashUtil.getMD5String(path);
        return new File(baseDir, key + "." + ext);
    }

    public interface CacheListener<Holder> {
        void onDownloadSucceed(Holder holder, File file);//下载成功

        void onDownloadFailed(Holder holder);//下载失败
    }
}
