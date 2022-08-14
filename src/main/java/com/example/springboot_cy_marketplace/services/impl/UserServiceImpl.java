package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.MailDTO;
import com.example.springboot_cy_marketplace.dto.ResetPasswordDTO;
import com.example.springboot_cy_marketplace.dto.UserDTO;
import com.example.springboot_cy_marketplace.dto.UserInfoDTO;
import com.example.springboot_cy_marketplace.entity.*;
import com.example.springboot_cy_marketplace.jwt.JwtTokenProvider;
import com.example.springboot_cy_marketplace.jwt.payload.request.*;
import com.example.springboot_cy_marketplace.jwt.payload.response.LoginResponse;
import com.example.springboot_cy_marketplace.model.CityModel;
import com.example.springboot_cy_marketplace.model.UserModel;
import com.example.springboot_cy_marketplace.repository.IAddressRepository;
import com.example.springboot_cy_marketplace.repository.IRoleRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.security.impl.UserDetailsImpl;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IUserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 3 * 60 * 1000; // 3 minutes
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IAddressRepository addressRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MailService mailService;
    @Autowired
    CityServiceImpl cityService;
    @Autowired
    DistrictServiceImpl districtService;
    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    AddressServiceImpl addressService;
    @Autowired
    WardServiceImpl wardService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AmazonClient amazonClient;
    @Value("${avatar.default.url}")
    private String defaultAvatarUrl;

    @Value("${server.host.fe.user}")
    private String feHost;

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:55 CH
     * @description-VN:  Xác thực người dùng
     * @description-EN:  Valid User
     * @param: userDetails
     * @return:
     *
     * */
    public void validUser(UserDetailsImpl userDetails) throws Exception {
        if (userDetails == null) throw new Exception("User not found !");
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername());
        if (!userEntity.isEnabled()) throw new Exception("User is enabled !");
        if (!userEntity.isAccountNonLocked()) throw new Exception("Account locked !");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:55 CH
     * @description-VN:  Đăng nhập thành công
     * @description-EN:  Login success
     * @param: authentication, loginRequest
     * @return:
     *
     * */
    @Override
    public Object loginSuccess(LoginRequest loginRequest) {

        UserDetailsImpl userDetails = new UserDetailsImpl(userRepository.findByEmail(loginRequest.getEmail()));

        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword(),
                        userDetails.getAuthorities()
                )
        );
        try {
            this.validUser(userDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        boolean rememberme = false;
        long timeValid = 18000l;
        if (loginRequest.isRememberme()){
            rememberme = true;
            timeValid = 86400l * 7;
        }
//        long timeValid = loginRequest.isRememberme() ? 86400l * 7 : 18000l;

        return LoginResponse.builder()
                .id(userDetails.getUserEntity().getId())
                .email(userDetails.getUsername())
                .rememberme(rememberme)
                .accessToken(jwtTokenProvider.generateTokenFormUsername(userDetails.getUsername(), timeValid))
                .tokenType(new LoginResponse().getTokenType())
                .role(userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public boolean checkOldPassWord(Long id, String oldPass) {
        UserEntity userEntity = userRepository.findById(id).get();
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getEmail(), oldPass));
        if (authentication == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean changePassword(UserInfoDTO userInfoDTO) {
        int leftLimit = 1000;
        int verifyCode = leftLimit * (1 + new Random().nextInt(9)) + new Random().nextInt(1000);
        UserEntity userEntity = userRepository.findByEmail(userInfoDTO.getEmail());
        userEntity.setVerifyCode(verifyCode);

        this.sendMailChangeInfoEmail(verifyCode, userEntity);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public boolean resetUserPassword(UserInfoDTO userInfoDTO) {
        UserEntity userEntity = userRepository.findByEmail(userInfoDTO.getEmail());

        if (userEntity.getVerifyCode() == userInfoDTO.getVerifyCode()) {
            userEntity.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
            userEntity.setVerifyCode(-1);
            userRepository.save(userEntity);
            return true;
        } else {
            return false;
        }
    }

    public void increaseFailedAttempts(UserEntity user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String username) {
        userRepository.updateFailedAttempts(0, username);
    }

    public void lock(UserEntity user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(UserEntity user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepository.save(user);

            return true;
        }

        return false;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:56 CH
     * @description-VN:  Hàm này để tạo câu hỏi mới.
     * @description-EN:  This function for adding question.
     * @param: userSignup
     * @return:
     *
     * */
    public boolean add(RegisterRequest userSignup) {
        UserEntity userEntity = this.registerRequestToEntity(userSignup);
        int leftLimit = 1000;
        int verifyCode = leftLimit * (1 + new Random().nextInt(9)) + new Random().nextInt(1000);
        userEntity.setVerifyCode(verifyCode);
        userEntity.setAvatar(defaultAvatarUrl);
        try {
            this.addressRepository.save(userEntity.getAddressEntityList().get(0));
            this.userRepository.save(userEntity);
            this.sendConfirmEmail(verifyCode, userSignup);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }
    public boolean updateDefaultAdd(UpdateDefaultAddressRequest userUpdate) {
        AddressEntity addressEntity = this.updateDefaultAddress(userUpdate);
        try {
            this.addressRepository.save(addressEntity);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }

    private AddressEntity updateDefaultAddress(UpdateDefaultAddressRequest obj) {
        List<AddressEntity> addressEntityList =  addressRepository.findAllByUserEntityList_id(obj.getIdUser());
        for (AddressEntity address:addressEntityList) {
            address.setId(address.getId());
            address.setCity(address.getCity());
            address.setDistrict(address.getDistrict());
            address.setWard(address.getWard());
            address.setAddress(address.getAddress());
            address.setIdCityGHN(address.getIdCityGHN());
            address.setIdDistrictGHN(address.getIdDistrictGHN());
            address.setDefaultAdd(false);
        }
        Optional<AddressEntity> addOld=addressRepository.findById(obj.getIdAddress());
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(obj.getIdAddress());
        addressEntity.setCity(addOld.get().getCity());
        addressEntity.setDistrict(addOld.get().getDistrict());
        addressEntity.setWard(addOld.get().getWard());
        addressEntity.setAddress(addOld.get().getAddress());
        addressEntity.setIdCityGHN(addOld.get().getIdCityGHN());
        addressEntity.setIdDistrictGHN(addOld.get().getIdDistrictGHN());
        addressEntity.setDefaultAdd(true);

        return addressEntity;
    }

    /*
     * @author: HieuMM
     * @since: 20-Jun-22 3:11 PM
     * @description-VN:  cập nhật email
     * @description-EN:
     * @param:
     * */
    public boolean updateProfile(UpdateProfileRequest userUpdate) {
        UserEntity userEntity = this.updateRequestToEntity(userUpdate);
        if (!userEntity.getEmail().equals(userUpdate.getEmail())) {
            int leftLimit = 1000;
            int verifyCode = leftLimit * (1 + new Random().nextInt(9)) + new Random().nextInt(1000);
            userEntity.setVerifyCode(verifyCode);
            try {
                this.userRepository.save(userEntity);
                this.sendConfirmEmail(verifyCode, userUpdate);
                return true;
            } catch (DataIntegrityViolationException ex) {
                return false;
            }
        } else {
            try {
                this.userRepository.save(userEntity);
                return true;
            } catch (DataIntegrityViolationException ex) {
                return false;
            }
        }

    }

    private UserEntity updateRequestToEntity(UpdateProfileRequest obj) {
        UserEntity oldUserEntity = userRepository.findById(obj.getId()).get();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(obj.getId());
        userEntity.setCreateBy(oldUserEntity.getCreateBy());
        userEntity.setCreateDate(oldUserEntity.getCreateDate());
        userEntity.setEnabled(true);
        userEntity.setPassword(oldUserEntity.getPassword());
        userEntity.setFullName(obj.getFullName());
        userEntity.setEmail(obj.getEmail());
        //  String avatarUrl = amazonClient.uploadFilewithFolder(obj.getAvatarFile(), "avatar");
        userEntity.setAvatar(oldUserEntity.getAvatar());
        userEntity.setPhoneNumber(obj.getPhoneNumber());
        userEntity.setAddressEntityList(oldUserEntity.getAddressEntityList());
        List<RoleEntity> roleEntityList = new ArrayList<>();
        userEntity.setRoleEntityList(oldUserEntity.getRoleEntityList());
        return userEntity;
    }



    /*
     * @author: HieuMM
     * @since: 22-Jun-22 11:13 AM
     * @description-VN:  sửa ảnh user
     * @description-EN:  update avatar user
     * @param:
     * */
    public boolean updateAvatarProfile(UpdateAvatarProfileRequest userUpdate) {
        UserEntity userEntity = this.updateAvatar(userUpdate);
        try {
            this.userRepository.save(userEntity);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }

}
    /*
     * @author: HieuMM
     * @since: 20-Jun-22 3:11 PM
     * @description-VN:  cập nhật địa chỉ
     * @description-EN:
     * @param:
     * */
    public boolean updateAddressProfile(UpdateAddressProfileRequest userUpdate) {
        AddressEntity addressEntity = this.updateAddress(userUpdate);
        try {
            this.addressRepository.save(addressEntity);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }

    }

    private AddressEntity updateAddress(UpdateAddressProfileRequest obj) {

        CityModel cityModel = cityService.findByID(obj.getCityId());
        DistrictEntity districtEntity = districtService.findById(obj.getDistrictId());
        WardEntity wardEntity = wardService.findById(obj.getWardId());
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(obj.getId());
        addressEntity.setCity(cityModel.getName());
        addressEntity.setDistrict(districtEntity.getName());
        addressEntity.setWard(wardEntity.getName());
        addressEntity.setAddress(obj.getAddress());
        addressEntity.setIdCityGHN(cityModel.getIdGHN());
        addressEntity.setIdDistrictGHN(districtEntity.getIdGHN());
        addressEntity.setDefaultAdd(false);

        return addressEntity;
    }

    /*
    * @author: HieuMM
    * @since: 22-Jun-22 10:31 AM
    * @description-VN:  Thêm địa chỉ mới
    * @description-EN:  Add new address of user in profile
    * @param:
    * */
    public boolean addAddressProfile(UpdateAddressProfileRequest userUpdate) {
        UserEntity userEntity = this.addNewAddress(userUpdate);
        try {
            this.userRepository.save(userEntity);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }


    }


    /*
     * @author: HieuMM
     * @since: 20-Jun-22 3:11 PM
     * @description-VN:  cập nhật mật khẩu
     * @description-EN:
     * @param:
     * */
    public boolean updatePassword(UpdatePasswordRequest userUpdate) {
        UserEntity userEntity = this.updatePasswordToEntity(userUpdate);
        try {
            this.userRepository.save(userEntity);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }


    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:56 CH
     * @description-VN:  Hàm này để kiểm tra xem địa chỉ email đã tồn tại trong CSDL chưa.
     * @description-EN:  This function for checking if email address existed in database.
     * @param: email
     * @return:
     *
     * */
    public boolean checkExistedEmail(String email) {
        UserEntity result = userRepository.findByEmail(email);
        if (result == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:57 CH
     * @description-VN:  Hàm này để gửi email chào mừng người dùng sau khi họ kích hoạt tài khoản.
     * @description-EN:  This function for sending welcome email after they activing their's account.
     * @param: fullName, receiveEmail
     * @return:
     *
     * */
    public void sendWelcomeEmail(String fullName, String receiveEmail) {
        MailDTO mail = new MailDTO();
        mail.setFrom("Trần Hoàng Mạnh");
        mail.setMailTo(receiveEmail);
        mail.setSubject("Chào mừng bạn đến với thế giới mua sắm");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fullName", fullName);
        model.put("homeUrl", feHost);
        mail.setProps(model);
        try {
            mailService.customTemplateEmail(mail, "welcome");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:58 CH
     * @description-VN:  Hàm này để gủi email chứa mã số bí mật để người dùng kích hoạt tài khoản
     * @description-EN:  This function for sending email include secret code for user to active account.
     * @param: verifyCode, userSignup
     * @return:
     *
     * */
    public void sendConfirmEmail(int verifyCode, RegisterRequest userSignup) {
        MailDTO mail = new MailDTO();
        mail.setFrom("Trần Hoàng Mạnh");
        mail.setMailTo(userSignup.getEmail());
        mail.setSubject("Mã xác nhận tài khoản");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("verifyCode", verifyCode);
        model.put("fullName", userSignup.getFullName());
        mail.setProps(model);
        try {
            mailService.customTemplateEmail(mail, "confirm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendConfirmEmail(int verifyCode, UpdateProfileRequest userSignup) {
        MailDTO mail = new MailDTO();
        mail.setFrom("Trần Hoàng Mạnh");
        mail.setMailTo(userSignup.getEmail());
        mail.setSubject("Mã xác nhận tài khoản");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("verifyCode", verifyCode);
        model.put("fullName", userSignup.getFullName());
        mail.setProps(model);
        try {
            mailService.customTemplateEmail(mail, "confirm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:58 CH
     * @description-VN:  Hàm này để người dùng kích hoạt tài khoản.
     * @description-EN:  This function for activing user's account.
     * @param: email, verifyCode, fullName
     * @return:
     *
     * */
    public boolean userConfirm(String email, int verifyCode, String fullName) {
        UserEntity userEntity = userRepository.findByEmailAndVerifyCode(email, verifyCode);
        if (userEntity != null && verifyCode != -1) {
            userEntity.setEnabled(true);
            userEntity.setVerifyCode(-1);
            userRepository.save(userEntity);
            this.sendWelcomeEmail(fullName, email);
            return true;
        } else {
            return false;
        }
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:58 CH
     * @description-VN:  Hàm này để chuyển dữ liệu nhận được khi người dùng đăng kí về entity.
     * @description-EN:  This function convert data which received when user registered to entity.
     * @param: obj
     * @return: UserEntity
     *
     * */
    private UserEntity registerRequestToEntity(RegisterRequest obj) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFullName(obj.getFullName());
        userEntity.setPassword(passwordEncoder.encode(obj.getPassword()));
        userEntity.setEmail(obj.getEmail());
        userEntity.setFullName(obj.getFullName());
        CityModel cityModel = cityService.findByID(obj.getCityId());

        DistrictEntity districtEntity = districtService.findById(obj.getDistrictId());
        WardEntity wardEntity = wardService.findById(obj.getWardId());

        AddressEntity addressEntity = new AddressEntity();
        List<AddressEntity> addressEntityList = new ArrayList<>();
        if (cityModel != null && districtEntity != null && wardEntity != null && obj.getAddress() != null) {
            AddressEntity fAddressEntity = addressService.findAddressEntity(cityModel.getName(),
                    districtEntity.getName(), wardEntity.getName(), obj.getAddress());
            if (fAddressEntity == null) {
                addressEntity.setCity(cityModel.getName());
                addressEntity.setDistrict(districtEntity.getName());
                addressEntity.setWard(wardEntity.getName());
                addressEntity.setIdCityGHN(cityModel.getIdGHN());
                addressEntity.setIdDistrictGHN(districtEntity.getIdGHN());
                addressEntity.setDefaultAdd(true);
            }
            addressEntityList.add(addressEntity);
        } else {
            addressEntity.setCity("N/A");
            addressEntity.setDistrict("N/A");
            addressEntity.setWard("N/A");
            addressEntity.setAddress("N/A");
            addressEntity.setDefaultAdd(true);
            addressEntityList.add(addressEntity);
        }
        userEntity.setAddressEntityList(addressEntityList);

        List<RoleEntity> roleEntityList = new ArrayList<>();
        //Mặc định là ROLE_BUYER
        RoleEntity roleEntity = roleRepository.findById(1L).get();
        roleEntityList.add(roleEntity);
        userEntity.setRoleEntityList(roleEntityList);
        return userEntity;
    }

    private UserEntity RequestToEntity(UpdateProfileRequest obj) {
        UserEntity oldUserEntity = userRepository.findById(obj.getId()).get();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(obj.getId());
        userEntity.setCreateBy(oldUserEntity.getCreateBy());
        userEntity.setCreateDate(oldUserEntity.getCreateDate());
        userEntity.setEnabled(true);
        userEntity.setPassword(oldUserEntity.getPassword());
        userEntity.setFullName(obj.getFullName());
        userEntity.setEmail(obj.getEmail());
      //  String avatarUrl = amazonClient.uploadFilewithFolder(obj.getAvatarFile(), "avatar");
        userEntity.setAvatar(oldUserEntity.getAvatar());
        userEntity.setPhoneNumber(obj.getPhoneNumber());
        userEntity.setAddressEntityList(oldUserEntity.getAddressEntityList());
        List<RoleEntity> roleEntityList = new ArrayList<>();
        userEntity.setRoleEntityList(oldUserEntity.getRoleEntityList());
        return userEntity;
    }
    private UserEntity updateAvatar(UpdateAvatarProfileRequest obj){
        UserEntity oldUserEntity = userRepository.findById(obj.getId()).get();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(obj.getId());
        userEntity.setCreateBy(oldUserEntity.getCreateBy());
        userEntity.setCreateDate(oldUserEntity.getCreateDate());
        userEntity.setEnabled(true);
        userEntity.setPassword(oldUserEntity.getPassword());
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setEmail(oldUserEntity.getEmail());
        String avatarUrl = amazonClient.uploadFilewithFolder(obj.getAvatarFile(), "avatar");
        userEntity.setAvatar(avatarUrl);
        userEntity.setPhoneNumber(oldUserEntity.getPhoneNumber());
        userEntity.setAddressEntityList(oldUserEntity.getAddressEntityList());
        List<RoleEntity> roleEntityList = new ArrayList<>();
        userEntity.setRoleEntityList(oldUserEntity.getRoleEntityList());
        return userEntity;
    }

    public void sendMailResetPassword(String email) throws UnsupportedEncodingException {
        String tokenString = RandomString.make(45);
        MailDTO mail = new MailDTO();
        mail.setFrom("Trần Hoàng Mạnh");
        mail.setMailTo(email);
        mail.setSubject("Thay đổi mật khẩu");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("tokenString", tokenString);
        model.put("url", feHost + "/resetpasswd?token=");
        mail.setProps(model);
        try {
            this.mailService.sendTextMail(mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        updateUserToken(email, tokenString);
    }

    private UserEntity updateAddressRequestToEntity(UpdateAddressProfileRequest obj) {
        UserEntity oldUserEntity = userRepository.findById(obj.getId()).get();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(obj.getId());
        userEntity.setCreateBy(oldUserEntity.getCreateBy());
        userEntity.setCreateDate(oldUserEntity.getCreateDate());
        userEntity.setEnabled(true);
        userEntity.setPassword(oldUserEntity.getPassword());
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setEmail(oldUserEntity.getEmail());
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setAvatar(oldUserEntity.getAvatar());
        userEntity.setPhoneNumber(oldUserEntity.getPhoneNumber());
        userEntity.setAddressEntityList(oldUserEntity.getAddressEntityList());


        CityModel cityModel = cityService.findByID(obj.getCityId());
        DistrictEntity districtEntity = districtService.findById(obj.getDistrictId());
        WardEntity wardEntity = wardService.findById(obj.getWardId());

        AddressEntity addressEntity = new AddressEntity();
        List<AddressEntity> addressEntityList = new ArrayList<>();
        AddressEntity fAddressEntity = null;
        if (cityModel != null && districtEntity != null && wardEntity != null && obj.getAddress() != null) {
            fAddressEntity = addressService.findAddressEntity(cityModel.getName(),
                    districtEntity.getName(), wardEntity.getName(), obj.getAddress());
            if (fAddressEntity == null) {
                addressEntity.setCity(cityModel.getName());
                addressEntity.setDistrict(districtEntity.getName());
                addressEntity.setWard(wardEntity.getName());
                addressEntity.setAddress(obj.getAddress());
                addressEntity.setIdCityGHN(cityModel.getIdGHN());
                addressEntity.setIdDistrictGHN(districtEntity.getIdGHN());
            }
            addressEntityList.add(addressEntity);
        } else {
            addressEntity.setCity("N/A");
            addressEntity.setDistrict("N/A");
            addressEntity.setWard("N/A");
            addressEntity.setAddress("N/A");
            addressEntityList.add(addressEntity);
        }
        userEntity.setAddressEntityList(addressEntityList);
        /*List<RoleEntity> roleEntityList = new ArrayList<>();
        //Mặc định là ROLE_BUYER
        RoleEntity roleEntity = roleRepository.findById(1L).get();
        roleEntityList.add(roleEntity);*/
        userEntity.setRoleEntityList(oldUserEntity.getRoleEntityList());
        return userEntity;
    }

    private UserEntity addNewAddress(UpdateAddressProfileRequest obj) {
        UserEntity oldUserEntity = userRepository.findById(obj.getId()).get();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(obj.getId());
        userEntity.setCreateBy(oldUserEntity.getCreateBy());
        userEntity.setCreateDate(oldUserEntity.getCreateDate());
        userEntity.setEnabled(true);
        userEntity.setPassword(oldUserEntity.getPassword());
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setEmail(oldUserEntity.getEmail());
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setAvatar(oldUserEntity.getAvatar());
        userEntity.setPhoneNumber(oldUserEntity.getPhoneNumber());



        userEntity.setAddressEntityList(oldUserEntity.getAddressEntityList());

        CityModel cityModel = cityService.findByID(obj.getCityId());
        DistrictEntity districtEntity = districtService.findById(obj.getDistrictId());
        WardEntity wardEntity = wardService.findById(obj.getWardId());

        AddressEntity addressEntity = new AddressEntity();
       /* List<AddressEntity> addressEntityList = new ArrayList<>();*/
        AddressEntity fAddressEntity = null;

        if (cityModel != null && districtEntity != null && wardEntity != null && obj.getAddress() != null) {
           /* fAddressEntity = addressService.findAddressEntity(cityModel.getName(),
                    districtEntity.getName(), wardEntity.getName(), obj.getAddress());*/
            if (fAddressEntity == null) {
                addressEntity.setCity(cityModel.getName());
                addressEntity.setDistrict(districtEntity.getName());
                addressEntity.setWard(wardEntity.getName());
                addressEntity.setAddress(obj.getAddress());
                addressEntity.setIdCityGHN(cityModel.getIdGHN());
                addressEntity.setIdDistrictGHN(districtEntity.getIdGHN());
                addressEntity.setDefaultAdd(false);
            }
            userEntity.getAddressEntityList().add(addressEntity);
        } else {
            addressEntity.setCity("N/A");
            addressEntity.setDistrict("N/A");
            addressEntity.setWard("N/A");
            addressEntity.setAddress("N/A");
           // addressEntity.add(addressEntity);
        }
     //   userEntity.setAddressEntityList(addressEntityList);

     //   userEntity.setAddressEntityList(addressEntityList);
        userEntity.setRoleEntityList(oldUserEntity.getRoleEntityList());
        return userEntity;
    }

    private UserEntity updatePasswordToEntity(UpdatePasswordRequest obj) {
        UserEntity oldUserEntity = userRepository.findById(obj.getId()).get();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(obj.getId());
        userEntity.setCreateBy(oldUserEntity.getCreateBy());
        userEntity.setCreateDate(oldUserEntity.getCreateDate());
        userEntity.setEnabled(true);
        userEntity.setPassword(passwordEncoder.encode(obj.getPassword()));
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setEmail(oldUserEntity.getEmail());
        userEntity.setFullName(oldUserEntity.getFullName());
        userEntity.setAvatar(oldUserEntity.getAvatar());
        userEntity.setPhoneNumber(oldUserEntity.getPhoneNumber());
        userEntity.setAddressEntityList(oldUserEntity.getAddressEntityList());
        List<RoleEntity> roleEntityList = new ArrayList<>();
        userEntity.setRoleEntityList(oldUserEntity.getRoleEntityList());
        return userEntity;
    }

    public void updateUserToken(String email, String token) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setResetPassWordToken(token);
        userRepository.save(userEntity);
    }

    public UserModel resetPassword(ResetPasswordDTO resetPasswordDTO) {
        UserEntity userEntity = userRepository.findByResetPassWordToken(resetPasswordDTO.getToken());
        userEntity.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        userEntity.setResetPassWordToken(null);
        return UserModel.entityToModel(userRepository.save(userEntity));
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:59 CH
     * @description-VN:  Hàm này để gửi lại email chứa mã kích hoạt tài khoản.
     * @description-EN:  This function for resending confirmation email.
     * @param: email
     * @return:
     *
     * */
    public Boolean resendConfirmEmail(String email) {
        UserEntity result = this.userRepository.findByEmail(email);
        if (result == null) {
            return false;
        } else if (result.getVerifyCode() != -1) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(email);
            registerRequest.setFullName(result.getFullName());
            this.sendConfirmEmail(result.getVerifyCode(), registerRequest);
            return true;
        } else {
            return false;
        }
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 5:38 SA
     * @description-VN: Thay đổi quyền cho người dùng.
     * @description-EN: Change user's role.
     * @param: userId - Mã người dùng muốn đổi quyền, roleId - Danh sách các quyền.
     * @return:
     *
     * */
    public boolean changeRolesForUser(Long userId, List<String> roleNames) {
        if (roleNames.size() == 0) {
            roleNames.add(this.roleRepository.findByName("ROLE_BUYER").getName());
            roleNames.add(this.roleRepository.findByName("ROLE_SELLER").getName());
            roleNames.add(this.roleRepository.findByName("ROLE_ADMIN").getName());
        }
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (!optionalUserEntity.isPresent()) {
            return false;
        }
        UserEntity userEntity = optionalUserEntity.get();
        List<RoleEntity> roleEntityList = new ArrayList<>();
        for (String roleName : roleNames) {
            RoleEntity roleEntity = roleRepository.findByName(roleName);
            if (roleEntity != null) {
                roleEntityList.add(roleEntity);
            }
        }
        if (roleEntityList.size() == 0) {
            return false;
        }
        userEntity.setRoleEntityList(roleEntityList);
        userRepository.save(userEntity);
        return true;
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 5:40 SA
     * @description-VN: Tìm kiếm tài khoản người dùng theo từ khoá.
     * @description-EN: Find user by keyword.
     * @param: keyword - Từ khoá tìm kiếm.
     * @return:
     *
     * */
    public Page<UserModel> findUserByKeyword(String keyword, Pageable pageable) {
        List<UserModel> result = this.userRepository.findByKeyword(keyword).stream().map(UserModel::entityToModel).collect(Collectors.toList());
        final long start = pageable.getOffset();
        final long end = Math.min(start + pageable.getPageSize(), result.size());
        return new PageImpl<>(result.subList((int) start, (int) end), pageable, result.size());
    }

    @Override
    public List<UserModel> findAll() {
        return null;
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 5:39 SA
     * @description-VN: Lấy tất cả người dùng.
     * @description-EN: Get all users.
     * @param: page - Phân trang.
     * @return:
     *
     * */
    @Override
    public Page<UserModel> findAll(Pageable page) {
        Pageable page2 = PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSort().isEmpty() ? Sort.by("createDate").descending() : page.getSort());
        return this.userRepository.findAll(page2).map(UserModel::entityToModel);
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 6:11 SA
     * @description-VN: Khoá tài khoản người dùng.
     * @description-EN: Block user.
     * @param: id - Mã tài khoản muốn khóa.
     * @return:
     *
     * */
    public boolean blockUser(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()) {
            return false;
        }

        UserEntity userEntity = optionalUserEntity.get();
        RoleEntity roleEntity = roleRepository.findByName("ROLE_ADMIN"); //id: 3
        if (userEntity.getRoleEntityList().contains(roleEntity) || !userEntity.isEnabled()) {
            return false;
        }

        userEntity.setEnabled(false);
        this.userRepository.save(userEntity);
        return true;
    }

    /*
     * @author: Manh Tran
     * @since: 15/06/2022 11:29 SA
     * @description-VN: Mở khoá tài khoản.
     * @description-EN: Unblock user.
     * @param: id - Mã tài khoản muốn mở khoá.
     * @return:
     *
     * */

    public boolean unblockUser(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()) {
            return false;
        }

        UserEntity userEntity = optionalUserEntity.get();
        RoleEntity roleEntity = roleRepository.findByName("ROLE_ADMIN"); //id: 3
        if (userEntity.getRoleEntityList().contains(roleEntity) || userEntity.isEnabled()) {
            return false;
        }

        userEntity.setEnabled(true);
        this.userRepository.save(userEntity);
        return true;
    }

    /*
     * @author: Manh Tran
     * @since: 14/06/2022 6:32 SA
     * @description-VN: Kiểm tra tài khoản có bị khoá không.
     * @description-EN: Check user is blocked or not.
     * @param:
     * @return:
     *
     * */
    public boolean checkBlocked(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()) {
            return false;
        }
        return !optionalUserEntity.get().isAccountNonLocked();
    }


    public void sendMailChangeInfoEmail(int verifycode, UserEntity userEntity) {
        MailDTO mail = new MailDTO();
        mail.setFrom("Trần Hoàng Mạnh");
        mail.setMailTo(userEntity.getEmail());
        mail.setSubject("Mã OTP để xác minh mật khẩu mới");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fullName", userEntity.getFullName());
        model.put("verifyCode", verifycode);
        model.put("url", feHost);
        mail.setProps(model);
        try {
            mailService.sendMailchangeInfoMail(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserModel findById(Long id) {
        return UserModel.entityToModel(userRepository.findById(id).get());
    }

    @Override
    public UserModel add(UserDTO model) {
        return null;
    }

    @Override
    public UserModel update(UserDTO model) {

        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List id) {
        return false;
    }

    @Override
    public List<RegisterRequest> add(List model) {
        return null;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:59 CH
     * @description-VN:  Tìm kiếm người dùng bằng id
     * @description-EN:  Find user by id
     * @param: email
     * @return: UserEntity
     *
     * */
    public UserEntity findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Object newUser(java.sql.Date startDate, java.sql.Date endDate) {
        int sum=userRepository.countNewUser( startDate, endDate);
        return sum;
    }

    public Object totalUser(){
        int totalUser=userRepository.totalUser();
        int totalUserIsLocked=userRepository.totalUserIsLocked();
        int totalUserIsUnlocked=totalUser-totalUserIsLocked;
        HashMap<String, Integer> user = new HashMap<String, Integer>();

        // Add keys and values (Country, City)
        user.put("Total", totalUser);
        user.put("totalUserIsLocked", totalUserIsLocked);
        user.put("totalUserIsUnlocked",totalUserIsUnlocked);
       // return Arrays.asList(totalUser,totalUserIsLocked,totalUserIsUnlocked);
        return user;
    }
    public Object totalUserIsLocked(){
        int sum=userRepository.totalUserIsLocked();
        return sum;
    }
    public Page<UserModel> listUserIsLocked(Pageable page,Boolean status) {
        Pageable page2 = PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSort().isEmpty() ? Sort.by("createDate").descending() : page.getSort());
        return this.userRepository.listUser(page2,status).map(UserModel::entityToModel);
    }
    public Page<UserModel> listUserInTime( java.sql.Date d1, java.sql.Date d2,Pageable page) {
        Pageable page2 = PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSort().isEmpty() ? Sort.by("createDate").descending() : page.getSort());
        return this.userRepository.listUserInTime(d1,d2,page).map(UserModel::entityToModel);
    }
}
