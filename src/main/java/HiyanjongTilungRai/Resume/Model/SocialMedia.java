package HiyanjongTilungRai.Resume.Model;

import jakarta.persistence.*;

@Entity
public class SocialMedia {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }



    // platform
    @Column( nullable = false)
    private String platform;

    public String getPlatform(){
        return platform;
    }
    public void setPlatform(String platform){
        this.platform = platform;
    }


// URL
    @Column(nullable = false)
    private String url;

    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }

}
