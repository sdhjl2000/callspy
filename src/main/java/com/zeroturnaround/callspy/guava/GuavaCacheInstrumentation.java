package com.zeroturnaround.callspy.guava;

import static com.zeroturnaround.callspy.plugin.match.HierarchyMatch.byHierarchyMatch;
import static com.zeroturnaround.callspy.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;

import java.util.List;

import com.google.common.collect.Lists;
import com.zeroturnaround.callspy.plugin.interceptor.ConstructorInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;


public class GuavaCacheInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "com.google.common.cache.CacheLoader";
    private static final String INTERCEPT_CLASS = "com.zeroturnaround.callspy.guava.GuavaCacheInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        String[] parentType=new String[]{ENHANCE_CLASS};
        return byName(ENHANCE_CLASS);
    }

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
                    return named("load");
                }

                @Override
                public String getMethodsInterceptor() {
                    return INTERCEPT_CLASS;
                }

                @Override
                public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }
}
