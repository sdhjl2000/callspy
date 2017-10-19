/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package com.zeroturnaround.callspy.mongodb.define;

import static com.zeroturnaround.callspy.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;
import static com.zeroturnaround.callspy.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;

import com.zeroturnaround.callspy.plugin.interceptor.ConstructorInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class MongoDBInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "org.springframework.data.mongodb.core.MongoTemplate";

    private static final String MONGDB_METHOD_INTERCET_CLASS = "com.zeroturnaround.callspy.mongodb.MongoDBMethodInterceptor";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return null;
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("doFindAndDelete").or(named("doFindAndModify")).or(named("doFindAndRemove")).or(named("doInsert")).or(named("doInsertBatch")).or(named("doInsert"))
                            .or(named("doRemove")).or(named("doSave")).or(named("doUpdate"));
                }

                @Override
                public String getMethodsInterceptor() {
                    return MONGDB_METHOD_INTERCET_CLASS;
                }

                @Override
                public boolean isOverrideArgs() {
                    return true;
                }
            }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }

}
