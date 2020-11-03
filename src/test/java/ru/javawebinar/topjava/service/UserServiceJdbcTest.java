package ru.javawebinar.topjava.service;


import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolverJdbc;
import ru.javawebinar.topjava.ActiveDbProfileResolverJpa;

@ActiveProfiles(resolver = ActiveDbProfileResolverJdbc.class)
public class UserServiceJdbcTest extends UserServiceTest {

}