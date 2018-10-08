package com.example.elastic.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

@Document(indexName = "megacorp",type = "employee")
public class Employee implements Serializable {

    private Long id ;

    private String about ;

    private Integer age ;

    private String first_name ;

    private String last_name ;

    private List<String> interests ;

    public Employee (){

    }

    public Employee(Long id,String last_name,String first_name,String about,Integer age,List<String> interests){
        this.id = id ;
        this.last_name = last_name;
        this.first_name = first_name ;
        this.about = about ;
        this.age = age ;
        this.interests = interests;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
