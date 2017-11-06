/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.stagingdata.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.repository.AdditionalJustifiableHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.AggravatedAssaultHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.ArresteeWasArmedWithTypeRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.search.nibrs.stagingdata.repository.DispositionOfArresteeUnder18TypeRepository;
import org.search.nibrs.stagingdata.repository.EthnicityOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.LocationTypeRepository;
import org.search.nibrs.stagingdata.repository.MethodOfEntryTypeRepository;
import org.search.nibrs.stagingdata.repository.MultipleArresteeSegmentsIndicatorTypeRepository;
import org.search.nibrs.stagingdata.repository.OffenderSuspectedOfUsingTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerActivityCircumstanceTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerAssignmentTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.PropertyDescriptionTypeRepository;
import org.search.nibrs.stagingdata.repository.RaceOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.ResidentStatusOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.search.nibrs.stagingdata.repository.SexOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SuspectedDrugTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeDrugMeasurementTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeInjuryTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfArrestTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfCriminalActivityTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfVictimTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfWeaponForceInvolvedTypeRepository;
import org.search.nibrs.stagingdata.repository.TypePropertyLossEtcTypeRepository;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeTypeRepository;
import org.search.nibrs.stagingdata.repository.VictimOffenderRelationshipTypeRepository;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.search.nibrs.stagingdata.repository.segment.OffenseSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class GroupAIncidentService {
	@Autowired
	AdministrativeSegmentRepository administrativeSegmentRepository;
	@Autowired
	OffenseSegmentRepository offenseSegmentRepository;
	@Autowired
	public DateTypeRepository dateTypeRepository; 
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public SegmentActionTypeRepository segmentActionTypeRepository; 
	@Autowired
	public ClearedExceptionallyTypeRepository clearedExceptionallyTypeRepository; 
	@Autowired
	public UcrOffenseCodeTypeRepository ucrOffenseCodeTypeRepository; 
	@Autowired
	public LocationTypeRepository locationTypeRepository; 
	@Autowired
	public MethodOfEntryTypeRepository methodOfEntryTypeRepository; 
	@Autowired
	public BiasMotivationTypeRepository biasMotivationTypeRepository; 
	@Autowired
	public TypeOfWeaponForceInvolvedTypeRepository typeOfWeaponForceInvolvedTypeRepository; 
	@Autowired
	public OffenderSuspectedOfUsingTypeRepository offenderSuspectedOfUsingTypeRepository; 
	@Autowired
	public TypeOfCriminalActivityTypeRepository typeOfCriminalActivityTypeRepository; 
	@Autowired
	public TypePropertyLossEtcTypeRepository typePropertyLossEtcTypeRepository; 
	@Autowired
	public TypeDrugMeasurementTypeRepository typeDrugMeasurementTypeRepository; 
	@Autowired
	public PropertyDescriptionTypeRepository propertyDescriptionTypeRepository; 
	@Autowired
	public SuspectedDrugTypeTypeRepository suspectedDrugTypeTypeRepository; 
	@Autowired
	public DispositionOfArresteeUnder18TypeRepository dispositionOfArresteeUnder18TypeRepository; 
	@Autowired
	public EthnicityOfPersonTypeRepository ethnicityOfPersonTypeRepository; 
	@Autowired
	public RaceOfPersonTypeRepository raceOfPersonTypeRepository; 
	@Autowired
	public SexOfPersonTypeRepository sexOfPersonTypeRepository; 
	@Autowired
	public TypeOfArrestTypeRepository typeOfArrestTypeRepository; 
	@Autowired
	public ResidentStatusOfPersonTypeRepository residentStatusOfPersonTypeRepository; 
	@Autowired
	public MultipleArresteeSegmentsIndicatorTypeRepository multipleArresteeSegmentsIndicatorTypeRepository; 
	@Autowired
	public ArresteeWasArmedWithTypeRepository arresteeWasArmedWithTypeRepository; 
	@Autowired
	public TypeOfVictimTypeRepository typeOfVictimTypeRepository; 
	@Autowired
	public OfficerActivityCircumstanceTypeRepository officerActivityCircumstanceTypeRepository; 
	@Autowired
	public OfficerAssignmentTypeTypeRepository officerAssignmentTypeTypeRepository; 
	@Autowired
	public AdditionalJustifiableHomicideCircumstancesTypeRepository additionalJustifiableHomicideCircumstancesTypeRepository; 
	@Autowired
	public TypeInjuryTypeRepository typeInjuryTypeRepository; 
	@Autowired
	public AggravatedAssaultHomicideCircumstancesTypeRepository aggravatedAssaultHomicideCircumstancesTypeRepository; 
	@Autowired
	public VictimOffenderRelationshipTypeRepository victimOffenderRelationshipTypeRepository; 
	
	SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");

	@Transactional
	public AdministrativeSegment saveAdministrativeSegment(AdministrativeSegment administrativeSegment){
		return administrativeSegmentRepository.save(administrativeSegment);
	}
	
	public AdministrativeSegment findAdministrativeSegment(Integer id){
		return administrativeSegmentRepository.findOne(id);
	}
	
	public OffenseSegment saveOffenseSegment(OffenseSegment offenseSegment){
		return offenseSegmentRepository.save(offenseSegment);
	}
	
	public Iterable<OffenseSegment> saveOffenseSegment(List<OffenseSegment> offenseSegments){
		return offenseSegmentRepository.save(offenseSegments);
	}
	
	public AdministrativeSegment saveGroupAIncidentReport(GroupAIncidentReport groupAIncidentReport){
		AdministrativeSegment administrativeSegment = new AdministrativeSegment(); 
		administrativeSegment.setAgency(agencyRepository.findFirstByAgencyOri(groupAIncidentReport.getOri()));
		
		String reportActionType = String.valueOf(groupAIncidentReport.getReportActionType());
		administrativeSegment.setSegmentActionType(segmentActionTypeRepository.findFirstBySegmentActionTypeCode(reportActionType));
		
		Optional<Integer> monthOfTape = Optional.ofNullable(groupAIncidentReport.getMonthOfTape());
		monthOfTape.ifPresent( m-> {
			administrativeSegment.setMonthOfTape(StringUtils.leftPad(String.valueOf(m), 2, '0'));
		});
		
		if (groupAIncidentReport.getYearOfTape() != null){
			administrativeSegment.setYearOfTape(String.valueOf(groupAIncidentReport.getYearOfTape()));
		}
		
		administrativeSegment.setCityIndicator(groupAIncidentReport.getCityIndicator());
		administrativeSegment.setOri(groupAIncidentReport.getOri());
		administrativeSegment.setIncidentNumber(groupAIncidentReport.getIncidentNumber());
		administrativeSegment.setIncidentDate(groupAIncidentReport.getIncidentDate().getValue());
		administrativeSegment.setIncidentDateType(getDateType(groupAIncidentReport.getIncidentDate().getValue()));
		administrativeSegment.setReportDateIndicator(groupAIncidentReport.getReportDateIndicator());
		administrativeSegment.setReportDateIndicator(groupAIncidentReport.getReportDateIndicator());
		
		administrativeSegment.setIncidentHour(StringUtils.EMPTY);
		Optional<Integer> incidentHour = Optional.ofNullable(groupAIncidentReport.getIncidentHour().getValue());
		incidentHour.ifPresent( value->administrativeSegment.setIncidentHour(String.valueOf(value)));
		
		ClearedExceptionallyType clearedExceptionallyType = 
				getCodeTableType(groupAIncidentReport.getExceptionalClearanceCode(), 
						clearedExceptionallyTypeRepository::findFirstByClearedExceptionallyCode, 
						ClearedExceptionallyType::new); 
		administrativeSegment.setClearedExceptionallyType(clearedExceptionallyType);
		
		Agency agency = getCodeTableType(groupAIncidentReport.getOri(), agencyRepository::findFirstByAgencyOri, Agency::new); 
		administrativeSegment.setAgency(agency);
		
		return this.saveAdministrativeSegment(administrativeSegment);
	}

	private DateType getDateType(Date date) {
		String dateString = null; 
		if (date != null){
			dateString = formatter.format(date);
		}
		
		DateType dateType = getCodeTableType( dateString, 
				dateTypeRepository::findFirstByDateMMDDYYYY, 
				DateType::new);  
		return dateType;
	}
	
	public <R> R getCodeTableType(String code,  Function<String, R> findByCodeFunction, Function<Integer, R> constructorFunction ) {
		R r = null;
		if (StringUtils.isNotBlank(code)){
			r = findByCodeFunction.apply(code);
		}
		
		if (r == null){
			r = constructorFunction.apply(99998);
		}
		return r;
	}
	
}
