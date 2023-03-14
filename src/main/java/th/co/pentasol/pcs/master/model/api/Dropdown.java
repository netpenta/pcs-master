package th.co.pentasol.pcs.master.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dropdown {
    private Object value;
    private String label;
}
