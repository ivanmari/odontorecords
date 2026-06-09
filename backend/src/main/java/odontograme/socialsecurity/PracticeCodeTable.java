package odontograme.socialsecurity;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by immari on 12/25/2016.
 */

//TODO Ojo si cambia la tabla con el server en marcha
@Component
public class PracticeCodeTable {

    public PracticeCodeTable(){
        entries = new ArrayList<>();
    }

    public void addEntry(PracticeCodeRow entry)
    {
        entries.add(entry);
    }

    public void removeEntry(PracticeCodeRow entry) {entries.remove(entry);}

    //TODO CRUD en base o en app?
    public void updateEntry(PracticeCodeRow entry) {}

    public boolean isFillingPractice(String code){
        boolean ret = this.entries.stream().anyMatch(e -> {
            return (e.getCode().equals(code) && e.isColored());
        });

        return ret;
    }

    public List<PracticeCodeRow> getEntries() {
        return entries;
    }

    @Field("name")
    public String getHealthProviderName() {
        return healthProviderName;
    }

    public void setHealthProviderName(String healthProviderName) {
        this.healthProviderName = healthProviderName;
    }

    public int getId() {
        return id;
    }

    private List<PracticeCodeRow> entries;
    private String healthProviderName;
    private int id;
}
