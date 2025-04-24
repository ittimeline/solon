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
package org.noear.solon.core.util;

import org.noear.solon.Utils;
import org.noear.solon.lang.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * 断言类
 *
 * @author noear
 * @since 3.1
 */
public final class Assert {
    /**
     * 不能为空集合
     */
    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (Utils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 不能为空集合
     */
    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        if (Utils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 不能为空字符串
     */
    public static void notEmpty(@Nullable String text, String message) {
        if (Utils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 不能为空白字符串
     */
    public static void notBlank(@Nullable String text, String message) {
        if (Utils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 不能为 null
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}