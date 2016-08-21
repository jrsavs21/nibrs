package org.search.nibrs.validation.groupa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.RuleViolationExemplarFactory;

public class GroupAIncidentReportValidatorTest {
		
	private GroupAIncidentReportValidator validator;
	private RuleViolationExemplarFactory exemplarFactory;
	
	@Before
	public void init() {
		validator = new GroupAIncidentReportValidator();
		exemplarFactory = RuleViolationExemplarFactory.getInstance();
	}
	
	@Test
	public void testRule115() {
		List<GroupAIncidentReport> exemplars = exemplarFactory.getGroupAIncidentsThatViolateRule(115);
		for (GroupAIncidentReport r : exemplars) {
			List<NIBRSError> errorList = validator.validate(r);
			assertEquals(1, errorList.size());
			NIBRSError e = errorList.get(0);
			assertEquals(NIBRSErrorCode._115, e.getNIBRSErrorCode());
		}
	}
		
	@Test
	public void _101_adminMandatoryFieldTest(){
									
		List<GroupAIncidentReport> groupAIncidentList = exemplarFactory.getGroupAIncidentsThatViolateRule(101);
		
		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
		
			NIBRSError _101Error = validator._101_adminMandatoryField(groupAIncidentReport, nibrsErrorList);
			
			Assert.assertNotNull(_101Error);
			
			Assert.assertEquals(NIBRSErrorCode._101, _101Error.getNIBRSErrorCode());
		}
		
		Assert.assertEquals(7, nibrsErrorList.size());
	}
	
	@Test
	public void _201_offenseRequiredFieldTest(){
		
		List<GroupAIncidentReport> groupAIncidentList = exemplarFactory.getGroupAIncidentsThatViolateRule(201);
		
		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
				
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _201Error = validator._201_offenseRequiredField(groupAIncidentReport,
					nibrsErrorList);
							
			Assert.assertNotNull(_201Error);
			
			Assert.assertEquals(NIBRSErrorCode._201, _201Error.getNIBRSErrorCode());						
		}	
		
		Assert.assertEquals(9, nibrsErrorList.size());		
	}
	
	//TODO enable when passing. Data scenario could me more concise to construct 
	// only the four null "header" fields being tested
	@Ignore
	public void _301_propertySegmentRequiredField(){
		
		List<GroupAIncidentReport> groupAIncidentList = exemplarFactory.getGroupAIncidentsThatViolateRule(301);

		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _301Error = validator._301_propertySegmentRequiredField(groupAIncidentReport,
					nibrsErrorList);
							
			Assert.assertNotNull(_301Error);
			
			Assert.assertEquals(NIBRSErrorCode._301, _301Error.getNIBRSErrorCode());						
		}	
		
		Assert.assertEquals(4, nibrsErrorList.size());			
	}
	
	//TODO enable when passing
	@Ignore
	public void _401_propertySegmentRequiredField(){
		
		List<GroupAIncidentReport> groupAIncidentList = exemplarFactory.getGroupAIncidentsThatViolateRule(401);

		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _401Error = validator._401_victimSegmentRequiredField(groupAIncidentReport, 
					nibrsErrorList);
							
			Assert.assertNotNull(_401Error);
			
			Assert.assertEquals(NIBRSErrorCode._401, _401Error.getNIBRSErrorCode());						
		}	
		
		Assert.assertEquals(7, nibrsErrorList.size());			
	}	
	
	//TODO enable when passing
	@Ignore
	public void _501_propertySegmentRequiredField(){
		
		List<GroupAIncidentReport> groupAIncidentList = exemplarFactory.getGroupAIncidentsThatViolateRule(501);

		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _501Error = validator._501_offenderSegmentRequiredField(groupAIncidentReport, 
					nibrsErrorList);
							
			Assert.assertNotNull(_501Error);
			
			Assert.assertEquals(NIBRSErrorCode._051, _501Error.getNIBRSErrorCode());						
		}	
		
		Assert.assertEquals(5, nibrsErrorList.size());			
	}		
	
}

