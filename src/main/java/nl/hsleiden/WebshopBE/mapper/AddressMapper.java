package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DTO.AddressDTO;
import nl.hsleiden.WebshopBE.model.AddressModel;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressModel toModel(AddressDTO dto) {
        AddressModel address = new AddressModel();
        address.setStreet(dto.getStreet());
        address.setHouseNumber(dto.getHouseNumber());
        address.setHouseNumberAddition(dto.getHouseNumberAddition());
        address.setPostalCode(dto.getPostalCode());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        return address;
    }

    public AddressModel mergeAddress(AddressModel base, AddressDTO update) {
        base.setStreet(update.getStreet());
        base.setHouseNumber(update.getHouseNumber());
        base.setHouseNumberAddition(update.getHouseNumberAddition());
        base.setPostalCode(update.getPostalCode());
        base.setCity(update.getCity());
        base.setCountry(update.getCountry());
        return base;
    }
} 