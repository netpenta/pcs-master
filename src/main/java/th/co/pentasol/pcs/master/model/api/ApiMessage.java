package th.co.pentasol.pcs.master.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiMessage {
    private String type;
    private String message;
}
