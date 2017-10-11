package com.zeroturnaround.callspy;

import java.lang.instrument.Instrumentation;

import com.zeroturnaround.callspy.plugin.AbstractClassEnhancePluginDefine;
import com.zeroturnaround.callspy.plugin.PluginBootstrap;
import com.zeroturnaround.callspy.plugin.PluginFinder;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

public class Agent {

  public static void premain(String args, Instrumentation instrumentation){

    final PluginFinder pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());


    new AgentBuilder.Default().type(pluginFinder.buildMatch()).transform(new AgentBuilder.Transformer() {
      @Override
      public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                              ClassLoader classLoader, JavaModule module) {
        AbstractClassEnhancePluginDefine pluginDefine = pluginFinder.find(typeDescription, classLoader);
        if (pluginDefine != null) {
          DynamicType.Builder<?> newBuilder = pluginDefine.define(typeDescription.getTypeName(), builder, classLoader);
          if (newBuilder != null) {
            System.out.println(String.format("Finish the prepare stage for {}.", typeDescription.getName()));
            return newBuilder;
          }
        }

        System.out.println(String.format("Matched class {}, but ignore by finding mechanism.", typeDescription.getTypeName()));
        return builder;
      }
    }).with(new AgentBuilder.Listener() {
      @Override
      public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

      }

      @Override
      public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                   boolean loaded, DynamicType dynamicType) {
        System.out.println(String.format("On Transformation class {}.", typeDescription.getName()));
      }

      @Override
      public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                            boolean loaded) {

      }

      @Override public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
                                    Throwable throwable) {
        System.out.println(String.format("Failed to enhance class " + typeName, throwable));
      }

      @Override
      public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
      }
    }).installOn(instrumentation);
  }
}
