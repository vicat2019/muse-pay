package com.nucc.util;

import com.nucc.entity.weixin.responseentity.WXMerchantResponse;
import com.nucc.util.weixinsdk.WXPayUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: xml工具类
 * @author: Vincent
 * @create: 2018-11-29 14:07
 **/
public class XMLUtils {
    private static Logger log = LoggerFactory.getLogger("XMLUtils");

    /**
     * 解析XML生成对象
     *
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T xmlToObject(String xml, Class<T> clazz) throws Exception {
        // 检查参数
        if (StringUtils.isEmpty(xml)) {
            throw new Exception("XML内容字符串为空");
        }

        // 生成类实例
        T instance = clazz.newInstance();

        try {
            // 读取XML文档
            Document document = DocumentHelper.parseText(xml);
            Element element = document.getRootElement();
            List<Element> children = element.elements();

            Map<String, String> valueMap = new HashMap<>();
            if (children != null && children.size() > 0) {
                children.forEach(item -> {
                    String propertyName = item.getName();
                    String propertyValue = item.getStringValue().trim();
                    valueMap.put(propertyName, propertyValue);
                });
            }

            // 设置对象属性值
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (valueMap.keySet().contains(field.getName())) {
                    field.setAccessible(true);

                    Object value = valueMap.get(field.getName());
                    // 类型
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        value = Integer.parseInt(valueMap.get(field.getName()));
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        value = Double.parseDouble(valueMap.get(field.getName()));
                    } else if (field.getType() == float.class || field.getType() == Float.class) {
                        value = Float.parseFloat(valueMap.get(field.getName()));
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        value = Boolean.valueOf(valueMap.get(field.getName()));
                    }
                    field.set(instance, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析XML生成类实例，异常=" + e.getMessage());
        }

        log.info("生成的类信息=" + instance);
        return instance;
    }

    /**
     * 对象转换成XML
     *
     * @param obj
     * @return
     */
    public static String objToXml(Object obj) throws Exception {
        Map<String, String> xmlMap = new HashMap<>();

        // 为空，返回空
        if (obj == null) {
            log.error("将对象转换成XML，对象为空");
            return "";
        }

        Class clazz = obj.getClass();
        // 获取当前类的有GET方法的属性
        getClassProperties(clazz, obj, xmlMap);

        // 获取父类属性值
        getSuperClassProperties(clazz, obj, xmlMap);

        // 打印结果
        StringBuilder sb = new StringBuilder();
        xmlMap.keySet().forEach(key -> {
            sb.append(key);
            sb.append("=");
            sb.append(xmlMap.get(key));
            sb.append(", ");
        });
        log.info("返回内容{" + (sb.length() >= 2 ? sb.substring(0, sb.length() - 2) : sb) + "}");

        // 调用微信SDK根据MAP生成XML
        return WXPayUtil.mapToXml(xmlMap);
    }

    /**
     * 获取父类的属性值
     *
     * @param clazz
     * @param instance
     * @param valueMap
     * @throws Exception
     */
    private static void getSuperClassProperties(Class clazz, Object instance, Map<String, String> valueMap) throws Exception {
        // 获取父类
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            getClassProperties(superClass, instance, valueMap);
        }
    }

    /**
     * 获取类对象的属性值
     *
     * @param clazz
     * @param instance
     * @param valueMap
     * @throws Exception
     */
    private static void getClassProperties(Class clazz, Object instance, Map<String, String> valueMap) throws Exception {
        // 获取父类GET方法
        List<String> getMethodList = getGetMethodNameList(clazz);
        // 获取属性值
        getTargetValue(clazz, instance, valueMap, getMethodList);
    }

    /**
     * 获取对象的属性值
     *
     * @param instance
     * @param targetMap
     * @param clazz
     * @param getMethodList
     * @throws IllegalAccessException
     */
    private static void getTargetValue(Class clazz, Object instance, Map<String, String> targetMap, List<String> getMethodList)
            throws IllegalAccessException {
        Field[] superClassFields = clazz.getDeclaredFields();
        for (Field field : superClassFields) {
            // 不存在GET方法
            if (!getMethodList.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(instance);
            if (value != null) {
                targetMap.put(field.getName(), value.toString());
            }
        }
    }

    /**
     * 获取当前层级类的GET方法名称
     *
     * @param clazz 父类Class
     * @return
     */
    private static List<String> getGetMethodNameList(Class clazz) {
        List<String> getMethodList = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                // 去掉get, 第一个字母变消息
                String temp = method.getName().substring(3, method.getName().length());
                String property = temp.substring(0, 1).toLowerCase() + temp.substring(1);
                getMethodList.add(property);
            }
        }
        return getMethodList;
    }


    public static void main(String[] args) {
        String content = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n" +
                "<xml>\n" +
                "    <appid>wxd930ea5d5a258f4f</appid>\n" +
                "    <mch_id>10000100</mch_id>\n" +
                "    <device_info>1000</device_info>\n" +
                "    <body>test</body>\n" +
                "    <nonce_str>ibuaiVcKdpRxkhJA</nonce_str>\n" +
                "    <sign>9A0A8659F005D6984697E2CA0A9CF3B7</sign>\n" +
                "</xml>";

        try {
/*            WXMerchantInfoVO test = xmlToObject(content, WXMerchantInfoVO.class);

            System.out.println(objToXml(test));*/


            WXMerchantResponse response = WXMerchantResponse.getSuccessResult();
            System.out.println(objToXml(response));

            System.out.println(response.getClass().getSuperclass());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class TestInfo {
    private int age;

    private Integer amount;

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

