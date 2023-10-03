package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import com.blogapp.model.Address;
import com.blogapp.model.Role;
import com.blogapp.model.User;
import com.blogapp.request.PasswordChangeRequest;
import com.blogapp.response.BlogAppPageableResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDTO registerUser(UserDTO user);
    UserDTO updateUser(UserDTO user, Integer userId);
    UserDTO getUserById(Integer userId);
    BlogAppPageableResponse<?> getAllUser(Integer pageNumber, Integer pageSize);
    void deleteUser(Integer userId);
    UserDTO updateRole(Set<String> roles, Integer userId);
    List<Role> geAllRoles();
    UserDTO deleteRole(Set<String> roles, Integer userId);
    UserDTO addAddress(Address addresses, Integer userId);
    Set<Address> findAddress(Integer userId);
    UserDTO updateAddress(Address addresses, Integer userId, Integer addressId);
    Address updateAddressByAdmin(Integer userId,Address address, Integer addressId);
    User deleteAddress(Integer userId,Integer addressId);
    String verifyUser(String token);
    User getUserByUsername(String userName);
    String changePassword(PasswordChangeRequest passwordChangeRequest, Integer userId);
}
