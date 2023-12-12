import org.ecos.logic.data.Candidate;
import org.junit.jupiter.api.Test;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AppTest {
    @Test
    void access() throws FileNotFoundException {
        JsonValue jsonValue = readJSON(Objects.requireNonNull(getClass().getResource("/Maria.json")).getPath());
        Candidate actual = readThe(jsonValue, new Candidate(),"");
        assertThat(actual.getName()).isEqualTo("María");
    }

    private Candidate readThe(JsonValue jsonValue, Candidate candidate, String fieldName) {
        if (jsonValue.getValueType() == JsonValue.ValueType.OBJECT){
            JsonObject jsonObject = jsonValue.asJsonObject();
            Set<String> fields = jsonObject.keySet();
            for (String field: fields) {
                readThe(jsonObject.getValue(field),candidate,field);
            }
        }
        if(jsonValue.getValueType() == JsonValue.ValueType.ARRAY){
            JsonArray jsonArray = jsonValue.asJsonArray();
            for (JsonValue value : jsonArray) {
                readThe(value, candidate,fieldName);
            }
        }
        if(jsonValue.getValueType() == JsonValue.ValueType.STRING){
            if(fieldName.equals("nombre")){
                candidate.setName(jsonValue.toString());
            }
        }

        return candidate;
    }

    private static JsonValue readJSON(String file) throws FileNotFoundException {
        try (JsonReader reader = Json.createReader(new FileReader(file))) {
            return reader.read();
        }
    }

}
