package com.zeroturnaround.callspy.jedis;

import java.lang.reflect.Method;

import com.zeroturnaround.callspy.logging.ILog;
import com.zeroturnaround.callspy.logging.LogManager;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.MethodInterceptResult;


public class JedisMethodInterceptor implements InstanceMethodsAroundInterceptor {
    private final ILog logger= LogManager.getLogger(JedisMethodInterceptor.class);

    @Override public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                       Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        String peer = String.valueOf(objInst.getSkyWalkingDynamicField());
        logger.info(peer);
    }

    @Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Object ret) throws Throwable {
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {

    }
}
