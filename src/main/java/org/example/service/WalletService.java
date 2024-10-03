package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class WalletService {

    public Long getBalance(Long userId) {
        return 1000000L;
    }
}
