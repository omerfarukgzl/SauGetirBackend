package com.saugetir.SauGetir.respository;

import com.saugetir.SauGetir.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Merchant, String> {
    
}
