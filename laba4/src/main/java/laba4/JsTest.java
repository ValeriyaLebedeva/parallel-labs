package laba4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsTest {
    public final static class User {
        public final String name;
        public final int age;
        public final String countryOfResidence;
        @JsonCreator
        public User(@JsonProperty("packageID") String name, @JsonProperty("age") int age, @JsonProperty("countryOfRecidence") String countryOfResidence) {
            this.name = name;
            this.age = age;
            this.countryOfResidence = countryOfResidence;
        }
    }
}
