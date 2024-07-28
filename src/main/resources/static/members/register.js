let isEmailVerified = false;

document.getElementById("emailVerifyButton").addEventListener("click", function () {
    const email = document.getElementById("email").value;
    fetch(`/api/v1/emails/verification-requests`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            "email": email
        })
    }).then(response => {
        if(response.ok) {
            alert("인증 이메일이 전송되었습니다.");
            document.getElementById("verificationForm").style.display = "block";
        } else if(response.status === 409) {
            response.json().then(data => {
                console.log(data);
                alert(data.message);
            });
        } else {
            alert("인증 이메일 전송에 실패하였습니다.");
        }
    }).catch(error => {
        console.error("Error:", error);
        alert("인증 이메일 전송 중 오류가 발생했습니다.");
    });

});

document.getElementById("verifyCodeButton").addEventListener("click", function () {
    var email = document.getElementById("email").value;
    var authCode = document.getElementById("authCode").value;
    fetch(`/api/v1/emails/verifications?email=${encodeURIComponent(email)}&code=${encodeURIComponent(authCode)}`, {
        method: "GET"
    }).then(response => {
        if(response.ok) {
            alert("인증이 완료되었습니다.");
            isEmailVerified = true;
            document.getElementById("signUpButtonContainer").style.display = "block";
            document.getElementById("signUpButton").disabled = false;
        } else if(response.status === 404) {
            response.json().then(data => {
                alert(data.message);
            });
        } else {
            alert("인증에 실패하였습니다. 다시 시도해주세요.");
        }
    }).catch(error => {
        console.error("Error:", error);
        alert("인증 과정에서 오류가 발생했습니다.");
    });
});