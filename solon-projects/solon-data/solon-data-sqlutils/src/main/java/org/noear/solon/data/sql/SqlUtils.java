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
package org.noear.solon.data.sql;

import org.noear.solon.Solon;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.solon.data.sql.impl.SimpleSqlUtils;
import org.noear.solon.lang.Preview;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Sql 工具类（线程安全，可作为单例保存）
 *
 * @author noear
 * @since 3.0
 */
@Preview("3.0")
public interface SqlUtils {
    static SqlUtils of(DataSource ds) {
        assert ds != null;
        return new SimpleSqlUtils(ds);
    }

    static SqlUtils ofName(String dsName) {
        return of(Solon.context().getBean(dsName));
    }

    /**
     * 初始化数据库
     *
     * @param scriptUri 示例：`classpath:demo.sql` 或 `file:demo.sql`
     */
    default void initDatabase(String scriptUri) throws IOException, SQLException {
        String sql = ResourceUtil.findResourceAsString(scriptUri);

        for (String s1 : sql.split(";")) {
            if (s1.trim().length() > 10) {
                this.sql(s1).update();
            }
        }
    }

    /**
     * 执行代码
     *
     * @param sql 代码
     */
    SqlQuerier sql(String sql, Object... args);

    /**
     * 执行代码
     *
     * @param sqlSpec 代码申明
     */
    default SqlQuerier sql(SqlSpec sqlSpec) {
        return sql(sqlSpec.getSql(), sqlSpec.getArgs());
    }
}