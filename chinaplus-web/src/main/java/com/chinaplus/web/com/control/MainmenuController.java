/**
 * @screen main
 * @author liu_yinchuan
 */
package com.chinaplus.web.com.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.OfficeResource;
import com.chinaplus.common.bean.TreeNode;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.ScreenInfoManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.AuthenticationException;
import com.chinaplus.core.exception.TimeoutException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.MainmenuAuthEntity;
import com.chinaplus.web.com.entity.OfficeAuthEntity;

/**
 * ContentsController.
 */
@Controller
public class MainmenuController extends BaseController {

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(MainmenuController.class);

    /**
     * 
     * <p>
     * the initialization method main page.
     * </p>
     * 
     * @param model page info
     * @param request request
     * @param response response
     * @return main.jsp
     */
    @RequestMapping(value = "/main")
    public String main(Model model, HttpServletRequest request, HttpServletResponse response) {
        
        // get user information
        UserInfo user = super.getLoginUser(request);
        if (user != null && user.getLanguage() != null) {
            
            // get locale
            Locale loc = user.getLanguage().getLocale();

            // set message
            request.setAttribute("loadStyles", MessageManager.getMessage("Common_Label_LoadStyles", loc));
            request.setAttribute("loadUIComp", MessageManager.getMessage("Common_Label_LoadUIComp", loc));
            request.setAttribute("loadCoreAPI", MessageManager.getMessage("Common_Label_LoadCoreAPI", loc));
            request.setAttribute("loadResource", MessageManager.getMessage("Common_Label_LoadResource", loc));
            request.setAttribute("loadSystemAPI", MessageManager.getMessage("Common_Label_LoadSystemAPI", loc));
        } else {
            
            // throw exception
            throw new TimeoutException();
        }

        logger.info("come into main page...");
        return "mainmenu";
    }

    /**
     * 
     * <p>
     * the initialization method resetpw page.
     * </p>
     * 
     * @param model page info
     * @param request request
     * @param response response
     * @return main.jsp
     */
    @RequestMapping(value = "/resetpw")
    public String resetpw(Model model, HttpServletRequest request, HttpServletResponse response) {

        logger.info("come into resetpw page...");

        return "resetpw";
    }

    /**
     * Get the accessable resource.
     * 
     * 
     * @param request request
     * @return menu
     * @throws Exception e
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/menu" })
    @ResponseBody
    protected JSONArray getMenuData(HttpServletRequest request) throws Exception {

        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        UserManager user = UserManager.getLocalInstance(context);
        JSONArray tree = buildTree(user.getResourceByOffice(), context.getLoginLocale());

        return tree;
    }

    /**
     * Build menu tree.
     * 
     * 
     * @param accessAbleResources access able resources
     * @param locale locale
     * @return menu tree
     * 
     * @author Common
     */
    private JSONArray buildTree(List<OfficeResource> accessAbleResources, Locale locale) {

        /**
         * [ { rootText:'imp', treeContent: { root:{ children: [ { url:'url', text:'text', leaf:true, id:'co' }, {
         * text:'text', leaf:true, id:'co' }{ text:'', children:[
         * 
         * ] } ] } }
         * 
         * }, { exp:{} }, { ad:{} }, { sys:{} } ]
         */

        int size = accessAbleResources.size();

        JSONArray leafArray = null;

        String treeType = null;
        JSONArray treeArray = new JSONArray();

        for (int i = 0; i < size; i++) {

            // get entity
            OfficeResource entity = accessAbleResources.get(i);

            // if no access level
            if (entity.getAccessLevel() == CodeConst.AccessLevel.NONE) {
                continue;
            }

            // if root change, then set into new root
            if (!entity.getRootId().equals(treeType)) {

                // set leaf
                setTreeLeaf(treeArray, leafArray, MessageManager.getMessage(treeType, locale));

                // new JSONArray
                leafArray = new JSONArray();

                // checker
                treeType = entity.getRootId();
            }

            // set node
            TreeNode node = new TreeNode();
            node.setText(MessageManager.getMessage(entity.getResourceId(), locale));
            node.setId(entity.getRootId().concat(entity.getResourceId()));
            node.setRoot(entity.getRootId());
            node.setScreenId(entity.getResourceId());
            // node.setUrl(entity.getResourceUrl());
            // node.setAccessLevel(entity.getAccessLevel());
            node.setLeaf(true);
            leafArray.add(node);
        }

        // set leaf
        setTreeLeaf(treeArray, leafArray, MessageManager.getMessage(treeType, locale));

        return treeArray;
    }

