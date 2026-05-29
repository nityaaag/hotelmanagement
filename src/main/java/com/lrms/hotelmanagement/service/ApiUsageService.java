package com.lrms.hotelmanagement.service;

import com.lrms.hotelmanagement.entity.ApiUsage;
import java.util.List;

public interface ApiUsageService {
    void logUsage(String partnerName, String endpoint, String method);
    List<ApiUsage> getAllUsage();
}