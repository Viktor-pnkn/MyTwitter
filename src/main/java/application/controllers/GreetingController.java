package application.controllers;

import application.entities.Message;
import application.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {
    @Autowired
    private MessageRepo repo;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           Map<String, Object> model) {
        //  model - то, куда мы будем складывать данные, которые хотим вернуть пользователю
        model.put("name", name); // кладем в поле name параметр, который получили на вход
        return "greeting"; // возвращаем контейнеру спринга имя файла, которое хотим отобразить (файл шаблон .html)
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = repo.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping
    public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        // аннотация @RequestParam берет значения из <form>, если это post запрос, а если get, то из url

        Message message = new Message(text, tag);
        repo.save(message);

        Iterable<Message> messages = repo.findAll();
        model.put("messages", messages);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = repo.findByTag(filter);
        }
        else {
            messages = repo.findAll();
        }
        model.put("messages", messages);
        return "main";
    }

}
