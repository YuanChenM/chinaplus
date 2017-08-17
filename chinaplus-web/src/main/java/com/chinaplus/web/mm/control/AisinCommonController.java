/**
 * AisinCommonControl.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.exception.UploadException;
import com.chinaplus.web.mm.entity.AisinCommonEntity;
import com.chinaplus.web.mm.service.AisinCommonService;
import com.chinaplus.web.mm.service.CPMPMS01Service;

/**
 * AisinCommonControl.
 */
@Controller
public class AisinCommonController extends BaseController {

    /**
     * aisinCommonService.
     */
    @Autowired
    private AisinCommonService aisinCommonService;
    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMPMS01Service cpmpms01Service;

    /**
     * deal tianjing and shanghai
     * 
     * @param datalists datalists
     * @param minFirstEtd minFirstEtd
     * @param maxLastEtd maxLastEtd
     * @param type type
     * @throws UploadException UploadException
     * @return maps maps
     */
    public Map<String, Object> dealUpdateData(List<AisinCommonEntity> datalists, long minFirstEtd, long maxLastEtd, int type)
        throws UploadException {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (datalists != null && datalists.size() > 0) {
            maps = aisinCommonService.dealUpdateData(datalists, minFirstEtd, maxLastEtd, type);
        }
        return maps;
    }

}
