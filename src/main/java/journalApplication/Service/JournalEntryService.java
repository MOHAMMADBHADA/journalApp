package journalApplication.Service;

import journalApplication.Entity.JournalEntry;
import journalApplication.Entity.User;
import journalApplication.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService
{
  @Autowired
  private JournalEntryRepository JER;

  @Autowired
  private UserService userService;

  @Transactional
  public void saveEntry(JournalEntry journalEntry, String username) {
    try {
      User user = userService.findByUsername(username);
      JournalEntry saved = JER.save(journalEntry);
      user.getJournalEntries().add(saved);
      userService.saveUser(user);
    }
    catch (Exception e)
    {
      throw new RuntimeException("An error occured while saving the entry",e);
    }
  }

  public void saveEntry(JournalEntry journalEntry)
  {
    JER.save(journalEntry);
  }

  public List<JournalEntry> getAll()
  {
    return JER.findAll();
  }

  public Optional<JournalEntry> findById(ObjectId id)
  {
    return JER.findById(id);
  }

  @Transactional
  public boolean deleteById(ObjectId id, String username)
  {
    boolean removed= false;
    try {
      User user = userService.findByUsername(username);
      removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
      if (removed) {
        userService.saveUser(user);
        JER.deleteById(id);
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
      throw new RuntimeException("An error occured while deleting the entry",e);
    }
    return removed;
  }
}

//service --> repository