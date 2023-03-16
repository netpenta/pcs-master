package th.co.pentasol.pcs.master.model.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiResponseWithPage extends ApiResponse{
    private Integer currentPage;
    private Integer totalPage;
    private Integer totalRecord;
    private Integer pageSize;
}
