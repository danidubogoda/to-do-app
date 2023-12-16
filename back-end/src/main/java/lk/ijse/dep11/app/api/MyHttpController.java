package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.app.to.TaskTo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;

@RestController
@RequestMapping(value = "/tasks")
public class MyHttpController {

    private final HikariDataSource pool;

    public MyHttpController() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/to_do_app");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("3339");
        config.addDataSourceProperty("maximumPoolSize", 10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public TaskTo addTask(
            @RequestBody @Validated TaskTo taskto){
        try(Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO task (description, status) VALUES (?, FALSE)",
                    Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, taskto.getDescription());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            taskto.setId(id);
            taskto.setStatus(false);

            return taskto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateTask(){
        System.out.println("updateTask()");
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteTask(@PathVariable("id") int taskId){

        try(Connection connection = pool.getConnection()) {
            PreparedStatement stmExit = connection.prepareStatement("SELECT * FROM task WHERE id = ?");
            stmExit.setInt(1, taskId);
            ResultSet rst = stmExit.executeQuery();

            if(!rst.next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM task WHERE id = ?");
            stm.setInt(1, taskId);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("deleteTask()");
    }



    public void getAllTask(){
        System.out.println("getAllTask()");
    }

}
