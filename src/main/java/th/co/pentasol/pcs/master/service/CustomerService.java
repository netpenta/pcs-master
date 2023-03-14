package th.co.pentasol.pcs.master.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.dao.CustomerDao;
import th.co.pentasol.pcs.master.entity.CustomerEntity;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerListModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.Dropdown;
import th.co.pentasol.pcs.master.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomerService {
   Message message;
   CustomerDao customerDao;

    public CustomerService(Message message, CustomerDao customerDao) {
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
        return customerDao.countByCondition(filter);
    }

    public List<CustomerListModel> getCustomerListByCondition(UserInfo userInfo, CustomerFilter filter) throws ServiceException{
        List<CustomerListModel> dataList = new ArrayList<>();
        List<CustomerEntity> entityList = customerDao.findAllByCondition(filter);
        int rowNo = 1;
        for(CustomerEntity entity : entityList){
            CustomerListModel data = new CustomerListModel();
            data.setRowNo(rowNo);
            data.setSerialNo(entity.getSerial_no());
            data.setCode(entity.getCust_cd());
            data.setBranchNo(entity.getBranch_cd());
            data.setBranchName(entity.getCust_branch_nm());
            data.setNameEn(entity.getCust_nm());
            data.setNameTh(entity.getCust_nm_th());
            data.setDeliveryLocation(entity.getDelivery_location());
            data.setTaxId(entity.getTax_id());
            data.setModifiedBy(entity.getUser_fname());
            data.setModifiedDateTime(DateTimeUtil.convertDateToddMMyyyyCommaHHmmss(entity.getModified_datetime(), userInfo.getLocale()));
            dataList.add(data);
            rowNo++;
        }
        return dataList;
    }

}
