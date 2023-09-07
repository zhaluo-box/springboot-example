package com.example.test.reflect;

import com.example.boot.base.common.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created  on 2023/3/22 09:9:35
 *
 * @author zl
 */
public class ParamTest {

    @Test
    @DisplayName("演示json对象多次转换")
    public void testDes() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        var approveClass = Approve.class;

        var methods = approveClass.getMethods();
        var approveMethod = methods[0];
        var parameterTypes = approveMethod.getGenericParameterTypes();

        for (Type paramType : parameterTypes) {
            System.out.println("paramType = " + paramType);
            System.out.println("paramType.getTypeName() = " + paramType.getTypeName());
            //ParameterizedType:参数化类型，判断是否是参数化类型。
            if (paramType instanceof ParameterizedType) {
                //获得源码中的真正的参数类型
                Type[] genericType = ((ParameterizedType) paramType).getActualTypeArguments();
                for (Type gt : genericType) {
                    System.out.println("泛型类型：" + gt);
                }
            }
        }

        var studentApproveVO = new ApproveVO<Student>().setName("zhang san").setData(new Student("666"));

        var json = JsonUtil.toJSON(studentApproveVO);

        Object o = JsonUtil.toObject(json, parameterTypes[0]);
        System.out.println(o);

        var approve = new Approve();

        approveMethod.invoke(approve, o);

    }

    @Test
    @DisplayName("spring 自带 反射工具类测试")
    public void testReflectUtilMethod() {

    }

    @Data
    @ToString
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApproveVO<T> {

        private String name;

        private T data;

    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {

        private String name;

    }

    public static class Approve {

        public void approveMethod(ApproveVO<Student> data) {

            System.out.println(data.getData().getName());
        }
    }
}
