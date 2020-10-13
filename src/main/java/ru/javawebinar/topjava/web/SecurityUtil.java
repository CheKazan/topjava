package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    public static int userId = 0;
    public static int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;
    public static int authUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        SecurityUtil.userId = userId;
    }

    public static void setCaloriesPerDay(int caloriesPerDay) {
        SecurityUtil.caloriesPerDay = caloriesPerDay;
    }

    public static int authUserCaloriesPerDay() {
        return caloriesPerDay;
    }
}