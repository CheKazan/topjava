package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends MealAbstractController{


    public JspMealController(MealService service) {
        super(service);
    }

    //+
    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", this.getAll());
        return "meals";
    }

    //+
    @GetMapping(value = "/meals", params = {"action=delete", "id"})
    public String deleteActionMeal(@RequestParam(value = "id", required = false) Integer id) {
        this.delete(id);
        return "redirect:meals";
    }

    //+
    @GetMapping(value = "/meals", params = {"action=update", "id"})
    public String updateActionMeal(HttpServletRequest request,
                                   @RequestParam(value = "id", required = false) Integer id) {
        request.setAttribute("meal", this.get(id));
        return "forward:mealForm";
    }

    //+
    @GetMapping(value = "/meals", params = {"action=create"})
    public String createActionMeal(HttpServletRequest request) {
        request.setAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "forward:mealForm";
    }

    //+
    @GetMapping(value = "/meals", params = {"action=filter", "startDate", "endDate", "startTime", "endTime"})
    public String filterActionMeals(Model model,
                                    @RequestParam(value = "startDate", required = false) String startDateS,
                                    @RequestParam(value = "endDate", required = false) String endDateS,
                                    @RequestParam(value = "startTime", required = false) String startTimeS,
                                    @RequestParam(value = "endTime", required = false) String endTimeS
    ) {
        LocalDate startDate = parseLocalDate(startDateS);
        LocalDate endDate = parseLocalDate(endDateS);
        LocalTime startTime = parseLocalTime(startTimeS);
        LocalTime endTime = parseLocalTime(endTimeS);
        model.addAttribute("meals", this.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    //+
    @GetMapping("/mealForm")
    public String mealForm() {
        return "mealForm";
    }

    //+
    @PostMapping("/meals")
    public String mealFormPost(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (!StringUtils.hasText(request.getParameter("id"))) {
            this.create(meal);
        } else {
            this.update(meal, getId(request));
        }
        return "redirect:meals";
    }

    //+
    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }


}

