package com.easonhotel.web.epidemic;

import com.alibaba.fastjson.JSONObject;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.HttpRequest;
import com.easonhotel.common.utils.HttpsRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/eh/epidemic")
public class EpidemicController {
    /**
     * 查询key 用于通过验证
     */
    private String KEY = "5758542001e82e57ea719945c2ae018a";

    /**
     * 查询那些城市被支持的接口地址
     */
    private String CITY_LIST_QUERY_URL = "http://apis.juhe.cn/springTravel/citys?key=%s";

    /**
     * 查询防疫政策接口地址
     */
    private String EPIDEMIC_POLICY_QUERY_URL = "http://apis.juhe.cn/springTravel/query?key=%s&from=%s&to=%s";

    /**
     * 查询风险地区接口地址
     */
    private String RISK_REGION_QUERY_URL = "http://apis.juhe.cn/springTravel/risk?key=%s";

    /**
     * 查询城市清单 反馈给前端支持哪些城市的查询
     * @author PengYiXing
     * @return
     */
    @GetMapping("/cityList")
    public ResData<Object> cityList() {
        String resStr = null;
        // 发起HTTP请求查询城市列表
        try {
            resStr = HttpRequest.doGet(String.format(CITY_LIST_QUERY_URL, KEY));
        } catch (IOException e) {
            e.printStackTrace();
            return ResData.create(-1, "请求异常");
        }
        JSONObject resJson = JSONObject.parseObject(resStr);
        List<JSONObject> provinceList = new ArrayList<>();
        // 封装数据 城市ID，城市名称，所属省份
        if (resJson.getIntValue("error_code") != 0) {
            return ResData.create(new ArrayList<JSONObject>());
        }
        for (JSONObject provinceJson : (List<JSONObject>) resJson.get("result")) {
            JSONObject json = new JSONObject();
            json.put("id", provinceJson.getString("province_id"));
            json.put("name", provinceJson.getString("province"));
            List<JSONObject> cityJsonArray = new ArrayList<>();
            for (JSONObject cityItem : (List<JSONObject>) provinceJson.get("citys")) {
                JSONObject cityJson = new JSONObject();
                cityJson.put("id", cityItem.getString("city_id"));
                cityJson.put("name", cityItem.getString("city"));
                cityJsonArray.add(cityJson);
            }
            json.put("cityList", cityJsonArray);
            provinceList.add(json);
        }
        return ResData.create(provinceList);
    }

    /**
     * 查询出发地和目的地防疫政策
     * @param from 出发地城市id，注意与我们自己系统的城市ID不一样，由cityList接口获取到
     * @param to 目的地城市id，同上
     * @author PengYiXing
     * @return
     */
    @GetMapping("/policy")
    public ResData<Object> policy(@RequestParam String from, @RequestParam String to) {
        String resStr = null;
        // 请求出行政策
        try {
            resStr = HttpRequest.doGet(String.format(EPIDEMIC_POLICY_QUERY_URL, KEY, from, to));
        } catch (IOException e) {
            e.printStackTrace();
            return ResData.create(-1, "请求异常");
        }
        // 封装数据
        JSONObject tempJson = JSONObject.parseObject(resStr);
        if (tempJson.getIntValue("error_code") != 0) {
            return ResData.create(new JSONObject());
        }
        JSONObject fromInfo = new JSONObject();
        // 健康码
        fromInfo.put("healthCodeDesc", tempJson.getJSONObject("result").getJSONObject("from_info").getString("health_code_desc"));
        // 健康码名称
        fromInfo.put("healthCodeName", tempJson.getJSONObject("result").getJSONObject("from_info").getString("health_code_name"));
        // 健康码图片
        fromInfo.put("healthCodePicture", tempJson.getJSONObject("result").getJSONObject("from_info").getString("health_code_picture").replaceAll("\\\\", ""));
        // 健康码的格式
        fromInfo.put("healthCodeStyle", tempJson.getJSONObject("result").getJSONObject("from_info").getString("health_code_style"));
        // 高风险地区的进入政策
        fromInfo.put("highInDesc", tempJson.getJSONObject("result").getJSONObject("from_info").getString("high_in_desc"));
        // 低风险进入政策
        fromInfo.put("lowInDesc", tempJson.getJSONObject("result").getJSONObject("from_info").getString("low_in_desc"));
        // 出行政策
        fromInfo.put("outDesc", tempJson.getJSONObject("result").getJSONObject("from_info").getString("out_desc"));
        // 风险等级
        fromInfo.put("riskLevel", tempJson.getJSONObject("result").getJSONObject("from_info").getString("risk_level"));
        // 城市名称
        fromInfo.put("cityName", tempJson.getJSONObject("result").getJSONObject("from_info").getString("city_name"));
        JSONObject toInfo = new JSONObject();
        toInfo.put("healthCodeDesc", tempJson.getJSONObject("result").getJSONObject("to_info").getString("health_code_desc"));
        toInfo.put("healthCodeName", tempJson.getJSONObject("result").getJSONObject("to_info").getString("health_code_name"));
        toInfo.put("healthCodePicture", tempJson.getJSONObject("result").getJSONObject("to_info").getString("health_code_picture").replaceAll("\\\\", ""));
        toInfo.put("healthCodeStyle", tempJson.getJSONObject("result").getJSONObject("to_info").getString("health_code_style"));
        toInfo.put("highInDesc", tempJson.getJSONObject("result").getJSONObject("to_info").getString("high_in_desc"));
        toInfo.put("lowInDesc", tempJson.getJSONObject("result").getJSONObject("to_info").getString("low_in_desc"));
        toInfo.put("outDesc", tempJson.getJSONObject("result").getJSONObject("to_info").getString("out_desc"));
        toInfo.put("riskLevel", tempJson.getJSONObject("result").getJSONObject("to_info").getString("risk_level"));
        toInfo.put("cityName", tempJson.getJSONObject("result").getJSONObject("to_info").getString("city_name"));
        JSONObject result = new JSONObject();
        result.put("fromInfo", fromInfo);
        result.put("toInfo", toInfo);
        return ResData.create(result);
    }

    /**
     * 查询全国的风险地区
     * @author PengYiXing
     * @return
     */
    @GetMapping("/riskRegion")
    public ResData<Object> riskRegion() {
        String resStr = null;
        // 请求数据
        try {
            resStr = HttpRequest.doGet(String.format(RISK_REGION_QUERY_URL, KEY));
        } catch (IOException e) {
            e.printStackTrace();
            return ResData.create(-1, "请求异常");
        }
        // 封装结果
        JSONObject tempJson = JSONObject.parseObject(resStr);
        if (tempJson.getIntValue("error_code") != 0) {
            return ResData.create(new JSONObject());
        }
        JSONObject result = tempJson.getJSONObject("result");
        JSONObject res = new JSONObject();
        // 更新时间
        res.put("updatedDate", result.getString("updated_date").split(":")[0]);
        // 高风险地区总数
        res.put("highCount", result.getString("high_count"));
        // 中风险地区总数
        res.put("middleCount", result.getString("middle_count"));
        // 高风险地区详情列表
        res.put("highList", result.get("high_list"));
        // 中风险地区列表
        res.put("middleList", result.get("middle_list"));
        return ResData.create(res);
    }
}
