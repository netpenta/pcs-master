package th.co.pentasol.pcs.master.model.api;

import lombok.Data;

@Data
public class FileResponse {
    private String fileType;
    private String fileName;
    private String filePath;
    private byte[] file;
}
