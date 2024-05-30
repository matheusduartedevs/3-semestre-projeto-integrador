const perfilLink = document.getElementById('perfil-link');
const acessibilidadeLink = document.getElementById('acessibilidade-link');
const perfilCampos = document.getElementById('perfil');
const acessibilidadeCampos = document.getElementById('acessibilidade');
const baixaVisaoSwitch = document.getElementById('baixa-visao')
const daltonicoSwitch = document.getElementById('daltonico')
const header = document.querySelector('header')

// Navegar entre menus
perfilLink.addEventListener('click', () => {
    perfilCampos.style.display = 'block';
    acessibilidadeCampos.style.display = 'none';
});

acessibilidadeLink.addEventListener('click', () => {
    perfilCampos.style.display = 'none';
    acessibilidadeCampos.style.display = 'block';
});

// Função de Notificação na página de configurações de perfil
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

// Função de Notificação na página de acessibilidade
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

// Modo Baixa Visão
const isModoBaixaVisaoAtivado = sessionStorage.getItem('baixa-visao') === 'true';

if (isModoBaixaVisaoAtivado) {
    header.classList.add('baixa-visao');
    baixaVisaoSwitch.checked = true
}

baixaVisaoSwitch.addEventListener('change', () => {
    header.classList.toggle('baixa-visao');
    

    sessionStorage.setItem('baixa-visao', header.classList.contains('baixa-visao'));
});

// Modo Daltônico
const isModoDaltonicoAtivado = sessionStorage.getItem('daltonico') === 'true';

if (isModoDaltonicoAtivado) {
    daltonicoSwitch.classList.add('daltonico');
    daltonicoSwitch.checked = true
}

daltonicoSwitch.addEventListener('change', () => {
    daltonicoSwitch.classList.toggle('daltonico');
    
    sessionStorage.setItem('daltonico', daltonicoSwitch.classList.contains('daltonico'));
});