package th.co.pentasol.pcs.master.model.api;

import lombok.Data;

import java.util.List;

@Data
public class Pagination {
    private String search;
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private List<String> sortByList;
    private String sortOrder;
}
