package laba4;

import com.example.UserRegistry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JsTestsStorage extends AbstractBehavior<JsTestsStorage.Command>  {

    // actor protocol
    interface Command {}

    public final static class GetUsers implements UserRegistry.Command {
        public final ActorRef<UserRegistry.Users> replyTo;
        public GetUsers(ActorRef<UserRegistry.Users> replyTo) {
            this.replyTo = replyTo;
        }
    }

    public final static class CreateUser implements UserRegistry.Command {
        public final UserRegistry.User user;
        public final ActorRef<UserRegistry.ActionPerformed> replyTo;
        public CreateUser(UserRegistry.User user, ActorRef<UserRegistry.ActionPerformed> replyTo) {
            this.user = user;
            this.replyTo = replyTo;
        }
    }

    public final static class GetUserResponse {
        public final Optional<UserRegistry.User> maybeUser;
        public GetUserResponse(Optional<UserRegistry.User> maybeUser) {
            this.maybeUser = maybeUser;
        }
    }

    public final static class GetUser implements UserRegistry.Command {
        public final String name;
        public final ActorRef<UserRegistry.GetUserResponse> replyTo;
        public GetUser(String name, ActorRef<UserRegistry.GetUserResponse> replyTo) {
            this.name = name;
            this.replyTo = replyTo;
        }
    }


    public final static class DeleteUser implements UserRegistry.Command {
        public final String name;
        public final ActorRef<UserRegistry.ActionPerformed> replyTo;
        public DeleteUser(String name, ActorRef<UserRegistry.ActionPerformed> replyTo) {
            this.name = name;
            this.replyTo = replyTo;
        }
    }


    public final static class ActionPerformed implements UserRegistry.Command {
        public final String description;
        public ActionPerformed(String description) {
            this.description = description;
        }
    }

    public final static class Test {
        public final String packageId;
        public final String jsScript;
        public final String functionName;
        public final String tests;
        @JsonCreator
        public Test(@JsonProperty("packageId") String packageId, @JsonProperty("jsScript") String jsScript, @JsonProperty("functionName") String functionName, @JsonProperty("tests") String tests) {
            this.packageId = packageId;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.tests = tests;
        }
    }

    public final static class Users{
        public final List<UserRegistry.User> users;
        public Users(List<UserRegistry.User> users) {
            this.users = users;
        }
    }
    //#user-case-classes

    private final List<UserRegistry.User> users = new ArrayList<>();

    private UserRegistry(ActorContext<UserRegistry.Command> context) {
        super(context);
    }

    public static Behavior<UserRegistry.Command> create() {
        return Behaviors.setup(UserRegistry::new);
    }

    @Override
    public Receive<UserRegistry.Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(UserRegistry.GetUsers.class, this::onGetUsers)
                .onMessage(UserRegistry.CreateUser.class, this::onCreateUser)
                .onMessage(UserRegistry.GetUser.class, this::onGetUser)
                .onMessage(UserRegistry.DeleteUser.class, this::onDeleteUser)
                .build();
    }

    private Behavior<UserRegistry.Command> onGetUsers(UserRegistry.GetUsers command) {
        // We must be careful not to send out users since it is mutable
        // so for this response we need to make a defensive copy
        command.replyTo.tell(new UserRegistry.Users(Collections.unmodifiableList(new ArrayList<>(users))));
        return this;
    }

    private Behavior<UserRegistry.Command> onCreateUser(UserRegistry.CreateUser command) {
        users.add(command.user);
        command.replyTo.tell(new UserRegistry.ActionPerformed(String.format("User %s created.", command.user.name)));
        return this;
    }

    private Behavior<UserRegistry.Command> onGetUser(UserRegistry.GetUser command) {
        Optional<UserRegistry.User> maybeUser = users.stream()
                .filter(user -> user.name.equals(command.name))
                .findFirst();
        command.replyTo.tell(new UserRegistry.GetUserResponse(maybeUser));
        return this;
    }

    private Behavior<UserRegistry.Command> onDeleteUser(UserRegistry.DeleteUser command) {
        users.removeIf(user -> user.name.equals(command.name));
        command.replyTo.tell(new UserRegistry.ActionPerformed(String.format("User %s deleted.", command.name)));
        return this;
    }

}
