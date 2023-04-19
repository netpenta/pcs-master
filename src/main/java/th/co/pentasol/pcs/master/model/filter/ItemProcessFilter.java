package th.co.pentasol.pcs.master.model.filter;

import lombok.Data;
import th.co.pentasol.pcs.master.model.api.Pagination;

@Data
public class ItemProcessFilter extends Pagination {
    /* CONCEPT 1 ITEM : 1 PROCESS */
    private  String itemCustCode;
    private String itemCode;
    private String itemRevNo;
    private Long lineNo; /* MAYBE INCREASE THIS VALUE TO CONCEPT 1 ITEM : MANY PROCESS */
}
