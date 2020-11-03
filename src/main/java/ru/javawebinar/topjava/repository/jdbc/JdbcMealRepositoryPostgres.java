package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;


@Repository
@Profile("postgres") //HSQLDB doesn't work with java8 time api-Arturka
public class JdbcMealRepositoryPostgres extends JdbcAbstractMealRepository {

    public JdbcMealRepositoryPostgres(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDateTime getCorrectTime(LocalDateTime localDateTime) {
        return localDateTime;
    }

}
