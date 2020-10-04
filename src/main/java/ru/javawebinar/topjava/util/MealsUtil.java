package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;


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

        //System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<MealTo> result = new ArrayList<>();
        Map<LocalDate,Integer> sumCalories = new HashMap<>(); //calories per day summ

        for (Meal u: meals) {
            LocalDate currentDate = u.getDateTime().toLocalDate();
            if (!sumCalories.containsKey(currentDate))
                sumCalories.put(currentDate, u.getCalories());
            else
                sumCalories.put(currentDate, sumCalories.get(currentDate) + u.getCalories());
        }
        // for (UserMeal u: meals) result.add(new UserMealWithExcess(u.getDateTime(),u.getDescription(),u.getCalories(),sumCalories.get(u.getDateTime().toLocalDate())>2000 ? true : false));
        for (Meal u: meals)
            if (u.getDateTime().toLocalTime().isBefore(endTime)
                    && u.getDateTime().toLocalTime().isAfter(startTime)
                    && sumCalories.getOrDefault(u.getDateTime().toLocalDate(), caloriesPerDay+1) <= caloriesPerDay)
                result.add(new MealTo(u.getDateTime(),
                        u.getDescription(),
                        u.getCalories(),
                        sumCalories.get(u.getDateTime().toLocalDate()) > caloriesPerDay));
        return result;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
       /* List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate,Integer> sumCalories = new HashMap<>(); //calories per day summ
        meals.stream().
                peek(p -> {
                    LocalDate currentDate = p.getDateTime().toLocalDate();
                    if (!sumCalories.containsKey(currentDate))
                        sumCalories.put(currentDate, p.getCalories());
                    else
                        sumCalories.put(currentDate, sumCalories.get(currentDate) + p.getCalories());
                })
                .filter(p-> p.getDateTime().toLocalTime().isBefore(endTime)
                        && p.getDateTime().toLocalTime().isAfter(startTime)
                        && sumCalories.getOrDefault(p.getDateTime().toLocalDate(), caloriesPerDay+1) <= caloriesPerDay)
                .forEach(u->
                        result.add(new UserMealWithExcess(u.getDateTime(),
                                u.getDescription(),
                                u.getCalories(),
                                sumCalories.get(u.getDateTime().toLocalDate()) > caloriesPerDay ? true : false)));
    */
        return null;
    }
}