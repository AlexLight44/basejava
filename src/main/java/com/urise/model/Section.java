package com.urise.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)

public abstract class Section implements Serializable {
    private SectionType type;
    @Serial
    private static final long serialVersionUID = 1L;

    public Section(SectionType type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    public Section() {
    }

    public SectionType getType() {
        return type;
    }

    public void setType(SectionType type) {
        this.type = type;
    }


}
