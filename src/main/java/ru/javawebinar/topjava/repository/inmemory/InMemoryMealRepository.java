package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Filter;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m-> save(m,m.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if(meal.getUserId()!=userId && userId!=0) return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if(repository.get(id) == null || (repository.get(id).getUserId() != userId && userId!=0)) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if(repository.get(id)== null || (repository.get(id).getUserId() != userId && userId!=0)) return null;
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository
                .values()
                .stream()
                .filter( userId==0 ? x-> true :  meal -> meal.getUserId().equals(userId))
                .sorted((Comparator.comparing(Meal::getDateTime).reversed())).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        return repository
                .values()
                .stream()
                .filter(userId==0 ? x-> true :  meal -> meal.getUserId().equals(userId))
                .filter(x-> DateTimeUtil.isBetweenHalfOpen(x.getDate(),startDate,endDate))
                .collect(Collectors.toList());
    }


}

