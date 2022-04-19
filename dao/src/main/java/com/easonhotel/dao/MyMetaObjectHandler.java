package com.easonhotel.dao;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("deleted", false, metaObject);
        this.setFieldValByName("createdAt", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("updatedAt", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("sortedNum", 1, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedAt", System.currentTimeMillis(), metaObject);
    }
}
