# LeadingBooks

## 1. 프로젝트 환경설정

### 사용기술

  * Spring Boot
  * Gradle
  * Thymeleaf
  * JPA
  * BootStrap
  * Tomcat
  * MySQL
  * Spring-Security
  * Java
  * Javascript

## 2. 요구사항 분석

### 기능목록

  회원기능
  
  * 회원등록
  * 회원조회
  * 회원삭제
  * 회원수정

  도서기능

  * 도서등록
  * 도서수정
  * 도서조회
  * 도서대여
  * 도서리뷰
  * 도서반납

  기타요구사항

  * 정지회원관리
  * 도서 연체 관리

## 3. 기능 명세서

### 관리자

  * 도서등록
  * 도서조회

### 회원

  * 토큰과 쿠키를 통한 로그인 기능
  * 첫 가입 시 ADMIN(관리자) 권한
  * 회원가입, 로그인, 로그아웃, 회원탈퇴, 비밀번호 찾기 기능 구현
  * 마이페이지 조회 및 닉네임 변경 기능 구현
  * 비밀번호 찾기 및 회원가입 시 이메일 인증 기능 구현

### 도서

  * 도서 검색기능
  * 각 도서 재고량 확인 기능

### 대여

  * 도서 대여기능
  * 도서 대여기간 연장기능
  * 도서 반납기능

### 리뷰

  * 도서 리뷰기능
  * 도서 세부정보 조회기능

### 정지 회원

  * 대출기한이 지나면 정지회원으로 전환하는 기능
  * 정지기간이 끝나면 일반회원으로 전환하는 기능

## 4. ERD 작성

 <https://www.erdcloud.com/d/RBvpaMc3nasB6h2H3>

## 5. 패키지 구조

  * admin
  * controller
  * domain
  * global
  * services
  * util

## 6. UI 디자인

 <https://www.figma.com/design/rKDpT24NE4VLHQK2MADwTR/leadingbooks?node-id=0-1&t=ONRigkEhbviRfvIS-0>

 
    
  
