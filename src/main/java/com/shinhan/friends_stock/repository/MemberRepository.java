package com.shinhan.friends_stock.repository;

import com.shinhan.friends_stock.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String username);
}
