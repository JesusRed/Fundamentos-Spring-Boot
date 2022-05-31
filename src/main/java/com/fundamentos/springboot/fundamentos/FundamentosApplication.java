package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.jni.Local;
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
    private UserService userService;

    public FundamentosApplication(@Qualifier("componentToImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService) {
        this.componentDependency = componentDependency;
        this.myBean = myBean;
        this.myBeanWithDependency = myBeanWithDependency;
        this.myBeanWithProperties = myBeanWithProperties;
        this.userPojo = userPojo;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FundamentosApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //beforeClasses();
        saveUsersInDatabase();
        getInfoJpqlUser();
        saveErrorTransac();
    }

    public void getInfoJpqlUser() {
        LOGGER.info("User by findbyemail: " + userRepository.findByUserEmail("juan@mail.com").
                orElseThrow(() -> new RuntimeException("user not exists")));

        userRepository.findAndSort("J", Sort.by("id").descending())
                .stream()
                .forEach(user -> LOGGER.info("UserList with Sort method " + user));

        userRepository.findByName("Wan")
                .stream()
                .forEach(user -> LOGGER.info("User by query method " + user));

        LOGGER.info("Usuario by queryMethod findByEmailAndName" + userRepository.findByEmailAndName("oan@mail.com", "Oan")
                .orElseThrow(() -> new RuntimeException("User not exists")));

        userRepository.findByNameLike("%ua%")
                .stream()
                .forEach(user -> LOGGER.info("User findByNameLike: " + user));

        userRepository.findByNameOrEmail(null, "juan@mail.com")
                .stream()
                .forEach(user -> LOGGER.info("User findByNameOrEmail: " + user.getName()));

        userRepository.findByBirthDateBetween(
                        LocalDate.of(1996, 1, 1), LocalDate.of(1997, 12, 31))
                .stream()
                .forEach(user -> LOGGER.info("User by findByBirthDateBetween: " + user.getName()));

        userRepository.findByNameLikeOrderByIdDesc("%an%")
                .stream()
                .forEach(user -> LOGGER.info("User by findByNameLikeOrderByIdDesc: " + user.getName() + "-" + user.getId()));

        userRepository.findByNameContainingOrderByIdAsc(("u"))
                .stream()
                .forEach(user -> LOGGER.info("User by findByNameContainingOrderByIdAsc: " + user));

        LOGGER.info("User form named parameter is: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(1995, 8, 13), "jooan@mail.com")
                .orElseThrow(() -> new RuntimeException("User not exits getAllByBirthDateAndEmail")));
    }

    private void saveErrorTransac() {
        User test1 = new User("TestTransactional1", "test1@mail.com", LocalDate.now());
        User test2 = new User("TestTransactional2", "test2@mail.com", LocalDate.now());
        User test3 = new User("TestTransactional3", "test5@mail.com", LocalDate.now());
        User test4 = new User("TestTransactional4", "test4@mail.com", LocalDate.now());
        List<User> users = Arrays.asList(test1, test2, test3, test4);
        try {
            userService.saveTransactional(users);
        } catch (Exception e) {
            LOGGER.error("Transactional error" + e.toString());
        }

        userService.getAllUsers().stream()
                .forEach(user -> LOGGER.info("This usere is by Transactional " + user));
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
