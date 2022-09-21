package com.ahddad.typeaapp.services;

import com.ahddad.typeaapp.helper.JwtUtil;
import com.ahddad.typeaapp.models.Admin;
import com.ahddad.typeaapp.models.JwtResponse;
import com.ahddad.typeaapp.models.Manifestation;
import com.ahddad.typeaapp.models.User;
import com.ahddad.typeaapp.repositories.AdminRepository;
import com.ahddad.typeaapp.repositories.ManifestationRepository;
import com.ahddad.typeaapp.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ManifestationRepository manifestationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> loginAdmin(Admin admin) throws Exception {
        String username = admin.getUsername();
        if(username != null && !username.isEmpty()){
            Admin a = searchByUsername(username);
            if(a != null){
                    String pass = admin.getPassword();
                    if(pass.equals(a.getPassword())){
                        String token = this.jwtUtil.generateToken(a);
                        return ResponseEntity.ok(new JwtResponse(token));
                    }else{
                        throw new Exception("le mot de passe incorrect");
                    }
            }else{
                throw new IllegalStateException("le nom d'utilisateur saisi est invalide");
            }
        }else{
            throw new Exception("le nom d'utilisateur ne peut pas etre vide !!");
        }

    }

    public Admin searchByUsername(String username){
        return adminRepository.findByUsername(username);
    }

    // la methode qui renvoie toutes les demandes
    public List<Manifestation> returns_all_manifestations() {
        return manifestationRepository.findAll();
    }

    // la methode qui accepte une demande
    public ResponseEntity<?> acceptRequest(int id){
        Manifestation m = manifestationRepository.findById(id).get();
        if(m != null){
            m.setAccepted(true);
            manifestationRepository.save(m);
            return ResponseEntity.ok(new JwtResponse("accepted"));
        }

        return ResponseEntity.ok(new JwtResponse("not-accepted"));
    }

    // la methode qui supprime une demande
    public Long deleteRequest(int id){
        return manifestationRepository.deleteById(id);
    }

    // la methode qui renvoie tous les users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // la methode qui permet de supprimer un utilisateur
    public int deleteUser(int id){
        userRepository.deleteById(id);
        return 1;
    }

}
