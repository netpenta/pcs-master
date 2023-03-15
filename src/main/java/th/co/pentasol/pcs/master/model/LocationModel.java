package th.co.pentasol.pcs.master.model;

import lombok.Data;

import java.util.Date;
@Data
public class LocationModel {
    private Long locationId;
    private String code;
    private String name;
    private Integer serialNo;
    private Integer effectDate;
    private String updatedBy;
    private Date updatedDate;
    private String systemId;
}

