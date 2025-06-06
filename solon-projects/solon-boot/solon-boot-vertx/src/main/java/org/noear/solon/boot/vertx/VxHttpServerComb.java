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
package org.noear.solon.boot.vertx;

import org.noear.solon.boot.ServerLifecycle;
import org.noear.solon.boot.http.HttpServerConfigure;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.handle.Handler;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author noear
 * @since 2.9
 */
public class VxHttpServerComb implements HttpServerConfigure, ServerLifecycle {
    protected final AppContext context;
    protected final HttpServerProps props;

    private Executor workExecutor;
    private boolean enableWebSocket;
    private Handler handler;
    private boolean enableSsl = true;
    private boolean enableHttp2 = false;
    private SSLContext sslContext;
    private Set<Integer> addHttpPorts = new LinkedHashSet<>();
    private List<VxHttpServer> servers = new ArrayList<>();

    public VxHttpServerComb(HttpServerProps props, AppContext context) {
        this.props = props;
        this.context = context;
    }

    @Override
    public void enableSsl(boolean enable, SSLContext sslContext) {
        this.enableSsl = enable;
        this.sslContext = sslContext;
    }

    @Override
    public void enableHttp2(boolean enable) {
        this.enableHttp2 = enable;
    }

    /**
     * 添加 HttpPort（当 ssl 时，可再开个 http 端口）
     */
    @Override
    public void addHttpPort(int port) {
        addHttpPorts.add(port);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void setExecutor(Executor executor) {
        this.workExecutor = executor;
    }

    public void enableWebSocket(boolean enableWebSocket) {
        this.enableWebSocket = enableWebSocket;
    }

    public boolean isSecure() {
        if (servers.size() > 0) {
            return servers.get(0).isSecure();
        } else {
            return false;
        }
    }

    @Override
    public void start(String host, int port) throws Throwable {
        {
            //主端口，支持外部扩展处理器（例，网关）
            VxHttpServer s1 = new VxHttpServer(props, context, true);
            s1.setWorkExecutor(workExecutor);
            s1.enableWebSocket(enableWebSocket);
            s1.setHandler(handler);
            s1.enableSsl(enableSsl, sslContext);
            s1.enableHttp2(enableHttp2);
            s1.start(host, port);

            servers.add(s1);
        }

        //增量端口，不支持外部扩展处理器
        for (Integer portAdd : addHttpPorts) {
            VxHttpServer s2 = new VxHttpServer(props, context, false);
            s2.setWorkExecutor(workExecutor);
            s2.enableWebSocket(enableWebSocket);
            s2.setHandler(handler);
            s2.enableSsl(false, null); //只支持http
            s2.start(host, portAdd);

            servers.add(s2);
        }
    }

    @Override
    public void stop() throws Throwable {
        for (ServerLifecycle s : servers) {
            s.stop();
        }
    }
}