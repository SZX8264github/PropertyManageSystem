package com.yjq.programmer.utils;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-26 19:09
 */

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 验证字符串是否为空
     * @return
     */
    public static boolean isEmpty(String str) {
        if(str == null || "".equals(str)) {
            return true; //为空
        }else {
            return false; //不为空
        }
    }

    /**
     * 返回指定格式的日期
     * @param str_date
     * @param formatter
     * @return
     */
    public static Date getFormatterDate(String str_date, String formatter){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
        try{
            Date date = simpleDateFormat.parse(str_date);
            return date;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断后缀是否是图片文件的后缀
     * @param suffix
     * @return
     */
    public static boolean isPhoto(String suffix){
        if("jpg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("png".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("gif".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("jpeg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 返回指定格式的日期字符串
     * @param date
     * @param formatter
     * @return
     */
    public static String getFormatterDate(Date date, String formatter){
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }

    /**
     * 文件大小转换
     * @param size
     * @return
     */
    public static String convertFileSize(Integer size) {
        Double KB = new BigDecimal(size * 1.00 / 1024).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        Double MB = new BigDecimal( (size * 1.00) / (1024 * 1024)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        Double GB = new BigDecimal( (size * 1.00) / (1024 * 1024 * 1024)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        if((size / 1024) == 0) {
            return size + "B";
        } else if ((size / (1024 * 1024)) == 0) {
            return KB + "KB";
        } else if ((size / (1024 * 1024 * 1024)) == 0) {
            return MB + "MB";
        } else {
            return GB + "GB";
        }
    }

    /**
     * 判断请求是否是ajax
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(header))return true;
        return false;
    }

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder(32);
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
