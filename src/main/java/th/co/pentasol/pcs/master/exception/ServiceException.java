package th.co.pentasol.pcs.master.exception;

import th.co.pentasol.pcs.master.model.api.ApiMessage;

public class ServiceException extends Exception{
    private int httpStatus;
    private ApiMessage apiMessage;
    public ServiceException(int httpStatus, ApiMessage apiMessage){
        super(apiMessage.getMessage());
        this.httpStatus = httpStatus;
        this.apiMessage = apiMessage;
    }

    public int getHttpStatus(){
        return this.httpStatus;
    }

    public ApiMessage getApiMessage(){
        return apiMessage;
    }
}
