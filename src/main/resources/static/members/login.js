document.getElementById("loginButton").addEventListener("click", function () {
    const form = document.querySelector('form');

    const formData = new FormData(form);

    fetch(form.action, {
        method: 'POST',
        body: formData,
    })
        .then(response => {
            if(response.ok) {
                alert("로그인 성공!");
                window.location.href = "/all/books";
            } else if(response.status === 404) {
                response.json().then(data => {
                    console.log(data);
                    alert(data.message);
                })
            } else if(response.status === 409) {
                response.json().then(data => {
                    console.log(data);
                    alert(data.message);
                })
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

