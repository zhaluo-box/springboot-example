package com.zl.box.bootmybaits.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table
@Entity
public class Test implements Serializable {
    private static final long serialVersionUID = 4585789535134053112L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

}
