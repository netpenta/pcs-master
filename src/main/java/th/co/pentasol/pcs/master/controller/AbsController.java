package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.model.api.ApiMessage;
import th.co.pentasol.pcs.master.model.api.ApiPaginator;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import th.co.pentasol.pcs.master.service.UserService;

import java.util.ArrayList;


@Slf4j
public class AbsController{
    @Autowired
    UserService userService;
    @Autowired
    Message message;

    public ResponseEntity<ApiResponse> responseOK(){
        return new ResponseEntity<>(new ApiResponse(), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> responseOK(Object data){
        ApiResponse apiResponse = new ApiResponse(data);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> responseOK(ApiMessage message){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessages(new ArrayList<ApiMessage>() {
            @Override
            public boolean add(ApiMessage apiMessage) {
                return super.add(apiMessage);
            }
        });
        apiResponse.setData(null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> responseOK(Object data, ApiMessage message){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessages(new ArrayList<ApiMessage>() {
            @Override
            public boolean add(ApiMessage apiMessage) {
                return super.add(apiMessage);
            }
        });
        apiResponse.setData(data);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> responseOK(Object data, Long totalRecords, Integer pageNo, Integer pageSize){
        ApiResponse apiResponse = new ApiResponse();

        ApiPaginator paginator = new ApiPaginator();
        paginator.setCurrentPage(pageNo);
        paginator.setPageSize(pageSize);
        paginator.setTotalPage(Util.calculateTotalPage(totalRecords, pageSize));
        paginator.setTotalRecord(totalRecords.intValue());

        apiResponse.setPaginator(paginator);
        apiResponse.setData(data);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
