package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolverDataJpa;
import ru.javawebinar.topjava.ActiveDbProfileResolverJdbc;

@ActiveProfiles(resolver = ActiveDbProfileResolverJdbc.class)
public class MealServiceJdbcTest extends MealServiceTest {
}
