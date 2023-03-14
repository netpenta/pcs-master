package th.co.pentasol.pcs.master.model.api;

import lombok.Data;

@Data
public class ApiPaginator {
    private Integer currentPage;
    private Integer totalPage;
    private Integer totalRecord;
    private Integer pageSize;
}
