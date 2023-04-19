package th.co.pentasol.pcs.master.model;

import lombok.Data;

import java.util.List;

@Data
public class ItemModel {
    private String itemCustCode;
    private String itemCode;
    private String itemRevNo;
    private Long itemDiv;
    private String itemName1;
    private String itemName2;
    private String stockUnitName;
    private String makerName;
    private String modelName;
    private String typeName;
    private String itemGroup1Div;
    private String itemGroup2Div;
    private String itemGroup3Div;
    private String itemGroup4Div;
    private String itemGroup5Div;
    private String inventoryGroup;
    private String lr;
    private Long coatingFlg;
    private String coatingType;
    private Long weight;
    private Long stdUnitCost;
    private String millMakerName;
    private Long minProdQtty;
    private Long safetyStock;
    private Long breakTestDiv;
    private Long breakTestCnt;
    private Long safetyLeadTime;
    private String materialSize;
    private String materialSpec;
    private String materialClass;
    private String stockPlaceCode;
    private String purFactoryCode;
    private String shelfNo;
    private String shelfNoDetail;
    private String sOrderUnitName;
    private String sOrderPacking;
    private Long sStockUnitInputQtty;
    private Long sOrderUnitInputQtty;
    private Long sOrderRate;
    private String struckUnitName;
    private String struckPacking;
    private Long struckStockUnitInputQtty;
    private Long structUnitInputQtty;
    private Long structRate;
    private String invtUnitName;
    private String invtPacking;
    private Long invtStockUnitInputQtty;
    private Long invtUnitInputQtty;
    private Long invtRate;
    private String itemPhotoFileName;
    private String itemRemark;
    private String shipmentPlaceCode;
    private Long spm;
    private Long deletedFlg;
    private String userId;
    private String programId;
    private String createdDatetime;
    private String modifiedDatetime;
    private String accItemCode;
    private Long safetyLeadTimeSec;
    private Long cavity;
    private Long rubberWeight;
    private Long rubberPerShot;
    private Long shotPerShift;
    private Long lowStockLimit;
    private Long maxStockLimit;
    private String formatTag;
    private Long expCheckDate;
    private String weightUnitName;
    private Long issueTagFlg;
    private Long specialTagFlg;
    private Long deliveryLeadTime;
    private Long stockCardFlg;
    private Long snpPacking;
    private String drawingNo;
    private Long sizeMPerMm;
    private String localSalesAccCode;
    private String exportSalesAccCode;
    private String purAccCode;
    private String debitCustAccCode;
    private String creditLocalAccCode;
    private String creditExportAccCode;
    private Long exportItemFlg;
    private String  inventoryGroupName;
    private String  materialClassName;
    private String  stockPlaceName;
    private String  factAbbrnm;
    private String  structUnitName;
    private String  structPacking;
    private Long  structStockUnitInputQtty;
    private String  shipmentPlaceName;
    private boolean notInPart;
    private Long  prodSnp;
    private String  procCode;
    private String  procName;
    private String  placeCode;
    private String  placeName;
    private Long stockUnitDecimal;
    private Long invtUnitDecimal;
    private Long sOrderUnitDecimal;
    private Long structUnitDecimal;
    private String  modifiedBy;
    private String  workPlaceCode;
    private String  workPlaceName;
    private String  machineSetCode;
    private String  dieSetCode;
    private String  dieSetName;
    private String  formatTagName;
    private boolean expCheck;
    private boolean specialTag;
    private Long issuedTagFlg;
    private boolean issuedTag;
    private List<String> procCodeSelected;
    private List<String> machineSetCodeSelected;
    private Long effectDate;
    private Long  pOrderUnitPrice;
    private boolean stockCard;
    private Long  reqQtty;
    private List<String> itemCodeSelected;
    private Long purResultTagQtty;
    private Boolean useStockPlace;
    private ItemProcessModel process;
    private ItemLocationModel location;
}