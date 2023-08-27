package com.org.stock.config;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.sql.SqlTemplate;
import org.nutz.dao.util.Daos;
import org.nutz.integration.spring.SpringDaoRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Set;

@Slf4j
@Configuration
public class DbConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public NutDao getNutDao(){
        //开启驼峰下划线转换
        Daos.FORCE_HUMP_COLUMN_NAME = true;

        NutDao nutDao = new NutDao(dataSource);
        nutDao.setRunner(new SpringDaoRunner());

        Set<Class<?>> classes = ClassUtil.scanPackage("");
        if (classes != null && !classes.isEmpty()){
            for(Class<?> aClass : classes){
                Table isTable = aClass.getAnnotation(Table.class);
                if(isTable != null){
                    nutDao.create(aClass, false);
                    Daos.migration(nutDao, aClass, true, false, true);
                }
            }
        }

        return nutDao;
    }

    @Bean
    public SqlTemplate getSqlTemplate(NutDao nutDao){
        return new SqlTemplate(nutDao);
    }
}
