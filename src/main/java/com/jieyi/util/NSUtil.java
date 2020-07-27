package com.jieyi.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Title: 字符工具类
 * </p>
 * <p>
 * Description:
 * </p>
 */
public class NSUtil {

    /**
     * 左补标志位
     */
    public static final int LPAN = 1;
    /**
     * 右补标志位
     */
    public static final int RPAN = 2;

    public NSUtil() {
    }

    /**
     * 功能：判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        if (str.matches("-?\\d+(.\\d+){0,1}") && str.length() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：把字符串转换成数字
     */
    public static int parseInt(String str) {
        return Integer.parseInt(NSUtil.isNumeric(str) ? str : "0");
    }

    /**
     * 功能：把字符串按进制数转换成数字
     */
    public static int parseInt(String str, int radix) {
        //FIXME 是否需要判断str是否为16进制
        return Integer.parseInt(str, radix);
    }

    /**
     * 功能：把字符串转换成数字
     */
    public static short parseShort(String str) {
        return Short.parseShort(NSUtil.isNumeric(str) ? str : "0");
    }

    /**
     * 功能：把字符串按进制数转换成数字
     */
    public static short parseShort(String str, int radix) {
        return Short.parseShort(str, radix);
    }

    /**
     * 功能：把字符串转换成数字
     */
    public static long parseLong(String str) {
        return Long.valueOf(NSUtil.isNumeric(str) ? str : "0");
    }

    /**
     * 功能：把字符串按进制数转换成数字
     */
    public static long parseLong(String str, int radix) {
        return Long.valueOf(Long.parseLong(str, radix));
    }

    /**
     * 功能：把字符串转换成单精度浮点数
     */
    public static float parseFloat(String str) {
        DecimalFormat decimalformat = new DecimalFormat("0.00");
        Float num = Float.parseFloat(NSUtil.isNumeric(str) ? str : "0");
        String numStr = decimalformat.format(num);
        return Float.parseFloat(numStr);
    }

