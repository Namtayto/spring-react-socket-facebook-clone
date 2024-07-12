package com.social.annotation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.social.model.user.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, PhoneNumber> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(PhoneNumber phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber.getPhoneCode() == null || phoneNumber.getPhoneNumber() == null) {
            return false;
        }
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber.getPhoneNumber(), phoneNumber.getPhoneCode()));
        } catch (NumberParseException e) {
            return false;
        }
    }
}