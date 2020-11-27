package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/meals";
    //+
    @Override
    @GetMapping
    public List<MealTo> getAll(){
        return super.getAll();
    }
    //+
    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id){
        return super.get(id);
    }
    //+
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    //+
    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    //+
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

//    @GetMapping(value = "/{startDateJson}/{endDateJson}/{startTimeJson}/{endTimeJson}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public List<MealTo> getBetweenJson(@PathVariable(required = false) String startDateJson,@PathVariable(required = false) String endDateJson,@PathVariable(required = false) String startTimeJson,@PathVariable(required = false) String endTimeJson){
//        LocalDate startDate = parseLocalDate(startDateJson);
//        LocalDate endDate = parseLocalDate(endDateJson);
//        LocalTime startTime = parseLocalTime(startTimeJson);
//        LocalTime endTime = parseLocalTime(endTimeJson);
//        return super.getBetween(startDate, startTime, endDate, endTime);
//    }


}