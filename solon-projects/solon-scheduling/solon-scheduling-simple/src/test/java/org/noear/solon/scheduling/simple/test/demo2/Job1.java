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
package org.noear.solon.scheduling.simple.test.demo2;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author noear 2022/10/15 created
 */
@Slf4j
@Scheduled(cron = "1/2 * * * * ?")
public class Job1 implements Runnable {
    @Override
    public void run() {
        log.warn(new Date() + ": 1/2 * * * * ?");

        try {
            Thread.sleep(1000 * 2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
