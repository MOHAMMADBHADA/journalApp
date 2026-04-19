package journalApplication.Controller;

import journalApplication.API.Response.WeatherResponse;
import journalApplication.Entity.User;
import journalApplication.Service.UserService;
import journalApplication.Repository.UserRepository;
import journalApplication.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepositoryService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public List<User> getAllUsers()
    {
      return userService.getAll();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user)
    {
       Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
       String username=authentication.getName();
       User userInDb=userService.findByUsername(username);
       userInDb.setUsername(user.getUsername());
       userInDb.setPassword(user.getPassword());
       userService.saveNewUser(userInDb);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userRepositoryService.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/weather-check")
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String city="Badargadh,Palanpur";
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String greeting = "";

        if (weatherResponse != null && weatherResponse.getCurrent() != null) {
            double temp = weatherResponse.getCurrent().getTemperature();
            long roundedTemp = Math.round(temp);
            String description = weatherResponse.getCurrent().getCondition().getText();
            greeting = ", Today " + city + " Weather is " + roundedTemp + "°C (" + description + ")";
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }
}
