package th.co.pentasol.pcs.master.model;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerListModel {
    private Integer rowNo;
    private String code;
    private String nameEn;
    private String nameTh;
    private Integer serialNo;
    private Date effectiveDate;
    private Date expiredDate;
    private String deliveryLocation;
    private String shortNameEn;
    private String shortNameTh;
    private String taxId;
    private String branchNo;
    private String branchName;
    private String modifiedBy;
    private String modifiedDateTime;
}
