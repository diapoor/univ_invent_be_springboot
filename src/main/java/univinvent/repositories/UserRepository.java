package univinvent.repositories;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import univinvent.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username LIKE :username")
    public User getUserByUsername(@Param("username") String username);
    boolean existsByUsername(String Username);

    public void deleteUserByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username LIKE :username AND u.fullName like :fullName")
    public User getUserByUsernameAndFullName(@Param("username") String username,@Param("fullName")String fullName);

    @Query("SELECT u FROM User u WHERE u.username LIKE :keyword OR u.fullName like :keyword ")
    public List<User> getUserByUsernameOrFullName(@Param("keyword") String keyword);
}
