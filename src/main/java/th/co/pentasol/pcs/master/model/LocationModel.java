package th.co.pentasol.pcs.master.model;

import lombok.Data;

import java.util.Date;
@Data
public class LocationModel {
    private String code;
    private String name;
    private String nameTh;
    private String type;
    private Integer serialNo;
    private Integer effectDate;
    private String updatedBy;
    private Date updatedDate;
    private String systemId;
}

