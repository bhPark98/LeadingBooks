window.addEventListener("load", function () {
    fetch("/userInfo", {
        method: "GET"
    })
        .then(response => response.text())
        .then(userId => {

            const myPageLink = document.getElementById("myPageLink");
            myPageLink.href = `/myPage/${userId}`;
        })
        .catch(error => console.error("Error:", error));
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