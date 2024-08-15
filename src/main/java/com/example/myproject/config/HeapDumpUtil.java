package com.example.myproject.config;

import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.management.ManagementFactory;

public class HeapDumpUtil {
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";
    private static HotSpotDiagnosticMXBean hotspotMBean;

    static {
        try {
            hotspotMBean = ManagementFactory.newPlatformMXBeanProxy(
                    ManagementFactory.getPlatformMBeanServer(), HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void dumpHeap(String filePath, boolean live) {
        try {
            hotspotMBean.dumpHeap(filePath, live);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
