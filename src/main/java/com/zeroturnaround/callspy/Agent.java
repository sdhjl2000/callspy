package com.zeroturnaround.callspy;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;

import com.ea.agentloader.AgentLoader;
import com.zeroturnaround.callspy.logging.EasyLogResolver;
import com.zeroturnaround.callspy.logging.ILog;
import com.zeroturnaround.callspy.logging.LogManager;
import com.zeroturnaround.callspy.plugin.AbstractClassEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.PluginBootstrap;
import com.zeroturnaround.callspy.plugin.PluginFinder;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;


public class Agent {
  private static final ILog logger;

  static {
    LogManager.setLogResolver(new EasyLogResolver());
    logger = LogManager.getLogger(Agent.class);
  }
  public static void agentmain(String args, Instrumentation instrumentation){
    final PluginFinder pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());
    new AgentBuilder.Default().type(pluginFinder.buildMatch()).transform(new AgentBuilder.Transformer() {
      @Override
      public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                              ClassLoader classLoader, JavaModule module) {
        AbstractClassEnhancePluginDefine pluginDefine = pluginFinder.find(typeDescription, classLoader);
        if (pluginDefine != null) {
          DynamicType.Builder<?> newBuilder = pluginDefine.define(typeDescription.getTypeName(), builder, classLoader);
          if (newBuilder != null) {
            logger.info("Finish the prepare stage for {}.", typeDescription.getName());
            return newBuilder;
          }
        }

        logger.info("Matched class {}, but ignore by finding mechanism.", typeDescription.getTypeName());
        return builder;
      }
    }).with(new AgentBuilder.Listener() {
      @Override
      public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

      }

      @Override
      public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                   boolean loaded, DynamicType dynamicType) {
        logger.info("On Transformation class {}.", typeDescription.getName());
      }

      @Override
      public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                            boolean loaded) {

      }

      @Override public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
                                    Throwable throwable) {
        logger.info("Failed to enhance class " + typeName, throwable);
      }

      @Override
      public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
      }
    }).installOn(instrumentation);
  }

  public static void main(String[] args) throws URISyntaxException {
    AgentLoader.loadAgentClass(Agent.class.getName(), null);

  }
}