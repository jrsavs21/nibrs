package org.search.nibrs.model;

import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representation of an Arrestee reported within an Incident in a NIBRS report.
 *
 */
public class ArresteeSegment extends AbstractPersonSegment
{
    
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ArresteeSegment.class);
	
	private Integer arresteeSequenceNumber;
    private String arrestTransactionNumber;
    private Date arrestDate;
    private String typeOfArrest;
    private String multipleArresteeSegmentsIndicator;
    private String ucrArrestOffenseCode;
    private String[] arresteeArmedWith;
    private String[] automaticWeaponIndicator;
    private String residentStatusOfArrestee;
    private String dispositionOfArresteeUnder18;

    public ArresteeSegment()
    {
        arresteeArmedWith = new String[2];
        automaticWeaponIndicator = new String[2];
    }
    
    public ArresteeSegment(ArresteeSegment a) {
    	super(a);
    	this.arresteeSequenceNumber = a.arresteeSequenceNumber;
    	this.arrestTransactionNumber = a.arrestTransactionNumber;
    	this.arrestDate = a.arrestDate;
    	this.typeOfArrest = a.typeOfArrest;
    	this.multipleArresteeSegmentsIndicator = a.multipleArresteeSegmentsIndicator;
    	this.ucrArrestOffenseCode = a.ucrArrestOffenseCode;
    	this.residentStatusOfArrestee = a.residentStatusOfArrestee;
    	this.dispositionOfArresteeUnder18 = a.dispositionOfArresteeUnder18;
    	this.arresteeArmedWith = CopyUtils.copyArray(a.arresteeArmedWith);
    	this.automaticWeaponIndicator = CopyUtils.copyArray(a.automaticWeaponIndicator);
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((arrestDate == null) ? 0 : arrestDate.hashCode());
		result = prime * result + ((arrestTransactionNumber == null) ? 0 : arrestTransactionNumber.hashCode());
		result = prime * result + Arrays.hashCode(arresteeArmedWith);
		result = prime * result + ((arresteeSequenceNumber == null) ? 0 : arresteeSequenceNumber.hashCode());
		result = prime * result + Arrays.hashCode(automaticWeaponIndicator);
		result = prime * result + ((dispositionOfArresteeUnder18 == null) ? 0 : dispositionOfArresteeUnder18.hashCode());
		result = prime * result + ((multipleArresteeSegmentsIndicator == null) ? 0 : multipleArresteeSegmentsIndicator.hashCode());
		result = prime * result + ((residentStatusOfArrestee == null) ? 0 : residentStatusOfArrestee.hashCode());
		result = prime * result + ((typeOfArrest == null) ? 0 : typeOfArrest.hashCode());
		result = prime * result + ((ucrArrestOffenseCode == null) ? 0 : ucrArrestOffenseCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

	public String getAutomaticWeaponIndicator(int position)
    {
        return automaticWeaponIndicator[position];
    }
    
    public void setAutomaticWeaponIndicator(int position, String value)
    {
        automaticWeaponIndicator[position] = value;
    }
    
    public String getArresteeArmedWith(int position)
    {
        return arresteeArmedWith[position];
    }
    
    public void setArresteeArmedWith(int position, String value)
    {
        arresteeArmedWith[position] = value;
    }
    
    public Date getArrestDate()
    {
        return arrestDate;
    }
    public void setArrestDate(Date arrestDate)
    {
        this.arrestDate = arrestDate;
    }
    public Integer getArresteeSequenceNumber()
    {
        return arresteeSequenceNumber;
    }
    public void setArresteeSequenceNumber(Integer arresteeSequenceNumber)
    {
        this.arresteeSequenceNumber = arresteeSequenceNumber;
    }
    public String getArrestTransactionNumber()
    {
        return arrestTransactionNumber;
    }
    public void setArrestTransactionNumber(String arrestTransactionNumber)
    {
        this.arrestTransactionNumber = arrestTransactionNumber;
    }
    public String getDispositionOfArresteeUnder18()
    {
        return dispositionOfArresteeUnder18;
    }
    public void setDispositionOfArresteeUnder18(String dispositionOfArresteeUnder18)
    {
        this.dispositionOfArresteeUnder18 = dispositionOfArresteeUnder18;
    }
    public String getMultipleArresteeSegmentsIndicator()
    {
        return multipleArresteeSegmentsIndicator;
    }
    public void setMultipleArresteeSegmentsIndicator(String multipleArresteeSegmentsIndicator)
    {
        this.multipleArresteeSegmentsIndicator = multipleArresteeSegmentsIndicator;
    }
    public String getResidentStatusOfArrestee()
    {
        return residentStatusOfArrestee;
    }
    public void setResidentStatusOfArrestee(String residentStatusOfArrestee)
    {
        this.residentStatusOfArrestee = residentStatusOfArrestee;
    }
    public String getTypeOfArrest()
    {
        return typeOfArrest;
    }
    public void setTypeOfArrest(String typeOfArrest)
    {
        this.typeOfArrest = typeOfArrest;
    }
    public String getUcrArrestOffenseCode()
    {
        return ucrArrestOffenseCode;
    }
    public void setUcrArrestOffenseCode(String ucrArrestOffenseCode)
    {
        this.ucrArrestOffenseCode = ucrArrestOffenseCode;
    }

}