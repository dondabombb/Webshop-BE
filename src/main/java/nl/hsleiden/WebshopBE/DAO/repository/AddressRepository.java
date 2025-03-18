package nl.hsleiden.WebshopBE.DAO.repository;

import nl.hsleiden.WebshopBE.model.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressModel, String> {
} 