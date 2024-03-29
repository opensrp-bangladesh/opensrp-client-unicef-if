package org.ei.opensrp.cursoradapter;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.commonregistry.CommonFtsObject;
import org.ei.opensrp.util.StringUtil;

import java.util.List;

/**
 * Created by raihan on 3/17/16.
 */
public class SmartRegisterQueryBuilder {
    String Selectquery;

    public String getSelectquery() {
        return Selectquery;
    }

    public void setSelectquery(String selectquery) {
        Selectquery = selectquery;
    }

    public SmartRegisterQueryBuilder(String selectquery) {
        Selectquery = selectquery;
    }

    public SmartRegisterQueryBuilder() {
    }

    /*
            This method takes in @param tablename and columns other than ID. Any special conditions
            for sorting if required can also be added in condition string and if not you can pass null.
            Alertname is the name of the alert you would like to sort this by.
             */
    public  String queryForRegisterSortBasedOnRegisterAndAlert(String tablename,String[]columns,String condition,String AlertName){
        Selectquery = "Select "+tablename+".id as _id";

        for(int i = 0;i<columns.length;i++){
            Selectquery= Selectquery + " , " + columns[i];
        }
        Selectquery= Selectquery+ " FROM " + tablename;
        Selectquery = Selectquery+ " LEFT JOIN alerts ";
        Selectquery = Selectquery+ " ON "+ tablename +".id = alerts.caseID";
        if(condition != null){
            Selectquery= Selectquery+ " WHERE " + condition + " AND";
        }
        Selectquery= Selectquery+ " WHERE " + "alerts.scheduleName = '" + AlertName + "' ";
        Selectquery = Selectquery + "ORDER BY CASE WHEN alerts.status = 'urgent' THEN '1'\n" +
                "WHEN alerts.status = 'upcoming' THEN '2'\n" +
                "WHEN alerts.status = 'normal' THEN '3'\n" +
                "WHEN alerts.status = 'expired' THEN '4'\n" +
                "WHEN alerts.status is Null THEN '5'\n" +
                "Else alerts.status END ASC";
        return Selectquery;
    }
    public String queryForCountOnRegisters(String tablename,String condition){
        String Selectquery = "SELECT COUNT (*) ";
        Selectquery= Selectquery+ " FROM " + tablename;
        if(condition != null){
            Selectquery= Selectquery+ " WHERE " + condition ;
        }
        return Selectquery;
    }
    public String addlimitandOffset(String selectquery,int limit,int offset){
        return selectquery + " LIMIT " +offset+","+limit;
    }
    public String limitandOffset(int limit,int offset){
        return Selectquery + " LIMIT " +offset+","+limit;
    }
    public  String Endquery(String selectquery){
        return selectquery+";";
    }
    public String SelectInitiateMainTable(String tablename,String [] columns){
        Selectquery = "Select "+tablename+".id as _id";

        for(int i = 0;i<columns.length;i++){
            Selectquery= Selectquery + " , " + columns[i];
        }
        Selectquery= Selectquery+ " FROM " + tablename;
        return Selectquery;
    }
    public String SelectInitiateMainTableCounts(String tablename){
        Selectquery = "SELECT COUNT(*)";
        Selectquery= Selectquery+ " FROM " + tablename;
        return Selectquery;
    }
    public String mainCondition(String condition){
        Selectquery= Selectquery+ " WHERE " + condition ;
        return Selectquery;
    }
    public String addCondition(String condition){
        Selectquery= Selectquery + condition ;
        return Selectquery;
    }
    public String orderbyCondition(String condition){
        Selectquery= Selectquery + " ORDER BY " + condition;
        return Selectquery;
    }
    public String joinwithALerts(String tablename,String alertname){
        Selectquery = Selectquery+ " LEFT JOIN alerts ";
        Selectquery = Selectquery+ " ON "+ tablename +".id = alerts.caseID AND  alerts.scheduleName = '"+alertname+"'" ;
        return Selectquery;
    }
    public String joinwithALerts(String tablename){
        Selectquery = Selectquery+ " LEFT JOIN alerts ";
        Selectquery = Selectquery+ " ON "+ tablename +".id = alerts.caseID " ;
        return Selectquery;
    }
    public String customJoin(String query){
        Selectquery = Selectquery+ " "+query;
        return Selectquery;
    }
    @Override
    public String toString(){
        return Selectquery;
    }

    public String toStringFts(List<String> ids, String idColumn){
        String res = Selectquery;

        // Remove where clause, Already used when fetching ids
        if(StringUtils.containsIgnoreCase(res, "WHERE")){
            res = res.substring(0, res.toUpperCase().indexOf("WHERE"));
        }

        if(ids.isEmpty()){
            res += String.format(" WHERE %s IN () ", idColumn);;
        }else {
            String joinedIds = "'" + StringUtils.join(ids, "','") + "'";
            res += String.format(" WHERE %s IN (%s) ", idColumn, joinedIds);
        }

        return res;
    }

