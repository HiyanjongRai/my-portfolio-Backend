// src/main/java/HiyanjongTilungRai/Resume/Model/Post.java
package HiyanjongTilungRai.Resume.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    // getters/setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public byte[] getData() { return data; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setData(byte[] data) { this.data = data; }
}
