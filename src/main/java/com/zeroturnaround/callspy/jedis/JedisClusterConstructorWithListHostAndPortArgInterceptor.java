package com.zeroturnaround.callspy.jedis;

import java.util.Set;

import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceConstructorInterceptor;
import redis.clients.jedis.HostAndPort;

public class JedisClusterConstructorWithListHostAndPortArgInterceptor implements InstanceConstructorInterceptor {

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        StringBuilder redisConnInfo = new StringBuilder();
        Set<HostAndPort> hostAndPorts = (Set<HostAndPort>)allArguments[0];
        for (HostAndPort hostAndPort : hostAndPorts) {
            redisConnInfo.append(hostAndPort.toString()).append(";");
        }

        objInst.setSkyWalkingDynamicField(redisConnInfo.toString());
    }
}