    /**
     * set tree array.
     * 
     * @param treeArray treeArray
     * @param leafArray leafArray
     * @param rootText rootText
     */
    private void setTreeLeaf(JSONArray treeArray, JSONArray leafArray, String rootText) {

        // for last one
        // set root
        if (leafArray != null && leafArray.size() > 0) {

            // set root
            JSONObject treeContent = new JSONObject();
            JSONObject treeContentRoot = new JSONObject();

            treeContentRoot.put("children", leafArray);
            treeContent.put("root", treeContentRoot);

            JSONObject rootTree = new JSONObject();
            rootTree.put("rootText", rootText);
            rootTree.put("treeContent", treeContent);

            treeArray.add(rootTree);
        }
    }

    /**
     * Get the language resource.
     * 
     * 
     * @param request request
     * @return menu
     * @throws Exception e
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/language" })
    @ResponseBody
    protected List<ComboData> getSystemLanuage(HttpServletRequest request) throws Exception {

        // init
        List<ComboData> lanuageLst = new ArrayList<ComboData>();

        // get languages from property files
        List<String> langLst = ConfigManager.getSystemLanguages();

        for (String lang : langLst) {

            // re init
            ComboData langCate = new ComboData();

            // set data
            langCate.setId(lang);
            langCate.setText(MessageManager.getMessage(lang));

            // set into list
            lanuageLst.add(langCate);
        }

        return lanuageLst;
    }

    /**
     * Get the language resource.
     * 
     * 
     * @param request request
     * @return menu
     * @throws Exception e
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/officelist" })
    @ResponseBody
    protected List<ComboData> getUserOffice(HttpServletRequest request) throws Exception {

        // get user manager
        UserInfo ui = getLoginUser(request);

        // init
        List<ComboData> cateLst = new ArrayList<ComboData>();

        for (UserOffice office : ui.getUserOffice()) {

            // re init
            ComboData cateEnti = new ComboData();

            // set data
            cateEnti.setId(String.valueOf(office.getOfficeId()));
            cateEnti.setText(office.getOfficeCode());

            // set into list
            cateLst.add(cateEnti);
        }

        return cateLst;
    }

    /**
     * Get the accessable resource.
     * 
     * @param param param
     * @param request request
     * @throws Exception e
     * @return result
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/changelanguage" })
    @ResponseBody
    protected BaseResult<String> changeLanguage(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {
        UserInfo user = getLoginUser(request);
        if (param.getSwapData().get("language") != null) {
            String language = param.getSwapData().get("language").toString();
            // reset language
            user.setLanguage(MessageManager.getLanguage(language));
            // set into session
            // get office
            List<UserOffice> officeLst = user.getUserOffice();

            // get offerCode + date
            String dataDateTime = "";
            SimpleDateFormat sdfE = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);
            SimpleDateFormat sdfZ = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (int i = 0; i < officeLst.size(); i++) {
                if (i > IntDef.INT_ONE) {
                    break;
                }
                UserOffice uo = officeLst.get(i);
                if (null != uo.getImpSyncTime()) {
                    String value = "";
                    if (language.equals(Language.CHINESE.getName())) {
                        value = sdfZ.format(uo.getImpSyncTime());
                    } else {
                        value = sdfE.format(uo.getImpSyncTime());
                    }
                    dataDateTime += "Last " + uo.getOfficeCode() + " LOGISTICS I/F Time: " + value;
                } else {
                    dataDateTime += "Last " + uo.getOfficeCode() + " LOGISTICS I/F Time: " + "N/A";
                }
                dataDateTime += StringConst.BLANK_JS + StringConst.BLANK_JS + StringConst.BLANK_JS;
            }

            // set dataDateTime
            user.setDataDateTime(dataDateTime);

            // set into user
            SessionInfoManager.getContextInstance(request).setLoginInfo(user);
        }
        return new BaseResult<String>();
    }

    /**
     * Get the accessable resource.
     * 
     * @param param param
     * @param request request
     * @throws Exception e
     * @return result
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/changeoffice" })
    @ResponseBody
    protected BaseResult<String> changeOffice(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {

        // get user information
        UserManager um = UserManager.getLocalInstance(this.getLoginUser(request));

        if (param.getSwapData().get("officeId") != null) {

            // get office id
            Integer officeId = Integer.valueOf(param.getSwapData().get("officeId").toString());

            // check office exists
            if (um.checkOfficeForUser(officeId)) {

                // set
                um.changeCurrentOffice(officeId);
            }
        }

        return new BaseResult<String>();
    }

    /**
     * Get the accessable resource.
     * 
     * @param param param
     * @param request request
     * @throws Exception e
     * @return result
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/mainResource" })
    @ResponseBody
    protected BaseResult<MainmenuAuthEntity> mainResource(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {

        // get user information
        String screenId = param.getScreenId();
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);

        // check V-V and aisin
        UserInfo userInfo = um.getUserInfo();

        String rootId = (String) param.getSwapData().get("rootId");
        if (rootId.equals(CoreConst.CPV00000)) {
            userInfo.setVvAisinFlag(CoreConst.VV);
        } else if (rootId.equals(CoreConst.CPA00000)) {
            userInfo.setVvAisinFlag(CoreConst.AISIN);
        } else {
            userInfo.setVvAisinFlag(null);
        }
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        context.setLoginInfo(userInfo);

        // get auth type
        Integer authType = ScreenInfoManager.getScreenAuthType(screenId, screenId);
        if (authType == null) {
            throw new AuthenticationException();
        }

        // get access level
        Integer accessLevel = CodeConst.AccessLevel.NONE;
        switch (authType) {
            case CodeConst.AuthType.NON:
                accessLevel = CodeConst.AccessLevel.MAINTAINERS;
                break;
            case CodeConst.AuthType.ALL:
                accessLevel = um.getMaxAccessLevel(screenId);
                break;
            case CodeConst.AuthType.OFFICE:
                accessLevel = um.getAccessLevel(screenId);
                break;
        }

        // if no access Level
        if (accessLevel.intValue() == CodeConst.AccessLevel.NONE) {
            throw new AuthenticationException();
        }

        // set main resource
        sm.setMainMenuResource(screenId);

        // set MainmenuAuthEntity
        MainmenuAuthEntity authEntity = new MainmenuAuthEntity();
        authEntity.setAccessLevel(accessLevel);
        authEntity.setMainResourceId(screenId);
        authEntity.setScreenAuthInfo(ScreenInfoManager.getScreenAuthInfoByRoot(screenId));
        // if all
        if (authType.compareTo(CodeConst.AuthType.ALL) == 0) {

            // prepare office auth list
            List<OfficeAuthEntity> officeLst = new ArrayList<OfficeAuthEntity>();
            for (UserOffice office : um.getUserInfo().getUserOffice()) {
                // new entity
                OfficeAuthEntity officeAuth = new OfficeAuthEntity();
                officeAuth.setOfficeId(office.getOfficeId());
                officeAuth.setAccessLevel(um.getAccessLevel(screenId, office.getOfficeId()));
                officeLst.add(officeAuth);
            }

            // set office list
            authEntity.setOfficeLst(officeLst);
        }

        // return accessLevel
        BaseResult<MainmenuAuthEntity> result = new BaseResult<MainmenuAuthEntity>();
        result.setData(authEntity);

        // return
        return result;
    }

    /**
     * check download result.
     * 
     * @param param parameter
     * @param request request
     * @throws Exception e
     * @return result
     * 
     * @author Common
     */
    @RequestMapping(value = { "/main/checkDownload" })
    @ResponseBody
    protected BaseResult<String> checkDownload(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {

        // do check
        // remove information into session
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);

        // set session data
        String jsonData = StringUtil.toSafeString(param.getJsonData());
        String token = StringUtil.toSafeString(param.getToken());
        String screenId = StringUtil.toSafeString(param.getScreenId());
        String clientTime = StringUtil.toSafeString(param.getClientTime());

        // prepare keys
        String downloadKey = StringUtil.arrayToString(new String[] { jsonData, token, screenId, clientTime });

        // prepare
        BaseResult<String> result = new BaseResult<String>();
        result.setData(StringUtil.toSafeString(sm.get(downloadKey)));

        return result;
    }

}
