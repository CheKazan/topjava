package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class UserUtils {

public static final List<User> users = Arrays.asList(
        new User(null,"Brtur #1","1@1.ru","123", DEFAULT_CALORIES_PER_DAY,true,Stream.of(Role.ADMIN).collect(Collectors.toSet())),
        new User(null,"Artur #2","2@1.ru","123",DEFAULT_CALORIES_PER_DAY,true,Stream.of(Role.USER).collect(Collectors.toSet())),
        new User(null,"Drtur #3","3@1.ru","123",DEFAULT_CALORIES_PER_DAY,true,Stream.of(Role.USER , Role.ADMIN).collect(Collectors.toSet())),
        new User(null,"Frtur #4","4@1.ru","123",DEFAULT_CALORIES_PER_DAY,true,Stream.of(Role.USER).collect(Collectors.toSet())),
        new User(null,"Hrtur #5","5@1.ru","123",DEFAULT_CALORIES_PER_DAY,true,Stream.of(Role.USER).collect(Collectors.toSet())),
        new User(null,"Ertur #6","6@1.ru","123",DEFAULT_CALORIES_PER_DAY,true,Stream.of(Role.USER).collect(Collectors.toSet()))
        );

        public static List<User> filterByPredicate(Collection<User> users, Predicate<User> filter) {
                return users.stream().filter(filter).collect(Collectors.toList());
        }

}

