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
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import jxl.write.DateTime;

public class ExcelWriter {

	private File file;
	
	public ExcelWriter(File file) {
		this.file = file;
	}

	public void write(Trial trial) throws FileNotFoundException {

		WritableWorkbook workbook = null;
		try {
			Workbook workbookIn = null;
			if (file.exists()) {
				workbookIn = Workbook.getWorkbook(file);
				workbook = Workbook.createWorkbook(file, workbookIn);

				WritableSheet sheet = workbook.getSheet("Page1");

				// find the first empty row in the spreadsheet
				int nextEmptyRow = 1;
				while (true) {
					if (sheet.getCell(0, nextEmptyRow).getContents().equals("")) {
						break;
					}
					nextEmptyRow++;
				}

				writeRow(trial, nextEmptyRow, workbook, sheet);

			} else {
				workbook = Workbook.createWorkbook(file);

				WritableSheet sheet = workbook.createSheet("Page1", 0);

				// Set Header
				ArrayList<String> header = new ArrayList<String>();
				
				header.add("Date");
				
				for (String detail : trial.getDetails().getDetailNames()){
					header.add(detail.replaceAll("\\s+", "_"));
				}
				
				for (InstantBehaviour instant : trial.getInstantBehaviours()){
					for (Location area : trial.getAreas()){
						header.add((instant.getName() + "_" + area.getName()).replaceAll("\\s+", "_"));
					}
				}
				
				for (TimedBehaviour timed : trial.getTimedBehaviours()){
					for (Location area : trial.getAreas()){
						header.add((timed.getName() + "_" + area.getName()).replaceAll("\\s+", "_"));
					}
				}
				
				for (Location area : trial.getAreas()){
					header.add(area.getName().replaceAll("\\s+", "_"));
				}
				
				header.add("Location_Changes");
				header.add("Trial_Section_Start");
				
				// Setting Background colour for Cells
				
				Colour bckcolor = Colour.DARK_GREEN;
				WritableCellFormat cellFormat = new WritableCellFormat();

				cellFormat.setBackground(bckcolor);

				// Setting Colour & Font for the Text

				WritableFont font = new WritableFont(WritableFont.ARIAL);

				font.setColour(Colour.GOLD);

				cellFormat.setFont(font);

				// Write the Header to the excel file
				int column = 0;
				for (String columnTitle : header) {
					int headerRow = 0;
					Label label = new Label(column, headerRow, columnTitle);

					sheet.addCell(label);

					WritableCell cell = sheet
							.getWritableCell(column, headerRow);
					cell.setCellFormat(cellFormat);

					column++;
				}

				writeRow(trial, 1, workbook, sheet);
			}

		} catch (FileNotFoundException e) { 
			throw e; // user can probably do something about this
		} catch (IOException | WriteException | BiffException e) {
			e.printStackTrace();
		}
	}

	public void writeRow(Trial trial, int nextEmptyRow,
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
				for (Location area : trial.getAreas()){
					Number num = new Number(column, nextEmptyRow, instant.getNumberOfOccurrences(area));
					sheet.addCell(num);
					++column;
				}
			}

			//Time that each behaviour occurred in each area
			for (TimedBehaviour timed : trial.getTimedBehaviours()){
				for (Location area : trial.getAreas()){
					Number num = new Number(column, nextEmptyRow, timed.getDuration(area));
					sheet.addCell(num);
					++column;
				}
			}
			
			//Time for each area
			for (Location area : trial.getAreas()){
				//Dividing by 1000 to get seconds
				Number num = new Number(column, nextEmptyRow, trial.getAreaTime(area) / 1000.0);
				sheet.addCell(num);
				++column;
			}
			
			//Number of transitions
			Number trans = new Number(column, nextEmptyRow, trial.getNumberOfAreaChanges());
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
