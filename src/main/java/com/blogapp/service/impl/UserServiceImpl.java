package com.blogapp.service.impl;

import com.blogapp.dto.AddressDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.dto.RoleDTO;
import com.blogapp.enums.UserStatus;
import com.blogapp.exception.BlogAppException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Address;
import com.blogapp.model.Role;
import com.blogapp.model.User;
import com.blogapp.enums.UserRole;
import com.blogapp.model.VerificationToken;
import com.blogapp.repository.AddressRepository;
import com.blogapp.repository.RoleRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.repository.VerificationTokenRepository;
import com.blogapp.request.PasswordChangeRequest;
import com.blogapp.response.BlogAppPageableResponse;
import com.blogapp.service.CommonService;
import com.blogapp.service.ImageService;
import com.blogapp.service.UserService;
import com.blogapp.util.AuthorityUtil;
import com.blogapp.util.EmailUtil;
import com.blogapp.validation.AddressValidation;
import com.blogapp.validation.PasswordChangedRequestValidation;
import com.blogapp.validation.UserValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.blogapp.constant.BlogAppConstant.ADDRESS_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.USER_SORT_FIELD;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final VerificationTokenRepository verificationTokenRepository;
    private final CommonService commonService;

    private final ObjectMapper objectMapper;

    private final ImageService imageService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailUtil emailUtil, VerificationTokenRepository verificationTokenRepository,CommonService commonService, ObjectMapper objectMapper, ImageService imageService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailUtil = emailUtil;
        this.verificationTokenRepository = verificationTokenRepository;
        this.commonService = commonService;
        this.objectMapper = objectMapper;
        this.imageService = imageService;
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public UserDTO registerUser(UserDTO userDTO) {
//        log.info("addUser method invoking");
//        UserValidation.validateUser(userDTO);
//        if (!StringUtils.hasText(userDTO.getPassword().trim())) {
//            log.error("Password is empty");
//            throw new BlogAppException("Password cannot be empty");
//        }
//        userDTO.setPassword(userDTO.getPassword().trim());
//        if(userRepository.isUserExist(userDTO.getUserName())){
//            log.error("Username already exist");
//            throw new BlogAppException("Username already exist. Please try other Username");
//        }
//        User user = modelMapper.map(userDTO, User.class);
//        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
//        Optional<Role> role = roleRepository.findByRoleName(UserRole.USER.getValue());
//        if(role.isPresent()){
//            user.getRoles().add(role.get());
//            log.info("Saving user information into data base");
//            user = userRepository.save(user);
//            userDTO = commonService.convertUserToUserDTO(user);
//            User finalUser = user;
//            log.info("Sending verification email");
//            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
//            log.info("addUser method called");
//            return userDTO;
//        }
//        log.error("Role is not available");
//        throw new BlogAppException("Role is not available");
//
//    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO registerUser(MultipartFile image,String userData) {
        log.info("addUser method invoking");
        UserDTO userDTO = null;
        try{
           userDTO  = objectMapper.readValue(userData, UserDTO.class);
        } catch (JsonProcessingException e){
            throw new BlogAppException("UserData is not in proper format");
        }
        String imageUrl = null;
        if(!ObjectUtils.isEmpty(image) && !image.isEmpty()) {
            Map<String,String> imageData = imageService.uploadImageOnCloudinary(image);
            if(imageData.containsKey("url")){
                imageUrl = imageData.get("url");
            }
        }
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
        user.setUserImage(imageUrl);
        Optional<Role> role = roleRepository.findByRoleName(UserRole.USER.getValue());
        if(role.isPresent()){
            user.getRoles().add(role.get());
            log.info("Saving user information into data base");
            user = userRepository.save(user);
            userDTO = commonService.convertUserToUserDTO(user);
            User finalUser = user;
            log.info("Sending verification email");
            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
            log.info("addUser method called");
            return userDTO;
        }
        log.error("Role is not available");
        throw new BlogAppException("Role is not available");

    }


