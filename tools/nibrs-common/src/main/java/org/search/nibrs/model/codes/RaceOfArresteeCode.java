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
package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum RaceOfArresteeCode {
	
	W("W","White"),
	B("B","Black or African American"),
	I("I","American Indian or Alaska Native"),
	A("A","Asian"),
	P("P","Native Hawaiian or Other Pacific Islander"),
	U("U","Unknown");
	
	private String code;
	
	private String description;
	
	private RaceOfArresteeCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	public static final Set<String> codeSet() {
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(RaceOfArresteeCode v : values()) {
			
			rCodeSet.add(v.code);
		}		
		return rCodeSet;
	}		

}
