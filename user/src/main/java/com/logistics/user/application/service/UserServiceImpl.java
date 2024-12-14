package com.logistics.user.application.service;

import com.logistics.user.application.dto.OrderUserDto;
import com.logistics.user.application.dto.UserModifyReqDto;
import com.logistics.user.application.dto.UserRoleUpdateDto;
import com.logistics.user.application.dto.UserSearchResDto;
import com.logistics.user.domain.entity.BaseEntity;
import com.logistics.user.domain.entity.DeliveryManager;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.repository.DeliveryManagerRepository;
import com.logistics.user.domain.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DeliveryManagerRepository deliveryManagerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserSearchResDto findUser(Long userId) {

        User user = userRepository.findByIdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        return UserSearchResDto.toUserResponse(user);
    }

    @Transactional
    @Override
    public UserSearchResDto modifyUser(Long userId, UserModifyReqDto request) {

        User user = userRepository.findByIdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        // 관리자 비밀번호 체크
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Admin password does not match");
        }

        validateUnique(user, request);

        // 수정
        user.userModify(request.getSlackId(), request.getName(), request.getEmail());

        return UserSearchResDto.toUserResponse(user);
    }

    @Transactional
    @Override
    public boolean updateUserRole(Long userId, UserRoleUpdateDto request) {

        User user = userRepository.findByIdAndRoleAndIsDeleteFalse(userId, UserRole.NORMAL)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        if(request.getSourceHubId() == null && request.getVendorId() == null){
            throw new IllegalArgumentException("ID가 없습니다.");
        }

        if(request.getSourceHubId() != null && request.getVendorId() != null){
            throw new IllegalArgumentException("한가지 ID만 요청해주세요");
        }

        if(request.getSourceHubId() != null){
            user.modifyRole(UserRole.HUB_MANAGER);
        }else{
            user.modifyRole(UserRole.VENDOR_MANAGER);
        }

        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public OrderUserDto getUserInfo(Long userId) {

        User user = userRepository.findByIdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        return OrderUserDto.toResponse(user.getName(), user.getSlackId(), user.getEmail());
    }


    @Transactional
    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findByIdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        // TODO user가 삭제될때 deliveryManager도 있으면 같이 삭제해야된다. 근데 배송중이면..? 중간에 터짐..?ㅋㅋ
        // TODO 2개가 잘 없어지는지 체크 해야된다.
        deliveryManagerRepository.findByUserIdAndIsDeleteFalse(userId)
                .ifPresent(BaseEntity::setIsDelete);

        user.setIsDelete();
    }

    // 수정 API시 중복검사!!
    private void validateUnique(User user, UserModifyReqDto userModifyReqDto) {
        List<String> duplicateColumns = new ArrayList<>();
        List<String> presentDuplicateColumns = new ArrayList<>();

        // 현재 사용자의 정보가 같으면 Error

        boolean haveSlackId = userModifyReqDto.getSlackId() != null && userModifyReqDto.getSlackId().isEmpty();
        boolean haveEmail = userModifyReqDto.getEmail() != null && userModifyReqDto.getEmail().isEmpty();
        boolean haveName = userModifyReqDto.getName() != null && userModifyReqDto.getName().isEmpty();

        if (haveSlackId && user.getSlackId().equals(userModifyReqDto.getSlackId())) {
            presentDuplicateColumns.add(user.getSlackId());
        }

        if (haveEmail && user.getEmail().equals(userModifyReqDto.getEmail())) {
            presentDuplicateColumns.add(user.getEmail());
        }

        if (haveName && user.getName().equals(userModifyReqDto.getName())) {
            presentDuplicateColumns.add(userModifyReqDto.getName());
        }

        if (!presentDuplicateColumns.isEmpty()) {
            throw new IllegalStateException("현재 사용자의 정보와 같습니다 : " + String.join(", ", duplicateColumns));
        }

        // 슬랙 ID DB 중복 체크
        if (haveSlackId && userRepository.existsBySlackIdAndIsDeleteFalseAndIdNot(userModifyReqDto.getSlackId(), user.getId())) {
            duplicateColumns.add(userModifyReqDto.getSlackId());
        }

        // Email DB 중복 체크
        if (haveEmail && userRepository.existsByEmailAndIsDeleteFalseAndIdNot(userModifyReqDto.getEmail(), user.getId())) {
            duplicateColumns.add(userModifyReqDto.getEmail());
        }

        // 중복된 컬럼이 있다면 예외 발생
        if (!duplicateColumns.isEmpty()) {
            throw new IllegalStateException("다른 사용자 정보와 중복되었습니다 : " + String.join(", ", duplicateColumns));
        }
    }

}