package com.ahddad.typeaapp.services;

import com.ahddad.typeaapp.models.JwtResponse;
import com.ahddad.typeaapp.models.Manifestation;
import com.ahddad.typeaapp.repositories.ManifestationRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ManifestationService {

    @Autowired
    private ManifestationRepository manifestationRepository;

    private String SECRET_KEY = "secret";
    public Manifestation saveManifestation (Manifestation m , String token){
        String username = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy , hh:mm:ss");
        String today = formatter.format(date);
        m.setUsername(username);
        m.setAccepted(false);
        m.setDateRequest(today);
        return manifestationRepository.save(m);
    }

    public List<Manifestation> returns_all_manifestations(String token){
        String username = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        return manifestationRepository.findAllByUsername(username);
    }

    public Long delete(int id){
        return manifestationRepository.deleteById(id);
    }
    
    public ResponseEntity<?> update(Manifestation m , int id){
        List<Manifestation> all = manifestationRepository.findAll();
        System.out.println(m.toString());
        for(int i = 0 ; i < all.size() ; i++){
            Manifestation m2 = all.get(i);
            if(m2.getId()==id){
                System.out.println(m2.toString());
                manifestationRepository.save(m);
                return ResponseEntity.ok(new JwtResponse("updated"));
            }
        }
        return ResponseEntity.ok(new JwtResponse("not updated"));
    }
}
