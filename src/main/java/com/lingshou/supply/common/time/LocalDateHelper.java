package com.lingshou.supply.common.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/1/23 下午4:28
 */
public class LocalDateHelper {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String DATE_WITHOUT_SEPARATOR_PATTERN = "yyyyMMdd";


    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return localDateTime2Date(LocalDateTime.now());
    }

    /**
     * 获取1970-01-01 00:00:00的日期
     *
     * @return
     */
    public static Date ofDefault() {
        return of(1970, 1, 1);
    }


    /**
     * 根据年月日 时分秒 构建日期
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return localDateTime2Date(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second));
    }


    /**
     * 根据年月日 构建日期
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static Date of(int year, int month, int dayOfMonth) {
        return localDate2Date(LocalDate.of(year, month, dayOfMonth));
    }

    /**
     * 获取给定日期当月的最后一天
     *
     * @param localDate
     * @return
     */
    private static LocalDate getLastDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取给定日期当月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        return localDate2Date(getLastDayOfMonth(date2localDate(date)));
    }

    /**
     * 获取给定日期当月的第一天
     *
     * @param localDate
     * @return
     */
    private static LocalDate getFirstDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取给定日期当月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        return localDate2Date(getFirstDayOfMonth(date2localDate(date)));
    }

    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static Date getFirstDayOfMonth() {
        return localDate2Date(getFirstDayOfMonth(LocalDate.now()));
    }

    /**
     * 获取当月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth() {
        return localDate2Date(getLastDayOfMonth(LocalDate.now()));
    }


    /**
     * 获取给定日期当月的最后一天,如果是当月则返回当日时间
     *
     * @param localDate
     * @return
     */
    private static LocalDate getLastOrCurrentDayOfDate(LocalDate localDate) {
        LocalDate now = LocalDate.now();
        if (Objects.equals(now.getYear(), localDate.getYear()) && Objects.equals(now.getMonth(), localDate.getMonth())){
            return now;
        }
        return getLastDayOfMonth(localDate);
    }

    /**
     * 获取给定日期当月的最后一天,如果是当月则返回当日时间
     *
     * @param date
     * @return
     */
    public static Date getLastOrCurrentDayOfDate(Date date) {
        return localDate2Date(getLastOrCurrentDayOfDate(date2localDate(date)));
    }

    /**
     * 获取给定日期前n月的最后一天
     *
     * @param localDate
     * @param months
     * @return
     */
    private static LocalDate getLastDayOfLocalDateMinusMonths(LocalDate localDate, long months) {
        return getLastDayOfMonth(localDate).minusMonths(months);
    }

    /**
     * 获取给定日期前n月的最后一天g
     *
     * @param date
     * @param months
     * @return
     */
    public static Date getLastDayOfLocalDateMinusMonths(Date date, long months) {
        return localDate2Date(getLastDayOfLocalDateMinusMonths(date2localDate(date), months));
    }

    /**
     * 获取给定日期后n月的最后一天
     *
     * @param localDate
     * @param months
     * @return
     */
    private static LocalDate getLastDayOfLocalDatePlusMonths(LocalDate localDate, long months) {
        return getLastDayOfMonth(localDate).plusMonths(months);
    }

    /**
     * 获取给定日期后n月的最后一天
     *
     * @param date
     * @param months
     * @return
     */
    public static Date getLastDayOfLocalDatePlusMonths(Date date, long months) {
        return localDate2Date(getLastDayOfLocalDatePlusMonths(date2localDate(date), months));
    }

    /**
     * 获取给定日期的最后一天的前n天
     *
     * @param localDate
     * @param days
     * @return
     */
    private static LocalDate getLastDayOfLocalDateMinusDays(LocalDate localDate, long days) {
        return getLastDayOfMonth(localDate).minusDays(days);
    }

    /**
     * 获取给定日期的最后一天的前n天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getLastDayOfLocalDateMinusDays(Date date, long days) {
        return localDate2Date(getLastDayOfLocalDateMinusDays(date2localDate(date), days));
    }

    /**
     * 获取给定日期后n月的最后一天
     *
     * @param localDate
     * @param days
     * @return
     */
    private static LocalDate getLastDayOfLocalDatePlusDays(LocalDate localDate, long days) {
        return getLastDayOfMonth(localDate).plusDays(days);
    }

    /**
     * 获取给定日期后n月的最后一天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getLastDayOfLocalDatePlusDays(Date date, long days) {
        return localDate2Date(getLastDayOfLocalDatePlusDays(date2localDate(date), days));
    }


    /**
     * 得到当前日期n小时后的日期
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date plusHours(Date date, long hours) {
        return localDateTime2Date(date2localDateTime(date).plusHours(hours));
    }


    /**
     * 得到当前日期n小时前的日期
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date minusHours(Date date, long hours) {
        return plusHours(date, -hours);
    }

    /**
     * 得到当前日期n天后的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date plusDays(Date date, long days) {
        return localDateTime2Date(date2localDateTime(date).plusDays(days));
    }

    /**
     * 得到当前日期n天前的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date minusDays(Date date, long days) {
        return plusDays(date, -days);
    }

    /**
     * 得到当前日期n月后的日期
     *
     * @param date
     * @param months
     * @return
     */
    public static Date plusMonths(Date date, long months) {
        return localDateTime2Date(date2localDateTime(date).plusMonths(months));
    }

    /**
     * 得到当前日期n月前的日期
     *
     * @param date
     * @param months
     * @return
     */
    public static Date minusMonths(Date date, long months) {
        return plusMonths(date, -months);
    }


    /**
     * 得到传入时间为周几
     *
     * @param localDate
     * @return
     */
    private static int getDayOfWeek(LocalDate localDate) {
        return localDate.getDayOfWeek().getValue();
    }

    /**
     * 得到传入时间为周几
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        return getDayOfWeek(date2localDate(date));
    }


    /**
     * 得到现在为周几
     *
     * @return
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(LocalDate.now());
    }

    /**
     * 判断是否为默认时间1970-01-01 00:00:00
     *
     * @param date
     * @return
     */
    public static boolean isDateEqualDefaultTime(Date date) {
        if(date==null){
            return false;
        }
        return secondsBetweenStartAndEnd(date, ofDefault()) == 0;
    }

    /**
     * 得到两个日期的相差的秒数
     *
     * @param begin
     * @param end
     * @return
     */
    private static long secondsBetweenStartAndEnd(LocalDateTime begin, LocalDateTime end) {
        return Duration.between(begin, end).getSeconds();
    }

    /**
     * 得到两个日期的相差的秒数
     *
     * @param begin
     * @param end
     * @return
     */
    public static long secondsBetweenStartAndEnd(Date begin, Date end) {
        return secondsBetweenStartAndEnd(date2localDateTime(begin), date2localDateTime(end));
    }

    /**
     * 得到两个日期的相差的天数
     *
     * @param begin
     * @param end
     * @return
     */
    private static int daysBetweenStartAndEnd(LocalDate begin, LocalDate end) {
        return Period.between(begin, end).getDays();
    }

    /**
     * 得到两个日期的相差的月数
     *
     * @param begin
     * @param end
     * @return
     */
    private static int monthsBetweenStartAndEnd(LocalDate begin, LocalDate end) {
        return Period.between(begin, end).getMonths();
    }

    /**
     * 得到两个日期的相差的年数
     *
     * @param begin
     * @param end
     * @return
     */
    private static int yearsBetweenStartAndEnd(LocalDate begin, LocalDate end) {
        return Period.between(begin, end).getYears();
    }


    /**
     * 得到两个日期的相差的天数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int daysBetweenStartAndEnd(Date begin, Date end) {
        return daysBetweenStartAndEnd(date2localDate(begin), date2localDate(end));
    }

    /**
     * 得到两个日期的相差的月数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int monthsBetweenStartAndEnd(Date begin, Date end) {
        return monthsBetweenStartAndEnd(date2localDate(begin), date2localDate(end));
    }

    /**
     * 得到两个日期的相差的年数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int yearsBetweenStartAndEnd(Date begin, Date end) {
        return yearsBetweenStartAndEnd(date2localDate(begin), date2localDate(end));
    }

    /**
     * 格式化为对应pattern
     *
     * @param localDate
     * @param pattern
     * @return
     */
    private static String formatWithPattern(LocalDate localDate, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    /**
     * 格式化为对应pattern
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatWithPattern(Date date, String pattern) {
        return formatWithPattern(date2localDate(date), pattern);
    }

    /**
     * 格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatWithDateTimePattern(Date date) {
        return formatWithPattern(date, DATE_TIME_PATTERN);
    }

    /**
     * 格式化为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatWithDatePattern(Date date) {
        return formatWithPattern(date, DATE_PATTERN);
    }

    /**
     * 格式化为yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String formatWithDateNoseparatorPattern(Date date) {
        return formatWithPattern(date, DATE_WITHOUT_SEPARATOR_PATTERN);
    }


   /*
    public static LocalDateTime parseWithPattern(String date, String pattern){
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }*/

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime
     * @return
     */
    private static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return
     */
    private static LocalDateTime date2localDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDate转 Date
     *
     * @param localDate
     * @return
     */
    private static Date localDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDate
     *
     * @param date
     * @return
     */
    private static LocalDate date2localDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


}


