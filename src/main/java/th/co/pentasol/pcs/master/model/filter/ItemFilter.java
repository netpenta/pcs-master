package th.co.pentasol.pcs.master.model.filter;

import lombok.Data;
import th.co.pentasol.pcs.master.model.api.Pagination;
@Data
public class ItemFilter extends Pagination {
    private  String itemCustCode;
    private String itemCode;
    private String itemRevNo;
    private String itemName1;
    private String formatTag;
    private String purFactoryCode;
    private String modelName;
    private String inventoryGroup;
    private String shipmentPlaceCode;
    private String stockPlaceCode;
    private String procCode;
    private String workPlaceCode;
    private String machineSetCode;
}
