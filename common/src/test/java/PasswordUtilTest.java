import com.alibaba.fastjson.JSONObject;
import com.easonhotel.common.Demo;
import com.easonhotel.common.utils.*;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
public class PasswordUtilTest {
    /**
     * 查询key 用于通过验证
     */
    private String KEY = "5758542001e82e57ea719945c2ae018a";

    /**
     * 查询那些城市被支持的接口地址
     */
    private String CITY_LIST_QUERY_URL = "http://apis.juhe.cn/springTravel/citys";

    /**
     * 查询防疫政策接口地址
     */
    private String EPIDEMIC_POLICY_QUERY_URL = "http://apis.juhe.cn/springTravel/query";

    /**
     * 查询风险地区接口地址
     */
    private String RISK_REGION_QUERY_URL = "http://apis.juhe.cn/springTravel/risk";

    private String[] hotelNameArray = { "缘梦缘酒店", "康辉酒店", "缘森主题酒店", "妙亨酒店", "远香湖酒店", "茂风酒店", "威盛酒店",
            "鼎然酒店", "园馨酒店", "悦居酒店", "悦己时尚酒店", "悦洱酒店", "月城酒店", "凯麟酒店", "祥亮酒店", "米可酒店", "梦雨露酒店", "琛聪酒店",
            "亦锋酒店", "儒衡酒店"};

    @Test
    public void test() throws IOException {
        System.out.println(DateUtils.parseDate(1649509292629l, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtils.parseDate(1649441690180l, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtils.parseDate(1649508989841l, "yyyy-MM-dd HH:mm:ss"));
    }
}
