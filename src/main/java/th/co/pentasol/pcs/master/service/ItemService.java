package th.co.pentasol.pcs.master.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.configuration.PcsConfig;
import th.co.pentasol.pcs.master.dao.ItemDao;
import th.co.pentasol.pcs.master.entity.ItemEntity;
import th.co.pentasol.pcs.master.entity.ItemLocationEntity;
import th.co.pentasol.pcs.master.entity.ItemProcessEntity;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.*;
import th.co.pentasol.pcs.master.model.api.ApiMessage;
import th.co.pentasol.pcs.master.model.filter.ItemFilter;
import th.co.pentasol.pcs.master.util.DateTimeUtil;
import th.co.pentasol.pcs.master.util.MySqlUtil;
import th.co.pentasol.pcs.master.util.NumberUtil;
import th.co.pentasol.pcs.master.util.Util;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItemService {
    @Autowired
    PcsConfig config;
    @Autowired
    Message message;
    @Autowired
    ItemDao itemDao;

    public ItemService(Message message, ItemDao itemDao) throws ServiceException {
        this.message = message;
        this.itemDao = itemDao;
    }
    /* Model Use For Return To JSF Detail page */
    private ItemModel convertItemEntityToModel(ItemEntity entity, UserInfo userInfo){
        ItemModel itemModel = new ItemModel();
        /**/
        itemModel.setItemCustCode(entity.getItem_cust_cd());
        itemModel.setItemCode(entity.getItem_cd());
        itemModel.setItemRevNo(entity.getItem_rev_no());
        itemModel.setItemName1(entity.getItem_nm1());
        itemModel.setItemName2(entity.getItem_nm2());
        itemModel.setItemDiv(entity.getItem_div());
        itemModel.setStockUnitName(entity.getStock_unit_nm());
        itemModel.setMakerName(entity.getMaker_nm());
        itemModel.setModelName(entity.getModel_nm());
        itemModel.setTypeName(entity.getType_nm());
        itemModel.setItemGroup1Div(entity.getItem_group_1_div());
        itemModel.setItemGroup2Div(entity.getItem_group_2_div());
        itemModel.setItemGroup3Div(entity.getItem_group_3_div());
        itemModel.setItemGroup4Div(entity.getItem_group_4_div());
        itemModel.setItemGroup5Div(entity.getItem_group_5_div());
        itemModel.setInventoryGroup(entity.getInventory_group());
        itemModel.setLr(entity.getLr());
        itemModel.setCoatingFlg(entity.getCoating_flg());
        itemModel.setCoatingType(entity.getCoating_type());
        itemModel.setWeight(entity.getWeight());
        itemModel.setStdUnitCost(entity.getStd_unit_cost());
        itemModel.setMillMakerName(entity.getMill_maker_nm());
        itemModel.setMinProdQtty(entity.getMin_prod_qtty());
        itemModel.setSafetyStock(entity.getSafety_stock());
        itemModel.setBreakTestDiv(entity.getBreak_test_div());
        itemModel.setBreakTestCnt(entity.getBreak_test_cnt());
        itemModel.setSafetyLeadTime(entity.getSafety_lead_time());
        itemModel.setMaterialSize(entity.getMaterial_size());
        itemModel.setMaterialSpec(entity.getMaterial_spec());
        itemModel.setMaterialClass(entity.getMaterial_class());
        itemModel.setStockPlaceCode(entity.getStock_place_cd());
        itemModel.setPurFactoryCode(entity.getPur_factory_cd());
        itemModel.setShelfNo(entity.getShelf_no());
        itemModel.setShelfNoDetail(entity.getShelf_no_detail());
        itemModel.setSOrderUnitName(entity.getS_order_unit_nm());
        itemModel.setSOrderPacking(entity.getS_order_packing());
        itemModel.setSStockUnitInputQtty(entity.getS_stock_unit_input_qtty());
        itemModel.setSOrderUnitInputQtty(entity.getS_order_unit_input_qtty());
        itemModel.setSOrderRate(entity.getS_order_rate());
        itemModel.setStruckUnitName(entity.getStruck_unit_nm());
        itemModel.setStruckPacking(entity.getStruck_packing());
        itemModel.setStruckStockUnitInputQtty(entity.getStruck_stock_unit_input_qtty());
        itemModel.setStructUnitInputQtty(entity.getStruct_unit_input_qtty());
        itemModel.setStructRate(entity.getStruct_rate());
        itemModel.setInvtUnitName(entity.getInvt_unit_nm());
        itemModel.setInvtPacking(entity.getInvt_packing());
        itemModel.setInvtStockUnitInputQtty(entity.getInvt_stock_unit_input_qtty());
        itemModel.setInvtUnitInputQtty(entity.getInvt_unit_input_qtty());
        itemModel.setInvtRate(entity.getInvt_rate());
        itemModel.setItemPhotoFileName(entity.getItem_photo_file_nm());
        itemModel.setItemRemark(entity.getItem_remark());
        itemModel.setShipmentPlaceCode(entity.getShipment_place_cd());
        itemModel.setSpm(entity.getSpm());
        itemModel.setDeletedFlg(entity.getDeleted_flg());
        itemModel.setUserId(entity.getUser_id());
        itemModel.setProgramId(entity.getProgram_id());
        itemModel.setCreatedDatetime(DateTimeUtil.convertDateToddMMyyyyCommaHHmmss(entity.getCreated_datetime(), userInfo.getLocale()));
        itemModel.setModifiedDatetime(DateTimeUtil.convertDateToddMMyyyyCommaHHmmss(entity.getModified_datetime(), userInfo.getLocale()));
        itemModel.setAccItemCode(entity.getAcc_item_cd());
        itemModel.setSafetyLeadTimeSec(entity.getSafety_lead_time_sec());
        itemModel.setCavity(entity.getCavity());
        itemModel.setRubberWeight(entity.getRubber_weight());
        itemModel.setRubberPerShot(entity.getRubber_per_shot());
        itemModel.setShotPerShift(entity.getShot_per_shift());
        itemModel.setLowStockLimit(entity.getLow_stock_limit());
        itemModel.setMaxStockLimit(entity.getMax_stock_limit());
        itemModel.setFormatTag(entity.getFormat_tag());
        itemModel.setExpCheckDate(entity.getExp_check_date());
        itemModel.setWeightUnitName(entity.getWeight_unit_nm());
        itemModel.setIssueTagFlg(entity.getIssue_tag_flg());
        itemModel.setSpecialTagFlg(entity.getSpecial_tag_flg());
        itemModel.setDeliveryLeadTime(entity.getDelivery_lead_time());
        itemModel.setStockCardFlg(entity.getStock_card_flg());
        itemModel.setSnpPacking(entity.getSnp_packing());
        itemModel.setDrawingNo(entity.getDrawing_no());
        itemModel.setSizeMPerMm(entity.getSize_m_per_mm());
        itemModel.setLocalSalesAccCode(entity.getLocal_sales_acc_cd());
        itemModel.setExportSalesAccCode(entity.getExport_sales_acc_cd());
        itemModel.setPurAccCode(entity.getPur_acc_cd());
        itemModel.setDebitCustAccCode(entity.getDebit_cust_acc_cd());
        itemModel.setCreditLocalAccCode(entity.getCredit_local_acc_cd());
        itemModel.setCreditExportAccCode(entity.getCredit_export_acc_cd());
        itemModel.setExportItemFlg(entity.getExport_item_flg());
        itemModel.setInventoryGroupName(entity.getInventory_group_nm());
        itemModel.setMaterialClassName(entity.getMaterial_class_nm());
        itemModel.setStockPlaceName(entity.getStock_place_nm());
        itemModel.setFactAbbrnm(entity.getFact_abbrnm());
        itemModel.setStructUnitName(entity.getStruct_unit_nm());
        itemModel.setStructPacking(entity.getStruct_packing());
        itemModel.setStructStockUnitInputQtty(entity.getStruct_stock_unit_input_qtty());
        itemModel.setShipmentPlaceName(entity.getShipment_place_nm());
        itemModel.setNotInPart(entity.isNot_in_part());
        itemModel.setProdSnp(entity.getProd_snp());
        itemModel.setProcCode(entity.getProc_cd());
        itemModel.setProcName(entity.getProc_nm());
        itemModel.setPlaceCode(entity.getPlace_cd());
        itemModel.setPlaceName(entity.getPlace_nm());
        itemModel.setStockUnitDecimal(entity.getStock_unit_decimal());
        itemModel.setInvtUnitDecimal(entity.getInvt_unit_decimal());
        itemModel.setSOrderUnitDecimal(entity.getS_order_unit_decimal());
        itemModel.setStructUnitDecimal(entity.getStruct_unit_decimal());
        itemModel.setModifiedBy(entity.getModified_by());
        itemModel.setWorkPlaceCode(entity.getWork_place_cd());
        itemModel.setWorkPlaceName(entity.getWork_place_nm());
        itemModel.setMachineSetCode(entity.getMachine_set_cd());
        itemModel.setDieSetCode(entity.getDie_set_cd());
        itemModel.setDieSetName(entity.getDie_set_nm());
        itemModel.setFormatTagName(entity.getFormat_tag_nm());
        itemModel.setExpCheck(entity.isExp_check());
        itemModel.setSpecialTag(entity.isSpecial_tag());
        itemModel.setIssuedTagFlg(entity.getIssued_tag_flg());
        itemModel.setIssuedTag(entity.isIssued_tag());
        itemModel.setEffectDate(entity.getEffect_date());
        itemModel.setPOrderUnitPrice(entity.getP_order_unit_price());
        itemModel.setStockCard(entity.isStock_card());
        itemModel.setReqQtty(entity.getReq_qtty());
        itemModel.setPurResultTagQtty(entity.getPur_result_tag_qtty());
        return itemModel;
    }
    private ItemProcessModel convertItemProcessEntityToModel(ItemProcessEntity entity, UserInfo userInfo){
        ItemProcessModel itemProcessModel = new ItemProcessModel();
        /**/
        itemProcessModel.setItemCustCode(entity.getItem_cust_cd());
        itemProcessModel.setItemCode(entity.getItem_cd());
        itemProcessModel.setItemRevNo(entity.getItem_rev_no());
        itemProcessModel.setLineNo(entity.getLine_no());
        itemProcessModel.setSortNo(entity.getSort_no());
        itemProcessModel.setProcCode(entity.getProc_cd());
        itemProcessModel.setProcName(entity.getProc_nm());
        itemProcessModel.setProcDiv(entity.getProc_div());
        itemProcessModel.setMaterialCode(entity.getMaterial_cd());
        itemProcessModel.setThickness(entity.getThickness());
        itemProcessModel.setWidth(entity.getWidth());
        itemProcessModel.setPitch(entity.getPitch());
        itemProcessModel.setWeight(entity.getWeight());
        itemProcessModel.setPacking(entity.getPacking());
        itemProcessModel.setProdSnp(entity.getProd_snp());
        itemProcessModel.setRoundQtty(entity.getRound_qtty());
        itemProcessModel.setPOrderDiv(entity.getP_order_div());
        itemProcessModel.setPOrderPoint(entity.getP_order_point());
        itemProcessModel.setPOrderPoInteger(entity.getP_order_poInteger());
        itemProcessModel.setWorkLoad(entity.getWork_load());
        itemProcessModel.setProcCost(entity.getProc_cost());
        itemProcessModel.setDeletedFlg(entity.getDeleted_flg());
        itemProcessModel.setUserId(entity.getUser_id());
        itemProcessModel.setProgramId(entity.getProgram_id()); ;
        itemProcessModel.setPlanPriorityCode(entity.getPlan_priority_cd());
        itemProcessModel.setOneShiftOneItemFlg(entity.getOne_shift_one_item_flg());
        return itemProcessModel;
    }
    private ItemLocationModel convertItemLocationEntityToModel(ItemLocationEntity entity, UserInfo userInfo) {
        ItemLocationModel itemLocationModel = new ItemLocationModel();
        /**/
        itemLocationModel.setItemCustCode(entity.getItem_cust_cd());
        itemLocationModel.setItemCode(entity.getItem_cd());
        itemLocationModel.setItemRevNo(entity.getItem_rev_no());
        itemLocationModel.setItemProcLineNo(entity.getItem_proc_line_no());
        itemLocationModel.setLineNo(entity.getLine_no());
        itemLocationModel.setSortNo(entity.getSort_no());
        itemLocationModel.setWorkPlaceCode(entity.getWork_place_cd());
        itemLocationModel.setWorkPlaceName(entity.getWork_place_nm());
        itemLocationModel.setWorkloadGroupCode(entity.getWorkload_group_cd());
        itemLocationModel.setWorkloadGroupBrNo(entity.getWorkload_group_br_no());
        itemLocationModel.setFixPlanGroupCode(entity.getFix_plan_group_cd());
        itemLocationModel.setMachineGroupDiv(entity.getMachine_group_div());
        itemLocationModel.setMachineGroupName(entity.getMachine_group_nm());
        itemLocationModel.setMachineSetCode(entity.getMachine_set_cd());
        itemLocationModel.setMachineSetName(entity.getMachine_set_nm());
        itemLocationModel.setDieGroupDiv(entity.getDie_group_div());
        itemLocationModel.setDieGroupName(entity.getDie_group_nm());
        itemLocationModel.setDieSetCode(entity.getDie_set_cd());
        itemLocationModel.setDieSetName(entity.getDie_set_nm());
        itemLocationModel.setDieJigSetCode(entity.getDie_jig_set_cd());
        itemLocationModel.setMaterialSupplyDiv(entity.getMaterial_supply_div());
        itemLocationModel.setYieldRate(entity.getYield_rate());
        itemLocationModel.setStdWaitTime(entity.getStd_wait_time());
        itemLocationModel.setStdPreparationTime(entity.getStd_preparation_time());
        itemLocationModel.setStdWorkTime(entity.getStd_work_time());
        itemLocationModel.setNoOfCavity(entity.getNo_of_cavity());
        itemLocationModel.setPOrderLeadTime(entity.getP_order_lead_time());
        itemLocationModel.setDeletedFlg(entity.getDeleted_flg());
        itemLocationModel.setUserId(entity.getUser_id());
        itemLocationModel.setProgramId(entity.getProgram_id());
        itemLocationModel.setProcDiv(entity.getProc_div());
        itemLocationModel.setFactoryCode(entity.getFactory_cd());
        itemLocationModel.setFactAbbrnm(entity.getFact_abbrnm());
        itemLocationModel.setUseStockPlaceDiv(entity.getUse_stock_place_div());
        itemLocationModel.setWorkDivideFlg(entity.getWork_divide_flg());
        itemLocationModel.setPreparationDivideFlg(entity.getPreparation_divide_flg());
        itemLocationModel.setWorkPreparationDivideFlg(entity.getWork_preparation_divide_flg());
        itemLocationModel.setHolidayWorkTime(entity.getHoliday_work_time());
        itemLocationModel.setWorkEndDate(entity.getWork_end_date());
        itemLocationModel.setResultWorkEndTime(entity.getResult_work_end_time());
        itemLocationModel.setPreWorkEndDate(entity.getPre_work_end_date());
        itemLocationModel.setResultPreWorkEndTime(entity.getResult_pre_work_end_time());
        return itemLocationModel;
    }
    /* ListModel Use For Return To JSF Header page */
    private ItemListModel convertEntityToListModel(int rowNo, ItemEntity entity, UserInfo userInfo){
        ItemListModel itemListModel = new ItemListModel();
        /**/
        itemListModel.setRowNo(rowNo);
        itemListModel.setItemCustCode(entity.getItem_cust_cd());
        itemListModel.setItemCode(entity.getItem_cd());
        itemListModel.setItemRevNo(entity.getItem_rev_no());
        itemListModel.setItemName1(entity.getItem_nm1());
        itemListModel.setItemName2(entity.getItem_nm2());
        itemListModel.setItemDiv(entity.getItem_div());
        itemListModel.setStockUnitName(entity.getStock_unit_nm());
        itemListModel.setMakerName(entity.getMaker_nm());
        itemListModel.setModelName(entity.getModel_nm());
        return itemListModel;
    }
    public ItemModel getItemByCode(String itemCustCode, String itemCode, String itemRevNo, UserInfo userInfo) {
        ItemModel item = new ItemModel();
        item = convertItemEntityToModel(itemDao.findOneItemByCondition(itemCustCode, itemCode, itemRevNo), userInfo);
        item.setProcess(convertItemProcessEntityToModel(itemDao.findOneItemProcessByCondition(itemCustCode, itemCode, itemRevNo), userInfo));
        item.setLocation(convertItemLocationEntityToModel(itemDao.findOneItemLocationByCondition(itemCustCode, itemCode, itemRevNo), userInfo));
        return item;
    }
    public Long getRowCountByCondition(ItemFilter filter, UserInfo userInfo) throws ServiceException{
        return itemDao.rowCountByCondition(filter);
    }
    public List<ItemListModel> getItemListByCondition(ItemFilter filter, UserInfo userInfo) {
        List<ItemListModel> itemList = new ArrayList<>();
        List<ItemEntity> itemEntityList = itemDao.findAllByCondition(filter);
        int rowNo = 1;
        for(ItemEntity entity : itemEntityList){
            itemList.add(convertEntityToListModel(rowNo, entity, userInfo));
            rowNo++;
        }
        return itemList;
    }
    public ItemModel save(ItemModel data, UserInfo userInfo) {
        data.setExpCheckDate((long) (data.isExpCheck() ? 1 : 0));
        data.setSpecialTagFlg((long) (data.isSpecialTag() ? 1 : 0));
        data.setIssuedTagFlg((long) (data.isIssuedTag() ? 1 : 0));
        data.setStockCardFlg((long) (data.isStockCard() ? 1 : 0));
        data.setUserId(userInfo.getUserName());
        data.setProgramId(this.getClass().getName() + ".save");

        data.getProcess().setItemCustCode(data.getItemCustCode());
        data.getProcess().setItemCode(data.getItemCode());
        data.getProcess().setItemRevNo(data.getItemRevNo());
        data.getProcess().setUserId(userInfo.getUserName());
        data.getProcess().setProgramId(this.getClass().getName() + ".save");

        data.getLocation().setItemCustCode(data.getItemCustCode());
        data.getLocation().setItemCode(data.getItemCode());
        data.getLocation().setItemRevNo(data.getItemRevNo());
        data.getLocation().setUserId(userInfo.getUserName());
        data.getLocation().setProgramId(this.getClass().getName() + ".save");

        itemDao.save(data, data.getProcess(), data.getLocation());

        return convertItemEntityToModel(itemDao.findOneItemByCondition(data.getItemCustCode(), data.getItemCode(), data.getItemRevNo()), userInfo);
    }
    public Boolean foundDataDeleted(ItemModel data, UserInfo userInfo) throws ServiceException{
        ItemFilter filter = new ItemFilter();
        filter.setItemCustCode(data.getItemCustCode());
        filter.setItemCode(data.getItemCode());
        filter.setItemRevNo("0");
        return itemDao.rowCountDataDeleted(filter) > 0 ? true:false;
    }
    public ApiMessage delete(ItemModel data, UserInfo userInfo) throws ServiceException{
        data.setUserId(userInfo.getUserName());
        data.setProgramId(this.getClass().getName() + ".delete");
        itemDao.delete(data);
        return message.getDeletedMessage(true, userInfo.getLocale());
    }
    public ApiMessage restore(ItemModel data, UserInfo userInfo) throws ServiceException{
        data.setUserId(userInfo.getUserName());
        data.setProgramId(this.getClass().getName() + ".restore");
        itemDao.restore(data);
        return message.getRestoreMessage(userInfo.getLocale());
    }
    public void calculateUnitRate(ItemModel data) {
        Long stock_ordunit_input = data.getSStockUnitInputQtty()      != null ? data.getSStockUnitInputQtty()      : 0;
        Long stock_strunit_input = data.getStructStockUnitInputQtty() != null ? data.getStructStockUnitInputQtty() : 0;
        Long stock_invunit_input = data.getInvtStockUnitInputQtty()   != null ? data.getInvtStockUnitInputQtty()   : 0;

        Long order_unit_input    = data.getSOrderUnitInputQtty()     != null ? data.getSOrderUnitInputQtty()       : 0;
        Long strut_unit_input    = data.getStructUnitInputQtty()     != null ? data.getStructUnitInputQtty()       : 0;
        Long invet_unit_input    = data.getInvtUnitInputQtty()       != null ? data.getInvtUnitInputQtty()         : 0;
        if(stock_ordunit_input   > 0) data.setSOrderRate(order_unit_input/stock_ordunit_input);
        if(stock_strunit_input   > 0) data.setStructRate(strut_unit_input/stock_strunit_input);
        if(stock_invunit_input   > 0) data.setInvtRate(invet_unit_input/stock_invunit_input);

        if(data.getProcess().getProcDiv() == 0 || data.getProcess().getProcDiv() == 2){
            data.setSafetyLeadTime(data.getSafetyLeadTime()*2);
        }
    }
    public ItemModel update(ItemModel data, UserInfo userInfo) {
        calculateUnitRate(data);
        data.setUserId(userInfo.getUserName());
        data.setProgramId(this.getClass().getName() + ".update");

        data.getProcess().setUserId(userInfo.getUserName());
        data.getProcess().setProgramId(this.getClass().getName() + ".update");

        data.getLocation().setUserId(userInfo.getUserName());
        data.getLocation().setProgramId(this.getClass().getName() + ".update");
        data.setUseStockPlace(true);
        data.getLocation().setUseStockPlaceDiv((long) (data.getUseStockPlace() == true ? 2 : 1));
        itemDao.update(data, userInfo);
        return getItemByCode(data.getItemCustCode(), data.getItemCode(), data.getItemRevNo(), userInfo);
    }
    public ItemModel getItemCopyByCode(String itemCustCode, String itemCode, String itemRevNo, UserInfo userInfo) {
        ItemModel item = new ItemModel();
        item = convertItemEntityToModel(itemDao.findOneItemByCondition(itemCustCode, itemCode, itemRevNo), userInfo);
        item.setProcess(convertItemProcessEntityToModel(itemDao.findOneItemProcessByCondition(itemCustCode, itemCode, itemRevNo), userInfo));
        item.setLocation(convertItemLocationEntityToModel(itemDao.findOneItemLocationByCondition(itemCustCode, itemCode, itemRevNo), userInfo));

        item.setStockCard(NumberUtil.convertToInteger(item.getStockCardFlg()) == 1 ? true : false);
//        data.setExp_check(checked_exp_date ? true : false);
//        data.setExp_check_date(checked_exp_date ? 1 : 0);
//        break_test_div  = data.getBreak_test_div() == 1 ? false : true;
//        use_stock_place = data.getLocation().getUse_stock_place_div() == 1 ? true : false;

        item.getProcess().setItemCustCode("");
        item.getProcess().setItemCode("");
        item.getProcess().setItemRevNo("");

        item.getLocation().setItemCustCode("");
        item.getLocation().setItemCode("");
        item.getLocation().setItemRevNo("");

        return item;
    }
}
