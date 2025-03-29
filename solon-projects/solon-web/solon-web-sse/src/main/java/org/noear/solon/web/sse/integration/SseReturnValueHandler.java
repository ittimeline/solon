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
package org.noear.solon.web.sse.integration;

import org.noear.solon.core.handle.ReturnValueHandler;
import org.noear.solon.core.handle.Context;
import org.noear.solon.web.sse.SseEmitter;
import org.noear.solon.web.sse.SseEmitterHandler;

/**
 * @author noear
 * @since 2.3
 */
public class SseReturnValueHandler implements ReturnValueHandler {
    @Override
    public boolean matched(Context ctx, Class<?> returnType) {
        return SseEmitter.class.isAssignableFrom(returnType);
    }

    @Override
    public void returnHandle(Context ctx, Object returnValue) throws Throwable {
        if (returnValue != null) {
            if (ctx.asyncSupported() == false) {
                throw new IllegalStateException("This boot plugin does not support asynchronous mode");
            }

            new SseEmitterHandler((SseEmitter) returnValue).start();
        }
    }
}
