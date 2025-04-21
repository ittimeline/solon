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
package org.noear.solon.web.rx.integration;

import org.noear.solon.rx.handle.RxChainManager;
import org.noear.solon.rx.handle.RxContext;
import org.noear.solon.rx.handle.RxContextDefault;
import org.noear.solon.rx.handle.RxHandler;
import org.noear.solon.core.util.MimeType;
import org.noear.solon.core.handle.ReturnValueHandler;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.ClassUtil;

import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;

/**
 * Action 响应式返回处理
 *
 * @author noear
 * @since 2.3
 */
public class RxReturnValueHandler implements ReturnValueHandler {
    private final boolean hasReactor;
    private RxChainManager chainManager;

    public RxReturnValueHandler() {
        this.hasReactor = ClassUtil.hasClass(() -> Flux.class);
        this.chainManager = RxChainManager.getInstance();
    }

    @Override
    public boolean matched(Context ctx, Class<?> returnType) {
        return Publisher.class.isAssignableFrom(returnType);
    }

    @Override
    public void returnHandle(Context ctx, Object result) throws Throwable {
        if (result != null) {
            //转为响应式上下文
            RxContext rxCtx = new RxContextDefault(ctx);

            //预处理
            boolean isStreaming = isStreaming(ctx);
            Publisher publisher = postPublisher(ctx, result, isStreaming);

            //处理
            RxHandler handler = new RxHandlerImpl(publisher, isStreaming);


            chainManager.doFilter(rxCtx, handler)
                    .doOnError(err -> {
                        try {
                            ctx.errors = err;
                            ctx.status(500);
                        } finally {
                            //onComplete();
                            if (ctx.asyncSupported()) {
                                ctx.asyncComplete();
                            }
                        }
                    }).doOnComplete(() -> {
                        if (ctx.asyncSupported()) {
                            ctx.asyncComplete();
                        }
                    }).subscribe();
        }
    }

    protected boolean isStreaming(Context ctx) {
        return MimeType.isStreaming(ctx.contentTypeNew()) ||
                MimeType.isStreaming(ctx.acceptNew());
    }

    /**
     * 确认发布者
     */
    protected Publisher postPublisher(Context ctx, Object result, boolean isStreaming) throws Throwable {
        if (hasReactor) {
            //reactor 排除也不会出错
            if (result instanceof Flux) {
                if (isStreaming == false) {
                    return ((Flux) result).collectList();
                }
            }
        }

        return (Publisher) result;
    }
}