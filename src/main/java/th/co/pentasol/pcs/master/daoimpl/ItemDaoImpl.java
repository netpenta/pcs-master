package th.co.pentasol.pcs.master.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import th.co.pentasol.pcs.master.dao.ItemDao;
import th.co.pentasol.pcs.master.entity.ItemEntity;
import th.co.pentasol.pcs.master.entity.ItemLocationEntity;
import th.co.pentasol.pcs.master.entity.ItemProcessEntity;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.ItemLocationModel;
import th.co.pentasol.pcs.master.model.ItemModel;
import th.co.pentasol.pcs.master.model.ItemProcessModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.filter.ItemFilter;
import th.co.pentasol.pcs.master.model.filter.ItemLocationFilter;
import th.co.pentasol.pcs.master.model.filter.ItemProcessFilter;
import th.co.pentasol.pcs.master.util.MySqlUtil;
import th.co.pentasol.pcs.master.util.Util;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ItemDaoImpl implements ItemDao {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ItemDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private StringBuilder selectItemSql(boolean count, ItemFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT item.* ,\n");
        sql.append("       (SELECT user_fname FROM m_user WHERE user_name = item.user_id ORDER BY deleted_flg ASC LIMIT 1) as modified_by,\n");
        sql.append("       (SELECT unt_decimal FROM m_unit WHERE unt_code = item.s_order_unit_nm ORDER BY deleted_flg ASC LIMIT 1) as s_order_unit_decimal,\n");
        sql.append("       (SELECT unt_decimal FROM m_unit WHERE unt_code = item.struct_unit_nm  ORDER BY deleted_flg ASC LIMIT 1) as struct_unit_decimal,\n");
        sql.append("       (SELECT unt_decimal FROM m_unit WHERE unt_code = item.invt_unit_nm    ORDER BY deleted_flg ASC LIMIT 1) as invt_unit_decimal,\n");
        sql.append("       (SELECT unt_decimal FROM m_unit WHERE unt_code = item.stock_unit_nm   ORDER BY deleted_flg ASC LIMIT 1) as stock_unit_decimal,\n");
        sql.append("       (SELECT place_nm    FROM m_places  WHERE place_cd = item.stock_place_cd    AND effect_date <= CAST(DATE_FORMAT(item.created_datetime, '%Y%m%d') as Decimal) AND CAST(DATE_FORMAT(item.created_datetime, '%Y%m%d') as Decimal) <= exp_date     ORDER BY effect_date, deleted_flg  ASC LIMIT 1) as stock_place_nm,\n");
        sql.append("       (SELECT place_nm    FROM m_places  WHERE place_cd = item.shipment_place_cd AND effect_date <= CAST(DATE_FORMAT(item.created_datetime, '%Y%m%d') as Decimal) AND CAST(DATE_FORMAT(item.created_datetime, '%Y%m%d') as Decimal) <= exp_date     ORDER BY effect_date, deleted_flg  ASC LIMIT 1) as shipment_place_nm,\n");
        sql.append("       (SELECT fact_abbrnm FROM m_factory WHERE fact_cd  = item.pur_factory_cd    AND fact_effdate<= CAST(DATE_FORMAT(item.created_datetime, '%Y%m%d') as Decimal) AND CAST(DATE_FORMAT(item.created_datetime, '%Y%m%d') as Decimal) <= fact_expdate ORDER BY fact_effdate, deleted_flg ASC LIMIT 1) as fact_abbrnm,\n");
        sql.append("       (SELECT reference_nm FROM m_reference WHERE reference_id = (SELECT config_17 FROM pcs_config WHERE config_cd = 'REFERENCE' AND config_define_field = 0 AND deleted_flg = 0) AND reference_cd = item.inventory_group ORDER BY deleted_flg ASC LIMIT 1) as inventory_group_nm,\n");
        sql.append("       (SELECT reference_nm FROM m_reference WHERE reference_id = (SELECT config_19 FROM pcs_config WHERE config_cd = 'REFERENCE' AND config_define_field = 0 AND deleted_flg = 0) AND reference_cd = item.material_class  ORDER BY deleted_flg ASC LIMIT 1) as material_class_nm,\n");
        sql.append("       (SELECT reference_nm FROM m_reference WHERE reference_id = (SELECT config_8  FROM pcs_config WHERE config_cd = 'REFERENCE' AND config_define_field = 0 AND deleted_flg = 0) AND reference_cd = item.format_tag  ORDER BY deleted_flg ASC LIMIT 1) as format_tag_nm,\n");
        sql.append("       proc.proc_cd, proc.proc_nm, proc.proc_div, place.work_place_cd, locat.place_nm as work_place_nm, machn.machine_set_cd, machn.machine_set_nm,\n");
        sql.append("       ds.die_set_cd, item.issue_tag_flg as issued_tag_flg\n");
        sql.append("FROM m_items as item\n");
        sql.append("LEFT OUTER JOIN m_parts_struct as part  ON part.parent_item_cust_cd = item.item_cust_cd AND part.parent_item_cd = item.item_cd AND part.parent_item_rev_no = item.item_rev_no AND part.deleted_flg = 0 AND part.child_item_cust_cd = item.item_cust_cd AND part.child_item_cd = item.item_cd AND part.child_item_rev_no = item.item_rev_no\n");
        sql.append("LEFT OUTER JOIN m_item_procs   as procs ON procs.item_cust_cd       = item.item_cust_cd AND procs.item_cd       = item.item_cd AND procs.item_rev_no       = item.item_rev_no AND procs.sort_no    = 1 AND procs.deleted_flg = 0\n");
        sql.append("LEFT OUTER JOIN m_item_places  as place ON place.item_cust_cd       = item.item_cust_cd AND place.item_cd       = item.item_cd AND place.item_rev_no       = item.item_rev_no AND place.sort_no    = 1 AND place.deleted_flg = 0 AND place.item_proc_line_no = procs.line_no\n");
        sql.append("LEFT OUTER JOIN m_machine_set  as machn ON machn.machine_set_cd     = place.machine_set_cd AND machn.sort_no = 1 AND machn.deleted_flg = 0\n");
        sql.append("LEFT OUTER JOIN m_die_set      as ds    ON ds.die_set_cd            = place.die_set_cd     AND ds.sort_no    = 1 AND ds.deleted_flg    = 0\n");
        sql.append("LEFT OUTER JOIN m_procs        as proc  ON proc.proc_cd             = procs.proc_cd\n");
        sql.append("LEFT OUTER JOIN m_places       as locat ON locat.place_cd           = place.work_place_cd  AND locat.effect_date <= DATE_FORMAT(item.created_datetime, '%Y%m%d') AND DATE_FORMAT(item.created_datetime, '%Y%m%d') <= locat.exp_date AND locat.deleted_flg = 0\n");
        sql.append("WHERE item.deleted_flg = 0 AND item.item_rev_no NOT LIKE '%_MOVE%' ");
        if(!Objects.isNull(filter)){
            if(Util.isNotEmpty(filter.getSearch())) {
                /*
                * if for search non-specific value in condition ( LIKE % )
                * */
                sql.append("AND (");
                sql.append("item.item_cust_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("item.item_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("item.item_nm1 LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' ");
                sql.append(")");
            }else {
                /*
                * else for search specific value in condition (ex. findByOne)
                * */
                if (Util.isNotEmpty(filter.getItemCustCode())) {
                    sql.append("AND item.item_cust_cd = :itemCustCode ");
                }else{
                    sql.append("AND item.item_cust_cd = '' ");
                }
                if (Util.isNotEmpty(filter.getItemCode())) {
                    sql.append("AND item.item_cd = :itemCode ");
                }
                if (Util.isNotEmpty(filter.getItemRevNo())) {
                    sql.append("AND item.item_rev_no = :itemRevNo ");
                }else{
                    sql.append("AND item.item_rev_no = '0' ");
                }
                if (Util.isNotEmpty(filter.getItemName1())) {
                    sql.append("AND item.item_nm1 = :itemName1 ");
                }
                if (Util.isNotEmpty(filter.getFormatTag())) {
                    sql.append("AND item.format_tag = :formatTag ");
                }
                if (Util.isNotEmpty(filter.getPurFactoryCode())) {
                    sql.append("AND item.pur_factory_cd = :purFactoryCode ");
                }
                if (Util.isNotEmpty(filter.getModelName())) {
                    sql.append("AND item.model_nm = :modelName ");
                }
                if (Util.isNotEmpty(filter.getInventoryGroup())) {
                    sql.append("AND item.inventory_group = :inventory ");
                }
                if (Util.isNotEmpty(filter.getShipmentPlaceCode())) {
                    sql.append("AND item.shipment_place_cd = :shipmentPlaceCode ");
                }
                if (Util.isNotEmpty(filter.getStockPlaceCode())) {
                    sql.append("AND item.stock_place_cd = :stockPlaceCode ");
                }
                if (Util.isNotEmpty(filter.getProcCode())) {
                    sql.append("AND procs.proc_cd = :procCode ");
                }
                if (Util.isNotEmpty(filter.getWorkPlaceCode())) {
                    sql.append("AND places.work_place_cd = :machineSetCode ");
                }
                if (Util.isNotEmpty(filter.getMachineSetCode())) {
                    sql.append("AND places.machine_set_cd = :machineSetCode ");
                }

            }
            if(!count) sql.append(MySqlUtil.limit("", filter.getPageNo(), filter.getPageSize()));
        }
        return sql;
    }
    private StringBuilder selectItemProcessSql(boolean count, ItemProcessFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT procs.*, (SELECT proc_nm FROM m_procs WHERE proc_cd = procs.proc_cd ORDER BY deleted_flg ASC LIMIT 1) as proc_nm\n");
        sql.append("FROM   m_item_procs as procs\n");
        sql.append("WHERE  procs.deleted_flg = 0 AND procs.sort_no = 1 AND procs.item_rev_no NOT LIKE '%_MOVE%' ");
        if(!Objects.isNull(filter)){
            if(Util.isNotEmpty(filter.getSearch())) {
                /*
                 * if for search non-specific value in condition ( LIKE % )
                 * */
                sql.append("AND (");
                sql.append("procs.item_cust_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("procs.item_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append(")");
            }else {
                /*
                 * else for search specific value in condition (ex. findByOne)
                 * */
                if (Util.isNotEmpty(filter.getItemCustCode())) {
                    sql.append("AND procs.item_cust_cd = :itemCustCode ");
                }else{
                    sql.append("AND procs.item_cust_cd = '' ");
                }
                if (Util.isNotEmpty(filter.getItemCode())) {
                    sql.append("AND procs.item_cd = :itemCode ");
                }
                if (Util.isNotEmpty(filter.getItemRevNo())) {
                    sql.append("AND procs.item_rev_no = :itemRevNo ");
                }else{
                    sql.append("AND procs.item_rev_no = '' ");
                }
                if (Util.isNotEmpty(filter.getLineNo().toString())) {
                    sql.append("AND procs.line_no = :lineNo ");
                }
            }
            if(!count) sql.append(MySqlUtil.limit("", filter.getPageNo(), filter.getPageSize()));
        }
        return sql;
    }
    private StringBuilder selectItemLocationSql(boolean count, ItemLocationFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT places.*, ms.machine_group_div, ms.machine_set_nm, ds.die_group_div, ds.die_set_nm,\n");
        sql.append("       (SELECT place_nm FROM m_places WHERE place_cd = places.work_place_cd AND effect_date <= CAST(DATE_FORMAT(places.created_datetime, '%Y%m%d') as Decimal) AND CAST(DATE_FORMAT(places.created_datetime, '%Y%m%d') as Decimal) <= exp_date ORDER BY effect_date, deleted_flg ASC LIMIT 1) as work_place_nm,\n");
        sql.append("       (SELECT reference_nm FROM m_reference WHERE reference_id = (SELECT config_5  FROM pcs_config WHERE config_cd = 'REFERENCE' AND config_define_field = 0 AND deleted_flg = 0) AND reference_cd = ms.machine_group_div ORDER BY deleted_flg ASC LIMIT 1) as machine_group_nm,\n");
        sql.append("       (SELECT reference_nm FROM m_reference WHERE reference_id = (SELECT config_16 FROM pcs_config WHERE config_cd = 'REFERENCE' AND config_define_field = 0 AND deleted_flg = 0) AND reference_cd = ds.die_group_div     ORDER BY deleted_flg ASC LIMIT 1) as die_group_nm\n");
        sql.append("FROM   m_item_places as places\n");
        sql.append("LEFT OUTER JOIN m_machine_set as ms ON ms.machine_set_cd = places.machine_set_cd AND ms.sort_no = 1 AND ms.deleted_flg = 0\n");
        sql.append("LEFT OUTER JOIN m_die_set     as ds ON ds.die_set_cd     = places.die_set_cd     AND ds.sort_no = 1 AND ds.deleted_flg = 0\n");
        sql.append("WHERE  places.deleted_flg = 0 AND places.sort_no = 1 AND places.item_rev_no NOT LIKE '%_MOVE%' ");
        if(!Objects.isNull(filter)){
            if(Util.isNotEmpty(filter.getSearch())) {
                /*
                 * if for search non-specific value in condition ( LIKE % )
                 * */
                sql.append("AND (");
                sql.append("places.item_cust_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("places.item_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append(")");
            }else {
                /*
                 * else for search specific value in condition (ex. findByOne)
                 * */
                if (Util.isNotEmpty(filter.getItemCustCode())) {
                    sql.append("AND places.item_cust_cd = :itemCustCode ");
                }else{
                    sql.append("AND places.item_cust_cd = '' ");
                }
                if (Util.isNotEmpty(filter.getItemCode())) {
                    sql.append("AND places.item_cd = :itemCode ");
                }
                if (Util.isNotEmpty(filter.getItemRevNo())) {
                    sql.append("AND places.item_rev_no = :itemRevNo ");
                }else{
                    sql.append("AND places.item_rev_no = '' ");
                }
                if (Util.isNotEmpty(filter.getItemProcLineNo().toString())) {
                    sql.append("AND places.item_proc_line_no = :item_proc_line_no ");
                }
                if (Util.isNotEmpty(filter.getLineNo().toString())) {
                    sql.append("AND places.line_no = :lineNo ");
                }
            }
            if(!count) sql.append(MySqlUtil.limit("", filter.getPageNo(), filter.getPageSize()));
        }
        return sql;
    }
    @Override
    public ItemEntity findOneItemByCondition(String itemCustCode, String itemCode, String itemRevNo){
        StringBuilder sql = selectItemSql(false, null);

        if(Util.isNotEmpty(itemCustCode)){
            sql.append("AND item.item_cust_cd = '"+ itemCustCode +"' ");
        }else {
            sql.append("AND item.item_cust_cd = '' ");
        }
        sql.append("AND item.item_cd = '" + itemCode + "' AND item.item_rev_no = '" + itemRevNo + "' ");

        try{
            return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(ItemEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }
    @Override
    public ItemProcessEntity findOneItemProcessByCondition(String itemCustCode, String itemCode, String itemRevNo){
        StringBuilder sql = selectItemProcessSql(false, null);
        if(Util.isNotEmpty(itemCustCode)){
            sql.append("AND procs.item_cust_cd = '"+ itemCustCode +"' ");
        }else {
            sql.append("AND procs.item_cust_cd = '' ");
        }
        sql.append("AND procs.item_cd = '" + itemCode + "' AND procs.item_rev_no = '" + itemRevNo + "' ");
        try{
            return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(ItemProcessEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }
    @Override
    public ItemLocationEntity findOneItemLocationByCondition(String itemCustCode, String itemCode, String itemRevNo){
        StringBuilder sql = selectItemLocationSql(false, null);

        if(Util.isNotEmpty(itemCustCode)){
            sql.append("AND places.item_cust_cd = '"+ itemCustCode +"' ");
        }else {
            sql.append("AND places.item_cust_cd = '' ");
        }
        sql.append("AND places.item_cd = '" + itemCode + "' AND places.item_rev_no = '" + itemRevNo + "' ");

        try{
            return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(ItemLocationEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }
    @Override
    public Long rowCountByCondition(ItemFilter filter){
        String sql = MySqlUtil.rowCountSql(selectItemSql(true, filter)).toString();
        return namedParameterJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(filter), Long.class);
    }
    @Override
    public Long rowCountDataDeleted(ItemFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT * FROM m_items as item\n");
        sql.append("WHERE item.deleted_flg = 1\n ");
        sql.append("AND item.item_cust_cd = :itemCustCode\n");
        sql.append("AND item.item_cd = :itemCode\n");
        sql.append("AND item.item_rev_no = :itemRevNo ");
        String rowCountSql = MySqlUtil.rowCountSql(sql).toString();
        return namedParameterJdbcTemplate.queryForObject(rowCountSql, new BeanPropertySqlParameterSource(filter), Long.class);
    }

    /* CHECK RESTORE
    {
      "search": "",
      "pageNo": 0,
      "pageSize": 0,
      "sortBy": "string",
      "sortByList": [
        "string"
      ],
      "sortOrder": "string",
      "itemCustCode": "",
      "itemCode": "NI",
      "itemRevNo": "0",
      "itemName1": "",
      "formatTage": "",
      "purFactoryCode": "",
      "modelName": "",
      "inventoryGroup": "",
      "shipmentPlaceCode": "",
      "stockPlaceCode": "",
      "procCode": "",
      "workPlaceCode": "",
      "machineSetCode": ""
    }
    */

    @Override
    public List<ItemEntity> findAllByCondition(ItemFilter filter){
        String sql = selectItemSql(false, filter).toString();
        try{
            return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(filter), new BeanPropertyRowMapper<>(ItemEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    /* CHECK INSERT AND UPDATE
    {
      "itemCustCode": "",
      "itemCode": "NNNN",
      "itemRevNo": "0",
      "itemDiv": 0,
      "itemName1": "NNNN NAME 1",
      "itemName2": "",
      "stockUnitName": "",
      "makerName": "",
      "modelName": "",
      "typeName": "",
      "itemGroup1Div": "",
      "itemGroup2Div": "",
      "itemGroup3Div": "",
      "itemGroup4Div": "",
      "itemGroup5Div": "",
      "inventoryGroup": "",
      "lr": "",
      "coatingFlg": 0,
      "coatingType": "",
      "weight": 0,
      "stdUnitCost": 0,
      "millMakerName": "",
      "minProdQtty": 0,
      "safetyStock": 0,
      "breakTestDiv": 0,
      "breakTestCnt": 0,
      "safetyLeadTime": 0,
      "materialSize": "",
      "materialSpec": "",
      "materialClass": "",
      "stockPlaceCode": "",
      "purFactoryCode": "",
      "shelfNo": "",
      "shelfNoDetail": "",
      "struckUnitName": "",
      "struckPacking": "",
      "struckStockUnitInputQtty": 0,
      "structUnitInputQtty": 0,
      "structRate": 0,
      "invtUnitName": "",
      "invtPacking": "",
      "invtStockUnitInputQtty": 0,
      "invtUnitInputQtty": 0,
      "invtRate": 0,
      "itemPhotoFileName": "",
      "itemRemark": "",
      "shipmentPlaceCode": "",
      "spm": 0,
      "deletedFlg": 0,
      "userId": "",
      "programId": "",
      "createdDatetime": "",
      "modifiedDatetime": "",
      "accItemCode": "",
      "safetyLeadTimeSec": 0,
      "cavity": 0,
      "rubberWeight": 0,
      "rubberPerShot": 0,
      "shotPerShift": 0,
      "lowStockLimit": 0,
      "maxStockLimit": 0,
      "formatTag": "",
      "expCheckDate": 0,
      "weightUnitName": "",
      "issueTagFlg": 0,
      "specialTagFlg": 0,
      "deliveryLeadTime": 0,
      "stockCardFlg": 0,
      "snpPacking": 0,
      "drawingNo": "",
      "sizeMPerMm": 0,
      "localSalesAccCode": "",
      "exportSalesAccCode": "",
      "purAccCode": "",
      "debitCustAccCode": "",
      "creditLocalAccCode": "",
      "creditExportAccCode": "",
      "exportItemFlg": 0,
      "inventoryGroupName": "",
      "materialClassName": "",
      "stockPlaceName": "",
      "factAbbrnm": "",
      "structUnitName": "",
      "structPacking": "",
      "structStockUnitInputQtty": 0,
      "shipmentPlaceName": "",
      "notInPart": true,
      "prodSnp": 0,
      "procCode": "",
      "procName": "",
      "placeCode": "",
      "placeName": "",
      "stockUnitDecimal": 0,
      "invtUnitDecimal": 0,
      "structUnitDecimal": 0,
      "modifiedBy": "",
      "workPlaceCode": "",
      "workPlaceName": "",
      "machineSetCode": "",
      "dieSetCode": "",
      "dieSetName": "",
      "formatTagName": "",
      "expCheck": true,
      "specialTag": true,
      "issuedTagFlg": 0,
      "issuedTag": true,
      "procCodeSelected": [
        ""
      ],
      "machineSetCodeSelected": [
        ""
      ],
      "effectDate": 0,
      "stockCard": true,
      "reqQtty": 0,
      "itemCodeSelected": [
        ""
      ],
      "purResultTagQtty": 0,
      "process": {
        "itemCustCode": "",
        "itemCode": "NNNN",
        "itemRevNo": "0",
        "lineNo": 0,
        "sortNo": 0,
        "procCode": "PURCHASE",
        "procName": "NASASD",
        "procDiv": 0,
        "materialCode": "",
        "thickness": 99,
        "width": 0,
        "pitch": 0,
        "weight": 0,
        "packing": "",
        "prodSnp": 0,
        "roundQtty": 0,
        "workLoad": 0,
        "procCost": 0,
        "deletedFlg": 0,
        "userId": "",
        "programId": "",
        "planPriorityCode": "",
        "oneShiftOneItemFlg": 0,
        "porderPoInteger": 0,
        "porderPoint": 0,
        "porderDiv": 0
      },
      "location": {
        "itemCustCode": "",
        "itemCode": "NNNN",
        "itemRevNo": "0",
        "itemProcLineNo": 0,
        "lineNo": 0,
        "sortNo": 0,
        "workPlaceCode": "TEST LOCATION CODE",
        "workPlaceName": "TEST LOCATION NAME",
        "workloadGroupCode": "",
        "workloadGroupBrNo": 0,
        "fixPlanGroupCode": "",
        "machineGroupDiv": "",
        "machineGroupName": "",
        "machineSetCode": "",
        "machineSetName": "",
        "dieGroupDiv": "",
        "dieGroupName": "",
        "dieSetCode": "",
        "dieSetName": "",
        "dieJigSetCode": "",
        "materialSupplyDiv": 0,
        "yieldRate": 12,
        "stdWaitTime": 0,
        "stdPreparationTime": 0,
        "stdWorkTime": 0,
        "noOfCavity": 0,
        "deletedFlg": 0,
        "userId": "",
        "programId": "",
        "procDiv": 0,
        "factoryCode": "",
        "factAbbrnm": "",
        "useStockPlaceDiv": 0,
        "workDivideFlg": 0,
        "preparationDivideFlg": 0,
        "workPreparationDivideFlg": 0,
        "holidayWorkTime": 0,
        "workEndDate": 0,
        "resultWorkEndTime": "",
        "preWorkEndDate": 0,
        "resultPreWorkEndTime": "",
        "workplacecdList": [
          ""
        ],
        "porderLeadTime": 0
      },
      "sstockUnitInputQtty": 0,
      "sorderUnitInputQtty": 0,
      "sorderUnitDecimal": 0,
      "sorderUnitName": "",
      "sorderRate": 0,
      "sorderPacking": "",
      "porderUnitPrice": 0
    }
     */
    private String insert_m_items(){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("INSERT m_items SET ");
        sql.append("acc_item_cd = :accItemCode, ");
        sql.append("item_cust_cd = :itemCustCode, ");
        sql.append("item_cd = :itemCode, ");
        sql.append("item_rev_no = :itemRevNo, ");
        sql.append("item_div = :itemDiv, ");
        sql.append("item_nm1 = :itemName1, ");
        sql.append("item_nm2 = :itemName2, ");
        sql.append("stock_unit_nm = :stockUnitName, ");
        sql.append("maker_nm = :makerName, ");
        sql.append("model_nm = :modelName, ");
        sql.append("type_nm = :typeName, ");
        sql.append("item_group_1_div = :itemGroup1Div, ");
        sql.append("item_group_2_div = :itemGroup2Div, ");
        sql.append("item_group_3_div = :itemGroup3Div, ");
        sql.append("item_group_4_div = :itemGroup4Div, ");
        sql.append("item_group_5_div = :itemGroup5Div, ");
        sql.append("inventory_group = :inventoryGroup, ");
        sql.append("lr = :lr, ");
        sql.append("weight = :weight, ");
        sql.append("std_unit_cost = :stdUnitCost, ");
        sql.append("mill_maker_nm = :millMakerName, ");
        sql.append("min_prod_qtty = IFNULL(:minProdQtty, 0), ");
        sql.append("safety_stock = IFNULL(:safetyStock, 0), ");
        sql.append("break_test_div = :breakTestDiv, ");
        sql.append("break_test_cnt = :breakTestCnt, ");
        sql.append("safety_lead_time = IFNULL(:safetyLeadTime, 0), ");
        sql.append("material_size = :materialSize, ");
        sql.append("material_spec = :materialSpec, ");
        sql.append("material_class = :materialClass, ");
        sql.append("stock_place_cd = :stockPlaceCode, ");
        sql.append("pur_factory_cd = :purFactoryCode, ");
        sql.append("shelf_no = :shelfNo, ");
        sql.append("shelf_no_detail = :shelfNoDetail, ");
        sql.append("s_order_unit_nm = :sOrderUnitName, ");
        sql.append("s_order_packing = :sOrderPacking, ");
        sql.append("s_stock_unit_input_qtty = :sStockUnitInputQtty, ");
        sql.append("s_order_unit_input_qtty = :sOrderUnitInputQtty, ");
        sql.append("s_order_rate = :sOrderRate, ");
        sql.append("struct_unit_nm = :structUnitName, ");
        sql.append("struct_packing = :structPacking, ");
        sql.append("struct_stock_unit_input_qtty = :structStockUnitInputQtty, ");
        sql.append("struct_unit_input_qtty = :structUnitInputQtty, ");
        sql.append("struct_rate = :structRate, ");
        sql.append("invt_unit_nm = :invtUnitName, ");
        sql.append("invt_stock_unit_input_qtty = :invtStockUnitInputQtty, ");
        sql.append("invt_unit_input_qtty = :invtUnitInputQtty, ");
        sql.append("invt_packing = :invtPacking, ");
        sql.append("invt_rate = :invtRate, ");
        sql.append("item_photo_file_nm = :itemPhotoFileName, ");
        sql.append("item_remark = :itemRemark, ");
        sql.append("shipment_place_cd = :shipmentPlaceCode, ");
        sql.append("coating_flg = :coatingFlg, ");
        sql.append("coating_type = :coatingType, ");
        sql.append("cavity = :cavity, ");
        sql.append("rubber_weight = :rubberWeight, ");
        sql.append("rubber_per_shot = :rubberPerShot, ");
        sql.append("shot_per_shift = :shotPerShift, ");
        sql.append("low_stock_limit = IFNULL(:lowStockLimit, 0), ");
        sql.append("max_stock_limit = IFNULL(:maxStockLimit, 0), ");
        sql.append("format_tag = :formatTag, ");
        sql.append("exp_check_date = :expCheckDate, ");
        sql.append("weight_unit_nm = :weightUnitName, ");
        sql.append("issue_tag_flg = :issuedTagFlg, ");
        sql.append("special_tag_flg = :specialTagFlg, ");
        sql.append("delivery_lead_time = :deliveryLeadTime, ");
        sql.append("stock_card_flg = :stockCardFlg, ");
        sql.append("snp_packing = :snpPacking, ");
        sql.append("drawing_no = :drawingNo, ");
        sql.append("size_m_per_mm = :sizeMPerMm, ");
        sql.append("local_sales_acc_cd = :localSalesAccCode, ");
        sql.append("export_sales_acc_cd = :exportSalesAccCode, ");
        sql.append("pur_acc_cd = :purAccCode, ");
//        sql.append("debit_cust_acc_cd = :debitCustAccCode, ");
//        sql.append("credit_local_acc_cd = :creditLocalAccCode, ");
//        sql.append("credit_export_acc_cd = :creditExportAccCode, ");
        sql.append("deleted_flg = :deletedFlg, ");
        sql.append("user_id = :userId, ");
        sql.append("program_id = :programId, ");
        sql.append("created_datetime = NOW();");
        return sql.toString();
    }
    public String insert_m_item_procs(){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("INSERT m_item_procs SET ");
        sql.append("item_cust_cd = :itemCustCode, ");
        sql.append("item_cd = :itemCode, ");
        sql.append("item_rev_no = :itemRevNo, ");
        sql.append("line_no = 1,");
        sql.append("sort_no = 1,");
        sql.append("proc_cd = :procCode, ");
        sql.append("proc_div = :procDiv,");
        sql.append("material_cd = :materialCode,");
        sql.append("thickness = :thickness,");
        sql.append("width = :width,");
        sql.append("pitch = :pitch,");
        sql.append("weight = :weight,");
        sql.append("packing= :packing,");
        sql.append("prod_snp = IFNULL(:prodSnp, 0),");
        sql.append("round_qtty = IFNULL(:roundQtty, 0),");
        sql.append("p_order_div = IFNULL(:pOrderDiv, 2),");
        sql.append("p_order_point = IFNULL(:pOrderPoint, 0),");
        sql.append("work_load = IFNULL(:workLoad, 0),");
        sql.append("proc_cost = :procCost,");
		sql.append("plan_priority_cd = :planPriorityCode,");
		sql.append("one_shift_one_item_flg = :oneShiftOneItemFlg,");
        sql.append("deleted_flg = 0, ");
        sql.append("program_id = :programId, ");
        sql.append("user_id = :userId, ");
        sql.append("created_datetime  = NOW() ");
        sql.append("ON DUPLICATE KEY UPDATE ");
        sql.append("proc_cd = :procCode, ");
        sql.append("proc_div = :procDiv,");
        sql.append("material_cd = :materialCode,");
        sql.append("thickness = :thickness,");
        sql.append("width = :width,");
        sql.append("pitch = :pitch,");
        sql.append("weight = :weight,");
        sql.append("packing= :packing,");
        sql.append("prod_snp = IFNULL(:prodSnp, 0),");
        sql.append("round_qtty = IFNULL(:roundQtty, 0),");
        sql.append("p_order_div = IFNULL(:pOrderDiv, 2),");
        sql.append("p_order_point = IFNULL(:pOrderPoint, 0),");
        sql.append("work_load = IFNULL(:workLoad, 0),");
        sql.append("proc_cost = :procCost,");
        sql.append("deleted_flg = 0, ");
        sql.append("user_id = :userId, ");
        sql.append("program_id = :programId, ");
        sql.append("modified_datetime  = NOW();");
        return sql.toString();
    }
    public String insert_m_item_places(){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("INSERT m_item_places SET ");
        sql.append("item_cust_cd = :itemCustCode, ");
        sql.append("item_cd = :itemCode, ");
        sql.append("item_rev_no = :itemRevNo, ");
        sql.append("item_proc_line_no = 1, ");
        sql.append("line_no = 1, ");
        sql.append("sort_no = 1, ");
        sql.append("work_place_cd = :workPlaceCode, ");
        sql.append("workload_group_cd = 1, ");
        sql.append("workload_group_br_no = 1, ");
        sql.append("fix_plan_group_cd = 1, ");
        sql.append("machine_set_cd = :machineSetCode, ");
        sql.append("die_set_cd = :dieSetCode, ");
        sql.append("yield_rate = (CASE WHEN :yieldRate IS NULL OR :yieldRate = 0 THEN 100 ELSE :yieldRate END), ");
        sql.append("std_wait_time = IFNULL(:stdWaitTime, 0), ");
        sql.append("std_preparation_time = IFNULL(:stdPreparationTime, 0), ");
        sql.append("std_work_time = IFNULL(:stdWorkTime, 0), ");
        sql.append("no_of_cavity = IFNULL(:noOfCavity,0) , ");
        sql.append("use_stock_place_div = :useStockPlaceDiv, ");
        sql.append("deleted_flg = 0, ");
        sql.append("program_id = :programId, ");
        sql.append("user_id = :userId, ");
        sql.append("created_datetime  = NOW() ");
        sql.append("ON DUPLICATE KEY UPDATE ");
        sql.append("work_place_cd = :workPlaceCode, ");
        sql.append("workload_group_cd = 1, ");
        sql.append("workload_group_br_no = 1, ");
        sql.append("fix_plan_group_cd = 1, ");
        sql.append("machine_set_cd = :machineSetCode, ");
        sql.append("die_set_cd = :dieSetCode, ");
        sql.append("yield_rate = (CASE WHEN :yieldRate IS NULL OR :yieldRate = 0 THEN 100 ELSE :yieldRate END), ");
        sql.append("std_wait_time = IFNULL(:stdWaitTime, 0), ");
        sql.append("std_preparation_time = IFNULL(:stdPreparationTime, 0), ");
        sql.append("std_work_time = IFNULL(:stdWorkTime, 0), ");
        sql.append("no_of_cavity = IFNULL(:noOfCavity,0) , ");
        sql.append("use_stock_place_div = :useStockPlaceDiv, ");
        sql.append("deleted_flg = 0, ");
        sql.append("user_id = :userId, ");
        sql.append("program_id = :programId, ");
        sql.append("modified_datetime  = NOW();");
        return sql.toString();
    }
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Map<String, Integer> save(ItemModel item, ItemProcessModel proc, ItemLocationModel place){
        Map<String, Integer> results = new HashMap<>();
        Integer result_save_m_items = namedParameterJdbcTemplate.update(insert_m_items(), new BeanPropertySqlParameterSource(item));
        Integer result_save_m_procs = namedParameterJdbcTemplate.update(insert_m_item_procs(), new BeanPropertySqlParameterSource(proc));
        Integer result_save_m_places = namedParameterJdbcTemplate.update(insert_m_item_places(), new BeanPropertySqlParameterSource(place));
        results.put("result_save_m_items", result_save_m_items);
        results.put("result_save_m_procs", result_save_m_procs);
        results.put("result_save_m_places", result_save_m_places);
        return results;
    }
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Map<String, Integer> delete(ItemModel data){
        Map<String, Integer> results = new HashMap<>();
        String delete_m_items      = "UPDATE m_items       SET deleted_flg = 1, modified_datetime = NOW(), program_id = :programId, user_id = :userId WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;";
        String delete_m_item_procs = "UPDATE m_item_procs  SET deleted_flg = 1, modified_datetime = NOW(), program_id = :programId, user_id = :userId WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;";
        String delete_m_item_places= "UPDATE m_item_places SET deleted_flg = 1, modified_datetime = NOW(), program_id = :programId, user_id = :userId WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;";
        Integer result_delete_m_items = namedParameterJdbcTemplate.update(delete_m_items, new BeanPropertySqlParameterSource(data));
        Integer result_delete_m_procs = namedParameterJdbcTemplate.update(delete_m_item_procs, new BeanPropertySqlParameterSource(data));
        Integer result_delete_m_places = namedParameterJdbcTemplate.update(delete_m_item_places, new BeanPropertySqlParameterSource(data));
        results.put("result_delete_m_items", result_delete_m_items);
        results.put("result_delete_m_procs", result_delete_m_procs);
        results.put("result_delete_m_places", result_delete_m_places);
        return  results;
    }
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Map<String, Integer> restore(ItemModel data){
        Map<String, Integer> results = new HashMap<>();
        String restore_m_items      = "UPDATE m_items       SET deleted_flg = 0, modified_datetime = NOW(), program_id = :programId, user_id = :userId WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;";
        String restore_m_item_procs = "UPDATE m_item_procs  SET deleted_flg = 0, modified_datetime = NOW(), program_id = :programId, user_id = :userId WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;";
        String restore_m_item_places= "UPDATE m_item_places SET deleted_flg = 0, modified_datetime = NOW(), program_id = :programId, user_id = :userId WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;";
        Integer result_restore_m_items = namedParameterJdbcTemplate.update(restore_m_items, new BeanPropertySqlParameterSource(data));
        Integer result_restore_m_item_procs = namedParameterJdbcTemplate.update(restore_m_item_procs, new BeanPropertySqlParameterSource(data));
        Integer result_restore_m_item_places = namedParameterJdbcTemplate.update(restore_m_item_places, new BeanPropertySqlParameterSource(data));
        results.put("restore_m_items", result_restore_m_items);
        results.put("restore_m_item_procs", result_restore_m_item_procs);
        results.put("restore_m_item_places", result_restore_m_item_places);
        return results;
    }
    private String update_m_items(){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("UPDATE m_items SET ");
        sql.append("acc_item_cd = :accItemCode, ");
        sql.append("item_cust_cd = :itemCustCode, ");
        sql.append("item_cd = :itemCode, ");
        sql.append("item_rev_no = :itemRevNo, ");
        sql.append("item_div = :itemDiv, ");
        sql.append("item_nm1 = :itemName1, ");
        sql.append("item_nm2 = :itemName2, ");
        sql.append("stock_unit_nm = :stockUnitName, ");
        sql.append("maker_nm = :makerName, ");
        sql.append("model_nm = :modelName, ");
        sql.append("type_nm = :typeName, ");
        sql.append("item_group_1_div = :itemGroup1Div, ");
        sql.append("item_group_2_div = :itemGroup2Div, ");
        sql.append("item_group_3_div = :itemGroup3Div, ");
        sql.append("item_group_4_div = :itemGroup4Div, ");
        sql.append("item_group_5_div = :itemGroup5Div, ");
        sql.append("inventory_group = :inventoryGroup, ");
        sql.append("lr = :lr, ");
        sql.append("weight = :weight, ");
        sql.append("std_unit_cost = :stdUnitCost, ");
        sql.append("mill_maker_nm = :millMakerName, ");
        sql.append("min_prod_qtty = IFNULL(:minProdQtty, 0), ");
        sql.append("safety_stock = IFNULL(:safetyStock, 0), ");
        sql.append("break_test_div = :breakTestDiv, ");
        sql.append("break_test_cnt = :breakTestCnt, ");
        sql.append("safety_lead_time = IFNULL(:safetyLeadTime, 0), ");
        sql.append("material_size = :materialSize, ");
        sql.append("material_spec = :materialSpec, ");
        sql.append("material_class = :materialClass, ");
        sql.append("stock_place_cd = :stockPlaceCode, ");
        sql.append("pur_factory_cd = :purFactoryCode, ");
        sql.append("shelf_no = :shelfNo, ");
        sql.append("shelf_no_detail = :shelfNoDetail, ");
        sql.append("s_order_unit_nm = :sOrderUnitName, ");
        sql.append("s_order_packing = :sOrderPacking, ");
        sql.append("s_stock_unit_input_qtty = :sStockUnitInputQtty, ");
        sql.append("s_order_unit_input_qtty = :sOrderUnitInputQtty, ");
        sql.append("s_order_rate = :sOrderRate, ");
        sql.append("struct_unit_nm = :structUnitName, ");
        sql.append("struct_packing = :structPacking, ");
        sql.append("struct_stock_unit_input_qtty = :structStockUnitInputQtty, ");
        sql.append("struct_unit_input_qtty = :structUnitInputQtty, ");
        sql.append("struct_rate = :structRate, ");
        sql.append("invt_unit_nm = :invtUnitName, ");
        sql.append("invt_stock_unit_input_qtty = :invtStockUnitInputQtty, ");
        sql.append("invt_unit_input_qtty = :invtUnitInputQtty, ");
        sql.append("invt_packing = :invtPacking, ");
        sql.append("invt_rate = :invtRate, ");
        sql.append("item_photo_file_nm = :itemPhotoFileName, ");
        sql.append("item_remark = :itemRemark, ");
        sql.append("shipment_place_cd = :shipmentPlaceCode, ");
        sql.append("coating_flg = :coatingFlg, ");
        sql.append("coating_type = :coatingType, ");
        sql.append("cavity = :cavity, ");
        sql.append("rubber_weight = :rubberWeight, ");
        sql.append("rubber_per_shot = :rubberPerShot, ");
        sql.append("shot_per_shift = :shotPerShift, ");
        sql.append("low_stock_limit = IFNULL(:lowStockLimit, 0), ");
        sql.append("max_stock_limit = IFNULL(:maxStockLimit, 0), ");
        sql.append("format_tag = :formatTag, ");
        sql.append("exp_check_date = :expCheckDate, ");
        sql.append("weight_unit_nm = :weightUnitName, ");
        sql.append("issue_tag_flg = :issuedTagFlg, ");
        sql.append("special_tag_flg = :specialTagFlg, ");
        sql.append("delivery_lead_time = :deliveryLeadTime, ");
        sql.append("stock_card_flg = :stockCardFlg, ");
        sql.append("snp_packing = :snpPacking, ");
        sql.append("drawing_no = :drawingNo, ");
        sql.append("size_m_per_mm = :sizeMPerMm, ");
        sql.append("local_sales_acc_cd = :localSalesAccCode, ");
        sql.append("export_sales_acc_cd = :exportSalesAccCode, ");
        sql.append("pur_acc_cd = :purAccCode, ");
//        sql.append("debit_cust_acc_cd = :debitCustAccCode, ");
//        sql.append("credit_local_acc_cd = :creditLocalAccCode, ");
//        sql.append("credit_export_acc_cd = :creditExportAccCode, ");
        sql.append("deleted_flg = 0, ");
        sql.append("user_id = :userId, ");
        sql.append("program_id = :programId, ");
        sql.append("modified_datetime = NOW() ");
        sql.append("WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo; ");
        return sql.toString();
    }
    private String update_purchase_price(){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE m_purchase_prices SET ");
        sql.append("p_order_unit_input_qtty = :sOrderUnitInputQtty, ");
        sql.append("p_order_stock_unit_input_qtty = :sStockUnitInputQtty, ");
        sql.append("p_order_rate = :s_orderRate, ");
        sql.append("p_order_unit_nm = :sOrderUnitName, ");
        sql.append("p_order_packing = :sOrderPacking ");
        sql.append("WHERE item_cust_cd = :itemCustCode AND item_cd = :itemCode AND item_rev_no = :itemRevNo;");
        return sql.toString();
    }
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Map<String, Integer> update(ItemModel data, UserInfo userInfo){
        Map<String, Integer> results = new HashMap<>();
        Integer result_update_m_items = namedParameterJdbcTemplate.update(update_m_items(), new BeanPropertySqlParameterSource(data));
        Integer result_update_m_procs = namedParameterJdbcTemplate.update(insert_m_item_procs(), new BeanPropertySqlParameterSource(data.getProcess()));
        Integer result_update_m_places = namedParameterJdbcTemplate.update(insert_m_item_places(), new BeanPropertySqlParameterSource(data.getLocation()));
//        Integer result_update_m_purchase_price = namedParameterJdbcTemplate.update(update_purchase_price(), new BeanPropertySqlParameterSource(data));
        results.put("result_update_m_items", result_update_m_items);
        results.put("result_update_m_procs", result_update_m_procs);
        results.put("result_update_m_places", result_update_m_places);
//        results.put("result_update_m_purchase_price", result_update_m_purchase_price);
        return results;
    }
}
