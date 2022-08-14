package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.ResetPasswordDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.UserInfoDTO;
import com.example.springboot_cy_marketplace.entity.RoleEntity;
import com.example.springboot_cy_marketplace.jwt.payload.request.*;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.services.impl.AddressServiceImpl;
import com.example.springboot_cy_marketplace.services.impl.MailService;
import com.example.springboot_cy_marketplace.services.impl.ProductCategoryServiceImpl;
import com.example.springboot_cy_marketplace.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Date;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MailService mailService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    AddressServiceImpl addressService;
    @Value("${server.host.fe.user}")
    private String feHost;

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:01 SA
     * @description-VN: Đăng kí tài khoản người dùng.
     * @description-EN: Register user.
     * @param: userSignup - Đối tượng chứa thông tin tài khoản người dùng.
     * @return:
     *
     * */
    @PostMapping(value = "/signup")
    public ResponseEntity<?> userSignup(@RequestBody RegisterRequest userSignup) {
        boolean result = userService.add(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(userSignup, "User signup"));
        } else {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    /*
     * @author: HieuMM
     * @since: 14-Jun-22 2:06 PM
     * @description-VN:  Cập nhật thông tin người dùng.
     * @description-EN:  Update user profile.
     * @param: userSignup - Đối tượng chứa thông tin tài khoản người dùng được update.
     * */
    @PostMapping(value = "/updateprofile")
    public ResponseEntity<?> updateprofile(UpdateProfileRequest userSignup) {
        boolean result = userService.updateProfile(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(result, "Profile update successfully"));
        } else {
            return ResponseEntity.internalServerError().body(ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    /*
     * @author: HieuMM
     * @since: 15-Jun-22 8:40 AM
     * @description-VN: Cập nhật thông tin địa chỉ người dùng.
     * @description-EN:  Update user address.
     * @param:
     * */
    @PostMapping(value = "/updateprofileaddress")
    public ResponseEntity<?> updateprofileaddress(@RequestBody UpdateAddressProfileRequest userSignup) {
        boolean result = userService.updateAddressProfile(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(userSignup, "Profile address update successfully"));
        } else {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    @PostMapping(value = "/update-default-address")
    public ResponseEntity<?> updateDefaultaddress(@RequestBody UpdateDefaultAddressRequest userSignup) {
        boolean result = userService.updateDefaultAdd(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(userSignup, "Profile address update successfully"));
        } else {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    /*
     * @author: HieuMM
     * @since: 22-Jun-22 11:15 AM
     * @description-VN:  Update avatar profile
     * @description-EN:
     * @param:
     * */
    @PostMapping(value = "/update-avatar-profile")
    public ResponseEntity<?> updateAvatarProfile(UpdateAvatarProfileRequest userSignup) {
        boolean result = userService.updateAvatarProfile(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(result, "Avatar update successfully"));
        } else {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    /*
     * @author: HieuMM
     * @since: 15-Jun-22 9:37 AM
     * @description-VN:  cập nhật mật khẩu người dùng.
     * @description-EN:  update user password.
     * @param:
     * */
    @PostMapping(value = "/updatepassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest userSignup) {
        boolean result = userService.updatePassword(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(userSignup, "update successfully"));
        } else {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    /*
     * @author: HieuMM
     * @since: 20-Jun-22 2:55 PM
     * @description-VN:  Thêm địa chỉ mới cho user
     * @description-EN:  Add new address for user
     * @param:
     * */
    @PostMapping(value = "/add-new-address")
    public ResponseEntity<?> addNewAdd(@RequestBody UpdateAddressProfileRequest userSignup) {
        boolean result = userService.addAddressProfile(userSignup);
        if (result) {
            return ResponseEntity.ok().body(ResponseDTO.of(userSignup, "Profile address update successfully"));
        } else {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.of("Email existed! Please use another email!", "User signup"));
        }
    }

    /*
     * @author: HieuMM
     * @since: 20-Jun-22 2:56 PM
     * @description-VN:   Xóa địa chỉ người dùng
     * @description-EN:  Delete address of user
     * @param:
     * */
    @DeleteMapping(value = "/deleteaddress/{id}")
    public Object deleteAddress(@PathVariable(value = "id") Long id) {
        boolean result = this.addressService.deleteById(id);
        if (result) {
            return ResponseDTO.show(200, "Delete address by id", id);
        } else {
            return ResponseDTO.show(400, "Delete address by id", null);
        }
    }

    /*
     * @author: HieuMM
     * @since: 21-Jun-22 9:04 AM
     * @description-VN:  Hiển thị tất cả địa chỉ người dùng
     * @description-EN:  Show list address of every user
     * @param: id(User)
     * */
    @GetMapping("/findAllAddress/{id}")
    public Object findAllByUserId(Pageable pageable, @PathVariable(value = "id") Long id) {
        return ResponseDTO.of(addressService.findAllByUserId(pageable, id), "List address of user");
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:02 SA
     * @description-VN: Kiểm tra email đã tồn tại hoặc có phải là email thật không.
     * @description-EN: Check email existed or not.
     * @param: email - Địa chỉ email muốn kiểm tra.
     * @return:
     *
     * */
    @GetMapping(value = "/check-existed-email")
    public ResponseEntity<?> checkExistedEmail(@RequestParam(value = "email") String email) {
        boolean isExistedEmail = userService.checkExistedEmail(email);
        boolean isFakeEmail = mailService.validationEmail(email);
        if (isFakeEmail) { //Mã lỗi 409
            return ResponseEntity.ok().body(true);
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("fake");
        }
        return ResponseEntity.ok().body(isExistedEmail);
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:03 SA
     * @description-VN: Kích hoạt tài khoản người dùng.
     * @description-EN: Activate user.
     * @param: email - Địa chỉ email muốn kích hoạt, verificationCode - Mã xác nhận.
     * @return:
     *
     * */
    @GetMapping(value = "/active-account")
    public ResponseEntity<?> userConfirmEmail(@RequestParam(value = "email") String email,
                                              @RequestParam(value = "verifycode") int verifyCode) {
        String fullName = userService.findUserByEmail(email).getFullName();
        boolean result = userService.userConfirm(email, verifyCode, fullName);
        return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body("Active account with email: " + email);
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:03 SA
     * @description-VN: Gửi lại email kích hoạt tài khoản.
     * @description-EN: Resend email to activate user.
     * @param: email - Địa chỉ email chưa kích hoạt.
     * @return:
     *
     * */
    @GetMapping(value = "/resend-confirm-email")
    public Object resendConfirmEmail(@RequestParam("email") String email) {
        Boolean result = userService.resendConfirmEmail(email);
        if (result) {
            return ResponseDTO.of(result, "Resend confirmation email");
        } else {
            return ResponseDTO.of(null, "Resend confirmation email");
        }
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:05 SA
     * @description-VN: Tìm người dùng theo mã tài khoản.
     * @description-EN: Find user by user id.
     * @param: id - Mã tài khoản.
     * @return:
     *
     * */
    @GetMapping(value = "/find-by-id")
    public Object getUserById(@RequestParam(value = "id") Long id) {
        return ResponseDTO.of(userService.findById(id), "Find user by id");
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 4:58 SA
     * @description-VN: Lấy danh sách người dùng.
     * @description-EN: Get list user.
     * @param:
     * @return:
     *
     * */
    @GetMapping(value = "/find-all")
    public Object findAllUser(Pageable pageable) {
        return ResponseDTO.show(200, "Find all user", userService.findAll(pageable));
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 6:03 SA
     * @description-VN: Tìm người dùng theo từ khoá.
     * @description-EN: Find user by keyword.
     * @param: keyword - Từ khoá tìm kiếm.
     * @return:
     *
     * */
    @GetMapping(value = "/find-by-keyword")
    public Object findByKeyword(@RequestParam(value = "keyword") String keyword, Pageable pageable) {
        return ResponseDTO.show(200, "Find user by keyword", userService.findUserByKeyword(keyword, pageable));
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 6:16 SA
     * @description-VN: Khoá tài khoản người dùng.
     * @description-EN: Block user.
     * @param: id - Mã tài khoản muốn khoá.
     * @return:
     *
     * */
    @GetMapping(value = "/block-user")
    public Object blockAccount(@RequestParam(value = "id") Long id) {
        return ResponseDTO.show(200, "Block user", userService.blockUser(id) ? true : null);
    }


    /*
     * @author: Manh Tran
     * @since: 15/06/2022 11:30 SA
     * @description-VN: Mở khoá tài khoản.
     * @description-EN: Unblock user.
     * @param:
     * @return:
     *
     * */
    @GetMapping(value = "/unblock-user")
    public Object unblockAccount(@RequestParam(value = "id") Long id) {
        return ResponseDTO.show(200, "Unblock user", userService.unblockUser(id) ? true : null);
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 6:20 SA
     * @description-VN: Nâng tài khoản lên quyền quản trị.
     * @description-EN: Upgrade user to admin.
     * @param: id - Mã tài khoản muốn nâng.
     * @return:
     *
     * */
    @GetMapping(value = "/upgrade-to-admin")
    public Object upgradeToAdmin(@RequestParam(value = "id") Long id) {
        return ResponseDTO.show(200, "Upgrade user to admin", !userService.changeRolesForUser(id, new ArrayList<>()) ? null : true);
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 6:31 SA
     * @description-VN: Kiểm tra tài khoản có bị khoá không.
     * @description-EN: Check user is blocked or not.
     * @param: id - Mã tài khoản muốn kiểm tra.
     * @return:
     *
     * */
    @GetMapping(value = "/check-blocked")
    public Object checkBlocked(@RequestParam(value = "id") Long id) {
        return ResponseDTO.show(200, "Check user is blocked or not", userService.checkBlocked(id));
    }

    /*
     * @author: HaiPhong
     * @since: 17/06/2022 11:52 SA
     * @description-VN:  Gửi mã OTP về email người dùng để xác thực thay đổi mật khẩu
     * @description-EN:  Send OTP code to user email to verify password change
     * @param: userInfoDTO
     * @return:
     *
     * */
    @PostMapping(value = "/change-password")
    public Object changePassword(@RequestBody UserInfoDTO userInfoDTO) {
        return ResponseDTO.of(userService.changePassword(userInfoDTO), "Change password");
    }

    /*
     * @author: HaiPhong
     * @since: 17/06/2022 11:55 SA
     * @description-VN:  Thay đổi mật khẩu của người dùng
     * @description-EN:  Change user's password
     * @param: userInfoDTO
     * @return:
     *
     * */
    @PostMapping(value = "/reset-password")
    public Object resetPassword(@RequestBody UserInfoDTO userInfoDTO) {
        if (userService.resetUserPassword(userInfoDTO)) {
            return ResponseDTO.show(200, "Resset password", userInfoDTO);
        } else {
            return ResponseDTO.show(500, "Resset password", null);
        }
    }
    /*
    * @author: HieuMM
    * @since: 29-Jun-22 11:35 AM
    * @description-VN:  Thong ke nguoi dung moi tu ngay den ngay
    * @description-EN:
    * @param:
    * */
    @GetMapping("/newAccount/{fromDate}/{toDate}")
    public Object newUserInDate(@PathVariable(value = "fromDate") Date fromDate,@PathVariable(value = "toDate") Date toDate){
        return ResponseDTO.of(userService.newUser(fromDate,toDate), "new User from "+fromDate+" to "+toDate);
    }
    /*
    * @author: HieuMM
    * @since: 30-Jun-22 8:24 AM
    * @description-VN:  Danh sách tài khoản được tạo từ ngày đến ngày
    * @description-EN:
    * @param:
    * */
    @GetMapping( "/list-user/{fromDate}/{toDate}")
    public Object listUserInTime(@PathVariable(value = "fromDate") Date fromDate,@PathVariable(value = "toDate") Date toDate,Pageable pageable){
        return ResponseDTO.show(200,"Find all user from "+fromDate+" to "+toDate, userService.listUserInTime(fromDate,toDate,pageable));
    }
    /*
    * @author: HieuMM
    * @since: 29-Jun-22 11:35 AM
    * @description-VN:  Tong so luong nguoi dung
    * @description-EN:
    * @param:
    * */
    @GetMapping("/totalUser")
    public Object totalUser(){
        return ResponseDTO.of(userService.totalUser(), "total User");
    }
    /*
    * @author: HieuMM
    * @since: 29-Jun-22 11:36 AM
    * @description-VN:  Tong so luong nguoi dung bi khoa
    * @description-EN:
    * @param:
    * */
    @GetMapping("/totalUserByStatus")
    public Object totalUserIsLocked(Pageable pageable,Boolean status){
        return ResponseDTO.of(userService.listUserIsLocked(pageable,status), "total user by status : "+status);
    }
/*
* @author: HieuMM
* @since: 30-Jun-22 8:24 AM
* @description-VN:  Danh sách tài khoản bị khóa
* @description-EN:
* @param:
* */
   /* @GetMapping("/list-user-locked")
    public Object listUserIsLocked(Pageable pageable){
        return ResponseDTO.show(200,"Find all user is locked", userService.listUserIsLocked(pageable));
    }*/

//    @PostMapping ("/count")
//    public int count(@RequestParam(name="endPoint") final String endPoint) throws IOException, JSONException
//    {
//        final String URLtoMap = "http://localhost:3000" + endPoint + "";
//        return sendRequestToURL(URLtoMap);
//    }
//    public int sendRequestToURL(@PathVariable("URLtoMap") String URLtoMap) throws IOException, JSONException
//    {
//        int count = 0;
//        StringBuilder result = new StringBuilder();
//        URL url = new URL(URLtoMap);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line;
//        while ((line = rd.readLine()) != null) {
//            result.append(line);
//        }
//        rd.close();
//
//        try {
//            JSONObject jsonObject =new JSONObject(result.toString().replace("\"", ""));
//            JSONObject jsonCountObject = new JSONObject(jsonObject.getJSONArray("measurements").get(0).toString());
//            count =(int) jsonCountObject.get("value");
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println(count);
//        return count;
//    }

}
