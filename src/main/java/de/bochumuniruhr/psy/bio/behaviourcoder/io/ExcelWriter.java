package de.bochumuniruhr.psy.bio.behaviourcoder.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;
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
				//Otherwise create a new workbook
				workbook = Workbook.createWorkbook(file);

				//Create the sheet for the results
				WritableSheet sheet = workbook.createSheet("Results", 0);

				//Create the list names for the header
				ArrayList<String> header = new ArrayList<String>();
				
				//Add the detail names to the header
				header.add("Date");
				for (String detail : trial.getDetails().getDetailNames()){
					header.add(detail.replaceAll("\\s+", "_"));
				}
				//Add the instant behaviours with each area to the header
				for (InstantBehaviour instant : trial.getInstantBehaviours()){
					for (Location area : trial.getLocations()){
						header.add((instant.getName() + "_" + area.getName()).replaceAll("\\s+", "_"));
					}
				}
				//Add the timed behaviours with each area to the header
				for (TimedBehaviour timed : trial.getTimedBehaviours()){
					for (Location area : trial.getLocations()){
						header.add((timed.getName() + "_" + area.getName()).replaceAll("\\s+", "_"));
					}
				}
				//Add the areas to the header
				for (Location area : trial.getLocations()){
					header.add(area.getName().replaceAll("\\s+", "_"));
				}
				//Add some additional information to the header
				header.add("Location_Changes");
				header.add("Trial_Section_Start");
				
				//Set the background colour for the header cells
				WritableCellFormat cellFormat = new WritableCellFormat();
				cellFormat.setBackground(Colour.DARK_GREEN);

				//Set the colour and font of the text for the header cells
				WritableFont font = new WritableFont(WritableFont.ARIAL);
				font.setColour(Colour.GOLD);
				cellFormat.setFont(font);

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

				//Now write the row
				writeRow(trial, 1, workbook, sheet);
			}

		} catch (FileNotFoundException e) { 
			//User should be informed of the file not being found
			//NOTE: May never be thrown
			throw e;
		} catch (IOException | WriteException | BiffException e) {
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
			//Number of occurrences of each behaviour in each area
			for (InstantBehaviour instant : trial.getInstantBehaviours()){
				for (Location area : trial.getLocations()){
					Number num = new Number(column, nextEmptyRow, instant.getNumberOfOccurrences(area));
					sheet.addCell(num);
					++column;
				}
			}
			//Time that each behaviour occurred in each area
			for (TimedBehaviour timed : trial.getTimedBehaviours()){
				for (Location area : trial.getLocations()){
					Number num = new Number(column, nextEmptyRow, timed.getDuration(area));
					sheet.addCell(num);
					++column;
				}
			}
			//Time for each area
			for (Location area : trial.getLocations()){
				//Dividing by 1000 to get seconds
				Number num = new Number(column, nextEmptyRow, trial.getLocationTime(area) / 1000.0);
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
}
