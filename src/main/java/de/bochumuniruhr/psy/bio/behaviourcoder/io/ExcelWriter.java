package de.bochumuniruhr.psy.bio.behaviourcoder.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialSection;
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

	public void write(TrialSection trial) throws FileNotFoundException {

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
				header.add("Rooster_ID");
				header.add("Trial_Type");
				header.add("Session_Num");
				header.add("Trial_Num");
				header.add("Section_Num");
				header.add("Mirror");
				header.add("Pecks_Close");
				header.add("Pecks_Far");
				header.add("Attacks_Close");
				header.add("Attacks_Far");
				header.add("Hackles_Close");
				header.add("Hackles_Far");
				header.add("Crouch_Close");
				header.add("Crouch_Far");
				header.add("Following_Close");
				header.add("Following_Far");
				header.add("Facing_Away_Close");
				header.add("Facing_Away_Far");
				header.add("Groom_Mark_Close");
				header.add("Groom_Mark_Far");
				header.add("Groom_Other_Close");
				header.add("Groom_Other_Far");
				header.add("Close");
				header.add("Far");
				header.add("Location_Change");
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

	public void writeRow(TrialSection trial, int nextEmptyRow,
			WritableWorkbook workbook, WritableSheet sheet) {

		try {

			// column, row

//			header.add("Date");
//			header.add("Rooster_ID");
//			header.add("Trial_Type");
//			header.add("Session_Num");
//			header.add("Trial_Num");
//			header.add("Section_Num");
//			header.add("Mirror");
//			header.add("Pecks_Close");
//			header.add("Pecks_Far");
//			header.add("Attacks_Close");
//			header.add("Attacks_Far");
//			header.add("Hackles_Close");
//			header.add("Hackles_Far");
//			header.add("Crouch_Close");
//			header.add("Crouch_Far");
//			header.add("Following_Close");
//			header.add("Following_Far");
//			header.add("Facing_Away_Close");
//			header.add("Facing_Away_Far");
//			header.add("Groom_Mark_Close");
//			header.add("Groom_Mark_Far");
//			header.add("Groom_Other_Close");
//			header.add("Groom_Other_Far");
//			header.add("Close");
//			header.add("Far");
//			header.add("Location_Change");

			DateTime date = new DateTime(0, nextEmptyRow, trial.getDate());
			sheet.addCell(date);
			
			Label roosterId = new Label(1, nextEmptyRow, "" + trial.getRoosterId());
			sheet.addCell(roosterId); 
			
			Label trialType = new Label(2, nextEmptyRow, "" + trial.getTrialType());
			sheet.addCell(trialType);
			
			Label sessionNumber = new Label(3, nextEmptyRow, "" + trial.getSessionNumber());
			sheet.addCell(sessionNumber);
			
			Label trialNumber = new Label(4, nextEmptyRow, trial.getTrialNumber());
			sheet.addCell(trialNumber);

			Label sectionNumber = new Label(5, nextEmptyRow, trial.getSectionNumber());
			sheet.addCell(sectionNumber);

			Label mirror = new Label(6, nextEmptyRow, trial.getMirror());
			sheet.addCell(mirror);

			Number pecksClose = new Number(7, nextEmptyRow, trial.getPecksClose());
			sheet.addCell(pecksClose);

			Number pecksFar = new Number(8, nextEmptyRow, trial.getPecksFar());
			sheet.addCell(pecksFar);

			Number attacksClose = new Number(9, nextEmptyRow, trial.getAttacksClose());
			sheet.addCell(attacksClose);

			Number attacksFar = new Number(10, nextEmptyRow, trial.getAttacksFar());
			sheet.addCell(attacksFar);

			Number hacklesClose = new Number(11, nextEmptyRow, trial.getHacklesClose());
			sheet.addCell(hacklesClose);

			Number hacklesFar = new Number(12, nextEmptyRow, trial.getHacklesFar());
			sheet.addCell(hacklesFar);
			
			Number crouchClose = new Number(13, nextEmptyRow, trial.getCrouchClose());
			sheet.addCell(crouchClose);

			Number crouchFar = new Number(14, nextEmptyRow, trial.getCouchFar());
			sheet.addCell(crouchFar);

			Number followingClose = new Number(15, nextEmptyRow, trial.getFollowingClose());
			sheet.addCell(followingClose);

			Number followingFar = new Number(16, nextEmptyRow, trial.getFollowingFar());
			sheet.addCell(followingFar);

			Number facingAwayClose = new Number(17, nextEmptyRow, trial.getFacingAwayClose());
			sheet.addCell(facingAwayClose);
			
			Number facingAwayFar = new Number(18, nextEmptyRow, trial.getFacingAwayFar());
			sheet.addCell(facingAwayFar);
			
			Number groomMarkClose = new Number(19, nextEmptyRow, trial.getGroomMarkClose());
			sheet.addCell(groomMarkClose);
			
			Number groomMarkFar = new Number(20, nextEmptyRow, trial.getGroomMarkFar());
			sheet.addCell(groomMarkFar);

			Number otherClose = new Number(21, nextEmptyRow, trial.getOtherClose());
			sheet.addCell(otherClose);

			Number otherFar = new Number(22, nextEmptyRow, trial.getOtherFar());
			sheet.addCell(otherFar);
			
			Number close = new Number(23, nextEmptyRow, trial.getClose());
			sheet.addCell(close);

			Number far = new Number(24, nextEmptyRow, trial.getFar());
			sheet.addCell(far);

			Number locationChanges = new Number(25, nextEmptyRow, trial.getLocationChanges());
			sheet.addCell(locationChanges);
			
			Number trialSectionStart = new Number(26, nextEmptyRow, trial.getTrialSectionStart());
			sheet.addCell(trialSectionStart);
			
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
