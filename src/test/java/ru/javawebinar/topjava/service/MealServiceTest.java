package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
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
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest extends TestCase {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void testGet() {
        assertMatch(service.get(100009, UserTestData.ADMIN_ID), adminMeals.get(0));
    }

    @Test
    public void testDelete() {
        service.delete(100010, UserTestData.ADMIN_ID);
        assertNull(repository.get(100010, UserTestData.ADMIN_ID));
    }

    @Test
    public void testGetBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(
                LocalDateTime.of(2020, Month.MAY, 8, 10, 0).toLocalDate(),
                LocalDateTime.of(2020, Month.MAY, 11, 20, 0).toLocalDate(),
                UserTestData.ADMIN_ID);
        assertMatch(meals, adminMeals);
    }


    public void testGetAll() {
        List<Meal> meals = service.getAll(UserTestData.ADMIN_ID);
        assertMatch(meals, adminMeals);
    }

    @Test
    public void testUpdate() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, UserTestData.ADMIN_ID);
        Integer newId = created.getId();

        newMeal.setId(newId);
        newMeal.setCalories(500);
        newMeal.setDescription("Updated");

        service.update(newMeal, UserTestData.ADMIN_ID);
        assertMatch(service.get(newId, UserTestData.ADMIN_ID), newMeal);
    }

    @Test
    public void testCreate() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, UserTestData.USER_ID);
        Integer newId = created.getId();

        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        Meal meal = service.get(100009, UserTestData.USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.delete(100009, UserTestData.USER_ID);
    }
}