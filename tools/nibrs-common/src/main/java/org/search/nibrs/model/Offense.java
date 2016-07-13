package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of an Offense reported within an Incident in a NIBRS report.
 *  
 */
public class Offense implements Serializable
{

	private static final long serialVersionUID = -313348054514046667L;
	
	private String ucrOffenseCode;
    private String offenseAttemptedCompleted;
    private String[] offendersSuspectedOfUsing;
    private String locationType;
    private Integer numberOfPremisesEntered;
    private String methodOfEntry;
    private String[] typeOfCriminalActivity;
    private String[] typeOfWeaponForceInvolved;
    private String[] automaticWeaponIndicator;
    private String[] biasMotivation;
    private int populatedBiasMotivationCount;
    private int populatedTypeOfWeaponForceInvolvedCount;
    private int populatedTypeOfCriminalActivityCount;
    private int populatedOffendersSuspectedOfUsingCount;

    public Offense()
    {
        offendersSuspectedOfUsing = new String[3];
        typeOfCriminalActivity = new String[3];
        typeOfWeaponForceInvolved = new String[3];
        automaticWeaponIndicator = new String[3];
        biasMotivation = new String[5];
    }
    
    public int getPopulatedOffendersSuspectedOfUsingCount() {
    	return populatedOffendersSuspectedOfUsingCount;
    }
    
    public int getPopulatedTypeOfCriminalActivityCount() {
    	return populatedTypeOfCriminalActivityCount;
    }
    
    public int getPopulatedTypeOfWeaponForceInvolvedCount() {
    	return populatedTypeOfWeaponForceInvolvedCount;
    }
    
    public int getPopulatedBiasMotivationCount() {
    	return populatedBiasMotivationCount;
    }
    
    public boolean getOffenseAttemptedIndicator() {
    	return "A".equals(getOffenseAttemptedCompleted());
    }

    public String getAutomaticWeaponIndicator(int position)
    {
        return automaticWeaponIndicator[position];
    }

    public void setAutomaticWeaponIndicator(int position, String value)
    {
        automaticWeaponIndicator[position] = value;
    }

    public String getTypeOfWeaponForceInvolved(int position)
    {
        return typeOfWeaponForceInvolved[position];
    }

    public void setTypeOfWeaponForceInvolved(int position, String value)
    {
        typeOfWeaponForceInvolved[position] = value;
        populatedTypeOfWeaponForceInvolvedCount = Math.max(populatedTypeOfWeaponForceInvolvedCount, position+1);
    }

    public String getTypeOfCriminalActivity(int position)
    {
        return typeOfCriminalActivity[position];
    }

    public void setTypeOfCriminalActivity(int position, String value)
    {
        typeOfCriminalActivity[position] = value;
        populatedTypeOfCriminalActivityCount = Math.max(populatedTypeOfCriminalActivityCount, position+1);
    }

    public String getOffendersSuspectedOfUsing(int position)
    {
        return offendersSuspectedOfUsing[position];
    }

    public void setOffendersSuspectedOfUsing(int position, String value)
    {
        offendersSuspectedOfUsing[position] = value;
        populatedOffendersSuspectedOfUsingCount = Math.max(populatedOffendersSuspectedOfUsingCount, position+1);
    }

    public String getBiasMotivation(int position)
    {
        return biasMotivation[position];
    }

    public void setBiasMotivation(int position, String value)
    {
        biasMotivation[position] = value;
        populatedBiasMotivationCount = Math.max(populatedBiasMotivationCount, position+1);
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getMethodOfEntry()
    {
        return methodOfEntry;
    }

    public void setMethodOfEntry(String methodOfEntry)
    {
        this.methodOfEntry = methodOfEntry;
    }

    public Integer getNumberOfPremisesEntered()
    {
        return numberOfPremisesEntered;
    }

    public void setNumberOfPremisesEntered(Integer numberOfPremisesEntered)
    {
        this.numberOfPremisesEntered = numberOfPremisesEntered;
    }

    public String getOffenseAttemptedCompleted()
    {
        return offenseAttemptedCompleted;
    }

    public void setOffenseAttemptedCompleted(String offenseAttemptedCompleted)
    {
        this.offenseAttemptedCompleted = offenseAttemptedCompleted;
    }

    public String getUcrOffenseCode()
    {
        return ucrOffenseCode;
    }

    public void setUcrOffenseCode(String ucrOffenseCode)
    {
        this.ucrOffenseCode = ucrOffenseCode;
    }

    public boolean isWeaponForceFirearm(int position)
    {
        return NIBRSRules.weaponForceTypeCodeIsFirearm(getTypeOfWeaponForceInvolved(position));
    }

    public boolean isWeaponForceWeapon(int position)
    {
        return NIBRSRules.weaponForceTypeCodeIsWeapon(getTypeOfWeaponForceInvolved(position));
    }
}
