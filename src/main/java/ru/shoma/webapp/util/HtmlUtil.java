package ru.shoma.webapp.util;

import ru.shoma.webapp.model.Organization;

/**
 * Created by Shoma on 26.08.2018.
 */
public class HtmlUtil {
    public static String formatDates(Organization.Position position){
        return DateUtil.format(position.getStartDate())+" - "+ DateUtil.format(position.getEndDate());
    }
    public static boolean isEmpty(String str){
        return str==null||str.trim().length()==0;
    }
}
