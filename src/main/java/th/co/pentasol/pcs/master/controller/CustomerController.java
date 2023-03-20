package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.entity.CustomerEntity;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.CustomerModel;
import th.co.pentasol.pcs.master.model.SelectedModel;
import th.co.pentasol.pcs.master.model.filter.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerListModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.service.CustomerService;
import th.co.pentasol.pcs.master.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("v1/customer")
public class CustomerController extends AbsController{
    @Autowired
    CustomerService customerService;
    @Autowired
    Message message;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/dropdown", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> dropdown(HttpServletRequest request) throws ServiceException {
        return responseOK(customerService.getCustomerDropdownList(userService.getUserInfo(request)));
    }

    @GetMapping(value = "/{code}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> view(HttpServletRequest request, @PathVariable String code) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(customerService.getCustomerByCode(userInfo, code));
    }

    @PostMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> search(HttpServletRequest request, @RequestBody CustomerFilter filter) throws  ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        Long rowCount = customerService.getRowCountByCondition(userInfo, filter);
        if(rowCount > 0){
            List<CustomerListModel> dataList = customerService.getCustomerListByCondition(userInfo, filter);
            return responseOK(dataList, rowCount, filter.getPageNo(), filter.getPageSize());
        }
        return responseOK();
    }

    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> create(HttpServletRequest request, @Valid @RequestBody CustomerModel data) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        CustomerFilter filter = new CustomerFilter();
        filter.setCustomerCode(data.getCode());
        filter.setBranchCode(data.getBranchCode());
        filter.setEffectDate(data.getEffectiveDate());
        Long rowCount = customerService.getRowCountByCondition(userInfo, filter);
        if(rowCount > 0){
            return responseOK(message.getMsgDuplicateName(userInfo.getLocale()));
        }else {
            return responseOK(customerService.save(userInfo, data), message.getSavedMessage(userInfo.getLocale()));
        }
    }

    @PostMapping(value = "/{code}/{branchCode}/{serialNo}/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> update(HttpServletRequest request, @PathVariable String code, @PathVariable String branchCode, @PathVariable Integer serialNo, @Valid @RequestBody CustomerModel data) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        data.setCode(code);
        data.setBranchCode(branchCode);
        data.setSerialNo(serialNo);
        return responseOK(customerService.update(userInfo, data), message.getUpdatedMessage(userInfo.getLocale()));
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> delete(HttpServletRequest request, @RequestBody CustomerModel data) throws ServiceException{
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(customerService.delete(userInfo, data));
    }
}
