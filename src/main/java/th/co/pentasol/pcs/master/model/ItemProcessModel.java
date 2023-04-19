package th.co.pentasol.pcs.master.model;

import lombok.Data;

@Data
public class ItemProcessModel {
    private String  itemCustCode;
    private String  itemCode;
    private String  itemRevNo;
    private Long lineNo;
    private Long sortNo;
    private String  procCode;
    private String  procName;
    private Long procDiv;
    private String  materialCode;
    private Long  thickness;
    private Long  width;
    private Long  pitch;
    private Long  weight;
    private String  packing;
    private Long  prodSnp;
    private Long  roundQtty;
    private Long pOrderDiv;
    private Long  pOrderPoint;
    private Long  pOrderPoInteger;
    private Long  workLoad;
    private Long  procCost;
    private Long deletedFlg;
    private String  userId;
    private String  programId;
    private String  planPriorityCode;
    private Long oneShiftOneItemFlg;
}
