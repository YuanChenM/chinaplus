/**
 * JsonDateSerializer.java
 * 
 * @screen common
 * @author ma_b
 */
package com.chinaplus.common.util;

import java.io.IOException;
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
 * Json Date Serializer.
 */
@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

    /**
     * Date Format for english.
     */
    private static final SimpleDateFormat DATE_FORMAT_ENGLISH = new SimpleDateFormat(DateTimeUtil.FORMAT_DDMMMYYYY,
        ChinaPlusConst.Language.ENGLISH.getLocale());

    /**
     * Date Format for chinese.
     */
    private static final SimpleDateFormat DATE_FORMAT_CHINESE = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
        ChinaPlusConst.Language.CHINESE.getLocale());
    
    /**
     * Date format serialize for object mapper.
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
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException,
        JsonProcessingException {
        if (date != null) {
            // format
            gen.writeString(getDateFormat().format(date));
        } else {
            // set empty
            gen.writeString(StringConst.EMPTY);
        }
    }
    
    /**
     * Get Date Format by locale.
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
