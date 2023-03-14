package th.co.pentasol.pcs.master.util;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("ALL")
public class MySqlUtil {
    public static String valueLike(String value){
        return Objects.isNull(value) ? "" : "%" + value.trim().replaceAll("'", "\'") + "%";
    }

    public static String valueLikeLeft(String value){
        return "%" + value.trim().replaceAll("'", "\'");
    }

    public static String valueLikeRight(String value){
        return value.trim().replaceAll("'", "\'") + "%";
    }

    public static String orderBy(String sortBy, String sortOrder) {
        if(!Objects.isNull(sortBy)){
               sortBy = " ORDER BY " + sortBy + " " + (Util.isNotEmpty(sortOrder) ? sortOrder.toUpperCase() + " " : "ASC ");
        }
        return sortBy;
    }

    public static String multipleOrderBy(List<String> multiSortBy, String sortOrder) {
        StringBuilder sortBy = new StringBuilder("");
        if(!Objects.isNull(multiSortBy)){
            int count = 0;
            sortBy.append("ORDER BY ");
            for(String sortField : multiSortBy){
                sortBy.append(sortField + " " + (Util.isNotEmpty(sortOrder) ? sortOrder.toUpperCase() + " " : "ASC "));
                if(count < multiSortBy.size()-1) sortBy.append(", ");
                else sortBy.append(" ");
                count++;
            }
        }
        return sortBy.toString();
    }

    public static String limit(String orderBy, Integer pageNo, Integer pageSize){
        if(NumberUtil.convertToInteger(pageSize) > 0) {
              Integer pageRow = (Objects.isNull(pageNo) ? 0 : pageNo) * (Objects.isNull(pageSize) ? 50 : pageSize);
              return (Objects.isNull(orderBy) || orderBy.trim().length() == 0 ? "" : orderBy) + " LIMIT " + pageRow + ", " + pageSize;
        }else return (Objects.isNull(orderBy) || orderBy.trim().length() == 0 ? "" : orderBy);
    }

    public static String limit(Integer pageNo, Integer pageSize){
        if(NumberUtil.convertToInteger(pageSize) > 0) {
                Integer pageRow = (Objects.isNull(pageNo) ? 0 : pageNo) * (Objects.isNull(pageSize) ? 50 : pageSize);
                return " LIMIT " + pageRow + ", " + pageSize;
        }else  return "";
    }

    public static String rowCountSql(String sql){
        StringBuilder sqlRowCount = new StringBuilder("\n");
        sqlRowCount.append("SELECT COUNT(1) FROM(\n");
        sqlRowCount.append(sql);
        sqlRowCount.append(") as count_row ");
        return sqlRowCount.toString();
    }

    public static String rowCountSql(StringBuilder sql){
        StringBuilder sqlRowCount = new StringBuilder("\n");
        sqlRowCount.append("SELECT COUNT(1) FROM(\n");
        sqlRowCount.append(sql);
        sqlRowCount.append(") as count_row ");
        return sqlRowCount.toString();
    }

    public static String getConditionSelected(List<String> dataList){
        StringBuilder selected = new StringBuilder();
        if(dataList != null && dataList.size() > 0){
            AtomicInteger count = new AtomicInteger(0);
            dataList.stream().forEach(data->{
                selected.append("'" + data + "' ");
                selected.append(dataList.size()-1 == count.get() ? "" : ", ");
                count.incrementAndGet();
            });
        }
        return selected.toString();
    }

    public static String getConditionIntSelected(List<Integer> dataList){
        StringBuilder selected = new StringBuilder();
        if(dataList != null && dataList.size() > 0){
            AtomicInteger count = new AtomicInteger(0);
            dataList.stream().forEach(data->{
                selected.append(data);
                selected.append(dataList.size()-1 == count.get() ? "" : ", ");
                count.incrementAndGet();
            });
        }
        return selected.toString();
    }

    public static String getConditionLongSelected(List<Long> dataList){
        StringBuilder selected = new StringBuilder();
        if(dataList != null && dataList.size() > 0){
            AtomicInteger count = new AtomicInteger(0);
            dataList.stream().forEach(data->{
                selected.append(data);
                selected.append(dataList.size()-1 == count.get() ? "" : ", ");
                count.incrementAndGet();
            });
        }
        return selected.toString();
    }
}
