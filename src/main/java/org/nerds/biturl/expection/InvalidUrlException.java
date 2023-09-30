package org.nerds.biturl.expection;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InvalidUrlException extends RuntimeException {
    private String originalUrl;
    private String error;

}
