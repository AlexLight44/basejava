package webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.io.Serial;
import java.io.Serializable;
@XmlAccessorType(XmlAccessType.FIELD)

public abstract class Section implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
