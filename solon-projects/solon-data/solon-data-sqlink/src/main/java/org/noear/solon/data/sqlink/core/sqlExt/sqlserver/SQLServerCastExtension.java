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
package org.noear.solon.data.sqlink.core.sqlExt.sqlserver;

import org.noear.solon.data.sqlink.base.SqLinkConfig;
import org.noear.solon.data.sqlink.base.expression.ISqlExpression;
import org.noear.solon.data.sqlink.base.expression.ISqlTypeExpression;
import org.noear.solon.data.sqlink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.noear.solon.data.sqlink.core.visitor.ExpressionUtil.*;

/**
 * SQLServer类型转换函数扩展
 *
 * @author kiryu1223
 * @since 3.0
 */
public class SQLServerCastExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(SqLinkConfig config, Method method, List<ISqlExpression> args) {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression expression = args.get(1);
        ISqlTypeExpression typeExpression = (ISqlTypeExpression) expression;
        Class<?> type = typeExpression.getType();
        templates.add("CAST(");
        sqlExpressions.add(args.get(0));
        String unit;
        if (isChar(type)) {
            unit = "NCHAR(1)";
        }
        else if (isString(type)) {
            unit = "NVARCHAR";
        }
        else if (isTime(type)) {
            unit = "TIME";
        }
        else if (isDate(type)) {
            unit = "DATE";
        }
        else if (isDateTime(type)) {
            unit = "DATETIME";
        }
        else if (isFloat(type) || isDouble(type)) {
            unit = "DECIMAL(32,16)";
        }
        else if (isDecimal(type)) {
            unit = "DECIMAL(32,18)";
        }
        else if (isByte(type)) {
            unit = "TINYINT";
        }
        else if (isShort(type)) {
            unit = "SMALLINT";
        }
        else if (isInt(type)) {
            unit = "INT";
        }
        else if (isLong(type)) {
            unit = "BIGINT";
        }
        else if (isBool(type)) {
            unit = "BIT";
        }
        else {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
        templates.add(" AS " + unit + ")");
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
