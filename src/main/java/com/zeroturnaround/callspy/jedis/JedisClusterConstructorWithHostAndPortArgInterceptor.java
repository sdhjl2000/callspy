package com.zeroturnaround.callspy.jedis;


import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceConstructorInterceptor;
import redis.clients.jedis.HostAndPort;

public class JedisClusterConstructorWithHostAndPortArgInterceptor implements InstanceConstructorInterceptor {

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        HostAndPort hostAndPort = (HostAndPort)allArguments[0];
        objInst.setSkyWalkingDynamicField(hostAndPort.getHost() + ":" + hostAndPort.getPort());
    }
}
