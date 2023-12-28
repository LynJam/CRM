package cn.edu.scnu.util;

import cn.edu.scnu.UserEntity;
import com.github.javafaker.Faker;
import java.util.Locale;

public class FakerUtil {
    public static UserEntity randomUser() {
        Faker faker = new Faker(Locale.CHINA);
        String name = faker.name().fullName();
        int age = faker.number().numberBetween(18, 60);
        String id = faker.idNumber().valid();
        return UserEntity.builder().age(age).name(name).id(id).build();
    }
}
