import com.musicservice.repository.DataBase.DBUserRepository;
import com.musicservice.repository.IUserRepository;
import com.musicservice.service.IUserService;
import com.musicservice.service.Implementation.UserService;
import org.junit.jupiter.api.Test;

public class ServiceTest {

    @Test
    void testService() {
        //User user = new User("Aadhi","aadhi@gmail.com","Aadhiran@10");
        IUserRepository userRepository = DBUserRepository.getInstance();
        IUserService userService = UserService.getInstance(userRepository);
        String email = "dharanb68@gmail.com";
        String password = "Stranger@10";
        boolean status = userService.login(email, password);
        System.out.println((status) ? "Success" : "Failed");
    }

}
