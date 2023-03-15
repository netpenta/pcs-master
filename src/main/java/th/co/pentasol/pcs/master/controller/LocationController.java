package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.LocationModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.service.LocationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
}
