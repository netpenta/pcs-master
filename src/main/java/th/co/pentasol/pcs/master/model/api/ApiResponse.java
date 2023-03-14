package th.co.pentasol.pcs.master.model.api;

import lombok.Data;
import th.co.pentasol.pcs.master.util.ApiStatus;
import java.util.List;

@Data
public class ApiResponse {
    private int status;
    private String error;
    private List<ApiMessage> messages;
    private String path;
    private ApiPaginator paginator;
    private Object data;



    public ApiResponse() {
        this.status = ApiStatus.STATUS_OK;
        this.error = "";
        this.path = "";
        this.paginator = new ApiPaginator();
    }

    public ApiResponse(Object data) {
        this.status = ApiStatus.STATUS_OK;
        this.error = "";
        this.path = "";
        this.data = data;
        this.paginator = new ApiPaginator();
    }
}
