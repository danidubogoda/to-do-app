package lk.ijse.dep11.app.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskTo implements Serializable {

    @Null(message = "Id should be empty")
    private Integer id;

    @NotBlank(message = "Description can't be empty")
    private String description;

    @NotNull(message = "Status should not be empty")
    private Boolean status;
}
