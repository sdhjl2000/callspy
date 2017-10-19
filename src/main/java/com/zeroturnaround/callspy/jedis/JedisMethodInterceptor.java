package com.zeroturnaround.callspy.jedis;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import cn.com.duiba.boot.ext.autoconfigure.perftest.PerfTestAutoConfiguration;
import com.zeroturnaround.callspy.logging.ILog;
import com.zeroturnaround.callspy.logging.LogManager;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.MethodInterceptResult;
import redis.clients.jedis.Jedis;


public class JedisMethodInterceptor implements InstanceMethodsAroundInterceptor {
    private final ILog logger= LogManager.getLogger(JedisMethodInterceptor.class);
    private final ConcurrentHashMap<String, String> perfKeys = new ConcurrentHashMap<>();
    @Override public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                       Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        //PerfTestAutoConfiguration.TransmittableThreadLocalHolder.threadLocal2PressureTest
        ((Jedis) objInst).select(2);
    }

    @Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Object ret) throws Throwable {
        ((Jedis) objInst).select(0);
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {

    }
}
