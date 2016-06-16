package org.search.nibrs.flatfile.importer;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.util.*;
import org.search.nibrs.model.*;

/**
 * Builder class that constructs incidents from a stream of NIBRS report data.
 * Incidents are broadcast to listeners as events; this keeps the class as
 * memory-unintensive as possible (NIBRS report streams can be rather large).
 * <br/>At some point, if other report elements than Incidents are desired, this
 * will need to be modified. Currently, it only broadcasts Incident "add"
 * records.
 *  
 */
public class IncidentBuilder
{
	
	private static final class LogListener implements IncidentListener {
		public int incidentCount = 0;
		public void newIncident(Incident newIncident) {
			LOG.info("Created incident # " + newIncident.getIncidentNumber());
			incidentCount++;
		}
		public void handleThrowable(Throwable t, String currentIncidentNumber) {
		}
	}

	private static final Logger LOG = LogManager.getLogger(IncidentBuilder.class);

    private List<IncidentListener> listeners;
    private LogListener logListener = new LogListener();

    public IncidentBuilder()
    {
        listeners = new ArrayList<IncidentListener>();
        listeners.add(logListener);
    }

    public void addIncidentListener(IncidentListener listener)
    {
        listeners.add(listener);
    }

    public void removeIncidentListener(IncidentListener listener)
    {
        listeners.remove(listener);
    }

    public List<NIBRSError> buildIncidents(Reader reader) throws IOException
    {
    	
    	List<NIBRSError> errorList = new ArrayList<NIBRSError>();
    	
        BufferedReader br = null;
        if (!(reader instanceof BufferedReader))
        {
            br = new BufferedReader(reader);
        }
        else
        {
            br = (BufferedReader) reader;
        }
        String line = null;
        Incident currentIncident = null;
        String currentIncidentNumber = null;
        Throwable currentThrowable = null;
        int lineNumber = 1;
        LOG.info("Processing NIBRS flat file");
        while ((line = br.readLine()) != null)
        {
            try
            {
                Segment s = new Segment();
                List<NIBRSError> segmentErrors = s.setData(lineNumber, line);
				errorList.addAll(segmentErrors);
                if (segmentErrors.isEmpty() && isIncluded(s))
                {
                    if (currentIncidentNumber == null || !currentIncidentNumber.equals(s.getIncidentNumber()))
                    {
                        if (currentThrowable == null)
                        {
                            handleNewIncident(currentIncident);
                        }
                        else
                        {
                            handleThrowable(s.getIncidentNumber(), currentThrowable);
                            currentThrowable = null;
                        }
                        currentIncidentNumber = s.getIncidentNumber();
                        currentIncident = buildIncidentSegment(s, errorList);
                    }
                    else
                    {
                        if (currentThrowable == null)
                        {
                            addSegmentToIncident(currentIncident, s, errorList);
                        }
                    }
                }
            }
            catch (Throwable t)
            {
                currentThrowable = t;
            }
            lineNumber++;
        }
        if (currentThrowable == null)
        {
            handleNewIncident(currentIncident);
        }
        else
        {
            handleThrowable(currentIncidentNumber, currentThrowable);
        }
        
        LOG.info("finished processing file, read " + (lineNumber-1) + " lines.");
        LOG.info("Encountered " + errorList.size() + " error(s).");
        LOG.info("Created " + logListener.incidentCount + " incident(s).");
        
        return errorList;
        
    }

    private void handleThrowable(String currentIncidentNumber, Throwable t)
    {
        for (Iterator<IncidentListener> it = listeners.iterator(); it.hasNext();)
        {
            IncidentListener listener = it.next();
            listener.handleThrowable(t, currentIncidentNumber);
        }
    }

    private final void handleNewIncident(Incident newIncident)
    {
        if (newIncident != null)
        {
            for (Iterator<IncidentListener> it = listeners.iterator(); it.hasNext();)
            {
                IncidentListener listener = it.next();
                listener.newIncident(newIncident);
            }
        }
    }

