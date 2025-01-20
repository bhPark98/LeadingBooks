document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("writeReviewBtn").addEventListener("click", function () {
        document.getElementById("reviewForm").style.display = "block";
    });

    document.getElementById("reviewForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const reviewData = {
            bId: document.getElementById("bId").value,
            mId: document.getElementById("detail_mId").value,
            rRating: document.getElementById("rRating").value,
            rContent: document.getElementById("rContent").value
        };

        fetch("/write/reviews", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: 'include',
            body: JSON.stringify(reviewData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.redirectUrl) {
                    let reviewList = document.getElementById("reviewList");
                    // reviewList가 null인 경우 새로운 요소 생성
                    if (!reviewList) {
                        reviewList = document.createElement("ul"); // 새로운 ul 요소 생성
                        reviewList.id = "reviewList"; // ID 설정
                        document.body.appendChild(reviewList); // body에 추가 (적절한 위치에 추가)
                    }

                        const newReview = document.createElement("li");
                        newReview.innerHTML = `<p>닉네임: ${data.mNickname}</p><p>내용: ${data.rContent}</p><p>별점: ${data.rRating}</p>`;
                        reviewList.appendChild(newReview);

                        // 리뷰 폼 숨기기
                        document.getElementById("reviewForm").style.display = "none";
                        // 폼 초기화
                        document.getElementById("reviewSubmitForm").reset(); // reviewSubmitForm을 초기화
                        // 현재 페이지로 리다이렉트
                        window.location.href = `${data.redirectUrl}?bId=${data.bId}`;
                        console.log(data.redirectUrl);
                    } else {
                        console.error("reviewList 요소를 찾을 수 없습니다.");
                }
            })
            .catch(error => console.error("Error:", error));
    });
});

document.querySelectorAll('.deleteReviewBtn').forEach(button => {
    button.addEventListener('click', function () {
        const reviewId = this.getAttribute('data-review-id');   // 리뷰 id 가져오기
        console.log('reviewId:', reviewId);

        fetch(`/delete/review?rId=${reviewId}`, {
            method: "DELETE",
            credentials: 'include'
        })
            .then(response => {
                if(!response.ok) {
                    throw new Error('리뷰 삭제 실패');
                }
                return response.json();
            })
            .then(data => {
                this.closest('li').remove();
                console.log('리뷰 삭제 성공:', data);
                location.reload();
            })
            .catch(error => console.error('Error:', error));
    });
});


// 페이지 로드 시 회원 정보를 가져와서 mId 필드를 설정하는 함수
function setDetailUserInfo() {
    fetch("/userInfo", {
        method: "GET"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json(); // JSON 형식으로 응답 처리
        })
        .then(data => {
            const userId = data.mId;
            console.log("userId:", userId);
            document.getElementById('detail_mId').value = userId; // 회원 ID 값을 설정
            const myPageLink = document.getElementById("myPageLink");
            myPageLink.href = `/myPage/${userId}`; // 마이페이지 링크 설정
        })
        .catch(error => console.error("Error:", error));
}

// 페이지 로드 시 회원 정보를 가져와서 mId 필드를 설정하는 함수
window.addEventListener("load", setDetailUserInfo);

// 도서 대여 버튼 클릭 이벤트 핸들러 추가
document.getElementById("borrowBookBtn").addEventListener("click", function () {
    const bId = document.getElementById("bId").value;
    const mId = document.getElementById("detail_mId").value;

    fetch(`/borrow/books?bId=${bId}&mId=${mId}`, {
        method: "GET"
    })
        .then(response => {
            console.log("Response:", response); // 응답 객체를 출력
            return response.json().then(data => {
                console.log("Data:", data); // JSON 데이터를 출력
                if (response.ok) {
                    alert("도서 대여 성공!");
                    location.reload();
                } else if(response.status === 510) {
                    alert(`도서 대여 실패: ${data.message}`);
                    location.reload();
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

document.getElementById("logoutButton").addEventListener("click", function () {
    const mId = document.getElementById("detail_mId").value;
    fetch(`/logout/${mId}`, {
        method: "POST"
    })
        .then(response => {
            if(!response.ok) {
                alert(`로그아웃 실패: ${response.status}`, );
                console.error('로그아웃 실패:', response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('로그아웃 성공:', data);

            window.location.href = '/sign/in';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('로그아웃 중 오류가 발생했습니다.');
        });
});