package com.easonhotel.web.vip;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.HttpRequest;
import com.easonhotel.common.utils.WxUtils;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.service.IDictService;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.entity.UserPoint;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.easonhotel.dao.web.service.IUserPointService;
import com.easonhotel.dao.web.vo.web.WxLoginParam;
import com.easonhotel.dao.web.vo.web.WxSession;
import com.easonhotel.web.security.LoginRequired;
import com.sun.deploy.net.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 微信小程序用户接口
 */
@RestController
@RequestMapping("/eh/user")
public class VipUserController {
    @Value("${WECHAT.APP.ID}")
    private String wxAppId;

    @Value("${WECHAT.APP.SECRET}")
    private String wxAppSecret;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserPointService userPointService;

    @Autowired
    private IDictService dictService;

    /**
     * 用户登录或注册
     * 当用户注册过则执行登录
     * 没有注册过则自动注册然后登录
     * @param loginParam
     * @return
     * @throws IOException
     */
    @Transactional
    @PostMapping("/login")
    public ResData<Object> login(@RequestBody WxLoginParam loginParam) throws IOException {
        // 通过appid appsecret到腾讯换取用户的openid
        String code2sessionUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?" +
                        "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wxAppId, wxAppSecret, loginParam.getCode());
        String response = HttpRequest.doGet(code2sessionUrl);
        JSONObject responseJson = JSONObject.parseObject(response);
        WxSession wxSession = new WxSession();
        wxSession.setOpenId(responseJson.getString("openid"));
        wxSession.setSessionKey(responseJson.getString("session_key"));
//        if (!WxUtils.valid(loginParam.getRawData(), wxSession.getSessionKey(), loginParam.getSignature())) {
//            return ResData.create(-1, "登录失败");
//        }
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 查询该用户是否存在于数据库中
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query().eq("deleted", 0).eq("openId", wxSession.getOpenId()));
        // 如果数据库中不存在 则直接进行注册
        if (userInfo == null) {
            // 会员信息入库
            userInfo = new UserInfo();
            userInfo.setId(UUID.randomUUID().toString());
            userInfo.setOpenId(wxSession.getOpenId());
            userInfo.setNickName(loginParam.getWxUserInfo().getNickName());
            userInfo.setToken(token);
            userInfo.setRealName(loginParam.getWxUserInfo().getName());
            userInfo.setIdCard(loginParam.getWxUserInfo().getIdCard());
            userInfo.setEmail(loginParam.getWxUserInfo().getEmail());
            userInfo.setPhoneNumber(loginParam.getWxUserInfo().getPhoneNumber());
            // 会员积分信息入库
            UserPoint userPoint = new UserPoint();
            userPoint.setUserId(userInfo.getId());
            userPoint.setPoint(0L);
            userPoint.setLevel("VIP_USER_LEVEL_ONE");
        } else {
            userInfo.setNickName(loginParam.getWxUserInfo().getNickName());
        }
        userInfoService.saveOrUpdate(userInfo);
        return ResData.create(userInfo);
    }

    /**
     * 查询用户余额
     * @param id
     * @return
     */
    @Transactional
    @GetMapping("/balance")
    public ResData<Object> balance(@RequestParam String id) {
        // SELECT * FROM web_user_info WHERE deleted = 0 AND id = id LIMIT 0, 1
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query()
                .eq("deleted", 0)
                .eq("id", id)
                .last("LIMIT 0, 1"));
        return ResData.create(userInfo);
    }

    @GetMapping("/info/{id}")
    public ResData<Object> info(@PathVariable String id) {
        // 查询用户基本信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query()
                .eq("deleted", 0).eq("id", id).last("LIMIT 0, 1"));
        // 查询用户积分等级信息
        UserPoint userPoint = userPointService.getOne(Wrappers.<UserPoint>query()
                .eq("deleted", 0).eq("userId", id).last("LIMIT 0, 1"));
        JSONObject userJson = new JSONObject();
        userJson.put("id", userInfo.getId());
        userJson.put("phoneNumber", userInfo.getPhoneNumber());
        userJson.put("email", userInfo.getEmail());
        userJson.put("nickName", userInfo.getNickName());
        userJson.put("realName", userInfo.getRealName());
        userJson.put("avatar", userInfo.getAvatar());
        userJson.put("balance", userInfo.getBalance());
        userJson.put("idCard", userInfo.getIdCard());
        userJson.put("point", userPoint.getPoint());
        userJson.put("level", dictService.getOne(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .eq("code", userPoint.getLevel()).last("LIMIT 0, 1")).getValue());
        return ResData.create(userJson);
    }

    @PostMapping("/update")
    public ResData<Object> update(@RequestBody UserInfo userInfo) {
        userInfoService.saveOrUpdate(userInfo);
        return ResData.create(0, "操作成功");
    }
}
