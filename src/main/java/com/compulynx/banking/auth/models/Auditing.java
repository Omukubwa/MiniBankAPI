package com.compulynx.banking.auth.models;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Auditing {
    private String createdBy = "System";
    private Date createdOn = new Date();
    private Date modifiedOn = null;
    private Date deletedOn=null;
    private String deletedBy="-";
    private String modifiedBy="-";
    private boolean deleted=false;
}
