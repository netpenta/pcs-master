package th.co.pentasol.pcs.master.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Slf4j
public class Util {


    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
    {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    public static String logInfoMsg(String methodName, String msg, long elapsedTime){
        StringBuilder info = new StringBuilder();
        info.append("" + methodName + " ***** " + msg + " ***** Run Times " + convertElapsedTime(elapsedTime));
        return info.toString();
    }

    public static String logInfoMsg(String methodName, String msg){
        StringBuilder info = new StringBuilder();
        info.append("" + methodName + " ***** " + msg + " *****" );
        return info.toString();
    }

    public static String logErrorMsg(String methodName, String msg){
        StringBuilder info = new StringBuilder();
        info.append("" + methodName + " ***** " + msg + " *****" );
        return info.toString();
    }

    public static String convertElapsedTime(long elapsedTime){
        //1 minute = 60 seconds
        //1 hour   = 60   x 60 = 3600  seconds
        //1 day    = 3600 x 24 = 86400 seconds
        //milliseconds

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli   = minutesInMilli * 60;
        long daysInMilli    = hoursInMilli * 24;

        long elapsedDays = elapsedTime / daysInMilli;
        elapsedTime = elapsedTime % daysInMilli;

        long elapsedHours = elapsedTime / hoursInMilli;
        elapsedTime = elapsedTime % hoursInMilli;

        long elapsedMinutes = elapsedTime / minutesInMilli;
        elapsedTime = elapsedTime % minutesInMilli;

        long elapsedSeconds = elapsedTime / secondsInMilli;

        return String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public static Integer calculateTotalPage(Long totalRecords, Integer pageSize){
        if(NumberUtil.convertToInteger(pageSize) > 0) {
            BigDecimal calcTotalPage = NumberUtil.convertToBigDecimal(totalRecords).divide(NumberUtil.convertToBigDecimal(pageSize), RoundingMode.CEILING);
            return Integer.parseInt(calcTotalPage.setScale(0, RoundingMode.UP).toString());
        }
        return 0;
    }

    public static List<Integer> splitToListInteger(String str){
        List<Integer> dataList = new ArrayList<>();
        if(!Objects.isNull(str) && str.trim().length() > 0){
            String[] splits = str.split(",");
            for(String data : splits){
                if(!dataList.contains(NumberUtil.convertToInteger(data))) {
                    dataList.add(NumberUtil.convertToInteger(data));
                }
            }
        }
        return dataList;
    }

    public static List<String> splitToListString(String str){
        List<String> dataList = new ArrayList<>();
        if(!Objects.isNull(str) && str.trim().length() > 0){
            String[] splits = str.split(",");
            for(String data : splits){
                if(!dataList.contains(data)) {
                    dataList.add(data);
                }
            }
        }
        return dataList;
    }

    public static String generatePassword(Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@!#$%&";
        String password = RandomStringUtils.random( length, characters );

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#$%&])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher( password );

        if (matcher.matches()) {
            return password;
        } else {
            return generatePassword(length);
        }
    }

    public static final String RECORD_STATUS_ACTIVE = "A";
    public static final String RECORD_STATUS_INACTIVE = "I";
    public static final String RECORD_STATUS_ACTIVE_NM = "Active";
    public static final String RECORD_STATUS_INACTIVE_NM = "Inactive";
    public static final String RECORD_STATUS_DELETED_NM = "Deleted";
    public static final String STATUS_ACTIVE_COLOR   = "#EAFFCD";
    public static final String STATUS_INACTIVE_COLOR = "#C6C6C6";
    public static final String STATUS_ACTIVE_COLOR_FONT   = "#8DDE21";
    public static final String STATUS_INACTIVE_COLOR_FONT = "#F0CA00";

    public static Integer convertStatusToActiveFlg(String status){
        return status.equalsIgnoreCase("A") ? 1 : 0;
    }


    public static RecordStatus getRecordStatus(Integer isActiveFlg){
        if(isActiveFlg == 1) {
            return new RecordStatus(RECORD_STATUS_ACTIVE, RECORD_STATUS_ACTIVE_NM, STATUS_ACTIVE_COLOR_FONT, STATUS_ACTIVE_COLOR);
        }else
            return new RecordStatus(RECORD_STATUS_INACTIVE, RECORD_STATUS_INACTIVE_NM, STATUS_INACTIVE_COLOR_FONT, STATUS_INACTIVE_COLOR);
    }

    @Data
    @AllArgsConstructor
    public static class RecordStatus {
        private String status;
        private String statusName;
        private String statusColor;
        private String statusFontColor;
    }

    public static String generateStrongPasswordHash(String password)
    {
        try {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return  null;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static boolean validatePassword(String originalPassword, String storedPassword)
    {
        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);

            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                    salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }

    public static StringBuilder getBatNo(String ipAddress, String userName){
        StringBuilder batNo = new StringBuilder();
        StringBuilder date   = new StringBuilder(new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH).format(((Date) new Date()).getTime()));
        if(Objects.isNull(ipAddress)){
            batNo.append(userName).append("-").append(date);
        }else {
            batNo.append(ipAddress).append("-").append(date);
        }
        return batNo;
    }

    public static boolean isNotEmpty(String value){
        if(value == null || value.trim().length() == 0){
            return false;
        }
        else return true;
    }

    public static boolean isNotEmpty(Date value){
        if(Objects.isNull(value) || value.toString().trim().length() == 0){
            return false;
        }
        else return true;
    }

    public static boolean isNullValue(Object value){
        if(Objects.isNull(value)){
            return false;
        }
        else return true;
    }

    public static boolean isNotEmpty(List<?> value){
        if(value == null || value.size() == 0){
            return false;
        }
        else return true;
    }
}

