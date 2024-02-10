package com.github.foodplacebe.web.controller.authAccount;

import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.service.authAccount.AccountService;
import com.github.foodplacebe.web.dto.account.AccountDto;
import com.github.foodplacebe.web.dto.account.AccountPatchDto;
import com.github.foodplacebe.web.dto.account.UpdateMyInfoRequest;
import com.github.foodplacebe.web.dto.account.UpdatePasswordRequest;
import com.github.foodplacebe.web.dto.postDto.PostRequest;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/my-page")
    public ResponseDto getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
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
    @DeleteMapping("/withdrawal")
    public ResponseDto withdrawal(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return accountService.withdrawal(customUserDetails);
    }

    @GetMapping("/my-info")
    public ResponseDto getAccountInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return accountService.getAccountInfo(customUserDetails);
    }

    @RequestMapping(value = "/update-my-info", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
    public ResponseDto updateMyInfo(
//                                    @RequestBody UpdateMyInfoRequest updateMyInfoRequest,
//                                    @RequestParam(value = "image") MultipartFile multipartFile)


                                    @RequestPart(value = "updateMyInfoRequest") UpdateMyInfoRequest updateMyInfoRequest,
                                    @RequestPart(value = "image") MultipartFile multipartFile)
    {
        return accountService.updateMyInfo( updateMyInfoRequest, multipartFile);
    }

    @PutMapping("/update-password")
    public ResponseDto updatePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return accountService.updatePassword(customUserDetails, updatePasswordRequest);
    }

}
