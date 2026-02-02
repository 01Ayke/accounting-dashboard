package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Table(name = "journal")
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier")
    @JsonBackReference
    private File file;

    @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<AccountingEntry> ecrituresComptables;

    public File getFile() {
        return file;
    }

    public Long getIdJournal() {
        return id_journal;
    }

    public Set<AccountingEntry> getEcrituresComptables() {
        return ecrituresComptables;
    }
}
