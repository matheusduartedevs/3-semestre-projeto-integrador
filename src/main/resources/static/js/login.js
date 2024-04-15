var form = document.querySelector('#loginForm');
        var email = document.querySelector('#email');
        var senha = document.querySelector('#senha');
        var emailError = document.querySelector('#emailError');
        var senhaError = document.querySelector('#senhaError');


form.addEventListener('submit', function(event) {
    event.preventDefault();

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/checkCredentials', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var credentialsValid = JSON.parse(xhr.responseText);
            if (credentialsValid) {
                form.submit();
            } else {
                email.classList.add('error');
                senha.classList.add('error');
                emailError.textContent = 'E-mail ou senha incorretos.';
                senhaError.textContent = 'E-mail ou senha incorretos.';
            }
        }
    };
    xhr.send('email=' + encodeURIComponent(email.value) + '&senha=' + encodeURIComponent(senha.value));
});