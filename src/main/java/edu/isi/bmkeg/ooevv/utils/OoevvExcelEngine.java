package edu.isi.bmkeg.ooevv.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.exceptions.MalformedOoevvFileException;
import edu.isi.bmkeg.ooevv.model.ExperimentalVariable;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.model.OoevvEntity;
import edu.isi.bmkeg.ooevv.model.OoevvProcess;
import edu.isi.bmkeg.ooevv.model.scale.BinaryScale;
import edu.isi.bmkeg.ooevv.model.scale.BinaryScaleWithNamedValues;
import edu.isi.bmkeg.ooevv.model.scale.CompositeScale;
import edu.isi.bmkeg.ooevv.model.scale.DecimalScale;
import edu.isi.bmkeg.ooevv.model.scale.HierarchicalScale;
import edu.isi.bmkeg.ooevv.model.scale.IntegerScale;
import edu.isi.bmkeg.ooevv.model.scale.MeasurementScale;
import edu.isi.bmkeg.ooevv.model.scale.NaturalLanguageScale;
import edu.isi.bmkeg.ooevv.model.scale.NominalScale;
import edu.isi.bmkeg.ooevv.model.scale.NominalScaleWithAllowedTerms;
import edu.isi.bmkeg.ooevv.model.scale.NumericScale;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScale;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScaleWithMaxRank;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScaleWithNamedRanks;
import edu.isi.bmkeg.ooevv.model.scale.RelativeTermScale;
import edu.isi.bmkeg.ooevv.model.scale.TimestampScale;
import edu.isi.bmkeg.ooevv.model.value.BinaryValue;
import edu.isi.bmkeg.ooevv.model.value.DecimalValue;
import edu.isi.bmkeg.ooevv.model.value.HierarchicalValue;
import edu.isi.bmkeg.ooevv.model.value.IntegerValue;
import edu.isi.bmkeg.ooevv.model.value.MeasurementValue;
import edu.isi.bmkeg.ooevv.model.value.NominalValue;
import edu.isi.bmkeg.ooevv.model.value.OrdinalValue;
import edu.isi.bmkeg.ooevv.model.value.RelativeValue;
import edu.isi.bmkeg.people.model.Person;
import edu.isi.bmkeg.terminology.model.Ontology;
import edu.isi.bmkeg.terminology.model.Term;
import edu.isi.bmkeg.terminology.utils.bioportal.BioportalSearch;
import edu.isi.bmkeg.uml.model.UMLattribute;
import edu.isi.bmkeg.uml.model.UMLclass;
import edu.isi.bmkeg.uml.model.UMLmodel;
import edu.isi.bmkeg.utils.excel.ExcelEngine;

public class OoevvExcelEngine extends ExcelEngine {

	Logger log = Logger.getLogger("edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine");

	private BioportalSearch bps = new BioportalSearch();

	private OoevvElementSet exptVbSet;

	private File dataDirectory;

	private short bStyle = HSSFCellStyle.BORDER_THIN;
	private short bStyle2 = HSSFCellStyle.BORDER_MEDIUM;
	private String currSheet;
	private int sheetnum;
	private String currentExptName;
	private SSTRecord sstrec;

	/*
	 * public java.io.File buildExcelWorkbook2() throws Exception { short
	 * rownum;
	 * 
	 * // create a new file File file = new File(this.dataDirectory.getPath() +
	 * "/" + art.getFilenameStem() + "-dump.xls"); FileOutputStream out = new
	 * FileOutputStream(file);
	 * 
	 * // create a new workbook wb = new HSSFWorkbook();
	 * 
	 * cs = wb.createCellStyle(); f = wb.createFont();
	 * f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); cs.setFont(f);
	 * 
	 * sCount = 0;
	 * 
	 * Iterator it = art.getExperiments().keySet().iterator(); while
	 * (it.hasNext()) { KefedModel expt = (KefedModel)
	 * art.getExperiments().get(it.next()); buildDataSheetsForExpt(expt);
	 * buildRelationSheetsForExpt(expt); buildCorrelationSheetsForExpt(expt); }
	 * 
	 * // write the workbook to the output stream // close our file (don't blow
	 * out our file handles wb.write(out); out.close();
	 * 
	 * return file;
	 * 
	 * }
	 */

	public void generateBlankOoevvExcelWorkbook(File file) throws Exception {

		FileOutputStream out = new FileOutputStream(file);

		// create a new workbook
		HSSFWorkbook wb = new HSSFWorkbook();
		setWb(wb);

		setCs(wb.createCellStyle());
		setF(wb.createFont());
		getF().setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		getCs().setFont(getF());

		// ____________________________________________
		// OoevvElementSet
		//
		HSSFSheet s = wb.createSheet();
		wb.setSheetName(0, "OoEVV");

		// declare a row object reference
		HSSFRow r = null;
		// declare a cell object reference
		HSSFCell c = null;

		r = s.createRow(0);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		r = s.createRow(1);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("description");

		r = s.createRow(2);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("resource");

		r = s.createRow(3);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("url");

		// ____________________________________________
		// Variables
		//
		s = wb.createSheet();
		wb.setSheetName(1, "OoEVV Variables");

		// Insert variable headings
		short rCount = 0;
		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		// write the top row of the data table.
		r = s.createRow(rCount);

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("term");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("scale");

		c = r.createCell((short) 5);
		c.setCellStyle(getCs());
		c.setCellValue("comments");

		c = r.createCell((short) 6);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		// ____________________________________________
		// Scales
		//
		s = wb.createSheet();
		wb.setSheetName(2, "OoEVV Scales");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		// write the top row of the data table.
		r = s.createRow(rCount);

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("comments");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("type");

		c = r.createCell((short) 5);
		c.setCellStyle(getCs());
		c.setCellValue("units");

		c = r.createCell((short) 6);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		c = r.createCell((short) 7);
		c.setCellStyle(getCs());
		c.setCellValue("values");

		// ____________________________________________
		// Values
		//
		s = wb.createSheet();
		wb.setSheetName(3, "OoEVV Values");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("scale");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("value");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("type");

		c = r.createCell((short) 5);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		// ____________________________________________
		// Processes
		//
		s = wb.createSheet();
		wb.setSheetName(4, "OoEVV Processes");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("value");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("obi");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("curator");
		
		// ____________________________________________
		// Entities
		//
		s = wb.createSheet();
		wb.setSheetName(5, "OoEVV Entities");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("value");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("obi");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("curator");
		
		// ____________________________________________
		// Curators
		//
		s = wb.createSheet();
		wb.setSheetName(6, "OoEVV Curators");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("initials");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("email");

		wb.write(out);

	}

