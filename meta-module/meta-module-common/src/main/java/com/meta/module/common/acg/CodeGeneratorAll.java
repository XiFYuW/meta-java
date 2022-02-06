package com.meta.module.common.acg;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author https://github.com/XiFYuW
 * @since  2020/08/28 9:39
 */
public class CodeGeneratorAll {

    public static void main(String[] args) {
        String outputDir = "/meta-module/meta-module-common/src/main/java";
        String author = "admin";
        String parentPackage = "com.meta.module.common";
        String dtoPackage = "com.meta.module.common.dto.fcOrderUSes";
        String structPackage = "com.meta.module.common.struct";
        String mapperDir = "/meta-module/meta-module-common/src/main/resources/mappers/mq/";
        String[] include = new String[]{"acn_basic"};
        genCode(outputDir, author, parentPackage, mapperDir, dtoPackage, structPackage, include);
    }

    private static void genCode(String outputDir,
                                String author,
                                String parentPackage,
                                String mapperDir,
                                String dtoPackage,
                                String structPackage,
                                String[] include){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + outputDir);
        gc.setAuthor(author);
        gc.setOpen(false);
        // 是否覆盖已有文件
        gc.setFileOverride(true);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3308/acn?useUnicode=true&serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackage);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("dtoPackage", dtoPackage);
                map.put("structPackage", structPackage);
                map.put("listDTO", "ListDTO");
                map.put("addDTO", "AddDTO");
                map.put("updateDTO", "UpdateDTO");
                map.put("delDTO", "DelDTO");
                this.setMap(map);
            }
        };

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        /*Struct*/
        String templatePathStruct = "/templates/all/struct-all.java.vm";
        focList.add(new FileOutConfig(templatePathStruct) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath
                        + outputDir
                        + "\\"
                        + structPackage.replaceAll("\\.", "/")
                        + "\\"
                        + tableInfo.getEntityName()
                        + "Struct" + StringPool.DOT_JAVA;
            }
        });

        /*delDto*/
        String templatePathDelDTO = "/templates/all/dto-all-del.java.vm";
        focList.add(new FileOutConfig(templatePathDelDTO) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath
                        + outputDir
                        + "\\"
                        + dtoPackage.replaceAll("\\.", "/")
                        + "\\"
                        + tableInfo.getEntityName()
                        + "DelDTO" + StringPool.DOT_JAVA;
            }
        });

        /*updateDto*/
        String templatePathUpdateDTO = "/templates/all/dto-all-update.java.vm";
        focList.add(new FileOutConfig(templatePathUpdateDTO) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath
                        + outputDir
                        + "\\"
                        + dtoPackage.replaceAll("\\.", "/")
                        + "\\"
                        + tableInfo.getEntityName()
                        + "UpdateDTO" + StringPool.DOT_JAVA;
            }
        });

        /*addDto*/
        String templatePathAddDTO = "/templates/all/dto-all-add.java.vm";
        focList.add(new FileOutConfig(templatePathAddDTO) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath
                        + outputDir
                        + "\\"
                        + dtoPackage.replaceAll("\\.", "/")
                        + "\\"
                        + tableInfo.getEntityName()
                        + "AddDTO" + StringPool.DOT_JAVA;
            }
        });

        /*listDto*/
        String templatePathListDTO = "/templates/all/dto-all-list.java.vm";
        focList.add(new FileOutConfig(templatePathListDTO) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath
                        + outputDir
                        + "\\"
                        + dtoPackage.replaceAll("\\.", "/")
                        + "\\"
                        + tableInfo.getEntityName()
                        + "ListDTO" + StringPool.DOT_JAVA;
            }
        });

        // Mapper.xml
        String templatePath = "/templates/mapper.xml.vm";
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + mapperDir + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new file(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setController("templates/all/controller-all.java");
        templateConfig.setService("templates/all/service-all.java");
        templateConfig.setServiceImpl("templates/all/serviceImpl-all.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        strategy.setInclude(include);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
