package th.co.pentasol.pcs.master.model;

import lombok.Data;

import java.util.List;

@Data
public class ItemLocationModel {
    private String  itemCustCode;
    private String  itemCode;
    private String  itemRevNo;
    private Long itemProcLineNo;
    private Long lineNo;
    private Long sortNo;
    private String  workPlaceCode;
    private String  workPlaceName;
    private String  workloadGroupCode;
    private Long workloadGroupBrNo;
    private String  fixPlanGroupCode;
    private String  machineGroupDiv;
    private String  machineGroupName;
    private String  machineSetCode;
    private String  machineSetName;
    private String  dieGroupDiv;
    private String  dieGroupName;
    private String  dieSetCode;
    private String  dieSetName;
    private String  dieJigSetCode;
    private Long materialSupplyDiv;
    private Long  yieldRate;
    private Long  stdWaitTime;
    private Long  stdPreparationTime;
    private Long  stdWorkTime;
    private Long  noOfCavity;
    private Long  pOrderLeadTime;
    private Long deletedFlg;
    private String  userId;
    private String  programId;
    private Long procDiv;
    private String  factoryCode;
    private String  factAbbrnm;
    private Long useStockPlaceDiv;
    private Long workDivideFlg;
    private Long preparationDivideFlg;
    private Long workPreparationDivideFlg;
    private Long holidayWorkTime;
    private Long workEndDate;
    private String  resultWorkEndTime;
    private Long preWorkEndDate;
    private String  resultPreWorkEndTime;
    private List<String> workplacecdList;
}
