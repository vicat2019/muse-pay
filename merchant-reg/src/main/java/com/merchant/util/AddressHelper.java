package com.merchant.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.merchant.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @program: merchant-register
 * @description: 随机生成地址
 * @author: Vincent
 * @create: 2019-02-15 11:08
 **/
@Component("addressHelper")
public class AddressHelper {

    @Autowired
    private HttpUtils httpUtils;

    // 四川省随机地址
    private static final String PROVINCE_SICHUAN = "四川省";
    private static final String CITY_SICHUAN = "成都市,自贡市,攀枝花市,泸州市,德阳市,绵阳市,广元市,遂宁市,内江市,乐山市,南充市,眉山市,宜宾市,广安市,达州市,雅安市,巴中市,资阳市,阿坝藏族羌族自治州,甘孜藏族自治州,凉山彝族自治州";
    private static final String AREA_SICHUAN = "锦江区,青羊区,金牛区,武侯区,成华区,龙泉驿区,青白江区,新都区,温江区,金堂县,双流县,郫县,大邑县,蒲江县,新津县,都江堰市,彭州市,邛崃市,崇州市";
    private static final String STREET_SICHUAN = "莲花社区北一巷10号,大业路8号,滨江中路13号,三圣街33号407室,三元巷51号,光华街1栋,牛沙路15号附1号,东顺城中街邮电五所,昭忠祠街8号侧,桂五桥西街52号,海椒市街60号,东光东怡街79号,东光街5号院内,水碾河南一街15栋院内,川师北大门,均隆街,秀水园,成龙路皇经社区内,洗面桥横街社区居委会建国巷1号,洗面桥东一街3号,洗面桥街7号,武侯祠横街12号2楼,武侯祠大街63号,一环路西一段11号,国学巷34号,小天东街4号附1号,南虹村,电信南街12号,临江路8号1单元2楼,一环路南二段7号附4号,十一街9号,致民路21号,十七街9号,郭家桥南街5号,郭家桥北街10号,玉林横街,玉林一巷茶园,玉林下横巷8号,玉洁巷16号,玉林五巷3号,人民南路4段9号,磨子巷4号,棕南西街1号,科院街4号附2号,南站派出所旁,长寿路53号,科华中路5号7楼,人南4段38号长寿桥干休所旁,棕树2组大院,高攀路,双楠路239号,双楠路61号,双楠街259号,少陵横街68号大院,大石南路36号,百吉街23号,董家湾北街2号-801,双丰西路17幢15号,红牌楼北街社区居委会红牌楼北街15号,簇桥乡福锦路239号乡政府内,机投正街,金花镇金花桥街28号镇政府内,晋阳路422号物业公司4楼,花径路138号,新鸿路77号,跳蹬河北路2号,猛追湾10号,二仙桥路1号,双建路70号,府青路三段府青巷4号,双桥路南2街90号,长天路28号,建设南街9号,建设路北三街58号,龙潭寺中街29号,蜀都大道五桂桥,云龙路115号,桂溪街道和平社区,肖家河东1巷花园,肖家河4巷6号,兴蓉东1号,二环路南四段16号,元通一巷,蓓蕾街中巷5号,芳草街24号长信公寓附24号,新能巷6号,荆竹北街27号,紫竹北路58号,新光路60号,石羊街道新街社区,百花潭路8号,横小南街2号,东门街69号,清江西路1号,石人西路36号,西御河沿街12号,双新科创园光明苑内,王家塘巷1号,白云寺街65号,忠烈祠西街53号,苏坡中路8号,文家场正街224号,光荣路19号,青西路98号,西体路5号,泰宏路9号,肖二巷2号,商贸大道一段7号,星辰路一街8号,银河北街3号交大吾家八期五楼,茶店子安蓉路8号,黄忠街8号,蜀西路26号,政通路（社区服务中心）,天回镇明月路,土桥金周路999号";

    // 省市区列表
    private static List<String> cityList;
    private static List<String> areaList;
    private static List<String> streetList;

    static {
        cityList = Lists.newArrayList(Splitter.on(",").split(CITY_SICHUAN));
        areaList = Lists.newArrayList(Splitter.on(",").split(AREA_SICHUAN));
        streetList = Lists.newArrayList(Splitter.on(",").split(STREET_SICHUAN));
    }

    // 缓存查询返回的省份下的城市信息
    private static Map<String, Map<String, String>> cacheCityMap = Maps.newHashMap();

    // 缓存查询返回的城市下的区县信息
    private static Map<String, Map<String, String>> cacheAreaMap = Maps.newHashMap();

