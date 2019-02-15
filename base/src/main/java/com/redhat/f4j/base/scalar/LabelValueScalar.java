package com.redhat.f4j.base.scalar;

import java.io.Serializable;

import com.redhat.f4j.base.util.UtilityBean;

/**
 * LabelValueScalar
 */
public class LabelValueScalar implements Serializable {

    private static final long serialVersionUID = 1L;
    private String label;
    public String getLabel() { return label;}
    public void setLabel(String label) {this.label = label;}
    
    private String value;
    public String getValue() {return value;}
    public void setValue(String value) {this.value = value;}

    public LabelValueScalar(){/**IGNORED */}
    public LabelValueScalar(String label, String value){
        this.label = label;
        this.value = value;
    }


    /** Utilities  */

    public boolean isApplicable(){
        return !(UtilityBean.isEmptyString(this.label) ||  UtilityBean.isEmptyString(this.value)) ;
    }

    /** FACTORY */
    public static LabelValueScalar createFilter(String label, String value){
        return new LabelValueScalar(label,value);
    }

}