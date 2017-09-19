package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.util.List;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;

public class BundledVLCLibsDiscoveryStrategy extends StandardNativeDiscoveryStrategy {

	/**
	 * Windows filename patterns to search for.
	 */
	private static final Pattern[] WINDOWS_FILENAME_PATTERNS = new Pattern[] { Pattern.compile("libvlc\\.dll"),
			Pattern.compile("libvlccore\\.dll"), };

	/**
	 * Linux filename patterns to search for.
	 */
	private static final Pattern[] LINUX_FILENAME_PATTERNS = new Pattern[] { Pattern.compile("libvlc\\.so"),
			Pattern.compile("libvlccore\\.so") };

	/**
	 * Mac filename patterns to search for.
	 */
	private static final Pattern[] MAC_FILENAME_PATTERNS = new Pattern[] { Pattern.compile("libvlc\\.dylib"),
			Pattern.compile("libvlccore\\.dylib") };

	@Override
	protected Pattern[] getFilenamePatterns() {

		String osArch = System.getProperty("os.arch");
		String osName = System.getProperty("os.name").toLowerCase();
		String osTempDirectory = System.getProperty("java.io.tmpdir");
		String name;
		String path;

		if (osName.startsWith("win")) {
			return WINDOWS_FILENAME_PATTERNS;
		} else if (osName.startsWith("linux")) {
			return LINUX_FILENAME_PATTERNS;
		} else if (osName.startsWith("mac")) { 
			return MAC_FILENAME_PATTERNS;
		} else { 
			throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
		}

	}

	@Override
	public final boolean supported() {
		return true;
	}

	/**
	 * Figure out the directory name where vlc is bundled.
	 */
	@Override
	protected void onGetDirectoryNames(List<String> directoryNames) {

		
		String osArch = System.getProperty("os.arch");
		String osName = System.getProperty("os.name").toLowerCase();
		String currentWorkingDirectory = System.getProperty("user.dir");
		//C:\code\BehaviourCoder\src\main\resources\lib
		String basePath = currentWorkingDirectory + "/src/main/resources/lib/";
		
		if (osName.startsWith("win")) {
			if (osArch.equalsIgnoreCase("x86")) {
				directoryNames.add(basePath + "win64");
			} else if (osArch.equalsIgnoreCase("amd64")) {
				directoryNames.add(basePath + "win64");
			} else { 
				throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
			}
		} else if (osName.startsWith("linux")) {
			if (osArch.equalsIgnoreCase("amd64")) {
				directoryNames.add(basePath + "linux64");
			} else if (osArch.equalsIgnoreCase("ia64")) {
				directoryNames.add(basePath + "linux64");
			} else if (osArch.equalsIgnoreCase("i386")) {
				directoryNames.add(basePath + "linux32");
			} else {
				throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
			}
		} else if (osName.startsWith("mac")) { 
			directoryNames.add(basePath + "mac");
		}
		else {
			throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
		}
	}

}
