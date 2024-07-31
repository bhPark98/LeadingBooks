document.getElementById("extendBookBtn").addEventListener("click", function () {
    const mId = document.querySelector(".mId").value;
    const checkBooks = document.querySelectorAll(".book-checkbox:checked");

    if(checkBooks.length === 0) {
        alert("연장할 책을 선택하세요.");
        return;
    }

    const bookIds = Array.from(checkBooks).map(checkBook => checkBook.value);

    const requests = bookIds.map(bId => {
        return fetch(`/api/v1/extend/books?mId=${mId}&bId=${bId}`, {
            method: "PUT"
        })
            .then(response => response.json().then(data => {
                if (!response.ok) {
                    throw new Error(data.message || "연장 실패!");
                }
                return { status: "fulfilled", data };
            }))
            .catch(error => {
                return { status: "rejected", reason: error.message || "알 수 없는 오류 발생" };
            });
    });

    Promise.allSettled(requests)
        .then(results => {
            console.log(results);
            const failedRequests = results.filter(result => result.status === 'rejected' || (result.status === 'fulfilled' && !result.value.data));
            const successfulRequests = results.filter(result => result.status === 'fulfilled' && result.value.data);
            console.log(failedRequests);
            console.log(successfulRequests);

            if (successfulRequests.length > 0) {
                alert("도서 대여 연장 성공!");
            }

            if (failedRequests.length > 0) {
                const errorMessages = new Set(failedRequests.map(result => result.reason.message));
                alert(`도서 연장 실패: ${[...errorMessages].join("\n")}`);
            }
        })
        .catch(error => {
            alert("도서 연장 실패: 서버 오류 발생!");
        });
});

document.getElementById("returnBookBtn").addEventListener("click", function () {
    const mId = document.querySelector(".mId").value;
    const checkBooks = document.querySelectorAll(".book-checkbox:checked");

    if (checkBooks.length === 0) {
        alert("반납할 책을 선택하세요.");
        return;
    }

    const bookIds = Array.from(checkBooks).map(checkBook => checkBook.value);

    const requests = bookIds.map(bId => {
        return fetch(`/api/v1/return/books?mId=${mId}&bId=${bId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.message || "반납 실패");
                    });
                }
                return response.json().then(data => ({ status: "fulfilled", data }));
            })
            .catch(error => ({ status: "rejected", reason: error }));
    });

    Promise.allSettled(requests)
        .then(results => {
            console.log(results);
            const failedRequests = results.filter(result => result.status === 'rejected');
            const successfulRequests = results.filter(result => result.status === 'fulfilled');

            if (successfulRequests.length > 0) {
                alert("도서 반납 성공!");
            }

            if (failedRequests.length > 0) {
                const errorMessages = new Set(failedRequests.map(result => result.reason.message));
                alert(`도서 반납 실패: ${[...errorMessages].join("\n")}`);
            }
        })
        .catch(error => {
            alert("도서 반납 실패: 서버 오류 발생!");
        });
});

document.getElementById("changeNicknameBtn").addEventListener("click", function () {
    const mId = document.querySelector(".mId").value;

    const nicknameInput = document.createElement("input");
    nicknameInput.type = "text";
    nicknameInput.placeholder = "수정할 닉네임";
    nicknameInput.classList.add("new-nickname");

    const changeNicknameBtn = document.createElement("button");
    changeNicknameBtn.textContent = "변경";
    changeNicknameBtn.classList.add("change-nickname-btn");

    this.parentNode.appendChild(nicknameInput);
    this.parentNode.appendChild(changeNicknameBtn);

    changeNicknameBtn.addEventListener("click", function () {
        const newNickname = nicknameInput.value;
        console.log(newNickname);

        if(!newNickname) {
            alert("새로운 닉네임을 입력해주세요.");
            return;
        }

        fetch(`/api/v1/changeNickname/${mId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ "newNickname": newNickname})
        })
            .then(response => {
                if(response.ok) {
                    alert("닉네임이 변경되었습니다.");
                    location.reload();
                } else {
                    return response.json().then(data => {
                        alert(`닉네임 변경 실패: ${data.message}`);
                    });
                }
            })
            .catch(error => {
                alert("닉네임 변경 중 오류가 발생했습니다.");
            });
    });
});