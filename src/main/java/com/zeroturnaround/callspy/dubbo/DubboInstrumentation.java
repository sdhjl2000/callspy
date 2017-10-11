package com.zeroturnaround.callspy.dubbo;

import static com.zeroturnaround.callspy.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;

import com.zeroturnaround.callspy.plugin.interceptor.ConstructorInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;


/**
 * {@link DubboInstrumentation} presents that skywalking intercepts {@link com.alibaba.dubbo.monitor.support.MonitorFilter#invoke(com.alibaba.dubbo.rpc.Invoker,
 * com.alibaba.dubbo.rpc.Invocation)} by using {@link DubboInterceptor}.
 *
 * @author zhangxin
 */
public class DubboInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "com.alibaba.dubbo.monitor.support.MonitorFilter";
    private static final String INTERCEPT_CLASS = "com.zeroturnaround.callspy.dubbo.DubboInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
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
                    return named("invoke");
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
