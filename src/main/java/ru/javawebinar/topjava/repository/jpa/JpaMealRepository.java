package ru.javawebinar.topjava.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

 //   @Autowired
 //   private UserRepository repository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        //problem with jpa -> no validation by entity manager
        //because named query, but it works and finish tests
//        if(meal.isNew()){
//            meal.setUser(repository.get(userId));
//            em.persist(meal);
//            return meal;
//        } else {
//            if (em.createNamedQuery(Meal.UPDATE)
//                .setParameter(1,meal.getId())
//                .setParameter(2,userId)
//                .setParameter(3,meal.getDescription())
//                .setParameter(4,meal.getCalories())
//                .setParameter(5,meal.getDateTime())
//                .executeUpdate()==0) {
//                return null;
//            }
//            return meal;
            try {
                //User currentUser = repository.get(userId);
                //proxy object with lazy init
                User currentUser = em.getReference(User.class,userId);
                if(meal.isNew()){
                    meal.setUser(currentUser);
                    em.persist(meal);
                    return meal;
                } else {
                    Meal mealFromBD = this.get(meal.getId(), userId);
                    if(mealFromBD.getUser().getId().equals(userId)) {
                        meal.setUser(currentUser);
                        return em.merge(meal);
                    }
                }
            } catch (NullPointerException e) {return null;}
            return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter(1, id)
                .setParameter(2, userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return (Meal) DataAccessUtils.singleResult(
                em.createNamedQuery(Meal.GET)
                .setParameter(1, id)
                .setParameter(2, userId)
                .getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED,Meal.class)
                .setParameter(1,userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN_HALF_OPEN,Meal.class)
                .setParameter(1,userId)
                .setParameter(2,startDateTime)
                .setParameter(3,endDateTime)
                .getResultList();
    }
}