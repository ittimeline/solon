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
package org.noear.solon.core.handle;

/**
 * Session 状态器工厂
 *
 * @author noear
 * @since 1.3
 */
public interface SessionStateFactory {
    /**
     * 优先级
     * */
    default int priority(){return 0;}

    /**
     * 创建会话状态
     * */
    SessionState create(Context ctx);
}
