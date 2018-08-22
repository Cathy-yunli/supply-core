package com.lingshou.supply.common.geo;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/2/12 上午10:34
 */
public class GeoHashUtils {
    private static int numbits = 30; //经纬度单独编码长度
    //32位编码对应字符
    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    //定义编码映射关系
    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
    //初始化编码映射内容
    static {
        int i = 0;
        for (char c : digits){
            lookup.put(c, i++);
        }
    }

    public static int getGeohashLengthByRang(int rangNum){
        Map<Integer,Integer> map = Maps.newHashMap();
        // key为范围，value为取定的位数  如1公里范围内，位数取6；3公里，位数取5
        map.put(1,6);
        map.put(2,6);
        map.put(3,5);
        map.put(4,5);
        map.put(5,5);
        map.put(6,5);
        map.put(7,5);
        map.put(8,5);
        map.put(9,5);
        map.put(10,5);
        return map.get(rangNum).intValue();
    }

    //对编码后的字符串解码
    public static BigDecimal[] decode(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {

            int i = lookup.get(c) + 32;
            buffer.append( Integer.toString(i, 2).substring(1) );
        }

        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();

        //偶数位，经度
        int j =0;
        for (int i=0; i< numbits*2;i+=2) {
            boolean isSet = false;
            if ( i < buffer.length() ) {
                isSet = buffer.charAt(i) == '1';
                lonset.set(j++, isSet);
            }
        }

        //奇数位，纬度
        j=0;
        for (int i=1; i< numbits*2;i+=2) {
            boolean isSet = false;
            if ( i < buffer.length() ) {
                isSet = buffer.charAt(i) == '1';
                latset.set(j++, isSet);
            }
        }

        BigDecimal lon = decode(lonset, new BigDecimal(-180), new BigDecimal(180));
        BigDecimal lat = decode(latset, new BigDecimal(-90), new BigDecimal(90));

        return new BigDecimal[] {lat, lon};
    }

    //根据二进制和范围解码
    private static BigDecimal decode(BitSet bs, BigDecimal floor, BigDecimal ceiling) {
        BigDecimal mid = BigDecimal.ZERO;
        for (int i=0; i<bs.length(); i++) {
            mid = (floor.add(ceiling)).divide(new BigDecimal(2),10,BigDecimal.ROUND_HALF_UP);
            if (bs.get(i))  {
                floor = mid;
            }else {
                ceiling = mid;
            }
        }
        return mid;
    }

    //对经纬度进行编码
    public static String encode(BigDecimal lat, BigDecimal lon) {
        BitSet latbits = getBits(lat, new BigDecimal(-90), new BigDecimal(90));
        BitSet lonbits = getBits(lon, new BigDecimal(-180), new BigDecimal(180));
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < numbits; i++) {
            buffer.append( (lonbits.get(i))?'1':'0');
            buffer.append( (latbits.get(i))?'1':'0');
        }
        return base32(Long.parseLong(buffer.toString(), 2));
    }

    //根据经纬度和范围，获取对应二进制
    private static BitSet getBits(BigDecimal lat, BigDecimal floor, BigDecimal ceiling) {
        BitSet buffer = new BitSet(numbits);
        for (int i = 0; i < numbits; i++) {
            BigDecimal mid = (floor.add(ceiling)).divide(new BigDecimal(2),10,BigDecimal.ROUND_HALF_UP);
            if (lat.compareTo(mid) >= 0 ) {
                buffer.set(i);
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return buffer;
    }

    //将经纬度合并后的二进制进行指定的32位编码
    private static String base32(long i) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative){
            i = -i;
        }
        while (i <= -32) {
            buf[charPos--] = digits[(int) (-(i % 32))];
            i /= 32;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative){
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (65 - charPos));
    }
}
