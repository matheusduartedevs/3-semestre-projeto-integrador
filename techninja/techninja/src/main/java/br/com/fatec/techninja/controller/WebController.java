package br.com.fatec.techninja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Controller
public class WebController {

    //O método login() responde a requisições GET para "/login" e retorna a página de login do aplicativo.
    // Os usuários acessam essa página para autenticar-se no sistema antes de acessar recursos protegidos.

    @GetMapping("/")
    public String inicial() {

        // Retorna a página inicial
        return "inicial";
    }

    @GetMapping("/login")
    public String login() {

        // Retorna a página de login
        return "login";
    }

    //Processo de autenticação do usuário o PostMapping é utilizad para capturar os dados submetidos pelo formulário de login.
     @PostMapping("/login")
     //Nessa parte vamos código verifica se as credenciais fornecidas pelo usuário durante o login correspondem a algum usuário registrado no sistema.
     // Se correspondem, o usuário é autenticado e redirecionado para a página inicial.
     //Se não correspondem, uma mensagem de erro é exibida e o usuário é redirecionado de volta para a página de login.
    public String login(@RequestParam String email, @RequestParam String senha, RedirectAttributes redirectAttributes, HttpSession session) {
         // Cria um parser JSON
         //Um objeto JSONParser é criado para analisar o conteúdo do arquivo "usuarios.json", que contém informações sobre os usuários do sistema.
        JSONParser jsonParser = new JSONParser();
        //abrir e ler o arquivo "usuarios.json" usando FileReader.
         //Se a leitura for bem-sucedida, o conteúdo é analisado como um objeto JSON.
        try (FileReader reader = new FileReader("usuarios.json")) {
            // Lê o arquivo JSON existente
            Object obj = jsonParser.parse(reader);
            //O objeto JSON é convertido em uma lista de usuários (JSONArray).
            JSONArray listaUsuarios = (JSONArray) obj;
            
            // Percorre a lista de usuários para verificar se as credenciais fornecidas existem
            for (Object usuarioObj : listaUsuarios) {
                JSONObject usuario = (JSONObject) usuarioObj;
                if (usuario.get("email").equals(email) && usuario.get("senha").equals(senha)) {
                    // Se as credenciais existirem, gera um logedToken e redireciona para pagina inicial
                    session.setAttribute("logedToken", true);
                    //Se as credenciais não correspondem a nenhum usuário na lista, uma mensagem de erro é adicionada aos atributos de redirecionamento.
                    //O usuário é redirecionado de volta para a página de login.
                    return "redirect:/home";
                }
            }

        } catch (IOException | ParseException e) {
            // Se houver algum erro ao ler o arquivo JSON ou ao processar os dados, uma pilha de erros será impressa para facilitar a depuração.
            e.printStackTrace();
        }

        // Se as credenciais não existirem, exibe um erro, adiciona um atributo de erro para exibição na página de login e redireciona de volta para o login
        redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos!");
        return "redirect:/login";
    }




    //Quando usuário acessar a url home, ele recebe um objeto HttpSession como parâmetro, que é usado para verificar se o usuário está autenticado.
    //
    @GetMapping("/home")
    public String home(HttpSession session) {
        // Verifica se o usuário possui um token de autenticação na sessão
        if (session.getAttribute("logedToken") != null) {
            // Se o usuário estiver autenticado, retorna a página inicial
            return "home";
        } else {
            // Se o usuário não possuir um logedToken, retorna uma página de erro
            return "error";
        }
    }



    //Este método é chamado quando um usuário acessa a URL "/cadastro".
    //Ele não possui parâmetros e não precisa verificar a autenticação do usuário.
    @GetMapping("/cadastro")
    public String cadastro() {

        // Retorna a página de cadastro
        return "cadastro";
    }






    @PostMapping("/cadastro")
    //Este método é mapeado para a rota "/cadastro" e é chamado quando um formulário de cadastro é submetido via método POST.
    //Este método captura os dados submetidos pelo formulário de cadastro, cria um novo usuário, atualiza o arquivo JSON que armazena os usuários e redireciona o usuário de volta para a página de login com uma mensagem de sucesso após o cadastro bem-sucedido.
    public String cadastro(@RequestParam String nome, @RequestParam String email, @RequestParam String senha, RedirectAttributes redirectAttributes) {
        // Cria um novo objeto JSON com as informações do usuário fornecidas representa um novo usuário
        //Os parâmetros nome, email e senha são obtidos a partir dos campos do formulário de cadastro.
        JSONObject novoUsuario = new JSONObject();
        novoUsuario.put("nome", nome);
        novoUsuario.put("email", email);
        novoUsuario.put("senha", senha);
        
        File file = new File("usuarios.json");
        // Cria um arquivo "usuarios.json" se ele não existir e inicializa com uma lista vazia
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Inicializa o arquivo com uma lista vazia
                try (FileWriter writer = new FileWriter("usuarios.json")) {
                    writer.write("[]");
                }
            } catch (IOException e) {
                // Em caso de exceção ao criar o arquivo, imprime a pilha de erros
                e.printStackTrace();
                return "Erro ao criar o arquivo usuarios.json";
            }
        }
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("usuarios.json")) {
            // Lê o arquivo JSON existente
            Object obj = jsonParser.parse(reader);
            JSONArray listaUsuarios = (JSONArray) obj;

            // Adiciona o novo usuário
            listaUsuarios.add(novoUsuario);

            // Reescreve o arquivo JSON com o novo usuário adicionando
            try (FileWriter fileWriter = new FileWriter("usuarios.json")) {
                fileWriter.write(listaUsuarios.toJSONString());
                fileWriter.flush();
            }

        } catch (IOException | ParseException e) {
            // Em caso de exceção ao ler ou escrever no arquivo JSON, imprime a pilha de erros
            e.printStackTrace();
        }

        // Adiciona um atributo de mensagem de sucesso e redireciona de volta para o login
        redirectAttributes.addFlashAttribute("message", "Usuário cadastrado com sucesso!");
        return "redirect:/login";
    }





    //cada método retorna o nome da página HTML correspondente à URL acessada, permitindo que o Spring MVC direcione a requisição para a visualização apropriada.
    @GetMapping("/perfil")
    public String perfil() {
        // Verifique se o usuário está logado e se ele está na página 'home' antes de retornar a página
        // Retorna a página de perfil
        return "perfil";
    }

    @GetMapping("/tutorial")
    public String tutorial() {

        // Retorna a página de tutorial
        return "tutorial";
    }

    @GetMapping("/perguntas")
    public String perguntas() {
        // Verifique se o usuário está logado e se ele passou pela página 'home' antes de retornar a página
        // Retorna a página de perguntas
        return "perguntas";
    }

    @GetMapping("/configuracoes")
    public String configuracoes() {

        // Retorna a página de configurações
        return "configuracoes";
    }
}