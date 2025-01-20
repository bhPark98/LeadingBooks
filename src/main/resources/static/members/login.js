document.getElementById("loginButton").addEventListener("click", function () {
    const form = document.querySelector('form');
    const formData = new FormData(form);

    fetch(form.action, {
        method: 'POST',
        body: formData,
    })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => {
                    alert("로그인 성공!");
                    window.location.href = "/all/books"; // 로그인 성공 후 페이지 이동
                });

            } else if(response.status === 401) {
                return response.text().then(url => {
                    window.location.href = url;
                });
            }
            else if (response.status === 404) {
                return response.json().then(data => {
                    console.log(data);
                    alert(data.message);
                });
            } else if (response.status === 409) {
                return response.json().then(data => {
                    console.log(data);
                    alert(data.message);
                });
            } else {
                return response.json().then(data => {
                    const errorMessages = Object.values(data).join(', ');
                    alert("로그인 실패: " + errorMessages);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});