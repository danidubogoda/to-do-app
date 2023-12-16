package lk.ijse.dep11.app.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tasks")
public class MyHttpController {

    public void addTask(){
        System.out.println(" addTask()");
    }


    public void updateTask(){
        System.out.println("updateTask()");
    }


    public void deleteTask(){
        System.out.println("deleteTask()");
    }


    public void getAllTask(){
        System.out.println("getAllTask()");
    }

}
