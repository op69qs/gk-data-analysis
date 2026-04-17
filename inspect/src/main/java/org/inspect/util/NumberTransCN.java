// NumberTransCN.java

package org.inspect.util;

/**
 * 阿拉伯数字转中文小写
 * @author Created by Samer on 2019/10/28.
 */
public class NumberTransCN {

    //阿拉伯数字转中文小写
    public static String transition(int si) {
        String cn_num = "";
        String[] aa = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};
        String[] bb = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] ch = Integer.toString(si).toCharArray();
        int maxindex = ch.length;
        // 字符的转换
        //两位数的特殊转换
        if (maxindex == 2) {
            for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
                if (ch[j] != 48) {
                    if (j == 0 && ch[j] == 49) {
                        cn_num += (bb[j] + aa[i]);
                    } else {
                        cn_num += (bb[ch[j] - 49] + aa[i]);
                    }
                }
            }
//其他位数的特殊转换，使用的是int类型最大的位数为十亿
        } else {
            for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
                if (ch[j] != 48) {
                    cn_num += (bb[ch[j] - 49] + aa[i]);
                }
            }
        }
        return cn_num;
    }

} ///:~
