package th.co.pentasol.pcs.master.entity;

import lombok.Data;

@Data
public class ItemProcessEntity {
    private String  item_cust_cd;
    private String  item_cd;
    private String  item_rev_no;
    private Long line_no;
    private Long sort_no;
    private String  proc_cd;
    private String  proc_nm;
    private Long proc_div;
    private String  material_cd;
    private Long  thickness;
    private Long  width;
    private Long  pitch;
    private Long  weight;
    private String  packing;
    private Long  prod_snp;
    private Long  round_qtty;
    private Long p_order_div;
    private Long  p_order_point;
    private Long  p_order_poInteger;
    private Long  work_load;
    private Long  proc_cost;
    private Long deleted_flg;
    private String  user_id;
    private String  program_id;
    private String  plan_priority_cd;
    private Long one_shift_one_item_flg;
}
