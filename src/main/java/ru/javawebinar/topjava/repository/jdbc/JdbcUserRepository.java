package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    @Transactional
    public User save(User user) {
        //jdbc doesnt validate annotation automatically, need handle validate
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() != 0) throw new ConstraintViolationException(violations);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        // dell all roles before insert or update,and will insert again
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        /*
        for every role 1 query - N+1 problem
        user.getRoles().forEach((u)->
        jdbcTemplate.update("INSERT INTO user_roles(user_id,role) VALUES (?,?)",user.getId(),u.toString())
        );
        */
        // fill queries in batch by cycles and then pull by 1 query without n+1 problem.
        // Size of batch and count of cycles define by second method
        List<Role> rolesList = List.copyOf(user.getRoles());
        jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id,role) VALUES (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setString(2, String.valueOf(rolesList.get(i)));
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role AS roles FROM users u " +
                "INNER JOIN user_roles r ON u.id=r.user_id WHERE u.id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(getUserWithAllRoles(users));
        //old
        //List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id)
        //return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role AS roles FROM users u " +
                "INNER JOIN user_roles r ON u.id=r.user_id WHERE u.email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(getUserWithAllRoles(users));
        //old
        // //return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        //List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        //return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role AS roles FROM users u " +
                "LEFT JOIN user_roles r ON u.id=r.user_id", ROW_MAPPER);
        return getUserWithAllRoles(users);
        // return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
    }

    private List<User> getUserWithAllRoles(List<User> users) {
        Map<Integer, User> resultMap = new HashMap<>();
        users.forEach((u) -> {
            if (resultMap.containsKey(u.getId())) {
                User userInMap = resultMap.get(u.getId());
                userInMap.getRoles().addAll(u.getRoles());
            } else resultMap.put(u.getId(), u);
        });
        return resultMap.values().stream().collect(Collectors.toList());
    }
}
