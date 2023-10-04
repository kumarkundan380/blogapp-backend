package com.blogapp.validation;

import com.blogapp.dto.AddressDTO;
import com.blogapp.exception.BlogAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class AddressValidation {

    public static void validateAddress(AddressDTO addressDTO){
        log.info("validateAddress method invoking");
        if(!StringUtils.hasText(addressDTO.getAddressLine1())){
            log.error("Address Line1 is empty");
            throw new BlogAppException("Address Line1 cannot be empty");
        } else if (!StringUtils.hasText(addressDTO.getCity())) {
            log.error("City is empty");
            throw new BlogAppException("City cannot be empty");
        } else if(!StringUtils.hasText(addressDTO.getState())){
            log.error("State is empty");
            throw new BlogAppException("State cannot be empty");
        } else if(!StringUtils.hasText(addressDTO.getCountry())){
            log.error("Country is empty");
            throw new BlogAppException("Country cannot be empty");
        } else if(!StringUtils.hasText(addressDTO.getPostalCode())){
            log.error("Postal Code is empty");
            throw new BlogAppException("Postal Code cannot be empty");
        }
        log.info("Removing extra spaces if any mandatory fields contains");
        addressDTO.setAddressLine1(addressDTO.getAddressLine1().trim());
        addressDTO.setAddressLine2(addressDTO.getAddressLine2().trim());
        addressDTO.setCity(addressDTO.getCity().trim());
        addressDTO.setCountry(addressDTO.getCountry().trim());
        addressDTO.setPostalCode(addressDTO.getPostalCode().trim());
        log.info("validateAddress method called");
    }

}
