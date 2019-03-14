package com.gen;

import com.gen.genservice.GenerateService;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Vic
 * @Date ${date} ${time}
 */
public class GeneratorMain {
    private static Logger genLog = LoggerFactory.getLogger("GeneratorMain");

    public static void main(String[] args) {
        try {
            /*generate("sk_product_info", "ProductInfo");
            generate("sk_user_info", "UserInfo");
            generate("sk_success_info", "SuccessInfo");*/

            generate("rs_channel_info", "ChannelInfo");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成代码(单个)
     *
     * @param tableName
     * @param modelName
     * @throws Exception
     */
    public static void generate(String tableName, String modelName) throws Exception {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(modelName)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        List<String> tableNameList = new ArrayList<>();
        tableNameList.add(tableName);
        List<String> modelNameList = new ArrayList<>();
        modelNameList.add(modelName);

        genLog.info("------------------------------开始生成------------------------------");
        // 生成MODEL, MAPPER
        generateMyBatis(tableNameList, modelNameList);

        // 生成Service
        GenerateService.genService(tableName, modelName);

        // 生成Controller
        // GenerateService.genController(tableName, modelName);
        genLog.info("------------------------------生成结束------------------------------");
    }

    /**
     * 生成MODEL,MAPPER
     *
     * @param tableNames
     * @param modelNames
     * @throws Exception
     */
    private static void generateMyBatis(List<String> tableNames, List<String> modelNames) throws Exception {
        if (tableNames == null || tableNames.size() == 0 || modelNames == null || modelNames.size() == 0) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (tableNames.size() != modelNames.size()) {
            throw new IllegalArgumentException("表名列表和对象名列表个数不一致");
        }

        genLog.info("--------------------生成mybatis代码开始--------------------");

        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File(GeneratorMain.class.getResource("/mybatis-generator/generatorConfig.xml").getFile());

        ConfigurationParser configParser = new ConfigurationParser(warnings);
        Configuration config = configParser.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);

        Context context = config.getContext("DB2Tables");
        List<TableConfiguration> tableList = context.getTableConfigurations();
        tableList.clear();

        // 循环生成
        for (int i = 0; i < tableNames.size(); i++) {
            TableConfiguration tableConfig = new TableConfiguration(context);
            tableConfig.setTableName(tableNames.get(i));
            tableConfig.setDomainObjectName(modelNames.get(i));
            tableConfig.setSelectByExampleQueryId("false");
            tableConfig.setSelectByExampleStatementEnabled(false);
            tableConfig.setDeleteByExampleStatementEnabled(false);
            tableConfig.setUpdateByExampleStatementEnabled(false);
            context.addTableConfiguration(tableConfig);
        }
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        genLog.info("--------------------生成mybatis代码结束--------------------");
    }

}
