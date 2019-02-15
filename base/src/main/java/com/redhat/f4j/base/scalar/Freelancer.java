package com.redhat.f4j.base.scalar;

import java.util.ArrayList;
import java.util.List;

/**
 * Freelancer
 */
public class Freelancer extends BaseEntity{

    private static final long serialVersionUID = 2672524586215535881L;
    
    private Person person = new Person();
    public Person getPerson() {return person;}
    public void setPerson(Person person) {this.person = person;}
    
    private List<Skill> skills = new ArrayList<Skill>();
    public List<Skill> getSkills() {return skills;}
    public void setSkills(List<Skill> skills) {this.skills = skills;}

}