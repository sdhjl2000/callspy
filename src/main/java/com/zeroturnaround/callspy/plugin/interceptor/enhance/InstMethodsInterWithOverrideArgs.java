package com.zeroturnaround.callspy.plugin.interceptor.enhance;

import java.lang.reflect.Method;

import com.zeroturnaround.callspy.logging.ILog;
import com.zeroturnaround.callspy.logging.LogManager;
import com.zeroturnaround.callspy.plugin.PluginException;
import com.zeroturnaround.callspy.plugin.interceptor.loader.InterceptorInstanceLoader;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;



/**
 * The actual byte-buddy's interceptor to intercept class instance methods.
 * In this class, it provide a bridge between byte-buddy and sky-walking plugin.
 *
 * @author wusheng
 */
public class InstMethodsInterWithOverrideArgs {
    private static final ILog logger = LogManager.getLogger(InstMethodsInterWithOverrideArgs.class);

    /**
     * An {@link InstanceMethodsAroundInterceptor}
     * This name should only stay in {@link String}, the real {@link Class} type will trigger classloader failure.
     * If you want to know more, please check on books about Classloader or Classloader appointment mechanism.
     */
    private InstanceMethodsAroundInterceptor interceptor;

    /**
     * @param instanceMethodsAroundInterceptorClassName class full name.
     */
    public InstMethodsInterWithOverrideArgs(String instanceMethodsAroundInterceptorClassName, ClassLoader classLoader) {
        try {
            interceptor = InterceptorInstanceLoader.load(instanceMethodsAroundInterceptorClassName, classLoader);
        } catch (Throwable t) {
            throw new PluginException("Can't create InstanceMethodsAroundInterceptor.", t);
        }
    }

    /**
     * Intercept the target instance method.
     *
     * @param obj target class instance.
     * @param allArguments all method arguments
     * @param method method description.
     * @param zuper the origin call ref.
     * @return the return value of target instance method.
     * @throws Exception only throw exception because of zuper.call() or unexpected exception in sky-walking ( This is a
     * bug, if anything triggers this condition ).
     */
    @RuntimeType
    public Object intercept(@This Object obj,
        @AllArguments Object[] allArguments,
        @Origin Method method,
        @Morph OverrideCallable zuper
    ) throws Throwable {
        EnhancedInstance targetObject = (EnhancedInstance)obj;

        MethodInterceptResult result = new MethodInterceptResult();
//        try {
        interceptor.beforeMethod(targetObject, method, allArguments, method.getParameterTypes(), result);
//        } catch (Throwable t) {
//            logger.error(t, "class[{}] before method[{}] intercept failure", obj.getClass(), method.getName());
//        }

        Object ret = null;
        try {
            if (!result.isContinue()) {
                ret = result._ret();
            } else {
                ret = zuper.call(allArguments);
            }
        } catch (Throwable t) {
            try {
                interceptor.handleMethodException(targetObject, method, allArguments, method.getParameterTypes(),
                    t);
            } catch (Throwable t2) {
                logger.error(t2, "class[{}] handle method[{}] exception failure", obj.getClass(), method.getName());
            }
            throw t;
        } finally {
            try {
                ret = interceptor.afterMethod(targetObject, method, allArguments, method.getParameterTypes(),
                    ret);
            } catch (Throwable t) {
                logger.error(t,"class[{}] after method[{}] intercept failure", obj.getClass(), method.getName());
            }
        }
        return ret;
    }
}
