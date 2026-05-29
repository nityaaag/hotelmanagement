package com.lrms.hotelmanagement.repository;

import com.lrms.hotelmanagement.entity.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {
}