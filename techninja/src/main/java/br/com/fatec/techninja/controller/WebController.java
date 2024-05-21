package br.com.fatec.techninja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.Map;

import java.nio.file.*;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Controller
public class WebController {
        @Autowired
        private HttpSession session;
    // Pagina Incial do Site -- Incial
    @GetMapping("/")
    public String inicial() {
        return "inicial";
    }

    // Pagina de Login -- Login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Logicas e Implementações de Login -- Logica Login
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, RedirectAttributes redirectAttributes,
            HttpSession session) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("usuarios.json")) {
            // Lê o arquivo JSON existente
            Object obj = jsonParser.parse(reader);
            JSONArray listaUsuarios = (JSONArray) obj;

            // Percorre a lista de usuários para verificar se as credenciais fornecidas
            // existem
            for (Object usuarioObj : listaUsuarios) {
                JSONObject usuario = (JSONObject) usuarioObj;
                if (usuario.get("email").equals(email) && usuario.get("senha").equals(senha)) {
                    // Se as credenciais existirem, gera um logedToken e redireciona para a home
                    session.setAttribute("logedToken", true);
                    session.setAttribute("logedEmail", email); // adicionando o email do usuario na sessão

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

    // Pagina de Cadastro -- Cadastro
    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    /*
     * @PostMapping("/cadastro")
     * public String cadastro(@RequestParam String nome, @RequestParam String
     * email, @RequestParam String senha, @RequestParam String repSenha,
     * RedirectAttributes redirectAttributes) {
     * JSONParser jsonParser = new JSONParser();
     * JSONArray listaUsuarios = new JSONArray();
     * 
     * // Verifica se o arquivo usuarios.json existe, se não existir ele cria o
     * arquivo
     * File file = new File("usuarios.json");
     * if (file.exists()) {
     * try (FileReader reader = new FileReader("usuarios.json")) {
     * // Lê o arquivo JSON existente
     * Object obj = jsonParser.parse(reader);
     * listaUsuarios = (JSONArray) obj;
     * } catch (IOException | ParseException e) {
     * // handle exception
     * }
     * }
     * 
     * // Adiciona o novo usuário
     * JSONObject novoUsuario = new JSONObject();
     * novoUsuario.put("nome", nome);
     * novoUsuario.put("email", email);
     * novoUsuario.put("senha", senha);
     * novoUsuario.put("repSenha", repSenha);
     * listaUsuarios.add(novoUsuario);
     * 
     * // Salva a lista atualizada no arquivo
     * try (FileWriter writer = new FileWriter("usuarios.json")) {
     * writer.write(listaUsuarios.toJSONString());
     * writer.flush();
     * } catch (IOException e) {
     * // handle exception
     * }
     * 
     * redirectAttributes.addFlashAttribute("success",
     * "Usuário cadastrado com sucesso.");
     * return "redirect:/login";
     * }
     */

    // Logicas e Implementações de Cadastro -- Logica Cadastro
    @SuppressWarnings("unchecked")
    @PostMapping("/cadastro")
    public String cadastro(@RequestParam String nome, @RequestParam String email, @RequestParam String senha,
            @RequestParam String repSenha,
            RedirectAttributes redirectAttributes) {
        JSONParser jsonParser = new JSONParser();
        JSONArray listaUsuarios = new JSONArray();

        // Verifica se o arquivo usuarios.json existe, se não existir ele cria o arquivo
        File file = new File("usuarios.json");
        if (file.exists()) {
            try (FileReader reader = new FileReader("usuarios.json")) {
                // Lê o arquivo JSON existente
                Object obj = jsonParser.parse(reader);
                listaUsuarios = (JSONArray) obj;

                // Verifica se o e-mail já existe
                for (Object usuarioObj : listaUsuarios) {
                    JSONObject usuario = (JSONObject) usuarioObj;
                    if (usuario.get("email").equals(email)) {
                        redirectAttributes.addFlashAttribute("error", "O e-mail já está em uso.");
                        return "redirect:/cadastro";
                    }
                }
            } catch (IOException | ParseException e) {
                // handle exception
            }
        }
        if (!senha.equals(repSenha)) {
            redirectAttributes.addFlashAttribute("error", "As senhas não coincidem.");
            return "redirect:/cadastro";
        }

        // Se o e-mail não existir na lista, adiciona o novo usuário
        JSONObject novoUsuario = new JSONObject();
        novoUsuario.put("nome", nome);
        novoUsuario.put("email", email);
        novoUsuario.put("senha", senha);
        novoUsuario.put("repSenha", repSenha);
        listaUsuarios.add(novoUsuario);

        // Salva a lista atualizada no arquivo
        try (FileWriter writer = new FileWriter("usuarios.json")) {
            writer.write(listaUsuarios.toJSONString());
            writer.flush();
        } catch (IOException e) {
            // handle exception
        }
        // associandos as informações do usuario com as funcionalidades do sistema

        // Carrega a lista de informações de usuários existentes
        JSONArray listaUsuariosInfo = new JSONArray();
        try (FileReader reader = new FileReader("usuariosInfo.json")) {
            listaUsuariosInfo = (JSONArray) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            // handle exception
        }
        JSONObject novoUsuarioInfo = new JSONObject();
        novoUsuarioInfo.put("email", email);
        novoUsuarioInfo.put("username", nome);
        novoUsuarioInfo.put("profilePicture", "/src/main/resources/static/images/ProfilePadrao.png");
        novoUsuarioInfo.put("quizScores", new JSONArray());
        novoUsuarioInfo.put("completedQuizzes", new JSONArray());
        novoUsuarioInfo.put("accountLevel", "bronze");
        // Adiciona o novo objeto à lista
        listaUsuariosInfo.add(novoUsuarioInfo);

        // Salva a lista atualizada no arquivo
        try (FileWriter writer = new FileWriter("usuariosInfo.json")) {
            writer.write(listaUsuariosInfo.toJSONString());
            writer.flush();
        } catch (IOException e) {
            // handle exception
        }

        redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso.");
        return "redirect:/login";
    }

    // Pagina Home -- Home
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

    @GetMapping("/tutorial")
    public String tutorial() {
        return "tutorial";
    }

    // Pagina de Perguntas -- Perguntas
    @GetMapping("/perguntas")
    public String perguntas() {
        // Verifique se o usuário está logado
        return "perguntas";
    }

    // Pagina de Configurações -- Configurações
    @GetMapping("/configuracoes")
    public String configuracoes() {
        // verificar se o usuário está logado
        return "configuracoes";
    }

    // Pagina de Esqueceu a Senha -- Esqueceu
    @GetMapping("/esqueceu")
    public String esqueceu() {
        return "esqueceu";
    }

    // Pagina de Testes -- Testes

    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        JSONParser jsonParser = new JSONParser();
        JSONArray listaUsuarios = new JSONArray();
        File file = new File("usuarios.json");
        if (file.exists()) {
            try (FileReader reader = new FileReader("usuarios.json")) {
                Object obj = jsonParser.parse(reader);
                listaUsuarios = (JSONArray) obj;
                for (Object usuarioObj : listaUsuarios) {
                    JSONObject usuario = (JSONObject) usuarioObj;
                    if (usuario.get("email").equals(email)) {
                        return ResponseEntity.ok(true);
                    }
                }
            } catch (IOException | ParseException e) {
                // handle exception
            }
        }
        return ResponseEntity.ok(false);
    }

    /*
     * @PostMapping("/checkCredentials")
     * 
     * @ResponseBody
     * public boolean checkCredentials(@RequestParam String email, @RequestParam
     * String senha) {
     * JSONParser jsonParser = new JSONParser();
     * JSONArray listaUsuarios = new JSONArray();
     * 
     * // Verifica se o arquivo usuarios.json existe
     * File file = new File("usuarios.json");
     * if (file.exists()) {
     * try (FileReader reader = new FileReader("usuarios.json")) {
     * // Lê o arquivo JSON existente
     * Object obj = jsonParser.parse(reader);
     * listaUsuarios = (JSONArray) obj;
     * 
     * // Verifica se o e-mail e a senha estão corretos
     * for (Object usuarioObj : listaUsuarios) {
     * JSONObject usuario = (JSONObject) usuarioObj;
     * if (usuario.get("email").equals(email) && usuario.get("senha").equals(senha))
     * {
     * return true;
     * }
     * }
     * } catch (IOException | ParseException e) {
     * // handle exception
     * }
     * }
     * return false;
     * }
     */

    @GetMapping("/quizes")
    public String quizes(HttpSession session, Model model) {
        String logedEmail = (String) session.getAttribute("logedEmail");
        model.addAttribute("logedEmail", logedEmail);
        // Verifique se o usuário está logado
        return "quizes";
    }

    @GetMapping("/quiz1")
    public String quiz1(HttpSession session, Model model) {
        String logedEmail = (String) session.getAttribute("logedEmail");
        model.addAttribute("logedEmail", logedEmail);
        // Verifique se o usuário está logado
        return "quiz1";
    }

    @GetMapping("/testes")
    public String testes(HttpSession session, Model model) {
        String logedEmail = (String) session.getAttribute("logedEmail");
        if (logedEmail == null) {
            return "redirect:/login";
        }
        model.addAttribute("logedEmail", logedEmail);

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            List<Map<String, Object>> users = objectMapper.readValue(Paths.get("/usuariosInfo.json").toFile(),
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            for (Map<String, Object> user : users) {
                if (logedEmail.equals(user.get("email"))) {
                    model.addAttribute("userInfo", user);

                    break;
                }
            }
        } catch (IOException e) {
            // handle exception
        }

        return "testes";
    }

@GetMapping("/perfil")
public String perfil(HttpSession session, Model model) {
    String email = (String) session.getAttribute("logedEmail");
    System.out.println("E-mail da sessão: " + email);
    if (email != null) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("usuariosInfo.json")) {
            Object obj = jsonParser.parse(reader);
            JSONArray listaUsuarios = (JSONArray) obj;

            for (Object usuarioObj : listaUsuarios) {
                JSONObject usuario = (JSONObject) usuarioObj;
                if (usuario.get("email").equals(email)) {
                    model.addAttribute("usuario", usuario);
                    break;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    return "profile";
}

    
}
