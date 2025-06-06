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
package org.noear.solon.validation.annotation;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.util.ClassUtil;
import org.noear.solon.validation.util.StringUtils;
import org.noear.solon.validation.Validator;

/**
 *
 * @author noear
 * @since 1.0
 * */
public class MinValidator implements Validator<Min> {
    public static final MinValidator instance = new MinValidator();

    @Override
    public String message(Min anno) {
        return anno.message();
    }

    @Override
    public Class<?>[] groups(Min anno) {
        return anno.groups();
    }

    @Override
    public boolean isSupportValueType(Class<?> type) {
        return ClassUtil.isNumberType(type);
    }

    @Override
    public Result validateOfValue(Min anno, Object val0, StringBuilder tmp) {
        if (val0 != null && val0 instanceof Number == false) {
            return Result.failure();
        }

        Number val = (Number) val0;

        if (verify(anno, val) == false) {
            return Result.failure();
        } else {
            return Result.succeed();
        }
    }

    @Override
    public Result validateOfContext(Context ctx, Min anno, String name, StringBuilder tmp) {
        String val = ctx.param(name);

        //如果为空，算通过（交由 @NotNull 或 @NotEmpty 或 @NotBlank 进一步控制）
        if (Utils.isEmpty(val)) {
            return Result.succeed();
        }

        if (StringUtils.isInteger(val) == false) {
            return Result.failure(name);
        }

        if (verify(anno, Long.parseLong(val)) == false) {
            return Result.failure(name);
        } else {
            return Result.succeed();
        }
    }

    private boolean verify(Min anno, Number val) {
        //如果为空，算通过（交由 @NotNull 进一步控制）
        if (val == null) {
            return true;
        }

        return val.longValue() >= anno.value();
    }
}