	public HSSFWorkbook generateOoevvExcelWorkbook(File file,
			OoevvElementSet exptVbSet) throws Exception {

		Set<ExperimentalVariable> exptVbs = ExtendedOoevvDaoImpl.listExptVbsInObjectGraph(exptVbSet);
		Set<MeasurementScale> scales = ExtendedOoevvDaoImpl.listScalesInObjectGraph(exptVbSet);
		Set<OoevvProcess> processes = ExtendedOoevvDaoImpl.listProcessesInObjectGraph(exptVbSet);
		Set<OoevvEntity> entities = ExtendedOoevvDaoImpl.listEntitiesInObjectGraph(exptVbSet);

		Map<Term, MeasurementValue> values = new HashMap<Term, MeasurementValue>();
		Set<Person> curators = new HashSet<Person>();

		FileOutputStream out = new FileOutputStream(file);

		// create a new workbook
		HSSFWorkbook wb = new HSSFWorkbook();
		setWb(wb);

		setCs(wb.createCellStyle());
		setF(wb.createFont());
		getF().setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		getCs().setFont(getF());

		// ____________________________________________
		// OoevvElementSet
		//
		HSSFSheet s = wb.createSheet();
		wb.setSheetName(0, "OoEVV");

		// declare a row object reference
		HSSFRow r = null;
		// declare a cell object reference
		HSSFCell c = null;

		r = s.createRow(0);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 1);
		c.setCellValue(exptVbSet.getTermValue());

		r = s.createRow(1);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("description");

		c = r.createCell((short) 1);
		c.setCellValue(exptVbSet.getDefinition());

		// ____________________________________________
		// Variables
		//
		s = wb.createSheet();
		wb.setSheetName(1, "OoEVV Variables");

		// Insert variable headings
		short rCount = 0;
		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		// write the top row of the data table.
		r = s.createRow(rCount);

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("term");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("scale");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("comments");

		c = r.createCell((short) 5);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		Iterator<ExperimentalVariable> vbIt = exptVbs.iterator();
		while (vbIt.hasNext()) {
			ExperimentalVariable vb = vbIt.next();

			rCount++;
			r = s.createRow(rCount);

			c = r.createCell((short) 0);
			c.setCellValue(vb.getShortTermId());

			c = r.createCell((short) 1);
			c.setCellValue(vb.getTermValue());

			c = r.createCell((short) 2);
			c.setCellValue(vb.getDefinition());

			c = r.createCell((short) 3);
			c.setCellValue(vb.getMeasures().getShortTermId());

			c = r.createCell((short) 3);
			c.setCellValue(vb.getScale().getShortTermId());

			c = r.createCell((short) 4);
			c.setCellValue("");

			// remove tracking of curator for all terms
			c = r.createCell((short) 5);
			String cStr = "";
			/*Iterator<TerminologyCurator> cIt = vb.getDefinitionEditor()
					.iterator();
			while (cIt.hasNext()) {
				TerminologyCurator cur = cIt.next();
				cStr += cur.getVpdmfId();
				if (cIt.hasNext())
					cStr += ",";
			}*/
			c.setCellValue(cStr);

		}

		// ____________________________________________
		// Scales
		//
		s = wb.createSheet();
		wb.setSheetName(2, "OoEVV Scales");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		// write the top row of the data table.
		r = s.createRow(rCount);

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("comments");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("type");

		c = r.createCell((short) 5);
		c.setCellStyle(getCs());
		c.setCellValue("units");

		c = r.createCell((short) 6);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		c = r.createCell((short) 7);
		c.setCellStyle(getCs());
		c.setCellValue("values");

