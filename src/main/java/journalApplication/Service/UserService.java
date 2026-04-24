package journalApplication.Service;

import lombok.extern.slf4j.Slf4j;
import journalApplication.Entity.User;
import journalApplication.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService
{
  @Autowired
  private UserRepository userRepository;

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public boolean saveNewUser(User user)
  {
    try {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(Arrays.asList("USER"));
      userRepository.save(user);
      return true;
    }
    catch (Exception e)
    {
     log.error("HAHAHAHAHAHAHAHA");
     log.warn("HAHAHAHAHAHAHAHA");
     log.info("HAHAHAHAHAHAHAHA");
     log.debug("HAHAHAHAHAHAHAHA");
     log.trace("HAHAHAHAHAHAHAHA");
      return false;
    }
  }

  public void saveAdmin(User user)
  {
    User userInDb = userRepository.findByUsername(user.getUsername());
    if (userInDb != null) {
      if (!userInDb.getRole().contains("ADMIN")) {
        userInDb.getRole().add("ADMIN");
      }
    }
    else {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(Arrays.asList("USER","ADMIN"));
      userRepository.save(user);
    }
  }

  public void saveUser(User user)
  {
    userRepository.save(user);
  }

  public List<User> getAll()
  {
    return userRepository.findAll();
  }

  public Optional<User> findById(ObjectId id)
  {
    return userRepository.findById(id);
  }

  public void deleteById(ObjectId id)
  {
     userRepository.deleteById(id);
  }

  public User findByUsername(String username)
  {
    return userRepository.findByUsername(username);
  }

}
