package com.example.myproject.service;

import com.example.myproject.domain.Admin;
import com.example.myproject.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

   private final AdminRepository adminRepository;

   public void saveAdmin(Admin admin) {
       adminRepository.save(admin);
   }
}
