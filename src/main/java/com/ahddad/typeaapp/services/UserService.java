package com.ahddad.typeaapp.services;

import com.ahddad.typeaapp.helper.JwtUtil;
import com.ahddad.typeaapp.models.JwtResponse;
import com.ahddad.typeaapp.models.*;
import com.ahddad.typeaapp.repositories.UserDetailsRepository;
import com.ahddad.typeaapp.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;


    // la methode saveUser
    public User saveUser(User user){
        return userRepository.save(user);
    }

    // la methode de registration
    public User registerUser(User user , String siteURL) throws Exception {
        String email = user.getEmail();
        if(email != null && !email.isEmpty()){
            User u = this.searchUserByEmail(email);
            if(u != null){
                throw new Exception("cet email : "+email+", est deja utilisé !!");
            }else{
                String userPassword = user.getPassword();
                String hashedPassword = hashPassword(userPassword);
                user.setPassword(hashedPassword);

                String randomCode = RandomString.make(64);
                user.setVerificationCode(randomCode);
                user.setEnabled(false);

                this.sendVerificationEmail(user, siteURL);
                return saveUser(user);
            }
        }else{
            throw new Exception("email ne peut pas etre vide !!");
        }

    }

    // la fonction qui cherche l'user par l'email
    public User searchUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // la fonction qui hash le mot de passe :
    public String hashPassword(String passToHash){
        String generatedSecuredPasswordHash = BCrypt.hashpw(passToHash, BCrypt.gensalt(9));
        return generatedSecuredPasswordHash;
    }

    // la fonction qui verifie le mot de passe :
    public boolean verifyPassword(String passToVerify , String hashedPass){
        boolean matched = BCrypt.checkpw(passToVerify, hashedPass);
        return matched;
    }

    // la fonction qui envoie le code de verification
    public void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "companymailer03@gmail.com";
        String senderName = "STYPEA-ahddad";
        String subject = "STYPEA-CONFIRMER VOTRE INSTRIPTION";
        String content = "<h3>Che(è)r(e) [[name]]</h3>,<br>"
                + "Veuillez cliquer sur le lien ci-dessous pour vérifier votre inscription:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFIER</a></h3>"
                + "Merci,<br>"
                + "STYPEA-ahddad.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String fullName = user.getFirstName() + " " + user.getLastName();
        content = content.replace("[[name]]", fullName);
        String verifyURL = siteURL + "/user/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    // la fonction qui verifie l'inscription
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        }else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            saveUser(user);
            return true;
        }

    }


    // la fonction login
    public ResponseEntity<?> loginUser(User user) throws Exception {
        String LoginEmail = user.getEmail();
        if(LoginEmail != null && !LoginEmail.isEmpty()){
            User u = searchUserByEmail(LoginEmail);
            if(u != null){
                if(u.isEnabled()){
                    String pass = user.getPassword();
                    String hashedPassword = u.getPassword();
                    if(verifyPassword(pass , hashedPassword)){
                        // return u;
                        String token = this.jwtUtil.generateToken(u);
                        return ResponseEntity.ok(new JwtResponse(token));
                    }else{
                        throw new Exception("le mot de passe incorrect");
                    }
                }else{
                    throw new Exception("votre compte n'est pas verifié");
                }

            }else{
                throw new IllegalStateException("l'email saisi est invalide");
            }
        }else{
            throw new Exception("email ne peut pas etre vide !!");
        }

    }

    // la fonction qui ajoute un element dans la table customUserDetails
    public CustomUserDetails completeInfos(CustomUserDetails details , String token) throws Exception {
        String username = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        if(username != null && !username.isEmpty()){
            CustomUserDetails u = userDetailsRepository.findByUsername(username);
            if(u != null){
                throw new Exception("vous avez deja rempli les informations professionnelles");
            }else{
                details.setUsername(username);
                return userDetailsRepository.save(details);
            }
        }else{
            throw new Exception("email ne peut pas etre vide !!");
        }


    }

    // pour recuperer l'username connecté
    private String SECRET_KEY = "secret";
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // pour verifier si l'user a repmli les donnees professionnelles
    public ResponseEntity<?> a_rempli_les_donnees_pro(String token){
        String username = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        CustomUserDetails ud = userDetailsRepository.findByUsername(username);
        if(ud != null){
            return ResponseEntity.ok("true");
        }else{
            return ResponseEntity.ok("false");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
