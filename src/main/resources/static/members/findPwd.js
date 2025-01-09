document.getElementById("findPwdForm").addEventListener("submit", function(event) {
    event.preventDefault(); // 기본 폼 제출을 막음

    const formData = new FormData(this);

    fetch(this.action, {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // 요청이 성공적으로 완료된 경우
                alert("비밀번호가 변경되었습니다."); // 알림 메시지 표시
                setTimeout(() => {
                    window.location.href = "/sign/up"; // 회원가입 페이지로 리다이렉트
                }, 2000); // 2초 후 리다이렉트
            } else {
                // 요청이 실패한 경우
                return response.json().then(errorResponse => {
                    alert(`비밀번호 변경 실패: ${errorResponse.message}`);
                });
            }
        })
        .catch(error => {
            alert("오류가 발생했습니다: " + error.message); // 오류 메시지 표시
        });
});