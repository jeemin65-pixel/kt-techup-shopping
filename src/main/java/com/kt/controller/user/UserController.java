package com.kt.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.SwaggerAssistance;
import com.kt.dto.user.UserRequest;
import com.kt.dto.user.UserUpdatePasswordRequest;
import com.kt.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "유저", description = "유저 관련 API") // Swagger 문서용 어노테이션, 컨트롤러를 '유저 관련 API'로 구분해서
@RestController // 이 클래스가 Spring MVC 의 REST API 컨트롤러임을 의미,
@RequiredArgsConstructor // final이 붙은 필드에 자동으로 생성자를 만들어줌
@RequestMapping("/users")
// @ApiResponses(value = {
// 	@ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
// 	@ApiResponse(responseCode = "500", description = "서버 에러 - 백엔드에 바로 문의 바랍니다.")
// })
public class UserController extends SwaggerAssistance { // 컨트롤러는 요청으로 들어온 JSON을 DTO 로 바꾸고, 그 DTO 를 서비스에게 전달하는 역할 수행
	// userservice를 di받아야함
	// di받는 방식이 생성자주입 씀 -> 재할당을 금지함

	private final UserService userService;

	// API 문서화는 크게 2가지의 방식이 존재
	// 1. Swagger -> 장점 UI가 이쁘다, 어노테이션 기반이라서 작성이 쉽다.
	//	단점: 프로덕션코드에 Swagger관련 어노테이션이 존재
	//	코드가 더러워지고 길어지고 그래서 유지보수가 힘듬
	// 2. RestDocs
	// 1번이랑 정반대
	// 장점 : 프로덕션 코드에 침범이 없다, 신뢰할 수 있음
	// 단점 : UI가 안이쁘다. 그리고 문서작성하는데 테스트코드 기반이라 시간이 걸림.

	@PostMapping // 클라이언트가 POST 방식으로 /users 경로에 요청을 보냈을 때 그 요청을 처리하는 역할
	@ResponseStatus(HttpStatus.CREATED) // 클라이언트가 POST 요청으로 사용자 만들기를 성공했을 때 응답 상태 코드가 201 (Created)로 나감
	// loginId, password, name, birthday
	// json형태의 body에 담겨서 /users로 post (생성) 요청이 들어오면 ( post /users : 새로운 사용자를 생성하겠다는 의미)
	// Spring은 @RequestBody를 보고 json 형태의 body 를 UserCreateRequest (DTO) 타입으로 바꿔야겠다고 인식함
	// JacksonObjectMapper가 동작해서 json을 읽어서 DTO 객체로 변환
	// 뱐환된 DTO (UserCreateRequest) 는 userService.create(request)로 전달 됨
	public ApiResult<Void> create(@Valid @RequestBody UserRequest.Create request) {
		userService.create(request);
		return ApiResult.ok();
	}

	// /users/duplicate-login-id?loginId=ktuser > loginId=ktuser 가 쿼리 스트링
	// IllegalArgumentException 발생 시 400에러
	// GET에서 쓰는 queryString
	// @RequestParam의 속성은 기본이 required = true > 파라미터의 값이 없으면 400 에러 발생
	@GetMapping("/duplicate-login-id") // duplicate-login-id 를 요청 URL 경로로 지정, 데이터 조회용 (서버 상태 영향 X)
	@ResponseStatus(HttpStatus.OK) // 이 메서드가 정상적으로 실행됐을 때 HTTP 응답 상태가 200 (OK)으로 나오도록 설정
	public ApiResult<Boolean> isDuplicateLoginId(@RequestParam String loginId) { // @RequestParam을 통해 loginId 쿼리 파라미터를 받음
		var result = userService.isDuplicateLoginId(loginId);

		return ApiResult.ok(result);
	}

	//uri는 식별이 가능해야한다.
	// 유저들x , 어떤 유저?
	// /users/update-password
	// body => json으로 넣어서 보내고

	// 1. 바디에 id값을 같이 받는다
	// 2. uri에 id값을 넣는다. /users/{id}/update-password
	// 3. 인증/인가 객체에서 id값을 꺼낸다. (V)
	@PutMapping("/{id}/update-password") // 리소스 수정 할 때 사용하는 어노테이션 (전체 교체 or 갱신 요청)
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> updatePassword(
		@PathVariable Long id, // {id} 가 이 코드로 들어감
		@RequestBody @Valid UserUpdatePasswordRequest request
	) {
		userService.changePassword(id, request.oldPassword(), request.newPassword());
		return ApiResult.ok();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> delete(@PathVariable Long id) {
		userService.delete(id);
		return ApiResult.ok();
	}
}
