package org.example.controller;


import org.example.out.Response;
import org.example.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Resource
    private WalletService walletService;

    @GetMapping("/getBalance/{id}")
    public Response<Long> getBalance(@PathVariable("id") Long userId){
        return Response.success(walletService.getBalance(userId));
    }
}
