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
package com.swagger.demo.controller.admin;

import com.swagger.demo.model.OrderType;
import com.swagger.demo.model.ResultModelEx;
import com.swagger.demo.model.bean.DeviceParamBean;
import com.swagger.demo.model.dto.EquipDto;
import com.swagger.demo.model.dto.MachineDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noear.snack.ONode;
import org.noear.solon.annotation.*;
import io.swagger.solon.annotation.ApiRes;
import io.swagger.solon.annotation.ApiResProperty;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.docs.ApiEnum;
import com.swagger.demo.model.ResultModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lbq
 * 联系方式: 526509994@qq.com
 * 创建日期: 2020/9/16
 */
@Mapping("/simple")
@Api(description = "HelloWorld", tags = "最简用法")
@Controller
public class SimpleController {

    @ApiOperation(value = "不描述返回值", notes = "SwaggerConst.COMMON_RES定义返回")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
    })
    @Mapping("test1")
    public Map test1() {
        return new HashMap();
    }


    @ApiOperation(value = "简单返回值", notes = "SwaggerConst.COMMON_RES.data中返回值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
    })
    @Mapping("test2")
    public Map test2() {
        return new HashMap();
    }

    @ApiOperation(value = "url参数Json", notes = "多个参数在url中提交,json参数支持多个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
            @ApiImplicitParam(name = "device", value = "网关附加数据JSON", dataTypeClass = DeviceParamBean.class),
            @ApiImplicitParam(name = "equip", value = "农机具JSON", dataTypeClass = EquipDto.class),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
    })
    @Put
    @Mapping("test3_put")
    public Map test3_put(String paramA, String paramB, String device) {
        return new HashMap();
    }

    @ApiOperation(value = "body接口参数Json", notes = "在body中提交JSON.注意body只支持一个json串. body提交多个json将失效", httpMethod = ApiEnum.METHOD_POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "2024-01-01 00:00:00"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
            @ApiImplicitParam(name = "paramC", value = "参数c"),
            @ApiImplicitParam(name = "device", value = "网关附加数据JSON", dataTypeClass = DeviceParamBean.class, paramType = ApiEnum.PARAM_TYPE_BODY),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
    })
    @Mapping("test4/${paramPath}")
    public Map test4(@Param(defaultValue = "") Date paramA,
                     String paramB,
                     OrderType paramC,
                     @Path Date paramPath,
                     String device) {
        return new HashMap();
    }

    @ApiOperation(value = "参数Object嵌套Object", notes = "递归实现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
            @ApiImplicitParam(name = "machine", value = "农机JSON", dataTypeClass = MachineDto.class, paramType = ApiEnum.PARAM_TYPE_BODY),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
    })
    @Mapping("test5")
    public Map test5(String paramA, String paramB, String device) {
        return new HashMap();
    }

    @ApiOperation(value = "返回值Object嵌套Object", notes = "递归实现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
            @ApiResProperty(name = "equip", value = "农机具信息", dataTypeClass = EquipDto.class),
            @ApiResProperty(name = "equipList", value = "农机具集合", dataTypeClass = EquipDto.class, allowMultiple = true),
            @ApiResProperty(name = "machine", value = "农机信息", dataTypeClass = MachineDto.class),
    })
    @Mapping("test6")
    public ResultModel test6(String paramA, String paramB, String device) {
        DeviceParamBean deviceParamBean = ONode.deserialize(device, DeviceParamBean.class);

        Map kv = new HashMap();
        kv.put("paramA", paramA);
        kv.put("paramB", paramB);
        kv.put("device", deviceParamBean);

        ResultModel swaggerRes = new ResultModel();
        swaggerRes.setData(kv);

        return swaggerRes;
    }

    @ApiOperation("test62")
    @Mapping("test62")
    public ResultModel test6_2(ResultModel model) {
       return model;
    }

    @ApiOperation("test62-2")
    @Mapping("test62-2")
    public ResultModelEx test6_2_2(ResultModelEx model) {
        return model;
    }

    @ApiOperation("test63")
    @Mapping("test63")
    public ResultModel test6_3(@Body ResultModel model) {
        return model;
    }

    @ApiOperation("test63-2")
    @Mapping("test63-2")
    public ResultModelEx test6_3_2(@Body ResultModelEx model) {
        return model;
    }

    @ApiOperation(value = "url数组参数Json", notes = "数组参数在url中提交,json参数支持多个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
            @ApiImplicitParam(name = "device", value = "网关附加数据JSON", dataTypeClass = DeviceParamBean.class, allowMultiple = true),
            @ApiImplicitParam(name = "equip", value = "农机具JSON", dataTypeClass = EquipDto.class, allowMultiple = true),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
    })
    @Mapping("test7")
    public Map test7(String paramA, String paramB, String device) {
        return new HashMap();
    }

    @ApiOperation(value = "body接口数组参数Json", notes = "在body中提交JSON.注意body只支持一个json串. body提交多个json将失效", httpMethod = ApiEnum.METHOD_POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramA", value = "参数a", defaultValue = "1111"),
            @ApiImplicitParam(name = "paramB", value = "参数b", defaultValue = "222"),
            @ApiImplicitParam(name = "device", value = "网关附加数据JSON", dataTypeClass = DeviceParamBean.class, allowMultiple = true, paramType = ApiEnum.PARAM_TYPE_BODY),
    })
    @ApiRes({
            @ApiResProperty(name = "resA", value = "返回值A", example = "hello word1"),
            @ApiResProperty(name = "resB", value = "返回值b", example = "hello word2"),
    })
    @Mapping("test8")
    public Map test8(String paramA, String paramB, String device) {
        return new HashMap();
    }


    @ApiOperation(value = "示例2", httpMethod = ApiEnum.METHOD_POST, produces = ApiEnum.PRODUCES_JSON, consumes = ApiEnum.CONSUMES_FORM_DATA)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传文件", dataType = ApiEnum.FILE),
    })
    @Mapping("upFile")
    public Map upFile() {
        return new HashMap();
    }

    @ApiOperation("示例2-2")
    @Mapping("upFile2")
    public Map upFile2(UploadedFile file) {
        return new HashMap();
    }
}
