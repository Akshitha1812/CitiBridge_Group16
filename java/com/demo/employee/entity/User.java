package com.demo.employee.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
@Data
@Table(name="user")
@Entity

public class User implements Serializable{
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    public String username;
    public String password;
    
}
