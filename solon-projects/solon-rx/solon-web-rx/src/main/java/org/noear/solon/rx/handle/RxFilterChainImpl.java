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
package org.noear.solon.rx.handle;

import org.noear.solon.core.util.RankEntity;
import org.noear.solon.rx.Completable;

import java.util.List;

/**
 * 响应式过滤器链实现
 *
 * @author noear
 * @since 3.1
 */
public class RxFilterChainImpl implements RxFilterChain {
    private final List<RankEntity<RxFilter>> filterList;
    private final RxHandler lastHandler;
    private int index;

    public RxFilterChainImpl(List<RankEntity<RxFilter>> filterList) {
        this(filterList, null);
    }

    public RxFilterChainImpl(List<RankEntity<RxFilter>> filterList, RxHandler lastHandler) {
        this.filterList = filterList;
        this.index = 0;
        this.lastHandler = lastHandler;
    }

    /**
     * 过滤
     *
     * @param ctx 上下文
     */
    @Override
    public Completable doFilter(RxContext ctx) {
        if (lastHandler == null) {
            return filterList.get(index++).target.doFilter(ctx, this);
        } else {
            if (index < filterList.size()) {
                return filterList.get(index++).target.doFilter(ctx, this);
            } else {
                return lastHandler.handle(ctx);
            }
        }
    }
}