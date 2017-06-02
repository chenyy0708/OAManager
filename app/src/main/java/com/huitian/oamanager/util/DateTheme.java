package com.huitian.oamanager.util;

import cn.aigestudio.datepicker.bizs.themes.DPTheme;

/**
 * Created by Chen on 2017/6/2.
 */

public class DateTheme extends DPTheme {
    @Override
    public int colorBG() {
        return 0x50B48172;
    }

    @Override
    public int colorBGCircle() {
        return 0xEE888888;
    }

    @Override
    public int colorTitleBG() {
        return 0xEE888888;
    }

    @Override
    public int colorTitle() {
        return 0x50B48172;
    }

    @Override
    public int colorToday() {
        return 0x50B48172;
    }

    @Override
    public int colorG() {
        return 0x50B48172;
    }

    @Override
    public int colorF() {
        return 0x50B48172;
    }

    @Override
    public int colorWeekend() {
        return 0x50B48172;
    }

    @Override
    public int colorHoliday() {
        return 0x50B48172;
    }


    /**
     * 农历文本颜色
     *
     * Color of Lunar text
     *
     * @return 16进制颜色值 hex color
     */
    public int colorL() {
        return 0xEE888888;
    }

    /**
     * 补休日期背景颜色
     *
     * Color of Deferred background
     *
     * @return 16进制颜色值 hex color
     */
    public int colorDeferred() {
        return 0x50B48172;
    }
}
