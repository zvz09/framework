package com.zvz.smart.framework.helper;

import com.zvz.smart.framework.annotation.Inject;
import com.zvz.smart.framework.util.ArrayUtil;
import com.zvz.smart.framework.util.CollectionUtil;
import com.zvz.smart.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 *
 * @author huangyong
 * @since 1.0.0
 */
public final class IocHelper {

    /**
     * 注意：所有Bean实例都是单例的
     */

    static {
        //获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            //遍历BeanMap
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                //从BeanMap中获取Bean类与实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取Bean勒种定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    //遍历
                    for (Field beanField : beanFields) {
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