		Iterator<MeasurementScale> scIt = scales.iterator();
		while (scIt.hasNext()) {
			MeasurementScale sc = scIt.next();

			rCount++;
			r = s.createRow(rCount);

			c = r.createCell((short) 0);
			c.setCellValue(sc.getShortTermId());

			c = r.createCell((short) 1);
			c.setCellValue(sc.getTermValue());

			c = r.createCell((short) 2);
			c.setCellValue(sc.getDefinition());

			c = r.createCell((short) 3);
			c.setCellValue("");

			c = r.createCell((short) 4);
			String t = sc.getClass().getSimpleName();
			c.setCellValue(t);

			c = r.createCell((short) 5);
			String units = "";
			if (sc instanceof NumericScale) {
				NumericScale ns = (NumericScale) sc;
				units = ns.getUnits().getShortTermId();
			}
			c.setCellValue(units);

			c = r.createCell((short) 6);
			String cStr = "";
			/*Iterator<TerminologyCurator> cIt = sc.getDefinitionEditor()
					.iterator();
			while (cIt.hasNext()) {
				Person cur = cIt.next();
				cStr += cur.getVpdmfId();
				if (cIt.hasNext())
					cStr += ",";
			}*/
			c.setCellValue(cStr);

			String sType = sc.getClass().getSimpleName();
			if (sType.equals("BinaryScaleWithNamedValues")) {

				BinaryScaleWithNamedValues bswnv = (BinaryScaleWithNamedValues) sc;

				c = r.createCell((short) 6);
				c.setCellValue(bswnv.getTrueValue().getShortTermId());
				c = r.createCell((short) 7);
				c.setCellValue(bswnv.getFalseValue().getShortTermId());

				values.put(bswnv.getTrueValue(), bswnv.getTrueValue());
				values.put(bswnv.getFalseValue(),
						bswnv.getFalseValue());

			} else if (sType.equals("NominalScaleWithAllowedTerms")) {

				NominalScaleWithAllowedTerms bswnv = (NominalScaleWithAllowedTerms) sc;

				Iterator<NominalValue> idIt = bswnv.getNominalValues()
						.iterator();
				short cc = (short) 6;
				while (idIt.hasNext()) {
					NominalValue av = idIt.next();
					c = r.createCell(cc);
					c.setCellValue(av.getShortTermId());
					cc++;
					values.put(av, av);
				}

			} else if (sType.equals("OrdinalScaleWithNamedRanks")) {

				OrdinalScaleWithNamedRanks oswnr = (OrdinalScaleWithNamedRanks) sc;

				Iterator<OrdinalValue> idIt = oswnr.getOrdinalValues()
						.iterator();
				short cc = (short) 6;
				while (idIt.hasNext()) {
					OrdinalValue av = idIt.next();
					c = r.createCell(cc);
					c.setCellValue(av.getShortTermId());
					cc++;
					values.put(av, av);
				}

			} else if (sType.equals("RelativeTermScale")) {

				RelativeTermScale rts = (RelativeTermScale) sc;

				Iterator<Term> idIt = rts.getAllowedRelations().iterator();
				short cc = (short) 6;
				while (idIt.hasNext()) {
					Term av = idIt.next();
					c = r.createCell(cc);
					c.setCellValue(av.getShortTermId());
					cc++;
					values.put(av, null);
				}

			} else if (sType.equals("HierarchicalScale")) {

				HierarchicalScale hts = (HierarchicalScale) sc;

				Iterator<HierarchicalValue> idIt = hts.getHierarchicalValues()
						.iterator();
				short cc = (short) 6;
				while (idIt.hasNext()) {
					HierarchicalValue av = idIt.next();
					c = r.createCell(cc);
					c.setCellValue(av.getShortTermId());
					cc++;
					values.put(av, av);
				}

			}

		}

		// ____________________________________________
		// Values
		//
		s = wb.createSheet();
		wb.setSheetName(3, "OoEVV Values");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("scale");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("value");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("type");

		c = r.createCell((short) 5);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		Iterator<Term> vIt = values.keySet().iterator();
		while (vIt.hasNext()) {
			Term t = vIt.next();

			rCount++;
			r = s.createRow(rCount);

			c = r.createCell((short) 0);
			c.setCellValue(t.getShortTermId());

			c = r.createCell((short) 1);
			c.setCellValue(t.getTermValue());

			c = r.createCell((short) 2);
			c.setCellValue(t.getDefinition());

			c = r.createCell((short) 3);
			c.setCellValue("");

			c = r.createCell((short) 4);
			String cStr = "";
			/*Iterator<TerminologyCurator> cIt = t.getDefinitionEditor().iterator();
			while (cIt.hasNext()) {
				TerminologyCurator cur = cIt.next();
				cStr += cur.getVpdmfId();
				if (cIt.hasNext())
					cStr += ",";
			}*/
			c.setCellValue(cStr);

		}

		// ____________________________________________
		// Processes
		//
		s = wb.createSheet();
		wb.setSheetName(4, "OoEVV Processes");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("value");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("obi");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		Iterator<OoevvProcess> pIt = processes.iterator();
		while (pIt.hasNext()) {
			OoevvProcess p = pIt.next();

			rCount++;
			r = s.createRow(rCount);

			c = r.createCell((short) 0);
			c.setCellValue(p.getShortTermId());

			c = r.createCell((short) 1);
			c.setCellValue(p.getTermValue());

			c = r.createCell((short) 2);
			c.setCellValue(p.getDefinition());

			c = r.createCell((short) 3);
			c.setCellValue(p.getObiTerm().getShortTermId());

			c = r.createCell((short) 4);
			c.setCellValue("");

			c = r.createCell((short) 5);
			String cStr = "";
			/*Iterator<TerminologyCurator> cIt = p.getDefinitionEditor().iterator();
			while (cIt.hasNext()) {
				TerminologyCurator cur = cIt.next();
				cStr += cur.getVpdmfId();
				if (cIt.hasNext())
					cStr += ",";
			}*/
			c.setCellValue(cStr);

		}

		// ____________________________________________
		// Entities
		//
		s = wb.createSheet();
		wb.setSheetName(5, "OoEVV Entities");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("shortId");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("value");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("obi");

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("curator");

