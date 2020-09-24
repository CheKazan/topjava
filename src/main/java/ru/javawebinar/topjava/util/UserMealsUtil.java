package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate,Integer> sumCalories = new HashMap<>(); //calories per day summ

        // cycle for calculation all days calories
        LocalDate currentDate=null;
        int dayCaloriesSumm=0;
        for (UserMeal u: meals) {
            if(currentDate==null)
                currentDate=u.getDateTime().toLocalDate();

            if(!currentDate.equals(u.getDateTime().toLocalDate())){
                sumCalories.put(currentDate,dayCaloriesSumm);
                dayCaloriesSumm=u.getCalories();
                currentDate=u.getDateTime().toLocalDate();
            }
            else   dayCaloriesSumm+=u.getCalories();
        }
        sumCalories.put(currentDate,dayCaloriesSumm);
        //generating result list with excess
        //without filtering by hours
        //for (UserMeal u: meals) result.add(new UserMealWithExcess(u.getDateTime(),u.getDescription(),u.getCalories(),sumCalories.get(u.getDateTime().toLocalDate())>2000 ? true : false ));

        //with filtering by hours
        for (UserMeal u: meals) if(u.getDateTime().toLocalTime().isBefore(endTime) && u.getDateTime().toLocalTime().isAfter(startTime) && sumCalories.getOrDefault(u.getDateTime().toLocalDate(),2001)<=2000)
            result.add(new UserMealWithExcess(u.getDateTime(),u.getDescription(),u.getCalories(),sumCalories.get(u.getDateTime().toLocalDate())>2000 ? true : false ));
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams

        return null;
    }
}
