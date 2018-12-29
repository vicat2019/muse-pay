package com.gen.genservice;

import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class GenerateService {
    // JDBC数据库连接信息
    private static final String JDBC_URL = "jdbc:mysql://192.168.0.90:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "sywg@2018";
    private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    // 项目名称
    private static final String PROJECT_NAME = "sec-kill";//"mybatis-generate";
    // 项目在硬盘上的基础路径=D:\muse-pay
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    // 模板位置
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/mybatis-generate/src/main/resources/mybatis-generator/template";
    // java文件路径
    private static final String JAVA_PATH = "/" + PROJECT_NAME + "/src/main/java";
    // 资源文件路径
    private static final String RESOURCES_PATH = "/" + PROJECT_NAME + "/src/main/resources";
    // 包信息
    private static final String BASE_PACKAGE = "com.gen";
    private static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    private static final String SERVICE_IMPL_PACKAGE = BASE_PACKAGE + ".service.impl";
    private static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";
    private static final String MODEL_PACKAGE = BASE_PACKAGE + ".entity";
    private static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";

    // 生成的Service存放路径
    private static final String PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);
    // 生成的Service实现存放路径
    private static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);
    // 生成的Controller存放路径
    private static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);

    // @author
    private static final String AUTHOR = "Vincent";
    // @date
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());


    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     *
     * @param tableNames 数据表名称...
     */
    public static void genCode(String... tableNames) {
        for (String tableName : tableNames) {
            genCode(tableName, null);
        }
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     *
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
    public static void genCode(String tableName, String modelName) {
        genModelAndMapper(tableName, modelName);
        genService(tableName, modelName);
    }

    /**
     * 生成实体和Mapper
     *
     * @param tableName
     * @param modelName
     */
    public static void genModelAndMapper(String tableName, String modelName) {
        // 配置上下文环境信息
        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        // 配置数据库连接信息
        JDBCConnectionConfiguration jdbcConnectionCfg = new JDBCConnectionConfiguration();
        jdbcConnectionCfg.setConnectionURL(JDBC_URL);
        jdbcConnectionCfg.setUserId(JDBC_USERNAME);
        jdbcConnectionCfg.setPassword(JDBC_PASSWORD);
        jdbcConnectionCfg.setDriverClass(JDBC_DIVER_CLASS_NAME);
        context.setJdbcConnectionConfiguration(jdbcConnectionCfg);

        // 配置插件信息
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        context.addPluginConfiguration(pluginConfiguration);

        // 配置JAVA MODEL生成信息
        JavaModelGeneratorConfiguration javaModelGenCfg = new JavaModelGeneratorConfiguration();
        javaModelGenCfg.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaModelGenCfg.setTargetPackage(MODEL_PACKAGE);
        context.setJavaModelGeneratorConfiguration(javaModelGenCfg);

        // 配置SQL MAPPER生成信息
        SqlMapGeneratorConfiguration sqlMapGenCfg = new SqlMapGeneratorConfiguration();
        sqlMapGenCfg.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGenCfg.setTargetPackage("mapper");
        context.setSqlMapGeneratorConfiguration(sqlMapGenCfg);

        // 配置JAVA客户端生成信息
        JavaClientGeneratorConfiguration javaClientGenCfg = new JavaClientGeneratorConfiguration();
        javaClientGenCfg.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaClientGenCfg.setTargetPackage(MAPPER_PACKAGE);
        javaClientGenCfg.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGenCfg);

        // 设置表格配置信息
        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        tableConfiguration.setDomainObjectName(modelName);
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = false;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成Model和Mapper失败", e);
        }
        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StringUtils.isEmpty(modelName)) {
            modelName = tableNameConvertUpperCamel(tableName);
        }
        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "Mapper.java 生成成功");
        System.out.println(modelName + "Mapper.xml 生成成功");
    }

    /**
     * 生成Service服务类(多个)
     *
     * @param tableNames
     * @param modelNames
     * @throws Exception
     */
    public static void genServiceList(List<String> tableNames, List<String> modelNames) throws Exception {
        if (tableNames == null || tableNames.size() == 0 || modelNames == null || modelNames.size() == 0) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (tableNames.size() != modelNames.size()) {
            throw new IllegalArgumentException("表名列表和对象名列表个数不一致");
        }
        for (int i = 0; i < tableNames.size(); i++) {
            genService(tableNames.get(i), modelNames.get(i));
        }
    }

    /**
     * 生成Service服务类(单个)
     *
     * @param tableName
     * @param modelName
     * @throws Exception
     */
    public static void genService(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            // 驼峰格式的模型名称
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;

            // 设置配置信息
            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", BASE_PACKAGE);

            // 根据模版生成服务接口类文件
            File serviceFile = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!serviceFile.getParentFile().exists()) {
                serviceFile.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data, new FileWriter(serviceFile));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            // 根据模版生成服务实现类文件
            File serviceImplFile = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!serviceImplFile.getParentFile().exists()) {
                serviceImplFile.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data, new FileWriter(serviceImplFile));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成Service失败", e);
        }
    }

    /**
     * 生成控制类Controller(多个)
     *
     * @param tableNames
     * @param modelNames
     * @throws Exception
     */
    public static void genControllerList(List<String> tableNames, List<String> modelNames) throws Exception {
        if (tableNames == null || tableNames.size() == 0 || modelNames == null || modelNames.size() == 0) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (tableNames.size() != modelNames.size()) {
            throw new IllegalArgumentException("表名列表和对象名列表个数不一致");
        }
        for (int i = 0; i < tableNames.size(); i++) {
            genController(tableNames.get(i), modelNames.get(i));
        }
    }

    /**
     * 生成控制类Controller(单个)
     *
     * @param tableName
     * @param modelName
     */
    public static void genController(String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            // 驼峰格式的模型名称
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;

            // 设置生成参数
            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", BASE_PACKAGE);

            // 根据模版生成控制类
            File controllerFile = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!controllerFile.getParentFile().exists()) {
                controllerFile.getParentFile().mkdirs();
            }
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(controllerFile));
            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成Controller失败", e);
        }
    }

    /**
     * 获取配置信息
     * @return
     * @throws IOException
     */
    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        File file = new File(TEMPLATE_FILE_PATH);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        System.out.println(file.getPath());
        System.out.println(file.exists());
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

    }

    private static String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();//兼容使用大写的表名
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }


}
