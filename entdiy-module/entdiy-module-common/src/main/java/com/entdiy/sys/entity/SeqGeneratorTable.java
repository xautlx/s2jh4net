package com.entdiy.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于 @see TableSeqGenerator 所需的数据表结构定义之用
 */
@Getter
@Setter
@Access(AccessType.FIELD)
@Entity
@Table(name = "seq_generator_table")
public class SeqGeneratorTable {

    @Id
    private String id;

    @Column(name = "initial_value")
    private Integer initialValue;

    @Column(name = "increment_size")
    private Integer incrementSize;

    @Column(name = "next_val")
    private Long nextVal;

}
