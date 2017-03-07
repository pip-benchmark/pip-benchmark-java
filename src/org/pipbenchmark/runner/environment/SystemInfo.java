package org.pipbenchmark.runner.environment;

import java.util.*;
import java.net.*;

public class SystemInfo extends LinkedHashMap<String, String> {
	private static final long serialVersionUID = -5500217368172807150L;

	public SystemInfo() {
        fillSystemInformation();
    }

    private void fillSystemInformation() {
    	try {
    		addSystemInfo("Machine Name", InetAddress.getLocalHost().getHostName());
    	} catch (UnknownHostException ex) {
    		addSystemInfo("Machine Name", "Unknown");
    	}
    	
        addSystemInfo("User Name", System.getProperty("user.name"));
        addSystemInfo("Operating System Name", System.getProperty("os.name"));
        addSystemInfo("Operating System Version", System.getProperty("os.version"));
        addSystemInfo("Operating System Architecture", System.getProperty("os.arch"));
        addSystemInfo("Java VM Name", System.getProperty("java.vm.name"));
        addSystemInfo("Java VM Version", System.getProperty("java.vm.version"));
        addSystemInfo("Java VM Vendor", System.getProperty("java.vm.vendor"));
    }

    private void addSystemInfo(String parameter, String value) {
        put(parameter, value);
    }
}
