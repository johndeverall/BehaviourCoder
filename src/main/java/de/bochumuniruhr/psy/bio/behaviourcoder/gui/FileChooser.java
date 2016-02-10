package de.bochumuniruhr.psy.bio.behaviourcoder.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {
	
	private FileFilter spreadsheetFilter;
	private FileFilter videoFilter;
	
	private JFrame frame;
	
	public FileChooser(JFrame frame){
		this.frame = frame;
		
		spreadsheetFilter = new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Spreadsheet (xls)";
			}
			
			@Override
			public boolean accept(File f) {
				String extension = getExtension(f);
				return f.isDirectory() || "xls".equals(extension);
			}
		};
		
		videoFilter = new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Supported Video";
			}
			
			@Override
			public boolean accept(File f) {
				String extension = getExtension(f);
				return f.isDirectory() 
						|| "m2v".equals(extension)
						|| "m4v".equals(extension)
						|| "mpeg1".equals(extension)
						|| "mpeg2".equals(extension)
						|| "mts".equals(extension)
						|| "divx".equals(extension)
						|| "dv".equals(extension)
						|| "flv".equals(extension)
						|| "m1v".equals(extension)
						|| "mkv".equals(extension)
						|| "mov".equals(extension)
						|| "mpeg4".equals(extension)
						|| "ts".equals(extension)
						|| "avi".equals(extension)
						|| "mpeg".equals(extension)
						|| "mpg".equals(extension)
						|| "wmv".equals(extension)
						|| "mp4".equals(extension)
						|| "ogg".equals(extension)
						|| "ogm".equals(extension);
			}
		};
		
		setAcceptAllFileFilterUsed(false);
	}
	
	/**
	 * Chooses a file to be saved.
	 */
	public File chooseSpreadsheet() {
		removeChoosableFileFilter(videoFilter);
		addChoosableFileFilter(spreadsheetFilter);
		int returnVal = showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return getSelectedFile();
		} else {
			return null;
		}
	}

	/**
	 * Chooses a video to be loaded.
	 */
	public File chooseVideo() {
		removeChoosableFileFilter(spreadsheetFilter);
		addChoosableFileFilter(videoFilter);
		int returnVal = showDialog(frame, "Load");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return getSelectedFile();
		} else {
			return null;
		}
	}
	
	/**
     * Gets the lower case extension of a file.
     */  
    private String getExtension(File f) {
        String[] split = f.getName().split("\\.");
        
        if (split.length >= 2){
        	return split[split.length - 1].toLowerCase();
        }
        return null;

    }
}
