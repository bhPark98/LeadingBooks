// 페이지 로드 시 회원 정보를 가져와서 mId 필드를 설정하는 함수
function sethomeUserInfo() {
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
            document.getElementById('home_mId').value = userId; // 회원 ID 값을 설정
            const myPageLink = document.getElementById("myPageLink");
            myPageLink.href = `/myPage/${userId}`; // 마이페이지 링크 설정
        })
        .catch(error => console.error("Error:", error));
}

// 페이지 로드 시 회원 정보를 가져와서 mId 필드를 설정하는 함수
window.addEventListener("load", sethomeUserInfo);

document.getElementById("homeLogoutButton").addEventListener("click", function (event) {
    event.preventDefault(); // 링크의 기본 동작 방지
    const mId = document.getElementById("home_mId").value;
    console.log("home_mId:", mId);
    fetch(`/logout/${mId}`, {
        method: 'POST',
        credentials: 'include',  // 쿠키를 포함하여 요청
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if(response.ok) {
                window.location.href = '/sign/in';  // 로그아웃 후 리다이렉트
            } else {
                alert(`로그아웃 실패: ${response.status}`, );
                console.error('로그아웃 실패:', response.status);
            }
        })
        .catch(error => console.error('Error:', error));
});


document.getElementById("myPageLink").addEventListener('click', function (event) {
    event.preventDefault();
    const mId = this.href.split('/').pop();

    window.location.href = `/myPage/${mId}`;
});


let selectedSearchType = 'title';

    document.querySelectorAll('.dropdown-item').forEach(item => {
        item.addEventListener('click', function() {
            console.log("Clicked item:", this.getAttribute('value')); // 클릭한 항목의 값을 출력
            selectedSearchType = this.getAttribute('value');
            document.getElementById('navbarDropdown').innerText = this.innerText;
            console.log("Selected Search Type updated to:", selectedSearchType);
        });
    });


document.getElementById('searchButton').addEventListener('click', function () {
    console.log("Selected Search Type:", selectedSearchType);
    const searchQuery = document.getElementById('search-input').value;

    let paramName;
    if(selectedSearchType === 'title') {
        paramName = 'bName';
    } else if(selectedSearchType === 'writer') {
        paramName = 'bWriter';
    } else if(selectedSearchType === 'category') {
        paramName = 'bCategory';
    }

    const apiUrl = `/search/${selectedSearchType}?${paramName}=${encodeURIComponent(searchQuery)}`;
    console.log("API URL:", apiUrl);

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            displayResults(data);
        })
        .catch(error => console.error('Error:', error));
});

function displayResults(books) {
    const resultContainer = document.getElementById('bookResults');
    resultContainer.innerHTML = '';

    books.forEach(book => {
        const row = document.createElement('tr');

        const titleCell = document.createElement('td');
        const titleLink = document.createElement('a');
        titleLink.href = `/detail/reviews?bId=${book.bid}`;
        console.log(titleLink);
        titleLink.innerText = book.bname;
        titleCell.appendChild(titleLink);
        row.appendChild(titleCell);

        const publishCell = document.createElement('td');
        publishCell.innerText = book.bpublish;
        row.appendChild(publishCell);

        const writerCell = document.createElement('td');
        writerCell.innerText = book.bwriter;
        row.appendChild(writerCell);

        const categoryCell = document.createElement('td');
        categoryCell.innerText = book.bcategory;
        row.appendChild(categoryCell);

        resultContainer.appendChild(row);

    })
}