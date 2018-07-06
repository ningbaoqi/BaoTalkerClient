package com.dashen.ningbaoqi.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.dashen.ningbaoqi.factory.Factory;

import java.io.File;
import java.util.Date;

import project.com.ningbaoqi.utils.HashUtil;

/**
 * 上传工具类，用于上传任何文件到阿里OSS存储
 */
public class UploadHelper {
    private static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";//与OSS上的存储区域有关系
    private static final String BUCKET_NAME = "baotalk";//上传到仓库名
    private static final String TAG = "nbq";

    private static OSS getClient() {
        //明文设置secret的方式建议只是在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIbHskM1VMdFFK", "iaC3mmh4gaRJiw1GQnqmTDM3miadWb");
        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传最终方法，同步的操作
     *
     * @param objkey 上传上去后在服务器上的独立的key
     * @param path   需要上传的文件的路径
     * @return 返回存储的地址
     */
    private static String upload(String objkey, String path) {
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objkey, path);//构造一个上传请求
        Log.d(TAG, "1-------" + request);
        try {
            OSS client = getClient();//初始化上传的client
            Log.d(TAG, "2-------" + request);
            PutObjectResult result = client.putObject(request);//开始同步上传
            Log.d(TAG, "3-------" + request);
            String url = client.presignPublicObjectURL(BUCKET_NAME, objkey);//得到外网可访问的地址
            Log.d(TAG, "PublicObjectURL-------" + url);
            return url;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 上传普通图片
     *
     * @param path 普通图片的本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 头像的本地地址
     * @return 服务器地址
     */
    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传音频文件
     *
     * @param path 音频文件的本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传规则image/201807/sadsadsad23asdasd.jpg
     *
     * @param path
     * @return
     */
    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));//拿到file地址
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    /**
     * 上传规则portrait/201807/sadsadsad23asdasd.jpg
     *
     * @param path
     * @return
     */
    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));//拿到file地址
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    /**
     * 上传规则audio/201807/sadsadsad23asdasd.mp3
     *
     * @param path
     * @return
     */
    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));//拿到file地址
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }

    /**
     * 分月存储，避免文件夹太多
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }
}
