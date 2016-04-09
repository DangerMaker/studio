package com.example.exerciseapp.utils;

/**
 * Created by Cherie_No.47 on 2016/4/7 23:23.
 * Email jascal@163.com
 */
public class SpeedConvert {

    //m/s to min/km
    public static double oriToShow(double speed) {
        if (0 == speed) return 0;
        return 1.0 / (0.06 * speed);
    }

    /**
     * @param time 单位 s
     *             若时间超过一小时则以时/分为单位，若没有超过则以分/秒为单位
     */
    public static String secToTime(long time) {
        String charTime = null;
        if (0 == time) {
            return "00:00";
        }
        if (0 == (time / 3600)) {
            charTime = (time - (time / 3600) * 3600) / 60 + ":"
                    + (time - time / 3600 * 3600 - (time - (time / 3600) * 3600) / 60 * 60);
        } else {
            charTime = time / 3600 + ":" + (time - (time / 3600) * 3600) / 60;
        }
//         time / 3600 + ":" + (time - (time / 3600) * 3600) / 60 + ":"
//                + (time - time / 3600 * 3600 - (time - (time / 3600) * 3600) / 60 * 60);
        return SpeedConvert.reForm(charTime);
    }

    public static String reForm(String charTime) {
        String args[] = charTime.split(":");
        if (1 == args[0].length()) {
            args[0] = "0" + args[0];
        }
        if (1 == args[1].length()) {
            args[1] = "0" + args[1];
        }
        return args[0] + ":" + args[1];
    }

}
