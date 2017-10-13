package com.zeroturnaround.callspy.logging;


import com.zeroturnaround.callspy.conf.Config;
import com.zeroturnaround.callspy.util.StringUtil;

public class WriterFactory {
    public static IWriter getLogWriter() {
        if (!StringUtil.isEmpty(Config.Logging.DIR)) {
            return FileWriter.get();
        } else {
            return SystemOutWriter.INSTANCE;
        }
    }
}
