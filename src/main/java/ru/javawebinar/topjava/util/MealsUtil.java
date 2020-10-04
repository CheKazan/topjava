package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("------Stream--------");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<MealTo> result = new ArrayList<>();
        Map<LocalDate, Integer> sumCalories = new HashMap<>(); //calories per day summ
        // 1st var my
        /* for (Meal u : meals) {
            LocalDate currentDate = u.getDateTime().toLocalDate();
            if (!sumCalories.containsKey(currentDate))
                sumCalories.put(currentDate, u.getCalories());
            else
                sumCalories.put(currentDate, sumCalories.get(currentDate) + u.getCalories());
        }
           // for (UserMeal u: meals) result
        // .add(new UserMealWithExcess(u.getDateTime(),u.getDescription(),u.getCalories(),sumCalories.get(u.getDateTime().toLocalDate())>2000));
        for (Meal u : meals)
            if (TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(),startTime,endTime)
                    && sumCalories.getOrDefault(u.getDateTime().toLocalDate(), caloriesPerDay + 1) <= caloriesPerDay)
                result.add(new MealTo(u.getDateTime(),
                        u.getDescription(),
                        u.getCalories(),
                        sumCalories.get(u.getDateTime().toLocalDate()) > caloriesPerDay));
        */
        //2 var after lesson
        for (Meal u : meals) {
            LocalDate currentDate = u.getDateTime().toLocalDate();
            sumCalories.put(currentDate,sumCalories.getOrDefault(currentDate,0) + u.getCalories());
        }
        for (Meal u : meals)
            if (TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(),startTime,endTime))
                result.add(new MealTo(u.getDateTime(),
                        u.getDescription(),
                        u.getCalories(),
                        sumCalories.get(u.getDateTime().toLocalDate()) > caloriesPerDay));

        return result;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        //after 1 lesson
       Map<LocalDate,Integer> caloriesPerDaySum = meals
               .stream()
               .collect(Collectors.groupingBy(m->m.getDateTime().toLocalDate(),Collectors.summingInt(Meal::getCalories)));
        return meals
                .stream()
                .filter(m-> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(),startTime,endTime))
                .map(m-> new MealTo(m.getDateTime(),m.getDescription(),m.getCalories(),caloriesPerDaySum.get(m.getDateTime().toLocalDate())>caloriesPerDay))
                .collect(Collectors.toList());
    }
}