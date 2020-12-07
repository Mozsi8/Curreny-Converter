package interviewtask.currencyconverter.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private String status;
    private String message;

    public ErrorMessage(String message) {
        this.status = "error";
        this.message = message;
    }
}
