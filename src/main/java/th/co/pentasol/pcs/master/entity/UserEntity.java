package th.co.pentasol.pcs.master.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserEntity {
    private Long userId;
    private Long roleId;
    private String roleName;
    private String userName;
    private String password;
    private String factory;
    private String section;
    private String workPlace;
    private String token;
    private Date tokenExpiredDate;
    private String fullName;
    private String position;
    private String email;
    private String telNo;
    private String locale;
    private Integer adminFlg;
    private Date lastLogin;
    private Date lastLogout;
    private Integer deletedFlg;
    private Date createdDatetime;
    private String modifiedId;
    private String modifiedBy;
    private Date modifiedDatetime;
    private String programId;
}
