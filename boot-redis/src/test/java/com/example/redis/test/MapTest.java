package com.example.redis.test;

import com.example.redis.entity.Consumer;
import com.example.redis.entity.Product;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MapTest {

    public static void main(String[] args) {
        Product product = new Product();
        product.setAddress("北京");
        product.setCreatetime(new Date());
        product.setDays(365 * 50);
        product.setName("原子弹");

        Consumer consumer = new Consumer("菜菜", 22, "上海");
        Consumer consumer1 = new Consumer("肉肉", 66, "上海");

        List<Consumer> consumerList = new ArrayList<>();
        consumerList.add(consumer);
        consumerList.add(consumer1);
        product.setConsumerList(consumerList);

        Map<String, String> map = objectToMap(product);
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + "---------->>>" + entry.getValue());
        }
        //System.out.println(map);

        /*Consumer consumer = new Consumer("菜菜",22,"上海");
        Consumer consumer1 = new Consumer("肉肉",66,"上海");
        List<Consumer> consumerList = new ArrayList<>();
        consumerList.add(consumer);
        consumerList.add(consumer1);
        Map map = new HashMap();
        map.put("address","西伯利亚");
        map.put("consumerList",consumerList);
        //数字必须是数字,不可以是字符串形式的数字 Map<String,Object>
        map.put("days",365);
        Product product1 = (Product) mapToObject(map,Product.class);
        System.out.println(product1);*/
    }

    /**
     * map集合转换为实体类
     *
     * @param map
     * @param
     * @return
     */
    public static Object mapToObject(Map map, Class type) {

        BeanInfo beanInfo = null;
        Object obj = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);
            obj = type.newInstance();

            // 给对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();

                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 实体类转换为map集合
     */
    public static Map objectToMap(Object object) {

        Class clazz = object.getClass();
        Map map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(object, new Object[0]);
                    if (result != null) {
                        map.put(propertyName, result);
                    } else {
                        map.put(propertyName, "");
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return map;
    }
}
