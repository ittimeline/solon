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
package webapp.nami;


import org.noear.nami.annotation.NamiBody;
import org.noear.nami.annotation.NamiMapping;
import org.noear.nami.annotation.NamiClient;

/**
 * @author noear 2022/12/6 created
 */
@NamiClient(name="local", path="/nami/ComplexModelService1/")
public interface ComplexModelService1 {
    @NamiMapping("PUT")
    void save(@NamiBody ComplexModel model);

    @NamiMapping("POST")
    String save2(String name1, @NamiBody ComplexModel model);

    ComplexModel read(Integer modelId);
}
