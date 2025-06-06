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
package features.test4;

import features.model.CustomDateDo;
import features.model.UserDo;
import org.junit.jupiter.api.Test;
import org.noear.solon.annotation.Import;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.serialization.snack3.SnackRenderFactory;
import org.noear.solon.test.SolonTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2023/1/16 created
 */
@Import(profiles = "classpath:features2_test4.yml")
@SolonTest
public class TestQuickConfig {
    @Inject
    SnackRenderFactory renderFactory;

    @Test
    public void hello2() throws Throwable {
        UserDo userDo = new UserDo();

        Map<String, Object> data = new HashMap<>();
        data.put("time", new Date(1673861993477L));
        data.put("long", 12L);
        data.put("int", 12);
        data.put("null", null);

        userDo.setMap1(data);

        ContextEmpty ctx = new ContextEmpty();
        renderFactory.create().render(userDo, ctx);
        String output = ctx.attr("output");

        System.out.println(output);

        //完美
        assert "{\"s0\":\"\",\"s1\":\"noear\",\"b0\":0,\"b1\":1,\"n0\":\"0\",\"n1\":\"1\",\"d0\":0.0,\"d1\":1.0,\"obj0\":null,\"list0\":[],\"map0\":null,\"map1\":{\"null\":null,\"time\":\"2023-01-16 17:39:53\",\"long\":\"12\",\"int\":12}}".equals(output);
    }

    @Test
    public void date2() throws Throwable {
        CustomDateDo customDateDo2 = new CustomDateDo();
        customDateDo2.setDate(new Date(1673861993477L));
        customDateDo2.setDate2(new Date(1673861993477L));

        ContextEmpty ctx = new ContextEmpty();
        renderFactory.create().render(customDateDo2, ctx);
        String output = ctx.attr("output");

        System.out.println(output);

        //完美
        assert "{\"date\":\"2023-01-16 17:39:53\",\"date2\":\"2023-01-16\"}".equals(output);
    }
}
