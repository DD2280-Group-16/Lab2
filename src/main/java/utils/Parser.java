package utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {
    private final Payload payload;

    public Parser(String jsonBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.payload = mapper.readValue(jsonBody, Payload.class);
    }

    public String getBranch() {
        if (payload.getRef() != null && payload.getRef().startsWith("refs/heads/")) {
            return payload.getRef().substring("refs/heads/".length());
        }
        return null;
    }

    public String getRepoName() {
        return payload.getRepository() != null
                ? payload.getRepository().getName()
                : null;
    }

    public Payload getPayload() {
        return payload;
    }
}