    private final Incident buildIncidentSegment(Segment s, List<NIBRSError> errorList)
    {
        Incident newIncident = new Incident();
        newIncident.setIncidentNumber(s.getIncidentNumber());
        newIncident.setOri(s.getOri());
        String segmentData = s.getData();
        newIncident.setMonthOfTape(getIntValueFromSegment(s, 7, 8, errorList, "Month of Submission must be a number"));
    	newIncident.setYearOfTape(getIntValueFromSegment(s, 9, 12, errorList, "Year of Submission must be a number"));
        newIncident.setCityIndicator(StringUtils.getStringBetween(13, 16, segmentData));
        int incidentYear = getIntValueFromSegment(s, 38, 41, errorList, "Incident Year must be a number");
        int incidentMonthOrig = getIntValueFromSegment(s, 42, 43, errorList, "Incident Month must be a number");
        int incidentMonth = DateUtils.convertMonthValue(incidentMonthOrig);
        int incidentDay = getIntValueFromSegment(s, 44, 45, errorList, "Incident Day must be a number");
        newIncident.setIncidentDate(DateUtils.makeDate(incidentYear, incidentMonth, incidentDay));
        newIncident.setReportDateIndicator(StringUtils.getStringBetween(46, 46, segmentData));
        String hourString = StringUtils.getStringBetween(47, 48, segmentData);
        if (hourString != null)
        {
            newIncident.setIncidentHour(new Integer(hourString));
        }
        newIncident.setExceptionalClearanceCode(StringUtils.getStringBetween(49, 49, segmentData));
        String clearanceYearString = StringUtils.getStringBetween(50, 53, segmentData);
        if (clearanceYearString != null)
        {
            int clearanceYear = getIntValueFromSegment(s, 50, 53, errorList, "Clearance Year must be a number");
            int clearanceMonthOrig = getIntValueFromSegment(s, 54, 55, errorList, "Clearance Month must be a number");
            int clearanceMonth = DateUtils.convertMonthValue(clearanceMonthOrig);
            int clearanceDay = getIntValueFromSegment(s, 56, 57, errorList, "Clearance Day must be a number");
            newIncident.setExceptionalClearanceDate(DateUtils.makeDate(clearanceYear, clearanceMonth, clearanceDay));
        }
        return newIncident;
    }

	private Integer getIntValueFromSegment(Segment s, int startPos, int endPos, List<NIBRSError> errorList, String errorMessage) {
		String sv = StringUtils.getStringBetween(startPos, endPos, s.getData());
        Integer i = null;
        try {
			i = new Integer(sv);
        } catch (NumberFormatException nfe) {
        	NIBRSError e = new NIBRSError();
        	e.setContext(s.getLineNumber());
        	e.setIncidentNumber(s.getIncidentNumber());
			e.setRuleDescription(errorMessage);
        	e.setValue(sv);
        	e.setSegmentType(s.getSegmentType());
        	errorList.add(e);
        	LOG.debug("Error in int conversion: lineNumber=" + s.getLineNumber() + ", value=" + sv);
        }
		return i;
	}

    private final void addSegmentToIncident(Incident currentIncident, Segment s, List<NIBRSError> errorList)
    {
        char segmentType = s.getSegmentType();
        switch (segmentType)
        {
        case '2':
            currentIncident.addOffense(buildOffenseSegment(s, errorList));
            break;
        case '3':
            currentIncident.addProperty(buildPropertySegment(s, errorList));
            break;
        case '4':
            currentIncident.addVictim(buildVictimSegment(s, errorList));
            break;
        case '5':
            currentIncident.addOffender(buildOffenderSegment(s, errorList));
            break;
        case '6':
            currentIncident.addArrestee(buildArresteeSegment(s, errorList));
            break;
        default:
            NIBRSError error = new NIBRSError();
            error.setContext(s.getLineNumber());
            error.setIncidentNumber(s.getIncidentNumber());
            error.setRuleNumber(51);
            error.setRuleDescription("Segment Level must contain data values 0–7.");
            error.setValue(segmentType);
            errorList.add(error);
        }
    }

