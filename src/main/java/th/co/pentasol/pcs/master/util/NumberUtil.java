package th.co.pentasol.pcs.master.util;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@SuppressWarnings("ALL")
@Slf4j
public class NumberUtil {
    public static boolean IsNumeric(String strNumeric){
        strNumeric = strNumeric.replaceAll(",", "");
        try{
                new BigDecimal(strNumeric);
                return true;
        }catch(NumberFormatException ex){
                log.error("Invalid Numeric : " + strNumeric, ex);
                return false;
        }
    }

    public static Integer convertToInteger(Object value){
        BigDecimal intValue = new BigDecimal(0);
        if(value != null && value.toString().trim().length() > 0){
            value = value.toString().replaceAll(",", "");
            value = value.toString().replaceAll(" ", "");
            if(IsNumeric(value.toString())) intValue = new BigDecimal(value.toString());
        }
        return intValue.intValue();
    }

    public static Double convertToDouble(Object value){
        BigDecimal decimalValue = new BigDecimal(0);
        if(value != null && value.toString().trim().length() > 0){
            value = value.toString().replaceAll(",", "");
            value = value.toString().replaceAll(" ", "");
            if(IsNumeric(value.toString())) decimalValue = new BigDecimal(value.toString());
        }
        return decimalValue.doubleValue();
    }

    public static BigDecimal convertToBigDecimal(Object value){
        BigDecimal decimalValue = new BigDecimal(0);
        if(value != null && value.toString().trim().length() > 0){
            value = value.toString().replaceAll(",", "");
            value = value.toString().replaceAll(" ", "");
            if(IsNumeric(value.toString())) decimalValue = new BigDecimal(value.toString());
        }
        return decimalValue;
    }

    public static double withTwoDecimalPlaces(Object value) {
        DecimalFormat df = new DecimalFormat("#.00");
        return new Double(df.format(convertToDouble(value)));
    }
}
