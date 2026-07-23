package com.wave.backend.user.repository;

import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("""
            SELECT DISTINCT candidate
            FROM User candidate
            JOIN WorkspaceMember candidateMembership
                ON candidateMembership.user = candidate
            WHERE candidate <> :currentUser
            AND EXISTS (
                SELECT currentMembership.id
                FROM WorkspaceMember currentMembership
                WHERE currentMembership.user = :currentUser
                AND currentMembership.workspace =
                    candidateMembership.workspace
            )
            AND (
                LOWER(candidate.username)
                    LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(candidate.firstName)
                    LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(candidate.lastName)
                    LIKE LOWER(CONCAT('%', :query, '%'))
            )
            ORDER BY candidate.username
            """)
    List<User> searchSharedWorkspaceUsers(
            @Param("currentUser") User currentUser,
            @Param("query") String query
    );

}
