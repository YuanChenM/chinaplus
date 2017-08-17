/**
 * JsonDateSerializer.java
 * 
 * @screen common
 * @author li_feng
 */
package com.chinaplus.common.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Json Month Serializer.
 */
@Component
public class JsonMonthSerializer extends JsonSerializer<String> {

    /**
     * DateMonth Format for english.
     */
    private static final SimpleDateFormat DATE_FORMAT_ENGLISH = new SimpleDateFormat(DateTimeUtil.FORMAT_MMMYYYY,
        ChinaPlusConst.Language.ENGLISH.getLocale());

    /**
     * DateMonth Format for chinese.
     */
    private static final SimpleDateFormat DATE_FORMAT_CHINESE = new SimpleDateFormat(DateTimeUtil.FORMAT_YYYYMM,
        ChinaPlusConst.Language.CHINESE.getLocale());
    
    /**
     * DateMonth format serialize for object mapper.
     *
     * @param date
     * @param gen
     * @param provider
     * @throws IOException
     * @throws JsonProcessingException
     * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
     *      com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
     */
    @Override
    public void serialize(String date, JsonGenerator gen, SerializerProvider provider) throws IOException,
        JsonProcessingException {
        if (date != null) {
            // format
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
            Date month = null;
            try {
                month = sdf.parse(date + "01");
            } catch (ParseException e) {
                
            }
            gen.writeString(getDateFormat().format(month));
        } else {
            // set empty
            gen.writeString(StringConst.EMPTY);
        }
    }
    
    /**
     * Get DateMonth Format by locale.
     *
     * @return Date Format
     */
    private SimpleDateFormat getDateFormat() {
        
        // get session manager
        SessionInfoManager ma = SessionInfoManager.getContextInstance();
        // get locale
        Locale locale = ma.getLoginLocale();

        // if chinese
        if (locale != null && ChinaPlusConst.Language.CHINESE.getLocale().equals(locale)) {
            return DATE_FORMAT_CHINESE;
        } else {
            return DATE_FORMAT_ENGLISH;
        }
    }

}