		Iterator<OoevvEntity> eIt = entities.iterator();
		while (eIt.hasNext()) {
			OoevvEntity e = eIt.next();

			rCount++;
			r = s.createRow(rCount);

			c = r.createCell((short) 0);
			c.setCellValue(e.getShortTermId());

			c = r.createCell((short) 1);
			c.setCellValue(e.getTermValue());

			c = r.createCell((short) 2);
			c.setCellValue(e.getDefinition());

			c = r.createCell((short) 3);
			c.setCellValue(e.getObiTerm().getShortTermId());

			c = r.createCell((short) 4);
			c.setCellValue("");

			c = r.createCell((short) 5);
			String cStr = "";
			/*Iterator<TerminologyCurator> cIt = e.getDefinitionEditor().iterator();
			while (cIt.hasNext()) {
				Person cur = cIt.next();
				cStr += cur.getVpdmfId();
				if (cIt.hasNext())
					cStr += ",";
			}*/
			c.setCellValue(cStr);

		}

		// ____________________________________________
		// Curators
		//
		s = wb.createSheet();
		wb.setSheetName(6, "OoEVV Curators");

		rCount = 0;

		r = s.createRow(rCount);
		c = r.createCell((short) 0);
		c.setCellStyle(getCs());

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("initials");

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("name");

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("email");

		Iterator<Person> curIt = curators.iterator();
		while (curIt.hasNext()) {
			Person cur = curIt.next();

			rCount++;
			r = s.createRow(rCount);

			c = r.createCell((short) 0);
			c.setCellValue(cur.getVpdmfId());

			c = r.createCell((short) 1);
			c.setCellValue(cur.getFullName());

			c = r.createCell((short) 2);
			c.setCellValue(cur.getEmail());

		}

