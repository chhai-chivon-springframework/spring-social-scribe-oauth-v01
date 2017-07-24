package com.phengtola.webservices.controllers.rest;

import com.phengtola.entities.User;
import com.phengtola.forms.FrmSocialLogin;
import com.phengtola.forms.FrmWebLogin;
import com.phengtola.responses.*;
import com.phengtola.responses.failure.ResponseRecordFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.phengtola.services.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by tolapheng on 7/24/17.
 */

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationRController {

    private  HttpStatus httpStatus = null;
    private UserService userService;

    @Autowired
    public AuthenticationRController(@Qualifier("apiUserServiceImpl") UserService userService){
        this.userService = userService;
    }

    @PostMapping("/user/find-by-email")
    public ResponseEntity<ResponseRecord<User>> findUserByEmail(@RequestBody @Valid  FrmWebLogin frmWebLogin , BindingResult result){
        ResponseRecord<User> responseRecord = new ResponseRecord<User>();

        if(result.hasErrors()){
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseRecord = new ResponseRecordFailure<User>(HttpMessage.invalid(Table.USERS, Transaction.Fail.CREATED, result.toString()),false,ResponseHttpStatus.BAD_REQUEST);
        }else{
            User user = userService.findUserByEmail(frmWebLogin.getEmail());
            try{
                if(user != null){
                    httpStatus = HttpStatus.OK;
                    responseRecord = new ResponseRecord<User>(HttpMessage.success(Table.USERS, Transaction.Success.RETRIEVE),true, user);
                }else{
                    httpStatus = HttpStatus.NOT_FOUND;
                    responseRecord = new ResponseRecordFailure<User>( HttpMessage.fail(Table.USERS, Transaction.Fail.RETRIEVE),false,ResponseHttpStatus.RECORD_NOT_FOUND);
                }
            }catch(Exception e){
                e.printStackTrace();
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                responseRecord = new ResponseRecordFailure<User>(  HttpMessage.fail(Table.USERS, Transaction.Fail.RETRIEVE),false,ResponseHttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<ResponseRecord<User>>(responseRecord, httpStatus);
    }


    // Insert use by social account

    @PostMapping("/user/insert-by-social-account")
    public ResponseEntity<ResponseRecord<User>> insertUserBySocialAccount(@RequestBody @Valid FrmSocialLogin frmSocialLogin , BindingResult result){
        ResponseRecord<User> responseRecord = new ResponseRecord<User>();

        if(result.hasErrors()){
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
            responseRecord = new ResponseRecordFailure<User>(HttpMessage.invalid(Table.USERS, Transaction.Fail.CREATED, result.toString()),false,ResponseHttpStatus.BAD_REQUEST);
        }else{

            try{
                if(frmSocialLogin.getEmail() == null){
                    System.out.print("User doesn't provide email address, or user doesn't have email address!");
                    // Set socialId to field email to avoid null value in column email in database
                    frmSocialLogin.setEmail(frmSocialLogin.getSocialId());
                }

                // TODO: Find user by email. If user existed it will return user data else It will insert user to database and return user data.

                User user = userService.findUserByEmail(frmSocialLogin.getEmail());
                if(user != null){
                    // TODO: Return user data
                    httpStatus = HttpStatus.OK;
                    responseRecord = new ResponseRecord<User>(HttpMessage.success(Table.USERS, Transaction.Success.RETRIEVE),true, user);
                    System.out.println("findUserByEmail ====> " + user.getEmail());
                }else{
                    // TODO: Insert user data to database
                    if(userService.insertUserWithSocialAccount(frmSocialLogin)){
                        // TODO: findUserByEmail and return to client
                        User findUserByEmail = userService.findUserByEmail(frmSocialLogin.getEmail());
                        responseRecord = new ResponseRecord<User>(HttpMessage.success(Table.USERS, Transaction.Success.RETRIEVE),true, findUserByEmail);
                        httpStatus = HttpStatus.OK;
                        System.out.println("insertUserWithSocialAccount success ==> findUserByEmail ====> " + findUserByEmail.getEmail());
                    }else{
                        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                        System.out.println("insertUserWithSocialAccount fail ====> " + user.getEmail());
                        responseRecord = new ResponseRecord<User>(HttpMessage.success(Table.USERS, Transaction.Fail.CREATED),true, null);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                responseRecord = new ResponseRecordFailure<User>(  HttpMessage.fail(Table.USERS, Transaction.Fail.RETRIEVE),false,ResponseHttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<ResponseRecord<User>>(responseRecord, httpStatus);
    }

}