    /**
     * Convert byte[] to hex string. 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] lowToHigh(int n) {
        byte[] b = new byte[7];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] highToLow(int n) {
        byte[] b = new byte[7];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * @param hexStr
     * @return
     * @Title: byteHigh2Low
     * @Description: 字节高低位转换
     */
    public static String byteHigh2Low(String hexStr) {
        byte[] srcByte = NSUtil.hexStringToBytes(hexStr);
        byte[] newByte = new byte[srcByte.length];
        for (int i = 0; i < srcByte.length; i++) {
            newByte[i] = srcByte[srcByte.length - 1 - i];
        }
        return NSUtil.bytesToHexString(newByte);
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return new byte[0];
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 左补零
     *
     * @param str
     * @param length
     * @return
     */
    public static String addLeftZero(String str, int length) {
        if (str == null) {
            str = "";
        }
        int str_length = str.length();
        for (int i = 0; i < (length - str_length); i = i + 1) {
            str = '0' + str;
        }
        return str;
    }

    /**
     * 右补零
     *
     * @param str
     * @param length
     * @return
     */
    public static String addRightZero(String str, int length) {
        if (str == null) {
            str = "";
        }
        int str_length = str.length();
        for (int i = 0; i < (length - str_length); i = i + 1) {
            str = str + '0';
        }
        return str;
    }

    /**
     * 右补字符
     *
     * @param str
     * @param length
     * @param lpad
     * @return
     */
    public static String addRightChar(String str, int length, String lpad) {
        if (str == null) {
            str = "";
        }
        int str_length = str.length();
        for (int i = 0; i < (length - str_length); i = i + 1) {
            str = str + lpad;
        }
        return str;
    }

    /**
     * 左补字符
     *
     * @param str
     * @param length
     * @param lpad
     * @return
     */
    public static String addLeftChar(String str, int length, String lpad) {
        if (str == null) {
            str = "";
        }
        int str_length = str.length();
        for (int i = 0; i < (length - str_length); i = i + 1) {
            str = lpad + str;
        }
        return str;
    }

    /**
     * 左补字符,按字节长度计算
     *
     * @param str
     * @param length
     * @return
     */
    public static String addLeftChar(String str, int length, char c, String encode) {
        if (str == null) {
            str = "";
        }
        int str_length = 0;
        try {
            str_length = str.getBytes(encode).length;
        } catch (UnsupportedEncodingException e) {
            ////logger.error("Error:", e);
        }
        for (int i = 0; i < (length - str_length); i = i + 1) {
            str = c + str;
        }
        return str;
    }

    /**
     * 右补字符,按字节长度计算
     *
     * @param str
     * @param length
     * @return
     */
    public static String addRightChar(String str, int length, char c, String encode) {
        if (str == null) {
            str = "";
        }
        int str_length = 0;
        try {
            str_length = str.getBytes(encode).length;
        } catch (UnsupportedEncodingException e) {
            ////logger.error("Error:", e);
        }
        for (int i = 0; i < (length - str_length); i = i + 1) {
            str = str + c;
        }
        return str;
    }

    /**
     * @param str
     * @return
     * @Title: delRightZero
     * @Description:删除右侧00
     */
    public static String delRightZero(String str) {
        if (str == null) {
            str = "";
        }
        while (true) {
            int str_length = str.length();
            if (str_length <= 2) {
                break;
            }
            if ("00".equals(str.substring(str_length - 2, str_length))) {
                str = str.substring(0, str_length - 2);
            } else {
                break;
            }
        }
        return str;
    }

    /**
     * @param str
     * @return
     * @Title: delLeftZero
     * @Description:删除左侧00
     */
    public static String delLeftZero(String str) {
        if (str == null) {
            str = "";
        }
        while (true) {
            int str_length = str.length();
            if (str_length <= 2) {
                break;
            }
            if ("00".equals(str.substring(0, 2))) {
                str = str.substring(2, str_length);
            } else {
                break;
            }
        }
        return str;
    }

    /**
     * 获取特定字节长度的字符串
     *
     * @param str
     * @param length
     * @return
     */
    public static String getByteLengthStr(String str, int length, String encode) {
        if (str == null) {
            str = "";
        }
        int str_length = 0;
        try {
            str_length = str.getBytes(encode).length;
        } catch (UnsupportedEncodingException e) {
            //logger.error("Error:", e);
        }
        if (str_length <= length) {
            return str;
        } else {
            byte[] byteTmp = new byte[length];
            String strTmp = "";
            try {
                byte[] strByte = str.getBytes(encode);

                for (int i = 0; i < length; i++) {
                    byteTmp[i] = strByte[i];
                }
                strTmp = new String(byteTmp, encode);
            } catch (UnsupportedEncodingException e) {
                //logger.error("Error:", e);
            }
            return strTmp;
        }
    }

    /**
     * 字符串截取补位高级函数 1.获取特定字节长度的字符串 2.左右补特定字符
     *
     * @param str
     * @param length
     * @param flag   LPAN:左，RPAN:右
     * @param c
     * @return
     */
    public static String getStrAd(String str, int length, int flag, char c, String encode) {
        if (flag == LPAN) {
            return NSUtil.addLeftChar(NSUtil.getByteLengthStr(str, length, encode), length, c, encode);
        } else {
            return NSUtil.addRightChar(NSUtil.getByteLengthStr(str, length, encode), length, c, encode);
        }
    }

    public static String getStringByByteNumber(String dealString, int startIndex, int byteLength, String charset) {
        String result = "";
        try {
            byte[] stringBytes = dealString.getBytes(charset);
            if (stringBytes.length < byteLength) {
                return result;
            }
            byte[] bytes = new byte[byteLength];
            for (int i = 0; i < byteLength; i++) {
                bytes[i] = stringBytes[startIndex + i];
            }
            result = new String(bytes, charset);
        } catch (Exception e) {
            //logger.error("Error:", e);
        }
        return result;
    }

    public static byte bswap(byte a) {
        byte b = 0;
        for (int i = 0; i < 8; ++i)
            b |= ((a & (1 << i)) == 0 ? 0 : 1) << (7 - i);
        return b;
    }

    /**
     * 字节数据相加
     *
     * @param sByte
     * @param aByte
     * @return
     */
    public static byte[] byteArrayAdd(byte[] sByte, byte[] aByte) {
        byte[] tByte = new byte[sByte.length + aByte.length];
        for (int i = 0; i < sByte.length; i++) {
            tByte[i] = sByte[i];
        }
        for (int i = 0; i < aByte.length; i++) {
            tByte[sByte.length + i] = aByte[i];
        }
        return tByte;
    }

    /**
     * 在字节数组获取，从第index开始，length长度的字节数
     *
     * @param byteArray
     * @param index     从0开始
     * @param length
     * @return
     */
    public static byte[] getBytes(byte[] byteArray, int index, int length) {
        byte[] getByteArray = new byte[length];

        for (int i = 0; i < length; i++) {
            getByteArray[i] = byteArray[index + i];
        }

        return getByteArray;
    }

    /**
     * 根据每段length长度分隔字符串
     *
     * @param str
     * @param length
     * @return
     */
    public static List<String> splitString(String str, int length) {
        String strHex = "";
        length = length * 2;
        try {
            strHex = bytesToHexString(str.getBytes("UTF-8"));
            str = strHex;
        } catch (UnsupportedEncodingException e) {
            //logger.error("Error:", e);
        }

        List<String> stringList = new ArrayList<String>();
        int strLength = str.length();
        int num = strLength / length;

        try {
            for (int i = 0; i < num; i++) {
                String strtmp = str.substring(i * length, (i + 1) * length);
                stringList.add(new String(hexStringToBytes(strtmp), "UTF-8"));
            }

            if (strLength % length != 0) {
                String strtmp = str.substring(num * length);
                stringList.add(new String(hexStringToBytes(strtmp), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            //logger.error("Error:", e);
        }

        return stringList;
    }

    /**
     * 左补字符串
     *
     * @param str
     * @param length
     * @param padStr
     * @return
     */
    public static String lpadStr(String str, int length, String padStr) {
        if (str == null) {
            str = "";
        }
        String lpadStr = str;
        while (true) {
            int strLength = lpadStr.length();
            if (strLength >= length) {
                break;
            } else {
                lpadStr = padStr + lpadStr;
            }
        }
        return lpadStr;
    }

    /**
     * 右补字符串
     *
     * @param str
     * @param length
     * @param padStr
     * @return
     */
    public static String rpadStr(String str, int length, String padStr) {
        if (str == null) {
            str = "";
        }
        String lpadStr = str;
        while (true) {
            int strLength = lpadStr.length();
            if (strLength >= length) {
                break;
            } else {
                lpadStr = lpadStr + padStr;
            }
        }
        return lpadStr;
    }

    public static String hexStringBack(String hexStr) {
        StringBuffer sb = new StringBuffer();
        hexStr = hexStr.toUpperCase();
        for (int i = 0; i < hexStr.length(); i++) {
            // 0123456789ABCDEF
            // FEDCBA9876543210

            if ("0".equals(hexStr.substring(i, i + 1))) {
                sb.append("F");
            }
            if ("1".equals(hexStr.substring(i, i + 1))) {
                sb.append("E");
            }
            if ("2".equals(hexStr.substring(i, i + 1))) {
                sb.append("D");
            }
            if ("3".equals(hexStr.substring(i, i + 1))) {
                sb.append("C");
            }
            if ("4".equals(hexStr.substring(i, i + 1))) {
                sb.append("B");
            }
            if ("5".equals(hexStr.substring(i, i + 1))) {
                sb.append("A");
            }
            if ("6".equals(hexStr.substring(i, i + 1))) {
                sb.append("9");
            }
            if ("7".equals(hexStr.substring(i, i + 1))) {
                sb.append("8");
            }
            if ("8".equals(hexStr.substring(i, i + 1))) {
                sb.append("7");
            }
            if ("9".equals(hexStr.substring(i, i + 1))) {
                sb.append("6");
            }
            if ("A".equals(hexStr.substring(i, i + 1))) {
                sb.append("5");
            }
            if ("B".equals(hexStr.substring(i, i + 1))) {
                sb.append("4");
            }
            if ("C".equals(hexStr.substring(i, i + 1))) {
                sb.append("3");
            }
            if ("D".equals(hexStr.substring(i, i + 1))) {
                sb.append("2");
            }
            if ("E".equals(hexStr.substring(i, i + 1))) {
                sb.append("1");
            }
            if ("F".equals(hexStr.substring(i, i + 1))) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    /**
     * 字节转换二进制
     *
     * @param byteArray
     * @return
     */
    public static String byteToBinary(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            byte b = byteArray[i];
            int z = b;
            z |= 256;
            String str = Integer.toBinaryString(z);
            int len = str.length();
            sb.append(str.substring(len - 8, len));
        }
        return sb.toString();
    }

    /**
     * 二进制转换字节
     *
     * @param bString
     * @return
     */
    public static byte[] binaryToByte(String bString) {
        byte[] bByte = new byte[0];
        for (int k = 0; k < bString.length() / 8; k++) {
            String bStringTmp = bString.substring(k * 8, (k + 1) * 8);
            byte result = 0;
            for (int i = bStringTmp.length() - 1, j = 0; i >= 0; i--, j++) {
                result += (Byte.parseByte(bStringTmp.charAt(i) + "") * Math.pow(2, j));
            }

            byte[] aByte = new byte[1];
            aByte[0] = result;

            bByte = byteArrayAdd(bByte, aByte);
        }

        return bByte;

    }

    public static String rPadEightZero(String str) {
        if (str == null) {
            return "00000000";
        }
        int strLen = str.length();
        if (strLen % 8 == 0) {
            return str;
        }
        int padLen = 8 - strLen % 8;
        String padStr = NSUtil.addRightZero(str, strLen + padLen);
        return padStr;
    }

    public static String lPadOneZero(String str) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen % 2 == 0) {
            return str;
        } else {
            return "0" + str;
        }

    }

    public static String moveLeftZero(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int i;
        for (i = 0; i < str.length(); i++) {
            if ("0".equals(str.substring(i, i + 1))) {

            } else {
                break;
            }
        }
        return str.substring(i);
    }

    /**
     * 寻找特定字符在字符串中第几次出现，前面的数据字符串
     *
     * @param str       原始数据
     * @param findValue 特定字符
     * @param index     第几次
     * @return
     */
    public static String findSpIndex(String str, char findValue, int index) {
        char[] chArray = str.toCharArray();
        int num = 0;
        int indexRet = -1;
        for (int i = 0; i < chArray.length; i++) {
            char ch = chArray[i];
            if (ch == findValue) {
                num++;
            }
            if (num == index) {
                indexRet = i;
                break;
            }
        }
        return str.substring(0, indexRet);
    }

    public static boolean isEmpty(String value) {
        if (value == null || "".equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String value) {
        if (value != null && !"".equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean equals(String src, String dist) {
        if (src == null && dist != null) {
            return dist.equals(src);
        } else if (dist == null && src != null) {
            return src.equals(dist);
        } else if (dist == null && src == null) {
            return true;
        } else {
            return src.equals(dist);
        }
    }

    /**
     * @param x 只能是1,2,4,6,8
     * @return
     * @Title: longToBytes
     * @Description:
     */
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    /**
     * @param bytes 不能大于8
     * @return
     * @Title: bytesToLong
     * @Description:
     */
    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip   
        return buffer.getLong();
    }

    /**
     * @param str
     * @param splitLen
     * @return
     * @Title: getInfoBysplitLen
     * @Description: 根据拆分长度，将字符串拆分，返回字符串内容。
     */
    public static String getInfoBysplitLen(String str, int splitLen) {
        String infoLeng;
        if (str.length() > splitLen) {
            infoLeng = str.substring(0, splitLen);
            str = str.substring(splitLen, splitLen + (Integer.parseInt(invertHex2Dec(infoLeng))));
        }
        return str;
    }

    public static String getWebRandom() {
        long f_str = (Long.parseLong(RandomUtil.getRandom(6)) % 1000) * 1000;
        long e_str = (Long.parseLong(RandomUtil.getRandom(6)) % 1000);
        String tot_str = String.valueOf(f_str + e_str);
        return tot_str;
    }

    /**
     * @param str
     * @param len
     * @return
     * @Title: dec2Hex
     * @Description:十进制转十六进制并补位
     */
    public static String dec2Hex(String str, int len) {

        Long data = NSUtil.parseLong(str);

        String s1 = Long.toHexString(data).toUpperCase();

        if (s1.length() > len) {
            s1 = s1.substring(s1.length() - len, s1.length());
        } else {
            s1 = NSUtil.addLeftZero(s1, len);
        }

        return s1;
    }

    /**
     * @param str
     * @return
     * @Title: hex2Dec
     * @Description:十六进制转十进制
     */
    public static String hex2Dec(String str) {

        Long data = NSUtil.parseLong(str, 16);

        return Long.toString(data);
    }

    /**
     * @param str
     * @return
     * @Title: hex2IntDec
     * @Description:十六进制转int十进制
     */
    public static String hex2IntDec(String str) {
        BigInteger bi = new BigInteger(str, 16);
        return bi.intValue() + "";
    }

    /**
     * @param str
     * @param len
     * @return
     * @Title: long2InvertHex
     * @Description 十进制转倒序十六进制并补位
     */
    public static String dec2InvertHex(String str, int len) {

        String s1 = dec2Hex(str, len);

        return NSUtil.byteHigh2Low(s1);
    }

    /**
     * @param str
     * @return
     * @Title: invertHex2Dec
     * @Description:倒序十六进制转十进制
     */
    public static String invertHex2Dec(String str) {

        String s1 = NSUtil.byteHigh2Low(str);

        return hex2Dec(s1);
    }

    /**
     * @param str
     * @return
     * @Title: invertHex2IntDec
     * @Description:倒序十六进制转int十进制 支持负数
     */
    public static String invertHex2IntDec(String str) {

        String s1 = NSUtil.byteHigh2Low(str);

        return hex2IntDec(s1);
    }

    /**
     * @param cron
     * @return
     * @Title: chkIfDisable
     * @Description:判断定时是否启用
     */
    public static boolean chkIfDisable(String cron) {
        if (cron.indexOf("2099") != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param oldList
     * @param newList
     * @return
     * @Title: listCompare
     * @Description: newList 比 oldList 多的数据，将多出的数据放入diffList
     */
    public static ArrayList<String> listCompare(ArrayList<String> oldList, ArrayList<String> newList) {
        ArrayList<String> diffList = new ArrayList<String>();
        HashMap<String, Integer> map = new HashMap<String, Integer>(oldList.size());
        for (String oldObj : oldList) {
            map.put(oldObj, 1);
        }
        for (String newObj : newList) {
            if (map.get(newObj) == null) {
                diffList.add(newObj);
            }
        }
        return diffList;
    }

    /**
     * @param s
     * @return
     * @Title: chkIfDisable
     * @Description:判断定时是否启用
     */
    public static String conv2Dot(String s) {
        if (isEmpty(s))
            return "0.00";
        if (s.length() > 2) {
            return s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2);
        } else if (s.length() == 2) {
            return "0." + s;
        } else {
            return "0.0" + s;
        }
    }

    /**
     * @param str
     * @param length
     * @return
     * @Title: trimLeftZeroSubstring
     * @Description: 密文组包控制长度过长使用, 左补0，然后截取
     */
    public static String trimLeftZeroSubstring(String str, int length) {
        if (str == null) {
            str = "";
        }
        return addLeftZero(str.trim(), length).substring(0, length);
    }

    /**
     * @param str
     * @param length
     * @return
     * @Title: trimRightZeroSubstring
     * @Description: 密文组包控制长度过长使用，右补0，然后截取
     */
    public static String trimRightZeroSubstring(String str, int length) {
        if (str == null) {
            str = "";
        }
        return addRightZero(str.trim(), length).substring(0, length);
    }

    //去除尾部特定字符
    public static String trimBackChar(String str, String chars) {
        if (NSUtil.isEmpty(str) || NSUtil.isEmpty(chars)) {
            return null;
        }

        int strLen = str.length();
        int charsLen = chars.length();
        int posback = strLen;
        int poshead = strLen;
        String trimStr = str;
        while (true) {
            poshead = poshead - charsLen;
            if (poshead < 0) {
                break;
            }
            if (chars.equals(trimStr.substring(poshead, posback))) {
                trimStr = trimStr.substring(0, poshead);
                posback = poshead;
            } else {
                break;
            }
        }

        return trimStr;
    }

    public static String addHexStringAsSomeMultipe(String data, int leftorright,
                                                   String string, int num) throws Exception {
        // TODO Auto-generated method stub
        String sData = data;
        int str_length = data.length();
        int yushu = str_length % num;

        if (string.length() != 2) {
            throw new Exception("string length is error!");
        }

        for (int i = 0; i < (num - yushu); i = i + 2) {
            if (leftorright == 0) {
                sData = string + sData;
            } else if (leftorright == 1) {
                sData = sData + string;
            } else {

            }
        }
        return sData;
    }

    public static String addRightString80(String data, int num) throws Exception {
        // TODO Auto-generated method stub
        String sData = data;
        int str_length = data.length();
        int yushu = str_length % num;

        sData = sData + "80";
        for (int i = 0; i < (num - yushu - 2); i++) {
            sData = sData + "0";
        }
        return sData;
    }

    public static void main(String[] arg) {
        System.out.println(parseFloat("100.81401"));

    }
}
