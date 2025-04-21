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
package org.noear.solon.data.rx.sql.impl;

import io.r2dbc.spi.ConnectionFactory;
import org.noear.solon.data.rx.sql.RxSqlQuerier;
import org.noear.solon.data.rx.sql.RxSqlUtils;

/**
 * Sql 工具类简单实现
 *
 * @author noear
 * @since 3.0
 */
public class SimpleRxSqlUtils implements RxSqlUtils {
    private final ConnectionFactory ds;

    public SimpleRxSqlUtils(ConnectionFactory ds) {
        this.ds = ds;
    }

    @Override
    public RxSqlQuerier sql(String sql, Object... args) {
        return new SimpleRxSqlQuerier(ds, sql).params(args);
    }
}