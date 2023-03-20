package th.co.pentasol.pcs.master.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class CustomerModel {
    @NotBlank(message = "{msg.required.customer.code}")
    @Size(max = 20, message = "{msg.max.length.customer.code}")
    private String code;
//    @NotBlank(message = "{msg.required.customer.name}")
//    @Size(max = 300, message = "{msg.max.length.customer.nameEn}")
    private String nameEn;
    private String nameTh;
    private Integer serialNo;
//    @NotBlank(message = "{msg.required.customer.effectiveDate}")
    private Date effectiveDate;
//    @NotBlank(message = "{msg.required.customer.expireDate}")
    private Date expiredDate;
    private Integer groupId;
    private String deliveryLocation;
    private String shortNameEn;
    private String shortNameTh;
    private String taxId;
    @NotBlank(message = "{msg.required.customer.branchCode}")
    @Size(max = 20, message = "{msg.max.length.customer.branchCode}")
    private String branchCode;
//    @NotBlank(message = "{msg.required.customer.branchNo}")
//    @Size(max = 20, message = "{msg.max.length.customer.branchNo}")
    private String branchNo;
//    @NotBlank(message = "{msg.required.customer.branchName}")
//    @Size(max = 300, message = "{msg.max.length.customer.branchName}")
    private String branchName;
//    @NotBlank(message = "{msg.required.customer.address1}")
//    @Size(max = 300, message = "{msg.max.length.customer.address1}")
    private String address1;
    private String address2;
    private String telephoneNo;
    private String faxNo;
    private String userName;
    private String systemId;
    private String modifiedBy;
    private String modifiedDateTime;
    private Integer deleted_flg;
    private Integer effectDate;
    private Integer expDate;
}
