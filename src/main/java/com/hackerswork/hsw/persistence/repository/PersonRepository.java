package com.hackerswork.hsw.persistence.repository;

import com.hackerswork.hsw.persistence.entity.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUserName(String userName);

    List<Person> findAllByUserNameIn(List<String> userName);
}
