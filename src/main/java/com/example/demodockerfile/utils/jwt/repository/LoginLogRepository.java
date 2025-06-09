package com.example.demodockerfile.utils.jwt.repository;


import com.example.demodockerfile.entity.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Long> {

}
