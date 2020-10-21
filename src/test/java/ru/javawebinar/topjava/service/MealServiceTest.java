package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertEquals( meal , UserTestData.meal);
        assertEquals( meal.getDateTime() , UserTestData.meal.getDateTime());
        assertEquals( meal.getCalories() , UserTestData.meal.getCalories());
        assertEquals( meal.getDescription() , UserTestData.meal.getDescription());
       // assertThat(meal).usingRecursiveComparison().isEqualTo(UserTestData.meal);
    }
    @Test
    public void getNotFoundMeal() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND,USER_ID));
    }
    @Test
    public void getNotFoundUser() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID,ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID,NOT_FOUND));
    }
    @Test
    public void getAll() throws Exception {
        List<Meal> userMeals = service.getAll(USER_ID);
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertEquals(userMeals, UserTestData.usersMeals);
        assertEquals(adminMeals,UserTestData.adminMeals);
    }
    @Test
    public void update() throws Exception {
        Meal base = service.get(MEAL_ID, USER_ID);
        Meal updatedLocal = getUpdatedMeal(base);
        service.update(updatedLocal,USER_ID);
        Meal updatedFromDB = service.get(MEAL_ID, USER_ID);
        assertEquals(updatedFromDB,updatedLocal);
    }
    @Test
    public void updateWithWrongUser() throws Exception {
        Meal base = service.get(MEAL_ID, USER_ID);
        Meal updatedLocal = getUpdatedMeal(base);
        //try to update meal from wrong user
        assertThrows(NotFoundException.class,()->service.update(updatedLocal,ADMIN_ID));
        assertThrows(NotFoundException.class,()->service.update(updatedLocal,NOT_FOUND));
    }
    @Test
    public void delete() throws Exception {
        service.delete(MEAL_ID,USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID,USER_ID));
    }
    @Test
    public void deleteNotFound() throws Exception {
        //wrong user try to delete meal
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID,ADMIN_ID));
        // try to delete not found meal
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND,USER_ID));
    }
    @Test
    public void create() throws Exception {
        Meal meal = getNewMeal();
        Meal newMealFromDB = service.create(meal,USER_ID);
        Integer idFromDB = newMealFromDB.getId();
        meal.setId(idFromDB);
        assertEquals(newMealFromDB,meal);
        assertEquals(service.get(idFromDB,USER_ID),meal);
    }
    @Test
    public void createWithAbsentUser() throws Exception {
        Meal meal = getNewMeal();
        assertThrows(Exception.class, () -> service.create(meal,NOT_FOUND));
    }



}

