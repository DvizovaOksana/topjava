package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static List<Meal> adminMeals = Arrays.asList(
            new Meal(100009, LocalDateTime.of(2020, Month.MAY, 9, 14, 0), "Админ ланч", 510),
            new Meal(100010, LocalDateTime.of(2020, Month.MAY, 9, 21, 0), "Админ ужин", 1500)
    );

    public static Meal getNew(){
        return new Meal(null,
                LocalDateTime.of(2020, Month.JUNE, 24, 10, 0),
                "Новая еда", 750);
    }

    public static Meal getUpdated(){
        Meal updated = new Meal();
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
