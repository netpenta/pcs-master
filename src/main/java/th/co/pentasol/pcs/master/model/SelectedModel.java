package th.co.pentasol.pcs.master.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SelectedModel {
    private List<String> selectedList;
    private List<Long> idList;
    @JsonIgnore
    private String userId;
    @JsonIgnore
    private String programId;
    @JsonIgnore
    private Date modifiedDateTime;
}
