package th.co.pentasol.pcs.master.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerEntity {
    private String cust_cd;
    private String branch_cd;
    private Integer serial_no;
    private Integer effect_date;
    private Integer exp_date;
    private Integer group_div;
    private String delivery_location;
    private String cust_nm;
    private String cust_nm_th;
    private String cust_short_nm;
    private String cust_short_nm_th;
    private String tax_id;
    private String cust_branch_id;
    private String cust_branch_nm;
    private String zip_cd;
    private String address_1;
    private String address_1_th;
    private String address_2;
    private String address_2_th;
    private String tel_no;
    private String fax_no;
    private String billing_cust_cd;
    private String billing_branch_cd;
    private Integer vat_div;
    private Integer round_div;
    private String employee_cd;
    private String cust_person;
    private String cust_person_th;
    private Integer delivery_lead_time;
    private Integer deleted_flg;
    private String user_id;
    private String user_fname;
    private String program_id;
    private Date created_datetime;
    private Date modified_datetime;
}
