package com.anae.api.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publication")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "id_researcher", nullable = false)
    private Researcher researcher;

    @OneToOne(mappedBy = "publication")
    private CoAuthor coAuthor;

    private String type;

    private String language;

    private String confidentiality;

    private String domain;

    private LocalDate data;

    @Lob
    @Column(name = "content", columnDefinition = "BLOB")
    private byte[] content;

}