//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
//        log.info("updateUser method invoking");
//        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
//        log.info("validateUser method invoking");
//        UserValidation.validateUser(userDTO);
//        if(AuthorityUtil.isAdminRole()) {
//            user.setStatus(userDTO.getUserStatus());
//            return getUpdatedUser(user,userDTO);
//        } else {
//            if(!userRepository.isDeletedUser(userDTO.getUserName())){
//                if(AuthorityUtil.isSameUser(userDTO.getUserName())){
//                    return getUpdatedUser(user,userDTO);
//                }
//                log.error("You cannot update other user profile");
//                throw new BlogAppException("You cannot update other user profile");
//            }
//            log.error("User is deleted");
//            throw new BlogAppException("User is deleted");
//        }
//    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO updateUser(MultipartFile image,String userData, Integer userId) {
        log.info("updateUser method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        UserDTO userDTO = null;
        try{
            userDTO  = objectMapper.readValue(userData, UserDTO.class);
        } catch (JsonProcessingException e){
            throw new BlogAppException("UserData is not in proper format");
        }
        log.info("validateUser method invoking");
        UserValidation.validateUser(userDTO);
        if(!ObjectUtils.isEmpty(image) && !image.isEmpty()) {
            Map<String,String> imageData = imageService.uploadImageOnCloudinary(image);
            if(imageData.containsKey("url")){
                user.setUserImage(imageData.get("url"));
            }
        }
        if(AuthorityUtil.isAdminRole()) {
            if(!ObjectUtils.isEmpty(userDTO.getUserStatus())){
                user.setStatus(userDTO.getUserStatus());
            }
            if(!ObjectUtils.isEmpty(userDTO.getIsVerified())){
                user.setIsUserVerified(userDTO.getIsVerified());
            }
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
        return commonService.convertUserToUserDTO(user);
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
                UserDTO userDTO = commonService.convertUserToUserDTO(user);
                userDTOS.add(userDTO);
            }
        }
        log.info("getAllUsers method called");
        return BlogAppPageableResponse.builder()
                .content(userDTOS)
                .pageNumber(ObjectUtils.isEmpty(pageUsers)?0:pageUsers.getNumber())
                .pageSize(ObjectUtils.isEmpty(pageUsers)?0:pageUsers.getSize())
                .totalElement(ObjectUtils.isEmpty(pageUsers)?0:pageUsers.getTotalElements())
                .totalPages(ObjectUtils.isEmpty(pageUsers)?0:pageUsers.getTotalPages())
                .isLast(ObjectUtils.isEmpty(pageUsers)?null:pageUsers.isLast())
                .isFirst(ObjectUtils.isEmpty(pageUsers)?null:pageUsers.isFirst())
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
    @Transactional(rollbackOn = Exception.class)
    public UserDTO addRole(Set<RoleDTO> userRoles, Integer userId) {
        log.info("updateUserRole method invoking");
        if(AuthorityUtil.isAdminRole()) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
            Set<Role> roles = roleRepository.findByRoleNameIn(userRoles.stream().map(RoleDTO::getRoleName).collect(Collectors.toSet()));
            user.getRoles().addAll(roles);
            log.info("Saving user information into data base");
            user = userRepository.save(user);
            return commonService.convertUserToUserDTO(user);
        } else {
            log.error("You do not have permission to add role");
            throw new BlogAppException("You do not have permission to add role");
        }
    }

    @Override
    public Set<RoleDTO> geAllRoles() {
        log.info("geAllRoles method invoking");
        if(AuthorityUtil.isAdminRole()) {
            List<Role> roles = roleRepository.findAll();
            return roles.stream().map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toSet());
        } else {
            log.error("You do not have permission to add role");
            throw new BlogAppException("You do not have permission to add role");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO deleteRole(Set<RoleDTO> userRoles, Integer userId) {
        log.info("deleteRole method invoking");
        if(AuthorityUtil.isAdminRole()) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
            Set<Role> roles = roleRepository.findByRoleNameIn(userRoles.stream().map(RoleDTO::getRoleName).collect(Collectors.toSet()));
            user.getRoles().removeAll(roles);
            log.info("Saving user information into data base");
            user = userRepository.save(user);
            return commonService.convertUserToUserDTO(user);
        } else {
            log.error("You do not have permission to add role");
            throw new BlogAppException("You do not have permission to add role");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO addAddress(AddressDTO addressDTO, Integer userId) {
        log.info("addAddress method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        AddressValidation.validateAddress(addressDTO);
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        address = addressRepository.save(address);
        user.getAddresses().add(address);
        return commonService.convertUserToUserDTO(user);
    }

    @Override
    public Set<AddressDTO> findAddress(Integer userId) {
        log.info("findAddress method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Set<Address> addresses = addressRepository.findByUser(user);
        Set<AddressDTO> addressDTOS = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).collect(Collectors.toSet());
        return addressDTOS;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO updateAddress(AddressDTO addressDTO, Integer userId, Integer addressId) {
        log.info("addAddress method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException(ADDRESS_EXCEPTION, EXCEPTION_FIELD, addressId));
        AddressValidation.validateAddress(addressDTO);
        getUpdatedAddress(address,addressDTO);
        address.setUser(user);
        address = addressRepository.save(address);
        user.getAddresses().add(address);
        return commonService.convertUserToUserDTO(user);
    }

    @Override
    public AddressDTO getOneAddress(Integer userId, Integer addressId) {
        log.info("deleteAddress method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Set<Address> addresses = user.getAddresses();
        Optional<Address> optionalAddress = addresses.stream().filter(address -> addressId.equals(address.getAddressId())).findAny();
        if(optionalAddress.isPresent()) {
            return modelMapper.map(optionalAddress.get(),AddressDTO.class);
        }
        log.error("Address Id not matched");
        throw new BlogAppException("Address Id not matched");
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserDTO deleteAddress(Integer userId, Integer addressId) {
        log.info("deleteAddress method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException(ADDRESS_EXCEPTION, EXCEPTION_FIELD, addressId));
        addressRepository.delete(address);
        user.getAddresses().remove(address);
        return commonService.convertUserToUserDTO(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String verifyUser(String token) {
        log.info("verifyUser method invoking");
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(()-> new BlogAppException("Token is either expired or invalid"));
        User user = verificationToken.getUser();
        if(verificationToken.getCreatedAt().plusMinutes(15).isAfter(LocalDateTime.now())) {
            user.setIsUserVerified(Boolean.TRUE);
            log.info("Saving user information into data bases");
            userRepository.save(user);
            log.info("verifyUser method called");
            return "Congratulations! Your account has been activated and email is verified!";
        } else {
            verificationTokenRepository.delete(verificationToken);
            sendVerificationEmail(user);
            log.info("verifyUser method called");
            return "This Link is expired. New Verification Link send to your E-Mail Please Verify it Within 15 min.";
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String changePassword(PasswordChangeRequest passwordChangeRequest, Integer userId) {
        log.info("changePassword method invoking");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        PasswordChangedRequestValidation.validatePasswordChangedRequest(passwordChangeRequest);
        if (passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            if (passwordChangeRequest.getOldPassword().equals(passwordChangeRequest.getNewPassword().trim())) {
                log.error("New Password matched with Existing Password");
                throw new BlogAppException("New Password matched with Existing Password. Please enter different Password");
            }
            user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
            log.info("Saving user information into data base");
            userRepository.save(user);
            log.info("changePassword method called");
            return "Password Changed";
        } else {
            log.error("Password Not matched with Existing Password");
            throw new BlogAppException("Password Not matched with Existing Password");
        }
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
                log.info("Assign isVerified field false for new Username");
                user.setIsUserVerified(Boolean.FALSE);
            }
        }
        user.setGender(userDTO.getGender());
        user.setAbout(userDTO.getAbout());
        log.info("Saving user information into data base");
        user = userRepository.save(user);
        userDTO = commonService.convertUserToUserDTO(user);
        if(!user.getIsUserVerified()){
            User finalUser = user;
            log.info("Sending verification mail for verifying email");
            CompletableFuture.runAsync(() -> sendVerificationEmail(finalUser));
        }
        log.info("getUpdatedUser method called");
        return userDTO;
    }

    private void getUpdatedAddress(Address address, AddressDTO addressDTO){
        log.info("getUpdatedAddress method invoking");
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setPostalCode(addressDTO.getPostalCode());
        log.info("getUpdatedAddress method called");
    }

}
