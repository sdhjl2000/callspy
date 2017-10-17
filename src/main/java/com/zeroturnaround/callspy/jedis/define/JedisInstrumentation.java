package com.zeroturnaround.callspy.jedis.define;

import static com.zeroturnaround.callspy.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;
import static com.zeroturnaround.callspy.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.takesArgument;

import java.net.URI;

import com.zeroturnaround.callspy.jedis.RedisMethodMatch;
import com.zeroturnaround.callspy.plugin.interceptor.ConstructorInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;


/**
 * {@link JedisInstrumentation} presents that skywalking intercept all constructors and methods of {@link
 * redis.clients.jedis.Jedis}. {@link JedisConstructorWithShardInfoArgInterceptor} intercepts all constructor with
 * argument {@link redis.clients.jedis.HostAndPort} ,{@link JedisConstructorWithUriArgInterceptor} intercepts the
 * constructors with uri argument and the other constructor intercept by class {@link
 * JedisConstructorWithShardInfoArgInterceptor}. {@link JedisMethodInterceptor} intercept all methods of {@link
 * redis.clients.jedis.Jedis}.
 *
 * @author zhangxin
 */
public class JedisInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String HOST_AND_PORT_ARG_TYPE_NAME = "redis.clients.jedis.HostAndPort";
    private static final String ENHANCE_CLASS = "redis.clients.jedis.Jedis";
    private static final String CONSTRUCTOR_WITH_STRING_ARG_INTERCEPT_CLASS = "com.zeroturnaround.callspy.jedis.JedisConstructorWithStringArgInterceptor";
    private static final String CONSTRUCTOR_WITH_SHARD_INFO_ARG_INTERCEPT_CLASS = "com.zeroturnaround.callspy.jedis.JedisConstructorWithShardInfoArgInterceptor";
    private static final String CONSTRUCTOR_WITH_URI_ARG_INTERCEPT_CLASS = "com.zeroturnaround.callspy.jedis.JedisConstructorWithUriArgInterceptor";
    private static final String JEDIS_METHOD_INTERCET_CLASS = "com.zeroturnaround.callspy.jedis.JedisMethodInterceptor";

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
                    return takesArgument(0, String.class);
                }

                @Override
                public String getConstructorInterceptor() {
                    return CONSTRUCTOR_WITH_STRING_ARG_INTERCEPT_CLASS;
                }
            },
            new ConstructorInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getConstructorMatcher() {
                    return takesArgumentWithType(0, HOST_AND_PORT_ARG_TYPE_NAME);
                }

                @Override
                public String getConstructorInterceptor() {
                    return CONSTRUCTOR_WITH_SHARD_INFO_ARG_INTERCEPT_CLASS;
                }
            },
            new ConstructorInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getConstructorMatcher() {
                    return takesArgument(0, URI.class);
                }

                @Override
                public String getConstructorInterceptor() {
                    return CONSTRUCTOR_WITH_URI_ARG_INTERCEPT_CLASS;
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
                    return RedisMethodMatch.INSTANCE.getJedisMethodMatcher();
                }

                @Override
                public String getMethodsInterceptor() {
                    return JEDIS_METHOD_INTERCET_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return true;
                }
            }
        };
    }
}
