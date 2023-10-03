package com.blogapp.service.impl;

import com.blogapp.dto.UserDTO;
import com.blogapp.dto.RoleDTO;
import com.blogapp.enums.UserStatus;
import com.blogapp.exception.BlogAppException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Address;
import com.blogapp.model.Role;
import com.blogapp.model.User;
import com.blogapp.enums.UserRole;
import com.blogapp.repository.RoleRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.request.PasswordChangeRequest;
import com.blogapp.response.BlogAppPageableResponse;
import com.blogapp.service.UserService;
import com.blogapp.util.AuthorityUtil;
import com.blogapp.util.EmailUtil;
import com.blogapp.validation.UserValidation;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.USER_SORT_FIELD;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailUtil emailUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailUtil = emailUtil;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO registerUser(UserDTO userDTO) {
        log.info("addUser method invoking");
        UserValidation.validateUser(userDTO);
        if (!StringUtils.hasText(userDTO.getPassword().trim())) {
            log.error("Password is empty");
            throw new BlogAppException("Password cannot be empty");
        }
        userDTO.setPassword(userDTO.getPassword().trim());
        if(userRepository.isUserExist(userDTO.getUserName())){
            log.error("Username already exist");
            throw new BlogAppException("Username already exist. Please try other Username");
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        Optional<Role> role = roleRepository.findByRoleName(UserRole.USER);
        if(role.isPresent()){
            user.getRoles().add(role.get());
            log.info("Saving user information into data base");
            user = userRepository.save(user);
            userDTO = convertUserToUserDTO(user);
            User finalUser = user;
            log.info("Sending verification email");
            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
            log.info("addUser method called");
            return userDTO;
        }
        log.error("Role is not available");
        throw new BlogAppException("Role is not available");

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        log.info("updateUser method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        log.info("validateUser method invoking");
        UserValidation.validateUser(userDTO);
        if(AuthorityUtil.isAdminRole()) {
            user.setStatus(userDTO.getUserStatus());
            user.setIsUserVerified(userDTO.getIsUserVerified());
            return getUpdatedUser(user,userDTO);
        } else {
            if(!userRepository.isDeletedUser(userDTO.getUserName())){
                if(AuthorityUtil.isSameUser(userDTO.getUserName())){
                    return getUpdatedUser(user,userDTO);
                }
                log.error("You cannot update other user profile");
                throw new BlogAppException("You cannot update other user profile");
            }
            log.error("User is deleted");
            throw new BlogAppException("User is deleted");
        }
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        log.info("getUserById method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        return convertUserToUserDTO(user);
    }

    @Override
    public BlogAppPageableResponse<?> getAllUser(Integer pageNumber, Integer pageSize) {
        log.info("getAllUsers method invoking");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, USER_SORT_FIELD));
        Page<User> pageUsers;
        if(AuthorityUtil.isAdminRole()){
            pageUsers = userRepository.findAll(pageable);
        } else {
            pageUsers = userRepository.findAllUser(UserStatus.ACTIVE.getValue(),Boolean.TRUE, pageable);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = null;
        if(pageUsers != null){
            users = pageUsers.getContent();
        }
        if(!CollectionUtils.isEmpty(users)){
            for(User user : users){
                UserDTO userDTO = convertUserToUserDTO(user);
                userDTOS.add(userDTO);
            }
        }
        log.info("getAllUsers method called");
        return BlogAppPageableResponse.builder()
                .content(userDTOS)
                .pageNumber(pageUsers.getNumber())
                .pageSize(pageUsers.getSize())
                .totalElement(pageUsers.getTotalElements())
                .totalPages(pageUsers.getTotalPages())
                .isLast(pageUsers.isLast())
                .isFirst(pageUsers.isFirst())
                .build();

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(Integer userId) {
        log.info("deleteUser method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        if(AuthorityUtil.isAdminRole() || AuthorityUtil.isSameUser(user.getUserName())) {
            if(!user.getStatus().equals(UserStatus.DELETED)){
                userRepository.deleteUser(userId);
            } else {
                log.error("User already deleted");
                throw new BlogAppException("User already deleted");
            }

        } else {
            log.error("You cannot delete other user");
            throw new BlogAppException("You cannot delete other user");
        }
        log.info("deleteUser method called");

    }

    @Override
    public UserDTO updateRole(Set<String> roles, Integer userId) {
        return null;
    }

    @Override
    public List<Role> geAllRoles() {
        return null;
    }

    @Override
    public UserDTO deleteRole(Set<String> roles, Integer userId) {
        return null;
    }

    @Override
    public UserDTO addAddress(Address addresses, Integer userId) {
        return null;
    }

    @Override
    public Set<Address> findAddress(Integer userId) {
        return null;
    }

    @Override
    public UserDTO updateAddress(Address addresses, Integer userId, Integer addressId) {
        return null;
    }

    @Override
    public Address updateAddressByAdmin(Integer userId, Address address, Integer addressId) {
        return null;
    }

    @Override
    public User deleteAddress(Integer userId, Integer addressId) {
        return null;
    }

    @Override
    public String verifyUser(String token) {
        return null;
    }

    @Override
    public User getUserByUsername(String userName) {
        return null;
    }

    @Override
    public String changePassword(PasswordChangeRequest passwordChangeRequest, Integer userId) {
        return null;
    }

    private UserDTO convertUserToUserDTO(User user) {
        log.info("convertUserToUserDTO method invoking");
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        if(AuthorityUtil.isAdminRole()){
            userDTO.setUserStatus(user.getStatus());
            userDTO.setIsUserVerified(user.getIsUserVerified());
        }
        Set<RoleDTO> roleDTOS = user.getRoles().stream().map(r -> modelMapper.map(r, RoleDTO.class)).collect(Collectors.toSet());
        userDTO.setRoles(roleDTOS);
        log.info("convertUserToUserDTO method called");
        return userDTO;
    }

    private void sendVerificationEmail(User user){
        log.info("sendVerificationEmail method invoking");
        try {
            emailUtil.sendEmailWithAttachment(user);
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("Exception raised while sending VerificationEmail:"+e.getMessage());
            userRepository.delete(user);
            throw new BlogAppException("Verification link is not sent to your email. Please register once again");
        }
        log.info("sendVerificationEmail method called");
    }

    private UserDTO getUpdatedUser(User user, UserDTO userDTO){
        log.info("getUpdatedUser method invoking");
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        Optional<User> optionalUser;
        if(!user.getUserName().trim().equalsIgnoreCase(userDTO.getUserName().trim())){
            optionalUser = userRepository.findByUserName(userDTO.getUserName());
            if(optionalUser.isPresent()) {
                log.error("Email already exist");
                throw new BlogAppException("Username already exist. Please try other Username");
            }
            if(!AuthorityUtil.isAdminRole()){
                log.info("Setting isVerified field false for new Username");
                user.setIsUserVerified(Boolean.FALSE);
            }
        }
        user.setGender(userDTO.getGender());
        log.info("Saving user information into data base");
        user = userRepository.save(user);
        userDTO = convertUserToUserDTO(user);
        if(!user.getIsUserVerified()){
            User finalUser = user;
            log.info("Sending verification mail for verifying email");
            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
        }
        log.info("getUpdatedUser method called");
        return userDTO;
    }


}
