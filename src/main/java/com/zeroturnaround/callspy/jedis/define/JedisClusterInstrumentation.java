package com.zeroturnaround.callspy.jedis.define;

import static com.zeroturnaround.callspy.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;
import static com.zeroturnaround.callspy.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.takesArgument;

import java.util.Set;

import com.zeroturnaround.callspy.jedis.RedisMethodMatch;
import com.zeroturnaround.callspy.plugin.interceptor.ConstructorInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;


/**
 * {@link JedisClusterInstrumentation} presents that skywalking intercepts all constructors and methods of {@link
 * redis.clients.jedis.JedisCluster}. {@link JedisClusterConstructorWithHostAndPortArgInterceptor}
 * intercepts all constructor with argument {@link redis.clients.jedis.HostAndPort} and the other constructor intercept
 * by class {@link JedisClusterConstructorWithListHostAndPortArgInterceptor}. {@link JedisMethodInterceptor} intercept
 * all methods of {@link redis.clients.jedis.JedisCluster}
 *
 * @author zhangxin
 */
public class JedisClusterInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ARGUMENT_TYPE_NAME = "redis.clients.jedis.HostAndPort";
    private static final String ENHANCE_CLASS = "redis.clients.jedis.JedisCluster";
    private static final String CONSTRUCTOR_WITH_LIST_HOSTANDPORT_ARG_INTERCEPT_CLASS = "com.zeroturnaround.callspy.jedis.JedisClusterConstructorWithListHostAndPortArgInterceptor";
    private static final String METHOD_INTERCEPT_CLASS = "com.zeroturnaround.callspy.jedis.JedisMethodInterceptor";
    private static final String CONSTRUCTOR_WITH_HOSTANDPORT_ARG_INTERCEPT_CLASS = "com.zeroturnaround.callspy.jedis.JedisClusterConstructorWithHostAndPortArgInterceptor";

    @Override
    public ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[] {
            new ConstructorInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getConstructorMatcher() {
                    return takesArgument(0, Set.class);
                }

                @Override
                public String getConstructorInterceptor() {
                    return CONSTRUCTOR_WITH_LIST_HOSTANDPORT_ARG_INTERCEPT_CLASS;
                }
            },
            new ConstructorInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getConstructorMatcher() {
                    return takesArgumentWithType(0, ARGUMENT_TYPE_NAME);
                }

                @Override
                public String getConstructorInterceptor() {
                    return CONSTRUCTOR_WITH_HOSTANDPORT_ARG_INTERCEPT_CLASS;
                }
            }
        };
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return RedisMethodMatch.INSTANCE.getJedisClusterMethodMatcher();
                }

                @Override
                public String getMethodsInterceptor() {
                    return METHOD_INTERCEPT_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }
}
