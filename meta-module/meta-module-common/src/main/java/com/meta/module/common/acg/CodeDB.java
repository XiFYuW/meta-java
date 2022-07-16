package com.meta.module.common.acg;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

public class CodeDB {
    private final static String db_url = "jdbc:mysql://124.222.138.201:9898/dev?useUnicode=true&serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf8";
    private final static String db_user = "test";
    private final static String db_pass = "qdZMrRPnM9bV4T2YdR";
    public static DataSourceConfig getDataSourceConfig(){
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(db_url);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(db_user);
        dsc.setPassword(db_pass);
        return dsc;
    }

    public static GlobalConfig getGlobalConfig(String outputDir, String author){
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
        return gc;
    }

    public static PackageConfig getPackageConfig(String parentPackage){
        return new PackageConfig().setParent(parentPackage);
    }
}
