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

    @GetMapping("/")
    public String inicial() {
        return "inicial";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
     @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, RedirectAttributes redirectAttributes, HttpSession session) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("usuarios.json")) {
            // Lê o arquivo JSON existente
            Object obj = jsonParser.parse(reader);
            JSONArray listaUsuarios = (JSONArray) obj;
            
            // Percorre a lista de usuários para verificar se as credenciais fornecidas existem
            for (Object usuarioObj : listaUsuarios) {
                JSONObject usuario = (JSONObject) usuarioObj;
                if (usuario.get("email").equals(email) && usuario.get("senha").equals(senha)) {
                    // Se as credenciais existirem, gera um logedToken e redireciona para a home
                    session.setAttribute("logedToken", true);
                    return "redirect:/home";
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // Se as credenciais não existirem, exibe um erro
        redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos!");
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session) {
        // Verifica se o usuário possui um logedToken
        if (session.getAttribute("logedToken") != null) {
            return "home";
        } else {
            // Se o usuário não possuir um logedToken, exibe um erro
            return "error";
        }
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }
    @PostMapping("/cadastro")
    public String cadastro(@RequestParam String nome, @RequestParam String email, @RequestParam String senha, RedirectAttributes redirectAttributes) {
        JSONObject novoUsuario = new JSONObject();
        novoUsuario.put("nome", nome);
        novoUsuario.put("email", email);
        novoUsuario.put("senha", senha);
        
        File file = new File("usuarios.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Inicializa o arquivo com uma lista vazia
                try (FileWriter writer = new FileWriter("usuarios.json")) {
                    writer.write("[]");
                }
            } catch (IOException e) {
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

            // Reescreve o arquivo JSON com o novo usuário
            try (FileWriter fileWriter = new FileWriter("usuarios.json")) {
                fileWriter.write(listaUsuarios.toJSONString());
                fileWriter.flush();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("message", "Usuário cadastrado com sucesso!");
        return "redirect:/login";
    }

    @GetMapping("/perfil")
    public String perfil() {
        // Verifique se o usuário está logado e se ele está na página 'home' antes de retornar a página
        return "perfil";
    }

    @GetMapping("/tutorial")
    public String tutorial() {
        return "tutorial";
    }

    @GetMapping("/perguntas")
    public String perguntas() {
        // Verifique se o usuário está logado e se ele passou pela página 'home' antes de retornar a página
        return "perguntas";
    }

    @GetMapping("/configuracoes")
    public String configuracoes() {
        return "configuracoes";
    }
}