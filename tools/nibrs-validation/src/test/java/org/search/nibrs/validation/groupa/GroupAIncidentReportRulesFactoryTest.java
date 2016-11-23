/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.validation.groupa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.validation.rules.Rule;

public class GroupAIncidentReportRulesFactoryTest {
	
	private GroupAIncidentReportRulesFactory rulesFactory = new GroupAIncidentReportRulesFactory();
	
	@Test
	public void testRule101() {
		
		Rule<GroupAIncidentReport> rule101 = rulesFactory.getRule101("ori", "1");
		GroupAIncidentReport report = buildBaseReport();
		NIBRSError e = rule101.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._101, e.getNIBRSErrorCode());
		assertNull(e.getValue());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getSource(), e.getContext());
		assertEquals("1", e.getDataElementIdentifier());
		report.setOri("Not blank");
		e = rule101.apply(report);
		assertNull(e);
		
		rule101 = rulesFactory.getRule101("incidentNumber", "2");
		report = buildBaseReport();
		e = rule101.apply(report);
		assertNotNull(e);
		assertEquals("2", e.getDataElementIdentifier());
		report.setIncidentNumber("Not blank");
		e = rule101.apply(report);
		assertNull(e);
		
		rule101 = rulesFactory.getRule101("exceptionalClearanceCode", "4");
		report = buildBaseReport();
		report.setExceptionalClearanceCode("K");
		e = rule101.apply(report);
		assertNotNull(e);
		assertEquals("4", e.getDataElementIdentifier());
		report.setExceptionalClearanceCode("A");
		e = rule101.apply(report);
		assertNull(e);
		
	}
	
	@Test
	public void testRule104ForCargoTheftIndicator() {
		
		Rule<GroupAIncidentReport> rule104 = rulesFactory.getRule104("cargoTheftIndicator");
		GroupAIncidentReport report = buildBaseReport();
		
		report.setIncludesCargoTheft(false);
		report.setCargoTheftIndicator(null);
		NIBRSError e = rule104.apply(report);
		assertNull(e);
		
		report.setIncludesCargoTheft(true);
		report.setCargoTheftIndicator("Y");
		e = rule104.apply(report);
		assertNull(e);
		
		report.setCargoTheftIndicator("X");
		e = rule104.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._104, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getCargoTheftIndicator(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
		
		report.setCargoTheftIndicator(null);
		e = rule104.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._104, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getCargoTheftIndicator(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
		
	}
	
	@Test
	public void testRule104ForMonthOfTape() {
		Rule<GroupAIncidentReport> rule104 = rulesFactory.getRule104("monthOfTape");
		GroupAIncidentReport report = buildBaseReport();
		report.setMonthOfTape(3);
		NIBRSError e = rule104.apply(report);
		assertNull(e);
		report.setMonthOfTape(null);
		e = rule104.apply(report);
		assertNull(e);
		report.setMonthOfTape(15);
		e = rule104.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._104, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getMonthOfTape(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule104ForYearOfTape() {
		Rule<GroupAIncidentReport> rule104 = rulesFactory.getRule104("yearOfTape");
		GroupAIncidentReport report = buildBaseReport();
		report.setYearOfTape(2012);
		NIBRSError e = rule104.apply(report);
		assertNull(e);
		report.setYearOfTape(null);
		e = rule104.apply(report);
		assertNull(e);
		report.setYearOfTape(1925);
		e = rule104.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._104, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getYearOfTape(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule104ForReportDateIndicator() {
		Rule<GroupAIncidentReport> rule104 = rulesFactory.getRule104("reportDateIndicator");
		GroupAIncidentReport report = buildBaseReport();
		report.setReportDateIndicator("R");
		NIBRSError e = rule104.apply(report);
		assertNull(e);
		report.setReportDateIndicator(null);
		e = rule104.apply(report);
		assertNull(e);
		report.setReportDateIndicator("X");
		e = rule104.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._104, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getReportDateIndicator(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule115() {
		Rule<GroupAIncidentReport> rule115 = rulesFactory.getRule115();
		GroupAIncidentReport report = buildBaseReport();
		report.setIncidentNumber("");
		NIBRSError e = rule115.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._115, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getIncidentNumber(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
		report.setIncidentNumber("A");
		assertNotNull(rule115.apply(report));
		report.setIncidentNumber("A B         ");
		assertNotNull(rule115.apply(report));
		report.setIncidentNumber(" AB         ");
		assertNotNull(rule115.apply(report));
		report.setIncidentNumber("AB         ");
		assertNotNull(rule115.apply(report));
		report.setIncidentNumber("AB           ");
		assertNotNull(rule115.apply(report));
		report.setIncidentNumber("AB          ");
		assertNull(rule115.apply(report));
		report.setIncidentNumber("ABBBBBBBBBBB");
		assertNull(rule115.apply(report));
	}
	
	@Test
	public void testRule117() {
		Rule<GroupAIncidentReport> rule117 = rulesFactory.getRule117();
		GroupAIncidentReport report = buildBaseReport();
		report.setIncidentNumber("11-123-SC   ");
		NIBRSError e = rule117.apply(report);
		assertNull(e);
		report.setIncidentNumber("11+123*SC   ");
		e = rule117.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._117, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getIncidentNumber(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
		report.setIncidentNumber("11-123-SC");
		e = rule117.apply(report);
		assertNotNull(e);
	}
	
	@Test
	public void testRule119() {
		Rule<GroupAIncidentReport> rule119 = rulesFactory.getRule119();
		GroupAIncidentReport report = buildBaseReport();
		report.setCargoTheftIndicator(null);
		OffenseSegment o = new OffenseSegment();
		report.addOffense(o);
		o.setUcrOffenseCode(OffenseCode._35A.code);
		NIBRSError e = rule119.apply(report);
		assertNull(e);
		o = new OffenseSegment();
		report.addOffense(o);
		o.setUcrOffenseCode(OffenseCode._120.code);
		e = rule119.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._119, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getCargoTheftIndicator(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule152() {
		Rule<GroupAIncidentReport> rule152 = rulesFactory.getRule152();
		GroupAIncidentReport report = buildBaseReport();
		report.setIncidentHour(2);
		NIBRSError e = rule152.apply(report);
		assertNull(e);
		report.setIncidentHour(null);
		e = rule152.apply(report);
		assertNull(e);
		report.setIncidentHour(30);
		e = rule152.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._152, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getIncidentHour(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}

	@Test
	public void testRule170() {
		Rule<GroupAIncidentReport> rule170 = rulesFactory.getRule170();
		GroupAIncidentReport report = buildBaseReport();
		report.setYearOfTape(2015);
		report.setMonthOfTape(12);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		report.setIncidentDate(null);
		NIBRSError e = rule170.apply(report);
		assertNull(e);
		report.setIncidentDate(c.getTime());
		e = rule170.apply(report);
		assertNull(e);
		c.set(Calendar.YEAR, 2016);
		report.setIncidentDate(c.getTime());
		e = rule170.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._170, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getIncidentDate(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule171() {
		Rule<GroupAIncidentReport> rule171 = rulesFactory.getRule171();
		GroupAIncidentReport report = buildBaseReport();
		report.setYearOfTape(2015);
		report.setMonthOfTape(12);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, Calendar.MARCH);
		c.set(Calendar.DAY_OF_MONTH, 1);
		report.setIncidentDate(null);
		NIBRSError e = rule171.apply(report);
		assertNull(e);
		report.setIncidentDate(c.getTime());
		e = rule171.apply(report);
		assertNull(e);
		c.set(Calendar.YEAR, 2013);
		report.setIncidentDate(c.getTime());
		e = rule171.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._171, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getIncidentDate(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule172() {
		Rule<GroupAIncidentReport> rule172 = rulesFactory.getRule172();
		GroupAIncidentReport report = buildBaseReport();
		report.setYearOfTape(2015);
		report.setMonthOfTape(12);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, Calendar.MARCH);
		c.set(Calendar.DAY_OF_MONTH, 1);
		report.setIncidentDate(null);
		NIBRSError e = rule172.apply(report);
		assertNull(e);
		report.setIncidentDate(c.getTime());
		e = rule172.apply(report);
		assertNull(e);
		c.set(Calendar.YEAR, 1990);
		report.setIncidentDate(c.getTime());
		e = rule172.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._172, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getIncidentDate(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule153() {
		Rule<GroupAIncidentReport> rule153 = rulesFactory.getRule153();
		GroupAIncidentReport report = buildBaseReport();
		report.setExceptionalClearanceCode("N");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, Calendar.MARCH);
		c.set(Calendar.DAY_OF_MONTH, 1);
		report.setExceptionalClearanceDate(null);
		assertNull(rule153.apply(report));
		report.setExceptionalClearanceDate(c.getTime());
		NIBRSError e = rule153.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._153, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getExceptionalClearanceCode(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule155() {
		Rule<GroupAIncidentReport> rule155 = rulesFactory.getRule155();
		GroupAIncidentReport report = buildBaseReport();
		report.setExceptionalClearanceCode("A");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, Calendar.MARCH);
		c.set(Calendar.DAY_OF_MONTH, 1);
		report.setExceptionalClearanceDate(null);
		assertNull(rule155.apply(report));
		report.setExceptionalClearanceDate(c.getTime());
		report.setIncidentDate(null);
		assertNull(rule155.apply(report));
		c.set(Calendar.MONTH, Calendar.APRIL);
		report.setIncidentDate(c.getTime());
		NIBRSError e = rule155.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._155, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getExceptionalClearanceDate(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	@Test
	public void testRule156() {
		Rule<GroupAIncidentReport> rule156 = rulesFactory.getRule156();
		GroupAIncidentReport report = buildBaseReport();
		report.setExceptionalClearanceCode("A");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, Calendar.MARCH);
		c.set(Calendar.DAY_OF_MONTH, 1);
		report.setExceptionalClearanceDate(c.getTime());
		assertNull(rule156.apply(report));
		report.setExceptionalClearanceDate(null);
		NIBRSError e = rule156.apply(report);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._156, e.getNIBRSErrorCode());
		assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
		assertEquals(report.getExceptionalClearanceCode(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
	}
	
	private GroupAIncidentReport buildBaseReport() {
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		return report;
	}
	
}
