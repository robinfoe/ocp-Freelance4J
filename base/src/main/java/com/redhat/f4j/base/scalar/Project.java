package com.redhat.f4j.base.scalar;

/**
 * Project
 */
public class Project extends BaseEntity{

    private static final long serialVersionUID = 2096101363051363957L;
    
    private String title;
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    
    private String description;
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    
    private String status;
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    private Person owner = new Person();
    public Person getOwner() {return owner;}
    public void setOwner(Person owner) {this.owner = owner;}

    public Project(){
        this.owner = new Person();
    }
    
}