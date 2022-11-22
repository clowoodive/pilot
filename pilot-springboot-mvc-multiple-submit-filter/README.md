# Custom Filter를 등록해서 Multiple Submit을 방지
    - Spring Security의 CsrfFilter와 유사한 Filter를 추가하고, CSRF 히든 필드를 폼에 삽입하는 bean을 재정의 해 Form Multiple Submit(FMS) 토큰을 Form에 삽입함.
    - 요청의 method에 따라 토큰 생성/갱신 하는 방식은 CSRF를 그대로 따름.