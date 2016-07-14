package org.search.nibrs.validation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.Offense;

/**
 * Class that manages a set of "edits" to baseline incidents.  These edits create "exemplars" of NIBRS rules violations that can be used to
 * unit test the validation logic in the precert tool.
 *
 */
public class RuleViolationExemplarFactory {
	
	private static final RuleViolationExemplarFactory INSTANCE = new RuleViolationExemplarFactory();
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(RuleViolationExemplarFactory.class);
	
	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;
	
	private RuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}
	
	/**
	 * Get an instance of the factory.
	 * @return the instance
	 */
	public static final RuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Get an Incident that violates the specified rule.  For rules and their numbers, reference the NIBRS Technical Specification, Section 5.
	 * @param ruleNumber the rule number
	 * @return an incident that exemplifies violation of the rule
	 */
	public List<GroupAIncidentReport> getGroupAIncidentsThatViolateRule(Integer ruleNumber) {
		return groupATweakerMap.get(ruleNumber).apply(BaselineIncidentFactory.getBaselineIncident());
	}
	
	private void populateGroupAExemplarMap() {

		groupATweakerMap.put(59, incident -> {
			/*First two positions must be the code of the state (e.g., SC, MD) in which the incident occurred.
			 For non-federal participants, every record must have the same code.
			*/
			GroupAIncidentReport ret = incident.deepCopy();
			ret.setOri("ZZ123456789");
			return Collections.singletonList(ret);
		});
		
		groupATweakerMap.put(101, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 1 is mandatory & must be present.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri(null);
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
			
								
		});
		
		
		groupATweakerMap.put(104, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 1 must be valid.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(1054);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(14);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri("WA1234");
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber("12345");
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(Date.from(LocalDateTime.of(1054, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode("X");
			GroupAIncidentReport copy7 = copy.deepCopy();
			copy7.setCityIndicator("ZZ12");
			//ReportDateIndicator should be set to "R" if unknown.
			GroupAIncidentReport copy8 = copy.deepCopy();
			copy8.setIncidentDate(null);
			copy8.setReportDateIndicator("S");
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			return incidents;
			
			});
		groupATweakerMap.put(105, incident -> {
			/*The data element in error contains a date that is not entered correctly.
			Each component of the date must be valid; that is, months must be 01 through 12,
			days must be 01 through 31, and year must include the century (i.e., 19xx, 20xx).
			In addition, days cannot exceed maximum for the month (e.g., June cannot have 31days).
			Also, the date cannot exceed the current date.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(0120);
			copy.setMonthOfTape(5);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy.setYearOfTape(2016);
			copy.setMonthOfTape(13);
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		
		groupATweakerMap.put(115, incident -> {
			/*(Incident Number) Must be blank right-fill if under 12 characters in length. 
			Cannot have embedded blanks between the first and last characters entered.
			*/
			GroupAIncidentReport ret = incident.deepCopy();
			ret.setIncidentNumber("1234 5678");
			return Collections.singletonList(ret);
		});
		
		groupATweakerMap.put(116, incident -> {
			/*(Incident Number) must be left-justified with blank right-fill.
			Since the number is less than 12 characters, it must begin in position 1.
			 */
			GroupAIncidentReport ret = incident.deepCopy();
			ret.setIncidentNumber(" 12345678");
			return Collections.singletonList(ret);
		});
		
		groupATweakerMap.put(117, incident -> {
			/*(Incident Number) can only have character combinations of A through Z, 0 through 9,
			hyphens, and/or blanks. For example, 89-123-SC is valid, but 89+123*SC is invalid.
			 */
			GroupAIncidentReport ret = incident.deepCopy();
			ret.setIncidentNumber("89+123*SC");
			return Collections.singletonList(ret);
		});
		
		groupATweakerMap.put(119, incident -> {
			/*Data Element 2A (Cargo Theft) must be populated with a valid data value when 
			Data Element 6 (UCR Offense Code) contains a Cargo Theft-related offense.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setCargoTheftIndicator(true);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		groupATweakerMap.put(151, incident -> {
			/*This field must be blank if the incident date is known. If the incident date is unknown,
		 	then the report date would be entered instead and must be indicated with an �R� in the Report
		 	Indicator field within the Administrative Segment. The "R" in this case is invalid.
			*/
			GroupAIncidentReport ret = incident.deepCopy();
			ret.setReportDateIndicator("R");
			return Collections.singletonList(ret);
		});
		
		
		groupATweakerMap.put(152, incident -> {
			/*If Hour is entered within Data Element 3 (Incident Date/Hour), it must be 00 through 23. 
			If 00=Midnight is entered, be careful that the Incident Date is entered as if the time was
			1 minute past midnight.
			*/
			GroupAIncidentReport ret = incident.deepCopy();
			ret.setIncidentDate(Date.from(LocalDateTime.of(2016, 5, 12, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			return Collections.singletonList(ret);
			});
			
			
		groupATweakerMap.put(201, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 2 is mandatory & must be present.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri(null);
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode(null);
			GroupAIncidentReport copy7 = copy.deepCopy();
			Offense UcrOffense = new Offense();
			UcrOffense.setUcrOffenseCode(null);
			GroupAIncidentReport copy8 = copy.deepCopy();
			Offense AttemptedOffense = new Offense();
			AttemptedOffense.setOffenseAttemptedCompleted(null);
			GroupAIncidentReport copy9 = copy.deepCopy();
			Offense BiasOffense = new Offense();
			BiasOffense.setBiasMotivation(0, null);
			GroupAIncidentReport copy10 = copy.deepCopy();
			Offense OffenderSuspectedOfUsing = new Offense();
			OffenderSuspectedOfUsing.setOffendersSuspectedOfUsing(0, null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			return incidents;
		});
		
		groupATweakerMap.put(204, incident -> {
			/*The referenced data element in a Group A Incident Report Segment 2 must 
			be populated with a valid data value.
			*/
			//ORI first 2 characters need to be valid state code
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setOri("1234567890123");
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setOri("ZZ123456789");
			GroupAIncidentReport copy3 = copy.deepCopy();
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("XXX");
			GroupAIncidentReport copy4 = copy.deepCopy();
			Offense BiasOffense = new Offense();
			BiasOffense.setBiasMotivation(0, "10");
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			return incidents;
		});
		
		groupATweakerMap.put(206, incident -> {
			/* The referenced data element in error is one that contains multiple
			 * data values. When more than one code is entered, none can be duplicate codes.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense firstOffendersSuspectedOfUsing = new Offense();
			firstOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "A");
			Offense secondOffendersSuspectedOfUsing = new Offense();
			secondOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "C");
			Offense thirdOffendersSuspectedOfUsing = new Offense();
			thirdOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "C");
			GroupAIncidentReport copy2 = incident.deepCopy();
			Offense firstBiasMotivationOffense = new Offense();
			firstBiasMotivationOffense.setBiasMotivation(0,"15");
			Offense secondBiasMotivationOffense = new Offense ();
			secondBiasMotivationOffense.setBiasMotivation(0, "26");
			Offense thirdBiasMotivationOffense = new Offense ();
			thirdBiasMotivationOffense.setBiasMotivation (0, "26");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
			
		});
		
			groupATweakerMap.put(207, incident -> {
			/* The referenced data element in error is one that contains multiple
			 * data values. However "N" is mutually exclusive with other codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense firstOffendersSuspectedOfUsing = new Offense();
			firstOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "A");
			Offense secondOffendersSuspectedOfUsing = new Offense();
			secondOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "N");
			incidents.add(copy);
			return incidents;
			
		});
				
		groupATweakerMap.put(251, incident -> {
			/*(Offense Attempted/Completed) Must be a valid code of A=Attempted or C=Completed.
			*/
			GroupAIncidentReport ret = incident.deepCopy();
			ret.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			return Collections.singletonList(ret);
		});
		
		groupATweakerMap.put(256, incident -> {
			/*Offense Attempted/Completed, Data Element 7, must be a valid code of A=Attempted or C=Completed if UCR code is Homicide
			 Assault.
			*/
			GroupAIncidentReport ret = incident.deepCopy();
			ret.getOffenses().get(0).setUcrOffenseCode("09A");
			ret.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			return Collections.singletonList(ret);
		});
		
			
			
		groupATweakerMap.put(262, incident -> {
			/*When a Group �A� Incident Report is submitted, the individual segments
		 	comprising the incident cannot contain duplicates. 
		 	In this case, two Offense Segments were submitted having the same
		 	offense in Data Element 6 (UCR Offense Code).
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			copy.addOffense(secondOffense);
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(263, incident -> {
			/*Can be submitted only 10 times for each Group A Incident Report; 
			10 offense codes are allowed for each incident.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("13A");
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			Offense thirdOffense = new Offense();
			thirdOffense.setUcrOffenseCode("13C");
			Offense fourthOffense = new Offense();
			fourthOffense.setUcrOffenseCode("13D");
			Offense fifthOffense = new Offense();
			fifthOffense.setUcrOffenseCode("13E");
			Offense sixthOffense = new Offense();
			sixthOffense.setUcrOffenseCode("13F");
			Offense seventhOffense = new Offense();
			seventhOffense.setUcrOffenseCode("13G");
			Offense eighthOffense = new Offense();
			eighthOffense.setUcrOffenseCode("13H");
			Offense ninthOffense = new Offense();
			ninthOffense.setUcrOffenseCode("13I");
			Offense tenthOffense = new Offense();
			tenthOffense.setUcrOffenseCode("13J");
			Offense eleventhOffense = new Offense();
			eleventhOffense.setUcrOffenseCode("13K");
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(264, incident -> {
			/*Group A Offense code cannot contain a Group B Offense
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("90A");
			incidents.add(copy);
			return incidents;
		});
		
		
		
		groupATweakerMap.put(266, incident -> {
			/*When a Justifiable Homicide is reported, no other offense may be
			reported in the Group �A� Incident Report. These should be submitted on another
			Group �A� Incident Report.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense JustifiableHomicideOffense = new Offense();
			JustifiableHomicideOffense.setUcrOffenseCode("09C");
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			incidents.add(copy);
			return incidents;
			
		});
				
		
		groupATweakerMap.put(267, incident -> {
			/*If a homicide offense is submitted, Data Element 13 (Type Weapon/Force Involved)
			cannot have 99=None. Some type of weapon/force must be used in a homicide offense.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			Offense HomicideOffense = new Offense();
			HomicideOffense.setUcrOffenseCode("09A");
			HomicideOffense.setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy2 = incident.deepCopy();
			Offense HomicideOffense2 = new Offense();
			HomicideOffense2.setUcrOffenseCode("09A");
			HomicideOffense2.setTypeOfWeaponForceInvolved(0, "99");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
			
		});
		
		
		
		groupATweakerMap.put(301, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 3 is mandatory & must be present.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri(null);
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});
		
		groupATweakerMap.put(401, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 4 is mandatory & must be present.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri(null);
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});
		
		groupATweakerMap.put(501, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 5 is mandatory & must be present.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri(null);
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});
		
		groupATweakerMap.put(601, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 6 is mandatory & must be present.
			*/
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = incident.deepCopy();
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = copy.deepCopy();
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = copy.deepCopy();
			copy3.setOri(null);
			GroupAIncidentReport copy4 = copy.deepCopy();
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = copy.deepCopy();
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = copy.deepCopy();
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});
		
			
		
		
		
		
		
		
		
		
	}

}
