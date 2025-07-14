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
package org.noear.solon.scheduling.quartz.integration;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import org.noear.solon.scheduling.annotation.Scheduled;
import org.noear.solon.scheduling.quartz.JobManager;
import org.noear.solon.scheduling.scheduled.manager.IJobManager;
import org.noear.solon.scheduling.scheduled.manager.JobExtractor;
import org.quartz.Job;
import org.quartz.Scheduler;

import java.lang.reflect.Method;

public class SchedulingQuartzPlugin implements Plugin {
    @Override
    public void start(AppContext context) {
        if (context.app().source().getAnnotation(EnableScheduling.class) == null) {
            return;
        }

        //注册 IJobManager
        context.wrapAndPut(IJobManager.class, JobManager.getInstance());

        //允许产生 Scheduler bean
        context.getBeanAsync(Scheduler.class, bean -> {
            JobManager.getInstance().setScheduler(bean);
        });

        //提取任务
        JobExtractor jobExtractor = new JobExtractor(JobManager.getInstance());
        context.beanBuilderAdd(Scheduled.class, jobExtractor);
        context.beanBuilderAdd(Scheduled.class, Job.class, ((clz, bw, anno) -> {
            Method method = Job.class.getDeclaredMethods()[0];
            jobExtractor.doExtract(bw, method, anno);
        }));


        context.beanExtractorAdd(Scheduled.class, jobExtractor);

        //容器加载完后，再启动任务
        context.lifecycle(Integer.MAX_VALUE, JobManager.getInstance());
    }
}
