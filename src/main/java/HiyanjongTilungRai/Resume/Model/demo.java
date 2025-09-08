package HiyanjongTilungRai.Resume.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class demo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String description;


    private int contact;

    private static class provider{

    }


}