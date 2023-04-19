package th.co.pentasol.pcs.master.entity;

import lombok.Data;

import java.util.List;

@Data
public class ItemLocationEntity {
    private String  item_cust_cd;
    private String  item_cd;
    private String  item_rev_no;
    private Long item_proc_line_no;
    private Long line_no;
    private Long sort_no;
    private String  work_place_cd;
    private String  work_place_nm;
    private String  workload_group_cd;
    private Long workload_group_br_no;
    private String  fix_plan_group_cd;
    private String  machine_group_div;
    private String  machine_group_nm;
    private String  machine_set_cd;
    private String  machine_set_nm;
    private String  die_group_div;
    private String  die_group_nm;
    private String  die_set_cd;
    private String  die_set_nm;
    private String  die_jig_set_cd;
    private Long material_supply_div;
    private Long  yield_rate;
    private Long  std_wait_time;
    private Long  std_preparation_time;
    private Long  std_work_time;
    private Long  no_of_cavity;
    private Long  p_order_lead_time;
    private Long deleted_flg;
    private String  user_id;
    private String  program_id;
    private Long proc_div;
    private String  factory_cd;
    private String  fact_abbrnm;
    private Long use_stock_place_div;
    private Long work_divide_flg;
    private Long preparation_divide_flg;
    private Long work_preparation_divide_flg;
    private Long holiday_work_time;
    private Long work_end_date;
    private String  result_work_end_time;
    private Long pre_work_end_date;
    private String  result_pre_work_end_time;
    private List<String> workplacecdList;
}
