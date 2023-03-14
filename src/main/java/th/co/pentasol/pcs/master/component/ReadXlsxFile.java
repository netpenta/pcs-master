package th.co.pentasol.pcs.master.component;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import th.co.pentasol.pcs.master.util.DateTimeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("ALL")
@Data
public class ReadXlsxFile {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private InputStream fis;

    public ReadXlsxFile(InputStream file, int sheetNo) throws IOException{
        this.fis      = file;
        this.workbook = new XSSFWorkbook(fis);
        this.sheet    = workbook.getSheetAt(sheetNo);
    }

    public ReadXlsxFile(InputStream file) throws IOException{
        this.fis      = file;
        this.workbook = new XSSFWorkbook(fis);
    }

    public int getTotalSheets(){
        int total = 0;
        try{
            total = workbook.getNumberOfSheets();
            workbook.close();
            fis.close();
        }catch (Exception ex) {
            logger.error(ex);
        }
        return total;
    }

    public String getSheetName(Integer sheetNo){
        return workbook.getSheetName(sheetNo);
    }

    public Map<Integer, List<String>> read(Integer sheetNo, Integer startRowNo) {
        Map<Integer, List<String>> data = new LinkedHashMap<>();
        try{

            if(!Objects.isNull(sheetNo)) sheet = workbook.getSheetAt(sheetNo);
            workbook.setForceFormulaRecalculation(true);

            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.clearAllCachedResultValues();

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isRowEmpty(row)) {
                    continue;
                }

                int lastColumn = row.getLastCellNum();
                if(row.getRowNum() >= startRowNo){
                    data.put(row.getRowNum(), new ArrayList<String>());
                    for (int column = 0; column < lastColumn; column++) {
                        Cell cell = row.getCell(column);
                        if (!Objects.isNull(cell)) {
                            switch (cell.getCellType()) {
                                case STRING : data.get(row.getRowNum()).add(formatter.formatCellValue(cell).trim()); break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                                        data.get(row.getRowNum()).add(DateTimeUtil.convertDateToddMMyyyy(date, Locale.ENGLISH).toString());
                                    } else {
                                        data.get(row.getRowNum()).add(new BigDecimal(String.valueOf(cell.getNumericCellValue())).toPlainString());
                                    }
                                    break;
                                case BOOLEAN: data.get(row.getRowNum()).add(cell.getBooleanCellValue() + ""); break;
                                case FORMULA: data.get(row.getRowNum()).add(formatter.formatCellValue(cell, evaluator)); break;
                                default: data.get(row.getRowNum()).add(" "); break;
                            }
                        }
                        else {
                            data.get(row.getRowNum()).add(" ");
                        }
                    }
                }
            }

        }catch (Exception ex) {
            logger.error(ex);
        }finally {
            try {
                workbook.close();
                fis.close();
            } catch (IOException ioEx) {
                logger.error(ioEx);
            }
        }
        return data;
    }

    private static boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        DataFormatter dataFormatter = new DataFormatter();

        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }

        return isEmpty;
    }
}

