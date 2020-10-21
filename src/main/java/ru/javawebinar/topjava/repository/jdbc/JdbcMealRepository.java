package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        //        List<Meal> result = jdbcTemplate.query("SELECT * FROM meals WHERE userid=? AND datetime=?",ROW_MAPPER,userId,meal.getDateTime());
        //        if(!result.isEmpty()){
        //            System.out.println("Nothing created");;
        //            return null;
        //        }
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("datetime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("description", meal.getDescription())
                .addValue("userid",userId);
        if(meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if(namedParameterJdbcTemplate
                .update("UPDATE meals SET datetime=:datetime, description=:description, " +
                        "calories=:calories WHERE id=:id AND userid=:userid", map) == 0){
        return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND userid=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
       List<Meal> result = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND userid=?", ROW_MAPPER,id,userId);
        return result.isEmpty() ? null : DataAccessUtils.singleResult(result);
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> result = jdbcTemplate.query("SELECT * FROM meals WHERE userid=? ORDER BY datetime DESC",ROW_MAPPER,userId);
        return result;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> result = jdbcTemplate.query("SELECT * FROM meals WHERE userid=? AND datetime BETWEEN ? AND ? ORDER BY datetime DESC",
                ROW_MAPPER,userId,startDateTime,endDateTime);
        return result;
    }
}
