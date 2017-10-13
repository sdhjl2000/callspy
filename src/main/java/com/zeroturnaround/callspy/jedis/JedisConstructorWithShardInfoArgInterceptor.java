package com.zeroturnaround.callspy.jedis;

import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceConstructorInterceptor;
import redis.clients.jedis.JedisShardInfo;

public class JedisConstructorWithShardInfoArgInterceptor implements InstanceConstructorInterceptor {

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        String redisConnInfo;
        JedisShardInfo shardInfo = (JedisShardInfo)allArguments[0];
        redisConnInfo = shardInfo.getHost() + ":" + shardInfo.getPort();
        objInst.setSkyWalkingDynamicField(redisConnInfo);
    }
}
