package com.ahddad.typeaapp.services;

import com.ahddad.typeaapp.models.Manifestation;
import com.ahddad.typeaapp.repositories.ManifestationRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PdfGeneratorService {

    @Autowired
    private ManifestationRepository manifestationRepository;

    public  void export(HttpServletResponse response ,  int id) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document , response.getOutputStream());

        document.open();

        Manifestation m = manifestationRepository.findById(id).get();
        if(m != null) {

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            titleFont.setSize(18);
            titleFont.setColor(163, 81, 25);

            Paragraph title = new Paragraph("Soutien de type A", titleFont);
            title.setAlignment(Paragraph.ALIGN_LEFT);

            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
            titleFont.setSize(18);
            subTitleFont.setColor(163, 81, 25);

            Paragraph subTitle = new Paragraph("Université Cadi Ayyad", subTitleFont);
            subTitle.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph requestTitle = new Paragraph(m.getTitle().toUpperCase(Locale.ROOT), titleFont);
            requestTitle.setAlignment(Paragraph.ALIGN_CENTER);

            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA);
            paragraphFont.setSize(12);

            Paragraph p1 = new Paragraph("Date de début : "+m.getDateStart());
            Paragraph p2 = new Paragraph("Heure de début : "+m.getTimeStart());
            Paragraph p3 = new Paragraph("Date de fin : "+m.getDateEnd());
            Paragraph p4 = new Paragraph("Heure de fin : "+m.getTimeEnd());
            Paragraph p5 = new Paragraph("Lieu : "+m.getPlace());
            Paragraph p6 = new Paragraph("Montant : "+m.getAmount() + " Dhs");

            Paragraph p7 = new Paragraph("");
            Paragraph p8 = new Paragraph("demande faite le : "+m.getDateRequest());
            Paragraph p9 = new Paragraph("par : "+m.getUsername());

            document.add(title);
            document.add(subTitle);
            document.add(requestTitle);
            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(p4);
            document.add(p5);
            document.add(p6);
            document.add(p7);
            document.add(p8);
            document.add(p9);
            document.close();

        } else{
            System.out.println("no request with this id " + id);
        }

    }
}
