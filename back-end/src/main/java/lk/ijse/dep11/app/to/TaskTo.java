package lk.ijse.dep11.app.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTo implements Serializable {

    @Null(message = "Id should be empty")
    private Integer id;

    @NotBlank(message = "Description can't be empty")
    private String description;

    @Null(message = "Status should be empty", groups = Create.class)
    @NotNull(message = "Status should not be empty", groups = Update.class)
    private Boolean status;

    public interface Update extends Default {}

    public interface Create extends Default {}
}
