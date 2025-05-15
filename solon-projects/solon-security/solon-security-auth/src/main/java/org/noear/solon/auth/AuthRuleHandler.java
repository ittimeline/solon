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
package org.noear.solon.auth;

import org.noear.solon.Utils;
import org.noear.solon.auth.annotation.AuthIgnore;
import org.noear.solon.core.handle.Action;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证规则'处理器'
 *
 * @author noear
 * @since 1.4
 * @since 3.0
 * @since 3.1
 */
public class AuthRuleHandler implements Handler {
    private String pathPrefix;

    /**
     * 设置规则路径前缀（用于支持 AuthAdapterSupplier 的前缀特性）
     */
    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    private List<AuthRule> rules = new ArrayList<>();

    public void addRule(AuthRule rule) {
        rules.add(rule);
    }

    /**
     * @since 3.1
     */
    @Override
    public void handle(Context ctx) throws Throwable {
        //尝试前缀过滤
        if (Utils.isNotEmpty(pathPrefix)) {
            if (ctx.pathNew().startsWith(pathPrefix) == false) {
                return;
            }
        }

        //尝试乎略处理 @since 3.3
        Action action = ctx.action();
        if (action != null) {
            if (action.method().isAnnotationPresent(AuthIgnore.class) ||
                    action.controller().rawClz().isAnnotationPresent(AuthIgnore.class)) {
                return;
            }
        }

        //尝试规则处理
        for (AuthRule r : rules) {
            if (ctx.getHandled()) {
                return;
            }

            r.handle(ctx);
        }
    }
}