package com.ahddad.typeaapp.controllers;

import com.ahddad.typeaapp.models.Manifestation;
import com.ahddad.typeaapp.services.ManifestationService;
import com.ahddad.typeaapp.services.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/manifestation")
public class ManifestationController {

    @Autowired
    private ManifestationService manifestationService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    // la methode qui enregistre une nouvelle manifestation;
    @PostMapping("/save/{token}")
    public Manifestation saveManifestation(@RequestBody Manifestation m , @PathVariable String token){
        return manifestationService.saveManifestation(m , token);
    }

    // la methode qui return toutes les demandes d'un user
    @GetMapping("/all/{token}")
    public List<Manifestation> touteLesDemandes(@PathVariable String token){
        return manifestationService.returns_all_manifestations(token);
    }

    // la methode qui supprime une ligne de la table manifestation
    @DeleteMapping("/delete/{id}")
    public Long deleteById(@PathVariable int id){
        return manifestationService.delete(id);
    }

    // la methode qui update une ligne de la table manifestation
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id , @RequestBody Manifestation m){
        return manifestationService.update(m,id);
    }

    // la methode qui permet de telecharger le document pdf
    @GetMapping("/pdf/{Id}")
    public void generatePdf(@PathVariable int Id , HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormater.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachement; filename=pdf_"+currentDateTime + ".pdf";
        response.setHeader(headerKey , headerValue);
        pdfGeneratorService.export(response,Id);
    }
}
