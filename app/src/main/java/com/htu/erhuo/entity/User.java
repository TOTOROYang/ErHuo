package com.htu.erhuo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yzw on 2017/1/24.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private String studentID;
    private String studentPassWord;
    private String studentName;
    private String studentGrade;
    @Generated(hash = 1409141946)
    public User(Long id, String studentID, String studentPassWord,
            String studentName, String studentGrade) {
        this.id = id;
        this.studentID = studentID;
        this.studentPassWord = studentPassWord;
        this.studentName = studentName;
        this.studentGrade = studentGrade;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStudentID() {
        return this.studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public String getStudentPassWord() {
        return this.studentPassWord;
    }
    public void setStudentPassWord(String studentPassWord) {
        this.studentPassWord = studentPassWord;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentGrade() {
        return this.studentGrade;
    }
    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }
    
}
