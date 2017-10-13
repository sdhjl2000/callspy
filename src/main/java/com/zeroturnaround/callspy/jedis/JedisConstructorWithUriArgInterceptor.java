package com.zeroturnaround.callspy.jedis;

import java.net.URI;

import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceConstructorInterceptor;


public class JedisConstructorWithUriArgInterceptor implements InstanceConstructorInterceptor {

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        URI uri = (URI)allArguments[0];
        objInst.setSkyWalkingDynamicField(uri.getHost() + ":" + uri.getPort());
    }
}
