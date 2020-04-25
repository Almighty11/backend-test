package com.example.backendtest.services;

import com.example.backendtest.dtos.UserRequest;
import com.example.backendtest.dtos.ResponseDTO;
import com.example.backendtest.models.User;
import com.example.backendtest.repositories.UserRepository;
import com.example.backendtest.specifications.UserSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String REGEX = "^(gt:|lt:|eq:)?[0-9]+$";
    private final Pattern pattern = Pattern.compile(REGEX, Pattern.MULTILINE);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseDTO> createUser(UserRequest userRequest) {

        User user = new User();

        for(int i=0; i<userRequest.getFields().length; i++){
            user.setProviderId(userRequest.getProviderId());
            user.setFullName(userRequest.getFields()[i].getName());
            user.setAge(userRequest.getFields()[i].getAge());
            user.setTimestamp(userRequest.getFields()[i].getTimestamp());

            userRepository.save(user);
        }

        return new ResponseEntity<>(new ResponseDTO("00", "Users created successfully"), HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseDTO> getUsers(int providerId, String name, String age, String timestamp) {

        if((age != null && !pattern.matcher(age).find())){
            return new ResponseEntity<>(new ResponseDTO("XX", "Incorrect pattern for age."), HttpStatus.BAD_REQUEST);
        }

        List<User> result =  getUserListBasedOnPatterns(providerId, name, age, timestamp);

        if(result.isEmpty()){
            return new ResponseEntity<>(new ResponseDTO("XX", "No users found"), HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(new ResponseDTO("00", "Successful", result), HttpStatus.FOUND);
        }

    }

    public List<User> getUserListBasedOnPatterns(int providerId, String name, String age, String timestamp){

        try {

            String[] splitString;

            Specification<User> providerIdSpecification = UserSpecification.withProviderId(providerId);
            Specification<User> nameSpecification = null;
            Specification<User> ageSpecification = null;
            Specification<User> timestampSpecification = null;

            if (name != null) {
                splitString = name.split(":");

                if (splitString.length > 1 && splitString[0].equalsIgnoreCase("eqc")) {

                        //take everything after "eqc:" as the name
                        name = name.replace("eqc:", "");

                }

                nameSpecification = UserSpecification.withName(name);

            }

            if (age != null) {
                splitString = age.split(":");

                switch (splitString[0]) {

                    //NOTE: We already checked for the pattern. So the next string after the conditional string will be the age
                    case "eq":
                        ageSpecification = UserSpecification.withAgeEqual(Integer.parseInt(splitString[1]));
                        break;

                    case "gt":
                        ageSpecification = UserSpecification.withAgeGreaterThan(Integer.parseInt(splitString[1]));
                        break;

                    case "lt":
                        ageSpecification = UserSpecification.withAgeLessThan(Integer.parseInt(splitString[1]));
                        break;

                    default:
                        ageSpecification = UserSpecification.withAgeEqual(Integer.parseInt(splitString[0]));
                        break;
                }

            }

            if (timestamp != null) {

                Date date;

                splitString = timestamp.split(":");

                switch (splitString[0]) {

                    case "eq":
                        timestamp = timestamp.replace("eq:", "");
                        date = new Date(Long.parseLong(timestamp));
                        timestampSpecification = UserSpecification.withTimeStampEqual(date);
                        break;

                    case "gt":
                        timestamp = timestamp.replace("gt:", "");
                        date = new Date(Long.parseLong(timestamp));
                        timestampSpecification = UserSpecification.withTimeStampGreaterThan(date);
                        break;

                    case "lt":
                        timestamp = timestamp.replace("lt:", "");
                        date = new Date(Long.parseLong(timestamp));
                        timestampSpecification = UserSpecification.withTimeStampLessThan(date);
                        break;

                    default:
                        timestampSpecification = UserSpecification.withTimeStampEqual(new Date(Long.parseLong(timestamp)));
                        break;
                }

            }

            return userRepository.findAll(
                    Specification.where(providerIdSpecification)
                            .and(Specification.where(nameSpecification))
                            .and(Specification.where(ageSpecification))
                            .and(Specification.where(timestampSpecification))

            );

        }catch (Exception exc){
            LOGGER.error("EXCEPTION OCCURRED {}", exc.getMessage());

            return new ArrayList<>();
        }


    }
}
