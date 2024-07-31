const form = document.querySelector('form');
form.addEventListener('submit', async (event) => {
    event.preventDefault();
    try {
        const response = await fetch(form.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-HTTP-Method-Override': 'DELETE'
            },
            body: JSON.stringify(Object.fromEntries(new FormData(form)))
        });
        if(response.ok) {
            document.getElementById('result-message').textContent = '회원탈퇴 성공!';
            window.location.href = "/api/v1/sign/up";
        } else {
            const errorData = await response.json();
            document.getElementById('result-message').textContent = `회원탈퇴 실패!: ${errorData.message}`;
        }
    } catch (error) {
        document.getElementById('result-message').textContent = `회원탈퇴 실패: ${error.message}`;
    }
})