package com.appleisle.tincase.exception;

public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s 값이 %s인 %s를 찾을 수 없습니다.", fieldName, fieldValue, resourceName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
