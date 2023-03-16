package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.LocationModel;
import th.co.pentasol.pcs.master.model.SelectedModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.model.api.ApiResponseWithPage;
import th.co.pentasol.pcs.master.model.filter.LocationFilter;
import th.co.pentasol.pcs.master.service.LocationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("v1/location")
public class LocationController extends AbsController {
    @Autowired
    LocationService locationService;
    @Autowired
    Message message;

    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> create(HttpServletRequest request, @Valid @RequestBody LocationModel data) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(locationService.save(data, userInfo), message.getSavedMessage(userInfo.getLocale()));
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> delete(HttpServletRequest request, @RequestBody SelectedModel data) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(locationService.delete(data, userInfo));
    }

    @PostMapping(value = "/{code}/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> update(HttpServletRequest request, @PathVariable String code, @Valid @RequestBody LocationModel data) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        data.setCode(code);
        return responseOK(locationService.update(data, userInfo), message.getUpdatedMessage(userInfo.getLocale()));
    }

    @GetMapping(value = "/{code}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> view(HttpServletRequest request, @PathVariable String code) throws ServiceException {
        return responseOK(locationService.getLocation(code, userService.getUserInfo(request)));
    }

    @PostMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponseWithPage> search(HttpServletRequest request, @RequestBody LocationFilter filter) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        Long rowCount = locationService.getRowCountLocationByCondition(filter, userInfo);
        List<Map<String, Object>> dataList = new ArrayList<>();
        if(rowCount.intValue() > 0){
            dataList = locationService.getLocationListByCondition(filter, userInfo);
        }
        return responseWithPageOK(dataList, rowCount, filter.getPageNo(), filter.getPageSize());
    }
}
