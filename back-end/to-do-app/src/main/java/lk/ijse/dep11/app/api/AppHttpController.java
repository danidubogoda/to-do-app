package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.app.to.TaskTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.PreDestroy;
import java.sql.*;


public class AppHttpController {

    private final HikariDataSource pool;

    public AppHttpController() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/todo_app");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("3339");
        config.addDataSourceProperty("maximumPoolSize", 10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){pool.close();}

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public TaskTO createTask(
            @RequestBody @Validated(TaskTO.Create.class) TaskTO taskTO){
         System.out.println("createTask()");

        try (Connection connection = pool.getConnection()) {

            PreparedStatement stm = connection
                    .prepareStatement("INSERT INTO task (description, status, email) VALUES (?, FALSE, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, taskTO.getDescription());
            stm.setString(2, taskTO.getEmail());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            taskTO.setId(id);
            taskTO.setStatus(false);
            return taskTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public void updateTask(){
        System.out.println("updateTask()");

    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteTask(@PathVariable int id){
         System.out.println("deleteTask()");

    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", params = {"email"})
    public void getAllTasks(String email){
         System.out.println("getAllTask()");

    }

}
