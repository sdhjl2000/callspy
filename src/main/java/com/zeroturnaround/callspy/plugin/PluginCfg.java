package com.zeroturnaround.callspy.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.zeroturnaround.callspy.plugin.exception.IllegalPluginDefineException;
import com.zeroturnaround.callspy.logging.ILog;
import com.zeroturnaround.callspy.logging.LogManager;

public enum PluginCfg {
    INSTANCE;

    private static final ILog logger = LogManager.getLogger(PluginCfg.class);

    private List<PluginDefine> pluginClassList = new ArrayList<PluginDefine>();

    void load(InputStream input) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String pluginDefine = null;
            while ((pluginDefine = reader.readLine()) != null) {
                try {
                    if (pluginDefine == null || pluginDefine.trim().length() == 0) {
                        continue;
                    }
                    PluginDefine plugin = PluginDefine.build(pluginDefine);
                    if (plugin.enable()) {
                        pluginClassList.add(plugin);
                    }
                } catch (IllegalPluginDefineException e) {
                    logger.error(e,"Failed to format plugin({}) define.", pluginDefine);
                }
            }
        } finally {
            input.close();
        }
    }

    public List<PluginDefine> getPluginClassList() {
        return pluginClassList;
    }

}
