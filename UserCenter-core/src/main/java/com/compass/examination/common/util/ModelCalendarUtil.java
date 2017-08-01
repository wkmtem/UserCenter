package com.compass.examination.common.util;

import java.util.Calendar;

/**
 * 
 * @Class Name: ModelCalendarUtil
 * @Description: 模块化calendar的类，封装了一些常用的方法
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年3月23日上午10:26:13
 * @version: 2.0
 */
public class ModelCalendarUtil {

    public final static int SUNDAY = 1;
    public final static int MONDAY = 2;
    public final static int TUESDAY = 3;
    public final static int WEDNESDAY = 4;
    public final static int THURSDAY = 5;
    public final static int FRIDAY = 6;
    public final static int SATURDAY = 7;
    public Calendar mCalendar = Calendar.getInstance();
    public int mYear;
    public int mMonth;

    /**
     * 
     * @Construction Name: 
     * @Description: 当前年月
     * @Create date: 2017年3月23日下午1:15:24
     */
    public ModelCalendarUtil() {
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        initCalendarUtil();
    }

    /**
     * 
     * @Construction Name: 
     * @Description: 指定年月
     * @Create date: 2017年3月23日下午1:15:51
     * @param year
     * @param month: 月份-1
     */
    public ModelCalendarUtil(int year, int month) {
        mYear = year;
        mMonth = month;
        initCalendarUtil();
    }

    /**
     * 
     * @Method Name: initCalendarUtil
     * @Description: 初始化:日历日期为月初
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:27:35:
     */
    private void initCalendarUtil() {
        mCalendar.set(mYear, mMonth, 1);
    }

    /**
     * 
     * @Method Name: getFirstDayWeek
     * @Description: 获取当前月第1天的星期
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:27:47
     * @return:
     */
    public int getFirstDayWeek() {
        int firstDayWeek;
        firstDayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        return firstDayWeek;
    }

    /**
     * 
     * @Method Name: getFirstDayWeek
     * @Description: 重载：获取指定月第1天的星期
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:28:22
     * @param addMonth
     * @return:
     */
    public int getFirstDayWeek(int addMonth) {
        int firstDayWeek;
        if (addMonth != 0) {
            mCalendar.add(Calendar.MONTH, addMonth);
            firstDayWeek = getFirstDayWeek();
            initCalendarUtil();
        } else {
            firstDayWeek = getFirstDayWeek();
        }
        return firstDayWeek;
    }

    /**
     * 
     * @Method Name: getLastDayWeek
     * @Description: 获取当前月最后1天的星期
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:29:21
     * @return:
     */
    public int getLastDayWeek() {
        int lastDayWeek;
        mCalendar.roll(Calendar.DATE, -1);
        lastDayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        initCalendarUtil();
        return lastDayWeek;
    }

    /**
     * 
     * @Method Name: getLastDayWeek
     * @Description: 重载：获取指定月最后1天的星期
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:30:33
     * @param addMonth
     * @return:
     */
    public int getLastDayWeek(int addMonth) {
        int lastDayWeek;
        if (addMonth != 0) {
            mCalendar.add(Calendar.MONTH, addMonth);
        }
        lastDayWeek = getLastDayWeek();
        return lastDayWeek;
    }

    /**
     * 
     * @Method Name: getDayWeek
     * @Description: 获取当前月指定号的星期
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:32:08
     * @param day
     * @return:
     */
    public int getDayWeek(int day) {
        int dayWeek;
        if (day != 0) {
            mCalendar.set(mYear, mMonth, day);
            // mCalendar.add(Calendar.DAY_OF_MONTH, day);
            dayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
            initCalendarUtil();
        } else {
            dayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        }
        return dayWeek;
    }

    /**
     * 
     * @Method Name: getDays
     * @Description: 获取当前月天数
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:32:35
     * @return:
     */
    public int getDays() {
        int days;
        days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    /**
     * 
     * @Method Name: getDays
     * @Description: 重载：获取指定月的天数
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:32:47
     * @param addMonth
     * @return:
     */
    public int getDays(int addMonth) {
        int days;
        if (addMonth != 0) {
            mCalendar.add(Calendar.MONTH, addMonth);
            days = getDays();
            initCalendarUtil();
        } else {
            days = getDays();
        }
        return days;
    }

    /**
     * 
     * @Method Name: getWeekOfMonth
     * @Description: 获取当前月中第几周
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:34:08
     * @param day
     * @return:
     */
    public int getWeekOfMonth(int day) {
        int weekOfMonth;
        if (day != 1) {
            mCalendar.set(mYear, mMonth, day);
            weekOfMonth = mCalendar.get(Calendar.WEEK_OF_MONTH);
            initCalendarUtil();
        } else {
            weekOfMonth = 1;
        }
        return weekOfMonth;
    }

    /**
     * 
     * @Method Name: getWeekOfMonth
     * @Description: 重载：获取指定月中第几周
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:34:49
     * @param addMonth
     * @param day
     * @return:
     */
    public int getWeekOfMonth(int addMonth, int day) {
        int weekOfMonth;
        mCalendar.set(mYear, mMonth, day);
        if (addMonth != 0) {
            mCalendar.add(Calendar.MONTH, addMonth);
            weekOfMonth = mCalendar.get(Calendar.WEEK_OF_MONTH);
            initCalendarUtil();
        } else {
            weekOfMonth = getWeekOfMonth(day);
        }
        return weekOfMonth;
    }

    /**
     * 
     * @Method Name: getDayOfYear
     * @Description: 获取当前年中第几天
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:35:38
     * @return:
     */
    public int getDayOfYear() {
        int days;
        days = mCalendar.get(Calendar.DAY_OF_YEAR);
        return days;
    }

    /**
     * 
     * @Method Name: getDayOfYear
     * @Description: 重载：获取指定年中第几天
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:36:14
     * @param addYear
     * @return:
     */
    public int getDayOfYear(int addYear) {
        int days;
        if (addYear != 0) {
            mCalendar.add(Calendar.YEAR, addYear);
            days = mCalendar.get(Calendar.DAY_OF_YEAR);
            initCalendarUtil();
        } else {
            days = mCalendar.get(Calendar.DAY_OF_YEAR);
        }
        return days;
    }

    /**
     * 
     * @Method Name: getDaysInYear
     * @Description: 获取当前年的天数
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:37:01
     * @return:
     */
    public int getDaysInYear() {
        int days;
        if (mYear % 400 == 0 || (mYear % 4 == 0 && mYear % 100 != 0)) {
            days = 366;
        } else {
            days = 365;
        }
        return days;
    }

    /**
     * 
     * @Method Name: getDaysInYear
     * @Description: 重载：获取指定年的天数
     * @params:
     * @author: wkm
     * @version: 2.0
     * @Create date: 2017年3月23日上午10:37:13
     * @param addYear
     * @return:
     */
    public int getDaysInYear(int addYear) {
        int days;
        int tYear = mYear + addYear;
        if (tYear % 400 == 0 || (tYear % 4 == 0 && tYear % 100 != 0)) {
            days = 366;
        } else {
            days = 365;
        }
        return days;
    }


    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    @Override
    public boolean equals(Object o) {
        ModelCalendarUtil other = (ModelCalendarUtil) o;
        if (other.mYear == mYear && other.mMonth == mMonth) {
            return true;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return mYear * 1000 + mMonth;
    }
}
