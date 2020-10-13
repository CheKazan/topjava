package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.UserUtils;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


import static org.slf4j.LoggerFactory.getLogger;


public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private UserRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryUserRepository();


    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("userId");
        String caloriesPerDay = request.getParameter("caloriesPerDay");
        log.info(" User now is {}", id);
        SecurityUtil.setUserId(Integer.parseInt(id));
        SecurityUtil.setCaloriesPerDay(Integer.parseInt(caloriesPerDay));
        response.sendRedirect("index.html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                repository.delete(id);
                response.sendRedirect("users");
                break;
//            case "create":
//            case "update":
//                final User newUser = "create".equals(action) ?
//                        new User(null,"Enter your name","Enter email","enter your pass", DEFAULT_CALORIES_PER_DAY,true, Collections.emptySet()) :
//                        repository.get(getId(request));
//                request.setAttribute("user", newUser);
//                request.getRequestDispatcher().forward(request, response);
//                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("users",
                        UserUtils.filterByPredicate(repository.getAll(), user -> true));
                request.getRequestDispatcher("/users.jsp").forward(request, response);
                break;
        }
    }
    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
