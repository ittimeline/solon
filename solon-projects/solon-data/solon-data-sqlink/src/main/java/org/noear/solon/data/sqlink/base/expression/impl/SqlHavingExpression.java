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
package org.noear.solon.data.sqlink.base.expression.impl;

import org.noear.solon.data.sqlink.base.SqLinkConfig;
import org.noear.solon.data.sqlink.base.expression.ISqlConditionsExpression;
import org.noear.solon.data.sqlink.base.expression.ISqlExpression;
import org.noear.solon.data.sqlink.base.expression.ISqlHavingExpression;
import org.noear.solon.data.sqlink.base.session.SqlValue;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlHavingExpression implements ISqlHavingExpression {
    private final ISqlConditionsExpression conditions;

    public SqlHavingExpression(ISqlConditionsExpression conditions) {
        this.conditions = conditions;
    }

    public void addCond(ISqlExpression condition) {
        conditions.addCondition(condition);
    }

    @Override
    public ISqlConditionsExpression getConditions() {
        return conditions;
    }

    @Override
    public String getSqlAndValue(SqLinkConfig config, List<SqlValue> values) {
        if (isEmpty()) return "";
        return "HAVING " + getConditions().getSqlAndValue(config, values);
    }
}
