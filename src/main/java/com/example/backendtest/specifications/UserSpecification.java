package com.example.backendtest.specifications;

import com.example.backendtest.models.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public final class UserSpecification {

    private UserSpecification() {
    }

    private static final String TIMESTAMP = "timestamp";

    public static Specification<User> withProviderId(int providerId){
        return (root, query, cb) -> cb.equal(root.get("providerId"), providerId);
    }

    public static Specification<User> withName(String name){
        return (root, query, cb) -> cb.equal(root.get("fullName"), name);
    }

    public static Specification<User> withAgeEqual(int age){
        return (root, query, cb) -> cb.equal(root.get("age"), age);
    }

    public static Specification<User> withAgeGreaterThan(int age){
        return (root, query, cb) -> cb.greaterThan(root.get("age"), age);
    }

    public static Specification<User> withAgeLessThan(int age){
        return (root, query, cb) -> cb.lessThan(root.get("age"), age);
    }

    public static Specification<User> withTimeStampEqual(Date timestamp){
        return (root, query, cb) -> cb.equal(root.get(TIMESTAMP), timestamp);
    }

    public static Specification<User> withTimeStampGreaterThan(Date timestamp){
        return (root, query, cb) -> cb.greaterThan(root.get(TIMESTAMP), timestamp);
    }

    public static Specification<User> withTimeStampLessThan(Date timestamp){
        return (root, query, cb) -> cb.lessThan(root.get(TIMESTAMP), timestamp);
    }

}
