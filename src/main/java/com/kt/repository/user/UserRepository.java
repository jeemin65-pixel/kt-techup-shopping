package com.kt.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kt.common.CustomException;
import com.kt.common.ErrorCode;
import com.kt.domain.user.User;

// <T, ID>
// T: Entity 클래스 => User
// ID: Entity 클래스의 PK 타입 => Long

// JpaRepository: 스프링 데이터 JPA가 제공하는 기본 CRUD 인터페이스 (별도로 쿼리 작성하지 않아도 내부에서 자동으로 JPA 쿼리 만들어서 실행)
public interface UserRepository extends JpaRepository<User, Long> { // User entity에 대한 데이터 접근 로직을 정의하는 인터페이스
	//여기에 쿼리를 작성해줘야
	// JPA에서는 쿼리를 작성하는 3가지 방법이 존재
	// 1. 네이티브 쿼리 작성 (3)
	// 2. JPQL 작성 -> 네이티브 쿼리랑 같은데 Entity 기반 (2) - 너무길어진 메소드이름을 그냥 쿼리작성해서 숨김
	// 3. querymethod 작성 -> 메서드 이름을 쿼리 처럼 작성 (1) - 길어지면 상당히 이상해보임
	// 찾는다 : findByXX , 존재하냐? existsByXX, 삭제 : deleteByXX

	Boolean existsByLoginId(String loginId); // 쿼리 메서드 방식으로 만든 메서드 (메서드 이름을 인식해 그에 맞는 쿼리 자동 생성)

	@Query("""
	SELECT exists (SELECT u FROM User u WHERE u.loginId = ?1)
""") // JPQL 작성 예시 (JPQL은 테이블이 아닌 entity 기준으로 쿼리 작성)
	Boolean existsByLoginIdJPQL(String loginId);

	// Page 는 Spring Data 에서 제공해주는 인터페이스
	// Pageable 을 이용해서 현재 페이지, 전체 페이지 수, 정렬 기준 설정 해야함
	// Page<User> 에는 쿼리 결과 값을 포함한 User 안에 있는 관련 필드 + 페이징 메타데이터가 들어있음
	Page<User> findAllByNameContaining(String name, Pageable pageable); // SQL LIKE '%값%' 검색

	default User findByIdOrThrow(Long id, ErrorCode errorCode) { // id 값이 존재하면 해당 id 를 가진 모든 엔티티 필드 값 리턴
		return findById(id).orElseThrow(() -> new CustomException(errorCode)); // .orElseThrow 는 optional 이 비어 있으면 예외 던짐
	}
}
