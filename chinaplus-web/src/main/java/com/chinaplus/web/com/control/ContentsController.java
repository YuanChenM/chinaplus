/**
 * ContentsController.java
 * 
 * @screen ContentsController
 * @author Common
 */
package com.chinaplus.web.com.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.chinaplus.common.util.ScreenInfoManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.exception.ScreenInitException;

/**
 * ContentsController.
 * 
 * 
 * @author COMMON
 */
@Controller
public class ContentsController extends MultiActionController {

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(ContentsController.class);

    /**
     * Get the screen json data.
     * 
     * 
     * @param baseParam baseParam
     * @param request request
     * @param response response
     * @throws Exception e
     * 
     * @author COMMON
     */
    @RequestMapping(value = { "screenContent" })
    protected void getScreen(@RequestBody PageParam baseParam, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        logger.info("get the screen json content[" + baseParam.getScreenId() + "] start");
        FileInputStream in = null;
        BufferedReader reader = null;

        try {

            SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
            String jsonFile = ScreenInfoManager.getScreenUrlByScreenId(sm.getMainMenuResource(),
                baseParam.getScreenId());
            in = new FileInputStream(getServletContext().getRealPath(jsonFile));
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            response.setContentType("application/json; charset=UTF-8");
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.getWriter().println(line);
            }
        } catch (FileNotFoundException e) {
            throw new ScreenInitException(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {}
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {}
            }
        }

        logger.info("get the screen json content[" + baseParam.getScreenId() + "] end");
    }

}
