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
package org.noear.solon.auth.impl;

import org.noear.solon.auth.AuthStatus;
import org.noear.solon.auth.annotation.AuthIgnore;
import org.noear.solon.core.handle.Action;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.auth.AuthFailureHandler;
import org.noear.solon.auth.AuthRule;
import org.noear.solon.auth.AuthUtil;
import org.noear.solon.core.route.PathRule;

/**
 * 授权规则默认实现
 *
 * @author noear
 * @since 1.4
 */
public class AuthRuleImpl implements AuthRule {
    /**
     * 路径规则
     */
    private PathRule pathRule = new PathRule();

    private boolean verifyIp;
    private boolean verifyLogined;
    private boolean verifyPath;
    private String[] verifyPermissions;
    private boolean verifyPermissionsAnd;
    private String[] verifyRoles;
    private boolean verifyRolesAnd;
    private AuthFailureHandler failureHandler;

    @Override
    public AuthRule include(String pathPattern) {
        pathRule.include(pathPattern);
        return this;
    }

    @Override
    public AuthRule exclude(String pathPattern) {
        pathRule.exclude(pathPattern);
        return this;
    }

    @Override
    public AuthRule verifyIp() {
        verifyIp = true;
        return this;
    }

    @Override
    public AuthRule verifyLogined() {
        verifyLogined = true;
        return this;
    }

    @Override
    public AuthRule verifyPath() {
        verifyPath = true;
        verifyLogined = true;
        return this;
    }

    @Override
    public AuthRule verifyPermissions(String... permissions) {
        verifyPermissions = permissions;
        verifyPermissionsAnd = false;
        verifyLogined = true;
        return this;
    }

    @Override
    public AuthRule verifyPermissionsAnd(String... permissions) {
        verifyPermissions = permissions;
        verifyPermissionsAnd = true;
        verifyLogined = true;
        return this;
    }

    @Override
    public AuthRule verifyRoles(String... roles) {
        verifyRoles = roles;
        verifyRolesAnd = false;
        verifyLogined = true;
        return this;
    }

    @Override
    public AuthRule verifyRolesAnd(String... roles) {
        verifyRoles = roles;
        verifyRolesAnd = true;
        verifyLogined = true;
        return this;
    }

    @Override
    public AuthRule failure(AuthFailureHandler handler) {
        failureHandler = handler;
        return this;
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        String path = ctx.pathNew();

        if (pathRule.test(path) == false) {
            return;
        }

        Action action = ctx.action();
        if (action != null) {
            if (action.method().isAnnotationPresent(AuthIgnore.class) ||
                    action.controller().rawClz().isAnnotationPresent(AuthIgnore.class)) {
                return;
            }
        }

        //
        //Ip验证
        //
        if (verifyIp) {
            //验证登录情况
            String ip = ctx.realIp();
            if (AuthUtil.verifyIp(ip) == false) {
                //验证失败的
                failureDo(ctx, AuthStatus.OF_IP.toResult(ip));
                return;
            }
        }

        //
        //登录验证
        //
        if (verifyLogined) {
            //验证登录情况
            if (AuthUtil.verifyLogined() == false) {
                //未登录的，跳到登录页
                if (AuthUtil.adapter().loginUrl() == null) {
                    failureDo(ctx, AuthStatus.OF_LOGINED.toResult());
                } else {
                    ctx.redirect(AuthUtil.adapter().loginUrl());
                    ctx.setHandled(true);
                }
                return;
            }
        }

        //
        //路径验证
        //
        if (verifyPath) {
            //验证路径与方式权限
            if (AuthUtil.verifyPath(path, ctx.method()) == false) {
                //验证失败的
                failureDo(ctx, AuthStatus.OF_PATH.toResult());
                return;
            }
        }

        //
        //权限验证
        //
        if (verifyPermissions != null && verifyPermissions.length > 0) {
            boolean isOk = false;
            if (verifyPermissionsAnd) {
                isOk = AuthUtil.verifyPermissionsAnd(verifyPermissions);
            } else {
                isOk = AuthUtil.verifyPermissions(verifyPermissions);
            }

            if (isOk == false) {
                //验证失败的
                failureDo(ctx, AuthStatus.OF_PERMISSIONS.toResult());
                return;
            }
        }

        //
        //角色验证
        //
        if (verifyRoles != null && verifyRoles.length > 0) {
            boolean isOk = false;
            if (verifyRolesAnd) {
                isOk = AuthUtil.verifyRolesAnd(verifyRoles);
            } else {
                isOk = AuthUtil.verifyRoles(verifyRoles);
            }

            if (isOk == false) {
                //验证失败的
                failureDo(ctx, AuthStatus.OF_ROLES.toResult());
                return;
            }
        }
    }

    private void failureDo(Context c, Result r) throws Throwable {
        c.setHandled(true);
        if (failureHandler != null) {
            failureHandler.onFailure(c, r);
        } else {
            AuthUtil.adapter().failure().onFailure(c, r);
        }
    }
}