    /**
     * 生成省市区街道
     *
     * @return String[]
     */
    public static String[] genAddressForSiChuan() {
        String[] address = new String[4];

        address[0] = PROVINCE_SICHUAN;

        // 城市
        String cityName = "成都市";
        address[1] = cityName;

        // 区县
        Random random2 = new Random();
        String areaName = areaList.get(random2.nextInt(areaList.size()));
        address[2] = areaName;

        // 街道
        Random random3 = new Random();
        String streetName = streetList.get(random3.nextInt(streetList.size()));

        // 号、栋、楼、室
        Random streetRandom = new Random();
        streetName = streetName.replaceAll("\\d+号", streetRandom.nextInt(100) + "号");
        streetName = streetName.replaceAll("\\d+栋", streetRandom.nextInt(10) + "号");
        streetName = streetName.replaceAll("\\d+楼", streetRandom.nextInt(10) + "楼");
        streetName = streetName.replaceAll("\\d+室", streetRandom.nextInt(10) + "0" + streetRandom.nextInt(10) + "室");
        address[3] = streetName;

        return address;
    }

    /**
     * 获取城市编号
     *
     * @param cityName 城市名称
     * @return String
     */
    public String getCityCodeForSiChuan(String cityName) {
        ResultData resultData = ResultData.getSuccessResult();
        resultData.setData("{\"code\":1,\"msg\":\"获取城市列表成功\",\"data\":[{\"value\":\"510100\",\"text\":\"成都市\"},{\"value\":\"510300\",\"text\":\"自贡市\"},{\"value\":\"510400\",\"text\":\"攀枝花市\"},{\"value\":\"510500\",\"text\":\"泸州市\"},{\"value\":\"510600\",\"text\":\"德阳市\"},{\"value\":\"510700\",\"text\":\"绵阳市\"},{\"value\":\"510800\",\"text\":\"广元市\"},{\"value\":\"510900\",\"text\":\"遂宁市\"},{\"value\":\"511000\",\"text\":\"内江市\"},{\"value\":\"511100\",\"text\":\"乐山市\"},{\"value\":\"511300\",\"text\":\"南充市\"},{\"value\":\"511400\",\"text\":\"眉山市\"},{\"value\":\"511500\",\"text\":\"宜宾市\"},{\"value\":\"511600\",\"text\":\"广安市\"},{\"value\":\"511700\",\"text\":\"达州市\"},{\"value\":\"511800\",\"text\":\"雅安市\"},{\"value\":\"511900\",\"text\":\"巴中市\"},{\"value\":\"512000\",\"text\":\"资阳市\"},{\"value\":\"513200\",\"text\":\"阿坝藏族羌族自治州\"},{\"value\":\"513300\",\"text\":\"甘孜藏族自治州\"},{\"value\":\"513400\",\"text\":\"凉山彝族自治州\"}]}");

        Map<String, String> cityMap = CommonUtils.parseBinNongResult(resultData);
        return cityMap.get(cityName);
    }

    /**
     * 获取区域编号
     *
     * @param areaName
     * @return Map
     */
    public String getAreaCodeForSiChuan(String areaName) {
        ResultData resultData = ResultData.getSuccessResult();
        resultData.setData("{\"code\":1,\"msg\":\"获取区县列表成功\",\"data\":[{\"value\":\"510101\",\"text\":\"市辖区\"},{\"value\":\"510104\",\"text\":\"锦江区\"},{\"value\":\"510105\",\"text\":\"青羊区\"},{\"value\":\"510106\",\"text\":\"金牛区\"},{\"value\":\"510107\",\"text\":\"武侯区\"},{\"value\":\"510108\",\"text\":\"成华区\"},{\"value\":\"510112\",\"text\":\"龙泉驿区\"},{\"value\":\"510113\",\"text\":\"青白江区\"},{\"value\":\"510114\",\"text\":\"新都区\"},{\"value\":\"510115\",\"text\":\"温江区\"},{\"value\":\"510121\",\"text\":\"金堂县\"},{\"value\":\"510122\",\"text\":\"双流县\"},{\"value\":\"510124\",\"text\":\"郫县\"},{\"value\":\"510129\",\"text\":\"大邑县\"},{\"value\":\"510131\",\"text\":\"蒲江县\"},{\"value\":\"510132\",\"text\":\"新津县\"},{\"value\":\"510181\",\"text\":\"都江堰市\"},{\"value\":\"510182\",\"text\":\"彭州市\"},{\"value\":\"510183\",\"text\":\"邛崃市\"},{\"value\":\"510184\",\"text\":\"崇州市\"}]}");

        Map<String, String> areaMap = CommonUtils.parseBinNongResult(resultData);
        return areaMap.get(areaName);
    }

