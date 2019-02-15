package com.redhat.f4j.base.scalar;

import java.io.Serializable;

/**
 * Skill
 */
public class Skill implements Serializable{

    private static final long serialVersionUID = 3082420447397230507L;
    
    private String name;
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    private String proficiency;
    public String getProficiency() {return proficiency;}
    public void setProficiency(String proficiency) {this.proficiency = proficiency;}

}