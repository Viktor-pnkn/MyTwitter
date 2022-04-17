package application.controllers;

import application.entities.Message;
import application.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepo repo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        //  model - то, куда мы будем складывать данные, которые хотим вернуть пользователю
        return "greeting"; // возвращаем контейнеру спринга имя файла, которое хотим отобразить (файл шаблон .html)
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = repo.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
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
