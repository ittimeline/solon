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
package org.noear.solon.annotation;

import org.noear.solon.core.aspect.Interceptor;

import java.lang.annotation.*;

/**
 * 环绕拦截处理（针对 Controller、Service、Dao 等所有基于 MethodWrap 运行的目标，附加拦截器）
 *
 * <pre>{@code
 * @Controller
 * public class DemoController{
 *     @Around(TranInterceptor.class)  //@Tran 注解即通过 @Around 实现
 *     @Mapping("/demo/*")
 *     public String hello(){
 *         return "heollo world;";
 *     }
 * }
 *
 * //
 * // 注解传导示例：（用于简化使用）
 * //
 * @Around(value = TranInterceptor.class, index = 7)
 * @Target({ElementType.METHOD})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface Tran {
 *     ....
 * }
 *
 * @Around(value = CacheInterceptor.class, index = 8)
 * @Target({ElementType.METHOD})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface Cache {
 *     ...
 * }
 * }</pre>
 *
 * @author noear
 * @since 1.0
 * */
@Inherited //要可继承
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Around { //intercept
    /**
     * 调用处理程序
     * */
    Class<? extends Interceptor> value();
    /**
     * 调用顺位
     * */
    int index() default 0;
}
