document.getElementById("writeReviewBtn").addEventListener("click", function () {
    document.getElementById("reviewForm").style.display="block";
});

document.getElementById("reviewSubmitForm").addEventListener("click", function (event) {
    event.preventDefault();
    const reviewData = {
        bId: document.getElementById("bId").value,
        mId: document.getElementById("mId").value,
        rRating: document.getElementById("rRating").value,
        rContent: document.getElementById("rContent").value
    };

    fetch("/api/v1/write/reviews", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reviewData)
    })
        .then(response => response.json())
        .then(data => {
            const reviewList = document.getElementById("reviewList");
            const newReview = document.createElement("li");
            newReview.innerHTML = `<p>닉네임: ${data.mNickname}</p><p>내용: ${data.rContent}</p><p>별점: ${data.rRating}</p>`;
            reviewList.appendChild(newReview);
            document.getElementById("reviewForm").style.display="none";
            document.getElementById("reviewSubmitForm").reset();
        })
        .catch(error => console.error("Error:", error));
});

// 페이지 로드 시 회원 정보를 가져와서 mId 필드를 설정하는 함수
window.addEventListener("load", function () {
    fetch("/api/v1/userInfo", {
        method: "GET"
    })
        .then(response => response.text())
        .then(userId => {
            document.getElementById("mId").value = userId; // 회원 ID 값을 설정
            const myPageLink = document.getElementById("myPageLink");
            myPageLink.href = `/api/v1/myPage/${userId}`;
        })
        .catch(error => console.error("Error:", error));
});

// 도서 대여 버튼 클릭 이벤트 핸들러 추가
document.getElementById("borrowBookBtn").addEventListener("click", function () {
    const bId = document.getElementById("bId").value;
    const mId = document.getElementById("mId").value;

    fetch(`/api/v1/borrow/books?bId=${bId}&mId=${mId}`, {
        method: "GET"
    })
        .then(response => {
            console.log("Response:", response); // 응답 객체를 출력
            return response.json().then(data => {
                console.log("Data:", data); // JSON 데이터를 출력
                if (response.ok) {
                    alert("도서 대여 성공!");
                } else {
                    alert(`도서 대여 실패: ${data.message}`);
                }
            })
                .catch(error => {
                    console.error("Error:", error);
                    alert("도서 대여 실패 : 서버 오류 발생!");
                });

        })
        .catch(error => {
            console.error("Fetch Error:", error);
            alert("도서 대여 실패: 서버 오류 발생!");
        });
});