package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerListModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.service.CustomerService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("v1/customer")
public class CustomerController extends AbsController{
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/dropdown", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> dropdown(HttpServletRequest request) throws ServiceException {
        return responseOK(customerService.getCustomerDropdownList(userService.getUserInfo(request)));
    }

    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> list(HttpServletRequest request, @RequestBody CustomerFilter filter) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        Long total = customerService.getRowCountByCondition(userInfo, filter);
        if(total > 0){
            List<CustomerListModel> data = customerService.getCustomerListByCondition(userInfo, filter);
            return responseOK(data, total, filter.getPageNo(), filter.getPageSize());
        }
        return responseOK();
    }

}
