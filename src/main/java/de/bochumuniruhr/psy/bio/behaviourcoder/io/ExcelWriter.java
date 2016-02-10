package de.bochumuniruhr.psy.bio.behaviourcoder.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails.Constraint;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import jxl.write.DateTime;

/**
 * Writer for saving trials as spreadsheets using the XLS format.
 */
public class ExcelWriter {

	/**
	 * The file to be written to.
	 */
	private File file;
	
	/**
	 * Creates an excel writer.
	 * 
	 * @param file - the file to write to. Must either not exist or be in the XLS format.
	 */
	public ExcelWriter(File file) {
		this.file = file;
	}

	/**
	 * Writes a trial to the file.
	 * 
	 * @param trial - the trial to be saved.
	 * @throws FileNotFoundException
	 */
	public void write(Trial trial) throws FileNotFoundException {
		//The workbook to be modified
		WritableWorkbook workbook = null;
		try {
			//If the file exists
			if (file.exists()) {
				//Load it and use it to create a modify version
				Workbook workbookIn = Workbook.getWorkbook(file);
				workbook = Workbook.createWorkbook(file, workbookIn);

				//Get the results sheet
				WritableSheet sheet = workbook.getSheet("Results");

				//Find the first empty row in the spreadsheet
				int nextEmptyRow = 1;
				while (true) {
					if (sheet.getCell(0, nextEmptyRow).getContents().equals("")) {
						break;
					}
					nextEmptyRow++;
				}

				//Write the trial in the empty row that was found
				writeRow(trial, nextEmptyRow, workbook, sheet);
			} else {
				throw new FileNotFoundException("");
			}

		} catch (FileNotFoundException e) { 
			//User should be informed of the file not being found
			//NOTE: May never be thrown
			throw e;
		} catch (IOException | BiffException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the trial into the spreadsheet as a row.
	 * 
	 * @param trial - the trial to write into the spreadsheet
	 * @param nextEmptyRow - the next empty row in the sheet
	 * @param workbook - the workbook being written to
	 * @param sheet - the sheet being written to
	 */
	private void writeRow(Trial trial, int nextEmptyRow,
			WritableWorkbook workbook, WritableSheet sheet) {
		try {
			TrialDetails details = trial.getDetails();
			
			//Trial session date
			DateTime date = new DateTime(0, nextEmptyRow, details.getDate());
			sheet.addCell(date);
			
			//Trial session details
			int column = 1;
			for (String detail : details.getDetailNames()){
				Label label = new Label(column, nextEmptyRow, details.getDetail(detail));
				sheet.addCell(label);
				++column;
			}
			//Number of occurrences of each behaviour in each location
			for (InstantBehaviour instant : trial.getInstantBehaviours()){
				if (instant.isAssociatedWithLocation()) {
					for (Location location : trial.getLocations()){
						Number num = new Number(column, nextEmptyRow, instant.getNumberOfOccurrences(location));
						sheet.addCell(num);
						++column;
					}
				} else {
					Number num = new Number(column, nextEmptyRow, instant.getNumberOfOccurrences());
					sheet.addCell(num);
					++column;
				}
			}
			//Time that each behaviour occurred in each location
			for (TimedBehaviour timed : trial.getTimedBehaviours()){
				if (timed.isAssociatedWithLocation()){
					for (Location location : trial.getLocations()){
						Number num = new Number(column, nextEmptyRow, timed.getDuration(location));
						sheet.addCell(num);
						++column;
					}
				} else {
					Number num = new Number(column, nextEmptyRow, timed.getTotalDuration());
					sheet.addCell(num);
					++column;
				}
			}
			//Time for each location
			for (Location location : trial.getLocations()){
				//Dividing by 1000 to get seconds
				Number num = new Number(column, nextEmptyRow, trial.getLocationTime(location) / 1000.0);
				sheet.addCell(num);
				++column;
			}
			//Number of transitions
			Number trans = new Number(column, nextEmptyRow, trial.getNumberOfLocationChanges());
			sheet.addCell(trans);
			++column;
			
			//Time before the video shows active trial
			Number offset = new Number(column, nextEmptyRow,trial.getDetails().getVideoTimeOffset());
			sheet.addCell(offset);
			++column;
			
			//Cleanup
			workbook.write();
			workbook.close();
		} catch (IOException | WriteException e) {
			e.printStackTrace();
		} 
	}
	
	public Config load() {
		try {
			Workbook workbook = Workbook.getWorkbook(file);
			
			Sheet config = workbook.getSheet("Config");
			
			//Locations
			List<Location> locations = new ArrayList<>();
			
			for (int col = 1; col < config.getColumns() && !"".equals(config.getCell(col, 0).getContents()); ++col) {
				locations.add(new Location(config.getCell(col, 0).getContents()));
			}
			
			//Details
			List<String> detailNames = new ArrayList<>();
			List<Constraint> constraints = new ArrayList<>();
			
			for (int row = 11; row < config.getRows() && !"".equals(config.getCell(0, row).getContents()); ++row) {
				detailNames.add(config.getCell(0, row).getContents());
				
				List<String> whitelist = new ArrayList<>();
				for (int col = 1; col < config.getColumns() && !"".equals(config.getCell(col, row).getContents()); ++col) {
					whitelist.add(config.getCell(col, row).getContents());
				}
				constraints.add(new Constraint(whitelist.toArray(new String[whitelist.size()])));
			}
			
			//Create the trial
			Trial trial = new Trial(120, locations, detailNames, constraints);
		
			//Timed
			List<Character> timKeys = new ArrayList<>();
			for (int col = 1; col < config.getColumns() && !"".equals(config.getCell(col, 1).getContents()); ++col) {
				Color color = null;
				if (!"".equals(config.getCell(col, 2).getContents())) {
					color = new Color(Integer.parseInt(config.getCell(col, 2).getContents()));
				}
				
				trial.addTimedBehaviour(new TimedBehaviour(config.getCell(col, 1).getContents(), 
						color, trial, Boolean.parseBoolean(config.getCell(col, 3).getContents())));
				
				timKeys.add(config.getCell(col, 4).getContents().charAt(0));
			}
			
			//Instant
			List<Character> incKeys = new ArrayList<>();
			List<Character> decKeys = new ArrayList<>();
			for (int col = 1; col < config.getColumns() && !"".equals(config.getCell(col, 5).getContents()); ++col) {
				Color color = null;
				if (!"".equals(config.getCell(col, 6).getContents())) {
					color = new Color(Integer.parseInt(config.getCell(col, 6).getContents()));
				}
				
				trial.addInstantBehaviour(new InstantBehaviour(config.getCell(col, 5).getContents(), 
						color, trial, Boolean.parseBoolean(config.getCell(col, 7).getContents())));
				
				incKeys.add(config.getCell(col, 8).getContents().charAt(0));
				decKeys.add(config.getCell(col, 9).getContents().charAt(0));
			}
			
			return new Config(trial, timKeys, incKeys, decKeys);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setup(Trial trial, List<Character> insIncKeys, List<Character> insDecKeys, List<Character> timKeys) {
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file);

			WritableSheet sheet = workbook.createSheet("Config", 0);
			
			//Set the background colour for the header cells
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setBackground(Colour.DARK_GREEN);
	
			//Set the colour and font of the text for the header cells
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			font.setColour(Colour.GOLD);
			cellFormat.setFont(font);
			
			//Locations
			Label locationLab = new Label(0, 0, "Locations");
			locationLab.setCellFormat(cellFormat);
			sheet.addCell(locationLab);
			
			int col = 1;
			for (Location loc : trial.getLocations()) {
				Label locs = new Label(col, 0, loc.getName());
				sheet.addCell(locs);
				++col;
			}
			
			//Timed
			Label timedLab = new Label(0, 1, "Timed");
			timedLab.setCellFormat(cellFormat);
			sheet.addCell(timedLab);
			
			col = 1;
			for (int i = 0; i < trial.getTimedBehaviours().size(); ++i) {
				TimedBehaviour behaviour = trial.getTimedBehaviours().get(i);
				Label name = new Label(col, 1, behaviour.getName());
				sheet.addCell(name);
				Label color = new Label(col, 2, (behaviour.getColor() != null) ? "" + behaviour.getColor().getRGB() : "");
				sheet.addCell(color);
				Label associate = new Label(col, 3, Boolean.toString(behaviour.isAssociatedWithLocation()));
				sheet.addCell(associate);
				Label key = new Label(col, 4, timKeys.get(i).toString());
				sheet.addCell(key);	
				++col;
			}
			
			//Instant
			Label countLab = new Label(0, 5, "Instant");
			countLab.setCellFormat(cellFormat);
			sheet.addCell(countLab);
			
			col = 1;
			for (int i = 0; i < trial.getInstantBehaviours().size(); ++i) {
				InstantBehaviour behaviour = trial.getInstantBehaviours().get(i);
				Label name = new Label(col, 5, behaviour.getName());
				sheet.addCell(name);
				Label color = new Label(col, 6, (behaviour.getColor() != null) ? "" + behaviour.getColor().getRGB() : "");
				sheet.addCell(color);
				Label associate = new Label(col, 7, Boolean.toString(behaviour.isAssociatedWithLocation()));
				sheet.addCell(associate);
				Label inc = new Label(col, 8, insIncKeys.get(i).toString());
				sheet.addCell(inc);	
				Label dec = new Label(col, 9, insDecKeys.get(i).toString());
				sheet.addCell(dec);	
				++col;
			}
			
			//Details
			Label detailLab = new Label(0, 10, "Details");
			detailLab.setCellFormat(cellFormat);
			sheet.addCell(detailLab);
			
			int row = 11;
			for (String detail : trial.getDetails().getDetailNames()) {
				Label name = new Label(0, row, detail);
				sheet.addCell(name);
				col = 1;
				for (String allowed : trial.getDetails().getConstraint(detail).getWhitelist()) {
					Label whitelist = new Label(col, row, allowed);
					sheet.addCell(whitelist);
					++col;
				}
				++row;
			}

			//Create the sheet for the results
			sheet = workbook.createSheet("Results", 1);
	
			//Create the list names for the header
			ArrayList<String> header = new ArrayList<String>();
			
			//Add the detail names to the header
			header.add("Date");
			for (String detail : trial.getDetails().getDetailNames()){
				header.add(detail.replaceAll("\\s+", "_"));
			}
			//Add the instant behaviours with each location to the header
			for (InstantBehaviour instant : trial.getInstantBehaviours()){
				if (instant.isAssociatedWithLocation()){
					for (Location location : trial.getLocations()){
						header.add((instant.getName() + "_" + location.getName()).replaceAll("\\s+", "_"));
					}
				} else {
					header.add(instant.getName().replaceAll("\\s+", "_"));
				}
			}
			//Add the timed behaviours with each location to the header
			for (TimedBehaviour timed : trial.getTimedBehaviours()){
				if (timed.isAssociatedWithLocation()){
					for (Location location : trial.getLocations()){
						header.add((timed.getName() + "_" + location.getName()).replaceAll("\\s+", "_"));
					}
				} else {
					header.add(timed.getName().replaceAll("\\s+", "_"));
				}
			}
			//Add the locations to the header
			for (Location location : trial.getLocations()){
				header.add(location.getName().replaceAll("\\s+", "_"));
			}
			//Add some additional information to the header
			header.add("Location_Changes");
			header.add("Trial_Section_Start");
	
			//Create the cells for each name in the header
			int column = 0;
			int headerRow = 0;
			for (String columnTitle : header) {
				//Create the label
				Label label = new Label(column, headerRow, columnTitle);
				label.setCellFormat(cellFormat);
				sheet.addCell(label);
	
				//Move along the header
				column++;
			}
	
			//Cleanup
			workbook.write();
			workbook.close();
		} catch (IOException | WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class Config {
		public final Trial trial;
		public final List<Character> timedKeys;
		public final List<Character> incInstantKeys;
		public final List<Character> decInstantKeys;
		
		private Config(Trial trial,  List<Character> timedKeys, List<Character> incInstantKeys, List<Character> decInstantKeys) {
			this.trial = trial;
			this.timedKeys = timedKeys;
			this.incInstantKeys = incInstantKeys;
			this.decInstantKeys = decInstantKeys;
		}
	}
}
