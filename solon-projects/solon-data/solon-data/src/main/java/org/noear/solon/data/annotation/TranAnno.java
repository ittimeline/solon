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
package org.noear.solon.data.annotation;

import org.noear.solon.data.tran.TranIsolation;
import org.noear.solon.data.tran.TranPolicy;

import java.lang.annotation.Annotation;

/**
 * 事务注解类
 *
 * @author noear
 * @since 1.9
 * @deprecated 3.1 {@link TransactionAnno}
 */
@Deprecated
public class TranAnno implements Tran {
    /**
     * 事务传导策略
     */
    private TranPolicy _policy = TranPolicy.required;
    /*
     * 事务隔离等级
     * */
    private TranIsolation _isolation = TranIsolation.unspecified;
    /**
     * 只读事务
     */
    private boolean _readOnly = false;
    /**
     * 消息
     * */
    private String _message = "";

    @Override
    public TranPolicy policy() {
        return _policy;
    }

    public TranAnno policy(TranPolicy policy){
        _policy = policy;
        return this;
    }

    @Override
    public TranIsolation isolation() {
        return _isolation;
    }
    public TranAnno isolation(TranIsolation isolation){
        _isolation = isolation;
        return this;
    }

    @Override
    public boolean readOnly() {
        return _readOnly;
    }

    @Override
    public String message() {
        return _message;
    }

    public TranAnno message(String message){
        _message = message;
        return this;
    }

    public TranAnno readOnly(boolean readOnly){
        _readOnly = readOnly;
        return this;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Tran.class;
    }
}
