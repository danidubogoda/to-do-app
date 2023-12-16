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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
@CrossOrigin
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
            @RequestBody @Validated(TaskTo.Create.class)  TaskTo taskto){
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



    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public void updateTask( @PathVariable int id,
                            @RequestBody @Validated(TaskTo.Update.class) TaskTo taskTo){

        try (Connection connection = pool.getConnection()){

            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM task WHERE id=?");
            stmExist.setInt(1, id);
            ResultSet rst = stmExist.executeQuery();
            if(!rst.next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("UPDATE task SET description=?, status=? WHERE id=?");
            stm.setString(1, taskTo.getDescription());
            stm.setBoolean(2, taskTo.getStatus());
            stm.setInt(3, id);

            stm.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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



    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public List<TaskTo> getAllTask(){

        try (Connection connection = pool.getConnection()){

            PreparedStatement stm = connection.prepareStatement("SELECT * FROM task ORDER BY id");
            ResultSet rst = stm.executeQuery();

            ArrayList<TaskTo> TaskList = new ArrayList<>();

            while (rst.next()){
                int id = rst.getInt("id");
                String description = rst.getString("description");
                boolean status = rst.getBoolean("status");

                TaskList.add(new TaskTo(id, description, status));
            }

            return TaskList;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
