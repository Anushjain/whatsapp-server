package com.anush.whatsapp.repos;

import com.anush.whatsapp.domain.Chatroom;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {
    Page<Chatroom> findByUsersIdOrderByLastMessageTimeDesc(UUID userId, PageRequest pageRequest);
}
