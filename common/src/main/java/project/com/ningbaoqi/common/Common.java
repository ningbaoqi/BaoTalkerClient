package project.com.ningbaoqi.common;

/**
 * @author ningbaoqi
 */
public class Common {
    /**
     * 一些不可变的永恒的参数；通常用于一些配置
     */
    public interface Constance {
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";//手机号的正则表达式;检测十一位手机号
        String API_URL = "http://192.168.188.8:8080/api/";//基础的网络请求地址；服务器的IP地址
        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;//最大的上传图片大小860kb
    }
}
