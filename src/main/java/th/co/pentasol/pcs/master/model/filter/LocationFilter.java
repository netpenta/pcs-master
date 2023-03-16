package th.co.pentasol.pcs.master.model.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.pentasol.pcs.master.model.api.Pagination;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationFilter extends Pagination {
    private String code;
    private String name;
    private String type;
}
