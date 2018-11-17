package szczkrzy.kanteam.model.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KTNotification {

    private String text;
    private String url;
    private List<KTUser> recipients;

    @JsonGetter
    private List<Integer> getRecipients() {
        return recipients.stream().map(KTUser::getId).collect(Collectors.toList());
    }
}
