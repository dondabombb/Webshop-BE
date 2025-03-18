package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.DAO.repository.AddressRepository;
import nl.hsleiden.WebshopBE.model.AddressModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressDAO {
    private final AddressRepository addressRepository;

    public AddressDAO(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Optional<AddressModel> getAddress(String addressId) {
        return this.addressRepository.findById(addressId);
    }

    public AddressModel createAddress(AddressModel address) {
        return this.addressRepository.save(address);
    }

    public AddressModel updateAddress(AddressModel updatedAddress) {
        return this.addressRepository.saveAndFlush(updatedAddress);
    }

    public void deleteAddress(String addressId) {
        this.addressRepository.deleteById(addressId);
    }
} 