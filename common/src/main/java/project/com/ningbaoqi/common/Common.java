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
    }
}
