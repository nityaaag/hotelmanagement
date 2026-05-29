package com.lrms.hotelmanagement.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrms.hotelmanagement.entity.ApiUsage;
import com.lrms.hotelmanagement.repository.ApiUsageRepository;
import com.lrms.hotelmanagement.service.ApiUsageService;

@Service
public class ApiUsageServiceImpl implements ApiUsageService {
    private final ApiUsageRepository repository;

    public ApiUsageServiceImpl(ApiUsageRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void logUsage(String partnerName, String endpoint, String method) {
        repository.save(new ApiUsage(partnerName, endpoint, method));
    }

    @Override
    public List<ApiUsage> getAllUsage() {
        return repository.findAll();
    }
}
