package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolverJpa;

@ActiveProfiles(resolver = ActiveDbProfileResolverJpa.class)
public class MealServiceJpaTest extends MealServiceTest {
}
