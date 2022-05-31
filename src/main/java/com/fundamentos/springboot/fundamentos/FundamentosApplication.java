package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

    private final Log LOGGER = LogFactory.getLog((FundamentosApplication.class));

    private ComponentDependency componentDependency;
    private MyBean myBean;
    private MyBeanWithDependency myBeanWithDependency;
    private MyBeanWithProperties myBeanWithProperties;
    private UserPojo userPojo;
    private UserRepository userRepository;

    public FundamentosApplication(@Qualifier("componentToImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository) {
        this.componentDependency = componentDependency;
        this.myBean = myBean;
        this.myBeanWithDependency = myBeanWithDependency;
        this.myBeanWithProperties = myBeanWithProperties;
        this.userPojo = userPojo;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(FundamentosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //beforeClasses();
        saveUsersInDatabase();
        getInfoJpqlUser();
    }

    public void getInfoJpqlUser(){
        LOGGER.info("User by findbyemail: " + userRepository.findByUserEmail("juan@mail.com").
                orElseThrow(()-> new RuntimeException("user not exists")));

        userRepository.findAndSort("J", Sort.by("id").descending())
                .stream()
                .forEach(user -> LOGGER.info("UserList with Sort method " +user));
    }

    private void saveUsersInDatabase() {
        User user1 = new User("Juan", "juan@mail.com", LocalDate.of(2022, 12, 12));
        User user2 = new User("Wan", "wan@mail.com", LocalDate.of(1998, 11, 24));
        User user3 = new User("Oan", "oan@mail.com", LocalDate.of(1997, 10, 1));
        User user4 = new User("Yuan", "yuan@mail.com", LocalDate.of(1996, 9, 28));
        User user5 = new User("Jooan", "jooan@mail.com", LocalDate.of(1995, 8, 13));
        List<User> list = Arrays.asList(user1, user2, user3, user4, user5);
        userRepository.saveAll(list);
    }

    private void beforeClasses() {
        componentDependency.greetings();
        myBean.print();
        myBeanWithDependency.printWithDependency();
        System.out.println(myBeanWithProperties.function());
        System.out.println(userPojo.getEmail() + "/" + userPojo.getPassword());
        try {
            int value = 10 / 0;
            LOGGER.debug("Value: " + value);
        } catch (Exception e) {
            LOGGER.error("THIS IS A ERROR!!!!! " + Arrays.toString(e.getStackTrace()));
        }
    }
}
