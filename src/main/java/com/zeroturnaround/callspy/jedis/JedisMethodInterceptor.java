package com.zeroturnaround.callspy.jedis;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.thoughtworks.paranamer.AnnotationParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
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
        String peer = String.valueOf(objInst.getSkyWalkingDynamicField());
        Paranamer info = new CachingParanamer(new AnnotationParanamer(new BytecodeReadingParanamer()));
        String[] parameterNames = info.lookupParameterNames(method);
        if (parameterNames.length > 0) {
            Integer index = Arrays.stream(parameterNames).collect(Collectors.toList()).indexOf("key");
            if (index >= 0) {
                ((Jedis) objInst).select(2);
                String key = (new String((byte[]) allArguments[index], Charset.forName("UTF-8")) + "_perf");
                allArguments[index] = key.getBytes(Charset.forName("UTF-8"));
            }
        }
    }

    @Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Object ret) throws Throwable {
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {

    }
}
