package utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Ignores unknown parts of the JSON body
/**
 * Class that represents the payload of the webhook, used by the parser.
 * Used to get and set the different information form the JSON body.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {
    private String ref;
    private Repository repository;

    // getters & setters
    public String getRef() { return ref; }
    public void setRef(String ref) {
        this.ref = ref;
    }

    public Repository getRepository() { return repository; }
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repository {
        private String name;

        public String getName() { return name; }
        public void setName(String name) {
            this.name = name;
        }
    }
}