    public String toStringFts(List<String> ids, String idColumn, String sort){
        String res = Selectquery;

        // Remove where clause, Already used when fetching ids
        if(StringUtils.containsIgnoreCase(res, "WHERE")){
            res = res.substring(0, res.toUpperCase().indexOf("WHERE"));
        }

        if(ids.isEmpty()){
            res += String.format(" WHERE %s IN () ", idColumn);;
        }else {
            String joinedIds = "'" + StringUtils.join(ids, "','") + "'";
            res += String.format(" WHERE %s IN (%s) ", idColumn, joinedIds);

            if (StringUtils.isNotBlank(sort)) {
                if(innerSort(sort)) {
                    res += " ORDER BY CASE " + idColumn;
                    for (int i = 0; i < ids.size(); i++) {
                        res += " WHEN '" + ids.get(i) + "' THEN " + i;
                    }
                    res += " END ";
                } else {
                    res += " ORDER BY " + sort;
                }
            }
        }

        return res;
    }

    public String searchQueryFts(String tablename, String searchJoinTable, String mainCondition, String searchFilter, String sort, int limit, int offset){
        if(StringUtils.isNotBlank(searchJoinTable) && StringUtils.isNotBlank(searchFilter)){
            String query = "SELECT " + CommonFtsObject.idColumn + " FROM " + CommonFtsObject.searchTableName(tablename)  + phraseClause(tablename, searchJoinTable, mainCondition, searchFilter) + orderByClause(sort) + limitClause(limit, offset);
            return query;
        }
        String query = "SELECT " + CommonFtsObject.idColumn + " FROM " + CommonFtsObject.searchTableName(tablename)  + phraseClause(mainCondition, searchFilter) + orderByClause(sort) + limitClause(limit, offset);
        return query;
    }


    public String countQueryFts(String tablename, String searchJoinTable, String mainCondition, String searchFilter){
        if(StringUtils.isNotBlank(searchJoinTable) && StringUtils.isNotBlank(searchFilter)){
            String query = "SELECT " + CommonFtsObject.idColumn + " FROM " + CommonFtsObject.searchTableName(tablename)  + phraseClause(searchJoinTable, mainCondition, searchFilter);
            return query;
        }
        String query = "SELECT " + CommonFtsObject.idColumn + " FROM " + CommonFtsObject.searchTableName(tablename)  + phraseClause(mainCondition, searchFilter);
        return query;
    }

    private String phraseClause(String mainCondition, String phrase){
        if(StringUtils.isNotBlank(phrase)) {
            String phraseClause = " WHERE " + mainConditionClause(mainCondition) + CommonFtsObject.phraseColumnName + " MATCH '" + phrase + "*'";
            return phraseClause;
        }else if(StringUtils.isNotBlank(mainCondition)){
            return " WHERE " + mainCondition;
        }
        return "";
    }

    private String phraseClause(String joinTable, String mainCondition, String phrase){
        String phraseClause = " WHERE " + mainConditionClause(mainCondition) + CommonFtsObject.phraseColumnName + " MATCH '" + phrase + "*'" +
                    " UNION SELECT " + CommonFtsObject.relationalIdColumn + " FROM " + CommonFtsObject.searchTableName(joinTable) + " WHERE " + CommonFtsObject.phraseColumnName + " MATCH '" + phrase + "*'";
        return phraseClause;
    }

    private String phraseClause(String tableName, String joinTable, String mainCondition, String phrase){
        String phraseClause = " WHERE " + CommonFtsObject.idColumn + " IN ( SELECT " + CommonFtsObject.idColumn + " FROM " + CommonFtsObject.searchTableName(tableName) + " WHERE " + mainConditionClause(mainCondition) + CommonFtsObject.phraseColumnName + " MATCH '" + phrase + "*'" +
                " UNION SELECT " + CommonFtsObject.relationalIdColumn + " FROM " + CommonFtsObject.searchTableName(joinTable) + " WHERE " + CommonFtsObject.phraseColumnName + " MATCH '" + phrase + "*' )";
        return phraseClause;
    }

    private String orderByClause(String sort){
        if(StringUtils.isNotBlank(sort) && innerSort(sort)){
            return " ORDER BY " + sort;
        }
        return "";
    }

    private String limitClause(int limit, int offset){
        return " LIMIT " +  offset + "," + limit;
    }

    private boolean innerSort(String sort){
        return !sort.contains("alerts".trim()) && !StringUtils.containsIgnoreCase(StringUtils.normalizeSpace(sort), "case when");
    }

    private String mainConditionClause(String mainCondition){
        if(StringUtils.isNotBlank(mainCondition)){
            return mainCondition += " AND ";
        }else{
            return mainCondition = "";
        }
    }
}
