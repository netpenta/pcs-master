package th.co.pentasol.pcs.master.model.filter;

import lombok.Data;
import th.co.pentasol.pcs.master.model.api.Pagination;

import java.util.Date;

@Data
public class CustomerFilter extends Pagination {
    private String customerCode;
    private String branchCode;
    private Integer serialNo;
    private Date effectDate;
}
