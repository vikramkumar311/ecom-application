package com.embarkx.FirstSpring.Service;

import com.embarkx.FirstSpring.dto.AddressDTO;
import com.embarkx.FirstSpring.dto.UserRequest;
import com.embarkx.FirstSpring.dto.UserResponse;
import com.embarkx.FirstSpring.model.Address;
import com.embarkx.FirstSpring.model.User;
import com.embarkx.FirstSpring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> fetchUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::mapToUserResponse);
    }


    public String addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);
        return "User added Successfully";
    }

    public boolean updateUser(Long userId, UserRequest updateUserRequest) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updateUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);

    }

    public UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        if(user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }

        return response;
    }

    public void updateUserFromRequest(User user, UserRequest userRequest) {
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPhone(userRequest.getPhone());
            user.setRole(userRequest.getRole());

            if(userRequest.getAddress() != null) {
                Address address = new Address();
                address.setStreet(userRequest.getAddress().getStreet());
                address.setCity(userRequest.getAddress().getCity());
                address.setState(userRequest.getAddress().getState());
                address.setCountry(userRequest.getAddress().getCountry());
                address.setZipcode(userRequest.getAddress().getZipcode());
                user.setAddress(address);
            }
    }
}