    private Arrestee buildArresteeSegment(Segment s, List<NIBRSError> errorList)
    {
        Arrestee newArrestee = new Arrestee();
        String segmentData = s.getData();
        newArrestee.setArresteeSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
        newArrestee.setArrestTransactionNumber(StringUtils.getStringBetween(40, 51, segmentData));
        newArrestee.setArrestDate(StringUtils.getDateBetween(52, 59, segmentData));
        newArrestee.setTypeOfArrest(StringUtils.getStringBetween(60, 60, segmentData));
        newArrestee.setMultipleArresteeSegmentsIndicator(StringUtils.getStringBetween(61, 61, segmentData));
        newArrestee.setUcrArrestOffenseCode(StringUtils.getStringBetween(62, 64, segmentData));
        for (int i=0;i < 2;i++)
        {
            newArrestee.setArresteeArmedWith(i, StringUtils.getStringBetween(65 + 3*i, 66 + 3*i, segmentData));
            newArrestee.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(67 + 3*i, 67 + 3*i, segmentData));
        }
        newArrestee.setAgeOfArresteeString(StringUtils.getStringBetween(71, 74, segmentData));
        newArrestee.setSexOfArrestee(StringUtils.getStringBetween(75, 75, segmentData));
        newArrestee.setRaceOfArrestee(StringUtils.getStringBetween(76, 76, segmentData));
        newArrestee.setEthnicityOfArrestee(StringUtils.getStringBetween(77, 77, segmentData));
        newArrestee.setResidentStatusOfArrestee(StringUtils.getStringBetween(78, 78, segmentData));
        newArrestee.setDispositionOfArresteeUnder18(StringUtils.getStringBetween(79, 79, segmentData));
        return newArrestee;
    }

    private Offender buildOffenderSegment(Segment s, List<NIBRSError> errorList)
    {
        Offender newOffender = new Offender();
        String segmentData = s.getData();
        newOffender.setOffenderSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
        newOffender.setAgeOfOffenderString(StringUtils.getStringBetween(40, 43, segmentData));
        newOffender.setSexOfOffender(StringUtils.getStringBetween(44, 44, segmentData));
        newOffender.setRaceOfOffender(StringUtils.getStringBetween(45, 45, segmentData));
        newOffender.setEthnicityOfOffender(StringUtils.getStringBetween(46, 46, segmentData));
        return newOffender;
    }

    private Victim buildVictimSegment(Segment s, List<NIBRSError> errorList)
    {
        
        Victim newVictim = new Victim();
        
        String segmentData = s.getData();
        
        newVictim.setVictimSequenceNumber(StringUtils.getIntegerBetween(38, 40, segmentData));
        
        for (int i=0;i < 10;i++)
        {
            newVictim.setUcrOffenseCodeConnection(i, StringUtils.getStringBetween(41 + 3*i, 43 + 3*i, segmentData));
            newVictim.setOffenderNumberRelated(i, StringUtils.getIntegerBetween(90 + 4*i, 91 + 4*i, segmentData));
            newVictim.setVictimOffenderRelationship(i, StringUtils.getStringBetween(92 + 4*i, 93 + 4*i, segmentData));
        }
        
        newVictim.setTypeOfVictim(StringUtils.getStringBetween(71, 71, segmentData));
        newVictim.setAgeOfVictimString(StringUtils.getStringBetween(72, 75, segmentData));
        newVictim.setSexOfVictim(StringUtils.getStringBetween(76, 76, segmentData));
        newVictim.setRaceOfVictim(StringUtils.getStringBetween(77, 77, segmentData));
        newVictim.setEthnicityOfVictim(StringUtils.getStringBetween(78, 78, segmentData));
        newVictim.setResidentStatusOfVictim(StringUtils.getStringBetween(79, 79, segmentData));
        newVictim.setAggravatedAssaultHomicideCircumstances(0, StringUtils.getStringBetween(80, 81, segmentData));
        newVictim.setAggravatedAssaultHomicideCircumstances(1, StringUtils.getStringBetween(82, 83, segmentData));
        newVictim.setAdditionalJustifiableHomicideCircumstances(StringUtils.getStringBetween(84, 84, segmentData));
        
        for (int i=0;i < 5;i++)
        {
            newVictim.setTypeOfInjury(i, StringUtils.getStringBetween(85 + i, 85 + i, segmentData));
        }
        
        newVictim.setTypeOfOfficerActivityCircumstance(StringUtils.getStringBetween(130, 131, segmentData));
        newVictim.setOfficerAssignmentType(StringUtils.getStringBetween(132, 132, segmentData));
        newVictim.setOfficerOtherJurisdictionORI(StringUtils.getStringBetween(133, 141, segmentData));
        
        return newVictim;
        
    }

    private Property buildPropertySegment(Segment s, List<NIBRSError> errorList)
    {

        Property newProperty = new Property();

        String segmentData = s.getData();

        newProperty.setTypeOfPropertyLoss(StringUtils.getStringBetween(38, 38, segmentData));
        
        for (int i=0;i < 10;i++)
        {
            newProperty.setPropertyDescription(i, StringUtils.getStringBetween(39 + 19*i, 40 + 19*i, segmentData));
            newProperty.setValueOfProperty(i, StringUtils.getIntegerBetween(41 + 19*i, 49 + 19*i, segmentData));
            newProperty.setDateRecovered(i, StringUtils.getDateBetween(50 + 19*i, 57 + 19*i, segmentData));
        }
        
        newProperty.setNumberOfStolenMotorVehicles(StringUtils.getIntegerBetween(229, 230, segmentData));
        newProperty.setNumberOfRecoveredMotorVehicles(StringUtils.getIntegerBetween(231, 232, segmentData));
        
        for (int i=0;i < 3;i++)
        {
            newProperty.setSuspectedDrugType(i, StringUtils.getStringBetween(233 + 15*i, 233 + 15*i, segmentData));
            String drugQuantityWholePartString = StringUtils.getStringBetween(234 + 15*i, 242 + 15*i, segmentData);
            String drugQuantityFractionalPartString = StringUtils.getStringBetween(243 + 15*i, 245 + 15*i, segmentData);
            if (drugQuantityWholePartString != null)
            {
                String fractionalValueString = "000";
                if (drugQuantityFractionalPartString != null)
                {
                    fractionalValueString = drugQuantityFractionalPartString;
                }
                String drugQuantityFullValueString = drugQuantityWholePartString + "." + fractionalValueString;
                newProperty.setEstimatedDrugQuantity(i, new Double(drugQuantityFullValueString));
            }
            newProperty.setTypeDrugMeasurement(i, StringUtils.getStringBetween(246 + 15*i, 247 + 15*i, segmentData));
        }

        return newProperty;

    }

    private Offense buildOffenseSegment(Segment s, List<NIBRSError> errorList)
    {

        Offense newOffense = new Offense();

        String segmentData = s.getData();

        newOffense.setUcrOffenseCode(StringUtils.getStringBetween(38, 40, segmentData));
        newOffense.setOffenseAttemptedCompleted(StringUtils.getStringBetween(41, 41, segmentData));
        newOffense.setLocationType(StringUtils.getStringBetween(45, 46, segmentData));
        newOffense.setNumberOfPremisesEntered(StringUtils.getIntegerBetween(47, 48, segmentData));
        newOffense.setMethodOfEntry(StringUtils.getStringBetween(49, 49, segmentData));
        
        for (int i=0; i < 5; i++) {
            newOffense.setBiasMotivation(i, StringUtils.getStringBetween(62+i, 63+i, segmentData));
        }
        
        for (int i = 0; i < 3; i++)
        {
            newOffense.setOffendersSuspectedOfUsing(i, StringUtils.getStringBetween(42 + i, 42 + i, segmentData));
            newOffense.setTypeOfCriminalActivity(i, StringUtils.getStringBetween(50 + i, 50 + i, segmentData));
            newOffense.setTypeOfWeaponForceInvolved(i, StringUtils.getStringBetween(53 + 3 * i, 54 + 3 * i, segmentData));
            newOffense.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(55 + 3 * i, 55 + 3 * i, segmentData));
        }

        return newOffense;

    }

    private final boolean isIncluded(Segment s)
    {
        // currently, we exclude all the segment types except for incident adds.
        return s.getActionType() == 'I';
    }

}