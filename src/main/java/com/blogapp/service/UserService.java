package com.blogapp.service;

import com.blogapp.dto.AddressDTO;
import com.blogapp.dto.RoleDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.request.ForgotPasswordRequest;
import com.blogapp.request.PasswordChangeRequest;
import com.blogapp.request.VerifyEmailRequest;
import com.blogapp.response.BlogAppPageableResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface UserService {
    UserDTO registerUser(MultipartFile image, String userData);
    UserDTO updateUser(MultipartFile image, String userData, Integer userId);
    UserDTO getUserById(Integer userId);
    BlogAppPageableResponse<?> getAllUser(Integer pageNumber, Integer pageSize);
    void deleteUser(Integer userId);
    UserDTO updateRole(Set<RoleDTO> roles, Integer userId);
    Set<RoleDTO> geAllRoles();
    UserDTO deleteRole(Set<RoleDTO> roles, Integer userId);
    UserDTO addAddress(AddressDTO addressDTO, Integer userId);
    Set<AddressDTO> findAddress(Integer userId);
    UserDTO updateAddress(AddressDTO addressDTO, Integer userId, Integer addressId);
    AddressDTO getOneAddress(Integer userId, Integer addressId);
    UserDTO deleteAddress(Integer userId,Integer addressId);
    String verifyUser(VerifyEmailRequest verifyEmailRequest);
    String changePassword(PasswordChangeRequest passwordChangeRequest);
    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}
