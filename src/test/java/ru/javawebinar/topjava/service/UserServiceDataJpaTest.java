package ru.javawebinar.topjava.service;


import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolverDataJpa;

@ActiveProfiles(resolver = ActiveDbProfileResolverDataJpa.class)
public class UserServiceDataJpaTest extends UserServiceTest {


}