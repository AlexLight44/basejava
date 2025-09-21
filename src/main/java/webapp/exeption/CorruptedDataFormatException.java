package webapp.exeption;

import java.io.IOException;

public class CorruptedDataFormatException extends IOException {
    public CorruptedDataFormatException(String massage){
        super(massage);
    }
}
