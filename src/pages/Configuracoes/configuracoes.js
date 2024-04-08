const perfilLink = document.getElementById('perfil-link');
const acessibilidadeLink = document.getElementById('acessibilidade-link');
const perfilCampos = document.getElementById('perfil');
const acessibilidadeCampos = document.getElementById('acessibilidade');

perfilLink.addEventListener('click', () => {
    perfilCampos.style.display = 'block';
    acessibilidadeCampos.style.display = 'none';
});

acessibilidadeLink.addEventListener('click', () => {
    perfilCampos.style.display = 'none';
    acessibilidadeCampos.style.display = 'block';
});

document.getElementById('profile-form').addEventListener('submit', function (event) {
    event.preventDefault()
    if ('Notification' in window) {
        Notification.requestPermission().then(function (permission) {
            if (permission === 'granted') {
                new Notification('Configurações salvas!')
            }
        })
    }
})

document.getElementById('accessibility-form').addEventListener('submit', function (event) {
    event.preventDefault()
    if ('Notification' in window) {
        Notification.requestPermission().then(function (permission) {
            if (permission === 'granted') {
                new Notification('Configurações salvas!')
            }
        })
    }
})