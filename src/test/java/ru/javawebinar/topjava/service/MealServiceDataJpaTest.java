package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolverDataJpa;
import ru.javawebinar.topjava.ActiveDbProfileResolverJpa;

@ActiveProfiles(resolver = ActiveDbProfileResolverDataJpa.class)
public class MealServiceDataJpaTest extends MealServiceTest {
}
