package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class CustomNativeDiscoveryStrategy extends StandardNativeDiscoveryStrategy {

	private Pattern[] filenamePatterns;
	
	/**
     * Filename patterns to search for.
     */
    private static final Pattern[] WINDOWS_FILENAME_PATTERNS = new Pattern[] {
        Pattern.compile("libvlc\\.dll"),
        Pattern.compile("libvlccore\\.dll")
    };
    
    /**
     * Filename patterns to search for.
     */
    private static final Pattern[] MAC_FILENAME_PATTERNS = new Pattern[] {
        Pattern.compile("libvlc\\.dylib"),
        Pattern.compile("libvlccore\\.dylib")
    };
    
    /**
     * Linux Filename patterns to search for.
     * <p>
     * The intent is to match one of (for example):
     * <ul>
     *   <li>libvlc.so</li>
     *   <li>libvlc.so.5</li>
     *   <li>libvlc.so.5.3.0</li>
     * </ul>
     */
    private static final Pattern[] LINUX_FILENAME_PATTERNS = new Pattern[] {
        Pattern.compile("libvlc\\.so(?:\\.\\d)*"),
        Pattern.compile("libvlccore\\.so(?:\\.\\d)*")
    };
	
	@Override
	protected Pattern[] getFilenamePatterns() {
		
		if (RuntimeUtil.isNix()) { 
			filenamePatterns = LINUX_FILENAME_PATTERNS;
		} else if (RuntimeUtil.isMac()) { 
			filenamePatterns = MAC_FILENAME_PATTERNS;
		} else if (RuntimeUtil.isWindows()) { 
			filenamePatterns = WINDOWS_FILENAME_PATTERNS;
		} 
		
		return filenamePatterns;
	}
	
	@Override 
	public final boolean supported() { 
		return true;
	}
	
	@Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
        // Try and find the location of the vlc installation directory from the registry
//        String installDir = WindowsRuntimeUtil.getVlcInstallDir();
//        if(installDir != null) {
//            directoryNames.add(0, installDir);
//        }
		directoryNames.add(Paths.get(".").toAbsolutePath().normalize().toString());
    }
	
}
