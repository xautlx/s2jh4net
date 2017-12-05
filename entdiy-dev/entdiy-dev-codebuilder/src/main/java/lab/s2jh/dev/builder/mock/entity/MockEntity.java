package com.entdiy.dev.builder.mock.entity;

import com.entdiy.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author LiXia[xautlx@hotmail.com]
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
public class MockEntity extends BaseNativeEntity {

    @NotNull
    private String filePath;
}
