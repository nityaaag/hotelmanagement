package com.lrms.hotelmanagement.serviceimpl;

import com.lrms.hotelmanagement.entity.User;
import com.lrms.hotelmanagement.repository.UserRepository;
import com.lrms.hotelmanagement.service.UserService;
import com.lrms.hotelmanagement.exception.InvalidBookingException; // Reusing for user not found
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Default to read-only for service methods
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional // Override read-only for write operations
    public User createUser(User user) {
        // Add any business logic for user creation, e.g., password encoding
        return userRepository.save(user);
    }

    @Override
    @Transactional // Override read-only for write operations
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidBookingException("User not found with id: " + id)); // Reusing exception

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword()); // In a real app, handle password updates carefully
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    @Override
    @Transactional // Override read-only for write operations
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new InvalidBookingException("User not found with id: " + id); // Reusing exception
        }
        userRepository.deleteById(id);
    }
}
