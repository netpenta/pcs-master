package th.co.pentasol.pcs.master.util;

@SuppressWarnings("ALL")
public class ApiStatus {

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATE = 201;
    public static final int STATUS_NO_CONTENT = 204;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;


    public static final String STATUS_OK_MSG = "OK";
    public static final String STATUS_CREATE_MSG = "Created";
    public static final String STATUS_CONTENT_MSG = "No Content";
    public static final String STATUS_BAD_REQUEST_MSG = "Bad Request";
    public static final String STATUS_UNAUTHORIZED_MSG = "Unauthorized";
    public static final String STATUS_FORBIDDEN_MSG = "Forbidden";
    public static final String STATUS_NOT_FOUND_MSG = "Not Found";
    public static final String STATUS_INTERNAL_SERVER_ERROR_MSG = "Internal Server Error";
    public static final String STATUS_SUCCESS = "Success";
    public static final String STATUS_FAILED = "Failed";

    public static final int ACTIVITY_TYPE_LOGIN  = 1;
    public static final int ACTIVITY_TYPE_LOGOUT = 2;
}