    /**
     * 获取所有省份信息
     *
     * @return Map
     */
    public Map<String, String> getAllProvince() {
        ResultData resultData = ResultData.getSuccessResult();
        resultData.setData("{\"code\":1,\"msg\":\"获取省份列表成功\",\"data\":[{\"value\":\"110000\",\"text\":\"北京市\"},{\"value\":\"120000\",\"text\":\"天津市\"},{\"value\":\"130000\",\"text\":\"河北省\"},{\"value\":\"140000\",\"text\":\"山西省\"},{\"value\":\"150000\",\"text\":\"内蒙古自治区\"},{\"value\":\"210000\",\"text\":\"辽宁省\"},{\"value\":\"220000\",\"text\":\"吉林省\"},{\"value\":\"230000\",\"text\":\"黑龙江省\"},{\"value\":\"310000\",\"text\":\"上海市\"},{\"value\":\"320000\",\"text\":\"江苏省\"},{\"value\":\"330000\",\"text\":\"浙江省\"},{\"value\":\"340000\",\"text\":\"安徽省\"},{\"value\":\"350000\",\"text\":\"福建省\"},{\"value\":\"360000\",\"text\":\"江西省\"},{\"value\":\"370000\",\"text\":\"山东省\"},{\"value\":\"410000\",\"text\":\"河南省\"},{\"value\":\"420000\",\"text\":\"湖北省\"},{\"value\":\"430000\",\"text\":\"湖南省\"},{\"value\":\"440000\",\"text\":\"广东省\"},{\"value\":\"450000\",\"text\":\"广西壮族自治区\"},{\"value\":\"460000\",\"text\":\"海南省\"},{\"value\":\"500000\",\"text\":\"重庆市\"},{\"value\":\"510000\",\"text\":\"四川省\"},{\"value\":\"520000\",\"text\":\"贵州省\"},{\"value\":\"530000\",\"text\":\"云南省\"},{\"value\":\"540000\",\"text\":\"西藏自治区\"},{\"value\":\"620000\",\"text\":\"甘肃省\"},{\"value\":\"630000\",\"text\":\"青海省\"},{\"value\":\"640000\",\"text\":\"宁夏回族自治区\"},{\"value\":\"650000\",\"text\":\"新疆维吾尔自治区\"},{\"value\":\"660000\",\"text\":\"新疆生产建设兵团\"},{\"value\":\"710000\",\"text\":\"台湾省\"},{\"value\":\"810000\",\"text\":\"香港特别行政区\"},{\"value\":\"820000\",\"text\":\"澳门特别行政区\"},{\"value\":\"610000\",\"text\":\"陕西省\"}]}");
        return CommonUtils.parseBinNongResult(resultData);
    }

    /**
     * 获取省份编号
     *
     * @param provinceName 省份名称
     * @return
     */
    public String getProvinceCode(String provinceName) {
        if (StringUtils.isEmpty(provinceName)) {
            return "";
        }

        Map<String, String> provinceMap = getAllProvince();
        return provinceMap.get(provinceName);
    }

    /**
     * 获取城市编码
     *
     * @param provinceCode 省份编码
     * @param cityName     城市名称
     * @return String
     */
    public String getCityCode(String provinceCode, String cityName) {
        Map<String, String> cityMap = getCityByProvince(provinceCode);
        return cityMap.get(cityName);
    }

    /**
     * 获取城市
     *
     * @param provinceCode 省份编码
     * @return Map
     */
    private Map<String, String> getCityByProvince(String provinceCode) {
        if (cacheCityMap.get(provinceCode) != null) {
            return cacheCityMap.get(provinceCode);
        }

        String url = "http://www.ruishengglass.cn/api-v1-zone/getCity?parent=" + provinceCode;
        ResultData resultData = httpUtils.get(url, Maps.newHashMap());
        Map<String, String> cityMap = CommonUtils.parseBinNongResult(resultData);
        if (cityMap != null) {
            cacheCityMap.put(provinceCode, cityMap);
        }

        return cityMap;
    }

    /**
     * 获取区域编码
     *
     * @param cityCode 城市编码
     * @param areaName 区县名称
     * @return String
     */
    public String getAreaCode(String cityCode, String areaName) {
        Map<String, String> areaMap = getAreaByCity(cityCode);
        return areaMap.get(areaName);
    }

    /**
     * 获取区域
     *
     * @param cityCode
     * @return Map
     */
    private Map<String, String> getAreaByCity(String cityCode) {
        if (cacheAreaMap.get(cityCode) != null) {
            return cacheAreaMap.get(cityCode);
        }

        String url = "http://www.ruishengglass.cn/api-v1-zone/getArea?parent=" + cityCode;
        ResultData resultData = httpUtils.get(url, Maps.newHashMap());
        Map<String, String> areaMap = CommonUtils.parseBinNongResult(resultData);
        if (areaMap != null) {
            cacheAreaMap.put(cityCode, areaMap);
        }

        return areaMap;
    }


}
