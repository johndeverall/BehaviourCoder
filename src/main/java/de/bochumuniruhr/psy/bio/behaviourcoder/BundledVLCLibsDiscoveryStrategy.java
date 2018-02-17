package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.extern.slf4j.Slf4j;
import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;

@Slf4j
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
	private static final Pattern[] MAC_FILENAME_PATTERNS = new Pattern[] { Pattern.compile(".*libvlc\\.dylib"),
			Pattern.compile(".*libvlccore\\.dylib") };

	@Override
	protected Pattern[] getFilenamePatterns() {

		String osArch = System.getProperty("os.arch");
		String osName = System.getProperty("os.name").toLowerCase();

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
		// String basePath = currentWorkingDirectory + "/src/main/resources/lib/";
		String basePath = currentWorkingDirectory + "/lib/";

		if (osName.startsWith("win")) {
			if (osArch.equalsIgnoreCase("x86")) {
				basePath = basePath + "win32";
			} else if (osArch.equalsIgnoreCase("amd64")) {
				basePath = basePath + "win64";
			} else {
				throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
			}
		} else if (osName.startsWith("linux")) {
			if (osArch.equalsIgnoreCase("amd64")) {
				basePath = basePath + "linux64";
			} else if (osArch.equalsIgnoreCase("ia64")) {
				basePath = basePath + "linux64";
			} else if (osArch.equalsIgnoreCase("i386")) {
				basePath = basePath + "linux32";
			} else {
				throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
			}
		} else if (osName.startsWith("mac")) {
			basePath = basePath + "mac";
		} else {
			throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
		}
		directoryNames.add(basePath);
		try {
			extractFile("/vlclib.zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void extractFile(String path) throws IOException {

		// extract file from jar
		InputStream in = this.getClass().getResourceAsStream(path);
		File zipFromJar = new File(System.getProperty("user.dir") + File.separator + "vlclib.zip");
		if (zipFromJar.exists()) {
			zipFromJar.delete();
		}
		Files.copy(in, zipFromJar.toPath());
		
		// unzip contents of zip file

		ZipFile zipFile = new ZipFile(zipFromJar);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		
		final int BUFFER = 2048;
		
		while (entries.hasMoreElements()) { 
			
			ZipEntry entry = entries.nextElement();
			File destFile = new File(System.getProperty("user.dir") + File.separator + "lib" + File.separator + entry.getName());
			destFile.getParentFile().mkdirs();
			
			if (entry.isDirectory()) { 
				// do nothing
			} else { 
				BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
				int currentByte;
				
				byte data[] = new byte[BUFFER];
			
				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
				
				// read and write until the last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                dest.write(data, 0, currentByte);
	            }
	            dest.flush();
	            dest.close();
	            is.close();
			}
		}
		
		zipFile.close();
		
		zipFromJar.delete();
		
	}
}
