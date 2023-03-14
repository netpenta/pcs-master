package th.co.pentasol.pcs.master.model;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerModel {
    private String code;
    private String nameEn;
    private String nameTh;
    private Integer serialNo;
    private Date effectiveDate;
    private Date expiredDate;
    private Integer groupId;
    private String deliveryLocation;
    private String shortNameEn;
    private String shortNameTh;
    private String taxId;
    private String branchNo;
    private String branchName;
    private String address1;
    private String address2;
    private String telephoneNo;
    private String faxNo;
    private String userName;
    private String systemId;
}
