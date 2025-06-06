/*
 * Copyright 2017-2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.nami.common;

import org.noear.solon.core.util.ClassUtil;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 函数代理工具（用于默认函数的处理跨版本）
 *
 * @author noear
 * @since 1.10
 */
public class MethodHandlerUtils {
    /**
     * java16+ 支持调用default method的方法
     */
    private static Method invokeDefaultMethod = null;

    static {
        //
        //JDK16+ 新增InvocationHandler.invokeDefault()
        //
        if (JavaUtils.JAVA_MAJOR_VERSION >= 16) {
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8253870
            Method[] ms = InvocationHandler.class.getMethods();

            for (Method call : ms) {
                if ("invokeDefault".equals(call.getName())) {
                    invokeDefaultMethod = call;
                    break;
                }
            }
            if (invokeDefaultMethod == null) {
                //不可能发生
                throw new UnsupportedOperationException("The current java " + JavaUtils.JAVA_MAJOR_VERSION + " is not found: invokeDefault");
            }
        }
    }

    /**
     * 在代理模式下调用接口的默认的函数
     */
    public static Object invokeDefault(Object proxy, Method method, Object[] args) throws Throwable {
        // https://dzone.com/articles/correct-reflective-access-to-interface-default-methods
        // https://gist.github.com/lukaseder/f47f5a0d156bf7b80b67da9d14422d4a
        if (JavaUtils.JAVA_MAJOR_VERSION <= 15) {
            final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class);

            ClassUtil.accessibleAsTrue(constructor);

            final Class<?> clazz = method.getDeclaringClass();
            return constructor.newInstance(clazz)
                    .in(clazz)
                    .unreflectSpecial(method, clazz)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } else {
            Method invoke = invokeDefaultMethod;
            return invoke.invoke(null, proxy, method, args);
        }
    }

    /**
     * 在代理模式下调用 Object 的默认的函数
     */
    public static Object invokeObject(Class<?> interfaceClz, Object proxy, Method method, Object[] args) {
        String name = method.getName();

        switch (name) {
            case "toString":
                return interfaceClz.getName() + ".$Proxy";
            case "hashCode":
                return System.identityHashCode(proxy);
            case "equals":
                return proxy == args[0];
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + interfaceClz.getName() + "::" + method.getName());
        }
    }
}
