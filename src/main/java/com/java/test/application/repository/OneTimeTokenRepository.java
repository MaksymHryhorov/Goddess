package com.java.test.application.repository;

import com.java.test.application.model.OneTimeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimeTokenRepository extends JpaRepository<OneTimeToken, Long> {
    Optional<OneTimeToken> findTokenByUuid(String uuid);

    void deleteOneTimeTokenByUuid(String uuid);
}
