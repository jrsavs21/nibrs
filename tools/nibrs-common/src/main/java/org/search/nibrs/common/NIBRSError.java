/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
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
package org.search.nibrs.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A class for objects that represent an error encountered in processing NIBRS data.
 *
 */
public class NIBRSError {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(NIBRSError.class);
	
	private AbstractReport report;
	private ReportSource context;
	private String reportUniqueIdentifier;
	private Object value;
	private char segmentType;
	private Object withinSegmentIdentifier;
	private String dataElementIdentifier;
	private NIBRSErrorCode nibrsErrorCode;
	private boolean warning;
	private boolean crossSegment;
	
	public NIBRSError() {
		warning = false;
		crossSegment = false;
	}
	
	public NIBRSError(NIBRSError e) {
		this.context = e.context;
		this.reportUniqueIdentifier = e.reportUniqueIdentifier;
		this.value = e.value;
		this.segmentType = e.segmentType;
		this.withinSegmentIdentifier = e.withinSegmentIdentifier;
		this.nibrsErrorCode = e.nibrsErrorCode;
		this.dataElementIdentifier = e.dataElementIdentifier;
		this.warning = e.warning;
		this.report = e.report;
		this.crossSegment = e.crossSegment;
	}
	
	@Override
	public String toString() {
		return "NIBRSError [context=" + context + ", ruleNumber=" + getRuleNumber() + ", ruleDescription=" + getShortenedRuleDescription() + ", reportUniqueIdentifier=" + reportUniqueIdentifier +
				", value=" + (value != null && value.getClass().isArray() ? Arrays.toString((Object[]) value) : value)
				+ ", segmentType=" + segmentType + ", withinSegmentIdentifier=" + withinSegmentIdentifier + ", dataElementIdentifier=" + dataElementIdentifier + ", nibrsErrorCode=" + nibrsErrorCode
				+ ", warning=" + warning + ", crossSegment=" + crossSegment + "]";
	}

	/**
	 * Get a shortened version of the error description, suitable for display in constrained UIs, etc.
	 * @return the first fifteen characters of the rule description, followed by an ellipsis
	 */
	public String getShortenedRuleDescription() {
		String ruleDescription = getRuleDescription();
		String ret = ruleDescription;
		if (ret != null && ret.length() > 15) {
			StringBuffer sb = new StringBuffer(ret.substring(0, 15));
			ret = sb.append("...").toString();
		}
		return ret;
	}

	/**
	 * The text of the rule, as provided by the FBI.
	 * @return the rule text
	 */
	public String getRuleDescription() {
		return nibrsErrorCode.description;
	}
	
	/**
	 * Get the report object (Group A Incident or Group B Arrest) with which this error is associated
	 * @return the report
	 */
	public AbstractReport getReport() {
		return report;
	}
	public void setReport(AbstractReport report) {
		this.report = report;
	}

	/**
	 * An object representing the "context" of where the error occurred.  The intent of this property is to help a human user find
	 * the offending record/data in question so they can pursue a correction.
	 * @return the context object
	 */
	public ReportSource getContext() {
		return context;
	}
	public void setContext(ReportSource context) {
		this.context = context;
	}
	/**
	 * The rule number from the FBI NIBRS specification.
	 * @return the rule number
	 */
	public String getRuleNumber() {
		return nibrsErrorCode.code;
	}
	/**
	 * A value that uniquely identifies the Report in which the error occurred.  For example, this will be the incident number
	 * for Group As and the ATN for Group Bs.
	 * @return the unique ID
	 */
	public String getReportUniqueIdentifier() {
		return reportUniqueIdentifier;
	}
	public void setReportUniqueIdentifier(String identifier) {
		this.reportUniqueIdentifier = identifier;
	}
	/**
	 * The specific data value that caused the error.  For example, if element X can only have a value of "A" or "B", and a
	 * report has a value of "Z" for element X, then getValue() should return "Z".
	 * @return
	 */
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * Get the segment type of the segment that is in error.
	 * @return the segment type code
	 */
	public char getSegmentType() {
		return segmentType;
	}
	public void setSegmentType(char segmentType) {
		this.segmentType = segmentType;
	}
	/**
	 * The value that uniquely identifies the erroneous segment within all the segments of that type in the report.  For example, if a 
	 * Group A report has three offense segments, with UCR Offense Codes = ('13A', '200', '24C'), then getWithinSegmentIdentifier() should
	 * return '200' to identify the second of these offense segments.
	 * @return
	 */
	public Object getWithinSegmentIdentifier() {
		return withinSegmentIdentifier;
	}
	public void setWithinSegmentIdentifier(Object object) {
		this.withinSegmentIdentifier = object;
	}
	
	/**
	 * The code for the error
	 * @return the enum value of the error code
	 */
	public NIBRSErrorCode getNIBRSErrorCode() {
		return nibrsErrorCode;
	}
	public void setNIBRSErrorCode(NIBRSErrorCode nibrsErrorCode) {
		this.nibrsErrorCode = nibrsErrorCode;
	}

	/**
	 * The identifier for the data element whose value is in error.  If this error addresses an entire segment or report, then
	 * the data element identifier is null.
	 * @return the identifier of the data element in error, or null (if error applies to an entire segment or report)
	 */
	public String getDataElementIdentifier() {
		return dataElementIdentifier;
	}
	public void setDataElementIdentifier(String dataElementIdentifier) {
		this.dataElementIdentifier = dataElementIdentifier;
	}
	
	/**
	 * Whether this error is a warning only.
	 * @return whether it's a warning
	 */
	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	/**
	 * Whether this error applies to multiple segments within an incident.
	 * @return whether it's a cross-segment
	 */
	public boolean isCrossSegment() {
		return crossSegment;
	}

	public void setCrossSegment(boolean crossSegment) {
		this.crossSegment = crossSegment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((nibrsErrorCode == null) ? 0 : nibrsErrorCode.hashCode());
		result = prime * result + segmentType;
		result = prime * result + (warning ? 1 : 0);
		result = prime * result + (crossSegment ? 1 : 0);
		result = prime * result + ((reportUniqueIdentifier == null) ? 0 : reportUniqueIdentifier.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((withinSegmentIdentifier == null) ? 0 : withinSegmentIdentifier.hashCode());
		result = prime * result + ((dataElementIdentifier == null) ? 0 : dataElementIdentifier.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

	public String getOffendingValues() {
		List<Object> valueList = new ArrayList<Object>();
		if (!(value instanceof Object[])) {
			valueList.add(value);
		} else {
			valueList = Arrays.asList((Object[]) value);
		}
		Set<String> allItems = new HashSet<String>();
		String values = valueList.stream()
				.filter(Objects::nonNull)
				.map(Object::toString)
				.filter(item-> !StringUtils.endsWith(getRuleNumber(), "06") || !allItems.add(item)) //Set.add() returns false if the item was already in the set.
				.filter(item->!item.equals("null"))
				.distinct()
				.limit(StringUtils.endsWith(getRuleNumber(), "06")? 1 : valueList.size())
				.reduce("", String::concat);
		return values;
	}

	public String getErrorMessage() {
		
		if (getNIBRSErrorCode().message.contains("[value]") && StringUtils.isNotBlank(getOffendingValues())){
			return getNIBRSErrorCode().message.replace("[value]", getOffendingValues()); 
		}
		
		return getNIBRSErrorCode().message;
	}

}
