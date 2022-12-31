package si.fri.rso.project.user.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import si.fri.rso.project.user.lib.User;
import si.fri.rso.project.user.services.beans.UserBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class UserMutations {

    @Inject
    private UserBean userBean;

    @GraphQLMutation
    public User createUser(@GraphQLArgument(name = "user") User user) {
        userBean.createUser(user);
        return user;
    }

    @GraphQLMutation
    public DeleteResponse deleteUser(@GraphQLArgument(name = "id") Integer id) {
        return new DeleteResponse(userBean.deleteUser(id));
    }

}