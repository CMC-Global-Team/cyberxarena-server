package internetcafe_management.mapper.Customer;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.entity.*;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDTO toDTO(Customer entity) {
        if (entity == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMembershipCard(entity.getMembershipCard());
        dto.setBalance(entity.getBalance());
        dto.setRegistrationDate(entity.getRegistrationDate());
        return dto;
    }

    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) return null;
        Customer entity = new Customer();
        entity.setCustomerId(dto.getCustomerId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setMembershipCard(dto.getMembershipCard());
        entity.setBalance(dto.getBalance());
        return entity;
    }
    public CustomerDTO toDTO(CustomerSortedAsc entity) {
        if (entity == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMembershipCard(entity.getMembershipCard());
        dto.setBalance(entity.getBalance());
        dto.setRegistrationDate(entity.getRegistrationDate());
        return dto;
    }
    public CustomerDTO toDTO(CustomerSortedDesc entity) {
        if (entity == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMembershipCard(entity.getMembershipCard());
        dto.setBalance(entity.getBalance());
        dto.setRegistrationDate(entity.getRegistrationDate());
        return dto;
    }
    public CustomerDTO toDTO(CustomerSortDateDesc entity) {
        if (entity == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMembershipCard(entity.getMembershipCard());
        dto.setBalance(entity.getBalance());
        dto.setRegistrationDate(entity.getRegistrationDate());
        return dto;
    }
    public CustomerDTO toDTO(CustomerSortDateAsc entity) {
        if (entity == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMembershipCard(entity.getMembershipCard());
        dto.setBalance(entity.getBalance());
        dto.setRegistrationDate(entity.getRegistrationDate());
        return dto;
    }

}