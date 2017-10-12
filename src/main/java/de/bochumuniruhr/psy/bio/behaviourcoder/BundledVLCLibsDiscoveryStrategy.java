package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

		String libVlcCore = System.mapLibraryName("libvlccore");
		String libVlc = System.mapLibraryName("libvlc");

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
				basePath = basePath + "win64";
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
			walk("/lib/win64");
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void walk(String startFolder) throws URISyntaxException, IOException {
		URI uri = BundledVLCLibsDiscoveryStrategy.class.getResource(startFolder).toURI();
		Path myPath = null;
		if (uri.getScheme().equals("jar")) {
			FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
			myPath = fileSystem.getPath(startFolder);
		} else {
			myPath = Paths.get(uri);
		}
		Stream<Path> walk = Files.walk(myPath, 1);
		for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
			Path p = it.next();
			boolean isStartFolder = p.toFile().getPath().replace("\\", "/").endsWith(startFolder);
			if (p.toFile().isDirectory() && !isStartFolder) {
				walk(startFolder + "/" + p.getFileName());
			} else if (isStartFolder) {
				// don't print folders
			} else {
				String fileName = startFolder + "/" + p.getFileName().toString();
				log.info(fileName);
				// NativeUtils.createTempLibraryFromJar(fileName);
				createLocalFile(fileName, System.getProperty("user.dir") + fileName);
			}
		}
		walk.close();
	}

	public void createLocalFile(String inputFile, String outputFile) throws FileNotFoundException, IOException {
		// InputStream ddlStream =
		// BundledVLCLibsDiscoveryStrategy.class.getResourceAsStream(inputFile);

		File file;
		try {

			File outputFileHandle = new File(outputFile);
			outputFileHandle.getParentFile().mkdirs();
			outputFileHandle.createNewFile();

			URL resource = BundledVLCLibsDiscoveryStrategy.class.getResource(inputFile);

			file = new File(resource.toURI());

			FileInputStream fis = new FileInputStream(file);
			FileChannel fci = fis.getChannel();
			FileOutputStream fos = new FileOutputStream(outputFile);

			FileChannel fco = fos.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(1024);

			while (true) {
				int read = fci.read(buffer);

				if (read == -1)
					break;
				buffer.flip();
				fco.write(buffer);
				buffer.clear();
			}

			fis.close();
			fos.close();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * try { URL resource =
		 * BundledVLCLibsDiscoveryStrategy.class.getResource(inputFile); File file = new
		 * File(resource.toURI()); FileInputStream fileInputStream = new
		 * FileInputStream(file); FileChannel srcChannel = fileInputStream.getChannel();
		 * 
		 * File outputFileHandle = new File(outputFile);
		 * outputFileHandle.getParentFile().mkdirs(); outputFileHandle.createNewFile();
		 * FileOutputStream fileOutputStream = new FileOutputStream(outputFileHandle);
		 * FileChannel dstChannel = fileOutputStream.getChannel();
		 * 
		 * dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		 * 
		 * srcChannel.close(); fileInputStream.close(); dstChannel.close();
		 * fileOutputStream.close(); } catch (IOException e) { } catch
		 * (URISyntaxException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		/*
		 * File file = new File(outputFile); file.getParentFile().mkdirs();
		 * file.createNewFile(); try (FileOutputStream fos = new
		 * FileOutputStream(outputFile);) { byte[] buf = new byte[2048]; int r; while
		 * (-1 != (r = ddlStream.read(buf))) { fos.write(buf, 0, r); } }
		 */
	}

}
