package com.task.archivalLibrary.repository;

import com.task.archivalLibrary.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

}
