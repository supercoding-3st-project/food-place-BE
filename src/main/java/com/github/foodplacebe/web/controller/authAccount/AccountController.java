package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.authAccount.AccountService;
import com.github.foodplacebe.web.dto.account.AccountDto;
import com.github.foodplacebe.web.dto.account.AccountPatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/my-page")
    public AccountDto getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return accountService.getMyInfo(customUserDetails);
    }

    @PatchMapping("/my-page")
    public AccountDto patchMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody AccountPatchDto accountDTO){
        return accountService.patchMyInfo(customUserDetails, accountDTO);
    }

//    @PostMapping("/my-page/cart")
//    public String cartAddItem(HttpServletRequest httpServletRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails){
//        return accountService.cartAddItem(httpServletRequest.getParameter("option-id"),
//                httpServletRequest.getParameter("quantity"),
//                customUserDetails);
//    }
    @PostMapping("/withdrawal")
    public String withdrawal(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return accountService.withdrawal(customUserDetails);
    }

}
