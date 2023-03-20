package th.co.pentasol.pcs.master.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LocationEntity { //same name with Database
    private String placeCd;
    private String placeNm;
    private String placeThNm;
    private Integer serialNo;
    private Integer effectDate;
    private Integer expDate;
    private String updatedBy;
    private Date createdDatetime;
    private Date modifiedDatetime;
}
