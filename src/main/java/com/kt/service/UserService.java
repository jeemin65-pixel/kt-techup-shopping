package com.kt.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.common.ErrorCode;
import com.kt.common.Preconditions;
import com.kt.domain.user.User;
import com.kt.dto.user.UserRequest;
import com.kt.repository.user.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service // 비즈니스 로직을 담은 클래스임을 명시, @Component 처럼 bean 으로 등록되지만, 역할 구분을 위해 따로 사용
@RequiredArgsConstructor // final 이나 @NonNull 이 붙은 필드에 대해서만 생성자를 만들어줌
@Transactional // 모두 성공하거나, 실패하거나 원칙, class 위에 사용 되었으므로 모든 public 메서드에 적용
public class UserService {
	private final UserRepository userRepository; // Service > Repository 순이므로 UserService 에 UserRepository 만들어줘야 함

	// 트랜잭션 처리해줘
	// PSA - Portable Service Abstraction
	// 환경설정을 살짝 바꿔서 일관된 서비스를 제공하는 것
	public void create(UserRequest.@Valid Create request) { // 컨트롤러로부터 DTO 를 받아 User 객체로 변환하고 repository 에 저장하는 로직
		var newUser = User.normalUser( // DTO로부터 받은 데이터를 기반으로 새로운 user entity 생성
			request.loginId(),
			request.password(),
			request.name(),
			request.email(),
			request.mobile(),
			request.gender(),
			request.birthday(),
			LocalDateTime.now(),
			LocalDateTime.now()
		);

		userRepository.save(newUser);
	}

	public boolean isDuplicateLoginId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}

	public void changePassword(Long id, String oldPassword, String password) {
		var user = userRepository.findByIdOrThrow(id, ErrorCode.NOT_FOUND_USER);

		//검증 작업
		// 긍정적인 상황만 생각하자 -> 패스워드가 이전것과 달라야 => 해피한
		// 패스워드가 같으면 안되는데 => 넌 해피하지 않은 상황
		Preconditions.validate(user.getPassword().equals(oldPassword), ErrorCode.DOES_NOT_MATCH_OLD_PASSWORD);
		Preconditions.validate(!oldPassword.equals(password), ErrorCode.CAN_NOT_ALLOWED_SAME_PASSWORD);

		user.changePassword(password);
	}


	// Pageable 인터페이스
	public Page<User> search(Pageable pageable, String keyword) {
		return userRepository.findAllByNameContaining(keyword, pageable);
	}

	public User detail(Long id) {
		return userRepository.findByIdOrThrow(id, ErrorCode.NOT_FOUND_USER);
	}

	public void update(Long id, String name, String email, String mobile) {
		var user = userRepository.findByIdOrThrow(id, ErrorCode.NOT_FOUND_USER);

		user.update(name, email, mobile);
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
		// 삭제에는 두가지 개념 - softdelete, harddelete
		// var user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		// userRepository.delete(user);
	}
}
