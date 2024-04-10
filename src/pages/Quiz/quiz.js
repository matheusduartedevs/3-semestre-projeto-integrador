document.addEventListener('DOMContentLoaded', function () {
    const respostas = document.querySelectorAll('.resposta-item');
    respostas.forEach((resposta) => {
        resposta.addEventListener('click', () => {
            respostas.forEach((element) => element.classList.remove('selected'));
            resposta.classList.add('selected');
        });
    });
});

function verificarResposta() {
    const respostaSelecionada = document.querySelector('.resposta-item.selected');
    if (respostaSelecionada) {
        alert(`Você selecionou: ${respostaSelecionada.textContent}`);
    } else {
        alert('Por favor, selecione uma resposta antes de avançar.');
    }
}