package de.bochumuniruhr.psy.bio.timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
				header.add("Task");
				header.add("Subject");
				header.add("Cohort");
				header.add("Zone One");
				header.add("Zone Two");
				header.add("Zone Three");
				header.add("Zone Four");
				header.add("Zone Five");
				header.add("Zone Six");
				header.add("Zone Seven");
				header.add("Zone Eight");
				header.add("Zone Nine");
				header.add("Total time");
				header.add("Social Calls");
				header.add("Alarm Calls");
				header.add("Head Bobs");
				header.add("Movements");
				header.add("Zone Movements");
				header.add("Combined movements");
				
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeRow(Trial trial, int nextEmptyRow,
			WritableWorkbook workbook, WritableSheet sheet) {

		try {

			// column, row
			
			DateTime date = new DateTime(0, nextEmptyRow, trial.getDate());
			sheet.addCell(date);
			
			Label taskName = new Label(1, nextEmptyRow, "" + trial.getTaskName());
			sheet.addCell(taskName); 
			
			Label subjectNumber = new Label(2, nextEmptyRow, "" + trial.getSubjectNumber());
			sheet.addCell(subjectNumber);
			
			Label cohort = new Label(3, nextEmptyRow, "" + trial.getCohort());
			sheet.addCell(cohort);
			
			Number zone1 = new Number(4, nextEmptyRow, trial.getZone(1));
			sheet.addCell(zone1);

			Number zone2 = new Number(5, nextEmptyRow, trial.getZone(2));
			sheet.addCell(zone2);

			Number zone3 = new Number(6, nextEmptyRow, trial.getZone(3));
			sheet.addCell(zone3);

			Number zone4 = new Number(7, nextEmptyRow, trial.getZone(4));
			sheet.addCell(zone4);

			Number zone5 = new Number(8, nextEmptyRow, trial.getZone(5));
			sheet.addCell(zone5);

			Number zone6 = new Number(9, nextEmptyRow, trial.getZone(6));
			sheet.addCell(zone6);

			Number zone7 = new Number(10, nextEmptyRow, trial.getZone(7));
			sheet.addCell(zone7);

			Number zone8 = new Number(11, nextEmptyRow, trial.getZone(8));
			sheet.addCell(zone8);

			Number zone9 = new Number(12, nextEmptyRow, trial.getZone(9));
			sheet.addCell(zone9);
			
			Number totalTime = new Number(13, nextEmptyRow, trial.getTotalTime());
			sheet.addCell(totalTime);

			Number socialCalls = new Number(14, nextEmptyRow, trial.getSocialCalls());
			sheet.addCell(socialCalls);

			Number alarmCalls = new Number(15, nextEmptyRow, trial.getAlarmCalls());
			sheet.addCell(alarmCalls);

			Number headBobs = new Number(16, nextEmptyRow, trial.getHeadBobs());
			sheet.addCell(headBobs);

			Number movements = new Number(17, nextEmptyRow, trial.getMovements());
			sheet.addCell(movements);
			
			Number zoneMovements = new Number(18, nextEmptyRow, trial.getZoneMovements());
			sheet.addCell(zoneMovements);
			
			Number combinedMovements = new Number(19, nextEmptyRow, (trial.getMovements() + trial.getZoneMovements()));
			sheet.addCell(combinedMovements);
			
			workbook.write();
			workbook.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
