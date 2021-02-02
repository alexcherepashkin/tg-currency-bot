package ua.alexch.bot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.alexch.bot.model.TgUser;

@Repository
@Transactional(readOnly = true)
public interface TgUserRepository extends JpaRepository<TgUser, Long> {

    Optional<TgUser> findByUserId(int userId);
}
