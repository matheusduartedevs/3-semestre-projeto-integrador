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