		return getWb();

	}

	
	public OoevvElementSet createExpVariableSetFromExcel(File f)
			throws Exception {
		
		return this.createExpVariableSetFromExcel(f, false);

	}
	
	public OoevvElementSet createExpVariableSetFromExcel(File f, boolean includeLookup)
			throws Exception {

		Ontology ooevv = this.buildOoEVV();

		this.exptVbSet = new OoevvElementSet();
		Map<CompositeScale, String> compositeScales = new HashMap<CompositeScale, String>();
		Map<String, ExperimentalVariable> exVbLookup = new HashMap<String, ExperimentalVariable>();
		Map<MeasurementValue, String> valueScaleLookup = new HashMap<MeasurementValue, String>();
		
		this.readFile(f);

		// ____________________________________________

		String sSheet = "OoEVV";

		// detect row headings
		Map<String, Integer> rh = getRowHeadings(sSheet);

		String name = this.getData(0, 1, sSheet);
		String desc = this.getData(1, 1, sSheet);

		String shortId = name.replaceAll("\\s+", "-").toLowerCase();

		this.exptVbSet.setTermValue(name);
		this.exptVbSet.setShortTermId(shortId);
		this.exptVbSet.setDefinition(desc);
		this.exptVbSet.setOntology(ooevv);

		// ____________________________________________

		String cSheet = "OoEVV Curators";
		Dimension cDim = this.getMatrixDimensions(cSheet);
		int nCurators = cDim.height - 1;

		Map<String, Term> terms = new HashMap<String, Term>();

		// ____________________________________________

		String vSheet = "OoEVV Values";
		Dimension vDim = this.getMatrixDimensions(vSheet);
		Map<String, Integer> ch = getColumnHeadings(vSheet);
		int nValues = vDim.height - 1;

		Map<String, MeasurementValue> values = new HashMap<String, MeasurementValue>();

		for (int i = 0; i < nValues; i++) {

			Integer idCol = ch.get("shortId");
			Integer scaleCol = ch.get("scale");
			Integer valCol = ch.get("value");
			Integer defCol = ch.get("definition");
			Integer typeCol = ch.get("type");
			Integer ontIdCol = ch.get("ontologyId");
			Integer curCol = ch.get("curator");

			if (idCol == null || valCol == null || defCol == null
					|| typeCol == null || curCol == null)
				throw new MalformedOoevvFileException("Misnamed Value Definition column headings");

			String scale = this.getData(i + 1, scaleCol, vSheet);
			String vid = this.getData(i + 1, idCol, vSheet);
			String val = this.getData(i + 1, valCol, vSheet);
			String def = this.getData(i + 1, defCol, vSheet);
			String type = this.getData(i + 1, typeCol, vSheet);

			String ontIdStr = this.getData(i + 1, ontIdCol, vSheet);
			if (ontIdStr.lastIndexOf(".") != -1)
				ontIdStr = ontIdStr.substring(0, ontIdStr.lastIndexOf("."));

			String cur = this.getData(i + 1, curCol, vSheet);

			if (vid == null || vid.isEmpty())
				continue;

			// ____________________________________________
			// Read the spreadsheet and instantiate
			// OoEVV MeasurementValue objects appropriately
			//
			MeasurementValue mv = null;
			if (type.equals("BinaryValue")) {

				mv = new BinaryValue();

			} else if (type.equals("IntegerValue")) {

				mv = new IntegerValue();

				// how to deal with units?

			} else if (type.equals("DecimalValue")) {

				mv = new DecimalValue();

				// how to deal with units?

			} else if (type.equals("NominalValue")) {

				mv = new NominalValue();

			} else if (type.equals("OrdinalValue")) {

				mv = new OrdinalValue();

			} else if (type.equals("RelativeTermValue")) {

				mv = new RelativeValue();
				RelativeValue rts = (RelativeValue) mv;

			} else if (type.equals("HierarchicalValue")) {

				mv = new HierarchicalValue();
				HierarchicalValue hts = (HierarchicalValue) mv;

			} else {

				throw new Exception("Don't recognize " + type
						+ ", as a type of value");

			}

			mv.setTermValue(val);
			mv.setShortTermId(vid);
			mv.setDefinition(def);
			mv.setOntology(ooevv);

			if (ontIdStr.length() > 0 && includeLookup) {

				Integer ontId = new Integer(ontIdStr);
				List<Ontology> ontHits = new ArrayList<Ontology>();
				List<Term> termHits = new ArrayList<Term>();
				Term vTerm = null;
				Ontology o = null;
				
				termHits = bps.termSearch(ontId, vid);
				ontHits = bps.ontologySearch(ontId);
				
				if (ontHits.size() == 1) {

					o = ontHits.get(0);
					
				} else {

					throw new Exception(ontId + " returns " + termHits.size()
							+ " results, should be a unique ontology.");

				}
				
				if (termHits.size() == 1) {

					vTerm = termHits.get(0);
					vTerm.setOntology(o);

				} else {

					throw new Exception(o.getDisplayName() + ":" + vid + 
							" returns " + termHits.size()
							+ " results, should be unique.");

				}

			}
			
			valueScaleLookup.put(mv, scale);

			if (vid == null || vid.length() == 0) {
				continue;
			}

			values.put(vid, mv);

		}
		


		// ____________________________________________

		sSheet = "OoEVV Scales";
		Dimension sDim = this.getMatrixDimensions(sSheet);
		int nScales = sDim.height - 1;

		Map<String, MeasurementScale> scales = new HashMap<String, MeasurementScale>();
		
		// detect column headings
		ch = getColumnHeadings(sSheet);

		for (int i = 0; i < nScales; i++) {

			// ____________________________________________
			// Read the spreadsheet.
			//
			String sid = this.getData(i + 1, ch.get("shortId"), sSheet);
			String sName = this.getData(i + 1, ch.get("name"), sSheet);
			String sDef = this.getData(i + 1, ch.get("definition"), sSheet);
			String sType = this.getData(i + 1, ch.get("type"), sSheet);
			String sUnits = this.getData(i + 1, ch.get("units"), sSheet);
			String sCurator = this.getData(i + 1, ch.get("curator"), sSheet);

			if (sName.length() == 0)
				continue;

			List<String> sValues = new ArrayList<String>();
			int valCol = ch.get("values");
			String sValue = this.getData(i + 1, valCol, sSheet);
			while (sValue.length() > 0 && valCol < sDim.width) {
				sValues.add(sValue);
				valCol++;
				if (valCol < sDim.width)
					sValue = this.getData(i + 1, valCol, sSheet);
			}

			// ____________________________________________
			// Read the spreadsheet and instantiate
			// an OoEVV scale appropriately
			//
			MeasurementScale ms = null;
			if (sType.equals("BinaryScale")) {

				ms = new BinaryScale();

			} else if (sType.equals("BinaryScaleWithNamedValues")) {

				ms = new BinaryScaleWithNamedValues();
				BinaryScaleWithNamedValues bswnv = (BinaryScaleWithNamedValues) ms;

				if (sValues.size() != 2)
					throw new Exception(
							"Please specify named values for BinaryScaleWithNamedValues:"
									+ sName);

				BinaryValue trueValue = (BinaryValue) values
						.get(sValues.get(0));
				trueValue.setBinaryValue(true);
				
				BinaryValue falseValue = (BinaryValue) values
						.get(sValues.get(1));
				falseValue.setBinaryValue(false);

				if (trueValue == null || falseValue == null)
					throw new Exception(
							"Nulls in true / false values for BinaryScaleWithNamedValues:"
									+ sName);

				bswnv.setTrueValue(trueValue);
				bswnv.setFalseValue(falseValue);

			} else if (sType.equals("IntegerScale")) {

				ms = new IntegerScale();

				// how to deal with units?

			} else if (sType.equals("DecimalScale")) {

				ms = new DecimalScale();

				// how to deal with units?

			} else if (sType.equals("TimestampScale")) {

				ms = new TimestampScale();
				TimestampScale tss = (TimestampScale) ms;
				tss.setFormat(sValue);

			} else if (sType.equals("NaturalLanguageScale")) {

				ms = new NaturalLanguageScale();

			} else if (sType.equals("NominalScale")) {

				ms = new NominalScale();

			} else if (sType.equals("NominalScaleWithAllowedTerms")) {

				ms = new NominalScaleWithAllowedTerms();

				NominalScaleWithAllowedTerms bswnv = (NominalScaleWithAllowedTerms) ms;

				Iterator<String> idIt = sValues.iterator();
				while (idIt.hasNext()) {
					String id = idIt.next();
					NominalValue av = (NominalValue) values.get(id);
					if (av != null)
						bswnv.getNominalValues().add(av);
				}

			} else if (sType.equals("OrdinalScale")) {

				ms = new OrdinalScale();

			} else if (sType.equals("OrdinalScaleWithMaxRank")) {

				ms = new OrdinalScaleWithMaxRank();
				OrdinalScaleWithMaxRank oswr = (OrdinalScaleWithMaxRank) ms;
				Iterator<String> idIt = sValues.iterator();
				while (idIt.hasNext()) {
					String id = idIt.next();
					Float temp = new Float(id);
					Long max = temp.longValue();
					oswr.setMaximumRank(max.intValue());
				}

			} else if (sType.equals("OrdinalScaleWithNamedRanks")) {

				ms = new OrdinalScaleWithNamedRanks();

				OrdinalScaleWithNamedRanks oswnr = (OrdinalScaleWithNamedRanks) ms;

				int r = 0;
				Iterator<String> idIt = sValues.iterator();
				while (idIt.hasNext()) {
					String id = idIt.next();
					OrdinalValue av = (OrdinalValue) values.get(id);
					if (av != null) {
						oswnr.getOrdinalValues().add(av);
						av.setRank(r);
						r++;
					}
				}

			} else if (sType.equals("RelativeTermScale")) {

				ms = new RelativeTermScale();
				RelativeTermScale rts = (RelativeTermScale) ms;

				Iterator<String> idIt = sValues.iterator();
				while (idIt.hasNext()) {
					String id = idIt.next();
					RelativeValue ar = (RelativeValue) values.get(id);
					// NEED TO DO THIS A DIFFERENT WAY.
					// if (ar != null)
					// rts.getAllowedRelations().add(ar);
				}

			} else if (sType.equals("HierarchicalScale")) {

				ms = new HierarchicalScale();

				HierarchicalScale hts = (HierarchicalScale) ms;

				Iterator<String> idIt = sValues.iterator();
				while (idIt.hasNext()) {
					String id = idIt.next();
					HierarchicalValue av = (HierarchicalValue) values.get(id);
					if (av != null)
						hts.getHierarchicalValues().add(av);
				}

			} else if (sType.equals("CompositeScale")) {

				ms = new CompositeScale();

				CompositeScale mvcs = (CompositeScale) ms;

				Iterator<String> idIt = sValues.iterator();
				String s = idIt.next();
				while (idIt.hasNext()) {
					s += "," + idIt.next();
				}
				compositeScales.put(mvcs, s);

			} else {

				throw new Exception("Don't recognize " + sType
						+ ", as a type of scale");

			}

			ms.setClassType(sType);

			ms.setTermValue(sName);
			ms.setShortTermId(sid);
			ms.setDefinition(sDef);
			ms.setOntology(ooevv);

			scales.put(sid, ms);

		}

		vSheet = "OoEVV Variables";
		vDim = this.getMatrixDimensions(vSheet);
		ch = getColumnHeadings(vSheet);
		int nVariables = vDim.height - 1;

		Pattern whitespacePattern = Pattern.compile("\\s+");
		Pattern slashPattern = Pattern.compile("\\/");
		Pattern percentPattern = Pattern.compile("\\%");

		for (int i = 0; i < nVariables; i++) {

			Integer idCol = ch.get("shortId");
			Integer nameCol = ch.get("name");
			Integer defCol = ch.get("definition");
			Integer termCol = ch.get("measures");
			Integer scaleCol = ch.get("scale");
			Integer comCol = ch.get("comments");
			Integer curCol = ch.get("curator");
			Integer ontIdCol = ch.get("ontologyId");

			if (idCol == null || nameCol == null || defCol == null
					|| termCol == null || scaleCol == null || comCol == null
					|| curCol == null)
				throw new Exception(
						"Misnamed Variable Definition column headings");

			String vid = this.getData(i + 1, idCol, vSheet);

			// trim leading and trailing whitespace
			vid = vid.replaceAll("(\\s+)$", "");
			vid = vid.replaceAll("^(\\s+)", "");

			String vName = this.getData(i + 1, nameCol, vSheet);
			String vDef = this.getData(i + 1, defCol, vSheet);
			String vMeasures = this.getData(i + 1, termCol, vSheet);
			String vScaleName = this.getData(i + 1, scaleCol, vSheet);
			String vComments = this.getData(i + 1, comCol, vSheet);
			String vCurator = this.getData(i + 1, curCol, vSheet);

			String ontIdStr = this.getData(i + 1, ontIdCol, vSheet);
			if (ontIdStr.lastIndexOf(".") != -1)
				ontIdStr = ontIdStr.substring(0, ontIdStr.lastIndexOf("."));

			if (vName.length() == 0)
				continue;

			Matcher m1 = whitespacePattern.matcher(vid);
			/*
			 * if( m1.find() ) { throw new BadlyFormedTermNameException(
			 * "White space is not allowed in term names"); }
			 * 
			 * Matcher m2 = percentPattern.matcher(vid); if( m2.find() ) { throw
			 * new BadlyFormedTermNameException(
			 * "Percentage signs are not allowed in term names"); }
			 * 
			 * Matcher m3 = slashPattern.matcher(vid); if( m2.find() ) { throw
			 * new BadlyFormedTermNameException(
			 * "Slash signs are not allowed in term names"); }
			 */

			Term measures = null;
			if (ontIdStr.length() > 0 && includeLookup) {

				Integer ontId = new Integer(ontIdStr);
				List<Ontology> ontHits = new ArrayList<Ontology>();
				List<Term> measureHits = new ArrayList<Term>();
				try {
					measureHits = bps.termSearch(ontId, vMeasures);
					ontHits = bps.ontologySearch(ontId);
				} catch (IOException e) {
					log.debug("Bioportal seems to be down");
				}

				if (measureHits.size() == 1) {

					measures = measureHits.get(0);
					Ontology o = ontHits.get(0);
					measures.setOntology(o);

				} else {

					throw new Exception(vMeasures + " returns "
							+ measureHits.size()
							+ " results, should be unique.");

				}

			}

			ExperimentalVariable v = new ExperimentalVariable();
			v.setElementType("ExperimentalVariable");
			v.setTermValue(vName);
			v.setShortTermId(vid);
			v.setDefinition(vDef);
			v.setOntology(ooevv);
			v.setMeasures(measures);

			if (vScaleName.length() != 0 && !vScaleName.equals("-")) {
				MeasurementScale ms = scales.get(vScaleName);
				if (ms == null)
					throw new Exception("Can't find scale named '" + vScaleName
							+ "'!");
				v.setScale(ms);
			}

			v.getSets().add(this.exptVbSet);
			this.exptVbSet.getElements().add(v);
			
			exVbLookup.put(v.getShortTermId(), v);

		}
		
		vSheet = "OoEVV Processes";
		vDim = this.getMatrixDimensions(vSheet);
		ch = getColumnHeadings(vSheet);
		int nProcesses = vDim.height - 1;

		for (int i = 0; i < nProcesses; i++) {

			Integer idCol = ch.get("shortId");
			Integer nameCol = ch.get("name");
			Integer defCol = ch.get("definition");
			Integer termCol = ch.get("obi");
			Integer comCol = ch.get("comments");
			Integer curCol = ch.get("curator");

			if (idCol == null || nameCol == null || defCol == null
					|| termCol == null || comCol == null
					|| curCol == null)
				throw new Exception(
						"Misnamed Variable Definition column headings");

			String vid = this.getData(i + 1, idCol, vSheet);

			// trim leading and trailing whitespace
			vid = vid.replaceAll("(\\s+)$", "");
			vid = vid.replaceAll("^(\\s+)", "");

			String vName = this.getData(i + 1, nameCol, vSheet);
			String vDef = this.getData(i + 1, defCol, vSheet);
			String vObi = this.getData(i + 1, termCol, vSheet);
			String vComments = this.getData(i + 1, comCol, vSheet);
			String vCurator = this.getData(i + 1, curCol, vSheet);

			if (vName.length() == 0)
				continue;

			Matcher m1 = whitespacePattern.matcher(vid);
			/*
			 * if( m1.find() ) { throw new BadlyFormedTermNameException(
			 * "White space is not allowed in term names"); }
			 * 
			 * Matcher m2 = percentPattern.matcher(vid); if( m2.find() ) { throw
			 * new BadlyFormedTermNameException(
			 * "Percentage signs are not allowed in term names"); }
			 * 
			 * Matcher m3 = slashPattern.matcher(vid); if( m2.find() ) { throw
			 * new BadlyFormedTermNameException(
			 * "Slash signs are not allowed in term names"); }
			 */
			
			Term obi = null;
			if (vObi.length() > 0 && includeLookup) {

				Integer ontId = new Integer(1123);
				List<Ontology> ontHits = new ArrayList<Ontology>();
				List<Term> measureHits = new ArrayList<Term>();
				try {
					measureHits = bps.termSearch(ontId, vObi);
					ontHits = bps.ontologySearch(ontId);
				} catch (IOException e) {
					log.debug("Bioportal seems to be down");
					continue;
				}

				if (measureHits.size() == 1) {

					obi = measureHits.get(0);
					Ontology o = ontHits.get(0);
					obi.setOntology(o);

				} else {

					throw new Exception(vObi + " returns "
							+ measureHits.size()
							+ " results, should be unique.");

				}

			}


			OoevvProcess v = new OoevvProcess();
			v.setElementType("OoevvProcess");
			v.setTermValue(vName);
			v.setShortTermId(vid);
			v.setDefinition(vDef);
			v.setOntology(ooevv);

			v.setObiTerm(obi);
			
			v.getSets().add(this.exptVbSet);
			this.exptVbSet.getElements().add(v);

		}

		vSheet = "OoEVV Entities";
		vDim = this.getMatrixDimensions(vSheet);
		ch = getColumnHeadings(vSheet);
		int nEntities = vDim.height - 1;

		for (int i = 0; i < nEntities; i++) {

			Integer idCol = ch.get("shortId");
			Integer nameCol = ch.get("name");
			Integer defCol = ch.get("definition");
			Integer termCol = ch.get("obi");
			Integer comCol = ch.get("comments");
			Integer curCol = ch.get("curator");

			if (idCol == null || nameCol == null || defCol == null
					|| termCol == null || comCol == null
					|| curCol == null)
				throw new Exception(
						"Misnamed Variable Definition column headings");

			String vid = this.getData(i + 1, idCol, vSheet);

			// trim leading and trailing whitespace
			vid = vid.replaceAll("(\\s+)$", "");
			vid = vid.replaceAll("^(\\s+)", "");

			String vName = this.getData(i + 1, nameCol, vSheet);
			String vDef = this.getData(i + 1, defCol, vSheet);
			String vObi = this.getData(i + 1, termCol, vSheet);
			String vComments = this.getData(i + 1, comCol, vSheet);
			String vCurator = this.getData(i + 1, curCol, vSheet);

			if (vName.length() == 0)
				continue;

			Matcher m1 = whitespacePattern.matcher(vid);
			/*
			 * if( m1.find() ) { throw new BadlyFormedTermNameException(
			 * "White space is not allowed in term names"); }
			 * 
			 * Matcher m2 = percentPattern.matcher(vid); if( m2.find() ) { throw
			 * new BadlyFormedTermNameException(
			 * "Percentage signs are not allowed in term names"); }
			 * 
			 * Matcher m3 = slashPattern.matcher(vid); if( m2.find() ) { throw
			 * new BadlyFormedTermNameException(
			 * "Slash signs are not allowed in term names"); }
			 */
			
			Term obi = null;
			if (vObi.length() > 0 && includeLookup) {

				Integer ontId = new Integer(1123);
				List<Ontology> ontHits = new ArrayList<Ontology>();
				List<Term> measureHits = new ArrayList<Term>();
				try {
					measureHits = bps.termSearch(ontId, vObi);
					ontHits = bps.ontologySearch(ontId);
				} catch (IOException e) {
					log.debug("Bioportal seems to be down");
				}

				if (measureHits.size() == 1) {

					obi = measureHits.get(0);
					Ontology o = ontHits.get(0);
					obi.setOntology(o);

				} else {

					throw new Exception(vObi + " returns "
							+ measureHits.size()
							+ " results, should be unique.");

				}

			}

			OoevvEntity e = new OoevvEntity();
			e.setElementType("OoevvEntity");
			e.setTermValue(vName);
			e.setShortTermId(vid);
			e.setDefinition(vDef);
			e.setOntology(ooevv);
			e.setObiTerm(obi);
			
			e.getSets().add(this.exptVbSet);
			this.exptVbSet.getElements().add(e);

		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Now that we have a good list of variables constructed, 
		// we can link the multi-variable scales. 
		//
		Iterator<CompositeScale> compIt = compositeScales.keySet().iterator();
		while( compIt.hasNext() ) {
			CompositeScale mvcs = compIt.next();
			String subVbListString = compositeScales.get(mvcs);
			String[] subVbList = subVbListString.split(",");
			for(int i=0; i<subVbList.length; i++) {
				String subVbId = subVbList[i];
				if( !exVbLookup.containsKey(subVbId) ) {
					throw new Exception("Can't find "+ subVbId + " in specification of " + 
								"MultiVariableCompositeScale: " + mvcs.getShortTermId());
				} else {
					ExperimentalVariable ev = exVbLookup.get(subVbId);
					mvcs.getHasParts().add(ev);
					ev.getPartOf().add(mvcs);
				}
			}
		}
		
		return this.exptVbSet;

	}

	public void generateBlankSubjectMatterExpertWorkbook(File file)
			throws Exception {

		this.generateSubjectMatterExpertWorkbook(file,
				new ArrayList<UMLmodel>());

	}

	public void generateSubjectMatterExpertWorkbook(File file,
			List<UMLmodel> models) throws Exception {

		FileOutputStream out = new FileOutputStream(file);

		// create a new workbook
		HSSFWorkbook wb = new HSSFWorkbook();
		setWb(wb);

		setCs(wb.createCellStyle());
		setF(wb.createFont());
		getF().setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		getCs().setFont(getF());

		Iterator<UMLmodel> mIt = models.iterator();
		while (mIt.hasNext()) {
			UMLmodel m = mIt.next();
			addSheetForModel(m, wb);
		}

		getWb().write(out);
		out.close();

	}

	private void addSheetForModel(UMLmodel m, HSSFWorkbook wb) {

		// ____________________________________________
		// Sheet 1
		//
		HSSFSheet s = wb.createSheet();
		wb.setSheetName(wb.getNumberOfSheets() - 1, m.getName());

		// declare a row object reference
		HSSFRow r = null;
		// declare a cell object reference
		HSSFCell c = null;

		// Insert variable headings
		short rCount = 0;
		r = s.createRow(rCount);

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		// Region header = new Region(rCount, (short) 0, rCount, (short)(4) );
		// s.addMergedRegion(header);

		// write the top row of the data table.
		r = s.createRow(rCount);

		c = r.createCell((short) 0);
		c.setCellStyle(getCs());
		c.setCellValue("Variable?");
		s.setColumnWidth((short) 0, (short) 1e3);

		c = r.createCell((short) 1);
		c.setCellStyle(getCs());
		c.setCellValue("Attribute");
		s.setColumnWidth((short) 1, (short) 5e3);

		c = r.createCell((short) 2);
		c.setCellStyle(getCs());
		c.setCellValue("definition");
		s.setColumnWidth((short) 2, (short) 10e3);

		c = r.createCell((short) 3);
		c.setCellStyle(getCs());
		c.setCellValue("links");
		s.setColumnWidth((short) 3, (short) 10e3);

		c = r.createCell((short) 4);
		c.setCellStyle(getCs());
		c.setCellValue("provenance");
		s.setColumnWidth((short) 4, (short) 5e3);

		// ____________________________________________
		// Run through UML model to generate base
		// for SME curation starting point.
		//
		Iterator<UMLclass> cIt = m.listClasses().values().iterator();
		CLASSLOOP: while (cIt.hasNext()) {
			UMLclass clz = cIt.next();

			Iterator<UMLattribute> aIt = clz.getAttributes().iterator();
			ATTLOOP: while (aIt.hasNext()) {
				UMLattribute att = aIt.next();

				rCount++;

				r = s.createRow(rCount);

				c = r.createCell((short) 1);
				c.setCellValue(att.getBaseName());

				r = s.createRow(rCount);
				c = r.createCell((short) 4);
				c.setCellValue(clz.getClassAddress() + "." + att.getBaseName());

			}

		}
	}

	public static Ontology buildOoEVV() {

		Ontology ooevv = new Ontology();

		ooevv.setShortName("OoEVV");
		ooevv.setDescription("A lightweight ontology for straightforward curation of variable "
				+ "definitions based on measurement scales.");
		ooevv.setDisplayName("OoEVV");
		ooevv.setFullName("Ontology of Experimental Variables and Values");

		return ooevv;

	}

}
