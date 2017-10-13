package com.zeroturnaround.callspy.jedis;


import com.zeroturnaround.callspy.plugin.interceptor.enhance.EnhancedInstance;
import com.zeroturnaround.callspy.plugin.interceptor.enhance.InstanceConstructorInterceptor;

public class JedisConstructorWithStringArgInterceptor implements InstanceConstructorInterceptor {

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        String host = (String)allArguments[0];
        String port = "6379";
        if (allArguments.length > 1) {
            port = String.valueOf(allArguments[1]);
        }

        objInst.setSkyWalkingDynamicField(host + ":" + port);
    }
}
