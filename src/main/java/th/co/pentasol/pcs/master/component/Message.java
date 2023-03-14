package th.co.pentasol.pcs.master.component;

import th.co.pentasol.pcs.master.model.api.ApiMessage;
import th.co.pentasol.pcs.master.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("all")
@Component
public class Message {
    @Autowired
    MessageSource messageSource;

    public static final String INFO = "INFO";
    public static final String ERROR = "ERROR";
    public static final String WARNING = "WARNING";

    public static final String MSG_SAVE_SUCCESS   = "msg.success.saved.data";
    public static final String MSG_UPDATE_SUCCESS = "msg.success.updated.data";
    public static final String MSG_DELETE_SUCCESS = "msg.success.deleted.data";
    public static final String MSG_DUPLICATE_NAME = "msg.duplicate.name";
    public static final String MSG_REQUIRE_VALUE  = "msg.required.value";
    public static final String MSG_INTERNAL_ERROR = "msg.internal.server.error";
    public static final String MSG_NOT_FOUND = "msg.not.found.data";
    public static final String MSG_FORBIDDEN = "msg.forbidden";
    public static final String MSG_INVALID_USERNAME_PASSWORD = "msg.invalid.username.password";


    public Message(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ApiMessage getSavedMessage(Locale locale){
        return new ApiMessage(INFO, messageSource.getMessage(MSG_SAVE_SUCCESS, null, locale));
    }

    public ApiMessage getUpdatedMessage(Locale locale){
        return new ApiMessage(INFO, messageSource.getMessage(MSG_UPDATE_SUCCESS, null, locale));
    }

    public ApiMessage getDeletedMessage(boolean success, Locale locale){
        return new ApiMessage(INFO, messageSource.getMessage(success ? "msg.success.deleted.data" : "msg.failed.deleted.data", null, locale));
    }

    public ApiMessage getCopiedMessage(Locale locale){
        return new ApiMessage(INFO, messageSource.getMessage(MSG_UPDATE_SUCCESS, null, locale));
    }

    public ApiMessage getNotFoundMessage(Locale locale){
        return new ApiMessage(INFO, messageSource.getMessage(MSG_NOT_FOUND, null, locale));
    }

    public ApiMessage getInternalErrorMessage(Locale locale){
        return new ApiMessage(ERROR, messageSource.getMessage(MSG_INTERNAL_ERROR, null, locale));
    }

    public String getMsgRequireValue(Locale locale, Object param){
        return messageSource.getMessage(MSG_REQUIRE_VALUE, new Object[] {param}, locale);
    }

    public String getMsgDuplicateName(Locale locale){
        return messageSource.getMessage(MSG_DUPLICATE_NAME, null, locale);
    }

    public ApiMessage getForbiddebMessage(Locale locale){
        return new ApiMessage(INFO, messageSource.getMessage(MSG_FORBIDDEN, null, locale));
    }

    public String getMsg(String msgKey, Locale locale){
        return messageSource.getMessage(msgKey, null, locale);
    }

    public String getMsgWithParams(String msgKey, Object[] params, Locale locale){
        return messageSource.getMessage(msgKey, params, locale);
    }

    public ApiMessage getApiMessageInfo(String msgKey, Locale locale){
        return new ApiMessage(INFO, getMsg(msgKey, locale));
    }

    public ApiMessage getApiMessageInfoWithParams(String msgKey, Object[] params, Locale locale){
        return new ApiMessage(INFO, getMsgWithParams(msgKey, params, locale));
    }

    public ApiMessage getApiMessageWarning(String msgKey, Locale locale){
        return new ApiMessage(WARNING, getMsg(msgKey, locale));
    }

    public ApiMessage getApiMessageError(String msgKey, Locale locale){
        return new ApiMessage(ERROR, getMsg(msgKey, locale));
    }

    public ApiMessage getApiMessageWarningWithParams(String msgKey, Object[] params, Locale locale){
        return new ApiMessage(WARNING, getMsgWithParams(msgKey, params, locale));
    }

//    public ApiMessage getApiMessageListWarning(List<String> msgList){
//        return new ApiMessage(WARNING, null, msgList);
//    }

    public ApiMessage getApiMessageInfo(String msg){
        return new ApiMessage(INFO, msg);
    }



    public ApiMessage getInvalidUserNameAndPasswordMessage(Locale locale){
        return new ApiMessage(WARNING, messageSource.getMessage(MSG_INVALID_USERNAME_PASSWORD, null, locale));
    }

    private  Map<Integer, String> getMonthList(Locale locale){
        Map<Integer, String> month = new LinkedHashMap<>();
        month.put(1, getMsg("month.january", locale));
        month.put(2, getMsg("month.february", locale));
        month.put(3, getMsg("month.march", locale));
        month.put(4, getMsg("month.april", locale));
        month.put(5, getMsg("month.may", locale));
        month.put(6, getMsg("month.june", locale));
        month.put(7, getMsg("month.july", locale));
        month.put(8, getMsg("month.august", locale));
        month.put(9, getMsg("month.september", locale));
        month.put(10, getMsg("month.october", locale));
        month.put(11, getMsg("month.november", locale));
        month.put(12, getMsg("month.december", locale));
        return month;
    }

    public String getFullMonthName(Integer month, Locale locale){
        String monthName = "";
        Map<Integer, String> monthList = getMonthList(locale);
        if(NumberUtil.convertToInteger(month) > 0){
            monthName = monthList.get(month);
        }
        return monthName;
    }
}
