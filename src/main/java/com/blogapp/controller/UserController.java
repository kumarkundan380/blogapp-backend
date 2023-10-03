package com.blogapp.controller;

import com.blogapp.dto.UserDTO;
import com.blogapp.enums.ResponseStatus;
import com.blogapp.model.Address;
import com.blogapp.request.PasswordChangeRequest;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.blogapp.constant.BlogAppConstant.ADDRESS_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.ADDRESS_PATH;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_USER;
import static com.blogapp.constant.BlogAppConstant.CHANGE_PASSWORD;
import static com.blogapp.constant.BlogAppConstant.PAGE_NUMBER;
import static com.blogapp.constant.BlogAppConstant.PAGE_NUMBER_VALUE;
import static com.blogapp.constant.BlogAppConstant.PAGE_SIZE;
import static com.blogapp.constant.BlogAppConstant.PAGE_SIZE_VALUE;
import static com.blogapp.constant.BlogAppConstant.ROLES_PATH;
import static com.blogapp.constant.BlogAppConstant.TOKEN_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.USER_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.VERIFY_USER;

@RestController
@RequestMapping(value = BASE_PATH_USER)
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<BlogAppResponse<?>> registerUser(@Valid @RequestBody UserDTO user){
		return new ResponseEntity<>(BlogAppResponse.builder()
			.status(ResponseStatus.SUCCESS)
			.message("Welcome to Blog App, please verify your email address")
			.body(userService.registerUser(user))
			.build(),
			HttpStatus.CREATED);
	}

	@PutMapping("/{" + USER_PARAMETER + "}")
	public ResponseEntity<BlogAppResponse<?>> updateUser(@Valid @RequestBody UserDTO user, @PathVariable Integer userId) {
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("User Updated successfully")
				.body(userService.updateUser(user,userId))
				.build(),
				HttpStatus.OK);
	}
	
	@GetMapping("/{"+ USER_PARAMETER + "}")
	public ResponseEntity<BlogAppResponse<?>> getOneUser(@PathVariable Integer userId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("Fetch User Successfully")
				.body(userService.getUserById(userId))
				.build(),
				HttpStatus.OK); 
	}
	
	@GetMapping
	public ResponseEntity<BlogAppResponse<?>> findAllUser(
			@RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
			@RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize) {
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("Fetch User Successfully")
				.body(userService.getAllUser(pageNumber,pageSize))
				.build(),
				HttpStatus.OK); 
	}
	
	@DeleteMapping("/{"+ USER_PARAMETER +"}")
	public ResponseEntity<BlogAppResponse<?>> deleteUser(@PathVariable Integer userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>(BlogAppResponse.builder()
					.status(ResponseStatus.SUCCESS)
					.message("User Deleted Successfully")
					.build(),
					HttpStatus.OK); 	
	}
	

	@PutMapping("/{" + USER_PARAMETER + "}/" + ROLES_PATH)
	public ResponseEntity<BlogAppResponse<?>> updateRole(@RequestBody Set<String> roles,
													 @PathVariable Integer userId){
		return new ResponseEntity<>(BlogAppResponse.builder()
			.status(ResponseStatus.SUCCESS)
			.message("User Role added successfully")
			.body(userService.updateRole(roles,userId))
			.build(),
			HttpStatus.OK);
	}

	@DeleteMapping("/{" + USER_PARAMETER + "}" + ROLES_PATH)
	public ResponseEntity<BlogAppResponse<?>> deleteRole(@RequestBody Set<String> roles,
														 @PathVariable Integer userId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("Role deleted successfully")
				.body(userService.deleteRole(roles,userId))
				.build(),
				HttpStatus.OK);
	}

	@PutMapping("/{" + USER_PARAMETER + "}" + ADDRESS_PATH)
	public ResponseEntity<BlogAppResponse<?>> addAddress(@RequestBody Address address,
														 @PathVariable Integer userId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("User Address added successfully")
				.body(userService.addAddress(address,userId))
				.build(),
				HttpStatus.OK);
	}

	@GetMapping("/{" + USER_PARAMETER + "}"+ ADDRESS_PATH)
	public ResponseEntity<BlogAppResponse<?>> findAddress(@PathVariable Integer userId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("Address fetched successfully")
				.body(userService.findAddress(userId))
				.build(),
				HttpStatus.OK);
	}

	@PutMapping("/{" + USER_PARAMETER + "}"+ ADDRESS_PATH +"/{" + ADDRESS_PARAMETER + "}")
	public ResponseEntity<BlogAppResponse<?>> updateAddress(@PathVariable Integer userId,
															@RequestBody Address address,
															@PathVariable Integer addressId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("User Address updated successfully")
				.body(userService.updateAddress(address,userId,addressId))
				.build(),
				HttpStatus.OK);
	}

	@DeleteMapping("/{" + USER_PARAMETER + "}"+ ADDRESS_PATH +"/{" + ADDRESS_PARAMETER + "}")
	public ResponseEntity<BlogAppResponse<?>> deleteAddress(@PathVariable Integer userId,
															@PathVariable Integer addressId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("User Address deleted successfully")
				.body(userService.deleteAddress(userId,addressId))
				.build(),
				HttpStatus.OK);
	}

	@PutMapping("/{" + USER_PARAMETER + "}" + CHANGE_PASSWORD)
	public ResponseEntity<BlogAppResponse<?>> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest,
															 @PathVariable Integer userId){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("Password changed successfully")
				.body(userService.changePassword(passwordChangeRequest,userId))
				.build(),
				HttpStatus.OK);
	}
	
	@GetMapping(VERIFY_USER + "/{" + TOKEN_PARAMETER + "}")
	public ResponseEntity<BlogAppResponse<?>> verifyUser(@PathVariable String token){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message(userService.verifyUser(token))
				.build(),
				HttpStatus.OK); 	
	}

}
