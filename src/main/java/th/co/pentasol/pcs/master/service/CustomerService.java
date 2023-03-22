package th.co.pentasol.pcs.master.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.configuration.PcsConfig;
import th.co.pentasol.pcs.master.dao.CustomerDao;
import th.co.pentasol.pcs.master.entity.CustomerEntity;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.CustomerModel;
import th.co.pentasol.pcs.master.model.SelectedModel;
import th.co.pentasol.pcs.master.model.api.ApiMessage;
import th.co.pentasol.pcs.master.model.filter.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerListModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.Dropdown;
import th.co.pentasol.pcs.master.util.ApiStatus;
import th.co.pentasol.pcs.master.util.DateTimeUtil;
import th.co.pentasol.pcs.master.util.Util;

import java.util.*;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    PcsConfig config;
    @Autowired
    Message message;
    @Autowired
    CustomerDao customerDao;

    public CustomerService(Message message, CustomerDao customerDao) throws ServiceException {
        this.message = message;
        this.customerDao = customerDao;
    }

    public List<Dropdown> getCustomerDropdownList(UserInfo userInfo) {
        List<Dropdown> dataList = new ArrayList<>();
        List<CustomerEntity> entityList = customerDao.findAll();

        for(CustomerEntity entity : entityList){
            dataList.add(new Dropdown(entity.getCust_cd(), entity.getCust_cd() + " : " + (userInfo.getLocale().getLanguage().equals("en") ? entity.getCust_nm() : entity.getCust_nm_th())));
        }
        return dataList;
    }

    public Long getRowCountByCondition(UserInfo userInfo, CustomerFilter filter) throws ServiceException {
        return customerDao.rowcountByCondition(filter);
    }

    public List<CustomerListModel> getCustomerListByCondition(UserInfo userInfo, CustomerFilter filter) throws ServiceException{
        List<CustomerListModel> dataList = new ArrayList<>();
        List<CustomerEntity> entityList = customerDao.findAllByCondition(filter);
        int rowNo = 1;
        for(CustomerEntity entity : entityList){
            dataList.add(entityToModelList(entity, userInfo, rowNo));
            rowNo++;
        }
        return dataList;
    }
    public CustomerModel getCustomerByCondition(UserInfo userInfo, CustomerFilter filter) throws ServiceException{
        return entityToModel(customerDao.findOneByCondition(filter), userInfo);
    }
    public CustomerModel getCustomerByCode(UserInfo userInfo, String code) throws ServiceException{
        return entityToModel(customerDao.findOneByCode(code), userInfo);
    }

    public CustomerModel getCustomerByFilter(UserInfo userInfo, CustomerFilter filter) throws ServiceException{
        return entityToModel(customerDao.findOneByFilter(filter), userInfo);
    }

    private CustomerEntity modelToEntity(CustomerModel model){
        CustomerEntity data = new CustomerEntity();
        data.setCust_cd(model.getCode());
        data.setBranch_cd(model.getBranchCode());
        data.setSerial_no(model.getSerialNo());
        data.setEffect_date(model.getEffectDate());
        data.setExp_date(model.getExpDate());
        data.setUser_id(model.getModifiedBy());
        data.setProgram_id(model.getSystemId());
        return data;
    }

    private CustomerEntity modelListToEntity(CustomerListModel model){
        CustomerEntity data = new CustomerEntity();
        data.setCust_cd(model.getCode());
        data.setBranch_cd(model.getBranchCode());
        data.setSerial_no(model.getSerialNo());
        data.setEffect_date(model.getEffectDate());
        data.setExp_date(model.getExpDate());
        data.setUser_id(model.getModifiedBy());
        data.setProgram_id(model.getSystemId());
        return data;
    }

    private CustomerModel entityToModel(CustomerEntity entity, UserInfo userInfo){
        CustomerModel data = new CustomerModel();
        data.setCode(entity.getCust_cd());
        data.setSerialNo(entity.getSerial_no());
        data.setBranchNo(entity.getBranch_cd());
        data.setBranchCode(entity.getBranch_cd());
        data.setBranchName(entity.getCust_branch_nm());
        data.setNameEn(entity.getCust_nm());
        data.setNameTh(entity.getCust_nm_th());
        data.setEffectDate(entity.getEffect_date());
        data.setExpDate(entity.getExp_date());
        data.setEffectiveDate(DateTimeUtil.convertNumbericToDate(entity.getEffect_date()));
        data.setExpiredDate(DateTimeUtil.convertNumbericToDate(entity.getExp_date()));
        data.setGroupId(entity.getGroup_div());
        data.setShortNameEn(entity.getCust_short_nm());
        data.setShortNameTh(entity.getCust_short_nm_th());
        data.setAddress1(entity.getAddress_1());
        data.setAddress2(entity.getAddress_2());
        data.setTelephoneNo(entity.getTel_no());
        data.setFaxNo(entity.getFax_no());
        data.setSystemId(entity.getProgram_id());
        data.setDeliveryLocation(entity.getDelivery_location());
        data.setTaxId(entity.getTax_id());
        data.setModifiedBy(entity.getUser_id());
        data.setModifiedDateTime(DateTimeUtil.convertDateToddMMyyyyCommaHHmmss(entity.getModified_datetime(), userInfo.getLocale()));
        return data;
    }

    private CustomerListModel entityToModelList(CustomerEntity entity, UserInfo userInfo, int rowNo){
        CustomerListModel data = new CustomerListModel();
        data.setRowNo(rowNo);
        data.setCode(entity.getCust_cd());
        data.setSerialNo(entity.getSerial_no());
        data.setBranchNo(entity.getBranch_cd());
        data.setBranchCode(entity.getBranch_cd());
        data.setBranchName(entity.getCust_branch_nm());
        data.setNameEn(entity.getCust_nm());
        data.setNameTh(entity.getCust_nm_th());
        data.setEffectDate(entity.getEffect_date());
        data.setExpDate(entity.getExp_date());
        data.setEffectiveDate(DateTimeUtil.convertNumbericToDate(entity.getEffect_date()));
        data.setExpiredDate(DateTimeUtil.convertNumbericToDate(entity.getExp_date()));
        data.setGroupId(entity.getGroup_div());
        data.setShortNameEn(entity.getCust_short_nm());
        data.setShortNameTh(entity.getCust_short_nm_th());
        data.setAddress1(entity.getAddress_1());
        data.setAddress2(entity.getAddress_2());
        data.setTelephoneNo(entity.getTel_no());
        data.setFaxNo(entity.getFax_no());
        data.setSystemId(entity.getProgram_id());
        data.setDeliveryLocation(entity.getDelivery_location());
        data.setTaxId(entity.getTax_id());
        data.setModifiedBy(entity.getUser_id());
        data.setModifiedDateTime(DateTimeUtil.convertDateToddMMyyyyCommaHHmmss(entity.getModified_datetime(), userInfo.getLocale()));
        return data;
    }


    private CustomerListModel modelToModelList(CustomerModel data, UserInfo userInfo, int rowNo){
        CustomerListModel row = new CustomerListModel();
        row.setRowNo(rowNo);
        row.setCode(data.getCode());
        row.setSerialNo(data.getSerialNo());
        row.setBranchCode(data.getBranchCode());
        row.setBranchNo(data.getBranchNo());
        row.setBranchName(data.getBranchName());
        row.setNameEn(data.getNameEn());
        row.setNameTh(data.getNameTh());
        row.setEffectiveDate(data.getEffectiveDate());
        row.setExpiredDate(data.getExpiredDate());
        row.setGroupId(data.getGroupId());
        row.setShortNameEn(data.getShortNameEn());
        row.setShortNameTh(data.getShortNameTh());
        row.setAddress1(data.getAddress1());
        row.setAddress2(data.getAddress2());
        row.setTelephoneNo(data.getTelephoneNo());
        row.setFaxNo(data.getFaxNo());
        row.setUserName(data.getModifiedBy());
        row.setSystemId(data.getSystemId());
        row.setDeliveryLocation(data.getDeliveryLocation());
        row.setTaxId(data.getTaxId());
        row.setModifiedBy(data.getUserName());
        row.setModifiedDateTime(data.getModifiedDateTime());
        return row;
    }

    private CustomerModel modelListToModel(CustomerListModel data, UserInfo userInfo){
        CustomerModel row = new CustomerModel();
        row.setCode(data.getCode());
        row.setSerialNo(data.getSerialNo());
        row.setBranchCode(data.getBranchCode());
        row.setBranchNo(data.getBranchNo());
        row.setBranchName(data.getBranchName());
        row.setNameEn(data.getNameEn());
        row.setNameTh(data.getNameTh());
        row.setEffectDate(data.getEffectDate());
        row.setEffectiveDate(data.getEffectiveDate());
        row.setExpDate(data.getExpDate());
        row.setExpiredDate(data.getExpiredDate());
        row.setGroupId(data.getGroupId());
        row.setShortNameEn(data.getShortNameEn());
        row.setShortNameTh(data.getShortNameTh());
        row.setAddress1(data.getAddress1());
        row.setAddress2(data.getAddress2());
        row.setTelephoneNo(data.getTelephoneNo());
        row.setFaxNo(data.getFaxNo());
        row.setUserName(data.getModifiedBy());
        row.setSystemId(data.getSystemId());
        row.setDeliveryLocation(data.getDeliveryLocation());
        row.setTaxId(data.getTaxId());
        row.setModifiedBy(data.getModifiedBy());
        row.setModifiedDateTime(data.getModifiedDateTime());
        return row;
    }

    public CustomerModel save(UserInfo userInfo, CustomerModel data) throws ServiceException {
        List<CustomerListModel> dataList = setExpireDate(userInfo, data);
        Integer serial_no = getMaxSerialNo(data);
        for(CustomerListModel row:dataList){
            row.setModifiedBy(userInfo.getUserName());
            row.setSystemId(this.getClass().getName() + ".save");
            if(Objects.isNull(row.getSerialNo()) || row.getSerialNo() == 0){
                row.setSerialNo(serial_no);
                customerDao.insert(modelListToModel(row, userInfo));
            }else{
                customerDao.update(modelListToModel(row, userInfo));
            }
        }
        CustomerFilter filter = new CustomerFilter();
        filter.setCustomerCode(data.getCode());
        filter.setBranchCode(data.getBranchCode());
        filter.setSerialNo(serial_no);
        return getCustomerByCondition(userInfo, filter);
    }
    private Integer getMaxSerialNo(CustomerModel data) {
        return customerDao.getMaxSerialNo(data.getCode(), data.getBranchCode());
    }

    private List<CustomerListModel> setExpireDate(UserInfo userInfo, CustomerModel data) throws ServiceException {
        CustomerFilter filter = new CustomerFilter();
        filter.setCustomerCode(data.getCode());
        filter.setBranchCode(data.getBranchCode());
        List<CustomerListModel> dataList = getCustomerListByCondition(userInfo, filter);
        if(data.getDeleted_flg() == null || data.getDeleted_flg() == 0){
            dataList.add(modelToModelList(data, userInfo, dataList.size() + 1));
        }
        dataList.sort(Comparator.comparingInt((CustomerListModel data2) -> DateTimeUtil.convertDateToNumeric(data2.getEffectiveDate())));
        for(int i = 0 ; i < dataList.size() ; i ++){
            if(i > 0 && i < dataList.size()){
                Date effect_date = dataList.get(i).getEffectiveDate();
                Date exp_date    = DateTimeUtil.findDate(effect_date, -1);
                dataList.get(i-1).setExpiredDate(exp_date);
                dataList.get(i-1).setExpDate(DateTimeUtil.convertDateToNumeric(exp_date));
            }
            if(i == dataList.size()-1){
                dataList.get(i).setEffectDate(DateTimeUtil.convertDateToNumeric(data.getEffectiveDate()));
                dataList.get(i).setExpDate(99999999);
            }
        }
        return dataList;
    }

    public CustomerModel update(UserInfo userInfo, CustomerModel data) throws ServiceException {
        // ----- unnecessary with jsf ?
        CustomerFilter filter = new CustomerFilter();
        CustomerModel oldData = new CustomerModel();
        filter.setCustomerCode(data.getCode());
        filter.setBranchCode(data.getBranchCode());
        filter.setSerialNo(data.getSerialNo());
        oldData = entityToModel(customerDao.findOneByCondition(filter),userInfo);
        data.setEffectDate(oldData.getEffectDate());
        data.setExpDate(oldData.getExpDate());
        // -----------------------------

        data.setModifiedBy(userInfo.getUserName());
        data.setSystemId(this.getClass().getName() + ".update");
        customerDao.update(data);
        return getCustomerByFilter(userInfo, filter);
    }

    public ApiMessage delete(UserInfo userInfo, CustomerModel data) throws  ServiceException{
        if(!Objects.isNull(data)){
            data.setEffectDate(DateTimeUtil.convertDateToNumeric(data.getEffectiveDate()));
            data.setExpDate(DateTimeUtil.convertDateToNumeric(new Date()));
            data.setModifiedBy(userInfo.getUserName());
            data.setSystemId(this.getClass().getName() + ".delete");
            customerDao.delete(modelToEntity(data));
        }
        return message.getDeletedMessage(true, userInfo.getLocale());
    }

    public CustomerModel restore(UserInfo userInfo, CustomerModel data) throws ServiceException{
        data.setEffectDate(DateTimeUtil.convertDateToNumeric(data.getEffectiveDate()));
        data.setModifiedBy(userInfo.getUserName());
        data.setSystemId(this.getClass().getName() + ".restore");
        customerDao.updateRestore(data);
        List<CustomerListModel> dataList = setExpireDate(userInfo, data);
        for(CustomerListModel row:dataList){
//            System.out.println("----- : " + row.getSerialNo() + " | " + row.getEffectDate());
//            row.setEffectDate(DateTimeUtil.convertDateToNumeric(data.getEffectiveDate()));
//            row.setModifiedBy(userInfo.getUserName());
//            row.setSystemId(this.getClass().getName() + ".restore");
//            if(Objects.isNull(row.getSerialNo()) || row.getSerialNo() == 0){
//                customerDao.restore(modelListToEntity(row));
//            }else{
//                customerDao.update(modelListToModel(row, userInfo));
//            }
        }
        CustomerFilter filter = new CustomerFilter();
//        filter.setCustomerCode(data.getCode());
//        filter.setBranchCode(data.getBranchCode());
//        return getCustomerByCondition(userInfo, filter);
        return null;
    }

    public CustomerModel renew(UserInfo userInfo, CustomerModel data) throws ServiceException{
//        customerDao.delete(modelToEntity(data));
//        List<CustomerListModel> dataList = setExpireDate(userInfo, data);
//        Integer serial_no = getMaxSerialNo(data);
//        for(CustomerListModel row:dataList){
//            row.setEffectDate(DateTimeUtil.convertDateToNumeric(data.getEffectiveDate()));
//            row.setModifiedBy(userInfo.getUserName());
//            row.setSystemId(this.getClass().getName() + ".renew");
//            if(Objects.isNull(row.getSerialNo()) || row.getSerialNo() == 0){
//                row.setSerialNo(serial_no);
//                customerDao.insert(modelListToModel(row, userInfo));
//            }else{
//                customerDao.update(modelListToModel(row, userInfo));
//            }
//        }
        CustomerFilter filter = new CustomerFilter();
//        filter.setCustomerCode(data.getCode());
//        filter.setBranchCode(data.getBranchCode());
//        filter.setSerialNo(serial_no);
//        return getCustomerByCondition(userInfo, filter);
        return null;
    }
}
